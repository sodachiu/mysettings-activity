package com.example.eileen.mysettings;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.eileen.mysettings.datetime.DateFormat;
import com.example.eileen.mysettings.datetime.myDateAdapter;

import java.util.List;

public class DateFormatActivity extends AppCompatActivity {
    private TextView tvMenu;
    private RecyclerView rvDateFormat;
    private myDateAdapter mAdapter;
    private List<DateFormat> mFormatList;
    private String mNowFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.date_format_activity);
        tvMenu = (TextView) findViewById(R.id.date_time);
        rvDateFormat = (RecyclerView) findViewById(R.id.datetime_rv_date_formats);

        refreshList();


        Intent intent = getIntent();
        mNowFormat = intent.getStringExtra("now_format");
    }


    public void refreshList(){
        String[] dateFormats = getResources().getStringArray(R.array.date_format);
        for (String item : dateFormats){
            DateFormat tmpFormat = new DateFormat(item);
            if (mNowFormat.equals(item)){
                tmpFormat.setChecked(true);
            }
            tmpFormat.setChecked(false);
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mAdapter = new myDateAdapter(mFormatList);
        rvDateFormat.setLayoutManager(layoutManager);
        rvDateFormat.setAdapter(mAdapter);
        tvMenu.setBackgroundResource(R.drawable.menu_item_select);
    }

}
