package com.example.eileen.mysettings.display;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.os.display.DisplayManager;
import com.example.eileen.mysettings.R;
import com.example.eileen.mysettings.Resolution;
import com.example.eileen.mysettings.ResolutionDialogActivity;
import com.example.eileen.mysettings.utils.LogUtil;

import java.util.List;

public class ResolutionAdapter extends RecyclerView.Adapter<ResolutionAdapter.ViewHolder>{

    private List<Resolution> mResolutionList;
    private LogUtil logUtil = new LogUtil("mydisplay");

    static class ViewHolder extends RecyclerView.ViewHolder{
        RadioButton radioButton;

        public ViewHolder(View view){
            super(view);
            radioButton = (RadioButton) view.findViewById(R.id.resolution_radio_button);

        }
    }

    public ResolutionAdapter(List<Resolution> resolutionList){
        this.mResolutionList = resolutionList;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.resolution_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Resolution resolution = mResolutionList.get(position);

                DisplayManager displayManager = (DisplayManager) view.getContext()
                        .getSystemService(Context.DISPLAY_MANAGER_SERVICE);

                int oldStandard = displayManager.getCurrentStandard();
                int standardId = resolution.getId();
                if (standardId == ResolutionUtil.DISPLAY_ADAPTIVE){
                    displayManager.setDisplayStandard(DisplayManager.DISPLAY_STANDARD_1080P_60);
                }else {
                    displayManager.setDisplayStandard(standardId);
                }

                logUtil.logi("当前分辨率为----" + resolution.getName());
                resolution.setIschecked(true);
                Activity activity = (Activity) view.getContext();
                Intent intent = new Intent(activity, ResolutionDialogActivity.class);
                intent.putExtra("old_standard_id", oldStandard);
                activity.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        Resolution resolution = mResolutionList.get(position);
        holder.radioButton.setText(resolution.getName());
        holder.radioButton.setId(resolution.getId());
        holder.radioButton.setChecked(resolution.getIsChecked());
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
    }

    @Override
    public int getItemCount(){
        return mResolutionList.size();
    }


}
