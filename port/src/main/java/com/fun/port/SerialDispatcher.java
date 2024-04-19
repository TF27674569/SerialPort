package com.fun.port;

import android.os.Process;
import android.os.SystemClock;
import android.util.Log;

import com.fun.port.err.SerialException;
import com.fun.port.err.SerialTimeoutException;

import java.util.concurrent.BlockingQueue;

public class SerialDispatcher extends Thread {
    private static final String TAG = SerialDispatcher.class.getName();
    private final BlockingQueue<Request> mQueue;

    private final ISerialStack mBasicSerialStack;

    private volatile boolean mQuit = false;

    public SerialDispatcher(BlockingQueue<Request> queue, ISerialStack basicStack) {
        mQueue = queue;
        mBasicSerialStack = basicStack;
    }

    public void quit() {
        mQuit = true;
        interrupt();
    }

    @Override
    public void run() {
        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
        while (true) {
            try {
                processRequest();
            } catch (InterruptedException e) {
                if (mQuit) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
    }

    private void processRequest() throws InterruptedException {
        Request request = mQueue.take();
        processRequest(request);
    }

    private void processRequest(Request request) {
        long startTimeMs = SystemClock.elapsedRealtime();
        try {
            byte[] bytes = mBasicSerialStack.performRequest(request);
            request.onSuccess(bytes);
        } catch (SerialException e) {
            e.setRS485TimeMs(SystemClock.elapsedRealtime() - startTimeMs);
            // 只处理超时异常，并且在重试范围内
            if (e instanceof SerialTimeoutException && request.retry()) {
                Log.d(TAG, "time out retry : " + FormatUtil.bytesToHexString(request.getPacket()));
                processRequest(request);
            } else {
                request.onError(e);
            }
        } catch (InterruptedException e) {
            request.onError(e);
        }
    }


}
