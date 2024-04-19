package com.fun.port;

abstract class BaseRequest {

    // 每次的超时时间
    private int outTime = 30;

    // 当前此时，第一次执行也算
    private int retryCount = 1;

    // 最大重试次数
    private int mMaxRetryCount = 3;

    public void setRetryCount(int count) {
        this.mMaxRetryCount = count;
    }

    public void setOutTime(int time) {
        this.outTime = time;
    }

    public int getTimeMs() {
        return outTime;
    }

    boolean retry() {
        if (retryCount >= mMaxRetryCount) {
            return false;
        }
        retryCount++;
        return true;
    }


    public abstract byte[] getPacket();

}
