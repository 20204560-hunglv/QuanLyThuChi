package view;

import Controller.UserController;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import components.LabelHeaderHinhThuc1;
import components.MessageDialog;
import components.PieChart;
import components.ScrollBar;
import components.TableHeader;
import services.ConnDatabase;
import models.GiaoDich;
import models.HanMuc;
import models.TienThuChi;
import models.TaiKhoan;
import models.TheLoai;
import models.ThongBao;

public class Home extends javax.swing.JFrame {

    private final DecimalFormat format = new DecimalFormat("#,##0.#");
    private ConnDatabase cd = new ConnDatabase();
    private DefaultTableModel defaultTableModel;
    private TaiKhoan tk;
    private int index = -1, indexSlideBefore, indexColor, indexThongBao = -1, indexRowHanMuc = -1;
    private int Id_GiaoDich, ID_ThongBao;
    private ArrayList<ThongBao> listThongBao;
    private ArrayList<TheLoai> listChi;
    private ArrayList<TheLoai> listThu;
    private ArrayList<GiaoDich> list;
    private ArrayList<HanMuc> listHanMuc;
    private ArrayList<TienThuChi> modelPieCharts;
    private String firstTime, seconTime, nowTime;
    private long taiKhoanGoc, thu, chi, hieu;
    private UserController uc = new UserController();

    public Home(TaiKhoan tk1) {
        initComponents();
        setIconImage(new ImageIcon(Home.class.getResource("/quanlythuchi/img/icons8_Stack_of_Money_127px.png")).getImage());
        setTk(tk1);
        setTable();
        setTableGanDay();
        setScrollBar();
        initSlide();
        jLabel17.setText("Chào " + tk.getTenDangNhap() + "!");
        thangNay();
        initChart();
        initBarChart();
        Time();
    }
//set Cursor

    private void setCursor() {
        button2.setCursor(Cursor.getPredefinedCursor(HAND_CURSOR));
    }

    private String getPercentage(double phantram) {
        return format.format(phantram);
    }

    private void checkHanMuc(int idTheLoai) {
        String tenhanmuc = cd.tenTheLoai(idTheLoai);
        thangNay();
        modelPieCharts = cd.tongTienCuaTheLoaiChiTime(tk.getIdTaiKhoan(), firstTime, seconTime);
        listHanMuc = cd.getHanMuc(tk.getIdTaiKhoan());
        int check = 0;//check xem Thể loại này có thiết lập hạn mức không 
        HanMuc hanmucCanTim = null;
        for (HanMuc hanmuc : listHanMuc) {
            if (hanmuc.getId_TheLoai() == idTheLoai) {
                hanmucCanTim = hanmuc;
                check++;
                break;
            }
        }
        if (check != 0) {// Nếu có
            for (TienThuChi tienThuChi : modelPieCharts) {
                if (tienThuChi.getName().equals(tenhanmuc)) {
                    if (tienThuChi.getValues() >= (double) hanmucCanTim.getGioiHan()) {
                        MessageDialog messageDialog = new MessageDialog(this);
                        messageDialog.showMessage("Thông báo", "Bạn đã chi tiền đến giới hạn của hạn mức " + hanmucCanTim.getTenHanMuc() + "!");
                        cd.insertThongBao(nowTime + "Bạn đã chi tiền đến giới hạn của hạn mức " + hanmucCanTim.getTenHanMuc() + "!", tk.getIdTaiKhoan());
                    }
                }
            }
        }
    }

    private void ghiChu() {
        thangNay();
        modelPieCharts = cd.tongTienCuaTheLoaiChiTime(tk.getIdTaiKhoan(), firstTime, seconTime);
        listHanMuc = cd.getHanMuc(tk.getIdTaiKhoan());
        for (HanMuc hanmuc : listHanMuc) {
            int idTheLoai = hanmuc.getId_TheLoai();
            String tenhanmuc = cd.tenTheLoai(idTheLoai);
            for (TienThuChi tienThuChi : modelPieCharts) {
                if (tienThuChi.getName().equals(tenhanmuc)) {
                    double phantram = (tienThuChi.getValues()) / ((double) hanmuc.getGioiHan()) * 100;
                    String ghiChu = getPercentage(phantram) + "%";
                    cd.upDateHanMuc(tk.getIdTaiKhoan(), idTheLoai, ghiChu);
                }
            }
        }
    }

// chart
    private void initBarChart() {
        long thu, chi, hieu;
        thu = cd.tongThuTime(tk.getIdTaiKhoan(), firstTime, seconTime);
        chi = cd.tongChiTime(tk.getIdTaiKhoan(), firstTime, seconTime);
        hieu = thu - chi;
        if (thu != 0 || chi != 0) {
            histogramPanel.clearData();
            histogramPanel.addHistogramColumn("Thu tiền", thu, new Color(44, 184, 75));
            histogramPanel.addHistogramColumn("Chi tiền", chi, new Color(239, 83, 97));
            histogramPanel.layoutHistogram();
            histogramPanel.setVisible(true);
        } else {
            PanelThongSo.setVisible(false);
            histogramPanel.setVisible(false);
        }
    }

    private void initBarChart1() {
        long thu, chi, hieu;
        thu = cd.tongThuTime(tk.getIdTaiKhoan(), firstTime, seconTime);
        chi = cd.tongChiTime(tk.getIdTaiKhoan(), firstTime, seconTime);
        hieu = thu - chi;
        if (thu != 0 || chi != 0) {
            labelThu.setText(formatNBer(thu) + " đ");
            labelChi.setText(formatNBer(chi) + " đ");
            labelHieu.setText(formatNBer(hieu) + " đ");
            histogramPanel1.clearData();
            histogramPanel1.addHistogramColumn("Thu tiền", thu, new Color(44, 184, 75));
            histogramPanel1.addHistogramColumn("Chi tiền", chi, new Color(239, 83, 97));
            histogramPanel1.setVisible(true);
            PanelThongSo.setVisible(true);
            histogramPanel1.layoutHistogram();
        } else {
            PanelThongSo.setVisible(false);
            histogramPanel1.setVisible(false);
        }
    }

    private void initChartChi() {
        modelPieCharts = cd.tongTienCuaTheLoaiChiTime(tk.getIdTaiKhoan(), firstTime, seconTime);
        pieChart2.setChartType(PieChart.PeiChartType.DONUT_CHART);
        pieChart2.clearData();
        for (TienThuChi mpc : modelPieCharts) {
            pieChart2.addData(mpc);
        }
    }

    private void initChartThu() {
        modelPieCharts = cd.tongTienCuaTheLoaiThuTime(tk.getIdTaiKhoan(), firstTime, seconTime);
        pieChart2.setChartType(PieChart.PeiChartType.DONUT_CHART);
        pieChart2.clearData();
        for (TienThuChi mpc : modelPieCharts) {
            pieChart2.addData(mpc);
        }
    }

    private void initChart() {
        modelPieCharts = cd.tongTienCuaTheLoaiChiTime(tk.getIdTaiKhoan(), firstTime, seconTime);
        pieChart1.setChartType(PieChart.PeiChartType.DONUT_CHART);
        pieChart1.clearData();
        for (TienThuChi mpc : modelPieCharts) {
            pieChart1.addData(mpc);
        }

    }
//Table

    private void tableBieuDo() {
        if (modelPieCharts.isEmpty()) {
            jScrollPaneBieuDo.setVisible(false);
        } else {
            jScrollPaneBieuDo.setVisible(true);
            DefaultTableModel defaultTableModelBieuDo = (DefaultTableModel) jTableBieuDo.getModel();
            defaultTableModelBieuDo.setRowCount(0);
            for (TienThuChi mpc : modelPieCharts) {
                defaultTableModelBieuDo.addRow(new Object[]{mpc.getName(), (long) mpc.getValues()});
            }
        }
    }

    private void tableThongBao() {
        listThongBao = cd.getThongBao(tk.getIdTaiKhoan());
        if (listThongBao.isEmpty()) {
            jPanelTbao.setVisible(false);
        } else {
            jPanelTbao.setVisible(true);
            DefaultTableModel defaultTableModelThongBao = (DefaultTableModel) jTableThongBao.getModel();
            defaultTableModelThongBao.setRowCount(0);
            for (ThongBao tb : listThongBao) {
                defaultTableModelThongBao.addRow(new Object[]{tb.getThongBao()});
            }
        }
    }

    private void getTableGanDay() {
        list.clear();
        list = cd.getGiaoDich(tk.getIdTaiKhoan());
        defaultTableModel = (DefaultTableModel) tableGanDay.getModel();
        defaultTableModel.setRowCount(0);
        int index = list.size();
        if (index > 3) {
            index = 3;
        }
        for (int i = 0; i < index; i++) {
            String hinh_thuc = list.get(i).getHinhThuc();
            double soTien = list.get(i).getSoTien();
            String thoiGian = list.get(i).getThoiGian();
            String theLoai = list.get(i).getTheLoai();
            String moTa = list.get(i).getMoTa();
            defaultTableModel.addRow(new Object[]{hinh_thuc, (long) soTien, thoiGian, theLoai, moTa});
        }
    }

    private void getTableTime(String timeFirst, String timeSecon) {
        ConnDatabase cd = new ConnDatabase();
        list = cd.getGiaoDichTime(tk.getIdTaiKhoan(), timeFirst, timeSecon);
        defaultTableModel = (DefaultTableModel) jTable.getModel();
        defaultTableModel.setRowCount(0);
        int index = list.size();
        for (int i = 0; i < index; i++) {
            String hinh_thuc = list.get(i).getHinhThuc();
            double soTien = list.get(i).getSoTien();
            String thoiGian = list.get(i).getThoiGian();
            String theLoai = list.get(i).getTheLoai();
            String moTa = list.get(i).getMoTa();
            defaultTableModel.addRow(new Object[]{hinh_thuc, (long) soTien, thoiGian, theLoai, moTa});
        }
    }

    private void getTablewhere(String object, String where) {
        ConnDatabase cd = new ConnDatabase();
        list.clear();
        list = cd.getGiaoDichwhere(tk.getIdTaiKhoan(), object, where);
        defaultTableModel = (DefaultTableModel) jTable.getModel();
        defaultTableModel.setRowCount(0);
        int index = list.size();
        for (int i = 0; i < index; i++) {
            String hinh_thuc = list.get(i).getHinhThuc();
            double soTien = list.get(i).getSoTien();
            String thoiGian = list.get(i).getThoiGian();
            String theLoai = list.get(i).getTheLoai();
            String moTa = list.get(i).getMoTa();
            defaultTableModel.addRow(new Object[]{hinh_thuc, (long) soTien, thoiGian, theLoai, moTa});
        }
    }

    private void getTableHanMuc() {
        ConnDatabase cd = new ConnDatabase();
        listHanMuc = cd.getHanMuc(tk.getIdTaiKhoan());
        defaultTableModel = (DefaultTableModel) jTableHanMuc.getModel();
        defaultTableModel.setRowCount(0);
        int index = listHanMuc.size();
        for (int i = 0; i < index; i++) {
            long gioihan = listHanMuc.get(i).getGioiHan();
            String tenHanMuc = listHanMuc.get(i).getTenHanMuc();
            String tenTheLoai = cd.tenTheLoai(listHanMuc.get(i).getId_TheLoai());
            String ghiChu = listHanMuc.get(i).getGhiChu();
            defaultTableModel.addRow(new Object[]{tenHanMuc, tenTheLoai, gioihan, ghiChu});
        }
    }

