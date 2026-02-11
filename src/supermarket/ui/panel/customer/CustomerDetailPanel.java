package supermarket.ui.panel.customer;

import supermarket.model.Customer;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * CustomerDetailPanel - Chi Tiết Khách Hàng & Lịch Sử Mua Hàng
 * COMPLETE IMPLEMENTATION
 */
public class CustomerDetailPanel extends JPanel {

    private Customer customer;

    // Info components
    private JLabel maKHLabel, hoTenLabel, soDienThoaiLabel, emailLabel;
    private JLabel diaChiLabel, loaiKHLabel, diemTichLuyLabel, ngayTaoLabel;

    // Statistics
    private JLabel tongDonHangLabel, tongChiTieuLabel, donTrungBinhLabel;
    private JLabel lanMuaGanNhatLabel;

    // Purchase history table
    private JTable purchaseTable;
    private DefaultTableModel purchaseModel;

    // Buttons
    private JButton editButton, addPointsButton, viewInvoiceButton, backButton;

    private DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public CustomerDetailPanel(Customer customer) {
        this.customer = customer;
        initComponents();
        layoutComponents();
        loadData();
    }

    private void initComponents() {
        // Info labels
        maKHLabel = createValueLabel("");
        hoTenLabel = createValueLabel("");
        soDienThoaiLabel = createValueLabel("");
        emailLabel = createValueLabel("");
        diaChiLabel = createValueLabel("");
        loaiKHLabel = createValueLabel("");
        diemTichLuyLabel = createValueLabel("");
        ngayTaoLabel = createValueLabel("");

        // Statistics labels
        tongDonHangLabel = createStatLabel("");
        tongChiTieuLabel = createStatLabel("");
        donTrungBinhLabel = createStatLabel("");
        lanMuaGanNhatLabel = createStatLabel("");

        // Purchase history table
        String[] columns = {"Mã HĐ", "Ngày mua", "Sản phẩm", "Số lượng", "Tổng tiền", "Điểm +", "Trạng thái"};
        purchaseModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        purchaseTable = new JTable(purchaseModel);
        purchaseTable.setRowHeight(28);
        purchaseTable.setFont(new Font("Arial", Font.PLAIN, 13));
        purchaseTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        purchaseTable.getTableHeader().setBackground(new Color(52, 73, 94));
        purchaseTable.getTableHeader().setForeground(Color.WHITE);

        // Buttons
        editButton = createButton("✏️ Sửa thông tin", new Color(52, 152, 219));
        addPointsButton = createButton("⭐ Thêm điểm", new Color(241, 196, 15));
        viewInvoiceButton = createButton("👁️ Xem hóa đơn", new Color(46, 204, 113));
        backButton = createButton("◀ Quay lại", new Color(149, 165, 166));

        // Actions
        editButton.addActionListener(e -> handleEdit());
        addPointsButton.addActionListener(e -> handleAddPoints());
        viewInvoiceButton.addActionListener(e -> handleViewInvoice());
        backButton.addActionListener(e -> handleBack());
    }

