package quanlythuchi.model;

public class GiaoDich {

    public GiaoDich() {
    }

    public GiaoDich(String hinhThuc, long soTien, String thoiGian, String theLoai, String moTa, int id_giaodich, int id_theloai) {
        this.hinhThuc = hinhThuc;
        this.soTien = soTien;
        this.thoiGian = thoiGian;
        this.theLoai = theLoai;
        this.moTa = moTa;
        this.id_giaodich = id_giaodich;
        this.id_theloai = id_theloai;
    }
    
    
    public String getHinhThuc() {
        return hinhThuc;
    }

    public void setHinhThuc(String hinhThuc) {
        this.hinhThuc = hinhThuc;
    }

    public long getSoTien() {
        return soTien;
    }

    public void setSoTien(int soTien) {
        this.soTien = soTien;
    }

    public String getThoiGian() {
        return thoiGian;
    }

    public void setThoiGian(String thoiGian) {
        this.thoiGian = thoiGian;
    }

    public String getTheLoai() {
        return theLoai;
    }

    public void setTheLoai(String theLoai) {
        this.theLoai = theLoai;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public int getId_giaodich() {
        return id_giaodich;
    }

    public void setId_giaodich(int id_giaodich) {
        this.id_giaodich = id_giaodich;
    }

    public int getId_theloai() {
        return id_theloai;
    }

    public void setId_theloai(int id_theloai) {
        this.id_theloai = id_theloai;
    }
    
    private String hinhThuc;
    private long soTien;
    private String thoiGian;
    private String theLoai;
    private String moTa;
    private int id_giaodich;
    private int id_theloai;
    @Override
    public String toString() {
        return hinhThuc+" "+soTien+" "+thoiGian+" "+theLoai+" "+moTa;
    }
    
    
}
