package com.essam.customloading;

import android.os.Handler;

/**
 * Created by essam on 28/01/17.
 */

public class BackgroundThread extends Thread {

    private Handler mHandler;
    private boolean mRunning;

    public BackgroundThread(Handler handler) {
        mHandler = handler;
    }

    public void setRunning(boolean running) {
        mRunning = running;
    }

    @Override
    public void run() {
        super.run();
        while (mRunning) {
            try {
                //this sleeps for 50 milliseconds
                sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                //send message to handler after sleep
                mHandler.sendEmptyMessage(0);
            }
        }
    }
}