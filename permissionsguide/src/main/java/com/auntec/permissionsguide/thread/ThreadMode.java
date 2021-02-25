package com.auntec.permissionsguide.thread;

/**
 * 线程类型
 *
 * @author shrek
 */
public enum ThreadMode {
    PostThread("当前线程"), MainThread("主线程"), BackgroundThread("其他线程");

    String descriptInfo;

    private ThreadMode(String descriptInfo) {
        this.descriptInfo = descriptInfo;
    }
}
