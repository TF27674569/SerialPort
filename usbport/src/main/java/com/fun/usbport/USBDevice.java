package com.fun.usbport;

import android.content.Context;
import android.hardware.usb.UsbManager;

import com.fun.port.SerialDevice;
import com.fun.port.err.SerialException;
import com.fun.port.err.SerialOpenException;

import java.util.Arrays;

import cn.wch.ch34xuartdriver.CH34xUARTDriver;

public class USBDevice extends SerialDevice {

    private static final String ACTION_USB_PERMISSION = "cn.wch.wchusbdriver.USB_PERMISSION";
    private static final int RECEIVE_MAX_BUF_SIZE = 256;
    private final byte[] receiveBuffer = new byte[RECEIVE_MAX_BUF_SIZE];
    private CH34xUARTDriver mDevice;

    public USBDevice(Context context) {
        super(context);
    }

    @Override
    public boolean open(String name,int baud_rate) throws SerialOpenException {
        // close device
        if (mDevice != null) {
            mDevice.CloseDevice();
        }
        // create device
        mDevice = new CH34xUARTDriver(
                (UsbManager) context.getApplicationContext().getSystemService(Context.USB_SERVICE),
                context.getApplicationContext(), ACTION_USB_PERMISSION);

        // connect
        if (mDevice.isConnected()) {
            throw new SerialOpenException("CH34x is connected");
        }

        // support
        if (!mDevice.UsbFeatureSupported()) {
            throw new SerialOpenException("CH34x is not supported");
        }

        // resume
        int rectal = mDevice.ResumeUsbList();
        if (rectal == -1) {
            throw new SerialOpenException("CH34x resume usb list error");
        }

        if (rectal != 0) {
            throw new SerialOpenException("CH34x resume usb list error, rectal: " + rectal);
        }
        // init
        boolean uartInit = false;
        try {
            uartInit = mDevice.UartInit();
        } catch (Exception e) {
            throw new SerialOpenException("CH34x uart init error", e);
        }

        if (!uartInit) {
            throw new SerialOpenException("CH34x uart init false error");
        }

        // config
        final int BAUD_RATE = baud_rate;
        final byte DATA_BIT = 8;
        final byte STOP_BIT = 1;
        final byte PARITY = 0; // NONE
        final byte FLOW_CONTROL = 0; // NONE
        mDevice.SetConfig(BAUD_RATE, DATA_BIT, STOP_BIT, PARITY, FLOW_CONTROL);

        return true;
    }

    @Override
    public int write(byte[] data) throws SerialException {
        return mDevice.WriteData(data, data.length);
    }

    @Override
    public byte[] read() throws SerialException {
        Arrays.fill(receiveBuffer, (byte) 0);
        int length = mDevice.ReadData(receiveBuffer, RECEIVE_MAX_BUF_SIZE);
        if (length < 0) {
            return null;
        }
        return Arrays.copyOf(receiveBuffer, length);
    }

    @Override
    public void close() throws SerialException {
        mDevice.CloseDevice();
    }

    @Override
    public boolean isConnected() {
        return mDevice.isConnected();
    }
}
