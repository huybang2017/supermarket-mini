package SieuThiMiniDAO;

import DTO.ChuongTrinhKhuyenMaiHDDTO;
import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;

public class ChuongTrinhKhuyenMaiHDDAO {
    MyConnection data = new MyConnection();

    public ArrayList<ChuongTrinhKhuyenMaiHDDTO> docDSKMHD(int ctkm_id) {
        ArrayList<ChuongTrinhKhuyenMaiHDDTO> dskmhd = new ArrayList<>();
        Connection cnn = data.getConnection();
        try {
            String qry = "SELECT * FROM chuongtrinhkhuyenmaihd WHERE chuongTrinhKhuyenMaiId = " + ctkm_id;
            Statement st = cnn.createStatement();
            ResultSet rs = st.executeQuery(qry);
            while (rs.next()) {
                ChuongTrinhKhuyenMaiHDDTO kmhd = new ChuongTrinhKhuyenMaiHDDTO();
                kmhd.setChuongTrinhKhuyenMaiId(rs.getInt("chuongTrinhKhuyenMaiId"));
                kmhd.setSoTienHd(rs.getInt("soTienHd"));
                kmhd.setGiaTriGiam(rs.getInt("giaTriGiam"));
                dskmhd.add(kmhd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            data.closeConnection();
        }
        return dskmhd;
    }

    public ArrayList<ChuongTrinhKhuyenMaiHDDTO> docTatCa() {
        ArrayList<ChuongTrinhKhuyenMaiHDDTO> dskmhd = new ArrayList<>();
        Connection cnn = data.getConnection();
        try {
            String qry = "SELECT * FROM chuongtrinhkhuyenmaihd";
            Statement st = cnn.createStatement();
            ResultSet rs = st.executeQuery(qry);
            while (rs.next()) {
                ChuongTrinhKhuyenMaiHDDTO kmhd = new ChuongTrinhKhuyenMaiHDDTO();
                kmhd.setChuongTrinhKhuyenMaiId(rs.getInt("chuongTrinhKhuyenMaiId"));
                kmhd.setSoTienHd(rs.getInt("soTienHd"));
                kmhd.setGiaTriGiam(rs.getInt("giaTriGiam"));
                dskmhd.add(kmhd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            data.closeConnection();
        }
        return dskmhd;
    }

    public void themKMHD(ChuongTrinhKhuyenMaiHDDTO kmhd) {
        Connection cnn = data.getConnection();
        try {
            String qry = "INSERT INTO chuongtrinhkhuyenmaihd VALUES ("
                    + kmhd.getChuongTrinhKhuyenMaiId() + ","
                    + kmhd.getSoTienHd() + ","
                    + kmhd.getGiaTriGiam() + ")";
            Statement st = cnn.createStatement();
            st.executeUpdate(qry);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Lỗi thêm điều kiện hóa đơn: " + ex.getMessage());
        } finally {
            data.closeConnection();
        }
    }

    public void xoaKMHD(int ctkm_id, int soTienHd) {
        Connection cnn = data.getConnection();
        try {
            String qry = "DELETE FROM chuongtrinhkhuyenmaihd WHERE chuongTrinhKhuyenMaiId = " + ctkm_id + " AND soTienHd = " + soTienHd;
            Statement st = cnn.createStatement();
            st.executeUpdate(qry);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi xóa: " + e.getMessage());
        } finally {
            data.closeConnection();
        }
    }

    public void xoaTheoKM(int ctkm_id) {
        Connection cnn = data.getConnection();
        try {
            String qry = "DELETE FROM chuongtrinhkhuyenmaihd WHERE chuongTrinhKhuyenMaiId = " + ctkm_id;
            Statement st = cnn.createStatement();
            st.executeUpdate(qry);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi xóa: " + e.getMessage());
        } finally {
            data.closeConnection();
        }
    }

    public void suaKMHD(ChuongTrinhKhuyenMaiHDDTO kmhd) {
        Connection cnn = data.getConnection();
        try {
            String qry = "UPDATE chuongtrinhkhuyenmaihd SET "
                    + "giaTriGiam = " + kmhd.getGiaTriGiam() + " "
                    + "WHERE chuongTrinhKhuyenMaiId = " + kmhd.getChuongTrinhKhuyenMaiId() 
                    + " AND soTienHd = " + kmhd.getSoTienHd();
            Statement st = cnn.createStatement();
            st.executeUpdate(qry);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi sửa: " + e.getMessage());
        } finally {
            data.closeConnection();
        }
    }
}
