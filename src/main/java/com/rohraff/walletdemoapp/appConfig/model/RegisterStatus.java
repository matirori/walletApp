package com.rohraff.walletdemoapp.appConfig.model;

public class RegisterStatus {

    private boolean isRegistered;
    private String info;

    public RegisterStatus(boolean isRegistered, String info) {
        this.isRegistered = isRegistered;
        this.info = info;
    }

    public boolean isRegistered() {
        return isRegistered;
    }

    public void setRegistered(boolean registered) {
        isRegistered = registered;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
