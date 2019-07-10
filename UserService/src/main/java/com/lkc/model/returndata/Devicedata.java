package com.lkc.model.returndata;

import java.util.List;

//用于返回一个设备列表给前端显示,构造结构体返回json
public class Devicedata {
    private String deviceId;
    private String deviceName;
    private String deviceRemark;
    private String sendRate;
    private String deviceBelong;
    private List<String> deviceChildren;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceRemark() {
        return deviceRemark;
    }

    public void setDeviceRemark(String deviceRemark) {
        this.deviceRemark = deviceRemark;
    }

    public String getSendRate() {
        return sendRate;
    }

    public void setSendRate(String sendRate) {
        this.sendRate = sendRate;
    }

    public String getDeviceBelong() {
        return deviceBelong;
    }

    public void setDeviceBelong(String deviceBelong) {
        this.deviceBelong = deviceBelong;
    }

    public List<String> getDeviceChildren() {
        return deviceChildren;
    }

    public void setDeviceChildren(List<String> deviceChildren) {
        this.deviceChildren = deviceChildren;
    }
}
