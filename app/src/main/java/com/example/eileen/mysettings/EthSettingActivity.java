package com.example.eileen.mysettings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Image;
import android.net.ethernet.EthernetManager;
import android.net.pppoe.PppoeManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eileen.mysettings.utils.LogUtil;

public class EthSettingActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView tvMenu;
    private LinearLayout llPppoe, llDhcp, llStatic;
    private TextView tvPppoeState, tvDhcpState, tvStaticState;
    private ImageView imgPppoe, imgDhcp, imgStatic;
    private EthernetManager mEthManager;
//    private PppoeManager mPppoeManager;

    private LogUtil logUtil = new LogUtil("mynetsetting");
    public static final int NO_CONNECT = -1;
    public static final int PPPOE_CONNECT = 1;
    public static final int DHCP_CONNECT = 2;
    public static final int STATIC_CONNECT = 3;
    public static final int DISCONNECT_SUCCESS = 4;
    public static final int CONNECT_FAILED = 5;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        logUtil.logi("EthSettingActivity---->onCreate()");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.ethernet_activity);
        mEthManager = (EthernetManager) getSystemService(Context.ETHERNET_SERVICE);
//        mPppoeManager = (PppoeManager) getSystemService(Context.PPPOE_SERVICE);

        IntentFilter filter = new IntentFilter();
        filter.addAction(EthernetManager.ETHERNET_STATE_CHANGED_ACTION);
        filter.addAction(PppoeManager.PPPOE_STATE_CHANGED_ACTION);
        registerReceiver(myReceiver, filter);
        findView();

    }

    @Override
    protected void onResume(){
        logUtil.logi("EthSettingActivity---->onResume()");
        super.onResume();
        initView();
    }

    @Override
    protected void onDestroy(){
        logUtil.logi("EthSettingActivity---->onDestroy()");

        super.onDestroy();
        unregisterReceiver(myReceiver);
        mHandler.removeCallbacksAndMessages(null);
    }


    @Override
    public void onClick(View v){
        logUtil.logi("EthSettingActivity---->onClick()");

        Intent intent;
        switch (v.getId()){
            case R.id.eth_ll_pppoe:
                logUtil.logi("点击PPPoE,尝试进入PPPoE设置界面");
                intent = new Intent(EthSettingActivity.this, PPPoEActivity.class);
                startActivity(intent);
                break;
            case R.id.eth_ll_dhcp:
                logUtil.logi("点击DHCP, 尝试连接网络");
                setDhcp();
                break;
            case R.id.eth_ll_static:
                logUtil.logi("点击静态IP，尝试进入静态IP设置界面");
                intent = new Intent(EthSettingActivity.this, StaticIpActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void findView(){
        logUtil.logi("EthSettingActivity---->findView()");

        tvMenu = (TextView) findViewById(R.id.net_setting);
        llPppoe = (LinearLayout) findViewById(R.id.eth_ll_pppoe);
        llDhcp = (LinearLayout) findViewById(R.id.eth_ll_dhcp);
        llStatic = (LinearLayout) findViewById(R.id.eth_ll_static);
        tvPppoeState = (TextView) findViewById(R.id.eth_tv_pppoe_connect);
        tvDhcpState = (TextView) findViewById(R.id.eth_tv_dhcp_connect);
        tvStaticState = (TextView) findViewById(R.id.eth_tv_static_connect);
        imgPppoe = (ImageView) findViewById(R.id.eth_img_pppoe);
        imgDhcp = (ImageView) findViewById(R.id.eth_img_dhcp);
        imgStatic = (ImageView) findViewById(R.id.eth_img_static);

    }

    private void initView(){
        logUtil.logi("EthSettingActivity---->initView()");

        tvMenu.setBackgroundResource(R.drawable.menu_item_select);
        llPppoe.setOnClickListener(this);
        llDhcp.setOnClickListener(this);
        llStatic.setOnClickListener(this);

    }



    public void setDhcp(){
        logUtil.logi("EthSettingActivity---->setDhcp()");

        mEthManager.setEthernetEnabled(false);
        mEthManager.setEthernetMode(EthernetManager.ETHERNET_CONNECT_MODE_DHCP, null);
        mEthManager.setEthernetEnabled(true);
    }

    private BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            logUtil.logi("EthSettingActivity---->onReceive()");

            String action = intent.getAction();
            logUtil.logi("onReceive()---->广播为：" + action);

            int ethEvent = intent.getIntExtra(EthernetManager.EXTRA_ETHERNET_STATE, -1);
            logUtil.logi("onReceive()---->网络事件代码：" + ethEvent);
            if (ethEvent == EthernetManager.EVENT_DHCP_CONNECT_SUCCESSED){
                logUtil.logi("连网方式---->DHCP");

                changeState(DHCP_CONNECT);
                Message msg = new Message();
                msg.what = DHCP_CONNECT;
                mHandler.sendMessage(msg);
            }else if (ethEvent == EthernetManager.EVENT_STATIC_CONNECT_SUCCESSED){
                logUtil.logi("连网方式---->Static");
                changeState(STATIC_CONNECT);
                Message msg = new Message();
                msg.what = STATIC_CONNECT;
                mHandler.sendMessage(msg);

            }else if (ethEvent == PppoeManager.EVENT_CONNECT_SUCCESSED) {
                logUtil.logi("连网方式---->PPPOE");
                changeState(PPPOE_CONNECT);
                Message msg = new Message();
                msg.what = PPPOE_CONNECT;
                mHandler.sendMessage(msg);
            }else if (ethEvent == EthernetManager.EVENT_DHCP_DISCONNECT_SUCCESSED
                    || ethEvent == EthernetManager.EVENT_STATIC_DISCONNECT_SUCCESSED
                    || ethEvent == PppoeManager.EVENT_DISCONNECT_SUCCESSED){

                logUtil.logi("网络连接已断开");

                changeState(NO_CONNECT);
                Message msg = new Message();
                msg.what = DISCONNECT_SUCCESS;
                mHandler.sendMessage(msg);

            }else if (ethEvent == EthernetManager.EVENT_DHCP_CONNECT_FAILED
                    || ethEvent == EthernetManager.EVENT_STATIC_CONNECT_FAILED
                    || ethEvent == PppoeManager.EVENT_CONNECT_FAILED) {

                logUtil.logi("网络连接失败");

                changeState(NO_CONNECT);
                Message msg = new Message();
                msg.what = CONNECT_FAILED;
                mHandler.sendMessage(msg);
            }

        }
    };
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            logUtil.logi("EthSettingActivity---->handleMessage()");

            switch (msg.what){
                case DISCONNECT_SUCCESS:
                    Toast.makeText(EthSettingActivity.this,
                            "网络连接已断开",
                            Toast.LENGTH_SHORT).show();
                    break;
                case CONNECT_FAILED:
                    Toast.makeText(EthSettingActivity.this,
                            "连接网络失败",
                            Toast.LENGTH_SHORT).show();
                    break;
                case DHCP_CONNECT:
                    Toast.makeText(EthSettingActivity.this,
                            "DHCP已连接",
                            Toast.LENGTH_SHORT).show();
                    break;
                case PPPOE_CONNECT:
                    Toast.makeText(EthSettingActivity.this,
                            "PPPOE已连接",
                            Toast.LENGTH_SHORT).show();
                    break;
                case STATIC_CONNECT:
                    Toast.makeText(EthSettingActivity.this,
                            "静态IP已连接",
                            Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    public void changeState(int flag){
        logUtil.logi("EthSettingActivity---->changeState()");

        if (flag == NO_CONNECT){
            logUtil.logi("flag---->NO_CONNECT");
            tvPppoeState.setVisibility(View.INVISIBLE);
            tvDhcpState.setVisibility(View.INVISIBLE);
            tvStaticState.setVisibility(View.INVISIBLE);
            imgPppoe.setImageResource(R.drawable.radio_unchecked_normal);
            imgDhcp.setImageResource(R.drawable.radio_unchecked_normal);
            imgStatic.setImageResource(R.drawable.radio_unchecked_normal);
        }else if (flag == PPPOE_CONNECT){
            logUtil.logi("flag---->PPPOE_CONNECT");
            tvPppoeState.setVisibility(View.VISIBLE);
            tvDhcpState.setVisibility(View.INVISIBLE);
            tvStaticState.setVisibility(View.INVISIBLE);
            imgPppoe.setImageResource(R.drawable.radio_checked_normal);
            imgDhcp.setImageResource(R.drawable.radio_unchecked_normal);
            imgStatic.setImageResource(R.drawable.radio_unchecked_normal);
        }else if (flag == DHCP_CONNECT){
            logUtil.logi("flag---->DHCP_CONNECT");
            tvPppoeState.setVisibility(View.INVISIBLE);
            tvDhcpState.setVisibility(View.VISIBLE);
            tvStaticState.setVisibility(View.INVISIBLE);
            imgPppoe.setImageResource(R.drawable.radio_unchecked_normal);
            imgDhcp.setImageResource(R.drawable.radio_checked_normal);
            imgStatic.setImageResource(R.drawable.radio_unchecked_normal);
        }else if (flag == STATIC_CONNECT){
            logUtil.logi("flag---->STATIC_CONNECT");
            tvPppoeState.setVisibility(View.INVISIBLE);
            tvDhcpState.setVisibility(View.INVISIBLE);
            tvStaticState.setVisibility(View.VISIBLE);
            imgPppoe.setImageResource(R.drawable.radio_unchecked_normal);
            imgDhcp.setImageResource(R.drawable.radio_unchecked_normal);
            imgStatic.setImageResource(R.drawable.radio_checked_normal);
        }

    }
}
