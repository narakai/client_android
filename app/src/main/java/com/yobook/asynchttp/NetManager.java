package com.yobook.asynchttp;

import com.yobook.asynchttp.URLBuilder.URL;

public class NetManager {

    private static AsyncHttpClient mClient = new AsyncHttpClient();
    
    public static void getXXXXXX(AsyncHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();

        mClient.get(URLBuilder.getUrl(URL.XXXXXXX), params, responseHandler);
    }
    


}