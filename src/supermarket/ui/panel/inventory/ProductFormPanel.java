package supermarket.ui.panel.inventory;

import supermarket.model.Product;
import javax.swing.*;
import java.awt.*;

/**
 * ProductFormPanel - Form Thêm/Sửa Sản Phẩm
 * COMPLETE IMPLEMENTATION
 */
public class ProductFormPanel extends JPanel {

    private JTextField maSanPhamField, tenSanPhamField, giaBanField, giaVonField;
    private JTextField soLuongField, donViTinhField, moTaField;
    private JComboBox<String> loaiCombo, hangCombo;
    private JCheckBox activeCheckbox;
    private JButton saveButton, cancelButton, clearButton;

    private Product editingProduct;
    private boolean isEditMode = false;

    public ProductFormPanel() {
        this(null);
    }

    public ProductFormPanel(Product product) {
        this.editingProduct = product;
        this.isEditMode = (product != null);

        initComponents();
        layoutComponents();

        if (isEditMode) {
            loadProductData();
        }
    }

    private void initComponents() {
        // Text fields
        maSanPhamField = new JTextField(20);
        tenSanPhamField = new JTextField(20);
        giaBanField = new JTextField(20);
        giaVonField = new JTextField(20);
        soLuongField = new JTextField(20);
        donViTinhField = new JTextField(20);
        moTaField = new JTextField(20);

        // Combos
        loaiCombo = new JComboBox<>(new String[]{
            "Chọn loại", "Nước uống", "Bánh kẹo", "Sữa", "Thực phẩm", "Trứng", "Gạo"
        });

        hangCombo = new JComboBox<>(new String[]{
            "Chọn hãng", "Coca-Cola", "Pepsi", "Lavie", "Oreo", "TH True Milk", "Acecook", "Vinamilk"
        });

        // Checkbox
        activeCheckbox = new JCheckBox("Đang kinh doanh", true);
        activeCheckbox.setFont(new Font("Arial", Font.PLAIN, 13));

        // Buttons
        saveButton = createButton("💾 Lưu", new Color(46, 204, 113));
        cancelButton = createButton("❌ Hủy", new Color(149, 165, 166));
        clearButton = createButton("🗑️ Xóa form", new Color(52, 152, 219));

        // Actions
        saveButton.addActionListener(e -> handleSave());
        cancelButton.addActionListener(e -> handleCancel());
        clearButton.addActionListener(e -> handleClear());
    }

