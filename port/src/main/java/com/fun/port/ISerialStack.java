package com.fun.port;

import com.fun.port.err.SerialException;

public interface ISerialStack {
    byte[] performRequest(Request request) throws SerialException, InterruptedException;
}
