package com.yobook.asynchttp;

import android.text.TextUtils;

import com.yobook.asynchttp.URLBuilder.URL;
import com.yobook.model.BookInfo;
import com.yobook.model.UserInfo;
import com.yobook.util.YLog;

import org.json.JSONObject;

import java.io.IOException;

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
        if (null == bookName && null == sn) {
            throw new IllegalArgumentException("BookName and ISBN could not be both null.");
        }
        RequestParams params = new RequestParams();
        if (null != bookName) {
            params.put("name", bookName);
        }
        if (null != sn) {
            params.put("sn", sn);
        }
        mClient.get(URLBuilder.getUrl(URL.QUERY_BOOK_BY_NAME), params, responseHandler);
    }

    public static void getBookInfoByName(String bookName, AsyncHttpResponseHandler responseHandler) {
        if (null == bookName) {
            throw new IllegalArgumentException("BookName could not be null.");
        }
        getBookInfoByNameOrISBN(bookName, null, responseHandler);
    }

    public static void getBookInfoByISBN(String sn, AsyncHttpResponseHandler responseHandler) {
        if (null == sn) {
            throw new IllegalArgumentException("ISBN could not be null.");
        }
        getBookInfoByNameOrISBN(null, sn, responseHandler);
    }

    public static void getBookInfoById(String id, AsyncHttpResponseHandler responseHandler) {
        mClient.get(URLBuilder.getUrl(URL.QUERY_BOOKS_BY_ID) + "/" + id, null, responseHandler);
    }

    public static void postBookInfo(BookInfo book, AsyncHttpResponseHandler responseHandler)
            throws IOException {
        String content = book.toJSONString();
        if (null == content) {
            throw new NullPointerException("can't create "
                    + BookInfo.class.getSimpleName() + " json value");
        }
        SimpleEntity entity = new SimpleEntity(content);
        mClient.post(null,URLBuilder.getUrl(URL.CREATE_BOOK_INFO), entity, null, responseHandler);
    }

    public static void postYoBookLogin(UserInfo u, AsyncHttpResponseHandler responseHandler)
            throws IOException {
        String content = u.toJSONString();
        if (null == content) {
            throw new NullPointerException("can't create "
                    + UserInfo.class.getSimpleName() + " json value");
        }
        YLog.i("MICHAEL", "postContent:" + content);
        SimpleEntity entity = new SimpleEntity(content);
        mClient.post(null, URLBuilder.getUrl(URL.USER_LOGIN), entity, null, responseHandler);
    }

    public static void queryAroundUser(double longitude, double latitude, int radius
            , AsyncHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("longitude", longitude + "");
        params.put("latitude", latitude + "");
        params.put("radius", radius + "");
        mClient.get(URLBuilder.getUrl(URL.USER_AROUND), params,responseHandler);
    }
}