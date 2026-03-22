package SieuThiMiniGUI;

import DTO.*;
import SieuThiMiniBUS.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;

public class SieuThiMiniGUI extends JFrame {
    private Color primaryColor = new Color(0, 123, 255);
    private Color secondaryColor = new Color(108, 117, 125);
    private Color bgColor = new Color(244, 246, 249);
    private Color sidebarBg = Color.WHITE;

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

        String[] menus = {"Trang Tổng Quan", "Loại Sản Phẩm", "Quản Lý Sản Phẩm","Quản Lý Hãng Sản Xuất", "Quản Lý Nhập Hàng","Quản Lý Nhà Cung Cấp","Quản Lý Nhân Viên","Quản Lý Giá Bán", "Quản Lý Đơn Hàng", "Quản Lý Khách Hàng","Quản Lý Khuyến Mãi", "Thống Kê & Báo Cáo"};

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
        
        // --- CHUYỂN CÁC BIẾN NÀY LÊN TRƯỚC ĐỂ DÙNG CHUNG CHO HÀM LỌC ---
        DefaultTableModel cartModel = new DefaultTableModel(new String[]{"Mã SP", "Tên", "SL", "Đơn Giá", "Thành Tiền"}, 0);
        JLabel lblSubTotal  = new JLabel("0 VNĐ");
        JLabel lblKMDiscount = new JLabel("Không có");
        JLabel lblFinalTotal = new JLabel("0 VNĐ");
        
        new SanPhamBUS().docDSSP();
        new ChuongTrinhKhuyenMaiBUS().docDSKM();
        new ChuongTrinhKhuyenMaiSpBUS().docTatCa();
        new ChuongTrinhKhuyenMaiHDBUS().docTatCa();

        JPanel productsGrid = new JPanel(new GridLayout(0, 5, 8, 8));
        productsGrid.setBackground(Color.WHITE);
        productsGrid.setBorder(new EmptyBorder(8, 10, 10, 10));

        final String[] currentCategory = {"Tất Cả"}; // Biến lưu danh mục đang được chọn

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
        btnSearch.setOpaque(true);
        btnSearch.setBorderPainted(false);
        btnSearch.setPreferredSize(new Dimension(100, 40));
        
        searchPanel.add(new JLabel("Tìm sản phẩm: "), BorderLayout.WEST);
        searchPanel.add(txtSearch, BorderLayout.CENTER);
        searchPanel.add(btnSearch, BorderLayout.EAST);

        // --- HÀM XỬ LÝ LỌC SẢN PHẨM ---
        Runnable filterProducts = () -> {
            productsGrid.removeAll();
            String kw = txtSearch.getText().trim().toLowerCase();
            
            int maLoaiFilter = -1;
            // LƯU Ý: Bạn cần đổi các số 1, 2, 3... dưới đây cho đúng với getMaloai() trong CSDL MySQL của bạn nhé!
            switch (currentCategory[0]) {
                case "Đồ uống lạnh": maLoaiFilter = 1; break; 
                case "Mì & Thực phẩm ăn liền": maLoaiFilter = 2; break;
                case "Sữa & Chế phẩm": maLoaiFilter = 3; break;
                case "Bánh Kẹo & Snack": maLoaiFilter = 4; break;
                case "Đồ ăn nhanh (Ready-to-eat)": maLoaiFilter = 5; break;
                case "Rượu Bia": maLoaiFilter = 6; break;
                case "Hàng Tiêu Dùng / Y Tế": maLoaiFilter = 7; break;
            }

            if (SanPhamBUS.dssp != null) {
                for (DTO.SanPhamDTO sp : SanPhamBUS.dssp) {
                    boolean matchName = kw.isEmpty() || sp.getTensanpham().toLowerCase().contains(kw);
                    boolean matchCat = currentCategory[0].equals("Tất Cả") || sp.getMaLoai() == maLoaiFilter;
                    
                    if (matchName && matchCat) {
                        productsGrid.add(createProductButton(sp, cartModel, lblSubTotal, lblKMDiscount, lblFinalTotal));
                    }
                }
            }
            productsGrid.revalidate();
            productsGrid.repaint();
        };

