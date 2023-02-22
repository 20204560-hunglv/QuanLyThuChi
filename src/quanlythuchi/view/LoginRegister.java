package quanlythuchi.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import javax.swing.Timer;
import quanlythuchi.services.JdbcConnection;
import quanlythuchi.model.TaiKhoan;
import quanlythuchi.services.ConnDatabase;

public class LoginRegister extends javax.swing.JFrame {

    private JdbcConnection jdbcConnection = new JdbcConnection();
    private ConnDatabase cd = new ConnDatabase();
    private TaiKhoan tk = new TaiKhoan();
    private String nowTime;
    public LoginRegister() {
        initComponents();
        setIconImage(new ImageIcon(LoginRegister.class.getResource("/quanlythuchi/img/icons8_Stack_of_Money_127px.png")).getImage());
        panelSlide.init(DangNhapPanel, DangKyPanel);
        panelSlide.setAnimate(15);
        keyListenner(txtpassword);
        Time();
    }

    // xu lý sự kiện nhấn enter
    private void keyListenner(JTextField jtf) {
        jtf.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                //nếu keycode == 10 ~ enter
                if (e.getKeyCode() == 10) {
                    login();
                }
            }
        });
    }
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
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        DangKyPanel = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jPasswordField2 = new javax.swing.JPasswordField();
        button2 = new quanlythuchi.components.Button();
        ButtonDangNhapSlide = new javax.swing.JButton();
        jPasswordField3 = new javax.swing.JPasswordField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator4 = new javax.swing.JSeparator();
        jSeparator5 = new javax.swing.JSeparator();
        labelNote1 = new javax.swing.JLabel();
        panelSlide = new quanlythuchi.components.PanelSlide();
        DangNhapPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtusername = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtpassword = new javax.swing.JPasswordField();
        buttonDangNhap = new quanlythuchi.components.Button();
        ButtonDangkySlide = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        labelNote = new javax.swing.JLabel();

        DangKyPanel.setBackground(new java.awt.Color(255, 255, 255));
        DangKyPanel.setAlignmentX(0.0F);
        DangKyPanel.setAlignmentY(0.0F);

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(25, 125, 225));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Đăng ký");

        jTextField2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField2.setForeground(new java.awt.Color(25, 125, 225));
        jTextField2.setBorder(null);

        jPasswordField2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jPasswordField2.setForeground(new java.awt.Color(25, 125, 225));
        jPasswordField2.setBorder(null);

        button2.setText("Đăng ký");
        button2.setBorderColor(new java.awt.Color(25, 125, 225));
        button2.setColorClick(new java.awt.Color(25, 125, 225));
        button2.setColorOver(new java.awt.Color(19, 103, 188));
        button2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button2ActionPerformed(evt);
            }
        });

        ButtonDangNhapSlide.setForeground(new java.awt.Color(25, 125, 225));
        ButtonDangNhapSlide.setText("Đăng nhập");
        ButtonDangNhapSlide.setContentAreaFilled(false);
        ButtonDangNhapSlide.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ButtonDangNhapSlide.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonDangNhapSlideActionPerformed(evt);
            }
        });

        jPasswordField3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jPasswordField3.setForeground(new java.awt.Color(25, 125, 225));
        jPasswordField3.setBorder(null);

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(25, 125, 225));
        jLabel5.setText("Tên đăng nhập");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(25, 125, 225));
        jLabel6.setText("Mật khẩu");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(25, 125, 225));
        jLabel7.setText("Xác nhận mật khẩu");

        labelNote1.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        labelNote1.setForeground(new java.awt.Color(255, 0, 0));
        labelNote1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout DangKyPanelLayout = new javax.swing.GroupLayout(DangKyPanel);
        DangKyPanel.setLayout(DangKyPanelLayout);
        DangKyPanelLayout.setHorizontalGroup(
            DangKyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DangKyPanelLayout.createSequentialGroup()
                .addContainerGap(100, Short.MAX_VALUE)
                .addGroup(DangKyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(button2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPasswordField2, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPasswordField3, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField2, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ButtonDangNhapSlide, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator3)
                    .addComponent(jSeparator4)
                    .addComponent(jSeparator5)
                    .addComponent(labelNote1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(100, Short.MAX_VALUE))
        );
        DangKyPanelLayout.setVerticalGroup(
            DangKyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DangKyPanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel4)
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(jLabel5)
                .addGap(0, 0, 0)
                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 5, Short.MAX_VALUE)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(jLabel6)
                .addGap(0, 0, 0)
                .addComponent(jPasswordField2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(jLabel7)
                .addGap(0, 0, 0)
                .addComponent(jPasswordField3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 43, Short.MAX_VALUE)
                .addComponent(button2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelNote1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(ButtonDangNhapSlide)
                .addGap(25, 25, 25))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("QuanLyThuChi");
        setResizable(false);

        panelSlide.setBackground(new java.awt.Color(255, 204, 255));

        DangNhapPanel.setBackground(new java.awt.Color(255, 255, 255));
        DangNhapPanel.setAlignmentX(0.0F);
        DangNhapPanel.setAlignmentY(400.0F);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(25, 125, 225));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Đăng nhập");

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/quanlythuchi/img/icons8_username_40px.png"))); // NOI18N

        txtusername.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtusername.setForeground(new java.awt.Color(25, 125, 225));
        txtusername.setBorder(null);

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/quanlythuchi/img/icons8_password_40px.png"))); // NOI18N

        txtpassword.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtpassword.setForeground(new java.awt.Color(25, 125, 225));
        txtpassword.setBorder(null);

        buttonDangNhap.setText("Đăng nhập");
        buttonDangNhap.setBorderColor(new java.awt.Color(25, 125, 225));
        buttonDangNhap.setColorClick(new java.awt.Color(25, 125, 225));
        buttonDangNhap.setColorOver(new java.awt.Color(19, 103, 188));
        buttonDangNhap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonDangNhapActionPerformed(evt);
            }
        });

        ButtonDangkySlide.setForeground(new java.awt.Color(25, 125, 225));
        ButtonDangkySlide.setText("Đăng ký");
        ButtonDangkySlide.setContentAreaFilled(false);
        ButtonDangkySlide.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ButtonDangkySlide.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonDangkySlideActionPerformed(evt);
            }
        });

        labelNote.setBackground(new java.awt.Color(255, 255, 255));
        labelNote.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        labelNote.setForeground(new java.awt.Color(255, 0, 0));
        labelNote.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout DangNhapPanelLayout = new javax.swing.GroupLayout(DangNhapPanel);
        DangNhapPanel.setLayout(DangNhapPanelLayout);
        DangNhapPanelLayout.setHorizontalGroup(
            DangNhapPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DangNhapPanelLayout.createSequentialGroup()
                .addContainerGap(100, Short.MAX_VALUE)
                .addGroup(DangNhapPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(DangNhapPanelLayout.createSequentialGroup()
                        .addGroup(DangNhapPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2))
                        .addGroup(DangNhapPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtusername, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                            .addComponent(txtpassword, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                            .addComponent(jSeparator1)
                            .addComponent(jSeparator2, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)))
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                    .addComponent(buttonDangNhap, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ButtonDangkySlide, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelNote, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE))
                .addContainerGap(100, Short.MAX_VALUE))
        );
        DangNhapPanelLayout.setVerticalGroup(
            DangNhapPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, DangNhapPanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 62, Short.MAX_VALUE)
                .addGroup(DangNhapPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtusername, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(0, 0, 0)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 22, Short.MAX_VALUE)
                .addGroup(DangNhapPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(txtpassword, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 119, Short.MAX_VALUE)
                .addComponent(buttonDangNhap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelNote, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(ButtonDangkySlide)
                .addContainerGap(28, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelSlideLayout = new javax.swing.GroupLayout(panelSlide);
        panelSlide.setLayout(panelSlideLayout);
        panelSlideLayout.setHorizontalGroup(
            panelSlideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
            .addGroup(panelSlideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(DangNhapPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelSlideLayout.setVerticalGroup(
            panelSlideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 500, Short.MAX_VALUE)
            .addGroup(panelSlideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(DangNhapPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelSlide, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelSlide, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void ButtonDangkySlideActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonDangkySlideActionPerformed
        // TODO add your handling code here:
        panelSlide.show(1);
    }//GEN-LAST:event_ButtonDangkySlideActionPerformed

    private void ButtonDangNhapSlideActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonDangNhapSlideActionPerformed
        // TODO add your handling code here:
        panelSlide.show(0);
    }//GEN-LAST:event_ButtonDangNhapSlideActionPerformed

    private void buttonDangNhapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonDangNhapActionPerformed
        // TODO add your handling code here:
        login();
    }//GEN-LAST:event_buttonDangNhapActionPerformed

    private void button2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button2ActionPerformed
        // TODO add your handling code here:
        register();
    }//GEN-LAST:event_button2ActionPerformed

    private void register() {
        if (jTextField2.getText().equals("") || jPasswordField2.getText().equals("") || jPasswordField3.getText().equals("")) {
            labelNote1.setText("Hãy nhập đầy đủ thông tin");
        } else if (!jPasswordField2.getText().equals(jPasswordField3.getText())) {
            labelNote1.setText("Mật khẩu không giống nhau");
        } else if (cd.checkTaiKhoan(jTextField2.getText())) {
            labelNote1.setText("Tài khoản đã tồn tại");
        } else {
            labelNote1.setText("");
            if (cd.register(jTextField2.getText(), jPasswordField2.getText())) {
                String username=jTextField2.getText();
                String pass=jPasswordField2.getText();        
                labelNote1.setText("Đăng ký thành công. Đăng nhập !");
                tk = cd.getTaiKhoan(username, pass);
                cd.insertThongBao(nowTime+", Chúc mừng bạn đã đăng ký tài khoản thành công ^_^", tk.getIdTaiKhoan());
            }
            else labelNote1.setText("Đăng ký thất bại");
        }
    }

    private void login() {
        if (txtusername.getText().equals("") || txtpassword.getText().equals("")) {
            labelNote.setText("Hãy nhập đầy đủ thông tin");
        } else {
            labelNote.setText("");
            if (cd.signIn(txtusername.getText(), txtpassword.getText())) {
                tk = cd.getTaiKhoan(txtusername.getText(), txtpassword.getText());
                new Home(tk).setVisible(true);
                setVisible(false);
            } else {
                labelNote.setText("Tài khoản hoặc mật khẩu không đúng");
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(LoginRegister.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LoginRegister.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LoginRegister.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LoginRegister.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LoginRegister().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ButtonDangNhapSlide;
    private javax.swing.JButton ButtonDangkySlide;
    private javax.swing.JPanel DangKyPanel;
    private javax.swing.JPanel DangNhapPanel;
    private quanlythuchi.components.Button button2;
    private quanlythuchi.components.Button buttonDangNhap;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPasswordField jPasswordField2;
    private javax.swing.JPasswordField jPasswordField3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JLabel labelNote;
    private javax.swing.JLabel labelNote1;
    private quanlythuchi.components.PanelSlide panelSlide;
    private javax.swing.JPasswordField txtpassword;
    private javax.swing.JTextField txtusername;
    // End of variables declaration//GEN-END:variables
}
