package quanlythuchi.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import quanlythuchi.model.TaiKhoan;

public class JdbcConnection {

    private Connection conn;
    private String sql_login = "select*from TAIKHOAN where TenDangNhap=? and MatKhau=?";

    public JdbcConnection() {
//        super();
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
    }

    public boolean checkTaiKhoan(String username){
        try {
            String sql_check = "select*from TAIKHOAN where TenDangNhap=?";
            PreparedStatement pstm = conn.prepareCall(sql_check);
            pstm.setString(1, username);
            ResultSet rs = pstm.executeQuery();
            if(rs.next()){
                return true;
            }
            else return  false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean register(String username, String password){
        try {
            String sql_signup = "insert into TAIKHOAN (TenDangNhap,MatKhau) values(?,?)";
            PreparedStatement psm = conn.prepareCall(sql_signup);
            psm.setString(1, username);
            psm.setString(2, password);
            int check = psm.executeUpdate(); // Trả về số dòng bị tác động
            
            if (check != 0){
                return true;//dang ky thanh cong
            }
            else return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean signIn(String username, String password) {
        try {
            PreparedStatement pstm = conn.prepareCall(sql_login);
            pstm.setString(1, username);
            pstm.setString(2, password);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) { //nếu có thì return true
                return true;
            } else {
                return false;
            }
        } catch (SQLException ex) {
            System.out.println("lỗi signIn");
            ex.printStackTrace();
        }
        return false;
    }

    public TaiKhoan getTaiKhoan(String username, String password) {
        TaiKhoan tk = new TaiKhoan();
        try {
            
            PreparedStatement pstm = conn.prepareCall(sql_login);
            pstm.setString(1, username);
            pstm.setString(2, password);
            ResultSet rs = pstm.executeQuery();
            //nếu có thì set các thuộc tính của tk
            if (rs.next()) {
                tk.setIdTaiKhoan(rs.getInt(1));
                tk.setTenDangNhap(rs.getString(2));
                tk.setMatKhau(rs.getString(3));
                tk.setGioiHanChi(rs.getInt(4));
            }
        } catch (SQLException ex) {
            System.out.println("lỗi signIn tk");
            ex.printStackTrace();
        }
        return tk;
    }
}
