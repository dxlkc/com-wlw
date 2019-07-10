package com.lkc.controller.Industry;

import com.lkc.model.returndata.Industrydata;
import com.lkc.model.returndata.Unitdata;

import com.lkc.service.serviceInterface.Industry.IndustryService;
import com.lkc.service.serviceInterface.Data.IndustrydataService;
import com.lkc.service.serviceInterface.Data.UnitdataService;
import net.sf.json.JSONArray;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping(value = "/industry")
public class IndustryController {
    @Resource
    private IndustryService industryService;
    @Resource
    private IndustrydataService industrydataService;
    @Resource
    private UnitdataService unitdataService;


    @RequestMapping(value = "/findall", method = RequestMethod.POST)
    public String findall(@RequestParam String id) {
        Industrydata industrydata = industrydataService.findall(id);
        JSONArray jsonarray = JSONArray.fromObject(industrydata);
        String jsonstr = jsonarray.toString();
        return jsonstr;
    }

    @RequestMapping(value = "/find/unit", method = RequestMethod.POST)
    public List<Unitdata> findunit(@RequestParam String id) {
        List<Unitdata> unitdataList = unitdataService.findunit(id);
        return unitdataList;
    }

    @RequestMapping(value = "/find/value", method = RequestMethod.POST)
    public String findByvalue(@RequestParam String industryId) {
        return industrydataService.findbyValue(industryId);
    }


    @RequestMapping(value = "/find/rule", method = RequestMethod.POST)
    public String findRule(@RequestParam String industryId) {
        return industrydataService.findbyrule(industryId);
    }

    /************更新**********/

    @RequestMapping(value = "/update/industry", method = RequestMethod.POST)
    public String updateIndustryInfo(@RequestParam String industryId, @RequestParam String industryName,
                                     @RequestParam String industryRemark) {
        return industryService.updateIndustry(industryId, industryName, industryRemark);
    }

    @RequestMapping(value = "/update/unit", method = RequestMethod.POST)
    public String updateAcqunitInfo(@RequestParam String industryId, @RequestParam String unitId,
                                    @RequestParam String newName, @RequestParam String unitRemark) {
        return industryService.updateAcqUnit(industryId, unitId, newName, unitRemark);
    }

    /************删除**********/

    @RequestMapping(value = "/delete/industry", method = RequestMethod.POST)
    public String deleteByIndustryId(@RequestParam String name,@RequestParam String id) {

        return industryService.deleteIndustry(name,id);
    }

    @RequestMapping(value = "/delete/unit", method = RequestMethod.POST)
    public String deleteByAcqUnitId(@RequestParam String industryId, @RequestParam String unitId) {
        return industryService.deleteAcqUnit(industryId, unitId);
    }

    /************添加**********/

    @RequestMapping(value = "/add/industry", method = RequestMethod.POST)
    public String addIndustry(@RequestParam String username,
                              @RequestParam String industryName,
                              @RequestParam String industryRemark,
                              @RequestParam String industryTime) {
        return industryService.addIndustry(username, industryName, industryRemark, industryTime);
    }

    @RequestMapping(value = "/add/unit", method = RequestMethod.POST)
    public String addAcqUnit(@RequestParam String industryId,
                             @RequestParam String UnitName,
                             @RequestParam String UnitRemark) {
        return industryService.addAcqUnit(industryId, UnitName, UnitRemark);
    }
}
