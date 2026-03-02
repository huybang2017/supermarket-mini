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
            String qry = "SELECT * FROM phieunhaphang";
            Statement st = cnn.createStatement();
            ResultSet rs = st.executeQuery(qry);
            while (rs.next()) {
                PhieuNhapHangDTO pn = new PhieuNhapHangDTO();
                pn.setMaPNH(rs.getString(1));
                pn.setMaNCC(rs.getString(2));
                pn.setNgayNhap((rs.getDate(3)));
                pn.setTongTien(rs.getInt(4));
                dspn.add(pn);
            }
        } catch (SQLException e) { System.out.println("Lỗi Đọc"); }
        return dspn;
    }

    public void themPNH(PhieuNhapHangDTO pn) {
        Connection cnn = data.getConnection();
        try {
            String qry = "INSERT INTO phieunhaphang VALUES ("
                    + "'" + pn.getMaPNH() + "',"
                    + "'" + pn.getMaNCC() + "',"
                    + "'" + pn.getNgayNhap() + "',"
                    + pn.getTongTien() + ")";
            Statement st = cnn.createStatement();
            st.executeUpdate(qry);
        } catch (SQLException e) { System.out.println("Lỗi Thêm"); }
    }

    public void xoaPNH(String ma) {
        Connection cnn = data.getConnection();
        try {
            String qry = "DELETE FROM nhacungcap WHERE id='" + ma + "'";
            Statement st = cnn.createStatement();
            st.executeUpdate(qry);
        } catch (SQLException e) { System.out.println("Sai chỗ xóa"); }
    }

    public void suaPNH(PhieuNhapHangDTO pn) {
        Connection cnn = data.getConnection();
        try {
            String qry = "UPDATE nhacungcap SET "
                    + "nhanVienId = '" + pn.getMaNV() + "', "
                    + "tongTien '" + pn.getTongTien() + "', "
                    + "nhaCungCapId = '" + pn.getMaNCC() + "', "
                    + "ngayNhap = '" + pn.getNgayNhap() + "', "
                    + "WHERE id = '" + pn.getMaPNH() + "'";
            Statement st = cnn.createStatement();
            st.executeUpdate(qry);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi sửa: " + e.getMessage());
        }finally {
            data.closeConnection(); 
        }
    }
}