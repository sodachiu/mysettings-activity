package com.example.eileen.mysettings.utils;

import android.util.Log;

public class LogUtil {
    public static String TAG;

    public LogUtil(String tag){
        TAG = tag;
    }

    public void LOGI(String msLog)
    {

        Log.i(TAG, msLog);

    }



    public void LOGE(String msLog)
    {

        Log.e(TAG, msLog);

    }

}
