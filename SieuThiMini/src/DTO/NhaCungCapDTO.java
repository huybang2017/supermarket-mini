package DTO;

public class NhaCungCapDTO {
    private int manhacungcap;
    private String tennhacungcap;
    private String diaChi;
    private String sdt;

    public NhaCungCapDTO() {
    }

    public NhaCungCapDTO(int manhacungcap, String tennhacungcap, String diaChi, String sdt) {
        this.manhacungcap = manhacungcap;
        this.tennhacungcap = tennhacungcap;
        this.diaChi = diaChi;
        this.sdt = sdt;
    }

    public int getMaNCC() { return manhacungcap; }
    public void setMaNCC(int manhacungcap) { this.manhacungcap = manhacungcap; }
    public String getTenNCC() { return tennhacungcap; }
    public void setTenNCC(String tennhacungcap) { this.tennhacungcap = tennhacungcap; }
    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }
    public String getSdt() { return sdt; }
    public void setSdt(String sdt) { this.sdt = sdt; }
}