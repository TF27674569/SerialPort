package com.fun.port;

import com.fun.port.err.SerialConnectException;
import com.fun.port.err.SerialException;
import com.fun.port.err.SerialTimeoutException;

public class SerialStack implements ISerialStack {

    private final SerialDevice mDevice;

    public SerialStack(SerialDevice device) {
        this.mDevice = device;
    }

    @Override
    public byte[] performRequest(Request request) throws SerialException, InterruptedException {
        if (!mDevice.isConnected()) {
            throw new SerialConnectException("Serial port no connection");
        }

        int len = mDevice.write(request.getPacket());
        if (len < 0) {
            throw new SerialException("write error, check diver online status ! ");
        }

        int timeoutMs = request.getTimeMs();
        long curMs = System.currentTimeMillis();
        long end = curMs;
        final int sleep_time = 6;
        while (true) {
            if ((end - curMs) >= timeoutMs) {
                throw new SerialTimeoutException("Serial port timeout");
            }
            byte[] pkt = mDevice.read();
            if (pkt == null || pkt.length == 0) {
                Thread.sleep(sleep_time);
                end = System.currentTimeMillis();
                continue;
            }
            return pkt;
        }
    }
}
