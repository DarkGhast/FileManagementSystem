package com.example.action;

import com.opensymphony.xwork2.ActionSupport;
import org.darkghast.fms.entity.User;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于测试Struts框架返回JSON的方法
 */
public class TestJsonAction extends ActionSupport {
    private List<User> userList = new ArrayList<>();

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    /**
     * 测试Struts以JSON格式返回List，Struts.xml中添加以下配置可以以JSON格式返回userList
     * <action name="testJson" class="com.example.action.TestJsonAction" method="testJson">
     * <result name="success" type="json">
     * <param name="root">userList</param>
     * </result>
     * </action>
     */
    public String testJson() {
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setUsername("username" + i);
            user.setPassword("password" + i);
            user.setId(i);
            userList.add(user);
        }
        return SUCCESS;
    }
}
