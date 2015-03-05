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

    public static void getBookInfoByNameOrISBN(String bookName, String sn, AsyncHttpResponseHandler responseHandler) {
        if( null == bookName && null == sn) {
            throw new IllegalArgumentException("BookName and ISBN could not be both null.");
        }
        RequestParams params = new RequestParams();
        if(null != bookName) {
            params.put("name", bookName);
        }
        if(null != sn) {
            params.put("sn", sn);
        }
        mClient.get(URLBuilder.getUrl(URL.QUERY_BOOK_BY_NAME),
                params, responseHandler);
    }

    public static void getBookInfoById(String id, AsyncHttpResponseHandler responseHandler) {
        mClient.get(URLBuilder.getUrl(URL.QUERY_BOOKS_BY_ID) + "/" + id, null, responseHandler);
    }


}