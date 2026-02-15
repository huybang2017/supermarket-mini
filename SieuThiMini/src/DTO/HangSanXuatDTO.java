package DTO;

public class HangSanXuatDTO {
    private String mahang;
    private String tenhang;
    private String diachi;
    private String sdt;

    public HangSanXuatDTO() {
    }

    public HangSanXuatDTO(String mahang, String tenhang, String diachi, String sdt) {
        this.mahang = mahang;
        this.tenhang = tenhang;
        this.diachi = diachi;
        this.sdt = sdt;
    }

    public String getMaHang() { return mahang; }
    public void setMaHang(String mahang) { this.mahang = mahang; }
    public String getTenHang() { return tenhang; }
    public void setTenHang(String tenhang) { this.tenhang = tenhang; }
    public String getDiaChi() { return diachi; }
    public void setDiaChi(String diachi) { this.diachi = diachi; }
    public String getSdt() { return sdt; }
    public void setSdt(String sdt) { this.sdt = sdt; }
}