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
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eileen.mysettings.network.MyNetworkUtil;
import com.example.eileen.mysettings.utils.LogUtil;

import java.net.Inet4Address;
import java.net.InetAddress;

public class StaticIpActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView tvMenu;
    private EditText etIp, etMask, etGateway, etDns1, etDns2;
    private Button btnConfirm, btnCancel;
    private String mUserIP, mUserMask, mUserGateway, mUserDns1, mUserDns2;
    private InetAddress miIP, miMask, miGateway, miDns1, miDns2;
    public static final int IP_ILLEGAL = 0;
    public static final int MASK_ILLEGAL = 1;
    public static final int GATEWAY_ILLEGAL = 2;
    public static final int DNS1_ILLEGAL = 3;
    public static final int DNS2_ILLEGAL = 4;
    public static final int NOT_ONE_SEGMENT = 5;
    public static final int STATIC_CONNECT_SUCCESS = 6;
    public static final int STATIC_CONNECT_FAILED = 7;

    private LogUtil logUtil = new LogUtil("mynetsettings");
    private EthernetManager mEthManager;
    private PppoeManager mPppoeManager;
    private DhcpInfo mDhcp = null;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case IP_ILLEGAL:
                    Toast.makeText(StaticIpActivity.this,
                            "IP不合法",
                            Toast.LENGTH_SHORT).show();
                    initView();
                    break;
                case MASK_ILLEGAL:
                    Toast.makeText(StaticIpActivity.this,
                            "子网掩码不合法",
                            Toast.LENGTH_SHORT).show();
                    initView();
                    break;
                case GATEWAY_ILLEGAL:
                    Toast.makeText(StaticIpActivity.this,
                            "网关不合法",
                            Toast.LENGTH_SHORT).show();
                    initView();
                    break;
                case DNS1_ILLEGAL:
                    Toast.makeText(StaticIpActivity.this,
                            "主用DNS不合法",
                            Toast.LENGTH_SHORT).show();
                    initView();
                    break;
                case DNS2_ILLEGAL:
                    Toast.makeText(StaticIpActivity.this,
                            "备用DNS不合法",
                            Toast.LENGTH_SHORT).show();
                    initView();
                    break;
                case NOT_ONE_SEGMENT:
                    Toast.makeText(StaticIpActivity.this,
                            "IP和网关不在同一个网段",
                            Toast.LENGTH_SHORT).show();
                    initView();
                    break;
                case STATIC_CONNECT_FAILED:
                    Toast.makeText(StaticIpActivity.this,
                            "连接失败",
                            Toast.LENGTH_SHORT).show();
                    break;
                case STATIC_CONNECT_SUCCESS:
                    Toast.makeText(StaticIpActivity.this,
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
        logUtil.logi("StaticIpActivity------>onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.static_ip_activity);
        getService();
        findView();
        IntentFilter filter = new IntentFilter();
        filter.addAction(EthernetManager.ETHERNET_STATE_CHANGED_ACTION);
        registerReceiver(myReceiver, filter);

    }

    @Override
    protected void onResume(){
        logUtil.logi("StaticIpActivity------>onResume()");
        super.onResume();
        initView();
    }

    @Override
    protected void onDestroy(){
        logUtil.logi("StaticIpActivity------>onDestroy()");
        super.onDestroy();
        unregisterReceiver(myReceiver);
        mHandler.removeCallbacksAndMessages(null);//防止handler造成的内存泄漏
    }


    private void getService(){
        logUtil.logi("StaticIpActivity------>getService()");

        try{
            mEthManager = (EthernetManager) getSystemService(Context.ETHERNET_SERVICE);
            mPppoeManager = (PppoeManager) getSystemService(Context.PPPOE_SERVICE);
        }catch (Exception e){
            logUtil.loge("获取网络服务失败---->" + e.toString());
        }

        if (EthernetManager.ETHERNET_CONNECT_MODE_PPPOE.equals(mEthManager.getEthernetMode())){
            mDhcp = mPppoeManager.getDhcpInfo();
        }else {
            mDhcp = mEthManager.getDhcpInfo();
        }
    }

    private void findView(){
        logUtil.logi("StaticIpActivity------>findView()");
        tvMenu = (TextView) findViewById(R.id.net_setting);
        etIp = (EditText) findViewById(R.id.static_et_ip);
        etMask = (EditText) findViewById(R.id.static_et_mask);
        etGateway = (EditText) findViewById(R.id.static_et_gateway);
        etDns1 = (EditText) findViewById(R.id.static_et_dns1);
        etDns2 = (EditText) findViewById(R.id.static_et_dns2);
        btnConfirm = (Button) findViewById(R.id.static_btn_confirm);
        btnCancel = (Button) findViewById(R.id.static_btn_cancel);
        tvMenu.setBackgroundResource(R.drawable.menu_item_select);

    }

    private void initView(){
        logUtil.logi("StaticIpActivity------>initView()");
        btnConfirm.requestFocus();

        String defaultInfo = getResources().getString(R.string.net_default_text);
        String sIP = defaultInfo, sMask = defaultInfo, sGateway = defaultInfo,
                sDns1 = defaultInfo, sDns2 = defaultInfo;
        if (mDhcp != null){
            sIP = NetworkUtils.intToInetAddress(mDhcp.ipAddress).getHostAddress();
            sMask = NetworkUtils.intToInetAddress(mDhcp.netmask).getHostAddress();
            sGateway = NetworkUtils.intToInetAddress(mDhcp.gateway).getHostAddress();
            sDns1 = NetworkUtils.intToInetAddress(mDhcp.dns1).getHostAddress();
            sDns2 = NetworkUtils.intToInetAddress(mDhcp.dns2).getHostAddress();
        }

        etIp.setText(sIP);
        etMask.setText(sMask);
        etGateway.setText(sGateway);
        etDns1.setText(sDns1);
        etDns2.setText(sDns2);

        etIp.setSelectAllOnFocus(true);
        etMask.setSelectAllOnFocus(true);
        etGateway.setSelectAllOnFocus(true);
        etDns1.setSelectAllOnFocus(true);
        etDns2.setSelectAllOnFocus(true);
        btnConfirm.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }


    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.static_btn_confirm:
                boolean dhcpItemLegal = checkDhcpItem();
                if (dhcpItemLegal){
                    logUtil.logi("用户输入的信息正确，进行静态IP连接");
                    setStatic();
                }else {
                    logUtil.logi("用户输入信息不合法，退出静态IP连接");
                }
                break;
            case R.id.static_btn_cancel:
                finish();
                break;
            default:
                break;
        }
    }

    private void setStatic(){
        logUtil.logi("StaticIpActivity---->setStatic()");

        DhcpInfo userDhcpInfo = new DhcpInfo();

        miIP = NetworkUtils.numericToInetAddress(mUserIP);
        userDhcpInfo.ipAddress = NetworkUtils.inetAddressToInt((Inet4Address) miIP);

        miMask = NetworkUtils.numericToInetAddress(mUserMask);
        userDhcpInfo.netmask = NetworkUtils.inetAddressToInt((Inet4Address) miMask);

        miGateway = NetworkUtils.numericToInetAddress(mUserGateway);
        userDhcpInfo.gateway = NetworkUtils.inetAddressToInt((Inet4Address) miGateway);

        miDns1 = NetworkUtils.numericToInetAddress(mUserDns1);
        userDhcpInfo.dns1 = NetworkUtils.inetAddressToInt((Inet4Address) miDns1);

        miDns2 = NetworkUtils.numericToInetAddress(mUserDns2);
        userDhcpInfo.dns2 = NetworkUtils.inetAddressToInt((Inet4Address) miDns2);

        mEthManager.setEthernetEnabled(false);
        mEthManager.setEthernetMode(EthernetManager.ETHERNET_CONNECT_MODE_MANUAL, userDhcpInfo);
        mEthManager.setEthernetEnabled(true);

    }


    /*
    * 检查用户输入的信息是否合法
    * */
    private boolean checkDhcpItem(){
        logUtil.logi("StaticIpActivity---->checkDhcpItem()");
        mUserIP = etIp.getText().toString();
        logUtil.logi("用户输入的IP为---->" + mUserIP);
        boolean ipLegal = MyNetworkUtil.checkDhcpItem(mUserIP);
        if (!ipLegal){
            Message mMsg = new Message();
            mMsg.what = IP_ILLEGAL;
            mHandler.sendMessage(mMsg);
            logUtil.logi("IP不合法");
            return false;
        }

        //比较与默认的mask是否相同
        mUserMask = etMask.getText().toString();
        logUtil.logi("用户输入的netmask为---->" + mUserMask);
        String netMask = NetworkUtils.intToInetAddress(mDhcp.netmask).getHostAddress();
        boolean maskLegal = netMask.equals(mUserMask);
        if (!maskLegal){
            Message mMsg = new Message();
            mMsg.what = MASK_ILLEGAL;
            mHandler.sendMessage(mMsg);
            logUtil.logi("子网掩码不合法");
            return false;
        }


        mUserGateway = etGateway.getText().toString();
        logUtil.logi("用户输入的gateway为---->" + mUserGateway);
        boolean gatewayLegal = MyNetworkUtil.checkDhcpItem(mUserGateway);
        if (!gatewayLegal){
            Message mMsg = new Message();
            mMsg.what = GATEWAY_ILLEGAL;
            mHandler.sendMessage(mMsg);
            logUtil.logi("默认网关不合法");
            return false;
        }

        mUserDns1 = etDns1.getText().toString();
        logUtil.logi("用户输入的dns1为---->" + mUserDns1);
        boolean dns1Legal = MyNetworkUtil.checkDhcpItem(mUserDns1);
        if (!dns1Legal){
            Message mMsg = new Message();
            mMsg.what = DNS1_ILLEGAL;
            mHandler.sendMessage(mMsg);
            logUtil.logi("主用DNS不合法");
            return false;
        }

        mUserDns2 = etDns2.getText().toString();
        logUtil.logi("用户输入的dns2为---->" + mUserDns2);
        boolean dns2Legal = MyNetworkUtil.checkDhcpItem(mUserDns2);
        if (!dns2Legal){
            Message mMsg = new Message();
            mMsg.what = DNS2_ILLEGAL;
            mHandler.sendMessage(mMsg);
            logUtil.logi("备用DNS不合法");
            return false;
        }

        boolean inOneSegment = MyNetworkUtil.checkInOneSegment(mUserIP, mUserGateway);
        if (!inOneSegment){
            Message mMsg = new Message();
            mMsg.what = NOT_ONE_SEGMENT;
            mHandler.sendMessage(mMsg);
            logUtil.logi("IP和网关不在同一个网段");
            return false;
        }

        logUtil.logi("checkDhcpItem结尾");
        return true;
    }

    /*
    * 监听网络变化的广播
    * */
    private BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            logUtil.logi("staticIpActivity---->onReceive()");

            String action = intent.getAction();
            logUtil.logi("onReceive()---->广播：" + action);

            int ethEvent = intent.getIntExtra(EthernetManager.EXTRA_ETHERNET_STATE, -1);

            if (ethEvent == EthernetManager.EVENT_STATIC_CONNECT_SUCCESSED){
                logUtil.logi("onReceive()---->静态IP连接成功");
                Message mMsg = new Message();
                mMsg.what = STATIC_CONNECT_SUCCESS;
                mHandler.sendMessage(mMsg);
            }else if (ethEvent == EthernetManager.EVENT_STATIC_CONNECT_FAILED){
                logUtil.logi("onReceive()---->静态IP连接失败");
                Message mMsg = new Message();
                mMsg.what = STATIC_CONNECT_FAILED;
                mHandler.sendMessage(mMsg);
            }

        }
    };

}
