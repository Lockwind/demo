package com.example.demo.Controller;

import com.example.demo.Service.userService;
import com.example.demo.domain.User;
import com.example.demo.response.Message;
import com.example.demo.response.loginMsg;
import com.example.demo.util.checkObject;
import com.example.demo.util.userTokenUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class waterController {

    @Autowired
    userService userService;

    @RequestMapping("/login")
    public Message<loginMsg> Login(String logininfo) throws IllegalAccessException{
        User userinfo = new Gson().fromJson(logininfo, User.class);
        if (checkObject.check(userinfo,"userName","userPwd","deviceToken")){
            return userService.login(userinfo);
        }else{
            return new Message<>(0,"参数异常",null);
        }
    }

    @RequestMapping("/login_out")
    public Message login_out(String Token) {
        int uId;
        if (!userTokenUtil.isTokenNull(Token)){
            return new Message<>(0,"参数异常",null);
        }
        try {
            //从token中取得用户id
            uId = Integer.parseInt(userTokenUtil.getStr(Token).split("/")[0]);
        }catch (NumberFormatException e){
            return new Message<>(0,"参数异常",null);
        }
        //在共有map中移除token和id
        if (userTokenUtil.Users.remove(uId, Token)) {
            return new Message<>(1, "success", null);
        } else {
            return new Message<>(0, "退出错误", null);
        }
    }

    @RequestMapping("/change_pwd")
    public Message change_pwd(String changeInfo) {
        //json转为map
        Map<String, String> info = new Gson().fromJson(changeInfo, new TypeToken<Map<String, String>>() {
        }.getType());
        Integer userid = Integer.parseInt(info.get("Userid"));
        String old_pwd = info.get("Old_pwd");
        String new_pwd = info.get("New_pwd");
        if (userid!=null && old_pwd!=null && new_pwd!=null) {
            return userService.updatepwd(userid, old_pwd, new_pwd);
        }else {
            return new Message<>(0,"参数异常",null);
        }
    }

    @RequestMapping("/list_users")
    public Message<List<User>> list_users(String Token) {
        int uId;
        if (!userTokenUtil.isTokenNull(Token)){
            return new Message<>(0,"参数异常",null);
        }
        try {
             uId=Integer.parseInt(userTokenUtil.getStr(Token).split("/")[0]);
            //取出id
        }catch (NumberFormatException e){
            return new Message<>(0,"参数异常",null);
        }
        return userService.selectAll(uId);
    }
}
