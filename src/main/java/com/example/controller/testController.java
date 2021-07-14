package com.example.controller;

import org.darkghast.fms.entity.User;
import org.darkghast.fms.service.SavedFileService;
import org.darkghast.fms.service.UserService;
import org.darkghast.fms.service.impl.UserServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * //@Controller 注解用于声明某类的实例是一个控制器。
 * //@RequestMapping 注解可用于类或方法上。用于类上，表示类中的所有响应请求的方法都以该地址作为父路径。
 */
@Controller
@RequestMapping(value = "testController", name = "测试Controller")
public class testController {
    @RequestMapping(value = "test.mvc")
    @ResponseBody
    public String test() {
        return "测试Controller";
    }

}
