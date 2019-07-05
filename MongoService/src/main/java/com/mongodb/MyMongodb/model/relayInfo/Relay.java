package com.mongodb.MyMongodb.model.relayInfo;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class Relay implements Serializable {
    //产业ID 固定
    private String industryId;
    //采集单元id 固定
    private String unitId;
    //板子ID 固定
    private String deviceId;
    //继电器名称 用户自定义
    private String relayName;
    //继电器 站号 固定值 用户填入
    private String relayAddr;
    //继电器 八路状态 从板子数据获取
    private String pinsState;
    //强电设备表
    private ArrayList<Machine> machineList = new ArrayList<>();

    //获取某个强电设备 在数组中的位置
    public int getMachineIndexByPosition(String machinePosition) {
        for (int i = 0; i < machineList.size(); i++) {
            if (machinePosition.equals(machineList.get(i).getMachinePostion())) {
                return i;
            }
        }
        return -1;
    }

    public String getRelayName() {
        return relayName;
    }

    public void setRelayName(String relayName) {
        this.relayName = relayName;
    }

    public List<Machine> getMachineList() {
        return machineList;
    }

    public void setMachineList(ArrayList<Machine> machineList) {
        this.machineList = machineList;
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

    public String getRelayAddr() {
        return relayAddr;
    }

    public void setRelayAddr(String relayAddr) {
        this.relayAddr = relayAddr;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getPinsState() {
        return pinsState;
    }

    public void setPinsState(String pinsState) {
        this.pinsState = pinsState;
    }
}
