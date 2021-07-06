package org.darkghast.fms.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;
import org.darkghast.fms.entity.SavedFile;
import org.darkghast.fms.service.SavedFileService;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * 用于下载的Action，只包含下载方法
 */
public class DownLoadAction extends ActionSupport {
    /**
     * 用于查询数据库
     */
    private SavedFileService savedFileService;
    /**
     * 下载时由Struts框架发送给前端的文件名
     */
    private String filename;
    /**
     * 文件的类型
     */
    private String contentType;
    /**
     * 提供struts配置文件使用，用于将文件传输给前端
     */
    private InputStream inputStream;

    /**
     * 下载文件
     */
    public String downloadFile() throws IOException {
        //先从前端获取文件id
        HttpServletRequest request = ServletActionContext.getRequest();
        long id = Long.parseLong(request.getParameter("id").trim());

        //从服务器获取文件的详细信息
        SavedFile savedFile = savedFileService.query(id);
        // 下载的文件的完整路径
        String completePath = savedFile.getCompletePath();//下载路径

        inputStream = new FileInputStream(completePath);
        // 保存文件的类型
        contentType = "application/x-msdownload";
        //传给前端的文件名应用数据库中保存的“realname（实际名称）”
        filename = savedFile.getRealFileName();
        filename= new String(filename.getBytes(StandardCharsets.UTF_8),"ISO8859-1");
        return SUCCESS;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public SavedFileService getSavedFileService() {
        return savedFileService;
    }

    public void setSavedFileService(SavedFileService savedFileService) {
        this.savedFileService = savedFileService;
    }
}