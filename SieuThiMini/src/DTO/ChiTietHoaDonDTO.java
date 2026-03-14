package DTO;

public class ChiTietHoaDonDTO {
    private int mahoadon;
    private int masanpham;
    private int soluong;
    private long dongia;
    private long thanhtien;

    public ChiTietHoaDonDTO() {
    }

    public ChiTietHoaDonDTO(int mahoadon, int masanpham, int soluong, long dongia, long thanhtien) {
        this.mahoadon = mahoadon;
        this.masanpham = masanpham;
        this.soluong = soluong;
        this.dongia = dongia;
        this.thanhtien = thanhtien;
    }

    public int getMaHD() { return mahoadon; }
    public void setMaHD(int mahoadon) { this.mahoadon = mahoadon; }
    public int getMaSP() { return masanpham; }
    public void setMaSP(int masanpham) { this.masanpham = masanpham; }
    public int getSoLuong() { return soluong; }
    public void setSoLuong(int soluong) { this.soluong = soluong; }
    public long getDonGia() { return dongia; }
    public void setDonGia(long dongia) { this.dongia = dongia; }
    public long getThanhTien() { return thanhtien; }
    public void setThanhTien(long thanhtien) { this.thanhtien = thanhtien; }
}