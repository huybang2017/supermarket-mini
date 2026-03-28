package SieuThiMiniBUS;

import DTO.ChiTietHoaDonDTO;
import SieuThiMiniDAO.ChiTietHoaDonDAO;
import java.util.ArrayList;
import java.util.List;

public class ChiTietHoaDonBUS {

    // 1. KHAI BÁO BIẾN TĨNH GIỐNG CÁC BUS KHÁC
    public static ArrayList<ChiTietHoaDonDTO> dscthd = null;
    
    private ChiTietHoaDonDAO chiTietDAO;

    public ChiTietHoaDonBUS() {
        chiTietDAO = new ChiTietHoaDonDAO();
        if (dscthd == null) {
            dscthd = new ArrayList<>();
        }
    }

    // 2. THÊM HÀM ĐỌC DỮ LIỆU TỪ DB LÊN RAM
    public void docDSCTHD() {
        dscthd = (ArrayList<ChiTietHoaDonDTO>) chiTietDAO.getAllChiTietHoaDon();
    }

    /**
     * Lấy toàn bộ chi tiết hóa đơn
     */
    public List<ChiTietHoaDonDTO> getAllChiTietHoaDon() {
        return chiTietDAO.getAllChiTietHoaDon();
    }

    /**
     * Lấy danh sách chi tiết theo mã hóa đơn
     */
    public List<ChiTietHoaDonDTO> getListByMaHD(int maHD) {
        return chiTietDAO.getByMaHD(maHD);
    }

    /**
     * Thêm chi tiết hóa đơn
     */
    public boolean addChiTietHoaDon(ChiTietHoaDonDTO ct) {
        if (ct == null) {
            return false;
        }

        List<ChiTietHoaDonDTO> dsHienTai = chiTietDAO.getByMaHD(ct.getMaHD());
        for (ChiTietHoaDonDTO item : dsHienTai) {
            if (item.getMaSP() == ct.getMaSP()) {
                return false; 
            }
        }

        ct.setThanhTien(ct.getSoLuong() * ct.getDonGia());
        boolean success = chiTietDAO.addChiTietHoaDon(ct);
        
        if (success && dscthd != null) {
            dscthd.add(ct);
        }
        return success;
    }

    /**
     * Cập nhật chi tiết hóa đơn
     */
    public boolean updateChiTietHoaDon(ChiTietHoaDonDTO ct) {
        if (ct == null) {
            return false;
        }

        ct.setThanhTien(ct.getSoLuong() * ct.getDonGia());
        boolean success = chiTietDAO.updateChiTietHoaDon(ct);
        
        if (success && dscthd != null) {
            for (int i = 0; i < dscthd.size(); i++) {
                if (dscthd.get(i).getMaHD() == ct.getMaHD() && dscthd.get(i).getMaSP() == ct.getMaSP()) {
                    dscthd.set(i, ct);
                    break;
                }
            }
        }
        return success;
    }

    /**
     * Xóa chi tiết hóa đơn
     */
    public boolean deleteChiTietHoaDon(int maHD, int maSP) {
        boolean success = chiTietDAO.deleteChiTietHoaDon(maHD, maSP);
        if (success && dscthd != null) {
            dscthd.removeIf(item -> item.getMaHD() == maHD && item.getMaSP() == maSP);
        }
        return success;
    }

    public boolean importExcel(ChiTietHoaDonDTO ct) {
        return chiTietDAO.importExcel(ct);
    }
}