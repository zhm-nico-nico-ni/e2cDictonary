package com.zhm.dictionary.friendly.Util;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

public class Daemon {

    public static final String TAG = "DaemonHandler";

    private static HandlerThread sHT;
    private static Handler sHandler;
    //查询数据库，耗时较少，优先级较高的数据库查询任务
    private static HandlerThread sDBHT;
    private static Handler sDBHandler;
    //其他非必要后台任务的执行线程
    private static HandlerThread sOtherHT;
    private static Handler sOtherHandler;

    public synchronized static Handler handler() {
        if (sHT == null) {
            sHT = new HandlerThread("handler-daemon");
            sHT.start();
        }

        if (sHandler == null) {
            sHandler = new CustomHandler(sHT.getLooper(), "handler");
        }

        return sHandler;
    }

    public synchronized static Handler dbHandler() {
        if (sDBHT == null) {
            sDBHT = new HandlerThread("Handler-db");
            sDBHT.start();
        }

        if (sDBHandler == null) {
            sDBHandler = new CustomHandler(sDBHT.getLooper(), "dbHandler");
        }

        return sDBHandler;
    }

    public synchronized static Handler otherHandler() {
        if (sOtherHT == null) {
            sOtherHT = new HandlerThread("yycall-other");
            sOtherHT.start();
        }

        if (sOtherHandler == null) {
            sOtherHandler = new CustomHandler(sOtherHT.getLooper(), "otherHandler");
        }

        return sOtherHandler;
    }


    private static final class CustomHandler extends Handler {

        private static final String DATA_POST_TIME = "post_time";

        private String flag;

        private CustomHandler(Callback callback) {
            super(callback);
        }

        private CustomHandler(Looper looper, String flag) {
            this(looper);
            this.flag = flag;
        }

        private CustomHandler(Looper looper) {
            super(looper);
        }

        private CustomHandler(Looper looper, Callback callback) {
            super(looper, callback);
        }

        @Override
        public void dispatchMessage(Message msg) {
            long start = 0;
            long threadStart = 0;
            long waitTime = 0;
            super.dispatchMessage(msg);
        }

        @Override
        public boolean sendMessageAtTime(Message msg, long uptimeMillis) {
            return super.sendMessageAtTime(msg, uptimeMillis);
        }
    }
}
