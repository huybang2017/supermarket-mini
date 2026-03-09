package SieuThiMiniDAO;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import DTO.NhaCungCapDTO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
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
                ncc.setMaNCC(rs.getString(1));
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
            String qry = "INSERT INTO nhacungcap VALUES ('" + ncc.getMaNCC() + "','" + ncc.getTenNCC() + "','" + ncc.getDiaChi() + "','" + ncc.getSdt() + "')";
            Statement st = cnn.createStatement();
            st.executeUpdate(qry);
        } catch (SQLException e) { System.out.println("Lỗi khúc thêm ấy"); }
        finally { data.closeConnection(); }
    }

    public void xoaNCC(String ma) {
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
}