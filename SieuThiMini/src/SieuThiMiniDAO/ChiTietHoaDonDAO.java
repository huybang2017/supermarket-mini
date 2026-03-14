package SieuThiMiniDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import DTO.ChiTietHoaDonDTO;

public class ChiTietHoaDonDAO {

    // Đã sửa lại toàn bộ tên cột: hoaDonId, sanPhamId, soLuong, donGia, thanhTien
    private static final String SELECT_ALL = "SELECT * FROM chitiethoadon";
    private static final String SELECT_BY_MAHD = "SELECT * FROM chitiethoadon WHERE hoaDonId = ?";
    private static final String INSERT = "INSERT INTO chitiethoadon (hoaDonId, sanPhamId, soLuong, donGia, thanhTien) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE chitiethoadon SET soLuong = ?, donGia = ?, thanhTien = ? WHERE hoaDonId = ? AND sanPhamId = ?";
    private static final String DELETE = "DELETE FROM chitiethoadon WHERE hoaDonId = ? AND sanPhamId = ?";

    private final MyConnection dataSource;

    public ChiTietHoaDonDAO() {
        this.dataSource = new MyConnection();
    }

    public ChiTietHoaDonDAO(MyConnection dataSource) {
        this.dataSource = dataSource;
    }

    public List<ChiTietHoaDonDTO> getAllChiTietHoaDon() {
        List<ChiTietHoaDonDTO> list = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(SELECT_ALL)) {

            while (rs.next()) {
                list.add(mapResultSetToDTO(rs));
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách chi tiết hóa đơn: " + e.getMessage());
            e.printStackTrace();
        }

        return list;
    }

    // Hàm này được BUS gọi để lấy danh sách chi tiết đổ lên GUI
    public List<ChiTietHoaDonDTO> getByMaHD(int maHD) {
        List<ChiTietHoaDonDTO> list = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_MAHD)) {

            ps.setInt(1, maHD);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToDTO(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy chi tiết hóa đơn theo mã HD: " + e.getMessage());
            e.printStackTrace();
        }

        return list;
    }

    public boolean addChiTietHoaDon(ChiTietHoaDonDTO ct) {
        if (ct == null) {
            return false;
        }

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT)) {

            setChiTietParameters(ps, ct, true);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Lỗi khi thêm chi tiết hóa đơn: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    public boolean updateChiTietHoaDon(ChiTietHoaDonDTO ct) {
        if (ct == null) {
            return false;
        }

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE)) {

            setChiTietParameters(ps, ct, false);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật chi tiết hóa đơn: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteChiTietHoaDon(int maHD, int maSP) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE)) {

            ps.setInt(1, maHD);
            ps.setInt(2, maSP);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa chi tiết hóa đơn: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Helper: Cập nhật đọc đúng tên cột DB
     */
    private ChiTietHoaDonDTO mapResultSetToDTO(ResultSet rs) throws SQLException {
        ChiTietHoaDonDTO ct = new ChiTietHoaDonDTO();

        ct.setMaHD(rs.getInt("hoaDonId"));
        ct.setMaSP(rs.getInt("sanPhamId"));
        ct.setSoLuong(rs.getInt("soLuong"));
        ct.setDonGia(rs.getLong("donGia"));
        ct.setThanhTien(rs.getLong("thanhTien"));

        return ct;
    }

    private void setChiTietParameters(PreparedStatement ps, ChiTietHoaDonDTO ct, boolean isInsert)
            throws SQLException {

        if (isInsert) {
            ps.setInt(1, ct.getMaHD());
            ps.setInt(2, ct.getMaSP());
            ps.setInt(3, ct.getSoLuong());
            ps.setDouble(4, ct.getDonGia());
            ps.setDouble(5, ct.getThanhTien());
        } else {
            ps.setInt(1, ct.getSoLuong());
            ps.setDouble(2, ct.getDonGia());
            ps.setDouble(3, ct.getThanhTien());
            ps.setInt(4, ct.getMaHD());
            ps.setInt(5, ct.getMaSP());
        }
    }
}