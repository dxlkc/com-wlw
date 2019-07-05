package com.mongodb.MyMongodb.dao;

import com.mongodb.MyMongodb.model.industryInfo.AcqUnit;
import com.mongodb.MyMongodb.model.industryInfo.Industry;

public interface IndustryDao {

    Industry findByIndustryId(String id);

    /************更新**********/

    long updateIndustryInfo(String industryId, String industryName, String industryRemark);

    long updateAcqunitInfo(String industryId, String unitId, String newName, String unitRemark);

    long updateAcqNum(String industryId, String industryUnitNum);

    /************删除**********/

    long deleteByIndustryId(String id);

    long deleteByAcqUnitId(String industryId, String unitId);

    /************添加**********/

    Industry addIndustry(Industry industry);

    long addAcqUnit(String industryId, AcqUnit acqUnit);

}
