package com.fun.port;

public abstract class Request extends BaseRequest {

    public Request() {
    }

    /**
     * 返回的结果，子类实现
     */
    public abstract void onSuccess(byte[] response);

    public abstract void onError(Exception e);
}
