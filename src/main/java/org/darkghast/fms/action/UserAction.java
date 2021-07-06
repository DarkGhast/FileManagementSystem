package org.darkghast.fms.action;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.json.annotations.JSON;
import org.darkghast.fms.entity.SavedFile;
import org.darkghast.fms.entity.User;
import org.darkghast.fms.service.FileDeleteService;
import org.darkghast.fms.service.SavedFileService;
import org.darkghast.fms.service.UserService;
import org.darkghast.fms.service.impl.FileDeleteServiceImpl;
import org.darkghast.fms.utils.PageQueryUtil;
import org.darkghast.fms.utils.UrlUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserAction extends ActionSupport{
    /**
     * 用于记录日志信息
     */
    static final Logger log = Logger.getLogger(UserAction.class.getName());
    /**
     * 用于调用UserService
     */
    UserService userService;
    /**
     * 用于调用SavedFileService
     */
    SavedFileService savedFileService;

    /**
     * 返回前端的页的总数
     */
    private Long count;
    /**
     * 查询到的用户列表，方便Struts框架返回JSON数据
     */
    private List<User> userList = new ArrayList<>();

    /**
     * 查询所有用户信息并返回给前端
     */
    public String queryAll(){
        userList = userService.queryAll();
        return SUCCESS;
    }

    /**
     * 分页查询所有用户信息并返回给前端
     */
    public String queryByPage() {
        HttpServletRequest request = ServletActionContext.getRequest();
        int pageNo = Integer.parseInt(request.getParameter("pageNo").trim());

        count=(userService.getCount()+PageQueryUtil.PAGE_SIZE-1)/PageQueryUtil.PAGE_SIZE;
        userList = userService.queryByPage(pageNo, PageQueryUtil.PAGE_SIZE);
        return SUCCESS;
    }

    /**
     * 按条件分页查询用户信息并返回给前端
     */
    public String queryByPageForCondition(){
        HttpServletRequest request = ServletActionContext.getRequest();
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

        count=(userService.getCountForCondition(user)+PageQueryUtil.PAGE_SIZE-1)/PageQueryUtil.PAGE_SIZE;
        userList = userService.queryByPageForCondition(user,pageNo, PageQueryUtil.PAGE_SIZE);
        return SUCCESS;
    }

    /**
     * 接收前端传来的id，若数据库中没有用户上传的文件，则将数据库中的对应的用户删除
     * 否则返回-100，表示用户仍有文件在数据库中，不允许用该方法删除
     */
    public void delete() throws IOException {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
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
    public void forceDelete() throws IOException {
        FileDeleteService fileDeleteService=new FileDeleteServiceImpl();
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
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
            response.getWriter().print(result);
            if (result == 1) {
                User user = (User) request.getSession().getAttribute("user");
                log.info("用户ID-"+user.getId()+"-删除了用户-"+id);
            }
        }else{
            response.getWriter().print(-200);
        }
    }

    /**
     * 接收前端传来的用户名和密码，在数据库中新增用户
     */
    public void insertUser() throws IOException {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        String email = request.getParameter("email").trim();
        String username = request.getParameter("username").trim();
        String password = request.getParameter("password").trim();
        int result = userService.register(new User(null, email, username, password));
        response.getWriter().print(result);
        if (result == 1) {
            User user = (User) request.getSession().getAttribute("user");
            log.info("用户ID-"+user.getId()+"-新增了用户-"+email);
        }
    }

    /**
     * 接收前端传来的id以及修改后的用户名和密码，将数据库中的对应的用户进行修改
     */
    public void updateUser() throws IOException {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        int id = Integer.parseInt(request.getParameter("id").trim());
        String email = request.getParameter("email").trim();
        String username = request.getParameter("username").trim();
        String password = request.getParameter("password").trim();
        int result = userService.update(new User(id, email, username, password));
        response.getWriter().print(result);
        if (result == 1) {
            User user = (User) request.getSession().getAttribute("user");
            log.info("用户ID-"+user.getId()+"-修改了用户-"+id+"的密码");
        }
    }


    public List<User> getUserList() {
        return userList;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setSavedFileService(SavedFileService savedFileService) {
        this.savedFileService = savedFileService;
    }

    public Long getCount() {
        return count;
    }
}
