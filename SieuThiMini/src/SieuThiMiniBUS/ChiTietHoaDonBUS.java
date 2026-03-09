package SieuThiMiniBUS;

import DTO.ChiTietHoaDonDTO;
import SieuThiMiniDAO.ChiTietHoaDonDAO;
import java.util.List;

public class ChiTietHoaDonBUS {

    private ChiTietHoaDonDAO chiTietDAO;

    public ChiTietHoaDonBUS() {
        chiTietDAO = new ChiTietHoaDonDAO();
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

        // Tính thành tiền
        ct.setThanhTien(ct.getSoLuong() * ct.getDonGia());

        return chiTietDAO.addChiTietHoaDon(ct);
    }

    /**
     * Cập nhật chi tiết hóa đơn
     */
    public boolean updateChiTietHoaDon(ChiTietHoaDonDTO ct) {

        if (ct == null) {
            return false;
        }

        // Tính lại thành tiền
        ct.setThanhTien(ct.getSoLuong() * ct.getDonGia());

        return chiTietDAO.updateChiTietHoaDon(ct);
    }

    /**
     * Xóa chi tiết hóa đơn
     */
    public boolean deleteChiTietHoaDon(int maHD, int maSP) {
        return chiTietDAO.deleteChiTietHoaDon(maHD, maSP);
    }
}