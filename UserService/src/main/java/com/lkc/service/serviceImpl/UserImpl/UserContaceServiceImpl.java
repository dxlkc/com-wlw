package com.lkc.service.serviceImpl.UserImpl;


import com.lkc.model.userInfo.UserContact;
import com.lkc.repository.ContactRepository;
import com.lkc.service.serviceInterface.User.UserContactService;
import com.lkc.tool.MyThreadPoolExecutor;
import net.sf.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadPoolExecutor;

@Service
public class UserContaceServiceImpl implements UserContactService {

    @Autowired
    private ContactRepository contactRepository;
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private JavaMailSender mailSender;

    @Value("860990180@qq.com")
    private String from;


    @Override
    public String findByname(String name) {
       UserContact userContact = contactRepository.findByName(name);
       Map userMap = new HashMap();
       userMap.put("name",userContact.getName());
       userMap.put("email",userContact.getEmail());
       userMap.put("email_ctl",userContact.getEmailctl().equals("on"));
        JSONArray jsonArray = JSONArray.fromObject(userMap);
        String jstr = jsonArray.toString();
        return jstr;
    }

    //发送邮件
    @Override
    public void sendMail(String title, String content, String email){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setSubject(title);
        message.setTo(email);
        message.setText(content);
        mailSender.send(message);
    }



    //添加用户关联信息
    @Override
    public void addContact(String name, String email) {
        System.out.println(email);
        UserContact contact = new UserContact();
        contact.setName(name);
        contact.setEmail(email);
        contact.setEmailctl("off");
        System.out.println(contact);
        int res = contactRepository.save(name,email,"off");
        System.out.println(res);

        logger.info("成功添加用户 " + name +  " 的信息");
    }

    //生成验证码并发送给邮件
    @Override
    public String setIndentity(String email){
        String code = " ";
        int tmp;

        //生成随机验证码
        Random random = new Random();
        for(int i=0;i<6;++i){
            switch (random.nextInt(3)){
                case 0:
                    tmp = random.nextInt(26)+65;
                    code = code + (char) tmp;
                    break;
                case 1:
                    tmp = random.nextInt(26)+97;
                    code = code + (char) tmp;
                    break;
                default:
                    tmp = random.nextInt(10);
                    code = code + String.valueOf(tmp);
            }
        }
        final String Code = code;
        //发送验证码到邮箱
        ThreadPoolExecutor threadPoolExecutor = MyThreadPoolExecutor.getInstance().getMyThreadPoolExecutor();
        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                String title = "检测系统发出验证码";
                String content = "验证码为：" + Code;
                sendMail(title,content,email);
            }
        });
        return Code;

    }


    //查找用户关联信息
    @Override
    public UserContact findByName(String name){
        UserContact contact = contactRepository.findByName(name);
        return contact;
    }

    //更改邮箱
    @Override
    public String changeEmail(String name, String email) {

        int result = contactRepository.changeEmail(name,email);
        if (result != 0){
            logger.info("用户 " + name + " 更改邮箱为 " + email + " 成功");
            return "success";
        }
        logger.info("用户 " + name + " 更改邮箱为 " + email + " 失败");
        return "fail";
    }


    @Override
    public String setEmailctl(String name, Boolean control) {
        int res = contactRepository.changeEmailctl(name,control ? "on" : "off");
        if(res != 0){
            return "success";
        }
        else
            return "fail";
    }
}
