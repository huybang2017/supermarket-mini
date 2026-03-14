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
                sp.setMasanpham(rs.getInt("id"));
                sp.setMaLoai(rs.getInt("loaiSanPhamId"));
                sp.setMaHang(rs.getInt("hangId"));
                sp.setTensanpham(rs.getString("ten"));
                sp.setSoluong(rs.getInt("soLuong"));
                sp.setDongia(rs.getInt("donGia"));
                sp.setGiaNhap(rs.getLong("giaNhap"));
                sp.setLoiNhuan(rs.getDouble("loiNhuan"));
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
    public void xoaSP(int maSP) {
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

    // --- 1. THÊM SẢN PHẨM ---
    public void themSP(SanPhamDTO sp) {
        Connection cnn = data.getConnection();
        try {
            // Dùng dấu ? đại diện cho các giá trị cần truyền vào
            String sql = "INSERT INTO sanpham (id, loaiSanPhamId, hangId, ten, soLuong, donGia, giaNhap, loiNhuan, donViTinh) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
            PreparedStatement pst = cnn.prepareStatement(sql);
            
            // Truyền giá trị vào các dấu ? theo đúng thứ tự (từ 1 đến 9)
            pst.setInt(1, sp.getMasanpham());
            pst.setInt(2, sp.getMaLoai());
            pst.setInt(3, sp.getMaHang());
            pst.setString(4, sp.getTensanpham()); // Tự động xử lý chữ có dấu nháy như Lay's
            pst.setInt(5, sp.getSoluong());
            pst.setLong(6, sp.getDongia());
            pst.setLong(7, sp.getGiaNhap());
            pst.setDouble(8, sp.getLoiNhuan());
            pst.setString(9, sp.getDonvitinh());

            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Thêm thành công!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Lỗi thêm: " + ex.getMessage());
        } finally {
            data.closeConnection(); 
        }
    }

    // --- 2. SỬA SẢN PHẨM ---
    public void suaSP(SanPhamDTO sp) {
        Connection cnn = data.getConnection();
        try {
            String sql = "UPDATE sanpham SET "
                    + "loaiSanPhamId = ?, "
                    + "hangId = ?, "
                    + "ten = ?, "
                    + "soLuong = ?, "
                    + "donGia = ?, "
                    + "giaNhap = ?, "
                    + "loiNhuan = ?, "
                    + "donViTinh = ? "
                    + "WHERE id = ?";
            
            PreparedStatement pst = cnn.prepareStatement(sql);
            
            pst.setInt(1, sp.getMaLoai());
            pst.setInt(2, sp.getMaHang());
            pst.setString(3, sp.getTensanpham());   
            pst.setInt(4, sp.getSoluong());
            pst.setLong(5, sp.getDongia());
            pst.setLong(6, sp.getGiaNhap());
            pst.setDouble(7, sp.getLoiNhuan());
            pst.setString(8, sp.getDonvitinh());
            pst.setInt(9, sp.getMasanpham()); 

            pst.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi sửa: " + e.getMessage());
        } finally {
            data.closeConnection(); 
        }
    }

    // --- 3. CẬP NHẬT SỐ LƯỢNG ---
    public void capNhatSoLuong(int masp, int soLuongMoi) {
        Connection cnn = data.getConnection();
        try {
            String sql = "UPDATE sanpham SET soLuong = ? WHERE id = ?"; 
            
            PreparedStatement pst = cnn.prepareStatement(sql);
            pst.setInt(1, soLuongMoi);
            pst.setInt(2, masp);
            
            pst.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi cập nhật số lượng: " + e.getMessage());
        } finally {
            data.closeConnection();
        }
    }
}

