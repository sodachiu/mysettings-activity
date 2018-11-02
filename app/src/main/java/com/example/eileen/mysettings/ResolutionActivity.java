package com.example.eileen.mysettings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.display.DisplayManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.eileen.mysettings.display.ResolutionUtil;

import java.util.ArrayList;
import java.util.List;

public class ResolutionActivity extends AppCompatActivity {
    private TextView tvMenu;
    private List<Resolution> mResolutionList = new ArrayList<>();
    private ResolutionAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private int mCurrentStandardId;
    public static final int RESOLUTION_ACTIVITY = 98;

    private DisplayManager displayManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resolution_activity);
        tvMenu = (TextView) findViewById(R.id.display);
        tvMenu.setBackgroundResource(R.drawable.menu_item_select);
        Context context = getApplicationContext();
        displayManager = (DisplayManager)context.getSystemService(
                Context.DISPLAY_MANAGER_SERVICE);
        mCurrentStandardId = displayManager.getCurrentStandard();
        mResolutionList = ResolutionUtil.initResolutionList(displayManager);
        mRecyclerView = (RecyclerView) findViewById(R.id.resolution_recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new ResolutionAdapter(mResolutionList);
        mRecyclerView.setAdapter(mAdapter);



    }

    protected void onResume(){
        super.onResume();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        switch (requestCode){
            case RESOLUTION_ACTIVITY:
                if (RESULT_OK == resultCode){
                    int oldStandardId = mCurrentStandardId;
                    mCurrentStandardId = data.getIntExtra("click_item_id", -1);
                    setDisplayStandard(oldStandardId);

                }else {
                    setDisplayStandard(mCurrentStandardId);
                }
                break;
            default:
                break;
        }
    }



    public void setDisplayStandard(int oldStandardId){

        int resolutionCount = mResolutionList.size();
        int oldStandardPosition = 0;
        int nowStandardPosition = 0;
        displayManager.setDisplayStandard(mCurrentStandardId);
        for (int i = 0; i < resolutionCount - 1; i++){
            Resolution tempResolution = mResolutionList.get(i);
            if (tempResolution.getId() == oldStandardId){
                oldStandardPosition = i;
                mResolutionList.get(i).setIschecked(false);
            }

            if (tempResolution.getId() == mCurrentStandardId){
                nowStandardPosition = i;
                mResolutionList.get(i).setIschecked(true);
            }

            mAdapter.notifyItemChanged(oldStandardPosition);
            mAdapter.notifyItemChanged(nowStandardPosition);
        }
     }
}
