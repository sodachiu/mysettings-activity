package com.example.eileen.mysettings;

import android.content.Intent;
import android.os.Bundle;
import android.os.RecoverySystem;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.eileen.mysettings.utils.ActivityId;
import com.example.eileen.mysettings.utils.LogUtil;
import com.example.eileen.mysettings.utils.QuitActivity;

public class ResetActivity extends QuitActivity
        implements View.OnClickListener, View.OnKeyListener{

    private TextView resFactory;
    private EditText password;
    private Button confirm;
    private LogUtil logUtil = new LogUtil("myreset");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recovery_system_activity);
        resFactory = (TextView) findViewById(R.id.res_factory);
        password = (EditText) findViewById(R.id.res_factory_pwd);
        confirm = (Button) findViewById(R.id.button_res_factory);

        resFactory.setFocusable(true);
        resFactory.setBackgroundResource(R.drawable.menu_focus_selector);
        resFactory.setOnKeyListener(this);
        confirm.setOnKeyListener(this);
        password.setOnKeyListener(this);
        confirm.setOnClickListener(this);

    }

    @Override
    protected void onResume(){
        super.onResume();
        password.setFocusable(true);
        confirm.setFocusable(true);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){

        logUtil.logi("requestCode---->" + requestCode);
        logUtil.logi("resultCode---->" + resultCode);

        if (requestCode != ActivityId.RESET_ACTIVITY){
            logUtil.logi("请求码有误");
            return;
        }


        if (resultCode == RESULT_OK){
            logUtil.logi("已收到请求，立即执行恢复出厂操作");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        logUtil.logi("正在尝试删除用户信息......");
                        RecoverySystem.rebootWipeUserData(ResetActivity.this);
                    }catch (Exception e){
                        logUtil.logi("擦除用户信息失败---->" + e.toString());
                    }
                }
            }).start();
        }else {
            logUtil.logi("取消恢复出厂");
        }

    }

    @Override
    public void onClick(View v){
        Intent intent = new Intent(ResetActivity.this,
                ResetDialogActivity.class);
        startActivityForResult(intent, ActivityId.RESET_ACTIVITY);
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent keyEvent){

        if (keyEvent.getAction() == KeyEvent.ACTION_DOWN){
            switch (v.getId()){
                case R.id.res_factory:
                    if (keyCode == KeyEvent.KEYCODE_DPAD_UP){
                        password.setFocusable(false);
                        confirm.setFocusable(false);
                        Intent intent = new Intent(ResetActivity.this,
                                AdvancedActivity.class);
                        startActivity(intent);
                    }
                    break;
                default:
                    break;

            }

        }
        return false;
    }




}
