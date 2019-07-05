package com.mongodb.MyMongodb.controller;

import com.mongodb.MyMongodb.dao.IndustryDao;
import com.mongodb.MyMongodb.model.industryInfo.AcqUnit;
import com.mongodb.MyMongodb.model.industryInfo.Industry;
import com.mongodb.MyMongodb.service.IndustryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/industry")
public class IndustryController {
    @Resource
    private IndustryDao industryDao;
    @Resource
    private IndustryService industryService;

    @PostMapping(value = "/find")
    public Industry findByIndustryId(@RequestParam String id) {
        return industryDao.findByIndustryId(id);
    }

    /************更新**********/

    @PostMapping(value = "/update/industry")
    public long updateIndustryInfo(@RequestParam String industryId, @RequestParam String industryName,
                                   @RequestParam String industryRemark) {
        return industryDao.updateIndustryInfo(industryId, industryName, industryRemark);
    }

    @PostMapping(value = "/update/unit")
    public long updateAcqunitInfo(@RequestParam String industryId, @RequestParam String unitId,
                                  @RequestParam String newName, @RequestParam String unitRemark) {
        return industryDao.updateAcqunitInfo(industryId, unitId, newName, unitRemark);
    }

    @PostMapping(value = "/update/unitNum")
    public long updateAcqunitNum(@RequestParam String industryId, @RequestParam String industryUnitNum) {
        return industryDao.updateAcqNum(industryId, industryUnitNum);
    }

    /************删除**********/

    @PostMapping(value = "/delete/industry")
    public long deleteByIndustryId(@RequestParam String id) {
        return industryService.deleteIndustry(id);
    }

    @PostMapping(value = "/delete/unit")
    public long deleteByAcqUnitId(@RequestParam String industryId, @RequestParam String unitId) {
        return industryService.deleteUnit(industryId, unitId);
    }

    /************添加**********/

    @PostMapping(value = "/add/industry")
    public Industry addIndustry(@RequestBody Industry industry) {
        return industryDao.addIndustry(industry);
    }

    @PostMapping(value = "/add/unit")
    public long addAcqUnit(@RequestParam String industryId, @RequestBody AcqUnit acqUnit) {
        return industryDao.addAcqUnit(industryId, acqUnit);
    }
}
