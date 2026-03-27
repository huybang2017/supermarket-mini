package SieuThiMiniDAO;

import DTO.HangSanXuatDTO;
import DTO.LoaiSanPhamDTO;

import javax.swing.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;

public class HangSanXuatDAO {
    MyConnection data = new MyConnection();
    public ArrayList<HangSanXuatDTO> docDSHSX(){
        ArrayList<HangSanXuatDTO> dshxs = new ArrayList<>();
        Connection cnn = data.getConnection();
        try {
            String qry = "SELECT * FROM hangsanxuat";
            Statement st = cnn.createStatement();
            ResultSet rs = st.executeQuery(qry);
            while (rs.next()) {
                HangSanXuatDTO hxs = new HangSanXuatDTO();
                hxs.setMaHang(rs.getInt("id"));
                hxs.setTenHang(rs.getString("ten"));
                hxs.setDiaChi(rs.getString("diaChi"));
                hxs.setSdt(rs.getString("phone"));
                dshxs.add(hxs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            data.closeConnection(); // ĐÓNG KẾT NỐI
        }
        return dshxs;
    }
    public void themHSX(HangSanXuatDTO hsx) {
        Connection cnn = data.getConnection();
        try {
            String qry = "INSERT INTO hangsanxuat VALUES ('"
                    + hsx.getMaHang() + "','"
                    + hsx.getTenHang() + "','"
                    + hsx.getDiaChi() + "','"
                    + hsx.getSdt() + "')";
            Statement st = cnn.createStatement();
            st.executeUpdate(qry);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Lỗi thêm: " + ex.getMessage());
        }finally {
            data.closeConnection(); // ĐÓNG KẾT NỐI
        }
    }

    public void xoaHSX(int maHang) {
        Connection cnn = data.getConnection();

        try {
            String qry = "DELETE FROM hangsanxuat WHERE id = '" + maHang + "'";
            Statement st = cnn.createStatement();
            st.executeUpdate(qry);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi xóa: " + e.getMessage());
        }finally {
            data.closeConnection(); // ĐÓNG KẾT NỐI
        }
    }

    public void suaHSX(HangSanXuatDTO hsx) {
        Connection cnn = data.getConnection();
        try {
            String qry = "UPDATE hangsanxuat SET "
                    + "ten = '" + hsx.getTenHang() + "', "
                    + "diaChi = '" + hsx.getDiaChi() + "', "
                    + "phone = '" + hsx.getSdt() + "', "
                    + "WHERE id = '" + hsx.getMaHang() + "'";
            Statement st = cnn.createStatement();
            st.executeUpdate(qry);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi sửa: " + e.getMessage());
        }finally {
            data.closeConnection(); // ĐÓNG KẾT NỐI
        }
    }

    public boolean importExcel(HangSanXuatDTO hsx) {
        String sql = "INSERT INTO hangsanxuat (id, ten, diaChi, phone) VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE ten = VALUES(ten), diaChi = VALUES(diaChi), phone = VALUES(phone)";
        try (Connection cnn = data.getConnection();
             PreparedStatement ps = cnn.prepareStatement(sql)) {
            ps.setInt(1, hsx.getMaHang());
            ps.setString(2, hsx.getTenHang());
            ps.setString(3, hsx.getDiaChi());
            ps.setString(4, hsx.getSdt());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi Import HangSanXuat: " + e.getMessage());
            return false;
        }
    }
    public ArrayList<HangSanXuatDTO> timHangSanXuat(String keyword) {
        ArrayList<HangSanXuatDTO> ketQua = new ArrayList<>();
        String sql = "SELECT * FROM hangsanxuat WHERE CAST(id AS CHAR) LIKE ? OR LOWER(ten) LIKE ? OR LOWER(diaChi) LIKE ? OR phone LIKE ?";
        try (Connection cnn = data.getConnection();
             PreparedStatement ps = cnn.prepareStatement(sql)) {
             
            String searchKw = "%" + keyword.toLowerCase() + "%";
            ps.setString(1, searchKw); 
            ps.setString(2, searchKw); 
            ps.setString(3, searchKw); 
            ps.setString(4, searchKw); 
    
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                HangSanXuatDTO hsx = new HangSanXuatDTO();
                hsx.setMaHang(rs.getInt("id")); 
                hsx.setTenHang(rs.getString("ten")); 
                hsx.setDiaChi(rs.getString("diaChi")); 
                hsx.setSdt(rs.getString("phone")); 
                ketQua.add(hsx);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi tìm kiếm Hãng Sản Xuất: " + e.getMessage());
        }
        return ketQua; 
    }
}
