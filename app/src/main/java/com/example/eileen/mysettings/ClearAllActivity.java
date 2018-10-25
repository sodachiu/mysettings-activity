package com.example.eileen.mysettings;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ClearAllActivity extends AppCompatActivity
        implements View.OnClickListener{

    private TextView advanced;
    private Button clearAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wipe_cache_activity);
        advanced = (TextView) findViewById(R.id.advanced);
        clearAll = (Button) findViewById(R.id.button_clear_all);
        advanced.setBackgroundResource(R.drawable.menu_item_select);

        clearAll.setOnClickListener(this);
    }


    @Override
    public void onClick(View v){
        Toast.makeText(ClearAllActivity.this,"清理完成", Toast.LENGTH_SHORT).show();
    }
}
