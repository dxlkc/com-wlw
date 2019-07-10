package com.lkc.service.serviceInterface.User;


import com.lkc.model.userInfo.UserContact;

public interface UserContactService {

    String findByname(String name);

    //发送邮件
    void sendMail(String title, String content, String email);

    //添加用户关联信息
    void addContact(String name, String email);

    //查找用户关联信息
    UserContact findByName(String name);

    //更改邮箱
    String changeEmail(String name, String email);

    //注册时生成验证码并发送给邮件
    String setIndentity(String email);

    //设置是否开启邮件功能（on-off）
    String setEmailctl(String name, Boolean control);

}
