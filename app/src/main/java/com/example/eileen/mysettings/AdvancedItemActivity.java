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
    private HiDisplayManager displayManager;
    private int mHdmiStatus;
    private String isSelfStandbyOpen;
    private int isTvStandbyOpen;
    private MyReceiver receiver;
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
        if (isSelfStandbyOpen.equals("true")){
            btnSelfStandby.setBackgroundResource(R.drawable.checkbox_on);
        }else {
            btnSelfStandby.setBackgroundResource(R.drawable.checkbox_off);
        }

        if (isTvStandbyOpen == 1){
            btnTvStandby.setBackgroundResource(R.drawable.checkbox_on);
        }else {
            btnTvStandby.setBackgroundResource(R.drawable.checkbox_off);
        }

        IntentFilter filter = new IntentFilter("com.cbox.action.autosleep");
        receiver = new MyReceiver(AdvancedItemActivity.this);
        registerReceiver(receiver, filter);
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
                //用服务做


                break;
            case R.id.standby_4_hours:
                //用服务做
                switchSelfStandbyStatus();
                break;
            default:
                break;
        }
    }

    private void switchTvStandbyStatus() {

        /*int isTvStandby = hdm.getHDMISuspendEnable();

        if (isTvStandby == 1){
            hdm.setHDMISuspendEnable(0);
            btnTvStandby.setBackgroundResource(R.drawable.checkbox_off);
        }else {
            hdm.setHDMISuspendEnable(1);
            PowerManager pm =  (PowerManager) context.getSystemService(
                    Context.POWER_SERVICE);
            pm.goToSleep(1000);
            logUtil.logi("我好像到了这里");
            btnTvStandby.setBackgroundResource(R.drawable.checkbox_on);

        }
        //读取是否开启了功能，如果开启，那么进入开启逻辑，如果关闭，则不处理
        mDisplayManager = new HiDisplayManager();
        logUtil.logi( mDisplayManager.getFmt() + "hdmi线输出情况");
    }*/
    }

    private void switchSelfStandbyStatus(){
        isSelfStandbyOpen = SystemProperties.get("persist.sys.suspend.noop");
        logUtil.logi(isSelfStandbyOpen + "我在判断是否开启机顶盒自动待机");
        if (isSelfStandbyOpen.equals("true")){
            SystemProperties.set("persist.sys.suspend.noop", "false");
            btnSelfStandby.setBackgroundResource(R.drawable.checkbox_off);
        }else {
            SystemProperties.set("persist.sys.suspend.noop", "true");
            SystemProperties.set("persist.sys.suspend.noop.time", "12000");
            btnSelfStandby.setBackgroundResource(R.drawable.checkbox_on);

        }

    }

}
