package DTO;

public class ChiTietHoaDonDTO {
    private String mahoadon;
    private String masanpham;
    private int soluong;
    private double dongia;
    private double thanhtien;

    public ChiTietHoaDonDTO() {
    }

    public ChiTietHoaDonDTO(String mahoadon, String masanpham, int soluong, double dongia, double thanhtien) {
        this.mahoadon = mahoadon;
        this.masanpham = masanpham;
        this.soluong = soluong;
        this.dongia = dongia;
        this.thanhtien = thanhtien;
    }

    public String getMaHD() { return mahoadon; }
    public void setMaHD(String mahoadon) { this.mahoadon = mahoadon; }
    public String getMaSP() { return masanpham; }
    public void setMaSP(String masanpham) { this.masanpham = masanpham; }
    public int getSoLuong() { return soluong; }
    public void setSoLuong(int soluong) { this.soluong = soluong; }
    public double getDonGia() { return dongia; }
    public void setDonGia(double dongia) { this.dongia = dongia; }
    public double getThanhTien() { return thanhtien; }
    public void setThanhTien(double thanhtien) { this.thanhtien = thanhtien; }
}