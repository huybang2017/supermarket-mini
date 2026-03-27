package SieuThiMiniBUS;

import DTO.*;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.Component;

public class Excel {
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

    private java.sql.Date toSqlDate(java.util.Date date) {
        return (date == null) ? null : new java.sql.Date(date.getTime());
    }
    private Date getSafeDate(Cell cell) {
        if (cell == null) return null;
        try {
            if (cell.getCellType() == org.apache.poi.ss.usermodel.CellType.NUMERIC && 
                org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) {
                return cell.getDateCellValue();
            } 
            else if (cell.getCellType() == org.apache.poi.ss.usermodel.CellType.STRING) {
                String str = cell.getStringCellValue().trim();
                if (str.isEmpty()) return null;
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

    public void exportFullDatabase(Component parent) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Sao lưu toàn bộ dữ liệu từ Database");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel Files (*.xlsx)", "xlsx");
        chooser.setFileFilter(filter);
        
        if (chooser.showSaveDialog(parent) != JFileChooser.APPROVE_OPTION) return;

        File file = chooser.getSelectedFile();
        String path = file.getAbsolutePath();
        if (!path.endsWith(".xlsx")) path += ".xlsx";

        refreshAllData();

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            CellStyle headerStyle = createHeaderStyle(workbook);

            // Sheet 1: Sản Phẩm
            exportSheetSanPham(workbook, headerStyle);
            // Sheet 2: Khách Hàng
            exportSheetKhachHang(workbook, headerStyle);
            // Sheet 3: Hóa Đơn
            exportSheetHoaDon(workbook, headerStyle);
            // Sheet 4: Hãng sản xuất
            exportSheetHSX(workbook, headerStyle);
            // Sheet 5: Loại sản phẩm
            exportSheetLSP(workbook, headerStyle);
            // Sheet 6: Nhà cung cấp
            exportSheetNCC(workbook, headerStyle);
            // Sheet 7: Nhân viên
            exportSheetNhanvVien(workbook,headerStyle);
            // Sheet 8: Phiếu nhập hàng
            exportSheetPhieuNhapHang(workbook,headerStyle);
            // Sheet 9: Chi tiết phiếu nhập hàng
            exportSheetCTPNH(workbook,headerStyle);
            // Sheet 10: Chi tiết hóa đơn
            exportSheetCTHD(workbook,headerStyle);
            // Sheet 11: Chương trình khuyến mãi
            exportSheetCTKM(workbook,headerStyle);
            // Sheet 12: Khuyến mãi hóa đơn
            exportSheetHDKM(workbook,headerStyle);
            // Sheet 13: Khuyến mãi sản phẩm
            exportSheetSPKM(workbook,headerStyle);

            try (FileOutputStream out = new FileOutputStream(path)) {
                workbook.write(out);
                JOptionPane.showMessageDialog(parent, "Xuất Excel thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(parent, "Lỗi khi lưu file: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    // --- HÀM NHẬP EXCEL (PHỤC HỒI DỮ LIỆU) ---
    public String importFullDatabase(Component parent) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Phục hồi dữ liệu (Restore)");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel Files (*.xlsx)", "xlsx");
        chooser.setFileFilter(filter);
        
        if (chooser.showOpenDialog(parent) != JFileChooser.APPROVE_OPTION) return null;

        File file = chooser.getSelectedFile();
        StringBuilder report = new StringBuilder("KẾT QUẢ PHỤC HỒI:\n\n");

        try (XSSFWorkbook workbook = new XSSFWorkbook(file)) {
            // 1. Nhập Loại Sản Phẩm
            importSheetLSP(workbook, report);
            // 2. Nhập Hãng Sản Xuất
            importSheetHSX(workbook, report);
            // 3. Nhập Sản Phẩm
            importSheetSP(workbook, report);
            // 4.Nhập nhà cung cấp
            importSheetNCC(workbook, report);
            // 5.Nhập nhân viên
            importSheetNhanVien(workbook, report);
            // 6.Nhập khách hàng
            importSheetKhachHang(workbook, report);
            // 7.Nhập hóa đơn
            importSheetHoaDon(workbook, report);
            // 8.Nhập phiếu nhập hàng
            importSheetPNH(workbook, report);
            // 9.Nhập chi tiết hóa đơn
            importSheetCTHD(workbook, report);
            // 10.Nhập chi tiết phiếu nhập hàng
            importSheetCTPNH(workbook, report);
            // 11.Nhập chương trình khuyến mãi
            importSheetCTKM(workbook, report);
            // 12.Nhập hóa đơn khuyến mãi
            importSheetHDKM(workbook, report);
            // 13.Nhập sản phẩm khuyến mãi
            importSheetSPKM(workbook, report);

            return report.toString();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(parent, "Lỗi đọc file Excel: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    // --- CÁC HÀM HỖ TRỢ (PRIVATE HELPER METHODS) ---

    private void refreshAllData() {
        new SanPhamBUS().docDSSP();
        new KhachHangBUS().docDSKH();
        new LoaiSanPhamBUS().docDSLSP();
        new HangSanXuatBUS().docDSHSX();
        // Thêm các lệnh docDS... khác nếu cần
    }

    private CellStyle createHeaderStyle(XSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        return style;
    }

    private void exportSheetSanPham(XSSFWorkbook workbook, CellStyle style) {
        System.out.println("=== EXPORT DEBUG: SanPham ===");
        System.out.println("BUS.dssp size before export: " + (SanPhamBUS.dssp != null ? SanPhamBUS.dssp.size() : "null"));
        System.out.println("Đang xuất Sheet Sản Phẩm...");    
        Sheet sheet = workbook.createSheet("SanPham");
        String[] cols = {"Mã SP", "Tên Sản Phẩm", "Số Lượng", "Đơn Giá", "Đơn Vị Tính", "Mã Loại", "Mã Hãng"};
        Row header = sheet.createRow(0);
        for (int i = 0; i < cols.length; i++) {
            Cell cell = header.createCell(i);
            cell.setCellValue(cols[i]);
            cell.setCellStyle(style);
        }
        int rowNum = 1;
        for (SanPhamDTO sp : SanPhamBUS.dssp) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(sp.getMasanpham());
            row.createCell(1).setCellValue(sp.getTensanpham());
            row.createCell(2).setCellValue(sp.getSoluong());
            row.createCell(3).setCellValue(sp.getDongia());
            row.createCell(4).setCellValue(sp.getDonvitinh());
            row.createCell(5).setCellValue(sp.getMaLoai());
            row.createCell(6).setCellValue(sp.getMaHang());
        }
    }

    private void exportSheetKhachHang(XSSFWorkbook workbook, CellStyle style){
            CellStyle headerStyle = createHeaderStyle(workbook);
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
            if (KhachHangBUS.dskh != null) {
                int rowNum = 1;
                for (KhachHangDTO kh : KhachHangBUS.dskh) {
                    Row row = sheetKH.createRow(rowNum++);
                    row.createCell(0).setCellValue(kh.getMaKH());
                    row.createCell(1).setCellValue(kh.getHoKH());
                    row.createCell(2).setCellValue(kh.getTenKH());
                    row.createCell(3).setCellValue(kh.getDiaChi());
                    row.createCell(4).setCellValue(kh.getSdt());
                }                
            }
    }
    
    private void exportSheetHoaDon(XSSFWorkbook workbook, CellStyle style){
        CellStyle headerStyle = createHeaderStyle(workbook);
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
    }

    private void exportSheetHSX(XSSFWorkbook workbook, CellStyle style){
        CellStyle headerStyle = createHeaderStyle(workbook);
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
        if (HangSanXuatBUS.dshsx != null) {
            int rowNum = 1;
            for (HangSanXuatDTO hsx : HangSanXuatBUS.dshsx) {
                Row row = sheetHSX.createRow(rowNum++);
                row.createCell(0).setCellValue(hsx.getMaHang());
                row.createCell(1).setCellValue(hsx.getTenHang());
            }
        }
    }

    private void exportSheetLSP(XSSFWorkbook workbook, CellStyle style){
        CellStyle headerStyle = createHeaderStyle(workbook);
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
        if (LoaiSanPhamBUS.dslsp != null) {
            int rowNum = 1;
            for (LoaiSanPhamDTO lsp : LoaiSanPhamBUS.dslsp) {
                Row row = sheetLSP.createRow(rowNum++);
                row.createCell(0).setCellValue(lsp.getMaLoai());
                row.createCell(1).setCellValue(lsp.getTenLoai());
            }
        }
    }

    private void exportSheetNCC(XSSFWorkbook workbook, CellStyle style){
        CellStyle headerStyle = createHeaderStyle(workbook);
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
        if (NhaCungCapBUS.dsncc != null) {
            int rowNum = 1;
            for (NhaCungCapDTO ncc : NhaCungCapBUS.dsncc) {
                Row row = sheetNCC.createRow(rowNum++);
                row.createCell(0).setCellValue(ncc.getMaNCC());
                row.createCell(1).setCellValue(ncc.getTenNCC());
                row.createCell(2).setCellValue(ncc.getDiaChi());
                row.createCell(3).setCellValue(ncc.getSdt());
            }
        }
    }

    private void exportSheetNhanvVien(XSSFWorkbook workbook, CellStyle style){
        CellStyle headerStyle = createHeaderStyle(workbook);
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
            }
        }
    }

    private void exportSheetPhieuNhapHang(XSSFWorkbook workbook , CellStyle style){
        CellStyle headerStyle = createHeaderStyle(workbook);
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
            }
        }
    }

