package com.mongodb.MyMongodb.model.industryInfo;

import lombok.*;

import java.io.Serializable;

@Data
public class AcqUnit implements Serializable {
    //采集单元名字 用户自定义
    private String unitName;
    //采集点id 默认自增
    private String unitId;
    //采集单元用户备注 用户自定义
    private String unitRemark = "";

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getUnitRemark() {
        return unitRemark;
    }

    public void setUnitRemark(String unitRemark) {
        this.unitRemark = unitRemark;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }
}
