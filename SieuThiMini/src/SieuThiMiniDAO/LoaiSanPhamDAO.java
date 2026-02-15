package SieuThiMiniDAO;

import DTO.LoaiSanPhamDTO;
import DTO.SanPhamDTO;

import javax.swing.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class LoaiSanPhamDAO {
    MyConnection data = new MyConnection();
    public ArrayList<LoaiSanPhamDTO> docDSLSP(){
        ArrayList<LoaiSanPhamDTO> dslsp = new ArrayList<>();
        Connection cnn = data.getConnection();
        try {
            String qry = "SELECT * FROM loaisanpham";
            Statement st = cnn.createStatement();
            ResultSet rs = st.executeQuery(qry);
            while (rs.next()) {
                LoaiSanPhamDTO lsp = new LoaiSanPhamDTO();
                lsp.setMaLoai(rs.getString("id"));
                lsp.setTenLoai(rs.getString("name"));
                lsp.setHang(rs.getString("hangId"));
                dslsp.add(lsp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            data.closeConnection(); // ĐÓNG KẾT NỐI
        }
        return dslsp;
    }
    public void themLSP(LoaiSanPhamDTO lsp) {
        Connection cnn = data.getConnection();
        try {
            String qry = "INSERT INTO loaisanpham VALUES ('"
                    + lsp.getMaLoai() + "','"
                    + lsp.getTenLoai() + "','"
                    + lsp.getHang() + "')";
            Statement st = cnn.createStatement();
            st.executeUpdate(qry);
            JOptionPane.showMessageDialog(null, "Thêm thành công!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Lỗi thêm: " + ex.getMessage());
        }finally {
            data.closeConnection(); // ĐÓNG KẾT NỐI
        }
    }

    public void xoaLSP(String maLoai) {
        Connection cnn = data.getConnection();

        try {
            String qry = "DELETE FROM loaisanpham WHERE id = '" + maLoai + "'";
            Statement st = cnn.createStatement();
            st.executeUpdate(qry);
            JOptionPane.showMessageDialog(null, "Xóa thành công!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi xóa: " + e.getMessage());
        }finally {
            data.closeConnection(); // ĐÓNG KẾT NỐI
        }
    }

    public void suaLSP(LoaiSanPhamDTO lsp) {
        Connection cnn = data.getConnection();
        try {
            String qry = "UPDATE loaisanpham SET "
                    + "name = '" + lsp.getTenLoai() + "', "
                    + "hangId = '" + lsp.getHang() + "', "
                    + "WHERE id = '" + lsp.getMaLoai() + "'";
            Statement st = cnn.createStatement();
            st.executeUpdate(qry);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi sửa: " + e.getMessage());
        }finally {
            data.closeConnection(); // ĐÓNG KẾT NỐI
        }
    }

}
