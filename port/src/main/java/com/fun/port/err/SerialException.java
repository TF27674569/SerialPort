package com.fun.port.err;

public class SerialException extends Exception{
    private long mRS485TimeMs;

    public SerialException(String message) {
        super(message);
    }

    public SerialException(String message, Throwable e) {
        super(message, e);
    }

    public void setRS485TimeMs(long rs485TimeMs) {
        mRS485TimeMs = rs485TimeMs;
    }

    public SerialException(Throwable cause) {
        super(cause);
    }

    public long getmRS485TimeMs() {
        return mRS485TimeMs;
    }
}
