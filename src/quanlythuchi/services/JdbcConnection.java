package quanlythuchi.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import quanlythuchi.model.TaiKhoan;

public class JdbcConnection {

    public static Connection JdbcConnection() {
        Connection conn=null;
        String sql_login = "select*from TAIKHOAN where TenDangNhap=? and MatKhau=?";
        String url = "jdbc:sqlserver://localhost:1433;databaseName=QuanLyThuChi";
        String user = "sa";
        String password = "1234567@";
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            System.out.println("lỗi connection");
            e.printStackTrace();
        }
        return conn;
    }

    
}
