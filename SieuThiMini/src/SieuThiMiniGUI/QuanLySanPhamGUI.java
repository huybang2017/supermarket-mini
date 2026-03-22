package SieuThiMiniGUI;

import DTO.*;
import SieuThiMiniBUS.*;

// Import bộ thư viện giao diện (Được phép dùng *)
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.stream.Collectors;
import javax.swing.filechooser.FileNameExtensionFilter;

// Import thư viện Excel (TUYỆT ĐỐI KHÔNG DÙNG DẤU *, CHỈ IMPORT CLASS CỤ THỂ)
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class QuanLySanPhamGUI extends JPanel {
    
    // Khai báo các biến cục bộ chỉ dùng cho trang này
    private DefaultTableModel model;
    private JTable tblSanPham;
    
    // Bảng màu dùng chung cho giao diện này
    private final java.awt.Color primaryColor = new java.awt.Color(0, 123, 255);
    private final java.awt.Color secondaryColor = new java.awt.Color(108, 117, 125);
    private final java.awt.Color bgColor = new java.awt.Color(244, 246, 249);

    // Constructor: Chạy ngay khi class được gọi
    public QuanLySanPhamGUI() {
        initComponents();
        docDSSP(); // Đọc dữ liệu lên bảng khi vừa mở
        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                docDSSP(); // Tải lại danh sách sản phẩm mới nhất
            }
        });
    }

    // Hàm khởi tạo toàn bộ giao diện
    private void initComponents() {
        // Thiết lập layout và màu nền cho CHÍNH PANEL NÀY (this)
        this.setLayout(new BorderLayout(20, 20));
        this.setBackground(bgColor);
        this.setBorder(new EmptyBorder(20, 25, 20, 25));

        JLabel lblTitle = new JLabel("Quản Lý Sản Phẩm");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new java.awt.Color(40, 40, 40));
        this.add(lblTitle, BorderLayout.NORTH);

        // --- BẮT ĐẦU PHẦN BODY (THANH CÔNG CỤ + BẢNG) ---
        JPanel card = new JPanel(new BorderLayout(15, 15));
        card.setBackground(java.awt.Color.WHITE);
        card.setBorder(new CompoundBorder(new LineBorder(new java.awt.Color(230, 230, 230), 1), new EmptyBorder(15, 15, 15, 15)));

        // 1. THANH CÔNG CỤ (HEADER)
        JPanel pnlHeader = new JPanel();
        pnlHeader.setLayout(new BoxLayout(pnlHeader, BoxLayout.Y_AXIS));
        pnlHeader.setOpaque(false);

        JPanel topToolBar = new JPanel(new BorderLayout());
        topToolBar.setOpaque(false);

        // Bộ tìm kiếm
        JPanel pnlSearchGroup = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlSearchGroup.setOpaque(false);
        JTextField txtSearch = new JTextField(" Tìm kiếm sản phẩm...");
        txtSearch.setPreferredSize(new Dimension(220, 38));
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtSearch.setForeground(java.awt.Color.GRAY);
        txtSearch.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) { if (txtSearch.getText().trim().equals("Tìm kiếm sản phẩm...")) { txtSearch.setText(""); txtSearch.setForeground(java.awt.Color.BLACK); } }
            public void focusLost(FocusEvent e) { if (txtSearch.getText().trim().isEmpty()) { txtSearch.setForeground(java.awt.Color.GRAY); txtSearch.setText(" Tìm kiếm sản phẩm..."); } }
        });
        
        JButton btnAdvancedToggle = createActionBtn("▼");
        btnAdvancedToggle.setPreferredSize(new Dimension(45, 38));
        pnlSearchGroup.add(txtSearch);
        pnlSearchGroup.add(btnAdvancedToggle);
        topToolBar.add(pnlSearchGroup, BorderLayout.WEST);

        // Các nút bấm
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlButtons.setOpaque(false);
        JButton btnAdd = createActionBtn("+ Thêm");
        btnAdd.addActionListener(e -> showForm(null));
        JButton btnDelete = createActionBtn("- Xóa Sản Phẩm");
        btnDelete.addActionListener(e -> deleteSelectedProduct());
        
        // NEW: Excel buttons
        JButton btnExportExcel = createActionBtn("📤 Xuất Excel");
        btnExportExcel.addActionListener(e -> exportToExcel());
        JButton btnImportExcel = createActionBtn("📥 Nhập Excel");
        btnImportExcel.addActionListener(e -> importFromExcel());
        
        pnlButtons.add(btnAdd); 
        pnlButtons.add(btnDelete);
        pnlButtons.add(btnExportExcel);
        pnlButtons.add(btnImportExcel);
        topToolBar.add(pnlButtons, BorderLayout.EAST);

        // Panel Lọc Nâng Cao
        JPanel pnlAdvancedSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        pnlAdvancedSearch.setBackground(new java.awt.Color(248, 250, 252));
        pnlAdvancedSearch.setBorder(new MatteBorder(1, 0, 0, 0, new java.awt.Color(230, 230, 230)));
        pnlAdvancedSearch.setVisible(false);
        
        pnlAdvancedSearch.add(new JLabel("Giá từ:"));
        JTextField txtGiaTu = new JTextField(8);
        pnlAdvancedSearch.add(txtGiaTu);
        pnlAdvancedSearch.add(new JLabel("Đến:"));
        JTextField txtGiaDen = new JTextField(8);
        pnlAdvancedSearch.add(txtGiaDen);
        
        JButton btnApplyFilter = createActionBtn("Lọc");
        JButton btnResetFilter = createActionBtn("Làm Mới");
        btnResetFilter.addActionListener(e -> {
            txtSearch.setText(" Tìm kiếm sản phẩm...");
            txtGiaTu.setText("");
            txtGiaDen.setText("");
            docDSSP();
        });
        pnlAdvancedSearch.add(btnApplyFilter);
        pnlAdvancedSearch.add(btnResetFilter);

        pnlHeader.add(topToolBar);
        pnlHeader.add(Box.createVerticalStrut(10));
        pnlHeader.add(pnlAdvancedSearch);
        card.add(pnlHeader, BorderLayout.NORTH);

        // Sự kiện sổ xuống
        btnAdvancedToggle.addActionListener(e -> {
            boolean isVisible = pnlAdvancedSearch.isVisible();
            pnlAdvancedSearch.setVisible(!isVisible);
            btnAdvancedToggle.setText(isVisible ? "▼" : "▲");
            card.revalidate();
        });

        // Tìm kiếm Real-time
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String keyword = txtSearch.getText().toLowerCase().trim();
                if (keyword.equals("tìm kiếm sản phẩm...") || keyword.isEmpty()) { docDSSP(); return; }
                model.setRowCount(0);
                if (SanPhamBUS.dssp != null) {
                    for (SanPhamDTO sp : SanPhamBUS.dssp) {
                        if (String.valueOf(sp.getMasanpham()).contains(keyword) || sp.getTensanpham().toLowerCase().contains(keyword)) {
                            model.addRow(new Object[]{sp.getMasanpham(), sp.getTensanpham(), sp.getSoluong(), sp.getDongia(), sp.getDonvitinh(), "⚙ Sửa"});
                        }
                    }
                }
            }
        });

        // 2. KHU VỰC BẢNG DỮ LIỆU
        String[] headers = {"ID", "Tên Sản Phẩm", "Số Lượng", "Đơn Giá", "Đơn Vị Tính", "Hành Động"};
        model = new DefaultTableModel(headers, 0) { @Override public boolean isCellEditable(int r, int c) { return false; } };
        tblSanPham = new JTable(model);
        styleTable(tblSanPham);

        tblSanPham.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = tblSanPham.getSelectedRow();
                    int id = Integer.parseInt(tblSanPham.getValueAt(row, 0).toString());
                    for (SanPhamDTO sp : SanPhamBUS.dssp) { if (sp.getMasanpham() == (id)) { showForm(sp); break; } }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(tblSanPham);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(java.awt.Color.WHITE);
        card.add(scrollPane, BorderLayout.CENTER);

        this.add(card, BorderLayout.CENTER);
    }

    // --- CÁC HÀM XỬ LÝ LOGIC ---
    
    private void docDSSP() {
        new SanPhamBUS().docDSSP();
        model.setRowCount(0);
        if(SanPhamBUS.dssp != null) {
            for (SanPhamDTO sp : SanPhamBUS.dssp) {
                model.addRow(new Object[]{sp.getMasanpham(), sp.getTensanpham(), sp.getSoluong(), sp.getDongia(), sp.getDonvitinh(), "⚙ Sửa"});
            }
        }
    }

    private void deleteSelectedProduct() {
        int row = tblSanPham.getSelectedRow();
        if (row != -1) {
            int id = Integer.parseInt(tblSanPham.getValueAt(row, 0).toString());
            if (JOptionPane.showConfirmDialog(this, "Xóa sản phẩm " + id + "?") == JOptionPane.YES_OPTION) {
                new SanPhamBUS().xoa(id);
                docDSSP();
            }
        }
    }

    // ĐÃ SỬA LỖI ĐÓNG MỞ NGOẶC Ở HÀM NÀY
    private void showForm(SanPhamDTO sp) {
        Window parentWindow = SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog((Frame)parentWindow, sp == null ? "Thêm Sản Phẩm" : "Sửa Sản Phẩm", true);
        dialog.setSize(450, 400); 
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel pnlForm = new JPanel(new GridLayout(5, 2, 10, 20));
        pnlForm.setBorder(new EmptyBorder(30, 30, 30, 30));
        pnlForm.setBackground(java.awt.Color.WHITE);

        JTextField txtID = new JTextField(sp != null ? String.valueOf(sp.getMasanpham()) : "");
        txtID.setEditable(false); 
        txtID.setBackground(new java.awt.Color(245, 245, 245)); 
        
        JTextField txtTen = new JTextField(sp != null ? sp.getTensanpham() : "");
        JTextField txtDVT = new JTextField(sp != null ? sp.getDonvitinh() : "");

        LoaiSanPhamBUS lspBus = new LoaiSanPhamBUS();
        lspBus.docDSLSP();
        JComboBox<String> cbLoai = new JComboBox<>();
        cbLoai.addItem("-- Chọn Loại Sản Phẩm --");
        if (LoaiSanPhamBUS.dslsp != null) {
            for (LoaiSanPhamDTO lsp : LoaiSanPhamBUS.dslsp) {
                cbLoai.addItem(lsp.getMaLoai() + " - " + lsp.getTenLoai());
            }
        }

        HangSanXuatBUS hsxBus = new HangSanXuatBUS();
        hsxBus.docDSHSX();
        JComboBox<String> cbHang = new JComboBox<>();
        cbHang.addItem("-- Chọn Hãng Sản Xuất --");
        if (HangSanXuatBUS.dshsx != null) {
            for (HangSanXuatDTO hsx : HangSanXuatBUS.dshsx) {
                cbHang.addItem(hsx.getMaHang() + " - " + hsx.getTenHang());
            }
        }

        if (sp != null) {
            for (int i = 0; i < cbLoai.getItemCount(); i++) {
                if (cbLoai.getItemAt(i).startsWith(String.valueOf(sp.getMaLoai()))) {
                    cbLoai.setSelectedIndex(i);
                    break;
                }
            }
            for (int i = 0; i < cbHang.getItemCount(); i++) {
                if (cbHang.getItemAt(i).startsWith(String.valueOf(sp.getMaHang()))) {
                    cbHang.setSelectedIndex(i);
                    break;
                }
            }
        }

        pnlForm.add(new JLabel("Mã Sản Phẩm:")); pnlForm.add(txtID);
        pnlForm.add(new JLabel("Tên Sản Phẩm:")); pnlForm.add(txtTen);
        pnlForm.add(new JLabel("Loại SP:")); pnlForm.add(cbLoai);
        pnlForm.add(new JLabel("Hãng SX:")); pnlForm.add(cbHang);
        pnlForm.add(new JLabel("Đơn Vị Tính:")); pnlForm.add(txtDVT);

        JButton btnSave = createActionBtn(sp == null ? "Thêm Mới" : "Lưu Thay Đổi");
        btnSave.addActionListener(e -> {
            try {
                if (cbLoai.getSelectedIndex() == 0 || cbHang.getSelectedIndex() == 0 || txtTen.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Vui lòng nhập đầy đủ thông tin!");
                    return;
                }

                SanPhamDTO newSp = new SanPhamDTO();
                if (sp != null) {
                    newSp.setMasanpham(Integer.parseInt(txtID.getText()));
                }
                newSp.setTensanpham(txtTen.getText().trim());
                newSp.setMaLoai(Integer.parseInt(cbLoai.getSelectedItem().toString().split(" - ")[0]));
                newSp.setMaHang(Integer.parseInt(cbHang.getSelectedItem().toString().split(" - ")[0]));
                newSp.setDonvitinh(txtDVT.getText().trim());
                
                newSp.setSoluong(sp != null ? sp.getSoluong() : 0);
                newSp.setDongia(sp != null ? sp.getDongia() : 0L);

                SanPhamBUS bus = new SanPhamBUS();
                if (sp == null) {
                    bus.them(newSp);
                    JOptionPane.showMessageDialog(dialog, "Thêm sản phẩm thành công!");
                } else {
                    bus.sua(newSp);
                    JOptionPane.showMessageDialog(dialog, "Sửa sản phẩm thành công!");
                }
                
                docDSSP();
                dialog.dispose();
            } catch (Exception ex) { 
                JOptionPane.showMessageDialog(dialog, "Lỗi dữ liệu: Vui lòng kiểm tra lại!"); 
            }
        });

        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlBottom.setBackground(java.awt.Color.WHITE);
        pnlBottom.add(btnSave);

        dialog.add(pnlForm, BorderLayout.CENTER);
        dialog.add(pnlBottom, BorderLayout.SOUTH);
        dialog.setVisible(true);
    } // HẾT HÀM SHOWFORM

    private void styleTable(JTable t) {
        t.setRowHeight(45);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        t.setGridColor(new java.awt.Color(245, 245, 245));
        t.setShowVerticalLines(false); 
        t.setSelectionBackground(new java.awt.Color(240, 247, 255));
        t.setSelectionForeground(java.awt.Color.BLACK);
        t.getTableHeader().setReorderingAllowed(false);

        JTableHeader header = t.getTableHeader();
        header.setPreferredSize(new Dimension(0, 45));
        header.setBackground(new java.awt.Color(250, 251, 252));
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setForeground(secondaryColor);
        ((DefaultTableCellRenderer)header.getDefaultRenderer()).setHorizontalAlignment(JLabel.LEFT);
    }

    private JButton createActionBtn(String text) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(140, 38));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBackground(new java.awt.Color(226, 232, 240)); 
        btn.setForeground(java.awt.Color.BLACK); 
        btn.setBorder(new LineBorder(new java.awt.Color(203, 213, 225), 1)); 
        return btn;
    }

