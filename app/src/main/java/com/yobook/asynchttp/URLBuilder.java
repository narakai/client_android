package com.yobook.asynchttp;


public class URLBuilder {
    /** 服务器基本地址 */
    private static final String BASE_URL = "http://api.ydspeed.com:9527";

    /**
     * XXXX注释
     * @param urlType url类型
     * @return 根据url类型返回对应的服务器地址
     */
    public static String getUrl(int urlType) {
        String url = BASE_URL;
        switch (urlType) {
            case URL.XXXXXXX:
                url += "XXXX";
                break;
            case URL.FETCH_SERVER_TIME:
                url += "/healthcheck";
                break;
            case URL.QUERY_BOOKS_BY_ID:
            case URL.QUERY_BOOK_BY_NAME:
                url += "/books";
                break;
            case URL.CREATE_BOOK_INFO:
                url += "/books";
                break;
            default:
                // do nothing...
                break;
                
        }
        
        return url;
    }
    
    public static class URL {
        public static final int XXXXXXX      = 0; // 例子
        public static final int FETCH_SERVER_TIME = 1; // 获取服务器时间。
        public static final int QUERY_BOOKS_BY_ID = 2; // 根据ID获取书籍信息
        public static final int QUERY_BOOK_BY_NAME = 3;// 根据名字获取书籍信息

        public static final int CREATE_BOOK_INFO  = 9;//创建图书信息

    }
}