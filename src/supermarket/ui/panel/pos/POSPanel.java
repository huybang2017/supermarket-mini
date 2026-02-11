package supermarket.ui.panel.pos;

import supermarket.model.Product;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * POSPanel - Màn Hình Bán Hàng Chính
 * COMPLETE IMPLEMENTATION - Enhanced Version
 */
public class POSPanel extends JPanel {

    private JTable productTable, cartTable;
    private DefaultTableModel productModel, cartModel;
    private JTextField searchField;
    private JLabel totalLabel, discountLabel, finalTotalLabel;
    private JButton checkoutButton, clearCartButton, addButton;
    private JComboBox<String> customerCombo;

    // Mock data
    private List<Product> products;
    private double cartSubtotal = 0;
    private double discount = 0;

    public POSPanel() {
        initMockData();
        initComponents();
        layoutComponents();
        loadProducts();
        updateCart();
    }

    private void initMockData() {
        products = new ArrayList<>();
        products.add(new Product(1L, "P001", "Coca Cola 330ml", 10000.0, 100));
        products.add(new Product(2L, "P002", "Pepsi 330ml", 9500.0, 80));
        products.add(new Product(3L, "P003", "Nước Lavie 500ml", 5000.0, 200));
        products.add(new Product(4L, "P004", "Bánh Oreo", 15000.0, 50));
        products.add(new Product(5L, "P005", "Sữa TH True Milk", 30000.0, 60));
        products.add(new Product(6L, "P006", "Mì Hảo Hảo", 3500.0, 150));
    }

    private void initComponents() {
        // Product table
        String[] productCols = {"Mã", "Tên sản phẩm", "Giá", "Tồn"};
        productModel = new DefaultTableModel(productCols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        productTable = new JTable(productModel);
        productTable.setRowHeight(25);
        productTable.setFont(new Font("Arial", Font.PLAIN, 13));
        productTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));

