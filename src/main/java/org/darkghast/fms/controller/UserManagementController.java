package org.darkghast.fms.controller;

import org.apache.log4j.Logger;
import org.darkghast.fms.entity.SavedFile;
import org.darkghast.fms.entity.User;
import org.darkghast.fms.service.FileDeleteService;
import org.darkghast.fms.service.SavedFileService;
import org.darkghast.fms.service.UserService;
import org.darkghast.fms.service.impl.FileDeleteServiceImpl;
import org.darkghast.fms.utils.PageQueryUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "UserManagement", name = "用户管理相关Controller")
public class UserManagementController {
    /**
     * 用于记录日志信息
     */
    static final Logger log = Logger.getLogger(UserManagementController.class.getName());
    /**
     * 用于调用UserService
     */
    private UserService userService;
    /**
     * 用于调用SavedFileService
     */
    SavedFileService savedFileService;

    /**
     * 查询所有用户信息并返回给前端
     */
    @RequestMapping(value = "queryAll",name = "查询所有用户信息并返回给前端")
    @ResponseBody
    public List<User> queryAll(){
        return userService.queryAll();
    }

    /**
     * 分页查询所有用户信息并返回给前端
     */
    @RequestMapping(value = "queryByPage",name = "分页查询所有用户信息并返回给前端")
    @ResponseBody
    public Map<String,Object> queryByPage(HttpServletRequest request) {
        Map<String,Object> result = new HashMap<>();
        int pageNo = Integer.parseInt(request.getParameter("pageNo").trim());

        result.put("count", (userService.getCount()+ PageQueryUtil.PAGE_SIZE-1) /PageQueryUtil.PAGE_SIZE);
        result.put("userList",userService.queryByPage(pageNo, PageQueryUtil.PAGE_SIZE));
        return result;
    }

    /**
     * 按条件分页查询用户信息并返回给前端
     */
    @RequestMapping(value = "queryByPageForCondition",name = "按条件分页查询用户信息并返回给前端")
    @ResponseBody
    public Map<String,Object> queryByPageForCondition(HttpServletRequest request){
        Map<String,Object> result = new HashMap<>();
        User user=new User();
        Integer id=null;
        String email=null,username="";
        int pageNo = Integer.parseInt(request.getParameter("pageNo").trim());
        if(!request.getParameter("id").equals("")&&request.getParameter("id")!=null){
            id = Integer.parseInt(request.getParameter("id").trim());
        }
        if(!request.getParameter("email").equals("")&&request.getParameter("email")!=null){
            email = request.getParameter("email").trim();
        }
        if(!request.getParameter("username").equals("")&&request.getParameter("username")!=null){
            username = request.getParameter("username").trim();
        }

        user.setId(id);
        user.setEmail(email);
        user.setUsername("%"+username+"%");


        result.put
                ("count", (userService.getCountForCondition(user)+PageQueryUtil.PAGE_SIZE-1) /PageQueryUtil.PAGE_SIZE);
        result.put
                ("userList",userService.queryByPageForCondition(user,pageNo, PageQueryUtil.PAGE_SIZE));
        return result;
    }

    /**
     * 接收前端传来的id，若数据库中没有用户上传的文件，则将数据库中的对应的用户删除
     * 否则返回-100，表示用户仍有文件在数据库中，不允许用该方法删除
     */
    @RequestMapping(value = "delete",name = "删除用户")
    @ResponseBody
    public void delete(HttpServletRequest request,HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id").trim());

        List<SavedFile> savedFileList = savedFileService.queryAllByUploaderId(id);
        if (savedFileList.size() == 0) {
            int result = userService.delete(id);
            response.getWriter().print(result);
            if (result == 1) {
                User user = (User) request.getSession().getAttribute("user");
                log.info("用户ID-"+user.getId()+"-删除了用户-"+id);
            }
        } else {
            response.getWriter().print(-100);
        }
    }

    /**
     * 接收前端传来的id，不经过校验，直接强制删除用户
     */
    @RequestMapping(value = "forceDelete",name = "强制删除用户")
    @ResponseBody
    public int forceDelete(HttpServletRequest request) throws IOException {
        FileDeleteService fileDeleteService=new FileDeleteServiceImpl();
        int id = Integer.parseInt(request.getParameter("id").trim());

        List<SavedFile> savedFiles = savedFileService.queryAllByUploaderId(id);
        boolean deleteFlag=true;
        for (SavedFile savedFile : savedFiles) {
            List<SavedFile> savedSameFileList = savedFileService.queryAllByIdentifier(savedFile.getIdentifier());
            int result = fileDeleteService.delete(savedFile.getId());
            /*
              删除文件的条件
              1.返回结果result为1，即数据库中已删除文件信息
              2.数据库中不存在相同的文件
             */
            if (result == 1
                    && savedSameFileList.size() == 1
                    && savedSameFileList.get(0).getId().equals(savedFile.getId())) {
                File deleteFile = new File(savedFile.getCompletePath());
                if (deleteFile.delete()) {
                    //成功删除后再删除数据库数据
                    fileDeleteService.commit();
                } else {
                    fileDeleteService.rollback();
                    deleteFlag=false;
                    break;
                }
            }else{
                fileDeleteService.commit();
            }
        }
        if(deleteFlag){
            int result = userService.delete(id);
            if (result == 1) {
                User user = (User) request.getSession().getAttribute("user");
                log.info("用户ID-"+user.getId()+"-删除了用户-"+id);
            }
            return result;
        }else{
            return -200;
        }
    }

    /**
     * 接收前端传来的用户名和密码，在数据库中新增用户
     */
    @RequestMapping(value = "insertUser",name = "强制删除用户")
    @ResponseBody
    public int insertUser(HttpServletRequest request) {
        String email = request.getParameter("email").trim();
        String username = request.getParameter("username").trim();
        String password = request.getParameter("password").trim();
        int result = userService.register(new User(null, email, username, password));
        if (result == 1) {
            User user = (User) request.getSession().getAttribute("user");
            log.info("用户ID-"+user.getId()+"-新增了用户-"+email);
        }
        return result;
    }

    /**
     * 接收前端传来的id以及修改后的用户名和密码，将数据库中的对应的用户进行修改
     */
    @RequestMapping(value = "updateUser",name = "强制删除用户")
    @ResponseBody
    public int updateUser(HttpServletRequest request){
        int id = Integer.parseInt(request.getParameter("id").trim());
        String email = request.getParameter("email").trim();
        String username = request.getParameter("username").trim();
        String password = request.getParameter("password").trim();
        int result = userService.update(new User(id, email, username, password));
        if (result == 1) {
            User user = (User) request.getSession().getAttribute("user");
            log.info("用户ID-"+user.getId()+"-修改了用户-"+id+"的密码");
        }
        return result;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setSavedFileService(SavedFileService savedFileService) {
        this.savedFileService = savedFileService;
    }
}
