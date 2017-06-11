package com.test.blur.utils;

/**
 * Created by zhaolin on 17-6-11.
 */

public class SafeUtils {

    public static <T> T checkNotNull(T t) {
        if (t == null) {
            new RuntimeException("");
        }
        return t;
    }
}
