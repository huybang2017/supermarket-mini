package SieuThiMiniDAO;

import DTO.SanPhamDTO;
import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SanPhamDAO { 
    MyConnection data = new MyConnection();

    public ArrayList<SanPhamDTO> docDSSP() {
        ArrayList<SanPhamDTO> dssp = new ArrayList<>();
        Connection cnn = data.getConnection(); 
        try {
            String qry = "SELECT * FROM sanpham";
            Statement st = cnn.createStatement();
            ResultSet rs = st.executeQuery(qry);
            while (rs.next()) {
                SanPhamDTO sp = new SanPhamDTO();
                sp.setMasanpham(rs.getInt("id"));
                sp.setMaLoai(rs.getInt("loaiSanPhamId"));
                sp.setMaHang(rs.getInt("hangId"));
                sp.setTensanpham(rs.getString("ten"));
                sp.setSoluong(rs.getInt("soLuong"));
                sp.setDongia(rs.getLong("donGia")); 
                sp.setGiaNhap(rs.getLong("giaNhap")); // Đã khôi phục
                sp.setLoiNhuan(rs.getDouble("loiNhuan")); // Đã khôi phục
                sp.setDonvitinh(rs.getString("donViTinh"));
                dssp.add(sp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            data.closeConnection(); 
        }
        return dssp;
    }

    public boolean themSP(SanPhamDTO sp) {
        Connection cnn = data.getConnection();
        try {
            String sql = "INSERT INTO sanpham (loaiSanPhamId, hangId, ten, soLuong, donGia, giaNhap, loiNhuan, donViTinh) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            
            PreparedStatement pst = cnn.prepareStatement(sql);
            pst.setInt(1, sp.getMaLoai());
            pst.setInt(2, sp.getMaHang());
            pst.setString(3, sp.getTensanpham());
            pst.setInt(4, sp.getSoluong());
            pst.setLong(5, sp.getDongia());
            pst.setLong(6, sp.getGiaNhap());
            pst.setDouble(7, sp.getLoiNhuan());
            pst.setString(8, sp.getDonvitinh());

            pst.executeUpdate();
            return true;
        } catch (SQLException ex) {
            return false;
        } finally {
            data.closeConnection(); 
        }
    }

    public boolean suaSP(SanPhamDTO sp) {
        Connection cnn = data.getConnection();
        try {
            String sql = "UPDATE sanpham SET "
                    + "loaiSanPhamId = ?, hangId = ?, ten = ?, soLuong = ?, donGia = ?, giaNhap = ?, loiNhuan = ?, donViTinh = ? "
                    + "WHERE id = ?";
            
            PreparedStatement pst = cnn.prepareStatement(sql);
            pst.setInt(1, sp.getMaLoai());
            pst.setInt(2, sp.getMaHang());
            pst.setString(3, sp.getTensanpham());   
            pst.setInt(4, sp.getSoluong());
            pst.setLong(5, sp.getDongia());
            pst.setLong(6, sp.getGiaNhap());
            pst.setDouble(7, sp.getLoiNhuan());
            pst.setString(8, sp.getDonvitinh());
            pst.setInt(9, sp.getMasanpham()); 

            pst.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        } finally {
            data.closeConnection(); 
        }
    }
        public boolean xoaSP(int maSP) {
        Connection cnn = data.getConnection();
        try {
            String qry = "DELETE FROM sanpham WHERE id = ?";
            PreparedStatement pst = cnn.prepareStatement(qry);
            pst.setInt(1, maSP);
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Xóa thành công!");
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi xóa (Có thể do SP đang nằm trong Hóa Đơn): " + e.getMessage());
            return false;
        } finally {
            data.closeConnection();
        }
    }

    public boolean capNhatSoLuong(int masp, int soLuongMoi) {
        Connection cnn = data.getConnection();
        try {
            String sql = "UPDATE sanpham SET soLuong = ? WHERE id = ?"; 
            PreparedStatement pst = cnn.prepareStatement(sql);
            pst.setInt(1, soLuongMoi);
            pst.setInt(2, masp);
            pst.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Lỗi cập nhật số lượng: " + e.getMessage());
            return false;
        } finally {
            data.closeConnection();
        }
    }

    public boolean importExcel(SanPhamDTO sp) {
        String sql = "INSERT INTO sanpham (id, loaiSanPhamId, hangId, ten, soLuong, donGia, giaNhap, loiNhuan, donViTinh) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE loaiSanPhamId = VALUES(loaiSanPhamId), hangId = VALUES(hangId), ten = VALUES(ten), soLuong = VALUES(soLuong), donGia = VALUES(donGia), giaNhap = VALUES(giaNhap), loiNhuan = VALUES(loiNhuan), donViTinh = VALUES(donViTinh)";
        try (Connection cnn = data.getConnection();
             PreparedStatement ps = cnn.prepareStatement(sql)) {
            ps.setInt(1, sp.getMasanpham());
            ps.setInt(2, sp.getMaLoai());
            ps.setInt(3, sp.getMaHang());
            ps.setString(4, sp.getTensanpham());
            ps.setInt(5, sp.getSoluong());
            ps.setLong(6, sp.getDongia());
            ps.setLong(7, sp.getGiaNhap());
            ps.setDouble(8, sp.getLoiNhuan());
            ps.setString(9, sp.getDonvitinh());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi Import SanPham: " + e.getMessage());
            return false;
        }
    }

    public ArrayList<SanPhamDTO> timSanPham(String keyword) {
        ArrayList<SanPhamDTO> ketQua = new ArrayList<>();
        String sql = "SELECT * FROM sanpham WHERE LOWER(ten) LIKE ? OR CAST(id AS CHAR) LIKE ?";
        try (Connection cnn = data.getConnection();
             PreparedStatement ps = cnn.prepareStatement(sql)) {

            String searchKeyword = "%" + keyword.toLowerCase() + "%";
            ps.setString(1, searchKeyword);
            ps.setString(2, searchKeyword);
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                SanPhamDTO sp = new SanPhamDTO();
                sp.setMasanpham(rs.getInt("id"));
                sp.setMaLoai(rs.getInt("loaiSanPhamId"));
                sp.setMaHang(rs.getInt("hangId"));
                sp.setTensanpham(rs.getString("ten"));
                sp.setSoluong(rs.getInt("soLuong"));
                sp.setDongia(rs.getLong("donGia")); 
                sp.setGiaNhap(rs.getLong("giaNhap")); 
                sp.setLoiNhuan(rs.getDouble("loiNhuan")); 
                sp.setDonvitinh(rs.getString("donViTinh"));
                ketQua.add(sp);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi tìm sản phẩm: " + e.getMessage());
        } 
        return ketQua;
    }
    public ArrayList<SanPhamDTO> timSanPhamTheoGia(long giaTu, long giaDen) {
        ArrayList<SanPhamDTO> ketQua = new ArrayList<>();
        String sql = "SELECT * FROM sanpham WHERE donGia >= ? AND donGia <= ?";
        try (Connection cnn = data.getConnection();
             PreparedStatement ps = cnn.prepareStatement(sql)) {

            ps.setLong(1, giaTu);
            ps.setLong(2, giaDen);
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                SanPhamDTO sp = new SanPhamDTO();
                sp.setMasanpham(rs.getInt("id"));
                sp.setMaLoai(rs.getInt("loaiSanPhamId"));
                sp.setMaHang(rs.getInt("hangId"));
                sp.setTensanpham(rs.getString("ten"));
                sp.setSoluong(rs.getInt("soLuong"));
                sp.setDongia(rs.getLong("donGia")); 
                sp.setGiaNhap(rs.getLong("giaNhap")); 
                sp.setLoiNhuan(rs.getDouble("loiNhuan")); 
                sp.setDonvitinh(rs.getString("donViTinh"));
                ketQua.add(sp);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi tìm sản phẩm theo giá: " + e.getMessage());
        } 
        return ketQua;
    }
}
