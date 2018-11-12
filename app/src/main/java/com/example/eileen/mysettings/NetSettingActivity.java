package com.example.eileen.mysettings;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.eileen.mysettings.utils.QuitActivity;

public class NetSettingActivity extends QuitActivity
        implements View.OnClickListener, View.OnKeyListener{

    private static final String TAG = "NetSettingActivity";
    private TextView tvNetSetting;
    private LinearLayout llSetNet;
    private LinearLayout llSetBluetooth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.net_setting_activity);

        tvNetSetting = (TextView) findViewById(R.id.net_setting);
        llSetNet = (LinearLayout) findViewById(R.id.set_net);
        llSetBluetooth = (LinearLayout) findViewById(R.id.set_bluetooth);

        tvNetSetting.setFocusable(true);
        tvNetSetting.setBackgroundResource(R.drawable.menu_focus_selector);
        llSetNet.setOnClickListener(this);
        llSetBluetooth.setOnClickListener(this);
        tvNetSetting.setOnKeyListener(this);


    }

    @Override
    protected void onResume() {
        super.onResume();
        llSetNet.setFocusable(true);
        llSetBluetooth.setFocusable(true);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.set_net:
                intent = new Intent(NetSettingActivity.this, EthSettingActivity.class);
                startActivity(intent);
                break;
            case R.id.set_bluetooth:
                intent = new Intent(NetSettingActivity.this, BluetoothActivity.class);
                startActivity(intent);
                break;

            default:
                break;
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event){
        if (event.getAction() == KeyEvent.ACTION_DOWN){

            switch (keyCode){
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    llSetNet.setFocusable(false);
                    llSetBluetooth.setFocusable(false);
                    Intent intentDown = new Intent(NetSettingActivity.this, NetInfoActivity.class);
                    NetSettingActivity.this.startActivity(intentDown);
                    break;

                case KeyEvent.KEYCODE_DPAD_UP:
                    Intent intentUp = new Intent(NetSettingActivity.this, MainActivity.class);
                    NetSettingActivity.this.startActivity(intentUp);
                    break;
                default:
                    break;

            }
        }
        return false;
    }
}
