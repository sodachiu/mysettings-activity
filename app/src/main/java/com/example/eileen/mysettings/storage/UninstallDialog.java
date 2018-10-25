package com.example.eileen.mysettings.storage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.eileen.mysettings.R;

public class UninstallDialog extends Activity
        implements View.OnClickListener{
    private Button confirm;
    private Button cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uninstall_dialog);

        confirm = (Button) findViewById(R.id.button_uninstall_confirm);
        cancel = (Button) findViewById(R.id.button_uninstall_cancel);

        confirm.setOnClickListener(this);
        cancel.setOnClickListener(this);

    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.button_uninstall_confirm:
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.button_uninstall_cancel:
                setResult(RESULT_CANCELED);
                finish();
                break;
            default:
                break;

        }
    }
}
