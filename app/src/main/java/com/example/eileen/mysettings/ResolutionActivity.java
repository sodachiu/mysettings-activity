package com.example.eileen.mysettings;

import android.content.Context;
import android.os.Bundle;
import android.os.display.DisplayManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.eileen.mysettings.display.ResolutionAdapter;
import com.example.eileen.mysettings.display.ResolutionUtil;
import com.example.eileen.mysettings.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class ResolutionActivity extends AppCompatActivity {
    private TextView tvMenu;
    private List<Resolution> mResolutionList = new ArrayList<>();
    private ResolutionAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private LogUtil logUtil = new LogUtil("mydisplay");

    private DisplayManager mDisplayManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resolution_activity);
        tvMenu = (TextView) findViewById(R.id.display);
        tvMenu.setBackgroundResource(R.drawable.menu_item_select);
        Context context = getApplicationContext();
        mDisplayManager = (DisplayManager)context.getSystemService(
                Context.DISPLAY_MANAGER_SERVICE);
        mRecyclerView = (RecyclerView) findViewById(R.id.resolution_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);



    }

    protected void onResume(){
        super.onResume();
        mResolutionList.clear();
        mResolutionList = ResolutionUtil.initResolutionList(mDisplayManager);
        mAdapter = new ResolutionAdapter(mResolutionList);
        mRecyclerView.setAdapter(mAdapter);
    }
}