    private void exportSheetCTPNH(XSSFWorkbook workbook, CellStyle style){
        CellStyle headerStyle = createHeaderStyle(workbook);
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
        ChiTietPhieuNhapHangBUS ctpnBus= new ChiTietPhieuNhapHangBUS();
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
            }
        }
    }

    private void exportSheetCTHD(XSSFWorkbook workbook, CellStyle style){
        CellStyle headerStyle = createHeaderStyle(workbook);
        System.out.println("=== EXPORT DEBUG: ChiTietHoaDon ===");
        Sheet sheetCTHD = workbook.createSheet("ChiTietHoaDon");
        String[] columnsCTHD = {"MaHD", "MaSP", "SoLuong", "DonGia", "ThanhTien"};
        Row headerCTHD = sheetCTHD.createRow(0);
        for (int i = 0; i < columnsCTHD.length; i++) {
            Cell cell = headerCTHD.createCell(i);
            cell.setCellValue(columnsCTHD[i]);
            cell.setCellStyle(headerStyle);
        }
        ChiTietHoaDonBUS cthdBus = new ChiTietHoaDonBUS();
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
            }
        }
    }

    private void exportSheetCTKM(XSSFWorkbook workbook, CellStyle style){
        CellStyle headerStyle = createHeaderStyle(workbook);
        System.out.println("=== EXPORT DEBUG: ChuongTrinhKhuyenMai ===");
        System.out.println("ChuongTrinhKhuyenMaiBUS.dskm size: " + ( true ? "Assuming dskm loaded" : "null"));
        Sheet sheetKM = workbook.createSheet("ChuongTrinhKhuyenMai");
        String[] columnsKM = {"ID", "Ten", "GhiChu", "NgayBatDau", "NgayKetThuc", "TrangThai"};
        Row headerKM = sheetKM.createRow(0);
        for (int i = 0; i < columnsKM.length; i++) {
            Cell cell = headerKM.createCell(i);
            cell.setCellValue(columnsKM[i]);
            cell.setCellStyle(headerStyle);
        }
        if (ChuongTrinhKhuyenMaiBUS.dskm != null) { 
            int rowNum = 1;
            for (ChuongTrinhKhuyenMaiDTO km : ChuongTrinhKhuyenMaiBUS.dskm) {
                Row row = sheetKM.createRow(rowNum++);
                row.createCell(0).setCellValue(km.getId());
                row.createCell(1).setCellValue(km.getTen());
                row.createCell(2).setCellValue(km.getGhiChu());
                row.createCell(3).setCellValue(km.getNgayBatDau());
                row.createCell(4).setCellValue(km.getNgayKetThuc());
                row.createCell(5).setCellValue(km.isTrangThai());
            }
        }
    }

    private void exportSheetHDKM(XSSFWorkbook workbook, CellStyle style){
        CellStyle headerStyle = createHeaderStyle(workbook);
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
        if (ChuongTrinhKhuyenMaiHDBUS.dskmhd != null) {
            int rowNum = 1;
            for (ChuongTrinhKhuyenMaiHDDTO kmhd : ChuongTrinhKhuyenMaiHDBUS.dskmhd) {
                Row row = sheetKMHD.createRow(rowNum++);
                row.createCell(0).setCellValue(kmhd.getChuongTrinhKhuyenMaiId());
                row.createCell(1).setCellValue(kmhd.getSoTienHd());
                row.createCell(2).setCellValue(kmhd.getGiaTriGiam());
            }
        }
    }

    private void exportSheetSPKM(XSSFWorkbook workbook, CellStyle style){
        CellStyle headerStyle = createHeaderStyle(workbook);
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
        if (ChuongTrinhKhuyenMaiSpBUS.dskmsp != null) {
            int rowNum = 1;
            for (ChuongTrinhKhuyenMaiSpDTO kmsp : ChuongTrinhKhuyenMaiSpBUS.dskmsp) {
                Row row = sheetKMSP.createRow(rowNum++);
                row.createCell(0).setCellValue(kmsp.getChuongTrinhKhuyenMaiId());
                row.createCell(1).setCellValue(kmsp.getSanPhamId());
                row.createCell(2).setCellValue(kmsp.getGiaTriGiam());
            }
        }
    }


    private void importSheetSP(XSSFWorkbook workbook, StringBuilder report) {
        Sheet sheet = workbook.getSheet("SanPham");
        if (sheet == null) return;
        SanPhamBUS bus = new SanPhamBUS();
        int imported = 0, errors = 0;
        for (int r = 1; r <= sheet.getLastRowNum(); r++) {
            Row row = sheet.getRow(r);
            if (row == null) continue;
            try {
                SanPhamDTO sp = new SanPhamDTO();
                sp.setMasanpham(getSafeInt(row.getCell(0),r,0,"MaSP"));
                sp.setTensanpham(getSafeString(row.getCell(1),r,1,"TenSP"));
                sp.setSoluong(getSafeInt(row.getCell(2),r,2,"SoLuong"));
                sp.setDongia(getSafeLong(row.getCell(3),r,3,"DonGia"));
                sp.setDonvitinh(getSafeString(row.getCell(4),r,4,"DonViTinh"));
                sp.setMaLoai(getSafeInt(row.getCell(5),r,5,"MaLoai"));
                sp.setMaHang(getSafeInt(row.getCell(6),r,6,"MaHang"));
                if (sp.getMasanpham() == -1) continue;
                if (bus.importExcel(sp)) imported++; else errors++;
            } catch (Exception e) { errors++; }
        }
        report.append(String.format("- Sản Phẩm: Nhập %d, Lỗi %d\n", imported, errors));
    }

    private void importSheetLSP(XSSFWorkbook workbook, StringBuilder report) {
        Sheet sheet = workbook.getSheet("LoaiSanPham");
        if (sheet == null) return;
        LoaiSanPhamBUS bus = new LoaiSanPhamBUS();
        int imported = 0, errors = 0;
        for (int r = 1; r <= sheet.getLastRowNum(); r++) {
            Row row = sheet.getRow(r);
            if (row == null) continue;
            try {
                LoaiSanPhamDTO lsp = new LoaiSanPhamDTO();
                lsp.setMaLoai(getSafeInt(row.getCell(0), r, 0, "MaLoai"));
                lsp.setTenLoai(getSafeString(row.getCell(1), r, 1, "TenLoai"));
                if (lsp.getMaLoai() == -1) continue;
                if (bus.importExcel(lsp)) imported++; else errors++;
            } catch (Exception e) { errors++; }
        }
        report.append(String.format("- Loại Sản Phẩm: Nhập %d, Lỗi %d\n", imported, errors));
    }
    
    private void importSheetHSX(XSSFWorkbook workbook, StringBuilder report) {
        Sheet sheet = workbook.getSheet("HangSanXuat");
        if (sheet == null) return;
        HangSanXuatBUS bus = new HangSanXuatBUS();
        int imported = 0, errors = 0;
        for (int r = 1; r <= sheet.getLastRowNum(); r++) {
            Row row = sheet.getRow(r);
            if (row == null) continue;
            try {
                HangSanXuatDTO hsx = new HangSanXuatDTO();
                hsx.setMaHang(getSafeInt(row.getCell(0), r, 0, "MaHang"));
                hsx.setTenHang(getSafeString(row.getCell(1), r, 1, "TenHang"));
                if (hsx.getMaHang() == -1) continue;
                if (bus.importExcel(hsx)) imported++; else errors++;
            } catch (Exception e) { errors++; }
        }
        bus.docDSHSX(); 
        report.append(String.format("- Hãng Sản Xuất: Nhập %d, Lỗi %d\n", imported, errors));
    }
    
    private void importSheetNCC(XSSFWorkbook workbook, StringBuilder report) {
        Sheet sheet = workbook.getSheet("NhaCungCap");
        if (sheet == null) return;
        NhaCungCapBUS bus = new NhaCungCapBUS();
        int imported = 0, errors = 0;
        for (int r = 1; r <= sheet.getLastRowNum(); r++) {
            Row row = sheet.getRow(r);
            if (row == null) continue;
            try {
                NhaCungCapDTO ncc = new NhaCungCapDTO();
                ncc.setMaNCC(getSafeInt(row.getCell(0), r, 0, "MaNCC"));
                ncc.setTenNCC(getSafeString(row.getCell(1), r, 1, "TenNCC"));
                ncc.setDiaChi(getSafeString(row.getCell(2), r, 2, "DiaChi"));
                ncc.setSdt(getSafeString(row.getCell(3), r, 3, "SDT"));
                if (ncc.getMaNCC() == -1) continue;
                if (bus.importExcel(ncc)) imported++; else errors++;
            } catch (Exception e) { errors++; }
        }
        bus.docDSNCC();
        report.append(String.format("- Nhà Cung Cấp: Nhập %d, Lỗi %d\n", imported, errors));
    }
    
    private void importSheetNhanVien(XSSFWorkbook workbook, StringBuilder report) {
        Sheet sheet = workbook.getSheet("NhanVien");
        if (sheet == null) return;
        NhanVienBUS bus = new NhanVienBUS();
        int imported = 0, errors = 0;
        for (int r = 1; r <= sheet.getLastRowNum(); r++) {
            Row row = sheet.getRow(r);
            if (row == null) continue;
            try {
                NhanVienDTO nv = new NhanVienDTO();
                nv.setMaNV(getSafeInt(row.getCell(0), r, 0, "MaNV"));
                nv.setHoNV(getSafeString(row.getCell(1), r, 1, "Ho"));
                nv.setTenNV(getSafeString(row.getCell(2), r, 2, "Ten"));
                nv.setNgaySinh(toSqlDate(getSafeDate(row.getCell(3)))); 
                nv.setDiaChi(getSafeString(row.getCell(4), r, 4, "DiaChi"));
                nv.setSdt(getSafeString(row.getCell(5), r, 5, "Sdt"));
                nv.setLuong((int) getSafeLong(row.getCell(6), r, 6, "Luong"));
    
                if (nv.getMaNV() == -1) continue;
                if (bus.importExcel(nv)) imported++; else errors++;
            } catch (Exception e) { errors++; }
        }
        report.append(String.format("- Nhân Viên: Nhập %d, Lỗi %d\n", imported, errors));
    }
    
    private void importSheetKhachHang(XSSFWorkbook workbook, StringBuilder report) {
        Sheet sheet = workbook.getSheet("KhachHang");
        if (sheet == null) return;
        KhachHangBUS bus = new KhachHangBUS();
        int imported = 0, errors = 0;
        for (int r = 1; r <= sheet.getLastRowNum(); r++) {
            Row row = sheet.getRow(r);
            if (row == null) continue;
            try {
                KhachHangDTO kh = new KhachHangDTO();
                kh.setMaKH(getSafeInt(row.getCell(0), r, 0, "MaKH"));
                kh.setHoKH(getSafeString(row.getCell(1), r, 1, "HoKH"));
                kh.setTenKH(getSafeString(row.getCell(2), r, 2, "TenKH"));
                kh.setDiaChi(getSafeString(row.getCell(3), r, 3, "DiaChi"));
                kh.setSdt(getSafeString(row.getCell(4), r, 4, "SDT"));
                if (kh.getMaKH() == -1) continue;
                if (bus.importExcel(kh)) imported++; else errors++;
            } catch (Exception e) { errors++; }
        }
        report.append(String.format("- Khách Hàng: Nhập %d, Lỗi %d\n", imported, errors));
    }
    
    private void importSheetHoaDon(XSSFWorkbook workbook, StringBuilder report) {
        Sheet sheet = workbook.getSheet("HoaDon");
        if (sheet == null) return;
        HoaDonBUS bus = new HoaDonBUS();
        int imported = 0, errors = 0;
        for (int r = 1; r <= sheet.getLastRowNum(); r++) {
            Row row = sheet.getRow(r);
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
                if (bus.importExcel(hd)) imported++; else errors++;
            } catch (Exception e) { errors++; }
        }
        report.append(String.format("- Hóa Đơn: Nhập %d, Lỗi %d\n", imported, errors));
    }
    
    private void importSheetPNH(XSSFWorkbook workbook, StringBuilder report) {
        Sheet sheet = workbook.getSheet("PhieuNhapHang");
        if (sheet == null) return;
        PhieuNhapHangBUS bus = new PhieuNhapHangBUS();
        int imported = 0, errors = 0;
        for (int r = 1; r <= sheet.getLastRowNum(); r++) {
            Row row = sheet.getRow(r);
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
                if (bus.importExcel(pnh)) imported++; else errors++;
            } catch (Exception e) { errors++; }
        }
        report.append(String.format("- Phiếu Nhập: Nhập %d, Lỗi %d\n", imported, errors));
    }
    
    private void importSheetCTHD(XSSFWorkbook workbook, StringBuilder report) {
        Sheet sheet = workbook.getSheet("ChiTietHoaDon");
        if (sheet == null) return;
        ChiTietHoaDonBUS bus = new ChiTietHoaDonBUS();
        int imported = 0, errors = 0;
        for (int r = 1; r <= sheet.getLastRowNum(); r++) {
            Row row = sheet.getRow(r);
            if (row == null) continue;
            try {
                ChiTietHoaDonDTO cthd = new ChiTietHoaDonDTO();
                cthd.setMaHD(getSafeInt(row.getCell(0), r, 0, "MaHD"));
                cthd.setMaSP(getSafeInt(row.getCell(1), r, 1, "MaSP"));
                cthd.setSoLuong(getSafeInt(row.getCell(2), r, 2, "SoLuong"));
                cthd.setDonGia(getSafeLong(row.getCell(3), r, 3, "DonGia"));
                cthd.setThanhTien(getSafeLong(row.getCell(4), r, 4, "ThanhTien"));
                if (cthd.getMaHD() == -1 || cthd.getMaSP() == -1) continue;
                if (bus.importExcel(cthd)) imported++; else errors++;
            } catch (Exception e) { errors++; }
        }
        report.append(String.format("- CT Hóa Đơn: Nhập %d, Lỗi %d\n", imported, errors));
    }
    
    private void importSheetCTPNH(XSSFWorkbook workbook, StringBuilder report) {
        Sheet sheet = workbook.getSheet("ChiTietPhieuNhapHang");
        if (sheet == null) return;
        ChiTietPhieuNhapHangBUS bus = new ChiTietPhieuNhapHangBUS();
        int imported = 0, errors = 0;
        for (int r = 1; r <= sheet.getLastRowNum(); r++) {
            Row row = sheet.getRow(r);
            if (row == null) continue;
            try {
                ChiTietPhieuNhapHangDTO ctpnh = new ChiTietPhieuNhapHangDTO();
                ctpnh.setMaPNH(getSafeInt(row.getCell(0), r, 0, "MaPNH"));
                ctpnh.setMaSP(getSafeInt(row.getCell(1), r, 1, "MaSP"));
                ctpnh.setDonGia(getSafeLong(row.getCell(2), r, 2, "DonGia"));
                ctpnh.setSoLuong(getSafeInt(row.getCell(3), r, 3, "SoLuong"));
                ctpnh.setThanhTien(getSafeLong(row.getCell(4), r, 4, "ThanhTien"));
                if (ctpnh.getMaPNH() == -1 || ctpnh.getMaSP() == -1) continue;
                if (bus.importExcel(ctpnh)) imported++; else errors++;
            } catch (Exception e) { errors++; }
        }
        report.append(String.format("- CT Phiếu Nhập: Nhập %d, Lỗi %d\n", imported, errors));
    }
    
    private void importSheetCTKM(XSSFWorkbook workbook, StringBuilder report) {
        Sheet sheet = workbook.getSheet("ChuongTrinhKhuyenMai");
        if (sheet == null) return;
        ChuongTrinhKhuyenMaiBUS bus = new ChuongTrinhKhuyenMaiBUS();
        int imported = 0, errors = 0;
        
        for (int r = 1; r <= sheet.getLastRowNum(); r++) {
            Row row = sheet.getRow(r);
            if (row == null) continue;
            try {
                ChuongTrinhKhuyenMaiDTO ctkm = new ChuongTrinhKhuyenMaiDTO();
                ctkm.setId(getSafeInt(row.getCell(0), r, 0, "ID"));
                ctkm.setTen(getSafeString(row.getCell(1), r, 1, "Ten"));
                ctkm.setGhiChu(getSafeString(row.getCell(2), r, 2, "GhiChu"));
    
                java.util.Date utDateBD = getSafeDate(row.getCell(3));
                if (utDateBD != null) {
                    ctkm.setNgayBatDau(new java.sql.Date(utDateBD.getTime()));
                }
    
                java.util.Date utDateKT = getSafeDate(row.getCell(4));
                if (utDateKT != null) {
                    ctkm.setNgayKetThuc(new java.sql.Date(utDateKT.getTime()));
                }
    
                String statusStr = getSafeString(row.getCell(5), r, 5, "TrangThai");
                ctkm.setTrangThai(Boolean.parseBoolean(statusStr));
    
                if (ctkm.getId() == -1) continue;
                if (bus.importExcel(ctkm)) imported++; else errors++;
            } catch (Exception e) { 
                System.err.println("Lỗi tại dòng " + r + ": " + e.getMessage());
                errors++; 
            }
        }
        bus.docDSKM();
        report.append(String.format("- Chương Trình KM: Nhập %d, Lỗi %d\n", imported, errors));
    }

    private void importSheetHDKM(XSSFWorkbook workbook, StringBuilder report) {
        Sheet sheet = workbook.getSheet("ChuongTrinhKhuyenMaiHD"); 
        if (sheet == null) return;
        ChuongTrinhKhuyenMaiHDBUS bus = new ChuongTrinhKhuyenMaiHDBUS();
        int imported = 0, errors = 0;
        for (int r = 1; r <= sheet.getLastRowNum(); r++) {
            Row row = sheet.getRow(r);
            if (row == null) continue;
            try {
                ChuongTrinhKhuyenMaiHDDTO dto = new ChuongTrinhKhuyenMaiHDDTO();
                dto.setChuongTrinhKhuyenMaiId(getSafeInt(row.getCell(0), r, 0, "CTKM_ID"));
                dto.setSoTienHd(getSafeInt(row.getCell(1), r, 1, "SoTienHD"));
                dto.setGiaTriGiam(getSafeInt(row.getCell(2), r, 2, "GiaTriGiam"));
                
                if (dto.getChuongTrinhKhuyenMaiId() == -1) continue;
                if (bus.importExcel(dto)) imported++; else errors++;
            } catch (Exception e) { errors++; }
        }
        report.append(String.format("- Hóa Đơn KM: Nhập %d, Lỗi %d\n", imported, errors));
    }

    private void importSheetSPKM(XSSFWorkbook workbook, StringBuilder report) {
        Sheet sheet = workbook.getSheet("ChuongTrinhKhuyenMaiSp"); 
        if (sheet == null) return;
        ChuongTrinhKhuyenMaiSpBUS bus = new ChuongTrinhKhuyenMaiSpBUS();
        int imported = 0, errors = 0;
        for (int r = 1; r <= sheet.getLastRowNum(); r++) {
            Row row = sheet.getRow(r);
            if (row == null) continue;
            try {
                ChuongTrinhKhuyenMaiSpDTO dto = new ChuongTrinhKhuyenMaiSpDTO();
                dto.setChuongTrinhKhuyenMaiId(getSafeInt(row.getCell(0), r, 0, "CTKM_ID"));
                dto.setSanPhamId(getSafeInt(row.getCell(1), r, 1, "SanPhamID"));
                dto.setGiaTriGiam(getSafeInt(row.getCell(2), r, 2, "GiaTriGiam"));
                
                if (dto.getChuongTrinhKhuyenMaiId() == -1) continue;
                if (bus.importExcel(dto)) imported++; else errors++;
            } catch (Exception e) { errors++; }
        }
        report.append(String.format("- Sản Phẩm KM: Nhập %d, Lỗi %d\n", imported, errors));
    }
    
    

}