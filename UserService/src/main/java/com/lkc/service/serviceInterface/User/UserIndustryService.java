package com.lkc.service.serviceInterface.User;

import java.util.ArrayList;

public interface UserIndustryService {

    //查找用户拥有的产业Id
    ArrayList<String> findUserindustryId(String name);

    //添加用户产业信息
    String addUserIndustry(String name, String industryId, String permission);



    //通过产业信息删除记录
    String deleteByIndustry(String industryid);

    //通过用户名和产业信息删除记录
    String deleteByNameAndIndustry(String name, String industryid);
}
