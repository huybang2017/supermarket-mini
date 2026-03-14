package SieuThiMiniBUS;

import SieuThiMiniDAO.ThongKeBaoCaoDAO;
import java.util.List;

public class ThongKeBaoCaoBUS {
    private ThongKeBaoCaoDAO data = new ThongKeBaoCaoDAO();

    public ThongKeBaoCaoBUS() {}

    // TAB 1
    public long tongDoanhThu() { return data.tongDoanhThu(); }
    public long tongPhiNhap() { return data.tongPhiNhap(); }
    public long loiNhuanUocTinh() { return tongDoanhThu() - tongPhiNhap(); }
    public List<Object[]> getChiTietDoanhThu(String loaiKy, int nam, int thang) {
        if ("Tháng".equals(loaiKy)) {
            // Trả về dữ liệu ngày trong tháng
            return data.getChiTietDoanhThuTheoThang(nam, thang);
        } else {
            // Trả về dữ liệu các tháng trong năm (Dùng chung cho lọc Năm và Quý)
            return data.getChiTietDoanhThuTheoNam(nam);
        }
    }
    // TAB 2
    public int tongSoKhachHang() { return data.tongSoKhachHang(); }
    public int tongKhachHangDaMua() { return data.tongKhachHangDaMua(); }
    public List<Object[]> getTopKhachHangTheoThoiGian(int nam, int thang) {
        return data.getTopKhachHangTheoThoiGian(nam, thang);
    }

    // TAB 3
    public int tongNhanVien() { return data.tongSoNhanVien(); }
    public String nhanVienXuatSac() { return data.nhanVienXuatSac(); }
    public long doanhThuTrungBinhNhanVien() {
        int soNV = tongNhanVien(); 
        return soNV > 0 ? tongDoanhThu() / soNV : 0; 
    }
    public List<Object[]> getTopNhanVienTheoThoiGian(int nam, int thang) {
        return data.getTopNhanVienTheoThoiGian(nam, thang);
    }

    // TAB 4
    public int tongSanPham() { return data.tongSanPhamDangKinhDoanh(); }
    public String sanPhamBanChay() { return data.sanPhamBanChayNhat(); }
    public int spSapHetHang() { return data.soSanPhamSapHetHang(); }
    public List<Object[]> getTopSanPhamTheoThoiGian(int nam, int thang) {
        return data.getTopSanPhamTheoThoiGian(nam, thang);
    }
}