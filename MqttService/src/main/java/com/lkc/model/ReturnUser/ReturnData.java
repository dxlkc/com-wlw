package com.lkc.model.ReturnUser;

import java.util.ArrayList;

//返回给用户的历史数据和实时数据模型
public class ReturnData {
    private String deviceId;
    private String type;
    private Float max;
    private Float min;
    private ArrayList<HistoryData> data;

    public Float getMax() {
        return max;
    }

    public void setMax(Float max) {
        this.max = max;
    }

    public Float getMin() {
        return min;
    }

    public void setMin(Float min) {
        this.min = min;
    }

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

    public ArrayList<HistoryData> getData() {
        return data;
    }

    public void setData(ArrayList<HistoryData> data) {
        this.data = data;
    }
}
