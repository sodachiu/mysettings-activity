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
import android.util.Log;
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
    private DhcpInfo mDhcpInfo;
    private EthernetManager mEthManager;
    boolean netAvailable = false;
    private MyNetworkUtil.MyDhcpInfo myDhcpInfo;
    private Context mContext;

    private static final String TAG = "mynetinfo";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.net_info_activity);

        mContext = getApplicationContext();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(EthernetManager.ETHERNET_STATE_CHANGED_ACTION);
        intentFilter.addAction(PppoeManager.PPPOE_STATE_CHANGED_ACTION);
        registerReceiver(myReceiver, intentFilter);

    }

    @Override
    protected void onResume(){
        Log.i(TAG, "onResume: ");
        super.onResume();
        initView();

    }

    @Override
    protected void onDestroy(){
        Log.i(TAG, "onDestroy: ");
        super.onDestroy();
        unregisterReceiver(myReceiver);
        myDhcpInfo = null;
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event){

        if (event.getAction() == KeyEvent.ACTION_DOWN){
            Log.i(TAG, "onKey: 按键代码---->" + keyCode);

            Intent intent;
            switch (keyCode){
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    intent = new Intent(NetInfoActivity.this, DateTimeActivity.class);
                    startActivity(intent);
                    break;
                case KeyEvent.KEYCODE_DPAD_UP:
                    intent = new Intent(NetInfoActivity.this, NetSettingActivity.class);
                    startActivity(intent);
                    break;
                default:
                    break;

            }
        }

        return false;
    }

    private BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            String ethMode = mEthManager.getEthernetMode();

            Log.i(TAG, "onReceive: 广播---->" + action);

            if (action.equals(EthernetManager.ETHERNET_STATE_CHANGED_ACTION)){
                int ethEvent = intent.getIntExtra(EthernetManager.EXTRA_ETHERNET_STATE, -1);
                Log.i(TAG, "onReceive: 网络事件---->" + ethEvent);
                
                if (ethMode.equals(EthernetManager.ETHERNET_CONNECT_MODE_DHCP)){
                    switch (ethEvent){
                        case EthernetManager.EVENT_DHCP_CONNECT_SUCCESSED:
                            netAvailable = true;
                            Log.i(TAG, "onReceive: EVENT_DHCP_CONNECT_SUCCESSED");
                            break;
                        case EthernetManager.EVENT_DHCP_DISCONNECT_FAILED:
                            netAvailable = true;
                            Log.i(TAG, "onReceive: EVENT_DHCP_DISCONNECT_FAILED");
                            break;
                        default:
                            netAvailable = false;
                            break;
                    }
                }else if (ethMode.equals(EthernetManager.ETHERNET_CONNECT_MODE_MANUAL)){
                    switch (ethEvent){
                        case EthernetManager.EVENT_STATIC_CONNECT_SUCCESSED:
                            netAvailable = true;
                            Log.i(TAG, "onReceive: EVENT_STATIC_CONNECT_SUCCESSED");
                            break;
                        case EthernetManager.EVENT_STATIC_DISCONNECT_FAILED:
                            netAvailable = true;
                            Log.i(TAG, "onReceive: EVENT_STATIC_DISCONNECT_FAILED");
                            break;
                        default:
                            netAvailable = false;
                            break;
                    }
                }
            }else if (action.equals(PppoeManager.PPPOE_STATE_CHANGED_ACTION)){
                int pppoeEvent = intent.getIntExtra(PppoeManager.EXTRA_PPPOE_STATE, -1);
                Log.i(TAG, "onReceive: 网络事件---->" + pppoeEvent);
                if (ethMode.equals(EthernetManager.ETHERNET_CONNECT_MODE_PPPOE)){
                    switch (pppoeEvent){
                        case PppoeManager.EVENT_CONNECT_SUCCESSED:
                            netAvailable = true;
                            Log.i(TAG, "onReceive: PppoeManager.EVENT_CONNECT_SUCCESSED");
                            break;
                        case PppoeManager.EVENT_DISCONNECT_FAILED:
                            netAvailable = true;
                            Log.i(TAG, "onReceive: PppoeManager.EVENT_DISCONNECT_FAILED");
                            break;
                        default:
                            netAvailable = false;
                            break;
                    }
                }
            }

            setData();
        }
    };


    private void initView(){
        Log.i(TAG, "initView: ");
        tvMenu = (TextView) findViewById(R.id.net_info);
        tvIP = (TextView) findViewById(R.id.netinfo_tv_ip);
        tvMask = (TextView) findViewById(R.id.netinfo_tv_mask);
        tvGateway = (TextView) findViewById(R.id.netinfo_tv_gateway);
        tvDns1 = (TextView) findViewById(R.id.netinfo_tv_dns1);
        tvDns2 = (TextView) findViewById(R.id.netinfo_tv_dns2);
        tvTitle = (TextView) findViewById(R.id.netinfo_tv_title);

        tvMenu.setFocusable(true);
        tvMenu.setBackgroundResource(R.drawable.menu_focus_selector);
        tvMenu.setOnKeyListener(this);

        netAvailable = MyNetworkUtil.checkNetAvailable(mContext);
        setData();

    }

    private void setData(){

        Log.i(TAG, "setData: ");
        myDhcpInfo = new MyNetworkUtil.MyDhcpInfo(mContext);
        mDhcpInfo = myDhcpInfo.getDhcpInfo();
        mEthManager = myDhcpInfo.getEthernetManager();
        String ethMode = mEthManager.getEthernetMode();
        Log.i(TAG, "setData: 网络是否可用---->" + netAvailable);
        Log.i(TAG, "setData: 当前网络模式---->" + ethMode);
        if (netAvailable && mDhcpInfo != null){
            tvIP.setText(myDhcpInfo.ipAddress);
            tvMask.setText(myDhcpInfo.netMask);
            tvGateway.setText(myDhcpInfo.gateway);
            tvDns1.setText(myDhcpInfo.dns1);
            tvDns2.setText(myDhcpInfo.dns2);
        }else {
            tvIP.setText(myDhcpInfo.getDefaultValue());
            tvMask.setText(myDhcpInfo.getDefaultValue());
            tvGateway.setText(myDhcpInfo.getDefaultValue());
            tvDns1.setText(myDhcpInfo.getDefaultValue());
            tvDns2.setText(myDhcpInfo.getDefaultValue());
        }

        String title = getResources().getString(R.string.netinfo_mode_unknown);
        if (ethMode.equals(EthernetManager.ETHERNET_CONNECT_MODE_PPPOE)){
            title = getResources().getString(R.string.netinfo_mode_pppoe);
        }else if (ethMode.equals(EthernetManager.ETHERNET_CONNECT_MODE_MANUAL)){
            title = getResources().getString(R.string.netinfo_mode_static);
        }else if (ethMode.equals(EthernetManager.ETHERNET_CONNECT_MODE_DHCP)){
            title = getResources().getString(R.string.netinfo_mode_dhcp);
        }

        tvTitle.setText(title);
    }


}
