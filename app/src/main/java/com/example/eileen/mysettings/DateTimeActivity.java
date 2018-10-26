package com.example.eileen.mysettings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.videoeditor.Transition;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView;

import com.example.eileen.mysettings.utils.ActivityId;
import com.example.eileen.mysettings.utils.LogUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimeActivity extends AppCompatActivity
implements View.OnKeyListener, View.OnClickListener{

    private TextView tvMenu;
    private LinearLayout llTimeFormat;
    private LinearLayout llDateFormat;
    private Button btnUse24hFormat;
    private TextView tvTime;
    private TextView tvDate;
    private TextView tvServer1;
    private TextView tvServer2;

    private String[] mFormatList;
    private boolean is24hFormat;
    private String mDateFormat; 
    private LogUtil logUtil = new LogUtil("mydatetime");
    private static final String HOURS_12 = "12";
    private static final String HOURS_24 = "24";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.date_time_activity);
        mFormatList = getResources().getStringArray(R.array.date_format);
        findView();
    }

    @Override
    protected void onResume(){
        super.onResume();
        initView();

    }

    public void findView(){
        tvMenu = (TextView) findViewById(R.id.date_time);
        llTimeFormat = (LinearLayout) findViewById(R.id.datetime_ll_time_format);
        llDateFormat = (LinearLayout) findViewById(R.id.datetime_ll_date_format);
        btnUse24hFormat = (Button) findViewById(R.id.datetime_btn_time_format);
        tvTime = (TextView) findViewById(R.id.datetime_tc_time_format);
        tvDate = (TextView) findViewById(R.id.datetime_tc_date_format);
        tvServer1 = (TextView) findViewById(R.id.datetime_tv_server1);
        tvServer2 = (TextView) findViewById(R.id.datetime_tv_server2);
        tvMenu.setFocusable(true);
        tvMenu.setBackgroundResource(R.drawable.menu_focus_selector);
        tvMenu.setOnKeyListener(this);

        String sServer1 = android.provider.Settings.Secure.getString(getContentResolver(), "ntp_server");
        String sServer2 = android.provider.Settings.Secure.getString(getContentResolver(), "ntp_server2");

        if (sServer1 == null){
            sServer1 = "";
        }
        if (sServer2 == null){
            sServer2 = "";
        }

        tvServer1.setText(sServer1);
        tvServer2.setText(sServer2);

    }



    public void initView(){

        llTimeFormat.setFocusable(true);
        llDateFormat.setFocusable(true);
        llTimeFormat.setOnClickListener(this);
        llDateFormat.setOnClickListener(this);
        btnUse24hFormat.setOnClickListener(this);
        is24hFormat = DateFormat.is24HourFormat(DateTimeActivity.this);
        String sTime; //当前时间
        String sDate; //当前日期
        Calendar nowDate = Calendar.getInstance();

        if (is24hFormat){
            btnUse24hFormat.setBackgroundResource(R.drawable.checkbox_on);
            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm");
            sTime =  timeFormat.format(nowDate.getTime());
        }else {
            btnUse24hFormat.setBackgroundResource(R.drawable.checkbox_off);
            SimpleDateFormat timeFormat = new SimpleDateFormat("aa h:mm");
            sTime = timeFormat.format(nowDate.getTime());
        }
        tvTime.setText(sTime);

        mDateFormat = android.provider.Settings.System.getString(
                getContentResolver(), android.provider.Settings.System.DATE_FORMAT);
        if (mDateFormat == null || mDateFormat.equals("")){
            mDateFormat = mFormatList[0];
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(mDateFormat);
        sDate = dateFormat.format(nowDate.getTime());
        tvDate.setText(sDate);

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK); //系统每分钟发一次广播
        filter.addAction(Intent.ACTION_TIME_CHANGED); //时间改变,例如手动修改设置里的时间
        registerReceiver(timeReceiver, filter);

    }

    private BroadcastReceiver timeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (!Intent.ACTION_TIME_TICK.equals(action) &&
                    !Intent.ACTION_TIME_CHANGED.equals(action)){
                logUtil.LOGI(action + "不是正确的广播");
                return;
            }

            initView();
        }
    };

    @Override
    protected void onDestroy(){
        super.onDestroy();
        unregisterReceiver(timeReceiver);
    }

    @Override
    public void onClick(View v){
        Intent intent;
        switch (v.getId()){
            case R.id.datetime_ll_time_format:
                is24hFormat = !is24hFormat;
                set24Hour(is24hFormat);
                initView();
                break;
            case R.id.datetime_ll_date_format:
                intent = new Intent(DateTimeActivity.this, DateFormatActivity.class);
                int position = getPosition(mDateFormat);
                intent.putExtra("format_position", position);
                startActivity(intent);
                break;
            default:
                break;

        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event){

        if (event.getAction() == KeyEvent.ACTION_DOWN){
            Intent intent;
            switch (keyCode){
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    intent = new Intent(DateTimeActivity.this, DisplayActivity.class);
                    startActivity(intent);
                    break;
                case KeyEvent.KEYCODE_DPAD_UP:
                    llTimeFormat.setFocusable(false);
                    llDateFormat.setFocusable(false);
                    intent = new Intent(DateTimeActivity.this, NetInfoActivity.class);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
        return false;
    }


    public int getPosition(String format){

        int position = 0;
        for (int i = 0; i < mFormatList.length; i++){
            if (format.equals(mFormatList[i])){
                position = i;
                break;
            }
        }

        return position;
    }

    private void set24Hour(boolean is24Hour) {
        android.provider.Settings.System.putString(getContentResolver(),
                android.provider.Settings.System.TIME_12_24,
                is24Hour ? HOURS_24 : HOURS_12);
    }

}
