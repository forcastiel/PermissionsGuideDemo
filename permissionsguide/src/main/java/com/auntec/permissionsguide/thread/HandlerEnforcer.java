package com.auntec.permissionsguide.thread;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;


public class HandlerEnforcer implements ZThreadEnforcer {
    private static HandlerEnforcer enforcer;

    private Handler mHandler, backgroudHandler;
    private HandlerThread mHandlerThread;

    public static HandlerEnforcer newInstance() {
        if (enforcer == null) {
            enforcer = new HandlerEnforcer();
        }
        return enforcer;
    }

    public HandlerEnforcer() {
        super();

    }

    public Handler getmHandler() {
        if (mHandler == null) {
            mHandler = new Handler(Looper.getMainLooper());
        }
        return mHandler;
    }

    public Handler getBackgroudHandler() {
        if (mHandlerThread == null) {
        }
        mHandlerThread = new HandlerThread("kx-background");
        mHandlerThread.start();

        backgroudHandler = new Handler(mHandlerThread.getLooper());
        return backgroudHandler;
    }


    @Override
    public void enforceMainThread(Runnable run) {
        enforceMainThreadDelay(run, 0);
    }

    @Override
    public void enforceMainThreadDelay(Runnable run, long millisecond) {
        enforceDelay(ThreadMode.MainThread, run, millisecond);
    }

    @Override
    public void removeMainThread(Runnable run) {
        getmHandler().removeCallbacks(run);
    }

    @Override
    public void enforceBackgroud(Runnable run) {
        enforceBackgroudDelay(run, 0);
    }

    @Override
    public void enforceBackgroudDelay(Runnable run, long millisecond) {
        enforceDelay(ThreadMode.BackgroundThread, run, millisecond);
    }

    @Override
    public void removeBackgroud(Runnable run) {
        getBackgroudHandler().removeCallbacks(run);
    }

    @Override
    public void enforce(ThreadMode tMode, Runnable run) {
        enforceDelay(tMode, run, 0);
    }

    @Override
    public void enforceDelay(ThreadMode tMode, Runnable run, long millisecond) {
        if (run == null) {
            throw new IllegalArgumentException("Thread enforcer Runnable must not null");
        }

        if (millisecond < 0) {
            throw new IllegalArgumentException("Thread enforcer millisecond > 0");
        }
        switch (tMode) {
            case MainThread:
                getmHandler().postDelayed(run, millisecond);
                break;
            case BackgroundThread:
                getBackgroudHandler().postDelayed(run, millisecond);
                break;
            case PostThread:
            default:
                run.run();
                break;
        }
    }

    public int getIdentityID() {
        return 0;
    }

    public void recycle() {
        mHandler = null;

        if (mHandlerThread != null) {
//			mHandlerThread.quitSafely();
            mHandlerThread.interrupt();
        }

        mHandlerThread = null;
        backgroudHandler = null;
    }

}