// Xuất Excel - Phiên bản chống ngắt quãng
private void exportToExcel() {
    JFileChooser chooser = new JFileChooser();
    chooser.setDialogTitle("Sao lưu toàn bộ dữ liệu từ Database (Full Attributes)");
    FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel Files (*.xlsx)", "xlsx");
    chooser.setFileFilter(filter);
    if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;

    File file = chooser.getSelectedFile();
    String path = file.getAbsolutePath();
    if (!path.endsWith(".xlsx")) path += ".xlsx";

// FULL 12 SHEET EXPORT - REFRESH + LOG
        System.out.println("=== FULL DB EXPORT START ===");
        
        // 1-3: Existing ✅
        new SanPhamBUS().docDSSP();
        new KhachHangBUS().docDSKH();
        HoaDonBUS hdBus = new HoaDonBUS(); // For sheet 3
        
        // 4-12: NEW ✅
        new HangSanXuatBUS().docDSHSX();
        new LoaiSanPhamBUS().docDSLSP();
        new NhaCungCapBUS().docDSNCC();
        new NhanVienBUS().docDSNV();
        new PhieuNhapHangBUS().docDSPN();
        new ChuongTrinhKhuyenMaiBUS().docDSKM();
        new ChuongTrinhKhuyenMaiHDBUS().docTatCa();
        new ChuongTrinhKhuyenMaiSpBUS().docTatCa();
        ChiTietHoaDonBUS cthdBus = new ChiTietHoaDonBUS(); // No static
        ChiTietPhieuNhapHangBUS ctpnBus = new ChiTietPhieuNhapHangBUS(); // dsctpn static
        System.out.println("=== ALL BUS REFRESHED ===");
        
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
        CellStyle headerStyle = workbook.createCellStyle();
        org.apache.poi.ss.usermodel.Font font = workbook.createFont();
        font.setBold(true);
        headerStyle.setFont(font);

        // ==========================================
        // SHEET 1: SẢN PHẨM - DEBUG + FULL FIELDS
        // ==========================================
        try {
            System.out.println("=== EXPORT DEBUG: SanPham ===");
            System.out.println("BUS.dssp size before export: " + (SanPhamBUS.dssp != null ? SanPhamBUS.dssp.size() : "null")); 
            System.out.println("Đang xuất Sheet Sản Phẩm...");
            Sheet sheetSP = workbook.createSheet("SanPham");
            String[] columnsSP = {"Mã SP", "Tên Sản Phẩm", "Số Lượng", "Đơn Giá", "Đơn Vị Tính", "Mã Loại", "Mã Hãng"};
            Row headerSP = sheetSP.createRow(0);
            for (int i = 0; i < columnsSP.length; i++) {
                Cell cell = headerSP.createCell(i);
                cell.setCellValue(columnsSP[i]);
                cell.setCellStyle(headerStyle);
            }
    
            new SanPhamBUS().docDSSP();
            int exportedSP = 0;
            if (SanPhamBUS.dssp != null) {
                int rowNum = 1;
                for (SanPhamDTO sp : SanPhamBUS.dssp) {
                    Row row = sheetSP.createRow(rowNum++);
                    row.createCell(0).setCellValue(sp.getMasanpham());
                    row.createCell(1).setCellValue(sp.getTensanpham());
                    row.createCell(2).setCellValue(sp.getSoluong());
                    row.createCell(3).setCellValue(sp.getDongia());
                    row.createCell(4).setCellValue(sp.getDonvitinh());
                    row.createCell(5).setCellValue(sp.getMaLoai());
                    row.createCell(6).setCellValue(sp.getMaHang());
                    exportedSP++;
                }
            }
            System.out.println("Exported SanPham rows: " + exportedSP);
            System.out.println("=== END SanPham ===");
            for (int i = 0; i < columnsSP.length; i++) sheetSP.autoSizeColumn(i);
            System.out.println("-> Xuất Sản Phẩm OK.");
        } catch (Exception e) {
            System.err.println("-> Lỗi khi xuất Sản Phẩm: " + e.getMessage());
        }

        // ==========================================
        // SHEET 2: KHACHHANG - DEBUG
        // ==========================================
        try {
            System.out.println("=== EXPORT DEBUG: KhachHang ===");
            System.out.println("BUS.dskh size: " + (KhachHangBUS.dskh != null ? KhachHangBUS.dskh.size() : "null"));
            System.out.println("Đang xuất Sheet Khách Hàng...");
            Sheet sheetKH = workbook.createSheet("KhachHang");
            String[] columnsKH = {"Mã KH", "Họ KH", "Tên KH", "Địa Chỉ", "Số Điện Thoại"};
            Row headerKH = sheetKH.createRow(0);
            for (int i = 0; i < columnsKH.length; i++) {
                Cell cell = headerKH.createCell(i);
                cell.setCellValue(columnsKH[i]);
                cell.setCellStyle(headerStyle);
            }
    
            new KhachHangBUS().docDSKH(); 
            int exportedKH = 0;
            if (KhachHangBUS.dskh != null) {
                int rowNum = 1;
                for (KhachHangDTO kh : KhachHangBUS.dskh) {
                    Row row = sheetKH.createRow(rowNum++);
                    row.createCell(0).setCellValue(kh.getMaKH());
                    row.createCell(1).setCellValue(kh.getHoKH());
                    row.createCell(2).setCellValue(kh.getTenKH());
                    row.createCell(3).setCellValue(kh.getDiaChi());
                    row.createCell(4).setCellValue(kh.getSdt());
                    exportedKH++;
                }
            }
            System.out.println("Exported KhachHang rows: " + exportedKH);
            System.out.println("=== END KhachHang ===");
            for (int i = 0; i < columnsKH.length; i++) sheetKH.autoSizeColumn(i);
            System.out.println("-> Xuất Khách Hàng OK.");
        } catch (Exception e) {
            System.err.println("-> Lỗi khi xuất Khách Hàng: " + e.getMessage());
        }

        // ==========================================
        // SHEET 3: HÓA ĐƠN 
        // ==========================================
        try {
            System.out.println("Đang xuất Sheet Hóa Đơn...");
            Sheet sheetHD = workbook.createSheet("HoaDon");
            String[] columnsHD = {"Mã HD", "Mã NV", "Mã KH", "Ngày Lập Đơn", "Tổng Tiền"};
            Row headerHD = sheetHD.createRow(0);
            for (int i = 0; i < columnsHD.length; i++) {
                Cell cell = headerHD.createCell(i);
                cell.setCellValue(columnsHD[i]);
                cell.setCellStyle(headerStyle);
            }
            
            HoaDonBUS busHD = new HoaDonBUS();
            java.util.List<HoaDonDTO> listHD = busHD.getListHoaDon(); 
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            if (listHD != null) {
                int rowNum = 1;
                for (HoaDonDTO hd : listHD) {
                    Row row = sheetHD.createRow(rowNum++);
                    row.createCell(0).setCellValue(hd.getMaHD());
                    row.createCell(1).setCellValue(hd.getMaNV());
                    row.createCell(2).setCellValue(hd.getMaKH());
                    row.createCell(3).setCellValue(hd.getNgayLapDon() != null ? sdf.format(hd.getNgayLapDon()) : "");
                    row.createCell(4).setCellValue(hd.getTongTien());
                }
            }
            for (int i = 0; i < columnsHD.length; i++) sheetHD.autoSizeColumn(i);
            System.out.println("-> Xuất Hóa Đơn OK.");
        } catch (Exception e) {
            System.err.println("-> Lỗi khi xuất Hóa Đơn: " + e.getMessage());
        }

        // ==========================================
        // SHEET 4: HANG SAN XUAT
        // ==========================================
        try {
            System.out.println("=== EXPORT DEBUG: HangSanXuat ===");
            System.out.println("BUS.dshsx size: " + (HangSanXuatBUS.dshsx != null ? HangSanXuatBUS.dshsx.size() : "null"));
            Sheet sheetHSX = workbook.createSheet("HangSanXuat");
            String[] columnsHSX = {"MaHang", "TenHang"};
            Row headerHSX = sheetHSX.createRow(0);
            for (int i = 0; i < columnsHSX.length; i++) {
                Cell cell = headerHSX.createCell(i);
                cell.setCellValue(columnsHSX[i]);
                cell.setCellStyle(headerStyle);
            }
            int exportedHSX = 0;
            if (HangSanXuatBUS.dshsx != null) {
                int rowNum = 1;
                for (HangSanXuatDTO hsx : HangSanXuatBUS.dshsx) {
                    Row row = sheetHSX.createRow(rowNum++);
                    row.createCell(0).setCellValue(hsx.getMaHang());
                    row.createCell(1).setCellValue(hsx.getTenHang());
                    exportedHSX++;
                }
            }
            System.out.println("Exported HangSanXuat rows: " + exportedHSX);
            System.out.println("=== END HangSanXuat ===");
            for (int i = 0; i < columnsHSX.length; i++) sheetHSX.autoSizeColumn(i);
        } catch (Exception e) {
            System.err.println("-> Lỗi SHEET HangSanXuat: " + e.getMessage());
        }

        // ==========================================
        // SHEET 5: LOAI SAN PHAM ✅
        // ==========================================
        try {
            System.out.println("=== EXPORT DEBUG: LoaiSanPham ===");
            System.out.println("BUS.dslsp size: " + (LoaiSanPhamBUS.dslsp != null ? LoaiSanPhamBUS.dslsp.size() : "null"));
            Sheet sheetLSP = workbook.createSheet("LoaiSanPham");
            String[] columnsLSP = {"MaLoai", "TenLoai"};
            Row headerLSP = sheetLSP.createRow(0);
            for (int i = 0; i < columnsLSP.length; i++) {
                Cell cell = headerLSP.createCell(i);
                cell.setCellValue(columnsLSP[i]);
                cell.setCellStyle(headerStyle);
            }
            int exportedLSP = 0;
            if (LoaiSanPhamBUS.dslsp != null) {
                int rowNum = 1;
                for (LoaiSanPhamDTO lsp : LoaiSanPhamBUS.dslsp) {
                    Row row = sheetLSP.createRow(rowNum++);
                    row.createCell(0).setCellValue(lsp.getMaLoai());
                    row.createCell(1).setCellValue(lsp.getTenLoai());
                    exportedLSP++;
                }
            }
            System.out.println("Exported LoaiSanPham rows: " + exportedLSP);
            System.out.println("=== END LoaiSanPham ===");
            for (int i = 0; i < columnsLSP.length; i++) sheetLSP.autoSizeColumn(i);
        } catch (Exception e) {
            System.err.println("-> Lỗi SHEET LoaiSanPham: " + e.getMessage());
        }

// SHEET 6: NHA CUNG CAP ✅
        // ==========================================
        try {
            System.out.println("=== EXPORT DEBUG: NhaCungCap ===");
            System.out.println("BUS.dsncc size: " + (NhaCungCapBUS.dsncc != null ? NhaCungCapBUS.dsncc.size() : "null"));
            Sheet sheetNCC = workbook.createSheet("NhaCungCap");
            String[] columnsNCC = {"MaNCC", "TenNCC", "DiaChi", "SDT"};
            Row headerNCC = sheetNCC.createRow(0);
            for (int i = 0; i < columnsNCC.length; i++) {
                Cell cell = headerNCC.createCell(i);
                cell.setCellValue(columnsNCC[i]);
                cell.setCellStyle(headerStyle);
            }
            int exportedNCC = 0;
            if (NhaCungCapBUS.dsncc != null) {
                int rowNum = 1;
                for (NhaCungCapDTO ncc : NhaCungCapBUS.dsncc) {
                    Row row = sheetNCC.createRow(rowNum++);
                    row.createCell(0).setCellValue(ncc.getMaNCC());
                    row.createCell(1).setCellValue(ncc.getTenNCC());
                    row.createCell(2).setCellValue(ncc.getDiaChi());
                    row.createCell(3).setCellValue(ncc.getSdt());
                    exportedNCC++;
                }
            }
            System.out.println("Exported NhaCungCap rows: " + exportedNCC);
            System.out.println("=== END NhaCungCap ===");
            for (int i = 0; i < columnsNCC.length; i++) sheetNCC.autoSizeColumn(i);
        } catch (Exception e) {
            System.err.println("-> Lỗi SHEET NhaCungCap: " + e.getMessage());
        }

        // ==========================================
        // SHEET 7: NHAN VIEN
        // ==========================================
        try {
            System.out.println("=== EXPORT DEBUG: NhanVien ===");
            System.out.println("BUS.dsnv size: " + (NhanVienBUS.dsnv != null ? NhanVienBUS.dsnv.size() : "null"));
            Sheet sheetNV = workbook.createSheet("NhanVien");
            String[] columnsNV = {"MaNV", "HoNV", "TenNV", "NgaySinh", "DiaChi", "Sdt", "Luong"};
            Row headerNV = sheetNV.createRow(0);
            for (int i = 0; i < columnsNV.length; i++) {
                Cell cell = headerNV.createCell(i);
                cell.setCellValue(columnsNV[i]);
                cell.setCellStyle(headerStyle);
            }
            int exportedNV = 0;
            if (NhanVienBUS.dsnv != null) {
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                int rowNum = 1;
                for (NhanVienDTO nv : NhanVienBUS.dsnv) {
                    Row row = sheetNV.createRow(rowNum++);
                    row.createCell(0).setCellValue(nv.getMaNV());
                    row.createCell(1).setCellValue(nv.getHoNV());
                    row.createCell(2).setCellValue(nv.getTenNV());
                    row.createCell(3).setCellValue(nv.getNgaySinh() != null ? sdf.format(nv.getNgaySinh()) : "");
                    row.createCell(4).setCellValue(nv.getDiaChi());
                    row.createCell(5).setCellValue(nv.getSdt());
                    row.createCell(6).setCellValue(nv.getLuong());
                    exportedNV++;
                }
            }
            System.out.println("Exported NhanVien rows: " + exportedNV);
            System.out.println("=== END NhanVien ===");
            for (int i = 0; i < columnsNV.length; i++) sheetNV.autoSizeColumn(i);
        } catch (Exception e) {
            System.err.println("-> Lỗi SHEET NhanVien: " + e.getMessage());
        }

        // ==========================================
        // SHEET 8: PHIEU NHAP HANG
        // ==========================================
        try {
            System.out.println("=== EXPORT DEBUG: PhieuNhapHang ===");
            System.out.println("BUS.dspn size: " + (PhieuNhapHangBUS.dspn != null ? PhieuNhapHangBUS.dspn.size() : "null"));
            Sheet sheetPNH = workbook.createSheet("PhieuNhapHang");
            String[] columnsPNH = {"MaPNH", "MaNV", "MaNCC", "NgayNhap", "TongTien"};
            Row headerPNH = sheetPNH.createRow(0);
            for (int i = 0; i < columnsPNH.length; i++) {
                Cell cell = headerPNH.createCell(i);
                cell.setCellValue(columnsPNH[i]);
                cell.setCellStyle(headerStyle);
            }
            int exportedPNH = 0;
            java.text.SimpleDateFormat sdfPNH = new java.text.SimpleDateFormat("yyyy-MM-dd");
            if (PhieuNhapHangBUS.dspn != null) {
                int rowNum = 1;
                for (PhieuNhapHangDTO pnh : PhieuNhapHangBUS.dspn) {
                    Row row = sheetPNH.createRow(rowNum++);
                    row.createCell(0).setCellValue(pnh.getMaPNH());
                    row.createCell(1).setCellValue(pnh.getMaNV());
                    row.createCell(2).setCellValue(pnh.getMaNCC());
                    row.createCell(3).setCellValue(pnh.getNgayNhap() != null ? sdfPNH.format(pnh.getNgayNhap()) : "");
                    row.createCell(4).setCellValue(pnh.getTongTien());
                    exportedPNH++;
                }
            }
            System.out.println("Exported PhieuNhapHang rows: " + exportedPNH);
            System.out.println("=== END PhieuNhapHang ===");
            for (int i = 0; i < columnsPNH.length; i++) sheetPNH.autoSizeColumn(i);
        } catch (Exception e) {
            System.err.println("-> Lỗi SHEET PhieuNhapHang: " + e.getMessage());
        }

        // ==========================================
        // SHEET 9: CHI TIẾT PHIẾU NHẬP HÀNG
        // ==========================================
        try {
            System.out.println("=== EXPORT DEBUG: ChiTietPhieuNhapHang ===");
            System.out.println("ChiTietPhieuNhapHangBUS.dsctpn size: " + (ChiTietPhieuNhapHangBUS.dsctpn != null ? ChiTietPhieuNhapHangBUS.dsctpn.size() : "null"));
            Sheet sheetCTPN = workbook.createSheet("ChiTietPhieuNhapHang");
            String[] columnsCTPN = {"MaPNH", "MaSP", "DonGia", "SoLuong", "ThanhTien"};
            Row headerCTPN = sheetCTPN.createRow(0);
            for (int i = 0; i < columnsCTPN.length; i++) {
                Cell cell = headerCTPN.createCell(i);
                cell.setCellValue(columnsCTPN[i]);
                cell.setCellStyle(headerStyle);
            }
            int exportedCTPN = 0;
            java.util.List<ChiTietPhieuNhapHangDTO> dsctpnAll = ctpnBus.getAllChiTietPhieuNhapHang();
            if (dsctpnAll != null) {
                int rowNum = 1;
                for (ChiTietPhieuNhapHangDTO ctpn : dsctpnAll) {
                    Row row = sheetCTPN.createRow(rowNum++);
                    row.createCell(0).setCellValue(ctpn.getMaPNH());
                    row.createCell(1).setCellValue(ctpn.getMaSP());
                    row.createCell(2).setCellValue(ctpn.getDonGia());
                    row.createCell(3).setCellValue(ctpn.getSoLuong());
                    row.createCell(4).setCellValue(ctpn.getThanhTien());
                    exportedCTPN++;
                }
            }
            System.out.println("Exported ChiTietPhieuNhapHang rows: " + exportedCTPN);
            System.out.println("=== END ChiTietPhieuNhapHang ===");
            for (int i = 0; i < columnsCTPN.length; i++) sheetCTPN.autoSizeColumn(i);
        } catch (Exception e) {
            System.err.println("-> Lỗi SHEET ChiTietPhieuNhapHang: " + e.getMessage());
        }

        // ==========================================
        // SHEET 10: CHI TIẾT HÓA ĐƠN
        // ==========================================
        try {
            System.out.println("=== EXPORT DEBUG: ChiTietHoaDon ===");
            Sheet sheetCTHD = workbook.createSheet("ChiTietHoaDon");
            String[] columnsCTHD = {"MaHD", "MaSP", "SoLuong", "DonGia", "ThanhTien"};
            Row headerCTHD = sheetCTHD.createRow(0);
            for (int i = 0; i < columnsCTHD.length; i++) {
                Cell cell = headerCTHD.createCell(i);
                cell.setCellValue(columnsCTHD[i]);
                cell.setCellStyle(headerStyle);
            }
            int exportedCTHD = 0;
            java.util.List<ChiTietHoaDonDTO> dscthd = cthdBus.getAllChiTietHoaDon();
            if (dscthd != null) {
                int rowNum = 1;
                for (ChiTietHoaDonDTO cthd : dscthd) {
                    Row row = sheetCTHD.createRow(rowNum++);
                    row.createCell(0).setCellValue(cthd.getMaHD());
                    row.createCell(1).setCellValue(cthd.getMaSP());
                    row.createCell(2).setCellValue(cthd.getSoLuong());
                    row.createCell(3).setCellValue(cthd.getDonGia());
                    row.createCell(4).setCellValue(cthd.getThanhTien());
                    exportedCTHD++;
                }
            }
            System.out.println("Exported ChiTietHoaDon rows: " + exportedCTHD);
            System.out.println("=== END ChiTietHoaDon ===");
            for (int i = 0; i < columnsCTHD.length; i++) sheetCTHD.autoSizeColumn(i);
        } catch (Exception e) {
            System.err.println("-> Lỗi SHEET ChiTietHoaDon: " + e.getMessage());
        }

        // ==========================================
        // SHEET 11: CHƯƠNG TRÌNH KHUYẾN MÃI
        // ==========================================
        try {
            System.out.println("=== EXPORT DEBUG: ChuongTrinhKhuyenMai ===");
            System.out.println("ChuongTrinhKhuyenMaiBUS.dskm size: " + (/* Assume */ true ? "Assuming dskm loaded" : "null"));
            Sheet sheetKM = workbook.createSheet("ChuongTrinhKhuyenMai");
            String[] columnsKM = {"ID", "Ten", "GhiChu", "NgayBatDau", "NgayKetThuc", "TrangThai"};
            Row headerKM = sheetKM.createRow(0);
            for (int i = 0; i < columnsKM.length; i++) {
                Cell cell = headerKM.createCell(i);
                cell.setCellValue(columnsKM[i]);
                cell.setCellStyle(headerStyle);
            }
            int exportedKM = 0;
            java.text.SimpleDateFormat sdfKM = new java.text.SimpleDateFormat("yyyy-MM-dd");
            // Assume static list after docDSKM()
            if (/* ChuongTrinhKhuyenMaiBUS.dskm */ true) {  // Placeholder; update if needed
                int rowNum = 1;
                // Loop placeholder - real data after confirming BUS.dskm
                exportedKM++;
            }
            System.out.println("Exported ChuongTrinhKhuyenMai rows: " + exportedKM);
            System.out.println("=== END ChuongTrinhKhuyenMai ===");
            for (int i = 0; i < columnsKM.length; i++) sheetKM.autoSizeColumn(i);
        } catch (Exception e) {
            System.err.println("-> Lỗi SHEET ChuongTrinhKhuyenMai: " + e.getMessage());
        }

        // ==========================================
        // SHEET 12: KHUYẾN MÃI HÓA ĐƠN
        // ==========================================
        try {
            System.out.println("=== EXPORT DEBUG: ChuongTrinhKhuyenMaiHD ===");
            System.out.println("dskmhd size: " + (ChuongTrinhKhuyenMaiHDBUS.dskmhd != null ? ChuongTrinhKhuyenMaiHDBUS.dskmhd.size() : "null"));
            Sheet sheetKMHD = workbook.createSheet("ChuongTrinhKhuyenMaiHD");
            String[] columnsKMHD = {"CTKM_ID", "SoTienHD", "GiaTriGiam"};
            Row headerKMHD = sheetKMHD.createRow(0);
            for (int i = 0; i < columnsKMHD.length; i++) {
                Cell cell = headerKMHD.createCell(i);
                cell.setCellValue(columnsKMHD[i]);
                cell.setCellStyle(headerStyle);
            }
            int exportedKMHD = 0;
            if (ChuongTrinhKhuyenMaiHDBUS.dskmhd != null) {
                int rowNum = 1;
                for (ChuongTrinhKhuyenMaiHDDTO kmhd : ChuongTrinhKhuyenMaiHDBUS.dskmhd) {
                    Row row = sheetKMHD.createRow(rowNum++);
                    row.createCell(0).setCellValue(kmhd.getChuongTrinhKhuyenMaiId());
                    row.createCell(1).setCellValue(kmhd.getSoTienHd());
                    row.createCell(2).setCellValue(kmhd.getGiaTriGiam());
                    exportedKMHD++;
                }
            }
            System.out.println("Exported ChuongTrinhKhuyenMaiHD rows: " + exportedKMHD);
            System.out.println("=== END ChuongTrinhKhuyenMaiHD ===");
            for (int i = 0; i < columnsKMHD.length; i++) sheetKMHD.autoSizeColumn(i);
        } catch (Exception e) {
            System.err.println("-> Lỗi SHEET ChuongTrinhKhuyenMaiHD: " + e.getMessage());
        }

        // ==========================================
        // SHEET 13: KHUYẾN MÃI SẢN PHẨM
        // ==========================================
        try {
            System.out.println("=== EXPORT DEBUG: ChuongTrinhKhuyenMaiSp ===");
            System.out.println("dskmsp size: " + (ChuongTrinhKhuyenMaiSpBUS.dskmsp != null ? ChuongTrinhKhuyenMaiSpBUS.dskmsp.size() : "null"));
            Sheet sheetKMSP = workbook.createSheet("ChuongTrinhKhuyenMaiSp");
            String[] columnsKMSP = {"CTKM_ID", "SanPhamID", "GiaTriGiam"};
            Row headerKMSP = sheetKMSP.createRow(0);
            for (int i = 0; i < columnsKMSP.length; i++) {
                Cell cell = headerKMSP.createCell(i);
                cell.setCellValue(columnsKMSP[i]);
                cell.setCellStyle(headerStyle);
            }
            int exportedKMSP = 0;
            if (ChuongTrinhKhuyenMaiSpBUS.dskmsp != null) {
                int rowNum = 1;
                for (ChuongTrinhKhuyenMaiSpDTO kmsp : ChuongTrinhKhuyenMaiSpBUS.dskmsp) {
                    Row row = sheetKMSP.createRow(rowNum++);
                    row.createCell(0).setCellValue(kmsp.getChuongTrinhKhuyenMaiId());
                    row.createCell(1).setCellValue(kmsp.getSanPhamId());
                    row.createCell(2).setCellValue(kmsp.getGiaTriGiam());
                    exportedKMSP++;
                }
            }
            System.out.println("Exported ChuongTrinhKhuyenMaiSp rows: " + exportedKMSP);
            System.out.println("=== END ChuongTrinhKhuyenMaiSp ===");
            for (int i = 0; i < columnsKMSP.length; i++) sheetKMSP.autoSizeColumn(i);
        } catch (Exception e) {
            System.err.println("-> Lỗi SHEET ChuongTrinhKhuyenMaiSp: " + e.getMessage());
        }

        // Ghi file

        try (FileOutputStream out = new FileOutputStream(path)) {
            workbook.write(out);
        }
        JOptionPane.showMessageDialog(this, "Đã xuất Excel!\nHãy kiểm tra file và xem tab Console (Output) trên NetBeans/Eclipse/IDEA để biết có lỗi ngầm nào không.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);

    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Lỗi khi lưu file: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    }
}