    private void setTable() {
//        jTable1.setShowHorizontalLines(true); //trong properties của jtable1
//        jTable1.setRowHeight(40);
        jTable.getTableHeader().setReorderingAllowed(false);// không cho phép sắp xếp lại cột
        jTable.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable jtable, Object o, boolean bln, boolean bln1, int i, int i1) {
                //i: hàng của ô để hiển thị
                //i1: cột của ô để hiển thị
                TableHeader header = new TableHeader(o + "");
                if (i1 == 1) {//Nếu là cột số tiền thì căn phải
                    header.setHorizontalAlignment(JLabel.RIGHT);
                } else {
                    header.setHorizontalAlignment(JLabel.CENTER);
                }
                return header;
            }
        });
        jTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable jtable, Object o, boolean selected, boolean bln1, int i, int i1) {
                if (i1 == 1) { // Căn phải các giá trị của cột Số Tiền
                    JLabel jLabelRight = new JLabel(formatNBer((long) o) + " đ");
                    jLabelRight.setOpaque(true);
                    jLabelRight.setFont(new Font("sansserif", 0, 14));
                    if (selected) { //Nếu click hoặc không click vào thì thiết lập màu
                        jLabelRight.setBackground(new Color(224, 224, 224));
                    } else {
                        jLabelRight.setBackground(Color.WHITE);
                    }
                    jLabelRight.setHorizontalAlignment(JLabel.RIGHT);
                    return jLabelRight;
                }
                if (i1 != 0) {  //Ngược lại, nếu khác cột "Hình thức", "Số tiền", thì căn giữa, thiết lập màu
                    Component com = super.getTableCellRendererComponent(jtable, o, selected, bln1, i, i1);
                    com.setFont(new Font("sansserif", 0, 14));
                    com.setForeground(new Color(33, 33, 33));
                    setHorizontalAlignment(CENTER);
                    setBorder(noFocusBorder);
                    if (selected) {
                        com.setBackground(new Color(224, 224, 224));
                    } else {
                        com.setBackground(Color.WHITE);
                    }
                    return com;
                } else { // ngược lại, nếu là cột "Hình thức", thì 
                    String hinh_thuc = (String) o;
                    JLabel labelHinhThuc = new JLabel(hinh_thuc);
                    labelHinhThuc.setOpaque(true);
                    labelHinhThuc.setFont(new Font("sansserif", 1, 14));
                    if (hinh_thuc.equals("Chi tiền")) {
                        labelHinhThuc.setForeground(new Color(239, 83, 97));
                    } else {
                        labelHinhThuc.setForeground(new Color(44, 184, 75));
                    }
                    if (selected) {
                        labelHinhThuc.setBackground(new Color(224, 224, 224));
                    } else {
                        labelHinhThuc.setBackground(Color.WHITE);
                    }
                    labelHinhThuc.setHorizontalAlignment(JLabel.CENTER);
                    return labelHinhThuc;
                }
            }
        });
    }

    private void setTableBieuDo() {
        jTableBieuDo.setRowHeight(50);
        jTableBieuDo.getTableHeader().setReorderingAllowed(false);// không cho phép sắp xếp lại cột
        jTableBieuDo.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable jtable, Object o, boolean bln, boolean bln1, int i, int i1) {
                //i: hàng của ô để hiển thị
                //i1: cột của ô để hiển thị
                TableHeader header = new TableHeader(o + "");
                header.setHorizontalAlignment(JLabel.CENTER);// chỉnh căn giữa các header
                return header;
            }
        });
        jTableBieuDo.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable jtable, Object o, boolean selected, boolean bln1, int i, int i1) {
                if (i1 == 1) { // Căn phải các giá trị của cột Số Tiền
                    JLabel jLabelRight = new JLabel(formatNBer((long) o) + " đ                    ");
                    jLabelRight.setOpaque(true);
                    jLabelRight.setForeground(new Color(33, 33, 33));
                    jLabelRight.setBackground(Color.WHITE);
                    jLabelRight.setFont(new Font("sansserif", 0, 14));
                    if (selected) { //Nếu click hoặc không click vào thì thiết lập màu
                        jLabelRight.setBackground(new Color(224, 224, 224));
                    } else {
                        jLabelRight.setBackground(Color.WHITE);
                    }
                    jLabelRight.setHorizontalAlignment(JLabel.RIGHT);
                    return jLabelRight;
                } else { // ngược lại, nếu là cột "Hình thức", thì 
                    String hinh_thuc = (String) o;
                    LabelHeaderHinhThuc1 labelHinhThuc1 = new LabelHeaderHinhThuc1(hinh_thuc, i);
                    return labelHinhThuc1;// thiết lập label màu
                }
            }
        });
    }

    private void setTableHanMuc() {
        jTableHanMuc.setRowHeight(50);
        jTableHanMuc.getTableHeader().setReorderingAllowed(false);// không cho phép sắp xếp lại cột
        jTableHanMuc.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable jtable, Object o, boolean bln, boolean bln1, int i, int i1) {
                //i: hàng của ô để hiển thị
                //i1: cột của ô để hiển thị
                TableHeader header = new TableHeader(o + "");
                header.setHorizontalAlignment(JLabel.CENTER);// chỉnh căn giữa các header
                return header;
            }
        });
        jTableHanMuc.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable jtable, Object o, boolean selected, boolean bln1, int i, int i1) {
                if (i1 == 2) { // Căn phải các giá trị của cột Số Tiền
                    JLabel jLabelRight = new JLabel(formatNBer((long) o) + " đ");
                    jLabelRight.setOpaque(true);
                    jLabelRight.setForeground(new Color(33, 33, 33));
                    jLabelRight.setBackground(Color.WHITE);
                    jLabelRight.setFont(new Font("sansserif", 0, 14));
                    if (selected) { //Nếu click hoặc không click vào thì thiết lập màu
                        jLabelRight.setBackground(new Color(224, 224, 224));
                    } else {
                        jLabelRight.setBackground(Color.WHITE);
                    }
                    jLabelRight.setHorizontalAlignment(JLabel.RIGHT);
                    return jLabelRight;
                } else { // ngược lại, nếu là cột "Hình thức", thì 
                    JLabel jLabel = new JLabel(o + "");
                    jLabel.setOpaque(true);
                    jLabel.setForeground(new Color(33, 33, 33));
                    jLabel.setFont(new Font("sansserif", 0, 14));
                    if (selected) { //Nếu click hoặc không click vào thì thiết lập màu
                        jLabel.setBackground(new Color(224, 224, 224));
                    } else {
                        jLabel.setBackground(Color.WHITE);
                    }
                    jLabel.setHorizontalAlignment(JLabel.CENTER);
                    return jLabel;
                }
            }
        });
    }

    private void setTableGanDay() {
        tableGanDay.getTableHeader().setReorderingAllowed(false);// không cho phép sắp xếp lại cột
        tableGanDay.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable jtable, Object o, boolean bln, boolean bln1, int i, int i1) {
                //i: hàng của ô để hiển thị
                //i1: cột của ô để hiển thị
                TableHeader header = new TableHeader(o + "");
                if (i1 == 1) {//Nếu là cột số tiền thì căn phải
                    header.setHorizontalAlignment(JLabel.RIGHT);
                } else {
                    header.setHorizontalAlignment(JLabel.CENTER);
                }
                return header;
            }
        });
        tableGanDay.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable jtable, Object o, boolean selected, boolean bln1, int i, int i1) {
                if (i1 == 1) {
                    JLabel jLabelRight = new JLabel(formatNBer((long) o) + " đ");
                    jLabelRight.setOpaque(true);
                    jLabelRight.setFont(new Font("sansserif", 0, 14));
                    if (selected) { //Nếu click hoặc không click vào thì thiết lập màu
                        jLabelRight.setBackground(new Color(224, 224, 224));
                    } else {
                        jLabelRight.setBackground(Color.WHITE);
                    }
                    jLabelRight.setHorizontalAlignment(JLabel.RIGHT);
                    return jLabelRight;
                } else if (i1 != 0) {
                    Component com = super.getTableCellRendererComponent(jtable, o, selected, bln1, i, i1);
                    com.setFont(new Font("sansserif", 0, 14));
                    com.setForeground(new Color(33, 33, 33));
                    setHorizontalAlignment(CENTER);
                    setBorder(noFocusBorder);
                    if (selected) {
                        com.setBackground(new Color(224, 224, 224));
                    } else {
                        com.setBackground(Color.WHITE);
                    }
                    return com;
                } else {
                    String hinh_thuc = (String) o;
                    JLabel labelHinhThuc = new JLabel(hinh_thuc);
                    labelHinhThuc.setOpaque(true);
                    labelHinhThuc.setFont(new Font("sansserif", 1, 14));
                    if (hinh_thuc.equals("Chi tiền")) {
                        labelHinhThuc.setForeground(new Color(239, 83, 97));
                    } else {
                        labelHinhThuc.setForeground(new Color(44, 184, 75));
                    }
                    if (selected) {
                        labelHinhThuc.setBackground(new Color(224, 224, 224));
                    } else {
                        labelHinhThuc.setBackground(Color.WHITE);
                    }
                    labelHinhThuc.setHorizontalAlignment(JLabel.CENTER);
                    return labelHinhThuc;
                }
            }
        });
    }

    private void setTableThongBao() {
        jTableThongBao.setRowHeight(40);
        jTableThongBao.getTableHeader().setReorderingAllowed(false);// không cho phép sắp xếp lại cột
        jTableThongBao.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable jtable, Object o, boolean bln, boolean bln1, int i, int i1) {
                //i: hàng của ô để hiển thị
                //i1: cột của ô để hiển thị
                JLabel header = new JLabel(o + "");
                header.setOpaque(true);
                header.setBackground(Color.WHITE);
                header.setFont(new Font("sansserif", 1, 14));
                header.setForeground(new Color(0, 0, 0));
                header.setBorder(new EmptyBorder(10, 5, 10, 5));
                header.setHorizontalAlignment(JLabel.CENTER);
                return header;
            }
        });
        jTableThongBao.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable jtable, Object o, boolean selected, boolean bln1, int i, int i1) {
                JLabel jLabel = new JLabel(o + "");
                jLabel.setOpaque(true);
                jLabel.setForeground(new Color(33, 33, 33));
                jLabel.setFont(new Font("sansserif", 0, 14));
                if (selected) { //Nếu click hoặc không click vào thì thiết lập màu
                    jLabel.setBackground(new Color(224, 224, 224));
                } else {
                    jLabel.setBackground(Color.WHITE);
                }
                jLabel.setHorizontalAlignment(JLabel.CENTER);
                return jLabel;
            }
        });
    }
//Time

    public void Time() {
        new Timer(0, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Date d = new Date();
                SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                nowTime = s.format(d);
            }
        }
        ).start();
    }
//định dạng số sang kiểu Việt Nam: có dấu ',' ngăn cách 

    private String formatNBer(long number) {
        NumberFormat currentLocale = NumberFormat.getInstance();

        // định dạng số của khu vực mặc định của máy ảo JVM
        // sử dụng phương thức format()
        return currentLocale.format(number);
    }

    private void thangNay() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);// đưa về ngày đầu tháng
        Date d = cal.getTime();
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        firstTime = s.format(d);

        cal.add(Calendar.MONTH, 1);//tăng tháng lên 1, đưa về ngày đầu tháng sau
        cal.add(Calendar.DAY_OF_MONTH, -1);// trừ 1 sẽ về ngày cuối của tháng này
        d = cal.getTime();
        seconTime = s.format(d);
    }

    private void thangTruoc() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);// đưa về ngày đầu tháng
        cal.add(Calendar.DAY_OF_MONTH, -1);// trừ 1 sẽ về ngày cuối của tháng trước
        Date d = cal.getTime();
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        seconTime = s.format(d);

        s = new SimpleDateFormat("yyyy-MM-01");
        firstTime = s.format(d);
    }

    private void ThangCustom() {
        firstTime = textTuNgay1.getText();
        seconTime = textDenNgay1.getText();
    }

