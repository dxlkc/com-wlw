package com.lkc.service.serviceInterface.User;

public interface UserRegistService {
    //注册
    String addUser(String name, String password, String email);

    //登录
    String login(String name, String password);

    //更改密码
    String changePassword(String name, String password);

    //确认密码
    String ensurePassword(String name, String password);

}
