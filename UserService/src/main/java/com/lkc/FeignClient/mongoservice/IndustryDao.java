package com.lkc.FeignClient.mongoservice;

import com.lkc.model.industry.industryInfo.AcqUnit;
import com.lkc.model.industry.industryInfo.Industry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "service")
public interface IndustryDao {

    @RequestMapping(value = "/industry/find", method = RequestMethod.POST)
    Industry findByIndustryId(@RequestParam String id);

    /************更新**********/

    @RequestMapping(value = "/industry/update/industry", method = RequestMethod.POST)
    long updateIndustryInfo(@RequestParam String industryId, @RequestParam String industryName,
                            @RequestParam String industryRemark);

    @RequestMapping(value = "/industry/update/unit", method = RequestMethod.POST)
    long updateAcqunitInfo(@RequestParam String industryId, @RequestParam String unitId,
                           @RequestParam String newName, @RequestParam String unitRemark);

    @RequestMapping(value = "/industry/update/unitNum", method = RequestMethod.POST)
    Long updateAcqNum(@RequestParam String industryId, @RequestParam String industryUnitNum);

    /************删除**********/
    @RequestMapping(value = "/industry/delete/industry", method = RequestMethod.POST)
    long deleteByIndustryId(@RequestParam String id);

    @RequestMapping(value = "/industry/delete/unit", method = RequestMethod.POST)
    long deleteByAcqUnitId(@RequestParam String industryId, @RequestParam String unitId);

    /************添加**********/
    @RequestMapping(value = "/industry/add/industry", method = RequestMethod.POST)
    Industry addIndustry(@RequestBody Industry industry);

    @RequestMapping(value = "/industry/add/unit", method = RequestMethod.POST)
    long addAcqUnit(@RequestParam String industryId,
                    @RequestBody AcqUnit industryUnitNum);

}
