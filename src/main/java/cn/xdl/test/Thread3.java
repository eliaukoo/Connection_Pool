package cn.xdl.test;

import cn.xdl.util.ConnectionPool;

import java.sql.Connection;
import java.sql.SQLException;

public class Thread3 implements Runnable {

    private ConnectionPool cp;

    public Thread3(ConnectionPool cp){
        this.cp = cp;
    }

    @Override
    public void run() {
        for (int i = 0; i < 3; i++) {
            Connection conn = cp.getConnection();
            try {
                Thread.sleep(500);
                conn.close();
            } catch (InterruptedException | SQLException e) {
                e.printStackTrace();
            }
        }

    }
}
