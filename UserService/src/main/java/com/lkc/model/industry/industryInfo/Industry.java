package com.lkc.model.industry.industryInfo;

import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class Industry implements Serializable {
    //产业名称 用户自定义
    private String industryName;
    //产业ID   默认自增
    private String industryId;
    //用户备注 用户自定义
    private String industryRemark = "";
    //创建时间
    private String industryTime;
    //采集单元
    private ArrayList<AcqUnit> acqUnitList = new ArrayList<>();

    private String industryUnitNum;

    public String getIndustryUnitNum() {
        return industryUnitNum;
    }

    public void setIndustryUnitNum(String industryUnitNum) {
        this.industryUnitNum = industryUnitNum;
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

    public void setIndustryTime(String industryTime) {
        this.industryTime = industryTime;
    }

    public String getIndustryTime() {
        return this.industryTime;
    }

}
