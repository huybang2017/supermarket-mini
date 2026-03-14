package DTO;

import java.sql.Date;

public class PhieuNhapHangDTO {
    private int maphieunhaphang, manhanvien, manhacungcap;
    private Date ngaynhap;
    private long tongtien;

    public PhieuNhapHangDTO() {
    }

    public PhieuNhapHangDTO(int maphieunhaphang, int manhanvien, int manhacungcap, Date ngaynhap, long tongtien) {
        this.maphieunhaphang = maphieunhaphang;
        this.manhanvien = manhanvien;
        this.manhacungcap = manhacungcap;
        this.ngaynhap = ngaynhap;
        this.tongtien = tongtien;
    }

    public int getMaPNH() { return maphieunhaphang; }
    public void setMaPNH(int maphieunhaphang) { this.maphieunhaphang = maphieunhaphang; }
    public int getMaNV() { return manhanvien; }
    public void setMaNV(int manhanvien) { this.manhanvien = manhanvien; }
    public int getMaNCC() { return manhacungcap; }
    public void setMaNCC(int manhacungcap) { this.manhacungcap = manhacungcap; }
    public Date getNgayNhap() { return ngaynhap; }
    public void setNgayNhap(Date ngaynhap) { this.ngaynhap = ngaynhap; }
    public long getTongTien() { return tongtien; }
    public void setTongTien(long tongtien) { this.tongtien = tongtien; }
}
