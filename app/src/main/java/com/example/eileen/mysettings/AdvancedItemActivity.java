package com.example.eileen.mysettings;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/*
* 记得修改变量名区分全局和块变量啊
* */
public class AdvancedItemActivity extends AppCompatActivity
        implements View.OnClickListener {

    private static final String TAG = "AdvancedItemActivity";
    private TextView advanced;
    private LinearLayout clearAll;
    private LinearLayout standbyMinutes;
    private LinearLayout standbyHours;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advanced_item_activity);
        advanced = (TextView) findViewById(R.id.advanced);
        clearAll = (LinearLayout) findViewById(R.id.clear_all);
        standbyMinutes = (LinearLayout) findViewById(R.id.standby_5_minutes);
        standbyHours = (LinearLayout) findViewById(R.id.standby_4_hours);
        advanced.setBackgroundResource(R.drawable.menu_item_select);
        clearAll.setOnClickListener(this);
        standbyMinutes.setOnClickListener(this);
        standbyHours.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clear_all:
                Intent intent = new Intent(AdvancedItemActivity.this, ClearAllActivity.class);
                startActivity(intent);
                break;
            case R.id.standby_5_minutes:
                Log.d(TAG, "onClick: 5minutes");
                //又是变图片的操作啊
                break;
            case R.id.standby_4_hours:
                //变图片的操作啊
                Log.d(TAG, "onClick: 4hours");
                break;
            default:
                break;
        }
    }
}
