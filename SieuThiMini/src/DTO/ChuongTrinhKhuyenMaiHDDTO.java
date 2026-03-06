package DTO;

public class ChuongTrinhKhuyenMaiHDDTO {
    private int chuongTrinhKhuyenMaiId;
    private int soTienHd;
    private int giaTriGiam;

    public ChuongTrinhKhuyenMaiHDDTO() {
    }

    public ChuongTrinhKhuyenMaiHDDTO(int chuongTrinhKhuyenMaiId, int soTienHd, int giaTriGiam) {
        this.chuongTrinhKhuyenMaiId = chuongTrinhKhuyenMaiId;
        this.soTienHd = soTienHd;
        this.giaTriGiam = giaTriGiam;
    }

    public int getChuongTrinhKhuyenMaiId() { return chuongTrinhKhuyenMaiId; }
    public void setChuongTrinhKhuyenMaiId(int chuongTrinhKhuyenMaiId) { this.chuongTrinhKhuyenMaiId = chuongTrinhKhuyenMaiId; }
    public int getSoTienHd() { return soTienHd; }
    public void setSoTienHd(int soTienHd) { this.soTienHd = soTienHd; }
    public int getGiaTriGiam() { return giaTriGiam; }
    public void setGiaTriGiam(int giaTriGiam) { this.giaTriGiam = giaTriGiam; }
}
