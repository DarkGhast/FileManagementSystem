package org.darkghast.fms.entity;

import org.apache.struts2.json.annotations.JSON;
import org.darkghast.fms.utils.SavedFileUtils;

import java.io.File;
import java.util.Date;

/**
 * （将要/已经）被保存的文件的信息
 */
public class SavedFile {
    /**
     * 文件的ID，作为唯一标识符
     */
    private Long id;
    /**
     * 文件的真实文件名，即下载时的文件名
     */
    private String realFileName;
    /**
     * 文件的上传者
     */
    private User uploader;
    /**
     * 文件的上传时间
     */
    private Date uploadTime;
    /**
     * 文件的保存路径
     */
    private String path;
    /**
     * 文件保存在介质中的文件名
     */
    private String savedFileName;
    /**
     * 文件的识别码，在介质中含有相同文件时不保存上传的文件，而是直接在数据库中存储数据
     * 在删除文件时，只有在没有其他相同文件时才进行删除
     */
    private String identifier;

    /**
     * 获取文件保存的完整路径，包含文件名
     *
     * @return 文件保存的完整路径
     */
    @JSON(serialize = false)
    public String getCompletePath() {
        return SavedFileUtils.getRootPath() + File.separator + path + File.separator + savedFileName;
    }

    /**
     * 获取文件保存的完整路径
     *
     * @return 文件保存的完整路径
     */
    @JSON(serialize = false)
    public String getSavePath() {
        return SavedFileUtils.getRootPath() + File.separator + path;
    }

    public SavedFile() {
    }

    /**
     * 带参数的文件的构造方法
     *
     * @param id            ID
     * @param realFileName  文件的真实文件名，即文件展示给用户的文件名
     * @param uploader      文件的上传者
     * @param uploadTime    文件的上传时间
     * @param path          文件的保存路径
     * @param savedFileName 文件的存储名
     */
    public SavedFile(Long id, String realFileName, User uploader, Date uploadTime, String path, String savedFileName) {
        this.id = id;
        this.realFileName = realFileName;
        this.uploader = uploader;
        this.uploadTime = uploadTime;
        this.path = path;
        this.savedFileName = savedFileName;
    }

    @Override
    public String toString() {
        return "SavedFile{" +
                "id=" + id +
                ", realFileName='" + realFileName + '\'' +
                ", uploader=" + uploader +
                ", uploadTime=" + uploadTime +
                ", path='" + path + '\'' +
                ", savedFileName='" + savedFileName + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JSON(serialize = false)
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getRealFileName() {
        return realFileName;
    }

    public void setRealFileName(String realFileName) {
        this.realFileName = realFileName;
    }

    @JSON(serialize = false)
    public String getSavedFileName() {
        return savedFileName;
    }

    public void setSavedFileName(String savedFileName) {
        this.savedFileName = savedFileName;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    public User getUploader() {
        return uploader;
    }

    public void setUploader(User uploader) {
        this.uploader = uploader;
    }

    @JSON(serialize = false)
    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}
