package SieuThiMiniGUI;

import DTO.ChiTietHoaDonDTO;
import DTO.HoaDonDTO;
import SieuThiMiniBUS.ChiTietHoaDonBUS;
import SieuThiMiniBUS.HoaDonBUS;
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
    private final Font fontTitle      = new Font("Segoe UI", Font.BOLD, 24);
    private final Font fontPlain      = new Font("Segoe UI", Font.PLAIN, 14);

    public QuanLyDonHangGUI() { 
        initComponents(); 
        loadHoaDon();
    }

    // =====================================================================
    //  INIT LAYOUT
    // =====================================================================
    private void initComponents() {
        setLayout(new BorderLayout(0, 15));
        setBackground(bgColor);
        setBorder(new EmptyBorder(20, 25, 20, 25));

        // Title
        JLabel lblTitle = new JLabel("Quản Lý Đơn Hàng");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(40, 40, 40));
        add(lblTitle, BorderLayout.NORTH);

        // Main split: top = HoaDon, bottom = ChiTietHoaDon
        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                buildHoaDonPanel(), buildChiTietPanel());
        split.setDividerLocation(320);
        split.setDividerSize(8);
        split.setBackground(bgColor);
        add(split, BorderLayout.CENTER);
    }

    // =====================================================================
    //  PANEL HOA DON (top)
    // =====================================================================
    private JPanel buildHoaDonPanel() {
        JPanel card = new JPanel(new BorderLayout(0, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
                new LineBorder(new Color(230, 230, 230), 1),
                new EmptyBorder(15, 15, 10, 15)));

        // --- toolbar ---
        JPanel toolbar = new JPanel(new BorderLayout(10, 0));
        toolbar.setOpaque(false);

        // Search
        JPanel searchGroup = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        searchGroup.setOpaque(false);
        JLabel lblSection = new JLabel("Hóa Đơn");
        lblSection.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblSection.setForeground(primaryColor);

        txtSearch = new JTextField(16);
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtSearch.setPreferredSize(new Dimension(180, 36));
        txtSearch.setToolTipText("Tìm theo mã hóa đơn...");

        JButton btnSearch = createBtn("Tìm", primaryColor);
        btnSearch.setPreferredSize(new Dimension(80, 36));
        btnSearch.addActionListener(e -> timKiemHoaDon());
        txtSearch.addActionListener(e -> timKiemHoaDon());

        JButton btnRefresh = createBtn("Làm Mới", secondaryColor);
        btnRefresh.setPreferredSize(new Dimension(100, 36));
        btnRefresh.addActionListener(e -> { txtSearch.setText(""); loadHoaDon(); });

        searchGroup.add(lblSection);
        searchGroup.add(Box.createHorizontalStrut(10));
        searchGroup.add(txtSearch);
        searchGroup.add(btnSearch);
        searchGroup.add(btnRefresh);
        toolbar.add(searchGroup, BorderLayout.WEST);

        // Action buttons
        JPanel btnGroup = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btnGroup.setOpaque(false);

        JButton btnAdd = createBtn("+ Tạo Đơn", successColor);
        btnAdd.setPreferredSize(new Dimension(120, 36));
        btnAdd.addActionListener(e -> showDialogTaoHoaDon());

        JButton btnDelete = createBtn("Xóa Đơn", dangerColor);
        btnDelete.setPreferredSize(new Dimension(100, 36));
        btnDelete.addActionListener(e -> xoaHoaDon());

        btnGroup.add(btnAdd);
        btnGroup.add(btnDelete);
        toolbar.add(btnGroup, BorderLayout.EAST);

        // --- table ---
        String[] hdHeaders = {"Mã HĐ", "Mã NV", "Mã KH", "Ngày Lập", "Tổng Tiền (VNĐ)"};
        modelHD = new DefaultTableModel(hdHeaders, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblHoaDon = new JTable(modelHD);
        styleTable(tblHoaDon);

        // column widths
        tblHoaDon.getColumnModel().getColumn(0).setPreferredWidth(70);
        tblHoaDon.getColumnModel().getColumn(1).setPreferredWidth(70);
        tblHoaDon.getColumnModel().getColumn(2).setPreferredWidth(70);
        tblHoaDon.getColumnModel().getColumn(3).setPreferredWidth(130);
        tblHoaDon.getColumnModel().getColumn(4).setPreferredWidth(160);

        // right-align money column
        DefaultTableCellRenderer rightAlign = new DefaultTableCellRenderer();
        rightAlign.setHorizontalAlignment(JLabel.RIGHT);
        tblHoaDon.getColumnModel().getColumn(4).setCellRenderer(rightAlign);

        // click row -> load chi tiet
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

    // =====================================================================
    //  PANEL CHI TIET HOA DON (bottom)
    // =====================================================================
    private JPanel buildChiTietPanel() {
        JPanel card = new JPanel(new BorderLayout(0, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
                new LineBorder(new Color(230, 230, 230), 1),
                new EmptyBorder(15, 15, 10, 15)));

        // --- toolbar ---
        JPanel toolbar = new JPanel(new BorderLayout(10, 0));
        toolbar.setOpaque(false);

        JLabel lblSection = new JLabel("Chi Tiết Hóa Đơn");
        lblSection.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblSection.setForeground(primaryColor);
        toolbar.add(lblSection, BorderLayout.WEST);

        JPanel btnGroup = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btnGroup.setOpaque(false);

        JButton btnAddCT = createBtn("+ Thêm SP", successColor);
        btnAddCT.setPreferredSize(new Dimension(120, 36));
        btnAddCT.addActionListener(e -> showDialogThemChiTiet());

        JButton btnEditCT = createBtn("Sửa", primaryColor);
        btnEditCT.setPreferredSize(new Dimension(80, 36));
        btnEditCT.addActionListener(e -> showDialogSuaChiTiet());

        JButton btnDeleteCT = createBtn("Xóa SP", dangerColor);
        btnDeleteCT.setPreferredSize(new Dimension(90, 36));
        btnDeleteCT.addActionListener(e -> xoaChiTiet());

        btnGroup.add(btnAddCT);
        btnGroup.add(btnEditCT);
        btnGroup.add(btnDeleteCT);
        toolbar.add(btnGroup, BorderLayout.EAST);

        // --- table ---
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

        // summary bar
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

        // update sum when model changes
        modelCT.addTableModelListener(e -> {
            double total = 0;
            for (int i = 0; i < modelCT.getRowCount(); i++) {
                total += Double.parseDouble(modelCT.getValueAt(i, 4).toString().replace(",", ""));
            }
            lblSum.setText(String.format("%,.0f VNĐ", total));
        });

        card.add(toolbar, BorderLayout.NORTH);
        card.add(scroll, BorderLayout.CENTER);
        card.add(summaryBar, BorderLayout.SOUTH);
        return card;
    }

    // =====================================================================
    //  DATA LOGIC
    // =====================================================================
    private void loadHoaDon() {
        modelHD.setRowCount(0);
        selectedMaHD = -1;
        modelCT.setRowCount(0);
        List<HoaDonDTO> list = hoaDonBUS.getListHoaDon();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        for (HoaDonDTO hd : list) {
            String ngay = hd.getNgayLapDon() != null ? sdf.format(hd.getNgayLapDon()) : "";
            modelHD.addRow(new Object[]{
                hd.getMaHD(),
                hd.getMaNV(),
                hd.getMaKH(),
                ngay,
                String.format("%,.0f", hd.getTongTien())
            });
        }
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
                    hd.getMaHD(), hd.getMaNV(), hd.getMaKH(), ngay,
                    String.format("%,.0f", hd.getTongTien())
                });
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy hóa đơn mã: " + maHD, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Mã hóa đơn phải là số nguyên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
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
                ct.getMaHD(),
                ct.getMaSP(),
                ct.getSoLuong(),
                String.format("%,.0f", ct.getDonGia()),
                String.format("%,.0f", ct.getThanhTien())
            });
        }
    }

    // =====================================================================
    //  CRUD HOA DON
    // =====================================================================
    private void showDialogTaoHoaDon() {
        JDialog dlg = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Tạo Hóa Đơn Mới", true);
        dlg.setSize(400, 280);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout(10, 10));

        JPanel form = new JPanel(new GridLayout(4, 2, 10, 12));
        form.setBorder(new EmptyBorder(20, 20, 10, 20));
        form.setBackground(Color.WHITE);

        JTextField txtMaHD  = new JTextField();
        JTextField txtMaNV  = new JTextField();
        JTextField txtMaKH  = new JTextField();
        JTextField txtNgay  = new JTextField(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));

        addFormRow(form, "Mã Hóa Đơn *:", txtMaHD);
        addFormRow(form, "Mã Nhân Viên *:", txtMaNV);
        addFormRow(form, "Mã Khách Hàng:", txtMaKH);
        addFormRow(form, "Ngày (dd/MM/yyyy):", txtNgay);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btnPanel.setBackground(Color.WHITE);
        JButton btnSave   = createBtn("Lưu", successColor);
        JButton btnCancel = createBtn("Hủy", secondaryColor);
        btnCancel.addActionListener(e -> dlg.dispose());
        btnSave.addActionListener(e -> {
            try {
                int maHD = Integer.parseInt(txtMaHD.getText().trim());
                int maNV = Integer.parseInt(txtMaNV.getText().trim());
                int maKH = txtMaKH.getText().trim().isEmpty() ? 0 : Integer.parseInt(txtMaKH.getText().trim());
                Date ngay = new SimpleDateFormat("dd/MM/yyyy").parse(txtNgay.getText().trim());

                HoaDonDTO hd = new HoaDonDTO(maHD, maNV, maKH, ngay, 0);
                if (hoaDonBUS.addHoaDon(hd)) {
                    JOptionPane.showMessageDialog(dlg, "Tạo hóa đơn thành công!", "OK", JOptionPane.INFORMATION_MESSAGE);
                    dlg.dispose();
                    loadHoaDon();
                } else {
                    JOptionPane.showMessageDialog(dlg, "Tạo hóa đơn thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dlg, "Dữ liệu không hợp lệ: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        btnPanel.add(btnCancel);
        btnPanel.add(btnSave);

        dlg.add(form, BorderLayout.CENTER);
        dlg.add(btnPanel, BorderLayout.SOUTH);
        dlg.getContentPane().setBackground(Color.WHITE);
        dlg.setVisible(true);
    }

    private void xoaHoaDon() {
        int row = tblHoaDon.getSelectedRow();
        if (row == -1) { msg("Vui lòng chọn hóa đơn cần xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE); return; }
        int maHD = (int) modelHD.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Xóa hóa đơn #" + maHD + " và toàn bộ chi tiết?",
                "Xác Nhận Xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm != JOptionPane.YES_OPTION) return;

        // xóa chi tiết trước (FK), rồi xóa hóa đơn
        List<ChiTietHoaDonDTO> chiTiet = chiTietBUS.getListByMaHD(maHD);
        for (ChiTietHoaDonDTO ct : chiTiet) {
            chiTietBUS.deleteChiTietHoaDon(ct.getMaHD(), ct.getMaSP());
        }
        if (hoaDonBUS.deleteHoaDon(maHD)) {
            msg("Xóa hóa đơn thành công!", "OK", JOptionPane.INFORMATION_MESSAGE);
            loadHoaDon();
        } else {
            msg("Xóa hóa đơn thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // =====================================================================
    //  CRUD CHI TIET HOA DON
    // =====================================================================
    private void showDialogThemChiTiet() {
        if (selectedMaHD == -1) { msg("Vui lòng chọn hóa đơn trước!", "Thông báo", JOptionPane.WARNING_MESSAGE); return; }

        JDialog dlg = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Thêm Sản Phẩm vào HĐ #" + selectedMaHD, true);
        dlg.setSize(380, 250);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout(10, 10));

        JPanel form = new JPanel(new GridLayout(3, 2, 10, 12));
        form.setBorder(new EmptyBorder(20, 20, 10, 20));
        form.setBackground(Color.WHITE);

        JTextField txtMaSP  = new JTextField();
        JTextField txtSL    = new JTextField();
        JTextField txtGia   = new JTextField();

        addFormRow(form, "Mã Sản Phẩm *:", txtMaSP);
        addFormRow(form, "Số Lượng *:", txtSL);
        addFormRow(form, "Đơn Giá *:", txtGia);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btnPanel.setBackground(Color.WHITE);
        JButton btnSave   = createBtn("Lưu", successColor);
        JButton btnCancel = createBtn("Hủy", secondaryColor);
        btnCancel.addActionListener(e -> dlg.dispose());
        btnSave.addActionListener(e -> {
            try {
                int maSP  = Integer.parseInt(txtMaSP.getText().trim());
                int sl    = Integer.parseInt(txtSL.getText().trim());
                double gia = Double.parseDouble(txtGia.getText().trim());

                ChiTietHoaDonDTO ct = new ChiTietHoaDonDTO(selectedMaHD, maSP, sl, gia, sl * gia);
                if (chiTietBUS.addChiTietHoaDon(ct)) {
                    // cập nhật lại tổng tiền hóa đơn
                    capNhatTongTien(selectedMaHD);
                    msg("Thêm sản phẩm thành công!", "OK", JOptionPane.INFORMATION_MESSAGE);
                    dlg.dispose();
                    loadChiTiet();
                    loadHoaDon();
                    // reselect row
                    selectRowByMaHD(selectedMaHD);
                } else {
                    msg("Thêm sản phẩm thất bại! (Trùng mã SP hoặc lỗi DB)", "Lỗi", JOptionPane.ERROR_MESSAGE);
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

    private void showDialogSuaChiTiet() {
        int row = tblChiTiet.getSelectedRow();
        if (row == -1) { msg("Vui lòng chọn dòng chi tiết cần sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE); return; }

        int maHD = (int) modelCT.getValueAt(row, 0);
        int maSP = (int) modelCT.getValueAt(row, 1);
        int sl   = (int) modelCT.getValueAt(row, 2);
        double gia = Double.parseDouble(modelCT.getValueAt(row, 3).toString().replace(",", ""));

        JDialog dlg = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Sửa Chi Tiết HĐ #" + maHD, true);
        dlg.setSize(360, 220);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout(10, 10));

        JPanel form = new JPanel(new GridLayout(2, 2, 10, 12));
        form.setBorder(new EmptyBorder(20, 20, 10, 20));
        form.setBackground(Color.WHITE);

        JTextField txtSL  = new JTextField(String.valueOf(sl));
        JTextField txtGia = new JTextField(String.valueOf(gia));

        addFormRow(form, "Số Lượng *:", txtSL);
        addFormRow(form, "Đơn Giá *:", txtGia);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btnPanel.setBackground(Color.WHITE);
        JButton btnSave   = createBtn("Cập Nhật", successColor);
        JButton btnCancel = createBtn("Hủy", secondaryColor);
        btnCancel.addActionListener(e -> dlg.dispose());
        btnSave.addActionListener(e -> {
            try {
                int newSL   = Integer.parseInt(txtSL.getText().trim());
                double newGia = Double.parseDouble(txtGia.getText().trim());
                ChiTietHoaDonDTO ct = new ChiTietHoaDonDTO(maHD, maSP, newSL, newGia, newSL * newGia);
                if (chiTietBUS.updateChiTietHoaDon(ct)) {
                    capNhatTongTien(maHD);
                    msg("Cập nhật thành công!", "OK", JOptionPane.INFORMATION_MESSAGE);
                    dlg.dispose();
                    loadChiTiet();
                    loadHoaDon();
                    selectRowByMaHD(maHD);
                } else {
                    msg("Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                msg("Dữ liệu không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
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
        int confirm = JOptionPane.showConfirmDialog(this,
                "Xóa sản phẩm #" + maSP + " khỏi hóa đơn #" + maHD + "?",
                "Xác Nhận", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm != JOptionPane.YES_OPTION) return;
        if (chiTietBUS.deleteChiTietHoaDon(maHD, maSP)) {
            capNhatTongTien(maHD);
            msg("Xóa sản phẩm thành công!", "OK", JOptionPane.INFORMATION_MESSAGE);
            loadChiTiet();
            loadHoaDon();
            selectRowByMaHD(maHD);
        } else {
            msg("Xóa sản phẩm thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // =====================================================================
    //  HELPERS
    // =====================================================================

    /** Tính lại tổng tiền hóa đơn từ chi tiết và cập nhật DB */
    private void capNhatTongTien(int maHD) {
        List<ChiTietHoaDonDTO> list = chiTietBUS.getListByMaHD(maHD);
        double total = 0;
        for (ChiTietHoaDonDTO ct : list) total += ct.getThanhTien();
        HoaDonDTO hd = hoaDonBUS.getHoaDonById(maHD);
        if (hd != null) {
            hd.setTongTien(total);
            hoaDonBUS.updateHoaDon(hd);
        }
    }

    /** Sau khi reload bảng HD, chọn lại dòng có maHD */
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

    private JButton createBtn(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void msg(String text, String title, int type) {
        JOptionPane.showMessageDialog(this, text, title, type);
    }
}
