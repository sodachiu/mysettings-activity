package com.example.eileen.mysettings;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.example.eileen.mysettings.utils.LogUtil;
import com.hisilicon.android.hidisplaymanager.HiDisplayManager;


public class ScaleActivity extends AppCompatActivity {

    private static final int VERTICAL_MAX_MARGIN = 136;
    private static final int HORIZONTAL_MAX_MARGIN = 200;
    private static final int MIN_MARGIN = 0;
    private static final int STEP = 1;
    private HiDisplayManager mDisplayManager;
    private int horizontal_margin = 0;
    private int vertical_margin = 0;
    private LogUtil logUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.output_region_activity);
        logUtil = new LogUtil("myscale");
        logUtil.logi("onCreate()");
        mDisplayManager = new HiDisplayManager();
        Rect rect = mDisplayManager.getGraphicOutRange(); //获取当前不可视区域
        horizontal_margin = rect.left;
        vertical_margin = rect.top;
    }

    @Override
    protected void onStop(){
        super.onStop();
        logUtil.logi("onStop()");
        mDisplayManager.saveParam();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        logUtil.logi("按键代码" + keyCode);
        logUtil.logi("按键事件" + event);
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                subHorizontalMargin();
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                addHorizontalMargin();
                break;
            case KeyEvent.KEYCODE_DPAD_UP:
                addVerticalMargin();
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                subVerticalMargin();
                break;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    /*
     * 增加水平方向的margin
     * */
    private void addHorizontalMargin() {
        logUtil.logi("调用了addHorizontalMargin()");
        int resultMargin = horizontal_margin + STEP;
        if (resultMargin > HORIZONTAL_MAX_MARGIN) {

            horizontal_margin = HORIZONTAL_MAX_MARGIN;
        }else {
            horizontal_margin += STEP;
        }

        setOutRange();
    }


    private void subHorizontalMargin() {

        logUtil.logi("调用了subHorizontalMargin()");
        int resultMargin = horizontal_margin - STEP;
        if (resultMargin < MIN_MARGIN) {
            horizontal_margin = MIN_MARGIN;
        }else {
            horizontal_margin -= STEP;
        }

        setOutRange();
    }

    /*
    * 增加垂直方向的margin
    * */
    private void addVerticalMargin() {
        logUtil.logi("调用了addVerticalMargin()");
        int resultMargin = vertical_margin + STEP;
        if (resultMargin > VERTICAL_MAX_MARGIN) {
            vertical_margin = VERTICAL_MAX_MARGIN;
        }else {
            vertical_margin += STEP;
        }

        setOutRange();
    }

    private void subVerticalMargin() {

        logUtil.logi("调用了subVerticalMargin()");
        int resultMargin = vertical_margin - STEP;
        if (resultMargin < MIN_MARGIN) {
            vertical_margin = MIN_MARGIN;
        }else {
            vertical_margin -= STEP;
        }
        
        setOutRange();
    }

    public void setOutRange(){

        logUtil.logi("调用了setOutRange()");
        mDisplayManager.setGraphicOutRange(
                horizontal_margin, vertical_margin, horizontal_margin, vertical_margin);
    }
}
