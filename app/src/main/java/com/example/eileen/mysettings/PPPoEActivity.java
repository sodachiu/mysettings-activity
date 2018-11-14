package com.example.eileen.mysettings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ethernet.EthernetManager;
import android.net.pppoe.PppoeManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eileen.mysettings.utils.LogUtil;


public class PPPoEActivity extends AppCompatActivity
        implements View.OnClickListener{

    private TextView tvMenu;
    private EditText etUsername, etUserPwd;
    private Button btnConfirm, btnCancel;
    private String mWorkUsername = "", mWorkPwd = "", mUsername = "", mUserPwd = "";
    private PppoeManager mPppoeManager;
    private EthernetManager mEthManager;

    public static final int NO_NAME = 0;
    public static final int NO_PASSWORD = 1;
    public static final int NO_PHY_LINK = 2;
    public static final int PPPOE_CONNECT_FAILED = 3;
    public static final int PPPOE_CONNECT_SUCCESS = 4;
    public static final int PPPOE_CONNECTING = 5;
    public static final int PPPOE_AUTO_RECONNECT = 6;
    public static final int PPPOE_DISCONNECT_SUCCESS = 7;
    public static final int PPPOE_DISCONNECT_FAILED = 8;
    public static final int PPPOE_INFO_REPEAT = 9;
    public static final String TAG = "mypppoe";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pppoe_activity);
        try {
            mPppoeManager = (PppoeManager) getSystemService(Context.PPPOE_SERVICE);
            mEthManager = (EthernetManager) getSystemService(Context.ETHERNET_SERVICE);
            Log.i(TAG, "onCreate: 获取服务成功");
        }catch (Exception e){
            Log.e(TAG, "onCreate: 获取服务失败---->" + e.toString());
        }

        IntentFilter filter = new IntentFilter(PppoeManager.PPPOE_STATE_CHANGED_ACTION);
        registerReceiver(myReceiver, filter);
        findView();

    }

    public void findView(){
        Log.i(TAG, "findView: ");
        tvMenu = (TextView) findViewById(R.id.net_setting);
        tvMenu.setBackgroundResource(R.drawable.menu_item_select);
        etUsername = (EditText) findViewById(R.id.pppoe_et_username);
        etUserPwd = (EditText) findViewById(R.id.pppoe_et_password);
        btnConfirm = (Button) findViewById(R.id.pppoe_btn_confirm);
        btnCancel = (Button) findViewById(R.id.pppoe_btn_cancel);

        btnConfirm.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    public void initView(){
        Log.i(TAG, "initView: ");
        if (mPppoeManager != null){
            if (mPppoeManager.getPppoeUsername() != null
                    && !mPppoeManager.getPppoeUsername().equals("")){
                mWorkUsername = mPppoeManager.getPppoeUsername();
            }

            if (mPppoeManager.getPppoePassword() != null
                    && !mPppoeManager.getPppoePassword().equals("")){
                mWorkPwd = mPppoeManager.getPppoePassword();
            }
            etUsername.setText(mWorkUsername);
            etUserPwd.setText(mWorkPwd);
        }
    }

    @Override
    protected void onResume(){
        Log.i(TAG, "onResume: ");
        super.onResume();
        initView();
        btnConfirm.requestFocus();
    }

    @Override
    protected void onDestroy(){
        Log.i(TAG, "onDestroy: ");
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
        unregisterReceiver(myReceiver);
    }


    @Override
    public void onClick(View v){
        Log.i(TAG, "onClick: ");
        switch (v.getId()){
            case R.id.pppoe_btn_confirm:
                Log.i(TAG, "onClick: 点击确定");
                setPppoe();
                break;
            case R.id.pppoe_btn_cancel:
                Log.i(TAG, "onClick: 点击取消");
                finish();
                break;
            default:
                break;
        }
    }

    private boolean checkInfoRepeat(){
        String ethMode = mEthManager.getEthernetMode();

        Log.i(TAG, "checkInfoRepeat: 当前模式为---->" + ethMode);

        if (!ethMode.equals(EthernetManager.ETHERNET_CONNECT_MODE_PPPOE)){
            Log.i(TAG, "checkInfoRepeat: 当前模式为pppoe，可以进行pppoe连接");
            return false;
        }

        if (mWorkPwd.equals(mUserPwd) && mWorkUsername.equals(mUsername)){
            //在所有信息均相同的情况下
            return true;
        }else {
            return false;
        }
    }

    private void setPppoe(){
        Log.i(TAG, "setPppoe: ");
        if (!mEthManager.getNetLinkStatus()){
            mHandler.sendEmptyMessage(NO_PHY_LINK);
            Log.i(TAG, "setPppoe: 请检查网线是否连接");
            return;

        }
        mUsername = etUsername.getText().toString().trim();
        mUserPwd = etUserPwd.getText().toString().trim();

        if (checkInfoRepeat()){
            mHandler.sendEmptyMessage(PPPOE_INFO_REPEAT);
            Log.i(TAG, "setPppoe: pppoe已经连接且没有改变任何值，拦截pppoe连接");
            return;
        }

        if (mUsername.equals("")){
            mHandler.sendEmptyMessage(NO_NAME);
            return;
        }

        if (mUserPwd.equals("")){
            mHandler.sendEmptyMessage(NO_PASSWORD);
            return;
        }
        mEthManager.setEthernetMode(EthernetManager.ETHERNET_CONNECT_MODE_PPPOE, null);
        mEthManager.setEthernetEnabled(false);
        mPppoeManager.setPppoeUsername(mUsername);
        mPppoeManager.setPppoePassword(mUserPwd);
        String ifaceName = null;
        Log.i(TAG, "setPppoe: 网络连接模式---->" + mEthManager.getEthernetMode());
        if (EthernetManager.ETHERNET_CONNECT_MODE_PPPOE.equals(mEthManager.getEthernetMode())
                && mEthManager.getEthernetState() == EthernetManager.ETHERNET_STATE_ENABLED) {
            ifaceName = mEthManager.getInterfaceName();
        }
        mEthManager.setEthernetEnabled(true);
        mPppoeManager.connect(mUsername, mUserPwd, ifaceName);

    }

    private Handler mHandler = new Handler(){
      @Override
      public void handleMessage(Message msg){
          switch (msg.what){
              case NO_NAME:
                  Toast.makeText(PPPoEActivity.this,
                          "用户名为空",
                          Toast.LENGTH_SHORT).show();
                  break;
              case NO_PASSWORD:
                  Toast.makeText(PPPoEActivity.this,
                          "密码为空",
                          Toast.LENGTH_SHORT).show();
                  break;
              case NO_PHY_LINK:
                  Toast.makeText(PPPoEActivity.this,
                          "请检查网线是否连接",
                          Toast.LENGTH_SHORT).show();
                  break;
              case PPPOE_CONNECT_FAILED :
                  Toast.makeText(PPPoEActivity.this,
                          "PPPoE未连接",
                          Toast.LENGTH_SHORT).show();
                  break;
              /*case PPPOE_DISCONNECT_SUCCESS :
                  Toast.makeText(PPPoEActivity.this,
                        "PPPoE连接已断开",
                        Toast.LENGTH_SHORT).show();
                  break;*/
              case PPPOE_DISCONNECT_FAILED :
                  Toast.makeText(PPPoEActivity.this,
                          "PPPoE断开连接失败",
                          Toast.LENGTH_SHORT).show();
                  break;
              case PPPOE_AUTO_RECONNECT :
                  Toast.makeText(PPPoEActivity.this,
                          "PPPoE正在尝试重连...",
                          Toast.LENGTH_SHORT).show();
                  break;
              case PPPOE_CONNECTING :
                  Toast.makeText(PPPoEActivity.this,
                          "正在连接请稍后...",
                          Toast.LENGTH_SHORT).show();
                  break;
              case PPPOE_INFO_REPEAT :
                  Toast.makeText(PPPoEActivity.this,
                          "PPPoE已经连接，没有修改任何信息",
                          Toast.LENGTH_SHORT).show();
                  break;
              case PPPOE_CONNECT_SUCCESS :
                  Toast.makeText(PPPoEActivity.this,
                          "PPPoE连接成功",
                          Toast.LENGTH_SHORT).show();
                  break;
              default:
                  break;

          }
      }
    };


    private BroadcastReceiver myReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.i(TAG, "onReceive: 广播---->" + action);
            if (action.equals(PppoeManager.PPPOE_STATE_CHANGED_ACTION)){
                int pppoeEvent = intent.getIntExtra(PppoeManager.EXTRA_PPPOE_STATE, -1);

                if (pppoeEvent == PppoeManager.EVENT_CONNECT_FAILED){

                    mHandler.sendEmptyMessage(PPPOE_CONNECT_FAILED);
                    String errorMsg = intent.getStringExtra(PppoeManager.EXTRA_PPPOE_ERRMSG);
                    if (errorMsg == null){
                        errorMsg = "999, PPPOE 拨号错误";
                    }
                    Log.i(TAG, "onReceive: 连接失败---->" + errorMsg);

                }else if (pppoeEvent == PppoeManager.EVENT_CONNECTING){

                    mHandler.sendEmptyMessage(PPPOE_CONNECTING);
                    Log.i(TAG, "onReceive: 正在连接");

                }else if (pppoeEvent == PppoeManager.EVENT_AUTORECONNECTING){

                    mHandler.sendEmptyMessage(PPPOE_AUTO_RECONNECT);
                    Log.i(TAG, "onReceive: pppoe尝试重连");

                }else if (pppoeEvent == PppoeManager.EVENT_CONNECT_SUCCESSED){

                    if (mEthManager.getEthernetMode().equals(
                            EthernetManager.ETHERNET_CONNECT_MODE_PPPOE)){
                        mHandler.sendEmptyMessage(PPPOE_CONNECT_SUCCESS);
                        Log.i(TAG, "onReceive: pppoe连接成功");
                    }


                }else if (pppoeEvent == PppoeManager.EVENT_DISCONNECT_SUCCESSED){

                    mHandler.sendEmptyMessage(PPPOE_DISCONNECT_SUCCESS);
                    Log.i(TAG, "onReceive: pppoe成功断开连接");

                }else if (pppoeEvent == PppoeManager.EVENT_DISCONNECT_FAILED){

                    mHandler.sendEmptyMessage(PPPOE_DISCONNECT_FAILED);
                    Log.i(TAG, "onReceive: pppoe断开失败");

                }
            }
        }
    };
}