// Scroll
    private void setScrollBarHanMuc() {
        jScrollPaneHanMuc.getViewport().setBackground(Color.WHITE);
        jScrollPaneHanMuc.setVerticalScrollBar(new ScrollBar());
    }

    private void setScrollBar() {
        tableScrollPane.getViewport().setBackground(Color.WHITE); // phông nền tableScrollPane
        tableScrollPane.setVerticalScrollBar(new ScrollBar());
        tableScrollPane1.getViewport().setBackground(Color.WHITE); // phông nền tableScrollPane
        tableScrollPane1.setVerticalScrollBar(new ScrollBar());
    }

    private void setScroolBarBieuDo() {
        jScrollPaneBieuDo.getViewport().setBackground(Color.WHITE); // phông nền tableScrollPane
        jScrollPaneBieuDo.setVerticalScrollBar(new ScrollBar());
    }

    private void setScroolBarThongBao() {
        jScrollPaneThongBao.getViewport().setBackground(Color.WHITE); // phông nền tableScrollPane
        jScrollPaneThongBao.setVerticalScrollBar(new ScrollBar());
    }

    private void initSlide() {
        Slide.init(TongQuan, PanelXemChiTiet, PanelBieuDo, giaoDich, ThietLap, ThongBao);
        Slide.setAnimate(30);
        panelSlideHanMuc.init(jPanelHan_Muc_1, jPanelHan_Muc_2, jPanelHan_Muc_3);
    }

    private void getListTL() {
        listChi = cd.getTheLoaiChi();
        listThu = cd.getTheLoaiThu();
    }

    private void getTheLoai() {
        if (comboBoxThuChi.getSelectedIndex() == 0) {
            comboBoxTL.removeAllItems();
            for (TheLoai theLoai : listChi) {
                if (theLoai.getIdCha() == 0) {
                    comboBoxTL.addItem(theLoai);
                }
            }
            comboBoxTL.setSelectedIndex(0);
        } else {
            comboBoxTL.removeAllItems();
            for (TheLoai theLoai : listThu) {
                if (theLoai.getIdCha() == 0) {
                    comboBoxTL.addItem(theLoai);
                }
            }
            comboBoxTL.setSelectedIndex(0);
        }
    }

    private void getTheLoai_SuaGD() {
        if (comboBox1.getSelectedIndex() == 0) {
            txtTheLoai.removeAllItems();
            for (TheLoai theLoai : listChi) {
                if (theLoai.getIdCha() == 0) {
                    txtTheLoai.addItem(theLoai);
                }
            }
            txtTheLoai.setSelectedIndex(-1);
        } else {
            txtTheLoai.removeAllItems();
            for (TheLoai theLoai : listThu) {
                if (theLoai.getIdCha() == 0) {
                    txtTheLoai.addItem(theLoai);
                }
            }
            txtTheLoai.setSelectedIndex(-1);
        }
    }

    private void getTheLoaiCon() {
        int selectedIndex = comboBoxTL.getSelectedIndex();
        if (selectedIndex != -1) {
            comboBoxTLcon.setSelectedIndex(-1);
            int id_cha = comboBoxTL.getItemAt(selectedIndex).getIdTheLoai();
            if (comboBoxThuChi.getSelectedIndex() == 0) {
                comboBoxTLcon.removeAllItems();
                for (TheLoai theLoai : listChi) {
                    if (theLoai.getIdCha() == id_cha) {
                        comboBoxTLcon.addItem(theLoai);
                    }
                }
                comboBoxTLcon.setSelectedIndex(-1);
            }
            if (comboBoxThuChi.getSelectedIndex() == 1) {
                comboBoxTLcon.removeAllItems();
                for (TheLoai theLoai : listThu) {
                    if (theLoai.getIdCha() == id_cha) {
                        comboBoxTLcon.addItem(theLoai);
                    }
                }
                comboBoxTLcon.setSelectedIndex(-1);
            }
        }
    }

    private void getTheLoaiCon_SuaGD() {
        int selectedIndex = txtTheLoai.getSelectedIndex();
        if (selectedIndex != -1) {
            comboBoxTLcon1.setSelectedIndex(-1);
            int id_cha = txtTheLoai.getItemAt(selectedIndex).getIdTheLoai();
            if (comboBox1.getSelectedIndex() == 0) {
                comboBoxTLcon1.removeAllItems();
                for (TheLoai theLoai : listChi) {
                    if (theLoai.getIdCha() == id_cha) {
                        comboBoxTLcon1.addItem(theLoai);
                    }
                }
                comboBoxTLcon1.setSelectedIndex(-1);
            }
            if (comboBox1.getSelectedIndex() == 1) {
                comboBoxTLcon1.removeAllItems();
                for (TheLoai theLoai : listThu) {
                    if (theLoai.getIdCha() == id_cha) {
                        comboBoxTLcon1.addItem(theLoai);
                    }
                }
                comboBoxTLcon1.setSelectedIndex(-1);
            }
        }
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setTk(TaiKhoan tk1) {
        this.tk = tk1;
    }

    private void soDu() {
        thangNay();
        taiKhoanGoc = cd.getTaiKhoanGoc(tk.getIdTaiKhoan());
        jFormattedTextField3.setValue(taiKhoanGoc);
        thu = cd.tongThuTime(tk.getIdTaiKhoan(), firstTime, seconTime);
        chi = cd.tongChiTime(tk.getIdTaiKhoan(), firstTime, seconTime);
        hieu = taiKhoanGoc + thu - chi;
        labelThu2.setText(formatNBer(thu) + " đ");
        labelChi2.setText(formatNBer(chi) + " đ");
        labelHieu2.setText(formatNBer(hieu) + " đ");
        jLabel13.setText(formatNBer(hieu) + " đ");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        DialogSua = new javax.swing.JDialog();
        jPanel7 = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        comboBox1 = new components.ComboBox();
        jSeparator4 = new javax.swing.JSeparator();
        txtTheLoai = new components.ComboBox<>();
        jLabel35 = new javax.swing.JLabel();
        comboBoxTLcon1 = new components.ComboBox<>();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        txtThoiGian = new components.TextField();
        txtMoTa = new components.TextField();
        jLabel38 = new javax.swing.JLabel();
        ButtonOk = new javax.swing.JButton();
        jLabelNoteSuaGD = new javax.swing.JLabel();
        txtSoTien = new javax.swing.JFormattedTextField();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        PanelXemChiTiet = new javax.swing.JPanel();
        tableScrollPane = new javax.swing.JScrollPane();
        jTable = new javax.swing.JTable();
        search = new components.ComboBox();
        searchtextField = new components.TextField();
        SuaButton = new components.Button();
        XoaButton = new components.Button();
        LamMoiButton = new components.Button();
        jLabel16 = new javax.swing.JLabel();
        ChonThangCBBox = new components.ComboBox();
        jPanelBieuDo1 = new javax.swing.JPanel();
        btnChon1 = new components.Button();
        textDenNgay1 = new components.TextField();
        textTuNgay1 = new components.TextField();
        button1 = new components.Button();
        PanelBieuDo = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel4 = new javax.swing.JPanel();
        pieChart2 = new components.PieChart();
        jScrollPaneBieuDo = new javax.swing.JScrollPane();
        jTableBieuDo = new javax.swing.JTable();
        comboBox3 = new components.ComboBox();
        jPanel8 = new javax.swing.JPanel();
        comboBoxBieuDo = new components.ComboBox();
        jPanelChooseDate1 = new javax.swing.JPanel();
        btnChon = new components.Button();
        textDenNgay = new components.TextField();
        textTuNgay = new components.TextField();
        histogramPanel1 = new components.HistogramPanel();
        PanelThongSo = new javax.swing.JPanel();
        labelChi = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        labelHieu = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        labelThu = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        dateChooser = new com.raven.datechooser.DateChooser();
        dateChooser1 = new com.raven.datechooser.DateChooser();
        dateChooser2 = new com.raven.datechooser.DateChooser();
        dateChooser3 = new com.raven.datechooser.DateChooser();
        dateChooser4 = new com.raven.datechooser.DateChooser();
        ChangePassDialog = new javax.swing.JDialog();
        jPanel2 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        MKmoi = new javax.swing.JPasswordField();
        jLabel30 = new javax.swing.JLabel();
        MKconfirm = new javax.swing.JPasswordField();
        button5 = new components.Button();
        noteMK = new javax.swing.JLabel();
        jSeparator5 = new javax.swing.JSeparator();
        jSeparator6 = new javax.swing.JSeparator();
        jSeparator7 = new javax.swing.JSeparator();
        MKcu = new javax.swing.JPasswordField();
        dateChooser5 = new com.raven.datechooser.DateChooser();
        DialogHanMuc = new javax.swing.JDialog();
        panelSlideHanMuc = new components.PanelSlide();
        jPanelHan_Muc_1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        button3 = new components.Button();
        button8 = new components.Button();
        jLabel42 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jScrollPaneHanMuc = new javax.swing.JScrollPane();
        jTableHanMuc = new javax.swing.JTable();
        btnXoaHM = new components.Button();
        btnSuaHM = new components.Button();
        jPanelHan_Muc_2 = new javax.swing.JPanel();
        jPanel27 = new javax.swing.JPanel();
        button33 = new components.Button();
        jLabel55 = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        jLabel43 = new javax.swing.JLabel();
        jFormattedTextField1 = new javax.swing.JFormattedTextField();
        jLabel44 = new javax.swing.JLabel();
        textField1 = new components.TextField();
        jLabel45 = new javax.swing.JLabel();
        comboBox4 = new components.ComboBox<>();
        button9 = new components.Button();
        jLabel46 = new javax.swing.JLabel();
        comboBox5 = new components.ComboBox();
        jLabel47 = new javax.swing.JLabel();
        jPanelHan_Muc_3 = new javax.swing.JPanel();
        jPanel28 = new javax.swing.JPanel();
        button34 = new components.Button();
        jLabel58 = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        jLabel48 = new javax.swing.JLabel();
        suaTienHM = new javax.swing.JFormattedTextField();
        jLabel49 = new javax.swing.JLabel();
        suaTenHM = new components.TextField();
        jLabel50 = new javax.swing.JLabel();
        suaComboboxHM = new components.ComboBox<>();
        suaHM = new components.Button();
        jLabel51 = new javax.swing.JLabel();
        comboBox7 = new components.ComboBox();
        jLabel59 = new javax.swing.JLabel();
        DialogSoDu = new javax.swing.JDialog();
        jPanel17 = new javax.swing.JPanel();
        button13 = new components.Button();
        jFormattedTextField3 = new javax.swing.JFormattedTextField();
        jLabel52 = new javax.swing.JLabel();
        button14 = new components.Button();
        jLabel53 = new javax.swing.JLabel();
        labelThu2 = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        labelChi2 = new javax.swing.JLabel();
        jLabel56 = new javax.swing.JLabel();
        jSeparator9 = new javax.swing.JSeparator();
        jLabel57 = new javax.swing.JLabel();
        labelHieu2 = new javax.swing.JLabel();
        button15 = new components.Button();
        jPanel1 = new javax.swing.JPanel();
        DangxuatButton = new components.Button();
        jPanel6 = new javax.swing.JPanel();
        buttonHome = new components.Button();
        buttonGiaoDich = new components.Button();
        buttonSetting = new components.Button();
        buttonThongBao = new components.Button();
        jLabel17 = new javax.swing.JLabel();
        Slide = new components.PanelSlide();
        TongQuan = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        pieChart1 = new components.PieChart();
        histogramPanel = new components.HistogramPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        comboBox2 = new components.ComboBox();
        jLabel12 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        tableScrollPane1 = new javax.swing.JScrollPane();
        tableGanDay = new javax.swing.JTable();
        jLabel15 = new javax.swing.JLabel();
        giaoDich = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        comboBoxTL = new components.ComboBox<>();
        txt_sotien = new components.TextField();
        txt_thoigian = new components.TextField();
        txt_mota = new components.TextField();
        comboBoxThuChi = new components.ComboBox();
        comboBoxTLcon = new components.ComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabelNoteGD = new javax.swing.JLabel();
        ThietLap = new javax.swing.JPanel();
        button2 = new components.Button();
        button4 = new components.Button();
        btnXoaTK = new components.Button();
        ThongBao = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jPanelTbao = new javax.swing.JPanel();
        jScrollPaneThongBao = new javax.swing.JScrollPane();
        jTableThongBao = new javax.swing.JTable();
        button6 = new components.Button();
        jLabel31 = new javax.swing.JLabel();

        DialogSua.setBackground(new java.awt.Color(255, 255, 255));
        DialogSua.setMinimumSize(new java.awt.Dimension(400, 600));
        DialogSua.setUndecorated(true);

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(25, 125, 225)));
        jPanel7.setPreferredSize(new java.awt.Dimension(400, 600));

        jLabel32.setBackground(new java.awt.Color(255, 255, 255));
        jLabel32.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(25, 150, 245));
        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel32.setText("X");
        jLabel32.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel32.setOpaque(true);
        jLabel32.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel32MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel32MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel32MouseExited(evt);
            }
        });

        jLabel34.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(25, 125, 225));
        jLabel34.setText("Hình thức");

        comboBox1.setBackground(new java.awt.Color(25, 150, 245));
        comboBox1.setForeground(new java.awt.Color(255, 255, 255));
        comboBox1.setMaximumRowCount(10);
        comboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Chi tiền", "Thu tiền" }));
        comboBox1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        comboBox1.setLabeText("");
        comboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBox1ActionPerformed(evt);
            }
        });

        jSeparator4.setForeground(new java.awt.Color(0, 153, 255));

        txtTheLoai.setForeground(new java.awt.Color(102, 102, 102));
        txtTheLoai.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        txtTheLoai.setLabeText("");
        txtTheLoai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTheLoaiActionPerformed(evt);
            }
        });

        jLabel35.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(25, 125, 225));
        jLabel35.setText("Thể loại");

        comboBoxTLcon1.setForeground(new java.awt.Color(102, 102, 102));
        comboBoxTLcon1.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        comboBoxTLcon1.setLabeText("");

        jLabel36.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(25, 125, 225));
        jLabel36.setText("Số tiền");

        jLabel37.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(25, 125, 225));
        jLabel37.setText("Ngày");
        jLabel37.setPreferredSize(new java.awt.Dimension(42, 20));

        txtThoiGian.setForeground(new java.awt.Color(102, 102, 102));
        txtThoiGian.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtThoiGian.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        txtThoiGian.setHint("Năm-Tháng-Ngày");

        txtMoTa.setForeground(new java.awt.Color(102, 102, 102));
        txtMoTa.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtMoTa.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        txtMoTa.setHint("");

        jLabel38.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(25, 125, 225));
        jLabel38.setText("Mô tả");

        ButtonOk.setBackground(new java.awt.Color(25, 150, 245));
        ButtonOk.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        ButtonOk.setForeground(new java.awt.Color(25, 125, 225));
        ButtonOk.setText("Lưu");
        ButtonOk.setContentAreaFilled(false);
        ButtonOk.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ButtonOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonOkActionPerformed(evt);
            }
        });

        jLabelNoteSuaGD.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        jLabelNoteSuaGD.setForeground(new java.awt.Color(255, 0, 0));
        jLabelNoteSuaGD.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        txtSoTien.setForeground(new java.awt.Color(102, 102, 102));
        txtSoTien.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,###"))));
        txtSoTien.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtSoTien.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N

        jLabel39.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel39.setIcon(new javax.swing.ImageIcon(getClass().getResource("/quanlythuchi/img/icons8_refresh_25px_1.png"))); // NOI18N
        jLabel39.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel39.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel39MouseClicked(evt);
            }
        });

        jLabel40.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel40.setIcon(new javax.swing.ImageIcon(getClass().getResource("/quanlythuchi/img/icons8_refresh_25px_1.png"))); // NOI18N
        jLabel40.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel40.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel40MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap(85, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel7Layout.createSequentialGroup()
                            .addComponent(jLabel37, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(txtThoiGian, javax.swing.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel7Layout.createSequentialGroup()
                            .addComponent(jLabel38)
                            .addGap(18, 18, 18)
                            .addComponent(txtMoTa, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(comboBoxTLcon1, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel7Layout.createSequentialGroup()
                            .addComponent(jLabel35)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtTheLoai, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addComponent(jSeparator4)
                        .addComponent(jLabel34, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(comboBox1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel7Layout.createSequentialGroup()
                            .addComponent(jLabel36)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(txtSoTien, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(ButtonOk, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabelNoteSuaGD, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel39)
                    .addComponent(jLabel40))
                .addContainerGap(52, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel32)
                        .addGap(17, 17, 17)
                        .addComponent(jLabel34)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(comboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTheLoai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(comboBoxTLcon1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel39))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel36, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtSoTien, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtThoiGian, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel40, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMoTa, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(73, 73, 73)
                .addComponent(ButtonOk, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabelNoteSuaGD, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32))
        );

        javax.swing.GroupLayout DialogSuaLayout = new javax.swing.GroupLayout(DialogSua.getContentPane());
        DialogSua.getContentPane().setLayout(DialogSuaLayout);
        DialogSuaLayout.setHorizontalGroup(
            DialogSuaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        DialogSuaLayout.setVerticalGroup(
            DialogSuaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        PanelXemChiTiet.setBackground(new java.awt.Color(255, 255, 255));
        PanelXemChiTiet.setPreferredSize(new java.awt.Dimension(1150, 700));

        tableScrollPane.setBackground(new java.awt.Color(255, 255, 255));

        jTable.setForeground(new java.awt.Color(255, 255, 255));
        jTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Hình Thức", "Số tiền", "Thời gian", "Thể Loại", "Mô tả"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable.setGridColor(new java.awt.Color(230, 230, 230));
        jTable.setRowHeight(40);
        jTable.setSelectionBackground(new java.awt.Color(102, 102, 255));
        jTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableMouseClicked(evt);
            }
        });
        tableScrollPane.setViewportView(jTable);
        if (jTable.getColumnModel().getColumnCount() > 0) {
            jTable.getColumnModel().getColumn(0).setPreferredWidth(5);
            jTable.getColumnModel().getColumn(1).setPreferredWidth(10);
        }

        search.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Hình Thức", "Thời gian", "Thể Loại" }));
        search.setSelectedIndex(-1);
        search.setLabeText("Tìm kiếm theo");
        search.setPreferredSize(new java.awt.Dimension(195, 50));

        searchtextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchtextFieldKeyReleased(evt);
            }
        });

        SuaButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/quanlythuchi/img/icons8_edit_20px.png"))); // NOI18N
        SuaButton.setText("Sửa");
        SuaButton.setBorderColor(new java.awt.Color(25, 150, 245));
        SuaButton.setColorClick(new java.awt.Color(66, 142, 179));
        SuaButton.setColorOver(new java.awt.Color(0, 228, 225));
        SuaButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SuaButtonActionPerformed(evt);
            }
        });

        XoaButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/quanlythuchi/img/icons8_delete_20px.png"))); // NOI18N
        XoaButton.setText("Xóa");
        XoaButton.setBorderColor(new java.awt.Color(25, 150, 245));
        XoaButton.setColorClick(new java.awt.Color(66, 142, 179));
        XoaButton.setColorOver(new java.awt.Color(0, 228, 225));
        XoaButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                XoaButtonActionPerformed(evt);
            }
        });

        LamMoiButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/quanlythuchi/img/icons8_refresh_20px.png"))); // NOI18N
        LamMoiButton.setText("Làm mới");
        LamMoiButton.setBorderColor(new java.awt.Color(25, 150, 245));
        LamMoiButton.setColorClick(new java.awt.Color(66, 142, 179));
        LamMoiButton.setColorOver(new java.awt.Color(0, 228, 225));
        LamMoiButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LamMoiButtonActionPerformed(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(25, 150, 245));
        jLabel16.setText("<< Quay lại");
        jLabel16.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel16.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel16MouseClicked(evt);
            }
        });

        ChonThangCBBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tháng này", "Tháng trước", "Tùy Chỉnh" }));
        ChonThangCBBox.setLabeText("");
        ChonThangCBBox.setPreferredSize(new java.awt.Dimension(100, 40));
        ChonThangCBBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChonThangCBBoxActionPerformed(evt);
            }
        });

        jPanelBieuDo1.setBackground(new java.awt.Color(255, 255, 255));
        jPanelBieuDo1.setPreferredSize(new java.awt.Dimension(310, 40));

        btnChon1.setText("Chọn");
        btnChon1.setBorderColor(new java.awt.Color(25, 150, 245));
        btnChon1.setColorClick(new java.awt.Color(51, 102, 255));
        btnChon1.setColorOver(new java.awt.Color(51, 204, 255));
        btnChon1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChon1ActionPerformed(evt);
            }
        });

        textDenNgay1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textDenNgay1.setHint("Đến ngày");

        textTuNgay1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textTuNgay1.setHint("Từ ngày");

        javax.swing.GroupLayout jPanelBieuDo1Layout = new javax.swing.GroupLayout(jPanelBieuDo1);
        jPanelBieuDo1.setLayout(jPanelBieuDo1Layout);
        jPanelBieuDo1Layout.setHorizontalGroup(
            jPanelBieuDo1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelBieuDo1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(textTuNgay1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(textDenNgay1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnChon1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelBieuDo1Layout.setVerticalGroup(
            jPanelBieuDo1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelBieuDo1Layout.createSequentialGroup()
                .addContainerGap(9, Short.MAX_VALUE)
                .addGroup(jPanelBieuDo1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textTuNgay1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textDenNgay1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnChon1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        button1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/quanlythuchi/img/icons8_add_20px.png"))); // NOI18N
        button1.setText("Thêm giao dịch");
        button1.setBorderColor(new java.awt.Color(25, 150, 245));
        button1.setColorClick(new java.awt.Color(66, 142, 179));
        button1.setColorOver(new java.awt.Color(0, 228, 225));
        button1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PanelXemChiTietLayout = new javax.swing.GroupLayout(PanelXemChiTiet);
        PanelXemChiTiet.setLayout(PanelXemChiTietLayout);
        PanelXemChiTietLayout.setHorizontalGroup(
            PanelXemChiTietLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelXemChiTietLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelXemChiTietLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelXemChiTietLayout.createSequentialGroup()
                        .addComponent(ChonThangCBBox, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanelBieuDo1, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 325, Short.MAX_VALUE)
                        .addComponent(search, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(searchtextField, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(tableScrollPane)
                    .addGroup(PanelXemChiTietLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(button1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(XoaButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(SuaButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(LamMoiButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel16)))
                .addContainerGap())
        );
        PanelXemChiTietLayout.setVerticalGroup(
            PanelXemChiTietLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelXemChiTietLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelXemChiTietLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(searchtextField, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(PanelXemChiTietLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jPanelBieuDo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(PanelXemChiTietLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(search, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ChonThangCBBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(9, 9, 9)
                .addComponent(tableScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 604, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelXemChiTietLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(PanelXemChiTietLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(XoaButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(button1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(SuaButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(LamMoiButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        PanelBieuDo.setBackground(new java.awt.Color(255, 255, 255));
        PanelBieuDo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(25, 150, 245)));
        PanelBieuDo.setPreferredSize(new java.awt.Dimension(1150, 700));

        jLabel10.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(25, 150, 245));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("<< quay lại");
        jLabel10.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel10MouseClicked(evt);
            }
        });

        jSeparator1.setForeground(new java.awt.Color(25, 150, 245));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        pieChart2.setBackground(new java.awt.Color(255, 255, 255));

        jTableBieuDo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Thể Loại", ""
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Long.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPaneBieuDo.setViewportView(jTableBieuDo);
        if (jTableBieuDo.getColumnModel().getColumnCount() > 0) {
            jTableBieuDo.getColumnModel().getColumn(0).setPreferredWidth(100);
        }

        comboBox3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Chi tiền", "Thu tiền" }));
        comboBox3.setLabeText("");
        comboBox3.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comboBox3ItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(comboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 44, 44)
                .addComponent(pieChart2, javax.swing.GroupLayout.PREFERRED_SIZE, 352, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPaneBieuDo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(72, 72, 72))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(0, 15, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPaneBieuDo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                    .addComponent(pieChart2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(comboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setPreferredSize(new java.awt.Dimension(1136, 276));

        comboBoxBieuDo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tháng này", "Tháng trước", "Tùy chọn" }));
        comboBoxBieuDo.setLabeText("");
        comboBoxBieuDo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxBieuDoActionPerformed(evt);
            }
        });

        jPanelChooseDate1.setBackground(new java.awt.Color(255, 255, 255));
        jPanelChooseDate1.setPreferredSize(new java.awt.Dimension(310, 40));

        btnChon.setText("Chọn");
        btnChon.setBorderColor(new java.awt.Color(25, 150, 245));
        btnChon.setColorClick(new java.awt.Color(0, 51, 255));
        btnChon.setColorOver(new java.awt.Color(0, 102, 255));
        btnChon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChonActionPerformed(evt);
            }
        });

        textDenNgay.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textDenNgay.setHint("Đến ngày");

        textTuNgay.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textTuNgay.setHint("Từ ngày");

        javax.swing.GroupLayout jPanelChooseDate1Layout = new javax.swing.GroupLayout(jPanelChooseDate1);
        jPanelChooseDate1.setLayout(jPanelChooseDate1Layout);
        jPanelChooseDate1Layout.setHorizontalGroup(
            jPanelChooseDate1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelChooseDate1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(textTuNgay, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(textDenNgay, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnChon, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelChooseDate1Layout.setVerticalGroup(
            jPanelChooseDate1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelChooseDate1Layout.createSequentialGroup()
                .addContainerGap(9, Short.MAX_VALUE)
                .addGroup(jPanelChooseDate1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textTuNgay, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textDenNgay, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnChon, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        histogramPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));

        PanelThongSo.setBackground(new java.awt.Color(255, 255, 255));

        labelChi.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        labelChi.setForeground(new java.awt.Color(255, 99, 71));
        labelChi.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        jLabel20.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(2, 255, 127));
        jLabel20.setText("Thu");

        labelHieu.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        labelHieu.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        jLabel21.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 99, 71));
        jLabel21.setText("Chi");

        jSeparator3.setForeground(new java.awt.Color(102, 102, 102));
        jSeparator3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        labelThu.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        labelThu.setForeground(new java.awt.Color(2, 255, 127));
        labelThu.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        javax.swing.GroupLayout PanelThongSoLayout = new javax.swing.GroupLayout(PanelThongSo);
        PanelThongSo.setLayout(PanelThongSoLayout);
        PanelThongSoLayout.setHorizontalGroup(
            PanelThongSoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelThongSoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelThongSoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator3)
                    .addComponent(labelHieu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(PanelThongSoLayout.createSequentialGroup()
                        .addComponent(jLabel20)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelThu, javax.swing.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE))
                    .addGroup(PanelThongSoLayout.createSequentialGroup()
                        .addComponent(jLabel21)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelChi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        PanelThongSoLayout.setVerticalGroup(
            PanelThongSoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelThongSoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelThongSoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelThu, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(PanelThongSoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelChi, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelHieu, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jLabel23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/quanlythuchi/img/icon-ong-bee-phan-van.png"))); // NOI18N

        jLabel24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/quanlythuchi/img/icons8_analytics_50px.png"))); // NOI18N

        jLabel25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/quanlythuchi/img/icons8_chart_50px.png"))); // NOI18N

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(comboBoxBieuDo, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanelChooseDate1, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                        .addGap(66, 66, 66)
                        .addComponent(jLabel23)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel24)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel25)))
                .addGap(119, 119, 119)
                .addComponent(PanelThongSo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(110, 110, 110)
                .addComponent(histogramPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 370, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(comboBoxBieuDo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanelChooseDate1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel23, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel24)
                            .addComponent(jLabel25)))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(histogramPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 17, Short.MAX_VALUE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(PanelThongSo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        javax.swing.GroupLayout PanelBieuDoLayout = new javax.swing.GroupLayout(PanelBieuDo);
        PanelBieuDo.setLayout(PanelBieuDoLayout);
        PanelBieuDoLayout.setHorizontalGroup(
            PanelBieuDoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelBieuDoLayout.createSequentialGroup()
                .addGroup(PanelBieuDoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelBieuDoLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel10))
                    .addGroup(PanelBieuDoLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, 1138, Short.MAX_VALUE))
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        PanelBieuDoLayout.setVerticalGroup(
            PanelBieuDoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelBieuDoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        dateChooser.setForeground(new java.awt.Color(25, 150, 245));
        dateChooser.setDateFormat("yyyy-MM-dd");
        dateChooser.setTextRefernce(txt_thoigian);

        dateChooser1.setForeground(new java.awt.Color(25, 150, 245));
        dateChooser1.setDateFormat("yyyy-MM-dd");
        dateChooser1.setTextRefernce(textTuNgay);

        dateChooser2.setForeground(new java.awt.Color(25, 150, 245));
        dateChooser2.setDateFormat("yyyy-MM-dd");
        dateChooser2.setTextRefernce(textDenNgay);

        dateChooser3.setForeground(new java.awt.Color(25, 150, 245));
        dateChooser3.setDateFormat("yyyy-MM-dd");
        dateChooser3.setTextRefernce(textTuNgay1);

        dateChooser4.setForeground(new java.awt.Color(25, 150, 245));
        dateChooser4.setDateFormat("yyyy-MM-dd");
        dateChooser4.setTextRefernce(textDenNgay1);

        ChangePassDialog.setMinimumSize(new java.awt.Dimension(400, 400));
        ChangePassDialog.setUndecorated(true);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(25, 150, 245)));
        jPanel2.setPreferredSize(new java.awt.Dimension(400, 400));

        jLabel18.setBackground(new java.awt.Color(255, 255, 255));
        jLabel18.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(25, 150, 245));
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setText("X");
        jLabel18.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel18.setOpaque(true);
        jLabel18.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel18MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel18MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel18MouseExited(evt);
            }
        });

        jLabel26.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(25, 125, 225));
        jLabel26.setText("Mật khẩu cũ");

        jLabel29.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(25, 125, 225));
        jLabel29.setText("Mật khẩu mới");

        MKmoi.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        MKmoi.setForeground(new java.awt.Color(25, 125, 225));
        MKmoi.setBorder(null);

        jLabel30.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(25, 125, 225));
        jLabel30.setText("Xác nhận mật khẩu");

        MKconfirm.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        MKconfirm.setForeground(new java.awt.Color(25, 125, 225));
        MKconfirm.setBorder(null);

        button5.setText("Lưu");
        button5.setBorderColor(new java.awt.Color(25, 125, 225));
        button5.setColorClick(new java.awt.Color(25, 125, 225));
        button5.setColorOver(new java.awt.Color(19, 103, 188));
        button5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button5ActionPerformed(evt);
            }
        });

        noteMK.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        noteMK.setForeground(new java.awt.Color(255, 0, 0));
        noteMK.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        MKcu.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        MKcu.setForeground(new java.awt.Color(25, 125, 225));
        MKcu.setBorder(null);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(99, 99, 99)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(MKcu)
                    .addComponent(noteMK, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(button5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(MKconfirm)
                    .addComponent(MKmoi)
                    .addComponent(jSeparator5, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator6, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel30)
                            .addComponent(jLabel29)
                            .addComponent(jLabel26))
                        .addGap(0, 116, Short.MAX_VALUE))
                    .addComponent(jSeparator7))
                .addGap(40, 40, 40)
                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addComponent(jLabel26))
                    .addComponent(jLabel18))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(MKcu, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel29)
                .addGap(5, 5, 5)
                .addComponent(MKmoi, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel30)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(MKconfirm, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(button5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(noteMK, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 8, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout ChangePassDialogLayout = new javax.swing.GroupLayout(ChangePassDialog.getContentPane());
        ChangePassDialog.getContentPane().setLayout(ChangePassDialogLayout);
        ChangePassDialogLayout.setHorizontalGroup(
            ChangePassDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        ChangePassDialogLayout.setVerticalGroup(
            ChangePassDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        dateChooser5.setForeground(new java.awt.Color(25, 150, 245));
        dateChooser5.setDateFormat("yyyy-MM-dd");
        dateChooser5.setTextRefernce(txtThoiGian);

        DialogHanMuc.setMinimumSize(new java.awt.Dimension(400, 600));
        DialogHanMuc.setUndecorated(true);

        panelSlideHanMuc.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(25, 150, 245)));
        panelSlideHanMuc.setLayout(new java.awt.CardLayout());

        jPanelHan_Muc_1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel3.setBackground(new java.awt.Color(25, 150, 245));

        button3.setForeground(new java.awt.Color(255, 255, 255));
        button3.setText("X");
        button3.setBorderColor(new java.awt.Color(25, 150, 245));
        button3.setColor(new java.awt.Color(25, 150, 245));
        button3.setColorClick(new java.awt.Color(0, 102, 255));
        button3.setColorOver(new java.awt.Color(0, 204, 255));
        button3.setFont(new java.awt.Font("SansSerif", 0, 24)); // NOI18N
        button3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button3ActionPerformed(evt);
            }
        });

        button8.setForeground(new java.awt.Color(255, 255, 255));
        button8.setText("+");
        button8.setBorderColor(new java.awt.Color(25, 150, 245));
        button8.setColor(new java.awt.Color(25, 150, 245));
        button8.setColorClick(new java.awt.Color(0, 102, 255));
        button8.setColorOver(new java.awt.Color(0, 204, 255));
        button8.setFont(new java.awt.Font("SansSerif", 0, 24)); // NOI18N
        button8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button8ActionPerformed(evt);
            }
        });

        jLabel42.setFont(new java.awt.Font("SansSerif", 0, 24)); // NOI18N
        jLabel42.setForeground(new java.awt.Color(255, 255, 255));
        jLabel42.setText("Hạn mức chi");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(button3, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(72, 72, 72)
                .addComponent(jLabel42)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 69, Short.MAX_VALUE)
                .addComponent(button8, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(button3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(button8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel42))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel14.setBackground(new java.awt.Color(255, 255, 255));

        jTableHanMuc.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Tên Hạn Mức", "Thể Loại", "Số tiền", "Ghi chú"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableHanMuc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableHanMucMouseClicked(evt);
            }
        });
        jScrollPaneHanMuc.setViewportView(jTableHanMuc);
        if (jTableHanMuc.getColumnModel().getColumnCount() > 0) {
            jTableHanMuc.getColumnModel().getColumn(0).setResizable(false);
            jTableHanMuc.getColumnModel().getColumn(1).setResizable(false);
            jTableHanMuc.getColumnModel().getColumn(2).setResizable(false);
            jTableHanMuc.getColumnModel().getColumn(3).setResizable(false);
            jTableHanMuc.getColumnModel().getColumn(3).setPreferredWidth(50);
        }

        btnXoaHM.setText("Xóa");
        btnXoaHM.setBorderColor(new java.awt.Color(25, 150, 245));
        btnXoaHM.setColorClick(new java.awt.Color(0, 102, 204));
        btnXoaHM.setColorOver(new java.awt.Color(0, 153, 255));
        btnXoaHM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaHMActionPerformed(evt);
            }
        });

        btnSuaHM.setText("Sửa");
        btnSuaHM.setBorderColor(new java.awt.Color(25, 150, 245));
        btnSuaHM.setColorClick(new java.awt.Color(0, 102, 204));
        btnSuaHM.setColorOver(new java.awt.Color(0, 153, 255));
        btnSuaHM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaHMActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPaneHanMuc, javax.swing.GroupLayout.DEFAULT_SIZE, 398, Short.MAX_VALUE)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnXoaHM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSuaHM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPaneHanMuc, javax.swing.GroupLayout.PREFERRED_SIZE, 488, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnXoaHM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSuaHM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanelHan_Muc_1Layout = new javax.swing.GroupLayout(jPanelHan_Muc_1);
        jPanelHan_Muc_1.setLayout(jPanelHan_Muc_1Layout);
        jPanelHan_Muc_1Layout.setHorizontalGroup(
            jPanelHan_Muc_1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanelHan_Muc_1Layout.setVerticalGroup(
            jPanelHan_Muc_1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelHan_Muc_1Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelSlideHanMuc.add(jPanelHan_Muc_1, "card2");

        jPanelHan_Muc_2.setBackground(new java.awt.Color(255, 255, 255));

        jPanel27.setBackground(new java.awt.Color(25, 150, 245));

        button33.setForeground(new java.awt.Color(255, 255, 255));
        button33.setText("<<");
        button33.setBorderColor(new java.awt.Color(25, 150, 245));
        button33.setColor(new java.awt.Color(25, 150, 245));
        button33.setColorClick(new java.awt.Color(0, 102, 255));
        button33.setColorOver(new java.awt.Color(0, 204, 255));
        button33.setFont(new java.awt.Font("SansSerif", 0, 24)); // NOI18N
        button33.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button33ActionPerformed(evt);
            }
        });

        jLabel55.setFont(new java.awt.Font("SansSerif", 0, 24)); // NOI18N
        jLabel55.setForeground(new java.awt.Color(255, 255, 255));
        jLabel55.setText("Thêm hạn mức");

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addComponent(button33, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(63, 63, 63)
                .addComponent(jLabel55)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(button33, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel55))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel15.setBackground(new java.awt.Color(255, 255, 255));

        jLabel43.setText("Số tiền");

        jFormattedTextField1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0"))));
        jFormattedTextField1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jFormattedTextField1.setFont(new java.awt.Font("SansSerif", 0, 24)); // NOI18N

        jLabel44.setText("Tên hạn mức");

        textField1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        textField1.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        textField1.setHint("");

        jLabel45.setText("Hạng mục");

        comboBox4.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        comboBox4.setLabeText("");

        button9.setForeground(new java.awt.Color(255, 255, 255));
        button9.setText("Lưu");
        button9.setBorderColor(new java.awt.Color(25, 150, 245));
        button9.setColor(new java.awt.Color(25, 150, 245));
        button9.setColorClick(new java.awt.Color(0, 153, 255));
        button9.setColorOver(new java.awt.Color(51, 204, 255));
        button9.setFont(new java.awt.Font("SansSerif", 0, 24)); // NOI18N
        button9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button9ActionPerformed(evt);
            }
        });

        jLabel46.setText("Thời gian");

        comboBox5.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tháng này" }));
        comboBox5.setEnabled(false);
        comboBox5.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        comboBox5.setLabeText("");

        jLabel47.setBackground(new java.awt.Color(255, 255, 255));
        jLabel47.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel47.setForeground(new java.awt.Color(255, 0, 0));
        jLabel47.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(textField1, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel15Layout.createSequentialGroup()
                            .addGap(76, 76, 76)
                            .addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel15Layout.createSequentialGroup()
                            .addGap(35, 35, 35)
                            .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel44)
                                .addComponent(jLabel43)
                                .addComponent(jLabel45)
                                .addComponent(jLabel46))))
                    .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel47, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(button9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 247, Short.MAX_VALUE)
                        .addComponent(comboBox4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(comboBox5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(72, Short.MAX_VALUE))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(jLabel43)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addComponent(jLabel44)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(textField1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addComponent(jLabel45)
                .addGap(18, 18, 18)
                .addComponent(comboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel46)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(comboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(button9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addComponent(jLabel47, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanelHan_Muc_2Layout = new javax.swing.GroupLayout(jPanelHan_Muc_2);
        jPanelHan_Muc_2.setLayout(jPanelHan_Muc_2Layout);
        jPanelHan_Muc_2Layout.setHorizontalGroup(
            jPanelHan_Muc_2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanelHan_Muc_2Layout.setVerticalGroup(
            jPanelHan_Muc_2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelHan_Muc_2Layout.createSequentialGroup()
                .addComponent(jPanel27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelSlideHanMuc.add(jPanelHan_Muc_2, "card3");

        jPanelHan_Muc_3.setBackground(new java.awt.Color(255, 255, 255));

        jPanel28.setBackground(new java.awt.Color(25, 150, 245));

        button34.setForeground(new java.awt.Color(255, 255, 255));
        button34.setText("<<");
        button34.setBorderColor(new java.awt.Color(25, 150, 245));
        button34.setColor(new java.awt.Color(25, 150, 245));
        button34.setColorClick(new java.awt.Color(0, 102, 255));
        button34.setColorOver(new java.awt.Color(0, 204, 255));
        button34.setFont(new java.awt.Font("SansSerif", 0, 24)); // NOI18N
        button34.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button34ActionPerformed(evt);
            }
        });

        jLabel58.setFont(new java.awt.Font("SansSerif", 0, 24)); // NOI18N
        jLabel58.setForeground(new java.awt.Color(255, 255, 255));
        jLabel58.setText("Sửa hạn mức");

        javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addComponent(button34, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(55, 55, 55)
                .addComponent(jLabel58)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel28Layout.setVerticalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(button34, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel58))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel16.setBackground(new java.awt.Color(255, 255, 255));

        jLabel48.setText("Số tiền");

        suaTienHM.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0"))));
        suaTienHM.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        suaTienHM.setFont(new java.awt.Font("SansSerif", 0, 24)); // NOI18N

        jLabel49.setText("Tên hạn mức");

        suaTenHM.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        suaTenHM.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        suaTenHM.setHint("");

        jLabel50.setText("Hạng mục");

        suaComboboxHM.setEnabled(false);
        suaComboboxHM.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        suaComboboxHM.setLabeText("");

        suaHM.setForeground(new java.awt.Color(255, 255, 255));
        suaHM.setText("Lưu");
        suaHM.setBorderColor(new java.awt.Color(25, 150, 245));
        suaHM.setColor(new java.awt.Color(25, 150, 245));
        suaHM.setColorClick(new java.awt.Color(0, 153, 255));
        suaHM.setColorOver(new java.awt.Color(51, 204, 255));
        suaHM.setFont(new java.awt.Font("SansSerif", 0, 24)); // NOI18N
        suaHM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                suaHMActionPerformed(evt);
            }
        });

        jLabel51.setText("Thời gian");

        comboBox7.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tháng này" }));
        comboBox7.setEnabled(false);
        comboBox7.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        comboBox7.setLabeText("");

        jLabel59.setBackground(new java.awt.Color(255, 255, 255));
        jLabel59.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel59.setForeground(new java.awt.Color(255, 0, 0));
        jLabel59.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(suaTenHM, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel16Layout.createSequentialGroup()
                            .addGap(76, 76, 76)
                            .addComponent(suaTienHM, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel16Layout.createSequentialGroup()
                            .addGap(35, 35, 35)
                            .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel49)
                                .addComponent(jLabel48)
                                .addComponent(jLabel50)
                                .addComponent(jLabel51))))
                    .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel59, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(suaHM, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 247, Short.MAX_VALUE)
                        .addComponent(suaComboboxHM, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(comboBox7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(72, Short.MAX_VALUE))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(jLabel48)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(suaTienHM, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addComponent(jLabel49)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(suaTenHM, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addComponent(jLabel50)
                .addGap(18, 18, 18)
                .addComponent(suaComboboxHM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel51)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(comboBox7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(suaHM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addComponent(jLabel59, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanelHan_Muc_3Layout = new javax.swing.GroupLayout(jPanelHan_Muc_3);
        jPanelHan_Muc_3.setLayout(jPanelHan_Muc_3Layout);
        jPanelHan_Muc_3Layout.setHorizontalGroup(
            jPanelHan_Muc_3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanelHan_Muc_3Layout.setVerticalGroup(
            jPanelHan_Muc_3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelHan_Muc_3Layout.createSequentialGroup()
                .addComponent(jPanel28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelSlideHanMuc.add(jPanelHan_Muc_3, "card3");

        javax.swing.GroupLayout DialogHanMucLayout = new javax.swing.GroupLayout(DialogHanMuc.getContentPane());
        DialogHanMuc.getContentPane().setLayout(DialogHanMucLayout);
        DialogHanMucLayout.setHorizontalGroup(
            DialogHanMucLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelSlideHanMuc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        DialogHanMucLayout.setVerticalGroup(
            DialogHanMucLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelSlideHanMuc, javax.swing.GroupLayout.DEFAULT_SIZE, 594, Short.MAX_VALUE)
        );

        DialogSoDu.setMinimumSize(new java.awt.Dimension(410, 600));
        DialogSoDu.setUndecorated(true);

        jPanel17.setBackground(new java.awt.Color(255, 255, 255));
        jPanel17.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(25, 150, 245)));
        jPanel17.setMinimumSize(new java.awt.Dimension(410, 600));

        button13.setText("X");
        button13.setBorderColor(new java.awt.Color(255, 255, 255));
        button13.setColorClick(new java.awt.Color(255, 0, 0));
        button13.setColorOver(new java.awt.Color(255, 0, 0));
        button13.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        button13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button13ActionPerformed(evt);
            }
        });

        jFormattedTextField3.setForeground(new java.awt.Color(25, 150, 245));
        jFormattedTextField3.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0"))));
        jFormattedTextField3.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jFormattedTextField3.setFont(new java.awt.Font("SansSerif", 0, 24)); // NOI18N

        jLabel52.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel52.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel52.setText("Tài khoản gốc");

        button14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/quanlythuchi/img/icons8_checked_checkbox_50px.png"))); // NOI18N
        button14.setBorderColor(new java.awt.Color(255, 255, 255));
        button14.setColorClick(new java.awt.Color(0, 153, 255));
        button14.setColorOver(new java.awt.Color(51, 204, 255));
        button14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button14ActionPerformed(evt);
            }
        });

        jLabel53.setFont(new java.awt.Font("SansSerif", 1, 24)); // NOI18N
        jLabel53.setForeground(new java.awt.Color(25, 150, 245));
        jLabel53.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel53.setText("Thiết lập số dư của tháng");

        labelThu2.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        labelThu2.setForeground(new java.awt.Color(2, 255, 127));
        labelThu2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        jLabel54.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        jLabel54.setForeground(new java.awt.Color(2, 255, 127));
        jLabel54.setText("Thu");

        labelChi2.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        labelChi2.setForeground(new java.awt.Color(255, 99, 71));
        labelChi2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        jLabel56.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        jLabel56.setForeground(new java.awt.Color(255, 99, 71));
        jLabel56.setText("Chi");

        jSeparator9.setForeground(new java.awt.Color(102, 102, 102));
        jSeparator9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel57.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel57.setText("Số dư");

        labelHieu2.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        labelHieu2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        button15.setText("Lưu");
        button15.setBorderColor(new java.awt.Color(25, 150, 245));
        button15.setColorClick(new java.awt.Color(0, 102, 204));
        button15.setColorOver(new java.awt.Color(51, 102, 255));
        button15.setFont(new java.awt.Font("SansSerif", 0, 24)); // NOI18N
        button15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button15ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                .addGap(0, 46, Short.MAX_VALUE)
                .addComponent(jLabel53, javax.swing.GroupLayout.PREFERRED_SIZE, 309, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                .addComponent(button13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel52)
                    .addComponent(jLabel54)
                    .addComponent(jLabel56)
                    .addComponent(jLabel57)
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(button15, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 310, Short.MAX_VALUE)
                            .addComponent(labelHieu2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(labelThu2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jFormattedTextField3, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelChi2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jSeparator9, javax.swing.GroupLayout.Alignment.LEADING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(button14, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(button13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(jLabel53, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(30, 30, 30)
                .addComponent(jLabel52)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jFormattedTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(button14, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jLabel54)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelThu2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel56)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelChi2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jSeparator9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel57)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelHieu2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addComponent(button15, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(47, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout DialogSoDuLayout = new javax.swing.GroupLayout(DialogSoDu.getContentPane());
        DialogSoDu.getContentPane().setLayout(DialogSoDuLayout);
        DialogSoDuLayout.setHorizontalGroup(
            DialogSoDuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 410, Short.MAX_VALUE)
            .addGroup(DialogSoDuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        DialogSoDuLayout.setVerticalGroup(
            DialogSoDuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 600, Short.MAX_VALUE)
            .addGroup(DialogSoDuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("QuanLyThuChi");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(25, 150, 245));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(25, 125, 225)));
        jPanel1.setPreferredSize(new java.awt.Dimension(150, 700));

        DangxuatButton.setText("Đăng xuất");
        DangxuatButton.setBorderColor(new java.awt.Color(25, 125, 225));
        DangxuatButton.setColorClick(new java.awt.Color(25, 125, 210));
        DangxuatButton.setColorOver(new java.awt.Color(25, 125, 220));
        DangxuatButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DangxuatButtonActionPerformed(evt);
            }
        });

        jPanel6.setOpaque(false);

        buttonHome.setBackground(new java.awt.Color(25, 150, 245));
        buttonHome.setForeground(new java.awt.Color(255, 255, 255));
        buttonHome.setIcon(new javax.swing.ImageIcon(getClass().getResource("/quanlythuchi/img/icons8_home_40px.png"))); // NOI18N
        buttonHome.setText("Tổng quan");
        buttonHome.setBorderColor(new java.awt.Color(25, 150, 245));
        buttonHome.setColor(new java.awt.Color(25, 150, 245));
        buttonHome.setColorClick(new java.awt.Color(25, 82, 224));
        buttonHome.setColorOver(new java.awt.Color(25, 150, 245));
        buttonHome.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        buttonHome.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                buttonHomeMouseClicked(evt);
            }
        });

        buttonGiaoDich.setBackground(new java.awt.Color(25, 150, 245));
        buttonGiaoDich.setForeground(new java.awt.Color(255, 255, 255));
        buttonGiaoDich.setIcon(new javax.swing.ImageIcon(getClass().getResource("/quanlythuchi/img/icons8_add_40px.png"))); // NOI18N
        buttonGiaoDich.setText("Giao dịch");
        buttonGiaoDich.setBorderColor(new java.awt.Color(25, 150, 245));
        buttonGiaoDich.setColor(new java.awt.Color(25, 150, 245));
        buttonGiaoDich.setColorClick(new java.awt.Color(25, 82, 224));
        buttonGiaoDich.setColorOver(new java.awt.Color(25, 150, 245));
        buttonGiaoDich.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        buttonGiaoDich.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                buttonGiaoDichMouseClicked(evt);
            }
        });

        buttonSetting.setBackground(new java.awt.Color(25, 150, 245));
        buttonSetting.setForeground(new java.awt.Color(255, 255, 255));
        buttonSetting.setIcon(new javax.swing.ImageIcon(getClass().getResource("/quanlythuchi/img/icons8_settings_40px.png"))); // NOI18N
        buttonSetting.setText("Thiết lập");
        buttonSetting.setBorderColor(new java.awt.Color(25, 150, 245));
        buttonSetting.setColor(new java.awt.Color(25, 150, 245));
        buttonSetting.setColorClick(new java.awt.Color(25, 82, 224));
        buttonSetting.setColorOver(new java.awt.Color(25, 150, 245));
        buttonSetting.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        buttonSetting.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                buttonSettingMouseClicked(evt);
            }
        });

        buttonThongBao.setBackground(new java.awt.Color(25, 150, 245));
        buttonThongBao.setForeground(new java.awt.Color(255, 255, 255));
        buttonThongBao.setIcon(new javax.swing.ImageIcon(getClass().getResource("/quanlythuchi/img/icons8_notification_40px.png"))); // NOI18N
        buttonThongBao.setText("Thông báo");
        buttonThongBao.setBorderColor(new java.awt.Color(25, 150, 245));
        buttonThongBao.setColor(new java.awt.Color(25, 150, 245));
        buttonThongBao.setColorClick(new java.awt.Color(25, 82, 224));
        buttonThongBao.setColorOver(new java.awt.Color(25, 150, 245));
        buttonThongBao.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        buttonThongBao.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                buttonThongBaoMouseClicked(evt);
            }
        });
        buttonThongBao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonThongBaoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(buttonHome, javax.swing.GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE)
            .addComponent(buttonSetting, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(buttonGiaoDich, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(buttonThongBao, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addComponent(buttonHome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonGiaoDich, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonSetting, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonThongBao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel17.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("Chào ...!");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(DangxuatButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 435, Short.MAX_VALUE)
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(DangxuatButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.LINE_START);

        Slide.setMinimumSize(new java.awt.Dimension(1150, 700));
        Slide.setLayout(new java.awt.CardLayout());

        TongQuan.setBackground(new java.awt.Color(255, 255, 255));
        TongQuan.setPreferredSize(new java.awt.Dimension(1150, 700));

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(25, 150, 245)));

        jLabel11.setForeground(new java.awt.Color(102, 102, 102));
        jLabel11.setText("Tổng số dư của tháng   >");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(25, 150, 245));

        jLabel33.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(25, 150, 245));
        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel33.setText("Thiết lập >>");
        jLabel33.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel33.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel33MouseClicked(evt);
            }
        });

        jLabel41.setIcon(new javax.swing.ImageIcon(getClass().getResource("/quanlythuchi/img/icons8_info_30px.png"))); // NOI18N
        jLabel41.setToolTipText("Tổng số dư của tháng này");
        jLabel41.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap(460, Short.MAX_VALUE)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(372, 372, 372)
                        .addComponent(jLabel33))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel41)))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel41)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel33)))
        );

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));
        jPanel10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(25, 150, 245)));

        histogramPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));

        jLabel7.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(25, 150, 245));
        jLabel7.setText("Xem chi tiết >>");
        jLabel7.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel7MouseClicked(evt);
            }
        });

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/quanlythuchi/img/icons8_refresh_25px_1.png"))); // NOI18N
        jLabel8.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel8MouseClicked(evt);
            }
        });

        jPanel12.setBackground(new java.awt.Color(255, 255, 255));

        comboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tháng này" }));
        comboBox2.setEnabled(false);
        comboBox2.setLabeText("");

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel12.setText("Tình hình thu chi");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addComponent(comboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(60, 60, 60))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(comboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(226, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                        .addComponent(histogramPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 83, Short.MAX_VALUE)
                        .addComponent(pieChart1, javax.swing.GroupLayout.PREFERRED_SIZE, 479, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel8))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addContainerGap())))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(histogramPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE)
                    .addComponent(jLabel8)
                    .addComponent(pieChart1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel7)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(31, Short.MAX_VALUE))
        );

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));
        jPanel11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(25, 150, 245)));
        jPanel11.setPreferredSize(new java.awt.Dimension(839, 249));

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel14.setText("Ghi chép gần đây");

        tableScrollPane1.setBackground(new java.awt.Color(255, 255, 255));

        tableGanDay.setForeground(new java.awt.Color(255, 255, 255));
        tableGanDay.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Hình Thức", "Số tiền", "Thời gian", "Thể Loại", "Mô tả"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableGanDay.setGridColor(new java.awt.Color(230, 230, 230));
        tableGanDay.setRowHeight(40);
        tableGanDay.setSelectionBackground(new java.awt.Color(102, 102, 255));
        tableGanDay.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableGanDayMouseClicked(evt);
            }
        });
        tableScrollPane1.setViewportView(tableGanDay);
        if (tableGanDay.getColumnModel().getColumnCount() > 0) {
            tableGanDay.getColumnModel().getColumn(0).setPreferredWidth(5);
            tableGanDay.getColumnModel().getColumn(1).setPreferredWidth(5);
        }

        jLabel15.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(25, 150, 245));
        jLabel15.setText("Xem ghi chép >>");
        jLabel15.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel15.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel15MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel15))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(tableScrollPane1)))
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tableScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout TongQuanLayout = new javax.swing.GroupLayout(TongQuan);
        TongQuan.setLayout(TongQuanLayout);
        TongQuanLayout.setHorizontalGroup(
            TongQuanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, TongQuanLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(TongQuanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel11, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 1138, Short.MAX_VALUE))
                .addContainerGap())
        );
        TongQuanLayout.setVerticalGroup(
            TongQuanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TongQuanLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, 253, Short.MAX_VALUE)
                .addContainerGap())
        );

        Slide.add(TongQuan, "card2");

        giaoDich.setBackground(new java.awt.Color(255, 255, 255));
        giaoDich.setPreferredSize(new java.awt.Dimension(1150, 700));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(25, 125, 225));
        jLabel3.setText("Thể loại");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(25, 125, 225));
        jLabel4.setText("Số tiền");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(25, 125, 225));
        jLabel5.setText("Ngày");
        jLabel5.setPreferredSize(new java.awt.Dimension(42, 20));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(25, 125, 225));
        jLabel6.setText("Mô tả");

        jButton1.setBackground(new java.awt.Color(25, 150, 245));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButton1.setForeground(new java.awt.Color(25, 125, 225));
        jButton1.setText("Lưu");
        jButton1.setContentAreaFilled(false);
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jSeparator2.setForeground(new java.awt.Color(0, 153, 255));

        comboBoxTL.setForeground(new java.awt.Color(102, 102, 102));
        comboBoxTL.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        comboBoxTL.setLabeText("");
        comboBoxTL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxTLActionPerformed(evt);
            }
        });

        txt_sotien.setForeground(new java.awt.Color(102, 102, 102));
        txt_sotien.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        txt_sotien.setHint("0 đ");

        txt_thoigian.setForeground(new java.awt.Color(102, 102, 102));
        txt_thoigian.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        txt_thoigian.setHint("Năm-Tháng-Ngày");

        txt_mota.setForeground(new java.awt.Color(102, 102, 102));
        txt_mota.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        txt_mota.setHint("");

        comboBoxThuChi.setBackground(new java.awt.Color(25, 150, 245));
        comboBoxThuChi.setForeground(new java.awt.Color(255, 255, 255));
        comboBoxThuChi.setMaximumRowCount(10);
        comboBoxThuChi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Chi tiền", "Thu tiền" }));
        comboBoxThuChi.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        comboBoxThuChi.setLabeText("");
        comboBoxThuChi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxThuChiActionPerformed(evt);
            }
        });

        comboBoxTLcon.setForeground(new java.awt.Color(102, 102, 102));
        comboBoxTLcon.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        comboBoxTLcon.setLabeText("");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(25, 125, 225));
        jLabel1.setText("Hình thức");

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/quanlythuchi/img/icons8_refresh_25px_1.png"))); // NOI18N
        jLabel2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
        });

        jLabel19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/quanlythuchi/img/icons8_refresh_25px_1.png"))); // NOI18N
        jLabel19.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel19.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel19MouseClicked(evt);
            }
        });

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(25, 150, 245));
        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/quanlythuchi/img/icons8_pencil_50px.png"))); // NOI18N
        jLabel9.setText("Thêm giao dịch");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(397, 397, 397)
                .addComponent(jLabel9)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(37, Short.MAX_VALUE)
                .addComponent(jLabel9)
                .addGap(44, 44, 44))
        );

        jLabel22.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(25, 150, 245));
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel22.setText("Lịch sử ghi chép>>");
        jLabel22.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel22.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel22MouseClicked(evt);
            }
        });

        jLabel27.setIcon(new javax.swing.ImageIcon(getClass().getResource("/quanlythuchi/img/kisspng-money-cartoon-animation-clip-art-negative-money-cliparts-5a76bb0a04f466.5513384215177305700203.png"))); // NOI18N

        jLabel28.setIcon(new javax.swing.ImageIcon(getClass().getResource("/quanlythuchi/img/kisspng-united-states-dollar-banknote-cartoon-vector-holding-dollar-5a979df45ad9f7.5011091915198858123721 (1).png"))); // NOI18N

        jLabelNoteGD.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        jLabelNoteGD.setForeground(new java.awt.Color(255, 0, 0));
        jLabelNoteGD.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout giaoDichLayout = new javax.swing.GroupLayout(giaoDich);
        giaoDich.setLayout(giaoDichLayout);
        giaoDichLayout.setHorizontalGroup(
            giaoDichLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(giaoDichLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel27)
                .addGap(73, 73, 73)
                .addGroup(giaoDichLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(giaoDichLayout.createSequentialGroup()
                        .addGroup(giaoDichLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabelNoteGD, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jSeparator2)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, giaoDichLayout.createSequentialGroup()
                                .addGroup(giaoDichLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(giaoDichLayout.createSequentialGroup()
                                        .addGroup(giaoDichLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel4)
                                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel6))
                                        .addGap(21, 21, 21))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, giaoDichLayout.createSequentialGroup()
                                        .addComponent(jLabel3)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                                .addGroup(giaoDichLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(comboBoxTL, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txt_sotien, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txt_thoigian, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txt_mota, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(comboBoxTLcon, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addComponent(jButton1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(comboBoxThuChi, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(giaoDichLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel19))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 102, Short.MAX_VALUE)
                .addGroup(giaoDichLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, giaoDichLayout.createSequentialGroup()
                        .addComponent(jLabel28)
                        .addGap(39, 39, 39))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, giaoDichLayout.createSequentialGroup()
                        .addComponent(jLabel22)
                        .addContainerGap())))
        );
        giaoDichLayout.setVerticalGroup(
            giaoDichLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(giaoDichLayout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(giaoDichLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(giaoDichLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                        .addGroup(giaoDichLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel27)
                            .addComponent(jLabel28))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel22)
                        .addGap(15, 15, 15))
                    .addGroup(giaoDichLayout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(comboBoxThuChi, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(giaoDichLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(comboBoxTL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(giaoDichLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(comboBoxTLcon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(giaoDichLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_sotien, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(giaoDichLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(giaoDichLayout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addGroup(giaoDichLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txt_thoigian, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(giaoDichLayout.createSequentialGroup()
                                .addGap(36, 36, 36)
                                .addComponent(jLabel19)))
                        .addGap(26, 26, 26)
                        .addGroup(giaoDichLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_mota, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(jLabelNoteGD, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        Slide.add(giaoDich, "card3");

        ThietLap.setBackground(new java.awt.Color(255, 255, 255));
        ThietLap.setPreferredSize(new java.awt.Dimension(1150, 700));

        button2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/quanlythuchi/img/icons8_password_100px.png"))); // NOI18N
        button2.setText("Thay đổi mật khẩu");
        button2.setBorderColor(new java.awt.Color(25, 150, 245));
        button2.setColorClick(new java.awt.Color(102, 204, 255));
        button2.setColorOver(new java.awt.Color(255, 255, 255));
        button2.setPreferredSize(new java.awt.Dimension(700, 150));
        button2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button2ActionPerformed(evt);
            }
        });

        button4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/quanlythuchi/img/icons8_money_circulation_100px.png"))); // NOI18N
        button4.setText("Hạn mức");
        button4.setBorderColor(new java.awt.Color(25, 150, 245));
        button4.setColorClick(new java.awt.Color(102, 204, 255));
        button4.setColorOver(new java.awt.Color(255, 255, 255));
        button4.setPreferredSize(new java.awt.Dimension(700, 150));
        button4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button4ActionPerformed(evt);
            }
        });

        btnXoaTK.setIcon(new javax.swing.ImageIcon(getClass().getResource("/quanlythuchi/img/icons8_delete_100px.png"))); // NOI18N
        btnXoaTK.setText("Xóa tài khoản");
        btnXoaTK.setBorderColor(new java.awt.Color(25, 150, 245));
        btnXoaTK.setColorClick(new java.awt.Color(102, 204, 255));
        btnXoaTK.setColorOver(new java.awt.Color(255, 255, 255));
        btnXoaTK.setPreferredSize(new java.awt.Dimension(700, 150));
        btnXoaTK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaTKActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout ThietLapLayout = new javax.swing.GroupLayout(ThietLap);
        ThietLap.setLayout(ThietLapLayout);
        ThietLapLayout.setHorizontalGroup(
            ThietLapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ThietLapLayout.createSequentialGroup()
                .addGap(226, 226, 226)
                .addGroup(ThietLapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnXoaTK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(button4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(button2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(224, Short.MAX_VALUE))
        );
        ThietLapLayout.setVerticalGroup(
            ThietLapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ThietLapLayout.createSequentialGroup()
                .addContainerGap(94, Short.MAX_VALUE)
                .addComponent(button2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42)
                .addComponent(button4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38)
                .addComponent(btnXoaTK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(76, 76, 76))
        );

        Slide.add(ThietLap, "card4");

        ThongBao.setBackground(new java.awt.Color(255, 255, 255));
        ThongBao.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(25, 150, 245)));
        ThongBao.setMinimumSize(new java.awt.Dimension(1150, 700));
        ThongBao.setPreferredSize(new java.awt.Dimension(1150, 700));
        ThongBao.setLayout(new java.awt.GridBagLayout());

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));
        jPanel13.setOpaque(false);

        jPanelTbao.setBackground(new java.awt.Color(255, 255, 255));

        jTableThongBao.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jTableThongBao.setForeground(new java.awt.Color(255, 255, 255));
        jTableThongBao.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Thông Báo"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableThongBao.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jTableThongBao.setRowHeight(40);
        jTableThongBao.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableThongBaoMouseClicked(evt);
            }
        });
        jScrollPaneThongBao.setViewportView(jTableThongBao);

        button6.setText("Xóa");
        button6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelTbaoLayout = new javax.swing.GroupLayout(jPanelTbao);
        jPanelTbao.setLayout(jPanelTbaoLayout);
        jPanelTbaoLayout.setHorizontalGroup(
            jPanelTbaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelTbaoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelTbaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPaneThongBao, javax.swing.GroupLayout.DEFAULT_SIZE, 1138, Short.MAX_VALUE)
                    .addGroup(jPanelTbaoLayout.createSequentialGroup()
                        .addComponent(button6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanelTbaoLayout.setVerticalGroup(
            jPanelTbaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelTbaoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPaneThongBao, javax.swing.GroupLayout.PREFERRED_SIZE, 646, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(button6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(7, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelTbao, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelTbao, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        ThongBao.add(jPanel13, gridBagConstraints);

        jLabel31.setBackground(new java.awt.Color(255, 255, 255));
        jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel31.setIcon(new javax.swing.ImageIcon(getClass().getResource("/quanlythuchi/img/thông báo.png"))); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        ThongBao.add(jLabel31, gridBagConstraints);

        Slide.add(ThongBao, "card5");

        getContentPane().add(Slide, java.awt.BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableMouseClicked
        setIndex(jTable.getSelectedRow());
    }//GEN-LAST:event_jTableMouseClicked

    private void buttonHomeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buttonHomeMouseClicked
        // TODO add your handling code here:
        soDu();
        buttonGiaoDich.setColor(new Color(25, 150, 245));
        buttonSetting.setColor(new Color(25, 150, 245));
        buttonThongBao.setColor(new Color(25, 150, 245));
        buttonHome.setColor(new Color(25, 82, 224));
        Slide.show(0);
    }//GEN-LAST:event_buttonHomeMouseClicked

    private void buttonGiaoDichMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buttonGiaoDichMouseClicked
        getListTL();
        getTheLoai();
        buttonHome.setColor(new Color(25, 150, 245));
        buttonSetting.setColor(new Color(25, 150, 245));
        buttonThongBao.setColor(new Color(25, 150, 245));
        buttonGiaoDich.setColor(new Color(25, 82, 224));
        Slide.show(3);
    }//GEN-LAST:event_buttonGiaoDichMouseClicked

    private void buttonSettingMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buttonSettingMouseClicked
        buttonGiaoDich.setColor(new Color(25, 150, 245));
        buttonHome.setColor(new Color(25, 150, 245));
        buttonThongBao.setColor(new Color(25, 150, 245));
        buttonSetting.setColor(new Color(25, 82, 224));
        Slide.show(4);
    }//GEN-LAST:event_buttonSettingMouseClicked

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        // TODO add your handling code here:
        soDu();
        getTableTime(firstTime, seconTime);
        getTableGanDay();

    }//GEN-LAST:event_formWindowOpened

    private void DangxuatButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DangxuatButtonActionPerformed
        // TODO add your handling code here:
        new LoginRegister().setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_DangxuatButtonActionPerformed
    //tìm kiếm
    private void searchtextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchtextFieldKeyReleased
        // TODO add your handling code here:
        jPanelBieuDo1.setVisible(false);
        if (search.getSelectedIndex() >= 0) {//nếu như chọn combobox
            int option = search.getSelectedIndex();
            String where = searchtextField.getText().trim();
            if (option == 0) {
                //tìm kiếm theo hình thức
                getTablewhere("HinhThuc", where);
            } else if (option == 1) {
                //tìm kiếm theo thời gian
                getTablewhere("ThoiGian", where);
            } else if (option == 2) {
                //tìm kiếm theo thể loại
                getTablewhere("TenTheLoai", where);
            }
        }
    }//GEN-LAST:event_searchtextFieldKeyReleased

    private void SuaButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SuaButtonActionPerformed
        // TODO add your handling code here:
        getListTL();
        getTheLoai_SuaGD();
        if (index >= 0) {
            comboBox1.setSelectedItem(jTable.getValueAt(index, 0));
            int id_theloai = list.get(index).getId_theloai();
            int id_theloaicha = cd.getIdCha(id_theloai);
            if (comboBox1.getSelectedIndex() == 0) { // nếu chọn hình thức chi
                if (id_theloaicha == 0) { // nếu không có thể loại cha
                    for (TheLoai theloai_chi : listChi) { // tìm theloai_chi 
                        if (theloai_chi.getIdTheLoai() == id_theloai) { // thỏa mãn id_theloai
                            txtTheLoai.setSelectedItem(theloai_chi); // thì set selected
                        }
                    }
                } else { // nếu có lớp cha
                    for (TheLoai theloai_chi_cha : listChi) { // tương tự, tìm lớp cha
                        if (theloai_chi_cha.getIdTheLoai() == id_theloaicha) { // thông qua id_theloaicha
                            txtTheLoai.setSelectedItem(theloai_chi_cha);
                        }
                    }
                    for (TheLoai theloai_chi : listChi) { // thiết lập theloai con
                        if (theloai_chi.getIdTheLoai() == id_theloai) {
                            comboBoxTLcon1.setSelectedItem(theloai_chi);
                        }
                    }
                }
            } else {
                if (id_theloaicha == 0) { // nếu không có thể loại cha
                    for (TheLoai theloai_chi : listThu) {
                        if (theloai_chi.getIdTheLoai() == id_theloai) {
                            txtTheLoai.setSelectedItem(theloai_chi);
                        }
                    }
                } else {
                    for (TheLoai theloai_chi_cha : listThu) {
                        if (theloai_chi_cha.getIdTheLoai() == id_theloaicha) {
                            txtTheLoai.setSelectedItem(theloai_chi_cha);
                        }
                    }
                    for (TheLoai theloai_chi : listThu) {
                        if (theloai_chi.getIdTheLoai() == id_theloai) {
                            comboBoxTLcon1.setSelectedItem(theloai_chi);
                        }
                    }
                }
            }
            txtSoTien.setValue(list.get(index).getSoTien());
            txtThoiGian.setText((String) jTable.getValueAt(index, 2));
            txtMoTa.setText((String) jTable.getValueAt(index, 4));
            DialogSua.setLocationRelativeTo(null);
            DialogSua.setVisible(true);
        } else {
            MessageDialog messageDialog = new MessageDialog(this);
            messageDialog.showMessage("Thông báo", "Hãy chọn một hàng để sửa");
        }

    }//GEN-LAST:event_SuaButtonActionPerformed

    private void XoaButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_XoaButtonActionPerformed
        // TODO add your handling code here:
        if (index >= 0) {
            MessageDialog messageDialog = new MessageDialog(this);
            messageDialog.showMessage("Thông báo", "Bạn có muốn xóa giao dịch này?");
            if (messageDialog.isChoose()) {
                Id_GiaoDich = list.get(index).getId_giaodich();
                int idtheloai = list.get(index).getId_theloai();
                cd.deleteGiaoDich(Id_GiaoDich);

                String hinhthuc = cd.tenHinhThuc(idtheloai);
                if (hinhthuc.equals("Chi tiền")) {
                    ghiChu();
                }

                setIndex(-1);
                list.clear();
                getTableTime(firstTime, seconTime);
                getTableGanDay();
                initBarChart();
                initChart();
            }
        } else {
            MessageDialog messageDialog = new MessageDialog(this);
            messageDialog.showMessage("Lỗi", "Hãy chọn một hàng để xóa");
        }


    }//GEN-LAST:event_XoaButtonActionPerformed

    private void LamMoiButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LamMoiButtonActionPerformed
        // TODO add your handling code here:
        search.setSelectedIndex(-1);
        searchtextField.setText("");
        list.clear();
        thangNay();
        ChonThangCBBox.setSelectedIndex(0);
        getTableTime(firstTime, seconTime);
        index = -1;
    }//GEN-LAST:event_LamMoiButtonActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        int idTheLoai_Add;
        if (comboBoxTLcon.getSelectedIndex() == -1) {//Nếu không chọn Thể loại con
            int selectedIndex = comboBoxTL.getSelectedIndex();
            idTheLoai_Add = comboBoxTL.getItemAt(selectedIndex).getIdTheLoai(); // Thì IDTheLoai là của thể loại cha
        } else { //Nếu chọn Thể loại con
            int selectedIndex = comboBoxTLcon.getSelectedIndex();
            idTheLoai_Add = comboBoxTLcon.getItemAt(selectedIndex).getIdTheLoai();// Thì IDTheLoai là của thể loại con
        }
        try {
            cd.insertGiaoDich(txt_thoigian.getText(), Long.valueOf(txt_sotien.getText()), txt_mota.getText(), tk.getIdTaiKhoan(), idTheLoai_Add);
            MessageDialog messageDialog = new MessageDialog(this);
            messageDialog.showMessage("Thông báo", "Lưu thành công");
        } catch (Exception e) {
            MessageDialog messageDialog = new MessageDialog(this);
            messageDialog.showMessage("Lỗi", "Vui lòng nhập đúng định dạng");
        }

        String hinhthuc = cd.tenHinhThuc(idTheLoai_Add);
        if (hinhthuc.equals("Chi tiền")) {
            checkHanMuc(idTheLoai_Add);
            ghiChu();
        }

        list.clear();
        thangNay();
        getTableTime(firstTime, seconTime);
        getTableGanDay();
        initBarChart();
        initChart();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void tableGanDayMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableGanDayMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tableGanDayMouseClicked

    private void jLabel15MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel15MouseClicked
        // TODO add your handling code here:
        jPanelBieuDo1.setVisible(false);
        indexSlideBefore = 0;
        Slide.show(1);
    }//GEN-LAST:event_jLabel15MouseClicked

    private void jLabel16MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel16MouseClicked
        // TODO add your handling code here:
        soDu();
        Slide.show(indexSlideBefore);
    }//GEN-LAST:event_jLabel16MouseClicked

    private void comboBoxThuChiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxThuChiActionPerformed
        // TODO add your handling code here:
        getTheLoai();
    }//GEN-LAST:event_comboBoxThuChiActionPerformed

    private void comboBoxTLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxTLActionPerformed
        // TODO add your handling code here:
        getTheLoaiCon();
    }//GEN-LAST:event_comboBoxTLActionPerformed

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        // TODO add your handling code here:
        comboBoxTLcon.setSelectedIndex(-1);
    }//GEN-LAST:event_jLabel2MouseClicked

    private void jLabel8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel8MouseClicked
        // TODO add your handling code here:
        thangNay();
        initBarChart();
        initChart();
    }//GEN-LAST:event_jLabel8MouseClicked

    private void jLabel7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel7MouseClicked
        // TODO add your handling code here:
        jPanelChooseDate1.setVisible(false);
        initBarChart1();
        initChartChi();
        setScroolBarBieuDo();
        tableBieuDo();
        setTableBieuDo();
        Slide.show(2);
    }//GEN-LAST:event_jLabel7MouseClicked

    private void jLabel10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel10MouseClicked
        // TODO add your handling code here:
        Slide.show(0);

    }//GEN-LAST:event_jLabel10MouseClicked

    private void jLabel19MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel19MouseClicked
        // TODO add your handling code here:
        dateChooser.toDay();
    }//GEN-LAST:event_jLabel19MouseClicked

    private void ChonThangCBBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChonThangCBBoxActionPerformed
        // TODO add your handling code here:
        if (ChonThangCBBox.getSelectedIndex() == 0) {
            list.clear();
            jPanelBieuDo1.setVisible(false);
            thangNay();
            getTableTime(firstTime, seconTime);
        } else if (ChonThangCBBox.getSelectedIndex() == 1) {
            list.clear();
            jPanelBieuDo1.setVisible(false);
            thangTruoc();
            getTableTime(firstTime, seconTime);
        } else if (ChonThangCBBox.getSelectedIndex() == 2) {
            jPanelBieuDo1.setVisible(true);
        }
    }//GEN-LAST:event_ChonThangCBBoxActionPerformed

    private void comboBoxBieuDoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxBieuDoActionPerformed
        // TODO add your handling code here:
        if (comboBoxBieuDo.getSelectedIndex() == 2) {
            jPanelChooseDate1.setVisible(true);
        } else if (comboBoxBieuDo.getSelectedIndex() == 0) {
            thangNay();
            initBarChart1();
            initChartChi();
            tableBieuDo();
            jPanelChooseDate1.setVisible(false);
        } else if (comboBoxBieuDo.getSelectedIndex() == 1) {
            thangTruoc();
            initBarChart1();
            initChartChi();
            tableBieuDo();
            jPanelChooseDate1.setVisible(false);
        }
        comboBox3.setSelectedIndex(0);
    }//GEN-LAST:event_comboBoxBieuDoActionPerformed

    private void btnChon1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChon1ActionPerformed
        // TODO add your handling code here:
        ThangCustom();
        list.clear();
        getTableTime(firstTime, seconTime);
    }//GEN-LAST:event_btnChon1ActionPerformed

    private void button1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button1ActionPerformed
        // TODO add your handling code here:
        getListTL();
        getTheLoai();
        Slide.show(3);
    }//GEN-LAST:event_button1ActionPerformed

    private void jLabel22MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel22MouseClicked
        // TODO add your handling code here:
        indexSlideBefore = 3;
        Slide.show(1);
    }//GEN-LAST:event_jLabel22MouseClicked

    private void btnChonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChonActionPerformed
        // TODO add your handling code here:
        comboBox3.setSelectedIndex(0);
        firstTime = textTuNgay.getText();
        seconTime = textDenNgay.getText();
        initBarChart1();
        initChartChi();
        tableBieuDo();
    }//GEN-LAST:event_btnChonActionPerformed

    private void button5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button5ActionPerformed
        // TODO add your handling code here:
        doiMatKhau();
    }//GEN-LAST:event_button5ActionPerformed
    private void doiMatKhau() {
        noteMK.setText("");
        
        String passOld = MKcu.getText();
        String passNew = MKmoi.getText();
        String passConfirm = MKconfirm.getText();
        
        if (uc.checkInput(passOld, passNew, passConfirm)) {
            MessageDialog messageDialog = new MessageDialog(this);
            messageDialog.showMessage("Lỗi", "Hãy điền đầy đủ thông tin");
        } else if (uc.checkPass(passNew, passConfirm)) {
            MessageDialog messageDialog = new MessageDialog(this);
            messageDialog.showMessage("Lỗi", "Mật khẩu mới không giống nhau");
        } else if (!passOld.equals(tk.getMatKhau())) {
            MessageDialog messageDialog = new MessageDialog(this);
            messageDialog.showMessage("Lỗi", "Mật khẩu không đúng");
        } else {
            MessageDialog messageDialog = new MessageDialog(this);
            messageDialog.showMessage("Thông báo", "Bạn có muốn đổi mật khẩu");
            if (messageDialog.isChoose()) {
                if (uc.changPass(passNew, tk.getIdTaiKhoan())) {
                    tk.setMatKhau(MKmoi.getText());
                    MKcu.setText("");
                    MKmoi.setText("");
                    MKconfirm.setText("");
                    messageDialog.showMessage("Chúc mừng", "Đổi mật khẩu thành công!");
                    cd.insertThongBao(nowTime + ", Tài khoản đã được thay đổi mật khẩu!", tk.getIdTaiKhoan());
                } else {
                    messageDialog.showMessage("Lỗi", "Có lỗi xảy ra!");
                }

            }
        }
    }
    private void jLabel18MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel18MouseClicked
        // TODO add your handling code here:
        ChangePassDialog.setVisible(false);
    }//GEN-LAST:event_jLabel18MouseClicked

    private void button2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button2ActionPerformed
        // TODO add your handling code here:
        ChangePassDialog.setVisible(true);
        ChangePassDialog.setLocationRelativeTo(null);
    }//GEN-LAST:event_button2ActionPerformed

    private void buttonThongBaoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buttonThongBaoMouseClicked
        // TODO add your handling code here:
        buttonGiaoDich.setColor(new Color(25, 150, 245));
        buttonHome.setColor(new Color(25, 150, 245));
        buttonSetting.setColor(new Color(25, 150, 245));
        buttonThongBao.setColor(new Color(25, 82, 224));
        setScroolBarThongBao();
        tableThongBao();
        setTableThongBao();
        Slide.show(5);
    }//GEN-LAST:event_buttonThongBaoMouseClicked

    private void buttonThongBaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonThongBaoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_buttonThongBaoActionPerformed

    private void jLabel18MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel18MouseEntered
        // TODO add your handling code here:
        jLabel18.setBackground(Color.RED);
    }//GEN-LAST:event_jLabel18MouseEntered

    private void jLabel18MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel18MouseExited
        // TODO add your handling code here:
        jLabel18.setBackground(Color.WHITE);
    }//GEN-LAST:event_jLabel18MouseExited

    private void jLabel32MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel32MouseClicked
        // TODO add your handling code here:
        DialogSua.setVisible(false);
    }//GEN-LAST:event_jLabel32MouseClicked

    private void jLabel32MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel32MouseEntered
        // TODO add your handling code here:
        jLabel32.setBackground(Color.RED);
    }//GEN-LAST:event_jLabel32MouseEntered

    private void jLabel32MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel32MouseExited
        // TODO add your handling code here:
        jLabel32.setBackground(Color.WHITE);
    }//GEN-LAST:event_jLabel32MouseExited

    private void button6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button6ActionPerformed
        // TODO add your handling code here:
        if (indexThongBao >= 0) {
            ID_ThongBao = listThongBao.get(indexThongBao).getID_ThongBao();
            cd.deleteThongBao(ID_ThongBao);
        }
        indexThongBao = -1;
        listThongBao.clear();
        tableThongBao();
    }//GEN-LAST:event_button6ActionPerformed

    private void jTableThongBaoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableThongBaoMouseClicked
        // TODO add your handling code here:
        indexThongBao = jTableThongBao.getSelectedRow();
    }//GEN-LAST:event_jTableThongBaoMouseClicked

    private void comboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBox1ActionPerformed
        // TODO add your handling code here:
        getTheLoai_SuaGD();
    }//GEN-LAST:event_comboBox1ActionPerformed

    private void txtTheLoaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTheLoaiActionPerformed
        // TODO add your handling code here:
        getTheLoaiCon_SuaGD();
    }//GEN-LAST:event_txtTheLoaiActionPerformed

    private void ButtonOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonOkActionPerformed
        // TODO add your handling code here:
        Id_GiaoDich = list.get(index).getId_giaodich();
        int idTheLoai_edit;
        if (comboBoxTLcon1.getSelectedIndex() == -1) {//Nếu không chọn Thể loại con
            int selectedIndex = txtTheLoai.getSelectedIndex();
            idTheLoai_edit = txtTheLoai.getItemAt(selectedIndex).getIdTheLoai(); // Thì IDTheLoai là của thể loại cha
        } else { //Nếu chọn Thể loại con
            int selectedIndex = comboBoxTLcon1.getSelectedIndex();
            idTheLoai_edit = comboBoxTLcon1.getItemAt(selectedIndex).getIdTheLoai();// Thì IDTheLoai là của thể loại con
        }
        try {
            cd.upDateGiaoDich(txtThoiGian.getText(), (long) txtSoTien.getValue(), txtMoTa.getText(), tk.getIdTaiKhoan(), idTheLoai_edit, Id_GiaoDich);
            DialogSua.dispose();
            MessageDialog messageDialog = new MessageDialog(this);
            messageDialog.showMessage("Thông báo", "Lưu thành công");
        } catch (Exception e) {
            MessageDialog messageDialog = new MessageDialog(this);
            messageDialog.showMessage("Lỗi", "Vui lòng nhập đúng định dạng");
        }

        String hinhthuc = cd.tenHinhThuc(idTheLoai_edit);
        if (hinhthuc.equals("Chi tiền")) {
            checkHanMuc(idTheLoai_edit);
            ghiChu();
        }
        list.clear();
        thangNay();
        getTableTime(firstTime, seconTime);
        getTableGanDay();
        initBarChart();
        initChart();
    }//GEN-LAST:event_ButtonOkActionPerformed

    private void jLabel39MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel39MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel39MouseClicked

    private void jLabel40MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel40MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel40MouseClicked

    private void button4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button4ActionPerformed
        // TODO add your handling code here:
        setScrollBarHanMuc();
        setTableHanMuc();
        getTableHanMuc();
        DialogHanMuc.setLocationRelativeTo(null);
        DialogHanMuc.setVisible(true);
    }//GEN-LAST:event_button4ActionPerformed

    private void button3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button3ActionPerformed
        // TODO add your handling code here:
        DialogHanMuc.setVisible(false);
    }//GEN-LAST:event_button3ActionPerformed

    private void button8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button8ActionPerformed
        // TODO add your handling code here:
        listChi = cd.getTheLoaiChi();
        comboBox4.removeAllItems();
        for (TheLoai theLoai : listChi) {
            if (theLoai.getIdCha() == 0) {
                comboBox4.addItem(theLoai);
            }
        }
        comboBox4.setSelectedIndex(0);
        panelSlideHanMuc.show(1);
    }//GEN-LAST:event_button8ActionPerformed

    private void button33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button33ActionPerformed
        // TODO add your handling code here:
        panelSlideHanMuc.show(0);
    }//GEN-LAST:event_button33ActionPerformed

    private void button9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button9ActionPerformed
        // TODO add your handling code here:
        int idTheLoai;
        int selectedIndex = comboBox4.getSelectedIndex();
        idTheLoai = comboBox4.getItemAt(selectedIndex).getIdTheLoai();
        int idtaikhoan = tk.getIdTaiKhoan();

        String tenHanMuc = textField1.getText();

        if ((jFormattedTextField1.getValue() == null) || ((long) jFormattedTextField1.getValue() <= 0) || (tenHanMuc.equals(""))) {
            MessageDialog messageDialog = new MessageDialog(this);
            messageDialog.showMessage("Lỗi", "Hãy điền chính xác thông tin");
            return;
        }
        long gioiHan = (long) jFormattedTextField1.getValue();
        ArrayList<HanMuc> listHanMuc = cd.getHanMuc(tk.getIdTaiKhoan());
        for (HanMuc hanmuc : listHanMuc) {
            if (hanmuc.getId_TheLoai() == idTheLoai) {
                MessageDialog messageDialog = new MessageDialog(this);
                messageDialog.showMessage("Lỗi", "Hạn mức của thể loại này đã tồn tại");
                return;
            }
        }
        cd.insertHanMuc(idtaikhoan, idTheLoai, gioiHan, tenHanMuc);
        MessageDialog messageDialog = new MessageDialog(this);
        messageDialog.showMessage("Chúc mừng", "Thêm hạn mức thành công");
        ghiChu();
        listHanMuc.clear();
        getTableHanMuc();
        panelSlideHanMuc.show(0);


    }//GEN-LAST:event_button9ActionPerformed

    private void jLabel33MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel33MouseClicked
        // TODO add your handling code here:
        soDu();
        DialogSoDu.setLocationRelativeTo(null);
        DialogSoDu.setVisible(true);
    }//GEN-LAST:event_jLabel33MouseClicked

    private void button13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button13ActionPerformed
        // TODO add your handling code here:
        DialogSoDu.setVisible(false);
    }//GEN-LAST:event_button13ActionPerformed

    private void button14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button14ActionPerformed
        // TODO add your handling code here:
        long taiKhoanGoc = (long) jFormattedTextField3.getValue();
        hieu = taiKhoanGoc + thu - chi;
        labelHieu2.setText(formatNBer(hieu) + " đ");
    }//GEN-LAST:event_button14ActionPerformed

    private void button15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button15ActionPerformed
        // TODO add your handling code here:
        taiKhoanGoc = (long) jFormattedTextField3.getValue();
        hieu = taiKhoanGoc + thu - chi;
        labelHieu2.setText(formatNBer(hieu) + " đ");
        cd.upDateTKGoc(tk.getIdTaiKhoan(), taiKhoanGoc);
        jLabel13.setText(labelHieu2.getText());
        DialogSoDu.setVisible(false);
    }//GEN-LAST:event_button15ActionPerformed

    private void comboBox3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboBox3ItemStateChanged
        // TODO add your handling code here:
        if (comboBox3.getSelectedIndex() == 0) {
            initChartChi();
            tableBieuDo();
        }
        if (comboBox3.getSelectedIndex() == 1) {
            initChartThu();
            tableBieuDo();
        }
    }//GEN-LAST:event_comboBox3ItemStateChanged

    private void jTableHanMucMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableHanMucMouseClicked
        // TODO add your handling code here:
        indexRowHanMuc = jTableHanMuc.getSelectedRow();
    }//GEN-LAST:event_jTableHanMucMouseClicked

    private void btnXoaHMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaHMActionPerformed
        // TODO add your handling code here:
        if (indexRowHanMuc < 0) {
            MessageDialog messageDialog = new MessageDialog(this);
            messageDialog.showMessage("Thông báo", "Hãy chọn hạn mức muốn xóa");
        } else {
            MessageDialog messageDialog = new MessageDialog(this);
            messageDialog.showMessage("Xác nhận", "Bạn muốn xóa hạn mức này?");
            if (messageDialog.isChoose()) {
                int idTheLoai = listHanMuc.get(indexRowHanMuc).getId_TheLoai();
                cd.deleteHanMuc(tk.getIdTaiKhoan(), idTheLoai);
                listHanMuc.clear();
                getTableHanMuc();
                indexRowHanMuc = -1;
            }

        }
    }//GEN-LAST:event_btnXoaHMActionPerformed

    private void btnSuaHMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaHMActionPerformed
        // TODO add your handling code here:
        if (indexRowHanMuc >= 0) {
            int idTheLoai = listHanMuc.get(indexRowHanMuc).getId_TheLoai();
            suaTienHM.setValue(listHanMuc.get(indexRowHanMuc).getGioiHan());
            suaTenHM.setText(listHanMuc.get(indexRowHanMuc).getTenHanMuc());
            listChi = cd.getTheLoaiChi();
            suaComboboxHM.removeAllItems();
            for (TheLoai theLoai : listChi) {
                if (theLoai.getIdCha() == 0) {
                    suaComboboxHM.addItem(theLoai);
                }
            }
            for (TheLoai theLoai : listChi) {
                if (theLoai.getIdTheLoai() == idTheLoai) {
                    suaComboboxHM.setSelectedItem(theLoai);
                    break;
                }
            }

            panelSlideHanMuc.show(2);
        } else {
            MessageDialog messageDialog = new MessageDialog(this);
            messageDialog.showMessage("Lỗi", "Hãy chọn hạn mức muốn sửa");
        }

