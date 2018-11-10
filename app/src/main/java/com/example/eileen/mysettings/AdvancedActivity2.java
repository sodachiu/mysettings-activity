package com.example.eileen.mysettings;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemProperties;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eileen.mysettings.advanced.StandbyService;
import com.example.eileen.mysettings.utils.LogUtil;
import com.hisilicon.android.hidisplaymanager.HiDisplayManager;


public class AdvancedActivity2 extends AppCompatActivity
        implements View.OnClickListener {

    private TextView tvMenu;
    private LinearLayout llClearCache, llStandby1, llStandby2;
    private Button btnStandby1, btnStandby2;
    private HiDisplayManager mDisplayManager;
    private Intent intentService;
    private LogUtil logUtil = new LogUtil("myadvanced");

    public static final String NO_OPERATE_SECONDS = "14400";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        logUtil.logi("AdvancedActivity2---->onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advanced_item_activity);
        initView();

    }

    @Override
    protected void onResume(){
        super.onResume();
        logUtil.logi("AdvancedActivity2---->onResume()");

        int isTvStandbyOpen = mDisplayManager.getHDMISuspendEnable();
        int tvState = mDisplayManager.getTVStatus();
        String noOpStandbyState = SystemProperties.get("persist.sys.suspend.noop");

        if (noOpStandbyState.equals("true")){
            btnStandby2.setBackgroundResource(R.drawable.checkbox_on);
        }else {
            btnStandby2.setBackgroundResource(R.drawable.checkbox_off);
        }

        if (isTvStandbyOpen == 1 && tvState != -99){
            btnStandby1.setBackgroundResource(R.drawable.checkbox_on);
        }else {
            btnStandby1.setBackgroundResource(R.drawable.checkbox_off);
        }

    }

    public void initView(){
        logUtil.logi("AdvancedActivity2---->initView()");
        tvMenu = (TextView) findViewById(R.id.advanced);
        llClearCache = (LinearLayout) findViewById(R.id.clear_all);
        llStandby1 = (LinearLayout) findViewById(R.id.standby_5_minutes);
        llStandby2 = (LinearLayout) findViewById(R.id.standby_4_hours);
        btnStandby1 = (Button) findViewById(R.id.advanced_btn_tv_standby);
        btnStandby2 = (Button) findViewById(R.id.advanced_btn_self_standby);
        tvMenu.setBackgroundResource(R.drawable.menu_item_select);
        llClearCache.setOnClickListener(this);
        llStandby1.setOnClickListener(this);
        llStandby2.setOnClickListener(this);

        mDisplayManager = new HiDisplayManager();
        intentService = new Intent(this, StandbyService.class);

    }

    @Override
    public void onClick(View v) {
        logUtil.logi("AdvancedActivity2---->onClick()");
        switch (v.getId()) {
            case R.id.clear_all:
                final Intent intent = new Intent(AdvancedActivity2.this,
                        WipeCacheActivity.class);
                startActivity(intent);
                break;
            case R.id.standby_5_minutes:
                switchStandbyState();
                break;
            case R.id.standby_4_hours:
                switchStandbyState2();
                break;
            default:
                break;
        }
    }

    private void switchStandbyState() {

        logUtil.logi("AdvancedActivity2---->switchStandbyState()");
        int tvState = mDisplayManager.getTVStatus();
        if (tvState == -99){
            Toast.makeText(this, "电视机不支持该功能", Toast.LENGTH_SHORT).show();
            return;
        }
        int isTvStandby = mDisplayManager.getHDMISuspendEnable();

        if (isTvStandby == 1){
            btnStandby1.setBackgroundResource(R.drawable.checkbox_off);
            mDisplayManager.setHDMISuspendEnable(0);
            stopService(intentService);
            logUtil.logi("switchStandbyState()----if模块执行完毕");

        }else {

            btnStandby1.setBackgroundResource(R.drawable.checkbox_on);
            mDisplayManager.setHDMISuspendEnable(1);
            startService(intentService);
            logUtil.logi("switchStandbyState()----else模块执行完毕");

        }

    }

    private void switchStandbyState2(){

        logUtil.logi("AdvancedActivity2---->switchStandbyState2()");

        String noOpStandbyState = SystemProperties.get("persist.sys.suspend.noop");
        if (noOpStandbyState.equals("true")){
            SystemProperties.set("persist.sys.suspend.noop", "false");
            btnStandby2.setBackgroundResource(R.drawable.checkbox_off);
        }else {
            SystemProperties.set("persist.sys.suspend.noop", "true");
            SystemProperties.set("persist.sys.suspend.noop.time", NO_OPERATE_SECONDS);
            btnStandby2.setBackgroundResource(R.drawable.checkbox_on);
        }

    }

}
