package com.lkc.model.DownLink;

import java.util.ArrayList;
import java.util.List;

//用户添加的传感器的所有信息
public class Sensor {
    //传感器名  前端需求
    private String sensorName;
    //设备id   前端需求
    private String deviceId;
    //添加目标 默认为 sensor
    private String target = "sensor";
    //操作指令
    private String operation;
    //站号
    private String addr;
    //485指令
    private String code485;
    //返回数据的长度
    private String returnLength;
    //传感器能测得的数据类型列表
    private List<Type> types = new ArrayList<>();
    //板子需要发布的主题
    private String topic;

    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getCode485() {
        return code485;
    }

    public void setCode485(String code485) {
        this.code485 = code485;
    }

    public String getReturnLength() {
        return returnLength;
    }

    public void setReturnLength(String returnLength) {
        this.returnLength = returnLength;
    }

    public List<Type> getTypes() {
        return types;
    }

    public void setTypes(List<Type> types) {
        this.types = types;
    }

}
