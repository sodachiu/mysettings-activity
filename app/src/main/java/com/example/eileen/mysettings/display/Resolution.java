package com.example.eileen.mysettings.display;

public class Resolution {
    private String name;
    private int id;
    private boolean isChecked = false;

    public Resolution(String name, int id){
        this.name = name;
        this.id = id;
    }

    public String getName(){
        return this.name;
    }

    public int getId(){
        return this.id;
    }

    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public boolean getIsChecked(){
        return this.isChecked;
    }
}
