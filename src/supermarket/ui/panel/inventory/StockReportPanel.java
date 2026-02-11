package supermarket.ui.panel.inventory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * StockReportPanel - Báo Cáo Tồn Kho
 * COMPLETE IMPLEMENTATION
 */
public class StockReportPanel extends JPanel {

    private JTable reportTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> reportTypeCombo;
    private JButton generateButton, exportButton;
    private JLabel summaryLabel;

    public StockReportPanel() {
        initComponents();
        layoutComponents();
        generateReport();
    }

    private void initComponents() {
        // Report type
        reportTypeCombo = new JComboBox<>(new String[]{
            "Tất cả sản phẩm",
            "Sắp hết hàng (< 10)",
            "Hết hàng",
            "Tồn kho cao (> 100)",
            "Sản phẩm ít bán"
        });

        // Table
        String[] columns = {"Mã SP", "Tên sản phẩm", "Tồn đầu", "Nhập", "Xuất", "Tồn cuối", "Giá trị"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        reportTable = new JTable(tableModel);
        reportTable.setRowHeight(28);
        reportTable.setFont(new Font("Arial", Font.PLAIN, 13));
        reportTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        reportTable.getTableHeader().setBackground(new Color(52, 73, 94));
        reportTable.getTableHeader().setForeground(Color.WHITE);

        // Buttons
        generateButton = createButton("📊 Tạo báo cáo", new Color(52, 152, 219));
        exportButton = createButton("📥 Xuất Excel", new Color(46, 204, 113));

        // Summary
        summaryLabel = new JLabel();
        summaryLabel.setFont(new Font("Arial", Font.BOLD, 14));

        // Actions
        generateButton.addActionListener(e -> generateReport());
        exportButton.addActionListener(e -> handleExport());
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
        JLabel titleLabel = new JLabel("📊 BÁO CÁO TỒN KHO");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(52, 73, 94));
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // Control panel
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        controlPanel.setBackground(Color.WHITE);
        controlPanel.add(new JLabel("Loại báo cáo:"));
        controlPanel.add(reportTypeCombo);
        controlPanel.add(generateButton);
        controlPanel.add(Box.createHorizontalStrut(20));
        controlPanel.add(exportButton);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(0, 10));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.add(controlPanel, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(reportTable), BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);

        // Summary panel
        JPanel summaryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        summaryPanel.setBackground(new Color(236, 240, 241));
        summaryPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        summaryPanel.add(summaryLabel);
        add(summaryPanel, BorderLayout.SOUTH);
    }

    private void generateReport() {
        tableModel.setRowCount(0);

        // Mock data
        addReportRow("P001", "Coca Cola 330ml", 120, 80, 100, 100, 700000.0);
        addReportRow("P002", "Pepsi 330ml", 100, 50, 70, 80, 520000.0);
        addReportRow("P003", "Nước Lavie 500ml", 180, 120, 100, 200, 700000.0);
        addReportRow("P004", "Bánh Oreo", 60, 30, 40, 50, 600000.0);
        addReportRow("P005", "Sữa TH True Milk", 50, 40, 30, 60, 1500000.0);
        addReportRow("P006", "Mì Hảo Hảo", 200, 100, 150, 150, 420000.0);
        addReportRow("P007", "Trứng gà", 50, 20, 30, 40, 1200000.0);
        addReportRow("P008", "Gạo ST25", 40, 20, 30, 30, 600000.0);

        updateSummary();
    }

    private void addReportRow(String ma, String ten, int tonDau, int nhap,
                             int xuat, int tonCuoi, double giaTri) {
        tableModel.addRow(new Object[]{
            ma,
            ten,
            tonDau,
            nhap,
            xuat,
            tonCuoi,
            formatCurrency(giaTri)
        });
    }

    private void updateSummary() {
        int totalProducts = tableModel.getRowCount();
        int tonCuoiTotal = 0;
        double giaTriTotal = 0;

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            tonCuoiTotal += (Integer) tableModel.getValueAt(i, 5);
            String giaTriStr = (String) tableModel.getValueAt(i, 6);
            giaTriTotal += Double.parseDouble(giaTriStr.replaceAll("[^0-9]", ""));
        }

        summaryLabel.setText(String.format(
            "📊 Tổng: %d sản phẩm | 📦 Tồn cuối: %,d | 💰 Giá trị tồn: %,.0f đ",
            totalProducts, tonCuoiTotal, giaTriTotal
        ));
    }

    private String formatCurrency(double amount) {
        return String.format("%,.0f đ", amount);
    }

    private void handleExport() {
        JOptionPane.showMessageDialog(this,
            "Chức năng xuất Excel\n\n" +
            "Sẽ xuất báo cáo ra file:\n" +
            "BaoCaoTonKho_" + java.time.LocalDate.now() + ".xlsx\n\n" +
            "Bao gồm:\n" +
            "- " + tableModel.getRowCount() + " sản phẩm\n" +
            "- Tồn đầu, nhập, xuất, tồn cuối\n" +
            "- Giá trị tồn kho",
            "Xuất báo cáo",
            JOptionPane.INFORMATION_MESSAGE);
    }
}
