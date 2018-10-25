package com.example.eileen.mysettings;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemProperties;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements View.OnKeyListener{

    private static final String TAG = "myabout";

    private TextView tvMenu, tvDeviceModel, tvSoftRelease, tvAndroidRelease, tvMac,
            tvStbid, tvAuthaddr1, tvAuthAddr2, tvTerminalAddr1, tvTerminalAddr2, tvAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

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
    public void onDestroy(){
        super.onDestroy();
    }



    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN){
            switch (keyCode){
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    Intent intent = new Intent(MainActivity.this, NetSettingActivity.class);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }

        Log.i("myabout", "按了下");
        return false;
    }


    public void initValue(){
        String sModel = SystemProperties.get("ro.product.model");
        String sSoftware = SystemProperties.get("ro.build.version.incremental");
        String sAndroid = SystemProperties.get("ro.build.version.release");
        String sMAC = SystemProperties.get("ro.mac");
        String sStbid = SystemProperties.get("ro.serialno");
        String sTerminal1 = SystemProperties.get("persist.sys.tr069.ServerURL");
        String sTerminal2 = SystemProperties.get("persist.sys.tr069.ServerURLBK");
        String sAccout = "";
        String sAuthen1 = "";
        String sAuthen2 = "";
        Uri uri = Uri.parse("content://stbconfig/authentication/eds_server2");

        try {
            sAuthen1 = SystemProperties.get("persist.sys.devinfo.hdcrurl");
            Log.i(TAG, "认证主地址" + sAuthen1);
        }catch (Exception e){
            Log.i(TAG, "获取认证主地址失败");
            Log.i(TAG, e.getMessage() + "");
        }

        try {
            Cursor cursor = getApplicationContext().getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    sAuthen2 = cursor.getString(cursor.getColumnIndex("value"));
                }
                cursor.close();
            }
        } catch (Exception e){
            Log.i(TAG, "获取认证业务备地址失败");
            Log.i(TAG, e.getMessage() + "");
        }

        try{
            sAccout = SystemProperties.get("persist.sys.devinfo.account");

        }catch (Exception e){
            Log.i(TAG, "获取认证业务账号失败");
            Log.i(TAG, e.getMessage() + "");
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
