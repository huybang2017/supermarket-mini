package SieuThiMiniBUS;

import DTO.HoaDonDTO;
import SieuThiMiniDAO.HoaDonDAO;
import java.util.List;

public class HoaDonBUS {

    private HoaDonDAO hoaDonDAO;

    public HoaDonBUS() {
        hoaDonDAO = new HoaDonDAO();
    }

    // Lấy danh sách hóa đơn
    public List<HoaDonDTO> getListHoaDon() {
        return hoaDonDAO.getListHoaDon();
    }

    // Thêm hóa đơn
    public boolean addHoaDon(HoaDonDTO hd) {
        if (hd == null) {
            return false;
        }
        return hoaDonDAO.addHoaDon(hd);
    }

    // Sửa hóa đơn
    public boolean updateHoaDon(HoaDonDTO hd) {
        if (hd == null) {
            return false;
        }
        return hoaDonDAO.updateHoaDon(hd);
    }

    // Xóa hóa đơn
    public boolean deleteHoaDon(int maHD) {
        return hoaDonDAO.deleteHoaDon(maHD);
    }

    // Tìm hóa đơn theo mã
    public HoaDonDTO getHoaDonById(int maHD) {
        return hoaDonDAO.getHoaDonById(maHD);
    }
}
