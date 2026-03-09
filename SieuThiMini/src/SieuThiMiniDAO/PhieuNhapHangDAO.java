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
                pn.setMaPNH(rs.getInt(1)); 
                pn.setMaNV(rs.getInt(2));  
                pn.setTongTien(rs.getDouble(3)); 
                pn.setMaNCC(rs.getInt(4)); 
                pn.setNgayNhap(rs.getDate(5));
                dspn.add(pn);
            }
        } catch (SQLException e) { System.out.println("Lỗi Đọc: " + e.getMessage()); }
        return dspn;
    }

    public void themPNH(PhieuNhapHangDTO pn) {
        Connection cnn = data.getConnection();
        try {
            String qry = "INSERT INTO phieunhaphang VALUES ("
                    + pn.getMaPNH() + ","
                    + pn.getMaNV() + ","
                    + pn.getTongTien() + ","
                    + pn.getMaNCC() + ","
                    + "'" + pn.getNgayNhap() + "')";
            Statement st = cnn.createStatement();
            st.executeUpdate(qry);
        } catch (SQLException e) { System.out.println("Lỗi Thêm: " + e.getMessage()); }
        finally { data.closeConnection(); }
    }

    public void xoaPNH(int ma) { 
        Connection cnn = data.getConnection();
        try {
            String qry = "DELETE FROM phieunhaphang WHERE id=" + ma;
            Statement st = cnn.createStatement();
            st.executeUpdate(qry);
        } catch (SQLException e) { System.out.println("Sai chỗ xóa"); }
        finally { data.closeConnection(); }
    }

    public void suaPNH(PhieuNhapHangDTO pn) {
        Connection cnn = data.getConnection();
        try {
            String qry = "UPDATE phieunhaphang SET "
                    + "nhanVienId = " + pn.getMaNV() + ", "
                    + "tongTien = " + pn.getTongTien() + ", "
                    + "nhaCungCapId = " + pn.getMaNCC() + ", "
                    + "ngayNhap = '" + pn.getNgayNhap() + "' "
                    + "WHERE id = " + pn.getMaPNH();
            Statement st = cnn.createStatement();
            st.executeUpdate(qry);
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