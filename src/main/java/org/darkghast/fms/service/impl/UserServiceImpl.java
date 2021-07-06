package org.darkghast.fms.service.impl;

import org.darkghast.fms.dao.UserMapper;
import org.darkghast.fms.entity.User;
import org.darkghast.fms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;

import java.util.List;

/**
 * 用于访问数据库用户信息的Service的实现类
 */
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    /**
     * 专门用于登录的查询
     *
     * @param user 登录的用户，需要包含用户名和密码字段
     * @return 如果成功则返回一个User对象，否则返回null
     */
    @Override
    public User login(User user) {
        return userMapper.queryUserForLogin(user);
    }

    /**
     * 注册
     *
     * @param user 注册的用户，需要包含用户名和密码字段
     * @return 操作成功返回1，否则返回0
     */
    @Override
    public int register(User user) {
        if("".equals(user.getEmail())||"".equals(user.getUsername())||"".equals(user.getPassword())){
            return -1001;
        }
        int result;
        try {
            result = userMapper.insert(user);
        }catch (DuplicateKeyException e){
            result = -1;
        }
        return result;
    }

    /**
     * 以[user.id]作为主键，其他的属性作为目的属性，对数据库中对应用户信息进行修改
     *
     * @param user 修改的用户信息
     * @return 操作成功返回1，失败返回0
     */
    @Override
    public int update(User user) {
        if("".equals(user.getEmail())||"".equals(user.getUsername())||"".equals(user.getPassword())){
            return -1001;
        }
        int result;
        try{
            result = userMapper.update(user);
        }catch (DuplicateKeyException e) {
            result = -1;
        }
        return result;
    }

    /**
     * 删除ID为[userId]的用户
     *
     * @param userId 目标用户的ID
     * @return 操作成功返回1，失败返回0
     */
    @Override
    public int delete(Integer userId){
        int result;
        result = userMapper.delete(userId);
        return result;
    }

    /**
     * 查询数据库中的所有用户
     *
     * @return 包含数据库中所有用户信息的列表
     */
    @Override
    public List<User> queryAll() {
        return userMapper.queryAll();
    }

    /**
     * 获取用户总数
     */
    @Override
    public Long getCount() {
        return userMapper.getCount();
    }

    /**
     * 查询ID=[userId]的用户
     *
     * @param userId 目标用户的ID
     * @return 目标用户的信息，查询不到则返回null
     */
    @Override
    public User queryById(Integer userId) {
        return userMapper.queryById(userId);
    }

    /**
     * 分页查询数据库中的用户信息
     *
     * @param pageNo   第几页 页数
     * @param pageSize 每一页展示几条记录
     * @return 用户信息的List
     */
    @Override
    public List<User> queryByPage(int pageNo, int pageSize) {
        return userMapper.queryByPage((pageNo - 1) * pageSize, pageSize);
    }

    /**
     * 按条件分页查询数据库中的用户信息
     * @param user 封装为user的条件
     * @param pageNo   第几页 页数
     * @param pageSize 每一页展示几条记录
     * @return 用户信息的List
     */
    @Override
    public List<User> queryByPageForCondition(User user, int pageNo, int pageSize) {
        return userMapper.queryByPageForCondition(user,(pageNo - 1) * pageSize, pageSize);
    }

    /**
     * @param condition 封装为User的条件
     * 获取符合条件的文件总数
     */
    @Override
    public Long getCountForCondition(User condition) {
        return userMapper.getCountForCondition(condition);
    }
}
