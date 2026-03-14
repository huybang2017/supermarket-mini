package SieuThiMiniDAO;

import java.sql.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import DTO.PhieuNhapHangDTO;

public class PhieuNhapHangDAO {
    MyConnection data = new MyConnection();     

    public ArrayList<PhieuNhapHangDTO> docDSPNH() {
        ArrayList<PhieuNhapHangDTO> dspn = new ArrayList<>();
        Connection cnn = data.getConnection();
        try {
            // Tên bảng trong SQL là PhieuNhapHang
            String qry = "SELECT * FROM PhieuNhapHang";
            Statement st = cnn.createStatement();
            ResultSet rs = st.executeQuery(qry);
            while (rs.next()) {
                PhieuNhapHangDTO pn = new PhieuNhapHangDTO();
                // SQL: id, nhanVienId, nhaCungCapId, tongTien, ngayNhap
                pn.setMaPNH(rs.getInt("id")); 
                pn.setMaNV(rs.getInt("nhanVienId"));  
                pn.setMaNCC(rs.getInt("nhaCungCapId")); 
                pn.setTongTien(rs.getLong("tongTien")); 
                pn.setNgayNhap(rs.getDate("ngayNhap"));
                dspn.add(pn);
            }
        } catch (SQLException e) { 
            System.out.println("Lỗi Đọc: " + e.getMessage()); 
        } finally {
            data.closeConnection(); 
        }
        return dspn;
    }

    public void themPNH(PhieuNhapHangDTO pn) {
        Connection cnn = data.getConnection();
        try {
            // Thứ tự cột: id, nhanVienId, nhaCungCapId, tongTien, ngayNhap
            String sql = "INSERT INTO PhieuNhapHang (id, nhanVienId, nhaCungCapId, tongTien, ngayNhap) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pst = cnn.prepareStatement(sql);
            
            pst.setInt(1, pn.getMaPNH());
            pst.setInt(2, pn.getMaNV());
            pst.setInt(3, pn.getMaNCC());
            pst.setDouble(4, pn.getTongTien());
            pst.setDate(5, new java.sql.Date(pn.getNgayNhap().getTime()));
            
            pst.executeUpdate();
        } catch (SQLException e) { 
            System.out.println("Lỗi Thêm: " + e.getMessage()); 
        } finally {
            data.closeConnection(); 
        }
    }

    public void xoaPNH(int ma) { 
        Connection cnn = data.getConnection();
        try {
            // Khóa chính trong SQL là 'id'
            String sql = "DELETE FROM PhieuNhapHang WHERE id = ?";
            PreparedStatement pst = cnn.prepareStatement(sql);
            pst.setInt(1, ma);
            pst.executeUpdate();
        } catch (SQLException e) { 
            System.out.println("Lỗi Xóa: " + e.getMessage()); 
        } finally {
            data.closeConnection(); 
        }
    }

    public void suaPNH(PhieuNhapHangDTO pn) {
        Connection cnn = data.getConnection();
        try {
            // Tên cột phải khớp: nhanVienId, nhaCungCapId, tongTien, ngayNhap
            String sql = "UPDATE PhieuNhapHang SET nhanVienId = ?, nhaCungCapId = ?, tongTien = ?, ngayNhap = ? WHERE id = ?";
            PreparedStatement pst = cnn.prepareStatement(sql);
            
            pst.setInt(1, pn.getMaNV());
            pst.setInt(2, pn.getMaNCC());
            pst.setDouble(3, pn.getTongTien());
            pst.setDate(4, new java.sql.Date(pn.getNgayNhap().getTime()));
            pst.setInt(5, pn.getMaPNH()); 
            
            pst.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi sửa: " + e.getMessage());
        } finally {
            data.closeConnection(); 
        }
    }

    public int getNextID() {
        Connection cnn = data.getConnection();
        int nextID = 1; 
        try {
            // Lấy MAX của cột 'id'
            String query = "SELECT MAX(id) FROM PhieuNhapHang";
            Statement st = cnn.createStatement();
            ResultSet rs = st.executeQuery(query);
            if (rs.next()) {
                nextID = rs.getInt(1) + 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally { 
            data.closeConnection(); 
        }
        return nextID;
    }
}