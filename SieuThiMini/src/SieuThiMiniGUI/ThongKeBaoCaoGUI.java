package SieuThiMiniGUI;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import SieuThiMiniBUS.ThongKeBaoCaoBUS;
import java.awt.*;
import java.util.ArrayList;

public class ThongKeBaoCaoGUI extends JPanel {
    private ThongKeBaoCaoBUS bus = new ThongKeBaoCaoBUS();
    
    private final Color primaryColor   = new Color(0, 123, 255);
    private final Color dangerColor    = new Color(220, 53, 69);
    private final Color successColor   = new Color(40, 167, 69);
    private final Color warningColor   = new Color(255, 193, 7);
    private final Color purpleColor    = new Color(111, 66, 193);
    private Color secondaryColor       = new Color(108, 117, 125);
    private Color bgColor              = new Color(244, 246, 249);
    
    private JTabbedPane tabs;
    private boolean isRefreshing = false;

    public ThongKeBaoCaoGUI() {
        this.setLayout(new BorderLayout(20, 20));
        this.setBackground(bgColor);
        this.setBorder(new EmptyBorder(20, 25, 20, 25));

        JLabel lblTitle = new JLabel("Thống Kê & Báo Cáo");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(40, 40, 40));
        add(lblTitle, BorderLayout.NORTH);

        tabs = new JTabbedPane(JTabbedPane.TOP);
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabs.setBackground(bgColor);
        
        tabs.addChangeListener(e -> {
            if (!isRefreshing && tabs.getSelectedIndex() != -1) refreshData();
        });

