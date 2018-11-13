package com.example.eileen.mysettings;

import android.content.Context;
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

import com.example.eileen.mysettings.utils.LogUtil;


public class PPPoEActivity extends AppCompatActivity
        implements View.OnClickListener{

    private TextView tvMenu;
    private EditText etUsername, etUserPwd;
    private Button btnConfirm, btnCancel;
    private PppoeManager mPppoeManager;
    private EthernetManager mEthManager;

    private LogUtil logUtil = new LogUtil("mynetsettings");
    public static final int NO_NAME = 0;
    public static final int NO_PASSWORD = 1;
    public static final int NO_PHY_LINK = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        logUtil.logi("PPPoEActivity---->onCreate()");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.pppoe_activity);
        try {
            mPppoeManager = (PppoeManager) getSystemService(Context.PPPOE_SERVICE);
            mEthManager = (EthernetManager) getSystemService(Context.ETHERNET_SERVICE);
            logUtil.logi("获取网络服务成功");
        }catch (Exception e){
            logUtil.loge("获取网络服务失败---->" + e.toString());
        }

        findView();

    }

    public void findView(){
        logUtil.logi("PPPoEActivity---->findView()");

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
        logUtil.logi("PPPoEActivity---->initView()");

        if (mPppoeManager != null){
            String username = "";
            String userpwd = "";
            if (mPppoeManager.getPppoeUsername() != null
                    && !mPppoeManager.getPppoeUsername().equals("")){
                username = mPppoeManager.getPppoeUsername();
            }

            if (mPppoeManager.getPppoePassword() != null
                    && !mPppoeManager.getPppoePassword().equals("")){
                userpwd = mPppoeManager.getPppoePassword();
            }
            etUsername.setText(username);
            etUserPwd.setText(userpwd);
        }
    }

    @Override
    protected void onResume(){
        logUtil.logi("PPPoEActivity---->onResume()");

        super.onResume();
        initView();
        btnConfirm.requestFocus();
    }

    @Override
    protected void onDestroy(){
        logUtil.logi("PPPoEActivity---->onDestroy()");

        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }


    @Override
    public void onClick(View v){
        logUtil.logi("PPPoEActivity---->onClick()");

        switch (v.getId()){
            case R.id.pppoe_btn_confirm:
                logUtil.logi("onClick()---->点击确定");
                setPppoe();
                break;
            case R.id.pppoe_btn_cancel:
                logUtil.logi("onClick()---->点击取消");
                finish();
                break;
            default:
                break;
        }
    }

    private void setPppoe(){
        logUtil.logi("PPPoEActivity---->setPpppoe()");
        if (!mEthManager.getNetLinkStatus()){
            mHandler.sendEmptyMessage(NO_PHY_LINK);
            logUtil.logi("没有网线连接");

        }

        String username = etUsername.getText().toString().trim();
        String userpwd = etUserPwd.getText().toString().trim();
        if (username.equals("")){
            mHandler.sendEmptyMessage(NO_NAME);
            return;
        }

        if (userpwd.equals("")){
            mHandler.sendEmptyMessage(NO_PASSWORD);
            return;
        }
        mEthManager.setEthernetMode(EthernetManager.ETHERNET_CONNECT_MODE_PPPOE, null);
        mEthManager.setEthernetEnabled(false);
        mPppoeManager.setPppoeUsername(username);
        mPppoeManager.setPppoePassword(userpwd);
        String ifaceName = null;
        logUtil.logi("网络连接模式---->" + mEthManager.getEthernetMode());
        if (EthernetManager.ETHERNET_CONNECT_MODE_PPPOE.equals(mEthManager.getEthernetMode())
                && mEthManager.getEthernetState() == EthernetManager.ETHERNET_STATE_ENABLED) {
            ifaceName = mEthManager.getInterfaceName();
        }
        mEthManager.setEthernetEnabled(true);
        mPppoeManager.connect(username, userpwd, ifaceName);

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
              default:
                  break;

          }
      }
    };

}
