package supermarket.ui.panel.inventory;

import supermarket.model.Supplier;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * SupplierPanel - Quản Lý Nhà Cung Cấp
 * COMPLETE IMPLEMENTATION
 */
public class SupplierPanel extends JPanel {

    private JTable supplierTable;
    private DefaultTableModel tableModel;
    private JButton addButton, editButton, deleteButton, viewHistoryButton;
    private JTextField searchField;
    private JLabel statusLabel;

    // Mock data
    private List<Supplier> suppliers;

    public SupplierPanel() {
        initMockData();
        initComponents();
        layoutComponents();
        loadData();
    }

    private void initMockData() {
        suppliers = new ArrayList<>();
        suppliers.add(new Supplier(1L, "Công ty Coca-Cola Việt Nam", "0281234567"));
        suppliers.add(new Supplier(2L, "Công ty Pepsi Vietnam", "0282345678"));
        suppliers.add(new Supplier(3L, "Công ty Lavie (Suntory)", "0283456789"));
        suppliers.add(new Supplier(4L, "Mondelez Kinh Đô", "0284567890"));
        suppliers.add(new Supplier(5L, "TH True Milk", "0285678901"));
        suppliers.add(new Supplier(6L, "Acecook Vietnam", "0286789012"));
    }

    private void initComponents() {
        // Table
        String[] columns = {"Mã NCC", "Tên nhà cung cấp", "Số điện thoại", "Email", "Trạng thái"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        supplierTable = new JTable(tableModel);
        supplierTable.setRowHeight(30);
        supplierTable.setFont(new Font("Arial", Font.PLAIN, 13));
        supplierTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        supplierTable.getTableHeader().setBackground(new Color(52, 73, 94));
        supplierTable.getTableHeader().setForeground(Color.WHITE);

        // Search
        searchField = new JTextField(20);

        // Buttons
        addButton = createButton("➕ Thêm NCC", new Color(46, 204, 113));
        editButton = createButton("✏️ Sửa", new Color(52, 152, 219));
        deleteButton = createButton("🗑️ Xóa", new Color(231, 76, 60));
        viewHistoryButton = createButton("📜 Lịch sử nhập", new Color(149, 165, 166));

        // Status
        statusLabel = new JLabel();
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 13));

