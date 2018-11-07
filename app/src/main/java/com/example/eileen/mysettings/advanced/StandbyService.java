package com.example.eileen.mysettings.advanced;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.display.DisplayManager;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.example.eileen.mysettings.utils.LogUtil;
import com.hisilicon.android.hidisplaymanager.HiDisplayManager;

import java.util.Timer;
import java.util.TimerTask;

public class StandbyService extends Service {

    private LogUtil logUtil = new LogUtil("mystandby");
    public static final int THREAD_SLEEP_TIME = 300000;



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /*
    * 构造函数
    * */
    public StandbyService(){

    }


    @Override
    public void onCreate() {
        logUtil.logi("StandbyService---->onCreate()");
        super.onCreate();
    }


    @Override
    public int onStartCommand(Intent intent, int flag, int startId){
        logUtil.logi("StandbyService---->onStartCommand()");
        Timer powerTimer = new Timer();
        powerTimer.schedule(powerTimerTask, 0, 2000);
        return super.onStartCommand(intent, flag, startId);
    }

    @Override
    public void onDestroy(){
        logUtil.logi("StandbyService---->onDestroy()");
        super.onDestroy();
    }

    /*
    * TimerTask 会自动开启一个线程
    * 此为电视机待机后5min后自动待机的TimerTask
    * */
    private TimerTask powerTimerTask = new TimerTask() {

        @Override
        public void run() {
            logUtil.logi("StandbyService---->powerTimerTask");
            HiDisplayManager hdm = new HiDisplayManager();
            int tvState = hdm.getTVStatus(); //-1:未连接hdmi；0：电视待机；1：电视开机
            logUtil.logi("DisplayManager 获取到的电视状态---->" + tvState);

            if (tvState == -99){
                logUtil.logi("电视不支持 CEC or HotPlug 或 电视处于开机状态");
            }else if (tvState == -1){
                logUtil.logi("机顶盒未连接hdmi");
            }else if (tvState == 1){
                logUtil.logi("电视处于开机状态");
            }else if (tvState == 0){
                try{
                    Thread.sleep(THREAD_SLEEP_TIME);
                    logUtil.logi("线程睡眠时长---->" + THREAD_SLEEP_TIME);
                    tvState = hdm.getTVStatus();
                    if (tvState == 0 || tvState == -1){
                        Intent intentSendBroadcast = new Intent("android.intent.action.ACTION_SHUTDOWN");
                        sendBroadcast(intentSendBroadcast);
                        logUtil.logi("发送休眠广播成功");
                    }
                }catch (Exception e){
                    logUtil.logi("电视待机，机顶盒待机出错");
                }
            }else {
                logUtil.loge("！！！未知状态！！！");
            }

        }
    };
}
