package com.example.eileen.mysettings.utils;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

public class QuitActivity extends AppCompatActivity {
    private LogUtil logUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){

        if (keyCode != KeyEvent.KEYCODE_BACK){
            return false;
        }
        ActivityCollector.finishAll();
        return false;
    }
}
