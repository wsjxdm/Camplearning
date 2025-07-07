package com.example.qgassessment.Weather;

import android.app.Application;

import org.xutils.x;

public class UniteApp extends Application {

    @Override
    public void onCreate(){
        super.onCreate();
        x.Ext.init(this); //框架
    }
}