//        list.clear();
//        thangNay();
//        getTableTime(firstTime, seconTime);
//        getTableGanDay();
//        initBarChart();
//        initChart();
    }//GEN-LAST:event_btnSuaHMActionPerformed

    private void button34ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button34ActionPerformed
        // TODO add your handling code here:
        panelSlideHanMuc.show(0);
    }//GEN-LAST:event_button34ActionPerformed

    private void suaHMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_suaHMActionPerformed
        // TODO add your handling code here:
        int idTheLoai;
        int selectedIndex = suaComboboxHM.getSelectedIndex();
        idTheLoai = suaComboboxHM.getItemAt(selectedIndex).getIdTheLoai();
        suaComboboxHM.setEnabled(false);
        int idtaikhoan = tk.getIdTaiKhoan();

        String tenHanMuc = suaTenHM.getText();

        if ((suaTienHM.getValue() == null) || ((long) suaTienHM.getValue() <= 0) || (tenHanMuc.equals(""))) {
            MessageDialog messageDialog = new MessageDialog(this);
            messageDialog.showMessage("Lỗi", "Hãy điền chính xác thông tin");
            return;
        }
        long gioiHan = (long) suaTienHM.getValue();
        ArrayList<HanMuc> listHanMuc = cd.getHanMuc(tk.getIdTaiKhoan());

        cd.upDateHanMuc(idtaikhoan, idTheLoai, gioiHan, tenHanMuc);
        MessageDialog messageDialog = new MessageDialog(this);
        messageDialog.showMessage("Chúc mừng", "Sửa hạn mức thành công");
        ghiChu();
        listHanMuc.clear();
        getTableHanMuc();
        indexRowHanMuc = -1;
        panelSlideHanMuc.show(0);
    }//GEN-LAST:event_suaHMActionPerformed

    private void btnXoaTKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaTKActionPerformed
        // TODO add your handling code here:
        xoaTK();
    }//GEN-LAST:event_btnXoaTKActionPerformed
    private void xoaTK(){
        MessageDialog messageDialog = new MessageDialog(this);
        messageDialog.showMessage("Chú ý", "Nếu bạn xóa tài khoản này, tất cả các ghi chép liên quân sẽ bị xóa. Dữ liệu bị xóa không thể khôi phục lại được. Bạn có thực sự muốn xóa không không?");
        if (messageDialog.isChoose()) {
            uc.deleteTK(tk.getIdTaiKhoan());
            messageDialog.showMessage("Thông báo", "Xóa tài khoản thành công");
            new LoginRegister().setVisible(true);
            setVisible(false);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ButtonOk;
    private javax.swing.JDialog ChangePassDialog;
    private components.ComboBox ChonThangCBBox;
    private components.Button DangxuatButton;
    private javax.swing.JDialog DialogHanMuc;
    private javax.swing.JDialog DialogSoDu;
    private javax.swing.JDialog DialogSua;
    private components.Button LamMoiButton;
    private javax.swing.JPasswordField MKconfirm;
    private javax.swing.JPasswordField MKcu;
    private javax.swing.JPasswordField MKmoi;
    private javax.swing.JPanel PanelBieuDo;
    private javax.swing.JPanel PanelThongSo;
    private javax.swing.JPanel PanelXemChiTiet;
    private components.PanelSlide Slide;
    private components.Button SuaButton;
    private javax.swing.JPanel ThietLap;
    private javax.swing.JPanel ThongBao;
    private javax.swing.JPanel TongQuan;
    private components.Button XoaButton;
    private components.Button btnChon;
    private components.Button btnChon1;
    private components.Button btnSuaHM;
    private components.Button btnXoaHM;
    private components.Button btnXoaTK;
    private components.Button button1;
    private components.Button button13;
    private components.Button button14;
    private components.Button button15;
    private components.Button button2;
    private components.Button button3;
    private components.Button button33;
    private components.Button button34;
    private components.Button button4;
    private components.Button button5;
    private components.Button button6;
    private components.Button button8;
    private components.Button button9;
    private components.Button buttonGiaoDich;
    private components.Button buttonHome;
    private components.Button buttonSetting;
    private components.Button buttonThongBao;
    private components.ComboBox comboBox1;
    private components.ComboBox comboBox2;
    private components.ComboBox comboBox3;
    private components.ComboBox<TheLoai> comboBox4;
    private components.ComboBox comboBox5;
    private components.ComboBox comboBox7;
    private components.ComboBox comboBoxBieuDo;
    private components.ComboBox<TheLoai> comboBoxTL;
    private components.ComboBox<TheLoai> comboBoxTLcon;
    private components.ComboBox<TheLoai> comboBoxTLcon1;
    private components.ComboBox comboBoxThuChi;
    private com.raven.datechooser.DateChooser dateChooser;
    private com.raven.datechooser.DateChooser dateChooser1;
    private com.raven.datechooser.DateChooser dateChooser2;
    private com.raven.datechooser.DateChooser dateChooser3;
    private com.raven.datechooser.DateChooser dateChooser4;
    private com.raven.datechooser.DateChooser dateChooser5;
    private javax.swing.JPanel giaoDich;
    private components.HistogramPanel histogramPanel;
    private components.HistogramPanel histogramPanel1;
    private javax.swing.JButton jButton1;
    private javax.swing.JFormattedTextField jFormattedTextField1;
    private javax.swing.JFormattedTextField jFormattedTextField3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelNoteGD;
    private javax.swing.JLabel jLabelNoteSuaGD;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel jPanelBieuDo1;
    private javax.swing.JPanel jPanelChooseDate1;
    private javax.swing.JPanel jPanelHan_Muc_1;
    private javax.swing.JPanel jPanelHan_Muc_2;
    private javax.swing.JPanel jPanelHan_Muc_3;
    private javax.swing.JPanel jPanelTbao;
    private javax.swing.JScrollPane jScrollPaneBieuDo;
    private javax.swing.JScrollPane jScrollPaneHanMuc;
    private javax.swing.JScrollPane jScrollPaneThongBao;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JTable jTable;
    private javax.swing.JTable jTableBieuDo;
    private javax.swing.JTable jTableHanMuc;
    private javax.swing.JTable jTableThongBao;
    private javax.swing.JLabel labelChi;
    private javax.swing.JLabel labelChi2;
    private javax.swing.JLabel labelHieu;
    private javax.swing.JLabel labelHieu2;
    private javax.swing.JLabel labelThu;
    private javax.swing.JLabel labelThu2;
    private javax.swing.JLabel noteMK;
    private components.PanelSlide panelSlideHanMuc;
    private components.PieChart pieChart1;
    private components.PieChart pieChart2;
    private components.ComboBox search;
    private components.TextField searchtextField;
    private components.ComboBox<TheLoai> suaComboboxHM;
    private components.Button suaHM;
    private components.TextField suaTenHM;
    private javax.swing.JFormattedTextField suaTienHM;
    private javax.swing.JTable tableGanDay;
    private javax.swing.JScrollPane tableScrollPane;
    private javax.swing.JScrollPane tableScrollPane1;
    private components.TextField textDenNgay;
    private components.TextField textDenNgay1;
    private components.TextField textField1;
    private components.TextField textTuNgay;
    private components.TextField textTuNgay1;
    private components.TextField txtMoTa;
    private javax.swing.JFormattedTextField txtSoTien;
    private components.ComboBox<TheLoai> txtTheLoai;
    private components.TextField txtThoiGian;
    private components.TextField txt_mota;
    private components.TextField txt_sotien;
    private components.TextField txt_thoigian;
    // End of variables declaration//GEN-END:variables
}
