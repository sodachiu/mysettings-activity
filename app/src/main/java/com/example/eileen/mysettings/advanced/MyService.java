package com.example.eileen.mysettings.advanced;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.display.DisplayManager;
import android.util.Log;

import com.example.eileen.mysettings.utils.LogUtil;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MyService extends Service {
    private LogUtil logUtil = new LogUtil("myservice");
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate(){
        super.onCreate();
        logUtil.logi("myservice被创建");
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId){

       logUtil.logi("myservice启动了");
        final DisplayManager dm = (DisplayManager) getSystemService(
                Context.DISPLAY_MANAGER_SERVICE);

        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                final int tvStatus = dm.getTVState(); //0：电视待机；1：电视开机；-1：未连接hdmi

                /*
                * 有个bug啊这个逻辑，进入线程了，定时被取消了，那我在五分钟之内又开机了咋个办
                * 还要再思考一下，然后就是，现在电视机不支持CEC or HotPlug，哦多克
                * 暂时把未连接hdmi放到待机里，测试一下功能，功能ok
                * */
                if (tvStatus == 0 || tvStatus == -1){
                    timer.cancel();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(10000);//测试用10s,实际需要改成5min
                                Intent intentSendBroadcast = new Intent("android.intent.action.ACTION_SHUTDOWN");
                                sendBroadcast(intentSendBroadcast);
                                logUtil.logi("tvStatus==0, 发送广播成功");
                            }catch (Exception e){
                                logUtil.logi("tvStatus==0, 待机失败" + e.toString());

                            }
                        }
                    }).start();
                }else if (tvStatus == 1){
                    logUtil.logi("电视开机");
                }else if (tvStatus == -99){
                    logUtil.logi("电视不支持CEC or HotPlug");
                }else {
                    logUtil.logi("位置状态tvstatus----" + tvStatus);
                }

            }
        }, 0, 2000);


        logUtil.logi("我走完启动的流程了");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        logUtil.logi("myservice被销毁");
    }
}
