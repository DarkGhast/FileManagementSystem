package org.darkghast.fms.service.impl;

import org.darkghast.fms.dao.SavedFileMapper;
import org.darkghast.fms.entity.SavedFile;
import org.darkghast.fms.service.SavedFileService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 用于访问数据库保存的文件信息的Service的实现类
 */
public class SavedFileServiceImpl implements SavedFileService {
    @Autowired
    SavedFileMapper savedFileMapper;

    /**
     * 查询ID为[id]的文件的信息
     *
     * @param id 目标文件的信息
     * @return 保存在数据库中的文件信息
     */
    @Override
    public SavedFile query(Long id) {
        return savedFileMapper.query(id);
    }

    /**
     * 查询数据库中所有的文件信息
     *
     * @return 文件信息的List
     */
    @Override
    public List<SavedFile> queryAll() {
        return savedFileMapper.queryAll();
    }

    @Override
    public Long getCount() {
        return savedFileMapper.getCount();
    }

    /**
     * 分页查询数据库中的文件信息
     *
     * @param pageNo   第几页 页数
     * @param pageSize 每一页展示几条记录
     * @return 文件信息的List
     */
    @Override
    public List<SavedFile> queryAllByPage(int pageNo, int pageSize) {
        return savedFileMapper.queryAllByPage((pageNo - 1) * pageSize, pageSize);
    }

    /**
     * 查找identifier值为[identifier]的所有文件
     *
     * @param identifier 查询的identifier
     * @return 数据库中对应的文件信息
     */
    @Override
    public List<SavedFile> queryAllByIdentifier(String identifier) {
        return savedFileMapper.queryAllByIdentifier(identifier);
    }

    /**
     * 按userId查找某用户上传过的文件
     *
     * @param uploaderId 目标用户的ID
     * @return 用户上传的文件
     */
    @Override
    public List<SavedFile> queryAllByUploaderId(int uploaderId) {
        return savedFileMapper.queryAllByUploaderId(uploaderId);
    }

    @Override
    public List<SavedFile> queryByPageForCondition(SavedFile savedFile, int pageNo, int pageSize) {
        return savedFileMapper.queryByPageForCondition(savedFile,(pageNo - 1) * pageSize, pageSize);
    }

    @Override
    public Long getCountForCondition(SavedFile condition) {
        return savedFileMapper.getCountForCondition(condition);
    }

    /**
     * 将文件信息保存到数据库中
     *
     * @param file 需要被保存的文件
     */
    @Override
    public int save(SavedFile file) {
        int result;
        result = savedFileMapper.save(file);
        return result;
    }

    /**
     * 更新数据库中的文件的文件名
     *
     * @param file 被更新的文件
     * @return 操作成功返回1，否则返回0
     */
    @Override
    public int update(SavedFile file) {
        int result;
        result = savedFileMapper.update(file);
        return result;
    }
}
