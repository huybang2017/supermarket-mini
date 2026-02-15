package DTO;

public class ChiTietPhieuNhapHangDTO {
    private String maphieunhaphang;
    private String masanpham;
    private double dongia;
    private int soluong;
    private double thanhtien;

    public ChiTietPhieuNhapHangDTO() {
    }

    public ChiTietPhieuNhapHangDTO(String maphieunhaphang, String masanpham, double dongia, int soluong, double thanhtien) {
        this.maphieunhaphang = maphieunhaphang;
        this.masanpham = masanpham;
        this.dongia = dongia;
        this.soluong = soluong;
        this.thanhtien = thanhtien;
    }

    public String getMaPNH() { return maphieunhaphang; }
    public void setMaPNH(String maphieunhaphang) { this.maphieunhaphang = maphieunhaphang; }
    public String getMaSP() { return masanpham; }
    public void setMaSP(String masanpham) { this.masanpham = masanpham; }
    public double getDonGia() { return dongia; }
    public void setDonGia(double dongia) { this.dongia = dongia; }
    public int getSoLuong() { return soluong; }
    public void setSoLuong(int soluong) { this.soluong = soluong; }
    public double getThanhTien() { return thanhtien; }
    public void setThanhTien(double thanhtien) { this.thanhtien = thanhtien; }
}