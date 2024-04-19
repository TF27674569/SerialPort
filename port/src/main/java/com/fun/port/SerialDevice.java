package com.fun.port;

import android.content.Context;

import com.fun.port.err.SerialException;
import com.fun.port.err.SerialOpenException;

public abstract class SerialDevice {

    protected Context context;

    public SerialDevice(Context context) {
        this.context = context;
    }

    public abstract boolean open(String name,int baud_rate) throws SerialOpenException;

    public abstract int write(byte[] data) throws SerialException;

    public abstract byte[] read() throws SerialException;

    public abstract void close() throws SerialException;

    public abstract boolean isConnected();
}
