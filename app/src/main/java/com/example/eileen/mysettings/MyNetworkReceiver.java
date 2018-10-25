package com.example.eileen.mysettings;/*
package com.example.eileen.mysettings;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.DhcpInfo;
import android.net.ethernet.EthernetManager;
import android.widget.TextView;

public class MyNetworkReceiver extends BroadcastReceiver {

    private static final String TAG = "TestBroadcast";
    private TextView mIpAddress;
    private TextView mNetMask;
    private TextView mGateway;
    private TextView mDNS1;
    private TextView mDNS2;
    @Override
    public void onReceive(Context context, Intent intent) {

        Activity activity = (Activity) context;
        mIpAddress = activity.findViewById(R.id.ip_addr);
        mNetMask = activity.findViewById(R.id.net_mask);
        mGateway = activity.findViewById(R.id.gateway);
        mDNS1 = activity.findViewById(R.id.dns1);
        mDNS2 = activity.findViewById(R.id.dns2);


        int message = -1;
        int rel = -1;

        ContentResolver resolver = context.getContentResolver();

        */
/*
         * 广播名称
         * ----ETHERNET_STATE_CHANGED_ACTION == "android.net.ethernet.ETHERNET_STATE_CHANGE"
         * ----NETWORK_STATE_CHANGED_ACTION == "android.net.ethernet.STATE_CHANGE"
         * 广播参数字段
         * ----EXTRA_ETHERNET_STATE == "ethernet_state"
         * ----EXTRA_NETWORK_INFO == "network_info"
         * *//*

        if (intent.getAction().equals(EthernetManager.ETHERNET_STATE_CHANGED_ACTION)){
            message = intent.getIntExtra(EthernetManager.EXTRA_ETHERNET_STATE, rel);
            switch (message){
                case EthernetManager.EVENT_DHCP_CONNECT_SUCCESSED:
                    if (null == mDNS1 || null == mDNS2 || null == mGateway || null == mNetMask){
                        updateUI();
                    }
            }
        }

    }
}
*/
