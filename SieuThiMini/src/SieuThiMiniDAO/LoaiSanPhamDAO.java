package SieuThiMiniDAO;

import DTO.LoaiSanPhamDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class LoaiSanPhamDAO {
    MyConnection data = new MyConnection();

    public ArrayList<LoaiSanPhamDTO> docDSLSP(){
        ArrayList<LoaiSanPhamDTO> dslsp = new ArrayList<>();
        try (Connection cnn = data.getConnection();
             PreparedStatement ps = cnn.prepareStatement("SELECT * FROM loaisanpham");
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                LoaiSanPhamDTO lsp = new LoaiSanPhamDTO();
                lsp.setMaLoai(rs.getInt("id"));
                lsp.setTenLoai(rs.getString("name"));
                lsp.setHang(rs.getInt("hangId"));
                dslsp.add(lsp);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi đọc danh sách loại SP: " + e.getMessage());
        }
        return dslsp;
    }

    public boolean themLSP(LoaiSanPhamDTO lsp) {
        // Cột id là AUTO_INCREMENT nên không cần truyền vào
        String qry = "INSERT INTO loaisanpham (name, hangId) VALUES (?, ?)";
        try (Connection cnn = data.getConnection();
             PreparedStatement ps = cnn.prepareStatement(qry)) {
            
            ps.setString(1, lsp.getTenLoai());
            ps.setInt(2, lsp.getHang());
            return ps.executeUpdate() > 0;
            
        } catch (SQLException ex) {
            System.err.println("Lỗi thêm: " + ex.getMessage());
            return false;
        }
    }

    public boolean xoaLSP(int maLoai) {
        String qry = "DELETE FROM loaisanpham WHERE id = ?";
        try (Connection cnn = data.getConnection();
             PreparedStatement ps = cnn.prepareStatement(qry)) {
            
            ps.setInt(1, maLoai);
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Lỗi xóa: " + e.getMessage());
            return false;
        }
    }

    public boolean suaLSP(LoaiSanPhamDTO lsp) {
        String qry = "UPDATE loaisanpham SET name = ?, hangId = ? WHERE id = ?";
        try (Connection cnn = data.getConnection();
             PreparedStatement ps = cnn.prepareStatement(qry)) {
            
            ps.setString(1, lsp.getTenLoai());
            ps.setInt(2, lsp.getHang());
            ps.setInt(3, lsp.getMaLoai());
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Lỗi sửa: " + e.getMessage());
            return false;
        }
    }
}