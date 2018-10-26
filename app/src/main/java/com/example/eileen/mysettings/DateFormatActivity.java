package com.example.eileen.mysettings;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.eileen.mysettings.utils.LogUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateFormatActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView tvMenu;
    private TextView tvFormat1, tvFormat2, tvFormat3, tvFormat4;
    private Button btnFormat1, btnFormat2, btnFormat3, btnFormat4;
    private LinearLayout llFormat1, llFormat2, llFormat3, llFormat4;
    private static int mPosition; //标记当前格式的位置
    private String[] mDateFormats;
    private LogUtil logUtil = new LogUtil("mydatetime");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.date_format_activity);
        initView();

    }

    @Override
    protected void onPause(){
        super.onPause();
        Intent intent = new Intent(Intent.ACTION_TIME_CHANGED);
        sendBroadcast(intent);
    }

    public void initView(){
        tvMenu = (TextView) findViewById(R.id.date_time);
        tvFormat1 = (TextView) findViewById(R.id.datetime_tv_format1);
        tvFormat2 = (TextView) findViewById(R.id.datetime_tv_format2);
        tvFormat3 = (TextView) findViewById(R.id.datetime_tv_format3);
        tvFormat4 = (TextView) findViewById(R.id.datetime_tv_format4);
        btnFormat1 = (Button) findViewById(R.id.datetime_btn_format1);
        btnFormat2 = (Button) findViewById(R.id.datetime_btn_format2);
        btnFormat3 = (Button) findViewById(R.id.datetime_btn_format3);
        btnFormat4 = (Button) findViewById(R.id.datetime_btn_format4);
        llFormat1 = (LinearLayout) findViewById(R.id.datetime_ll_format1);
        llFormat2 = (LinearLayout) findViewById(R.id.datetime_ll_format2);
        llFormat3 = (LinearLayout) findViewById(R.id.datetime_ll_format3);
        llFormat4 = (LinearLayout) findViewById(R.id.datetime_ll_format4);

        llFormat1.setOnClickListener(this);
        llFormat2.setOnClickListener(this);
        llFormat3.setOnClickListener(this);
        llFormat4.setOnClickListener(this);
        tvMenu.setBackgroundResource(R.drawable.menu_item_select);

        Intent intent = getIntent();
        int position = intent.getIntExtra("format_position", -1);
        logUtil.LOGI("传过来的位置---->" + position);
        mDateFormats = getResources().getStringArray(R.array.date_format);
        tvFormat1.setText("区域(" + formatDate(mDateFormats[0]) + ")");
        tvFormat2.setText(formatDate(mDateFormats[1]));
        tvFormat3.setText(formatDate(mDateFormats[2]));
        tvFormat4.setText(formatDate(mDateFormats[3]));

        changeFormat(position);
    }

    public String formatDate(String format){
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        Calendar calendar = Calendar.getInstance();
        return dateFormat.format(calendar.getTime());
    }
    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.datetime_ll_format1:
                changeFormat(0);
                break;
            case R.id.datetime_ll_format2:
                changeFormat(1);
                break;
            case R.id.datetime_ll_format3:
                changeFormat(2);
                break;
            case R.id.datetime_ll_format4:
                changeFormat(3);
                break;
            default:
                break;
        }
    }

    public void changeFormat(int position){

        switch (position){
            case 0:
                logUtil.LOGI("然后我进了0");
                btnFormat1.setBackgroundResource(R.drawable.checkbox_on);
                btnFormat2.setBackgroundResource(R.drawable.checkbox_off);
                btnFormat3.setBackgroundResource(R.drawable.checkbox_off);
                btnFormat4.setBackgroundResource(R.drawable.checkbox_off);
                android.provider.Settings.System.putString(getContentResolver(),
                        android.provider.Settings.System.DATE_FORMAT, mDateFormats[0]);
                mPosition = 0;
                break;
            case 1:
                logUtil.LOGI("然后我进了1");
                btnFormat1.setBackgroundResource(R.drawable.checkbox_off);
                btnFormat2.setBackgroundResource(R.drawable.checkbox_on);
                btnFormat3.setBackgroundResource(R.drawable.checkbox_off);
                btnFormat4.setBackgroundResource(R.drawable.checkbox_off);
                android.provider.Settings.System.putString(getContentResolver(),
                        android.provider.Settings.System.DATE_FORMAT, mDateFormats[1]);
                mPosition = 1;
                break;
            case 2:
                btnFormat1.setBackgroundResource(R.drawable.checkbox_off);
                btnFormat2.setBackgroundResource(R.drawable.checkbox_off);
                btnFormat3.setBackgroundResource(R.drawable.checkbox_on);
                btnFormat4.setBackgroundResource(R.drawable.checkbox_off);
                android.provider.Settings.System.putString(getContentResolver(),
                        android.provider.Settings.System.DATE_FORMAT, mDateFormats[2]);
                mPosition = 2;
                break;
            case 3:
                btnFormat1.setBackgroundResource(R.drawable.checkbox_off);
                btnFormat2.setBackgroundResource(R.drawable.checkbox_off);
                btnFormat3.setBackgroundResource(R.drawable.checkbox_off);
                btnFormat4.setBackgroundResource(R.drawable.checkbox_on);
                android.provider.Settings.System.putString(getContentResolver(),
                        android.provider.Settings.System.DATE_FORMAT, mDateFormats[3]);
                mPosition = 3;
                break;
            default:
                break;

        }
    }

}
