package com.example.eileen.mysettings;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyReceiver  extends BroadcastReceiver{
    private Context mContext;
    private Activity mActivity;
    @Override
    public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action.equals("com.cbox.action.autosleep")){
                Intent tmpIntent = new Intent(mContext, StandbyDialog.class);
                mActivity.startActivity(tmpIntent);

            }
    }

    public MyReceiver(Context context){
        this.mContext = context;
        this.mActivity = (Activity) mContext;
    }
}