        refreshData();
        add(tabs, BorderLayout.CENTER);
    }

    public void refreshData() {
        isRefreshing = true;
        int selectedIndex = tabs.getSelectedIndex();
        tabs.removeAll();
        
        tabs.addTab("  Doanh Thu & Chi Phí  ", buildDoanhThuTab());
        tabs.addTab("  Theo Khách Hàng  ",     buildKhachHangTab());
        tabs.addTab("  Theo Nhân Viên  ",       buildNhanVienTab());
        tabs.addTab("  Theo Sản Phẩm  ",        buildSanPhamTab());
        
        if (selectedIndex >= 0 && selectedIndex < tabs.getTabCount()) {
            tabs.setSelectedIndex(selectedIndex);
        }
        isRefreshing = false;
    }

    // =========================================================================
    //  TAB 1 – DOANH THU & CHI PHÍ
    // =========================================================================
    private JPanel buildDoanhThuTab() {
        JPanel root = new JPanel(new BorderLayout(0, 14));
        root.setBackground(bgColor);
        root.setBorder(new EmptyBorder(14, 0, 0, 0));
        
        JPanel filterCard = buildCard();
        filterCard.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 10));

        filterCard.add(label("Kỳ thống kê:"));
        JComboBox<String> cboLoaiKy = new JComboBox<>(new String[]{"Năm", "Quý", "Tháng"});
        styleCombo(cboLoaiKy); filterCard.add(cboLoaiKy);

        filterCard.add(label("Năm:"));
        JComboBox<String> cboNam = new JComboBox<>(new String[]{"2026", "2025", "2024", "2023"});
        styleCombo(cboNam); filterCard.add(cboNam);

        filterCard.add(label("Tháng/Quý:"));
        JComboBox<String> cboKy = new JComboBox<>(new String[]{"1","2","3","4","5","6","7","8","9","10","11","12"});
        styleCombo(cboKy); filterCard.add(cboKy);

        cboLoaiKy.addActionListener(e -> {
            String sel = (String) cboLoaiKy.getSelectedItem();
            if ("Quý".equals(sel)) {
                cboKy.setModel(new DefaultComboBoxModel<>(new String[]{"Q1","Q2","Q3","Q4"}));
                cboKy.setEnabled(true);
            } else if ("Năm".equals(sel)) {
                cboKy.setEnabled(false);
            } else {
                cboKy.setModel(new DefaultComboBoxModel<>(new String[]{"1","2","3","4","5","6","7","8","9","10","11","12"}));
                cboKy.setEnabled(true);
            }
        });
        cboKy.setEnabled(false);
        JButton btnFilter = createBtn("Thống Kê", primaryColor);
        btnFilter.setPreferredSize(new Dimension(120, 36));
        filterCard.add(btnFilter);
        root.add(filterCard, BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(0, 14));
        center.setBackground(bgColor);

        JPanel pnlCards = new JPanel(new GridLayout(1, 3, 14, 0));
        pnlCards.setOpaque(false);
        JLabel lblTopDT = new JLabel("0 đ"); lblTopDT.setFont(new Font("Segoe UI", Font.BOLD, 20)); lblTopDT.setForeground(successColor);
        JLabel lblTopCP = new JLabel("0 đ"); lblTopCP.setFont(new Font("Segoe UI", Font.BOLD, 20)); lblTopCP.setForeground(dangerColor);
        JLabel lblTopLN = new JLabel("0 đ"); lblTopLN.setFont(new Font("Segoe UI", Font.BOLD, 20)); lblTopLN.setForeground(purpleColor);
        pnlCards.add(createCardTemplate("Tổng Doanh Thu Bán", lblTopDT, "Giai đoạn đang lọc"));
        pnlCards.add(createCardTemplate("Tổng Chi Phí Nhập", lblTopCP, "Giai đoạn đang lọc"));
        pnlCards.add(createCardTemplate("Lợi Nhuận Ước Tính", lblTopLN, "Doanh thu - Chi phí"));
        center.add(pnlCards, BorderLayout.NORTH);

        JPanel tableCard = buildCard();
        tableCard.setLayout(new BorderLayout(0, 10));
        tableCard.add(sectionLabel("Chi Tiết Theo Kỳ"), BorderLayout.NORTH);

        String[] cols = {"Kỳ", "Doanh Thu Bán (đ)", "Chi Phí Nhập (đ)", "Lợi Nhuận (đ)", "Tăng Trưởng"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) { @Override public boolean isCellEditable(int r, int c) { return false; } };
        JTable tbl = new JTable(model);
        styleTable(tbl);

        DefaultTableCellRenderer rightAlign = new DefaultTableCellRenderer();
        rightAlign.setHorizontalAlignment(JLabel.RIGHT);
        for (int c = 1; c <= 3; c++) tbl.getColumnModel().getColumn(c).setCellRenderer(rightAlign);

        tableCard.add(buildScroll(tbl), BorderLayout.CENTER);
        
        JLabel lblSumFooter = new JLabel("0 đ"); lblSumFooter.setFont(new Font("Segoe UI", Font.BOLD, 16)); lblSumFooter.setForeground(successColor);
        tableCard.add(buildSumBar("Tổng doanh thu hiển thị:", lblSumFooter), BorderLayout.SOUTH);
        center.add(tableCard, BorderLayout.CENTER);

