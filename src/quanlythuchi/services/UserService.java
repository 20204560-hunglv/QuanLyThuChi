package quanlythuchi.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserService {
    private Connection conn;

    public UserService() {
        conn = JdbcConnection.JdbcConnection();
    }
    public boolean checkTaiKhoan(String username) {
        try {
            String sql_check = "select*from TAIKHOAN where TenDangNhap=?";
            PreparedStatement pstm = conn.prepareCall(sql_check);
            pstm.setString(1, username);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean save(String username,String password){
        try {
            String sql_signup = "insert into TAIKHOAN (TenDangNhap,MatKhau) values(?,?)";
            PreparedStatement psm = conn.prepareCall(sql_signup);
            psm.setString(1, username);
            psm.setString(2, password);
            int check = psm.executeUpdate(); // Trả về số dòng bị tác động

            if (check != 0) {
                return true;//dang ky thanh cong
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
