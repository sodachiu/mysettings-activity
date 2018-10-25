package com.example.eileen.mysettings;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class PppoeConnectService extends Service {
    private static final String TAG = "PppoeConnectService";
    private PppoeConnectTask pppoeConnectTask;

    private PppoeConnectListener listener = new PppoeConnectListener() {
        @Override
        public void onProgress() {

            Toast.makeText(
                    PppoeConnectService.this,
                    "正在连接，请稍后",
                    Toast.LENGTH_SHORT
            ).show();

            Log.d(TAG, "onProgress: ");
        }

        @Override
        public void onSuccess() {
            Log.d(TAG, "onSuccess: ");
            Toast.makeText(
                    PppoeConnectService.this,
                    "连接成功",
                    Toast.LENGTH_SHORT
            ).show();
        }

        @Override
        public void onFail() {
            Toast.makeText(
                    PppoeConnectService.this,
                    "连接失败",
                    Toast.LENGTH_SHORT
            ).show();
            Log.d(TAG, "onFail: ");
        }

        @Override
        public void onCancel() {

            Log.d(TAG, "onCancel: ");
        }
    };

    private PppoeConnectBinder mBinder = new PppoeConnectBinder();
    @Override
    public IBinder onBind(Intent intent){
        return mBinder;
    }

    class PppoeConnectBinder extends Binder{
        public void connect(Context context){
            if (pppoeConnectTask == null){
                pppoeConnectTask = new PppoeConnectTask(listener);
                pppoeConnectTask.execute(context);
                Toast.makeText(PppoeConnectService.this,
                        "正在连接",
                        Toast.LENGTH_SHORT).show();
            }
        }

        public void disconnect(){
            if (pppoeConnectTask != null){
                pppoeConnectTask.cancelConnect();
            }
        }

    }
}
