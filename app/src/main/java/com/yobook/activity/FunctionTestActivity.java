package com.yobook.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.yobook.R;
import com.yobook.asynchttp.AsyncHttpResponseHandler;
import com.yobook.asynchttp.NetManager;
import com.yobook.model.BookInfo;
import com.yobook.model.UserInfo;
import com.yobook.util.YLog;
import android.widget.Toast;

import org.apache.http.client.HttpResponseException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;


public class FunctionTestActivity extends ActionBarActivity {
    private static final String TAG = FunctionTestActivity.class.getName();

    private static final String QQ_APP_KEY = "1104321992";
    //用于显示当前的输出结果。
    TextView mOutput = null;

    private Tencent mTencent;
    private UserInfo mUserInfo = new UserInfo();
    private com.tencent.connect.UserInfo tencentInfo;

    private LocationClient mLocationClient = null;

    private BDLocationListener mLocationListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            //Receive Location
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(bdLocation.getTime());
            sb.append("\nerror code : ");
            sb.append(bdLocation.getLocType());
            sb.append("\nlatitude : ");
            sb.append(bdLocation.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(bdLocation.getLongitude());
            sb.append("\nradius : ");
            sb.append(bdLocation.getRadius());
            if (bdLocation.getLocType() == BDLocation.TypeGpsLocation){
                sb.append("\nspeed : ");
                sb.append(bdLocation.getSpeed());
                sb.append("\nsatellite : ");
                sb.append(bdLocation.getSatelliteNumber());
                sb.append("\ndirection : ");
                sb.append("\naddr : ");
                sb.append(bdLocation.getAddrStr());
                sb.append(bdLocation.getDirection());
            } else if (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation){
                sb.append("\naddr : ");
                sb.append(bdLocation.getAddrStr());
                sb.append("\noperationers : ");
                sb.append(bdLocation.getOperators());
            }
            YLog.i(TAG, sb.toString());
            int ret = bdLocation.getLocType();
            if(ret == 61 || ret == 65 || ret == 161) {
                //61 ： GPS定位结果
                //65 ： 定位缓存的结果。
                //161： 表示网络定位结果
                if(null != mUserInfo) {
                    mUserInfo.updateCurrentPosition(bdLocation.getLatitude()
                            , bdLocation.getLongitude());
                }

            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity_function);

        initViews();
        mTencent = Tencent.createInstance(QQ_APP_KEY, FunctionTestActivity.this);

        // Baidu Map start
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(mLocationListener);
        LocationClientOption option = new LocationClientOption();
        //LocationMode.Hight_Accuracy   高精度
        //LocationMode.Battery_Saving   低功耗
        //LocationMode.Device_Sensors   仅设备
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //gcj02(国测局加密经纬度坐标)
        //bd09ll(百度加密经纬度坐标)
        //bd09(百度加密墨卡托坐标)
        option.setCoorType("gcj02");
        //定位时间间隔(ms)
        option.setScanSpan(10000);

        mLocationClient.setLocOption(option);
        //int code = mLocationClient.requestLocation();
        //0：正常发起了定位。
        //1：服务没有启动。
        //2：没有监听函数。
        //6：请求间隔过短。 前后两次请求定位时间间隔不能小于1000ms。
        //YLog.i(TAG, "start to locate position:" + code);
        mLocationClient.start();
        //Baidu Map end
    }

