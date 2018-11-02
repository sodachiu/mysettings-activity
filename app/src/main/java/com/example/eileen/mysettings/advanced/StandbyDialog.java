package com.example.eileen.mysettings.advanced;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.eileen.mysettings.R;
import com.example.eileen.mysettings.utils.LogUtil;

public class StandbyDialog {

    private String desc;
    private Context mContext;
    private AlertDialog build;
    private Activity mActivity;
    private View mView;
    LogUtil logUtil = new LogUtil("myalertdialog");

    public StandbyDialog(String desc, Context context){
        logUtil.logi("我进了构造函数");
        this.desc = desc;
        this.mContext = context;
        this.build = new AlertDialog.Builder(mContext).create();
        this.mActivity = (Activity) mContext;
        this.mView = mActivity.getLayoutInflater().inflate(R.layout.alert_dialog, null);
        build.setView(mView, 0, 0, 0, 0);

    }

    public void showOneDialog() {

        build.show();
        int width = mActivity.getWindowManager().getDefaultDisplay().getWidth();
        WindowManager.LayoutParams params = build.getWindow().getAttributes();
        params.width = width - (width / 6);
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.CENTER;
        build.getWindow().setAttributes(params);
        Button btnContinue = (Button) mView.findViewById(R.id.standby_btn_cancel);
        TextView tvWarnMsg = (TextView) mView.findViewById(R.id.standby_tv_timer);
        tvWarnMsg.setText(desc);

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //这里写取消待机，停止倒计时。
                logUtil.logi("点击了左边的按钮");
                build.dismiss();
            }
        });

    }
}
