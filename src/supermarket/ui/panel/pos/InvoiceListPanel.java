package supermarket.ui.panel.pos;

import supermarket.model.Invoice;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * InvoiceListPanel - Danh Sách Hóa Đơn COMPLETE IMPLEMENTATION
 */
public class InvoiceListPanel extends JPanel {

    private JTable invoiceTable;
    private DefaultTableModel tableModel;
    private JButton viewDetailButton, printButton, cancelButton, refreshButton;
    private JTextField searchField;
    private JComboBox<String> statusFilter, dateFilter;
    private JLabel statusLabel;

    // Mock data
    private List<Invoice> invoices;
    private DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private DateTimeFormatter dateOnlyFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public InvoiceListPanel() {
        initMockData();
        initComponents();
        layoutComponents();
        loadData();
    }

    private void initMockData() {
        invoices = new ArrayList<>();

        // Invoice 1
        Invoice inv1 = new Invoice();
        inv1.setId(1L);
        inv1.setMaHoaDon("HD001");
        inv1.setNgayTao(LocalDateTime.now().minusDays(5));
        inv1.setIdKhachHang(1L);
        inv1.setIdNhanVien(1L);
        inv1.setTongTien(125000.0);
        inv1.setTienGiam(0.0);
        inv1.setThanhToan(125000.0);
        inv1.setPhuongThucThanhToan("Tiền mặt");
        inv1.setTrangThai("Hoàn thành");
        invoices.add(inv1);

        // Invoice 2
        Invoice inv2 = new Invoice();
        inv2.setId(2L);
        inv2.setMaHoaDon("HD002");
        inv2.setNgayTao(LocalDateTime.now().minusDays(4));
        inv2.setIdKhachHang(2L);
        inv2.setIdNhanVien(1L);
        inv2.setTongTien(85000.0);
        inv2.setTienGiam(5000.0);
        inv2.setThanhToan(80000.0);
        inv2.setPhuongThucThanhToan("Chuyển khoản");
        inv2.setTrangThai("Hoàn thành");
        invoices.add(inv2);

        // Invoice 3
        Invoice inv3 = new Invoice();
        inv3.setId(3L);
        inv3.setMaHoaDon("HD003");
        inv3.setNgayTao(LocalDateTime.now().minusDays(3));
        inv3.setIdKhachHang(null); // khách lẻ
        inv3.setIdNhanVien(1L);
        inv3.setTongTien(45000.0);
        inv3.setTienGiam(0.0);
        inv3.setThanhToan(45000.0);
        inv3.setPhuongThucThanhToan("Tiền mặt");
        inv3.setTrangThai("Hoàn thành");
        invoices.add(inv3);

        // Invoice 4
        Invoice inv4 = new Invoice();
        inv4.setId(4L);
        inv4.setMaHoaDon("HD004");
        inv4.setNgayTao(LocalDateTime.now().minusDays(2));
        inv4.setIdKhachHang(3L);
        inv4.setIdNhanVien(1L);
        inv4.setTongTien(200000.0);
        inv4.setTienGiam(20000.0);
        inv4.setThanhToan(180000.0);
        inv4.setPhuongThucThanhToan("Chuyển khoản");
        inv4.setTrangThai("Hoàn thành");
        invoices.add(inv4);

        // Invoice 5
        Invoice inv5 = new Invoice();
        inv5.setId(5L);
        inv5.setMaHoaDon("HD005");
        inv5.setNgayTao(LocalDateTime.now().minusDays(1));
        inv5.setIdKhachHang(4L);
        inv5.setIdNhanVien(1L);
        inv5.setTongTien(60000.0);
        inv5.setTienGiam(0.0);
        inv5.setThanhToan(60000.0);
        inv5.setPhuongThucThanhToan("Tiền mặt");
        inv5.setTrangThai("Hoàn thành");
        invoices.add(inv5);

        // Invoice 6 - Today
        Invoice inv6 = new Invoice();
        inv6.setId(6L);
        inv6.setMaHoaDon("HD006");
        inv6.setNgayTao(LocalDateTime.now().minusHours(2));
        inv6.setIdKhachHang(5L);
        inv6.setIdNhanVien(1L);
        inv6.setTongTien(150000.0);
        inv6.setTienGiam(15000.0);
        inv6.setThanhToan(135000.0);
        inv6.setPhuongThucThanhToan("Chuyển khoản");
        inv6.setTrangThai("Hoàn thành");
        invoices.add(inv6);

        // Invoice 7 - Cancelled
        Invoice inv7 = new Invoice();
        inv7.setId(7L);
        inv7.setMaHoaDon("HD007");
        inv7.setNgayTao(LocalDateTime.now().minusHours(1));
        inv7.setIdKhachHang(null); // khách lẻ
        inv7.setIdNhanVien(1L);
        inv7.setTongTien(30000.0);
        inv7.setTienGiam(0.0);
        inv7.setThanhToan(0.0);
        inv7.setPhuongThucThanhToan("Tiền mặt");
        inv7.setTrangThai("Đã hủy");
        invoices.add(inv7);
    }

