package DTO;

import java.util.Date;

public class HoaDonDTO {
    private int mahoadon;
    private int manhanvien;
    private int makhachhang;
    private Date ngaylapdon;
    private long tongtien;

    public HoaDonDTO() {
    }

    public HoaDonDTO(int mahoadon, int manhanvien, int makhachhang, Date ngaylapdon, long tongtien) {
        this.mahoadon = mahoadon;
        this.manhanvien = manhanvien;
        this.makhachhang = makhachhang;
        this.ngaylapdon = ngaylapdon;
        this.tongtien = tongtien;
    }

    public int getMaHD() { return mahoadon; }
    public void setMaHD(int mahoadon) { this.mahoadon = mahoadon; }
    public int getMaNV() { return manhanvien; }
    public void setMaNV(int manhanvien) { this.manhanvien = manhanvien; }
    public int getMaKH() { return makhachhang; }
    public void setMaKH(int makhachhang) { this.makhachhang = makhachhang; }
    public Date getNgayLapDon() { return ngaylapdon; }
    public void setNgayLapDon(Date ngaylapdon) { this.ngaylapdon = ngaylapdon; }
    public long getTongTien() { return tongtien; }
    public void setTongTien(long tongtien) { this.tongtien = tongtien; }
}