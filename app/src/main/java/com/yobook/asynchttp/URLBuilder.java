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
            case URL.HEALTH_CHECK:
                url += "/healthcheck";
                break;
            default:
                // do nothing...
                break;
                
        }
        
        return url;
    }
    
    public static class URL {
        public static final int XXXXXXX      = 0; // 例子
        public static final int HEALTH_CHECK = 1; // XXXX我也不知道这个接口是啥


    }
}