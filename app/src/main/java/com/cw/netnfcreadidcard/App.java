package com.cw.netnfcreadidcard;

import android.app.Application;

/**
 * 作者：李阳
 * 时间：2019/7/8
 * 描述：
 */
public class App extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        CrashHandler.getInstance().init(this);
    }
}
