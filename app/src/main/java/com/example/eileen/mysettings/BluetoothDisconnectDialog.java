package com.example.eileen.mysettings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.eileen.mysettings.bluetooth.BluetoothLog;

public class BluetoothDisconnectDialog extends Activity
        implements View.OnClickListener{

    int mPosition;
    String mDeviceInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_bluetooth_disconnect);
        Intent intent = getIntent();
        mDeviceInfo = intent.getStringExtra("device_info");
        mPosition = intent.getIntExtra("device_position", -1);
        BluetoothLog.LOGI("dialog" + mDeviceInfo);
        BluetoothLog.LOGI("dialog position" + mPosition);
        BluetoothLog.LOGI("hello");
        TextView tvPromptInfo = (TextView) findViewById(R.id.bluetooth_disconnect_prompt_info);
        Button btnConfirm = (Button) findViewById(R.id.bluetooth_btn_disconnect_confirm);
        Button btnCancel = (Button) findViewById(R.id.bluetooth_btn_disconnect_cancel);
        String promptInfo = "确定要取消与【" + mDeviceInfo + "】的配对吗？";
        tvPromptInfo.setText(promptInfo);

        btnConfirm.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){

            case R.id.bluetooth_btn_disconnect_confirm:
                Intent intent = new Intent();
                intent.putExtra("device_position", mPosition);
                intent.putExtra("device_info", mDeviceInfo);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.bluetooth_btn_disconnect_cancel:
                Intent intent1 = new Intent();
                setResult(RESULT_CANCELED, intent1);
                finish();
                break;
            default:
                break;
        }
    }
}
