package com.example.eileen.mysettings;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DisplayActivity extends AppCompatActivity
        implements View.OnClickListener, View.OnKeyListener{
    private TextView display;
    private LinearLayout setResolution;
    private LinearLayout adjustRegion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_activity);
        display = (TextView) findViewById(R.id.display);
        setResolution = (LinearLayout) findViewById(R.id.set_resolution);
        adjustRegion = (LinearLayout) findViewById(R.id.adjust_frame_output_region);

        display.setFocusable(true);
        display.setBackgroundResource(R.drawable.menu_focus_selector);
        display.setOnKeyListener(this);
        setResolution.setOnClickListener(this);
        adjustRegion.setOnClickListener(this);


    }

    @Override
    protected void onResume(){
        super.onResume();
        setResolution.setFocusable(true);
        adjustRegion.setFocusable(true);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    @Override
    public void onClick(View v){
        Intent intent;
        switch (v.getId()){
            case R.id.set_resolution:
                intent = new Intent(DisplayActivity.this, ResolutionActivity.class);
                startActivity(intent);
                break;
            case R.id.adjust_frame_output_region:
                intent = new Intent(DisplayActivity.this, OutputRegionActivity.class);
                startActivity(intent);
                break;
            default:
                break;


        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event){

        Intent intent;
        if (event.getAction() == KeyEvent.ACTION_DOWN){
            switch (keyCode){
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    intent = new Intent(DisplayActivity.this, StorageActivity.class);
                    startActivity(intent);
                    break;
                case KeyEvent.KEYCODE_DPAD_UP:
                    setResolution.setFocusable(false);
                    adjustRegion.setFocusable(false);
                    intent = new Intent(DisplayActivity.this, DateTimeActivity.class);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
        return false;
    }
}
