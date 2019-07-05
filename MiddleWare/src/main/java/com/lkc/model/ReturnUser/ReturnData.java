package com.lkc.model.ReturnUser;

import java.util.ArrayList;

//返回给用户的历史数据和实时数据模型
public class ReturnData {
    private String deviceId;
    private String type;
    private ArrayList<ArrayList<String>> data;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<ArrayList<String>> getData() {
        return data;
    }

    public void setData(ArrayList<ArrayList<String>> data) {
        this.data = data;
    }
}
