package SieuThiMiniBUS;

import DTO.NhanVienDTO;
import DTO.SanPhamDTO;
import SieuThiMiniDAO.NhanVienDAO;
import SieuThiMiniDAO.SanPhamDAO;
import java.util.ArrayList;

public class SanPhamBUS {
    public static ArrayList<SanPhamDTO> dssp;

    public SanPhamBUS(){}

    public void docDSSP() {
        SanPhamDAO data = new SanPhamDAO();
        dssp = data.docDSSP(); // Lấy trực tiếp từ DB để tránh lệch pha
    }

    public void them(SanPhamDTO sp) {
        SanPhamDAO data = new SanPhamDAO();
        if (data.themSP(sp)) {
            docDSSP(); // Thêm thành công thì tải lại bảng
        }
    }

    public void xoa(int ma) {
        SanPhamDAO data = new SanPhamDAO();
        if (data.xoaSP(ma)) {
            docDSSP(); // Xoá thành công thì tải lại bảng
        }
    }

    public void sua(SanPhamDTO sp) {
        SanPhamDAO data = new SanPhamDAO();
        if (data.suaSP(sp)) {
            docDSSP(); // Sửa thành công thì tải lại bảng
        }
    }

    public void capNhatSoLuong(int masp, int soLuongMoi) {
        SanPhamDAO data = new SanPhamDAO();
        if (data.capNhatSoLuong(masp, soLuongMoi)) {
            docDSSP(); // Đồng bộ ngay lập tức lên RAM
        }
    }

    public boolean importExcel(SanPhamDTO sp) {
        SanPhamDAO data = new SanPhamDAO();
        return data.importExcel(sp);
    }

    public ArrayList<SanPhamDTO> timSanPham(String keyword) {
        SanPhamDAO data = new SanPhamDAO();
        return data.timSanPham(keyword);
    }
    public ArrayList<SanPhamDTO> timSanPhamTheoGia(long giaTu, long giaDen) {
        SanPhamDAO data = new SanPhamDAO();
        return data.timSanPhamTheoGia(giaTu, giaDen);
    }

}