package com.example.eileen.mysettings.bluetooth;

import android.util.Log;

public class BluetoothLog {

    public static boolean bDebugInfo = true;
    public static String TAG = "mybluetooth";

    public static void LOGI(String msLog)
    {

        Log.i(BluetoothLog.TAG, msLog);

    }

    public static void LOGD(String msLog)
    {
        if (BluetoothLog.bDebugInfo) {
            Log.d(BluetoothLog.TAG, msLog);
        }
    }

    public static void LOGE(String msLog)
    {

        Log.e(BluetoothLog.TAG, msLog);

    }
}
