package org.darkghast.fms.controller;

import org.darkghast.fms.entity.User;
import org.darkghast.fms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "Login", name = "用户登录状态相关Controller")
public class UserLoginController {
    /**
     * 用于调用UserService
     */
    UserService userService;

    /**
     * 从前端获取用户名和密码，进行登录
     */
    @RequestMapping(value = "login",name = "登录")
    public String login(User user,HttpServletRequest request){
        //创建新的User对象，并以此对象对数据库进行查询
        User login = userService.login(new User(null, user.getEmail(), null, user.getPassword()));
        if (login == null) {
            //如果查询结果为null（查询不到）说明登录失败
            return "redirect:../error.html";
        } else {
            //如果查询结果不为null（查询到）则登陆成功
            //1.把登录的用户信息保存到Session对象中
            request.getSession().setAttribute("user", login);
            //把userId保存的Session对象中，以便后端获取
            request.getSession().setAttribute("userId", login.getId());
            //2.跳转到首页
            return "redirect:../user/FileManagement.html";
        }
    }

    /**
     * 注销登录
     */
    @RequestMapping(value = "logout",name = "注销")
    public String logout(HttpServletRequest request){
        //1.销毁保存有用户信息的session对象
        request.getSession().invalidate();
        //2.跳转回首页
        return "redirect:../index.html";
    }


    /**
     * 获取已登录的用户的用户名，若发现未登录则调用logout
     */
    @RequestMapping(value = "getMyName",name = "获取用户名")
    @ResponseBody
    public String getMyName(HttpServletRequest request){
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        try {
            return user.getUsername();
        } catch (NullPointerException e) {
            return "";
        }
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
