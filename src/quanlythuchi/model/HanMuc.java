package quanlythuchi.model;

public class HanMuc {
    private int id_TaiKhoan;
    private int id_TheLoai;
    private long gioiHan;
    private String tenHanMuc;
    private String ghiChu;
    

    public int getId_TaiKhoan() {
        return id_TaiKhoan;
    }

    public void setId_TaiKhoan(int id_TaiKhoan) {
        this.id_TaiKhoan = id_TaiKhoan;
    }

    public int getId_TheLoai() {
        return id_TheLoai;
    }

    public void setId_TheLoai(int id_TheLoai) {
        this.id_TheLoai = id_TheLoai;
    }

    public long getGioiHan() {
        return gioiHan;
    }

    public void setGioiHan(long gioiHan) {
        this.gioiHan = gioiHan;
    }

    public String getTenHanMuc() {
        return tenHanMuc;
    }

    public void setTenHanMuc(String tenHanMuc) {
        this.tenHanMuc = tenHanMuc;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }
    
    public HanMuc() {
    }

//    public HanMuc(int id_TaiKhoan, int id_TheLoai, long gioiHan, String tenHanMuc) {
//        this.id_TaiKhoan = id_TaiKhoan;
//        this.id_TheLoai = id_TheLoai;
//        this.gioiHan = gioiHan;
//        this.tenHanMuc = tenHanMuc;
//    }

    public HanMuc(int id_TaiKhoan, int id_TheLoai, long gioiHan, String tenHanMuc, String ghiChu) {
        this.id_TaiKhoan = id_TaiKhoan;
        this.id_TheLoai = id_TheLoai;
        this.gioiHan = gioiHan;
        this.tenHanMuc = tenHanMuc;
        this.ghiChu = ghiChu;
    }
    
    
}
