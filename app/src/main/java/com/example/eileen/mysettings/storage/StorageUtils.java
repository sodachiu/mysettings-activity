package com.example.eileen.mysettings.storage;

import android.os.Environment;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.StatFs;
import android.os.storage.IMountService;

import java.io.File;
import java.lang.reflect.Method;

public class StorageUtils {

    static IMountService iMountService;

    public static void unMount(String path) {

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

    public StorageUtils(){

    }

    /*public static void mount() {
        try {
            iMountService.mountVolume(android.os.Environment.getExternalStorageDirectory().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    public static boolean fileIsExists(String path) {
        try {
            File f = new File(path);
            StorageLog.LOGI("f path >>>" + f.getAbsolutePath());
            if (!f.exists()) {
                StorageLog.LOGI("文件夹不存在" );

                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 获得机身内部存储总大小
     * 总ROM 获取到的是4.20GB
     *
     * @return
     */
    public static long getRomTotalSize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSizeLong();
        long totalBlocks = stat.getBlockCountLong();
        return blockSize * totalBlocks;
    }

    /**
     * 获得内存存储可用空间
     * ROM
     *
     * @return
     */
    public static long getRomAvailableSize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSizeLong();
        long availableBlocks = stat.getAvailableBlocksLong();
        return blockSize * availableBlocks;
        //return Formatter.formatFileSize(mContext, freeSize);
    }

    /**
     * 获得外部存储总大小
     *
     * @return
     */
    public static long getExternalTotalSize(String strpath) {
        String path = strpath;
        StatFs stat = new StatFs(path);
        long blockSize = stat.getBlockSizeLong();
        long totalBlocks = stat.getBlockCountLong();
        return blockSize * totalBlocks;
    }

    /**
     * 获得外部存储可用空间
     * U盘
     *
     * @return
     */
    public static long getExternalAvailableSize(String strpath) {
        String path = strpath;
        StatFs stat = new StatFs(path);
        long blockSize = stat.getBlockSizeLong();
        long availableBlocks = stat.getAvailableBlocksLong();
        return blockSize * availableBlocks;
    }

}
