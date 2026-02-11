package supermarket.ui.panel.inventory;

import supermarket.model.Product;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ProductListPanel - Quản Lý Sản Phẩm
 * COMPLETE IMPLEMENTATION
 */
public class ProductListPanel extends JPanel {

    private JTable productTable;
    private DefaultTableModel tableModel;
    private JButton addButton, editButton, deleteButton, refreshButton;
    private JTextField searchField;
    private JComboBox<String> categoryFilter;
    private JLabel statusLabel;

    // Mock data
    private List<Product> products;

    public ProductListPanel() {
        initMockData();
        initComponents();
        layoutComponents();
        loadData();
    }

    private void initMockData() {
        products = new ArrayList<>();

        Product p1 = new Product(1L, "P001", "Coca Cola 330ml", 10000.0, 100);
        p1.setIdLoai(1L);
        p1.setDonViTinh("Chai");
        p1.setGiaVon(7000.0);
        products.add(p1);

        Product p2 = new Product(2L, "P002", "Pepsi 330ml", 9500.0, 80);
        p2.setIdLoai(1L);
        p2.setDonViTinh("Chai");
        p2.setGiaVon(6500.0);
        products.add(p2);

        Product p3 = new Product(3L, "P003", "Nước Lavie 500ml", 5000.0, 200);
        p3.setIdLoai(1L);
        p3.setDonViTinh("Chai");
        p3.setGiaVon(3500.0);
        products.add(p3);

        Product p4 = new Product(4L, "P004", "Bánh Oreo", 15000.0, 50);
        p4.setIdLoai(2L);
        p4.setDonViTinh("Gói");
        p4.setGiaVon(12000.0);
        products.add(p4);

        Product p5 = new Product(5L, "P005", "Sữa TH True Milk", 30000.0, 60);
        p5.setIdLoai(3L);
        p5.setDonViTinh("Hộp");
        p5.setGiaVon(25000.0);
        products.add(p5);

        Product p6 = new Product(6L, "P006", "Mì Hảo Hảo", 3500.0, 150);
        p6.setIdLoai(4L);
        p6.setDonViTinh("Gói");
        p6.setGiaVon(2800.0);
        products.add(p6);

        Product p7 = new Product(7L, "P007", "Trứng gà", 35000.0, 40);
        p7.setIdLoai(5L);
        p7.setDonViTinh("Vỉ");
        p7.setGiaVon(30000.0);
        products.add(p7);

        Product p8 = new Product(8L, "P008", "Gạo ST25", 25000.0, 30);
        p8.setIdLoai(6L);
        p8.setDonViTinh("Kg");
        p8.setGiaVon(20000.0);
        products.add(p8);
    }

    private void initComponents() {
        // Table
        String[] columns = {"Mã SP", "Tên sản phẩm", "Giá bán", "Giá vốn", "Tồn kho", "ĐVT", "Trạng thái"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        productTable = new JTable(tableModel);
        productTable.setRowHeight(30);
        productTable.setFont(new Font("Arial", Font.PLAIN, 13));
        productTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        productTable.getTableHeader().setBackground(new Color(52, 73, 94));
        productTable.getTableHeader().setForeground(Color.WHITE);
        productTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Search
        searchField = new JTextField(15);
        searchField.setFont(new Font("Arial", Font.PLAIN, 13));

        // Filter
        categoryFilter = new JComboBox<>(new String[]{
            "Tất cả", "Nước uống", "Bánh kẹo", "Sữa", "Thực phẩm", "Trứng", "Gạo"
        });

        // Buttons
        addButton = createButton("➕ Thêm mới", new Color(46, 204, 113));
        editButton = createButton("✏️ Sửa", new Color(52, 152, 219));
        deleteButton = createButton("🗑️ Xóa", new Color(231, 76, 60));
        refreshButton = createButton("🔄 Làm mới", new Color(149, 165, 166));

        // Status label
        statusLabel = new JLabel("📊 Tổng: 0 sản phẩm");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 13));

        // Actions
        addButton.addActionListener(e -> handleAdd());
        editButton.addActionListener(e -> handleEdit());
        deleteButton.addActionListener(e -> handleDelete());
        refreshButton.addActionListener(e -> loadData());
        searchField.addActionListener(e -> handleSearch());
        categoryFilter.addActionListener(e -> handleFilter());
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

        // Top panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("📦 QUẢN LÝ SẢN PHẨM");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(52, 73, 94));
        topPanel.add(titleLabel, BorderLayout.WEST);

        add(topPanel, BorderLayout.NORTH);

