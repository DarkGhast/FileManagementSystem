import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.darkghast.fms.dao.SavedFileMapper;
import org.darkghast.fms.entity.SavedFile;
import org.darkghast.fms.entity.User;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

public class mybatis_example_SavedFile {
    @Test
    public void Test4Query() throws IOException {
        Long Id=0L;
        InputStream inputStream=Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory= new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();

        SavedFileMapper savedFileMapper = sqlSession.getMapper(SavedFileMapper.class);
        System.out.println(savedFileMapper.query(Id));
    }

    @Test
    public void Test4QueryAll() throws IOException {
        InputStream inputStream=Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory= new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();

        SavedFileMapper savedFileMapper = sqlSession.getMapper(SavedFileMapper.class);
        System.out.println(savedFileMapper.queryAll());
    }

    @Test
    public void Test4QueryAllByPage() throws IOException {
        int pageNo=1;
        int pagesize=20;
        InputStream inputStream=Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory= new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();

        SavedFileMapper savedFileMapper = sqlSession.getMapper(SavedFileMapper.class);
        System.out.println(savedFileMapper.queryAllByPage((pageNo-1)*pagesize,pagesize));
    }

    @Test
    public void Test4Save() throws IOException {
        User uploader=new User(1,null,null,null);
        SavedFile savedFile=new SavedFile(1L,"realFilename",uploader,new Date(),"path","savedFileName");

        InputStream inputStream=Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory= new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();

        SavedFileMapper savedFileMapper = sqlSession.getMapper(SavedFileMapper.class);
        System.out.println(savedFileMapper.save(savedFile));
        sqlSession.commit();
    }

    @Test
    public void Test4Delete() throws IOException {
        Long Id=1L;

        InputStream inputStream=Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory= new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();

        SavedFileMapper savedFileMapper = sqlSession.getMapper(SavedFileMapper.class);
        System.out.println(savedFileMapper.delete(Id));
        sqlSession.commit();
    }

    @Test
    public void Test4Update() throws IOException {
        SavedFile savedFile=new SavedFile(1L,"realFilename",null,new Date(),"path","savedFileName");

        InputStream inputStream=Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory= new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();

        SavedFileMapper savedFileMapper = sqlSession.getMapper(SavedFileMapper.class);
        System.out.println(savedFileMapper.update(savedFile));
        sqlSession.commit();
    }
}
