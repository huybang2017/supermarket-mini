package SieuThiMiniGUI;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import javax.swing.table.*;

import DTO.ChiTietPhieuNhapHangDTO;
import DTO.NhaCungCapDTO;
import DTO.PhieuNhapHangDTO;
import DTO.SanPhamDTO;
import SieuThiMiniBUS.ChiTietPhieuNhapHangBUS;
import SieuThiMiniBUS.NhaCungCapBUS;
import SieuThiMiniBUS.PhieuNhapHangBUS;
import SieuThiMiniBUS.SanPhamBUS;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Vector;

public class QuanLyNhapHangGUI extends JPanel {
    private DefaultTableModel model;
    private JTable tblNhapHang;
    
    private PhieuNhapHangBUS pnBUS = new PhieuNhapHangBUS();
    private NhaCungCapBUS nccBUS = new NhaCungCapBUS();
    private ChiTietPhieuNhapHangBUS ctBUS = new ChiTietPhieuNhapHangBUS();
    private SanPhamBUS spBUS = new SanPhamBUS();

    private Color secondaryColor = new Color(108, 117, 125);
    private Color bgColor = new Color(244, 246, 249);
    private Font fontTitle = new Font("Segoe UI", Font.BOLD, 24);
    private Font fontPlain = new Font("Segoe UI", Font.PLAIN, 14);

    public QuanLyNhapHangGUI() { 
        initComponents();
        docDSPN();
     }

    private void initComponents() {
        this.setLayout(new BorderLayout(20, 20));
        this.setBackground(bgColor);
        this.setBorder(new EmptyBorder(20, 25, 20, 25));

        JLabel lblTitle = new JLabel("Quản Lý Nhập Hàng");
        lblTitle.setFont(fontTitle);
        lblTitle.setForeground(new Color(40, 40, 40));
        this.add(lblTitle, BorderLayout.NORTH);

        JPanel card = new JPanel(new BorderLayout(15, 15));
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(new LineBorder(new Color(230, 230, 230), 1), new EmptyBorder(15, 15, 15, 15)));

        JPanel pnlHeader = new JPanel();
        pnlHeader.setLayout(new BoxLayout(pnlHeader, BoxLayout.Y_AXIS));
        pnlHeader.setOpaque(false);

        JPanel topToolBar = new JPanel(new BorderLayout());
        topToolBar.setOpaque(false);

