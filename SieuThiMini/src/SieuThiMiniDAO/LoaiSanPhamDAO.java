package SieuThiMiniDAO;

import DTO.HangSanXuatDTO;
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
                dslsp.add(lsp);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi đọc danh sách loại SP: " + e.getMessage());
        }
        return dslsp;
    }

    public boolean themLSP(LoaiSanPhamDTO lsp) {
        // Cột id là AUTO_INCREMENT nên không cần truyền vào
        String qry = "INSERT INTO loaisanpham (name) VALUES (?)";
        try (Connection cnn = data.getConnection();
            PreparedStatement ps = cnn.prepareStatement(qry)) {
            
            ps.setString(1, lsp.getTenLoai());
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
            ps.setInt(3, lsp.getMaLoai());
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Lỗi sửa: " + e.getMessage());
            return false;
        }
    }

    public boolean importExcel(LoaiSanPhamDTO lsp) {
        String sql = "INSERT INTO loaisanpham (id, name) VALUES (?, ?) ON DUPLICATE KEY UPDATE name = VALUES(name)";
        try (Connection cnn = data.getConnection();
             PreparedStatement ps = cnn.prepareStatement(sql)) {
            ps.setInt(1, lsp.getMaLoai());
            ps.setString(2, lsp.getTenLoai());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi Import LoaiSanPham: " + e.getMessage());
            return false;
        }
    }

    public LoaiSanPhamDTO timLoaiSanPham(String keyword) {
        String sql = "SELECT * FROM loaisanpham WHERE id = ? OR LOWER(name) LIKE ?";
        try (Connection cnn = data.getConnection();
             PreparedStatement ps = cnn.prepareStatement(sql)) {
    
            ps.setString(1, keyword); 
            ps.setString(2, "%" + keyword.toLowerCase() + "%"); 
    
            ResultSet rs = ps.executeQuery();
    
            if (rs.next()) {
                LoaiSanPhamDTO lsp = new LoaiSanPhamDTO();
                lsp.setMaLoai(rs.getInt("id")); 
                lsp.setTenLoai(rs.getString("name")); 
                return lsp;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi tìm kiếm Loại Sản Phẩm: " + e.getMessage());
        }
        return null; 
    }
}