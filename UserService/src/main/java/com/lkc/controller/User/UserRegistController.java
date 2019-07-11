package com.lkc.controller.User;

import com.lkc.Utils.RedisUtil;
import com.lkc.model.userInfo.UserAll;
import com.lkc.service.serviceInterface.Industry.IndustryService;
import com.lkc.service.serviceInterface.User.UserContactService;
import com.lkc.service.serviceInterface.User.UserIndustryService;
import com.lkc.service.serviceInterface.User.UserRegistService;
import com.lkc.tool.MD5tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@RestController
@RequestMapping(value = "/user")
public class UserRegistController {
    @Resource
    private UserRegistService userRegistService;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private UserContactService userContactService;

    //查找用户信息
    @RequestMapping(value = "/find", method = RequestMethod.POST)
    public String findUser(@RequestBody UserAll user) {
        return userContactService.findByname(user.getName());
    }

    //发送验证码,并存到redis
    @RequestMapping(value = "/identity", method = RequestMethod.POST)
    public String Useridentity(@RequestBody UserAll user) {
        String identity = userContactService.setIndentity(user.getEmail());
        if (identity.isEmpty()) {
            return "发送验证码失败";
        } else {
            redisUtil.set(user.getEmail(), identity, 600);
            return "success";
        }
    }

    //注册
    @RequestMapping(value = "/regist", method = RequestMethod.POST)
    public String Useradd(@RequestBody UserAll user) {
        //从redis中拿到验证码并删除
        String indentity = redisUtil.get(user.getEmail()).toString();

        if (indentity.trim().equals(user.getIdentity().trim())) {
            //密码加密
            String passwd = MD5tool.encode(user.getPassword());
            redisUtil.del(user.getEmail());
            return userRegistService.addUser(user.getName(), passwd, user.getEmail());
        }
        return "fail";
    }

    //登陆 添加了 token
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String UserLogin(@RequestBody UserAll user, HttpServletResponse httpServletResponse) {
        String token = UUID.randomUUID().toString().substring(0, 15);
        //缓存保留10分钟
        /***********************************************/
        redisUtil.set(token, user.getName(), 600);
        httpServletResponse.addHeader("token", token);
        /***********************************************/

        String passwd = MD5tool.encode(user.getPassword());
        return userRegistService.login(user.getName(), passwd);
    }

    //修改密码
    @RequestMapping(value = "/changepassword", method = RequestMethod.POST)
    public String Changepassword(@RequestBody UserAll user) {
        String passwd = MD5tool.encode(user.getPassword());
        return userRegistService.changePassword(user.getName(), passwd);
    }

    //修改邮箱
    @RequestMapping(value = "/changemail", method = RequestMethod.POST)
    public String ChangeMail(@RequestBody UserAll user) {
        //从redis中拿到验证码并删除
        String indentity = redisUtil.get(user.getEmail()).toString();

        if (!indentity.trim().equals(user.getIdentity().trim())) {
            redisUtil.del(user.getEmail());
            return "fail";
        }
        return userContactService.changeEmail(user.getName(), user.getEmail());
    }

    //忘记密码重设
    @RequestMapping(value = "/forgetpassword", method = RequestMethod.POST)
    public String forgetPassword(@RequestBody UserAll user) {
        //从redis中拿到验证码并删除
        String indentity = redisUtil.get(user.getEmail()).toString();
        redisUtil.del(user.getEmail());

        if (indentity.trim().equals(user.getIdentity().trim())) {
            String passwd = MD5tool.encode(user.getPassword());
            return userRegistService.changePassword(user.getName(), passwd);
        }
        return "identity_error";
    }

    @RequestMapping(value = "/changemailctl", method = RequestMethod.POST)
    public String Changemailctl(@RequestParam String name, @RequestParam Boolean email_ctl) {
        return userContactService.setEmailctl(name, email_ctl);
    }

    @RequestMapping(value = "/ensure/password", method = RequestMethod.POST)
    public String ensurePassword(@RequestBody UserAll user) {
        return userRegistService.ensurePassword(user.getName(), user.getPassword());
    }
}
