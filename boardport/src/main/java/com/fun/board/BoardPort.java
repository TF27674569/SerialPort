package com.fun.board;

import android.content.Context;
import android.util.Log;

import com.fun.port.SerialDevice;
import com.fun.port.err.SerialException;
import com.fun.port.err.SerialOpenException;

import java.io.File;


public class BoardPort extends SerialDevice {
    private final SerialPort mPort;
    private int mFd;
    private boolean mIsConnect;


    public BoardPort(Context context) {
        super(context);
        mPort = new SerialPort();
    }

    @Override
    public boolean open(String name,int baud_rate) throws SerialOpenException {
        try {
            File device = new File(name);
            if (!device.canRead() || !device.canWrite()) {
                Process su;
                su = Runtime.getRuntime().exec("/system/xbin/su");
                String cmd = "chmod 777 " + device.getAbsolutePath() + "\n"
                        + "exit\n";
                su.getOutputStream().write(cmd.getBytes());
                if ((su.waitFor() != 0) || !device.canRead()
                        || !device.canWrite()) {
                    throw new SerialOpenException("文件没有打开的权限");
                }
            }
            mFd = mPort.open(name, baud_rate);
            Log.d("@TF@", "open: " + mFd);
            return mIsConnect = mFd >= 0;
        } catch (Exception e) {
            throw new SerialOpenException(e);
        }
    }

    @Override
    public int write(byte[] data) throws SerialException {
        int len = mPort.write(mFd, data, data.length);
        mIsConnect = len > 0;
        return len;
    }

    @Override
    public byte[] read() throws SerialException {
        return mPort.read(mFd);
    }

    @Override
    public void close() throws SerialException {
        mPort.close(mFd);
    }

    @Override
    public boolean isConnected() {
        return mIsConnect;
    }
}
