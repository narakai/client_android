package com.yobook.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.yobook.R;

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

        testLoginView.setOnClickListener(mClickListener);
        testLogoutView.setOnClickListener(mClickListener);


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
            StringBuilder sb = new StringBuilder(100);
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
