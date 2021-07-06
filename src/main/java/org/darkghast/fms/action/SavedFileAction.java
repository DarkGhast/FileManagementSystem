package org.darkghast.fms.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.darkghast.fms.entity.SavedFile;
import org.darkghast.fms.entity.User;
import org.darkghast.fms.service.FileDeleteService;
import org.darkghast.fms.service.SavedFileService;
import org.darkghast.fms.service.impl.FileDeleteServiceImpl;
import org.darkghast.fms.utils.PageQueryUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 包含对已保存的文件进行操作的方法
 */
public class SavedFileAction extends ActionSupport {
    /**
     * 用于记录日志信息
     */
    static final Logger log = Logger.getLogger(SavedFileAction.class.getName());
    /**
     * 用于调用SavedFileService
     */
    private SavedFileService savedFileService;

    /**
     * 返回前端的页的总数
     */
    private Long count;
    /**
     * 返回前端的数据表
     */
    private List<SavedFile> savedFileList = new ArrayList<>();

    /**
     * 查询所有数据库中保存的文件信息
     *
     * @return 以JSON将查询到的数据返回给前端
     */
    public String queryAll(){
        savedFileList = savedFileService.queryAll();
        return SUCCESS;
    }

    /**
     * 分页查询所有文件信息并返回给前端
     */
    public String queryByPage() {
        HttpServletRequest request = ServletActionContext.getRequest();
        int pageNo = Integer.parseInt(request.getParameter("pageNo").trim());
        count=(savedFileService.getCount()+PageQueryUtil.PAGE_SIZE-1)/PageQueryUtil.PAGE_SIZE;
        savedFileList = savedFileService.queryAllByPage(pageNo, PageQueryUtil.PAGE_SIZE);
        return SUCCESS;
    }

    /**
     * 按条件分页查询用户信息并返回给前端
     */
    public String queryByPageForCondition(){
        HttpServletRequest request = ServletActionContext.getRequest();
        SavedFile savedFile=new SavedFile();
        User user=new User();
        Long id=null;
        Integer uploaderId=null;
        String realFilename="",username="";
        int pageNo = Integer.parseInt(request.getParameter("pageNo").trim());
        if(!request.getParameter("id").equals("")&&request.getParameter("id")!=null){
            id = Long.parseLong(request.getParameter("id").trim());
        }
        if(!request.getParameter("uploadUserId").equals("")&&request.getParameter("uploadUserId")!=null){
            uploaderId = Integer.parseInt(request.getParameter("uploadUserId").trim());
        }
        if(!request.getParameter("filename").equals("")&&request.getParameter("filename")!=null){
            realFilename = request.getParameter("filename").trim();
        }
        if(!request.getParameter("uploadUsername").equals("")&&request.getParameter("uploadUsername")!=null){
            username = request.getParameter("uploadUsername").trim();
        }

        user.setId(uploaderId);
        user.setUsername("%"+username+"%");

        savedFile.setRealFileName("%"+realFilename+"%");
        savedFile.setId(id);
        savedFile.setUploader(user);

        count=(savedFileService.getCountForCondition(savedFile)+PageQueryUtil.PAGE_SIZE-1)/PageQueryUtil.PAGE_SIZE;
        savedFileList = savedFileService.queryByPageForCondition(savedFile,pageNo, PageQueryUtil.PAGE_SIZE);
        return SUCCESS;
    }

    /**
     * 用前端传来的id进行数据库以及保存的文件的删除
     */
    public void delete() throws IOException {
        FileDeleteService fileDeleteService = new FileDeleteServiceImpl();
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        long id = Long.parseLong(request.getParameter("id").trim());
        SavedFile savedFile = savedFileService.query(id);
        List<SavedFile> savedFileList = savedFileService.queryAllByIdentifier(savedFile.getIdentifier());
        File deleteFile = new File(savedFile.getCompletePath());
        int result = fileDeleteService.delete(id);

        /*
          删除文件的条件
          1.返回结果result为1，即数据库中已删除文件信息
          2.数据库中不存在相同的文件
         */
        if (result == 1
                && savedFileList.size() == 1
                && savedFileList.get(0).getId() == id) {
            if (deleteFile.delete()) {
                //成功删除后再删除数据库数据
                fileDeleteService.commit();
                User user = (User) request.getSession().getAttribute("user");
                log.info("用户ID-" + user.getId() + "-删除了文件" + savedFile.getSavedFileName());
            } else {
                fileDeleteService.rollback();
                result = 0;
            }
        } else {
            //如果无需删除文件，则直接删除数据库中的文件数据
            fileDeleteService.commit();
        }
        response.getWriter().print(result);
    }

    /**
     * 接收前端传来的id及修改的文件名的字段，将数据库中的文件名进行修改
     */
    public void update() throws IOException {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        SavedFile savedFile = new SavedFile();
        savedFile.setId(Long.parseLong(request.getParameter("id").trim()));
        savedFile.setRealFileName(request.getParameter("realname").trim());
        int result = savedFileService.update(savedFile);
        response.getWriter().print(result);
    }


    public List<SavedFile> getSavedFileList() {
        return savedFileList;
    }

    public void setSavedFileService(SavedFileService savedFileService) {
        this.savedFileService = savedFileService;
    }

    public Long getCount() {
        return count;
    }
}
