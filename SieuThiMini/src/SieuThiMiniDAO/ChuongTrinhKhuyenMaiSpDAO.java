package SieuThiMiniDAO;

import DTO.ChuongTrinhKhuyenMaiSpDTO;
import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;

public class ChuongTrinhKhuyenMaiSpDAO {
    MyConnection data = new MyConnection();

    public ArrayList<ChuongTrinhKhuyenMaiSpDTO> docDSKMSP(int ctkm_id) {
        ArrayList<ChuongTrinhKhuyenMaiSpDTO> dskmsp = new ArrayList<>();
        Connection cnn = data.getConnection();
        try {
            String qry = "SELECT * FROM chuongtrinhkhuyenmaisp WHERE chuongTrinhKhuyenMaiId = " + ctkm_id;
            Statement st = cnn.createStatement();
            ResultSet rs = st.executeQuery(qry);
            while (rs.next()) {
                ChuongTrinhKhuyenMaiSpDTO kmsp = new ChuongTrinhKhuyenMaiSpDTO();
                kmsp.setChuongTrinhKhuyenMaiId(rs.getInt("chuongTrinhKhuyenMaiId"));
                kmsp.setSanPhamId(rs.getInt("sanPhamId"));
                kmsp.setGiaTriGiam(rs.getInt("giaTriGiam"));
                dskmsp.add(kmsp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            data.closeConnection();
        }
        return dskmsp;
    }

    public ArrayList<ChuongTrinhKhuyenMaiSpDTO> docTatCa() {
        ArrayList<ChuongTrinhKhuyenMaiSpDTO> dskmsp = new ArrayList<>();
        Connection cnn = data.getConnection();
        try {
            String qry = "SELECT * FROM chuongtrinhkhuyenmaisp";
            Statement st = cnn.createStatement();
            ResultSet rs = st.executeQuery(qry);
            while (rs.next()) {
                ChuongTrinhKhuyenMaiSpDTO kmsp = new ChuongTrinhKhuyenMaiSpDTO();
                kmsp.setChuongTrinhKhuyenMaiId(rs.getInt("chuongTrinhKhuyenMaiId"));
                kmsp.setSanPhamId(rs.getInt("sanPhamId"));
                kmsp.setGiaTriGiam(rs.getInt("giaTriGiam"));
                dskmsp.add(kmsp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            data.closeConnection();
        }
        return dskmsp;
    }

    public void themKMSP(ChuongTrinhKhuyenMaiSpDTO kmsp) {
        Connection cnn = data.getConnection();
        try {
            String qry = "INSERT INTO chuongtrinhkhuyenmaisp VALUES ("
                    + kmsp.getChuongTrinhKhuyenMaiId() + ","
                    + kmsp.getSanPhamId() + ","
                    + kmsp.getGiaTriGiam() + ")";
            Statement st = cnn.createStatement();
            st.executeUpdate(qry);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Lỗi thêm sản phẩm vào KM: " + ex.getMessage());
        } finally {
            data.closeConnection();
        }
    }

    public void xoaKMSP(int ctkm_id, int sp_id) {
        Connection cnn = data.getConnection();
        try {
            String qry = "DELETE FROM chuongtrinhkhuyenmaisp WHERE chuongTrinhKhuyenMaiId = " + ctkm_id + " AND sanPhamId = " + sp_id;
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
            String qry = "DELETE FROM chuongtrinhkhuyenmaisp WHERE chuongTrinhKhuyenMaiId = " + ctkm_id;
            Statement st = cnn.createStatement();
            st.executeUpdate(qry);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi xóa: " + e.getMessage());
        } finally {
            data.closeConnection();
        }
    }

    public void suaKMSP(ChuongTrinhKhuyenMaiSpDTO kmsp) {
        Connection cnn = data.getConnection();
        try {
            String qry = "UPDATE chuongtrinhkhuyenmaisp SET "
                    + "giaTriGiam = " + kmsp.getGiaTriGiam() + " "
                    + "WHERE chuongTrinhKhuyenMaiId = " + kmsp.getChuongTrinhKhuyenMaiId() 
                    + " AND sanPhamId = " + kmsp.getSanPhamId();
            Statement st = cnn.createStatement();
            st.executeUpdate(qry);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi sửa: " + e.getMessage());
        } finally {
            data.closeConnection();
        }
    }
}
