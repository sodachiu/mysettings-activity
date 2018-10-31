package com.example.eileen.mysettings;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eileen.mysettings.advanced.CacheManager;
import com.example.eileen.mysettings.utils.LogUtil;


public class WipeCacheActivity extends AppCompatActivity
        implements View.OnClickListener{

    private TextView tvMenu;
    private Button btnWipeCache;
    private TextView tvCacheSize;
    private CacheManager cacheManager;
    private Context mContext;
    private LogUtil logUtil;
    private MyHandler mHanler;
    private static final int REFRESH_UI = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wipe_cache_activity);
        mContext = getApplicationContext();
        logUtil = new LogUtil("mywipe");
        cacheManager = new CacheManager(mContext);
        mHanler = new MyHandler();
        initView();

    }


    @Override
    public void onClick(View v){
        new Thread(new Runnable() {
            @Override
            public void run() {
                cacheManager.clearCache();
                Message msg = new Message();
                msg.what = REFRESH_UI;
                mHanler.sendMessage(msg);
            }
        }).start();
    }

    private class MyHandler extends Handler{
        @Override
        public void handleMessage(Message message){
            switch (message.what){
                case REFRESH_UI:
                    Toast.makeText(WipeCacheActivity.this,
                            "清理完成",
                            Toast.LENGTH_SHORT).show();
                    initView();
                    break;
                default:
                    break;

            }
        }
    }

    public void initView(){
        tvMenu = (TextView) findViewById(R.id.advanced);
        btnWipeCache = (Button) findViewById(R.id.advanced_btn_wipe);
        tvCacheSize = (TextView) findViewById(R.id.advanced_tv_cache_size);
        tvMenu.setBackgroundResource(R.drawable.menu_item_select);

        try {
            //设置当前的缓存垃圾
            String sCacheSize = cacheManager.getTotalCacheSize();
            tvCacheSize.setText(sCacheSize);

        } catch (Exception e) {

            e.printStackTrace();
            logUtil.logi("设置缓存垃圾出错");
            logUtil.logi(e.toString());
        }
        btnWipeCache.setOnClickListener(this);
    }


}
