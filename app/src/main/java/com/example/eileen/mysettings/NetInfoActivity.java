package com.example.eileen.mysettings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.DhcpInfo;
import android.net.NetworkUtils;
import android.net.ethernet.EthernetManager;
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

public class NetInfoActivity extends QuitActivity 
        implements View.OnKeyListener, View.OnClickListener{

    private TextView tvMenu, tvIP, tvMask, tvGateway;
    private EditText etDns1, etDns2;
    private Button btnConfirm;
    private DhcpInfo dhcpInfo;
    private EthernetManager ethManager;
    private String mIp, mMask, mGateway, mDns1, mDns2;
    private NetworkChangeReceiver networkChangeReceiver;
    private Message msg = new Message();

    private LogUtil logUtil = new LogUtil("mynetinfo");
    public static final int DNS1_ILLEGAL = 0;
    public static final int DNS2_ILLEGAL = 1;
    public static final int MODIFY_FAILED = 2;
    public static final int DHCP_CONNECTING = 3;
    public static final int DHCP_CONNECTED = 4;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case DNS1_ILLEGAL:
                    Toast.makeText(NetInfoActivity.this,
                            "主用DNS不合法",
                            Toast.LENGTH_SHORT).show();
                    break;
                case DNS2_ILLEGAL:
                    Toast.makeText(NetInfoActivity.this,
                            "备用DNS不合法",
                            Toast.LENGTH_SHORT).show();
                    break;
                case MODIFY_FAILED:
                    Toast.makeText(NetInfoActivity.this,
                            "设置失败",
                            Toast.LENGTH_SHORT).show();
                    break;
                case DHCP_CONNECTING:
                    Toast.makeText(NetInfoActivity.this,
                            "正在尝试连接网络...",
                            Toast.LENGTH_SHORT).show();
                    break;
                case DHCP_CONNECTED:
                    Toast.makeText(NetInfoActivity.this,
                            "连接成功",
                            Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        logUtil.logi("NetInfoActivity---->onCreate()");
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
        logUtil.logi("NetInfoActivity---->onResume()");
        super.onResume();
        initView();

    }

    @Override
    protected void onDestroy(){
        logUtil.logi("NetInfoActivity---->onDestroy()");
        super.onDestroy();
        unregisterReceiver(networkChangeReceiver);
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event){

        if (event.getAction() == KeyEvent.ACTION_DOWN){
            logUtil.logi("NetInfoActivity---->onKey()");

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
                etDns1.setText(mDns1);
                etDns2.setText(mDns2);

                logUtil.logi("dhcp信息---->" + dhcpInfo.toString());
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
        logUtil.logi("NetInfoActivity---->findView()");
        tvMenu = (TextView) findViewById(R.id.net_info);
        tvIP = (TextView) findViewById(R.id.netinfo_tv_ip);
        tvMask = (TextView) findViewById(R.id.netinfo_tv_mask);
        tvGateway = (TextView) findViewById(R.id.netinfo_tv_gateway);
        etDns1 = (EditText) findViewById(R.id.netinfo_et_dns1);
        etDns2 = (EditText) findViewById(R.id.netinfo_et_dns2);
        btnConfirm = (Button) findViewById(R.id.netinfo_btn_confirm);

    }

    private void initView(){
        logUtil.logi("NetInfoActivity---->initView()");
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
        logUtil.logi("NetInfoActivity---->onClick()");
        ethManager.setEthernetEnabled(false);
        switch (v.getId()){
            case R.id.netinfo_btn_confirm:
                setStatic();
                break;
            default:
                break;
        }
    }

    /*
    * 当用户点击确定按钮后，尝试将网络连接模式设置为静态
    * */
    private void setStatic(){
        String staticDns1 = etDns1.getText().toString();
        String staticDns2 = etDns2.getText().toString();

        try{
            boolean dns1IsLegal = MyNetworkUtil.checkDhcpItem(staticDns1);
            boolean dns2IsLegal = MyNetworkUtil.checkDhcpItem(staticDns2);

            if (dns1IsLegal){
                InetAddress idns1 = NetworkUtils.numericToInetAddress(staticDns1);
                dhcpInfo.dns1 = NetworkUtils.inetAddressToInt((Inet4Address) idns1);
            }else {
                msg.what = DNS1_ILLEGAL;
                mHandler.sendMessage(msg);
                logUtil.logi("dns1不合法");
            }

            if (dns2IsLegal){
                InetAddress idns2 = NetworkUtils.numericToInetAddress(staticDns2);
                dhcpInfo.dns2 = NetworkUtils.inetAddressToInt((Inet4Address) idns2);
            }else {
                msg.what = DNS2_ILLEGAL;
                mHandler.sendMessage(msg);
                logUtil.logi("dns2不合法");
            }

            ethManager.setEthernetMode(EthernetManager.ETHERNET_CONNECT_MODE_MANUAL, dhcpInfo);
            logUtil.logi("静态dhcp信息---->" + dhcpInfo.toString());

        }catch (Exception e){
            msg.what = MODIFY_FAILED;
            mHandler.sendMessage(msg);
            logUtil.loge("设置静态ip失败");

        }
        ethManager.setEthernetEnabled(true);
        msg.what = DHCP_CONNECTING;
        mHandler.sendMessage(msg);

        if (ethManager.getEthernetMode().equals(EthernetManager.ETHERNET_CONNECT_MODE_MANUAL)
                && ethManager.getDhcpInfo() != null){
            msg.what = DHCP_CONNECTED;
            mHandler.sendMessage(msg);
            logUtil.logi("网络连接成功");
        }
    }
}