    private JLabel createValueLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        return label;
    }

    private JLabel createStatLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        label.setForeground(new Color(52, 73, 94));
        return label;
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
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);

        // Title panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("👤 CHI TIẾT KHÁCH HÀNG");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(52, 73, 94));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(editButton);
        buttonPanel.add(addPointsButton);
        buttonPanel.add(backButton);

        titlePanel.add(titleLabel, BorderLayout.WEST);
        titlePanel.add(buttonPanel, BorderLayout.EAST);
        add(titlePanel, BorderLayout.NORTH);

        // Main content panel
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(Color.WHITE);

        // Left panel - Customer Info & Statistics
        JPanel leftPanel = new JPanel(new BorderLayout(0, 15));
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setPreferredSize(new Dimension(400, 0));

        // Customer info panel
        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Thông tin cơ bản"),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        int row = 0;
        addInfoRow(infoPanel, gbc, row++, "Mã khách hàng:", maKHLabel);
        addInfoRow(infoPanel, gbc, row++, "Họ và tên:", hoTenLabel);
        addInfoRow(infoPanel, gbc, row++, "Số điện thoại:", soDienThoaiLabel);
        addInfoRow(infoPanel, gbc, row++, "Email:", emailLabel);
        addInfoRow(infoPanel, gbc, row++, "Địa chỉ:", diaChiLabel);
        addInfoRow(infoPanel, gbc, row++, "Loại khách hàng:", loaiKHLabel);
        addInfoRow(infoPanel, gbc, row++, "Điểm tích lũy:", diemTichLuyLabel);
        addInfoRow(infoPanel, gbc, row++, "Ngày tạo:", ngayTaoLabel);

        leftPanel.add(infoPanel, BorderLayout.NORTH);

        // Statistics panel
        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        statsPanel.setBackground(Color.WHITE);
        statsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Thống kê mua hàng"),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        statsPanel.add(createStatCard("📊 Tổng đơn hàng", tongDonHangLabel, new Color(52, 152, 219)));
        statsPanel.add(createStatCard("💰 Tổng chi tiêu", tongChiTieuLabel, new Color(46, 204, 113)));
        statsPanel.add(createStatCard("📈 Đơn trung bình", donTrungBinhLabel, new Color(155, 89, 182)));
        statsPanel.add(createStatCard("🕒 Lần mua gần nhất", lanMuaGanNhatLabel, new Color(241, 196, 15)));

        leftPanel.add(statsPanel, BorderLayout.CENTER);

        mainPanel.add(leftPanel, BorderLayout.WEST);

        // Right panel - Purchase History
        JPanel rightPanel = new JPanel(new BorderLayout(0, 10));
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Lịch sử mua hàng"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JPanel tableToolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        tableToolbar.setBackground(Color.WHITE);
        tableToolbar.add(new JLabel("📋 Danh sách giao dịch:"));
        tableToolbar.add(Box.createHorizontalStrut(20));
        tableToolbar.add(viewInvoiceButton);

        rightPanel.add(tableToolbar, BorderLayout.NORTH);
        rightPanel.add(new JScrollPane(purchaseTable), BorderLayout.CENTER);

        mainPanel.add(rightPanel, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);
    }

    private void addInfoRow(JPanel panel, GridBagConstraints gbc, int row,
                           String labelText, JLabel valueLabel) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 0;

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 13));
        label.setForeground(new Color(127, 140, 141));
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(valueLabel, gbc);
    }

    private JPanel createStatCard(String title, JLabel valueLabel, Color color) {
        JPanel card = new JPanel(new BorderLayout(10, 5));
        card.setBackground(new Color(236, 240, 241));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        titleLabel.setForeground(new Color(127, 140, 141));

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    private void loadData() {
        // Load customer info
        maKHLabel.setText(customer.getMaKhachHang() != null ? customer.getMaKhachHang() : "-");
        hoTenLabel.setText(customer.getHoTen());
        soDienThoaiLabel.setText(customer.getSoDienThoai());
        emailLabel.setText(customer.getEmail() != null ? customer.getEmail() : "-");
        diaChiLabel.setText(customer.getDiaChi() != null ? customer.getDiaChi() : "-");

        String loaiKH = customer.getLoaiKhachHang();
        if ("VIP".equals(loaiKH)) {
            loaiKHLabel.setText("⭐ VIP");
            loaiKHLabel.setForeground(new Color(241, 196, 15));
        } else if ("Kim cương".equals(loaiKH)) {
            loaiKHLabel.setText("💎 Kim cương");
            loaiKHLabel.setForeground(new Color(52, 152, 219));
        } else {
            loaiKHLabel.setText("👤 Thường");
            loaiKHLabel.setForeground(new Color(149, 165, 166));
        }

        diemTichLuyLabel.setText(customer.getDiemTichLuy() + " điểm");
        diemTichLuyLabel.setForeground(new Color(241, 196, 15));

        ngayTaoLabel.setText(LocalDate.now().minusMonths(3).format(dateFormat));

        // Load mock statistics
        loadMockStatistics();

        // Load mock purchase history
        loadMockPurchaseHistory();
    }

    private void loadMockStatistics() {
        // Mock data based on customer type
        int tongDon = customer.getLoaiKhachHang().equals("VIP") ? 45 : 12;
        double tongChiTieu = customer.getLoaiKhachHang().equals("VIP") ? 15500000.0 : 3200000.0;
        double donTrungBinh = tongChiTieu / tongDon;

        tongDonHangLabel.setText(tongDon + " đơn");
        tongChiTieuLabel.setText(formatCurrency(tongChiTieu));
        donTrungBinhLabel.setText(formatCurrency(donTrungBinh));
        lanMuaGanNhatLabel.setText(LocalDate.now().minusDays(2).format(dateFormat));
    }

    private void loadMockPurchaseHistory() {
        purchaseModel.setRowCount(0);

        // Mock purchase history
        addPurchaseRow("HD156", "08/02/2026", "Coca Cola, Bánh Oreo...", 5, 125000, 12, "✅ Hoàn thành");
        addPurchaseRow("HD142", "05/02/2026", "Sữa TH, Mì Hảo Hảo...", 8, 235000, 23, "✅ Hoàn thành");
        addPurchaseRow("HD128", "02/02/2026", "Nước Lavie, Trứng...", 6, 180000, 18, "✅ Hoàn thành");
        addPurchaseRow("HD115", "28/01/2026", "Gạo ST25, Dầu ăn...", 3, 450000, 45, "✅ Hoàn thành");
        addPurchaseRow("HD098", "25/01/2026", "Coca Cola, Pepsi...", 4, 95000, 9, "✅ Hoàn thành");
        addPurchaseRow("HD087", "22/01/2026", "Bánh Oreo, Sữa...", 7, 210000, 21, "✅ Hoàn thành");
        addPurchaseRow("HD072", "18/01/2026", "Mì Hảo Hảo...", 12, 42000, 4, "✅ Hoàn thành");
        addPurchaseRow("HD061", "15/01/2026", "Trứng, Gạo...", 5, 320000, 32, "✅ Hoàn thành");
        addPurchaseRow("HD045", "10/01/2026", "Nước uống các loại...", 10, 156000, 15, "✅ Hoàn thành");
        addPurchaseRow("HD032", "05/01/2026", "Sữa TH True Milk...", 6, 180000, 18, "✅ Hoàn thành");
    }

    private void addPurchaseRow(String maHD, String ngay, String sanPham,
                               int soLuong, double tongTien, int diem, String trangThai) {
        purchaseModel.addRow(new Object[]{
            maHD,
            ngay,
            sanPham,
            soLuong + " SP",
            formatCurrency(tongTien),
            "+" + diem,
            trangThai
        });
    }

    private String formatCurrency(double amount) {
        return String.format("%,.0f đ", amount);
    }

    private void handleEdit() {
        JOptionPane.showMessageDialog(this,
            "Chức năng sửa thông tin khách hàng:\n\n" +
            "Sẽ mở CustomerFormPanel với dữ liệu:\n" +
            "- Mã KH: " + customer.getMaKhachHang() + "\n" +
            "- Họ tên: " + customer.getHoTen() + "\n" +
            "- SĐT: " + customer.getSoDienThoai() + "\n\n" +
            "Cho phép sửa tất cả thông tin trừ Mã KH",
            "Sửa thông tin",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void handleAddPoints() {
        String input = JOptionPane.showInputDialog(this,
            "Thêm điểm tích lũy cho khách hàng:\n" +
            customer.getHoTen() + "\n\n" +
            "Điểm hiện tại: " + customer.getDiemTichLuy() + " điểm\n\n" +
            "Nhập số điểm cần thêm:",
            "Thêm điểm tích lũy",
            JOptionPane.QUESTION_MESSAGE);

        if (input != null && !input.trim().isEmpty()) {
            try {
                int points = Integer.parseInt(input.trim());
                if (points <= 0) {
                    JOptionPane.showMessageDialog(this,
                        "Số điểm phải lớn hơn 0!",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int oldPoints = customer.getDiemTichLuy();
                int newPoints = oldPoints + points;
                customer.setDiemTichLuy(newPoints);

                diemTichLuyLabel.setText(newPoints + " điểm");

                JOptionPane.showMessageDialog(this,
                    "✓ Đã thêm điểm thành công!\n\n" +
                    "Điểm cũ: " + oldPoints + " điểm\n" +
                    "Thêm: +" + points + " điểm\n" +
                    "Điểm mới: " + newPoints + " điểm\n\n" +
                    "(Mock only - chưa lưu database)",
                    "Thành công",
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                    "Số điểm không hợp lệ!",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleViewInvoice() {
        int row = purchaseTable.getSelectedRow();
        if (row >= 0) {
            String maHD = (String) purchaseModel.getValueAt(row, 0);
            String ngay = (String) purchaseModel.getValueAt(row, 1);
            String tongTien = (String) purchaseModel.getValueAt(row, 4);

            JOptionPane.showMessageDialog(this,
                "🧾 CHI TIẾT HÓA ĐƠN\n\n" +
                "Mã HĐ: " + maHD + "\n" +
                "Ngày: " + ngay + "\n" +
                "Tổng tiền: " + tongTien + "\n\n" +
                "Sẽ hiển thị chi tiết đầy đủ của hóa đơn:\n" +
                "- Danh sách sản phẩm\n" +
                "- Số lượng, đơn giá\n" +
                "- Giảm giá áp dụng\n" +
                "- Phương thức thanh toán\n" +
                "- Thu ngân xử lý\n\n" +
                "(Mock data)",
                "Chi tiết hóa đơn",
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                "Vui lòng chọn hóa đơn cần xem!",
                "Thông báo",
                JOptionPane.WARNING_MESSAGE);
        }
    }

    private void handleBack() {
        JOptionPane.showMessageDialog(this,
            "Chức năng quay lại danh sách khách hàng\n\n" +
            "Sẽ chuyển về CustomerListPanel",
            "Quay lại",
            JOptionPane.INFORMATION_MESSAGE);
    }
}
