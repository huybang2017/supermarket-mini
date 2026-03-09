package SieuThiMiniDAO;

import java.sql.*;
import java.util.ArrayList;
import DTO.ChiTietPhieuNhapHangDTO;

public class ChiTietPhieuNhapHangDAO {
    MyConnection data = new MyConnection();

public ArrayList<ChiTietPhieuNhapHangDTO> docTheoMaPN(int maPN) { 
    ArrayList<ChiTietPhieuNhapHangDTO> dsct = new ArrayList<>();
    Connection cnn = data.getConnection();
    try {
        String qry = "SELECT * FROM chitietphieunhaphang WHERE phieuNhapHangId=" + maPN;
        Statement st = cnn.createStatement();
        ResultSet rs = st.executeQuery(qry);
        while (rs.next()) {
            ChiTietPhieuNhapHangDTO ct = new ChiTietPhieuNhapHangDTO();
            ct.setMaPNH(rs.getInt(1)); 
            ct.setMaSP(rs.getInt(2));  
            ct.setSoLuong(rs.getInt(3));
            ct.setDonGia(rs.getDouble(4));
            dsct.add(ct);
        }
    } catch (SQLException e) { System.out.println("Lỗi đọc chi tiết"); }
    return dsct;
}

public void themCTPNH(ChiTietPhieuNhapHangDTO ct) {
    Connection cnn = data.getConnection();
    try {
        String qry = "INSERT INTO chitietphieunhaphang VALUES (" 
                + ct.getMaPNH() + "," + ct.getMaSP() + "," 
                + ct.getSoLuong() + "," + ct.getDonGia() + "," + ct.getThanhTien() + ")";
        Statement st = cnn.createStatement();
        st.executeUpdate(qry);
    } catch (SQLException e) { System.out.println("Lỗi thêm chi tiết"); }
    finally { data.closeConnection(); }
}

public void xoaCTPNH(int ma){
    Connection cnn = data.getConnection();
    try{
        String qry = "DELETE FROM chitietphieunhaphang WHERE phieuNhapHangId = " + ma;
        Statement st = cnn.createStatement();
        st.executeUpdate(qry);
    } catch (SQLException e) {
         System.out.println("Lỗi xóa: " + e.getMessage());
    } finally { data.closeConnection(); }
}

public void suaCTPNH(ChiTietPhieuNhapHangDTO ct) {
    Connection cnn = data.getConnection();
    try {
        String qry = "UPDATE chitietphieunhaphang SET "
                + "sanPhamId = " + ct.getMaSP() + ", "
                + "soLuong = " + ct.getSoLuong() + ", "
                + "donGia = " + ct.getDonGia() + " "
                + "WHERE phieuNhapHangId = " + ct.getMaPNH();
        Statement st = cnn.createStatement();
        st.executeUpdate(qry);
    } catch (SQLException e) {
         System.out.println("Lỗi sửa: " + e.getMessage());
    } finally { data.closeConnection(); }
}
}