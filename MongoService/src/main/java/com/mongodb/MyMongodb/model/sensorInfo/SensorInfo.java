package com.mongodb.MyMongodb.model.sensorInfo;

import lombok.Data;

import java.io.Serializable;

@Data
public class SensorInfo implements Serializable {
    //产业ID 固定
    private String industryId;
    //采集单元id 固定
    private String unitId;
    //板子ID 固定
    private String deviceId;
    //传感器名 用户自定义
    private String sensorName;
    //站号 固定
    private String sensorAddr;
    //485指令 固定
    private String code;
    //传感器测的数据类型 固定
    private String type;
    //起始位置 固定
    private String byteStart;
    //字节数 固定
    private String byteCount;
    //数据类型 固定
    private String dataType;
    //小数点位数 固定
    private String dicimal;
    //返回的数据长度 固定 用户填的
    private String retLength;
    //最新值  从板子获取数据
    private String value;
    //value的更新时间 从板子获取数据
    private String time;
    //最大阈值 用户自定义
    private String max="";
    //最小阈值 用户自定义
    private String min="";

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDicimal() {
        return dicimal;
    }

    public void setDicimal(String dicimal) {
        this.dicimal = dicimal;
    }

    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
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

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getSensorAddr() {
        return sensorAddr;
    }

    public void setSensorAddr(String sensorAddr) {
        this.sensorAddr = sensorAddr;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getByteStart() {
        return byteStart;
    }

    public void setByteStart(String byteStart) {
        this.byteStart = byteStart;
    }

    public String getByteCount() {
        return byteCount;
    }

    public void setByteCount(String byteCount) {
        this.byteCount = byteCount;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getPointCount() {
        return dicimal;
    }

    public void setPointCount(String pointCount) {
        this.dicimal = pointCount;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getRetLength() {
        return retLength;
    }

    public void setRetLength(String retLength) {
        this.retLength = retLength;
    }
}
