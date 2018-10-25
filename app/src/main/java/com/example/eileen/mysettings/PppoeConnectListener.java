package com.example.eileen.mysettings;

public interface PppoeConnectListener {
    void onProgress();

    void onSuccess();

    void onFail();

    void onCancel();
}
