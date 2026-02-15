package SieuThiMiniDAO;

import DTO.HangSanXuatDTO;
import DTO.LoaiSanPhamDTO;

import javax.swing.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
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
                hxs.setMaHang(rs.getString("id"));
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
            JOptionPane.showMessageDialog(null, "Thêm thành công!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Lỗi thêm: " + ex.getMessage());
        }finally {
            data.closeConnection(); // ĐÓNG KẾT NỐI
        }
    }

    public void xoaHSX(String maHang) {
        Connection cnn = data.getConnection();

        try {
            String qry = "DELETE FROM hangsanxuat WHERE id = '" + maHang + "'";
            Statement st = cnn.createStatement();
            st.executeUpdate(qry);
            JOptionPane.showMessageDialog(null, "Xóa thành công!");
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
                    + "diaChia = '" + hsx.getDiaChi() + "', "
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

}
