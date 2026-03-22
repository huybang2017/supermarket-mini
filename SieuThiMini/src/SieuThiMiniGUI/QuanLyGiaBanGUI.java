package SieuThiMiniGUI;

import DTO.SanPhamDTO;
import SieuThiMiniBUS.SanPhamBUS;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Locale;

public class QuanLyGiaBanGUI extends JPanel {
    private DefaultTableModel model;
    private JTable tblGiaBan;
    private JTextField txtSearch;
    
    private Color secondaryColor = new Color(108, 117, 125);
    private Color bgColor = new Color(244, 246, 249);

    public QuanLyGiaBanGUI() { 
        initComponents(); 
        docDSGiaBan(); 
    }

    private void initComponents() {
        this.setLayout(new BorderLayout(20, 20));
        this.setBackground(bgColor);
        this.setBorder(new EmptyBorder(20, 25, 20, 25));

        JLabel lblTitle = new JLabel("Quản Lý Giá Bán");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        this.add(lblTitle, BorderLayout.NORTH);

        JPanel card = new JPanel(new BorderLayout(15, 15));
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(new LineBorder(new Color(230, 230, 230), 1), new EmptyBorder(15, 15, 15, 15)));

        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setOpaque(false);

        // ===== THANH TÌM KIẾM (Đã fix hiệu ứng chữ mờ giống mẫu) =====
        JPanel pnlSearchGroup = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlSearchGroup.setOpaque(false);
        txtSearch = new JTextField(" Tìm mã hoặc tên sản phẩm...");
        txtSearch.setPreferredSize(new Dimension(250, 38));
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtSearch.setForeground(Color.GRAY);
        txtSearch.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) { 
                if(txtSearch.getText().equals(" Tìm mã hoặc tên sản phẩm...")) { 
                    txtSearch.setText(""); 
                    txtSearch.setForeground(Color.BLACK); 
                } 
            }
            public void focusLost(FocusEvent e) { 
                if(txtSearch.getText().isEmpty()) { 
                    txtSearch.setText(" Tìm mã hoặc tên sản phẩm..."); 
                    txtSearch.setForeground(Color.GRAY); 
                } 
            }
        });
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) { timKiem(); }
        });
        pnlSearchGroup.add(txtSearch);
        pnlHeader.add(pnlSearchGroup, BorderLayout.WEST);
        card.add(pnlHeader, BorderLayout.NORTH);

        // ===== BẢNG DỮ LIỆU =====
        String[] headers = {"Mã SP", "Tên Sản Phẩm", "Giá Nhập Vô (VNĐ)", "Lợi Nhuận (%)", "Giá Bán Ra (VNĐ)", "Hành Động"};
        model = new DefaultTableModel(headers, 0) { @Override public boolean isCellEditable(int r, int c) { return false; } };
        tblGiaBan = new JTable(model);
        
        // Gọi hàm styleTable chuẩn mẫu
        styleTable(tblGiaBan);

        tblGiaBan.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = tblGiaBan.getSelectedRow();
                    if (row != -1) { 
                        int id = Integer.parseInt(tblGiaBan.getValueAt(row, 0).toString());
                        String ten = tblGiaBan.getValueAt(row, 1).toString();
                        
                        double giaNhap = 0, loiNhuan = 0;
                        if (SanPhamBUS.dssp != null) {
                            for (SanPhamDTO sp : SanPhamBUS.dssp) {
                                if (sp.getMasanpham() == id) {
                                    giaNhap = sp.getGiaNhap(); 
                                    loiNhuan = sp.getLoiNhuan(); 
                                    break;
                                }
                            }
                        }
                        showEditPriceForm(id, ten, giaNhap, loiNhuan);
                    }
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tblGiaBan);
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); // Xóa viền bao quanh cuộn giống mẫu
        scrollPane.getViewport().setBackground(Color.WHITE);
        card.add(scrollPane, BorderLayout.CENTER);
        this.add(card, BorderLayout.CENTER);
        
        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override public void componentShown(java.awt.event.ComponentEvent e) { docDSGiaBan(); }
        });
    }

    private void docDSGiaBan() {
        new SanPhamBUS().docDSSP();
        model.setRowCount(0);
        if(SanPhamBUS.dssp != null) {
            for (SanPhamDTO sp : SanPhamBUS.dssp) {
                model.addRow(new Object[]{
                    sp.getMasanpham(), sp.getTensanpham(), 
                    Math.round(sp.getGiaNhap()), sp.getLoiNhuan(), Math.round(sp.getDongia()), "⚙ Chỉnh Giá"
                });
            }
        }
    }

    private void timKiem() {
        String kw = txtSearch.getText().toLowerCase().trim();
        if (kw.equals(" tìm mã hoặc tên sản phẩm...") || kw.isEmpty()) { docDSGiaBan(); return; }
        model.setRowCount(0);
        if (SanPhamBUS.dssp != null) {
            for (SanPhamDTO sp : SanPhamBUS.dssp) {
                if (String.valueOf(sp.getMasanpham()).contains(kw) || sp.getTensanpham().toLowerCase().contains(kw)) {
                    model.addRow(new Object[]{sp.getMasanpham(), sp.getTensanpham(), Math.round(sp.getGiaNhap()), sp.getLoiNhuan(), Math.round(sp.getDongia()), "⚙ Chỉnh Giá"});
                }
            }
        }
    }

    private void showEditPriceForm(int id, String ten, double giaNhapHienTai, double loiNhuanHienTai) {
        Window parentWindow = SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog((Frame)parentWindow, "Chỉnh Sửa Giá Bán - " + ten, true);
        dialog.setSize(450, 250); 
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel pnlForm = new JPanel(new GridLayout(3, 2, 15, 20));
        pnlForm.setBorder(new EmptyBorder(25, 35, 25, 35)); 
        pnlForm.setBackground(Color.WHITE);

        JTextField txtGiaNhap = new JTextField(String.valueOf(Math.round(giaNhapHienTai)));
        JTextField txtLoiNhuan = new JTextField(String.format(Locale.US, "%.1f", loiNhuanHienTai));
        
        JTextField txtGiaBan = new JTextField();
        txtGiaBan.setEditable(false); 
        txtGiaBan.setBackground(new Color(240, 240, 240));
        txtGiaBan.setFont(new Font("Segoe UI", Font.BOLD, 14));
        txtGiaBan.setForeground(new Color(220, 53, 69)); 

        pnlForm.add(new JLabel("Giá Nhập Vô (VNĐ):")); pnlForm.add(txtGiaNhap);
        pnlForm.add(new JLabel("Lợi Nhuận (%):")); pnlForm.add(txtLoiNhuan);
        pnlForm.add(new JLabel("Giá Bán Ra (VNĐ):")); pnlForm.add(txtGiaBan);

        Runnable calcPrice = () -> {
            try {
                String strGN = txtGiaNhap.getText().trim();
                String strLN = txtLoiNhuan.getText().trim();
                if (strGN.isEmpty() || strLN.isEmpty()) { txtGiaBan.setText("0"); return; }

                long gn = Long.parseLong(strGN);
                double ln = Double.parseDouble(strLN);
                long gb = Math.round(gn + (gn * ln / 100.0));
                txtGiaBan.setText(String.valueOf(gb));
            } catch (Exception ex) { txtGiaBan.setText("Lỗi số!"); }
        };

        txtGiaNhap.getDocument().addDocumentListener(new SimpleDocumentListener(calcPrice));
        txtLoiNhuan.getDocument().addDocumentListener(new SimpleDocumentListener(calcPrice));
        calcPrice.run(); 

        JButton btnSave = createActionBtn("Lưu Thay Đổi");
        btnSave.addActionListener(e -> {
            try {
                long gn = Long.parseLong(txtGiaNhap.getText().trim());
                double ln = Double.parseDouble(txtLoiNhuan.getText().trim());
                long gb = Long.parseLong(txtGiaBan.getText().trim());

                SanPhamDTO spUpdate = null;
                for (SanPhamDTO sp : SanPhamBUS.dssp) {
                    if (sp.getMasanpham() == id) { spUpdate = sp; break; }
                }

                if (spUpdate != null) {
                    spUpdate.setGiaNhap(gn);
                    spUpdate.setLoiNhuan(ln);
                    spUpdate.setDongia(gb); 

                    new SanPhamBUS().sua(spUpdate); 
                    JOptionPane.showMessageDialog(dialog, "Cập nhật thành công!");
                    docDSGiaBan(); 
                    dialog.dispose(); 
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Dữ liệu không hợp lệ!");
            }
        });        
        
        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlBottom.setBackground(Color.WHITE);
        pnlBottom.add(btnSave);
        dialog.add(pnlForm, BorderLayout.CENTER);
        dialog.add(pnlBottom, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    // ===== HÀM ĐỊNH DẠNG CHUẨN MẪU =====
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

    private class SimpleDocumentListener implements DocumentListener {
        private Runnable action;
        public SimpleDocumentListener(Runnable action) { this.action = action; }
        public void insertUpdate(DocumentEvent e) { action.run(); }
        public void removeUpdate(DocumentEvent e) { action.run(); }
        public void changedUpdate(DocumentEvent e) { action.run(); }
    }
}