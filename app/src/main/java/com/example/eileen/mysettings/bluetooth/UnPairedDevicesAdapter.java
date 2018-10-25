package com.example.eileen.mysettings.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.eileen.mysettings.R;

import java.util.List;

public class UnPairedDevicesAdapter extends RecyclerView.Adapter<UnPairedDevicesAdapter.ViewHolder> {

    private List<BluetoothDevice> mUnpairedList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvDeviceName;

        public ViewHolder(View view){
            super(view);
            tvDeviceName = (TextView) view.findViewById(
                    R.id.bluetooth_tv_unpaired_item_name);

        }
    }

    public UnPairedDevicesAdapter(List<BluetoothDevice> bluetoothDevicesList){
        this.mUnpairedList = bluetoothDevicesList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bluetooth_unpaired_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = (Activity) view.getContext();
                int position = holder.getAdapterPosition();
                BluetoothDevice tmpDevice = mUnpairedList.get(position);
                tmpDevice.createBond();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        BluetoothDevice device = mUnpairedList.get(position);
        if (device.getName() != null && !device.getName().equals("")){
            holder.tvDeviceName.setText(device.getName());
        }else {
            holder.tvDeviceName.setText(device.getAddress());
        }
    }

    @Override
    public int getItemCount(){
        return mUnpairedList.size();
    }

}
