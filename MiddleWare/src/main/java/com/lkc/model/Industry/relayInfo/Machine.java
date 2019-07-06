package com.lkc.model.Industry.relayInfo;

import java.io.Serializable;

public class Machine implements Serializable {
    //用户定义的强电设备名  用户自定义
    private String machineName = "";
    //用户定义的备注 用户自定义
    private String machineRemark = "";
    //强电设备在继电器的位置 用户自定义
    private String machinePostion;
    //强电设备的状态 从板子数据获取
    private String machineState;

    public String getMachineNmame() {
        return machineName;
    }

    public void setMachineNmame(String machineNmame) {
        this.machineName = machineNmame;
    }

    public String getMachineRemark() {
        return machineRemark;
    }

    public void setMachineRemark(String machineRemark) {
        this.machineRemark = machineRemark;
    }

    public String getMachineState() {
        return machineState;
    }

    public void setMachineState(String machineState) {
        this.machineState = machineState;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public String getMachinePostion() {
        return machinePostion;
    }

    public void setMachinePostion(String machinePostion) {
        this.machinePostion = machinePostion;
    }
}