package SieuThiMiniGUI;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

import SieuThiMiniBUS.ThongKeBaoCaoBUS;

import java.awt.*;

public class ThongKeBaoCaoGUI extends JPanel {
    private ThongKeBaoCaoBUS tKeBaoCaoBUS = new ThongKeBaoCaoBUS();
    
    // Bootstrap-inspired colors
    private final Color primaryColor   = new Color(0, 123, 255);      // Blue
    private final Color dangerColor    = new Color(220, 53, 69);      // Red
    private final Color successColor   = new Color(40, 167, 69);      // Green
    private final Color warningColor   = new Color(255, 193, 7);      // Yellow/Orange
    private final Color purpleColor    = new Color(111, 66, 193);     // Purple
    private Color secondaryColor = new Color(108, 117, 125);
    private Color bgColor = new Color(244, 246, 249);
    private Font fontTitle = new Font("Segoe UI", Font.BOLD, 24);

    public ThongKeBaoCaoGUI() {
        this.setLayout(new BorderLayout(20, 20));
        this.setBackground(bgColor);
        this.setBorder(new EmptyBorder(20, 25, 20, 25));

        // Title
        JLabel lblTitle = new JLabel("Thống Kê & Báo Cáo");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(40, 40, 40));
        add(lblTitle, BorderLayout.NORTH);

        // Tabbed pane
        JTabbedPane tabs = new JTabbedPane(JTabbedPane.TOP);
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabs.setBackground(bgColor);
        tabs.addTab("  Doanh Thu & Chi Phí  ", buildDoanhThuTab());
        tabs.addTab("  Theo Khách Hàng  ",     buildKhachHangTab());
        tabs.addTab("  Theo Nhân Viên  ",       buildNhanVienTab());
        tabs.addTab("  Theo Sản Phẩm  ",        buildSanPhamTab());

