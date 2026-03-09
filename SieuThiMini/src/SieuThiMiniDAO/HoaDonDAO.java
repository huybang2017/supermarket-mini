package SieuThiMiniDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import DTO.HoaDonDTO;

public class HoaDonDAO {
    
    // Đã sửa lại tên cột cho khớp với database (id, nhanVienId, khachHangId, ngayLapHD, tongTien)
    private static final String SELECT_ALL = "SELECT * FROM hoadon";
    private static final String SELECT_BY_ID = "SELECT * FROM hoadon WHERE id = ?";
    private static final String INSERT = "INSERT INTO hoadon (id, nhanVienId, khachHangId, ngayLapHD, tongTien) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE hoadon SET nhanVienId = ?, khachHangId = ?, ngayLapHD = ?, tongTien = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM hoadon WHERE id = ?";
    
    private final MyConnection dataSource;
    
    public HoaDonDAO() {
        this.dataSource = new MyConnection();
    }
    
    public HoaDonDAO(MyConnection dataSource) {
        this.dataSource = dataSource;
    }

    public List<HoaDonDTO> getListHoaDon() {
        List<HoaDonDTO> list = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(SELECT_ALL)) {
            
            while (rs.next()) {
                list.add(mapResultSetToDTO(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách hóa đơn: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    public boolean addHoaDon(HoaDonDTO hd) {
        if (hd == null) return false;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT)) {
            setHoaDonParameters(ps, hd, true);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi thêm hóa đơn: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateHoaDon(HoaDonDTO hd) {
        if (hd == null) return false;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE)) {
            setHoaDonParameters(ps, hd, false);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật hóa đơn: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteHoaDon(int maHD) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE)) {
            ps.setInt(1, maHD);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa hóa đơn: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public HoaDonDTO getHoaDonById(int maHD) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID)) {
            ps.setInt(1, maHD);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToDTO(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy hóa đơn theo ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Helper method: Đã sửa lại tên lấy từ cột DB
     */
    private HoaDonDTO mapResultSetToDTO(ResultSet rs) throws SQLException {
        HoaDonDTO hd = new HoaDonDTO();
        hd.setMaHD(rs.getInt("id"));               // Database là id
        hd.setMaNV(rs.getInt("nhanVienId"));       // Database là nhanVienId
        hd.setMaKH(rs.getInt("khachHangId"));      // Database là khachHangId
        hd.setNgayLapDon(rs.getTimestamp("ngayLapHD")); // Database là ngayLapHD (dùng getTimestamp để lấy cả giờ phút)
        hd.setTongTien(rs.getDouble("tongTien"));  // Database là tongTien
        return hd;
    }
    
    private void setHoaDonParameters(PreparedStatement ps, HoaDonDTO hd, boolean isInsert) 
            throws SQLException {
        if (isInsert) {
            ps.setInt(1, hd.getMaHD());
            ps.setInt(2, hd.getMaNV());
            ps.setInt(3, hd.getMaKH());
            ps.setTimestamp(4, new java.sql.Timestamp(hd.getNgayLapDon().getTime())); // Dùng Timestamp
            ps.setDouble(5, hd.getTongTien());
        } else {
            ps.setInt(1, hd.getMaNV());
            ps.setInt(2, hd.getMaKH());
            ps.setTimestamp(3, new java.sql.Timestamp(hd.getNgayLapDon().getTime()));
            ps.setDouble(4, hd.getTongTien());
            ps.setInt(5, hd.getMaHD());
        }
    }
}