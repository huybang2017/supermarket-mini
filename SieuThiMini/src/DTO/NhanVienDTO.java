package DTO;

import java.util.Date;

public class NhanVienDTO {
    private String maNV;
    private String hoNV;
    private String tenNV;
    private Date ngaySinh;
    private String diaChi;
    private String sdt;
    private double luong;

    public NhanVienDTO() {
    }

    public NhanVienDTO(String maNV, String hoNV, String tenNV, Date ngaySinh, String diaChi, String sdt, double luong) {
        this.maNV = maNV;
        this.hoNV = hoNV;
        this.tenNV = tenNV;
        this.ngaySinh = ngaySinh;
        this.diaChi = diaChi;
        this.sdt = sdt;
        this.luong = luong;
    }

    public String getMaNV() { return maNV; }
    public void setMaNV(String maNV) { this.maNV = maNV; }
    public String getHoNV() { return hoNV; }
    public void setHoNV(String hoNV) { this.hoNV = hoNV; }
    public String getTenNV() { return tenNV; }
    public void setTenNV(String tenNV) { this.tenNV = tenNV; }
    public Date getNgaySinh() { return ngaySinh; }
    public void setNgaySinh(Date ngaySinh) { this.ngaySinh = ngaySinh; }
    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }
    public String getSdt() { return sdt; }
    public void setSdt(String sdt) { this.sdt = sdt; }
    public double getLuong() { return luong; }
    public void setLuong(double luong) { this.luong = luong; }
}