        add(tabs, BorderLayout.CENTER);
    }

    // =========================================================================
    //  TAB 1 – DOANH THU & CHI PHÍ
    // =========================================================================
    private JPanel buildDoanhThuTab() {
        JPanel root = new JPanel(new BorderLayout(0, 14));
        root.setBackground(bgColor);
        root.setBorder(new EmptyBorder(14, 0, 0, 0));
        
        // --- Filter bar ---
        JPanel filterCard = buildCard();
        filterCard.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 10));

        filterCard.add(label("Kỳ thống kê:"));
        JComboBox<String> cboLoaiKy = new JComboBox<>(new String[]{"Tháng", "Quý", "Năm"});
        styleCombo(cboLoaiKy);
        filterCard.add(cboLoaiKy);

        filterCard.add(label("Năm:"));
        JComboBox<String> cboNam = new JComboBox<>(new String[]{"2026", "2025", "2024", "2023"});
        styleCombo(cboNam);
        filterCard.add(cboNam);

        filterCard.add(label("Tháng/Quý:"));
        JComboBox<String> cboKy = new JComboBox<>(new String[]{
            "1","2","3","4","5","6","7","8","9","10","11","12"
        });
        styleCombo(cboKy);
        filterCard.add(cboKy);

        // toggle ky combo khi đổi loại kỳ
        cboLoaiKy.addActionListener(e -> {
            String sel = (String) cboLoaiKy.getSelectedItem();
            if ("Quý".equals(sel)) {
                cboKy.setModel(new DefaultComboBoxModel<>(new String[]{"Q1","Q2","Q3","Q4"}));
                cboKy.setEnabled(true);
            } else if ("Năm".equals(sel)) {
                cboKy.setEnabled(false);
            } else {
                cboKy.setModel(new DefaultComboBoxModel<>(new String[]{
                    "1","2","3","4","5","6","7","8","9","10","11","12"}));
                cboKy.setEnabled(true);
            }
        });

        JButton btnFilter = createBtn("Thống Kê", primaryColor);
        btnFilter.setPreferredSize(new Dimension(120, 36));
        filterCard.add(btnFilter);

        JButton btnExport = createBtn("Xuất Excel", successColor);
        btnExport.setPreferredSize(new Dimension(120, 36));
        filterCard.add(btnExport);

        root.add(filterCard, BorderLayout.NORTH);

        // --- Center: summary cards + table ---
        JPanel center = new JPanel(new BorderLayout(0, 14));
        center.setBackground(bgColor);

        // Lấy dữ liệu từ BUS
        long doanhThu = tKeBaoCaoBUS.tongDoanhThu();
        long phiNhap = tKeBaoCaoBUS.tongPhiNhap();
        long loiNhuan = tKeBaoCaoBUS.loiNhuanUocTinh();

        // Summary cards
        JPanel pnlCards = new JPanel(new GridLayout(1, 3, 14, 0));
        pnlCards.setOpaque(false);
        pnlCards.add(createSummaryCard("Tổng Doanh Thu Bán", String.format("%,d đ", doanhThu), successColor, "Từ đơn hàng bán ra"));
        pnlCards.add(createSummaryCard("Tổng Chi Phí Nhập", String.format("%,d đ", phiNhap), dangerColor, "Từ phiếu nhập hàng"));
        pnlCards.add(createSummaryCard("Lợi Nhuận Ước Tính", String.format("%,d đ", loiNhuan), purpleColor, "Doanh thu - Chi phí"));
        center.add(pnlCards, BorderLayout.NORTH);

        // Table
        JPanel tableCard = buildCard();
        tableCard.setLayout(new BorderLayout(0, 10));
        JLabel lblSub = sectionLabel("Chi Tiết Theo Kỳ");
        tableCard.add(lblSub, BorderLayout.NORTH);

        String[] cols = {"Kỳ", "Doanh Thu Bán (đ)", "Chi Phí Nhập (đ)", "Lợi Nhuận (đ)", "Tăng Trưởng"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        // LẤY DỮ LIỆU THẬT TỪ BUS (Mặc định năm 2026 theo hình ảnh của bạn)
        java.util.List<Object[]> dsChiTiet = tKeBaoCaoBUS.getChiTietThang(2026); 
        for (Object[] row : dsChiTiet) {
            model.addRow(new Object[]{
                row[0],
                String.format("%,d", (long)row[1]),
                String.format("%,d", (long)row[2]),
                String.format("%,d", (long)row[3]),
                row[4]
            });
        }        
        JTable tbl = new JTable(model);
        styleTable(tbl);

        DefaultTableCellRenderer rightAlign = new DefaultTableCellRenderer();
        rightAlign.setHorizontalAlignment(JLabel.RIGHT);
        for (int c = 1; c <= 3; c++) tbl.getColumnModel().getColumn(c).setCellRenderer(rightAlign);

        tbl.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val, boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                setHorizontalAlignment(JLabel.CENTER);
                String v = val == null ? "" : val.toString();
                setForeground(v.startsWith("+") ? successColor : dangerColor);
                setFont(getFont().deriveFont(Font.BOLD));
                return this;
            }
        });

        tableCard.add(buildScroll(tbl), BorderLayout.CENTER);
        tableCard.add(buildSumBar("Tổng doanh thu:", String.format("%,d đ", doanhThu), successColor), BorderLayout.SOUTH);
        center.add(tableCard, BorderLayout.CENTER);

        root.add(center, BorderLayout.CENTER);
        return root;
    }

    // =========================================================================
    //  TAB 2 – THEO KHÁCH HÀNG
    // =========================================================================
    private JPanel buildKhachHangTab() {
        JPanel root = new JPanel(new BorderLayout(0, 14));
        root.setBackground(bgColor);
        root.setBorder(new EmptyBorder(14, 0, 0, 0));

        // Filter
        JPanel filterCard = buildCard();
        filterCard.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 10));
        filterCard.add(label("Năm:"));
        JComboBox<String> cboNam = new JComboBox<>(new String[]{"2026","2025","2024"});
        styleCombo(cboNam); filterCard.add(cboNam);
        filterCard.add(label("Tháng:"));
        JComboBox<String> cboThang = new JComboBox<>(new String[]{"Tất cả","1","2","3","4","5","6","7","8","9","10","11","12"});
        styleCombo(cboThang); filterCard.add(cboThang);
        filterCard.add(label("Tìm KH:"));
        JTextField txtSearch = styledField(14);
        txtSearch.setPreferredSize(new Dimension(160, 36));
        filterCard.add(txtSearch);
        JButton btn = createBtn("Thống Kê", primaryColor);
        btn.setPreferredSize(new Dimension(110, 36));
        filterCard.add(btn);
        root.add(filterCard, BorderLayout.NORTH);

        // --- SUMMARY CARDS (Đã cập nhật dữ liệu thật) ---
        JPanel center = new JPanel(new BorderLayout(0, 14));
        center.setBackground(bgColor);

        int tongKH = tKeBaoCaoBUS.tongSoKhachHang();
        int khDaMua = tKeBaoCaoBUS.tongKhachHangDaMua();
        long tongDoanhThu = tKeBaoCaoBUS.tongDoanhThu();

        JPanel cards = new JPanel(new GridLayout(1, 3, 14, 0));
        cards.setOpaque(false);
        cards.add(createSummaryCard("Tổng Khách Hàng", tongKH + " KH", primaryColor, "Có trong hệ thống"));
        cards.add(createSummaryCard("Khách Đã Mua", khDaMua + " KH", successColor, "Có phát sinh giao dịch"));
        cards.add(createSummaryCard("Tổng Doanh Thu", String.format("%,d đ", tongDoanhThu), warningColor, "Tất cả khách hàng"));
        center.add(cards, BorderLayout.NORTH);

        // --- TABLE (Đã cập nhật dữ liệu thật) ---
        JPanel tableCard = buildCard();
        tableCard.setLayout(new BorderLayout(0, 10));
        tableCard.add(sectionLabel("Danh Sách Khách Hàng Theo Doanh Thu"), BorderLayout.NORTH);

        String[] cols = {"Xếp Hạng", "Mã KH", "Tên Khách Hàng", "Số Đơn Hàng", "Tổng Chi Tiêu (đ)", "Trung Bình/Đơn (đ)"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        // Lấy danh sách từ DB và đổ vào bảng
        java.util.List<Object[]> topKH = tKeBaoCaoBUS.getTopKhachHang();
        for(Object[] row : topKH) {
            long chiTieu = (long) row[4];
            long trungBinh = (long) row[5];
            model.addRow(new Object[]{
                row[0], row[1], row[2], row[3], String.format("%,d", chiTieu), String.format("%,d", trungBinh)
            });
        }

        JTable tbl = new JTable(model);
        styleTable(tbl);
        DefaultTableCellRenderer center1 = new DefaultTableCellRenderer();
        center1.setHorizontalAlignment(JLabel.CENTER);
        tbl.getColumnModel().getColumn(0).setCellRenderer(center1);
        DefaultTableCellRenderer right = new DefaultTableCellRenderer();
        right.setHorizontalAlignment(JLabel.RIGHT);
        tbl.getColumnModel().getColumn(3).setCellRenderer(center1);
        tbl.getColumnModel().getColumn(4).setCellRenderer(right);
        tbl.getColumnModel().getColumn(5).setCellRenderer(right);

        tableCard.add(buildScroll(tbl), BorderLayout.CENTER);
        tableCard.add(buildSumBar("Tổng doanh thu:", String.format("%,d đ", tongDoanhThu), warningColor), BorderLayout.SOUTH);
        center.add(tableCard, BorderLayout.CENTER);
        root.add(center, BorderLayout.CENTER);
        
        return root;
    }

    // =========================================================================
    //  TAB 3 – THEO NHÂN VIÊN
    // =========================================================================
    private JPanel buildNhanVienTab() {
        JPanel root = new JPanel(new BorderLayout(0, 14));
        root.setBackground(bgColor);
        root.setBorder(new EmptyBorder(14, 0, 0, 0));

        // Filter
        JPanel filterCard = buildCard();
        filterCard.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 10));
        filterCard.add(label("Năm:"));
        JComboBox<String> cboNam = new JComboBox<>(new String[]{"2026","2025","2024"});
        styleCombo(cboNam); filterCard.add(cboNam);
        filterCard.add(label("Tháng:"));
        JComboBox<String> cboThang = new JComboBox<>(new String[]{"Tất cả","1","2","3","4","5","6","7","8","9","10","11","12"});
        styleCombo(cboThang); filterCard.add(cboThang);
        filterCard.add(label("Tìm NV:"));
        JTextField txtSearch = styledField(14);
        txtSearch.setPreferredSize(new Dimension(160, 36));
        filterCard.add(txtSearch);
        JButton btn = createBtn("Thống Kê", primaryColor);
        btn.setPreferredSize(new Dimension(110, 36));
        filterCard.add(btn);
        root.add(filterCard, BorderLayout.NORTH);

        // Summary cards
        JPanel center = new JPanel(new BorderLayout(0, 14));
        center.setBackground(bgColor);

        // Lấy dữ liệu thật từ BUS
        int tongNV = tKeBaoCaoBUS.tongNhanVien();
        String nvXuatSac = tKeBaoCaoBUS.nhanVienXuatSac();
        long dtTrungBinh = tKeBaoCaoBUS.doanhThuTrungBinhNhanVien();

        JPanel cards = new JPanel(new GridLayout(1, 3, 14, 0));
        cards.setOpaque(false);
        // Thay "24 NV" bằng (tongNV + " NV")
        cards.add(createSummaryCard("Tổng Nhân Viên", tongNV + " NV", primaryColor,  "Đang hoạt động"));
        cards.add(createSummaryCard("NV Xuất Sắc", nvXuatSac, successColor,  "Doanh thu cao nhất"));
        cards.add(createSummaryCard("Doanh Thu Trung Bình", String.format("%,d đ", dtTrungBinh), purpleColor,   "Trung bình/Nhân viên"));
        
        center.add(cards, BorderLayout.NORTH);
        // Table
        JPanel tableCard = buildCard();
        tableCard.setLayout(new BorderLayout(0, 10));
        tableCard.add(sectionLabel("Hiệu Suất Nhân Viên"), BorderLayout.NORTH);

        String[] cols = {"Xếp Hạng", "Mã NV", "Tên Nhân Viên", "Vị Trí", "Số Đơn Phụ Trách", "Doanh Thu (đ)", "Tỷ Lệ %"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        // --- ĐỔ DỮ LIỆU THẬT TỪ DATABASE ---
        long tongDoanhThuAll = tKeBaoCaoBUS.tongDoanhThu();
        java.util.List<Object[]> topNV = tKeBaoCaoBUS.getTopNhanVien();
        
        for(Object[] row : topNV) {
            long dt = (long) row[5];
            // Tính phần trăm đóng góp của nhân viên so với tổng doanh thu
            String tyLe = tongDoanhThuAll > 0 ? String.format("%.1f%%", (double)dt / tongDoanhThuAll * 100) : "0%";
            model.addRow(new Object[]{
                row[0], row[1], row[2], row[3], row[4], String.format("%,d", dt), tyLe
            });
        }
        // -----------------------------------
        
        JTable tbl = new JTable(model);
        styleTable(tbl);        
        DefaultTableCellRenderer centerR = new DefaultTableCellRenderer();
        centerR.setHorizontalAlignment(JLabel.CENTER);
        DefaultTableCellRenderer rightR = new DefaultTableCellRenderer();
        rightR.setHorizontalAlignment(JLabel.RIGHT);
        tbl.getColumnModel().getColumn(0).setCellRenderer(centerR);
        tbl.getColumnModel().getColumn(4).setCellRenderer(centerR);
        tbl.getColumnModel().getColumn(5).setCellRenderer(rightR);
        tbl.getColumnModel().getColumn(6).setCellRenderer(centerR);

        tableCard.add(buildScroll(tbl), BorderLayout.CENTER);
        tableCard.add(buildSumBar("Tổng doanh thu:", String.format("%,d đ", tKeBaoCaoBUS.tongDoanhThu()), purpleColor), BorderLayout.SOUTH);
        center.add(tableCard, BorderLayout.CENTER);
        root.add(center, BorderLayout.CENTER);
        return root;
    }

    // =========================================================================
    //  TAB 4 – THEO SẢN PHẨM
    // =========================================================================
    private JPanel buildSanPhamTab() {
        JPanel root = new JPanel(new BorderLayout(0, 14));
        root.setBackground(bgColor);
        root.setBorder(new EmptyBorder(14, 0, 0, 0));

        // Filter
        JPanel filterCard = buildCard();
        filterCard.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 10));
        filterCard.add(label("Năm:"));
        JComboBox<String> cboNam = new JComboBox<>(new String[]{"2026","2025","2024"});
        styleCombo(cboNam); filterCard.add(cboNam);
        filterCard.add(label("Tháng:"));
        JComboBox<String> cboThang = new JComboBox<>(new String[]{"Tất cả","1","2","3","4","5","6","7","8","9","10","11","12"});
        styleCombo(cboThang); filterCard.add(cboThang);
        filterCard.add(label("Danh mục:"));
        JComboBox<String> cboDM = new JComboBox<>(new String[]{"Tất cả","Thực Phẩm","Đồ Uống","Hóa Mỹ Phẩm","Gia Dụng"});
        styleCombo(cboDM); filterCard.add(cboDM);
        filterCard.add(label("Tìm SP:"));
        JTextField txtSearch = styledField(14);
        txtSearch.setPreferredSize(new Dimension(150, 36));
        filterCard.add(txtSearch);
        JButton btn = createBtn("Thống Kê", primaryColor);
        btn.setPreferredSize(new Dimension(110, 36));
        filterCard.add(btn);
        root.add(filterCard, BorderLayout.NORTH);

        // --- SUMMARY CARDS (Đã gọi data thật) ---
        JPanel center = new JPanel(new BorderLayout(0, 14));
        center.setBackground(bgColor);

        int tongSP = tKeBaoCaoBUS.tongSanPham();
        String spBanChay = tKeBaoCaoBUS.sanPhamBanChay();
        int spSapHet = tKeBaoCaoBUS.spSapHetHang();
        long tongDoanhThu = tKeBaoCaoBUS.tongDoanhThu();

        JPanel cards = new JPanel(new GridLayout(1, 4, 14, 0));
        cards.setOpaque(false);
        cards.add(createSummaryCard("Tổng Sản Phẩm", tongSP + " SP", primaryColor,  "Có trong kho"));
        cards.add(createSummaryCard("SP Bán Chạy", spBanChay, successColor,  "Nhiều nhất"));
        cards.add(createSummaryCard("Sắp Hết Hàng", spSapHet + " SP", dangerColor,   "Dưới 15 sản phẩm"));
        cards.add(createSummaryCard("Tổng Doanh Thu", String.format("%,d đ", tongDoanhThu), warningColor,  "Từ tất cả SP"));
        center.add(cards, BorderLayout.NORTH);

        // --- TABLE (Đã gọi data thật) ---
        JPanel tableCard = buildCard();
        tableCard.setLayout(new BorderLayout(0, 10));
        tableCard.add(sectionLabel("Top Sản Phẩm Theo Doanh Thu"), BorderLayout.NORTH);

        String[] cols = {"Xếp Hạng", "Mã SP", "Tên Sản Phẩm", "Danh Mục", "SL Bán Ra", "Đơn Giá (đ)", "Doanh Thu (đ)"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        // Đổ dữ liệu thật vào bảng
        java.util.List<Object[]> topSP = tKeBaoCaoBUS.getTopSanPham();
        for(Object[] row : topSP) {
            long donGia = (long) row[5];
            long dt = (long) row[6];
            model.addRow(new Object[]{
                row[0], row[1], row[2], row[3], row[4], 
                String.format("%,d", donGia), String.format("%,d", dt)
            });
        }

        JTable tbl = new JTable(model);
        styleTable(tbl);
        DefaultTableCellRenderer centerR = new DefaultTableCellRenderer();
        centerR.setHorizontalAlignment(JLabel.CENTER);
        DefaultTableCellRenderer rightR = new DefaultTableCellRenderer();
        rightR.setHorizontalAlignment(JLabel.RIGHT);
        
        tbl.getColumnModel().getColumn(0).setCellRenderer(centerR);
        tbl.getColumnModel().getColumn(4).setCellRenderer(centerR);
        tbl.getColumnModel().getColumn(5).setCellRenderer(rightR);
        tbl.getColumnModel().getColumn(6).setCellRenderer(rightR);

        // Chỉnh lại độ rộng các cột cho đẹp mắt hơn
        tbl.getColumnModel().getColumn(2).setPreferredWidth(180); 
        tbl.getColumnModel().getColumn(3).setPreferredWidth(120);

        tableCard.add(buildScroll(tbl), BorderLayout.CENTER);
        tableCard.add(buildSumBar("Tổng doanh thu sản phẩm:", String.format("%,d đ", tongDoanhThu), warningColor), BorderLayout.SOUTH);
        center.add(tableCard, BorderLayout.CENTER);
        root.add(center, BorderLayout.CENTER);
        
        return root;
    }

    // =========================================================================
    //  SHARED UI HELPERS
    // =========================================================================

    private JPanel createSummaryCard(String title, String value, Color valueColor, String subtitle) {
        JPanel card = new JPanel(new BorderLayout(4, 4));
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
                new LineBorder(new Color(230, 230, 230), 1),
                new EmptyBorder(16, 16, 16, 16)));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblTitle.setForeground(secondaryColor);

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblValue.setForeground(valueColor);

        JLabel lblSub = new JLabel(subtitle);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSub.setForeground(new Color(160, 160, 160));

        JPanel valPanel = new JPanel(new BorderLayout(2, 2));
        valPanel.setOpaque(false);
        valPanel.add(lblValue, BorderLayout.NORTH);
        valPanel.add(lblSub, BorderLayout.CENTER);

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(valPanel, BorderLayout.CENTER);
        return card;
    }

    private JPanel buildCard() {
        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
                new LineBorder(new Color(230, 230, 230), 1),
                new EmptyBorder(14, 14, 14, 14)));
        return card;
    }

    private JPanel buildSumBar(String labelText, String value, Color valueColor) {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 6));
        bar.setBackground(new Color(248, 249, 250));
        bar.setBorder(new MatteBorder(1, 0, 0, 0, new Color(220, 220, 220)));
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JLabel val = new JLabel(value);
        val.setFont(new Font("Segoe UI", Font.BOLD, 16));
        val.setForeground(valueColor);
        bar.add(lbl); bar.add(val);
        return bar;
    }

    private JScrollPane buildScroll(JTable tbl) {
        JScrollPane scroll = new JScrollPane(tbl);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Color.WHITE);
        return scroll;
    }

    private JLabel sectionLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lbl.setForeground(primaryColor);
        return lbl;
    }

    private JLabel label(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return lbl;
    }

    private void styleCombo(JComboBox<String> cb) {
        cb.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cb.setPreferredSize(new Dimension(110, 36));
    }

    private JTextField styledField(int cols) {
        JTextField f = new JTextField(cols);
        f.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        f.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1),
                new EmptyBorder(4, 8, 4, 8)));
        return f;
    }

    private void styleTable(JTable t) {
        t.setRowHeight(40);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        t.setGridColor(new Color(245, 245, 245));
        t.setShowVerticalLines(false);
        t.setSelectionBackground(new Color(240, 247, 255));
        t.setSelectionForeground(Color.BLACK);
        t.getTableHeader().setReorderingAllowed(false);
        JTableHeader header = t.getTableHeader();
        header.setPreferredSize(new Dimension(0, 42));
        header.setBackground(new Color(250, 251, 252));
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setForeground(secondaryColor);
        ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(JLabel.LEFT);
    }

    private JButton createBtn(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}