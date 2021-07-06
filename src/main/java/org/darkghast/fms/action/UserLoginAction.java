package org.darkghast.fms.action;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.json.annotations.JSON;
import org.darkghast.fms.entity.User;
import org.darkghast.fms.service.UserService;
import org.darkghast.fms.utils.UrlUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class UserLoginAction extends ActionSupport implements ModelDriven<User> {
    /**
     * 用于从前端获取用户信息(Struts 模型驱动注入）
     */
    User user = new User();
    /**
     * 用于调用UserService
     */
    UserService userService;

    /**
     * 从前端获取用户名和密码，进行登录
     */
    public void login() throws IOException {
        HttpServletResponse response = ServletActionContext.getResponse();
        HttpServletRequest request = ServletActionContext.getRequest();
        //创建新的User对象，并以此对象对数据库进行查询
        User login = userService.login(new User(null, user.getEmail(), null, user.getPassword()));
        if (login == null) {
            //如果查询结果为null（查询不到）说明登录失败
            response.sendRedirect(UrlUtils.getDOMAIN() + "./error.html");
        } else {
            //如果查询结果不为null（查询到）则登陆成功
            //1.把登录的用户信息保存到Session对象中
            request.getSession().setAttribute("user", login);
            //把userId保存的Session对象中，以便后端获取
            request.getSession().setAttribute("userId", login.getId());
            //2.跳转到首页
            response.sendRedirect(UrlUtils.getDOMAIN() + "./user/FileManagement.html");
        }
    }

    /**
     * 注销登录
     */
    public void logout() throws IOException {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        //1.销毁保存有用户信息的session对象
        request.getSession().invalidate();
        //2.跳转回首页
        response.sendRedirect(UrlUtils.getDOMAIN() + "./index.html");
    }


    /**
     * 获取已登录的用户的用户名，若发现未登录则调用logout
     */
    @JSON(serialize = false)
    public void getMyName() throws IOException {
        HttpServletResponse response = ServletActionContext.getResponse();
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        try {
            response.getWriter().print(user.getUsername());
        } catch (NullPointerException e) {
            response.getWriter().print("");
        }
    }

    @Override
    @JSON(serialize = false)
    public User getModel() {
        return user;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
