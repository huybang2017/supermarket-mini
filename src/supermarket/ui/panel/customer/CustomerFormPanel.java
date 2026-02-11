package supermarket.ui.panel.customer;

import supermarket.model.Customer;
import javax.swing.*;
import java.awt.*;

/**
 * CustomerFormPanel - Form Thêm/Sửa Khách Hàng
 * COMPLETE IMPLEMENTATION
 */
public class CustomerFormPanel extends JPanel {

    private JTextField maKhachHangField, hoTenField, soDienThoaiField, emailField;
    private JTextField diaChiField, diemTichLuyField;
    private JComboBox<String> loaiKhachHangCombo, gioiTinhCombo;
    private JTextArea ghiChuArea;
    private JButton saveButton, cancelButton, clearButton;

    private Customer editingCustomer;
    private boolean isEditMode = false;

    public CustomerFormPanel() {
        this(null);
    }

    public CustomerFormPanel(Customer customer) {
        this.editingCustomer = customer;
        this.isEditMode = (customer != null);

        initComponents();
        layoutComponents();

        if (isEditMode) {
            loadCustomerData();
        }
    }

    private void initComponents() {
        // Text fields
        maKhachHangField = new JTextField(20);
        hoTenField = new JTextField(20);
        soDienThoaiField = new JTextField(20);
        emailField = new JTextField(20);
        diaChiField = new JTextField(20);
        diemTichLuyField = new JTextField(20);
        diemTichLuyField.setText("0");

        // Combos
        loaiKhachHangCombo = new JComboBox<>(new String[]{"Thường", "VIP", "Kim cương"});
        gioiTinhCombo = new JComboBox<>(new String[]{"Không xác định", "Nam", "Nữ", "Khác"});

        // Text area
        ghiChuArea = new JTextArea(4, 20);
        ghiChuArea.setLineWrap(true);
        ghiChuArea.setWrapStyleWord(true);
        ghiChuArea.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        // Buttons
        saveButton = createButton("💾 Lưu", new Color(46, 204, 113));
        cancelButton = createButton("❌ Hủy", new Color(149, 165, 166));
        clearButton = createButton("🗑️ Xóa form", new Color(52, 152, 219));

        // Actions
        saveButton.addActionListener(e -> handleSave());
        cancelButton.addActionListener(e -> handleCancel());
        clearButton.addActionListener(e -> handleClear());

        // Auto-generate customer code if new
        if (!isEditMode) {
            maKhachHangField.setText("KH" + String.format("%03d", (int)(Math.random() * 1000)));
        }
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
        JLabel titleLabel = new JLabel(isEditMode ? "✏️ SỬA THÔNG TIN KHÁCH HÀNG" : "➕ THÊM KHÁCH HÀNG MỚI");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(52, 73, 94));
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Thông tin khách hàng"),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        int row = 0;

        // Row 1: Mã khách hàng
        addFormRow(formPanel, gbc, row++, "Mã khách hàng:", maKhachHangField, true);
        if (!isEditMode) {
            maKhachHangField.setEnabled(false);
            maKhachHangField.setBackground(new Color(240, 240, 240));
        }

        // Row 2: Họ tên
        addFormRow(formPanel, gbc, row++, "Họ và tên:", hoTenField, true);

        // Row 3: Số điện thoại
        addFormRow(formPanel, gbc, row++, "Số điện thoại:", soDienThoaiField, true);

        // Row 4: Email
        addFormRow(formPanel, gbc, row++, "Email:", emailField, false);

        // Row 5: Giới tính
        addFormRow(formPanel, gbc, row++, "Giới tính:", gioiTinhCombo, false);

        // Row 6: Địa chỉ
        addFormRow(formPanel, gbc, row++, "Địa chỉ:", diaChiField, false);

        // Row 7: Loại khách hàng
        addFormRow(formPanel, gbc, row++, "Loại khách hàng:", loaiKhachHangCombo, true);

        // Row 8: Điểm tích lũy
        addFormRow(formPanel, gbc, row++, "Điểm tích lũy:", diemTichLuyField, false);

