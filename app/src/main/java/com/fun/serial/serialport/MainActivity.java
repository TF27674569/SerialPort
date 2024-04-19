package com.fun.serial.serialport;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.fun.board.BoardPort;
import com.fun.port.SerialControl;
import com.fun.port.SerialDevice;
import com.fun.serial.serialport.cmd.TempRequest;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // 请求权限
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 200);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 200) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                SerialDevice device = new BoardPort(this);
                // 驱动文件和波特率 这里是dev文件下的ttyS4驱动 波特率为9600
                device.open("/dev/ttyS4",9600);
                SerialControl.get().init(device);
            } else {
                Toast.makeText(this, "权限被拒绝", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    public void test(View view) {
        SerialControl.get().request(new TempRequest() {
            @Override
            protected void onResult(String value) {
                // 注意这是子线程
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "value："+value, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}