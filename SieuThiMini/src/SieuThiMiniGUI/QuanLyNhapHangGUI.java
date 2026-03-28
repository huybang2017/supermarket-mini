package SieuThiMiniGUI;

import DTO.*;
import SieuThiMiniBUS.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class QuanLyNhapHangGUI extends JPanel {
    
    private DefaultTableModel modelPN;
    private DefaultTableModel modelCT;
    private JTable tblPhieuNhap;
    private JTable tblChiTiet;
    
    private JTextField txtSearch;
    private int selectedMaPN = -1; 
    
    private final Color primaryColor = new Color(0, 123, 255);
    private final Color secondaryColor = new Color(108, 117, 125);
    private final Color bgColor = new Color(244, 246, 249);
    private final Color dangerColor = new Color(220, 53, 69);

    public QuanLyNhapHangGUI() {
        initComponents();
        loadPhieuNhap(); 
        
        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                loadPhieuNhap(); 
            }
        });
    }

    private void initComponents() {
        setLayout(new BorderLayout(0, 15));
        setBackground(bgColor);
        setBorder(new EmptyBorder(20, 25, 20, 25));

        JLabel lblTitle = new JLabel("Quản Lý Nhập Hàng");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        add(lblTitle, BorderLayout.NORTH);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, buildPhieuNhapPanel(), buildChiTietPanel());
        split.setDividerLocation(300);
        split.setDividerSize(8);
        split.setBackground(bgColor);
        add(split, BorderLayout.CENTER);
    }

    private JPanel buildPhieuNhapPanel() {
        JPanel card = new JPanel(new BorderLayout(0, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(new LineBorder(new Color(230, 230, 230), 1), new EmptyBorder(15, 15, 10, 15)));

        JPanel toolbar = new JPanel(new BorderLayout());
        toolbar.setOpaque(false);

        JPanel pnlLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pnlLeft.setOpaque(false);
        txtSearch = new JTextField(" Tìm kiếm mã phiếu nhập...");
        txtSearch.setPreferredSize(new Dimension(200, 36));
        txtSearch.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) { if(txtSearch.getText().equals(" Tìm kiếm mã phiếu nhập...")) txtSearch.setText(""); }
            public void focusLost(FocusEvent e) { if(txtSearch.getText().isEmpty()) txtSearch.setText(" Tìm kiếm mã phiếu nhập..."); }
        });
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) { 
                timKiem(); 
            }
        });
        pnlLeft.add(txtSearch);

        JPanel pnlRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlRight.setOpaque(false);
        JButton btnAdd = createActionBtn("+ Nhập Hàng Mới");
        btnAdd.setBackground(new Color(40, 167, 69)); btnAdd.setForeground(Color.WHITE);
        btnAdd.addActionListener(e -> showDialogNhapHangMoi()); 

        JButton btnDel = createActionBtn("Xóa phiếu");
        btnDel.setBackground(new Color(40, 167, 69)); btnAdd.setForeground(Color.WHITE);
        btnDel.addActionListener(e -> showDialogXoaPhieu());

        JButton btnEdit = createActionBtn("Sửa Phiếu");
        btnEdit.addActionListener(e -> showDialogSuaPhieu()); 

        pnlRight.add(btnAdd);
        pnlRight.add(btnDel);
        pnlRight.add(btnEdit);
        
        toolbar.add(pnlLeft, BorderLayout.WEST);
        toolbar.add(pnlRight, BorderLayout.EAST);

        String[] headers = {"Mã PN", "Mã NV", "Mã NCC", "Ngày Nhập", "Tổng Tiền (VNĐ)"};
        modelPN = new DefaultTableModel(headers, 0) { @Override public boolean isCellEditable(int r, int c) { return false; } };
        tblPhieuNhap = new JTable(modelPN);
        styleTable(tblPhieuNhap);

        tblPhieuNhap.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblPhieuNhap.getSelectedRow() != -1) {
                selectedMaPN = Integer.parseInt(tblPhieuNhap.getValueAt(tblPhieuNhap.getSelectedRow(), 0).toString());
                loadChiTiet();
            }
        });

        JScrollPane scroll = new JScrollPane(tblPhieuNhap);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        card.add(toolbar, BorderLayout.NORTH);
        card.add(scroll, BorderLayout.CENTER);
        return card;
    }

    private JPanel buildChiTietPanel() {
        JPanel card = new JPanel(new BorderLayout(0, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(new LineBorder(new Color(230, 230, 230), 1), new EmptyBorder(10, 15, 10, 15)));

        JLabel lblTitle = new JLabel("Chi Tiết Phiếu Nhập");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(primaryColor);
        card.add(lblTitle, BorderLayout.NORTH);

        String[] headers = {"Mã SP", "Tên Sản Phẩm", "Số Lượng Nhập", "Giá Nhập (VNĐ)", "Thành Tiền (VNĐ)"};
        modelCT = new DefaultTableModel(headers, 0) { @Override public boolean isCellEditable(int r, int c) { return false; } };
        tblChiTiet = new JTable(modelCT);
        styleTable(tblChiTiet);

        JScrollPane scroll = new JScrollPane(tblChiTiet);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        card.add(scroll, BorderLayout.CENTER);
        return card;
    }

    private void loadPhieuNhap() {
        PhieuNhapHangBUS pnBus = new PhieuNhapHangBUS();
        pnBus.docDSPN(); 
        
        modelPN.setRowCount(0);
        if (PhieuNhapHangBUS.dspn != null) { 
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            for (PhieuNhapHangDTO pn : PhieuNhapHangBUS.dspn) {
                String ngay = pn.getNgayNhap() != null ? sdf.format(pn.getNgayNhap()) : "";
                modelPN.addRow(new Object[]{
                    pn.getMaPNH(), pn.getMaNV(), pn.getMaNCC(), ngay, String.format(java.util.Locale.US, "%,d", (long)pn.getTongTien())
                });
            }
        }
    }

    private void loadChiTiet() {
        modelCT.setRowCount(0);
        if (selectedMaPN == -1) return;
        
        ChiTietPhieuNhapHangBUS ctBus = new ChiTietPhieuNhapHangBUS();
        ArrayList<ChiTietPhieuNhapHangDTO> listCT = ctBus.timTheoMaPN(selectedMaPN); 
        
        if (listCT != null) {
            SanPhamBUS spBus = new SanPhamBUS();
            if (SanPhamBUS.dssp == null) spBus.docDSSP();
            
            for (ChiTietPhieuNhapHangDTO ct : listCT) {
                String tenSP = "Unknown";
                if (SanPhamBUS.dssp != null) {
                    for (SanPhamDTO sp : SanPhamBUS.dssp) {
                        if (sp.getMasanpham() == ct.getMaSP()) {
                            tenSP = sp.getTensanpham();
                            break;
                        }
                    }
                }
                modelCT.addRow(new Object[]{
                    ct.getMaSP(), tenSP, ct.getSoLuong(),
                    String.format(java.util.Locale.US, "%,d", ct.getDonGia()), 
                    String.format(java.util.Locale.US, "%,d", ct.getThanhTien())
                });
            }
        }
    }

    private void timKiem() {
        String query = txtSearch.getText().toLowerCase().trim();
        if (query.isEmpty() || query.contains("tìm")) {
            loadPhieuNhap(); // Sửa hienThiBang() thành loadPhieuNhap()
            return;
        }
        
        PhieuNhapHangBUS pnhBUS = new PhieuNhapHangBUS(); // Khởi tạo BUS
        ArrayList<PhieuNhapHangDTO> dsKetQua = pnhBUS.timPhieuNhapHang(query);
        
        modelPN.setRowCount(0); // Dùng modelPN thay vì model
        
        if (dsKetQua != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            for (PhieuNhapHangDTO pn : dsKetQua) {
                String ngay = pn.getNgayNhap() != null ? sdf.format(pn.getNgayNhap()) : "";
                // Đảm bảo addRow khớp với cột của hàm loadPhieuNhap()
                modelPN.addRow(new Object[]{
                    pn.getMaPNH(), pn.getMaNV(), pn.getMaNCC(), ngay, 
                    String.format(java.util.Locale.US, "%,d", (long)pn.getTongTien())
                });
            }
        }
    }            

    // ==========================================================
    // FORM TẠO PHIẾU NHẬP (GIỎ HÀNG NHO NHỎ) ĐÃ ĐƯỢC LÀM TO RA
    // ==========================================================
    private void showDialogNhapHangMoi() {
        JDialog dlg = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Lập Phiếu Nhập Hàng", true);
        // Nới rộng kích thước form để không bị che nút
        dlg.setSize(900, 600);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout());

        JPanel pnlTop = new JPanel(new GridLayout(2, 4, 10, 10));
        pnlTop.setBorder(new EmptyBorder(15, 20, 10, 20));
        pnlTop.setBackground(Color.WHITE);

        JComboBox<String> cbNCC = new JComboBox<>();
        NhaCungCapBUS nccBus = new NhaCungCapBUS(); nccBus.docDSNCC();
        if (NhaCungCapBUS.dsncc != null) {
            for (NhaCungCapDTO ncc : NhaCungCapBUS.dsncc) cbNCC.addItem(ncc.getMaNCC() + " - " + ncc.getTenNCC());
        }

        JComboBox<String> cbNV = new JComboBox<>();
        NhanVienBUS nvBus = new NhanVienBUS(); nvBus.docDSNV();
        if (NhanVienBUS.dsnv != null) {
            for (NhanVienDTO nv : NhanVienBUS.dsnv) cbNV.addItem(nv.getMaNV() + " - " + nv.getTenNV());
        }

        pnlTop.add(new JLabel("Nhà Cung Cấp:")); pnlTop.add(cbNCC);
        pnlTop.add(new JLabel("Nhân Viên Nhập:")); pnlTop.add(cbNV);

        JPanel pnlMiddle = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        pnlMiddle.setBackground(new Color(248, 250, 252));
        pnlMiddle.setBorder(new TitledBorder("Thêm Sản Phẩm Vào Phiếu"));

        JComboBox<String> cbSP = new JComboBox<>();
        // Set cứng kích thước để dàn hàng ngang đẹp
        cbSP.setPreferredSize(new Dimension(240, 36)); 
        SanPhamBUS spBus = new SanPhamBUS(); spBus.docDSSP();
        if (SanPhamBUS.dssp != null) {
            for (SanPhamDTO sp : SanPhamBUS.dssp) cbSP.addItem(sp.getMasanpham() + " - " + sp.getTensanpham());
        }

        JTextField txtSL = new JTextField("1");
        txtSL.setPreferredSize(new Dimension(80, 36));
        
        JTextField txtGiaNhap = new JTextField("0");
        txtGiaNhap.setPreferredSize(new Dimension(120, 36));

        cbSP.addActionListener(e -> {
            if (cbSP.getSelectedItem() != null) {
                int maSP = Integer.parseInt(cbSP.getSelectedItem().toString().split(" - ")[0]);
                for (SanPhamDTO sp : SanPhamBUS.dssp) {
                    if (sp.getMasanpham() == maSP) {
                        txtGiaNhap.setText(String.valueOf(Math.round(sp.getGiaNhap())));
                        break;
                    }
                }
            }
        });
        if(cbSP.getItemCount() > 0) cbSP.setSelectedIndex(0); 

        JButton btnAddCart = createActionBtn("Thêm Xuống Giỏ ⬇");
        btnAddCart.setBackground(primaryColor); btnAddCart.setForeground(Color.WHITE);

        pnlMiddle.add(new JLabel("Sản phẩm:")); pnlMiddle.add(cbSP);
        pnlMiddle.add(new JLabel("Số lượng:")); pnlMiddle.add(txtSL);
        pnlMiddle.add(new JLabel("Giá Nhập:")); pnlMiddle.add(txtGiaNhap);
        pnlMiddle.add(btnAddCart);

        String[] cartHeaders = {"Mã SP", "Tên SP", "Số Lượng", "Giá Nhập", "Thành Tiền"};
        DefaultTableModel cartModel = new DefaultTableModel(cartHeaders, 0);
        JTable tblCart = new JTable(cartModel);
        styleTable(tblCart);
        JScrollPane scrollCart = new JScrollPane(tblCart);
        scrollCart.setBorder(new EmptyBorder(10, 20, 10, 20));

        JLabel lblTongTien = new JLabel("Tổng Tiền: 0 VNĐ");
        lblTongTien.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTongTien.setForeground(dangerColor);

        btnAddCart.addActionListener(e -> {
            try {
                int maSP = Integer.parseInt(cbSP.getSelectedItem().toString().split(" - ")[0]);
                String tenSP = cbSP.getSelectedItem().toString().split(" - ")[1];
                int sl = Integer.parseInt(txtSL.getText().trim());
                long gia = Long.parseLong(txtGiaNhap.getText().trim());
                
                if (sl <= 0 || gia < 0) { JOptionPane.showMessageDialog(dlg, "Số lượng và giá phải hợp lệ!"); return; }

                long thanhTien = sl * gia;
                cartModel.addRow(new Object[]{maSP, tenSP, sl, gia, thanhTien});

                long tong = 0;
                for (int i = 0; i < cartModel.getRowCount(); i++) tong += Long.parseLong(cartModel.getValueAt(i, 4).toString());
                lblTongTien.setText("Tổng Tiền: " + String.format("%,d", tong) + " VNĐ");

            } catch (Exception ex) { JOptionPane.showMessageDialog(dlg, "Vui lòng nhập đúng số liệu!"); }
        });

        JPanel pnlBottom = new JPanel(new BorderLayout());
        pnlBottom.setBorder(new EmptyBorder(10, 20, 15, 20));
        pnlBottom.setBackground(Color.WHITE);
        pnlBottom.add(lblTongTien, BorderLayout.WEST);

        JButton btnSavePN = createActionBtn("LƯU PHIẾU NHẬP");
        btnSavePN.setBackground(dangerColor); btnSavePN.setForeground(Color.WHITE);
        btnSavePN.addActionListener(e -> {
            if (cartModel.getRowCount() == 0) { JOptionPane.showMessageDialog(dlg, "Phiếu nhập chưa có sản phẩm nào!"); return; }
            
            try {
                int maNCC = Integer.parseInt(cbNCC.getSelectedItem().toString().split(" - ")[0]);
                int maNV = Integer.parseInt(cbNV.getSelectedItem().toString().split(" - ")[0]);
                long tongTienDB = 0;
                for (int i = 0; i < cartModel.getRowCount(); i++) tongTienDB += Long.parseLong(cartModel.getValueAt(i, 4).toString());

                PhieuNhapHangBUS pnBus = new PhieuNhapHangBUS();
                int nextId = pnBus.getNextID(); 
                
                PhieuNhapHangDTO pn = new PhieuNhapHangDTO();
                pn.setMaPNH(nextId);
                pn.setMaNV(maNV);
                pn.setMaNCC(maNCC);
                pn.setNgayNhap(new Date());
                pn.setTongTien(tongTienDB);
                
                pnBus.them(pn); 
                
                ChiTietPhieuNhapHangBUS ctBus = new ChiTietPhieuNhapHangBUS();
                
                for (int i = 0; i < cartModel.getRowCount(); i++) {
                    int msp = (int) cartModel.getValueAt(i, 0);
                    int sl = (int) cartModel.getValueAt(i, 2);
                    long gn = (long) cartModel.getValueAt(i, 3);
                    
                    ChiTietPhieuNhapHangDTO ct = new ChiTietPhieuNhapHangDTO();
                    ct.setMaPNH(nextId);  
                    ct.setMaSP(msp);      
                    ct.setSoLuong(sl);
                    ct.setDonGia(gn);     
                    
                    ctBus.them(ct); 
                    
                    for (SanPhamDTO sp : SanPhamBUS.dssp) {
                        if (sp.getMasanpham() == msp) {
                            spBus.capNhatSoLuong(msp, sp.getSoluong() + sl);
                            break;
                        }
                    }
                }

                JOptionPane.showMessageDialog(dlg, "Đã lưu phiếu nhập thành công!");
                loadPhieuNhap();
                dlg.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dlg, "Đã xảy ra lỗi: " + ex.getMessage());
            }
        });

        pnlBottom.add(btnSavePN, BorderLayout.EAST);
        JPanel pnlWrapper = new JPanel(new BorderLayout());
        pnlWrapper.add(pnlTop, BorderLayout.NORTH);
        pnlWrapper.add(pnlMiddle, BorderLayout.CENTER);

        dlg.add(pnlWrapper, BorderLayout.NORTH);
        dlg.add(scrollCart, BorderLayout.CENTER);
        dlg.add(pnlBottom, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    private void showDialogSuaPhieu() {
        int row = tblPhieuNhap.getSelectedRow();
        if(row == -1) { JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 Phiếu nhập để sửa!"); return; }

        int maPN = Integer.parseInt(modelPN.getValueAt(row, 0).toString());
        int oldMaNV = Integer.parseInt(modelPN.getValueAt(row, 1).toString());
        int oldMaNCC = Integer.parseInt(modelPN.getValueAt(row, 2).toString());

        JDialog dlg = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Sửa Phiếu Nhập #" + maPN, true);
        dlg.setSize(400, 250);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(2, 2, 10, 20));
        form.setBorder(new EmptyBorder(30, 30, 30, 30));
        form.setBackground(Color.WHITE);

        JComboBox<String> cbNCC = new JComboBox<>();
        if (NhaCungCapBUS.dsncc != null) {
            for (NhaCungCapDTO ncc : NhaCungCapBUS.dsncc) cbNCC.addItem(ncc.getMaNCC() + " - " + ncc.getTenNCC());
        }

        JComboBox<String> cbNV = new JComboBox<>();
        if (NhanVienBUS.dsnv != null) {
            for (NhanVienDTO nv : NhanVienBUS.dsnv) cbNV.addItem(nv.getMaNV() + " - " + nv.getTenNV());
        }

        for (int i = 0; i < cbNCC.getItemCount(); i++) {
            if (cbNCC.getItemAt(i).startsWith(oldMaNCC + " - ")) { cbNCC.setSelectedIndex(i); break; }
        }
        for (int i = 0; i < cbNV.getItemCount(); i++) {
            if (cbNV.getItemAt(i).startsWith(oldMaNV + " - ")) { cbNV.setSelectedIndex(i); break; }
        }

        form.add(new JLabel("Sửa Nhà Cung Cấp:")); form.add(cbNCC);
        form.add(new JLabel("Sửa Nhân Viên:")); form.add(cbNV);

        JButton btnSave = createActionBtn("Cập Nhật");
        btnSave.addActionListener(e -> {
            int newMaNCC = Integer.parseInt(cbNCC.getSelectedItem().toString().split(" - ")[0]);
            int newMaNV = Integer.parseInt(cbNV.getSelectedItem().toString().split(" - ")[0]);
            
            PhieuNhapHangDTO pnUpdate = new PhieuNhapHangDTO();
            pnUpdate.setMaPNH(maPN);
            pnUpdate.setMaNV(newMaNV);
            pnUpdate.setMaNCC(newMaNCC);
            
            for(PhieuNhapHangDTO old : PhieuNhapHangBUS.dspn){
                if(old.getMaPNH() == maPN){
                    pnUpdate.setNgayNhap(old.getNgayNhap());
                    pnUpdate.setTongTien(old.getTongTien());
                    break;
                }
            }
            
            PhieuNhapHangBUS pnBus = new PhieuNhapHangBUS();
            pnBus.sua(pnUpdate); 
            
            JOptionPane.showMessageDialog(dlg, "Cập nhật phiếu thành công!");
            loadPhieuNhap();
            dlg.dispose();
        });

        JPanel pnlBot = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlBot.setBackground(Color.WHITE); pnlBot.add(btnSave);
        dlg.add(form, BorderLayout.CENTER); dlg.add(pnlBot, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    private void showDialogXoaPhieu() {
        try{
        int selectedRow = tblPhieuNhap.getSelectedRow();
    
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phiếu nhập để xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int maPN = (int) tblPhieuNhap.getValueAt(selectedRow, 0); 
    
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Bạn có chắc chắn muốn xóa phiếu nhập có mã: " + maPN + "?", 
            "Xác nhận xóa", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.QUESTION_MESSAGE);
    
        if (confirm == JOptionPane.YES_OPTION) {
            PhieuNhapHangBUS bus = new PhieuNhapHangBUS();
            bus.xoa(maPN);
            ((DefaultTableModel) tblPhieuNhap.getModel()).removeRow(selectedRow);
            JOptionPane.showMessageDialog(this, "Xóa phiếu nhập thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }}catch(Exception ex){
            JOptionPane.showMessageDialog(this, "Xóa phiếu nhập thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
    }

    private void styleTable(JTable t) {
        t.setRowHeight(40); t.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        t.setGridColor(new Color(245, 245, 245)); t.setShowVerticalLines(false);
        t.setSelectionBackground(new Color(240, 247, 255)); t.setSelectionForeground(Color.BLACK);
        t.getTableHeader().setPreferredSize(new Dimension(0, 42));
        t.getTableHeader().setBackground(new Color(250, 251, 252));
        t.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        t.getTableHeader().setForeground(secondaryColor);
        ((DefaultTableCellRenderer) t.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.LEFT);
    }

    private JButton createActionBtn(String text) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(140, 36)); btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false); btn.setBackground(new Color(226, 232, 240));
        btn.setBorder(new LineBorder(new Color(203, 213, 225), 1)); 
        return btn;
    }
}