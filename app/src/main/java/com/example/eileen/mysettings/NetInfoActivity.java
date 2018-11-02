package com.example.eileen.mysettings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.DhcpInfo;
import android.net.NetworkUtils;
import android.net.ethernet.EthernetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.eileen.mysettings.utils.ActivityCollector;
import com.example.eileen.mysettings.utils.LogUtil;
import com.example.eileen.mysettings.utils.QuitActivity;

public class NetInfoActivity extends QuitActivity implements View.OnKeyListener{

    private static final String TAG = "NetInfoActivity";
    private TextView netInfo;
    private TextView ipAddress;
    private TextView netMask;
    private TextView gateway;
    private EditText dns1;
    private EditText dns2;
    private NetworkChangeReceiver networkChangeReceiver;
    private LogUtil logUtil = new LogUtil("mynetinfo");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.net_info_activity);

        netInfo = (TextView) findViewById(R.id.net_info);
        ipAddress = (TextView) findViewById(R.id.ip_addr);
        netMask = (TextView) findViewById(R.id.net_mask);
        gateway = (TextView) findViewById(R.id.gateway);
        dns1 = (EditText) findViewById(R.id.dns1);
        dns2 = (EditText) findViewById(R.id.dns2);

        dns1.setSelectAllOnFocus(true);
        dns2.setSelectAllOnFocus(true);
        netInfo.setFocusable(true);
        netInfo.setBackgroundResource(R.drawable.menu_focus_selector);
        netInfo.setOnKeyListener(this);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(EthernetManager.ETHERNET_STATE_CHANGED_ACTION);
        intentFilter.addAction(EthernetManager.NETWORK_STATE_CHANGED_ACTION);
        networkChangeReceiver = new NetworkChangeReceiver();
        registerReceiver(networkChangeReceiver, intentFilter);

    }



    @Override
    protected void onResume(){
        super.onResume();
        dns1.setFocusable(true);
        dns2.setFocusable(true);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        unregisterReceiver(networkChangeReceiver);
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event){

        if (event.getAction() == KeyEvent.ACTION_DOWN){
            Intent intent;
            switch (keyCode){
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    dns1.setFocusable(false);
                    dns2.setFocusable(false);
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

    class NetworkChangeReceiver extends BroadcastReceiver{
        private DhcpInfo dhcpInfo;
        private String mIpAddress;
        private String mNetMask;
        private String mGateway;
        private String mDns1;
        private String mDns2;
        @Override
        public void onReceive(Context context, Intent intent) {
            EthernetManager ethManager = (EthernetManager)context.getSystemService(
                    Context.ETHERNET_SERVICE);

            int ethStatus = intent.getIntExtra(EthernetManager.EXTRA_ETHERNET_STATE, -1);
            Log.d(TAG, "onReceive: ");
//            Log.d(TAG, "onReceive: " + ethManager.getNetLinkStatus());
            if (EthernetManager.EVENT_DHCP_CONNECT_SUCCESSED == ethStatus){

                dhcpInfo = ethManager.getDhcpInfo();
                mIpAddress = NetworkUtils.intToInetAddress(dhcpInfo.ipAddress).getHostAddress();
                mNetMask = NetworkUtils.intToInetAddress(dhcpInfo.netmask).getHostAddress();
                mGateway = NetworkUtils.intToInetAddress(dhcpInfo.gateway).getHostAddress();
                mDns1 = NetworkUtils.intToInetAddress(dhcpInfo.dns1).getHostAddress();
                mDns2 = NetworkUtils.intToInetAddress(dhcpInfo.dns2).getHostAddress();

                ipAddress.setText(mIpAddress);
                netMask.setText(mNetMask);
                gateway.setText(mGateway);
                dns1.setText(mDns1);
                dns2.setText(mDns2);
            }else {
                ipAddress.setText(R.string.net_default_text);
                netMask.setText(R.string.net_default_text);
                gateway.setText(R.string.net_default_text);
                dns1.setText(R.string.net_default_text);
                dns2.setText(R.string.net_default_text);
            }
        }
    }
}
