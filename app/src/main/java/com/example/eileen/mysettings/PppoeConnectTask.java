package com.example.eileen.mysettings;

import android.app.Activity;
import android.content.Context;
import android.net.ethernet.EthernetManager;
import android.net.pppoe.PppoeManager;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

public class PppoeConnectTask extends AsyncTask<Context, Integer, Integer> {
    private static final String TAG = "PppoeConnectTask";
    public static final int CONNECT_SUCCESS = 0;
    public static final int CONNECT_FAILED = 1;
    public static final int CONNECT_CANCEL = 2;
    public static final int CONNECTING = 3;

    private PppoeManager mPppoeManager;
    private EthernetManager mEthManager;

    private PppoeConnectListener pppoeConnectListener;
    private Boolean isCancelConnect = false;

    public PppoeConnectTask(PppoeConnectListener pppoeConnectListener){
        this.pppoeConnectListener = pppoeConnectListener;
    }

    @Override
    protected Integer doInBackground(Context... params){
        Context context = params[0];
        EditText etUsername = (EditText) ((Activity) context).findViewById(R.id.pppoe_et_username);
        EditText etPassword = (EditText) ((Activity) context).findViewById(R.id.pppoe_et_password);

        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        String ifaceName;
        mPppoeManager = (PppoeManager) context.getSystemService(
                Context.PPPOE_SERVICE);
        mEthManager = (EthernetManager) context.getSystemService(
                Context.ETHERNET_SERVICE);
        /*if (EthernetManager.ETHERNET_CONNECT_MODE_PPPOE
                .equals(mEthManager.getEthernetMode())
                &&
                mEthManager.getEthernetState() == EthernetManager.ETHERNET_STATE_ENABLED) {
            ifaceName = mEthManager.getInterfaceName();
            Log.d(TAG, "doInBackground: " + ifaceName);
        }*/

        ifaceName = mEthManager.getInterfaceName();
        if (!username.equals("") && !password.equals("")){
            Log.d(TAG, "doInBackground: " + mPppoeManager);
            
                mPppoeManager.setPppoeUsername(username);
                Log.d(TAG, "doInBackground: username ok");
                mPppoeManager.setPppoePassword(password);
                Log.d(TAG, "doInBackground: password ok");

                mPppoeManager.connect(username, password, ifaceName);

               /*try{

                Thread.currentThread().sleep(10000);
               } catch (Exception e){
                   Log.d(TAG, "doInBackground: sleep");
               }*/
                Log.d(TAG, "doInBackground: connect ok");
                Toast.makeText(
                        context,
                        "connect success",
                        Toast.LENGTH_SHORT
                ).show();
                int pppoeState = mPppoeManager.getPppoeState();
                if (PppoeManager.PPPOE_STATE_CONNECT == pppoeState){
                    return CONNECT_SUCCESS;
                }else if (PppoeManager.PPPOE_STATE_UNKNOWN == pppoeState){
                    return CONNECT_FAILED;
                }
            
            
        }
        return CONNECT_FAILED;

    }

    @Override
    protected void onProgressUpdate(Integer... values){
        pppoeConnectListener.onProgress();
    }

    @Override
    protected void onPostExecute(Integer status){
        switch (status){
            case CONNECT_SUCCESS:
                pppoeConnectListener.onSuccess();
                break;
            case CONNECT_FAILED:
                pppoeConnectListener.onFail();
                break;
            case CONNECT_CANCEL:
                pppoeConnectListener.onCancel();
                break;
            case CONNECTING:
                pppoeConnectListener.onProgress();
            default:
                break;
        }

    }

    public void cancelConnect(){
        isCancelConnect = true;
    }


}
