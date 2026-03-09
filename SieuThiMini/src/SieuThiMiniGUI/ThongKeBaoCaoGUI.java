package SieuThiMiniGUI;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;

public class ThongKeBaoCaoGUI extends JPanel {
    
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

        // Summary cards
        JPanel pnlCards = new JPanel(new GridLayout(1, 3, 14, 0));
        pnlCards.setOpaque(false);
        pnlCards.add(createSummaryCard("Tổng Doanh Thu Bán", "350,500,000 đ", successColor,
                "Từ đơn hàng bán ra"));
        pnlCards.add(createSummaryCard("Tổng Chi Phí Nhập", "198,200,000 đ", dangerColor,
                "Từ phiếu nhập hàng"));
        pnlCards.add(createSummaryCard("Lợi Nhuận Ước Tính", "152,300,000 đ", purpleColor,
                "Doanh thu - Chi phí"));
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
        model.addRow(new Object[]{"Tháng 1/2026", "42,000,000",  "24,000,000", "18,000,000", "+5.2%"});
        model.addRow(new Object[]{"Tháng 2/2026", "38,500,000",  "21,500,000", "17,000,000", "-8.3%"});
        model.addRow(new Object[]{"Tháng 3/2026", "51,000,000",  "29,000,000", "22,000,000", "+32.5%"});
        model.addRow(new Object[]{"Tháng 4/2026", "47,800,000",  "26,800,000", "21,000,000", "-6.3%"});
        model.addRow(new Object[]{"Tháng 5/2026", "55,200,000",  "31,200,000", "24,000,000", "+15.5%"});
        model.addRow(new Object[]{"Tháng 6/2026", "62,000,000",  "35,000,000", "27,000,000", "+12.3%"});

        JTable tbl = new JTable(model);
        styleTable(tbl);

        // right-align numeric columns
        DefaultTableCellRenderer rightAlign = new DefaultTableCellRenderer();
        rightAlign.setHorizontalAlignment(JLabel.RIGHT);
        for (int c = 1; c <= 3; c++) tbl.getColumnModel().getColumn(c).setCellRenderer(rightAlign);