        JPanel pnlSearchGroup = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlSearchGroup.setOpaque(false);
        JTextField txtSearch = new JTextField(" Tìm mã phiếu, nhà cung cấp...");
        txtSearch.setPreferredSize(new Dimension(220, 38));
        txtSearch.setFont(fontPlain);
        txtSearch.setForeground(Color.GRAY);
        txtSearch.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) { if (txtSearch.getText().trim().equals("Tìm mã phiếu, nhà cung cấp...")) { txtSearch.setText(""); txtSearch.setForeground(Color.BLACK); } }
            public void focusLost(FocusEvent e) { if (txtSearch.getText().trim().isEmpty()) { txtSearch.setForeground(Color.GRAY); txtSearch.setText(" Tìm mã phiếu, nhà cung cấp..."); } }
        });
        
        JButton btnAdvToggle = createActionBtn("▼");
        btnAdvToggle.setPreferredSize(new Dimension(45, 38));
        pnlSearchGroup.add(txtSearch); pnlSearchGroup.add(btnAdvToggle);
        topToolBar.add(pnlSearchGroup, BorderLayout.WEST);

        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlButtons.setOpaque(false);
        topToolBar.add(pnlButtons, BorderLayout.EAST);

        JButton btnThem = createActionBtn("Tạo Phiếu Mới");
        btnThem.addActionListener(e -> openForm(null,null));

        JButton btnXoa = createActionBtn("Xóa Phiếu");
        btnXoa.addActionListener(e -> deletePhieu());
        
        JButton btnSua = createActionBtn("Sửa Phiếu");
        btnSua.addActionListener(e -> editPhieu());

        pnlButtons.add(btnThem);
        pnlButtons.add(btnSua);
        pnlButtons.add(btnXoa);


        JPanel pnlAdvSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        pnlAdvSearch.setBackground(new Color(248, 250, 252));
        pnlAdvSearch.setBorder(new MatteBorder(1, 0, 0, 0, new Color(230, 230, 230)));
        pnlAdvSearch.setVisible(false);
        
        JLabel lblTu = new JLabel("Từ ngày:"); lblTu.setFont(fontPlain);
        JLabel lblDen = new JLabel("Đến ngày:"); lblDen.setFont(fontPlain);
        JTextField txtTu = new JTextField(8); txtTu.setFont(fontPlain);
        JTextField txtDen = new JTextField(8); txtDen.setFont(fontPlain);
        
        pnlAdvSearch.add(lblTu); pnlAdvSearch.add(txtTu);
        pnlAdvSearch.add(lblDen); pnlAdvSearch.add(txtDen);
        pnlAdvSearch.add(createActionBtn("Lọc")); pnlAdvSearch.add(createActionBtn("Làm Mới"));

        btnAdvToggle.addActionListener(e -> {
            pnlAdvSearch.setVisible(!pnlAdvSearch.isVisible());
            btnAdvToggle.setText(pnlAdvSearch.isVisible() ? "▼" : "▲");
            card.revalidate();
        });

        JButton btnLoc = createActionBtn("Lọc");
        btnLoc.addActionListener(e -> filterNangCao(txtTu,txtDen));

        JButton btnLamMoi = createActionBtn("Làm Mới");
        btnLamMoi.addActionListener(e -> {
            txtSearch.setText(" Tìm mã phiếu, nhà cung cấp...");
            txtTu.setText("");
            txtDen.setText("");
            docDSPN(); 
        });

        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filterCoBan(txtSearch);
            }
        });

        

        pnlHeader.add(topToolBar);
        pnlHeader.add(Box.createVerticalStrut(10));
        pnlHeader.add(pnlAdvSearch);
        card.add(pnlHeader, BorderLayout.NORTH);

        String[] headers = {"Mã Phiếu", "Nhà Cung Cấp", "Người Lập", "Ngày Nhập", "Tổng Tiền", "Hành Động"};
        model = new DefaultTableModel(headers, 0) { @Override public boolean isCellEditable(int r, int c) { return false; } };
        tblNhapHang = new JTable(model);
        styleTable(tblNhapHang);


        JScrollPane scrollPane = new JScrollPane(tblNhapHang);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        card.add(scrollPane, BorderLayout.CENTER);

        this.add(card, BorderLayout.CENTER);

        tblNhapHang.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    showDetail();
                }
            }
        });

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
    

    // --- CÁC HÀM XỬ LÝ LOGIC ---

 

    public void docDSPN() {
        pnBUS.docDSPN();
        model.setRowCount(0);
        for (PhieuNhapHangDTO pn : PhieuNhapHangBUS.dspn) {
            Vector<Object> row = new Vector<>();
            row.add(pn.getMaPNH());
            row.add(pn.getMaNCC());
            row.add(pn.getMaNV());
            row.add(pn.getNgayNhap());
            row.add(pn.getTongTien());
            model.addRow(row);
        }
    }

    private void filterCoBan(JTextField txtSearch) {
        String query = txtSearch.getText().toLowerCase().trim();
        if (query.equals("tìm mã phiếu, nhà cung cấp...")) {
            return;
        }
    
        model.setRowCount(0);
        for (PhieuNhapHangDTO pn : PhieuNhapHangBUS.dspn) {
            String maPN = String.valueOf(pn.getMaPNH()).toLowerCase();
            String maNCC = String.valueOf(pn.getMaNCC()).toLowerCase();
            
            if (maPN.contains(query) || maNCC.contains(query)) {
                Vector<Object> row = new Vector<>();
                row.add(pn.getMaPNH());
                row.add(pn.getMaNCC());
                row.add(pn.getMaNV());
                row.add(pn.getNgayNhap());
                row.add(pn.getTongTien());
                model.addRow(row);
            }
        }
    }
    private void filterNangCao(JTextField txtTu, JTextField txtDen) {
        try {
            String strTu = txtTu.getText().trim();
            String strDen = txtDen.getText().trim();
    
            if (strTu.isEmpty() || strDen.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ khoảng ngày (yyyy-mm-dd)");
                return;
            }
    
            java.sql.Date dateTu = java.sql.Date.valueOf(strTu);
            java.sql.Date dateDen = java.sql.Date.valueOf(strDen);
    
            model.setRowCount(0);
            for (PhieuNhapHangDTO pn : PhieuNhapHangBUS.dspn) {
                java.sql.Date ngayNhap = (java.sql.Date) pn.getNgayNhap();
                
                if (!ngayNhap.before(dateTu) && !ngayNhap.after(dateDen)) {
                    Vector<Object> row = new Vector<>();
                    row.add(pn.getMaPNH());
                    row.add(pn.getMaNCC());
                    row.add(pn.getMaNV());
                    row.add(pn.getNgayNhap());
                    row.add(pn.getTongTien());
                    model.addRow(row);
                }
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Định dạng ngày không hợp lệ! Vui lòng dùng: yyyy-mm-dd");
        }
    }
    

    private void showDetail() {
        int i = tblNhapHang.getSelectedRow();
        if (i >= 0) {
            int maPN = Integer.parseInt(model.getValueAt(i, 0).toString());
            ArrayList<ChiTietPhieuNhapHangDTO> dsct = ctBUS.timTheoMaPN(maPN);
            
            JDialog detailDlg = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Chi Tiết Phiếu Nhập: " + maPN, true);
            detailDlg.setSize(600, 400);
            detailDlg.setLayout(new BorderLayout());

            String[] ctHeaders = {"Mã SP", "Số Lượng", "Đơn Giá", "Thành Tiền"};
            DefaultTableModel ctModel = new DefaultTableModel(ctHeaders, 0);
            for (ChiTietPhieuNhapHangDTO ct : dsct) {
                ctModel.addRow(new Object[]{ct.getMaSP(), ct.getSoLuong(), ct.getDonGia(), (ct.getSoLuong() * ct.getDonGia())});
            }
            
            JTable tblCT = new JTable(ctModel);
            styleTable(tblCT);
            detailDlg.add(new JScrollPane(tblCT), BorderLayout.CENTER);
            detailDlg.setLocationRelativeTo(this);
            detailDlg.setVisible(true);
        }
    }

    private void calcThanhTien(JTextField txtSL, JTextField txtDG, JTextField txtTT) {
        try {
            int sl = Integer.parseInt(txtSL.getText().trim());
            double dg = Double.parseDouble(txtDG.getText().trim());
            double tt = sl * dg;
            txtTT.setText(String.valueOf(tt));
        } catch (NumberFormatException e) {
            txtTT.setText("0");
        }
    }

    private void openForm(PhieuNhapHangDTO pn, ChiTietPhieuNhapHangDTO ct) {
        Window parentWindow = SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog((Frame) parentWindow, pn == null ? "Thêm Phiếu Nhập" : "Sửa Phiếu Nhập", true);
        dialog.setSize(500, 450);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
    
        JPanel pnlForm = new JPanel(new GridLayout(8, 2, 10, 25));
        pnlForm.setBorder(new EmptyBorder(30, 30, 30, 30));
        pnlForm.setBackground(Color.WHITE);
    
        

        JTextField txtID = new JTextField(pn != null ? String.valueOf(pn.getMaPNH()) : "1"); 
        JTextField txtMaNV = new JTextField(pn != null ? String.valueOf(pn.getMaNV()) : "1");
        JTextField txtNgay = new JTextField(pn != null ? pn.getNgayNhap().toString() : new java.sql.Date(System.currentTimeMillis()).toString());
        JTextField txtTongTien = new JTextField(pn != null ? String.valueOf(pn.getTongTien()) : "0");
        JTextField txtDonGia = new JTextField(ct != null ? String.valueOf(ct.getDonGia()) : "0");
        JTextField txtSoLuong = new JTextField(ct != null ? String.valueOf(ct.getSoLuong()) : "0");
        
        nccBUS.docDSNCC();
        JComboBox<String> cbNCC = new JComboBox<>();
        cbNCC.addItem("-- Chọn Nhà Cung Cấp --");
        for (NhaCungCapDTO ncc : NhaCungCapBUS.dsncc) {
            cbNCC.addItem(ncc.getMaNCC() + " - " + ncc.getTenNCC());
        }

        spBUS.docDSSP();
        JComboBox<String> cbSP = new JComboBox<>();
        cbSP.addItem("-- Chọn Sản Phẩm --");
        for (SanPhamDTO spt : SanPhamBUS.dssp) {
            cbSP.addItem(spt.getMasanpham() + " - " + spt.getTensanpham());
        }
    
        txtSoLuong.getDocument().addDocumentListener(new DocumentListener() {
            
        public void changedUpdate(DocumentEvent e) { calculate(); }
        public void removeUpdate(DocumentEvent e) { calculate(); }
        public void insertUpdate(DocumentEvent e) { calculate(); }

        private void calculate() {
            calcThanhTien(txtSoLuong, txtDonGia, txtTongTien);
        }
        });

        txtDonGia.getDocument().addDocumentListener(new DocumentListener() {
        public void changedUpdate(DocumentEvent e) { calculate(); }
        public void removeUpdate(DocumentEvent e) { calculate(); }
        public void insertUpdate(DocumentEvent e) { calculate(); }

        private void calculate() {
            calcThanhTien(txtSoLuong, txtDonGia, txtTongTien);
        }
        });
        
        if (pn != null) {
            txtID.setEditable(false);
            txtID.setText(String.valueOf(pn.getMaPNH())); 
            
            for (int i = 0; i < cbNCC.getItemCount(); i++) {
                String maNCCStr = String.valueOf(pn.getMaNCC());
                
                if (cbNCC.getItemAt(i).startsWith(maNCCStr + " -")) { 
                    cbNCC.setSelectedIndex(i);
                    break; 
                }
            }
        }
    
        pnlForm.add(new JLabel("Mã Phiếu Nhập:")); pnlForm.add(txtID);
        pnlForm.add(new JLabel("Nhà Cung Cấp:")); pnlForm.add(cbNCC);
        pnlForm.add(new JLabel("Mã Nhân Viên:")); pnlForm.add(txtMaNV);
        pnlForm.add(new JLabel("Chọn Sản Phẩm:")); pnlForm.add(cbSP);
        pnlForm.add(new JLabel("Ngày Nhập (yyyy-mm-dd):")); pnlForm.add(txtNgay);
        pnlForm.add(new JLabel("Tổng Tiền:")); pnlForm.add(txtTongTien);
        pnlForm.add(new JLabel("Đơn Giá:")); pnlForm.add(txtDonGia);
        pnlForm.add(new JLabel("Số Lượng:")); pnlForm.add(txtSoLuong);

    
        JButton btnSave = new JButton(pn == null ? "Thêm Phiếu" : "Lưu Thay Đổi");
        btnSave.setBackground(new Color(40, 167, 69)); 
        btnSave.setForeground(Color.WHITE);
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        btnSave.addActionListener(e -> {
            try {
                if (cbNCC.getSelectedIndex() == 0) {
                    JOptionPane.showMessageDialog(dialog, "Vui lòng chọn Nhà cung cấp!");
                    return;
                }
    
                PhieuNhapHangDTO newPn = new PhieuNhapHangDTO();
                newPn.setMaPNH(Integer.parseInt((txtID.getText())));
                newPn.setMaNCC(Integer.parseInt(cbNCC.getSelectedItem().toString().split(" - ")[0]));
                newPn.setMaNV(Integer.parseInt(txtMaNV.getText()));
                newPn.setNgayNhap(java.sql.Date.valueOf(txtNgay.getText())); 
                newPn.setTongTien(Double.parseDouble(txtTongTien.getText()));

                ChiTietPhieuNhapHangDTO newCt = new ChiTietPhieuNhapHangDTO();
                newCt.setMaPNH(Integer.parseInt((txtID.getText())));
                newCt.setMaSP(Integer.parseInt(cbSP.getSelectedItem().toString().split(" - ")[0]));
                newCt.setDonGia((int) Double.parseDouble(txtDonGia.getText()));
                newCt.setSoLuong(Integer.parseInt(txtSoLuong.getText()));
    
                if (pn == null) {
                    pnBUS.them(newPn);
                    ctBUS.them(newCt);
                    JOptionPane.showMessageDialog(dialog, "Thêm phiếu nhập thành công!");
                } else {
                    pnBUS.sua(newPn); 
                    ArrayList<ChiTietPhieuNhapHangDTO> danhSachSua = new ArrayList<>();
                    danhSachSua.add(newCt); 
                    ctBUS.sua(newPn.getMaPNH(), danhSachSua); 
                    JOptionPane.showMessageDialog(dialog, "Cập nhật thành công!");
                }
                
                docDSPN(); 
                dialog.dispose();
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Lỗi dữ liệu: Kiểm tra định dạng ngày (yyyy-mm-dd) và tiền!");
                ex.printStackTrace();
            }
        });
    
        dialog.add(pnlForm, BorderLayout.CENTER);
        dialog.add(btnSave, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void deletePhieu() {
        int i = tblNhapHang.getSelectedRow();
        if (i >= 0) {
            int res = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa phiếu này và toàn bộ chi tiết?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (res == JOptionPane.YES_OPTION) {
                int maPN = Integer.parseInt(model.getValueAt(i, 0).toString());
                ctBUS.xoa(maPN);
                pnBUS.xoa(maPN);
                docDSPN();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phiếu cần xóa!");
        }
    }

    private void editPhieu() {
        int i = tblNhapHang.getSelectedRow();
        if (i >= 0) {
            int maPN = Integer.parseInt(tblNhapHang.getValueAt(i, 0).toString());
            PhieuNhapHangDTO pnChon = null;
            for (PhieuNhapHangDTO pn : PhieuNhapHangBUS.dspn) {
                if (pn.getMaPNH() == maPN) {
                    pnChon = pn;
                    break;
                }
            }
    
            ArrayList<ChiTietPhieuNhapHangDTO> dsChiTiet = ctBUS.timTheoMaPN(maPN);
            
            if (pnChon != null) {
                ChiTietPhieuNhapHangDTO ctDauTien = (dsChiTiet != null && !dsChiTiet.isEmpty()) ? dsChiTiet.get(0) : null;
                openForm(pnChon, ctDauTien); 
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phiếu cần sửa!");
        }
    }

    
}