        // Row 9: Ghi chú
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        JLabel ghiChuLabel = new JLabel("Ghi chú:");
        ghiChuLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        formPanel.add(ghiChuLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        JScrollPane scrollPane = new JScrollPane(ghiChuArea);
        scrollPane.setPreferredSize(new Dimension(0, 80));
        formPanel.add(scrollPane, gbc);

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
        gbc.fill = GridBagConstraints.HORIZONTAL;

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

    private void loadCustomerData() {
        if (editingCustomer != null) {
            maKhachHangField.setText(editingCustomer.getMaKhachHang());
            hoTenField.setText(editingCustomer.getHoTen());
            soDienThoaiField.setText(editingCustomer.getSoDienThoai());
            emailField.setText(editingCustomer.getEmail() != null ? editingCustomer.getEmail() : "");
            diaChiField.setText(editingCustomer.getDiaChi() != null ? editingCustomer.getDiaChi() : "");
            diemTichLuyField.setText(String.valueOf(editingCustomer.getDiemTichLuy()));

            if (editingCustomer.getLoaiKhachHang() != null) {
                loaiKhachHangCombo.setSelectedItem(editingCustomer.getLoaiKhachHang());
            }

            // Disable customer code editing
            maKhachHangField.setEnabled(false);
            maKhachHangField.setBackground(new Color(240, 240, 240));
        }
    }

    private void handleSave() {
        // Validation
        if (!validateForm()) {
            return;
        }

        // Collect data
        String maKH = maKhachHangField.getText().trim();
        String hoTen = hoTenField.getText().trim();
        String soDienThoai = soDienThoaiField.getText().trim();
        String email = emailField.getText().trim();
        String diaChi = diaChiField.getText().trim();
        String loaiKH = (String) loaiKhachHangCombo.getSelectedItem();
        int diemTichLuy = Integer.parseInt(diemTichLuyField.getText().trim());

        String message = isEditMode ?
            "Cập nhật thông tin khách hàng:\n" :
            "Thêm khách hàng mới:\n";

        message += "\nMã KH: " + maKH;
        message += "\nHọ tên: " + hoTen;
        message += "\nSĐT: " + soDienThoai;
        message += "\nEmail: " + (email.isEmpty() ? "-" : email);
        message += "\nLoại KH: " + loaiKH;
        message += "\nĐiểm tích lũy: " + diemTichLuy;

        int confirm = JOptionPane.showConfirmDialog(this,
            message,
            "Xác nhận",
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this,
                "✓ " + (isEditMode ? "Cập nhật" : "Thêm") + " khách hàng thành công!\n\n" +
                "Thông tin:\n" +
                "- Mã KH: " + maKH + "\n" +
                "- Họ tên: " + hoTen + "\n" +
                "- Loại KH: " + loaiKH + "\n\n" +
                "(Mock only - chưa lưu database)",
                "Thành công",
                JOptionPane.INFORMATION_MESSAGE);

            if (!isEditMode) {
                handleClear();
            }
        }
    }

    private boolean validateForm() {
        if (maKhachHangField.getText().trim().isEmpty()) {
            showError("Vui lòng nhập mã khách hàng!");
            maKhachHangField.requestFocus();
            return false;
        }

        if (hoTenField.getText().trim().isEmpty()) {
            showError("Vui lòng nhập họ tên!");
            hoTenField.requestFocus();
            return false;
        }

        String soDienThoai = soDienThoaiField.getText().trim();
        if (soDienThoai.isEmpty()) {
            showError("Vui lòng nhập số điện thoại!");
            soDienThoaiField.requestFocus();
            return false;
        }

        // Validate phone number format (basic)
        if (!soDienThoai.matches("^0\\d{9,10}$")) {
            showError("Số điện thoại không hợp lệ!\nVui lòng nhập số điện thoại Việt Nam (10-11 số, bắt đầu bằng 0)");
            soDienThoaiField.requestFocus();
            return false;
        }

        // Validate email if provided
        String email = emailField.getText().trim();
        if (!email.isEmpty() && !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            showError("Email không hợp lệ!");
            emailField.requestFocus();
            return false;
        }

        // Validate points
        try {
            int diemTichLuy = Integer.parseInt(diemTichLuyField.getText().trim());
            if (diemTichLuy < 0) {
                showError("Điểm tích lũy không được âm!");
                return false;
            }
        } catch (NumberFormatException e) {
            showError("Điểm tích lũy không hợp lệ!");
            diemTichLuyField.requestFocus();
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
        hoTenField.setText("");
        soDienThoaiField.setText("");
        emailField.setText("");
        diaChiField.setText("");
        diemTichLuyField.setText("0");
        ghiChuArea.setText("");
        loaiKhachHangCombo.setSelectedIndex(0);
        gioiTinhCombo.setSelectedIndex(0);

        // Generate new customer code
        maKhachHangField.setText("KH" + String.format("%03d", (int)(Math.random() * 1000)));

        hoTenField.requestFocus();
    }
}
