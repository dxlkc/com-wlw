package com.lkc.model.returndata;

public class Unitdata {
    //采集单元名字 用户自定义
    private String unitName;
    //采集点id 默认自增
    private String unitId;
    //采集单元用户备注 用户自定义
    private String unitRemark = "";

    private String unitDeviceNum;

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getUnitRemark() {
        return unitRemark;
    }

    public void setUnitRemark(String unitRemark) {
        this.unitRemark = unitRemark;
    }

    public String getUnitDeviceNum() {
        return unitDeviceNum;
    }

    public void setUnitDeviceNum(String unitDeviceNum) {
        this.unitDeviceNum = unitDeviceNum;
    }
}
