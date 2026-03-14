package SieuThiMiniGUI;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;

import DTO.LoaiSanPhamDTO;
import SieuThiMiniBUS.LoaiSanPhamBUS;

public class LoaiSanPhamGUI extends JPanel {
    private DefaultTableModel model;
    private JTable tblLoaiSP;
    private JTextField txtSearch;
    private LoaiSanPhamBUS lspBUS = new LoaiSanPhamBUS();
    
    private Color secondaryColor = new Color(108, 117, 125);
    private Color bgColor = new Color(244, 246, 249);
    private Font fontTitle = new Font("Segoe UI", Font.BOLD, 24);
    private Font fontPlain = new Font("Segoe UI", Font.PLAIN, 14);

    public LoaiSanPhamGUI() { 
        initComponents(); 
        docDSLSP();
    }

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

        // ===== THANH TÌM KIẾM =====
        JPanel pnlSearchGroup = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlSearchGroup.setOpaque(false);
        txtSearch = new JTextField(" Tìm mã hoặc tên loại...");
        txtSearch.setPreferredSize(new Dimension(220, 38));
        txtSearch.setFont(fontPlain);
        txtSearch.setForeground(Color.GRAY);
        
        txtSearch.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) { 
                if (txtSearch.getText().trim().equals("Tìm mã hoặc tên loại...")) { 
                    txtSearch.setText(""); txtSearch.setForeground(Color.BLACK); 
                } 
            }
            public void focusLost(FocusEvent e) { 
                if (txtSearch.getText().trim().isEmpty()) { 
                    txtSearch.setForeground(Color.GRAY); txtSearch.setText(" Tìm mã hoặc tên loại..."); 
                } 
            }
        });
        
        // Sự kiện gõ phím để tìm kiếm
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                timKiem();
            }
        });
        
        pnlSearchGroup.add(txtSearch);
        pnlHeader.add(pnlSearchGroup, BorderLayout.WEST);

        // ===== NÚT CHỨC NĂNG =====
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlButtons.setOpaque(false);
        
        JButton btnThem = createActionBtn("+ Thêm Loại SP");
        btnThem.addActionListener(e -> openForm(null)); // null tức là Thêm mới
        
        JButton btnXoa = createActionBtn("- Xóa Loại SP");
        btnXoa.addActionListener(e -> xoaLoaiSP());
        
        pnlButtons.add(btnThem);
        pnlButtons.add(btnXoa);
        pnlHeader.add(pnlButtons, BorderLayout.EAST);

        card.add(pnlHeader, BorderLayout.NORTH);

        // ===== BẢNG DỮ LIỆU =====
        String[] headers = {"Mã Loại", "Tên Loại", "Mã Hãng", "Hành Động"};
        model = new DefaultTableModel(headers, 0) { 
            @Override public boolean isCellEditable(int r, int c) { return false; } 
        };
        tblLoaiSP = new JTable(model);
        styleTable(tblLoaiSP);
        
        // Sự kiện click đúp để sửa
        tblLoaiSP.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 || tblLoaiSP.getSelectedColumn() == 3) {
                    int row = tblLoaiSP.getSelectedRow();
                    if (row >= 0) {
                        int ma = Integer.parseInt(model.getValueAt(row, 0).toString());
                        LoaiSanPhamDTO lsp = lspBUS.getLoaiSP(ma);
                        if (lsp != null) openForm(lsp);
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(tblLoaiSP);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        card.add(scrollPane, BorderLayout.CENTER);

        this.add(card, BorderLayout.CENTER);
    }

    private void docDSLSP() {
        lspBUS.docDSLSP();
        hienThiBang();
    }
    
    private void hienThiBang() {
        model.setRowCount(0);
        if(LoaiSanPhamBUS.dslsp != null) {
            for (LoaiSanPhamDTO lsp : LoaiSanPhamBUS.dslsp) {
                model.addRow(new Object[]{lsp.getMaLoai(), lsp.getTenLoai(), lsp.getHang(), "⚙ Sửa"});
            }
        }
    }

    private void timKiem() {
        String query = txtSearch.getText().toLowerCase().trim();
        if (query.equals("tìm mã hoặc tên loại...") || query.isEmpty()) {
            hienThiBang();
            return;
        }
        
        model.setRowCount(0);
        for (LoaiSanPhamDTO lsp : LoaiSanPhamBUS.dslsp) {
            if (String.valueOf(lsp.getMaLoai()).contains(query) || 
                lsp.getTenLoai().toLowerCase().contains(query)) {
                model.addRow(new Object[]{lsp.getMaLoai(), lsp.getTenLoai(), lsp.getHang(), "⚙ Sửa"});
            }
        }
    }

    // ===== POPUP FORM THÊM / SỬA =====
    private void openForm(LoaiSanPhamDTO lsp) {
        Window parent = SwingUtilities.getWindowAncestor(this);
        JDialog dlg = new JDialog((Frame) parent, lsp == null ? "Thêm Loại Sản Phẩm" : "Sửa Loại Sản Phẩm", true);
        dlg.setSize(380, 250);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout(10, 10));

        JPanel form = new JPanel(new GridLayout(3, 2, 10, 20));
        form.setBorder(new EmptyBorder(20, 20, 10, 20));
        form.setBackground(Color.WHITE);

        JTextField txtTen = new JTextField(lsp != null ? lsp.getTenLoai() : "");
        JTextField txtHang = new JTextField(lsp != null ? String.valueOf(lsp.getHang()) : "");

        form.add(new JLabel("Tên Loại SP *:")); form.add(txtTen);
        form.add(new JLabel("Mã Hãng SX *:")); form.add(txtHang);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(Color.WHITE);
        JButton btnSave = createActionBtn("Lưu");
        
        btnSave.addActionListener(e -> {
            try {
                if (txtTen.getText().trim().isEmpty() || txtHang.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(dlg, "Vui lòng nhập đầy đủ thông tin!");
                    return;
                }
                
                LoaiSanPhamDTO obj = new LoaiSanPhamDTO();
                obj.setTenLoai(txtTen.getText().trim());
                obj.setHang(Integer.parseInt(txtHang.getText().trim()));
                
                if (lsp == null) {
                    if (lspBUS.them(obj)) {
                        JOptionPane.showMessageDialog(dlg, "Thêm thành công!");
                        dlg.dispose();
                        hienThiBang();
                    }
                } else {
                    obj.setMaLoai(lsp.getMaLoai());
                    if (lspBUS.sua(obj)) {
                        JOptionPane.showMessageDialog(dlg, "Cập nhật thành công!");
                        dlg.dispose();
                        hienThiBang();
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dlg, "Mã hãng phải là số nguyên!");
            }
        });
        
        btnPanel.add(btnSave);
        dlg.add(form, BorderLayout.CENTER);
        dlg.add(btnPanel, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    private void xoaLoaiSP() {
        int row = tblLoaiSP.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn Loại SP cần xóa!");
            return;
        }
        
        int maLoai = Integer.parseInt(model.getValueAt(row, 0).toString());
        int ans = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa Loại SP mã " + maLoai + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        
        if (ans == JOptionPane.YES_OPTION) {
            if (lspBUS.xoa(maLoai)) {
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                hienThiBang();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại! Có thể do Loại SP đang ràng buộc với Sản Phẩm.");
            }
        }
    }

    // --- 2 HÀM NÀY QUYẾT ĐỊNH STYLE CỦA BẢNG VÀ NÚT ---
    private void styleTable(JTable t) {
        t.setRowHeight(45);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 14)); 
        t.setGridColor(new Color(245, 245, 245));
        t.setShowVerticalLines(false); 
        t.setSelectionBackground(new Color(240, 247, 255));
        t.setSelectionForeground(Color.BLACK);
        t.getTableHeader().setReorderingAllowed(false);

        JTableHeader header = t.getTableHeader();
        header.setPreferredSize(new Dimension(0, 45));
        header.setBackground(new Color(250, 251, 252));
        header.setFont(new Font("Segoe UI", Font.BOLD, 13)); 
        header.setForeground(secondaryColor);
        ((DefaultTableCellRenderer)header.getDefaultRenderer()).setHorizontalAlignment(JLabel.LEFT);
    }

    private JButton createActionBtn(String text) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(140, 38));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13)); 
        btn.setFocusPainted(false);
        btn.setBackground(new Color(226, 232, 240)); 
        btn.setForeground(Color.BLACK); 
        btn.setBorder(new LineBorder(new Color(203, 213, 225), 1)); 
        return btn;
    }
}