package models;

public class ThongBao {
    private int ID_ThongBao;
    private String thongBao;
    private int ID_TaiKhoan;

    public int getID_ThongBao() {
        return ID_ThongBao;
    }

    public void setID_ThongBao(int ID_ThongBao) {
        this.ID_ThongBao = ID_ThongBao;
    }
    

    public String getThongBao() {
        return thongBao;
    }

    public void setThongBao(String thongBao) {
        this.thongBao = thongBao;
    }

    public int getID_TaiKhoan() {
        return ID_TaiKhoan;
    }

    public void setID_TaiKhoan(int ID_TaiKhoan) {
        this.ID_TaiKhoan = ID_TaiKhoan;
    }

    public ThongBao() {
    }

    public ThongBao(String thongBao, int ID_TaiKhoan,int ID_ThongBao) {
        this.thongBao = thongBao;
        this.ID_TaiKhoan = ID_TaiKhoan;
        this.ID_ThongBao=ID_ThongBao;
    }
    
}
