package com.example.eileen.mysettings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.DhcpInfo;
import android.net.NetworkUtils;
import android.net.ethernet.EthernetManager;
import android.net.pppoe.PppoeManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eileen.mysettings.network.MyNetworkUtil;
import com.example.eileen.mysettings.utils.LogUtil;
import com.example.eileen.mysettings.utils.QuitActivity;

import java.net.Inet4Address;
import java.net.InetAddress;

public class NetInfoActivity extends QuitActivity implements View.OnKeyListener{

    private TextView tvMenu, tvIP, tvMask, tvGateway, tvTitle, tvDns1, tvDns2;
    private DhcpInfo dhcpInfo;
    private EthernetManager ethManager;
    private String mIp, mMask, mGateway, mDns1, mDns2;
    private NetworkChangeReceiver networkChangeReceiver;
    private Message msg = new Message();

    private LogUtil logUtil = new LogUtil("mynetinfo");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        logUtil.logi("NetInfoActivity---->onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.net_info_activity);
        findView();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(EthernetManager.ETHERNET_STATE_CHANGED_ACTION);
        intentFilter.addAction(EthernetManager.NETWORK_STATE_CHANGED_ACTION);
        intentFilter.addAction(PppoeManager.PPPOE_STATE_CHANGED_ACTION);
        networkChangeReceiver = new NetworkChangeReceiver();
        registerReceiver(networkChangeReceiver, intentFilter);

    }

    @Override
    protected void onResume(){
        logUtil.logi("NetInfoActivity---->onResume()");
        super.onResume();
        initView();

    }

    @Override
    protected void onDestroy(){
        logUtil.logi("NetInfoActivity---->onDestroy()");
        super.onDestroy();
        unregisterReceiver(networkChangeReceiver);
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event){

        if (event.getAction() == KeyEvent.ACTION_DOWN){
            logUtil.logi("NetInfoActivity---->onKey()");

            Intent intent;
            switch (keyCode){
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    logUtil.logi("用户按键---->KEYCODE_DPAD_DOWN");
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
        @Override
        public void onReceive(Context context, Intent intent) {
            logUtil.logi("NetInfoActivity---->onReceive()");

            String action = intent.getAction();
            logUtil.logi("收到的广播---->" + action);

            ethManager = (EthernetManager)context.getSystemService(
                    Context.ETHERNET_SERVICE);

            int ethStatus = intent.getIntExtra(EthernetManager.EXTRA_ETHERNET_STATE, -1);
            logUtil.logi("网络状态---->" + ethStatus);

            if (EthernetManager.EVENT_DHCP_CONNECT_SUCCESSED == ethStatus
                    || EthernetManager.EVENT_STATIC_CONNECT_SUCCESSED == ethStatus){

                dhcpInfo = ethManager.getDhcpInfo();
                mIp = NetworkUtils.intToInetAddress(dhcpInfo.ipAddress).getHostAddress();
                mMask = NetworkUtils.intToInetAddress(dhcpInfo.netmask).getHostAddress();
                mGateway = NetworkUtils.intToInetAddress(dhcpInfo.gateway).getHostAddress();
                mDns1 = NetworkUtils.intToInetAddress(dhcpInfo.dns1).getHostAddress();
                mDns2 = NetworkUtils.intToInetAddress(dhcpInfo.dns2).getHostAddress();

                tvIP.setText(mIp);
                tvMask.setText(mMask);
                tvGateway.setText(mGateway);
                tvDns1.setText(mDns1);
                tvDns2.setText(mDns2);

                logUtil.logi("dhcp信息---->" + dhcpInfo.toString());
            }else {
                tvIP.setText(R.string.net_default_text);
                tvMask.setText(R.string.net_default_text);
                tvGateway.setText(R.string.net_default_text);
                tvDns1.setText(R.string.net_default_text);
                tvDns2.setText(R.string.net_default_text);
            }
        }
    }

    private void findView(){
        logUtil.logi("NetInfoActivity---->findView()");
        tvMenu = (TextView) findViewById(R.id.net_info);
        tvIP = (TextView) findViewById(R.id.netinfo_tv_ip);
        tvMask = (TextView) findViewById(R.id.netinfo_tv_mask);
        tvGateway = (TextView) findViewById(R.id.netinfo_tv_gateway);
        tvDns1 = (TextView) findViewById(R.id.netinfo_et_dns1);
        tvDns2 = (TextView) findViewById(R.id.netinfo_et_dns2);

    }

    private void initView(){
        logUtil.logi("NetInfoActivity---->initView()");
        tvMenu.setFocusable(true);
        tvMenu.setBackgroundResource(R.drawable.menu_focus_selector);
        tvMenu.setOnKeyListener(this);

    }


}
