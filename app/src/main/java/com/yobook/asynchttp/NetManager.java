package com.yobook.asynchttp;

import com.yobook.asynchttp.URLBuilder.URL;

/**
 *  网络协议这块，请参考
 *  https://github.com/yobook/api
 */
public class NetManager {

    private static AsyncHttpClient mClient = new AsyncHttpClient();
    
    public static void getXXXXXX(AsyncHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();

        mClient.get(URLBuilder.getUrl(URL.XXXXXXX), params, responseHandler);
    }


    public static void getHealthCHeck(AsyncHttpResponseHandler responseHandler) {
        //RequestParams params = new RequestParams();
        mClient.get(URLBuilder.getUrl(URL.HEALTHCHECK), null, responseHandler);
    }


}