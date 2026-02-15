package DTO;

import java.util.Date;

public class HoaDonDTO {
    private String mahoadon;
    private String manhanvien;
    private String makhachhang;
    private Date ngaylapdon;
    private double tongtien;

    public HoaDonDTO() {
    }

    public HoaDonDTO(String mahoadon, String manhanvien, String makhachhang, Date ngaylapdon, double tongtien) {
        this.mahoadon = mahoadon;
        this.manhanvien = manhanvien;
        this.makhachhang = makhachhang;
        this.ngaylapdon = ngaylapdon;
        this.tongtien = tongtien;
    }

    public String getMaHD() { return mahoadon; }
    public void setMaHD(String mahoadon) { this.mahoadon = mahoadon; }
    public String getMaNV() { return manhanvien; }
    public void setMaNV(String manhanvien) { this.manhanvien = manhanvien; }
    public String getMaKH() { return makhachhang; }
    public void setMaKH(String makhachhang) { this.makhachhang = makhachhang; }
    public Date getNgayLapDon() { return ngaylapdon; }
    public void setNgayLapDon(Date ngaylapdon) { this.ngaylapdon = ngaylapdon; }
    public double getTongTien() { return tongtien; }
    public void setTongTien(double tongtien) { this.tongtien = tongtien; }
}