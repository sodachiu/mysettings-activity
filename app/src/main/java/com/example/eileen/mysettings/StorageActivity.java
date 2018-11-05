package com.example.eileen.mysettings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eileen.mysettings.storage.StorageUtils;
import com.example.eileen.mysettings.storage.UninstallDialog;
import com.example.eileen.mysettings.utils.ActivityId;
import com.example.eileen.mysettings.utils.LogUtil;
import com.example.eileen.mysettings.utils.QuitActivity;


public class StorageActivity extends QuitActivity
        implements View.OnKeyListener, View.OnClickListener{

    private TextView tvMenu;
    private LinearLayout llUninstall;
    private TextView tvTotal;
    private TextView tvTotalDevices1;
    private TextView tvAlt;
    private TextView tvTotalDevices2;

    private MediaReceiver mediaReceiver;
    private Context mContext;
    private int devCount = 1;

    private LogUtil logUtil = new LogUtil("mystorage");

    private static final String MEDIA_EJECT = "android.intent.action.MEDIA_EJECT";
    private static final String MEDIA_MOUNTED = "android.intent.action.MEDIA_MOUNTED";
    private static final String MEDIA_UNMOUNTED = "android.intent.action.MEDIA_UNMOUNTED";
    private static final String PATH1 = "/mnt/sda/sda1";
    private static final String PATH2 = "/mnt/sdb/sdb1";
    private static boolean path1Exists = false;
    private static boolean path2Exists = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.storage_activity);
        initView();

        IntentFilter filter = new IntentFilter();
        filter.addAction(MEDIA_EJECT); //物理拔出打开状态的sd卡
        filter.addAction(MEDIA_MOUNTED); //机顶盒正确挂载sd卡
        filter.addAction(MEDIA_UNMOUNTED); //sd卡存在，但没有挂载
        filter.addDataScheme("file");

        mediaReceiver = new MediaReceiver();
        registerReceiver(mediaReceiver, filter);


    }

    private void initView(){
        tvMenu = (TextView) findViewById(R.id.store_info);
        llUninstall = (LinearLayout) findViewById(R.id.storage_unmount);
        tvTotal = (TextView) findViewById(R.id.storage_tv_total);
        tvTotalDevices1 = (TextView) findViewById(R.id.storage_tv_devices_count1);
        tvAlt = (TextView) findViewById(R.id.storage_tv_available);
        tvTotalDevices2 = (TextView) findViewById(R.id.storage_tv_devices_count2);
        mContext = getApplicationContext();

        tvMenu.setFocusable(true);
        tvMenu.setBackgroundResource(R.drawable.menu_focus_selector);
        tvMenu.setOnKeyListener(this);
        llUninstall.setOnClickListener(this);
    }


    @Override
    protected void onResume(){
        super.onResume();
        llUninstall.setFocusable(true);
        refreshView();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        unregisterReceiver(mediaReceiver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        logUtil.logi("onActivity()");
        if (requestCode != ActivityId.STORE_INFO_ACTIVITY){
            Toast.makeText(mContext,
                    "onActivityResult() 返回值有误",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        switch (resultCode){
            case RESULT_OK:
                if (path1Exists){
                    try {
                        StorageUtils.unMount(PATH1);
                        logUtil.logi("设备1卸载成功");

                    }catch (Exception e){
                        logUtil.loge("设备1卸载失败---->" + e.toString());
                    }
                }

                if (path2Exists){
                    try {
                        StorageUtils.unMount(PATH2);
                        logUtil.logi("设备2卸载成功");

                    }catch (Exception e){
                        logUtil.loge("设备2卸载失败---->" + e.toString());
                    }
                }
                break;
            case RESULT_CANCELED:
                logUtil.logi("取消卸载操作");
                break;
            default:
                break;
        }

    }

    class MediaReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            logUtil.logi("接受到的广播为---->" + action);
            refreshView();
        }
    }



    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.storage_unmount:
                if (devCount == 1){
                    Toast.makeText(StorageActivity.this,
                            "没有挂载外部设备",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(StorageActivity.this,
                        UninstallDialog.class);
                startActivityForResult(intent, ActivityId.STORE_INFO_ACTIVITY);
                break;
            default:
                break;
        }

    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event){

        if (event.getAction() == KeyEvent.ACTION_DOWN){
            Intent intent;
            switch (keyCode){
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    intent = new Intent(StorageActivity.this, AdvancedActivity.class);
                    startActivity(intent);
                    break;
                case KeyEvent.KEYCODE_DPAD_UP:
                    llUninstall.setFocusable(false);
                    intent = new Intent(StorageActivity.this, DisplayActivity.class);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
        return false;
    }

    public void refreshView(){
        logUtil.logi("refreshView()");
        devCount = 1;
        long totalSize = 0;
        long availableSize = 0;

        path1Exists = StorageUtils.fileIsExists(PATH1);
        path2Exists = StorageUtils.fileIsExists(PATH2);

        totalSize += StorageUtils.getRomTotalSize();
        availableSize += StorageUtils.getRomAvailableSize();
        if(path1Exists){
            ++devCount;
            totalSize += StorageUtils.getExternalTotalSize(PATH1);
            availableSize += StorageUtils.getExternalAvailableSize(PATH1);
        }
        if (path2Exists){
            ++devCount;
            totalSize += StorageUtils.getExternalTotalSize(PATH2);
            availableSize += StorageUtils.getExternalAvailableSize(PATH2);
        }


        String sTotalSize = Formatter.formatFileSize(mContext, totalSize);
        String sAvailableSize = Formatter.formatFileSize(mContext, availableSize);
        String totalDev = getResources().getString(R.string.storage_devices_count) + devCount;
        tvTotalDevices1.setText(totalDev);
        tvTotalDevices2.setText(totalDev);
        tvTotal.setText(sTotalSize);
        tvAlt.setText(sAvailableSize);
    }

}
