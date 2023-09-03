package models;

import java.awt.Color;

public class TienThuChi {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getValues() {
        return values;
    }

    public void setValues(double values) {
        this.values = values;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getId_cha() {
        return id_cha;
    }

    public void setId_cha(int id_cha) {
        this.id_cha = id_cha;
    }

    public TienThuChi(String name, double values, Color color) {
        this.name = name;
        this.values = values;
        this.color = color;
    }

    public TienThuChi() {
    }

    private String name;
    private double values;
    private Color color;
    private int id_cha;
}
