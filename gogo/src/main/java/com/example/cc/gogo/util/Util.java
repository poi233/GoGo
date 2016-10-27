package com.example.cc.gogo.util;

public class Util {

    /**
     * 根据系统类型获取换行
     *
     * @return
     */
    public static String getChangeRow() {
        return System.getProperty("line.separator", "/n");
    }
}
