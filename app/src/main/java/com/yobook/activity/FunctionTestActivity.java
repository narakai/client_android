package com.yobook.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.yobook.R;
import com.yobook.asynchttp.AsyncHttpResponseHandler;
import com.yobook.asynchttp.NetManager;
import com.yobook.model.BookInfo;
import com.yobook.util.YLog;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;


public class FunctionTestActivity extends BaseActivity {
    private static final String TAG = FunctionTestActivity.class.getName();
    //用于显示当前的输出结果。
    TextView mOutput = null;

    private Tencent mTencent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity_function);
        initViews();
        mTencent = Tencent.createInstance("1104321992", FunctionTestActivity.this);
    }

    private void initViews() {
        View testLoginView = findViewById(R.id.testLogin);
        View testLogoutView = findViewById(R.id.testLogout);
        View testServerTime = findViewById(R.id.testServerTime);
        View testReadBookInfo = findViewById(R.id.testReadBookInfo);
        View testUploadBookInfo = findViewById(R.id.testUploadBookInfo);


        testLoginView.setOnClickListener(mClickListener);
        testLogoutView.setOnClickListener(mClickListener);
        testServerTime.setOnClickListener(mClickListener);
        testReadBookInfo.setOnClickListener(mClickListener);
        testUploadBookInfo.setOnClickListener(mClickListener);


        mOutput = (TextView)findViewById(R.id.output);

    }

    public View.OnClickListener mClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.testLogin:
                    if(null != mTencent && !mTencent.isSessionValid()) {
                        updateCurrentOutput("TestLogin", false);
                        mTencent.login(FunctionTestActivity.this, "all", new IUiListener() {
                            @Override
                            public void onComplete(Object o) {
                                updateCurrentOutput("login success:" + o.toString(), true);
                            }

                            @Override
                            public void onError(UiError uiError) {
                                updateCurrentOutput("login failed", true);
                            }

                            @Override
                            public void onCancel() {
                                updateCurrentOutput("login cancel", true);
                            }
                        });
                    }
                    break;

                case R.id.testLogout:

                    if(null != mTencent && mTencent.isSessionValid()) {
                        mTencent.logout(FunctionTestActivity.this);
                    }
                    break;

                case R.id.testServerTime:
                    updateCurrentOutput("start to fetch data", false);
                    NetManager.getServerTime(new AsyncHttpResponseHandler() {

                        @Override
                        public void onSuccess(String response) {
                            // Successfully got a response
                            updateCurrentOutput(response, true);
                        }

                        @Override
                        public void onFailure(Throwable e, String response) {
                            // Response failed :(
                            updateCurrentOutput(response, true);
                        }
                    });
                    break;
                case R.id.testReadBookInfo:

                    updateCurrentOutput("start to fetch book info", false);
                    NetManager.getBookInfoByNameOrISBN(null, "034930490", new AsyncHttpResponseHandler() {

                        @Override
                        public void onSuccess(String response) {
                            // Successfully got a response
                            updateCurrentOutput("Success in query BookName:" + response, true);
                        }

                        @Override
                        public void onFailure(Throwable e, String response) {
                            // Response failed :(
                            updateCurrentOutput("Failure in query BookName:" + response, true);
                        }
                    });



                    updateCurrentOutput("start to fetch book info", true);
                    NetManager.getBookInfoById("00313",new AsyncHttpResponseHandler(){

                        @Override
                        public void onSuccess(String response) {
                            // Successfully got a response
                            updateCurrentOutput("Success in query BookId:" + response, true);
                        }

                        @Override
                        public void onFailure(Throwable e, String response) {
                            // Response failed :(
                            updateCurrentOutput("Failure in query BookId:" + response, true);
                        }
                    });

                    break;
                case R.id.testUploadBookInfo:
                    updateCurrentOutput("start to upload book info", false);
                    BookInfo b = new BookInfo("000000000", "test_book", "summery");
                    try {
                        NetManager.postBookInfo(b, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(String content) {
                                updateCurrentOutput("Success in upload BookInfo:" + content, true);
                            }

                            @Override
                            public void onFailure(Throwable error, String content) {


                                updateCurrentOutput("failed in upload BookInfo:" + content, true);
                                updateCurrentOutput(YLog.getStackTrace(null, error),true);
                                YLog.e(TAG, YLog.getStackTrace(null, error));
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

    /*
    *
    * 将输出结果打印在屏幕上，根据自己需求，选择是否重置输出结果。
    *
    * */
    private void updateCurrentOutput(String message, boolean append) {
        if(null != mOutput) {
            StringBuilder sb = new StringBuilder(60);
            if(append) {
                sb.append(mOutput.getText());
            }


            sb.append(message);
            sb.append("\n");
            mOutput.setText(sb.toString());
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_function_test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mTencent.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
