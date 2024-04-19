package com.fun.board;


class SerialPort {
    public native int open(String dev, int baud_rate);
    public native int write(int fd, byte[] buf, int buf_len);
    public native byte[] read(int fd);
    public native void close(int fd);


    static {
        System.loadLibrary("serial_port");
    }
}
