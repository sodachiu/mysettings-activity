package com.example.eileen.mysettings;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

public class ReturnDataUtil {
    private static final int STORE_INFO = 100;
    private static final int RES_FACTORY = 99;
    private static final String TAG = "ReturnDataUtil";

    private static Activity activity = ActivityCollector.getTopActivity();
    private static int ok = activity.RESULT_OK;
    private static int cancel = activity.RESULT_CANCELED;
    public static void disposeOfData(int requestCode, int resultCode, Intent data){
        switch (requestCode){
            case STORE_INFO:
                if (resultCode == ok){
                    //做确定处理
                    Log.d(TAG, "onActivityResult: store_info 确定" + ok);
                }else{
                    Log.d(TAG, "onActivityResult: store_info 取消" + cancel);
                }
                break;
            case RES_FACTORY:
                if (resultCode == ok){
                    Log.d(TAG, "disposeOfData: res_factory 确定" + ok);
                }else{
                    Log.d(TAG, "disposeOfData: res_factory 取消" + cancel);
                }
                break;

            default:
                break;
        }
    }
}
