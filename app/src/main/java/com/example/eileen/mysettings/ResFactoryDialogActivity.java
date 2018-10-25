package com.example.eileen.mysettings;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ResFactoryDialogActivity extends Activity
        implements View.OnClickListener{

    private Button confirm;
    private Button cancel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recovery_system_dialog);
        confirm = (Button) findViewById(R.id.button_res_confirm);
        cancel = (Button) findViewById(R.id.button_res_cancel);

        confirm.setOnClickListener(this);
        cancel.setOnClickListener(this);

    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.button_res_confirm:
                setResult(RESULT_OK);
                finish();
                break;
            case R.id.button_res_cancel:
                setResult(RESULT_CANCELED);
                finish();
                break;
            default:
                break;
        }
    }
}
