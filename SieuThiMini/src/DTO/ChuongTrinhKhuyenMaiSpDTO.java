package DTO;

public class ChuongTrinhKhuyenMaiSpDTO {
    private int chuongTrinhKhuyenMaiId;
    private int sanPhamId;
    private int giaTriGiam;

    public ChuongTrinhKhuyenMaiSpDTO() {
    }

    public ChuongTrinhKhuyenMaiSpDTO(int chuongTrinhKhuyenMaiId, int sanPhamId, int giaTriGiam) {
        this.chuongTrinhKhuyenMaiId = chuongTrinhKhuyenMaiId;
        this.sanPhamId = sanPhamId;
        this.giaTriGiam = giaTriGiam;
    }

    public int getChuongTrinhKhuyenMaiId() { return chuongTrinhKhuyenMaiId; }
    public void setChuongTrinhKhuyenMaiId(int chuongTrinhKhuyenMaiId) { this.chuongTrinhKhuyenMaiId = chuongTrinhKhuyenMaiId; }
    public int getSanPhamId() { return sanPhamId; }
    public void setSanPhamId(int sanPhamId) { this.sanPhamId = sanPhamId; }
    public int getGiaTriGiam() { return giaTriGiam; }
    public void setGiaTriGiam(int giaTriGiam) { this.giaTriGiam = giaTriGiam; }
}
