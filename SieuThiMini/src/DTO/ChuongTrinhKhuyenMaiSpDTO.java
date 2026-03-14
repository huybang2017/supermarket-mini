package DTO;

public class ChuongTrinhKhuyenMaiSpDTO {
    private int chuongTrinhKhuyenMaiId;
    private int sanPhamId;
    private long giaTriGiam;

    public ChuongTrinhKhuyenMaiSpDTO() {
    }

    public ChuongTrinhKhuyenMaiSpDTO(int chuongTrinhKhuyenMaiId, int sanPhamId, long giaTriGiam) {
        this.chuongTrinhKhuyenMaiId = chuongTrinhKhuyenMaiId;
        this.sanPhamId = sanPhamId;
        this.giaTriGiam = giaTriGiam;
    }

    public int getChuongTrinhKhuyenMaiId() { return chuongTrinhKhuyenMaiId; }
    public void setChuongTrinhKhuyenMaiId(int chuongTrinhKhuyenMaiId) { this.chuongTrinhKhuyenMaiId = chuongTrinhKhuyenMaiId; }
    public int getSanPhamId() { return sanPhamId; }
    public void setSanPhamId(int sanPhamId) { this.sanPhamId = sanPhamId; }
    public long getGiaTriGiam() { return giaTriGiam; }
    public void setGiaTriGiam(long giaTriGiam) { this.giaTriGiam = giaTriGiam; }
}