// -- Xử lý lọc dữ liệu Tab 1 (Đã làm lại logic hiển thị Ngày/Tháng/Năm) --
Runnable loadTableData = () -> {
    model.setRowCount(0);
    try {
        int nam = Integer.parseInt(cboNam.getSelectedItem().toString());
        String loaiKy = cboLoaiKy.getSelectedItem().toString();
        String kySelected = cboKy.getSelectedItem() != null ? cboKy.getSelectedItem().toString() : "1";

        long sumDT = 0, sumCP = 0;

        if (loaiKy.equals("Tháng")) {
            // Nếu chọn Tháng -> Đổ dữ liệu từng NGÀY
            int thang = Integer.parseInt(kySelected);
            java.util.List<Object[]> ds = bus.getChiTietDoanhThu(loaiKy, nam, thang);
            for (Object[] row : ds) {
                model.addRow(new Object[]{ 
                    row[0] + "/" + thang + "/" + nam, 
                    String.format("%,d", (long)row[1]), 
                    String.format("%,d", (long)row[2]), 
                    String.format("%,d", (long)row[3]), 
                    row[4] 
                });
                sumDT += (long)row[1];
                sumCP += (long)row[2];
            }
        } else {
            // Nếu chọn Năm hoặc Quý -> Đổ dữ liệu từng THÁNG
            java.util.List<Object[]> ds = bus.getChiTietDoanhThu("Năm", nam, 0);
            for (Object[] row : ds) {
                int thangRow = Integer.parseInt(row[0].toString().replace("Tháng ", ""));
                boolean match = false;

                // Nếu chọn Năm thì lấy hết, nếu chọn Quý thì kiểm tra tháng đó thuộc quý nào
                if (loaiKy.equals("Năm")) match = true;
                else if (loaiKy.equals("Quý")) {
                    int q = Integer.parseInt(kySelected.replace("Q", ""));
                    if ((thangRow - 1) / 3 + 1 == q) match = true;
                }

                if (match) {
                    model.addRow(new Object[]{ 
                        row[0] + "/" + nam, 
                        String.format("%,d", (long)row[1]), 
                        String.format("%,d", (long)row[2]), 
                        String.format("%,d", (long)row[3]), 
                        row[4] 
                    });
                    sumDT += (long)row[1];
                    sumCP += (long)row[2];
                }
            }
        }
        
        // Cập nhật lại các Label Box ở phía trên để khớp với bảng
        lblTopDT.setText(String.format("%,d đ", sumDT));
        lblTopCP.setText(String.format("%,d đ", sumCP));
        lblTopLN.setText(String.format("%,d đ", sumDT - sumCP));
        lblSumFooter.setText(String.format("%,d đ", sumDT));
    } catch (Exception ex) { ex.printStackTrace(); }
};
        loadTableData.run();
        btnFilter.addActionListener(e -> loadTableData.run());

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

        JPanel filterCard = buildCard();
        filterCard.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 10));
        filterCard.add(label("Năm:")); JComboBox<String> cboNam = new JComboBox<>(new String[]{"Tất cả","2026","2025","2024"}); styleCombo(cboNam); filterCard.add(cboNam);
        filterCard.add(label("Tháng:")); JComboBox<String> cboThang = new JComboBox<>(new String[]{"Tất cả","1","2","3","4","5","6","7","8","9","10","11","12"}); styleCombo(cboThang); filterCard.add(cboThang);
        filterCard.add(label("Tìm KH:")); JTextField txtSearch = styledField(14); txtSearch.setPreferredSize(new Dimension(160, 36)); filterCard.add(txtSearch);
        JButton btn = createBtn("Tìm Kiếm", primaryColor); btn.setPreferredSize(new Dimension(110, 36)); filterCard.add(btn);
        root.add(filterCard, BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(0, 14));
        center.setBackground(bgColor);

        JPanel cards = new JPanel(new GridLayout(1, 3, 14, 0));
        cards.setOpaque(false);
        cards.add(createSummaryCard("Tổng Khách Hàng", bus.tongSoKhachHang() + " KH", primaryColor, "Có trong hệ thống"));
        cards.add(createSummaryCard("Khách Đã Mua", bus.tongKhachHangDaMua() + " KH", successColor, "Có phát sinh giao dịch"));
        JLabel lblTopDT = new JLabel("0 đ"); lblTopDT.setFont(new Font("Segoe UI", Font.BOLD, 20)); lblTopDT.setForeground(warningColor);
        cards.add(createCardTemplate("Tổng Doanh Thu (Theo bảng)", lblTopDT, "Thay đổi theo thời gian"));
        center.add(cards, BorderLayout.NORTH);

        JPanel tableCard = buildCard();
        tableCard.setLayout(new BorderLayout(0, 10));
        tableCard.add(sectionLabel("Danh Sách Khách Hàng Theo Doanh Thu"), BorderLayout.NORTH);

        String[] cols = {"Xếp Hạng", "Mã KH", "Tên Khách Hàng", "Số Đơn Hàng", "Tổng Chi Tiêu (đ)", "Trung Bình/Đơn (đ)"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) { @Override public boolean isCellEditable(int r, int c) { return false; } };
        JTable tbl = new JTable(model);
        styleTable(tbl);
        
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        tbl.setRowSorter(sorter);

        DefaultTableCellRenderer center1 = new DefaultTableCellRenderer(); center1.setHorizontalAlignment(JLabel.CENTER);
        DefaultTableCellRenderer right = new DefaultTableCellRenderer(); right.setHorizontalAlignment(JLabel.RIGHT);
        tbl.getColumnModel().getColumn(0).setCellRenderer(center1); tbl.getColumnModel().getColumn(3).setCellRenderer(center1);
        tbl.getColumnModel().getColumn(4).setCellRenderer(right); tbl.getColumnModel().getColumn(5).setCellRenderer(right);

        tableCard.add(buildScroll(tbl), BorderLayout.CENTER);
        
        JLabel lblSumFooter = new JLabel("0 đ"); lblSumFooter.setFont(new Font("Segoe UI", Font.BOLD, 16)); lblSumFooter.setForeground(warningColor);
        tableCard.add(buildSumBar("Tổng doanh thu hiển thị:", lblSumFooter), BorderLayout.SOUTH);
        center.add(tableCard, BorderLayout.CENTER);
        root.add(center, BorderLayout.CENTER);
        
        // -- Logic Load & Lọc Tab 2 --
        Runnable loadData = () -> {
            String n = cboNam.getSelectedItem().toString();
            int nam = n.equals("Tất cả") ? 0 : Integer.parseInt(n);            String t = cboThang.getSelectedItem().toString();
            int thang = t.equals("Tất cả") ? 0 : Integer.parseInt(t);

            model.setRowCount(0);
            long tongDT = 0;
            for(Object[] row : bus.getTopKhachHangTheoThoiGian(nam, thang)) {
                model.addRow(new Object[]{ row[0], row[1], row[2], row[3], String.format("%,d", (long)row[4]), String.format("%,d", (long)row[5]) });
                tongDT += (long) row[4];
            }
            lblTopDT.setText(String.format("%,d đ", tongDT));
            lblSumFooter.setText(String.format("%,d đ", tongDT));

            String tuKhoa = txtSearch.getText().trim();
            if (tuKhoa.isEmpty()) sorter.setRowFilter(null);
            else sorter.setRowFilter(RowFilter.regexFilter("(?i)" + tuKhoa, 1, 2));
        };

        loadData.run();
        btn.addActionListener(e -> loadData.run());
        
        return root;
    }

    // =========================================================================
    //  TAB 3 – THEO NHÂN VIÊN
    // =========================================================================
    private JPanel buildNhanVienTab() {
        JPanel root = new JPanel(new BorderLayout(0, 14));
        root.setBackground(bgColor);
        root.setBorder(new EmptyBorder(14, 0, 0, 0));

        JPanel filterCard = buildCard();
        filterCard.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 10));
        filterCard.add(label("Năm:")); JComboBox<String> cboNam = new JComboBox<>(new String[]{"Tất cả","2026","2025","2024"}); styleCombo(cboNam); filterCard.add(cboNam);
        filterCard.add(label("Tháng:")); JComboBox<String> cboThang = new JComboBox<>(new String[]{"Tất cả","1","2","3","4","5","6","7","8","9","10","11","12"}); styleCombo(cboThang); filterCard.add(cboThang);
        filterCard.add(label("Tìm NV:")); JTextField txtSearch = styledField(14); txtSearch.setPreferredSize(new Dimension(160, 36)); filterCard.add(txtSearch);
        JButton btn = createBtn("Tìm Kiếm", primaryColor); btn.setPreferredSize(new Dimension(110, 36)); filterCard.add(btn);
        root.add(filterCard, BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(0, 14));
        center.setBackground(bgColor);

        JPanel cards = new JPanel(new GridLayout(1, 3, 14, 0));
        cards.setOpaque(false);
        cards.add(createSummaryCard("Tổng Nhân Viên", bus.tongNhanVien() + " NV", primaryColor, "Đang hoạt động"));
        cards.add(createSummaryCard("NV Xuất Sắc", bus.nhanVienXuatSac(), successColor, "Doanh thu cao nhất toàn TG"));
        JLabel lblTopDT = new JLabel("0 đ"); lblTopDT.setFont(new Font("Segoe UI", Font.BOLD, 20)); lblTopDT.setForeground(purpleColor);
        cards.add(createCardTemplate("Tổng DT (Theo bảng)", lblTopDT, "Thay đổi theo thời gian"));
        center.add(cards, BorderLayout.NORTH);

        JPanel tableCard = buildCard();
        tableCard.setLayout(new BorderLayout(0, 10));
        tableCard.add(sectionLabel("Hiệu Suất Nhân Viên"), BorderLayout.NORTH);

        String[] cols = {"Xếp Hạng", "Mã NV", "Tên Nhân Viên", "Vị Trí", "Số Đơn Phụ Trách", "Doanh Thu (đ)", "Tỷ Lệ %"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) { @Override public boolean isCellEditable(int r, int c) { return false; } };
        JTable tbl = new JTable(model); styleTable(tbl);        
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model); tbl.setRowSorter(sorter);

        DefaultTableCellRenderer centerR = new DefaultTableCellRenderer(); centerR.setHorizontalAlignment(JLabel.CENTER);
        DefaultTableCellRenderer rightR = new DefaultTableCellRenderer(); rightR.setHorizontalAlignment(JLabel.RIGHT);
        tbl.getColumnModel().getColumn(0).setCellRenderer(centerR); tbl.getColumnModel().getColumn(4).setCellRenderer(centerR);
        tbl.getColumnModel().getColumn(5).setCellRenderer(rightR); tbl.getColumnModel().getColumn(6).setCellRenderer(centerR);

        tableCard.add(buildScroll(tbl), BorderLayout.CENTER);
        
        JLabel lblSumFooter = new JLabel("0 đ"); lblSumFooter.setFont(new Font("Segoe UI", Font.BOLD, 16)); lblSumFooter.setForeground(purpleColor);
        tableCard.add(buildSumBar("Tổng doanh thu hiển thị:", lblSumFooter), BorderLayout.SOUTH);
        center.add(tableCard, BorderLayout.CENTER);
        root.add(center, BorderLayout.CENTER);
        
        Runnable loadData = () -> {
            String n = cboNam.getSelectedItem().toString();
            int nam = n.equals("Tất cả") ? 0 : Integer.parseInt(n);            String t = cboThang.getSelectedItem().toString();
            int thang = t.equals("Tất cả") ? 0 : Integer.parseInt(t);

            model.setRowCount(0);
            long tongDT = 0;
            java.util.List<Object[]> listNV = bus.getTopNhanVienTheoThoiGian(nam, thang);
            for(Object[] row : listNV) { tongDT += (long) row[5]; }

            for(Object[] row : listNV) {
                long dt = (long) row[5];
                String tyLe = tongDT > 0 ? String.format("%.1f%%", (double)dt / tongDT * 100) : "0%";
                model.addRow(new Object[]{ row[0], row[1], row[2], row[3], row[4], String.format("%,d", dt), tyLe });
            }
            lblTopDT.setText(String.format("%,d đ", tongDT));
            lblSumFooter.setText(String.format("%,d đ", tongDT));

            String tuKhoa = txtSearch.getText().trim();
            if (tuKhoa.isEmpty()) sorter.setRowFilter(null);
            else sorter.setRowFilter(RowFilter.regexFilter("(?i)" + tuKhoa, 1, 2));
        };

        loadData.run();
        btn.addActionListener(e -> loadData.run());
        
        return root;
    }

    // =========================================================================
    //  TAB 4 – THEO SẢN PHẨM
    // =========================================================================
    private JPanel buildSanPhamTab() {
        JPanel root = new JPanel(new BorderLayout(0, 14));
        root.setBackground(bgColor);
        root.setBorder(new EmptyBorder(14, 0, 0, 0));

        JPanel filterCard = buildCard();
        filterCard.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 10));
        filterCard.add(label("Năm:")); JComboBox<String> cboNam = new JComboBox<>(new String[]{"Tất cả","2026","2025","2024"}); styleCombo(cboNam); filterCard.add(cboNam);
        filterCard.add(label("Tháng:")); JComboBox<String> cboThang = new JComboBox<>(new String[]{"Tất cả","1","2","3","4","5","6","7","8","9","10","11","12"}); styleCombo(cboThang); filterCard.add(cboThang);
        filterCard.add(label("Danh mục:")); JComboBox<String> cboDM = new JComboBox<>(new String[]{"Tất cả","Thực Phẩm","Đồ Uống","Hóa Mỹ Phẩm","Gia Dụng"}); styleCombo(cboDM); filterCard.add(cboDM);
        filterCard.add(label("Tìm SP:")); JTextField txtSearch = styledField(14); txtSearch.setPreferredSize(new Dimension(150, 36)); filterCard.add(txtSearch);
        JButton btn = createBtn("Tìm Lọc", primaryColor); btn.setPreferredSize(new Dimension(110, 36)); filterCard.add(btn);
        root.add(filterCard, BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(0, 14));
        center.setBackground(bgColor);
        root.add(center, BorderLayout.CENTER);

        JPanel cards = new JPanel(new GridLayout(1, 4, 14, 0));
        cards.setOpaque(false);
        cards.add(createSummaryCard("Tổng Sản Phẩm", bus.tongSanPham() + " SP", primaryColor, "Có trong kho"));
        cards.add(createSummaryCard("SP Bán Chạy", bus.sanPhamBanChay(), successColor, "Nhiều nhất toàn TG"));
        cards.add(createSummaryCard("Sắp Hết Hàng", bus.spSapHetHang() + " SP", dangerColor, "Dưới 15 sản phẩm"));
        JLabel lblTopDT = new JLabel("0 đ"); lblTopDT.setFont(new Font("Segoe UI", Font.BOLD, 20)); lblTopDT.setForeground(warningColor);
        cards.add(createCardTemplate("Tổng DT (Theo bảng)", lblTopDT, "Thay đổi theo thời gian"));
        center.add(cards, BorderLayout.NORTH);

        JPanel tableCard = buildCard();
        tableCard.setLayout(new BorderLayout(0, 10));
        tableCard.add(sectionLabel("Top Sản Phẩm Theo Doanh Thu"), BorderLayout.NORTH);

        String[] cols = {"Xếp Hạng", "Mã SP", "Tên Sản Phẩm", "Danh Mục", "SL Bán Ra", "Đơn Giá (đ)", "Doanh Thu (đ)"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) { @Override public boolean isCellEditable(int r, int c) { return false; } };
        JTable tbl = new JTable(model); styleTable(tbl);
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model); tbl.setRowSorter(sorter);

        DefaultTableCellRenderer centerR = new DefaultTableCellRenderer(); centerR.setHorizontalAlignment(JLabel.CENTER);
        DefaultTableCellRenderer rightR = new DefaultTableCellRenderer(); rightR.setHorizontalAlignment(JLabel.RIGHT);
        tbl.getColumnModel().getColumn(0).setCellRenderer(centerR); tbl.getColumnModel().getColumn(4).setCellRenderer(centerR);
        tbl.getColumnModel().getColumn(5).setCellRenderer(rightR); tbl.getColumnModel().getColumn(6).setCellRenderer(rightR);

        tableCard.add(buildScroll(tbl), BorderLayout.CENTER);
        JLabel lblSumFooter = new JLabel("0 đ"); lblSumFooter.setFont(new Font("Segoe UI", Font.BOLD, 16)); lblSumFooter.setForeground(warningColor);
        tableCard.add(buildSumBar("Tổng doanh thu sản phẩm hiển thị:", lblSumFooter), BorderLayout.SOUTH);
        center.add(tableCard, BorderLayout.CENTER);

        Runnable loadData = () -> {
            String n = cboNam.getSelectedItem().toString();
            int nam = n.equals("Tất cả") ? 0 : Integer.parseInt(n);            String t = cboThang.getSelectedItem().toString();
            int thang = t.equals("Tất cả") ? 0 : Integer.parseInt(t);

            model.setRowCount(0); 
            long tongDT = 0;
            for(Object[] row : bus.getTopSanPhamTheoThoiGian(nam, thang)) {
                long donGia = (long) row[5];
                long dt = (long) row[6];
                model.addRow(new Object[]{ row[0], row[1], row[2], row[3], row[4], String.format("%,d", donGia), String.format("%,d", dt) });
                tongDT += dt;
            }
            lblTopDT.setText(String.format("%,d đ", tongDT));
            lblSumFooter.setText(String.format("%,d đ", tongDT));

            String tuKhoa = txtSearch.getText().trim();
            String danhMuc = cboDM.getSelectedItem().toString();
            java.util.List<RowFilter<Object,Object>> filters = new ArrayList<>();
            if (!tuKhoa.isEmpty()) filters.add(RowFilter.regexFilter("(?i)" + tuKhoa, 1, 2));
            if (!"Tất cả".equals(danhMuc)) filters.add(RowFilter.regexFilter("(?i)" + danhMuc, 3));
            
            if (filters.isEmpty()) sorter.setRowFilter(null);
            else sorter.setRowFilter(RowFilter.andFilter(filters));
        };

        loadData.run();
        btn.addActionListener(e -> loadData.run());

        return root;
    }

    // =========================================================================
    //  SHARED UI HELPERS
    // =========================================================================

    private JPanel createSummaryCard(String title, String value, Color valueColor, String subtitle) {
        JLabel lblVal = new JLabel(value);
        lblVal.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblVal.setForeground(valueColor);
        return createCardTemplate(title, lblVal, subtitle);
    }

    private JPanel createCardTemplate(String title, JLabel valueLabel, String subtitle) {
        JPanel card = new JPanel(new BorderLayout(4, 4));
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(new LineBorder(new Color(230, 230, 230), 1), new EmptyBorder(16, 16, 16, 16)));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblTitle.setForeground(secondaryColor);

        JLabel lblSub = new JLabel(subtitle);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSub.setForeground(new Color(160, 160, 160));

        JPanel valPanel = new JPanel(new BorderLayout(2, 2));
        valPanel.setOpaque(false);
        valPanel.add(valueLabel, BorderLayout.NORTH);
        valPanel.add(lblSub, BorderLayout.CENTER);

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(valPanel, BorderLayout.CENTER);
        return card;
    }

    private JPanel buildCard() {
        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(new LineBorder(new Color(230, 230, 230), 1), new EmptyBorder(14, 14, 14, 14)));
        return card;
    }

    private JPanel buildSumBar(String labelText, JLabel valueLabel) {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 6));
        bar.setBackground(new Color(248, 249, 250));
        bar.setBorder(new MatteBorder(1, 0, 0, 0, new Color(220, 220, 220)));
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        bar.add(lbl); bar.add(valueLabel);
        return bar;
    }

    private JScrollPane buildScroll(JTable tbl) {
        JScrollPane scroll = new JScrollPane(tbl);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Color.WHITE);
        return scroll;
    }

    private JLabel sectionLabel(String text) {
        JLabel lbl = new JLabel(text); lbl.setFont(new Font("Segoe UI", Font.BOLD, 15)); lbl.setForeground(primaryColor); return lbl;
    }

    private JLabel label(String text) {
        JLabel lbl = new JLabel(text); lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14)); return lbl;
    }

    private void styleCombo(JComboBox<String> cb) {
        cb.setFont(new Font("Segoe UI", Font.PLAIN, 14)); cb.setPreferredSize(new Dimension(110, 36));
    }

    private JTextField styledField(int cols) {
        JTextField f = new JTextField(cols); f.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        f.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(200, 200, 200), 1), new EmptyBorder(4, 8, 4, 8))); return f;
    }

    private void styleTable(JTable t) {
        t.setRowHeight(40); t.setFont(new Font("Segoe UI", Font.PLAIN, 14)); t.setGridColor(new Color(245, 245, 245)); t.setShowVerticalLines(false);
        t.setSelectionBackground(new Color(240, 247, 255)); t.setSelectionForeground(Color.BLACK); t.getTableHeader().setReorderingAllowed(false);
        JTableHeader header = t.getTableHeader(); header.setPreferredSize(new Dimension(0, 42)); header.setBackground(new Color(250, 251, 252));
        header.setFont(new Font("Segoe UI", Font.BOLD, 13)); header.setForeground(secondaryColor);
        ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(JLabel.LEFT);
    }

    private JButton createBtn(String text, Color bg) {
        JButton btn = new JButton(text); btn.setFont(new Font("Segoe UI", Font.BOLD, 13)); btn.setBackground(bg); btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false); btn.setBorderPainted(false); btn.setCursor(new Cursor(Cursor.HAND_CURSOR)); return btn;
    }
}