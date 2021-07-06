package org.darkghast.fms.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.darkghast.fms.entity.SavedFile;
import org.darkghast.fms.entity.User;
import org.darkghast.fms.service.SavedFileService;
import org.darkghast.fms.utils.SavedFileUtils;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UploadAction extends ActionSupport {
    /**
     * 用于调用SavedFileService
     */
    private SavedFileService savedFileService;
    /**
     * 用于从前端获取文件
     */
    private File[] uploadFiles;
    /**
     * 用于从前端获取文件类型
     */
    private String[] uploadFilesContentType;
    /**
     * 用于从前端获取文件名
     */
    private String[] uploadFilesFileName;


    /**
     * 用于保存错误信息，每次调用前需清空
     */
    private List<SavedFile> errorList = new ArrayList<>();

    /**
     * 保存前端上传的文件，并将文件信息存入数据库
     */
    public String saveFile() throws IOException {
        //先清空错误表
        errorList.clear();

        HttpSession session = ServletActionContext.getRequest().getSession();
        String realpath = SavedFileUtils.getRootPath();
        File path = new File(realpath);
        User uploader = new User();
        uploader.setId(Integer.parseInt(session.getAttribute("userId").toString()));
        //如果目录不存在则先创建
        if (!path.exists()) {
            path.mkdirs();
        }

        for (int i = 0; i < uploadFiles.length; i++) {
            //先得到将要保存的文件的信息
            SavedFile savedFile = new SavedFile();
            savedFile.setId(null);
            savedFile.setRealFileName(uploadFilesFileName[i]);
            savedFile.setUploader(uploader);
            savedFile.setUploadTime(SavedFileUtils.getTime());
            savedFile.setPath(SavedFileUtils.getRandomSavePath(uploadFilesFileName[i]));
            //如果源文件文件名过长，则去除源文件文件名（不作为后缀）
            if (uploadFilesFileName[i].length() > 155) {
                savedFile.setSavedFileName(SavedFileUtils.getPrefix(session));
            } else {
                savedFile.setSavedFileName(SavedFileUtils.getPrefix(session) + uploadFilesFileName[i]);
            }
            savedFile.setIdentifier(SavedFileUtils.getIdentifier(uploadFiles[i]));
            try {
                List<SavedFile> savedFileList = savedFileService.queryAllByIdentifier(savedFile.getIdentifier());
                //如果数据库不存在相同（Identifier识别码）的文件才在介质中保存
                if (savedFileService.queryAllByIdentifier(savedFile.getIdentifier()).size() == 0) {
                    File uploadFile = uploadFiles[i];
                    FileUtils.copyFile(uploadFile, new File(savedFile.getSavePath(), savedFile.getSavedFileName()));
                } else {
                    //存在的话将文件名改为数据库中相同文件的文件名
                    savedFile.setSavedFileName(savedFileList.get(0).getSavedFileName());
                }

                //文件保存后将信息保存到数据库
                savedFileService.save(savedFile);
            } catch (IOException e) {
                //如果上传错误则将上传的文件删除
                new File(savedFile.getSavePath(), savedFile.getSavedFileName()).delete();
                //将上传错误的文件名添加到errorList，在上传任务完成后返回《上传错误的文件名》的JSON数据
                errorList.add(savedFile);
            }
        }
        return SUCCESS;
    }

    public File[] getUploadFiles() {
        return uploadFiles;
    }

    public void setUploadFiles(File[] uploadFiles) {
        this.uploadFiles = uploadFiles;
    }

    public String[] getUploadFilesContentType() {
        return uploadFilesContentType;
    }

    public void setUploadFilesContentType(String[] uploadFilesContentType) {
        this.uploadFilesContentType = uploadFilesContentType;
    }

    public String[] getUploadFilesFileName() {
        return uploadFilesFileName;
    }

    public void setUploadFilesFileName(String[] uploadFilesFileName) {
        this.uploadFilesFileName = uploadFilesFileName;
    }

    public List<SavedFile> getErrorList() {
        return errorList;
    }

    public void setErrorList(List<SavedFile> errorList) {
        this.errorList = errorList;
    }

    public SavedFileService getSavedFileService() {
        return savedFileService;
    }

    public void setSavedFileService(SavedFileService savedFileService) {
        this.savedFileService = savedFileService;
    }
}
