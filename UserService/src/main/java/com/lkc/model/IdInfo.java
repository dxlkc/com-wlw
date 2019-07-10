package com.lkc.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "idcount_info")
public class IdInfo {
    @Id
    @Column(name = "countid")
    private String countId;
    @Column(name = "industrycount")
    private String industryId;
    @Column(name = "acqunitcount")
    private String acqunitId;

    public String getIndustryId() {
        return industryId;
    }

    public void setIndustryId(String industryId) {
        this.industryId = industryId;
    }

    public String getAcqunitId() {
        return acqunitId;
    }

    public void setAcqunitId(String acqunitId) {
        this.acqunitId = acqunitId;
    }

    public String getCountId() {
        return countId;
    }

    public void setCountId(String countId) {
        this.countId = countId;
    }
}
