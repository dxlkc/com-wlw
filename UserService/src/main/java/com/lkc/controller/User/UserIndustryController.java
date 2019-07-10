package com.lkc.controller.User;

import com.lkc.model.industry.industryInfo.Industry;
import com.lkc.service.serviceInterface.Industry.IndustryService;
import com.lkc.service.serviceInterface.User.UserIndustryService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;

@RestController
@RequestMapping("/userindustry")
public class UserIndustryController {
    @Resource
    private UserIndustryService userIndustryService;
    @Resource
    private IndustryService industryService;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String addUserIndustry(@RequestParam String name,
                                  @RequestParam String industryId,
                                  @RequestParam String permission) {
        return userIndustryService.addUserIndustry(name, industryId, permission);
    }


    @RequestMapping(value = "/add/userindustry",method = RequestMethod.POST)
    public String addByindustry(@RequestParam String name,@RequestParam String industryId)
    {
        Industry industry = industryService.findIndustry(industryId);
        System.out.println(industryId + "   " + industry);
        if(industry == null)
            return "fail";
        industry.setAcqUnitList(null);
        JSONObject jsonObject = JSONObject.fromObject(industry);
        String jstr = jsonObject.toString();
        String res = userIndustryService.addUserIndustry(name,industryId,"0");
        if(!res.equals("success"))
            return "fail";

        return jstr;
    }
    @RequestMapping(value = "/find", method = RequestMethod.POST)
    public String finUserIndustry(@RequestParam String name) {
        ArrayList<Industry> industries = new ArrayList<>();
        ArrayList<String> userindustries = userIndustryService.findUserindustryId(name);
        for (int i = 0; i < userindustries.size(); i++) {
            String id = userindustries.get(i);
            Industry industry = industryService.findIndustry(id);
            industry.setAcqUnitList(null);
            industries.add(industry);
        }
        JSONArray jsonarray = JSONArray.fromObject(industries);

        return jsonarray.toString();
    }


}
