package DTO;

public class KhachHangDTO {
    private int maKH;
    private String hoKH;
    private String tenKH;
    private String diaChi;
    private String sdt;

    public KhachHangDTO() {
    }

    public KhachHangDTO(int maKH, String hoKH, String tenKH, String diaChi, String sdt) {
        this.maKH = maKH;
        this.hoKH = hoKH;
        this.tenKH = tenKH;
        this.diaChi = diaChi;
        this.sdt = sdt;
    }

    public int getMaKH() { return maKH; }
    public void setMaKH(int maKH) { this.maKH = maKH; }
    public String getHoKH() { return hoKH; }
    public void setHoKH(String hoKH) { this.hoKH = hoKH; }
    public String getTenKH() { return tenKH; }
    public void setTenKH(String tenKH) { this.tenKH = tenKH; }
    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }
    public String getSdt() { return sdt; }
    public void setSdt(String sdt) { this.sdt = sdt; }
}