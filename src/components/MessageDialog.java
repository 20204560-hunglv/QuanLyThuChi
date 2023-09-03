package components;

import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTargetAdapter;

public class MessageDialog extends javax.swing.JDialog {

    private final JFrame fram;
    private Animator animator;
    private boolean show;
    private boolean choose;// true: OK, false: cancel

    public boolean isChoose() {
        return choose;
    }

    public void setChoose(boolean choose) {
        this.choose = choose;
    }
    
    public MessageDialog(JFrame fram) {
        super(fram, true);
        this.fram = fram;
        initComponents();
        init();
    }

    private void init() {
        setBackground(new Color(0, 0, 0, 0));
        StyledDocument doc = tpMessage.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
        tpMessage.setOpaque(false);
        tpMessage.setBackground(new Color(0,0,0,0));
        animator = new Animator( 350, new TimingTargetAdapter(){
            @Override
            public void timingEvent(float fraction) {
                float f= show ? fraction : 1f - fraction;
                setOpacity(f);
            }

            @Override
            public void end() {
                if (show == false ){
                    dispose();
                }
            }
           
        });
        animator.setResolution(0);
        animator.setAcceleration(.5f);
        animator.setDeceleration(.5f);
        setOpacity(0f);
    }
    
    private void startAnimator(boolean show){
        if (animator.isRunning()){
            float f=animator.getTimingFraction();
            animator.stop();
            animator.setStartFraction(1f-f);
        }else {
            animator.setStartFraction(0f);
        }
        this.show=show;
        animator.start();
    }

    public void showMessage(String title, String message) {
        lbTitle.setText(title);
        tpMessage.setText(message);
        setLocationRelativeTo(fram);
        startAnimator(true);
        setVisible(true);
    }

    public void closeMessage() {
        startAnimator(false);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        background1 = new components.Background();
        btnOK = new components.Button();
        jLabel1 = new javax.swing.JLabel();
        lbTitle = new javax.swing.JLabel();
        tpMessage = new javax.swing.JTextPane();
        btnHuy = new components.Button();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setPreferredSize(new java.awt.Dimension(300, 300));

        background1.setBackground(new java.awt.Color(255, 255, 255));
        background1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(25, 150, 245)));

        btnOK.setText("Xác nhận");
        btnOK.setBorderColor(new java.awt.Color(25, 150, 245));
        btnOK.setColor(new java.awt.Color(25, 150, 245));
        btnOK.setColorClick(new java.awt.Color(0, 102, 204));
        btnOK.setColorOver(new java.awt.Color(0, 153, 204));
        btnOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOKActionPerformed(evt);
            }
        });

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/quanlythuchi/img/icons8_notification_60px_1.png"))); // NOI18N

        lbTitle.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        lbTitle.setForeground(new java.awt.Color(255, 51, 51));
        lbTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbTitle.setText("Message title");

        tpMessage.setEditable(false);
        tpMessage.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        tpMessage.setForeground(new java.awt.Color(76, 76, 76));
        tpMessage.setText("Message text\nSimple");
        tpMessage.setFocusable(false);

        btnHuy.setText("X");
        btnHuy.setBorderColor(new java.awt.Color(255, 255, 255));
        btnHuy.setColorClick(new java.awt.Color(255, 0, 0));
        btnHuy.setColorOver(new java.awt.Color(255, 0, 0));
        btnHuy.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnHuy.setOpaque(true);
        btnHuy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHuyActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout background1Layout = new javax.swing.GroupLayout(background1);
        background1.setLayout(background1Layout);
        background1Layout.setHorizontalGroup(
            background1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lbTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 298, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, background1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(background1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, background1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnHuy, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, background1Layout.createSequentialGroup()
                        .addComponent(tpMessage)
                        .addContainerGap())))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, background1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnOK, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(108, 108, 108))
        );
        background1Layout.setVerticalGroup(
            background1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(background1Layout.createSequentialGroup()
                .addGroup(background1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnHuy, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                .addComponent(lbTitle)
                .addGap(26, 26, 26)
                .addComponent(tpMessage, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnOK, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(background1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(background1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnHuyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHuyActionPerformed
        choose=false;
        closeMessage();
    }//GEN-LAST:event_btnHuyActionPerformed

    private void btnOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOKActionPerformed
        // TODO add your handling code here:
        choose=true;
        closeMessage();
    }//GEN-LAST:event_btnOKActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private components.Background background1;
    private components.Button btnHuy;
    private components.Button btnOK;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel lbTitle;
    private javax.swing.JTextPane tpMessage;
    // End of variables declaration//GEN-END:variables
}
