package SieuThiMiniDAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ThongKeBaoCaoDAO {
    MyConnection data = new MyConnection();

    // --- TAB 1: DOANH THU & CHI PHÍ (Đã có) ---
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
// Lấy dữ liệu chi tiết doanh thu và chi phí theo tháng của một năm cụ thể
public java.util.List<Object[]> getChiTietTheoThang(int nam) {
    java.util.List<Object[]> list = new java.util.ArrayList<>();
    Connection cnn = data.getConnection();
    try {
        // SQL lấy doanh thu và chi phí group theo tháng
        String qry = "SELECT sub.thang, MAX(sub.dt) as doanhThu, MAX(sub.cp) as chiPhi FROM (" +
                     "  SELECT MONTH(ngayLapHD) as thang, SUM(tongTien) as dt, 0 as cp FROM hoadon WHERE YEAR(ngayLapHD) = ? GROUP BY MONTH(ngayLapHD) " +
                     "  UNION " +
                     "  SELECT MONTH(ngayNhap) as thang, 0 as dt, SUM(tongTien) as cp FROM phieunhaphang WHERE YEAR(ngayNhap) = ? GROUP BY MONTH(ngayNhap)" +
                     ") sub GROUP BY sub.thang ORDER BY sub.thang";
        
        java.sql.PreparedStatement ps = cnn.prepareStatement(qry);
        ps.setInt(1, nam);
        ps.setInt(2, nam);
        ResultSet rs = ps.executeQuery();
        
        while (rs.next()) {
            int thang = rs.getInt("thang");
            long dt = rs.getLong("doanhThu");
            long cp = rs.getLong("chiPhi");
            long ln = dt - cp;
            list.add(new Object[]{
                "Tháng " + thang + "/" + nam,
                dt, cp, ln, "0%" // Tạm để 0% tăng trưởng
            });
        }
    } catch (SQLException e) { e.printStackTrace(); }
    finally { data.closeConnection(); }
    return list;
}
    // --- TAB 2: THEO KHÁCH HÀNG ---
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

    // --- Lấy số lượng khách hàng đã từng mua hàng (có hóa đơn) ---
    public int tongKhachHangDaMua() {
        int count = 0;
        Connection cnn = data.getConnection();
        try {
            // Sửa lại cho đúng tên cột khachHangId
            String qry = "SELECT COUNT(DISTINCT khachHangId) AS total FROM hoadon WHERE khachHangId IS NOT NULL";
            Statement st = cnn.createStatement();
            ResultSet rs = st.executeQuery(qry);
            if (rs.next()) count = rs.getInt("total");
        } catch (SQLException e) { e.printStackTrace(); }
        finally { data.closeConnection(); }
        return count;
    }

    // --- Lấy danh sách Top Khách Hàng đổ vào bảng ---
    public java.util.List<Object[]> getTopKhachHang() {
        java.util.List<Object[]> list = new java.util.ArrayList<>();
        Connection cnn = data.getConnection();
        try {
            String qry = "SELECT kh.id, kh.ho, kh.ten, COUNT(hd.id) as soDon, SUM(hd.tongTien) as chiTieu " +
                         "FROM khachhang kh JOIN hoadon hd ON kh.id = hd.khachHangId " +
                         "GROUP BY kh.id ORDER BY chiTieu DESC";
            Statement st = cnn.createStatement();
            ResultSet rs = st.executeQuery(qry);
            int rank = 1;
            while (rs.next()) {
                String tenKH = rs.getString("ho") + " " + rs.getString("ten");
                long chiTieu = rs.getLong("chiTieu");
                int soDon = rs.getInt("soDon");
                long trungBinh = soDon > 0 ? chiTieu / soDon : 0;
                
                list.add(new Object[]{
                    "#" + rank++,
                    "KH" + String.format("%03d", rs.getInt("id")),
                    tenKH,
                    soDon,
                    chiTieu,
                    trungBinh
                });
            }
        } catch (SQLException e) { e.printStackTrace(); }
        finally { data.closeConnection(); }
        return list;
    }

    // --- TAB 3: THEO NHÂN VIÊN ---
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

    // --- Cập nhật lại hàm lấy NV Xuất Sắc ---
    public String nhanVienXuatSac() {
        String tenNV = "Chưa có";
        Connection cnn = data.getConnection();
        try {
            // Lấy họ và tên nhân viên có tổng doanh thu (tongTien) cao nhất
            String qry = "SELECT nv.ho, nv.ten FROM hoadon hd JOIN nhanvien nv ON hd.nhanVienId = nv.id " +
                         "GROUP BY hd.nhanVienId ORDER BY SUM(hd.tongTien) DESC LIMIT 1";
            Statement st = cnn.createStatement();
            ResultSet rs = st.executeQuery(qry);
            if (rs.next()) {
                tenNV = rs.getString("ho") + " " + rs.getString("ten");
            }
        } catch (SQLException e) { e.printStackTrace(); }
        finally { data.closeConnection(); }
        return tenNV;
    }

    // --- TAB 4: THEO SẢN PHẨM ---
// --- Lấy tổng số sản phẩm có trong CSDL ---
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

// --- Lấy tên sản phẩm bán được nhiều số lượng nhất ---
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

// --- Đếm số sản phẩm sắp hết hàng (tồn kho < 15) ---
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

// --- Lấy danh sách Top Sản Phẩm theo doanh thu ---
public java.util.List<Object[]> getTopSanPham() {
    java.util.List<Object[]> list = new java.util.ArrayList<>();
    Connection cnn = data.getConnection();
    try {
        String qry = "SELECT sp.id, sp.ten, lsp.name as danhMuc, SUM(cthd.soLuong) as slBan, sp.donGia, SUM(cthd.thanhTien) as doanhThu " +
                     "FROM sanpham sp " +
                     "JOIN chitiethoadon cthd ON sp.id = cthd.sanPhamId " +
                     "LEFT JOIN loaisanpham lsp ON sp.loaiSanPhamId = lsp.id " +
                     "GROUP BY sp.id ORDER BY doanhThu DESC";
        Statement st = cnn.createStatement();
        ResultSet rs = st.executeQuery(qry);
        int rank = 1;
        while (rs.next()) {
            String danhMuc = rs.getString("danhMuc");
            list.add(new Object[]{
                "#" + rank++,
                "SP" + String.format("%03d", rs.getInt("id")),
                rs.getString("ten"),
                danhMuc != null ? danhMuc : "Khác",
                rs.getInt("slBan"),
                rs.getLong("donGia"),
                rs.getLong("doanhThu")
            });
        }
    } catch (SQLException e) { e.printStackTrace(); }
    finally { data.closeConnection(); }
    return list;
}

public java.util.List<Object[]> getTopNhanVien() {
    java.util.List<Object[]> list = new java.util.ArrayList<>();
    Connection cnn = data.getConnection();
    try {
        // SQL JOIN để lấy dữ liệu thực tế từ 2 bảng
        String qry = "SELECT nv.id, nv.ho, nv.ten, COUNT(hd.id) as soDon, SUM(hd.tongTien) as doanhThu " +
                     "FROM nhanvien nv JOIN hoadon hd ON nv.id = hd.nhanVienId " +
                     "GROUP BY nv.id ORDER BY doanhThu DESC";
        Statement st = cnn.createStatement();
        ResultSet rs = st.executeQuery(qry);
        int rank = 1;
        while (rs.next()) {
            String tenFull = rs.getString("ho") + " " + rs.getString("ten");
            list.add(new Object[]{
                "#" + rank++,                                    // Xếp hạng
                "NV" + String.format("%03d", rs.getInt("id")),   // Mã NV
                tenFull,                                         // Tên NV
                "Nhân Viên",                                     // Vị trí
                rs.getInt("soDon"),                              // Số đơn
                rs.getLong("doanhThu")                           // Tổng tiền
            });
        }
    } catch (SQLException e) { e.printStackTrace(); }
    finally { data.closeConnection(); }
    return list;
}
}