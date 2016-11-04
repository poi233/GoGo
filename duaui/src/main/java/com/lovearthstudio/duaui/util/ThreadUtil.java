package com.lovearthstudio.duaui.util;

import android.os.Handler;
import android.os.Looper;

public class ThreadUtil {
    public static Handler mainHandler=new Handler(Looper.getMainLooper());
    public static void runOnMainThread(Runnable runnable){
        mainHandler.post(runnable);
    }
}
