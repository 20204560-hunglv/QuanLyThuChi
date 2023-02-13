package quanlythuchi.services;

import java.awt.Color;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.util.ArrayList;
import quanlythuchi.model.ChooseColor;
import quanlythuchi.model.GiaoDich;
import quanlythuchi.model.TienThuChi;
import quanlythuchi.model.TheLoai;
import quanlythuchi.model.ThongBao;

public class ConnDatabase {

    private Connection conn;

    public ConnDatabase() {
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

    public ArrayList<GiaoDich> getGiaoDich(int idTaiKhoan) {
        ArrayList<GiaoDich> list = new ArrayList<GiaoDich>();
        String sql = "select HinhThuc,SoTien,ThoiGian,TenTheLoai,MoTa,ID_GiaoDich,GIAODICH.ID_TheLoai from TAIKHOAN,GIAODICH,THELOAI WHERE TAIKHOAN.ID_TaiKhoan = " + idTaiKhoan + "  and TAIKHOAN.ID_TaiKhoan = GIAODICH.ID_TaiKhoan AND GIAODICH.ID_TheLoai = THELOAI.ID_TheLoai order by ThoiGian DESC";
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                list.add(new GiaoDich(rs.getString(1), rs.getInt(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6),rs.getInt(7)));
            }
            rs.close();
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public ArrayList<GiaoDich> getGiaoDichTime(int idTaiKhoan, String timeFirst, String timeSecon) {
        ArrayList<GiaoDich> list = new ArrayList<GiaoDich>();
        String sql = "select HinhThuc,SoTien,ThoiGian,TenTheLoai,MoTa,ID_GiaoDich,GIAODICH.ID_TheLoai from TAIKHOAN,GIAODICH,THELOAI WHERE TAIKHOAN.ID_TaiKhoan = " + idTaiKhoan + "  and TAIKHOAN.ID_TaiKhoan = GIAODICH.ID_TaiKhoan AND GIAODICH.ID_TheLoai = THELOAI.ID_TheLoai AND ThoiGian between '"+timeFirst+"' and '"+timeSecon+"' order by ThoiGian DESC ";
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                list.add(new GiaoDich(rs.getString(1), rs.getInt(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6),rs.getInt(7)));
            }
            rs.close();
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public ArrayList<GiaoDich> getGiaoDichwhere(int idTaiKhoan, String object, String where) {
        ArrayList<GiaoDich> list = new ArrayList<GiaoDich>();
        String sql = "select HinhThuc,SoTien,ThoiGian,TenTheLoai,MoTa,ID_GiaoDich,GIAODICH.ID_TheLoai from TAIKHOAN,GIAODICH,THELOAI WHERE TAIKHOAN.ID_TaiKhoan = " + idTaiKhoan + "  and TAIKHOAN.ID_TaiKhoan = GIAODICH.ID_TaiKhoan AND GIAODICH.ID_TheLoai = THELOAI.ID_TheLoai AND " + object + "  like N'%" + where + "%' order by ThoiGian DESC";
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                list.add(new GiaoDich(rs.getString(1), rs.getInt(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6),rs.getInt(7)));
            }
            rs.close();
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public String tenTheLoai(int idTheLoai) {
        String tenTL = null;
        String sql = "select TenTheLoai\n"
                + "from THELOAI\n"
                + "where ID_TheLoai=" + idTheLoai + "";
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                tenTL = rs.getString(1);
            }
            rs.close();
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tenTL;
    }

