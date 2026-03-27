package SieuThiMiniDAO;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import DTO.NhaCungCapDTO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class NhaCungCapDAO {
    MyConnection data = new MyConnection();


    public ArrayList<NhaCungCapDTO> docDSNCC() {
        ArrayList<NhaCungCapDTO> dsncc = new ArrayList<>();
        Connection cnn = data.getConnection();

        try {
            String qry = "SELECT * FROM nhacungcap";
            Statement st = cnn.createStatement();
            ResultSet rs = st.executeQuery(qry);
            while (rs.next()) {
                NhaCungCapDTO ncc = new NhaCungCapDTO();
                ncc.setMaNCC(rs.getInt(1));
                ncc.setTenNCC(rs.getString(2));
                ncc.setDiaChi(rs.getString(3));
                ncc.setSdt(rs.getString(4));
                dsncc.add(ncc);
            }
        } catch (SQLException e) { System.out.println("Lỗi dòng 2 rồi em ơi"); }
        return dsncc;
    }

    public void themNCC(NhaCungCapDTO ncc) {
        Connection cnn = data.getConnection();
        try {
            // Đã sửa: Bỏ cột id ra khỏi câu lệnh INSERT để CSDL tự tăng
            String qry = "INSERT INTO nhacungcap (ten, diaChi, phone) VALUES ('" 
                       + ncc.getTenNCC() + "','" + ncc.getDiaChi() + "','" + ncc.getSdt() + "')";
            Statement st = cnn.createStatement();
            st.executeUpdate(qry);
        } catch (SQLException e) { 
            System.out.println("Lỗi khúc thêm: " + e.getMessage()); 
        } finally { 
            data.closeConnection(); 
        }
    }

    public void xoaNCC(int ma) {
        Connection cnn = data.getConnection();
        try {
            String qry = "DELETE FROM nhacungcap WHERE id='" + ma + "'";
            Statement st = cnn.createStatement();
            st.executeUpdate(qry);
        } catch (SQLException e) { System.out.println("Sai chỗ xóa"); }
        finally { data.closeConnection(); }
    }


    public void suaNCC(NhaCungCapDTO ncc) {
        Connection cnn = data.getConnection();
        try {
            // Thêm dấu = ở diaChi và xóa dấu phẩy trước WHERE
            String qry = "UPDATE nhacungcap SET "
                    + "ten = '" + ncc.getTenNCC() + "', "
                    + "diaChi = '" + ncc.getDiaChi() + "', "
                    + "phone = '" + ncc.getSdt() + "' "
                    + "WHERE id = '" + ncc.getMaNCC() + "'";
            Statement st = cnn.createStatement();
            st.executeUpdate(qry);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi sửa: " + e.getMessage());
        } finally {
            data.closeConnection(); 
        }
    }

    public boolean importExcel(NhaCungCapDTO ncc) {
        String sql = "INSERT INTO nhacungcap (id, ten, diaChi, phone) VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE ten = VALUES(ten), diaChi = VALUES(diaChi), phone = VALUES(phone)";
        try (Connection cnn = data.getConnection();
             PreparedStatement ps = cnn.prepareStatement(sql)) {
            ps.setInt(1, ncc.getMaNCC());
            ps.setString(2, ncc.getTenNCC());
            ps.setString(3, ncc.getDiaChi());
            ps.setString(4, ncc.getSdt());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi Import NhaCungCap: " + e.getMessage());
            return false;
        }
    }

    public ArrayList<NhaCungCapDTO> timNhaCungCap(String keyword) {
        ArrayList<NhaCungCapDTO> ketQua = new ArrayList<>();
        String sql = "SELECT * FROM nhacungcap WHERE CAST(id AS CHAR) LIKE ? OR LOWER(ten) LIKE ? OR LOWER(diaChi) LIKE ? OR phone LIKE ?";
        try (Connection cnn = data.getConnection();
             PreparedStatement ps = cnn.prepareStatement(sql)) {
    
            String searchKw = "%" + keyword.toLowerCase() + "%";
            ps.setString(1, searchKw);
            ps.setString(2, searchKw); 
            ps.setString(3, searchKw);
            ps.setString(4, searchKw); 
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                NhaCungCapDTO ncc = new NhaCungCapDTO();
                ncc.setMaNCC(rs.getInt("id")); 
                ncc.setTenNCC(rs.getString("ten")); 
                ncc.setDiaChi(rs.getString("diaChi"));
                ncc.setSdt(rs.getString("phone"));
                ketQua.add(ncc);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi tìm kiếm Nhà Cung Cấp: " + e.getMessage());
        }
        return ketQua; 
    }
}
