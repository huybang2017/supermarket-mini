package SieuThiMiniDAO;

import DTO.NhanVienDTO;
import javax.swing.*;

import java.sql.*;
import java.util.ArrayList;

public class NhanVienDAO { 
    MyConnection data = new MyConnection();

    // 1. Đọc danh sách nhân viên
    public ArrayList<NhanVienDTO> docDSNV() {
        ArrayList<NhanVienDTO> dsnv = new ArrayList<>();
        Connection cnn = data.getConnection(); // MỞ KẾT NỐI
        try {
            String qry = "SELECT * FROM nhanvien";
            Statement st = cnn.createStatement();
            ResultSet rs = st.executeQuery(qry);
            while (rs.next()) {
                NhanVienDTO nv = new NhanVienDTO();
                nv.setMaNV(rs.getInt("id"));
                nv.setHoNV(rs.getString("ho"));
                nv.setTenNV(rs.getString("ten"));
                nv.setSdt(rs.getString("phone"));
                nv.setDiaChi(rs.getString("diaChi"));
                nv.setNgaySinh(rs.getDate("ngaySinh"));
                nv.setLuong(rs.getDouble("luong"));
                dsnv.add(nv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            data.closeConnection(); // ĐÓNG KẾT NỐI
        }
        return dsnv;
    }

    // 2. Thêm nhân viên
    public void themNV(NhanVienDTO nv) {
        Connection cnn = data.getConnection();
        try {
            // Đặt tiền tố N ở trước Họ, Tên, Địa chỉ để lưu tiếng Việt có dấu
            String qry = "INSERT INTO nhanvien VALUES ('"
                    + nv.getMaNV() + "', N'"
                    + nv.getHoNV() + "', N'"
                    + nv.getTenNV() + "', '"
                    + nv.getSdt() + "', N'"
                    + nv.getDiaChi() + "', '"
                    + nv.getNgaySinh() + "', '"
                    + nv.getLuong() + "')";
            Statement st = cnn.createStatement();
            st.executeUpdate(qry);
            JOptionPane.showMessageDialog(null, "Thêm nhân viên thành công!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Lỗi thêm NV: " + ex.getMessage());
        } finally {
            data.closeConnection(); // ĐÓNG KẾT NỐI
        }
    }

    // 3. Xóa nhân viên (Đã đổi tên từ xoaSP -> xoaNV)
    public void xoaNV(int manv) {
        Connection cnn = data.getConnection();
        try {
            String qry = "DELETE FROM nhanvien WHERE id = '" + manv + "'";
            Statement st = cnn.createStatement();
            st.executeUpdate(qry);
            JOptionPane.showMessageDialog(null, "Xóa nhân viên thành công!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi xóa NV: " + e.getMessage());
        } finally {
            data.closeConnection(); // ĐÓNG KẾT NỐI
        }
    }

    // 4. Sửa nhân viên (Đã đổi tên từ suaSP -> suaNV và thêm nháy đơn '')
    public void suaNV(NhanVienDTO nv) {
        Connection cnn = data.getConnection();
        try {
            // Đã bổ sung dấu nháy đơn '' cho diaChi và ngaySinh, thêm N cho tiếng Việt
            String qry = "UPDATE nhanvien SET "
                    + "ho = N'" + nv.getHoNV() + "', "
                    + "ten = N'" + nv.getTenNV() + "', "
                    + "phone = '" + nv.getSdt() + "', "
                    + "diaChi = N'" + nv.getDiaChi() + "', "
                    + "ngaySinh = '" + nv.getNgaySinh() + "', "
                    + "luong = '" + nv.getLuong() + "' "
                    + "WHERE id = '" + nv.getMaNV() + "'";
            Statement st = cnn.createStatement();
            st.executeUpdate(qry);
            JOptionPane.showMessageDialog(null, "Sửa thông tin nhân viên thành công!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi sửa NV: " + e.getMessage());
        } finally {
            data.closeConnection(); // ĐÓNG KẾT NỐI
        }
    }

    public boolean importExcel(NhanVienDTO nv) {
        String sql = "INSERT INTO nhanvien (id, ho, ten, phone, diaChi, ngaySinh, luong) VALUES (?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE ho = VALUES(ho), ten = VALUES(ten), phone = VALUES(phone), diaChi = VALUES(diaChi), ngaySinh = VALUES(ngaySinh), luong = VALUES(luong)";
        try (Connection cnn = data.getConnection();
             PreparedStatement ps = cnn.prepareStatement(sql)) {
            ps.setInt(1, nv.getMaNV());
            ps.setString(2, nv.getHoNV());
            ps.setString(3, nv.getTenNV());
            ps.setString(4, nv.getSdt());
            ps.setString(5, nv.getDiaChi());
            ps.setDate(6, new java.sql.Date(nv.getNgaySinh().getTime()));
            ps.setDouble(7, nv.getLuong());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi Import NhanVien: " + e.getMessage());
            return false;
        }
    }

public ArrayList<NhanVienDTO> timNhanVien(String keyword) {
        ArrayList<NhanVienDTO> ketQua = new ArrayList<>();
        String sql = "SELECT * FROM nhanvien WHERE CAST(id AS CHAR) LIKE ? OR LOWER(ho) LIKE ? OR LOWER(ten) LIKE ? OR phone LIKE ?";
        try (Connection cnn = data.getConnection();
             PreparedStatement ps = cnn.prepareStatement(sql)) {
             
            String searchKw = "%" + keyword.toLowerCase() + "%";
            ps.setString(1, searchKw);
            ps.setString(2, searchKw);
            ps.setString(3, searchKw);
            ps.setString(4, searchKw);
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                NhanVienDTO nv = new NhanVienDTO();
                nv.setMaNV(rs.getInt("id"));
                nv.setHoNV(rs.getString("ho")); // Đã fix lỗi sai tên cột
                nv.setTenNV(rs.getString("ten"));
                nv.setSdt(rs.getString("phone"));
                nv.setDiaChi(rs.getString("diaChi"));
                nv.setNgaySinh(rs.getDate("ngaySinh"));
                nv.setLuong(rs.getDouble("luong"));
                ketQua.add(nv);
            } 
        } catch (SQLException e) {
            System.err.println("Lỗi tìm kiếm nhân viên: " + e.getMessage());
        }
        return ketQua;
    }
    public ArrayList<NhanVienDTO> timNhanVienTheoLuong(double luongTu, double luongDen) {
        ArrayList<NhanVienDTO> ketQua = new ArrayList<>();
        String sql = "SELECT * FROM nhanvien WHERE luong >= ? AND luong <= ?";
        try (java.sql.Connection cnn = data.getConnection();
             java.sql.PreparedStatement ps = cnn.prepareStatement(sql)) {

            ps.setDouble(1, luongTu);
            ps.setDouble(2, luongDen);
            
            java.sql.ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                NhanVienDTO nv = new NhanVienDTO();
                nv.setMaNV(rs.getInt("id")); 
                nv.setHoNV(rs.getString("ho"));
                nv.setTenNV(rs.getString("ten"));
                nv.setNgaySinh(rs.getDate("ngaySinh"));
                nv.setDiaChi(rs.getString("diaChi"));
                nv.setSdt(rs.getString("phone"));
                nv.setLuong(rs.getDouble("luong"));
                ketQua.add(nv);
            }
        } catch (java.sql.SQLException e) {
            System.err.println("Lỗi tìm NV theo lương: " + e.getMessage());
        } 
        return ketQua;
    }
}