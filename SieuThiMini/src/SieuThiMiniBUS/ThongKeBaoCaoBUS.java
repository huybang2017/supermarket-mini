package SieuThiMiniBUS;

import SieuThiMiniDAO.ThongKeBaoCaoDAO;
import java.util.List;

public class ThongKeBaoCaoBUS {
    private ThongKeBaoCaoDAO data = new ThongKeBaoCaoDAO();

    public ThongKeBaoCaoBUS() {}

    // --- Tab 1: Doanh Thu ---
    public long tongDoanhThu() {
        return data.tongDoanhThu();
    }

    public long tongPhiNhap() {
        return data.tongPhiNhap();
    }

    public long loiNhuanUocTinh() {
        return tongDoanhThu() - tongPhiNhap();
    }
    public java.util.List<Object[]> getChiTietThang(int nam) {
        return data.getChiTietTheoThang(nam);
    }
    // --- Tab 2: Khách hàng ---
    public int tongSoKhachHang() {
        return data.tongSoKhachHang();
    }

    public int tongKhachHangDaMua() {
        return data.tongKhachHangDaMua();
    }

    public List<Object[]> getTopKhachHang() {
        return data.getTopKhachHang();
    }

    // --- Tab 3: Nhân viên ---
    public String nhanVienXuatSac() {
        return data.nhanVienXuatSac();
    }

    public int tongNhanVien() {
        return data.tongSoNhanVien();
    }

    public long doanhThuTrungBinhNhanVien() {
        long tong = tongDoanhThu();
        int soNV = tongNhanVien(); 
        return soNV > 0 ? tong / soNV : 0; 
    }

    // Hàm bị thiếu gây lỗi ở GUI
    public List<Object[]> getTopNhanVien() {
        return data.getTopNhanVien();
    }

    // --- Tab 4: Sản phẩm ---
    public int tongSanPham() {
        return data.tongSanPhamDangKinhDoanh();
    }

    public String sanPhamBanChay() {
        return data.sanPhamBanChayNhat();
    }

    public int spSapHetHang() {
        return data.soSanPhamSapHetHang();
    }

    public List<Object[]> getTopSanPham() {
        return data.getTopSanPham();
    }
}