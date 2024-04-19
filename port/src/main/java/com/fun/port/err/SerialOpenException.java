package com.fun.port.err;

public class SerialOpenException extends RuntimeException {

    public SerialOpenException(String message) {
        super(message);
    }

    public SerialOpenException(String message, Throwable cause) {
        super(message, cause);
    }

    public SerialOpenException(Throwable cause) {
        super(cause);
    }

    public SerialOpenException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
