package quanlythuchi.model;

public class TaiKhoan {

    public TaiKhoan() {
    }

    public TaiKhoan(int idTaiKhoan, String tenDangNhap, String matKhau, int gioiHanChi) {
        this.idTaiKhoan = idTaiKhoan;
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.gioiHanChi = gioiHanChi;
    }

    public int getIdTaiKhoan() {
        return idTaiKhoan;
    }

    public void setIdTaiKhoan(int idTaiKhoan) {
        this.idTaiKhoan = idTaiKhoan;
    }

    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public void setTenDangNhap(String tenDangNhap) {
        this.tenDangNhap = tenDangNhap;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public int getGioiHanChi() {
        return gioiHanChi;
    }

    public void setGioiHanChi(int gioiHanChi) {
        this.gioiHanChi = gioiHanChi;
    }
    
    private int idTaiKhoan;
    private String tenDangNhap;
    private String matKhau;
    private int gioiHanChi;
}
