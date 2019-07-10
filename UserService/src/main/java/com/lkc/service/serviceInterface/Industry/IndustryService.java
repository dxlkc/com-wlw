package com.lkc.service.serviceInterface.Industry;

import com.lkc.model.industry.industryInfo.Industry;

public interface IndustryService {

    //添加产业
    String addIndustry(String username, String industryName, String industryRemark, String industryTime);

    //添加采集点
    String addAcqUnit(String industryId, String UnitName, String UnitRemark);

    //删除产业
    String deleteIndustry(String name,String id);

    //删除采集点
    String deleteAcqUnit(String industryId, String unitId);

    //更新产业信息
    String updateIndustry(String industryId,
                          String industryName, String industryRemark);

    //更新采集点信息
    String updateAcqUnit(String industryId, String unitId,
                         String newName, String unitRemark);


    //查找产业
    Industry findIndustry(String industryId);
}