    private void initComponents() {
        // Table
        String[] columns = {"Mã HĐ", "Ngày lập", "Khách hàng", "Thu ngân", "Tổng tiền", "Giảm giá", "Thanh toán", "Trạng thái"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        invoiceTable = new JTable(tableModel);
        invoiceTable.setRowHeight(30);
        invoiceTable.setFont(new Font("Arial", Font.PLAIN, 13));
        invoiceTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        invoiceTable.getTableHeader().setBackground(new Color(52, 73, 94));
        invoiceTable.getTableHeader().setForeground(Color.WHITE);

        // Column widths
        invoiceTable.getColumnModel().getColumn(0).setPreferredWidth(80);  // Mã HĐ
        invoiceTable.getColumnModel().getColumn(1).setPreferredWidth(120); // Ngày
        invoiceTable.getColumnModel().getColumn(2).setPreferredWidth(150); // KH
        invoiceTable.getColumnModel().getColumn(3).setPreferredWidth(100); // NV
        invoiceTable.getColumnModel().getColumn(4).setPreferredWidth(100); // Tổng
        invoiceTable.getColumnModel().getColumn(5).setPreferredWidth(90);  // Giảm
        invoiceTable.getColumnModel().getColumn(6).setPreferredWidth(100); // Thanh toán
        invoiceTable.getColumnModel().getColumn(7).setPreferredWidth(100); // Trạng thái

        // Search
        searchField = new JTextField(15);

        // Filters
        statusFilter = new JComboBox<>(new String[]{"Tất cả", "Hoàn thành", "Đã hủy"});
        dateFilter = new JComboBox<>(new String[]{
            "Tất cả", "Hôm nay", "Hôm qua", "7 ngày qua", "30 ngày qua", "Tùy chỉnh"
        });

        // Date choosers (mock - using simple JTextField instead of JDateChooser)
        // Note: In real project, would use JCalendar library or Java 8 DatePicker
        // Buttons
        viewDetailButton = createButton("👁️ Chi tiết", new Color(52, 152, 219));
        printButton = createButton("🖨️ In HĐ", new Color(46, 204, 113));
        cancelButton = createButton("❌ Hủy HĐ", new Color(231, 76, 60));
        refreshButton = createButton("🔄 Làm mới", new Color(149, 165, 166));

        // Status
        statusLabel = new JLabel();
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 13));

