package SieuThiMiniGUI;

import DTO.NhaCungCapDTO;
import SieuThiMiniBUS.NhaCungCapBUS;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;

public class NhaCungCapGUI extends JPanel {
    
    private DefaultTableModel model;
    private JTable tblNhaCungCap;
    private NhaCungCapBUS nccBUS = new NhaCungCapBUS();
    private JTextField txtSearch;
    
    private Color secondaryColor = new Color(108, 117, 125);
    private Color bgColor = new Color(244, 246, 249);

    public NhaCungCapGUI() {
        initComponents();
        docDSNCC(); 
    }

    private void initComponents() {
        this.setLayout(new BorderLayout(20, 20));
        this.setBackground(bgColor);
        this.setBorder(new EmptyBorder(20, 25, 20, 25));

        JLabel lblTitle = new JLabel("Quản Lý Nhà Cung Cấp");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        this.add(lblTitle, BorderLayout.NORTH);

        JPanel card = new JPanel(new BorderLayout(15, 15));
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(new LineBorder(new Color(230, 230, 230), 1), new EmptyBorder(15, 15, 15, 15)));

        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setOpaque(false);

        // Thanh tìm kiếm
        txtSearch = new JTextField(" Tìm kiếm NCC...");
        txtSearch.setPreferredSize(new Dimension(250, 38));
        txtSearch.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) { if(txtSearch.getText().equals(" Tìm kiếm NCC...")) txtSearch.setText(""); }
            public void focusLost(FocusEvent e) { if(txtSearch.getText().isEmpty()) txtSearch.setText(" Tìm kiếm NCC..."); }
        });
        txtSearch.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) { timKiem(); }
        });
        pnlHeader.add(txtSearch, BorderLayout.WEST);

        // Nút chức năng
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlButtons.setOpaque(false);
        JButton btnAdd = createActionBtn("+ Thêm NCC");
        btnAdd.addActionListener(e -> showForm(null)); 
        JButton btnDelete = createActionBtn("- Xóa NCC");
        btnDelete.addActionListener(e -> deleteSelectedNCC()); 
        pnlButtons.add(btnAdd); 
        pnlButtons.add(btnDelete);
        pnlHeader.add(pnlButtons, BorderLayout.EAST);

        card.add(pnlHeader, BorderLayout.NORTH);

        // Bảng dữ liệu
        String[] headers = {"Mã NCC", "Tên Nhà Cung Cấp", "Số Điện Thoại", "Địa Chỉ", "Hành Động"};
        model = new DefaultTableModel(headers, 0) { @Override public boolean isCellEditable(int r, int c) { return false; } };
        tblNhaCungCap = new JTable(model);
        styleTable(tblNhaCungCap);

        tblNhaCungCap.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = tblNhaCungCap.getSelectedRow();
                    int id = Integer.parseInt(tblNhaCungCap.getValueAt(row, 0).toString());
                    for (NhaCungCapDTO ncc : NhaCungCapBUS.dsncc) {
                        if (ncc.getMaNCC() == id) { showForm(ncc); break; }
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(tblNhaCungCap);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        card.add(scrollPane, BorderLayout.CENTER);

        this.add(card, BorderLayout.CENTER);
    }

    private void docDSNCC() {
        nccBUS.docDSNCC();
        hienThiBang();
    }
    
    private void hienThiBang() {
        model.setRowCount(0);
        if(NhaCungCapBUS.dsncc != null) {
            for (NhaCungCapDTO ncc : NhaCungCapBUS.dsncc) {
                model.addRow(new Object[]{ncc.getMaNCC(), ncc.getTenNCC(), ncc.getSdt(), ncc.getDiaChi(), "⚙ Sửa"});
            }
        }
    }

    private void timKiem() {
        String kw = txtSearch.getText().toLowerCase();
        if(kw.equals(" tìm kiếm ncc...") || kw.isEmpty()) { hienThiBang(); return; }
        model.setRowCount(0);
        NhaCungCapBUS nccBUS = new NhaCungCapBUS();
        NhaCungCapDTO ncc = new NhaCungCapDTO();
        nccBUS.timNhaCungCap(kw , ncc);
        model.addRow(new Object[]{ncc.getMaNCC(), ncc.getTenNCC(), ncc.getSdt(), ncc.getDiaChi(), "⚙ Sửa"});
        }

    private void deleteSelectedNCC() {
        int row = tblNhaCungCap.getSelectedRow();
        if (row != -1) {
            int id = Integer.parseInt(tblNhaCungCap.getValueAt(row, 0).toString());
            if (JOptionPane.showConfirmDialog(this, "Xóa NCC mã " + id + "?", "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                // Đã đồng bộ thành nccBUS.xoa()
                nccBUS.xoa(id);
                JOptionPane.showMessageDialog(this, "Xoá thành công!");
                docDSNCC();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn NCC cần xóa!");
        }
    }

    private void showForm(NhaCungCapDTO ncc) {
        Window parentWindow = SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog((Frame)parentWindow, ncc == null ? "Thêm NCC" : "Sửa NCC", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel pnlForm = new JPanel(new GridLayout(3, 2, 10, 20));
        pnlForm.setBorder(new EmptyBorder(30, 30, 30, 30));
        pnlForm.setBackground(Color.WHITE);

        JTextField txtTen = new JTextField(ncc != null ? ncc.getTenNCC() : "");
        JTextField txtSdt = new JTextField(ncc != null ? ncc.getSdt() : "");
        JTextField txtDiaChi = new JTextField(ncc != null ? ncc.getDiaChi() : "");

        pnlForm.add(new JLabel("Tên NCC:")); pnlForm.add(txtTen);
        pnlForm.add(new JLabel("Số Điện Thoại:")); pnlForm.add(txtSdt);
        pnlForm.add(new JLabel("Địa Chỉ:")); pnlForm.add(txtDiaChi);

        JButton btnSave = createActionBtn("Lưu Thay Đổi");
        btnSave.addActionListener(e -> {
            NhaCungCapDTO newNcc = new NhaCungCapDTO();
            if (ncc != null) newNcc.setMaNCC(ncc.getMaNCC());
            newNcc.setTenNCC(txtTen.getText());
            // Đã fix lỗi cú pháp ở đây
            newNcc.setSdt(txtSdt.getText());
            newNcc.setDiaChi(txtDiaChi.getText());

            // Đã đồng bộ thành nccBUS.them() và nccBUS.sua()
            if (ncc == null) {
                nccBUS.them(newNcc);
                JOptionPane.showMessageDialog(dialog, "Thêm thành công!");
            } else {
                nccBUS.sua(newNcc);
                JOptionPane.showMessageDialog(dialog, "Sửa thành công!");
            }
            
            docDSNCC();
            dialog.dispose();
        });
        
        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlBottom.setBackground(Color.WHITE);
        pnlBottom.add(btnSave);
        
        dialog.add(pnlForm, BorderLayout.CENTER);
        dialog.add(pnlBottom, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void styleTable(JTable t) {
        t.setRowHeight(45); t.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        t.setGridColor(new Color(245, 245, 245)); t.setShowVerticalLines(false);
        t.getTableHeader().setPreferredSize(new Dimension(0, 45));
        t.getTableHeader().setBackground(new Color(250, 251, 252));
        t.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        ((DefaultTableCellRenderer)t.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.LEFT);
    }

    private JButton createActionBtn(String text) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(140, 38)); btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(new Color(226, 232, 240)); btn.setBorder(new LineBorder(new Color(203, 213, 225), 1));
        return btn;
    }
}