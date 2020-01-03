package cn.xdl.test;

import cn.xdl.util.ConnectionPool;
import cn.xdl.util.ConnectionPoolFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionPoolTest {
    public static void main(String[] args) throws IOException, SQLException {




        InputStream is = ConnectionPoolTest.class.getClassLoader().getResourceAsStream("dbcp.properties");
        Properties p = new Properties();
        p.load(is);
        ConnectionPool dataSource = ConnectionPoolFactory.createDataSource(p);

        Thread1 t1 = new Thread1(dataSource);
        Thread1 t2 = new Thread1(dataSource);
        Thread1 t3 = new Thread1(dataSource);

        Thread tt1 = new Thread(t1);
        Thread tt2 = new Thread(t2);
        Thread tt3 = new Thread(t3);

        tt1.start();
        tt2.start();
        tt3.start();







    }
}
