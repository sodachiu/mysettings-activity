package com.example.eileen.mysettings;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

public class StaticIpActivity extends AppCompatActivity {

    private TextView netSetting;
    private Button confirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.static_ip_activity);
        netSetting = (TextView) findViewById(R.id.net_setting);
        confirm = (Button) findViewById(R.id.static_ip_confirm);
        netSetting.setBackgroundResource(R.drawable.menu_item_select);
        confirm.requestFocus();
    }
}
