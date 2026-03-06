package SieuThiMiniDAO;

import DTO.ChuongTrinhKhuyenMaiDTO;
import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;

public class ChuongTrinhKhuyenMaiDAO {
    MyConnection data = new MyConnection();

    public ArrayList<ChuongTrinhKhuyenMaiDTO> docDSKM() {
        ArrayList<ChuongTrinhKhuyenMaiDTO> dskm = new ArrayList<>();
        Connection cnn = data.getConnection();
        try {
            String qry = "SELECT * FROM chuongtrinhkhuyenmai";
            Statement st = cnn.createStatement();
            ResultSet rs = st.executeQuery(qry);
            while (rs.next()) {
                ChuongTrinhKhuyenMaiDTO km = new ChuongTrinhKhuyenMaiDTO();
                km.setId(rs.getInt("id"));
                km.setTen(rs.getString("ten"));
                km.setGhiChu(rs.getString("ghiChu"));
                km.setNgayBatDau(rs.getDate("ngayBatDau"));
                km.setNgayKetThuc(rs.getDate("ngayKetThuc"));
                km.setTrangThai(rs.getBoolean("trangThai"));
                dskm.add(km);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            data.closeConnection();
        }
        return dskm;
    }

    public void themKM(ChuongTrinhKhuyenMaiDTO km) {
        Connection cnn = data.getConnection();
        try {
            String qry = "INSERT INTO chuongtrinhkhuyenmai (ten, ghiChu, ngayBatDau, ngayKetThuc, trangThai) VALUES (N'"
                    + km.getTen() + "',N'"
                    + km.getGhiChu() + "','"
                    + new java.sql.Date(km.getNgayBatDau().getTime()) + "','"
                    + new java.sql.Date(km.getNgayKetThuc().getTime()) + "',"
                    + (km.isTrangThai() ? 1 : 0) + ")";
            Statement st = cnn.createStatement();
            st.executeUpdate(qry);
            JOptionPane.showMessageDialog(null, "Thêm chương trình khuyến mãi thành công!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Lỗi thêm: " + ex.getMessage());
        } finally {
            data.closeConnection();
        }
    }

    public void xoaKM(int id) {
        Connection cnn = data.getConnection();
        try {
            String qry = "DELETE FROM chuongtrinhkhuyenmai WHERE id = " + id;
            Statement st = cnn.createStatement();
            st.executeUpdate(qry);
            JOptionPane.showMessageDialog(null, "Xóa chương trình khuyến mãi thành công!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi xóa: " + e.getMessage());
        } finally {
            data.closeConnection();
        }
    }

    public void suaKM(ChuongTrinhKhuyenMaiDTO km) {
        Connection cnn = data.getConnection();
        try {
            String qry = "UPDATE chuongtrinhkhuyenmai SET "
                    + "ten = N'" + km.getTen() + "', "
                    + "ghiChu = N'" + km.getGhiChu() + "', "
                    + "ngayBatDau = '" + new java.sql.Date(km.getNgayBatDau().getTime()) + "', "
                    + "ngayKetThuc = '" + new java.sql.Date(km.getNgayKetThuc().getTime()) + "', "
                    + "trangThai = " + (km.isTrangThai() ? 1 : 0) + " "
                    + "WHERE id = " + km.getId();
            Statement st = cnn.createStatement();
            st.executeUpdate(qry);
            JOptionPane.showMessageDialog(null, "Cập nhật chương trình khuyến mãi thành công!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi sửa: " + e.getMessage());
        } finally {
            data.closeConnection();
        }
    }
}
