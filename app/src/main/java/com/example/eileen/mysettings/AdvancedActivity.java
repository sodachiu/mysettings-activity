package com.example.eileen.mysettings;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.eileen.mysettings.utils.QuitActivity;

public class AdvancedActivity extends QuitActivity
        implements View.OnClickListener, View.OnKeyListener{

    private static final String TAG = "AdvancedActivity";

    private Button mBtnConfirm;
    private EditText mEtPassword;
    private TextView mTvPromptInfo;

    private TextView advanced;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advanced_activity);

        advanced = (TextView) findViewById(R.id.advanced);
        mBtnConfirm = (Button) findViewById(R.id.button_advanced);
        mEtPassword = (EditText) findViewById(R.id.advanced_pwd);
        mTvPromptInfo = (TextView) findViewById(R.id.advanced_wrong_pwd_text);

        advanced.setFocusable(true);
        advanced.setBackgroundResource(R.drawable.menu_focus_selector);

        advanced.setOnKeyListener(this);
        mBtnConfirm.setOnClickListener(this);

    }


    @Override
    protected void onResume(){
        super.onResume();
        mEtPassword.setFocusable(true);
        mBtnConfirm.setFocusable(true);
        mTvPromptInfo.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    @Override
    public void onClick(View v){
        String pwdText = mEtPassword.getText().toString();
        Log.d(TAG, "onClick: " + pwdText);

        // 拿到系统密码，与之对比
        if (pwdText.equals("10086") ){
            Intent intent = new Intent(AdvancedActivity.this, AdvancedItemActivity.class);
            startActivity(intent);

        }else {
            mTvPromptInfo.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event){

        if (event.getAction() == KeyEvent.ACTION_DOWN){

            Intent intent = new Intent();
            switch (keyCode){
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    mBtnConfirm.setFocusable(false);
                    intent = new Intent(AdvancedActivity.this, ResetActivity.class);
                    startActivity(intent);
                    break;
                case KeyEvent.KEYCODE_DPAD_UP:
                    mEtPassword.setFocusable(false);
                    intent = new Intent(AdvancedActivity.this, StorageActivity.class);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
        return false;
    }



}
