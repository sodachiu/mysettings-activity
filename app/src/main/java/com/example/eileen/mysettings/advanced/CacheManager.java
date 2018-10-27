package com.example.eileen.mysettings.advanced;

import android.content.Context;
import android.os.Environment;
import android.text.format.Formatter;

import com.example.eileen.mysettings.WipeCacheActivity;
import com.example.eileen.mysettings.utils.LogUtil;

import java.io.File;

public class CacheManager {


    private static LogUtil logUtil = new LogUtil("mycachemanager");
    public static String getTotalCacheSize(Context context) throws Exception {

        long cacheSize = getFolderSize(context.getCacheDir());

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

            cacheSize += getFolderSize(context.getExternalCacheDir());

        }

        return Formatter.formatFileSize(context, cacheSize);

    }

    public static void clearAllCache(Context context) {

        try {
            File romFile = context.getCacheDir();
            File externalFile = context.getExternalCacheDir();
            logUtil.logi("获取文件异常");
        }catch (Exception e){
            logUtil.logi("clearAllCache 获取文件异常");
        }


        deleteDir(context.getCacheDir());

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

            deleteDir(context.getExternalCacheDir());

        }

    }

    private static boolean deleteDir(File dir) {

        if (dir != null && dir.isDirectory()) {

            String[] children = dir.list();

            for (int i = 0; i < children.length; i++) {

                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }



    //格式化数据
    /*public static String getFormatSize(double size) {

        double kiloByte = size / 1024;

        if (kiloByte < 1) {

            return size + "Byte";

        }

        double megaByte = kiloByte / 1024;

        if (megaByte < 1) {

            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));

            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";

        }

        double gigaByte = megaByte / 1024;

        if (gigaByte < 1) {

            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));

            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";

        }

        double teraBytes = gigaByte / 1024;

        if (teraBytes < 1) {

            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));

            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";

        }

        BigDecimal result4 = new BigDecimal(teraBytes);

        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";

    }*/


    /*
    * 获取缓存目录下的所有文件容量大小
    * 参数 File file 为缓存目录
    * 返回文件大小
    *
    * */
    public static long getFolderSize(File file) throws Exception {

        long size = 0;
        try {

            File[] fileList = file.listFiles(); //获取缓存目录下所有文件和目录的绝对路径，返回file数组

            for (int i = 0; i < fileList.length; i++) {
                // 如果下面还有文件
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length(); //获取文件大小
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return size;
    }

//清理垃圾
class ClearCache implements Runnable {

    @Override

    public void run() {

        try {

            WipeCacheActivity.CacheDataManager.clearAllCache(mContext);

            Thread.sleep(1000);

            if (WipeCacheActivity.CacheDataManager.getTotalCacheSize(mContext).startsWith("0")) {

                mHandler.sendEmptyMessage(0);

            }

        } catch (Exception e) {

            return;

        }

    }
}
