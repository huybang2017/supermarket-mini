package SieuThiMiniGUI;

import DTO.HangSanXuatDTO;
import SieuThiMiniBUS.HangSanXuatBUS;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;

public class HangSanXuatGUI extends JPanel {
    
    private DefaultTableModel model;
    private JTable tblHangSanXuat;
    private HangSanXuatBUS hsxBUS = new HangSanXuatBUS();
    private JTextField txtSearch;
    
    private Color secondaryColor = new Color(108, 117, 125);
    private Color bgColor = new Color(244, 246, 249);

    public HangSanXuatGUI() {
        initComponents();
        docDSHSX(); 
    }

    private void initComponents() {
        this.setLayout(new BorderLayout(20, 20));
        this.setBackground(bgColor);
        this.setBorder(new EmptyBorder(20, 25, 20, 25));

        JLabel lblTitle = new JLabel("Quản Lý Hãng Sản Xuất");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        this.add(lblTitle, BorderLayout.NORTH);

        JPanel card = new JPanel(new BorderLayout(15, 15));
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(new LineBorder(new Color(230, 230, 230), 1), new EmptyBorder(15, 15, 15, 15)));

        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setOpaque(false);

        // Thanh tìm kiếm
        txtSearch = new JTextField(" Tìm kiếm Hãng SX...");
        txtSearch.setPreferredSize(new Dimension(250, 38));
        txtSearch.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) { if(txtSearch.getText().equals(" Tìm kiếm Hãng SX...")) txtSearch.setText(""); }
            public void focusLost(FocusEvent e) { if(txtSearch.getText().isEmpty()) txtSearch.setText(" Tìm kiếm Hãng SX..."); }
        });
        txtSearch.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) { timKiem(); }
        });
        pnlHeader.add(txtSearch, BorderLayout.WEST);

        // Nút chức năng
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlButtons.setOpaque(false);
        JButton btnAdd = createActionBtn("+ Thêm Hãng SX");
        btnAdd.addActionListener(e -> showForm(null)); 
        JButton btnDelete = createActionBtn("- Xóa Hãng SX");
        btnDelete.addActionListener(e -> deleteSelectedHSX()); 
        pnlButtons.add(btnAdd); 
        pnlButtons.add(btnDelete);
        pnlHeader.add(pnlButtons, BorderLayout.EAST);

        card.add(pnlHeader, BorderLayout.NORTH);

        // Bảng dữ liệu
        String[] headers = {"Mã Hãng", "Tên Hãng Sản Xuất", "Số Điện Thoại", "Địa Chỉ", "Hành Động"};
        model = new DefaultTableModel(headers, 0) { @Override public boolean isCellEditable(int r, int c) { return false; } };
        tblHangSanXuat = new JTable(model);
        styleTable(tblHangSanXuat);

        tblHangSanXuat.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = tblHangSanXuat.getSelectedRow();
                    int id = Integer.parseInt(tblHangSanXuat.getValueAt(row, 0).toString());
                    for (HangSanXuatDTO hsx : HangSanXuatBUS.dshsx) {
                        if (hsx.getMaHang() == id) { showForm(hsx); break; }
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(tblHangSanXuat);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        card.add(scrollPane, BorderLayout.CENTER);

        this.add(card, BorderLayout.CENTER);
    }

    private void docDSHSX() {
        hsxBUS.docDSHSX();
        hienThiBang();
    }
    
    private void hienThiBang() {
        model.setRowCount(0);
        if(HangSanXuatBUS.dshsx != null) {
            for (HangSanXuatDTO hsx : HangSanXuatBUS.dshsx) {
                model.addRow(new Object[]{hsx.getMaHang(), hsx.getTenHang(), hsx.getSdt(), hsx.getDiaChi(), "⚙ Sửa"});
            }
        }
    }

    private void timKiem() {
        String kw = txtSearch.getText().toLowerCase();
        if(kw.equals(" tìm kiếm hãng sx...") || kw.isEmpty()) { hienThiBang(); return; }
        model.setRowCount(0);
        if (HangSanXuatBUS.dshsx != null) {
            for (HangSanXuatDTO hsx : HangSanXuatBUS.dshsx) {
                if (hsx.getTenHang().toLowerCase().contains(kw) || hsx.getSdt().contains(kw)) {
                    model.addRow(new Object[]{hsx.getMaHang(), hsx.getTenHang(), hsx.getSdt(), hsx.getDiaChi(), "⚙ Sửa"});
                }
            }
        }
    }

    private void deleteSelectedHSX() {
        int row = tblHangSanXuat.getSelectedRow();
        if (row != -1) {
            int id = Integer.parseInt(tblHangSanXuat.getValueAt(row, 0).toString());
            if (JOptionPane.showConfirmDialog(this, "Xóa Hãng Sản Xuất mã " + id + "?", "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                hsxBUS.xoa(id);
                JOptionPane.showMessageDialog(this, "Xoá thành công!");
                docDSHSX();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn Hãng Sản Xuất cần xóa!");
        }
    }

    private void showForm(HangSanXuatDTO hsx) {
        Window parentWindow = SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog((Frame)parentWindow, hsx == null ? "Thêm Hãng Sản Xuất" : "Sửa Hãng Sản Xuất", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel pnlForm = new JPanel(new GridLayout(3, 2, 10, 20));
        pnlForm.setBorder(new EmptyBorder(30, 30, 30, 30));
        pnlForm.setBackground(Color.WHITE);

        JTextField txtTen = new JTextField(hsx != null ? hsx.getTenHang() : "");
        JTextField txtSdt = new JTextField(hsx != null ? hsx.getSdt() : "");
        JTextField txtDiaChi = new JTextField(hsx != null ? hsx.getDiaChi() : "");

        pnlForm.add(new JLabel("Tên Hãng SX:")); pnlForm.add(txtTen);
        pnlForm.add(new JLabel("Số Điện Thoại:")); pnlForm.add(txtSdt);
        pnlForm.add(new JLabel("Địa Chỉ:")); pnlForm.add(txtDiaChi);

        JButton btnSave = createActionBtn("Lưu Thay Đổi");
        btnSave.addActionListener(e -> {
            HangSanXuatDTO newHsx = new HangSanXuatDTO();
            if (hsx != null) newHsx.setMaHang(hsx.getMaHang());
            newHsx.setTenHang(txtTen.getText());
            newHsx.setSdt(txtSdt.getText());
            newHsx.setDiaChi(txtDiaChi.getText());

            if (hsx == null) {
                hsxBUS.them(newHsx);
                JOptionPane.showMessageDialog(dialog, "Thêm thành công!");
            } else {
                hsxBUS.sua(newHsx);
                JOptionPane.showMessageDialog(dialog, "Sửa thành công!");
            }
            
            docDSHSX();
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