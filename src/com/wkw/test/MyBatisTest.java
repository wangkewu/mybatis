package com.wkw.test;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import com.wkw.beans.Employee;
import com.wkw.dao.EmployeeMapper;
/**
 * 1.接口式编程
 * 		原生    		Dao        ======》  DaoImpl
 * 		mybatis 	Mapper 	   ======》 xxMapper.xml
 * 
 * 2.sqlSession代表和数据库的一次会话 用完必须关闭
 * 3.SqlSession和connection一样都是非线程安全的。每次都应该去获取新的对象
 * 3.mapper接口没有实现类，但是mybatis会为这个接口生成一个代理对象
 * 			（将接口和xml进行绑定)
 * 			EmployeeMapper $employeeMapper = sqlSession.getMapper(EmployeeMapper.class);
 * 4.两个重要的文件
 * 	  mybatis的全局配置文件：包含数据库连接池信息 事务管理信息等。。。系统运行环境信息
 * 	  sql映射文件：保存了一个sql语句的映射信息
 * 				将sql抽取出来
 * @author wangkewu
 *
 */
public class MyBatisTest {
	
	public static SqlSessionFactory  getSqlSessionFactory() throws IOException{
		String resource = "Mybatis-config.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		return new SqlSessionFactoryBuilder().build(inputStream);
	}
	/**
	 * 1.根据xml配置文件（全局配置文件）创建一个SqlSessionFactory对象
	 * 		有数据源信息
	 * 2.sql映射文件 配置了每一个sql 以及sql的封装规则等
	 * 3.将sql映射文件注册在配置文件中
	 * 4.写代码 
	 * 	1)通过全局配置文件得到SqlSessionFactory
	 * 	2)使用sqlSessionFactory工厂 获取到sqlSession对象使用他来执行增删改查
	 * 		一个sqlSession就是代表和数据库的一次会话 用完需要关闭
	 *  3）使用sql的标志来告诉MyBatis执行哪个sql.sql都是保存在映射文件中的
	 * @throws IOException
	 */
	@Test
	public void test() throws IOException {
		String resource = "Mybatis-config.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		//2.获取一个sqlSession实例 能直接执行已经映射的sql语句
		SqlSession session = sqlSessionFactory.openSession();
		try {
			Employee employee = session.selectOne("com.wkw.beans.EmployeeMapper.selectEmp", 1);
			System.out.println(employee);
		} finally {
		  session.close();
		}
	}
	@Test
	public void test01() throws IOException{
		//1.获取SqlSessionFactory
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		//2.获取SqlSession对象
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try{
			
			//3.获取接口的实现对象
			//会为接口自动的创建一个代理对象 代理对象去执行增删改查
			EmployeeMapper employeeMapper = sqlSession.getMapper(EmployeeMapper.class);
			Employee employee = employeeMapper.getEmppById(1);
			System.out.println(employee);
		}finally{
			sqlSession.close();
		}
	}

}
