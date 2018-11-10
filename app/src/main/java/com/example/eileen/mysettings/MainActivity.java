package com.example.eileen.mysettings;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemProperties;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.example.eileen.mysettings.advanced.StandbyService;
import com.example.eileen.mysettings.utils.LogUtil;
import com.example.eileen.mysettings.utils.QuitActivity;
import com.hisilicon.android.hidisplaymanager.HiDisplayManager;


public class MainActivity extends QuitActivity implements View.OnKeyListener{

    private TextView tvMenu, tvDeviceModel, tvSoftRelease, tvAndroidRelease, tvMac,
            tvStbid, tvAuthaddr1, tvAuthAddr2, tvTerminalAddr1, tvTerminalAddr2, tvAccount;

    private LogUtil logUtil = new LogUtil("myabout");
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        initView();
        logUtil.logi("完成oncreate()");

        //启动自动待机服务
        HiDisplayManager hdm = new HiDisplayManager();
        int powerStandbyIsOpen = hdm.getHDMISuspendEnable();
        int tvState = hdm.getTVStatus();
        if (powerStandbyIsOpen == 1 && tvState != -99){
            Intent intent = new Intent(this, StandbyService.class);
            startService(intent);
        }

    }


    private void initView(){
        logUtil.logi("MainActivity---->initView()");
        tvMenu = (TextView) findViewById(R.id.my_info);
        tvDeviceModel = (TextView) findViewById(R.id.about_tv_model);
        tvSoftRelease = (TextView) findViewById(R.id.about_tv_software);
        tvAndroidRelease = (TextView) findViewById(R.id.about_tv_android);
        tvMac = (TextView) findViewById(R.id.about_tv_mac);
        tvStbid = (TextView) findViewById(R.id.about_tv_stbid);
        tvAuthaddr1 = (TextView) findViewById(R.id.about_tv_auth_addr1);
        tvAuthAddr2 = (TextView) findViewById(R.id.about_tv_auth_addr2);
        tvTerminalAddr1 = (TextView) findViewById(R.id.about_tv_terminal1);
        tvTerminalAddr2 = (TextView) findViewById(R.id.about_tv_terminal2);
        tvAccount = (TextView) findViewById(R.id.about_tv_account);

        tvMenu.setFocusable(true);
        tvMenu.setOnKeyListener(this);
        tvMenu.setBackgroundResource(R.drawable.menu_focus_selector);
        initValue();
    }


    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN){
            logUtil.logi("MainActivity--->onKey()");
            switch (keyCode){
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    Intent intent = new Intent(MainActivity.this, NetSettingActivity.class);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }

        return false;
    }


    public void initValue(){
        logUtil.logi("MainActivity--->initValue()");
        String sModel = SystemProperties.get("ro.product.model"); //硬件产品品牌型号
        String sSoftware = SystemProperties.get("ro.build.version.incremental"); //软件版本
        String sAndroid = SystemProperties.get("ro.build.version.release"); //安卓版本
        String sMAC = SystemProperties.get("ro.mac"); //mac地址
        String sStbid = SystemProperties.get("ro.serialno"); //32位设备串号，同Build.SERIAL
        String sTerminal1 = SystemProperties.get("persist.sys.tr069.ServerURL"); //终端管理主地址
        String sTerminal2 = SystemProperties.get("persist.sys.tr069.ServerURLBK"); //终端管理备地址
        String sAccout = SystemProperties.get("ro.deviceid"); //15位串号
        String sAuthen1 = "";
        String sAuthen2 = "";

        Uri tableAuth  = Uri.parse("content://stbconfig/authentication");
        Cursor cursor;

        try{
            cursor = getApplicationContext().getContentResolver().query(tableAuth,
                    null, null, null, null);

            if (cursor != null && cursor.moveToFirst()){

                logUtil.logi("表的总行数" + cursor.getCount());

                do {
                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    String value = cursor.getString(cursor.getColumnIndex("value"));
                    logUtil.logi(name + "----" + value);
                    if (sAuthen1.equals("") && name.equals("eds_server")){
                        sAuthen1 = value;
                    }

                    if (sAuthen2.equals("") && name.equals("eds_server2")){
                        sAuthen2 = value;
                    }

                } while (cursor.moveToNext());
                cursor.close();
            }else {
                logUtil.logi("cursor 为 null");
            }

        }catch (Exception e){
            logUtil.loge("数据库信息获取失败");
        }

        tvDeviceModel.setText(sModel);
        tvSoftRelease.setText(sSoftware);
        tvAndroidRelease.setText(sAndroid);
        tvMac.setText(sMAC);
        tvStbid.setText(sStbid);
        tvAuthaddr1.setText(sAuthen1);
        tvAuthAddr2.setText(sAuthen2);
        tvTerminalAddr1.setText(sTerminal1);
        tvTerminalAddr2.setText(sTerminal2);
        tvAccount.setText(sAccout);

    }

}
