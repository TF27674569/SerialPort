package com.fun.serial.serialport;

import android.Manifest;
import android.app.Application;
import android.content.pm.PackageManager;
import android.widget.Toast;

import com.fun.board.BoardPort;
import com.fun.port.SerialControl;
import com.fun.port.SerialDevice;

import androidx.core.content.ContextCompat;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // BoardPort 板载串口，232 485都行
        // UsbPort 是usb转的驱动(应该是485)
        // 如果有特殊使用场景 直接实现SerialDevice接口，用socket通信都可以
        // 这里需要注意权限问题(SDCARD的读写) 最好是在引导页申请权限后初始化
        // 如果是内置系统应用，放入权限白名单
        // 或将包名放入frameworks/base/services/core/java/com/android/server/pm/DefaultPermissionGrantPolicy.java文件里面自动授权
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_DENIED) {
            SerialDevice device = new BoardPort(this);
            // 驱动文件和波特率 这里是dev文件下的ttyS4驱动 波特率为9600
            device.open("/dev/ttyS4",9600);
            SerialControl.get().init(device);
        }

    }
}
