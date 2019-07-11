package com.lkc.model.Industry.deviceInfo;

import java.io.Serializable;
import java.util.List;

public class Device implements Serializable {
    //产业ID 固定
    private String industryId;
    //采集单元id 固定
    private String unitId;
    //板子ID 固定值 用户填入
    private String deviceId;
    //连接状态  断开：0  连接中：1
    private String linkState;
    //用户定义的板子名称 用户自定义
    private String deviceName;
    //板子备注 用户自定义
    private String deviceRemark;
    //板子经度  从板子数据获取
    private String longitude;
    //板子纬度  从板子数据获取
    private String latitude;
    //具体位置信息 从板子数据获取
    private String locationDetail;
    //发送速率  用户自定义
    private String sendRate;
    //用户自定义规则
    private List<Rule> rules;

    public Device() {
        this.longitude = "";
        this.latitude = "";
        this.sendRate = "";
        this.industryId = "";
        this.unitId = "";
        this.deviceName = "";
        this.deviceRemark = "";
        this.locationDetail = "";
        this.linkState = "0";
    }

    public int getRuleInduxByRuleId(String id) {
        for (int i = 0; i < rules.size(); i++) {
            if (id.equals(rules.get(i).getRuleId())) {
                return i;
            }
        }
        return -1;
    }

    public List<Rule> getRules() {
        return rules;
    }

    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }

    public String getLinkState() {
        return linkState;
    }

    public void setLinkState(String linkState) {
        this.linkState = linkState;
    }

    public Device(String longitude, String latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

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

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getSendRate() {
        return sendRate;
    }

    public void setSendRate(String sendRate) {
        this.sendRate = sendRate;
    }

    public String getDeviceRemark() {
        return deviceRemark;
    }

    public void setDeviceRemark(String deviceRemark) {
        this.deviceRemark = deviceRemark;
    }

    public String getIndustryId() {
        return industryId;
    }

    public void setIndustryId(String industryId) {
        this.industryId = industryId;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getLocationDetail() {
        return locationDetail;
    }

    public void setLocationDetail(String locationDetail) {
        this.locationDetail = locationDetail;
    }
}
