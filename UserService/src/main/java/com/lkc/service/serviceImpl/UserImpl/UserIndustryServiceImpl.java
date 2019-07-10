package com.lkc.service.serviceImpl.UserImpl;

import com.lkc.model.userInfo.UserIndustry;
import com.lkc.repository.IndustryRepository;
import com.lkc.repository.UserRepository;
import com.lkc.FeignClient.mongoservice.IndustryDao;
import com.lkc.service.serviceInterface.User.UserIndustryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserIndustryServiceImpl implements UserIndustryService {
    @Autowired
    private IndustryRepository industryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IndustryDao industryDao;
    @Override
    public ArrayList<String> findUserindustryId(String name) {

        return industryRepository.findByName(name);
    }

    @Override
    public String addUserIndustry(String name, String industryId, String permission) {
        if(userRepository.findByName(name) == null)
            return "name_error";
        else if(industryDao.findByIndustryId(industryId) == null)
            return "industry_error";
        else {
            UserIndustry userIndustry = new UserIndustry();
            userIndustry.setName(name);
            userIndustry.setIndustryId(industryId);
            userIndustry.setPermission(permission);
            industryRepository.save(userIndustry);
            return "success";
        }
    }

    @Override
    public String deleteByIndustry(String industryid) {
        industryRepository.deleteByIndustryId(industryid);
        return "success";
    }

    @Override
    public String deleteByNameAndIndustry(String name, String industryid) {
        industryRepository.deleteByNameAndIndustryId(name,industryid);
        return "success";
    }


}
