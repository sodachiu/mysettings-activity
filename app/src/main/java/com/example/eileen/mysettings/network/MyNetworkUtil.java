package com.example.eileen.mysettings.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.NetworkUtils;
import android.net.ethernet.EthernetManager;
import android.net.pppoe.PppoeManager;
import android.util.Log;

import com.example.eileen.mysettings.R;
import com.example.eileen.mysettings.utils.LogUtil;

import java.util.regex.Pattern;



public class MyNetworkUtil {

    private static final String TAG = "qll_mynetworkutil";

    /*
    * 检查ip，网关，dns的格式是否合法
    * true：合法
    * false: 不合法
    * */
    public static boolean checkDhcpItem(String ip){

        Log.i(TAG, "checkDhcpItem: ");
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
        Log.i(TAG, "checkInOneSegment: ip---->" + ip + " gateway---->" + gateway);
        try{
            String[] ips = ip.split("\\.");
            String[] gateways = gateway.split("\\.");
            for (int i = 0; i < 3; i++){
                if (!ips[i].equals(gateways[i])){
                    Log.i(TAG, "checkInOneSegment: 不在同一网段，错误");
                    return false;
                }
            }
            Log.i(TAG, "checkInOneSegment: 同一网段，正确");
            return true;
        }catch (Exception e){
            Log.i(TAG, "checkInOneSegment: 分割字符出错");
            return false;

        }
    }

    /*
    * 检查网络是否连接
    * */
    public static boolean checkNetAvailable(Context context){

        Log.i(TAG, "checkNetAvailable: ");
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()){
            Log.i(TAG, "checkNetAvailable: 网络可用");
            return true;
        }

        Log.i(TAG, "checkNetAvailable: 网络不可用");
        return false;
    }

    public static class MyDhcpInfo{
        public String ipAddress, netMask, gateway, dns1, dns2;
        private EthernetManager em;
        private PppoeManager pm;
        private DhcpInfo dhcpInfo;
        private String defaultValue;

        public MyDhcpInfo(Context context, boolean isNetAvailable){
            Log.i(TAG, "MyDhcpInfo: ");
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
                Log.i(TAG, "MyDhcpInfo: 当前网络连接模式为PPPOE，dhcp的信息");
                this.dhcpInfo = pm.getDhcpInfo();
            }else {
                Log.i(TAG, "MyDhcpInfo: 当前网络连接模式为DHCP或Static，获取dhcp的信息");
                this.dhcpInfo = em.getDhcpInfo();
            }

            Log.i(TAG, "MyDhcpInfo: dhcpInfo是否为空---->" + (dhcpInfo == null));
            if (dhcpInfo != null && isNetAvailable){
                Log.i(TAG, "MyDhcpInfo: dhcpInfo不为空，进行信息设置");
                this.ipAddress = NetworkUtils.intToInetAddress(dhcpInfo.ipAddress).getHostAddress();
                this.netMask = NetworkUtils.intToInetAddress(dhcpInfo.netmask).getHostAddress();
                this.gateway = NetworkUtils.intToInetAddress(dhcpInfo.gateway).getHostAddress();
                this.dns1 = NetworkUtils.intToInetAddress(dhcpInfo.dns1).getHostAddress();
                this.dns2 = NetworkUtils.intToInetAddress(dhcpInfo.dns2).getHostAddress();
            }
        }

        public EthernetManager getEthernetManager(){
            Log.i(TAG, "getEthernetManager: ");
            return this.em;
        }

        public PppoeManager getPPPoEManager() {
            Log.i(TAG, "getPPPoEManager: ");
            return this.pm;
        }


        public DhcpInfo getDhcpInfo(){
            Log.i(TAG, "getDhcpInfo: ");
            return this.dhcpInfo;
        }

        public String getDefaultValue(){
            Log.i(TAG, "getDefaultValue: ");
            return this.defaultValue;
        }

    }

}
