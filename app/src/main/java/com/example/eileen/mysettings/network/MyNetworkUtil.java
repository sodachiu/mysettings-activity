package com.example.eileen.mysettings.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.eileen.mysettings.utils.LogUtil;

import java.util.regex.Pattern;



public class MyNetworkUtil {

    private static LogUtil logUtil = new LogUtil("mynetsettings");

    /*
    * 检查ip，网关，dns的格式是否合法
    * true：合法
    * false: 不合法
    * */
    public static boolean checkDhcpItem(String ip){
        logUtil.logi("MyNetworkUtil--->checkDhcpItem()");

        String ipRegEx = "^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}" +
                "(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";

        return Pattern.matches(ipRegEx, ip);

    }


    /*
    * 检查ip和网关是否在同一个网段
    * true: 在同一个网段
    * false：不在同一个网段
    * */
    public static boolean checkInOneSegment(String ip, String gateway){
        logUtil.logi("MyNetworkUtil--->isInOneSegment()");
        logUtil.logi("传入的值---->ip:" + ip + " gateway:" + gateway);
        try{
            String[] ips = ip.split("\\.");
            String[] gateways = gateway.split("\\.");
            for (int i = 0; i < 3; i++){
                if (!ips[i].equals(gateways[i])){
                    logUtil.logi("MyNetworkUtil--->不在同一网段，错误");
                    return false;
                }
            }
            logUtil.logi("MyNetworkUtil--->在同一网段，正确");
            return true;
        }catch (Exception e){
            logUtil.loge("分割字符串出错---->" + e.toString());
            return false;

        }
    }

    /*
    * 检查网络是否连接
    * */
    public static boolean checkNetAvailable(Context context){
        logUtil.logi("MyNetworkUtil--->checkNetAvailable(Context, context)");
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()){
            logUtil.logi("checkNetAvailable()---->网络可用");
            return true;
        }

        logUtil.logi("checkNetAvailable()---->网络不可用");
        return false;
    }

}
