package com.example.demo.controller;

import com.example.demo.domain.Role;
import com.example.demo.domain.User;
import com.example.demo.repositories.User.UserRepositoryMongoImpl;
import com.example.demo.services.UserService;
import com.example.demo.tools.EmailContext;
import com.example.demo.tools.MD5Util;
import com.example.demo.tools.SendEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@RestController

public class UserApiController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/active", method = RequestMethod.GET)
    public String activeAccount(@RequestParam("email") String email, @RequestParam("validateCode") String validateCode){
        String ans = null;
        User user = userService.LoadUserByEmail(email);
        if(user != null){
            if(!user.isStatus()){
                Date currentTime = new Date();//获取当前时间
                //验证链接是否过期
//                currentTime.before(user.getRegisterTime());
                if(currentTime.before(user.getLastActivateTime())) {
                    //验证激活码是否正确
                    if(validateCode.equals(user.getValidateCode())) {
                        //激活成功， //并更新用户的激活状态，为已激活
//                        System.out.println("==sq==="+user.getStatus());
                        user.setStatus(true);//把状态改为激活
                        //System.out.println("==sh==="+user.getStatus());
                        userService.Save(user);
                    } else {
                        //throw new ServiceException("激活码不正确");
                        ans = "Active Code is not Correct!!";
                    }
                } else {
//                    throw new ServiceException("激活码已过期！");
                    ans = "The Active Code is OverTime!!";
                }
            } else {
//                throw new ServiceException("邮箱已激活，请登录！");
                ans = "The Account have been active, please login!!";
            }
        } else {
//            throw new ServiceException("该邮箱未注册（邮箱地址不存在）！");
            ans = "This Email is not exist!!";
        }
        return ans == null ? "Active Account Success!!" : ans;
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String signUp(@RequestParam("username") String username, @RequestParam("firstname") String firstname, @RequestParam("lastname") String lastname,
                         @RequestParam("password") String password, @RequestParam("email") String email ){

        String ans = null;
        User u = userService.loadUserByUsername(username);
        if(u != null){
            ans = "this username Already exist!!";
        }

        User us = userService.LoadUserByEmail(email);
        if( ans == null && us != null && us.isStatus()){
            ans = "This Email Already Exist!!";
        }

        if(ans == null){
            List<Role> userRoles = new ArrayList<>();
            userRoles.add(new Role("ROLE_USER"));
            User user = new User.Builder().email(email).password(password).
                            firstname(firstname).lastname(lastname).authorities(userRoles).
                            username(username).status(false).registerDate(new Date()).validateCode(MD5Util.encode2hex(email))
                    .build();

            userService.Save(user);
            String context = EmailContext.setEmaiContext(user, email);
            SendEmail.send(email, context);
        }
        return ans == null ? "\"OK\"" : ans;
    }

    @PreAuthorize("hasPermission(#uid, 'Role', 'ROLE_USER')")
    @RequestMapping(value = "/user/{uid}/research", method = RequestMethod.GET)
    public List<User> findUserWithQueryParams(@PathVariable String uid, @RequestParam("condition") String condition){
        return userService.findByFilter(uid, condition);
    }

}
