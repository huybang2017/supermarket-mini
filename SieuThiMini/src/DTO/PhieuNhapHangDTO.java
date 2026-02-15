package DTO;

import java.util.Date;

public class PhieuNhapHangDTO {
    private String maphieunhaphang;
    private String manhanvien;
    private String manhacungcap;
    private Date ngaynhap;
    private double tongtien;

    public PhieuNhapHangDTO() {
    }

    public PhieuNhapHangDTO(String maphieunhaphang, String manhanvien, String manhacungcap, Date ngaynhap, double tongtien) {
        this.maphieunhaphang = maphieunhaphang;
        this.manhanvien = manhanvien;
        this.manhacungcap = manhacungcap;
        this.ngaynhap = ngaynhap;
        this.tongtien = tongtien;
    }

    public String getMaPNH() { return maphieunhaphang; }
    public void setMaPNH(String maphieunhaphang) { this.maphieunhaphang = maphieunhaphang; }
    public String getMaNV() { return manhanvien; }
    public void setMaNV(String manhanvien) { this.manhanvien = manhanvien; }
    public String getMaNCC() { return manhacungcap; }
    public void setMaNCC(String manhacungcap) { this.manhacungcap = manhacungcap; }
    public Date getNgayNhap() { return ngaynhap; }
    public void setNgayNhap(Date ngaynhap) { this.ngaynhap = ngaynhap; }
    public double getTongTien() { return tongtien; }
    public void setTongTien(double tongtien) { this.tongtien = tongtien; }
}
