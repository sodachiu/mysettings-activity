package com.example.eileen.mysettings.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.NetworkUtils;
import android.net.ethernet.EthernetManager;
import android.net.pppoe.PppoeManager;

import com.example.eileen.mysettings.R;
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

    /*
    *
    * 返回当前网络信息
    * */

    public static class MyDhcpInfo{
        public String ipAddress, netMask, gateway, dns1, dns2;
        private EthernetManager em;
        private PppoeManager pm;
        private DhcpInfo dhcpInfo;
        private String defaultValue;

        public MyDhcpInfo(Context context){

            em = (EthernetManager) context.getSystemService(Context.ETHERNET_SERVICE);
            pm = (PppoeManager) context.getSystemService(Context.PPPOE_SERVICE);
            String ethMode = em.getEthernetMode();
            defaultValue = context.getResources().getString(R.string.net_default_text);

            ipAddress = defaultValue;
            netMask = defaultValue;
            gateway = defaultValue;
            dns1 = defaultValue;
            dns2 = defaultValue;

            if (ethMode.equals(EthernetManager.ETHERNET_CONNECT_MODE_PPPOE)){
                this.dhcpInfo = pm.getDhcpInfo();
            }else {
                this.dhcpInfo = em.getDhcpInfo();
            }

            if (dhcpInfo != null){
                this.ipAddress = NetworkUtils.intToInetAddress(dhcpInfo.ipAddress).getHostAddress();
                this.netMask = NetworkUtils.intToInetAddress(dhcpInfo.netmask).getHostAddress();
                this.gateway = NetworkUtils.intToInetAddress(dhcpInfo.gateway).getHostAddress();
                this.dns1 = NetworkUtils.intToInetAddress(dhcpInfo.dns1).getHostAddress();
                this.dns2 = NetworkUtils.intToInetAddress(dhcpInfo.dns2).getHostAddress();
            }
        }

        public EthernetManager getEthernetManager(){
            return this.em;
        }

        public PppoeManager getPPPoEManager() {
            return this.pm;
        }


        public DhcpInfo getDhcpInfo(){
            return this.dhcpInfo;
        }

        public String getDefaultValue(){
            return this.defaultValue;
        }

    }

}