    private void initViews() {
        //如果要使用toolBar,必须继承ActionBarActivity
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // App Logo
        toolbar.setLogo(R.drawable.ic_launcher);
        // Title
        toolbar.setTitle("My Title");
        // Sub Title
        toolbar.setSubtitle("Sub title");
        FunctionTestActivity.this.setSupportActionBar(toolbar);

        View testLoginView = findViewById(R.id.testLogin);
        View testLogoutView = findViewById(R.id.testLogout);
        View testServerTime = findViewById(R.id.testServerTime);
        View testReadBookInfo = findViewById(R.id.testReadBookInfo);
        View testUploadBookInfo = findViewById(R.id.testUploadBookInfo);
        View testShareInfo = findViewById(R.id.testShareInfo);
        View testQueryAround = findViewById(R.id.testAroundUser);

        testLoginView.setOnClickListener(mClickListener);
        testLogoutView.setOnClickListener(mClickListener);
        testServerTime.setOnClickListener(mClickListener);
        testReadBookInfo.setOnClickListener(mClickListener);
        testUploadBookInfo.setOnClickListener(mClickListener);
        testShareInfo.setOnClickListener(mClickListener);
        testQueryAround.setOnClickListener(mClickListener);


        mOutput = (TextView)findViewById(R.id.output);

    }

    public View.OnClickListener mClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.testLogin:
                    userQQLogin();
                    break;

                case R.id.testShareInfo:
                    shareThings();
                    break;

                case R.id.testLogout:
                    userLogout();
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
                    fetchBookInfo();
                    break;

