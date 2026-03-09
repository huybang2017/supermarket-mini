package SieuThiMiniGUI;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;

public class QuanLyKhachHangGUI extends JPanel {
    private DefaultTableModel model;
    private JTable tblKhachHang;
    
    // Bảng màu chuẩn
    private Color primaryColor = new Color(0, 123, 255);
    private Color secondaryColor = new Color(108, 117, 125);
    private Color bgColor = new Color(244, 246, 249);
    
    // Font chuẩn cho toàn bộ app
    private Font fontTitle = new Font("Segoe UI", Font.BOLD, 24);
    private Font fontPlain = new Font("Segoe UI", Font.PLAIN, 14);

    public QuanLyKhachHangGUI() { 
        initComponents(); 
    }

    private void initComponents() {
        this.setLayout(new BorderLayout(20, 20));
        this.setBackground(bgColor);
        this.setBorder(new EmptyBorder(20, 25, 20, 25));

        // 1. STYLE TIÊU ĐỀ Y HỆT SẢN PHẨM
        JLabel lblTitle = new JLabel("Quản Lý Khách Hàng");
        lblTitle.setFont(fontTitle);
        lblTitle.setForeground(new Color(40, 40, 40)); 
        this.add(lblTitle, BorderLayout.NORTH);

        JPanel card = new JPanel(new BorderLayout(15, 15));
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(new LineBorder(new Color(230, 230, 230), 1), new EmptyBorder(15, 15, 15, 15)));

        // --- HEADER ---
        JPanel pnlHeader = new JPanel();
        pnlHeader.setLayout(new BoxLayout(pnlHeader, BoxLayout.Y_AXIS));
        pnlHeader.setOpaque(false);

        JPanel topToolBar = new JPanel(new BorderLayout());
        topToolBar.setOpaque(false);

        // 2. STYLE THANH TÌM KIẾM CÓ HIỆU ỨNG CHỮ MỜ
        JPanel pnlSearchGroup = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlSearchGroup.setOpaque(false);
        
        JTextField txtSearch = new JTextField(" Tìm SDT hoặc Tên KH...");
        txtSearch.setPreferredSize(new Dimension(220, 38));
        txtSearch.setFont(fontPlain); 
        txtSearch.setForeground(Color.GRAY);
        
        // Sự kiện tạo Placeholder giống trang Sản phẩm
        txtSearch.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) { 
                if (txtSearch.getText().trim().equals("Tìm SDT hoặc Tên KH...")) { 
                    txtSearch.setText(""); 
                    txtSearch.setForeground(Color.BLACK); 
                } 
            }
            public void focusLost(FocusEvent e) { 
                if (txtSearch.getText().trim().isEmpty()) { 
                    txtSearch.setForeground(Color.GRAY); 
                    txtSearch.setText(" Tìm SDT hoặc Tên KH..."); 
                } 
            }
        });

        JButton btnAdvToggle = createActionBtn("▼");
        btnAdvToggle.setPreferredSize(new Dimension(45, 38));
        pnlSearchGroup.add(txtSearch); 
        pnlSearchGroup.add(btnAdvToggle);
        topToolBar.add(pnlSearchGroup, BorderLayout.WEST);

        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlButtons.setOpaque(false);
        pnlButtons.add(createActionBtn("+ Thêm KH")); 
        pnlButtons.add(createActionBtn("- Xóa KH"));
        topToolBar.add(pnlButtons, BorderLayout.EAST);

        // 3. STYLE FORM TÌM KIẾM NÂNG CAO
        JPanel pnlAdvSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        pnlAdvSearch.setBackground(new Color(248, 250, 252));
        pnlAdvSearch.setBorder(new MatteBorder(1, 0, 0, 0, new Color(230, 230, 230))); // Viền trên
        pnlAdvSearch.setVisible(false);
        
        // Nhớ set font cho các Label trong này
        JLabel lblTu = new JLabel("Điểm tích luỹ từ:"); lblTu.setFont(fontPlain);
        JLabel lblDen = new JLabel("Đến:"); lblDen.setFont(fontPlain);
        JTextField txtDiemTu = new JTextField(8); txtDiemTu.setFont(fontPlain);
        JTextField txtDiemDen = new JTextField(8); txtDiemDen.setFont(fontPlain);

        pnlAdvSearch.add(lblTu); pnlAdvSearch.add(txtDiemTu);
        pnlAdvSearch.add(lblDen); pnlAdvSearch.add(txtDiemDen);
        pnlAdvSearch.add(createActionBtn("Lọc")); 
        pnlAdvSearch.add(createActionBtn("Làm Mới"));

        btnAdvToggle.addActionListener(e -> {
            pnlAdvSearch.setVisible(!pnlAdvSearch.isVisible());
            btnAdvToggle.setText(pnlAdvSearch.isVisible() ? "▼" : "▲");
            card.revalidate();
        });

        pnlHeader.add(topToolBar); 
        pnlHeader.add(Box.createVerticalStrut(10)); 
        pnlHeader.add(pnlAdvSearch);
        card.add(pnlHeader, BorderLayout.NORTH);

        // --- BẢNG DỮ LIỆU ---
        String[] headers = {"Mã KH", "Tên Khách Hàng", "Số Điện Thoại", "Điểm Tích Lũy", "Hành Động"};
        model = new DefaultTableModel(headers, 0) { @Override public boolean isCellEditable(int r, int c) { return false; } };
        tblKhachHang = new JTable(model);
        styleTable(tblKhachHang);

        JScrollPane scrollPane = new JScrollPane(tblKhachHang);
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