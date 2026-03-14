package SieuThiMiniDAO;

import java.sql.*;
import java.util.ArrayList;
import DTO.ChiTietPhieuNhapHangDTO;

public class ChiTietPhieuNhapHangDAO {
    MyConnection data = new MyConnection();

    public ArrayList<ChiTietPhieuNhapHangDTO> docTheoMaPN(int maPN) { 
        ArrayList<ChiTietPhieuNhapHangDTO> dsct = new ArrayList<>();
        // Sử dụng try-with-resources để tự động đóng kết nối (Chuẩn giống Hóa Đơn)
        try (Connection cnn = data.getConnection();
             PreparedStatement ps = cnn.prepareStatement("SELECT * FROM chitietphieunhaphang WHERE phieuNhapHangId = ?")) {
            
            ps.setInt(1, maPN);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ChiTietPhieuNhapHangDTO ct = new ChiTietPhieuNhapHangDTO();
                    ct.setMaPNH(rs.getInt("phieuNhapHangId")); 
                    ct.setMaSP(rs.getInt("sanPhamId"));  
                    ct.setSoLuong(rs.getInt("soLuong"));
                    ct.setDonGia(rs.getLong("giaNhap")); // Lưu ý: Database là giaNhap
                    ct.setThanhTien(rs.getLong("thanhTien"));
                    dsct.add(ct);
                }
            }
        } catch (SQLException e) { 
            System.err.println("Lỗi đọc chi tiết: " + e.getMessage()); 
        }
        return dsct;
    }

    public boolean themCTPNH(ChiTietPhieuNhapHangDTO ct) {
        try (Connection cnn = data.getConnection();
             PreparedStatement ps = cnn.prepareStatement(
                 "INSERT INTO chitietphieunhaphang (phieuNhapHangId, sanPhamId, soLuong, giaNhap, thanhTien) VALUES (?, ?, ?, ?, ?)"
             )) {
            
            ps.setInt(1, ct.getMaPNH());
            ps.setInt(2, ct.getMaSP());
            ps.setInt(3, ct.getSoLuong());
            ps.setLong(4, ct.getDonGia()); 
            ps.setLong(5, ct.getThanhTien()); // Bây giờ thanhTien đã được tính từ BUS nên sẽ không bị lỗi nữa
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) { 
            System.err.println("Lỗi thêm chi tiết phiếu nhập: " + e.getMessage()); 
            e.printStackTrace();
        }
        return false;
    }

    public void xoaCTPNH(int ma){
        try (Connection cnn = data.getConnection();
             PreparedStatement ps = cnn.prepareStatement("DELETE FROM chitietphieunhaphang WHERE phieuNhapHangId = ?")) {
            
            ps.setInt(1, ma);
            ps.executeUpdate();
            
        } catch (SQLException e) {
             System.err.println("Lỗi xóa: " + e.getMessage());
        }
    }

    public void suaCTPNH(ChiTietPhieuNhapHangDTO ct) {
        try (Connection cnn = data.getConnection();
             PreparedStatement ps = cnn.prepareStatement(
                 "UPDATE chitietphieunhaphang SET soLuong = ?, giaNhap = ?, thanhTien = ? WHERE phieuNhapHangId = ? AND sanPhamId = ?"
             )) {
            
            ps.setInt(1, ct.getSoLuong());
            ps.setLong(2, ct.getDonGia());
            ps.setLong(3, ct.getThanhTien());
            ps.setInt(4, ct.getMaPNH());
            ps.setInt(5, ct.getMaSP());
            ps.executeUpdate();
            
        } catch (SQLException e) {
             System.err.println("Lỗi sửa: " + e.getMessage());
        }
    }
}