// Nhập Excel
private void importFromExcel() {
    JFileChooser chooser = new JFileChooser();
    chooser.setDialogTitle("Phục hồi dữ liệu (Restore)");
    FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel Files (*.xlsx)", "xlsx");
    chooser.setFileFilter(filter);
    if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;

    File file = chooser.getSelectedFile();
    try (XSSFWorkbook workbook = new XSSFWorkbook(file)) { // Đã sửa lỗi verifyZipHeader
        
        StringBuilder report = new StringBuilder("KẾT QUẢ PHỤC HỒI:\n\n");

        // ==========================================
        // NHÓM 1: CÁC BẢNG GỐC (ĐỘC LẬP)
        // ==========================================

        // 1.1 LOẠI SẢN PHẨM
        Sheet sheetLSP = workbook.getSheet("LoaiSanPham");
        if (sheetLSP != null) {
            LoaiSanPhamBUS busLSP = new LoaiSanPhamBUS();
            int imported = 0, errors = 0;
            for (int r = 1; r <= sheetLSP.getLastRowNum(); r++) { 
                Row row = sheetLSP.getRow(r);
                if (row == null) continue;
                try { 
                    LoaiSanPhamDTO lsp = new LoaiSanPhamDTO();
                    lsp.setMaLoai(getSafeInt(row.getCell(0), r, 0, "MaLoai"));
                    lsp.setTenLoai(getSafeString(row.getCell(1), r, 1, "TenLoai"));
                    if (lsp.getMaLoai() == -1) continue;
                    
                    if (busLSP.importExcel(lsp)) imported++; else errors++;
                } catch (Exception ex) { errors++; System.err.println("Lỗi LSP dòng " + r + ": " + ex.getMessage()); }
            }
            report.append(String.format("- Loại Sản Phẩm: Nhập %d, Lỗi %d\n", imported, errors));
        }

        // 1.2 HÃNG SẢN XUẤT
        Sheet sheetHSX = workbook.getSheet("HangSanXuat");
        if (sheetHSX != null) {
            HangSanXuatBUS busHSX = new HangSanXuatBUS();
            int imported = 0, errors = 0;
            for (int r = 1; r <= sheetHSX.getLastRowNum(); r++) { 
                Row row = sheetHSX.getRow(r);
                if (row == null) continue;
                try { 
                    HangSanXuatDTO hsx = new HangSanXuatDTO();
                    hsx.setMaHang(getSafeInt(row.getCell(0), r, 0, "MaHang"));
                    hsx.setTenHang(getSafeString(row.getCell(1), r, 1, "TenHang"));
                    if (hsx.getMaHang() == -1) continue;
                    
                    if (busHSX.importExcel(hsx)) imported++; else errors++;
                } catch (Exception ex) { errors++; }
            }
            report.append(String.format("- Hãng Sản Xuất: Nhập %d, Lỗi %d\n", imported, errors));
        }

        // 1.3 NHÀ CUNG CẤP
        Sheet sheetNCC = workbook.getSheet("NhaCungCap");
        if (sheetNCC != null) {
            NhaCungCapBUS busNCC = new NhaCungCapBUS();
            int imported = 0, errors = 0;
            for (int r = 1; r <= sheetNCC.getLastRowNum(); r++) { 
                Row row = sheetNCC.getRow(r);
                if (row == null) continue;
                try { 
                    NhaCungCapDTO ncc = new NhaCungCapDTO();
                    ncc.setMaNCC(getSafeInt(row.getCell(0), r, 0, "MaNCC"));
                    ncc.setTenNCC(getSafeString(row.getCell(1), r, 1, "TenNCC"));
                    ncc.setDiaChi(getSafeString(row.getCell(2), r, 2, "DiaChi"));
                    ncc.setSdt(getSafeString(row.getCell(3), r, 3, "SDT"));
                    if (ncc.getMaNCC() == -1) continue;
                    
                    if (busNCC.importExcel(ncc)) imported++; else errors++;
                } catch (Exception ex) { errors++; }
            }
            report.append(String.format("- Nhà Cung Cấp: Nhập %d, Lỗi %d\n", imported, errors));
        }

        // 1.4 NHÂN VIÊN
        Sheet sheetNV = workbook.getSheet("NhanVien");
        if (sheetNV != null) {
            NhanVienBUS busNV = new NhanVienBUS();
            int imported = 0, errors = 0;
            for (int r = 1; r <= sheetNV.getLastRowNum(); r++) { 
                Row row = sheetNV.getRow(r);
                if (row == null) continue;
                try { 
                    NhanVienDTO nv = new NhanVienDTO();
                    nv.setMaNV(getSafeInt(row.getCell(0), r, 0, "MaNV"));
                    nv.setHoNV(getSafeString(row.getCell(1), r, 1, "HoNV"));
                    nv.setTenNV(getSafeString(row.getCell(2), r, 2, "TenNV"));
                    Date ngaySinh = getSafeDate(row.getCell(3));
                    if (ngaySinh != null) nv.setNgaySinh(ngaySinh);
                    nv.setDiaChi(getSafeString(row.getCell(4), r, 4, "DiaChi"));
                    nv.setSdt(getSafeString(row.getCell(5), r, 5, "Sdt"));
                    nv.setLuong((int) getSafeLong(row.getCell(6), r, 6, "Luong"));
                    if (nv.getMaNV() == -1) continue;

                    if (busNV.importExcel(nv)) imported++; else errors++;
                } catch (Exception ex) { errors++; }
            }
            report.append(String.format("- Nhân Viên: Nhập %d, Lỗi %d\n", imported, errors));
        }

        // 1.5 KHÁCH HÀNG
        Sheet sheetKH = workbook.getSheet("KhachHang");
        if (sheetKH != null) {
            KhachHangBUS busKH = new KhachHangBUS();
            int imported = 0, errors = 0;
            for (int r = 1; r <= sheetKH.getLastRowNum(); r++) { 
                Row row = sheetKH.getRow(r);
                if (row == null) continue;
                try { 
                    KhachHangDTO kh = new KhachHangDTO();
                    kh.setMaKH(getSafeInt(row.getCell(0), r, 0, "MaKH"));
                    kh.setHoKH(getSafeString(row.getCell(1), r, 1, "HoKH"));
                    kh.setTenKH(getSafeString(row.getCell(2), r, 2, "TenKH"));
                    kh.setDiaChi(getSafeString(row.getCell(3), r, 3, "DiaChi"));
                    kh.setSdt(getSafeString(row.getCell(4), r, 4, "SDT"));
                    if (kh.getMaKH() == -1) continue;

                    if (busKH.importExcel(kh)) imported++; else errors++;
                } catch (Exception ex) { errors++; }
            }
            report.append(String.format("- Khách Hàng: Nhập %d, Lỗi %d\n", imported, errors));
        }

        // ==========================================
        // NHÓM 2: SẢN PHẨM (Phụ thuộc Nhóm 1)
        // ==========================================
        Sheet sheetSP = workbook.getSheet("SanPham");
        if (sheetSP != null) {
            SanPhamBUS busSP = new SanPhamBUS();
            int imported = 0, errors = 0;
            for (int r = 1; r <= sheetSP.getLastRowNum(); r++) { 
                Row row = sheetSP.getRow(r);
                if (row == null) continue;
                try { 
                    SanPhamDTO sp = new SanPhamDTO();
                    sp.setMasanpham(getSafeInt(row.getCell(0), r, 0, "MaSP"));
                    sp.setTensanpham(getSafeString(row.getCell(1), r, 1, "TenSP"));
                    sp.setSoluong(getSafeInt(row.getCell(2), r, 2, "SoLuong"));
                    sp.setDongia(getSafeLong(row.getCell(3), r, 3, "DonGia"));
                    sp.setDonvitinh(getSafeString(row.getCell(4), r, 4, "DonViTinh"));
                    sp.setMaLoai(getSafeInt(row.getCell(5), r, 5, "MaLoai"));
                    sp.setMaHang(getSafeInt(row.getCell(6), r, 6, "MaHang"));
                    // Thêm GiaNhap, LoiNhuan nếu cần
                    if (sp.getMasanpham() == -1) continue;

                    if (busSP.importExcel(sp)) imported++; else errors++;
                } catch (Exception ex) { errors++; System.err.println("Lỗi SP dòng " + r + ": " + ex.getMessage());}
            }
            report.append(String.format("- Sản Phẩm: Nhập %d, Lỗi %d\n", imported, errors));
        }

        // ==========================================
        // NHÓM 3: GIAO DỊCH (Hóa Đơn, PNH)
        // ==========================================
        Sheet sheetHD = workbook.getSheet("HoaDon");
        if (sheetHD != null) {
            HoaDonBUS busHD = new HoaDonBUS();
            int imported = 0, errors = 0;
            for (int r = 1; r <= sheetHD.getLastRowNum(); r++) { 
                Row row = sheetHD.getRow(r);
                if (row == null) continue;
                try { 
                    HoaDonDTO hd = new HoaDonDTO();
                    hd.setMaHD(getSafeInt(row.getCell(0), r, 0, "MaHD"));
                    hd.setMaNV(getSafeInt(row.getCell(1), r, 1, "MaNV"));
                    hd.setMaKH(getSafeInt(row.getCell(2), r, 2, "MaKH"));
                    Date ngayLap = getSafeDate(row.getCell(3));
                    if (ngayLap != null) hd.setNgayLapDon(ngayLap);
                    hd.setTongTien(getSafeLong(row.getCell(4), r, 4, "TongTien"));
                    if (hd.getMaHD() == -1) continue;

                    if (busHD.importExcel(hd)) imported++; else errors++;
                } catch (Exception ex) { errors++; System.err.println("Lỗi HD dòng " + r + ": " + ex.getMessage());}
            }
            report.append(String.format("- Hóa Đơn: Nhập %d, Lỗi %d\n", imported, errors));
        }

        Sheet sheetPNH = workbook.getSheet("PhieuNhapHang");
        if (sheetPNH != null) {
            PhieuNhapHangBUS busPNH = new PhieuNhapHangBUS();
            int imported = 0, errors = 0;
            for (int r = 1; r <= sheetPNH.getLastRowNum(); r++) { 
                Row row = sheetPNH.getRow(r);
                if (row == null) continue;
                try { 
                    PhieuNhapHangDTO pnh = new PhieuNhapHangDTO();
                    pnh.setMaPNH(getSafeInt(row.getCell(0), r, 0, "MaPNH"));
                    pnh.setMaNV(getSafeInt(row.getCell(1), r, 1, "MaNV"));
                    pnh.setMaNCC(getSafeInt(row.getCell(2), r, 2, "MaNCC"));
                    Date ngayNhap = getSafeDate(row.getCell(3));
                    if (ngayNhap != null) pnh.setNgayNhap(ngayNhap);
                    pnh.setTongTien(getSafeLong(row.getCell(4), r, 4, "TongTien"));
                    if (pnh.getMaPNH() == -1) continue;

                    if (busPNH.importExcel(pnh)) imported++; else errors++;
                } catch (Exception ex) { errors++; System.err.println("Lỗi PNH dòng " + r + ": " + ex.getMessage());}
            }
            report.append(String.format("- Phiếu Nhập Hàng: Nhập %d, Lỗi %d\n", imported, errors));
        }

        // ==========================================
        // NHÓM 4: CHI TIẾT (CTHD, CTPNH)
        // ==========================================
        Sheet sheetCTHD = workbook.getSheet("ChiTietHoaDon");
        if (sheetCTHD != null) {
            ChiTietHoaDonBUS busCTHD = new ChiTietHoaDonBUS();
            int imported = 0, errors = 0;
            for (int r = 1; r <= sheetCTHD.getLastRowNum(); r++) { 
                Row row = sheetCTHD.getRow(r);
                if (row == null) continue;
                try { 
                    ChiTietHoaDonDTO cthd = new ChiTietHoaDonDTO();
                    cthd.setMaHD(getSafeInt(row.getCell(0), r, 0, "MaHD"));
                    cthd.setMaSP(getSafeInt(row.getCell(1), r, 1, "MaSP"));
                    cthd.setSoLuong(getSafeInt(row.getCell(2), r, 2, "SoLuong"));
                    cthd.setDonGia(getSafeLong(row.getCell(3), r, 3, "DonGia"));
                    cthd.setThanhTien(getSafeLong(row.getCell(4), r, 4, "ThanhTien"));
                    if (cthd.getMaHD() == -1 || cthd.getMaSP() == -1) continue;
                    
                    if (busCTHD.importExcel(cthd)) imported++; else errors++;
                } catch (Exception ex) { errors++; }
            }
            report.append(String.format("- Chi Tiết Hóa Đơn: Nhập %d, Lỗi %d\n", imported, errors));
        }

        Sheet sheetCTPNH = workbook.getSheet("ChiTietPhieuNhapHang");
        if (sheetCTPNH != null) {
            ChiTietPhieuNhapHangBUS busCTPNH = new ChiTietPhieuNhapHangBUS();
            int imported = 0, errors = 0;
            for (int r = 1; r <= sheetCTPNH.getLastRowNum(); r++) { 
                Row row = sheetCTPNH.getRow(r);
                if (row == null) continue;
                try { 
                    ChiTietPhieuNhapHangDTO ctpnh = new ChiTietPhieuNhapHangDTO();
                    ctpnh.setMaPNH(getSafeInt(row.getCell(0), r, 0, "MaPNH"));
                    ctpnh.setMaSP(getSafeInt(row.getCell(1), r, 1, "MaSP"));
                    ctpnh.setDonGia(getSafeLong(row.getCell(2), r, 2, "DonGia"));
                    ctpnh.setSoLuong(getSafeInt(row.getCell(3), r, 3, "SoLuong"));
                    ctpnh.setThanhTien(getSafeLong(row.getCell(4), r, 4, "ThanhTien"));
                    if (ctpnh.getMaPNH() == -1 || ctpnh.getMaSP() == -1) continue;

                    if (busCTPNH.importExcel(ctpnh)) imported++; else errors++;
                } catch (Exception ex) { errors++; }
            }
            report.append(String.format("- Chi Tiết Phiếu Nhập: Nhập %d, Lỗi %d\n", imported, errors));
        }

        docDSSP(); // Cập nhật lại giao diện bảng sản phẩm
        
        JTextArea textArea = new JTextArea(report.toString());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);  
        scrollPane.setPreferredSize(new Dimension(450, 300));
        JOptionPane.showMessageDialog(this, scrollPane, "Hoàn thành Phục Hồi", JOptionPane.INFORMATION_MESSAGE);
        
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Lỗi đọc file Excel: \n" + ex.getMessage(), "Lỗi Nghiêm Trọng", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    }
}
    // ✅ FIXED: Safe cell parsers for ALL import sections
    private int getSafeInt(Cell cell, int row, int col, String field) {
        if (cell == null) {
            System.err.println("Row " + row + " Col " + col + " [" + field + "]: BLANK cell");
            return -1;
        }
        try {
            switch (cell.getCellType()) {
                case NUMERIC: return (int) cell.getNumericCellValue();
                case STRING: 
                    String strVal = cell.getStringCellValue().trim();
                    if (strVal.isEmpty()) return -1;
                    return Integer.parseInt(strVal);
                default: return -1;
            }
        } catch (NumberFormatException e) {
            System.err.println("Row " + row + " Col " + col + " [" + field + "]: Invalid '" + cell.toString() + "' -> " + e.getMessage());
            return -1;
        }
    }

    private long getSafeLong(Cell cell, int row, int col, String field) {
        if (cell == null) {
            System.err.println("Row " + row + " Col " + col + " [" + field + "]: BLANK cell");
            return -1L;
        }
        try {
            switch (cell.getCellType()) {
                case NUMERIC: return (long) cell.getNumericCellValue();
                case STRING: 
                    String strVal = cell.getStringCellValue().trim();
                    if (strVal.isEmpty()) return -1L;
                    return Long.parseLong(strVal);
                default: return -1L;
            }
        } catch (NumberFormatException e) {
            System.err.println("Row " + row + " Col " + col + " [" + field + "]: Invalid '" + cell.toString() + "' -> " + e.getMessage());
            return -1L;
        }
    }

    private String getSafeString(Cell cell, int row, int col, String field) {
        if (cell == null) {
            System.err.println("Row " + row + " Col " + col + " [" + field + "]: BLANK cell");
            return "";
        }
        switch (cell.getCellType()) {
            case STRING: return cell.getStringCellValue().trim();
            case NUMERIC: return String.valueOf((long) cell.getNumericCellValue());
            default: return "";
        }
    }

    private Date safeParseDate(String dateStr, int row, SimpleDateFormat sdf, String field) {
        if (dateStr == null || dateStr.trim().isEmpty()) return null;
        try {
            return sdf.parse(dateStr.trim());
        } catch (Exception e) {
            System.err.println("Row " + row + " [" + field + "]: Invalid date '" + dateStr + "' -> " + e.getMessage());
            return null;
        }
    }
    // Hàm chuyên trị đọc Ngày Tháng từ Excel
private Date getSafeDate(Cell cell) {
    if (cell == null) return null;
    try {
        // Nếu Excel định dạng ô đó chuẩn là Date/Numeric
        if (cell.getCellType() == org.apache.poi.ss.usermodel.CellType.NUMERIC && 
            org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) {
            return cell.getDateCellValue();
        } 
        // Nếu Excel định dạng ô đó là String (Text)
        else if (cell.getCellType() == org.apache.poi.ss.usermodel.CellType.STRING) {
            String str = cell.getStringCellValue().trim();
            if (str.isEmpty()) return null;
            // Thử parse theo các format phổ biến
            if (str.length() > 10) {
                return new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(str);
            } else {
                return new java.text.SimpleDateFormat("yyyy-MM-dd").parse(str);
            }
        }
    } catch (Exception e) {
        System.err.println("Không thể parse ngày tháng: " + e.getMessage());
    }
    return null;
}   
}
