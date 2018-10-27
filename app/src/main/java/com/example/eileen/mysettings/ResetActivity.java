package com.example.eileen.mysettings;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.eileen.mysettings.utils.ActivityCollector;
import com.example.eileen.mysettings.utils.ActivityId;
import com.example.eileen.mysettings.utils.QuitActivity;

public class ResetActivity extends QuitActivity
        implements View.OnClickListener, View.OnKeyListener{

    private static final int RES_FACTORY = 99;
    private TextView resFactory;
    private EditText password;
    private Button confirm;
    private LinearLayout layout;

    private static final String TAG = "myreset";
    private static final String MASTER_CLEAR = "android.intent.action.MASTER_CLEAR";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recovery_system_activity);
        resFactory = (TextView) findViewById(R.id.res_factory);
        password = (EditText) findViewById(R.id.res_factory_pwd);
        confirm = (Button) findViewById(R.id.button_res_factory);
        layout = (LinearLayout) findViewById(R.id.layout_res_factory);

        resFactory.setFocusable(true);
        resFactory.setBackgroundResource(R.drawable.menu_focus_selector);
        resFactory.setOnKeyListener(this);
        confirm.setOnKeyListener(this);
        password.setOnKeyListener(this);
        confirm.setOnClickListener(this);

    }

    @Override
    protected void onResume(){
        super.onResume();
        password.setFocusable(true);
        confirm.setFocusable(true);
    }


    /*@Override
    protected void onDestroy(){
        super.onDestroy();
        ActivityCollector.finishAll();
    }*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){

        if (requestCode != ActivityId.RESET_ACTIVITY){
            Log.i(TAG, "不是正确的恢复出厂id");
            return;
        }

        switch (resultCode){
            case RESULT_OK:
                Intent intent = new Intent(MASTER_CLEAR);
                sendBroadcast(intent);
                Log.i(TAG, "恢复出厂成功");
                break;
            case RESULT_CANCELED:
                Log.i(TAG, "取消恢复出厂");
                break;
            default:
                break;
        }


    }

    @Override
    public void onClick(View v){
        Intent intent = new Intent(ResetActivity.this, ResFactoryDialogActivity.class);
        startActivityForResult(intent, RES_FACTORY);
        

    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent keyEvent){


        /*这段看起来比较恼火，是为了能够在按下返回键直接退出程序，之后会应用到所有第一层
        * 活动中，会考虑写个工具类啥的，直接一步操作完。看起来也没那么烦。
        * */
        if (keyEvent.getAction() == KeyEvent.ACTION_DOWN){
            switch (v.getId()){
                case R.id.res_factory:
                    if (keyCode == KeyEvent.KEYCODE_DPAD_UP){
                        password.setFocusable(false);
                        confirm.setFocusable(false);
                        Intent intent = new Intent(ResetActivity.this, AdvancedActivity.class);
                        startActivity(intent);
                    }else if (keyCode == KeyEvent.KEYCODE_BACK){
                        ActivityCollector.finishAll();
                    }else{

                    }
                    break;
                default:
                    ActivityCollector.finishAll();
                    break;

            }

            /*switch (keyCode){
                case KeyEvent.KEYCODE_DPAD_UP:
                    password.setFocusable(false);
                    confirm.setFocusable(false);
                    Intent intent = new Intent(ResetActivity.this, AdvancedActivity.class);
                    startActivity(intent);
                    break;
                default:
                    break;
            }*/
        }
        return false;
    }




}
