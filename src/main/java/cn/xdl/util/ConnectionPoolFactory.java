package cn.xdl.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class ConnectionPoolFactory {

/*    private static List<Connection> pool = new LinkedList<Connection>();
    private static String driver;
    private static String url;
    private static String username;
    private static String password;
    private static int initialSize;
    private static int maxActive;
    private static int poolSize = 0;*/

/*    {
        ConnectionPool cp = new ConnectionPool();
        driver = cp.getDriver();
        url = cp.getUrl();
        username = cp.getUsername();
        password = cp.getPassword();
        initialSize = cp.getInitialSize();
        maxActive = cp.getMaxActive();
        poolSize = cp.getPoolSize();
    }*/

    /**
     * 创建连接池并获取连接池对象
     * */
    public static ConnectionPool createDataSource(Properties properties){
        ConnectionPool cp = new ConnectionPool();
        cp.parseProperties(properties);
        cp.createPool();
        return cp;
    }


}
