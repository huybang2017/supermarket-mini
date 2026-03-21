package DTO;

public class LoaiSanPhamDTO {
    private int maloai;
    private String tenloai;

    public LoaiSanPhamDTO() {
    }

    public LoaiSanPhamDTO(int maloai, String tenloai ) {
        this.maloai = maloai;
        this.tenloai = tenloai;
    }

    public int getMaLoai() { return maloai; }
    public void setMaLoai(int maloai) { this.maloai = maloai; }
    public String getTenLoai() { return tenloai; }
    public void setTenLoai(String tenloai) { this.tenloai = tenloai; }
}