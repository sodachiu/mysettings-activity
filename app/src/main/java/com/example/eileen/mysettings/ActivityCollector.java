package com.example.eileen.mysettings;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

public class ActivityCollector {

    public static List<Activity> activityList = new ArrayList<>();

    public static void addActivity(Activity activity){
        activityList.add(activity);
    }

    public static void removeActivity(Activity activity){
        activityList.remove(activity);
    }

    public static void finishAll(){
        for (Activity item : activityList){
            if (!item.isFinishing()){
                item.finish();
            }
        }
        activityList.clear();
    }

    //获取当前活动
    public static Activity getTopActivity(){
        int lastIndex = activityList.size() - 1;
        return activityList.get(lastIndex);
    }

}
