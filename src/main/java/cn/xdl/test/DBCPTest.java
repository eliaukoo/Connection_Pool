package cn.xdl.test;

import org.apache.commons.dbcp.BasicDataSourceFactory;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

public class DBCPTest {

    static{
        try {
            Properties properties = new Properties();
            InputStream is = DBCPTest.class.getClassLoader().getResourceAsStream("dbcp.properties");
            properties.load(is);
            is.close();
            DataSource dataSource = BasicDataSourceFactory.createDataSource(properties);
            Connection conn = dataSource.getConnection();
            System.out.println(conn);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("初始化连接池失败");
        }



    }

    public static void main(String[] args) throws Exception {

        Properties p = new Properties();
        InputStream is = DBCPTest.class.getClassLoader().getResourceAsStream("dbcp.properties");
        p.load(is);
        is.close();
        //通过数据库连接的工厂对象创建一个连接池
        DataSource dataSource = BasicDataSourceFactory.createDataSource(p);
        //通过连接池的getConnection方法从连接池获得一个连接
        Connection conn = dataSource.getConnection();
        conn.close();
        System.out.println(conn);


    }

}
