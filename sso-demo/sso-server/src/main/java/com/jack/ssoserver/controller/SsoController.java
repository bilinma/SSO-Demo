package com.jack.ssoserver.controller;

import com.jack.ssoserver.util.JwtUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class SsoController {

    private List<String> list=new ArrayList<>();

    // http://www.sso.com:9090/preLogin?url=www.user.com:8081/user/wel
    @RequestMapping("/preLogin")
    public String preLogin(String url, HttpServletRequest request, Model model){
        System.out.println(url);
        // 判断有没有全局的jsessionid
        HttpSession session = request.getSession(false);
        if(session==null){
            // 表示没有其他子系统来创建过，我是第一次来
            model.addAttribute("url",url);
            // 给用户返回一个登录界面
            return "login";
        }else{
            // 有其他子系统已经创建过全局的session对象，怎么办？ TODO:
            // 直接颁发token给对应的子系统
            String token =(String) session.getAttribute("token");
            return "redirect:"+url+"?token="+token;
        }
    }

    // 处理用户的登录请求的接口
    @RequestMapping("/login")
    public String preLogin(String username,String password,String url,HttpServletRequest request){
        // 判断用户名和密码是否正确
        if("Jack".equals(username)&& "123456".equals(password)){
            String token=null;
            // token要求，要安全性比较高，与此同时既能生成又能验证   JWT:Json Web Token
            try {
                token= JwtUtil.createJwt();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            System.out.println(token);
            // 登录成功，创建全局的session对象
            request.getSession().setAttribute("token",token);
            list.add(token);
            // 颁发token给对应的子系统
            return "redirect:"+url+"?token="+token;
        }else{
            return "login";
        }
    }

    @RequestMapping("/checkToken")
    @ResponseBody
    public String checkToken(String token){
        if(list.contains(token)&&JwtUtil.verifyJwt(token)){
            return "correct";
        }else{
            return "incorrect";
        }
    }

}
