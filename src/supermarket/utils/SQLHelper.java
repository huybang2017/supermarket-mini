/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package supermarket.utils;

/**
 *
 * @author ndhba
 */
public class SQLHelper {
    public static final String GET_ALL_INVOICE =
        "SELECT * FROM HoaDon ORDER BY ngayLapHD DESC";

    public static final String GET_INVOICE_BY_ID =
        "SELECT * FROM HoaDon WHERE id = ?";

    public static final String INSERT_INVOICE =
        "INSERT INTO HoaDon(khachHangId, nhanVienId, tongTien, ngayLapHD) VALUES(?,?,?,?)";

    // Product
    public static final String GET_ALL_PRODUCT =
        "SELECT * FROM SanPham";
}
