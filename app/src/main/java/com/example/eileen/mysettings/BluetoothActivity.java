package com.example.eileen.mysettings;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eileen.mysettings.utils.ActivityId;
import com.example.eileen.mysettings.bluetooth.BluetoothLog;
import com.example.eileen.mysettings.bluetooth.PairedDevicesAdapter;
import com.example.eileen.mysettings.bluetooth.UnPairedDevicesAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BluetoothActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView netSetting;
    private Button btnBluetoothStatus;
    private LinearLayout llBluetoothSwitchStatus;
    RecyclerView rvPairedDevices;
    RecyclerView rvUnpairedDevices;


    private BluetoothAdapter mBluetoothAdapter;
    private Set<BluetoothDevice> mSetPairedDevices;
    private List<BluetoothDevice> mPairedDevicesList = new ArrayList<>();
    private List<BluetoothDevice> mUnpairedDevicesList = new ArrayList<>();
    private PairedDevicesAdapter mAdapter1;
    private UnPairedDevicesAdapter mAdapter2;
    private MyBluetoothReceiver mReceiver;
    public static final int START_DISCOVERY_ERROR = 0;
    public static final int DISCOVERY_FINISHED = 1;

    private Handler handler = new Handler(){
        public void handleMesssage(Message msg){
          switch (msg.what){
              case START_DISCOVERY_ERROR:
                  Toast.makeText(BluetoothActivity.this,
                          "开启扫描失败",
                          Toast.LENGTH_SHORT).show();
                  break;
              case DISCOVERY_FINISHED:
                  Toast.makeText(BluetoothActivity.this,
                          "扫描完成",
                          Toast.LENGTH_SHORT).show();
                  break;
              default:
                  break;
          }
      }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth_activity);
        btnBluetoothStatus = (Button) findViewById(R.id.bluetooth_btn_status);
        rvPairedDevices = (RecyclerView) findViewById(R.id.bluetooth_rv_paired_list);
        rvUnpairedDevices = (RecyclerView) findViewById(R.id.bluetooth_rv_unpaired_list);
        llBluetoothSwitchStatus = (LinearLayout) findViewById(R.id.bluetooth_ll_switch_status);
        netSetting = (TextView) findViewById(R.id.net_setting);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);


        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null){
            Toast.makeText(BluetoothActivity.this,
                    "此设备不支持蓝牙",
                    Toast.LENGTH_SHORT).show();
            BluetoothLog.LOGI("该设备不支持蓝牙");
            return;
        }

        mAdapter1 = new PairedDevicesAdapter(mPairedDevicesList);
        mAdapter2 = new UnPairedDevicesAdapter(mUnpairedDevicesList);

        netSetting.setBackgroundResource(R.drawable.menu_item_select);
        llBluetoothSwitchStatus.setOnClickListener(this);

        rvPairedDevices.setLayoutManager(layoutManager);
        rvUnpairedDevices.setLayoutManager(layoutManager1);
        rvPairedDevices.setAdapter(mAdapter1);
        rvUnpairedDevices.setAdapter(mAdapter2);


        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        mReceiver = new MyBluetoothReceiver();
        registerReceiver(mReceiver, filter);


    }

    @Override
    protected void onResume(){
        super.onResume();
        if (mBluetoothAdapter.isEnabled()){
            btnBluetoothStatus.setBackgroundResource(R.drawable.checkbox_on);
            if (!mBluetoothAdapter.isDiscovering()) {
                mBluetoothAdapter.startDiscovery();
            }
            refreshView(true);
        }else {

            btnBluetoothStatus.setBackgroundResource(R.drawable.checkbox_off);
            refreshView(false);

        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        if (ActivityId.BLUETOOTH_ACTIVITY != requestCode){
            BluetoothLog.LOGI("取消配对的 requestCode 有问题呢");
            return;
        }else if (resultCode == RESULT_CANCELED){
            BluetoothLog.LOGI("用户取消了取消配对操作");
            return;
        }
        String deviceInfo = data.getStringExtra("device_info");
        int position = data.getIntExtra("device_position", -1);

        BluetoothDevice remoteDevice = mPairedDevicesList.get(position);
        String remoteInfo;
        if (remoteDevice.getName() != null && !remoteDevice.getName().equals("")){
            remoteInfo = remoteDevice.getName();
        }else {
            remoteInfo = remoteDevice.getAddress();
        }


        if (remoteInfo.equals(deviceInfo)){
            //将已绑定列表中得那项移出，但不加入未绑定列表
            mPairedDevicesList.remove(position);
            remoteDevice.removeBond();
        }else {
            BluetoothLog.LOGI("找不到匹配项");

        }
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.bluetooth_ll_switch_status:
                if (mBluetoothAdapter.isEnabled()) {

                    btnBluetoothStatus.setBackgroundResource(R.drawable.checkbox_off);
                    mBluetoothAdapter.cancelDiscovery();
                    mBluetoothAdapter.disable();

                    rvPairedDevices.setVisibility(View.INVISIBLE);
                    rvUnpairedDevices.setVisibility(View.INVISIBLE);
                    BluetoothLog.LOGI("关闭蓝牙");
                } else {
                    btnBluetoothStatus.setBackgroundResource(R.drawable.checkbox_on);
                    mBluetoothAdapter.enable();
                    rvPairedDevices.setVisibility(View.VISIBLE);
                    rvUnpairedDevices.setVisibility(View.VISIBLE);
                    refreshView(true);
                    BluetoothLog.LOGI("开启蓝牙");
                }
        }
    }

    public void refreshView(boolean flag){

        if (!flag){
            rvPairedDevices.setVisibility(View.INVISIBLE);
            rvUnpairedDevices.setVisibility(View.INVISIBLE);
            return;
        }
        if (mBluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE
                &&
                mBluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE
                ){

            mBluetoothAdapter.setScanMode(BluetoothAdapter.SCAN_MODE_CONNECTABLE, 86400000);

        }

        mSetPairedDevices = mBluetoothAdapter.getBondedDevices();
        mPairedDevicesList.clear();
        mPairedDevicesList.addAll(mSetPairedDevices);
        mAdapter1.notifyDataSetChanged();
        mAdapter2.notifyDataSetChanged();
    }

    class MyBluetoothReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            BluetoothLog.LOGI(action);
            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {

                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                        BluetoothAdapter.ERROR);
                switch (state) {
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        BluetoothLog.LOGI("蓝牙正在关闭，尝试断开断开所有正常连接");
                        //目前只做配对，不做连接，所以直接
                        rvPairedDevices.setVisibility(View.GONE);
                        rvUnpairedDevices.setVisibility(View.GONE);
                        break;
                    case BluetoothAdapter.STATE_OFF:
                        BluetoothLog.LOGI("蓝牙处于关闭状态.");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        BluetoothLog.LOGI("蓝牙已经开启,开启后才能使用适配器");
                        if (mBluetoothAdapter.isEnabled()) {
                            BluetoothLog.LOGI("蓝牙开启，蓝牙适配器可用");
                            new Thread(new Runnable() {
                                Message message = new Message();
                                @Override
                                public void run() {
                                    //启动扫描任务，
                                    if (mBluetoothAdapter.isDiscovering()){
                                        mBluetoothAdapter.cancelDiscovery();
                                    }

                                    boolean isDiscovery = mBluetoothAdapter.startDiscovery();

                                    if (!isDiscovery){
                                        message.what = START_DISCOVERY_ERROR;
                                        handler.sendMessage(message);
                                    }

                                }
                            }).start();
                        } else {
                            BluetoothLog.LOGI("蓝牙开启，但蓝牙适配器不可用");
                            mBluetoothAdapter.setScanMode(BluetoothAdapter.SCAN_MODE_CONNECTABLE, 86400000);
                            BluetoothLog.LOGI("fail to enable mBluetoothAdapte");
                        }
                        break;
                    default:
                        break;

                }
            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {

                BluetoothDevice foundDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (foundDevice == null) {
                    BluetoothLog.LOGI("获取到的设备为空");
                    return;
                }

                boolean inUnpairedDevices =  mUnpairedDevicesList.contains(foundDevice);
                if (foundDevice.getBondState() != BluetoothDevice.BOND_BONDED
                        && !inUnpairedDevices) {
                    mUnpairedDevicesList.add(foundDevice);
                    mAdapter2.notifyItemInserted(mUnpairedDevicesList.size() - 1);

                }

            } else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                switch (device.getBondState()) {
                    case BluetoothDevice.BOND_BONDING://正在配对
                        BluetoothLog.LOGI("正在配对");

                        break;
                    case BluetoothDevice.BOND_BONDED://配对结束


                        mUnpairedDevicesList.remove(device);
                        mPairedDevicesList.add(device);
                        mAdapter1.notifyDataSetChanged();
                        mAdapter2.notifyDataSetChanged();
                        Toast.makeText(BluetoothActivity.this,
                                "配对成功！",
                                Toast.LENGTH_SHORT).show();
                        noItemToGone();

                        break;
                    case BluetoothDevice.BOND_NONE://取消配对/未配对
                        mPairedDevicesList.remove(device);
                        mAdapter1.notifyDataSetChanged();
                        noItemToGone();
                    default:
                        break;
                }

            }
        }
    }

    /*
    * 如果无配对或待配对的设备，则设置控件不可见
    * */
    public void noItemToGone(){
        int pairedDevices = mPairedDevicesList.size();
        int unpairedDevices = mUnpairedDevicesList.size();
        if (pairedDevices == 0){
            rvPairedDevices.setVisibility(View.INVISIBLE);
        }
        if (unpairedDevices == 0){
            rvUnpairedDevices.setVisibility(View.INVISIBLE);
        }
    }
}
