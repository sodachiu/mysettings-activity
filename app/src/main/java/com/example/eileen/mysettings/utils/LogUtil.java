package com.example.eileen.mysettings.utils;

import android.util.Log;

public class LogUtil {
    public static String TAG;

    public LogUtil(String tag){
        TAG = tag;
    }

    public void logi(String msLog)
    {

        Log.i(TAG, msLog);

    }

    public void loge(String msLog)
    {

        Log.e(TAG, msLog);

    }

}
