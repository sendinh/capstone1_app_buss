package com.team03.dtuevent.helper;

public class HttpUtils {
    private static final String BASE_URL = "https://eventdtu.herokuapp.com";


    public static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
