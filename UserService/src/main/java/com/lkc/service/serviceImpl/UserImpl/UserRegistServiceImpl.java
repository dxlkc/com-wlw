package com.lkc.service.serviceImpl.UserImpl;


import com.lkc.model.userInfo.User;
import com.lkc.repository.UserRepository;
import com.lkc.service.serviceInterface.User.UserContactService;
import com.lkc.service.serviceInterface.User.UserRegistService;
import com.lkc.tool.MD5tool;
import com.lkc.tool.MyThreadPoolExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.ThreadPoolExecutor;

@Service
public class UserRegistServiceImpl implements UserRegistService {


    private Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private UserRepository userRepository;

    @Resource
    private UserContactService contactService;

    @Override
    public String addUser(String name, String password, String email)
    {
        User user = userRepository.findByName(name);
        System.out.println("name = " + name);
        if(user != null)
        {
            System.out.println("have user");
            return "name_repeat";
        }
        else
        {
            long currentTime = new Date().getTime();
            String timestr = String.valueOf(currentTime/1000);
            Integer time = Integer.valueOf(timestr);

            User user1 = new User();
            user1.setName(name);
            user1.setPassword(password);
            user1.setRegtime(time);
            userRepository.save(user1);

            contactService.addContact(name,email);

            //发送邮件
            ThreadPoolExecutor threadPoolExecutor = MyThreadPoolExecutor.getInstance().getMyThreadPoolExecutor();
            threadPoolExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    String title = "注册了检测系统账号";
                    String setText = "成功注册！";
                    contactService.sendMail(title, setText, email);
                     logger.trace("给"+email+"发送注册邮件成功！");
                }
            });
            return "success";
        }
    }

    @Override
    public String login(String name, String password){
        User user = userRepository.findByName(name);

        if(user == null){
            logger.trace("登录："+ name +" 用户名不存在");
            return "fail"; //用户名不存在
        }else if(user.getPassword().equals(password)){
            logger.trace("登录：" + name + "登陆成功！");
            return "success";
        }else{
            logger.trace("登录：" + name + "密码错误！");
            return "fail";
        }
    }

    @Override
    public String changePassword(String name, String password)
    {
        User user = userRepository.findByName(name);
        if(user == null)
            return "fail";
        int flag = userRepository.updatePasswordByName(name,password);
        if(flag != 0)
        {
            return "success";
        }
        else
            return "fail";
    }

    @Override
    public String ensurePassword(String name, String password) {
        String passwd = MD5tool.encode(password);
        User user = userRepository.findByName(name);
        if(user.getPassword().equals(passwd))
        {
            return "success";
        }
        else
            return "fail";
    }
}
