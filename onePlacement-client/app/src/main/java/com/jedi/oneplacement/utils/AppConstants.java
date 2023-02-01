package com.jedi.oneplacement.utils;

public class AppConstants {
    // todo: use it ?
    public static String JSON_DATA = "JsonUser";
    public static String BASE_URL = "http://192.168.243.117:8080";
    public static String DEFAULT_ROLE = "Internship";
    public static String APP_NAME = "ONE_PLACEMENT";
    public static String JWT = "JWT";
    public static final String AUTH = "Authorization";

    private static final int maxMemSize = (int) Runtime.getRuntime().maxMemory() / 1024;
    public static final int imgCacheSize = maxMemSize/10;
}