package com.example.eileen.mysettings;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.DhcpInfo;
import android.net.ethernet.EthernetManager;
import android.net.pppoe.PppoeManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


/**/
public class PPPoEActivity extends AppCompatActivity
        implements View.OnClickListener{
    private PppoeConnectService.PppoeConnectBinder mBinder;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBinder = (PppoeConnectService.PppoeConnectBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    private static final String TAG = "PPPoEActivity";
    private static final String PPPOE_LOCAL_BROADCAST = "com.example.eileen.mysettings.PPPOE_LOCAL_BROADCAST";
    private TextView tvNetSetting;
    private EditText etUsername;
    private EditText etPassword;
    private Button btnConfirm;
    private Button btnCancel;
    public static final String ETHERNET_SERVICE = "ethernet";
    public static final String PPPOE_SERVICE = "pppoe";
    private PppoeManager pppoeManager;
    private EthernetManager ethernetManager;
    private PppoeChangeReceiver pppoeChangeReceiver;
    private DhcpInfo dhcpInfo;
    private String ifaceName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pppoe_activity);
        Context context = getApplicationContext();

        pppoeManager = (PppoeManager) context.getSystemService(
                Context.PPPOE_SERVICE);
        ethernetManager = (EthernetManager) context.getSystemService(
                Context.ETHERNET_SERVICE);
        ifaceName = ethernetManager.getInterfaceName();
        tvNetSetting = (TextView) findViewById(R.id.net_setting);
        etUsername = (EditText) findViewById(R.id.pppoe_et_username);
        etPassword = (EditText) findViewById(R.id.pppoe_et_password);
        btnConfirm = (Button) findViewById(R.id.pppoe_btn_confirm);
        btnCancel = (Button) findViewById(R.id.pppoe_btn_cancel);
        tvNetSetting.setBackgroundResource(R.drawable.menu_item_select);
        btnConfirm.requestFocus();

        btnConfirm.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        Intent intent = new Intent(PPPoEActivity.this, PppoeConnectService.class);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(PppoeManager.PPPOE_STATE_CHANGED_ACTION);
        intentFilter.addAction(PPPOE_LOCAL_BROADCAST);
        pppoeChangeReceiver = new PppoeChangeReceiver();
        this.registerReceiver(pppoeChangeReceiver, intentFilter);
        startService(intent);
        bindService(intent, connection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.d(TAG, "onDestroy: hello");
        this.unregisterReceiver(pppoeChangeReceiver);
    }

    class PppoeChangeReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(PPPOE_LOCAL_BROADCAST)){
                int ethState = ethernetManager.getNetLinkStatus(ifaceName);
                int pppoeState = pppoeManager.getPppoeState();
                /*Toast.makeText(
                        PPPoEActivity.this,
                        "net state " + pppoeManager.getPppoeMode(),
                        Toast.LENGTH_SHORT
                ).show();*/
            }else if (intent.getAction().equals(PppoeManager.PPPOE_STATE_CHANGED_ACTION)){
                Toast.makeText(
                        PPPoEActivity.this,
                        "pppoe state changed action " ,
                        Toast.LENGTH_SHORT
                ).show();
            }else {
                Toast.makeText(
                        PPPoEActivity.this,
                        "pppoe unknown broadcast" ,
                        Toast.LENGTH_SHORT
                ).show();
            }

            /*int pppoeState = pppoeManager.getPppoeState();
            Toast.makeText(PPPoEActivity.this, "pppoechangereceiver"+pppoeState,
                    Toast.LENGTH_SHORT).show();
            Log.d(TAG, "onReceive: " + pppoeState);*/
            /*if (pppoeState == PppoeManager.PPPOE_CONNECT_RESULT_CONNECTING){
                Log.d(TAG, "onReceive: connecting" );
            }else if (pppoeState == PppoeManager.PPPOE_CONNECT_RESULT_CONNECT){

            }*/

        }
    }

    @Override
    public void onClick(View v){

        switch (v.getId()){
            case R.id.pppoe_btn_confirm:

                Intent intentBroadcast = new Intent(PPPOE_LOCAL_BROADCAST);
                sendBroadcast(intentBroadcast);
                mBinder.connect(PPPoEActivity.this);

                break;
            case R.id.pppoe_btn_cancel:
                break;
            default:
                break;
        }
    }

}