    private JButton createButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(130, 40));
        return btn;
    }

    private void layoutComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);

        // Title
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(Color.WHITE);
        JLabel titleLabel = new JLabel(isEditMode ? "✏️ SỬA SẢN PHẨM" : "➕ THÊM SẢN PHẨM MỚI");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(52, 73, 94));
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Thông tin sản phẩm"),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        int row = 0;

        // Row 1: Mã sản phẩm
        addFormRow(formPanel, gbc, row++, "Mã sản phẩm:", maSanPhamField, true);

        // Row 2: Tên sản phẩm
        addFormRow(formPanel, gbc, row++, "Tên sản phẩm:", tenSanPhamField, true);

        // Row 3: Loại sản phẩm
        addFormRow(formPanel, gbc, row++, "Loại sản phẩm:", loaiCombo, true);

        // Row 4: Hãng sản xuất
        addFormRow(formPanel, gbc, row++, "Hãng sản xuất:", hangCombo, true);

        // Row 5: Giá bán
        addFormRow(formPanel, gbc, row++, "Giá bán (VNĐ):", giaBanField, true);

        // Row 6: Giá vốn
        addFormRow(formPanel, gbc, row++, "Giá vốn (VNĐ):", giaVonField, true);

        // Row 7: Số lượng tồn
        addFormRow(formPanel, gbc, row++, "Số lượng tồn:", soLuongField, true);

        // Row 8: Đơn vị tính
        addFormRow(formPanel, gbc, row++, "Đơn vị tính:", donViTinhField, true);

        // Row 9: Mô tả
        addFormRow(formPanel, gbc, row++, "Mô tả:", moTaField, false);

        // Row 10: Active checkbox
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        formPanel.add(activeCheckbox, gbc);

        add(formPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(saveButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row,
                           String labelText, JComponent field, boolean required) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 0;

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 13));
        if (required) {
            label.setText(labelText + " *");
            label.setForeground(new Color(52, 73, 94));
        }
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(field, gbc);
    }

    private void loadProductData() {
        if (editingProduct != null) {
            maSanPhamField.setText(editingProduct.getMaSanPham());
            tenSanPhamField.setText(editingProduct.getTenSanPham());
            giaBanField.setText(editingProduct.getGiaBan() != null ?
                editingProduct.getGiaBan().toString() : "");
            giaVonField.setText(editingProduct.getGiaVon() != null ?
                editingProduct.getGiaVon().toString() : "");
            soLuongField.setText(editingProduct.getSoLuongTon() != null ?
                editingProduct.getSoLuongTon().toString() : "");
            donViTinhField.setText(editingProduct.getDonViTinh());
            moTaField.setText(editingProduct.getMoTa());

            if (editingProduct.getIdLoai() != null) {
                loaiCombo.setSelectedIndex(editingProduct.getIdLoai().intValue());
            }

            activeCheckbox.setSelected(editingProduct.getActive() != null &&
                editingProduct.getActive());
        }
    }

    private void handleSave() {
        // Validation
        if (!validateForm()) {
            return;
        }

        // Collect data
        String maSP = maSanPhamField.getText().trim();
        String tenSP = tenSanPhamField.getText().trim();
        double giaBan = Double.parseDouble(giaBanField.getText().trim());
        double giaVon = Double.parseDouble(giaVonField.getText().trim());
        int soLuong = Integer.parseInt(soLuongField.getText().trim());
        String donVi = donViTinhField.getText().trim();

        String message = isEditMode ?
            "Cập nhật sản phẩm:\n" :
            "Thêm sản phẩm mới:\n";

        message += "Mã: " + maSP + "\n";
        message += "Tên: " + tenSP + "\n";
        message += "Giá bán: " + String.format("%,.0f đ", giaBan) + "\n";
        message += "Số lượng: " + soLuong + " " + donVi;

        int confirm = JOptionPane.showConfirmDialog(this,
            message,
            "Xác nhận",
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this,
                "✓ " + (isEditMode ? "Cập nhật" : "Thêm") + " sản phẩm thành công!\n" +
                "(Mock only - chưa lưu database)",
                "Thành công",
                JOptionPane.INFORMATION_MESSAGE);

            if (!isEditMode) {
                handleClear();
            }
        }
    }

    private boolean validateForm() {
        if (maSanPhamField.getText().trim().isEmpty()) {
            showError("Vui lòng nhập mã sản phẩm!");
            maSanPhamField.requestFocus();
            return false;
        }

        if (tenSanPhamField.getText().trim().isEmpty()) {
            showError("Vui lòng nhập tên sản phẩm!");
            tenSanPhamField.requestFocus();
            return false;
        }

        if (loaiCombo.getSelectedIndex() == 0) {
            showError("Vui lòng chọn loại sản phẩm!");
            return false;
        }

        try {
            double giaBan = Double.parseDouble(giaBanField.getText().trim());
            if (giaBan <= 0) {
                showError("Giá bán phải lớn hơn 0!");
                return false;
            }
        } catch (NumberFormatException e) {
            showError("Giá bán không hợp lệ!");
            giaBanField.requestFocus();
            return false;
        }

        try {
            double giaVon = Double.parseDouble(giaVonField.getText().trim());
            if (giaVon <= 0) {
                showError("Giá vốn phải lớn hơn 0!");
                return false;
            }
        } catch (NumberFormatException e) {
            showError("Giá vốn không hợp lệ!");
            giaVonField.requestFocus();
            return false;
        }

        try {
            int soLuong = Integer.parseInt(soLuongField.getText().trim());
            if (soLuong < 0) {
                showError("Số lượng không được âm!");
                return false;
            }
        } catch (NumberFormatException e) {
            showError("Số lượng không hợp lệ!");
            soLuongField.requestFocus();
            return false;
        }

        if (donViTinhField.getText().trim().isEmpty()) {
            showError("Vui lòng nhập đơn vị tính!");
            donViTinhField.requestFocus();
            return false;
        }

        return true;
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Lỗi nhập liệu",
            JOptionPane.ERROR_MESSAGE);
    }

    private void handleCancel() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Bạn có chắc muốn hủy?\nDữ liệu chưa lưu sẽ bị mất!",
            "Xác nhận hủy",
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this,
                "Đã hủy thao tác!",
                "Thông báo",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void handleClear() {
        maSanPhamField.setText("");
        tenSanPhamField.setText("");
        giaBanField.setText("");
        giaVonField.setText("");
        soLuongField.setText("");
        donViTinhField.setText("");
        moTaField.setText("");
        loaiCombo.setSelectedIndex(0);
        hangCombo.setSelectedIndex(0);
        activeCheckbox.setSelected(true);
        maSanPhamField.requestFocus();
    }
}
