package SieuThiMiniDAO;

import DTO.NhanVienDTO;
import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;

public class NhanVienDAO { 
    MyConnection data = new MyConnection();

    // 1. Đọc danh sách nhân viên
    public ArrayList<NhanVienDTO> docDSNV() {
        ArrayList<NhanVienDTO> dsnv = new ArrayList<>();
        Connection cnn = data.getConnection(); // MỞ KẾT NỐI
        try {
            String qry = "SELECT * FROM nhanvien";
            Statement st = cnn.createStatement();
            ResultSet rs = st.executeQuery(qry);
            while (rs.next()) {
                NhanVienDTO nv = new NhanVienDTO();
                nv.setMaNV(rs.getInt("id"));
                nv.setHoNV(rs.getString("ho"));
                nv.setTenNV(rs.getString("ten"));
                nv.setSdt(rs.getString("phone"));
                nv.setDiaChi(rs.getString("diaChi"));
                nv.setNgaySinh(rs.getDate("ngaySinh"));
                nv.setLuong(rs.getDouble("luong"));
                dsnv.add(nv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            data.closeConnection(); // ĐÓNG KẾT NỐI
        }
        return dsnv;
    }

    // 2. Thêm nhân viên
    public void themNV(NhanVienDTO nv) {
        Connection cnn = data.getConnection();
        try {
            // Đặt tiền tố N ở trước Họ, Tên, Địa chỉ để lưu tiếng Việt có dấu
            String qry = "INSERT INTO nhanvien VALUES ('"
                    + nv.getMaNV() + "', N'"
                    + nv.getHoNV() + "', N'"
                    + nv.getTenNV() + "', '"
                    + nv.getSdt() + "', N'"
                    + nv.getDiaChi() + "', '"
                    + nv.getNgaySinh() + "', '"
                    + nv.getLuong() + "')";
            Statement st = cnn.createStatement();
            st.executeUpdate(qry);
            JOptionPane.showMessageDialog(null, "Thêm nhân viên thành công!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Lỗi thêm NV: " + ex.getMessage());
        } finally {
            data.closeConnection(); // ĐÓNG KẾT NỐI
        }
    }

    // 3. Xóa nhân viên (Đã đổi tên từ xoaSP -> xoaNV)
    public void xoaNV(int manv) {
        Connection cnn = data.getConnection();
        try {
            String qry = "DELETE FROM nhanvien WHERE id = '" + manv + "'";
            Statement st = cnn.createStatement();
            st.executeUpdate(qry);
            JOptionPane.showMessageDialog(null, "Xóa nhân viên thành công!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi xóa NV: " + e.getMessage());
        } finally {
            data.closeConnection(); // ĐÓNG KẾT NỐI
        }
    }

    // 4. Sửa nhân viên (Đã đổi tên từ suaSP -> suaNV và thêm nháy đơn '')
    public void suaNV(NhanVienDTO nv) {
        Connection cnn = data.getConnection();
        try {
            // Đã bổ sung dấu nháy đơn '' cho diaChi và ngaySinh, thêm N cho tiếng Việt
            String qry = "UPDATE nhanvien SET "
                    + "ho = N'" + nv.getHoNV() + "', "
                    + "ten = N'" + nv.getTenNV() + "', "
                    + "phone = '" + nv.getSdt() + "', "
                    + "diaChi = N'" + nv.getDiaChi() + "', "
                    + "ngaySinh = '" + nv.getNgaySinh() + "', "
                    + "luong = '" + nv.getLuong() + "' "
                    + "WHERE id = '" + nv.getMaNV() + "'";
            Statement st = cnn.createStatement();
            st.executeUpdate(qry);
            JOptionPane.showMessageDialog(null, "Sửa thông tin nhân viên thành công!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi sửa NV: " + e.getMessage());
        } finally {
            data.closeConnection(); // ĐÓNG KẾT NỐI
        }
    }
}