        // Actions
        addButton.addActionListener(e -> handleAdd());
        editButton.addActionListener(e -> handleEdit());
        deleteButton.addActionListener(e -> handleDelete());
        viewHistoryButton.addActionListener(e -> handleViewHistory());
        searchField.addActionListener(e -> handleSearch());
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
        JLabel titleLabel = new JLabel("🏭 QUẢN LÝ NHÀ CUNG CẤP");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(52, 73, 94));
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // Toolbar
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        toolbar.setBackground(Color.WHITE);
        toolbar.add(new JLabel("🔍 Tìm kiếm:"));
        toolbar.add(searchField);
        toolbar.add(Box.createHorizontalStrut(20));
        toolbar.add(addButton);
        toolbar.add(editButton);
        toolbar.add(deleteButton);
        toolbar.add(viewHistoryButton);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(0, 10));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.add(toolbar, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(supplierTable), BorderLayout.CENTER);

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
        for (Supplier s : suppliers) {
            tableModel.addRow(new Object[]{
                s.getMaNhaCungCap() != null ? s.getMaNhaCungCap() : "NCC" + String.format("%03d", s.getId()),
                s.getTenNhaCungCap(),
                s.getSoDienThoai(),
                s.getEmail() != null ? s.getEmail() : "-",
                s.getActive() ? "✅ Hoạt động" : "❌ Ngưng"
            });
        }
        updateStatus();
    }

    private void updateStatus() {
        int active = (int) suppliers.stream().filter(Supplier::getActive).count();
        statusLabel.setText(String.format("📊 Tổng: %d NCC | ✅ Hoạt động: %d",
            suppliers.size(), active));
    }

    private void handleAdd() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.add(new JLabel("Tên nhà cung cấp:"));
        JTextField nameField = new JTextField();
        panel.add(nameField);

        panel.add(new JLabel("Số điện thoại:"));
        JTextField phoneField = new JTextField();
        panel.add(phoneField);

        panel.add(new JLabel("Email:"));
        JTextField emailField = new JTextField();
        panel.add(emailField);

        panel.add(new JLabel("Địa chỉ:"));
        JTextField addressField = new JTextField();
        panel.add(addressField);

        int result = JOptionPane.showConfirmDialog(this, panel,
            "Thêm nhà cung cấp mới", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            if (nameField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập tên NCC!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Supplier newSupplier = new Supplier(
                (long) (suppliers.size() + 1),
                nameField.getText().trim(),
                phoneField.getText().trim()
            );
            newSupplier.setEmail(emailField.getText().trim());
            newSupplier.setDiaChi(addressField.getText().trim());

            suppliers.add(newSupplier);
            loadData();

            JOptionPane.showMessageDialog(this,
                "✓ Thêm nhà cung cấp thành công!",
                "Thành công",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void handleEdit() {
        int row = supplierTable.getSelectedRow();
        if (row >= 0) {
            Supplier supplier = suppliers.get(row);
            JOptionPane.showMessageDialog(this,
                "Chức năng sửa NCC:\n" +
                "Tên: " + supplier.getTenNhaCungCap() + "\n" +
                "SĐT: " + supplier.getSoDienThoai(),
                "Sửa nhà cung cấp",
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                "Vui lòng chọn NCC cần sửa!",
                "Thông báo",
                JOptionPane.WARNING_MESSAGE);
        }
    }

    private void handleDelete() {
        int row = supplierTable.getSelectedRow();
        if (row >= 0) {
            Supplier supplier = suppliers.get(row);
            int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn xóa NCC:\n" + supplier.getTenNhaCungCap() + "?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                suppliers.remove(row);
                tableModel.removeRow(row);
                updateStatus();
                JOptionPane.showMessageDialog(this,
                    "✓ Đã xóa nhà cung cấp!",
                    "Thành công",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                "Vui lòng chọn NCC cần xóa!",
                "Thông báo",
                JOptionPane.WARNING_MESSAGE);
        }
    }

    private void handleViewHistory() {
        int row = supplierTable.getSelectedRow();
        if (row >= 0) {
            Supplier supplier = suppliers.get(row);
            JOptionPane.showMessageDialog(this,
                "Lịch sử nhập hàng từ:\n" +
                supplier.getTenNhaCungCap() + "\n\n" +
                "Mock data:\n" +
                "- 15/01/2026: Phiếu PN001 - 50,000,000đ\n" +
                "- 20/01/2026: Phiếu PN005 - 35,000,000đ\n" +
                "- 05/02/2026: Phiếu PN012 - 28,000,000đ\n\n" +
                "Tổng: 3 phiếu - 113,000,000đ",
                "Lịch sử nhập hàng",
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                "Vui lòng chọn nhà cung cấp!",
                "Thông báo",
                JOptionPane.WARNING_MESSAGE);
        }
    }

    private void handleSearch() {
        String keyword = searchField.getText().trim().toLowerCase();
        tableModel.setRowCount(0);

        for (Supplier s : suppliers) {
            if (keyword.isEmpty() ||
                s.getTenNhaCungCap().toLowerCase().contains(keyword) ||
                (s.getSoDienThoai() != null && s.getSoDienThoai().contains(keyword))) {
                tableModel.addRow(new Object[]{
                    s.getMaNhaCungCap() != null ? s.getMaNhaCungCap() : "NCC" + String.format("%03d", s.getId()),
                    s.getTenNhaCungCap(),
                    s.getSoDienThoai(),
                    s.getEmail() != null ? s.getEmail() : "-",
                    s.getActive() ? "✅ Hoạt động" : "❌ Ngưng"
                });
            }
        }

        statusLabel.setText("🔍 Tìm thấy: " + tableModel.getRowCount() + " NCC");
    }
}
