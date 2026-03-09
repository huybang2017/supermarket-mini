package SieuThiMiniDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import DTO.HoaDonDTO;

public class HoaDonDAO {
    
    // Constants cho SQL queries
    private static final String SELECT_ALL = "SELECT * FROM hoadon";
    private static final String SELECT_BY_ID = "SELECT * FROM hoadon WHERE mahoadon = ?";
    private static final String INSERT = "INSERT INTO hoadon (mahoadon, manhanvien, makhachhang, ngaylapdon, tongtien) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE hoadon SET manhanvien = ?, makhachhang = ?, ngaylapdon = ?, tongtien = ? WHERE mahoadon = ?";
    private static final String DELETE = "DELETE FROM hoadon WHERE mahoadon = ?";
    
    private final MyConnection dataSource;
    
    // Constructor với dependency injection
    public HoaDonDAO() {
        this.dataSource = new MyConnection();
    }
    
    public HoaDonDAO(MyConnection dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Lấy danh sách tất cả hóa đơn
     */
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

    /**
     * Thêm hóa đơn mới
     */
    public boolean addHoaDon(HoaDonDTO hd) {
        if (hd == null) {
            return false;
        }
        
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

    /**
     * Cập nhật thông tin hóa đơn
     */
    public boolean updateHoaDon(HoaDonDTO hd) {
        if (hd == null) {
            return false;
        }
        
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

    /**
     * Xóa hóa đơn theo mã
     */
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

    /**
     * Lấy hóa đơn theo mã
     */
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
     * Helper method: Chuyển ResultSet thành DTO
     */
    private HoaDonDTO mapResultSetToDTO(ResultSet rs) throws SQLException {
        HoaDonDTO hd = new HoaDonDTO();
        hd.setMaHD(rs.getInt("mahoadon"));
        hd.setMaNV(rs.getInt("manhanvien"));
        hd.setMaKH(rs.getInt("makhachhang"));
        hd.setNgayLapDon(rs.getDate("ngaylapdon"));
        hd.setTongTien(rs.getDouble("tongtien"));
        return hd;
    }
    
    /**
     * Helper method: Set parameters cho PreparedStatement
     */
    private void setHoaDonParameters(PreparedStatement ps, HoaDonDTO hd, boolean isInsert) 
            throws SQLException {
        if (isInsert) {
            ps.setInt(1, hd.getMaHD());
            ps.setInt(2, hd.getMaNV());
            ps.setInt(3, hd.getMaKH());
            ps.setDate(4, new java.sql.Date(hd.getNgayLapDon().getTime()));
            ps.setDouble(5, hd.getTongTien());
        } else {
            ps.setInt(1, hd.getMaNV());
            ps.setInt(2, hd.getMaKH());
            ps.setDate(3, new java.sql.Date(hd.getNgayLapDon().getTime()));
            ps.setDouble(4, hd.getTongTien());
            ps.setInt(5, hd.getMaHD());
        }
    }
}