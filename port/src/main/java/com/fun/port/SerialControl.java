package com.fun.port;

import java.util.concurrent.LinkedBlockingDeque;

public class SerialControl {
    private volatile LinkedBlockingDeque<Request> mDequeue;

    private volatile boolean mIsInit;

    private static final SerialControl INSTANCE = new SerialControl();

    public static SerialControl get() {
        return INSTANCE;
    }

    public void init(SerialDevice device) {
        if (device == null) {
            throw new NullPointerException("device is null!");
        }

        if (!device.isConnected()) {
            throw new NullPointerException("device not connect!");
        }

        mIsInit = true;
        SerialStack stack = new SerialStack(device);
        mDequeue = new LinkedBlockingDeque<>();
        SerialDispatcher dispatcher = new SerialDispatcher(mDequeue, stack);
        dispatcher.start();
    }

    public void request(Request request) {
        if (!mIsInit) {
            throw new RuntimeException("must be call init!");
        }
        mDequeue.add(request);
    }

}
