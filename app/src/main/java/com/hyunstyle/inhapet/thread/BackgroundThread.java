package com.hyunstyle.inhapet.thread;

import android.os.Handler;
import android.util.Log;


/**
 * Created by sh on 2018-03-08.
 */

public class BackgroundThread extends Thread {
    private Handler handler;
    private boolean isAlive;

    public BackgroundThread(Handler handler) {
        this.handler = handler;
        this.isAlive = true;
    }

    public void terminate() {
        synchronized (this) {
            this.isAlive = false;
        }
    }


    @Override
    public void run() {
        while(isAlive) {
            Log.d("thread", "running");
            handler.sendEmptyMessage(0);
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
