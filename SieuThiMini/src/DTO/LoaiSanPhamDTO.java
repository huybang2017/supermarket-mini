package DTO;

public class LoaiSanPhamDTO {
    private String maloai;
    private String tenloai;
    private String hang;

    public LoaiSanPhamDTO() {
    }

    public LoaiSanPhamDTO(String maloai, String tenloai, String hang) {
        this.maloai = maloai;
        this.tenloai = tenloai;
        this.hang = hang;
    }

    public String getMaLoai() { return maloai; }
    public void setMaLoai(String maloai) { this.maloai = maloai; }
    public String getTenLoai() { return tenloai; }
    public void setTenLoai(String tenloai) { this.tenloai = tenloai; }
    public String getHang() { return hang; }
    public void setHang(String hang) { this.hang = hang; }
}