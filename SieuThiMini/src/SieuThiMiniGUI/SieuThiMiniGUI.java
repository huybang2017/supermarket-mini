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

        String[] menus = {"Trang Tổng Quan", "Loại Sản Phẩm", "Quản Lý Sản Phẩm", "Quản Lý Nhập Hàng","Quản Lý Nhân Viên","Quản Lý Giá Bán", "Quản Lý Đơn Hàng", "Quản Lý Khách Hàng","Quản Lý Khuyến Mãi", "Thống Kê & Báo Cáo"};

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
        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(bgColor);
        
        // Header với thời gian real-time
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        JLabel lblTitle = new JLabel("🛒 Máy Tính Tiền POS");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(primaryColor);
        
        JLabel lblTime = new JLabel();
        lblTime.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        Timer timer = new Timer(1000, e -> {
            lblTime.setText(new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date()));
        });
        timer.start();
        
        headerPanel.add(lblTitle, BorderLayout.WEST);
        headerPanel.add(lblTime, BorderLayout.EAST);
        
        // Content chính: Left (sản phẩm) + Right (giỏ hàng)
        JPanel contentPanel = new JPanel(new BorderLayout(15, 0));
        contentPanel.setBackground(bgColor);
        contentPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        // LEFT: Catalog sản phẩm
        JPanel leftPanel = new JPanel(new BorderLayout(10, 10));
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        
        // Search bar
        JPanel searchPanel = new JPanel(new BorderLayout(10, 0));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(new EmptyBorder(15, 15, 10, 15));
        
        JTextField txtSearch = new JTextField();
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            new EmptyBorder(8, 12, 8, 12)
        ));
        txtSearch.setPreferredSize(new Dimension(0, 40));
        
        JButton btnSearch = new JButton("🔍 Tìm");
        btnSearch.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSearch.setBackground(primaryColor);
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setFocusPainted(false);
        btnSearch.setPreferredSize(new Dimension(100, 40));
        
        searchPanel.add(new JLabel("Tìm sản phẩm: "), BorderLayout.WEST);
        searchPanel.add(txtSearch, BorderLayout.CENTER);
        searchPanel.add(btnSearch, BorderLayout.EAST);
        
        // Category tabs
        JPanel categoryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        categoryPanel.setBackground(Color.WHITE);
        categoryPanel.setBorder(new EmptyBorder(0, 15, 10, 15));
        
        String[] categories = {"Tất Cả", "Đồ Uống", "Bánh Kẹo", "Mì Ăn Liền", "Sữa", "Snack", "Gia Dụng"};
        ButtonGroup bgCategory = new ButtonGroup();
        
        for (String cat : categories) {
            JToggleButton btnCat = new JToggleButton(cat);
            btnCat.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            btnCat.setBackground(Color.WHITE);
            btnCat.setFocusPainted(false);
            btnCat.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                new EmptyBorder(6, 15, 6, 15)
            ));
            if (cat.equals("Tất Cả")) {
                btnCat.setSelected(true);
                btnCat.setBackground(primaryColor);
                btnCat.setForeground(Color.WHITE);
            }
            bgCategory.add(btnCat);
            categoryPanel.add(btnCat);
        }
        
        // Product Grid
        JPanel productsGrid = new JPanel(new GridLayout(0, 4, 15, 15));
        productsGrid.setBackground(Color.WHITE);
        productsGrid.setBorder(new EmptyBorder(0, 15, 15, 15));
        
        // Load sản phẩm
        new SanPhamBUS().docDSSP();
        DefaultTableModel cartModel = new DefaultTableModel(
            new String[]{"Mã SP", "Tên", "SL", "Đơn Giá", "Thành Tiền"}, 0
        );
        JLabel lblTotal = new JLabel("0 VNĐ");
        
        if (SanPhamBUS.dssp != null) {
            for (SanPhamDTO sp : SanPhamBUS.dssp) {
                JButton btnProduct = createProductButton(sp, cartModel, lblTotal);
                productsGrid.add(btnProduct);
            }
        }
        
        JScrollPane scrollProducts = new JScrollPane(productsGrid);
        scrollProducts.setBorder(null);
        scrollProducts.getVerticalScrollBar().setUnitIncrement(16);
        
        leftPanel.add(searchPanel, BorderLayout.NORTH);
        leftPanel.add(categoryPanel, BorderLayout.CENTER);
        JPanel gridContainer = new JPanel(new BorderLayout());
        gridContainer.setBackground(Color.WHITE);
        gridContainer.add(scrollProducts, BorderLayout.CENTER);
        leftPanel.add(gridContainer, BorderLayout.SOUTH);
        
        // RIGHT: Giỏ hàng và thanh toán
        JPanel rightPanel = new JPanel(new BorderLayout(0, 15));
        rightPanel.setPreferredSize(new Dimension(450, 0));
        rightPanel.setBackground(bgColor);
        
        // Giỏ hàng
        JPanel cartPanel = new JPanel(new BorderLayout());
        cartPanel.setBackground(Color.WHITE);
        cartPanel.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        
        JLabel lblCartTitle = new JLabel("  🛒 Giỏ Hàng");
        lblCartTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblCartTitle.setOpaque(true);
        lblCartTitle.setBackground(new Color(248, 249, 250));
        lblCartTitle.setBorder(new EmptyBorder(12, 10, 12, 10));
        
        JTable tblCart = new JTable(cartModel) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblCart.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tblCart.setRowHeight(35);
        tblCart.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tblCart.getTableHeader().setBackground(new Color(240, 240, 240));
        
        JScrollPane scrollCart = new JScrollPane(tblCart);
        scrollCart.setBorder(null);
        
        JPanel cartActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        cartActions.setBackground(Color.WHITE);
        
        JButton btnRemove = new JButton("- Xóa Sp");
        btnRemove.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnRemove.setBackground(new Color(220, 53, 69));
        btnRemove.setForeground(Color.WHITE);
        btnRemove.setFocusPainted(false);
        btnRemove.addActionListener(e -> {
            int row = tblCart.getSelectedRow();
            if (row != -1) {
                cartModel.removeRow(row);
                updateTotal(cartModel, lblTotal);
            }
        });
        
        JButton btnClear = new JButton("🗑 Xóa Tất Cả");
        btnClear.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnClear.setBackground(secondaryColor);
        btnClear.setForeground(Color.WHITE);
        btnClear.setFocusPainted(false);
        btnClear.addActionListener(e -> {
            cartModel.setRowCount(0);
            updateTotal(cartModel, lblTotal);
        });
        
        cartActions.add(btnRemove);
        cartActions.add(btnClear);
        
        cartPanel.add(lblCartTitle, BorderLayout.NORTH);
        cartPanel.add(scrollCart, BorderLayout.CENTER);
        cartPanel.add(cartActions, BorderLayout.SOUTH);
        
        // Thanh toán
        JPanel paymentPanel = new JPanel(new BorderLayout());
        paymentPanel.setBackground(Color.WHITE);
        paymentPanel.setBorder(new CompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            new EmptyBorder(20, 20, 20, 20)
        ));
        
        JPanel paymentForm = new JPanel(new GridLayout(4, 1, 0, 12));
        paymentForm.setBackground(Color.WHITE);
        
        // Tổng tiền
        JPanel totalRow = new JPanel(new BorderLayout());
        totalRow.setBackground(new Color(240, 247, 255));
        totalRow.setBorder(new EmptyBorder(12, 15, 12, 15));
        JLabel lblTotalLabel = new JLabel("TỔNG TIỀN:");
        lblTotalLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTotal.setForeground(new Color(220, 53, 69));
        lblTotal.setHorizontalAlignment(SwingConstants.RIGHT);
        totalRow.add(lblTotalLabel, BorderLayout.WEST);
        totalRow.add(lblTotal, BorderLayout.EAST);
        
        // Tiền khách đưa
        JPanel customerRow = new JPanel(new BorderLayout(10, 0));
        customerRow.setBackground(Color.WHITE);
        JLabel lblCustomer = new JLabel("Tiền khách đưa:");
        lblCustomer.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTextField txtCustomerPay = new JTextField();
        txtCustomerPay.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtCustomerPay.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            new EmptyBorder(8, 12, 8, 12)
        ));
        customerRow.add(lblCustomer, BorderLayout.WEST);
        customerRow.add(txtCustomerPay, BorderLayout.CENTER);
        
        // Tiền thừa
        JPanel changeRow = new JPanel(new BorderLayout());
        changeRow.setBackground(new Color(240, 255, 240));
        changeRow.setBorder(new EmptyBorder(12, 15, 12, 15));
        JLabel lblChangeLabel = new JLabel("Tiền thừa:");
        lblChangeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JLabel lblChange = new JLabel("0 VNĐ");
        lblChange.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblChange.setForeground(new Color(40, 167, 69));
        lblChange.setHorizontalAlignment(SwingConstants.RIGHT);
        changeRow.add(lblChangeLabel, BorderLayout.WEST);
        changeRow.add(lblChange, BorderLayout.EAST);
        
        // Auto calculate change
        txtCustomerPay.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                try {
                    long total = Long.parseLong(lblTotal.getText().replace(",", "").replace(" VNĐ", ""));
                    long customerPay = Long.parseLong(txtCustomerPay.getText().trim());
                    long change = customerPay - total;
                    lblChange.setText(String.format("%,d VNĐ", change));
                    lblChange.setForeground(change >= 0 ? new Color(40, 167, 69) : new Color(220, 53, 69));
                } catch (Exception ex) {
                    lblChange.setText("0 VNĐ");
                }
            }
        });
        
        // Buttons
        JPanel btnRow = new JPanel(new GridLayout(1, 2, 10, 0));
        btnRow.setBackground(Color.WHITE);
        
        JButton btnCancel = new JButton("✗ HỦY");
        btnCancel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnCancel.setBackground(secondaryColor);
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setFocusPainted(false);
        btnCancel.setPreferredSize(new Dimension(0, 50));
        btnCancel.addActionListener(e -> {
            cartModel.setRowCount(0);
            txtCustomerPay.setText("");
            updateTotal(cartModel, lblTotal);
            lblChange.setText("0 VNĐ");
        });
        
        JButton btnCheckout = new JButton("✓ THANH TOÁN");
        btnCheckout.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnCheckout.setBackground(new Color(40, 167, 69));
        btnCheckout.setForeground(Color.WHITE);
        btnCheckout.setFocusPainted(false);
        btnCheckout.setPreferredSize(new Dimension(0, 50));
        btnCheckout.addActionListener(e -> {
            if (cartModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(mainPanel, "Giỏ hàng trống!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                long total = Long.parseLong(lblTotal.getText().replace(",", "").replace(" VNĐ", ""));
                long customerPay = Long.parseLong(txtCustomerPay.getText().trim());
                if (customerPay < total) {
                    JOptionPane.showMessageDialog(mainPanel, "Tiền khách đưa không đủ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                JOptionPane.showMessageDialog(mainPanel, 
                    "✓ Thanh toán thành công!\n" +
                    "Tổng tiền: " + String.format("%,d VNĐ", total) + "\n" +
                    "Tiền nhận: " + String.format("%,d VNĐ", customerPay) + "\n" +
                    "Tiền thừa: " + String.format("%,d VNĐ", customerPay - total),
                    "Thành công", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Reset
                cartModel.setRowCount(0);
                txtCustomerPay.setText("");
                updateTotal(cartModel, lblTotal);
                lblChange.setText("0 VNĐ");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(mainPanel, "Vui lòng nhập đúng số tiền!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        btnRow.add(btnCancel);
        btnRow.add(btnCheckout);
        
        paymentForm.add(totalRow);
        paymentForm.add(customerRow);
        paymentForm.add(changeRow);
        paymentForm.add(btnRow);
        
        paymentPanel.add(paymentForm, BorderLayout.CENTER);
        
        rightPanel.add(cartPanel, BorderLayout.CENTER);
        rightPanel.add(paymentPanel, BorderLayout.SOUTH);
        
        contentPanel.add(leftPanel, BorderLayout.CENTER);
        contentPanel.add(rightPanel, BorderLayout.EAST);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        return mainPanel;
    }
    
    private JButton createProductButton(SanPhamDTO sp, DefaultTableModel cartModel, JLabel lblTotal) {
        JButton btn = new JButton();
        btn.setLayout(new BorderLayout(5, 5));
        btn.setBackground(Color.WHITE);
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            new EmptyBorder(12, 10, 12, 10)
        ));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JPanel infoPanel = new JPanel(new GridLayout(3, 1, 0, 3));
        infoPanel.setOpaque(false);
        
        JLabel lblName = new JLabel("<html><b>" + sp.getTensanpham() + "</b></html>");
        lblName.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblName.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel lblPrice = new JLabel(String.format("%,d VNĐ", sp.getDongia()));
        lblPrice.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblPrice.setForeground(new Color(220, 53, 69));
        lblPrice.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel lblStock = new JLabel("Kho: " + sp.getSoluong());
        lblStock.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblStock.setForeground(new Color(108, 117, 125));
        lblStock.setHorizontalAlignment(SwingConstants.CENTER);
        
        infoPanel.add(lblName);
        infoPanel.add(lblPrice);
        infoPanel.add(lblStock);
        
        btn.add(infoPanel, BorderLayout.CENTER);
        
        btn.addActionListener(e -> {
            // Kiểm tra đã có trong giỏ chưa
            boolean found = false;
            for (int i = 0; i < cartModel.getRowCount(); i++) {
                if (cartModel.getValueAt(i, 0).toString().equals(sp.getMasanpham())) {
                    int currentQty = Integer.parseInt(cartModel.getValueAt(i, 2).toString());
                    int newQty = currentQty + 1;
                    cartModel.setValueAt(newQty, i, 2);
                    cartModel.setValueAt(sp.getDongia() * newQty, i, 4);
                    found = true;
                    break;
                }
            }
            
            if (!found) {
                cartModel.addRow(new Object[]{
                    sp.getMasanpham(),
                    sp.getTensanpham(),
                    1,
                    sp.getDongia(),
                    sp.getDongia()
                });
            }
            
            updateTotal(cartModel, lblTotal);
            
            // Animation effect
            btn.setBackground(new Color(200, 230, 255));
            Timer timer = new Timer(200, evt -> btn.setBackground(Color.WHITE));
            timer.setRepeats(false);
            timer.start();
        });
        
        return btn;
    }
    
    private void updateTotal(DefaultTableModel model, JLabel lblTotal) {
        long total = 0;
        for (int i = 0; i < model.getRowCount(); i++) {
            total += Long.parseLong(model.getValueAt(i, 4).toString());
        }
        lblTotal.setText(String.format("%,d VNĐ", total));
    }

    private void initMainContent() {
        pnlMainContent = new JPanel(cardLayout);
        pnlMainContent.add(creatTongQuanPanel(),"Trang Tổng Quan");
        pnlMainContent.add(new JPanel(),"Loại Sản Phẩm");
        pnlMainContent.add(new QuanLySanPhamGUI(),"Quản Lý Sản Phẩm");
        pnlMainContent.add(new LoaiSanPhamGUI(), "Loại Sản Phẩm");
        pnlMainContent.add(new QuanLyKhachHangGUI(), "Quản Lý Khách Hàng");
        pnlMainContent.add(new QuanLyNhanVienGUI(), "Quản Lý Nhân Viên");
        pnlMainContent.add(new QuanLyDonHangGUI(), "Quản Lý Đơn Hàng");
        pnlMainContent.add(new QuanLyGiaBanGUI(), "Quản Lý Giá Bán");        
        pnlMainContent.add(new QuanLyNhapHangGUI(), "Quản Lý Nhập Hàng");        
        pnlMainContent.add(new KhuyenMaiGUI(), "Quản Lý Khuyến Mãi");        
        pnlMainContent.add(new ThongKeBaoCaoGUI(), "Thống Kê & Báo Cáo");
        add(pnlMainContent, BorderLayout.CENTER);
    }
    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
        SwingUtilities.invokeLater(() -> new SieuThiMiniGUI().setVisible(true));
    }
}