package com.example.eileen.mysettings;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.display.DisplayManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.example.eileen.mysettings.utils.LogUtil;

public class ResolutionDialogActivity extends Activity
        implements View.OnClickListener{

    private Button btnConfirm;
    private Button btnCancel;
    private int oldStandard;
    private LogUtil logUtil = new LogUtil("mydisplay");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resolution_dialog);
        btnConfirm = (Button) findViewById(R.id.resolution_btn_confirm);
        btnCancel = (Button) findViewById(R.id.resolution_btn_cancel);

        btnConfirm.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        Intent intent = getIntent();
        oldStandard = intent.getIntExtra("old_standard_id", -1);

        if (oldStandard == -1){
            logUtil.loge("获取的制式id有误----" + oldStandard);
        }

    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.resolution_btn_confirm:
                finish();
                break;
            case R.id.resolution_btn_cancel:

                DisplayManager displayManager = (DisplayManager) getApplicationContext()
                        .getSystemService(Context.DISPLAY_MANAGER_SERVICE);
                displayManager.setDisplayStandard(oldStandard);
                finish();
                break;
            default:
                break;
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){

        if (keyCode == KeyEvent.KEYCODE_BACK){

            return false;
        }
        return false;
    }
}
