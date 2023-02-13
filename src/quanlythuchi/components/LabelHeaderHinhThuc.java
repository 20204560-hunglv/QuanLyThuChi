package quanlythuchi.components;

import java.awt.Color;
import javax.swing.JLabel;

public class LabelHeaderHinhThuc extends JLabel {

    private String hinhThuc;

    public LabelHeaderHinhThuc(String hinhThuc) {
        this.hinhThuc = hinhThuc;
        setHorizontalAlignment(CENTER);
        setSize(30, HEIGHT);
        setText(hinhThuc);
        if (hinhThuc.equals("Chi tiền")) {
            setForeground(new Color(239,83,97));
        } else {
            setForeground(new Color(44,184,75));
        }
//        repaint();
    }

//    @Override
//    protected void paintComponent(Graphics grphcs) {
//        if (hinhThuc != null) {
//            Graphics2D g2 = (Graphics2D) grphcs;
//            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//            GradientPaint g;
//            if (hinhThuc.equals("Chi tiền")) {
//                g = new GradientPaint(0, 0, new Color(255,99,71), 0, getHeight(), new Color(254, 95, 74));
//            } 
//            else {
//                g = new GradientPaint(0, 0, new Color(0,255,127), 0, getHeight(), new Color(5, 253, 132));
//            }
//            g2.setPaint(g);
//            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 1, 1);
//        }
//        super.paintComponent(grphcs);
//    }
}
