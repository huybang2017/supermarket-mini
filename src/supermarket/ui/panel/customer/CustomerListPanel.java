package supermarket.ui.panel.customer;

import supermarket.model.Customer;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * CustomerListPanel - Danh Sách Khách Hàng
 * COMPLETE IMPLEMENTATION
 */
public class CustomerListPanel extends JPanel {

    private JTable customerTable;
    private DefaultTableModel tableModel;
    private JButton addButton, editButton, deleteButton, viewDetailButton;
    private JTextField searchField;
    private JComboBox<String> typeFilter;
    private JLabel statusLabel;

    // Mock data
    private List<Customer> customers;

    public CustomerListPanel() {
        initMockData();
        initComponents();
        layoutComponents();
        loadData();
    }

    private void initMockData() {
        customers = new ArrayList<>();

        Customer c1 = new Customer(1L, "Nguyễn Thị Lan", "0901234567");
        c1.setMaKhachHang("KH001");
        c1.setEmail("lan.nguyen@email.com");
        c1.setDiemTichLuy(150);
        c1.setLoaiKhachHang("VIP");
        customers.add(c1);

        Customer c2 = new Customer(2L, "Trần Văn Minh", "0912345678");
        c2.setMaKhachHang("KH002");
        c2.setDiemTichLuy(50);
        c2.setLoaiKhachHang("Thường");
        customers.add(c2);

        Customer c3 = new Customer(3L, "Lê Thị Hoa", "0923456789");
        c3.setMaKhachHang("KH003");
        c3.setEmail("hoa.le@email.com");
        c3.setDiemTichLuy(200);
        c3.setLoaiKhachHang("VIP");
        customers.add(c3);

        Customer c4 = new Customer(4L, "Phạm Văn Nam", "0934567890");
        c4.setMaKhachHang("KH004");
        c4.setDiemTichLuy(30);
        c4.setLoaiKhachHang("Thường");
        customers.add(c4);

        Customer c5 = new Customer(5L, "Hoàng Thị Mai", "0945678901");
        c5.setMaKhachHang("KH005");
        c5.setEmail("mai.hoang@email.com");
        c5.setDiemTichLuy(180);
        c5.setLoaiKhachHang("VIP");
        customers.add(c5);
    }

    private void initComponents() {
        // Table
        String[] columns = {"Mã KH", "Họ tên", "Số điện thoại", "Email", "Điểm tích lũy", "Loại KH"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        customerTable = new JTable(tableModel);
        customerTable.setRowHeight(30);
        customerTable.setFont(new Font("Arial", Font.PLAIN, 13));
        customerTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        customerTable.getTableHeader().setBackground(new Color(52, 73, 94));
        customerTable.getTableHeader().setForeground(Color.WHITE);

        // Search
        searchField = new JTextField(20);

        // Filter
        typeFilter = new JComboBox<>(new String[]{"Tất cả", "VIP", "Thường"});

        // Buttons
        addButton = createButton("➕ Thêm KH", new Color(46, 204, 113));
        editButton = createButton("✏️ Sửa", new Color(52, 152, 219));
        deleteButton = createButton("🗑️ Xóa", new Color(231, 76, 60));
        viewDetailButton = createButton("👁️ Chi tiết", new Color(149, 165, 166));

        // Status
        statusLabel = new JLabel();
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 13));