    public ArrayList<TheLoai> getTheLoaiChi() {
        ArrayList<TheLoai> listTheLoai = new ArrayList<TheLoai>();
        String sql = "select ID_TheLoai,TenTheLoai,GioiHan,ID_Cha\n"
                + "from THELOAI where HinhThuc=N'Chi tiền'";
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                listTheLoai.add(new TheLoai(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4)));
            }
            rs.close();
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listTheLoai;
    }
    public TheLoai getTheLoai(int idTheLoai) {
        TheLoai TheLoai = null ;
        String sql = "select ID_TheLoai,TenTheLoai,GioiHan,ID_Cha\n"
                + "from THELOAI where ID_TheLoai=" + idTheLoai + "";
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                TheLoai = new TheLoai(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4));
            }
            rs.close();
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return TheLoai;
    }
    public int getIdCha(int idTheLoai) {
        int idcha=0;
        String sql = "select ID_Cha\n"
                + "from THELOAI where ID_TheLoai=" + idTheLoai + "";
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                idcha = rs.getInt(1);
            }
            rs.close();
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return idcha;
    }
    public ArrayList<TheLoai> getTheLoaiThu() {
        ArrayList<TheLoai> listTheLoai = new ArrayList<TheLoai>();
        String sql = "select ID_TheLoai,TenTheLoai,GioiHan,ID_Cha\n"
                + "from THELOAI where HinhThuc=N'Thu tiền'";
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                listTheLoai.add(new TheLoai(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4)));
            }
            rs.close();
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listTheLoai;
    }

    public int getIdTheLoai(String tenTheLoai) {
        try {
            String select = "select * from THELOAI where TenTheLoai=N'" + tenTheLoai + "'";
            Statement state = conn.createStatement();
            ResultSet rs = state.executeQuery(select);
            while (rs.next()) {
                return rs.getInt(1);
            }
            rs.close();
            state.close();
        } catch (Exception e) {
        }
        return 0;
    }

    public void upDateGiaoDich(String thoigian, long sotien, String mota, int idtaikhoan, int idtheloai, int idgiaodich) {
        try {
            String update = "update GiaoDich set [ThoiGian]=N'" + thoigian + "',[SoTien] = " + sotien + ", [MoTa] = N'" + mota + "', [ID_TaiKhoan] = " + idtaikhoan + ", [ID_TheLoai]=" + idtheloai + " where ID_GiaoDich=" + idgiaodich + " ";
            Statement st = conn.createStatement();
            st.executeUpdate(update);
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteGiaoDich(int idgiaodich) {
        try {
            String delete = "delete from GIAODICH  where ID_GiaoDich=" + idgiaodich + " ";
            Statement st = conn.createStatement();
            st.executeUpdate(delete);
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertGiaoDich(String thoigian, long sotien, String mota, int idtaikhoan, int idtheloai) {
        try {
            String insert_into = "insert into GIAODICH (ThoiGian,SoTien,MoTa,ID_TaiKhoan,ID_TheLoai) values('" + thoigian + "'," + sotien + ",N'" + mota + "'," + idtaikhoan + "," + idtheloai + ")";
            Statement st = conn.createStatement();
            st.executeUpdate(insert_into);
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public long tongThuTime(int idtaikhoan,String timeFirst, String timeSecon) {
        try {
            String select = "select SoTien,HinhThuc,ThoiGian from GIAODICH,THELOAI where ID_TaiKhoan=" + idtaikhoan + " and GIAODICH.ID_TheLoai=THELOAI.ID_TheLoai and HinhThuc=N'Thu tiền' AND ThoiGian between '"+timeFirst+"' and '"+timeSecon+"' ";
            Statement state = conn.createStatement();
            ResultSet rs = state.executeQuery(select);
            long thu = 0;
            while (rs.next()) {
                thu = thu + rs.getLong(1);
            }
            rs.close();
            state.close();
            return thu;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    public long tongThu(int idtaikhoan) {
        try {
            String select = "select SoTien,HinhThuc from GIAODICH,THELOAI where ID_TaiKhoan=" + idtaikhoan + " and GIAODICH.ID_TheLoai=THELOAI.ID_TheLoai and HinhThuc=N'Thu tiền'";
            Statement state = conn.createStatement();
            ResultSet rs = state.executeQuery(select);
            long thu = 0;
            while (rs.next()) {
                thu = thu + rs.getLong(1);
            }
            rs.close();
            state.close();
            return thu;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    public long tongChiTime(int idtaikhoan,String timeFirst, String timeSecon) {
        try {
            String select = "select SoTien,HinhThuc from GIAODICH,THELOAI where ID_TaiKhoan=" + idtaikhoan + " and GIAODICH.ID_TheLoai=THELOAI.ID_TheLoai and HinhThuc=N'Chi tiền' AND ThoiGian between '"+timeFirst+"' and '"+timeSecon+"' ";
            Statement state = conn.createStatement();
            ResultSet rs = state.executeQuery(select);
            long Chi = 0;
            while (rs.next()) {
                Chi = Chi + rs.getLong(1);
            }
            rs.close();
            state.close();
            return Chi;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    public long tongChi(int idtaikhoan) {
        try {
            String select = "select SoTien,HinhThuc from GIAODICH,THELOAI where ID_TaiKhoan=" + idtaikhoan + " and GIAODICH.ID_TheLoai=THELOAI.ID_TheLoai and HinhThuc=N'Chi tiền'";
            Statement state = conn.createStatement();
            ResultSet rs = state.executeQuery(select);
            long Chi = 0;
            while (rs.next()) {
                Chi = Chi + rs.getLong(1);
            }
            rs.close();
            state.close();
            return Chi;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    public ArrayList<TienThuChi> tongTienCuaTheLoaiChiTime(int idTaiKhoan,String timeFirst, String timeSecon) {
        ArrayList<TienThuChi> list = new ArrayList<TienThuChi>();
        String sql_null = "select THELOAI.ID_TheLoai,ID_Cha,SoTien,HinhThuc\n"
                + "from GIAODICH,THELOAI\n"
                + "where GIAODICH.ID_TheLoai=THELOAI.ID_TheLoai\n"
                + "and ID_TaiKhoan=" + idTaiKhoan + "\n"
                + "and HinhThuc=N'Chi tiền' and ID_Cha IS NULL\n"
                + "AND ThoiGian between '"+timeFirst+"' and '"+timeSecon+"'";
        String sql_notnull = "select THELOAI.ID_TheLoai,ID_Cha,SoTien,HinhThuc\n"
                + "from GIAODICH,THELOAI\n"
                + "where GIAODICH.ID_TheLoai=THELOAI.ID_TheLoai\n"
                + "and ID_TaiKhoan=" + idTaiKhoan + "\n"
                + "and HinhThuc=N'Chi tiền' and ID_Cha IS NOT NULL\n"
                + "AND ThoiGian between '"+timeFirst+"' and '"+timeSecon+"'";
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql_null);
            int indexcolor = 0;
            ArrayList<Color> colors = new ChooseColor().getarraycolor();
            while (rs.next()) {
                    list.add(new TienThuChi(tenTheLoai(rs.getInt(1)), rs.getLong(3), colors.get(indexcolor)));
                    indexcolor++;
            }
            rs.close();
            ResultSet rs_notnull = st.executeQuery(sql_notnull);
            while(rs_notnull.next()){
                    //Ngược lại:
                    if (list.isEmpty()) {//Nếu list chưa có phần tử nào
                        list.add(new TienThuChi(tenTheLoai(rs_notnull.getInt(2)), rs_notnull.getLong(3), colors.get(indexcolor)));
                        indexcolor++;
                    } else {//Nhưng, nếu list có phần tử rồi
                        //kiểm tra xem có tồn tại thể loại cha chưa
                        int check=0;// Dùng để kiểm tra có thuộc lớp cha nào không
                        int index=0;
                        for (TienThuChi al : list) {// với mỗi phần tử trong list
                            //Nếu tên Thể loại cha lấy từ rs = tên thể loại cha trong list al.get(1)
                            if (tenTheLoai(rs_notnull.getInt(2)).equals(al.getName())) {
                                //Thêm giá trị số tiền vào phần tử list al này
                                check=rs_notnull.getInt(2);
//                                al.setValues(al.getValues()+(double)rs_notnull.getLong(3));
                                break;
                            }
                            index++;
                            //Ngược lại
//                            else {
//                                list.add(new TienThuChi(tenTheLoai(rs_notnull.getInt(2)), rs_notnull.getLong(3), colors.get(indexcolor)));
//                                indexcolor++;
//                            }  
                        }
                        if(check==0){//Nếu không
                            list.add(new TienThuChi(tenTheLoai(rs_notnull.getInt(2)), rs_notnull.getLong(3), colors.get(indexcolor)));
                            indexcolor++;
                        }
                        else {
                            list.get(index).setValues( list.get(index).getValues() + (double)rs_notnull.getLong(3) );
                        }
                        
                    }
            }
            rs_notnull.close();
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    public ArrayList<TienThuChi> tongTienCuaTheLoaiThuTime(int idTaiKhoan,String timeFirst, String timeSecon) {
        ArrayList<TienThuChi> list = new ArrayList<TienThuChi>();
        String sql_null = "select THELOAI.ID_TheLoai,ID_Cha,SoTien,HinhThuc\n"
                + "from GIAODICH,THELOAI\n"
                + "where GIAODICH.ID_TheLoai=THELOAI.ID_TheLoai\n"
                + "and ID_TaiKhoan=" + idTaiKhoan + "\n"
                + "and HinhThuc=N'Thu tiền' and ID_Cha IS NULL\n"
                + "AND ThoiGian between '"+timeFirst+"' and '"+timeSecon+"'";
        String sql_notnull = "select THELOAI.ID_TheLoai,ID_Cha,SoTien,HinhThuc\n"
                + "from GIAODICH,THELOAI\n"
                + "where GIAODICH.ID_TheLoai=THELOAI.ID_TheLoai\n"
                + "and ID_TaiKhoan=" + idTaiKhoan + "\n"
                + "and HinhThuc=N'Thu tiền' and ID_Cha IS NOT NULL\n"
                + "AND ThoiGian between '"+timeFirst+"' and '"+timeSecon+"'";
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql_null);
            int indexcolor = 0;
            ArrayList<Color> colors = new ChooseColor().getarraycolor();
            while (rs.next()) {
                    list.add(new TienThuChi(tenTheLoai(rs.getInt(1)), rs.getLong(3), colors.get(indexcolor)));
                    indexcolor++;
            }
            rs.close();
            ResultSet rs_notnull = st.executeQuery(sql_notnull);
            while(rs_notnull.next()){
                    //Ngược lại:
                    if (list.isEmpty()) {//Nếu list chưa có phần tử nào
                        list.add(new TienThuChi(tenTheLoai(rs_notnull.getInt(2)), rs_notnull.getLong(3), colors.get(indexcolor)));
                        indexcolor++;
                    } else {//Nhưng, nếu list có phần tử rồi
                        //kiểm tra xem có tồn tại thể loại cha chưa
                        int check=0;// Dùng để kiểm tra có thuộc lớp cha nào không
                        int index=0;
                        for (TienThuChi al : list) {// với mỗi phần tử trong list
                            //Nếu tên Thể loại cha lấy từ rs = tên thể loại cha trong list al.get(1)
                            if (tenTheLoai(rs_notnull.getInt(2)).equals(al.getName())) {
                                //Thêm giá trị số tiền vào phần tử list al này
                                check=rs_notnull.getInt(2);
//                                al.setValues(al.getValues()+(double)rs_notnull.getLong(3));
                                break;
                            }
                            index++;
                            //Ngược lại
//                            else {
//                                list.add(new TienThuChi(tenTheLoai(rs_notnull.getInt(2)), rs_notnull.getLong(3), colors.get(indexcolor)));
//                                indexcolor++;
//                            }  
                        }
                        if(check==0){//Nếu không
                            list.add(new TienThuChi(tenTheLoai(rs_notnull.getInt(2)), rs_notnull.getLong(3), colors.get(indexcolor)));
                            indexcolor++;
                        }
                        else {
                            list.get(index).setValues( list.get(index).getValues() + (double)rs_notnull.getLong(3) );
                        }
                        
                    }
            }
            rs_notnull.close();
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    public boolean changePass(String matkhaumoi,int idTaiKhoan) {
        try {
            String update = "update TAIKHOAN set [MatKhau]=N'" + matkhaumoi + "' where ID_TaiKhoan=" + idTaiKhoan + " ";
            Statement st = conn.createStatement();
            if (st.executeUpdate(update) == 1){
                st.close();
                return true;
            }
            else {
                st.close();
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public ArrayList<ThongBao> getThongBao(int idTaiKhoan) {
        ArrayList<ThongBao> list = new ArrayList<ThongBao>();
        String sql = "select ID_ThongBao,ThongBao,ID_TaiKhoan from THONGBAO WHERE ID_TaiKhoan = " + idTaiKhoan + " order by ID_ThongBao DESC ";
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                list.add(new ThongBao(rs.getString(2), rs.getInt(3), rs.getInt(1)));
            }
            rs.close();
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    public void insertThongBao(String thongbao,int idtaikhoan) {
        try {
            String insert_into = "insert into THONGBAO (ThongBao,ID_TaiKhoan) values(N'"+thongbao+"'," +idtaikhoan+ " )";
            Statement st = conn.createStatement();
            st.executeUpdate(insert_into);
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void deleteThongBao(int idthongbao) {
        try {
            String delete = "delete from THONGBAO  where ID_ThongBao=" + idthongbao + " ";
            Statement st = conn.createStatement();
            st.executeUpdate(delete);
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
