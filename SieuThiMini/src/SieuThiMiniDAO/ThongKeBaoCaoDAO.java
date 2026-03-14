package SieuThiMiniDAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ThongKeBaoCaoDAO {
    MyConnection data = new MyConnection();

    // =======================================================
    // TAB 1: DOANH THU & CHI PHÍ
    // =======================================================
    public long tongDoanhThu() {
        long tongdoanhthu = 0;
        Connection cnn = data.getConnection();
        try {
            String qry = "SELECT SUM(tongTien) AS total FROM hoadon";
            Statement st = cnn.createStatement();
            ResultSet rs = st.executeQuery(qry);
            if (rs.next()) tongdoanhthu = rs.getLong("total");
        } catch (SQLException e) { e.printStackTrace(); }
        finally { data.closeConnection(); }
        return tongdoanhthu;
    }

    public long tongPhiNhap() {
        long tongphinhap = 0;
        Connection cnn = data.getConnection();
        try {
            String qry = "SELECT SUM(tongTien) AS total FROM phieunhaphang";
            Statement st = cnn.createStatement();
            ResultSet rs = st.executeQuery(qry);
            if (rs.next()) tongphinhap = rs.getLong("total");
        } catch (SQLException e) { e.printStackTrace(); }
        finally { data.closeConnection(); }
        return tongphinhap;
    }

// 1. Thống kê theo 12 THÁNG trong NĂM
public java.util.List<Object[]> getChiTietDoanhThuTheoNam(int nam) {
    java.util.List<Object[]> list = new java.util.ArrayList<>();
    Connection cnn = data.getConnection();
    try {
        String qry = "SELECT sub.ky, SUM(sub.dt) as doanhThu, SUM(sub.cp) as chiPhi FROM (" +
                     "  SELECT MONTH(ngayLapHD) as ky, SUM(tongTien) as dt, 0 as cp FROM hoadon WHERE YEAR(ngayLapHD) = ? GROUP BY MONTH(ngayLapHD) " +
                     "  UNION ALL " +
                     "  SELECT MONTH(ngayNhap) as ky, 0 as dt, SUM(tongTien) as cp FROM phieunhaphang WHERE YEAR(ngayNhap) = ? GROUP BY MONTH(ngayNhap)" +
                     ") sub GROUP BY sub.ky ORDER BY sub.ky";
        java.sql.PreparedStatement ps = cnn.prepareStatement(qry);
        ps.setInt(1, nam); ps.setInt(2, nam);
        ResultSet rs = ps.executeQuery();
        
        long prevDt = 0;
        while (rs.next()) {
            int ky = rs.getInt("ky");
            long dt = rs.getLong("doanhThu");
            long cp = rs.getLong("chiPhi");
            
            // Thuật toán tính phần trăm tăng trưởng so với kỳ trước
            String tangTruong = "0%";
            if (prevDt > 0) {
                double rate = (double)(dt - prevDt) / prevDt * 100;
                tangTruong = (rate > 0 ? "+" : "") + String.format("%.1f%%", rate);
            } else if (dt > 0 && prevDt == 0) {
                tangTruong = "+100%";
            }
            prevDt = dt;
            
            list.add(new Object[]{"Tháng " + ky, dt, cp, dt - cp, tangTruong});
        }
    } catch (SQLException e) { e.printStackTrace(); }
    finally { data.closeConnection(); }
    return list;
}

// 2. Thống kê theo TỪNG NGÀY trong THÁNG
public java.util.List<Object[]> getChiTietDoanhThuTheoThang(int nam, int thang) {
    java.util.List<Object[]> list = new java.util.ArrayList<>();
    Connection cnn = data.getConnection();
    try {
        String qry = "SELECT sub.ky, SUM(sub.dt) as doanhThu, SUM(sub.cp) as chiPhi FROM (" +
                     "  SELECT DAY(ngayLapHD) as ky, SUM(tongTien) as dt, 0 as cp FROM hoadon WHERE YEAR(ngayLapHD) = ? AND MONTH(ngayLapHD) = ? GROUP BY DAY(ngayLapHD) " +
                     "  UNION ALL " +
                     "  SELECT DAY(ngayNhap) as ky, 0 as dt, SUM(tongTien) as cp FROM phieunhaphang WHERE YEAR(ngayNhap) = ? AND MONTH(ngayNhap) = ? GROUP BY DAY(ngayNhap)" +
                     ") sub GROUP BY sub.ky ORDER BY sub.ky";
        java.sql.PreparedStatement ps = cnn.prepareStatement(qry);
        ps.setInt(1, nam); ps.setInt(2, thang);
        ps.setInt(3, nam); ps.setInt(4, thang);
        ResultSet rs = ps.executeQuery();
        
        long prevDt = 0;
        while (rs.next()) {
            int ky = rs.getInt("ky");
            long dt = rs.getLong("doanhThu");
            long cp = rs.getLong("chiPhi");
            
            String tangTruong = "0%";
            if (prevDt > 0) {
                double rate = (double)(dt - prevDt) / prevDt * 100;
                tangTruong = (rate > 0 ? "+" : "") + String.format("%.1f%%", rate);
            } else if (dt > 0 && prevDt == 0) {
                tangTruong = "+100%";
            }
            prevDt = dt;
            
            list.add(new Object[]{"Ngày " + ky, dt, cp, dt - cp, tangTruong});
        }
    } catch (SQLException e) { e.printStackTrace(); }
    finally { data.closeConnection(); }
    return list;
}
    // =======================================================
    // TAB 2: KHÁCH HÀNG
    // =======================================================
    public int tongSoKhachHang() {
        int count = 0;
        Connection cnn = data.getConnection();
        try {
            String qry = "SELECT COUNT(*) AS total FROM khachhang";
            Statement st = cnn.createStatement();
            ResultSet rs = st.executeQuery(qry);
            if (rs.next()) count = rs.getInt("total");
        } catch (SQLException e) { e.printStackTrace(); }
        finally { data.closeConnection(); }
        return count;
    }

    public int tongKhachHangDaMua() {
        int count = 0;
        Connection cnn = data.getConnection();
        try {
            String qry = "SELECT COUNT(DISTINCT khachHangId) AS total FROM hoadon WHERE khachHangId IS NOT NULL";
            Statement st = cnn.createStatement();
            ResultSet rs = st.executeQuery(qry);
            if (rs.next()) count = rs.getInt("total");
        } catch (SQLException e) { e.printStackTrace(); }
        finally { data.closeConnection(); }
        return count;
    }

    public java.util.List<Object[]> getTopKhachHangTheoThoiGian(int nam, int thang) {
        java.util.List<Object[]> list = new java.util.ArrayList<>();
        Connection cnn = data.getConnection();
        try {
            String qry = "SELECT kh.id, kh.ho, kh.ten, COUNT(hd.id) as soDon, SUM(hd.tongTien) as chiTieu " +
                         "FROM khachhang kh JOIN hoadon hd ON kh.id = hd.khachHangId WHERE 1=1 ";
            
            if (nam > 0) qry += "AND YEAR(hd.ngayLapHD) = ? ";
            if (thang > 0) qry += "AND MONTH(hd.ngayLapHD) = ? ";
            qry += "GROUP BY kh.id ORDER BY chiTieu DESC";

            java.sql.PreparedStatement ps = cnn.prepareStatement(qry);
            int pIndex = 1;
            if (nam > 0) ps.setInt(pIndex++, nam);
            if (thang > 0) ps.setInt(pIndex++, thang);
            
            ResultSet rs = ps.executeQuery();
            int rank = 1;
            while (rs.next()) {
                long chiTieu = rs.getLong("chiTieu");
                int soDon = rs.getInt("soDon");
                list.add(new Object[]{ "#" + rank++, "KH" + String.format("%03d", rs.getInt("id")), 
                                       rs.getString("ho") + " " + rs.getString("ten"), soDon, chiTieu, (soDon > 0 ? chiTieu / soDon : 0) });
            }
        } catch (SQLException e) { e.printStackTrace(); }
        finally { data.closeConnection(); }
        return list;
    }

    // =======================================================
    // TAB 3: NHÂN VIÊN
    // =======================================================
    public int tongSoNhanVien() {
        int count = 0;
        Connection cnn = data.getConnection();
        try {
            String qry = "SELECT COUNT(*) AS total FROM nhanvien";
            Statement st = cnn.createStatement();
            ResultSet rs = st.executeQuery(qry);
            if (rs.next()) count = rs.getInt("total");
        } catch (SQLException e) { e.printStackTrace(); }
        finally { data.closeConnection(); }
        return count;
    }

    public String nhanVienXuatSac() {
        String tenNV = "Chưa có";
        Connection cnn = data.getConnection();
        try {
            String qry = "SELECT nv.ho, nv.ten FROM hoadon hd JOIN nhanvien nv ON hd.nhanVienId = nv.id " +
                         "GROUP BY hd.nhanVienId ORDER BY SUM(hd.tongTien) DESC LIMIT 1";
            Statement st = cnn.createStatement();
            ResultSet rs = st.executeQuery(qry);
            if (rs.next()) tenNV = rs.getString("ho") + " " + rs.getString("ten");
        } catch (SQLException e) { e.printStackTrace(); }
        finally { data.closeConnection(); }
        return tenNV;
    }

public java.util.List<Object[]> getTopNhanVienTheoThoiGian(int nam, int thang) {
        java.util.List<Object[]> list = new java.util.ArrayList<>();
        Connection cnn = data.getConnection();
        try {
            String qry = "SELECT nv.id, nv.ho, nv.ten, COUNT(hd.id) as soDon, SUM(hd.tongTien) as doanhThu " +
                         "FROM nhanvien nv JOIN hoadon hd ON nv.id = hd.nhanVienId WHERE 1=1 ";
            
            if (nam > 0) qry += "AND YEAR(hd.ngayLapHD) = ? ";
            if (thang > 0) qry += "AND MONTH(hd.ngayLapHD) = ? ";
            qry += "GROUP BY nv.id ORDER BY doanhThu DESC";

            java.sql.PreparedStatement ps = cnn.prepareStatement(qry);
            int pIndex = 1;
            if (nam > 0) ps.setInt(pIndex++, nam);
            if (thang > 0) ps.setInt(pIndex++, thang);
            
            ResultSet rs = ps.executeQuery();
            int rank = 1;
            while (rs.next()) {
                list.add(new Object[]{ "#" + rank++, "NV" + String.format("%03d", rs.getInt("id")), 
                                       rs.getString("ho") + " " + rs.getString("ten"), "Nhân Viên", rs.getInt("soDon"), rs.getLong("doanhThu") });
            }
        } catch (SQLException e) { e.printStackTrace(); }
        finally { data.closeConnection(); }
        return list;
    }

    // =======================================================
    // TAB 4: SẢN PHẨM
    // =======================================================
    public int tongSanPhamDangKinhDoanh() {
        int count = 0;
        Connection cnn = data.getConnection();
        try {
            String qry = "SELECT COUNT(*) AS total FROM sanpham";
            Statement st = cnn.createStatement();
            ResultSet rs = st.executeQuery(qry);
            if (rs.next()) count = rs.getInt("total");
        } catch (SQLException e) { e.printStackTrace(); }
        finally { data.closeConnection(); }
        return count;
    }

    public String sanPhamBanChayNhat() {
        String tenSP = "Chưa có";
        Connection cnn = data.getConnection();
        try {
            String qry = "SELECT sp.ten FROM chitiethoadon cthd JOIN sanpham sp ON cthd.sanPhamId = sp.id " +
                         "GROUP BY cthd.sanPhamId ORDER BY SUM(cthd.soLuong) DESC LIMIT 1";
            Statement st = cnn.createStatement();
            ResultSet rs = st.executeQuery(qry);
            if (rs.next()) tenSP = rs.getString("ten");
        } catch (SQLException e) { e.printStackTrace(); }
        finally { data.closeConnection(); }
        return tenSP;
    }

    public int soSanPhamSapHetHang() {
        int count = 0;
        Connection cnn = data.getConnection();
        try {
            String qry = "SELECT COUNT(*) AS total FROM sanpham WHERE soLuong < 15";
            Statement st = cnn.createStatement();
            ResultSet rs = st.executeQuery(qry);
            if (rs.next()) count = rs.getInt("total");
        } catch (SQLException e) { e.printStackTrace(); }
        finally { data.closeConnection(); }
        return count;
    }

    public java.util.List<Object[]> getTopSanPhamTheoThoiGian(int nam, int thang) {
        java.util.List<Object[]> list = new java.util.ArrayList<>();
        Connection cnn = data.getConnection();
        try {
            String qry = "SELECT sp.id, sp.ten, lsp.name as danhMuc, SUM(cthd.soLuong) as slBan, sp.donGia, SUM(cthd.thanhTien) as doanhThu " +
                         "FROM sanpham sp JOIN chitiethoadon cthd ON sp.id = cthd.sanPhamId " +
                         "JOIN hoadon hd ON cthd.hoaDonId = hd.id LEFT JOIN loaisanpham lsp ON sp.loaiSanPhamId = lsp.id WHERE 1=1 ";
            
            if (nam > 0) qry += "AND YEAR(hd.ngayLapHD) = ? ";
            if (thang > 0) qry += "AND MONTH(hd.ngayLapHD) = ? ";
            qry += "GROUP BY sp.id ORDER BY doanhThu DESC";

            java.sql.PreparedStatement ps = cnn.prepareStatement(qry);
            int pIndex = 1;
            if (nam > 0) ps.setInt(pIndex++, nam);
            if (thang > 0) ps.setInt(pIndex++, thang);
            
            ResultSet rs = ps.executeQuery();
            int rank = 1;
            while (rs.next()) {
                String danhMuc = rs.getString("danhMuc");
                list.add(new Object[]{ "#" + rank++, "SP" + String.format("%03d", rs.getInt("id")), rs.getString("ten"),
                                       danhMuc != null ? danhMuc : "Khác", rs.getInt("slBan"), rs.getLong("donGia"), rs.getLong("doanhThu") });
            }
        } catch (SQLException e) { e.printStackTrace(); }
        finally { data.closeConnection(); }
        return list;
    }
}