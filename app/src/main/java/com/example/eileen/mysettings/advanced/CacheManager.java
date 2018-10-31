package com.example.eileen.mysettings.advanced;

import android.content.Context;
import android.text.format.Formatter;

import com.example.eileen.mysettings.utils.LogUtil;
import java.io.File;

public class CacheManager {


    private LogUtil logUtil = new LogUtil("mywipe");
    private Context mContext;
    private File mRomFolder;
    private File mExternalFolder;

    public CacheManager(Context context){

        this.mContext = context;
    }

    public String getTotalCacheSize() {
        String sCacheSize;
        long cacheSize = 0;

        try{
            mRomFolder = mContext.getCacheDir();
            mExternalFolder = mContext.getExternalCacheDir();

            cacheSize = getFolderSize(mRomFolder);
            if (mExternalFolder != null) {
                logUtil.logi("mExternalFolder 不为 null");
                cacheSize += getFolderSize(mExternalFolder);
            }
        }catch (Exception e){
            logUtil.loge("获取缓存文件夹异常");
        }


        sCacheSize = Formatter.formatFileSize(mContext, cacheSize);
        logUtil.logi("cache 总大小----" + cacheSize);
        return sCacheSize;

    }

    public void clearCache() {
        if (mRomFolder != null){
            boolean romIsDelete = deleteDir(mRomFolder);
            logUtil.logi("rom 删除结果" + romIsDelete);
        }

        if (mExternalFolder != null){
            boolean sdcardIsDelete = deleteDir(mExternalFolder);
            logUtil.logi("sdcard 删除结果" + sdcardIsDelete);
        }

    }


    private static boolean deleteDir(File file) {

        if (file.isDirectory()) {

            String[] children = file.list();

            for (int i = 0; i < children.length; i++) {

                boolean success = deleteDir(new File(file, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return file.delete();
    }



    public long getFolderSize(File file) {

        long size = 0;
        try {

            File[] fileList = file.listFiles(); //获取缓存目录下所有文件和目录的绝对路径，返回file数组

            for (File item : fileList){
                if (item.isDirectory()){
                    getFolderSize(item);
                }else {
                    size += item.length();
                }
            }
        } catch (Exception e) {
            logUtil.loge("CacheManager-getFolderSize() 出错----" + e.toString());
        }

        return size;
    }
}
