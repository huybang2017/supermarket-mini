package supermarket.ui.frame;

import supermarket.ui.panel.pos.POSPanel;
import supermarket.ui.panel.pos.InvoiceListPanel;
import supermarket.ui.panel.inventory.ProductListPanel;
import supermarket.ui.panel.inventory.SupplierPanel;
import supermarket.ui.panel.inventory.StockReportPanel;
import supermarket.ui.panel.customer.CustomerListPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * MainSystemFrame - Complete POS System with Full Menu
 *
 * 8 Modules: 1. Dashboard 2. POS (Bán hàng) 3. Inventory (Kho hàng) 4. Customer
 * (Khách hàng) 5. Employee (Nhân viên) 6. Promotion (Khuyến mãi) 7. Report (Báo
 * cáo) 8. Config (Cấu hình)
 */
public class MainSystemFrame extends JFrame {

    private JPanel mainPanel;
    private JPanel menuPanel;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private String currentUser = "Admin";

    public MainSystemFrame() {
        initFrame();
        createMenuPanel();
        createContentPanel();
        layoutComponents();
    }

    private void initFrame() {
        setTitle("Supermarket Mini POS - Hệ Thống Quản Lý");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 800);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(1200, 700));
    }

    private void createMenuPanel() {
        menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(new Color(44, 62, 80));
        menuPanel.setPreferredSize(new Dimension(250, 0));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        // Header
        JLabel headerLabel = new JLabel("MENU CHÍNH");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        menuPanel.add(headerLabel);
        menuPanel.add(Box.createVerticalStrut(20));

        // Module 1: Dashboard
        addMenuSection("TỔNG QUAN");
        addMenuItem("Dashboard", "dashboard");
        menuPanel.add(Box.createVerticalStrut(15));

        // Module 2: POS
        addMenuSection("BÁN HÀNG");
        addMenuItem("Màn Hình Bán Hàng", "pos");
        addMenuItem("Danh Sách Hóa Đơn", "invoice-list");
        menuPanel.add(Box.createVerticalStrut(15));

        // Module 3: Inventory
        addMenuSection("KHO HÀNG");
        addMenuItem("Danh Sách Sản Phẩm", "product-list");
        addMenuItem("Nhập Hàng", "import");
        addMenuItem("Nhà Cung Cấp", "supplier");
        addMenuItem("Báo Cáo Tồn Kho", "stock-report");
        menuPanel.add(Box.createVerticalStrut(15));

        // Module 4: Customer
        addMenuSection("KHÁCH HÀNG");
        addMenuItem("Danh Sách Khách Hàng", "customer-list");
        menuPanel.add(Box.createVerticalStrut(15));

        // Module 5: Employee
        addMenuSection("NHÂN VIÊN");
        addMenuItem("Danh Sách Nhân Viên", "employee-list");
        menuPanel.add(Box.createVerticalStrut(15));

        // Module 6: Promotion
        addMenuSection("KHUYẾN MÃI");
        addMenuItem("Danh Sách Khuyến Mãi", "promotion-list");
        addMenuItem("Tạo Khuyến Mãi", "promotion-create");
        menuPanel.add(Box.createVerticalStrut(15));

        // Module 7: Report
        addMenuSection("BÁO CÁO");
        addMenuItem("Doanh Thu", "revenue-report");
        menuPanel.add(Box.createVerticalStrut(15));

        // Module 8: Config
        addMenuSection("CẤU HÌNH");
        addMenuItem("Hệ Thống", "system-config");
        addMenuItem("Loại Sản Phẩm", "category");
        addMenuItem("Hãng Sản Xuất", "manufacturer");
        addMenuItem("Phân Quyền", "permission");

        // Spacer
        menuPanel.add(Box.createVerticalGlue());

        // Logout
        JButton logoutBtn = createMenuButton("ĐĂNG XUẤT", true);
        logoutBtn.setBackground(new Color(231, 76, 60));
        logoutBtn.addActionListener(e -> logout());
        menuPanel.add(logoutBtn);

        // Version
        JLabel versionLabel = new JLabel("v1.0.0");
        versionLabel.setForeground(Color.LIGHT_GRAY);
        versionLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        versionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(versionLabel);
    }

    private void addMenuSection(String title) {
        JLabel label = new JLabel(title);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setForeground(new Color(149, 165, 166));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        menuPanel.add(label);
        menuPanel.add(Box.createVerticalStrut(5));
    }

    private void addMenuItem(String text, String panelName) {
        JButton button = createMenuButton(text, false);
        button.addActionListener(e -> showPanel(panelName));
        menuPanel.add(button);
        menuPanel.add(Box.createVerticalStrut(3));
    }

    private JButton createMenuButton(String text, boolean isLogout) {
        JButton button = new JButton(text);
        button.setMaximumSize(new Dimension(230, 35));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setFont(new Font("Arial", Font.PLAIN, 13));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setHorizontalAlignment(SwingConstants.LEFT);

        if (isLogout) {
            button.setForeground(Color.WHITE);
        } else {
            button.setBackground(new Color(52, 73, 94));
            button.setForeground(Color.WHITE);
            button.setBorderPainted(false);
        }

        return button;
    }

    private void createContentPanel() {
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(Color.WHITE);

        // Add all panels
        // Dashboard - Placeholder
        contentPanel.add(createPlaceholder("DASHBOARD", "Tổng quan hệ thống"), "dashboard");

        // MODULE 3: POS - COMPLETE ✅
        contentPanel.add(new POSPanel(), "pos");
        contentPanel.add(new InvoiceListPanel(), "invoice-list");

        // MODULE 1: INVENTORY - COMPLETE ✅
        contentPanel.add(new ProductListPanel(), "product-list");
        contentPanel.add(createPlaceholder("NHẬP HÀNG", "Import Goods"), "import");
        contentPanel.add(new SupplierPanel(), "supplier");
        contentPanel.add(new StockReportPanel(), "stock-report");

        // MODULE 2: CUSTOMER - COMPLETE ✅
        contentPanel.add(new CustomerListPanel(), "customer-list");

        // MODULE 5: EMPLOYEE - Placeholders
        contentPanel.add(createPlaceholder("👨‍💼 DANH SÁCH NHÂN VIÊN", "Employee List"), "employee-list");
        contentPanel.add(createPlaceholder("📈 HIỆU SUẤT NHÂN VIÊN", "Performance"), "performance");

        // MODULE 6: PROMOTION - Placeholders
        contentPanel.add(createPlaceholder("🎁 DANH SÁCH KHUYẾN MÃI", "Promotion List"), "promotion-list");
        contentPanel.add(createPlaceholder("➕ TẠO KHUYẾN MÃI", "Create Promotion"), "promotion-create");

        // MODULE 7: REPORT - Placeholders
        contentPanel.add(createPlaceholder("📊 BÁO CÁO DOANH THU", "Revenue Report"), "revenue-report");
        contentPanel.add(createPlaceholder("🔥 SẢN PHẨM BÁN CHẠY", "Best Seller"), "bestseller");
        contentPanel.add(createPlaceholder("📦 NHẬP-XUẤT-TỒN", "Inventory Movement"), "inventory-movement");
        contentPanel.add(createPlaceholder("💰 BÁO CÁO LỢI NHUẬN", "Profit Report"), "profit");

        // MODULE 8: CONFIG - Placeholders
        contentPanel.add(createPlaceholder("⚙️ CẤU HÌNH HỆ THỐNG", "System Config"), "system-config");
        contentPanel.add(createPlaceholder("📁 LOẠI SẢN PHẨM", "Category"), "category");
        contentPanel.add(createPlaceholder("🏭 HÃNG SẢN XUẤT", "Manufacturer"), "manufacturer");
        contentPanel.add(createPlaceholder("🔐 PHÂN QUYỀN", "Permission"), "permission");

        // Show dashboard by default
        cardLayout.show(contentPanel, "dashboard");
    }

    private JPanel createPlaceholder(String title, String subtitle) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(new Color(52, 73, 94));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitleLabel.setForeground(Color.GRAY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel noteLabel = new JLabel("(Panel sẽ được implement ở đây)");
        noteLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        noteLabel.setForeground(Color.LIGHT_GRAY);
        noteLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(titleLabel);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(subtitleLabel);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(noteLabel);
        centerPanel.add(Box.createVerticalGlue());

        panel.add(centerPanel, BorderLayout.CENTER);

        return panel;
    }

    private void showPanel(String panelName) {
        cardLayout.show(contentPanel, panelName);
        System.out.println("✓ Switched to: " + panelName);
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc muốn đăng xuất?",
                "Xác nhận đăng xuất",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            this.dispose();
            SwingUtilities.invokeLater(() -> {
                LoginFrame loginFrame = new LoginFrame();
                loginFrame.setVisible(true);
            });
        }
    }

    private void layoutComponents() {
        mainPanel = new JPanel(new BorderLayout());

        // 1. Cấu hình Header
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // 2. Cấu hình Sidebar với ScrollPane
        JScrollPane menuScrollPane = new JScrollPane(menuPanel);

        // QUAN TRỌNG: Xóa border của ScrollPane để menu dính sát vào cạnh
        menuScrollPane.setBorder(null);

        // Luôn hiện thanh cuộn dọc khi cần, giấu thanh cuộn ngang
        menuScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        menuScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        // Tăng tốc độ cuộn chuột (mặc định của Swing rất chậm)
        menuScrollPane.getVerticalScrollBar().setUnitIncrement(16);

        mainPanel.add(menuScrollPane, BorderLayout.WEST);

        // 3. Content area
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(41, 128, 185));
        panel.setPreferredSize(new Dimension(0, 60));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel titleLabel = new JLabel("SUPERMARKET MINI POS");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);

        JLabel userLabel = new JLabel("User: " + currentUser);
        userLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        userLabel.setForeground(Color.WHITE);

        panel.add(titleLabel, BorderLayout.WEST);
        panel.add(userLabel, BorderLayout.EAST);

        return panel;
    }
}
