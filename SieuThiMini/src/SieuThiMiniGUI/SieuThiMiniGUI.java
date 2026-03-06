package SieuThiMiniGUI;

import DTO.*;
import SieuThiMiniBUS.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

public class SieuThiMiniGUI extends JFrame {
    private Color primaryColor = new Color(0, 123, 255);
    private Color secondaryColor = new Color(108, 117, 125);
    private Color bgColor = new Color(244, 246, 249);
    private Color sidebarBg = Color.WHITE;

    private DefaultTableModel model;
    private JTable tblSanPham;
    private JPanel pnlMainContent;
    private CardLayout cardLayout = new CardLayout();

    public SieuThiMiniGUI() {
        setTitle("Hệ Thống Quản Lý Siêu Thị Mini");
        setSize(1300, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        initSidebar();
        initMainContent();
    }

    private void initSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(250, 0));
        sidebar.setBackground(sidebarBg);
        sidebar.setBorder(new MatteBorder(0, 0, 0, 1, new Color(220, 220, 220)));

        JLabel lblLogo = new JLabel("Admin Panel");
        lblLogo.setForeground(primaryColor);
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblLogo.setBorder(new EmptyBorder(30, 25, 30, 10));
        lblLogo.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(lblLogo);

        String[] menus = {"Trang Tổng Quan", "Loại Sản Phẩm", "Quản Lý Sản Phẩm", "Quản Lý Nhập Hàng", "Quản Lý Giá Bán", "Quản Lý Đơn Hàng", "Quản Lý Khách Hàng", "Thống Kê & Báo Cáo"};

        for (String m : menus) {
            JButton btn = createSidebarBtn(m);
            btn.addActionListener(e ->{
                switchTab(m);
                resetSidebarButton();
                btn.setBackground(new Color(240,247,255));
                btn.setForeground(primaryColor);
                btn.setFont(new Font("Segoe UI", Font.BOLD, 15));

            });
            if (m.equals("Trang Tổng Quan")) {
                btn.setBackground(new Color(240, 247, 255));
                btn.setForeground(primaryColor);
                btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
            }
            sidebar.add(btn);
        }
        add(sidebar, BorderLayout.WEST);
    }
    private void switchTab(String tabName){
        cardLayout.show(pnlMainContent,tabName);
    }
    private void resetSidebarButton(){
        for(Component c : ((JPanel)((BorderLayout)getContentPane().getLayout()).getLayoutComponent(BorderLayout.WEST)).getComponents()){
            if (c instanceof JButton) {
                c.setBackground(sidebarBg);
                c.setForeground(new Color(70,70,70));
                c.setFont(new Font("Segoe UI" , Font.PLAIN, 15));
            }
        }
    }
    private JButton createSidebarBtn(String text) {
        JButton btn = new JButton("  " + text);
        btn.setMaximumSize(new Dimension(250, 50));
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        btn.setBackground(sidebarBg);
        btn.setForeground(new Color(70, 70, 70));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        return btn;
    }
    private JPanel creatTongQuanPanel(){
        JPanel pnl = new JPanel(new GridBagLayout());
        pnl.setBackground(bgColor);
        pnl.add(new JLabel("Chào Mừng Đến Trang Tổng Quan"));
        return pnl;
    }

    private void initMainContent() {
        pnlMainContent = new JPanel(cardLayout);
        pnlMainContent.add(creatTongQuanPanel(),"Trang Tổng Quan");
        pnlMainContent.add(new JPanel(),"Loại Sản Phẩm");
        pnlMainContent.add(new QuanLySanPhamGUI(),"Quản Lý Sản Phẩm");
        pnlMainContent.add(new LoaiSanPhamGUI(), "Loại Sản Phẩm");
        pnlMainContent.add(new QuanLyKhachHangGUI(), "Quản Lý Khách Hàng");
        pnlMainContent.add(new QuanLyDonHangGUI(), "Quản Lý Đơn Hàng");
        pnlMainContent.add(new QuanLyGiaBanGUI(), "Quản Lý Giá Bán");        
        pnlMainContent.add(new QuanLyNhapHangGUI(), "Quản Lý Nhập Hàng");        
        pnlMainContent.add(new ThongKeBaoCaoGUI(), "Thống Kê & Báo Cáo");
        add(pnlMainContent, BorderLayout.CENTER);
    }
    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
        SwingUtilities.invokeLater(() -> new SieuThiMiniGUI().setVisible(true));
    }
}