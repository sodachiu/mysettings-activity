package com.example.eileen.mysettings.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.eileen.mysettings.BluetoothDisconnectDialog;
import com.example.eileen.mysettings.R;
import com.example.eileen.mysettings.utils.ActivityId;

import java.util.List;

public class PairedDevicesAdapter extends RecyclerView.Adapter<PairedDevicesAdapter.ViewHolder> {

    private List<BluetoothDevice> mBoundDevicesList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvDeviceName;

        public ViewHolder(View view){
            super(view);
            tvDeviceName = (TextView) view.findViewById(
                    R.id.bluetooth_tv_paired_item_name);

        }
    }

    public PairedDevicesAdapter(List<BluetoothDevice> bluetoothDevicesList){
        this.mBoundDevicesList = bluetoothDevicesList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bluetooth_paired_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);

        view.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                BluetoothDevice tmpDevice = mBoundDevicesList.get(position);
                Activity activity = (Activity) view.getContext();
                String deviceInfo;
                Intent intent = new Intent(activity, BluetoothDisconnectDialog.class);
                if (tmpDevice.getName() == null || tmpDevice.getName().equals("")){
                    deviceInfo = tmpDevice.getAddress();
                }else {
                    deviceInfo = tmpDevice.getName();
                }

                intent.putExtra("device_info", deviceInfo);
                intent.putExtra("device_position", position);
                activity.startActivityForResult(intent, ActivityId.BLUETOOTH_ACTIVITY);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        BluetoothDevice device = mBoundDevicesList.get(position);
        if (device.getName() != null && !device.getName().equals("")){
            holder.tvDeviceName.setText(device.getName());
        }else {
            holder.tvDeviceName.setText(device.getAddress());
        }
    }

    @Override
    public int getItemCount(){
        return mBoundDevicesList.size();
    }

}
