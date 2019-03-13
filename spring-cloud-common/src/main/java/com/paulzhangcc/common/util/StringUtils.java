package com.paulzhangcc.common.util;

/**
 * @author paul
 * @description
 * @date 2019/3/13
 */
public class StringUtils {
    public static boolean isEmpty(String value) {
        return value == null || value.trim().length() == 0;
    }
}
