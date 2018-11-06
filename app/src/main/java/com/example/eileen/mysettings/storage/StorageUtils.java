package com.example.eileen.mysettings.storage;

import android.os.Environment;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.StatFs;
import android.os.storage.IMountService;

import com.example.eileen.mysettings.utils.LogUtil;

import java.io.File;
import java.lang.reflect.Method;

public class StorageUtils {

    private static LogUtil logUtil = new LogUtil("mystorage");

    /*
    *
    * 卸载设备
    * */
    public static void unMount(String path) {
        logUtil.logi("unMount()");
        IMountService iMountService = null;
        try {
            Method method = Class.forName("android.os.ServiceManager").getMethod("getService", String.class);
            IBinder binder = (IBinder) method.invoke(null, "mount");
            iMountService = IMountService.Stub.asInterface(binder);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            iMountService.unmountVolume(path, true, true);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static boolean fileIsExists(String path) {
        logUtil.logi("fileIsExists()");
        try {
            File f = new File(path);
            if (!f.exists()) {
                logUtil.logi(path + "不存在");
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        logUtil.logi(path + "存在");
        return true;
    }

    /**
     * 获得机身内部存储总大小
     *
     */
    public static long getRomTotalSize() {
        logUtil.logi("getRomTotalSize()");
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSizeLong();
        long totalBlocks = stat.getBlockCountLong();
        return blockSize * totalBlocks;
    }

    /**
     * 获得内存存储可用空间
     * ROM
     */
    public static long getRomAvailableSize() {
        logUtil.logi("getRomAvailableSize()");
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSizeLong();
        long availableBlocks = stat.getAvailableBlocksLong();
        return blockSize * availableBlocks;
    }

    /**
     * 获得外部存储总大小
     *
     */
    public static long getExternalTotalSize(String strpath) {
        logUtil.logi("getExternalTotalSize()");
        String path = strpath;
        StatFs stat = new StatFs(path);
        long blockSize = stat.getBlockSizeLong();
        long totalBlocks = stat.getBlockCountLong();
        return blockSize * totalBlocks;
    }

    /**
     * 获得外部存储可用空间
     *
     */
    public static long getExternalAvailableSize(String strpath) {
        logUtil.logi("getExternalAvailableSize()");
        String path = strpath;
        StatFs stat = new StatFs(path);
        long blockSize = stat.getBlockSizeLong();
        long availableBlocks = stat.getAvailableBlocksLong();
        return blockSize * availableBlocks;
    }

}
