package org.darkghast.fms.service.impl;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.darkghast.fms.dao.SavedFileMapper;
import org.darkghast.fms.service.FileDeleteService;

import java.io.IOException;
import java.io.InputStream;

/**
 * 由于涉及事务commit与rollback，需要将删除提取为单独的service
 */
public class FileDeleteServiceImpl implements FileDeleteService {
    private SqlSession sqlSession;
    /**
     * 将数据库中的文件信息删除
     * 需要进行commit
     * @param Id 目标文件的ID
     * @return 操作成功返回1，否则返回0
     */
    @Override
    public int delete(Long Id) throws IOException {
        int result;
        InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        this.sqlSession = sqlSession;

        SavedFileMapper savedFileMapper = sqlSession.getMapper(SavedFileMapper.class);
        result = savedFileMapper.delete(Id);
        return result;
    }

    /**
     * 在执行删除时需要调用commit才能真正从数据库删除数据
     */
    public void commit() {
        sqlSession.commit();
        sqlSession = null;
    }

    /**
     * 在执行删除时调用rollback回滚
     */
    public void rollback() {
        sqlSession.rollback();
        sqlSession = null;
    }
}
