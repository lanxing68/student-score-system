package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CF {

    // 获取数据库连接，直接写死带修复参数的url
    public static Connection getConn() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/fruit?allowPublicKeyRetrieval=true&useSSL=false";
        String user = "root";
        String password = "123456";

        // 加载驱动
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return DriverManager.getConnection(url, user, password);
    }

    public static void main(String[] args) throws SQLException {
        // 运行这个main方法，控制台打印出连接对象就代表数据库连通成功
        System.out.println(getConn());
    }
}