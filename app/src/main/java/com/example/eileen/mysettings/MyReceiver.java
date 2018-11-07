package com.example.eileen.mysettings;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.eileen.mysettings.utils.LogUtil;

public class MyReceiver  extends BroadcastReceiver{
    private Context mContext;
    private LogUtil logUtil = new LogUtil("myreceiver");

    private static final int TOTAL_TIME = 60000;
    private static final int ONCE_TIME = 1000;
    private static final String AUTO_SLEEP = "com.cbox.action.autosleep";
    private static final String ACTION_SHUTDOWN = "android.intent.action.ACTION_SHUTDOWN";

    private TextView tvTimer;



    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        String action = intent.getAction();

        if (action.equals(AUTO_SLEEP)){
            logUtil.logi("收到广播，四小时没有任何操作，执行待机");
            handleStandby();

        }else if (action.equals(ACTION_SHUTDOWN)){
            logUtil.logi("收到了电视机待机的广播，立即执行待机操作");
            PowerManager pm = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
            pm.goToSleep(SystemClock.uptimeMillis());
        }

    }



    public void handleStandby(){
        logUtil.logi("handleStandby()");
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.alert_dialog, null);
        Button btnCancel = (Button) view.findViewById(R.id.standby_btn_cancel);
        tvTimer = (TextView) view.findViewById(R.id.standby_tv_timer);

        logUtil.logi("取到view了");
        final WindowManager windowManager = (WindowManager) mContext.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();

        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        params.format = PixelFormat.RGBA_8888;

        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;

        logUtil.logi("params 的宽高设置成功");

        params.gravity = Gravity.CENTER;
        /*params.x = 200;
        params.y = 200;*/
        logUtil.logi("params 的x,y设置成功");
        logUtil.logi(params.gravity + "对齐方式");
        windowManager.addView(view, params);
        countDownTimer.start();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                windowManager.removeView(view);
                countDownTimer.cancel();
            }
        });

        logUtil.logi("我出了handleStandby()");
    }

    private CountDownTimer countDownTimer = new CountDownTimer(TOTAL_TIME, ONCE_TIME) {
        @Override
        public void onTick(long millisUntilFinished) {
            String value = String.valueOf((int) (millisUntilFinished / 1000));
            value = mContext.getResources().getString(R.string.count_down) + value;
            tvTimer.setText(value);
            logUtil.logi("执行一次countTimer");
        }

        @Override
        public void onFinish() {
            //进入待机

        }
    };
}
