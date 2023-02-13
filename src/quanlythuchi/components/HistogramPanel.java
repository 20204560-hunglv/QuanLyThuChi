package quanlythuchi.components;

import java.awt.*;
import java.text.NumberFormat;
import java.util.List;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.*;

public class HistogramPanel extends JPanel {

    private int histogramHeight = 200;
    private int barWidth = 50;
    private int barGap = 10;

    private JPanel barPanel;
    private JPanel labelPanel;

    private List<Bar> bars = new ArrayList<Bar>();

    public HistogramPanel() {
        setOpaque(false);
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setLayout(new BorderLayout());
        barPanel = new JPanel(new GridLayout(1, 0, barGap, 0));
        barPanel.setBackground(Color.WHITE);
        Border outer = new MatteBorder(1, 1, 1, 1, new Color(255, 255, 255));
        Border inner = new EmptyBorder(10, 10, 0, 10);
        Border compound = new CompoundBorder(outer, inner);
        barPanel.setBorder(compound);

        labelPanel = new JPanel(new GridLayout(1, 0, barGap, 0));
        labelPanel.setBackground(Color.WHITE);
        labelPanel.setBorder(new EmptyBorder(5, 10, 0, 10));

        add(barPanel, BorderLayout.CENTER);
        add(labelPanel, BorderLayout.PAGE_END);
    }
//định dạng số sang kiểu Việt Nam: có dấu ',' ngăn cách 
    private String formatNBer(long number){
        NumberFormat currentLocale = NumberFormat.getInstance();
		
	// định dạng số của khu vực mặc định của máy ảo JVM
	// sử dụng phương thức format()
	return currentLocale.format(number);
    }
    public void clearData() {
        bars.clear();
    }

    public void addHistogramColumn(String label, long value, Color color) {
        Bar bar = new Bar(label, value, color);
        bars.add(bar);
    }

    public void layoutHistogram() {
        barPanel.removeAll();
        labelPanel.removeAll();

        long maxValue = 0;

        for (Bar bar : bars) {
            maxValue = Math.max(maxValue, bar.getValue());
        }

        for (Bar bar : bars) {
            JLabel label = new JLabel(formatNBer(bar.getValue()) + "đ");
            label.setForeground(new Color(25, 150, 245));
            label.setHorizontalTextPosition(JLabel.CENTER);
            label.setHorizontalAlignment(JLabel.CENTER);
            label.setVerticalTextPosition(JLabel.TOP);
            label.setVerticalAlignment(JLabel.BOTTOM);
            // nếu chưa có dữ liệu thì maxValue =0, thì không chia được
            long barHeight = (bar.getValue() * histogramHeight) / maxValue;
            Icon icon = new ColorIcon(bar.getColor(), barWidth, barHeight);
            label.setIcon(icon);
            barPanel.add(label);

            JLabel barLabel = new JLabel(bar.getLabel());
            barLabel.setHorizontalAlignment(JLabel.CENTER);
            barLabel.setForeground(new Color(25, 150, 245));
            labelPanel.add(barLabel);
        }
    }

    private class Bar {

        private String label;
        private long value;
        private Color color;

        public Bar(String label, long value, Color color) {
            this.label = label;
            this.value = value;
            this.color = color;
        }

        public String getLabel() {
            return label;
        }

        public long getValue() {
            return value;
        }

        public Color getColor() {
            return color;
        }
    }

    private class ColorIcon implements Icon {

        private int shadow = 3;

        private Color color;
        private int width;
        private long height;

        public ColorIcon(Color color, int width, long height) {
            this.color = color;
            this.width = width;
            this.height = height;
        }

        public int getIconWidth() {
            return width;
        }

        public int getIconHeight() {
            return (int) height;
        }

        public void paintIcon(Component c, Graphics g, int x, int y) {
            g.setColor(color);
            g.fillRect(x, y, width - shadow, (int) height);
            g.setColor(Color.GRAY);
            g.fillRect(x + width - shadow, y + shadow, shadow, (int) (height - shadow));
        }
    }

}
