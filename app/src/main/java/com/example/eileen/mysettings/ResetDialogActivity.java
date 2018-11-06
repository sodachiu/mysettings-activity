package com.example.eileen.mysettings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.eileen.mysettings.utils.LogUtil;

public class ResetDialogActivity extends Activity
        implements View.OnClickListener{

    private Button confirm;
    private Button cancel;
    private LogUtil logUtil = new LogUtil("myreset");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recovery_system_dialog);
        confirm = (Button) findViewById(R.id.button_res_confirm);
        cancel = (Button) findViewById(R.id.button_res_cancel);

        confirm.setOnClickListener(this);
        cancel.setOnClickListener(this);

    }

    @Override
    public void onClick(View v){
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.button_res_confirm:
                logUtil.logi("用户点击了确定恢复出厂");
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.button_res_cancel:
                logUtil.logi("用户点击了取消恢复出厂");
                setResult(RESULT_CANCELED, intent);
                finish();
                break;
            default:
                break;
        }
    }
}