        // Cart table
        String[] cartCols = {"Sản phẩm", "SL", "Đơn giá", "Thành tiền"};
        cartModel = new DefaultTableModel(cartCols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        cartTable = new JTable(cartModel);
        cartTable.setRowHeight(28);
        cartTable.setFont(new Font("Arial", Font.PLAIN, 13));
        cartTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));

        // Search
        searchField = new JTextField(20);

        // Customer
        customerCombo = new JComboBox<>(new String[]{
            "Khách lẻ", "KH001 - Nguyễn Lan (VIP)", "KH002 - Trần Minh"
        });

        // Labels
        totalLabel = new JLabel("0 đ");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 18));

        discountLabel = new JLabel("0 đ");
        discountLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        finalTotalLabel = new JLabel("0 đ");
        finalTotalLabel.setFont(new Font("Arial", Font.BOLD, 24));
        finalTotalLabel.setForeground(new Color(231, 76, 60));

        // Buttons
        addButton = createButton("➕ Thêm", new Color(46, 204, 113));
        checkoutButton = createButton("💳 THANH TOÁN", new Color(46, 204, 113));
        checkoutButton.setFont(new Font("Arial", Font.BOLD, 18));
        checkoutButton.setPreferredSize(new Dimension(0, 60));

        clearCartButton = createButton("🗑️ Xóa giỏ", new Color(231, 76, 60));

        // Actions
        addButton.addActionListener(e -> handleAddToCart());
        checkoutButton.addActionListener(e -> handleCheckout());
        clearCartButton.addActionListener(e -> handleClearCart());
        searchField.addActionListener(e -> handleSearch());

        // Double click to add
        productTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    handleAddToCart();
                }
            }
        });
    }

    private JButton createButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void layoutComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(Color.WHITE);

        // Left panel - Products
        JPanel leftPanel = new JPanel(new BorderLayout(5, 5));
        leftPanel.setBackground(Color.WHITE);

        // Search bar
        JPanel searchPanel = new JPanel(new BorderLayout(5, 0));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.add(new JLabel("🔍"), BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(addButton, BorderLayout.EAST);

        leftPanel.add(searchPanel, BorderLayout.NORTH);
        leftPanel.add(new JScrollPane(productTable), BorderLayout.CENTER);

        // Right panel - Cart
        JPanel rightPanel = new JPanel(new BorderLayout(5, 5));
        rightPanel.setPreferredSize(new Dimension(450, 0));
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(BorderFactory.createTitledBorder("🛒 GIỎ HÀNG"));

        // Customer selection
        JPanel customerPanel = new JPanel(new BorderLayout(5, 0));
        customerPanel.setBackground(Color.WHITE);
        customerPanel.add(new JLabel("Khách hàng:"), BorderLayout.WEST);
        customerPanel.add(customerCombo, BorderLayout.CENTER);

        rightPanel.add(customerPanel, BorderLayout.NORTH);
        rightPanel.add(new JScrollPane(cartTable), BorderLayout.CENTER);

        // Summary panel
        JPanel summaryPanel = new JPanel();
        summaryPanel.setLayout(new BoxLayout(summaryPanel, BoxLayout.Y_AXIS));
        summaryPanel.setBackground(Color.WHITE);
        summaryPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        summaryPanel.add(createSummaryRow("Tạm tính:", totalLabel));
        summaryPanel.add(Box.createVerticalStrut(5));
        summaryPanel.add(createSummaryRow("Giảm giá:", discountLabel));
        summaryPanel.add(Box.createVerticalStrut(5));
        summaryPanel.add(new JSeparator());
        summaryPanel.add(Box.createVerticalStrut(10));

        JPanel finalTotalPanel = new JPanel(new BorderLayout());
        finalTotalPanel.setBackground(Color.WHITE);
        JLabel finalLabel = new JLabel("TỔNG TIỀN:");
        finalLabel.setFont(new Font("Arial", Font.BOLD, 20));
        finalTotalPanel.add(finalLabel, BorderLayout.WEST);
        finalTotalPanel.add(finalTotalLabel, BorderLayout.EAST);
        summaryPanel.add(finalTotalPanel);

        rightPanel.add(summaryPanel, BorderLayout.SOUTH);

        // Bottom buttons
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        buttonPanel.add(checkoutButton);
        buttonPanel.add(clearCartButton);
        rightPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(600);
        add(splitPane, BorderLayout.CENTER);
    }

    private JPanel createSummaryRow(String label, JLabel valueLabel) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(lbl, BorderLayout.WEST);
        panel.add(valueLabel, BorderLayout.EAST);
        return panel;
    }

    private void loadProducts() {
        productModel.setRowCount(0);
        for (Product p : products) {
            productModel.addRow(new Object[]{
                p.getMaSanPham(),
                p.getTenSanPham(),
                formatCurrency(p.getGiaBan()),
                p.getSoLuongTon()
            });
        }
    }

    private void handleAddToCart() {
        int row = productTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng chọn sản phẩm!",
                "Thông báo",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        Product product = products.get(row);

        String input = JOptionPane.showInputDialog(this,
            "Nhập số lượng cho:\n" + product.getTenSanPham(),
            "Số lượng",
            JOptionPane.QUESTION_MESSAGE);

        if (input != null && !input.trim().isEmpty()) {
            try {
                int quantity = Integer.parseInt(input.trim());
                if (quantity <= 0) {
                    JOptionPane.showMessageDialog(this, "Số lượng phải lớn hơn 0!");
                    return;
                }

                if (quantity > product.getSoLuongTon()) {
                    JOptionPane.showMessageDialog(this,
                        "Không đủ hàng trong kho!\nTồn: " + product.getSoLuongTon());
                    return;
                }

                double itemTotal = product.getGiaBan() * quantity;
                cartModel.addRow(new Object[]{
                    product.getTenSanPham(),
                    quantity,
                    formatCurrency(product.getGiaBan()),
                    formatCurrency(itemTotal)
                });

                cartSubtotal += itemTotal;
                updateCart();

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Số lượng không hợp lệ!");
            }
        }
    }

    private void updateCart() {
        totalLabel.setText(formatCurrency(cartSubtotal));
        discountLabel.setText(formatCurrency(discount));
        double finalTotal = cartSubtotal - discount;
        finalTotalLabel.setText(formatCurrency(finalTotal));
    }

    private void handleCheckout() {
        if (cartModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this,
                "Giỏ hàng trống!",
                "Thông báo",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        double finalTotal = cartSubtotal - discount;
        String message = String.format(
            "XÁC NHẬN THANH TOÁN\n\n" +
            "Tạm tính: %s\n" +
            "Giảm giá: %s\n" +
            "━━━━━━━━━━━━━━━\n" +
            "TỔNG TIỀN: %s\n\n" +
            "Khách hàng: %s\n\n" +
            "Xác nhận thanh toán?",
            formatCurrency(cartSubtotal),
            formatCurrency(discount),
            formatCurrency(finalTotal),
            customerCombo.getSelectedItem()
        );

        int confirm = JOptionPane.showConfirmDialog(this, message,
            "Thanh toán", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this,
                "✓ Thanh toán thành công!\n" +
                "Mã HĐ: HD" + System.currentTimeMillis() % 10000 + "\n" +
                "Tổng: " + formatCurrency(finalTotal) + "\n\n" +
                "(Mock only - chưa lưu database)",
                "Thành công",
                JOptionPane.INFORMATION_MESSAGE);

            handleClearCart();
        }
    }

    private void handleClearCart() {
        if (cartModel.getRowCount() > 0) {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Xóa tất cả sản phẩm trong giỏ?",
                "Xác nhận",
                JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                cartModel.setRowCount(0);
                cartSubtotal = 0;
                discount = 0;
                updateCart();
            }
        }
    }

    private void handleSearch() {
        String keyword = searchField.getText().trim().toLowerCase();
        productModel.setRowCount(0);

        for (Product p : products) {
            if (keyword.isEmpty() ||
                p.getTenSanPham().toLowerCase().contains(keyword) ||
                p.getMaSanPham().toLowerCase().contains(keyword)) {
                productModel.addRow(new Object[]{
                    p.getMaSanPham(),
                    p.getTenSanPham(),
                    formatCurrency(p.getGiaBan()),
                    p.getSoLuongTon()
                });
            }
        }
    }

    private String formatCurrency(double amount) {
        return String.format("%,.0f đ", amount);
    }
}
