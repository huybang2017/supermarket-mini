package DTO;

public class NhaCungCapDTO {
    private String manhacungcap;
    private String tennhacungcap;
    private String diaChi;
    private String sdt;

    public NhaCungCapDTO() {
    }

    public NhaCungCapDTO(String manhacungcap, String tennhacungcap, String diaChi, String sdt) {
        this.manhacungcap = manhacungcap;
        this.tennhacungcap = tennhacungcap;
        this.diaChi = diaChi;
        this.sdt = sdt;
    }

    public String getMaNCC() { return manhacungcap; }
    public void setMaNCC(String manhacungcap) { this.manhacungcap = manhacungcap; }
    public String getTenNCC() { return tennhacungcap; }
    public void setTenNCC(String tennhacungcap) { this.tennhacungcap = tennhacungcap; }
    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }
    public String getSdt() { return sdt; }
    public void setSdt(String sdt) { this.sdt = sdt; }
}