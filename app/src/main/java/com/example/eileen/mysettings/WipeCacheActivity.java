package com.example.eileen.mysettings;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.math.BigDecimal;

public class WipeCacheActivity extends AppCompatActivity
        implements View.OnClickListener{

    private TextView tvMenu;
    private Button btnWipeCache;
    private TextView tvCacheSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wipe_cache_activity);

    }


    @Override
    public void onClick(View v){


        Toast.makeText(WipeCacheActivity.this,"清理完成", Toast.LENGTH_SHORT).show();
    }

    public void initView(){
        tvMenu = (TextView) findViewById(R.id.advanced);
        btnWipeCache = (Button) findViewById(R.id.advanced_btn_wipe);
        tvCacheSize = (TextView) findViewById(R.id.advanced_tv_cache_size);
        tvMenu.setBackgroundResource(R.drawable.menu_item_select);

        try {
            //设置当前的缓存垃圾
            String sCacheSize = CacheDataManager.getTotalCacheSize(getApplicationContext());
            tv1.setText(CacheDataManager.getTotalCacheSize(mContext));
            Log.i("Cacheee", CacheDataManager.getTotalCacheSize(mContext));

        } catch (Exception e) {

            e.printStackTrace();
            Log.i("SSSS", e.toString());
        }
        btnWipeCache.setOnClickListener(this);
    }


    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    Toast.makeText(mContext, "清理完成", Toast.LENGTH_SHORT).show();
                    try {
                        Log.i("Cacheee after", CacheDataManager.getTotalCacheSize(mContext));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {

                        tv1.setText(CacheDataManager.getTotalCacheSize(mContext));

                    } catch (Exception e) {

                        e.printStackTrace();

                    }
                    break;
                default:
                    break;
            }
        }
    };
}
