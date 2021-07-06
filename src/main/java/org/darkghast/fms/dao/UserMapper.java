package org.darkghast.fms.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.darkghast.fms.entity.SavedFile;
import org.darkghast.fms.entity.User;

import java.util.List;

@Mapper
public interface UserMapper {
    /**
     * 用于登录的查询用户方法
     *
     * @param user 带有username和password的user对象
     * @return 如果返回值为null，登录失败。
     */
    User queryUserForLogin(User user);

    /**
     * 将新的用户信息插入数据库
     *
     * @param user 新增的用户的信息
     * @return 操作成功返回1，失败返回0
     */
    int insert(User user);

    /**
     * 以[user.id]作为主键，其他的属性作为目的属性，对数据库中对应用户信息进行修改
     *
     * @param user 修改的用户信息
     * @return 操作成功返回1，失败返回0
     */
    int update(User user);

    /**
     * 删除ID为[userId]的用户
     *
     * @param userId 目标用户的ID
     * @return 操作成功返回1，失败返回0
     */
    int delete(Integer userId);

    /**
     * 查询数据库中的所有用户
     *
     * @return 包含数据库中所有用户信息的列表
     */
    List<User> queryAll();

    /**
     * 获取用户总数
     */
    Long getCount();

    /**
     * 查询ID=[userId]的用户
     *
     * @param userId 目标用户的ID
     * @return 目标用户的信息，查询不到则返回null
     */
    User queryById(Integer userId);

    /**
     * 分页查找
     * @param offset   跳过的记录数
     * @param rowCount 获取的记录数
     * @return 数据库中对应范围的用户信息列表
     */
    List<User> queryByPage(@Param("offset") int offset, @Param("rowCount") int rowCount);

    /**
     * 按条件分页查找
     * @param condition 封装为user的条件
     * @param offset   跳过的记录数
     * @param rowCount 获取的记录数
     * @return 数据库中对应范围的用户信息列表
     */
    List<User> queryByPageForCondition(@Param("user")User condition,@Param("offset") int offset, @Param("rowCount") int rowCount);

    /**
     * 获取符合条件的用户总数
     */
    Long getCountForCondition(@Param("user")User condition);
}
