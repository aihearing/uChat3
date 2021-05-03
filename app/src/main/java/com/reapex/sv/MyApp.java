package com.reapex.sv;

import android.app.Application;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MySP.getInstance().init(this);
    }
}