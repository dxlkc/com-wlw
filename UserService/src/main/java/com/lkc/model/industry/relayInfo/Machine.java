package com.lkc.model.industry.relayInfo;

import lombok.Data;

import java.io.Serializable;

@Data
public class Machine implements Serializable {
    //用户定义的强电设备名  用户自定义
    private String machineName = "";
    //强电设备在继电器的位置 用户自定义
    private String machinePosition;
    //强电设备的状态 从板子数据获取
    private String machineState;

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

    public String getMachinePosition() {
        return machinePosition;
    }

    public void setMachinePosition(String machinePosition) {
        this.machinePosition = machinePosition;
    }
}