                case R.id.testUploadBookInfo:
                    registerBookInfo();
                    break;
                case R.id.testAroundUser:
                    queryAroundUser();
                    break;
            }
        }
    };

    /**
     *  用户登录模块，当用户成功登录以后，尝试成功获取用户信息以后，提交系统注册
     */
    private void userQQLogin() {
        if(null != mTencent && !mTencent.isSessionValid()) {
            updateCurrentOutput("TestLogin", false);
            mTencent.login(FunctionTestActivity.this, "all", new IUiListener() {
                @Override
                public void onComplete(Object o) {
                    updateCurrentOutput("login success:" + o.toString(), true);
                    YLog.i(TAG, o.toString());
                    //当我们登录成功后，立马去请求当前用户信息。
                    try {
                        JSONObject obj = new JSONObject(o.toString());
                        int ret = obj.getInt("ret");
                        // 0 is right value.
                        if(ret != 0) {
                            updateCurrentOutput("login fail:" + o.toString(), true);
                            return ;
                        }
                        String accessToken = obj.getString("access_token");
                        String platformId = obj.getString("openid");
                        mUserInfo.initUserInfo(UserInfo.LOGIN_TYPE_QQ, platformId, accessToken);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return ;
                    }
                    YLog.i(TAG," mUserInfo:" + mUserInfo.toJSONString());
                    //暂时可能不需要用户的基本信息，但还是选择先获取然后再向YOBOOK服务器请求登录。
                    tencentInfo = new com.tencent.connect.UserInfo(
                            FunctionTestActivity.this, mTencent.getQQToken());
                    tencentInfo.getUserInfo(new IUiListener() {
                        @Override
                        public void onComplete(Object o) {
                            try {
                                JSONObject obj = new JSONObject(o.toString());
                                int ret = obj.getInt("ret");
                                // ret == 0 is the right value.
                                if(ret != 0) {
                                    updateCurrentOutput("getUserInfo failed" + o.toString(), true);
                                    return ;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            updateCurrentOutput("getUserInfo" + o.toString(), true);
                            YLog.i(TAG,"getUserInfo:" + o.toString());
                            loginOnYoBook();
                        }

                        @Override
                        public void onError(UiError uiError) {
                            updateCurrentOutput("getUserInfo failed", true);
                        }

                        @Override
                        public void onCancel() {
                            updateCurrentOutput("getUserInfo failed", true);
                        }
                    });

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
    }

    /**
     * 用户登出系统。
     */
    private void userLogout() {
        if (null != mTencent && mTencent.isSessionValid()) {
            mTencent.logout(FunctionTestActivity.this);
        }
    }

    /**
     * 用户在服务器上进行登录
     * PS. 请将该线程移至可控的线程组进行控制。
     */
    private void loginOnYoBook() {
        if (null == mTencent || null == mUserInfo) {
            //add check for mTencent and mUserInfo.
            YLog.i(TAG, "Neither mTencent or mUserInfo could be null.");
            throw new IllegalStateException("Neither mTencent or mUserInfo could be null.");
        }
        new Thread(new Runnable(){
            @Override
            public void run() {

                try {
                    NetManager.postYoBookLogin(mUserInfo, new AsyncHttpResponseHandler(){
                        @Override
                        public void onSuccess(String response) {
                            // Successfully got a response
                            updateCurrentOutput("Success in query UserInfo:" + response, true);
                        }

                        @Override
                        public void onFailure(Throwable e, String response) {
                            // Response failed :(
                            int status = ((HttpResponseException)e).getStatusCode();
                            YLog.i(TAG, "status:" + status);
                            //400 means 请求错误 403 提供的access_token和open_id在from下无法成功通过验证
                            updateCurrentOutput("Failure in query UserInfo:" + response + "\tstatus:"
                                    + status, true);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 查询周围水友
     */
    private void queryAroundUser() {
        int radius = 500;//半径
        NetManager.queryAroundUser(mUserInfo.getLongitude(), mUserInfo.getLatitude(), radius
                , new AsyncHttpResponseHandler(){
            @Override
            public void onSuccess(String response) {
                // Successfully got a response
                updateCurrentOutput("Success in query AroundUser:" + response, true);
            }

            @Override
            public void onFailure(Throwable e, String response) {
                // Response failed :(
                int status = ((HttpResponseException)e).getStatusCode();
                YLog.i(TAG, "status:" + status);
                //200 找到半径内的用户 400 参数错误  404 一个都没找到
                updateCurrentOutput("Failure in query AroundUser:" + response + "\tstatus:"
                        + status, true);
            }
        } );
    }


    /**
     * 分享信息
     */
    private void shareThings() {
        if(null != mTencent && mTencent.isSessionValid()) {
            updateCurrentOutput("share start", false);
            Bundle bundle = new Bundle();
            bundle.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE,
                    QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
            bundle.putString(QzoneShare.SHARE_TO_QQ_TITLE, "Test title");
            bundle.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, "Test Summary");
            bundle.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL,"http://www.baidu.com");
            ArrayList<String> imgList = new ArrayList<String>();
            imgList.add("http://img3.douban.com/lpic/s3635685.jpg");
            bundle.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL,
                    imgList);

            //这里出现几个问题，随后继续研究。
            //1.可能出现分享的LISTENER提前返回。
            //2.可以正常分享，但离开QQ界面以后跳出选择器，选择可分享的界面，离开以后，无LISTENER返回。
            mTencent.shareToQzone(FunctionTestActivity.this, bundle, new IUiListener() {
                @Override
                public void onComplete(Object o) {
                    updateCurrentOutput("share complete" + o.toString(), true);
                }

                @Override
                public void onError(UiError uiError) {
                    updateCurrentOutput("share error:" + uiError.errorDetail +
                            "\t\nmsg:" + uiError.errorMessage
                            +"\t\ncode:" + uiError.errorCode, true);
                }

                @Override
                public void onCancel() {
                    updateCurrentOutput("share cancel111", true);
                }
            });
        }
    }
    /**
     * 提交书籍的注册信息。
     */
    private void registerBookInfo() {
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
    }

    /**
     * 获取服务器上书本信息。
     */
    private void fetchBookInfo() {
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
                int status = ((HttpResponseException)e).getStatusCode();

                updateCurrentOutput("Failure in query BookId:" + response, true);
            }
        });
    }

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
    protected void onDestroy() {
        mLocationClient.stop();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();

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
        /*
        if (id == R.id.action_settings) {
            return true;
        }
        */

        String result = null;
        switch(id) {
            case R.id.action_edit:
                result = "Click edit";
                break;
            case R.id.action_share:
                result = "Click share";
                break;
            case R.id.action_settings:
                result = "Click settings";
                break;
        }
        if( null != result) {
            Toast.makeText(FunctionTestActivity.this, result, Toast.LENGTH_LONG).show();
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
