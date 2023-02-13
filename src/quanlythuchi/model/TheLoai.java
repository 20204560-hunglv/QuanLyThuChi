package quanlythuchi.model;

public class TheLoai {

    public TheLoai() {
    }

    public TheLoai(int idTheLoai, String tenTheLoai, int gioiHan, int idCha) {
        this.idTheLoai = idTheLoai;
        this.tenTheLoai = tenTheLoai;
        this.gioiHan = gioiHan;
        this.idCha = idCha;
    }

    public int getIdTheLoai() {
        return idTheLoai;
    }

    public void setIdTheLoai(int idTheLoai) {
        this.idTheLoai = idTheLoai;
    }

    public String getTenTheLoai() {
        return tenTheLoai;
    }

    public void setTenTheLoai(String tenTheLoai) {
        this.tenTheLoai = tenTheLoai;
    }

    public int getGioiHan() {
        return gioiHan;
    }

    public void setGioiHan(int gioiHan) {
        this.gioiHan = gioiHan;
    }

    public int getIdCha() {
        return idCha;
    }

    public void setIdCha(int idCha) {
        this.idCha = idCha;
    }
    
    private int idTheLoai;
    private String tenTheLoai;
    private int gioiHan;
    private int idCha;

    @Override
    public String toString() {
        return tenTheLoai;
    }
    
}
