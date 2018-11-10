package com.example.eileen.mysettings.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.eileen.mysettings.R;

public class Dialog implements View.OnClickListener{

    private String[] mText;
    private Context mContext;
    private LogUtil logUtil = new LogUtil("mydialog");
    public Dialog(String[] promptText, Context context){
        logUtil.logi("Dialog---->Dialog()");
        //把需要显示的信息提取出来
        this.mText = promptText;
        this.mContext = context;

    }

    public void showDialog(){
        LayoutInflater layoutInflater = (LayoutInflater)
                mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.dialog, null);
        TextView tvPromptText1 = (TextView) view.findViewById(R.id.dialog_tv_prompt_text1);
        TextView tvPromptText2 = (TextView) view.findViewById(R.id.dialog_tv_prompt_text2);
        TextView tvPromptText3 = (TextView) view.findViewById(R.id.dialog_tv_prompt_text3);
        Button btnConfirm = (Button) view.findViewById(R.id.dialog_btn_confirm);
        Button btnCancel = (Button) view.findViewById(R.id.dialog_btn_cancel);

        if (mText[0] != null && !mText[0].equals("")){
            tvPromptText1.setText(mText[0]);
        }

        if (mText[1] != null && !mText[1].equals("")){
            tvPromptText2.setText(mText[1]);
        }else {
            tvPromptText2.setVisibility(View.GONE);
        }

        if (mText[2] != null && !mText[2].equals("")){
            tvPromptText3.setText(mText[2]);
        }else {
            tvPromptText3.setVisibility(View.GONE);
        }


        btnConfirm.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

    }


    @Override
    public void onClick(View v){
        //分activity吗，不分的话，怎么返回值呢？返回值的话，我的activity怎么接收呢，不慌不慌
    }
}
