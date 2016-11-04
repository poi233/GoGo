package com.example.cc.gogo.app;

import android.app.Application;

import com.lovearthstudio.duasdk.Dua;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Dua.init(this);
    }
}
