package com.example.eileen.mysettings;

import android.content.Intent;
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

    private Calendar mDummyDate;
    private Calendar nowDate;
    private boolean is24HourFormat;
    private String[] dateFormats;
    private String[] dateFormatteds;



    private static final String TAG = "mydatetime";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.date_time_activity);
        tvMenu = (TextView) findViewById(R.id.date_time);
        llTimeFormat = (LinearLayout) findViewById(R.id.datetime_ll_time_format);
        llDateFormat = (LinearLayout) findViewById(R.id.datetime_ll_date_format);
        btnUse24hFormat = (Button) findViewById(R.id.datetime_btn_time_format);
        tvTime = (TextView) findViewById(R.id.datetime_tc_time_format);
        tvDate = (TextView) findViewById(R.id.datetime_tc_date_format);

        tvMenu.setFocusable(true);
        tvMenu.setBackgroundResource(R.drawable.menu_focus_selector);

        llTimeFormat.setOnClickListener(this);
        llDateFormat.setOnClickListener(this);
        btnUse24hFormat.setOnClickListener(this);
        tvMenu.setOnKeyListener(this);

    }

    @Override
    protected void onResume(){
        super.onResume();
        llTimeFormat.setFocusable(true);
        llDateFormat.setFocusable(true);
        initView();
        String[] dateFormats = getResources().getStringArray(R.array.date_format);
        Log.i(TAG, dateFormats[0]);

    }

    @Override
    protected void onPause(){
        super.onPause();
    }



    @Override
    public void onClick(View v){
        Intent intent;
        switch (v.getId()){
            case R.id.datetime_ll_time_format:
                break;
            case R.id.datetime_ll_date_format:
                intent = new Intent(DateTimeActivity.this, DateFormatActivity.class);
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


    public void initView(){
        nowDate = Calendar.getInstance();
        boolean is24hFormat = DateFormat.is24HourFormat(DateTimeActivity.this);
        String sTime = "";
        if (is24hFormat){
            btnUse24hFormat.setBackgroundResource(R.drawable.checkbox_on);
            SimpleDateFormat dateFormat = new SimpleDateFormat("H:mm");
            sTime =  dateFormat.format(nowDate.getTime());
            Log.i(TAG, dateFormat.toLocalizedPattern());
        }else {
            btnUse24hFormat.setBackgroundResource(R.drawable.checkbox_off);
            SimpleDateFormat dateFormat = new SimpleDateFormat("aa h:mm");
            dateFormat.format(nowDate.getTime());
            Log.i(TAG, dateFormat.getDateFormatSymbols().toString());

        }
        tvTime.setText(sTime);

    }

}
