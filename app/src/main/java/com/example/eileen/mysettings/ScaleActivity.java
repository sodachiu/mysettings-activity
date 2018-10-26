package com.example.eileen.mysettings;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.KeyEvent;

import com.hisilicon.android.hidisplaymanager.HiDisplayManager;


public class ScaleActivity extends AppCompatActivity {

    private HiDisplayManager mDisplayManager;
    private static final int VERTICAL_MAX_MARGIN = 136;
    private static final int HORIZONTAL_MAX_MARGIN = 200;
    private static final int MIN_MARGIN = 0;
    private static final int STEP = 1;
    private static int top_margin = 0;
    private static int left_margin = 0;
    private static int right_margin = 0;
    private static int bottom_margin = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.output_region_activity);

        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);

        mDisplayManager = new HiDisplayManager();
        Rect rect = mDisplayManager.getGraphicOutRange(); //获取当前不可视区域
        left_margin = rect.left;
        top_margin = rect.top;
        right_margin = rect.right;
        bottom_margin = rect.bottom;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

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

    private void subHorizontalMargin() {

        int resultMargin = left_margin - STEP;
        if (resultMargin < MIN_MARGIN) {
            left_margin = MIN_MARGIN;
            right_margin = MIN_MARGIN;
        }else {
            left_margin -= STEP;
            right_margin = left_margin;
        }

        refreshView();
    }

    private void addHorizontalMargin() {

        int resultMargin = left_margin + STEP;
        if (resultMargin > HORIZONTAL_MAX_MARGIN) {

            left_margin = HORIZONTAL_MAX_MARGIN;
        }else {
            left_margin += STEP;
        }
        right_margin = left_margin;
        refreshView();
    }

    private void addVerticalMargin() {

        int resultMargin = top_margin + STEP;
        if (resultMargin > VERTICAL_MAX_MARGIN) {
            top_margin = VERTICAL_MAX_MARGIN;
        }else {
            top_margin += STEP;
        }
        bottom_margin = top_margin;
        refreshView();
    }

    private void subVerticalMargin() {

        int resultMargin = top_margin - STEP;
        if (resultMargin < MIN_MARGIN) {
            top_margin = MIN_MARGIN;
        }else {
            top_margin -= STEP;
        }
        bottom_margin = top_margin;
        refreshView();
    }

    public void refreshView(){

        mDisplayManager.setGraphicOutRange(
                left_margin, top_margin, right_margin, bottom_margin);
        mDisplayManager.saveParam();
    }
}
