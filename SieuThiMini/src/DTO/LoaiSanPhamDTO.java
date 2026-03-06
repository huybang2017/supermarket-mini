package DTO;

public class LoaiSanPhamDTO {
    private int maloai;
    private String tenloai;
    private int hang;

    public LoaiSanPhamDTO() {
    }

    public LoaiSanPhamDTO(int maloai, String tenloai, int hang) {
        this.maloai = maloai;
        this.tenloai = tenloai;
        this.hang = hang;
    }

    public int getMaLoai() { return maloai; }
    public void setMaLoai(int maloai) { this.maloai = maloai; }
    public String getTenLoai() { return tenloai; }
    public void setTenLoai(String tenloai) { this.tenloai = tenloai; }
    public int getHang() { return hang; }
    public void setHang(int hang) { this.hang = hang; }
}