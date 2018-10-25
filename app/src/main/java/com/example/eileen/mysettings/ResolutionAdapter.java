package com.example.eileen.mysettings;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.List;

public class ResolutionAdapter extends RecyclerView.Adapter<ResolutionAdapter.ViewHolder>{

    private static final String TAG = "ResolutionAdapter";
    private List<Resolution> mResolutionList;
//    private static boolean mResolutionIsChecked = false;

    static class ViewHolder extends RecyclerView.ViewHolder{
        RadioButton radioButton;

        public ViewHolder(View view){
            super(view);
            radioButton = (RadioButton) view.findViewById(R.id.resolution_radio_button);

        }

        /*public void setItemIsChecked(boolean checked){
            mResolutionIsChecked = checked;

        }*/
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
//                Log.d(TAG, "onClick: " + resolution);
                Toast.makeText(v.getContext(), resolution.getName(), Toast.LENGTH_SHORT).show();
                Activity activity = (Activity) view.getContext();
//                Log.d(TAG, "onClick: " + activity);
                Intent intent = new Intent(activity, ResolutionDialogActivity.class);
                intent.putExtra("click_item_id", resolution.getId());
                activity.startActivityForResult(intent, ResolutionActivity.RESOLUTION_ACTIVITY);
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

    /*@Override
    public void notifyDataSetChange(){
        super();
    }*/


}
