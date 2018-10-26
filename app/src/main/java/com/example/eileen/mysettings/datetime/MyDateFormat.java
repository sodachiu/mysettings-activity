package com.example.eileen.mysettings.datetime;

public class MyDateFormat {
    private String format;
    private boolean isChecked = false;

    public MyDateFormat(String format){
        this.format = format;
    }

    public String getFormat(){
        return this.format;
    }

    public boolean getIsChecked(){
        return this.isChecked;
    }

    public void setChecked(boolean flag){
        this.isChecked = flag;
    }
}
