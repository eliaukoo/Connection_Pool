package cn.xdl.util;

import com.sun.glass.ui.Size;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLOutput;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

/**
 * 定义连接池类
 * */
public class ConnectionPool {

    private List<Connection> pool = new LinkedList<Connection>();
    private String driver;
    private String url;
    private String username;
    private String password;
    private int initialSize;
    private int maxActive;
    private long maxWait;
    private int poolSize = 0;


    /**
     * 配置文件解析
     * */
    public void parseProperties(Properties properties){
        driver = properties.getProperty("driver");
        url = properties.getProperty("url");
        username = properties.getProperty("username");
        password = properties.getProperty("password");
        //获取初始化参数
        String initialSizeStr = properties.getProperty("initialSize");
        initialSize = Integer.valueOf(initialSizeStr);
        //获取最大连接数
        String maxActiveStr = properties.getProperty("maxActive");
        maxActive = Integer.valueOf(maxActiveStr);
        //获取等待时间
        String maxWaitStr = properties.getProperty("maxWait");
        maxWait = Long.valueOf(maxWaitStr);
    }

    /**
     * 创建连接
     * */
    public Connection createConnection(){
        Connection conn = null;
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url,username,password);
            poolSize++;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return conn;
    }


    /**
     * 创建连接池
     * */
    public void createPool(){
        for(int i = 0;i<initialSize;i++){
            pool.add(createConnection());
        }
    }

    /**
     * 当连接池满时动态添加连接
     * */
    public synchronized void dynamicAdd(){
        //当连接池中连接数为空时：连接已经被取完
        if(pool.size() <= 0){
            //当连接池为空且没达到最大连接数时
            if(poolSize < maxActive){
                pool.add(createConnection());
            }else{      //连接池为空且已经达到最大连接数
                System.out.println("连接数已经达到最大");

                long startTime = System.currentTimeMillis()+maxWait;
                System.out.println("开始等待时间："+startTime);
                while(pool.size() <= 0 && maxWait > 0){
                    try {
                        //让出时间片进入等待状态，当有连接被放回时次线程被唤醒
                        this.wait(maxWait);
                        //如果开始等待时间减去当前时间小于0 则说明等待期间并没有线程将连接放回，此时超出最大等待时间，抛出连接超时异常
                        System.out.println("等待结束时间："+System.currentTimeMillis());
                        if(startTime - System.currentTimeMillis() < 0){
                            throw new RuntimeException("连接超时");
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }



    /**
     * 从连接池获取连接
     * */
    public synchronized Connection getConnection(){
        //先调用动态添加连接方法查看连接池已经满了是否需要添加新连接
        dynamicAdd();
        System.out.println("当前剩余连接数："+pool.size()+"  总连接数："+poolSize);
        final Connection conn = pool.remove(0);
        //创建代理对象
        Connection proxyConn = (Connection) Proxy.newProxyInstance(conn.getClass().getClassLoader(), new Class[]{Connection.class}, new InvocationHandler() {
            @Override
            public synchronized Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Object obj = null;
                //如果调用的是close方法则进if 将该连接对象添加到连接池  如果不是close方法则执行该执行的方法
                if ("close".equals(method.getName())) {
                    pool.add(conn);
                    //放回一个数据库连接就唤醒所有线程
                    this.notifyAll();
                    System.out.println(conn.getClass()+" 连接放回连接池");
                } else {
                    obj = method.invoke(conn, args);
                }
                return obj;
            }
        });
        System.out.println("代理对象："+proxyConn.getClass());
        return proxyConn;
    }

    /**
     *放回连接池
     * */
 /*   public void close(Connection connection){
        if(pool.size() < initialSize){
            pool.add(connection);
        }
        if(pool.size() > initialSize){
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }*/



}
