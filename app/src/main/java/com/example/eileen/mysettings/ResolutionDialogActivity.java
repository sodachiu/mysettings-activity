package com.example.eileen.mysettings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ResolutionDialogActivity extends Activity
        implements View.OnClickListener{

    private Button btnConfirm;
    private Button btnCancel;
    private int mClickItemId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resolution_dialog);
        btnConfirm = (Button) findViewById(R.id.resolution_btn_confirm);
        btnCancel = (Button) findViewById(R.id.resolution_btn_cancel);

        btnConfirm.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        Intent intent = getIntent();
        mClickItemId = intent.getIntExtra("click_item_id", -1);

    }

    @Override
    public void onClick(View v){
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.resolution_btn_confirm:
                intent.putExtra("click_item_id", mClickItemId);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.resolution_btn_cancel:
                setResult(RESULT_CANCELED, intent);
                finish();
                break;
            default:
                break;
        }
    }
}
