package com.example.eileen.mysettings.datetime;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.eileen.mysettings.R;

import java.util.List;

public class myDateAdapter extends RecyclerView.Adapter<myDateAdapter.ViewHolder> {

    List<DateFormat> mFormatList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvFormat;
        Button btnFormat;

        public ViewHolder(View view){
            super(view);
            tvFormat = (TextView) view.findViewById(R.id.datetime_tv_format);
            btnFormat = (Button) view.findViewById(R.id.datetime_btn_format);
        }
    }
    public myDateAdapter(List<DateFormat> list){
        this.mFormatList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.datetime_format_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        DateFormat dateFormat = mFormatList.get(position);
        holder.tvFormat.setText(dateFormat.getFormat());
        if (dateFormat.getIsChecked()){
            holder.btnFormat.setBackgroundResource(R.drawable.checkbox_on);
        }else {
            holder.btnFormat.setBackgroundResource(R.drawable.checkbox_off);
        }
    }

    @Override
    public int getItemCount(){
        return mFormatList.size();
    }


}
