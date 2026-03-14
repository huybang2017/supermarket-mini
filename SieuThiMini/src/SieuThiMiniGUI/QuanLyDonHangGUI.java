package SieuThiMiniGUI;

import DTO.ChiTietHoaDonDTO;
import DTO.HoaDonDTO;
import DTO.KhachHangDTO;
import DTO.NhanVienDTO;
import DTO.SanPhamDTO;
import SieuThiMiniBUS.ChiTietHoaDonBUS;
import SieuThiMiniBUS.HoaDonBUS;
import SieuThiMiniBUS.KhachHangBUS;
import SieuThiMiniBUS.NhanVienBUS;
import SieuThiMiniBUS.SanPhamBUS;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class QuanLyDonHangGUI extends JPanel {
    
    // ===== BUS INSTANCES =====
    private final HoaDonBUS hoaDonBUS = new HoaDonBUS();
    private final ChiTietHoaDonBUS chiTietBUS = new ChiTietHoaDonBUS();
    private final SanPhamBUS sanPhamBUS = new SanPhamBUS();
    
    // ===== TABLE MODELS =====
    private DefaultTableModel modelHD;
    private DefaultTableModel modelCT;
    
    // ===== TABLES =====
    private JTable tblHoaDon;
    private JTable tblChiTiet;
    
    // ===== FIELDS =====
    private JTextField txtSearch;
    private int selectedMaHD = -1;
    
    // ===== COLORS & FONTS =====
    private final Color primaryColor  = new Color(0, 123, 255);
    private final Color dangerColor   = new Color(220, 53, 69);
    private final Color successColor  = new Color(40, 167, 69);
    private final Color secondaryColor= new Color(108, 117, 125);
    private final Color bgColor       = new Color(244, 246, 249);

    public QuanLyDonHangGUI() { 
        initComponents(); 
        loadHoaDon();
        
        // Khởi tạo danh sách sản phẩm nếu chưa có
        if (SanPhamBUS.dssp == null) {
            sanPhamBUS.docDSSP();
        }
    }

    private void initComponents() {
        setLayout(new BorderLayout(0, 15));
        setBackground(bgColor);
        setBorder(new EmptyBorder(20, 25, 20, 25));

        JLabel lblTitle = new JLabel("Quản Lý Đơn Hàng");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(40, 40, 40));
        add(lblTitle, BorderLayout.NORTH);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                buildHoaDonPanel(), buildChiTietPanel());
        split.setDividerLocation(320);
        split.setDividerSize(8);
        split.setBackground(bgColor);
        add(split, BorderLayout.CENTER);
    }

    private JPanel buildHoaDonPanel() {
        JPanel card = new JPanel(new BorderLayout(0, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
                new LineBorder(new Color(230, 230, 230), 1),
                new EmptyBorder(15, 15, 10, 15)));

        JPanel toolbar = new JPanel(new BorderLayout(10, 0));
        toolbar.setOpaque(false);

        JPanel searchGroup = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        searchGroup.setOpaque(false);
        JLabel lblSection = new JLabel("Hóa Đơn");
        lblSection.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblSection.setForeground(primaryColor);

        txtSearch = new JTextField(16);
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtSearch.setPreferredSize(new Dimension(180, 36));

        JButton btnSearch = createActionBtn("Tìm");
        btnSearch.setPreferredSize(new Dimension(80, 36));
        btnSearch.addActionListener(e -> timKiemHoaDon());
        txtSearch.addActionListener(e -> timKiemHoaDon());

        JButton btnRefresh = createActionBtn("Làm Mới");
        btnRefresh.setPreferredSize(new Dimension(100, 36));
        btnRefresh.addActionListener(e -> { txtSearch.setText(""); loadHoaDon(); });

        searchGroup.add(lblSection);
        searchGroup.add(Box.createHorizontalStrut(10));
        searchGroup.add(txtSearch);
        searchGroup.add(btnSearch);
        searchGroup.add(btnRefresh);
        toolbar.add(searchGroup, BorderLayout.WEST);

        JPanel btnGroup = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btnGroup.setOpaque(false);

        JButton btnAdd = createActionBtn("+ Tạo Đơn");
        btnAdd.setPreferredSize(new Dimension(120, 36));
        btnAdd.addActionListener(e -> showDialogTaoHoaDon());

        JButton btnDelete = createActionBtn("Xóa Đơn");
        btnDelete.setPreferredSize(new Dimension(100, 36));
        btnDelete.addActionListener(e -> xoaHoaDon());

        btnGroup.add(btnAdd);
        btnGroup.add(btnDelete);
        toolbar.add(btnGroup, BorderLayout.EAST);

        String[] hdHeaders = {"Mã HĐ", "Tên NV", "Tên KH", "Ngày Lập", "Tổng Tiền (VNĐ)"};
        modelHD = new DefaultTableModel(hdHeaders, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblHoaDon = new JTable(modelHD);
        styleTable(tblHoaDon);

        tblHoaDon.getColumnModel().getColumn(0).setPreferredWidth(70);
        tblHoaDon.getColumnModel().getColumn(1).setPreferredWidth(70);
        tblHoaDon.getColumnModel().getColumn(2).setPreferredWidth(70);
        tblHoaDon.getColumnModel().getColumn(3).setPreferredWidth(130);
        tblHoaDon.getColumnModel().getColumn(4).setPreferredWidth(160);

        DefaultTableCellRenderer rightAlign = new DefaultTableCellRenderer();
        rightAlign.setHorizontalAlignment(JLabel.RIGHT);
        tblHoaDon.getColumnModel().getColumn(4).setCellRenderer(rightAlign);

        tblHoaDon.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) loadChiTiet();
        });

        JScrollPane scroll = new JScrollPane(tblHoaDon);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Color.WHITE);

        card.add(toolbar, BorderLayout.NORTH);
        card.add(scroll, BorderLayout.CENTER);
        return card;
    }

    private JPanel buildChiTietPanel() {
        JPanel card = new JPanel(new BorderLayout(0, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
                new LineBorder(new Color(230, 230, 230), 1),
                new EmptyBorder(15, 15, 10, 15)));

        JPanel toolbar = new JPanel(new BorderLayout(10, 0));
        toolbar.setOpaque(false);

        JLabel lblSection = new JLabel("Chi Tiết Hóa Đơn");
        lblSection.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblSection.setForeground(primaryColor);
        toolbar.add(lblSection, BorderLayout.WEST);

        JPanel btnGroup = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btnGroup.setOpaque(false);

        JButton btnAddCT = createActionBtn("+ Thêm SP");
        btnAddCT.setPreferredSize(new Dimension(120, 36));
        btnAddCT.addActionListener(e -> showDialogThemChiTiet());

        JButton btnEditCT = createActionBtn("Sửa");
        btnEditCT.setPreferredSize(new Dimension(80, 36));
        btnEditCT.addActionListener(e -> showDialogSuaChiTiet());

        JButton btnDeleteCT = createActionBtn("Xóa SP");
        btnDeleteCT.setPreferredSize(new Dimension(90, 36));
        btnDeleteCT.addActionListener(e -> xoaChiTiet());

        btnGroup.add(btnAddCT);
        btnGroup.add(btnEditCT);
        btnGroup.add(btnDeleteCT);
        toolbar.add(btnGroup, BorderLayout.EAST);

        String[] ctHeaders = {"Mã HĐ", "Mã SP", "Số Lượng", "Đơn Giá (VNĐ)", "Thành Tiền (VNĐ)"};
        modelCT = new DefaultTableModel(ctHeaders, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblChiTiet = new JTable(modelCT);
        styleTable(tblChiTiet);

        tblChiTiet.getColumnModel().getColumn(0).setPreferredWidth(70);
        tblChiTiet.getColumnModel().getColumn(1).setPreferredWidth(70);
        tblChiTiet.getColumnModel().getColumn(2).setPreferredWidth(90);

        DefaultTableCellRenderer rightAlign = new DefaultTableCellRenderer();
        rightAlign.setHorizontalAlignment(JLabel.RIGHT);
        tblChiTiet.getColumnModel().getColumn(3).setCellRenderer(rightAlign);
        tblChiTiet.getColumnModel().getColumn(4).setCellRenderer(rightAlign);

        JScrollPane scroll = new JScrollPane(tblChiTiet);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Color.WHITE);

        JPanel summaryBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 6));
        summaryBar.setBackground(new Color(248, 249, 250));
        summaryBar.setBorder(new MatteBorder(1, 0, 0, 0, new Color(220, 220, 220)));
        JLabel lblSumLabel = new JLabel("Tổng cộng:");
        lblSumLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JLabel lblSum = new JLabel("0 VNĐ");
        lblSum.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblSum.setForeground(dangerColor);
        summaryBar.add(lblSumLabel);
        summaryBar.add(lblSum);

        modelCT.addTableModelListener(e -> {
            long total = 0;
            for (int i = 0; i < modelCT.getRowCount(); i++) {
                // Dùng replaceAll xóa CẢ DẤU CHẤM VÀ DẤU PHẨY thì parse mới không bị lỗi chia 1000
                String val = modelCT.getValueAt(i, 4).toString().replaceAll("[,\\.]", "");
                total += Long.parseLong(val);
            }
            lblSum.setText(String.format(java.util.Locale.US, "%,d VNĐ", total));
        });

        card.add(toolbar, BorderLayout.NORTH);
        card.add(scroll, BorderLayout.CENTER);
        card.add(summaryBar, BorderLayout.SOUTH);
        return card;
    }

    private void loadHoaDon() {
        modelHD.setRowCount(0);
        selectedMaHD = -1;
        modelCT.setRowCount(0);
        List<HoaDonDTO> list = hoaDonBUS.getListHoaDon();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        for (HoaDonDTO hd : list) {
            String ngay = hd.getNgayLapDon() != null ? sdf.format(hd.getNgayLapDon()) : "";
            modelHD.addRow(new Object[]{hd.getMaHD(), getTenNhanVien(hd.getMaNV()), getTenKhachHang(hd.getMaKH()), ngay, String.format(java.util.Locale.US, "%,d", hd.getTongTien())
            });        }
    }

    private void timKiemHoaDon() {
        String kw = txtSearch.getText().trim();
        if (kw.isEmpty()) { loadHoaDon(); return; }
        try {
            int maHD = Integer.parseInt(kw);
            HoaDonDTO hd = hoaDonBUS.getHoaDonById(maHD);
            modelHD.setRowCount(0);
            modelCT.setRowCount(0);
            selectedMaHD = -1;
            if (hd != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String ngay = hd.getNgayLapDon() != null ? sdf.format(hd.getNgayLapDon()) : "";
                modelHD.addRow(new Object[]{
                    hd.getMaHD(), hd.getMaNV(), hd.getMaKH(), ngay, String.format(java.util.Locale.US, "%,d", hd.getTongTien())
                });
            } else {
                msg("Không tìm thấy hóa đơn mã: " + maHD, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            msg("Mã hóa đơn phải là số nguyên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadChiTiet() {
        int row = tblHoaDon.getSelectedRow();
        if (row == -1) return;
        selectedMaHD = (int) modelHD.getValueAt(row, 0);
        modelCT.setRowCount(0);
        List<ChiTietHoaDonDTO> list = chiTietBUS.getListByMaHD(selectedMaHD);
        for (ChiTietHoaDonDTO ct : list) {
            modelCT.addRow(new Object[]{
                ct.getMaHD(), ct.getMaSP(), ct.getSoLuong(),
                String.format(java.util.Locale.US, "%,d", ct.getDonGia()), 
                String.format(java.util.Locale.US, "%,d", ct.getThanhTien())
            });
        }
    }

    private void showDialogTaoHoaDon() {
        JDialog dlg = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Tạo Hóa Đơn Mới", true);
        dlg.setSize(480, 380);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout(10, 10));

        JPanel form = new JPanel(new GridLayout(5, 2, 10, 15));
        form.setBorder(new EmptyBorder(20, 20, 10, 20));
        form.setBackground(Color.WHITE);

        // 1. Mã HĐ tự động (Lấy mã lớn nhất + 1)
        int nextMaHD = 1;
        List<HoaDonDTO> listHD = hoaDonBUS.getListHoaDon();
        if (listHD != null && !listHD.isEmpty()) {
            nextMaHD = listHD.get(listHD.size() - 1).getMaHD() + 1;
        }
        JTextField txtMaHD = new JTextField(String.valueOf(nextMaHD));
        txtMaHD.setEditable(false);
        txtMaHD.setBackground(new Color(240, 240, 240));

        // 2. ComboBox Nhân viên
        JComboBox<String> cbNV = new JComboBox<>();
        if (NhanVienBUS.dsnv == null) new NhanVienBUS().docDSNV();
        for (NhanVienDTO nv : NhanVienBUS.dsnv) {
            cbNV.addItem(nv.getMaNV() + " - " + nv.getTenNV());
        }

        // 3. Khách Hàng (Tìm theo SĐT)
        JTextField txtSdtKH = new JTextField();
        JButton btnCheckKH = createActionBtn("Tìm");
        btnCheckKH.setPreferredSize(new Dimension(70, 30));
        
        JPanel pnlSdt = new JPanel(new BorderLayout(5, 0));
        pnlSdt.setOpaque(false);
        pnlSdt.add(txtSdtKH, BorderLayout.CENTER);
        pnlSdt.add(btnCheckKH, BorderLayout.EAST);

        JLabel lblTenKH = new JLabel("Khách vãng lai");
        lblTenKH.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTenKH.setForeground(new Color(0, 123, 255));
        final int[] currentMaKH = {0}; // Mặc định 0 = Khách vãng lai

        // Sự kiện tìm kiếm SĐT
        btnCheckKH.addActionListener(e -> {
            String sdt = txtSdtKH.getText().trim();
            if (sdt.isEmpty()) {
                currentMaKH[0] = 0;
                lblTenKH.setText("Khách vãng lai");
                return;
            }
            if (KhachHangBUS.dskh == null) new KhachHangBUS().docDSKH();
            KhachHangDTO found = null;
            for (KhachHangDTO kh : KhachHangBUS.dskh) {
                if (kh.getSdt() != null && kh.getSdt().equals(sdt)) { 
                    found = kh; 
                    break; 
                }
            }
            
            if (found != null) {
                currentMaKH[0] = found.getMaKH();
                lblTenKH.setText(found.getHoKH() + " " + found.getTenKH());
            } else {
                int ans = JOptionPane.showConfirmDialog(dlg, "SĐT chưa có trong hệ thống. Bạn có muốn đăng ký khách hàng mới?", "Chưa có thông tin", JOptionPane.YES_NO_OPTION);
                if (ans == JOptionPane.YES_OPTION) {
                    showFormDangKyKH_Nhanh(dlg, sdt, currentMaKH, lblTenKH);
                }
            }
        });

        JTextField txtNgay = new JTextField(new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date()));

        // Add các thành phần vào form
        addFormRow(form, "Mã Hóa Đơn (Tự động):", txtMaHD);
        
        JLabel lblNV = new JLabel("Nhân Viên Lập *:");
        lblNV.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        form.add(lblNV); form.add(cbNV);

        JLabel lblSdt = new JLabel("SĐT Khách Hàng:");
        lblSdt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        form.add(lblSdt); form.add(pnlSdt);

        JLabel lblKhTitle = new JLabel("Tên Khách Hàng:");
        lblKhTitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        form.add(lblKhTitle); form.add(lblTenKH);

        addFormRow(form, "Ngày (dd/MM/yyyy):", txtNgay);

        // Các nút bấm Lưu/Hủy
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btnPanel.setBackground(Color.WHITE);
        JButton btnSave   = createActionBtn("Lưu Hóa Đơn");
        JButton btnCancel = createActionBtn("Hủy");
        btnCancel.addActionListener(e -> dlg.dispose());
        
        btnSave.addActionListener(e -> {
            try {
                int maHD = Integer.parseInt(txtMaHD.getText().trim());
                int maNV = Integer.parseInt(cbNV.getSelectedItem().toString().split(" - ")[0]);
                int maKH = currentMaKH[0];
                java.util.Date ngay = new java.text.SimpleDateFormat("dd/MM/yyyy").parse(txtNgay.getText().trim());

                HoaDonDTO hd = new HoaDonDTO(maHD, maNV, maKH, ngay, 0);
                if (hoaDonBUS.addHoaDon(hd)) {
                    msg("Tạo hóa đơn thành công!", "OK", JOptionPane.INFORMATION_MESSAGE);
                    dlg.dispose();
                    loadHoaDon();
                } else {
                    msg("Tạo hóa đơn thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                msg("Dữ liệu không hợp lệ: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        btnPanel.add(btnCancel);
        btnPanel.add(btnSave);

        dlg.add(form, BorderLayout.CENTER);
        dlg.add(btnPanel, BorderLayout.SOUTH);
        dlg.getContentPane().setBackground(Color.WHITE);
        dlg.setVisible(true);
    }
    // Hàm hỗ trợ đăng ký khách hàng nhanh ngay tại form bán hàng
    private void showFormDangKyKH_Nhanh(JDialog parentDlg, String sdt, int[] currentMaKH, JLabel lblTenKH) {
        JDialog dialog = new JDialog(parentDlg, "Đăng Ký Khách Hàng Nhanh", true);
        dialog.setSize(380, 280);
        dialog.setLocationRelativeTo(parentDlg);
        dialog.setLayout(new BorderLayout());

        JPanel pnlForm = new JPanel(new GridLayout(4, 2, 10, 15));
        pnlForm.setBorder(new EmptyBorder(20, 20, 10, 20));
        pnlForm.setBackground(Color.WHITE);

        JTextField txtHo = new JTextField();
        JTextField txtTen = new JTextField();
        JTextField txtSdt = new JTextField(sdt);
        JTextField txtDiaChi = new JTextField();

        addFormRow(pnlForm, "Họ KH:", txtHo);
        addFormRow(pnlForm, "Tên KH *:", txtTen);
        addFormRow(pnlForm, "SĐT *:", txtSdt);
        addFormRow(pnlForm, "Địa Chỉ:", txtDiaChi);

        JButton btnSave = createActionBtn("Đăng Ký");
        btnSave.addActionListener(e -> {
            try {
                if (txtTen.getText().trim().isEmpty() || txtSdt.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Tên và SĐT không được để trống!");
                    return;
                }
                KhachHangDTO newKh = new KhachHangDTO();
                newKh.setHoKH(txtHo.getText().trim());
                newKh.setTenKH(txtTen.getText().trim());
                newKh.setSdt(txtSdt.getText().trim());
                newKh.setDiaChi(txtDiaChi.getText().trim());

                KhachHangBUS bus = new KhachHangBUS();
                bus.themKH(newKh); 
                
                // Cập nhật lại danh sách và lấy mã KH vừa tạo
                bus.docDSKH();
                if (bus.dskh != null && !bus.dskh.isEmpty()) {
                    KhachHangDTO createdKh = bus.dskh.get(bus.dskh.size() - 1); // Lấy KH mới nhất
                    currentMaKH[0] = createdKh.getMaKH();
                    lblTenKH.setText(createdKh.getHoKH() + " " + createdKh.getTenKH());
                }
                
                JOptionPane.showMessageDialog(dialog, "Đăng ký thành công!");
                dialog.dispose();
            } catch (Exception ex) { 
                JOptionPane.showMessageDialog(dialog, "Lỗi đăng ký!"); 
            }
        });

        JPanel pnlBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlBtn.setBackground(Color.WHITE);
        pnlBtn.setBorder(new EmptyBorder(0, 0, 10, 20));
        pnlBtn.add(btnSave);

        dialog.add(pnlForm, BorderLayout.CENTER);
        dialog.add(pnlBtn, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    private void xoaHoaDon() {
        int row = tblHoaDon.getSelectedRow();
        if (row == -1) { msg("Vui lòng chọn hóa đơn cần xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE); return; }
        int maHD = (int) modelHD.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Xóa hóa đơn #" + maHD + " và toàn bộ chi tiết?",
                "Xác Nhận Xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm != JOptionPane.YES_OPTION) return;

        // Xóa chi tiết trước (FK), rồi xóa hóa đơn
        List<ChiTietHoaDonDTO> chiTiet = chiTietBUS.getListByMaHD(maHD);
        for (ChiTietHoaDonDTO ct : chiTiet) {
            if (chiTietBUS.deleteChiTietHoaDon(ct.getMaHD(), ct.getMaSP())) {
                // ===== HOÀN TRẢ KHO =====
                SanPhamDTO sp = getSanPham(ct.getMaSP());
                if (sp != null) {
                    sanPhamBUS.capNhatSoLuong(ct.getMaSP(), sp.getSoluong() + ct.getSoLuong());
                }
            }
        }
        if (hoaDonBUS.deleteHoaDon(maHD)) {
            msg("Xóa hóa đơn và hoàn trả tồn kho thành công!", "OK", JOptionPane.INFORMATION_MESSAGE);
            loadHoaDon();
        } else {
            msg("Xóa hóa đơn thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showDialogThemChiTiet() {
        if (selectedMaHD == -1) { msg("Vui lòng chọn hóa đơn trước!", "Thông báo", JOptionPane.WARNING_MESSAGE); return; }

        JDialog dlg = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Thêm Sản Phẩm vào HĐ #" + selectedMaHD, true);
        dlg.setSize(420, 280);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout(10, 10));

        JPanel form = new JPanel(new GridLayout(3, 2, 10, 15));
        form.setBorder(new EmptyBorder(20, 20, 10, 20));
        form.setBackground(Color.WHITE);

        // 1. ComboBox Sản Phẩm (Hiển thị Tên và Số lượng tồn kho)
        JComboBox<String> cbSP = new JComboBox<>();
        if (SanPhamBUS.dssp == null) new SanPhamBUS().docDSSP();
        for (SanPhamDTO sp : SanPhamBUS.dssp) {
            // Chỉ hiển thị các sản phẩm còn hàng
            if (sp.getSoluong() > 0) {
                cbSP.addItem(sp.getMasanpham() + " - " + sp.getTensanpham() + " (Tồn: " + sp.getSoluong() + ")");
            }
        }

        JTextField txtSL = new JTextField("1"); // Mặc định số lượng là 1
        JTextField txtThanhTien = new JTextField("0");
        txtThanhTien.setEditable(false); // Khóa không cho sửa
        txtThanhTien.setBackground(new Color(245, 245, 245));
        txtThanhTien.setFont(new Font("Segoe UI", Font.BOLD, 14));
        txtThanhTien.setForeground(new Color(220, 53, 69)); // Màu đỏ cho nổi bật

        // 2. Hàm Tự Động Tính Thành Tiền
        Runnable calcThanhTien = () -> {
            try {
                if (cbSP.getSelectedItem() == null) return;
                
                // Lấy mã SP từ chuỗi hiển thị
                int maSP = Integer.parseInt(cbSP.getSelectedItem().toString().split(" - ")[0]);
                SanPhamDTO sp = getSanPham(maSP);
                if (sp != null) {
                    int sl = Integer.parseInt(txtSL.getText().trim());
                    long thanhTien = sl * sp.getDongia();
                    txtThanhTien.setText(String.format(java.util.Locale.US, "%,d", thanhTien));
                }
            } catch (Exception ex) {
                txtThanhTien.setText("0"); // Nếu nhập sai định dạng thì hiển thị 0
            }
        };

        // Gắn sự kiện để tự tính tiền khi Chọn SP khác hoặc Gõ thay đổi số lượng
        cbSP.addActionListener(e -> calcThanhTien.run());
        txtSL.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) { calcThanhTien.run(); }
        });
        
        calcThanhTien.run(); // Chạy tính toán lần đầu ngay khi mở form

        // 3. Đưa các thành phần vào Form
        JLabel lblSP = new JLabel("Sản Phẩm *:");
        lblSP.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        form.add(lblSP); form.add(cbSP);

        addFormRow(form, "Số Lượng *:", txtSL);
        addFormRow(form, "Thành Tiền (VNĐ):", txtThanhTien);

        // 4. Các nút thao tác
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btnPanel.setBackground(Color.WHITE);
        JButton btnSave   = createActionBtn("Lưu");
        JButton btnCancel = createActionBtn("Hủy");
        btnCancel.addActionListener(e -> dlg.dispose());
        
        btnSave.addActionListener(e -> {
            // ===== 1. KHÓA NÚT NGAY LẬP TỨC ĐỂ CHỐNG DOUBLE CLICK =====
            btnSave.setEnabled(false); 

            try {
                if (cbSP.getSelectedItem() == null) {
                    msg("Không có sản phẩm nào được chọn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    btnSave.setEnabled(true); // Bật lại nút cho người dùng sửa
                    return;
                }
                
                int maSP  = Integer.parseInt(cbSP.getSelectedItem().toString().split(" - ")[0]);
                int sl    = Integer.parseInt(txtSL.getText().trim());
                
                if (sl <= 0) {
                    msg("Số lượng mua phải lớn hơn 0!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    btnSave.setEnabled(true); // Bật lại nút
                    return;
                }

                // ===== KIỂM TRA TỒN KHO =====
                SanPhamDTO sp = getSanPham(maSP);
                if (sp == null) {
                    msg("Sản phẩm không tồn tại trong kho!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    btnSave.setEnabled(true); // Bật lại nút
                    return;
                }
                if (sp.getSoluong() < sl) {
                    msg("Kho không đủ hàng! Chỉ còn: " + sp.getSoluong(), "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                    btnSave.setEnabled(true); // Bật lại nút
                    return;
                }

                long gia = sp.getDongia(); 
                long thanhTien = sl * gia;

                ChiTietHoaDonDTO ct = new ChiTietHoaDonDTO();
                ct.setMaHD(selectedMaHD);
                ct.setMaSP(maSP);
                ct.setSoLuong(sl);
                ct.setDonGia(gia);
                ct.setThanhTien(thanhTien);
                
                if (chiTietBUS.addChiTietHoaDon(ct)) {
                    // ===== TRỪ SỐ LƯỢNG KHO =====
                    sanPhamBUS.capNhatSoLuong(maSP, sp.getSoluong() - sl);
                    
                    capNhatTongTien(selectedMaHD);
                    msg("Thêm sản phẩm thành công!", "OK", JOptionPane.INFORMATION_MESSAGE);
                    int currentHD = selectedMaHD; 
                    dlg.dispose(); 
                    loadHoaDon(); 
                    selectRowByMaHD(currentHD); 
                } else {
                    msg("Sản phẩm này đã có trong hóa đơn. Vui lòng bấm Sửa số lượng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    btnSave.setEnabled(true); // Bật lại nút
                }
            } catch (NumberFormatException ex) {
                msg("Số lượng nhập không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                btnSave.setEnabled(true); // Bật lại nút
            }
        });        
        btnPanel.add(btnCancel);
        btnPanel.add(btnSave);

        dlg.add(form, BorderLayout.CENTER);
        dlg.add(btnPanel, BorderLayout.SOUTH);
        dlg.getContentPane().setBackground(Color.WHITE);
        dlg.setVisible(true);
    }

private void showDialogSuaChiTiet() {
        int row = tblChiTiet.getSelectedRow();
        if (row == -1) { msg("Vui lòng chọn dòng chi tiết cần sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE); return; }

        int maHD = (int) modelCT.getValueAt(row, 0);
        int maSP = (int) modelCT.getValueAt(row, 1);
        int oldSL = (int) modelCT.getValueAt(row, 2);
        
        // Lấy đơn giá gốc từ bảng (đã xóa các dấu phẩy định dạng)
        long gia = Long.parseLong(modelCT.getValueAt(row, 3).toString().replaceAll("[,\\.]", ""));
        
        JDialog dlg = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Sửa Số Lượng - HĐ #" + maHD, true);
        dlg.setSize(380, 220);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout(10, 10));

        JPanel form = new JPanel(new GridLayout(2, 2, 10, 15));
        form.setBorder(new EmptyBorder(20, 20, 10, 20));
        form.setBackground(Color.WHITE);

        JTextField txtSL = new JTextField(String.valueOf(oldSL));
        
        // Ô Thành Tiền hiển thị tự động
        JTextField txtThanhTien = new JTextField(String.format(java.util.Locale.US, "%,d", oldSL * gia));
        txtThanhTien.setEditable(false); // Khóa không cho sửa
        txtThanhTien.setBackground(new Color(245, 245, 245));
        txtThanhTien.setFont(new Font("Segoe UI", Font.BOLD, 14));
        txtThanhTien.setForeground(new Color(220, 53, 69));

        // Tự động tính Thành Tiền khi gõ Số lượng mới
        Runnable calcThanhTien = () -> {
            try {
                int sl = Integer.parseInt(txtSL.getText().trim());
                long thanhTien = sl * gia;
                txtThanhTien.setText(String.format(java.util.Locale.US, "%,d", thanhTien));
            } catch (Exception ex) {
                txtThanhTien.setText("0");
            }
        };

        txtSL.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) { calcThanhTien.run(); }
        });

        addFormRow(form, "Số Lượng *:", txtSL);
        addFormRow(form, "Thành Tiền (VNĐ):", txtThanhTien);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btnPanel.setBackground(Color.WHITE);
        JButton btnSave   = createActionBtn("Cập Nhật");
        JButton btnCancel = createActionBtn("Hủy");
        btnCancel.addActionListener(e -> dlg.dispose());
        
        btnSave.addActionListener(e -> {
            try {
                int newSL = Integer.parseInt(txtSL.getText().trim());
                if (newSL <= 0) {
                    msg("Số lượng phải lớn hơn 0!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                int diff = newSL - oldSL; 

                // ===== KIỂM TRA KHI TĂNG SỐ LƯỢNG MUA =====
                SanPhamDTO sp = getSanPham(maSP);
                if (sp != null) {
                    if (diff > 0 && sp.getSoluong() < diff) {
                        msg("Kho không đủ hàng để mua thêm! Chỉ còn: " + sp.getSoluong(), "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }

                long newThanhTien = newSL * gia;
                ChiTietHoaDonDTO ct = new ChiTietHoaDonDTO();
                ct.setMaHD(selectedMaHD);
                ct.setMaSP(maSP);
                ct.setDonGia(gia);
                ct.setThanhTien(newThanhTien);                
                if (chiTietBUS.updateChiTietHoaDon(ct)) {
                    // ===== CẬP NHẬT KHO BÙ TRỪ =====
                    if (sp != null) {
                        sanPhamBUS.capNhatSoLuong(maSP, sp.getSoluong() - diff);
                    }

                    capNhatTongTien(maHD);
                    msg("Cập nhật thành công!", "OK", JOptionPane.INFORMATION_MESSAGE);
                    int currentHD = selectedMaHD; // Lưu tạm mã HĐ đang chọn
                    dlg.dispose(); // Đóng form
                    loadHoaDon(); // Làm mới bảng Hóa Đơn (để cập nhật Tổng Tiền)
                    selectRowByMaHD(currentHD); // Tự động chọn lại dòng vừa click (Nó sẽ tự kích hoạt load lại Chi Tiết luôn)
                } else {
                    msg("Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                msg("Dữ liệu nhập không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        btnPanel.add(btnCancel);
        btnPanel.add(btnSave);

        dlg.add(form, BorderLayout.CENTER);
        dlg.add(btnPanel, BorderLayout.SOUTH);
        dlg.getContentPane().setBackground(Color.WHITE);
        dlg.setVisible(true);
    }
    private void xoaChiTiet() {
        int row = tblChiTiet.getSelectedRow();
        if (row == -1) { msg("Vui lòng chọn sản phẩm cần xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE); return; }
        int maHD = (int) modelCT.getValueAt(row, 0);
        int maSP = (int) modelCT.getValueAt(row, 1);
        int deletedSL = (int) modelCT.getValueAt(row, 2);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Xóa sản phẩm #" + maSP + " khỏi hóa đơn #" + maHD + "?",
                "Xác Nhận", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm != JOptionPane.YES_OPTION) return;

        if (chiTietBUS.deleteChiTietHoaDon(maHD, maSP)) {
            // ===== HOÀN TRẢ KHO =====
            SanPhamDTO sp = getSanPham(maSP);
            if (sp != null) {
                sanPhamBUS.capNhatSoLuong(maSP, sp.getSoluong() + deletedSL);
            }

            capNhatTongTien(maHD);
            msg("Xóa sản phẩm thành công!", "OK", JOptionPane.INFORMATION_MESSAGE);
            int currentHD = maHD;
            loadHoaDon();
            selectRowByMaHD(currentHD);
        } else {
            msg("Xóa sản phẩm thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // =====================================================================
    //  HELPERS
    // =====================================================================

    /** Lấy sản phẩm từ danh sách dssp trong SanPhamBUS */
    private SanPhamDTO getSanPham(int maSP) {
        if (SanPhamBUS.dssp == null) {
            sanPhamBUS.docDSSP();
        }
        for (SanPhamDTO sp : SanPhamBUS.dssp) {
            if (sp.getMasanpham() == maSP) {
                return sp;
            }
        }
        return null; // Không tìm thấy
    }

    private void capNhatTongTien(int maHD) {
        List<ChiTietHoaDonDTO> list = chiTietBUS.getListByMaHD(maHD);
        long total = 0;
        for (ChiTietHoaDonDTO ct : list) total += ct.getThanhTien();
        HoaDonDTO hd = hoaDonBUS.getHoaDonById(maHD);
        if (hd != null) {
            hd.setTongTien(total);
            hoaDonBUS.updateHoaDon(hd);
        }
    }

    private void selectRowByMaHD(int maHD) {
        selectedMaHD = maHD;
        for (int i = 0; i < modelHD.getRowCount(); i++) {
            if ((int) modelHD.getValueAt(i, 0) == maHD) {
                tblHoaDon.setRowSelectionInterval(i, i);
                break;
            }
        }
    }

    private void addFormRow(JPanel form, String label, JTextField field) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1),
                new EmptyBorder(4, 8, 4, 8)));
        form.add(lbl);
        form.add(field);
    }

    private void styleTable(JTable t) {
        t.setRowHeight(40);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        t.setGridColor(new Color(245, 245, 245));
        t.setShowVerticalLines(false);
        t.setSelectionBackground(new Color(240, 247, 255));
        t.setSelectionForeground(Color.BLACK);
        t.getTableHeader().setReorderingAllowed(false);
        JTableHeader header = t.getTableHeader();
        header.setPreferredSize(new Dimension(0, 42));
        header.setBackground(new Color(250, 251, 252));
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setForeground(secondaryColor);
        ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(JLabel.LEFT);
    }

    // private JButton createBtn(String text, Color bg) {
    //     JButton btn = new JButton(text);
    //     btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
    //     btn.setBackground(bg);
    //     btn.setForeground(Color.WHITE);
    //     btn.setFocusPainted(false);
    //     btn.setBorderPainted(false);
    //     btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    //     return btn;
    // }
    
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
    
    private void msg(String text, String title, int type) {
        JOptionPane.showMessageDialog(this, text, title, type);
    }
    private String getTenNhanVien(int maNV) {
        if (NhanVienBUS.dsnv == null) new NhanVienBUS().docDSNV();
        if (NhanVienBUS.dsnv != null) {
            for (NhanVienDTO nv : NhanVienBUS.dsnv) {
                // Sửa nv.getTenNV() thành hàm tương ứng trong DTO của bạn nếu bị lỗi đỏ
                if (nv.getMaNV() == maNV) return nv.getTenNV(); 
            }
        }
        return String.valueOf(maNV);
    }

    private String getTenKhachHang(int maKH) {
        if (maKH == 0) return "Khách Vãng Lai";
        if (KhachHangBUS.dskh == null) new KhachHangBUS().docDSKH();
        if (KhachHangBUS.dskh != null) {
            for (KhachHangDTO kh : KhachHangBUS.dskh) {
                // Sửa kh.getHo() và kh.getTen() thành hàm tương ứng trong DTO của bạn
                if (kh.getMaKH() == maKH) return kh.getHoKH() + " " + kh.getTenKH(); 
            }
        }
        return String.valueOf(maKH);
    }
}