package com.robl2e.thistodo;

import android.app.Application;

import io.paperdb.Paper;

/**
 * Created by robl2e on 8/15/2017.
 */

public class ThisTodoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Paper.init(this);
    }
}
