package com.example.eileen.mysettings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.SystemProperties;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.eileen.mysettings.advanced.MyService;
import com.example.eileen.mysettings.advanced.StandbyDialog;
import com.example.eileen.mysettings.utils.LogUtil;
import com.hisilicon.android.hidisplaymanager.HiDisplayManager;

import java.lang.ref.WeakReference;


public class AdvancedItemActivity extends AppCompatActivity
        implements View.OnClickListener {

    private TextView advanced;
    private LinearLayout clearAll;
    private LinearLayout standbyMinutes;
    private LinearLayout standbyHours;
    private Button btnTvStandby;
    private Button btnSelfStandby;
    private HiDisplayManager mDisplayManager;
    private Context mContext;
    private int mHdmiStatus;
    private String isSelfStandbyOpen;
    private int isTvStandbyOpen;
    private MyReceiver receiver;
    private Intent intentService;
    private LogUtil logUtil = new LogUtil("advancedItemActivity");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advanced_item_activity);
        initView();

    }

    @Override
    protected void onResume(){
        super.onResume();
        isSelfStandbyOpen = SystemProperties.get("persist.sys.suspend.noop");
        isTvStandbyOpen = mDisplayManager.getHDMISuspendEnable();
        intentService = new Intent(this, MyService.class);
        if (isSelfStandbyOpen.equals("true")){
            btnSelfStandby.setBackgroundResource(R.drawable.checkbox_on);
        }else {
            btnSelfStandby.setBackgroundResource(R.drawable.checkbox_off);
        }

        if (isTvStandbyOpen == 1){
            btnTvStandby.setBackgroundResource(R.drawable.checkbox_on);
            startService(intentService);
        }else {
            btnTvStandby.setBackgroundResource(R.drawable.checkbox_off);
            stopService(intentService);
        }

    }

    public void initView(){
        advanced = (TextView) findViewById(R.id.advanced);
        clearAll = (LinearLayout) findViewById(R.id.clear_all);
        standbyMinutes = (LinearLayout) findViewById(R.id.standby_5_minutes);
        standbyHours = (LinearLayout) findViewById(R.id.standby_4_hours);
        btnTvStandby = (Button) findViewById(R.id.advanced_btn_tv_standby);
        btnSelfStandby = (Button) findViewById(R.id.advanced_btn_self_standby);
        advanced.setBackgroundResource(R.drawable.menu_item_select);
        clearAll.setOnClickListener(this);
        standbyMinutes.setOnClickListener(this);
        standbyHours.setOnClickListener(this);

        mContext = getApplicationContext();
        mDisplayManager = new HiDisplayManager();
        logUtil.logi("suspendtime" + mDisplayManager.getHDMISuspendTime());


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clear_all:
                final Intent intent = new Intent(AdvancedItemActivity.this,
                        WipeCacheActivity.class);
                startActivity(intent);
                break;
            case R.id.standby_5_minutes:
                switchTvStandbyStatus();
                break;
            case R.id.standby_4_hours:
                switchSelfStandbyStatus();
                break;
            default:
                break;
        }
    }

    private void switchTvStandbyStatus() {

        int isTvStandby = mDisplayManager.getHDMISuspendEnable();

        if (isTvStandby == 1){
            btnTvStandby.setBackgroundResource(R.drawable.checkbox_off);
            mDisplayManager.setHDMISuspendEnable(0);
            stopService(intentService);
            logUtil.logi("switchTvStandbyStatus()----if模块执行完毕");

        }else {

            btnTvStandby.setBackgroundResource(R.drawable.checkbox_on);
            mDisplayManager.setHDMISuspendEnable(1);
            startService(intentService);
            logUtil.logi("switchTvStandbyStatus()----else模块执行完毕");

        }

    }

    private void switchSelfStandbyStatus(){
        isSelfStandbyOpen = SystemProperties.get("persist.sys.suspend.noop");
        if (isSelfStandbyOpen.equals("true")){
            SystemProperties.set("persist.sys.suspend.noop", "false");
            btnSelfStandby.setBackgroundResource(R.drawable.checkbox_off);
            logUtil.logi("我关闭了倒计时");
        }else {
            SystemProperties.set("persist.sys.suspend.noop", "true");
            SystemProperties.set("persist.sys.suspend.noop.time", "10");
            String time = SystemProperties.get("persist.sys.suspend.noop.time");
            btnSelfStandby.setBackgroundResource(R.drawable.checkbox_on);
            logUtil.logi("我打开了倒计时,现在倒计时需要的时间为" + time);
        }

    }

}
