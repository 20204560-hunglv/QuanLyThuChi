package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
    public boolean signIn(String username, String password) {
        try {
            String sql_login = "select*from TAIKHOAN where TenDangNhap=? and MatKhau=?";
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
            ex.printStackTrace();
        }
        return false;
    }
    
    public boolean changePass(String matkhaumoi, int idTaiKhoan) {
        try {
            String update = "update TAIKHOAN set [MatKhau]=N'" + matkhaumoi + "' where ID_TaiKhoan=" + idTaiKhoan + " ";
            Statement st = conn.createStatement();
            if (st.executeUpdate(update) == 1) {
                st.close();
                return true;
            } else {
                st.close();
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public void deleteTaiKhoan(int idtaikhoan) {
        try {
            Statement st = conn.createStatement();
            String deleteThongBao = "delete from THONGBAO where ID_TaiKhoan="+idtaikhoan+"  ";
            st.executeUpdate(deleteThongBao);
            
            String deleteGioiHan = "delete from GIOIHANCHI where ID_TaiKhoan="+idtaikhoan+"  ";
            st.executeUpdate(deleteGioiHan);
            
            String deleteGiaoDich = "delete from GIAODICH where ID_TaiKhoan="+idtaikhoan+"  ";
            st.executeUpdate(deleteGiaoDich);
            
            String deleteTaiKhoan = "delete from TAIKHOAN where ID_TaiKhoan="+idtaikhoan+"  ";
            st.executeUpdate(deleteTaiKhoan);
            
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
