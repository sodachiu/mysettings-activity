package com.example.eileen.mysettings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.ethernet.EthernetManager;
import android.net.pppoe.PppoeManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eileen.mysettings.network.MyNetworkUtil;
import com.example.eileen.mysettings.utils.LogUtil;

public class EthSettingActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "mynetsetting";
    private TextView tvMenu;
    private LinearLayout llPppoe, llDhcp, llStatic;
    private TextView tvPppoeState, tvDhcpState, tvStaticState;
    private ImageView imgPppoe, imgDhcp, imgStatic;
    private EthernetManager mEthManager;
    private Context mContext = null;
    private boolean isNetAvailable = false;

    public static final int DHCP_CONNECT_SUCCESS = 1;
    public static final int DHCP_CONNECT_FAILED = 2;
    public static final int DHCP_DISCONNECT_FAILED = 3;
    public static final int DHCP_DISCONNECT_SUCCESS = 11;
    public static final int DHCP_ALREADY_CONNECT = 4;
    public static final int PHY_LINK_DOWN = 5;
    public static final int PHY_LINK_UP = 6;
    public static final int NO_PHY_LINK = 7;
    public static final int STATIC_CONNECT_SUCCESS = 8;
    public static final int PPPOE_CONNECT_SUCCESS = 9;
    public static final int DHCP_CONNECTING = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ethernet_activity);
        mEthManager = (EthernetManager) getSystemService(Context.ETHERNET_SERVICE);
        mContext = getApplicationContext();
        IntentFilter filter = new IntentFilter();
        filter.addAction(PppoeManager.PPPOE_STATE_CHANGED_ACTION);
        filter.addAction(EthernetManager.ETHERNET_STATE_CHANGED_ACTION);
        registerReceiver(myReceiver, filter);

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
        isNetAvailable = MyNetworkUtil.checkNetAvailable(mContext);
        super.onDestroy();
        unregisterReceiver(myReceiver);
        mHandler.removeCallbacksAndMessages(null);
    }


    @Override
    public void onClick(View v){
        Log.i(TAG, "onClick: ");
        Intent intent;
        switch (v.getId()){
            case R.id.eth_ll_pppoe:
                Log.i(TAG, "onClick: 点击pppoe，尝试进入pppoe界面");
                intent = new Intent(EthSettingActivity.this, PPPoEActivity.class);
                startActivity(intent);
                break;
            case R.id.eth_ll_dhcp:
                Log.i(TAG, "onClick: 正在尝试dhcp连接");
                setDhcp();
                break;
            case R.id.eth_ll_static:
                Log.i(TAG, "onClick: 点击静态IP,尝试进入静态IP设置界面");
                intent = new Intent(EthSettingActivity.this, StaticIpActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void initView(){
        Log.i(TAG, "initView: ");
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

        tvMenu.setBackgroundResource(R.drawable.menu_item_select);
        llPppoe.setOnClickListener(this);
        llDhcp.setOnClickListener(this);
        llStatic.setOnClickListener(this);

        changeState();

    }




    public void setDhcp(){

        if (!mEthManager.getNetLinkStatus()){
            mHandler.sendEmptyMessage(NO_PHY_LINK);
            Log.i(TAG, "setDhcp: 没有检测到网线连接");
            return;
        }
        if (mEthManager.getEthernetMode().equals(EthernetManager.ETHERNET_CONNECT_MODE_DHCP)
                && MyNetworkUtil.checkNetAvailable(mContext)){

            mHandler.sendEmptyMessage(DHCP_ALREADY_CONNECT);
            Log.i(TAG, "setDhcpa: dhcp已经连接，不要重复尝试连接dhcp");
            return;
        }
        mHandler.sendEmptyMessage(DHCP_CONNECTING);
        Log.i(TAG, "setDhcp: ");
        mEthManager.setEthernetMode(EthernetManager.ETHERNET_CONNECT_MODE_DHCP, null);
        mEthManager.setEthernetEnabled(false);
        mEthManager.setEthernetEnabled(true);
    }

    private BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "onReceive: ");

            String action = intent.getAction();
            String ethMode = mEthManager.getEthernetMode();
            Log.i(TAG, "onReceive: 广播为---->" + action);
            Log.i(TAG, "onReceive: 当前网络连接模式---->" + ethMode);

            if (action.equals(EthernetManager.ETHERNET_STATE_CHANGED_ACTION)){

                int ethEvent = intent.getIntExtra(EthernetManager.EXTRA_ETHERNET_STATE, -1);
                Log.i(TAG, "onReceive: 网络事件代码---->" + ethEvent);

                if (ethMode.equals(EthernetManager.ETHERNET_CONNECT_MODE_DHCP)){
                    switch (ethEvent){
                        case EthernetManager.EVENT_DHCP_CONNECT_SUCCESSED:
                            isNetAvailable = true;
                            mHandler.sendEmptyMessage(DHCP_CONNECT_SUCCESS);
                            Log.i(TAG, "onReceive: DHCP连接成功");
                            break;
                        case EthernetManager.EVENT_DHCP_CONNECT_FAILED:
                            isNetAvailable = false;
                            mHandler.sendEmptyMessage(DHCP_CONNECT_FAILED);
                            Log.i(TAG, "onReceive: DHCP连接失败");
                            break;
                        case EthernetManager.EVENT_DHCP_DISCONNECT_FAILED:
                            isNetAvailable = true;
                            mHandler.sendEmptyMessage(DHCP_DISCONNECT_FAILED);
                            Log.i(TAG, "onReceive: DHCP断开失败");
                            break;
                        case EthernetManager.EVENT_DHCP_DISCONNECT_SUCCESSED:
                            isNetAvailable = false;
                            mHandler.sendEmptyMessage(DHCP_DISCONNECT_SUCCESS);
                            Log.i(TAG, "onReceive: DHCP断开成功");
                            break;
                    }
                }else if (ethMode.equals(EthernetManager.ETHERNET_CONNECT_MODE_MANUAL)){
                    switch (ethEvent){
                        case EthernetManager.EVENT_STATIC_CONNECT_SUCCESSED:
                            isNetAvailable = true;
                            mHandler.sendEmptyMessage(STATIC_CONNECT_SUCCESS);
                            Log.i(TAG, "onReceive: 静态IP连接成功");
                            break;
                    }
                }else {
                    switch (ethEvent){
                        case EthernetManager.EVENT_PHY_LINK_DOWN:
                            isNetAvailable = false;
                            mHandler.sendEmptyMessage(PHY_LINK_DOWN);
                            Log.i(TAG, "onReceive: 网线拔出");
                            break;
                        case EthernetManager.EVENT_PHY_LINK_UP:
                            isNetAvailable = true;
                            mHandler.sendEmptyMessage(PHY_LINK_UP);
                            Log.i(TAG, "onReceive: 网线插入");
                            break;
                    }
                }
            } else if (action.equals(PppoeManager.PPPOE_STATE_CHANGED_ACTION)){

                int pppoeEvent = intent.getIntExtra(PppoeManager.EXTRA_PPPOE_STATE, -1);
                if (pppoeEvent == PppoeManager.EVENT_CONNECT_SUCCESSED){

                    if (ethMode.equals(EthernetManager.ETHERNET_CONNECT_MODE_PPPOE)){
                        isNetAvailable = true;
                        mHandler.sendEmptyMessage(PPPOE_CONNECT_SUCCESS);
                        Log.i(TAG, "onReceive: pppoe连接成功");
                    }

                }
            }

            changeState();

        }
    };

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            Log.i(TAG, "handleMessage: ");
            switch (msg.what){
                case DHCP_CONNECT_SUCCESS :
                    Toast.makeText(EthSettingActivity.this,
                            "DHCP连接成功",
                            Toast.LENGTH_SHORT).show();
                    break;
                case DHCP_CONNECT_FAILED :
                    Toast.makeText(EthSettingActivity.this,
                            "DHCP连接失败",
                            Toast.LENGTH_SHORT).show();
                    break;
                case DHCP_ALREADY_CONNECT :
                    Toast.makeText(EthSettingActivity.this,
                            "DHCP已经连接",
                            Toast.LENGTH_SHORT).show();
                    break;
                case PHY_LINK_DOWN :
                    Toast.makeText(EthSettingActivity.this,
                            "网线拔出",
                            Toast.LENGTH_SHORT).show();
                    break;
                case PHY_LINK_UP :
                    Toast.makeText(EthSettingActivity.this,
                            "网线插入",
                            Toast.LENGTH_SHORT).show();
                    break;
                case NO_PHY_LINK :
                    Toast.makeText(EthSettingActivity.this,
                            "请检查网线是否连接",
                            Toast.LENGTH_SHORT).show();
                    break;
                case STATIC_CONNECT_SUCCESS :
                    Toast.makeText(EthSettingActivity.this,
                            "静态IP连接成功",
                            Toast.LENGTH_SHORT).show();
                    break;
                case PPPOE_CONNECT_SUCCESS :
                    Toast.makeText(EthSettingActivity.this,
                            "PPPOE连接成功",
                            Toast.LENGTH_SHORT).show();
                    break;
                case DHCP_CONNECTING :
                    Toast.makeText(EthSettingActivity.this,
                            "正在连接请稍后...",
                            Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    public void changeState(){
        Log.i(TAG, "changeState: ");
        Log.i(TAG, "changeState: 网络是否可用---->" + isNetAvailable);
        if (isNetAvailable){
            String ethMode = mEthManager.getEthernetMode();
            Log.i(TAG, "changeState: 网络当前模式---->" + ethMode);
            if (ethMode.equals(EthernetManager.ETHERNET_CONNECT_MODE_PPPOE)){
                tvPppoeState.setVisibility(View.VISIBLE);
                tvDhcpState.setVisibility(View.INVISIBLE);
                tvStaticState.setVisibility(View.INVISIBLE);
                imgPppoe.setImageResource(R.drawable.radio_checked_normal);
                imgDhcp.setImageResource(R.drawable.radio_unchecked_normal);
                imgStatic.setImageResource(R.drawable.radio_unchecked_normal);
                llPppoe.requestFocus();
            }else if (ethMode.equals(EthernetManager.ETHERNET_CONNECT_MODE_MANUAL)){

                tvPppoeState.setVisibility(View.INVISIBLE);
                tvDhcpState.setVisibility(View.INVISIBLE);
                tvStaticState.setVisibility(View.VISIBLE);
                imgPppoe.setImageResource(R.drawable.radio_unchecked_normal);
                imgDhcp.setImageResource(R.drawable.radio_unchecked_normal);
                imgStatic.setImageResource(R.drawable.radio_checked_normal);
                llStatic.requestFocus();
            }else if (ethMode.equals(EthernetManager.ETHERNET_CONNECT_MODE_DHCP)){
                tvPppoeState.setVisibility(View.INVISIBLE);
                tvDhcpState.setVisibility(View.VISIBLE);
                tvStaticState.setVisibility(View.INVISIBLE);
                imgPppoe.setImageResource(R.drawable.radio_unchecked_normal);
                imgDhcp.setImageResource(R.drawable.radio_checked_normal);
                imgStatic.setImageResource(R.drawable.radio_unchecked_normal);
                llDhcp.requestFocus();

            }
        }else {
            Log.i(TAG, "changeState: 网络不可用");
            tvPppoeState.setVisibility(View.INVISIBLE);
            tvDhcpState.setVisibility(View.INVISIBLE);
            tvStaticState.setVisibility(View.INVISIBLE);
            imgPppoe.setImageResource(R.drawable.radio_unchecked_normal);
            imgDhcp.setImageResource(R.drawable.radio_unchecked_normal);
            imgStatic.setImageResource(R.drawable.radio_unchecked_normal);
        }

    }

}
