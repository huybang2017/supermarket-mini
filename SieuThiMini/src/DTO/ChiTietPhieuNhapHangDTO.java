package DTO;

public class ChiTietPhieuNhapHangDTO {
    private int maphieunhaphang;
    private int masanpham;
    private long dongia;
    private int soluong;
    private long thanhtien;

    public ChiTietPhieuNhapHangDTO() {
    }

    public ChiTietPhieuNhapHangDTO(int maphieunhaphang, int masanpham, long dongia, int soluong, long thanhtien) {
        this.maphieunhaphang = maphieunhaphang;
        this.masanpham = masanpham;
        this.dongia = dongia;
        this.soluong = soluong;
        this.thanhtien = thanhtien;
    }

    public int getMaPNH() { return maphieunhaphang; }
    public void setMaPNH(int maphieunhaphang) { this.maphieunhaphang = maphieunhaphang; }
    public int getMaSP() { return masanpham; }
    public void setMaSP(int masanpham) { this.masanpham = masanpham; }
    public long getDonGia() { return dongia; }
    public void setDonGia(long dongia) { this.dongia = dongia; }
    public int getSoLuong() { return soluong; }
    public void setSoLuong(int soluong) { this.soluong = soluong; }
    public long getThanhTien() {return this.soluong * this.dongia;}
    public void setThanhTien(long thanhtien) { this.thanhtien = thanhtien; }
}