package com.example.eileen.mysettings;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.eileen.mysettings.utils.LogUtil;

public class StandbyDialog extends Activity implements View.OnClickListener{
    private Button btnContinue;
    private TextView tvTimer;
    private LogUtil logUtil = new LogUtil("standbydialog");

    private static final int TOTAL_TIME = 60000;
    private static final int ONCE_TIME = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_standby);
        initView();
        countDownTimer.start();
    }

    private void initView(){
        btnContinue = (Button) findViewById(R.id.advanced_btn_not_standby);
        tvTimer = (TextView) findViewById(R.id.standby_tv_timer);
        btnContinue.setOnClickListener(this);
    }

    public void onClick(View v){
        countDownTimer.cancel();
//        SystemProperties.set("persist.sys.suspend.noop.time", "");
        finish();
    }

    private CountDownTimer countDownTimer = new CountDownTimer(TOTAL_TIME, ONCE_TIME) {
        @Override
        public void onTick(long millisUntilFinished) {
            String value = String.valueOf((int) (millisUntilFinished / 1000));
            tvTimer.setText(value);
        }

        @Override
        public void onFinish() {
            //进入待机

        }
    };

}