        // Gắn sự kiện tìm kiếm
        btnSearch.addActionListener(e -> filterProducts.run());
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) { filterProducts.run(); } // Lọc realtime khi gõ phím
        });

        // Category tabs - SỬ DỤNG BOXLAYOUT ĐỂ ÉP NẰM TRÊN 1 HÀNG NGANG
        JPanel categoryPanel = new JPanel();
        categoryPanel.setLayout(new BoxLayout(categoryPanel, BoxLayout.X_AXIS));
        categoryPanel.setBackground(Color.WHITE);
        categoryPanel.setBorder(new EmptyBorder(5, 15, 5, 15));
        
        // Nhớ đổi lại tên các danh mục này cho khớp với Database của bạn nhé
        String[] categories = {"Tất Cả", "Đồ uống lạnh", "Mì & Thực phẩm ăn liền", "Sữa & Chế phẩm", "Bánh Kẹo & Snack","Đồ ăn nhanh (Ready-to-eat)","Rượu Bia","Hàng Tiêu Dùng / Y Tế"};
        ButtonGroup bgCategory = new ButtonGroup();
        
        for (String cat : categories) {
            JToggleButton btnCat = new JToggleButton(cat);
            btnCat.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            btnCat.setOpaque(true);
            btnCat.setFocusPainted(false);
            btnCat.setCursor(new Cursor(Cursor.HAND_CURSOR));
            // Giảm padding một chút để nút thon gọn hơn
            btnCat.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 123, 255), 1),
                new EmptyBorder(6, 12, 6, 12) 
            ));
            btnCat.setBackground(Color.WHITE);
            btnCat.setForeground(primaryColor);
            
            if (cat.equals("Tất Cả")) {
                btnCat.setSelected(true);
                btnCat.setBackground(primaryColor);
                btnCat.setForeground(Color.WHITE);
                btnCat.setFont(new Font("Segoe UI", Font.BOLD, 13));
            }
            
            btnCat.addItemListener(e -> {
                if (btnCat.isSelected()) {
                    btnCat.setBackground(primaryColor);
                    btnCat.setForeground(Color.WHITE);
                    btnCat.setFont(new Font("Segoe UI", Font.BOLD, 13));
                    
                    currentCategory[0] = cat; 
                    filterProducts.run(); 
                } else {
                    btnCat.setBackground(Color.WHITE);
                    btnCat.setForeground(primaryColor);
                    btnCat.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                }
            });
            bgCategory.add(btnCat);
            categoryPanel.add(btnCat);
            
            // Tạo khoảng trống 8px giữa các nút thay vì dùng FlowLayout
            categoryPanel.add(Box.createRigidArea(new Dimension(8, 0))); 
        }

        // BỌC VÀO JSCROLLPANE ĐỂ CUỘN NGANG KHI BỊ TRÀN
        JScrollPane scrollCategory = new JScrollPane(categoryPanel);
        scrollCategory.setBorder(null);
        scrollCategory.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER); // Tắt cuộn dọc
        scrollCategory.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); // Bật cuộn ngang
        scrollCategory.setPreferredSize(new Dimension(0, 50)); // Khóa cứng chiều cao, không cho bự ra
        scrollCategory.getHorizontalScrollBar().setUnitIncrement(16);

        // Gọi lọc lần đầu để load toàn bộ SP
        filterProducts.run();

        // THÊM PANEL BAO BỌC ĐỂ NGĂN GRIDLAYOUT KÉO GIÃN
        JPanel gridWrapper = new JPanel(new BorderLayout());
        gridWrapper.setBackground(Color.WHITE);
        gridWrapper.add(productsGrid, BorderLayout.NORTH); // Ép grid nằm sát lên trên

        // Đưa gridWrapper vào JScrollPane thay vì productsGrid
        JScrollPane scrollProducts = new JScrollPane(gridWrapper);
        scrollProducts.setBorder(null);
        scrollProducts.getVerticalScrollBar().setUnitIncrement(16);       
        // topBar: search + category gom lại thành NORTH; grid vào CENTER
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(Color.WHITE);
        topBar.add(searchPanel, BorderLayout.NORTH);
        topBar.add(scrollCategory, BorderLayout.SOUTH);
        leftPanel.add(topBar, BorderLayout.NORTH);
        leftPanel.add(scrollProducts, BorderLayout.CENTER);
        
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
        btnRemove.setOpaque(true);
        btnRemove.setBorderPainted(false);
        btnRemove.addActionListener(e -> {
            int row = tblCart.getSelectedRow();
            if (row != -1) {
                cartModel.removeRow(row);
                updateTotal(cartModel, lblSubTotal, lblKMDiscount, lblFinalTotal);
            }
        });

        JButton btnClear = new JButton("🗑 Xóa Tất Cả");
        btnClear.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnClear.setBackground(secondaryColor);
        btnClear.setForeground(Color.WHITE);
        btnClear.setFocusPainted(false);
        btnClear.setOpaque(true);
        btnClear.setBorderPainted(false);
        btnClear.addActionListener(e -> {
            cartModel.setRowCount(0);
            updateTotal(cartModel, lblSubTotal, lblKMDiscount, lblFinalTotal);
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
            new EmptyBorder(15, 15, 15, 15)
        ));

        JPanel paymentForm = new JPanel(new GridLayout(6, 1, 0, 8));
        paymentForm.setBackground(Color.WHITE);

        // Row 1: Tổng tiền hàng
        JPanel subTotalRow = new JPanel(new BorderLayout());
        subTotalRow.setBackground(Color.WHITE);
        subTotalRow.setBorder(new EmptyBorder(6, 10, 6, 10));
        JLabel lblSubTotalLabel = new JLabel("Tổng tiền hàng:");
        lblSubTotalLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubTotal.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubTotal.setHorizontalAlignment(SwingConstants.RIGHT);
        subTotalRow.add(lblSubTotalLabel, BorderLayout.WEST);
        subTotalRow.add(lblSubTotal, BorderLayout.EAST);

        // Row 2: Giảm KM hóa đơn
        JPanel kmRow = new JPanel(new BorderLayout());
        kmRow.setBackground(new Color(240, 255, 245));
        kmRow.setBorder(new EmptyBorder(6, 10, 6, 10));
        JLabel lblKMLabel = new JLabel("🏷 Giảm KM HĐ:");
        lblKMLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblKMDiscount.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblKMDiscount.setForeground(new Color(108, 117, 125));
        lblKMDiscount.setHorizontalAlignment(SwingConstants.RIGHT);
        kmRow.add(lblKMLabel, BorderLayout.WEST);
        kmRow.add(lblKMDiscount, BorderLayout.EAST);

        // Row 3: Thực trả
        JPanel finalRow = new JPanel(new BorderLayout());
        finalRow.setBackground(new Color(240, 247, 255));
        finalRow.setBorder(new EmptyBorder(10, 10, 10, 10));
        JLabel lblFinalLabel = new JLabel("THỰC TRẢ:");
        lblFinalLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblFinalTotal.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblFinalTotal.setForeground(new Color(220, 53, 69));
        lblFinalTotal.setHorizontalAlignment(SwingConstants.RIGHT);
        finalRow.add(lblFinalLabel, BorderLayout.WEST);
        finalRow.add(lblFinalTotal, BorderLayout.EAST);

        // Row 4: Tiền khách đưa
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

        // Row 5: Tiền thừa
        JPanel changeRow = new JPanel(new BorderLayout());
        changeRow.setBackground(new Color(240, 255, 240));
        changeRow.setBorder(new EmptyBorder(10, 10, 10, 10));
        JLabel lblChangeLabel = new JLabel("Tiền thừa:");
        lblChangeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JLabel lblChange = new JLabel("0 VNĐ");
        lblChange.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblChange.setForeground(new Color(40, 167, 69));
        lblChange.setHorizontalAlignment(SwingConstants.RIGHT);
        changeRow.add(lblChangeLabel, BorderLayout.WEST);
        changeRow.add(lblChange, BorderLayout.EAST);

        // Auto tính tiền thừa dựa theo THỰC TRẢ
        txtCustomerPay.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                try {
                    long finalAmt = Long.parseLong(lblFinalTotal.getText().replaceAll("[,\\.]", "").replace(" VNĐ", ""));
                    long customerPay = Long.parseLong(txtCustomerPay.getText().trim());
                    long change = customerPay - finalAmt;
                    lblChange.setText(String.format("%,d VNĐ", change));
                    lblChange.setForeground(change >= 0 ? new Color(40, 167, 69) : new Color(220, 53, 69));
                } catch (Exception ex) {
                    lblChange.setText("0 VNĐ");
                }
            }
        });

        // Row 6: Buttons
        JPanel btnRow = new JPanel(new GridLayout(1, 2, 10, 0));
        btnRow.setBackground(Color.WHITE);

        JButton btnCancel = new JButton("✗ HỦY");
        btnCancel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnCancel.setBackground(secondaryColor);
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setFocusPainted(false);
        btnCancel.setOpaque(true);
        btnCancel.setBorderPainted(false);
        btnCancel.setPreferredSize(new Dimension(0, 50));
        btnCancel.addActionListener(e -> {
            cartModel.setRowCount(0);
            txtCustomerPay.setText("");
            updateTotal(cartModel, lblSubTotal, lblKMDiscount, lblFinalTotal);
            lblChange.setText("0 VNĐ");
        });

        JButton btnCheckout = new JButton("✓ THANH TOÁN");
        btnCheckout.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnCheckout.setBackground(new Color(40, 167, 69));
        btnCheckout.setForeground(Color.WHITE);
        btnCheckout.setFocusPainted(false);
        btnCheckout.setOpaque(true);
        btnCheckout.setBorderPainted(false);
        btnCheckout.setPreferredSize(new Dimension(0, 50));
        btnCheckout.addActionListener(e -> {
            if (cartModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(mainPanel, "Giỏ hàng trống!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            long finalAmt;
            long customerPay;
            try {
                finalAmt = Long.parseLong(lblFinalTotal.getText().replaceAll("[,\\.]", "").replace(" VNĐ", ""));
                customerPay = Long.parseLong(txtCustomerPay.getText().trim());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(mainPanel, "Vui lòng nhập đúng số tiền!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (customerPay < finalAmt) {
                JOptionPane.showMessageDialog(mainPanel, "Tiền khách đưa không đủ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Tải danh sách NV nếu chưa có
            if (NhanVienBUS.dsnv == null || NhanVienBUS.dsnv.isEmpty()) new NhanVienBUS().docDSNV();
            java.util.ArrayList<DTO.NhanVienDTO> dsNV = NhanVienBUS.dsnv;
            if (dsNV == null || dsNV.isEmpty()) {
                JOptionPane.showMessageDialog(mainPanel, "Chưa có nhân viên trong hệ thống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // TẠO DIALOG XÁC NHẬN THANH TOÁN (Giống QuanLyDonHangGUI)
            JDialog dlg = new JDialog((Frame) SwingUtilities.getWindowAncestor(mainPanel), "Thông Tin Khách Hàng & Nhân Viên", true);
            dlg.setSize(450, 250);
            dlg.setLocationRelativeTo(mainPanel);
            dlg.setLayout(new BorderLayout(10, 10));

            JPanel form = new JPanel(new GridLayout(3, 2, 10, 15));
            form.setBorder(new EmptyBorder(20, 20, 10, 20));
            form.setBackground(Color.WHITE);

            // 1. ComboBox Nhân viên
            JComboBox<String> cbNV = new JComboBox<>();
            for (DTO.NhanVienDTO nv : dsNV) {
                cbNV.addItem(nv.getMaNV() + " - " + nv.getTenNV());
            }

            // 2. Khách Hàng (Tìm theo SĐT)
            JTextField txtSdtKH = new JTextField();
            JButton btnCheckKH = new JButton("Tìm");
            btnCheckKH.setBackground(primaryColor);
            btnCheckKH.setForeground(Color.WHITE);
            btnCheckKH.setFocusPainted(false);
            btnCheckKH.setPreferredSize(new Dimension(60, 30));
            
            JPanel pnlSdt = new JPanel(new BorderLayout(5, 0));
            pnlSdt.setOpaque(false);
            pnlSdt.add(txtSdtKH, BorderLayout.CENTER);
            pnlSdt.add(btnCheckKH, BorderLayout.EAST);

            JLabel lblTenKH = new JLabel("Khách vãng lai");
            lblTenKH.setFont(new Font("Segoe UI", Font.BOLD, 14));
            lblTenKH.setForeground(primaryColor);
            final int[] currentMaKH = {0}; // Mặc định 0 = Khách vãng lai

            // Sự kiện tìm kiếm SĐT
            btnCheckKH.addActionListener(evt -> {
                String sdt = txtSdtKH.getText().trim();
                if (sdt.isEmpty()) {
                    currentMaKH[0] = 0;
                    lblTenKH.setText("Khách vãng lai");
                    return;
                }
                if (KhachHangBUS.dskh == null) new KhachHangBUS().docDSKH();
                DTO.KhachHangDTO found = null;
                for (DTO.KhachHangDTO kh : KhachHangBUS.dskh) {
                    if (kh.getSdt() != null && kh.getSdt().equals(sdt)) { 
                        found = kh; 
                        break; 
                    }
                }
                
                if (found != null) {
                    currentMaKH[0] = found.getMaKH();
                    lblTenKH.setText(found.getHoKH() + " " + found.getTenKH());
                } else {
                    int ans = JOptionPane.showConfirmDialog(dlg, "SĐT chưa có trong hệ thống. Bạn có muốn đăng ký khách hàng mới?", "Chưa có thông tin", JOptionPane.YES_NO_OPTION);
                    if (ans == JOptionPane.YES_OPTION) {
                        showFormDangKyKH_Nhanh(dlg, sdt, currentMaKH, lblTenKH); // Gọi hàm đăng ký nhanh
                    }
                }
            });

            JLabel lblNV = new JLabel("Nhân Viên Lập *:");
            lblNV.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            form.add(lblNV); form.add(cbNV);

            JLabel lblSdt = new JLabel("SĐT Khách Hàng:");
            lblSdt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            form.add(lblSdt); form.add(pnlSdt);

            JLabel lblKhTitle = new JLabel("Tên Khách Hàng:");
            lblKhTitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            form.add(lblKhTitle); form.add(lblTenKH);

            // Panel Nút bấm
            JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
            btnPanel.setBackground(Color.WHITE);
            JButton btnXacNhan = new JButton("Xác Nhận & Xuất HĐ");
            btnXacNhan.setBackground(new Color(40, 167, 69));
            btnXacNhan.setForeground(Color.WHITE);
            JButton btnDong = new JButton("Hủy");
            btnDong.addActionListener(evt -> dlg.dispose());
            btnXacNhan.addActionListener(evt -> {
                int selectedNV = Integer.parseInt(cbNV.getSelectedItem().toString().split(" - ")[0]);
                int selectedKH = currentMaKH[0];

                // Tạo HoaDon
                DTO.HoaDonDTO hoaDon = new DTO.HoaDonDTO();
                hoaDon.setMaKH(selectedKH);
                hoaDon.setMaNV(selectedNV);
                hoaDon.setNgayLapDon(new java.util.Date());
                hoaDon.setTongTien(finalAmt);

                int newHDId = new HoaDonBUS().addHoaDonReturnId(hoaDon);
                if (newHDId == -1) {
                    JOptionPane.showMessageDialog(dlg, "Lỗi khi tạo hóa đơn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Tạo ChiTietHoaDon và cập nhật tồn kho
                SanPhamBUS spBUS = new SanPhamBUS();
                for (int i = 0; i < cartModel.getRowCount(); i++) {
                    int maSP = Integer.parseInt(cartModel.getValueAt(i, 0).toString());
                    int soLuong = Integer.parseInt(cartModel.getValueAt(i, 2).toString());
                    long donGia = Long.parseLong(cartModel.getValueAt(i, 3).toString().replaceAll("[,\\.]", "")); // Đã sửa lỗi ép kiểu

                    DTO.ChiTietHoaDonDTO ct = new DTO.ChiTietHoaDonDTO();
                    ct.setMaHD(newHDId);
                    ct.setMaSP(maSP);
                    ct.setSoLuong(soLuong);
                    ct.setDonGia(donGia);
                    ct.setThanhTien(donGia * soLuong);
                    new SieuThiMiniDAO.ChiTietHoaDonDAO().addChiTietHoaDon(ct);

                    // Cập nhật tồn kho
                    int tonKhoHienTai = 0;
                    if (SanPhamBUS.dssp != null) {
                        for (DTO.SanPhamDTO sp : SanPhamBUS.dssp) {
                            if (sp.getMasanpham() == maSP) { tonKhoHienTai = sp.getSoluong(); break; }
                        }
                    }
                    spBUS.capNhatSoLuong(maSP, tonKhoHienTai - soLuong);
                }

                long subtotalAmt = Long.parseLong(lblSubTotal.getText().replaceAll("[,\\.]", "").replace(" VNĐ", ""));
                long discountAmt = subtotalAmt - finalAmt;
                String discountLine = discountAmt > 0 ? "\nGiảm KM: -" + String.format("%,d VNĐ", discountAmt) : "";
                
                dlg.dispose(); // Đóng form thông tin
                
                JOptionPane.showMessageDialog(mainPanel,
                    "✓ Thanh toán thành công! Mã HĐ: #" + newHDId + "\n" +
                    "Tổng hàng: " + String.format("%,d VNĐ", subtotalAmt) +
                    discountLine + "\n" +
                    "Thực trả: " + String.format("%,d VNĐ", finalAmt) + "\n" +
                    "Tiền nhận: " + String.format("%,d VNĐ", customerPay) + "\n" +
                    "Tiền thừa: " + String.format("%,d VNĐ", customerPay - finalAmt),
                    "Thành công", JOptionPane.INFORMATION_MESSAGE);

                // RESET GIAO DIỆN (Xóa giỏ hàng, tiền thừa, tiền khách đưa)
                cartModel.setRowCount(0);
                txtCustomerPay.setText("");
                updateTotal(cartModel, lblSubTotal, lblKMDiscount, lblFinalTotal);
                lblChange.setText("0 VNĐ");
                            
            // RESET CATALOG SẢN PHẨM VÀ ÁP DỤNG LẠI BỘ LỌC
            new SanPhamBUS().docDSSP();
            filterProducts.run();
            });

            btnPanel.add(btnDong);
            btnPanel.add(btnXacNhan);
            dlg.add(form, BorderLayout.CENTER);
            dlg.add(btnPanel, BorderLayout.SOUTH);
            dlg.setVisible(true);
        });
        btnRow.add(btnCancel);
        btnRow.add(btnCheckout);

        paymentForm.add(subTotalRow);
        paymentForm.add(kmRow);
        paymentForm.add(finalRow);
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

        // Mỗi lần tab được hiển thị: reload KM + rebuild product buttons
        mainPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                new SanPhamBUS().docDSSP();
                new ChuongTrinhKhuyenMaiBUS().docDSKM();
                new ChuongTrinhKhuyenMaiSpBUS().docTatCa();
                new ChuongTrinhKhuyenMaiHDBUS().docTatCa();
                filterProducts.run();
            }
        });

        return mainPanel;
    }
    
    private JButton createProductButton(SanPhamDTO sp, DefaultTableModel cartModel,
            JLabel lblSubTotal, JLabel lblKMDiscount, JLabel lblFinalTotal) {
        long giamGiaSP = getGiamGiaSP(sp.getMasanpham());
        long giaThucTe = Math.max(0, sp.getDongia() - giamGiaSP);
        boolean coKM = giamGiaSP > 0;

        JButton btn = new JButton();
        btn.setPreferredSize(new Dimension(150, 180));
        btn.setLayout(new BorderLayout(5, 5));
        btn.setBackground(Color.WHITE);
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(coKM ? new Color(255, 140, 0) : new Color(220, 220, 220), coKM ? 2 : 1),
            new EmptyBorder(6, 5, 6, 5)
        ));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel infoPanel = new JPanel(new GridLayout(coKM ? 4 : 3, 1, 0, 2));
        infoPanel.setOpaque(false);

        JLabel lblName = new JLabel("<html><center>" + sp.getTensanpham() + "</center></html>");
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblName.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel lblPrice;
        if (coKM) {
            lblPrice = new JLabel("<html><center>"
                + "<span style='color:#999;text-decoration:line-through;font-size:10px'>"
                + String.format("%,d", sp.getDongia()) + "</span><br>"
                + "<span style='color:#dc3545;font-size:12px;font-weight:bold'>"
                + String.format("%,d", giaThucTe) + " VNĐ</span>"
                + "</center></html>");
            lblPrice.setHorizontalAlignment(SwingConstants.CENTER);
        } else {
            lblPrice = new JLabel(String.format("%,d VNĐ", sp.getDongia()));
            lblPrice.setFont(new Font("Segoe UI", Font.BOLD, 12));
            lblPrice.setForeground(new Color(220, 53, 69));
            lblPrice.setHorizontalAlignment(SwingConstants.CENTER);
        }

        JLabel lblStock = new JLabel("Kho: " + sp.getSoluong());
        lblStock.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lblStock.setForeground(new Color(108, 117, 125));
        lblStock.setHorizontalAlignment(SwingConstants.CENTER);

        infoPanel.add(lblName);
        infoPanel.add(lblPrice);
        infoPanel.add(lblStock);
        if (coKM) {
            JLabel lblBadge = new JLabel("🏷 -" + String.format("%,d", giamGiaSP) + " VNĐ");
            lblBadge.setFont(new Font("Segoe UI", Font.BOLD, 10));
            lblBadge.setForeground(new Color(255, 140, 0));
            lblBadge.setHorizontalAlignment(SwingConstants.CENTER);
            infoPanel.add(lblBadge);
        }

        btn.add(infoPanel, BorderLayout.CENTER);

        btn.addActionListener(e -> {
            boolean found = false;
            for (int i = 0; i < cartModel.getRowCount(); i++) {
                if (cartModel.getValueAt(i, 0).toString().equals(String.valueOf(sp.getMasanpham()))) {
                    int newQty = Integer.parseInt(cartModel.getValueAt(i, 2).toString()) + 1;
                    cartModel.setValueAt(newQty, i, 2);
                    cartModel.setValueAt(giaThucTe * newQty, i, 4);
                    found = true;
                    break;
                }
            }
            if (!found) {
                cartModel.addRow(new Object[]{
                    sp.getMasanpham(),
                    sp.getTensanpham(),
                    1,
                    giaThucTe,
                    giaThucTe
                });
            }
            updateTotal(cartModel, lblSubTotal, lblKMDiscount, lblFinalTotal);

            btn.setBackground(new Color(200, 230, 255));
            Timer timer = new Timer(200, evt -> btn.setBackground(Color.WHITE));
            timer.setRepeats(false);
            timer.start();
        });

        return btn;
    }
    
    private void updateTotal(DefaultTableModel model, JLabel lblSubTotal, JLabel lblKMDiscount, JLabel lblFinalTotal) {
        long subtotal = 0;
        for (int i = 0; i < model.getRowCount(); i++) {
            subtotal += Long.parseLong(model.getValueAt(i, 4).toString());
        }
        long discount = getGiamGiaHD(subtotal);
        long finalTotal = Math.max(0, subtotal - discount);

        lblSubTotal.setText(String.format("%,d VNĐ", subtotal));
        if (discount > 0) {
            lblKMDiscount.setText("- " + String.format("%,d VNĐ", discount));
            lblKMDiscount.setForeground(new Color(40, 167, 69));
        } else {
            lblKMDiscount.setText("Không có");
            lblKMDiscount.setForeground(new Color(108, 117, 125));
        }
        lblFinalTotal.setText(String.format("%,d VNĐ", finalTotal));
    }

    /** Kiểm tra chương trình KM còn hiệu lực (trangThai=true và trong thời hạn) */
    private boolean isKMActive(int kmId) {
        if (ChuongTrinhKhuyenMaiBUS.dskm == null) return false;
        
        // Tạo bộ lọc chỉ lấy Năm, Tháng, Ngày
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyyMMdd");
        int todayInt = Integer.parseInt(sdf.format(new java.util.Date()));
        
        for (ChuongTrinhKhuyenMaiDTO km : ChuongTrinhKhuyenMaiBUS.dskm) {
            if (km.getId() == kmId && km.isTrangThai()) {
                if (km.getNgayBatDau() != null && km.getNgayKetThuc() != null) {
                    int startInt = Integer.parseInt(sdf.format(km.getNgayBatDau()));
                    int endInt = Integer.parseInt(sdf.format(km.getNgayKetThuc()));
                    return todayInt >= startInt && todayInt <= endInt;
                }
                return true;
            }
        }
        return false;
    }

    /** Trả về số tiền giảm của sản phẩm (0 nếu không có KM) */
    private long getGiamGiaSP(int spId) {
        if (ChuongTrinhKhuyenMaiSpBUS.dskmsp == null) return 0;
        for (ChuongTrinhKhuyenMaiSpDTO kmsp : ChuongTrinhKhuyenMaiSpBUS.dskmsp) {
            if (kmsp.getSanPhamId() == spId && isKMActive(kmsp.getChuongTrinhKhuyenMaiId())) {
                return kmsp.getGiaTriGiam();
            }
        }
        return 0;
    }

    /** Trả về mức giảm giá hóa đơn cao nhất áp dụng được */
    private long getGiamGiaHD(long tongTien) {
        if (ChuongTrinhKhuyenMaiHDBUS.dskmhd == null) return 0;
        long maxGiam = 0;
        for (ChuongTrinhKhuyenMaiHDDTO kmhd : ChuongTrinhKhuyenMaiHDBUS.dskmhd) {
            if (isKMActive(kmhd.getChuongTrinhKhuyenMaiId()) && tongTien >= kmhd.getSoTienHd()) {
                maxGiam = Math.max(maxGiam, kmhd.getGiaTriGiam());
            }
        }
        return maxGiam;
    }

    private void initMainContent() {
        pnlMainContent = new JPanel(cardLayout);
        pnlMainContent.add(creatTongQuanPanel(),"Trang Tổng Quan");
        pnlMainContent.add(new QuanLySanPhamGUI(),"Quản Lý Sản Phẩm");
        pnlMainContent.add(new LoaiSanPhamGUI(), "Loại Sản Phẩm");
        pnlMainContent.add(new NhaCungCapGUI(), "Quản Lý Nhà Cung Cấp");
        pnlMainContent.add(new HangSanXuatGUI(), "Quản Lý Hãng Sản Xuất");
        pnlMainContent.add(new QuanLyKhachHangGUI(), "Quản Lý Khách Hàng");
        pnlMainContent.add(new QuanLyNhanVienGUI(), "Quản Lý Nhân Viên");
        pnlMainContent.add(new QuanLyDonHangGUI(), "Quản Lý Đơn Hàng");
        pnlMainContent.add(new QuanLyGiaBanGUI(), "Quản Lý Giá Bán");        
        pnlMainContent.add(new QuanLyNhapHangGUI(), "Quản Lý Nhập Hàng");        
        pnlMainContent.add(new KhuyenMaiGUI(), "Quản Lý Khuyến Mãi");        
        pnlMainContent.add(new ThongKeBaoCaoGUI(), "Thống Kê & Báo Cáo");
        add(pnlMainContent, BorderLayout.CENTER);
    }// Hàm hỗ trợ đăng ký khách hàng nhanh ngay tại form POS
    private void showFormDangKyKH_Nhanh(JDialog parentDlg, String sdt, int[] currentMaKH, JLabel lblTenKH) {
        JDialog dialog = new JDialog(parentDlg, "Đăng Ký Khách Hàng Nhanh", true);
        dialog.setSize(380, 280);
        dialog.setLocationRelativeTo(parentDlg);
        dialog.setLayout(new BorderLayout());

        JPanel pnlForm = new JPanel(new GridLayout(4, 2, 10, 15));
        pnlForm.setBorder(new EmptyBorder(20, 20, 10, 20));
        pnlForm.setBackground(Color.WHITE);

        JTextField txtHo = new JTextField();
        JTextField txtTen = new JTextField();
        JTextField txtSdt = new JTextField(sdt);
        JTextField txtDiaChi = new JTextField();

        pnlForm.add(new JLabel("Họ KH:")); pnlForm.add(txtHo);
        pnlForm.add(new JLabel("Tên KH *:")); pnlForm.add(txtTen);
        pnlForm.add(new JLabel("SĐT *:")); pnlForm.add(txtSdt);
        pnlForm.add(new JLabel("Địa Chỉ:")); pnlForm.add(txtDiaChi);

        JButton btnSave = new JButton("Đăng Ký");
        btnSave.setBackground(primaryColor);
        btnSave.setForeground(Color.WHITE);
        btnSave.addActionListener(e -> {
            try {
                if (txtTen.getText().trim().isEmpty() || txtSdt.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Tên và SĐT không được để trống!");
                    return;
                }
                DTO.KhachHangDTO newKh = new DTO.KhachHangDTO();
                newKh.setHoKH(txtHo.getText().trim());
                newKh.setTenKH(txtTen.getText().trim());
                newKh.setSdt(txtSdt.getText().trim());
                newKh.setDiaChi(txtDiaChi.getText().trim());

                KhachHangBUS bus = new KhachHangBUS();
                bus.themKH(newKh); 
                
                // Cập nhật lại danh sách và lấy mã KH vừa tạo gắn ngay vào Form Thanh Toán
                bus.docDSKH();
                if (bus.dskh != null && !bus.dskh.isEmpty()) {
                    DTO.KhachHangDTO createdKh = bus.dskh.get(bus.dskh.size() - 1); 
                    currentMaKH[0] = createdKh.getMaKH();
                    lblTenKH.setText(createdKh.getHoKH() + " " + createdKh.getTenKH());
                }
                
                JOptionPane.showMessageDialog(dialog, "Đăng ký thành công!");
                dialog.dispose();
            } catch (Exception ex) { 
                JOptionPane.showMessageDialog(dialog, "Lỗi đăng ký!"); 
            }
        });

        JPanel pnlBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlBtn.setBackground(Color.WHITE);
        pnlBtn.setBorder(new EmptyBorder(0, 0, 10, 20));
        pnlBtn.add(btnSave);

        dialog.add(pnlForm, BorderLayout.CENTER);
        dialog.add(pnlBtn, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); } catch (Exception e) {}
        SwingUtilities.invokeLater(() -> new SieuThiMiniGUI().setVisible(true));
    }
}