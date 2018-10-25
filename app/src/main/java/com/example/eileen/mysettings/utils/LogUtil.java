package com.example.eileen.mysettings.utils;

import android.util.Log;

import com.example.eileen.mysettings.storage.StorageLog;

public class LogUtil {
    public static String TAG;

    public LogUtil(String tag){
        TAG = tag;
    }

    public void LOGI(String msLog)
    {

        Log.i(StorageLog.TAG, msLog);

    }

    public void LOGD(String msLog)
    {
        if (StorageLog.bDebugInfo) {
            Log.d(StorageLog.TAG, msLog);
        }
    }

    public void LOGE(String msLog)
    {

        Log.e(StorageLog.TAG, msLog);

    }

}
