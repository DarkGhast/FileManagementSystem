package org.darkghast.fms.service;

import org.darkghast.fms.entity.User;

import java.util.List;

/**
 * 用于访问数据库用户信息的Service的接口
 */
public interface UserService {
    /**
     * 专门用于登录的查询
     *
     * @param user 登录的用户，需要包含用户名和密码字段
     * @return 如果成功则返回一个User对象，否则返回null
     */
    User login(User user);

    /**
     * 注册
     *
     * @param user 注册的用户，需要包含用户名和密码字段
     * @return 操作成功返回1，否则返回0
     */
    int register(User user);

    /**
     * 以[user.id]作为主键，其他的属性作为目的属性，对数据库中对应用户信息进行修改
     *
     * @param user 修改的用户信息
     * @return 操作成功返回1，失败返回0
     */
    int update(User user) ;

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
     * 分页查询数据库中的用户信息
     * @param pageNo   第几页 页数
     * @param pageSize 每一页展示几条记录
     * @return 用户信息的List
     */
    List<User> queryByPage(int pageNo, int pageSize);

    /**
     * 按条件分页查找
     * @param user 封装为user的条件
     * @param pageNo   第几页 页数
     * @param pageSize 每一页展示几条记录
     * @return 数据库中对应范围的用户信息列表
     */
    List<User> queryByPageForCondition(User user,int pageNo, int pageSize);

    /**
     * @param condition 封装为User的条件
     * 获取符合条件的文件总数
     */
    Long getCountForCondition(User condition);
}
