package com.yobook.activity;

import android.graphics.Bitmap;
import android.os.Handler;

import com.google.zxing.Result;
import com.yobook.zxing.view.ViewfinderView;

public abstract class BaseScanActivity extends BaseActivity {

    abstract public Handler getHandler();

    abstract public void drawViewfinder();

    abstract public ViewfinderView getViewfinderView();

    abstract public void handleDecode(Result result, Bitmap barcode);
}

