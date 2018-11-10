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

import com.example.eileen.mysettings.utils.LogUtil;

public class EthernetSettingActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView tvMenu;
    private LinearLayout llPppoe, llDhcp, llStatic;
    private TextView tvPppoeState, tvDhcpState, tvStaticState;

    private LogUtil logUtil = new LogUtil("mynetsetting");

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.ethernet_activity);
        findView();

    }

    @Override
    protected void onResume(){
        super.onResume();
        initView();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }


    @Override
    public void onClick(View v){
        Intent intent;
        switch (v.getId()){
            case R.id.eth_ll_pppoe:
                intent = new Intent(EthernetSettingActivity.this, PPPoEActivity.class);
                startActivity(intent);
                break;
            case R.id.eth_ll_dhcp:
                //这里做另外的点击事件处理，即点击就尝试连接有线

                break;
            case R.id.eth_ll_static:

                logUtil.logi("点击了静态IP，进入设置静态IP界面");
                intent = new Intent(EthernetSettingActivity.this, StaticIpActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void findView(){
        tvMenu = (TextView) findViewById(R.id.net_setting);
        llPppoe = (LinearLayout) findViewById(R.id.eth_ll_pppoe);
        llDhcp = (LinearLayout) findViewById(R.id.eth_ll_dhcp);
        llStatic = (LinearLayout) findViewById(R.id.eth_ll_static);
        tvPppoeState = (TextView) findViewById(R.id.eth_tv_pppoe_connect);
        tvDhcpState = (TextView) findViewById(R.id.eth_tv_dhcp_connect);
        tvStaticState = (TextView) findViewById(R.id.eth_tv_static_connect);

    }

    private void initView(){
        tvMenu.setBackgroundResource(R.drawable.menu_item_select);
        llPppoe.setOnClickListener(this);
        llDhcp.setOnClickListener(this);
        llStatic.setOnClickListener(this);

    }
}
