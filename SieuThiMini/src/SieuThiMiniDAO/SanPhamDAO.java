package SieuThiMiniDAO;
import DTO.SanPhamDTO;
import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
public class SanPhamDAO {
    MyConnection data = new MyConnection();
    public ArrayList<SanPhamDTO> docDSSP() {
        ArrayList<SanPhamDTO> dssp = new ArrayList<>();
        Connection cnn = data.getConnection(); // MỞ KẾT NỐI
        try {
            String qry = "SELECT * FROM sanpham";
            Statement st = cnn.createStatement();
            ResultSet rs = st.executeQuery(qry);
            while (rs.next()) {
                SanPhamDTO sp = new SanPhamDTO();
                sp.setMasanpham(rs.getString("id"));
                sp.setMaLoai(rs.getString("loaiSanPhamId"));
                sp.setMaHang(rs.getString("hangId"));
                sp.setTensanpham(rs.getString("ten"));
                sp.setSoluong(rs.getInt("soLuong"));
                sp.setDongia(rs.getInt("donGia"));
                sp.setDonvitinh(rs.getString("donViTinh"));
                dssp.add(sp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            data.closeConnection(); // ĐÓNG KẾT NỐI
        }
        return dssp;
    }

    public void themSP(SanPhamDTO sp) {
        Connection cnn = data.getConnection();
        try {
            // Bao quanh các giá trị String bằng dấu nháy đơn ''
            String qry = "INSERT INTO sanpham VALUES ('"
                    + sp.getMasanpham() + "','"
                    + sp.getMaLoai() + "','"
                    + sp.getMaHang() + "',N'"
                    + sp.getTensanpham() + "','"
                    + sp.getSoluong() + "','"
                    + sp.getDongia() + "',N'"
                    + sp.getDonvitinh() + "')";
            Statement st = cnn.createStatement();
            st.executeUpdate(qry);
            JOptionPane.showMessageDialog(null, "Thêm thành công!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Lỗi thêm: " + ex.getMessage());
        }finally {
            data.closeConnection(); // ĐÓNG KẾT NỐI
        }
    }

    public void xoaSP(String maSP) {
        Connection cnn = data.getConnection();

        try {
            String qry = "DELETE FROM sanpham WHERE id = '" + maSP + "'";
            Statement st = cnn.createStatement();
            st.executeUpdate(qry);
            JOptionPane.showMessageDialog(null, "Xóa thành công!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi xóa: " + e.getMessage());
        }finally {
            data.closeConnection(); // ĐÓNG KẾT NỐI
        }
    }

    public void suaSP(SanPhamDTO sp) {
        Connection cnn = data.getConnection();
        try {
            String qry = "UPDATE sanpham SET "
                    + "loaiSanPhamId = '" + sp.getMaLoai() + "', "
                    + "hangId = '" + sp.getMaHang() + "', "
                    + "ten = N'" + sp.getTensanpham() + "', "
                    + "soLuong = " + sp.getSoluong() + ", "
                    + "donGia = " + sp.getDongia() + ", "
                    + "donViTinh = N'" + sp.getDonvitinh() + "' "
                    + "WHERE id = '" + sp.getMasanpham() + "'";
            Statement st = cnn.createStatement();
            st.executeUpdate(qry);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi sửa: " + e.getMessage());
        }finally {
            data.closeConnection(); // ĐÓNG KẾT NỐI
        }
    }
}

