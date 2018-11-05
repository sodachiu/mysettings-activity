package com.example.eileen.mysettings;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.DhcpInfo;
import android.net.NetworkUtils;
import android.net.ethernet.EthernetManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eileen.mysettings.utils.ActivityCollector;
import com.example.eileen.mysettings.utils.LogUtil;
import com.example.eileen.mysettings.utils.QuitActivity;

import java.net.Inet4Address;
import java.net.InetAddress;

public class NetInfoActivity extends QuitActivity 
        implements View.OnKeyListener, View.OnClickListener{

    private TextView tvMenu, tvIP, tvMask, tvGateway;
    private EditText etDns1, etDns2;
    private Button btnConfirm;
    private DhcpInfo dhcpInfo;
    private EthernetManager ethManager;
    private NetworkChangeReceiver networkChangeReceiver;

    private LogUtil logUtil = new LogUtil("mynetinfo");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        logUtil.logi("onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.net_info_activity);
        findView();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(EthernetManager.ETHERNET_STATE_CHANGED_ACTION);
        intentFilter.addAction(EthernetManager.NETWORK_STATE_CHANGED_ACTION);
        networkChangeReceiver = new NetworkChangeReceiver();
        registerReceiver(networkChangeReceiver, intentFilter);

    }

    @Override
    protected void onResume(){
        logUtil.logi("onResume()");
        super.onResume();
        initView();

    }

    @Override
    protected void onDestroy(){
        logUtil.logi("onDestroy()");
        super.onDestroy();
        unregisterReceiver(networkChangeReceiver);
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event){
        logUtil.logi("onKey()");
        if (event.getAction() == KeyEvent.ACTION_DOWN){
            Intent intent;
            switch (keyCode){
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    logUtil.logi("用户按键---->KEYCODE_DPAD_DOWN");
                    etDns1.setFocusable(false);
                    etDns2.setFocusable(false);
                    btnConfirm.setFocusable(false);
                    intent = new Intent(NetInfoActivity.this, DateTimeActivity.class);
                    startActivity(intent);
                    break;
                case KeyEvent.KEYCODE_DPAD_UP:
                    logUtil.logi("用户按键---->KEYCODE_DPAD_UP");
                    intent = new Intent(NetInfoActivity.this, NetSettingActivity.class);
                    startActivity(intent);
                    break;
                default:
                    break;

            }
        }

        return false;
    }

    class NetworkChangeReceiver extends BroadcastReceiver{
        private String sIp;
        private String sMask;
        private String sGateway;
        private String sDns1;
        private String sDns2;
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            logUtil.logi("收到的广播---->" + action);

            ethManager = (EthernetManager)context.getSystemService(
                    Context.ETHERNET_SERVICE);

            int ethStatus = intent.getIntExtra(EthernetManager.EXTRA_ETHERNET_STATE, -1);
            logUtil.logi("网络状态---->" + ethStatus);

            if (EthernetManager.EVENT_DHCP_CONNECT_SUCCESSED == ethStatus){

                dhcpInfo = ethManager.getDhcpInfo();
                sIp = NetworkUtils.intToInetAddress(dhcpInfo.ipAddress).getHostAddress();
                sMask = NetworkUtils.intToInetAddress(dhcpInfo.netmask).getHostAddress();
                sGateway = NetworkUtils.intToInetAddress(dhcpInfo.gateway).getHostAddress();
                sDns1 = NetworkUtils.intToInetAddress(dhcpInfo.dns1).getHostAddress();
                sDns2 = NetworkUtils.intToInetAddress(dhcpInfo.dns2).getHostAddress();

                tvIP.setText(sIp);
                tvMask.setText(sMask);
                tvGateway.setText(sGateway);
                etDns1.setText(sDns1);
                etDns2.setText(sDns2);

                logUtil.logi("ip---->" + sIp);
                logUtil.logi("mask---->" + sMask);
                logUtil.logi("gateway---->" + sGateway);
                logUtil.logi("dns1---->" + sDns1);
                logUtil.logi("dns2---->" + sDns2);
            }else {
                tvIP.setText(R.string.net_default_text);
                tvMask.setText(R.string.net_default_text);
                tvGateway.setText(R.string.net_default_text);
                etDns1.setText(R.string.net_default_text);
                etDns2.setText(R.string.net_default_text);
            }
        }
    }

    private void findView(){
        tvMenu = (TextView) findViewById(R.id.net_info);
        tvIP = (TextView) findViewById(R.id.netinfo_tv_ip);
        tvMask = (TextView) findViewById(R.id.netinfo_tv_mask);
        tvGateway = (TextView) findViewById(R.id.netinfo_tv_gateway);
        etDns1 = (EditText) findViewById(R.id.netinfo_et_dns1);
        etDns2 = (EditText) findViewById(R.id.netinfo_et_dns2);
        btnConfirm = (Button) findViewById(R.id.netinfo_btn_confirm);

    }

    private void initView(){
        logUtil.logi("initView()");
        tvMenu.setFocusable(true);
        tvMenu.setBackgroundResource(R.drawable.menu_focus_selector);
        tvMenu.setOnKeyListener(this);

        etDns1.setFocusable(true);
        etDns2.setFocusable(true);
        etDns1.setSelectAllOnFocus(true);
        etDns2.setSelectAllOnFocus(true);
        btnConfirm.setOnClickListener(this);

    }
    
    @Override
    public void onClick(View v){
        logUtil.logi("onClick()");
        switch (v.getId()){
            case R.id.netinfo_btn_confirm:
                String userDns1 = etDns1.getText().toString();
                String userDns2 = etDns2.getText().toString();

                try{

                    InetAddress idns1 = NetworkUtils.numericToInetAddress(userDns1);
                    InetAddress idns2 = NetworkUtils.numericToInetAddress(userDns2);
                    dhcpInfo.dns1 = NetworkUtils.inetAddressToInt((Inet4Address) idns1);
                    dhcpInfo.dns2 = NetworkUtils.inetAddressToInt((Inet4Address) idns2);
                    logUtil.logi("手动设置的dhcp---->" + dhcpInfo.toString());

                    ethManager.setEthernetMode(EthernetManager.ETHERNET_CONNECT_MODE_MANUAL, dhcpInfo);

                    logUtil.logi("以太网模式---->" + ethManager.getEthernetMode());
                    Toast.makeText(NetInfoActivity.this,
                            "设置成功",
                             Toast.LENGTH_SHORT).show();

                }catch (Exception e){
                    logUtil.loge("存储dns失败");
                    Toast.makeText(NetInfoActivity.this,
                            "设置失败",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
}
