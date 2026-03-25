package SieuThiMiniDAO;

import DTO.KhachHangDTO;
import javax.swing.*;

import java.sql.*;
import java.util.ArrayList;

public class KhachHangDAO {
    MyConnection data = new MyConnection();

    // 1. Đọc danh sách khách hàng
    public ArrayList<KhachHangDTO> docDSKH() {
        ArrayList<KhachHangDTO> dskh = new ArrayList<>();
        Connection cnn = data.getConnection();
        try {
            String qry = "SELECT * FROM khachhang";
            Statement st = cnn.createStatement();
            ResultSet rs = st.executeQuery(qry);
            while (rs.next()) {
                KhachHangDTO kh = new KhachHangDTO();
                kh.setMaKH(rs.getInt("id"));
                kh.setHoKH(rs.getString("ho"));
                kh.setTenKH(rs.getString("ten"));
                kh.setSdt(rs.getString("phone"));
                kh.setDiaChi(rs.getString("diaChi"));
                dskh.add(kh);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            data.closeConnection();
        }
        return dskh;
    }

    // 2. Thêm khách hàng
    public void themKH(KhachHangDTO kh) {
        Connection cnn = data.getConnection();
        try {
            String qry = "INSERT INTO khachhang (ho, ten, phone, diaChi) VALUES ('"
                    + kh.getHoKH() + "','"
                    + kh.getTenKH() + "','"
                    + kh.getSdt() + "','"
                    + kh.getDiaChi() + "')";
            Statement st = cnn.createStatement();
            st.executeUpdate(qry);
            JOptionPane.showMessageDialog(null, "Thêm khách hàng thành công!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Lỗi thêm KH: " + ex.getMessage());
        } finally {
            data.closeConnection();
        }
    }

    // 3. Xóa khách hàng
    public void xoaKH(int makh) {
        Connection cnn = data.getConnection();
        try {
            String qry = "DELETE FROM khachhang WHERE id = '" + makh + "'";
            Statement st = cnn.createStatement();
            st.executeUpdate(qry);
            JOptionPane.showMessageDialog(null, "Xóa khách hàng thành công!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi xóa KH: " + e.getMessage());
        } finally {
            data.closeConnection();
        }
    }

    // 4. Sửa khách hàng
    public void suaKH(KhachHangDTO kh) {
        Connection cnn = data.getConnection();
        try {
            String qry = "UPDATE khachhang SET "
                    + "ho = '" + kh.getHoKH() + "', "
                    + "ten = '" + kh.getTenKH() + "', "
                    + "phone = '" + kh.getSdt() + "', "
                    + "diaChi = '" + kh.getDiaChi() + "' "
                    + "WHERE id = '" + kh.getMaKH() + "'";
            Statement st = cnn.createStatement();
            st.executeUpdate(qry);
            JOptionPane.showMessageDialog(null, "Sửa thông tin khách hàng thành công!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi sửa KH: " + e.getMessage());
        } finally {
            data.closeConnection();
        }
    }

    public boolean importExcel(KhachHangDTO kh) {
        String sql = "INSERT INTO khachhang (id, ho, ten, phone, diaChi) VALUES (?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE ho = VALUES(ho), ten = VALUES(ten), phone = VALUES(phone), diaChi = VALUES(diaChi)";
        try (Connection cnn = data.getConnection();
             PreparedStatement ps = cnn.prepareStatement(sql)) {
            ps.setInt(1, kh.getMaKH());
            ps.setString(2, kh.getHoKH());
            ps.setString(3, kh.getTenKH());
            ps.setString(4, kh.getSdt());
            ps.setString(5, kh.getDiaChi());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi Import KhachHang: " + e.getMessage());
            return false;
        }
    }

    public KhachHangDTO timKhachHang(String keyword){
        Connection cnn = data.getConnection();
        try {
            String qry = "SELECT * FROM khachhang WHERE id = ? OR ho LIKE ? OR ten LIKE ? OR phone LIKE ?";
            PreparedStatement pst = cnn.prepareStatement(qry);
            pst.setString(1,keyword);
            pst.setString(2, "%" + keyword + "%");
            pst.setString(3, "%" + keyword + "%");
            pst.setString(4, "%" + keyword + "%");
            ResultSet rs = pst.executeQuery();
            while (rs.next()){
                KhachHangDTO kh = new KhachHangDTO();
            kh.setMaKH(rs.getInt("id")); 
            kh.setHoKH(rs.getString("ho")); 
            kh.setTenKH(rs.getString("ten")); 
            kh.setSdt(rs.getString("phone")); 
            kh.setDiaChi(rs.getString("diachi")); 
            return kh;
        }
    } catch (SQLException e) {
        System.err.println("Lỗi tìm kiếm Khách Hàng: " + e.getMessage());
    } finally {
        try {
            if (cnn != null) cnn.close();
        } catch (SQLException e) {
            System.err.println("Lỗi đóng kết nối: " + e.getMessage());
        }
    }
    return null;
}
}
