package com.fun.serial.serialport.cmd;


import com.fun.port.FormatUtil;
import com.fun.port.Request;

import java.math.BigDecimal;

public abstract class TempRequest extends Request {
    private static final String CMD = FormatUtil.getCrcStr("060404000002");

    private static final byte[] commands = FormatUtil.hexStringToBytes(CMD);

    @Override
    public byte[] getPacket() {
        return commands;
    }

    @Override
    public void onSuccess(byte[] response) {
        String res = FormatUtil.bytesToHexString(response);
        float[] parseTemp = parseTemp(res);
        if (parseTemp != null) {
            float temp_in = parseTemp[0];
            float temp_out = parseTemp[1];
            float tTemp = temp_in;
            if (temp_in > temp_out) {
                temp_in = temp_out;
                temp_out = tTemp;
            }
            onResult("温度：in:" + temp_in + "℃, out:" + temp_in + "℃");
        }
    }

    @Override
    public void onError(Exception e) {
        onResult("温度：执行失败:" + e.getMessage());
    }

    protected abstract void onResult(String value);

    private float[] parseTemp(String result) {
        if (result != null) {
            float temp1 = new BigDecimal(Integer.parseInt(result.substring(6, 10), 16)).divide(new BigDecimal(10), 1, BigDecimal.ROUND_HALF_UP).floatValue();
            float temp2 = new BigDecimal(Integer.parseInt(result.substring(10, 14), 16)).divide(new BigDecimal(10), 1, BigDecimal.ROUND_HALF_UP).floatValue();
            return new float[]{temp1, temp2};
        } else {
            return null;
        }
    }
}
