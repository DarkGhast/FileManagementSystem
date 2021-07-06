package org.darkghast.fms.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.darkghast.fms.entity.SavedFile;
import org.darkghast.fms.entity.User;

import java.util.List;

@Mapper
public interface SavedFileMapper {
    /**
     * 根据id查询文件信息
     *
     * @param id 目标文件的ID
     * @return 查询到的文件的信息
     */
    SavedFile query(Long id);

    /**
     * 查询所有的文件
     *
     * @return 数据库中所有文件信息的列表
     */
    List<SavedFile> queryAll();

    /**
     * 获取文件总数
     */
    Long getCount();

    /**
     * 分页查找
     *
     * @param offset   跳过的记录数
     * @param rowCount 获取的记录数
     * @return 数据库中对应的文件信息
     */
    List<SavedFile> queryAllByPage(@Param("offset") int offset, @Param("rowCount") int rowCount);

    /**
     * 查找identifier值为[identifier]的所有文件
     *
     * @param identifier 查询的identifier
     * @return 数据库中对应的文件信息
     */
    List<SavedFile> queryAllByIdentifier(@Param("identifier") String identifier);

    /**
     * 按userId查找某用户上传过的文件
     * @param uploaderId 目标用户的ID
     * @return 用户上传的文件
     */
    List<SavedFile> queryAllByUploaderId(@Param("uploaderId")int uploaderId);

    /**
     * 按条件分页查找
     * @param condition 封装为SavedFile的条件
     * @param offset   跳过的记录数
     * @param rowCount 获取的记录数
     * @return 数据库中对应范围的用户信息列表
     */
    List<SavedFile> queryByPageForCondition(@Param("savedFile")SavedFile condition, @Param("offset") int offset, @Param("rowCount") int rowCount);

    /**
     * 获取符合条件的文件总数
     */
    Long getCountForCondition(@Param("savedFile")SavedFile condition);

    /**
     * 保存文件信息
     *
     * @param file 保存的文件的信息
     * @return 操作成功返回1，失败返回0
     */
    int save(@Param("file") SavedFile file);

    /**
     * 删除文件信息
     *
     * @param Id 删除的目标文件的ID
     * @return 操作成功返回1，失败返回0
     */
    int delete(@Param("Id") Long Id);

    /**
     * 更新文件信息
     *
     * @param file 更新的文件信息
     * @return 操作成功返回1，失败返回0
     */
    int update(@Param("file") SavedFile file);
}
