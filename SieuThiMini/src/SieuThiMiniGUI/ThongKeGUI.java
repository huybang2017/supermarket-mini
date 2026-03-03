package SieuThiMiniGUI;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;

public class ThongKeGUI extends JPanel {
    
    private Color secondaryColor = new Color(108, 117, 125);
    private Color bgColor = new Color(244, 246, 249);
    private Font fontTitle = new Font("Segoe UI", Font.BOLD, 24);

    public ThongKeGUI() {
        this.setLayout(new BorderLayout(20, 20));
        this.setBackground(bgColor);
        this.setBorder(new EmptyBorder(20, 25, 20, 25));

        JLabel lblTitle = new JLabel("Thống Kê & Báo Cáo");
        lblTitle.setFont(fontTitle);
        lblTitle.setForeground(new Color(40, 40, 40));
        this.add(lblTitle, BorderLayout.NORTH);

        JPanel pnlContent = new JPanel(new BorderLayout(20, 20));
        pnlContent.setOpaque(false);

        // 1. Dãy Card tổng quan phía trên
        JPanel pnlCards = new JPanel(new GridLayout(1, 4, 15, 0));
        pnlCards.setOpaque(false);
        pnlCards.add(createStatCard("Tổng Doanh Thu", "150,000,000 đ", new Color(40, 167, 69)));
        pnlCards.add(createStatCard("Đơn Hàng Hôm Nay", "42 Đơn", new Color(0, 123, 255)));
        pnlCards.add(createStatCard("Sản Phẩm Sắp Hết", "15 SP", new Color(220, 53, 69)));
        pnlCards.add(createStatCard("Tổng Khách Hàng", "1,240 KH", new Color(253, 126, 20)));
        pnlContent.add(pnlCards, BorderLayout.NORTH);

        // 2. Bảng sản phẩm bán chạy ở dưới
        JPanel pnlTable = new JPanel(new BorderLayout(10, 10));
        pnlTable.setBackground(Color.WHITE);
        pnlTable.setBorder(new CompoundBorder(new LineBorder(new Color(230, 230, 230), 1), new EmptyBorder(15, 15, 15, 15)));
        
        JLabel lblSubTitle = new JLabel("Top Sản Phẩm Bán Chạy Trong Tháng");
        lblSubTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblSubTitle.setForeground(new Color(40, 40, 40));
        pnlTable.add(lblSubTitle, BorderLayout.NORTH);

        String[] headers = {"Xếp Hạng", "Mã SP", "Tên Sản Phẩm", "Số Lượng Bán", "Doanh Thu"};
        DefaultTableModel tkModel = new DefaultTableModel(headers, 0);
        // Dữ liệu mẫu
        tkModel.addRow(new Object[]{"#1", "SP002", "Dầu Gội Sunsilk", "120", "6,600,000 đ"});
        tkModel.addRow(new Object[]{"#2", "SP005", "Bánh Cosy", "95", "4,275,000 đ"});
        
        JTable tblTk = new JTable(tkModel);
        styleTable(tblTk);
        
        JScrollPane scrollPane = new JScrollPane(tblTk);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        pnlTable.add(scrollPane, BorderLayout.CENTER);

        pnlContent.add(pnlTable, BorderLayout.CENTER);
        this.add(pnlContent, BorderLayout.CENTER);
    }

    // Hàm tạo Card thống kê
    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(new LineBorder(new Color(230, 230, 230), 1), new EmptyBorder(20, 15, 20, 15)));
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setForeground(secondaryColor);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        JLabel lblValue = new JLabel(value);
        lblValue.setForeground(color);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 22));
        
        card.add(lblTitle, BorderLayout.NORTH);
        card.add(lblValue, BorderLayout.CENTER);
        return card;
    }

    // --- 2 HÀM NÀY QUYẾT ĐỊNH STYLE CỦA BẢNG VÀ NÚT ---
    private void styleTable(JTable t) {
        t.setRowHeight(45);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Font thân bảng
        t.setGridColor(new Color(245, 245, 245));
        t.setShowVerticalLines(false); 
        t.setSelectionBackground(new Color(240, 247, 255));
        t.setSelectionForeground(Color.BLACK);
        t.getTableHeader().setReorderingAllowed(false);

        JTableHeader header = t.getTableHeader();
        header.setPreferredSize(new Dimension(0, 45));
        header.setBackground(new Color(250, 251, 252));
        header.setFont(new Font("Segoe UI", Font.BOLD, 13)); // Font Header bảng
        header.setForeground(secondaryColor);
        ((DefaultTableCellRenderer)header.getDefaultRenderer()).setHorizontalAlignment(JLabel.LEFT);
    }

    private JButton createActionBtn(String text) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(140, 38));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13)); // Font của Nút
        btn.setFocusPainted(false);
        btn.setBackground(new Color(226, 232, 240)); 
        btn.setForeground(Color.BLACK); 
        btn.setBorder(new LineBorder(new Color(203, 213, 225), 1)); 
        return btn;
    }
}