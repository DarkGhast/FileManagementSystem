package org.darkghast.fms.service;

import org.apache.ibatis.annotations.Param;
import org.darkghast.fms.entity.SavedFile;
import org.darkghast.fms.entity.User;

import java.util.List;

/**
 * 用于访问数据库保存的文件信息的Service的接口
 */
public interface SavedFileService {
    /**
     * 查询ID为[id]的文件的信息
     *
     * @param id 目标文件的信息
     * @return 保存在数据库中的文件信息
     */
    SavedFile query(Long id);

    /**
     * 查询数据库中所有的文件信息
     *
     * @return 文件信息的List
     */
    List<SavedFile> queryAll();

    /**
     * 获取文件总数
     */
    Long getCount();

    /**
     * 分页查询数据库中的文件信息
     *
     * @param pageNo   第几页 页数
     * @param pageSize 每一页展示几条记录
     * @return 文件信息的List
     */
    List<SavedFile> queryAllByPage(int pageNo, int pageSize);

    /**
     * 查找identifier值为[identifier]的所有文件
     *
     * @param identifier 查询的identifier
     * @return 数据库中对应的文件信息
     */
    List<SavedFile> queryAllByIdentifier(String identifier);

    /**
     * 按userId查找某用户上传过的文件
     * @param uploaderId 目标用户的ID
     * @return 用户上传的文件
     */
    List<SavedFile> queryAllByUploaderId(int uploaderId);

    /**
     * 按条件分页查找
     * @param savedFile 封装为SavedFile的条件
     * @param pageNo   第几页 页数
     * @param pageSize 每一页展示几条记录
     * @return 数据库中对应范围的用户信息列表
     */
    List<SavedFile> queryByPageForCondition(SavedFile savedFile, int pageNo, int pageSize);

    /**
     * @param condition 封装为SavedFile的条件
     * 获取符合条件的文件总数
     */
    Long getCountForCondition(SavedFile condition);

    /**
     * 将文件信息保存到数据库中
     *
     * @param file 需要被保存的文件
     */
    int save(SavedFile file);

    /**
     * 更新数据库中的文件的文件名
     *
     * @param file 被更新的文件
     * @return 操作成功返回1，否则返回0
     */
    int update(SavedFile file);
}
