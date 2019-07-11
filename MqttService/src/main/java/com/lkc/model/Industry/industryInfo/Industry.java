package com.lkc.model.Industry.industryInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Industry implements Serializable {
    //产业名称 用户自定义
    private String industryName;
    //产业ID   默认自增
    private String industryId;
    //产业创建时间 前端传的值 固定
    private String industryTime;
    //用户备注 用户自定义
    private String industryRemark = "";
    //采集单元
    private ArrayList<AcqUnit> acqUnitList = new ArrayList<>();
    //采集单元数量
    private String industryUnitNum;

    //获取某个采集单元
    public AcqUnit getUnitById(String unitId) {
        for (AcqUnit acqUnit : acqUnitList) {
            if (acqUnit.getUnitId().equals(unitId)) {
                return acqUnit;
            }
        }
        return null;
    }

    //获取某个采集单元 在数组中的位置
    public int getUnitIndexById(String unitId) {
        for (int i = 0; i < acqUnitList.size(); i++) {
            if (acqUnitList.get(i).getUnitId().equals(unitId)) {
                return i;
            }
        }
        return -1;
    }

    public String getIndustryName() {
        return industryName;
    }

    public void setIndustryName(String industryName) {
        this.industryName = industryName;
    }

    public String getIndustryId() {
        return industryId;
    }

    public void setIndustryId(String industryId) {
        this.industryId = industryId;
    }

    public String getIndustryRemark() {
        return industryRemark;
    }

    public void setIndustryRemark(String industryRemark) {
        this.industryRemark = industryRemark;
    }

    public List<AcqUnit> getAcqUnitList() {
        return acqUnitList;
    }

    public void setAcqUnitList(ArrayList<AcqUnit> acqUnitList) {
        this.acqUnitList = acqUnitList;
    }

    public String getIndustryTime() {
        return industryTime;
    }

    public void setIndustryTime(String industryTime) {
        this.industryTime = industryTime;
    }

    public String getIndustryUnitNum() {
        return industryUnitNum;
    }

    public void setIndustryUnitNum(String industryUnitNum) {
        this.industryUnitNum = industryUnitNum;
    }
}
