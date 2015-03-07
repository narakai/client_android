package com.yobook.util;


import android.util.Log;

public class YLog {

    private static final int LEVEL_DEBUG = 0;

    private static final int LEVEL_DEVELOPER = 1;

    private static final int LEVEL_TEST = 2;

    private static final int LEVEL_ALPHA = 3;

    private static final int LEVEL_BETA = 4;

    private static final int LEVEL_GRAY = 5;

    private static final int LEVEL_OFFICIAL = 6;

    private static final int LEVEL_OFFICIAL_COLOR = 7;

    private static int slevel = LEVEL_DEBUG;

    public static void i(String tag, String msg) {
        Log.i(tag, msg);
    }

    public static void d(String tag, String msg) {
        Log.d(tag, msg);
    }

    public static void v(String tag, String msg) {
        Log.v(tag, msg);
    }

    public static void w(String tag, String msg) {
        Log.w(tag, msg);
    }

    public static void e(String tag, String msg) {
        Log.e(tag, msg);
    }



    /*
     * Getting it from 4.4 android source code.
     * luni/src/main/java/java/lang/Throwable.java
     */
    public static String getStackTrace(String indent, Throwable ex) {
        StringBuilder sb = new StringBuilder();
        sb.append(ex.toString());
        sb.append("\n");

        StackTraceElement[] stack = ex.getStackTrace();
        for(int i = 0 ; i < stack.length; i++) {
            sb.append(indent);
            sb.append("\tat ");
            sb.append(stack[i].toString());
            sb.append("\n");
        }
        Throwable cause = ex.getCause();
        if (cause != null) {
            sb.append(indent);
            sb.append("Cause by: " + cause.toString() + "\n");
            StackTraceElement[] stacks = cause.getStackTrace();
            for(int i = 0 ; i < stacks.length; i++) {
                sb.append(indent);
                sb.append("\tat ");
                sb.append(stacks[i].toString());
                sb.append("\n");
            }
        }
        return sb.toString();
    }


}
