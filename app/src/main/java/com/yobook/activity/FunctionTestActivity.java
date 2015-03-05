package com.yobook.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.yobook.R;
import com.yobook.asynchttp.AsyncHttpResponseHandler;
import com.yobook.asynchttp.NetManager;



public class FunctionTestActivity extends BaseActivity {
    //用于显示当前的输出结果。
    TextView mOutput = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity_function);
        initViews();
    }

    private void initViews() {
        View testLoginView = findViewById(R.id.testLogin);
        View testLogoutView = findViewById(R.id.testLogout);
        View testServerTime = findViewById(R.id.testServerTime);
        View testReadBookInfo = findViewById(R.id.testReadBookInfo);

        testLoginView.setOnClickListener(mClickListener);
        testLogoutView.setOnClickListener(mClickListener);
        testServerTime.setOnClickListener(mClickListener);
        testReadBookInfo.setOnClickListener(mClickListener);


        mOutput = (TextView)findViewById(R.id.output);

    }

    public View.OnClickListener mClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.testLogin:
                    updateCurrentOutput("TestLogin", true);
                    break;

                case R.id.testLogout:
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
                    NetManager.getBookInfoByNameOrISBN(null, "034930490",new AsyncHttpResponseHandler(){

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
}
