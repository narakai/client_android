package com.yobook.activity;

import android.app.ProgressDialog;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.yobook.R;
import com.yobook.eventbus.event.ISBNCodeEvent;
import com.yobook.zxing.camera.CameraManager;
import com.yobook.zxing.decoding.CaptureActivityHandler;
import com.yobook.zxing.decoding.InactivityTimer;
import com.yobook.zxing.view.ViewfinderView;

import java.io.IOException;
import java.util.Vector;

import de.greenrobot.event.EventBus;

public class ScanISBNCodeActivity extends BaseScanActivity implements Callback {
    private static final float BEEP_VOLUME = 0.10f;
    private CaptureActivityHandler mHandler = null;
    private ViewfinderView mViewfinderView = null;
    private boolean mHasSurface = false;
    private Vector<BarcodeFormat> mDecodeFormats = null;
    private String mCharacterSet = null;
    private InactivityTimer mInactivityTimer = null;
    private MediaPlayer mMediaPlayer = null;
    private boolean mPlayBeep = false;
    private boolean mVibrate = false;
    private ProgressDialog mProgressDlg = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);
        mProgressDlg = new ProgressDialog(ScanISBNCodeActivity.this);
        CameraManager.init(getApplication());
        mViewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
        mInactivityTimer = new InactivityTimer(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (mHasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        mDecodeFormats = null;
        mCharacterSet = null;

        mPlayBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            mPlayBeep = false;
        }
        initBeepSound();
        mVibrate = true;

    }

    @Override
    public void onPause() {
        super.onPause();
        if (mHandler != null) {
            mHandler.quitSynchronously();
            mHandler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy() {
        mInactivityTimer.shutdown();
        super.onDestroy();
    }

    public void handleDecode(Result result, Bitmap barcode) {
        mInactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        final String resultString = result.getText();
        if (resultString.equals("")) {
            Toast.makeText(ScanISBNCodeActivity.this, "Scan failed!", Toast.LENGTH_SHORT).show();

            //ScanISBNCodeActivity.this.finish();
        } else {
            Toast.makeText(ScanISBNCodeActivity.this, "Scan successful! Code is :" + resultString, Toast.LENGTH_LONG).show();
            //ScanISBNCodeActivity.this.finish();
        }
        //michael add on 2015-03-01 for transfer ISBNCode to MainActivity.
        EventBus.getDefault().post(new ISBNCodeEvent(resultString));

        ScanISBNCodeActivity.this.finish();
        //michael add end
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (mHandler == null) {
            mHandler = new CaptureActivityHandler(this, mDecodeFormats, mCharacterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!mHasSurface) {
            mHasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mHasSurface = false;
    }

    public ViewfinderView getViewfinderView() {
        return mViewfinderView;
    }

    public Handler getHandler() {
        return mHandler;
    }

    public void drawViewfinder() {
        mViewfinderView.drawViewfinder();
    }

    private void initBeepSound() {
        if (mPlayBeep && mMediaPlayer == null) {

            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
            try {
                mMediaPlayer.setDataSource(file.getFileDescriptor(),
                        file.getStartOffset(), file.getLength());
                file.close();
                mMediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mMediaPlayer.prepare();
            } catch (IOException e) {
                mMediaPlayer = null;
            }
        }
    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (mPlayBeep && mMediaPlayer != null) {
            mMediaPlayer.start();
        }
        if (mVibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final OnCompletionListener beepListener = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

}