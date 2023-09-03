package components;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import static java.awt.image.ImageObserver.HEIGHT;
import java.util.ArrayList;
import javax.swing.JLabel;
import static javax.swing.SwingConstants.CENTER;
import models.ChooseColor;

public class LabelHeaderHinhThuc1 extends JLabel {

    private String hinhThuc;
    private int indexColor;
    private ArrayList<Color> colors = new ChooseColor().getarraycolor();
    public LabelHeaderHinhThuc1(String hinhThuc, int indexColor) {
        this.hinhThuc = hinhThuc;
        this.indexColor=indexColor;
        setHorizontalAlignment(CENTER);
        setSize(30, HEIGHT);
        setFont(new Font("sansserif", 0, 14));
        setText(hinhThuc);
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics grphcs) {
        if (hinhThuc != null) {
            Graphics2D g2 = (Graphics2D) grphcs;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            GradientPaint g;
            if (indexColor==0) {
                g = new GradientPaint(0, 0, colors.get(indexColor), 0, getHeight(), colors.get(indexColor));
            } 
            else if (indexColor==1) {
                g = new GradientPaint(0, 0, colors.get(indexColor), 0, getHeight(), colors.get(indexColor));
            }else if (indexColor==2) {
                g = new GradientPaint(0, 0, colors.get(indexColor), 0, getHeight(), colors.get(indexColor));
            }else if (indexColor==3) {
                g = new GradientPaint(0, 0, colors.get(indexColor), 0, getHeight(), colors.get(indexColor));
            }else if (indexColor==4) {
                g = new GradientPaint(0, 0, colors.get(indexColor), 0, getHeight(), colors.get(indexColor));
            }else if (indexColor==5) {
                g = new GradientPaint(0, 0, colors.get(indexColor), 0, getHeight(), colors.get(indexColor));
            }else if (indexColor==6) {
                g = new GradientPaint(0, 0, colors.get(indexColor), 0, getHeight(), colors.get(indexColor));
            }else if (indexColor==7) {
                g = new GradientPaint(0, 0, colors.get(indexColor), 0, getHeight(), colors.get(indexColor));
            }else if (indexColor==8) {
                g = new GradientPaint(0, 0, colors.get(indexColor), 0, getHeight(), colors.get(indexColor));
            }else {
                g = new GradientPaint(0, 0, colors.get(9), 0, getHeight(), colors.get(9));
            }
            g2.setPaint(g);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 1, 1);
        }
        super.paintComponent(grphcs);
    }

}