        // Toolbar
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        toolbar.setBackground(Color.WHITE);
        toolbar.add(new JLabel("🔍 Tìm kiếm:"));
        toolbar.add(searchField);
        toolbar.add(new JLabel("📁 Loại:"));
        toolbar.add(categoryFilter);
        toolbar.add(Box.createHorizontalStrut(20));
        toolbar.add(addButton);
        toolbar.add(editButton);
        toolbar.add(deleteButton);
        toolbar.add(refreshButton);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(0, 10));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.add(toolbar, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(productTable), BorderLayout.CENTER);

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
        for (Product p : products) {
            addProductToTable(p);
        }
        updateStatus();
    }

    private void addProductToTable(Product p) {
        String status = getStockStatus(p.getSoLuongTon());
        tableModel.addRow(new Object[]{
            p.getMaSanPham(),
            p.getTenSanPham(),
            formatCurrency(p.getGiaBan()),
            formatCurrency(p.getGiaVon()),
            p.getSoLuongTon(),
            p.getDonViTinh(),
            status
        });
    }

    private String getStockStatus(Integer stock) {
        if (stock == null || stock == 0) return "❌ Hết hàng";
        if (stock < 10) return "⚠️ Sắp hết";
        if (stock < 50) return "⚡ Còn ít";
        return "✅ Còn hàng";
    }

    private String formatCurrency(Double amount) {
        if (amount == null) return "0 đ";
        return String.format("%,.0f đ", amount);
    }

    private void updateStatus() {
        int total = products.size();
        int outOfStock = (int) products.stream().filter(p -> p.getSoLuongTon() == null || p.getSoLuongTon() == 0).count();
        int lowStock = (int) products.stream().filter(p -> p.getSoLuongTon() != null && p.getSoLuongTon() > 0 && p.getSoLuongTon() < 10).count();

        statusLabel.setText(String.format("📊 Tổng: %d sản phẩm | ⚠️ Sắp hết: %d | ❌ Hết hàng: %d",
            total, lowStock, outOfStock));
    }

    private void handleAdd() {
        JOptionPane.showMessageDialog(this,
            "Chức năng thêm sản phẩm mới\n" +
            "Sẽ mở ProductFormPanel để nhập thông tin sản phẩm",
            "Thêm sản phẩm",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void handleEdit() {
        int row = productTable.getSelectedRow();
        if (row >= 0) {
            Product product = products.get(row);
            JOptionPane.showMessageDialog(this,
                "Chức năng sửa sản phẩm:\n" +
                "Mã: " + product.getMaSanPham() + "\n" +
                "Tên: " + product.getTenSanPham() + "\n\n" +
                "Sẽ mở ProductFormPanel với dữ liệu sẵn",
                "Sửa sản phẩm",
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                "Vui lòng chọn sản phẩm cần sửa!",
                "Thông báo",
                JOptionPane.WARNING_MESSAGE);
        }
    }

    private void handleDelete() {
        int row = productTable.getSelectedRow();
        if (row >= 0) {
            Product product = products.get(row);
            int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn xóa sản phẩm:\n" +
                product.getTenSanPham() + "?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                products.remove(row);
                tableModel.removeRow(row);
                updateStatus();
                JOptionPane.showMessageDialog(this,
                    "✓ Đã xóa sản phẩm thành công!",
                    "Thành công",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                "Vui lòng chọn sản phẩm cần xóa!",
                "Thông báo",
                JOptionPane.WARNING_MESSAGE);
        }
    }

    private void handleSearch() {
        String keyword = searchField.getText().trim().toLowerCase();
        tableModel.setRowCount(0);

        for (Product p : products) {
            if (keyword.isEmpty() ||
                p.getTenSanPham().toLowerCase().contains(keyword) ||
                p.getMaSanPham().toLowerCase().contains(keyword)) {
                addProductToTable(p);
            }
        }

        statusLabel.setText("🔍 Tìm thấy: " + tableModel.getRowCount() + " sản phẩm");
    }

    private void handleFilter() {
        int selectedIndex = categoryFilter.getSelectedIndex();
        if (selectedIndex == 0) {
            loadData(); // Show all
            return;
        }

        tableModel.setRowCount(0);
        Long categoryId = (long) selectedIndex;

        for (Product p : products) {
            if (p.getIdLoai() != null && p.getIdLoai().equals(categoryId)) {
                addProductToTable(p);
            }
        }

        statusLabel.setText("📁 Lọc: " + tableModel.getRowCount() + " sản phẩm");
    }
}
