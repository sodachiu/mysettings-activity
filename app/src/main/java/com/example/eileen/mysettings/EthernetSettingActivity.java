package com.example.eileen.mysettings;

import android.content.Context;
import android.content.Intent;
import android.net.ethernet.EthernetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class EthernetSettingActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "EthernetSettingActivity";
    private TextView tvNetSetting;
    private LinearLayout llPppoe;
    private LinearLayout llDhcp;
    private LinearLayout llStaticIP;
    private TextView tvDhcpState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.ethernet_activity);
        tvNetSetting = (TextView) findViewById(R.id.net_setting);
        llPppoe = (LinearLayout) findViewById(R.id.pppoe_container);
        llDhcp = (LinearLayout) findViewById(R.id.dhcp_container);
        llStaticIP = (LinearLayout) findViewById(R.id.static_ip_container);
        tvDhcpState = (TextView) findViewById(R.id.dhcp_state);

        tvNetSetting.setBackgroundResource(R.drawable.menu_item_select);
        llPppoe.setOnClickListener(this);
        llDhcp.setOnClickListener(this);
        llStaticIP.setOnClickListener(this);
    }

    public void onClick(View v){
        Intent intent;
        switch (v.getId()){
            case R.id.pppoe_container:
                intent = new Intent(EthernetSettingActivity.this, PPPoEActivity.class);
                startActivity(intent);
                break;
            case R.id.dhcp_container:
                //这里做另外的点击事件处理，即点击就尝试连接有线
                Context context  = getApplicationContext();
                EthernetManager ethernetManager = (EthernetManager)context.getSystemService(
                        Context.ETHERNET_SERVICE);
                if (ethernetManager.getEthernetState() == EthernetManager.ETHERNET_STATE_ENABLED
                        &&
                        ethernetManager.isEthernetConfigured()){
                    tvDhcpState.setText(R.string.connected);

                }else {
                    tvDhcpState.setText("");
                }


                Log.d(TAG, "onClick: " + ethernetManager.isEthernetConfigured());
                break;
            case R.id.static_ip_container:
                intent = new Intent(EthernetSettingActivity.this, StaticIpActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
