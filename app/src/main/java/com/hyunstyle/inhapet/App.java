package com.hyunstyle.inhapet;

import android.app.Application;

import io.realm.Realm;

/**
 * Created by sh on 2018-04-06.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }
}