        // color "Tăng Trưởng" column
        tbl.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                setHorizontalAlignment(JLabel.CENTER);
                String v = val == null ? "" : val.toString();
                setForeground(v.startsWith("+") ? successColor : dangerColor);
                setFont(getFont().deriveFont(Font.BOLD));
                return this;
            }
        });

        JScrollPane scroll = buildScroll(tbl);

        // summary bar
        JPanel sumBar = buildSumBar("Tổng doanh thu:", "296,500,000 đ", successColor);
        tableCard.add(scroll, BorderLayout.CENTER);
        tableCard.add(sumBar, BorderLayout.SOUTH);
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
        JComboBox<String> cboThang = new JComboBox<>(new String[]{
            "Tất cả","1","2","3","4","5","6","7","8","9","10","11","12"});
        styleCombo(cboThang); filterCard.add(cboThang);
        filterCard.add(label("Tìm KH:"));
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

        JPanel cards = new JPanel(new GridLayout(1, 3, 14, 0));
        cards.setOpaque(false);
        cards.add(createSummaryCard("Tổng Khách Hàng",   "1,240 KH",       primaryColor, "Đã mua hàng"));
        cards.add(createSummaryCard("Khách Hàng Mới",     "87 KH",          successColor, "Trong kỳ"));
        cards.add(createSummaryCard("Doanh Thu Từ KH",    "350,500,000 đ",  warningColor, "Tổng cộng"));
        center.add(cards, BorderLayout.NORTH);

        // Table
        JPanel tableCard = buildCard();
        tableCard.setLayout(new BorderLayout(0, 10));
        tableCard.add(sectionLabel("Danh Sách Khách Hàng Theo Doanh Thu"), BorderLayout.NORTH);

        String[] cols = {"Xếp Hạng", "Mã KH", "Tên Khách Hàng", "Số Đơn Hàng", "Tổng Chi Tiêu (đ)", "Trung Bình/Đơn (đ)"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        model.addRow(new Object[]{"#1", "KH012", "Nguyễn Thị Lan",   "28", "15,400,000", "550,000"});
        model.addRow(new Object[]{"#2", "KH034", "Trần Minh Đức",    "22", "12,100,000", "550,000"});
        model.addRow(new Object[]{"#3", "KH007", "Lê Thị Hoa",       "19", "10,450,000", "550,000"});
        model.addRow(new Object[]{"#4", "KH091", "Phạm Văn Khoa",    "15",  "8,250,000", "550,000"});
        model.addRow(new Object[]{"#5", "KH055", "Hoàng Thị Mai",    "12",  "6,600,000", "550,000"});

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
        tableCard.add(buildSumBar("Tổng doanh thu từ KH:", "350,500,000 đ", warningColor), BorderLayout.SOUTH);
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
        JComboBox<String> cboThang = new JComboBox<>(new String[]{
            "Tất cả","1","2","3","4","5","6","7","8","9","10","11","12"});
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

        JPanel cards = new JPanel(new GridLayout(1, 3, 14, 0));
        cards.setOpaque(false);
        cards.add(createSummaryCard("Tổng Nhân Viên",       "24 NV",          primaryColor,  "Đang hoạt động"));
        cards.add(createSummaryCard("NV Xuất Sắc",          "Nguyễn Văn An",  successColor,  "Doanh thu cao nhất"));
        cards.add(createSummaryCard("Doanh Thu Trung Bình", "14,600,000 đ",   purpleColor,   "Mỗi nhân viên/tháng"));
        center.add(cards, BorderLayout.NORTH);

        // Table
        JPanel tableCard = buildCard();
        tableCard.setLayout(new BorderLayout(0, 10));
        tableCard.add(sectionLabel("Hiệu Suất Nhân Viên"), BorderLayout.NORTH);

        String[] cols = {"Xếp Hạng", "Mã NV", "Tên Nhân Viên", "Vị Trí", "Số Đơn Phụ Trách", "Doanh Thu (đ)", "Tỷ Lệ %"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        model.addRow(new Object[]{"#1", "NV001", "Nguyễn Văn An",   "Thu Ngân",  "156", "85,800,000", "24.5%"});
        model.addRow(new Object[]{"#2", "NV003", "Trần Thị Bình",   "Thu Ngân",  "142", "78,100,000", "22.3%"});
        model.addRow(new Object[]{"#3", "NV007", "Lê Văn Cường",    "Bán Hàng",  "128", "70,400,000", "20.1%"});
        model.addRow(new Object[]{"#4", "NV012", "Phạm Thị Dung",   "Bán Hàng",  "105", "57,750,000", "16.5%"});
        model.addRow(new Object[]{"#5", "NV018", "Hoàng Văn Em",    "Kho",        "89", "48,950,000", "14.0%"});

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
        tableCard.add(buildSumBar("Tổng doanh thu nhân viên:", "341,000,000 đ", purpleColor), BorderLayout.SOUTH);
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
        JComboBox<String> cboThang = new JComboBox<>(new String[]{
            "Tất cả","1","2","3","4","5","6","7","8","9","10","11","12"});
        styleCombo(cboThang); filterCard.add(cboThang);
        filterCard.add(label("Danh mục:"));
        JComboBox<String> cboDM = new JComboBox<>(new String[]{
            "Tất cả","Thực Phẩm","Đồ Uống","Hóa Mỹ Phẩm","Gia Dụng"});
        styleCombo(cboDM); filterCard.add(cboDM);
        filterCard.add(label("Tìm SP:"));
        JTextField txtSearch = styledField(14);
        txtSearch.setPreferredSize(new Dimension(150, 36));
        filterCard.add(txtSearch);
        JButton btn = createBtn("Thống Kê", primaryColor);
        btn.setPreferredSize(new Dimension(110, 36));
        filterCard.add(btn);
        root.add(filterCard, BorderLayout.NORTH);

        // Summary cards
        JPanel center = new JPanel(new BorderLayout(0, 14));
        center.setBackground(bgColor);

        JPanel cards = new JPanel(new GridLayout(1, 4, 14, 0));
        cards.setOpaque(false);
        cards.add(createSummaryCard("Tổng Sản Phẩm",    "342 SP",         primaryColor,  "Đang kinh doanh"));
        cards.add(createSummaryCard("SP Bán Chạy Nhất", "Dầu Gội Sunsilk",successColor,  "120 SP / tháng"));
        cards.add(createSummaryCard("SP Sắp Hết Hàng",  "15 SP",          dangerColor,   "Dưới mức tối thiểu"));
        cards.add(createSummaryCard("Doanh Thu SP",      "350,500,000 đ",  warningColor,  "Tổng cộng"));
        center.add(cards, BorderLayout.NORTH);

        // Table
        JPanel tableCard = buildCard();
        tableCard.setLayout(new BorderLayout(0, 10));
        tableCard.add(sectionLabel("Top Sản Phẩm Theo Doanh Thu"), BorderLayout.NORTH);

        String[] cols = {"Xếp Hạng", "Mã SP", "Tên Sản Phẩm", "Danh Mục", "SL Bán Ra", "Đơn Giá (đ)", "Doanh Thu (đ)"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        model.addRow(new Object[]{"#1", "SP002", "Dầu Gội Sunsilk",       "Hóa Mỹ Phẩm", "120", "55,000",  "6,600,000"});
        model.addRow(new Object[]{"#2", "SP015", "Nước Ngọt Pepsi 330ml", "Đồ Uống",      "310", "12,000",  "3,720,000"});
        model.addRow(new Object[]{"#3", "SP005", "Bánh Cosy",              "Thực Phẩm",    "95",  "45,000",  "4,275,000"});
        model.addRow(new Object[]{"#4", "SP031", "Sữa Vinamilk 1L",       "Đồ Uống",      "88",  "35,000",  "3,080,000"});
        model.addRow(new Object[]{"#5", "SP008", "Kem Đánh Răng P/S",     "Hóa Mỹ Phẩm", "76",  "30,000",  "2,280,000"});
        model.addRow(new Object[]{"#6", "SP022", "Gạo ST25 5kg",          "Thực Phẩm",    "60", "125,000",  "7,500,000"});
        model.addRow(new Object[]{"#7", "SP040", "Nước Mắm Chinsu",       "Thực Phẩm",    "55",  "28,000",  "1,540,000"});

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

        tableCard.add(buildScroll(tbl), BorderLayout.CENTER);
        tableCard.add(buildSumBar("Tổng doanh thu sản phẩm:", "350,500,000 đ", warningColor), BorderLayout.SOUTH);
        center.add(tableCard, BorderLayout.CENTER);
        root.add(center, BorderLayout.CENTER);
        return root;
    }

    // =========================================================================
    //  SHARED UI HELPERS
    // =========================================================================

    /** Card tổng quan với tiêu đề phụ */
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

    /** White card panel with border */
    private JPanel buildCard() {
        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
                new LineBorder(new Color(230, 230, 230), 1),
                new EmptyBorder(14, 14, 14, 14)));
        return card;
    }

    /** Summary bar at bottom of table */
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
