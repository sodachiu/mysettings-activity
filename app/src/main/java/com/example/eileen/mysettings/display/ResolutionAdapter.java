package com.example.eileen.mysettings.display;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.os.display.DisplayManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eileen.mysettings.R;
import com.example.eileen.mysettings.ResolutionDialogActivity;
import com.example.eileen.mysettings.utils.LogUtil;

import java.util.List;

public class ResolutionAdapter extends RecyclerView.Adapter<ResolutionAdapter.ViewHolder>{

    private List<Resolution> mResolutionList;
    private LogUtil logUtil = new LogUtil("mydisplay");

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imgItem;
        TextView tvItem;
        LinearLayout llItem;

        public ViewHolder(View view){
            super(view);
            imgItem = (ImageView) view.findViewById(R.id.resolution_img_item);
            tvItem = (TextView) view.findViewById(R.id.resolution_tv_item);
            llItem = (LinearLayout) view.findViewById(R.id.resolution_ll_item);

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
        holder.llItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Resolution resolution = mResolutionList.get(position);

                if (resolution.getId() == ResolutionUtil.DISPLAY_ADAPTIVE){
                    Toast.makeText(view.getContext(),
                            "功能开发中",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

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
                resolution.setChecked(true);
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
        boolean isChecked = resolution.getIsChecked();

        if (isChecked){
            holder.imgItem.setImageResource(R.drawable.radio_checked_normal);
            holder.llItem.requestFocus();
            logUtil.logi("ResolutionAdapter:需要焦点的位置为---->" + position);
        }else {
            holder.imgItem.setImageResource(R.drawable.radio_unchecked_normal);
        }

        holder.tvItem.setText(resolution.getName());

        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
    }

    @Override
    public int getItemCount(){
        return mResolutionList.size();
    }


}
