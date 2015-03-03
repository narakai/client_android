package com.yobook.asynchttp;


public class URLBuilder {
    private static final String BASE_URL = "http://api.ydspeed.com:9527";


    
    public static String getUrl(int urlType) {
        String url = BASE_URL;
        switch (urlType) {
            case URL.XXXXXXX:
                url += "XXXX";
                break;
            case URL.HEALTHCHECK:
                url += "/healthcheck";
                break;
            default:
                // do nothing...
                break;
                
        }
        
        return url;
    }
    
    public static class URL {
        public static final int XXXXXXX = 0;
        public static final int HEALTHCHECK = 1;


    }
}