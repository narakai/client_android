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


    public static void getServerTime(AsyncHttpResponseHandler responseHandler) {
        //RequestParams params = new RequestParams();
        mClient.get(URLBuilder.getUrl(URL.FETCH_SERVER_TIME), null, responseHandler);
    }

    public static void getBookInfoByName(String bookName, AsyncHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("name", bookName);

        mClient.get(URLBuilder.getUrl(URL.QUERY_BOOK_BY_NAME), params, responseHandler);
    }

    public static void getBookInfoById(String id, AsyncHttpResponseHandler responseHandler) {
        mClient.get(URLBuilder.getUrl(URL.QUERY_BOOKS_BY_ID) + "/" + id, null, responseHandler);
    }


}