        // Actions
        viewDetailButton.addActionListener(e -> handleViewDetail());
        printButton.addActionListener(e -> handlePrint());
        cancelButton.addActionListener(e -> handleCancel());
        refreshButton.addActionListener(e -> loadData());
        searchField.addActionListener(e -> handleSearch());
        statusFilter.addActionListener(e -> handleFilter());
        dateFilter.addActionListener(e -> handleDateFilter());
    }

    private JButton createButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void layoutComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(Color.WHITE);

        // Title
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(Color.WHITE);
        JLabel titleLabel = new JLabel("🧾 QUẢN LÝ HÓA ĐƠN");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(52, 73, 94));
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // Toolbar
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        toolbar.setBackground(Color.WHITE);

        toolbar.add(new JLabel("🔍 Tìm kiếm:"));
        toolbar.add(searchField);

        toolbar.add(Box.createHorizontalStrut(10));
        toolbar.add(new JLabel("📁 Trạng thái:"));
        toolbar.add(statusFilter);

        toolbar.add(Box.createHorizontalStrut(10));
        toolbar.add(new JLabel("📅 Thời gian:"));
        toolbar.add(dateFilter);

        toolbar.add(Box.createHorizontalStrut(20));
        toolbar.add(refreshButton);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(viewDetailButton);
        buttonPanel.add(printButton);
        buttonPanel.add(cancelButton);

        // Top panel (toolbar + buttons)
        JPanel topPanel = new JPanel(new BorderLayout(0, 10));
        topPanel.setBackground(Color.WHITE);
        topPanel.add(toolbar, BorderLayout.NORTH);
        topPanel.add(buttonPanel, BorderLayout.CENTER);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(0, 10));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(invoiceTable), BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);

        // Status bar
        JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusBar.setBackground(new Color(236, 240, 241));
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        statusBar.add(statusLabel);
        add(statusBar, BorderLayout.SOUTH);
    }

    private void loadData() {
        tableModel.setRowCount(0);
        for (Invoice inv : invoices) {
            addInvoiceToTable(inv);
        }
        updateStatus();
    }

    private void addInvoiceToTable(Invoice inv) {

        String khachHang = inv.getIdKhachHang() != null
                ? "KH#" + inv.getIdKhachHang()
                : "Khách lẻ";

        String nhanVien = inv.getIdNhanVien() != null
                ? "NV#" + inv.getIdNhanVien()
                : "Admin";

        String statusBadge;
        if ("Hoàn thành".equals(inv.getTrangThai())) {
            statusBadge = "✅ Hoàn thành";
        } else if ("Đã hủy".equals(inv.getTrangThai())) {
            statusBadge = "❌ Đã hủy";
        } else {
            statusBadge = "⏳ " + inv.getTrangThai();
        }

        tableModel.addRow(new Object[]{
            inv.getMaHoaDon(),
            inv.getNgayTao().format(dateFormat),
            khachHang,
            nhanVien,
            formatCurrency(inv.getTongTien()),
            formatCurrency(inv.getTienGiam()),
            formatCurrency(inv.getThanhToan()),
            statusBadge
        });
    }

    private void updateStatus() {
        int total = invoices.size();
        int completed = (int) invoices.stream()
                .filter(i -> "Hoàn thành".equals(i.getTrangThai()))
                .count();
        int cancelled = total - completed;

        double totalRevenue = invoices.stream()
                .filter(i -> "Hoàn thành".equals(i.getTrangThai()))
                .mapToDouble(Invoice::getThanhToan)
                .sum();

        statusLabel.setText(String.format(
                "📊 Tổng: %d HĐ | ✅ Hoàn thành: %d | ❌ Đã hủy: %d | 💰 Doanh thu: %s",
                total, completed, cancelled, formatCurrency(totalRevenue)
        ));
    }

    private void handleViewDetail() {
        int row = invoiceTable.getSelectedRow();

        if (row >= 0) {
            Invoice invoice = invoices.get(row);
            String khachHang = invoice.getIdKhachHang() != null
                    ? "KH#" + invoice.getIdKhachHang()
                    : "Khách lẻ";

            String nhanVien = invoice.getIdNhanVien() != null
                    ? "NV#" + invoice.getIdNhanVien()
                    : "Admin";
            String detail = String.format(
                    "🧾 CHI TIẾT HÓA ĐƠN\n\n"
                    + "Mã HĐ: %s\n"
                    + "Ngày lập: %s\n"
                    + "━━━━━━━━━━━━━━━━━━━━━\n"
                    + "Khách hàng: %s\n"
                    + "Thu ngân: %s\n"
                    + "━━━━━━━━━━━━━━━━━━━━━\n\n"
                    + "CHI TIẾT SẢN PHẨM:\n"
                    + "1. Coca Cola 330ml    x2    20,000 đ\n"
                    + "2. Bánh Oreo          x3    45,000 đ\n"
                    + "3. Sữa TH True Milk   x2    60,000 đ\n"
                    + "━━━━━━━━━━━━━━━━━━━━━\n"
                    + "Tạm tính:       %s\n"
                    + "Giảm giá:       %s\n"
                    + "━━━━━━━━━━━━━━━━━━━━━\n"
                    + "TỔNG TIỀN:      %s\n\n"
                    + "Trạng thái: %s\n"
                    + "(Mock data - Chi tiết thực sẽ từ ChiTietHoaDon)",
                    invoice.getMaHoaDon(),
                    invoice.getNgayTao().format(dateFormat),
                    khachHang,
                    nhanVien,
                    formatCurrency(invoice.getTongTien()),
                    formatCurrency(invoice.getTienGiam()),
                    formatCurrency(invoice.getThanhToan()),
                    invoice.getTrangThai()
            );

            JOptionPane.showMessageDialog(this, detail,
                    "Chi tiết hóa đơn",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn hóa đơn!",
                    "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void handlePrint() {
        int row = invoiceTable.getSelectedRow();
        if (row >= 0) {
            Invoice invoice = invoices.get(row);

            if ("Đã hủy".equals(invoice.getTrangThai())) {
                JOptionPane.showMessageDialog(this,
                        "Không thể in hóa đơn đã hủy!",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            JOptionPane.showMessageDialog(this,
                    "🖨️ IN HÓA ĐƠN\n\n"
                    + "Mã HĐ: " + invoice.getMaHoaDon() + "\n"
                    + "Tổng tiền: " + formatCurrency(invoice.getThanhToan()) + "\n\n"
                    + "Chức năng in hóa đơn sẽ được triển khai:\n"
                    + "- In nhiệt (80mm)\n"
                    + "- In A4\n"
                    + "- Xuất PDF\n\n"
                    + "(Mock only)",
                    "In hóa đơn",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn hóa đơn cần in!",
                    "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void handleCancel() {
        int row = invoiceTable.getSelectedRow();
        if (row >= 0) {
            Invoice invoice = invoices.get(row);
            String khachHang = invoice.getIdKhachHang() != null
                    ? "KH#" + invoice.getIdKhachHang()
                    : "Khách lẻ";
            if ("Đã hủy".equals(invoice.getTrangThai())) {
                JOptionPane.showMessageDialog(this,
                        "Hóa đơn này đã bị hủy trước đó!",
                        "Thông báo",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            String message = String.format(
                    "⚠️ XÁC NHẬN HỦY HÓA ĐƠN\n\n"
                    + "Mã HĐ: %s\n"
                    + "Ngày lập: %s\n"
                    + "Khách hàng: %s\n"
                    + "Tổng tiền: %s\n\n"
                    + "Lý do hủy:\n"
                    + "[ ] Khách hàng yêu cầu\n"
                    + "[ ] Nhập sai thông tin\n"
                    + "[ ] Khác\n\n"
                    + "Bạn có chắc muốn hủy hóa đơn này?",
                    invoice.getMaHoaDon(),
                    invoice.getNgayTao().format(dateFormat),
                    khachHang,
                    formatCurrency(invoice.getThanhToan())
            );

            int confirm = JOptionPane.showConfirmDialog(this, message,
                    "Hủy hóa đơn", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                invoice.setTrangThai("Đã hủy");
                invoice.setThanhToan(0.0);
                loadData();

                JOptionPane.showMessageDialog(this,
                        "✓ Đã hủy hóa đơn " + invoice.getMaHoaDon() + "\n"
                        + "(Mock only - Thực tế sẽ cập nhật database và hoàn tồn kho)",
                        "Thành công",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn hóa đơn cần hủy!",
                    "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void handleSearch() {
        String keyword = searchField.getText().trim().toLowerCase();
        tableModel.setRowCount(0);

        for (Invoice inv : invoices) {
            String kh = inv.getIdKhachHang() != null ? "KH#" + inv.getIdKhachHang() : "Khách lẻ";
            String nv = inv.getIdNhanVien() != null ? "NV#" + inv.getIdNhanVien() : "Admin";
            if (keyword.isEmpty()
                    || inv.getMaHoaDon().toLowerCase().contains(keyword)
                    || kh.toLowerCase().contains(keyword)
                    || nv.toLowerCase().contains(keyword)) {
                addInvoiceToTable(inv);
            }
        }

        statusLabel.setText("🔍 Tìm thấy: " + tableModel.getRowCount() + " hóa đơn");
    }

    private void handleFilter() {
        String status = (String) statusFilter.getSelectedItem();
        if ("Tất cả".equals(status)) {
            loadData();
            return;
        }

        tableModel.setRowCount(0);
        for (Invoice inv : invoices) {
            if (inv.getTrangThai().equals(status)) {
                addInvoiceToTable(inv);
            }
        }

        statusLabel.setText("📁 Lọc: " + tableModel.getRowCount() + " hóa đơn");
    }

    private void handleDateFilter() {
        String period = (String) dateFilter.getSelectedItem();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDate = null;

        switch (period) {
            case "Hôm nay":
                startDate = now.withHour(0).withMinute(0).withSecond(0);
                break;
            case "Hôm qua":
                startDate = now.minusDays(1).withHour(0).withMinute(0).withSecond(0);
                break;
            case "7 ngày qua":
                startDate = now.minusDays(7);
                break;
            case "30 ngày qua":
                startDate = now.minusDays(30);
                break;
            case "Tùy chỉnh":
                JOptionPane.showMessageDialog(this,
                        "Chức năng chọn ngày tùy chỉnh:\n"
                        + "Sẽ hiển thị 2 JDateChooser để chọn từ ngày - đến ngày\n\n"
                        + "(Cần thêm thư viện JCalendar hoặc Java 8 DatePicker)",
                        "Chọn ngày",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            default:
                loadData();
                return;
        }

        if (startDate != null) {
            tableModel.setRowCount(0);
            final LocalDateTime finalStartDate = startDate;
            for (Invoice inv : invoices) {
                if (inv.getNgayTao().isAfter(finalStartDate)) {
                    addInvoiceToTable(inv);
                }
            }
            statusLabel.setText("📅 Lọc theo: " + period + " (" + tableModel.getRowCount() + " HĐ)");
        }
    }

    private String formatCurrency(double amount) {
        return String.format("%,.0f đ", amount);
    }
}
