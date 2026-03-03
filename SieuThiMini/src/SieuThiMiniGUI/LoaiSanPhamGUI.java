package SieuThiMiniGUI;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;

public class LoaiSanPhamGUI extends JPanel {
    private DefaultTableModel model;
    private JTable tblLoaiSP;
    
    private Color primaryColor = new Color(0, 123, 255);
    private Color secondaryColor = new Color(108, 117, 125);
    private Color bgColor = new Color(244, 246, 249);
    private Font fontTitle = new Font("Segoe UI", Font.BOLD, 24);
    private Font fontPlain = new Font("Segoe UI", Font.PLAIN, 14);

    public LoaiSanPhamGUI() { initComponents(); }

    private void initComponents() {
        this.setLayout(new BorderLayout(20, 20));
        this.setBackground(bgColor);
        this.setBorder(new EmptyBorder(20, 25, 20, 25));

        JLabel lblTitle = new JLabel("Quản Lý Loại Sản Phẩm");
        lblTitle.setFont(fontTitle);
        lblTitle.setForeground(new Color(40, 40, 40));
        this.add(lblTitle, BorderLayout.NORTH);

        JPanel card = new JPanel(new BorderLayout(15, 15));
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(new LineBorder(new Color(230, 230, 230), 1), new EmptyBorder(15, 15, 15, 15)));

        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setOpaque(false);

        JPanel pnlSearchGroup = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlSearchGroup.setOpaque(false);
        JTextField txtSearch = new JTextField(" Tìm mã hoặc tên loại...");
        txtSearch.setPreferredSize(new Dimension(220, 38));
        txtSearch.setFont(fontPlain);
        txtSearch.setForeground(Color.GRAY);
        txtSearch.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) { if (txtSearch.getText().trim().equals("Tìm mã hoặc tên loại...")) { txtSearch.setText(""); txtSearch.setForeground(Color.BLACK); } }
            public void focusLost(FocusEvent e) { if (txtSearch.getText().trim().isEmpty()) { txtSearch.setForeground(Color.GRAY); txtSearch.setText(" Tìm mã hoặc tên loại..."); } }
        });
        pnlSearchGroup.add(txtSearch);
        pnlHeader.add(pnlSearchGroup, BorderLayout.WEST);

        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlButtons.setOpaque(false);
        pnlButtons.add(createActionBtn("+ Thêm Loại SP"));
        pnlButtons.add(createActionBtn("- Xóa Loại SP"));
        pnlHeader.add(pnlButtons, BorderLayout.EAST);

        card.add(pnlHeader, BorderLayout.NORTH);

        String[] headers = {"Mã Loại", "Tên Loại", "Mô Tả", "Hành Động"};
        model = new DefaultTableModel(headers, 0) { @Override public boolean isCellEditable(int r, int c) { return false; } };
        tblLoaiSP = new JTable(model);
        styleTable(tblLoaiSP);

        JScrollPane scrollPane = new JScrollPane(tblLoaiSP);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        card.add(scrollPane, BorderLayout.CENTER);

        this.add(card, BorderLayout.CENTER);
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