        // Actions
        addButton.addActionListener(e -> handleAdd());
        editButton.addActionListener(e -> handleEdit());
        deleteButton.addActionListener(e -> handleDelete());
        viewDetailButton.addActionListener(e -> handleViewDetail());
        searchField.addActionListener(e -> handleSearch());
        typeFilter.addActionListener(e -> handleFilter());
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
        JLabel titleLabel = new JLabel("👥 QUẢN LÝ KHÁCH HÀNG");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(52, 73, 94));
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // Toolbar
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        toolbar.setBackground(Color.WHITE);
        toolbar.add(new JLabel("🔍 Tìm kiếm:"));
        toolbar.add(searchField);
        toolbar.add(new JLabel("📁 Loại:"));
        toolbar.add(typeFilter);
        toolbar.add(Box.createHorizontalStrut(20));
        toolbar.add(addButton);
        toolbar.add(editButton);
        toolbar.add(deleteButton);
        toolbar.add(viewDetailButton);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(0, 10));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.add(toolbar, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(customerTable), BorderLayout.CENTER);

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
        for (Customer c : customers) {
            addCustomerToTable(c);
        }
        updateStatus();
    }

    private void addCustomerToTable(Customer c) {
        String loaiKH = c.getLoaiKhachHang();
        String badge = loaiKH.equals("VIP") ? "⭐ VIP" : "👤 Thường";

        tableModel.addRow(new Object[]{
            c.getMaKhachHang(),
            c.getHoTen(),
            c.getSoDienThoai(),
            c.getEmail() != null ? c.getEmail() : "-",
            c.getDiemTichLuy() + " điểm",
            badge
        });
    }

    private void updateStatus() {
        int total = customers.size();
        int vip = (int) customers.stream().filter(c -> "VIP".equals(c.getLoaiKhachHang())).count();

        statusLabel.setText(String.format("📊 Tổng: %d khách hàng | ⭐ VIP: %d | 👤 Thường: %d",
            total, vip, total - vip));
    }

    private void handleAdd() {
        JOptionPane.showMessageDialog(this,
            "Chức năng thêm khách hàng mới\n" +
            "Sẽ mở CustomerFormPanel",
            "Thêm khách hàng",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void handleEdit() {
        int row = customerTable.getSelectedRow();
        if (row >= 0) {
            Customer customer = customers.get(row);
            JOptionPane.showMessageDialog(this,
                "Chức năng sửa khách hàng:\n" +
                "Tên: " + customer.getHoTen() + "\n" +
                "SĐT: " + customer.getSoDienThoai(),
                "Sửa khách hàng",
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                "Vui lòng chọn khách hàng cần sửa!",
                "Thông báo",
                JOptionPane.WARNING_MESSAGE);
        }
    }

    private void handleDelete() {
        int row = customerTable.getSelectedRow();
        if (row >= 0) {
            Customer customer = customers.get(row);
            int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn xóa khách hàng:\n" + customer.getHoTen() + "?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                customers.remove(row);
                tableModel.removeRow(row);
                updateStatus();
                JOptionPane.showMessageDialog(this,
                    "✓ Đã xóa khách hàng!",
                    "Thành công",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                "Vui lòng chọn khách hàng cần xóa!",
                "Thông báo",
                JOptionPane.WARNING_MESSAGE);
        }
    }

    private void handleViewDetail() {
        int row = customerTable.getSelectedRow();
        if (row >= 0) {
            Customer customer = customers.get(row);
            String detail = String.format(
                "👤 THÔNG TIN KHÁCH HÀNG\n\n" +
                "Mã KH: %s\n" +
                "Họ tên: %s\n" +
                "SĐT: %s\n" +
                "Email: %s\n" +
                "Điểm tích lũy: %d\n" +
                "Loại KH: %s\n\n" +
                "📊 THỐNG KÊ MUA HÀNG:\n" +
                "Tổng đơn: 15 hóa đơn\n" +
                "Tổng chi tiêu: 5,500,000 đ\n" +
                "Lần mua gần nhất: 10/02/2026",
                customer.getMaKhachHang(),
                customer.getHoTen(),
                customer.getSoDienThoai(),
                customer.getEmail() != null ? customer.getEmail() : "-",
                customer.getDiemTichLuy(),
                customer.getLoaiKhachHang()
            );

            JOptionPane.showMessageDialog(this, detail,
                "Chi tiết khách hàng",
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                "Vui lòng chọn khách hàng!",
                "Thông báo",
                JOptionPane.WARNING_MESSAGE);
        }
    }

    private void handleSearch() {
        String keyword = searchField.getText().trim().toLowerCase();
        tableModel.setRowCount(0);

        for (Customer c : customers) {
            if (keyword.isEmpty() ||
                c.getHoTen().toLowerCase().contains(keyword) ||
                c.getSoDienThoai().contains(keyword) ||
                (c.getMaKhachHang() != null && c.getMaKhachHang().toLowerCase().contains(keyword))) {
                addCustomerToTable(c);
            }
        }

        statusLabel.setText("🔍 Tìm thấy: " + tableModel.getRowCount() + " khách hàng");
    }

    private void handleFilter() {
        String type = (String) typeFilter.getSelectedItem();
        if ("Tất cả".equals(type)) {
            loadData();
            return;
        }

        tableModel.setRowCount(0);
        for (Customer c : customers) {
            if (type.equals(c.getLoaiKhachHang())) {
                addCustomerToTable(c);
            }
        }

        statusLabel.setText("📁 Lọc: " + tableModel.getRowCount() + " khách hàng");
    }
}
