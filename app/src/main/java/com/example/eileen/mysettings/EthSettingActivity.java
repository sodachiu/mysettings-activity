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
    private TextView tvMenu;
    private LinearLayout llPppoe, llDhcp, llStatic;
    private TextView tvPppoeState, tvDhcpState, tvStaticState;
    private ImageView imgPppoe, imgDhcp, imgStatic;
    private EthernetManager mEthManager;
    private PppoeManager mPppoeManager;
    private Context mContext = null;
    private LogUtil logUtil = new LogUtil("mynetsettings");

    public static final int NO_CONNECT = -1;
    public static final int DHCP_CONNECT_SUCCESS = 1;
    public static final int DHCP_CONNECT_FAILED = 2;
    public static final int DHCP_DISCONNECT_FAILED = 3;
    public static final int DHCP_DISCONNECT_SUCCESS = 4;
    public static final int STATIC_CONNECT_SUCCESS = 5;
    public static final int STATIC_CONNECT_FAILED = 6;
    public static final int STATIC_DISCONNECT_SUCCESS = 7;
    public static final int STATIC_DISCONNECT_FAILED = 8;
    public static final int PPPOE_CONNECT_FAILED = 9;
    public static final int PPPOE_CONNECT_SUCCESS = 10;
    public static final int PPPOE_DISCONNECT_SUCCESS = 11;
    public static final int PPPOE_DISCONNECT_FAILED = 12;
    public static final int PPPOE_AUTO_RECONNECT = 13;
    public static final int PPPOE_CONNECTING = 14;
    public static final int DHCP_ALREADY_CONNECT = 15;
    public static final int PHY_LINK_DOWN = 16;
    public static final int PHY_LINK_UP = 17;
    public static final int NO_PHY_LINK = 18;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        logUtil.logi("EthSettingActivity---->onCreate()");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.ethernet_activity);
        mEthManager = (EthernetManager) getSystemService(Context.ETHERNET_SERVICE);
        mPppoeManager = (PppoeManager) getSystemService(Context.PPPOE_SERVICE);
        mContext = getApplicationContext();
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
                if (!mEthManager.getNetLinkStatus()){
                    mHandler.sendEmptyMessage(NO_PHY_LINK);
                    logUtil.logi("没有检测到网线连接");
                    return;
                }
                if (mEthManager.getEthernetMode().equals(EthernetManager.ETHERNET_CONNECT_MODE_DHCP)
                        && MyNetworkUtil.checkNetAvailable(mContext)){

                    mHandler.sendEmptyMessage(DHCP_ALREADY_CONNECT);
                    logUtil.logi("dhcp已连接");
                    return;
                }
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

        String ethMode = mEthManager.getEthernetMode();
        logUtil.logi("当前网络模式---->" + mEthManager.getEthernetMode());
        if (!ethMode.equals(EthernetManager.ETHERNET_CONNECT_MODE_PPPOE)){
            String ifName = mEthManager.getInterfaceName();
            mPppoeManager.disconnect(ifName);
        }
    }



    public void setDhcp(){
        logUtil.logi("EthSettingActivity---->setDhcp()");
        mEthManager.setEthernetMode(EthernetManager.ETHERNET_CONNECT_MODE_DHCP, null);
        mEthManager.setEthernetEnabled(false);
        mEthManager.setEthernetEnabled(true);
    }

    private BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            logUtil.logi("EthSettingActivity---->onReceive()");

            String action = intent.getAction();
            logUtil.logi("onReceive()---->广播为：" + action);

            if (action.equals(EthernetManager.ETHERNET_STATE_CHANGED_ACTION)){

                int ethEvent = intent.getIntExtra(EthernetManager.EXTRA_ETHERNET_STATE, -1);
                logUtil.logi("onReceive()---->网络事件代码：" + ethEvent);

                if (ethEvent == EthernetManager.EVENT_DHCP_CONNECT_SUCCESSED){

                    logUtil.logi("DHCP连接成功");

                }else if (ethEvent == EthernetManager.EVENT_STATIC_CONNECT_SUCCESSED){

                    logUtil.logi("静态IP连接成功");

                } else if (ethEvent == EthernetManager.EVENT_DHCP_DISCONNECT_SUCCESSED){

                    mHandler.sendEmptyMessage(DHCP_DISCONNECT_SUCCESS);
                    logUtil.logi("DHCP连接已断开");

                }else if (ethEvent == EthernetManager.EVENT_STATIC_DISCONNECT_SUCCESSED){

                    mHandler.sendEmptyMessage(STATIC_DISCONNECT_SUCCESS);
                    logUtil.logi("静态IP连接已断开");

                } else if (ethEvent == EthernetManager.EVENT_DHCP_CONNECT_FAILED) {
                    mHandler.sendEmptyMessage(DHCP_CONNECT_FAILED);
                    logUtil.logi("DHCP连接失败");

                }else if (ethEvent == EthernetManager.EVENT_STATIC_CONNECT_FAILED){

                    mHandler.sendEmptyMessage(STATIC_CONNECT_FAILED);
                    logUtil.logi("静态IP连接失败");

                }else if (ethEvent == EthernetManager.EVENT_DHCP_DISCONNECT_FAILED){

                    mHandler.sendEmptyMessage(DHCP_DISCONNECT_FAILED);
                    logUtil.logi("DHCP断开失败");

                }else if (ethEvent == EthernetManager.EVENT_STATIC_DISCONNECT_FAILED){

                    mHandler.sendEmptyMessage(STATIC_DISCONNECT_FAILED);
                    logUtil.logi("静态IP断开失败");

                }else if (ethEvent == EthernetManager.EVENT_PHY_LINK_DOWN){
                    mHandler.sendEmptyMessage(PHY_LINK_DOWN);
                    logUtil.logi("网线拔出");
                }else if (ethEvent == EthernetManager.EVENT_PHY_LINK_UP){
                    mHandler.sendEmptyMessage(PHY_LINK_UP);
                    logUtil.logi("网线插入");
                }
            }else if (action.equals(PppoeManager.PPPOE_STATE_CHANGED_ACTION)){

                int pppoeEvent = intent.getIntExtra(PppoeManager.EXTRA_PPPOE_STATE, -1);
                logUtil.logi("PPPOE事件---->" + pppoeEvent);

                if (pppoeEvent == PppoeManager.EVENT_CONNECT_FAILED){

                    mHandler.sendEmptyMessage(PPPOE_CONNECT_FAILED);
                    String errorMsg = intent.getStringExtra(PppoeManager.EXTRA_PPPOE_ERRMSG);
                    if (errorMsg == null){
                        errorMsg = "999, PPPOE 拨号错误";
                    }
                    logUtil.logi("ppppoe 连接失败---->" + errorMsg);

                }else if (pppoeEvent == PppoeManager.EVENT_CONNECTING){

                    mHandler.sendEmptyMessage(PPPOE_CONNECTING);
                    logUtil.logi("ppppoe 正在连接");

                }else if (pppoeEvent == PppoeManager.EVENT_AUTORECONNECTING){

                    mHandler.sendEmptyMessage(PPPOE_AUTO_RECONNECT);
                    logUtil.logi("pppoe尝试重连");

                }else if (pppoeEvent == PppoeManager.EVENT_CONNECT_SUCCESSED){

                    logUtil.logi("pppoe 连接成功");

                }else if (pppoeEvent == PppoeManager.EVENT_DISCONNECT_SUCCESSED){

                    mHandler.sendEmptyMessage(PPPOE_DISCONNECT_SUCCESS);
                    logUtil.logi("PPPoE连接已断开");

                }else if (pppoeEvent == PppoeManager.EVENT_DISCONNECT_FAILED){

                    mHandler.sendEmptyMessage(PPPOE_DISCONNECT_SUCCESS);
                    logUtil.logi("PPPoE断开失败");

                }
            }
            changeState();

        }
    };

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            logUtil.logi("EthSettingActivity---->handleMessage()");

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
                case DHCP_DISCONNECT_FAILED :
                    Toast.makeText(EthSettingActivity.this,
                            "DHCP断开连接失败",
                            Toast.LENGTH_SHORT).show();
                    break;
                /*case DHCP_DISCONNECT_SUCCESS :
                    Toast.makeText(EthSettingActivity.this,
                            "DHCP连接已断开",
                            Toast.LENGTH_SHORT).show();
                    break;*/
                case STATIC_CONNECT_SUCCESS :
                    Toast.makeText(EthSettingActivity.this,
                            "静态IP连接成功",
                            Toast.LENGTH_SHORT).show();
                    break;
                case STATIC_CONNECT_FAILED :
                    Toast.makeText(EthSettingActivity.this,
                            "静态IP连接失败",
                            Toast.LENGTH_SHORT).show();
                    break;
                /*case STATIC_DISCONNECT_SUCCESS :
                    Toast.makeText(EthSettingActivity.this,
                            "静态IP连接已断开",
                            Toast.LENGTH_SHORT).show();
                    break;*/
                case STATIC_DISCONNECT_FAILED :
                    Toast.makeText(EthSettingActivity.this,
                            "静态IP断开连接失败",
                            Toast.LENGTH_SHORT).show();
                    break;
                case PPPOE_CONNECT_FAILED :
                    Toast.makeText(EthSettingActivity.this,
                            "PPPoE连接失败",
                            Toast.LENGTH_SHORT).show();
                    break;
                case PPPOE_CONNECT_SUCCESS :
                    Toast.makeText(EthSettingActivity.this,
                            "PPPoE连接成功",
                            Toast.LENGTH_SHORT).show();
                    break;
                /*case PPPOE_DISCONNECT_SUCCESS :
                    Toast.makeText(EthSettingActivity.this,
                            "PPPoE连接已断开",
                            Toast.LENGTH_SHORT).show();
                    break;*/
                case PPPOE_DISCONNECT_FAILED :
                    Toast.makeText(EthSettingActivity.this,
                            "PPPoE断开连接失败",
                            Toast.LENGTH_SHORT).show();
                    break;
                case PPPOE_AUTO_RECONNECT :
                    Toast.makeText(EthSettingActivity.this,
                            "PPPoE正在尝试重连...",
                            Toast.LENGTH_SHORT).show();
                    break;
                case PPPOE_CONNECTING :
                    Toast.makeText(EthSettingActivity.this,
                            "PPPoE正在连接...",
                            Toast.LENGTH_SHORT).show();
                    break;
                case DHCP_ALREADY_CONNECT :
                    Toast.makeText(EthSettingActivity.this,
                            "DHCP已经连接",
                            Toast.LENGTH_SHORT).show();
                    break;
                /*case PHY_LINK_DOWN :
                    Toast.makeText(EthSettingActivity.this,
                            "网线拔出",
                            Toast.LENGTH_SHORT).show();
                    break;
                case PHY_LINK_UP :
                    Toast.makeText(EthSettingActivity.this,
                            "网线插入",
                            Toast.LENGTH_SHORT).show();
                    break;*/
                case NO_PHY_LINK :
                    Toast.makeText(EthSettingActivity.this,
                            "请检查网线是否插入",
                            Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    public void changeState(){
        logUtil.logi("EthSettingActivity---->changeState()");
        boolean netAvailable = MyNetworkUtil.checkNetAvailable(mContext);
        String ethMode = mEthManager.getEthernetMode();
        if (!netAvailable){
            logUtil.logi("网络不可用");
            tvPppoeState.setVisibility(View.INVISIBLE);
            tvDhcpState.setVisibility(View.INVISIBLE);
            tvStaticState.setVisibility(View.INVISIBLE);
            imgPppoe.setImageResource(R.drawable.radio_unchecked_normal);
            imgDhcp.setImageResource(R.drawable.radio_unchecked_normal);
            imgStatic.setImageResource(R.drawable.radio_unchecked_normal);
        }else if (ethMode.equals(EthernetManager.ETHERNET_CONNECT_MODE_PPPOE)){
            logUtil.logi("flag---->PPPOE_CONNECT");
            mHandler.sendEmptyMessage(PPPOE_CONNECT_SUCCESS);
            tvPppoeState.setVisibility(View.VISIBLE);
            tvDhcpState.setVisibility(View.INVISIBLE);
            tvStaticState.setVisibility(View.INVISIBLE);
            imgPppoe.setImageResource(R.drawable.radio_checked_normal);
            imgDhcp.setImageResource(R.drawable.radio_unchecked_normal);
            imgStatic.setImageResource(R.drawable.radio_unchecked_normal);
        }else if (ethMode.equals(EthernetManager.ETHERNET_CONNECT_MODE_DHCP)){
            logUtil.logi("flag---->DHCP_CONNECT");
            mHandler.sendEmptyMessage(DHCP_CONNECT_SUCCESS);
            tvPppoeState.setVisibility(View.INVISIBLE);
            tvDhcpState.setVisibility(View.VISIBLE);
            tvStaticState.setVisibility(View.INVISIBLE);
            imgPppoe.setImageResource(R.drawable.radio_unchecked_normal);
            imgDhcp.setImageResource(R.drawable.radio_checked_normal);
            imgStatic.setImageResource(R.drawable.radio_unchecked_normal);
        }else if (ethMode.equals(EthernetManager.ETHERNET_CONNECT_MODE_MANUAL)){
            logUtil.logi("flag---->STATIC_CONNECT");
            mHandler.sendEmptyMessage(STATIC_CONNECT_SUCCESS);
            tvPppoeState.setVisibility(View.INVISIBLE);
            tvDhcpState.setVisibility(View.INVISIBLE);
            tvStaticState.setVisibility(View.VISIBLE);
            imgPppoe.setImageResource(R.drawable.radio_unchecked_normal);
            imgDhcp.setImageResource(R.drawable.radio_unchecked_normal);
            imgStatic.setImageResource(R.drawable.radio_checked_normal);
        }

    }

}
