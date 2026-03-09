package SieuThiMiniDAO;

import java.sql.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import DTO.PhieuNhapHangDTO;

public class PhieuNhapHangDAO {
    MyConnection data = new MyConnection();     

    // ĐỌC DANH SÁCH PHIẾU NHẬP
    public ArrayList<PhieuNhapHangDTO> docDSPNH() {
        ArrayList<PhieuNhapHangDTO> dspn = new ArrayList<>();
        Connection cnn = data.getConnection();
        try {
            String qry = "SELECT * FROM phieunhaphang";
            Statement st = cnn.createStatement();
            ResultSet rs = st.executeQuery(qry);
            while (rs.next()) {
                PhieuNhapHangDTO pn = new PhieuNhapHangDTO();
                pn.setMaPNH(rs.getInt(1)); 
                pn.setMaNV(rs.getInt(2));  
                pn.setTongTien(rs.getDouble(3)); 
                pn.setMaNCC(rs.getInt(4)); 
                pn.setNgayNhap(rs.getDate(5));
                dspn.add(pn);
            }
        } catch (SQLException e) { 
            System.out.println("Lỗi Đọc: " + e.getMessage()); 
        } finally {
            data.closeConnection(); // Nên luôn đóng kết nối
        }
        return dspn;
    }

    // THÊM PHIẾU NHẬP
    public void themPNH(PhieuNhapHangDTO pn) {
        Connection cnn = data.getConnection();
        try {
            String sql = "INSERT INTO phieunhaphang VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pst = cnn.prepareStatement(sql);
            
            pst.setInt(1, pn.getMaPNH());
            pst.setInt(2, pn.getMaNV());
            pst.setDouble(3, pn.getTongTien());
            pst.setInt(4, pn.getMaNCC());
            
            // Xử lý ngày tháng an toàn và chuẩn xác hơn kiểu nối chuỗi
            pst.setDate(5, new java.sql.Date(pn.getNgayNhap().getTime()));
            
            pst.executeUpdate();
            
        } catch (SQLException e) { 
            System.out.println("Lỗi Thêm: " + e.getMessage()); 
        } finally {
            data.closeConnection(); 
        }
    }

    // XÓA PHIẾU NHẬP
    public void xoaPNH(int ma) { 
        Connection cnn = data.getConnection();
        try {
            String sql = "DELETE FROM phieunhaphang WHERE id = ?";
            PreparedStatement pst = cnn.prepareStatement(sql);
            pst.setInt(1, ma);
            
            pst.executeUpdate();
            
        } catch (SQLException e) { 
            System.out.println("Lỗi Xóa: " + e.getMessage()); 
        } finally {
            data.closeConnection(); 
        }
    }

    // SỬA PHIẾU NHẬP
    public void suaPNH(PhieuNhapHangDTO pn) {
        Connection cnn = data.getConnection();
        try {
            String sql = "UPDATE phieunhaphang SET "
                    + "nhanVienId = ?, "
                    + "tongTien = ?, "
                    + "nhaCungCapId = ?, "
                    + "ngayNhap = ? "
                    + "WHERE id = ?";
            
            PreparedStatement pst = cnn.prepareStatement(sql);
            
            pst.setInt(1, pn.getMaNV());
            pst.setDouble(2, pn.getTongTien());
            pst.setInt(3, pn.getMaNCC());
            pst.setDate(4, new java.sql.Date(pn.getNgayNhap().getTime()));
            pst.setInt(5, pn.getMaPNH()); // Tham số của WHERE
            
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
            String querry = "SELECT MAX(maPNH) FROM phieunhaphang";
            Statement st =cnn.createStatement();
            ResultSet rs = st.executeQuery(querry);
            if (rs.next()) {
                nextID = rs.getInt(1) + 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally { data.closeConnection(); }
        return nextID;
    }
}