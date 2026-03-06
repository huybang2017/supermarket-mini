package DTO;

import java.util.Date;

public class ChuongTrinhKhuyenMaiDTO {
    private int id;
    private String ten;
    private String ghiChu;
    private Date ngayBatDau;
    private Date ngayKetThuc;
    private boolean trangThai;

    public ChuongTrinhKhuyenMaiDTO() {
    }

    public ChuongTrinhKhuyenMaiDTO(int id, String ten, String ghiChu, Date ngayBatDau, Date ngayKetThuc, boolean trangThai) {
        this.id = id;
        this.ten = ten;
        this.ghiChu = ghiChu;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.trangThai = trangThai;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTen() { return ten; }
    public void setTen(String ten) { this.ten = ten; }
    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }
    public Date getNgayBatDau() { return ngayBatDau; }
    public void setNgayBatDau(Date ngayBatDau) { this.ngayBatDau = ngayBatDau; }
    public Date getNgayKetThuc() { return ngayKetThuc; }
    public void setNgayKetThuc(Date ngayKetThuc) { this.ngayKetThuc = ngayKetThuc; }
    public boolean isTrangThai() { return trangThai; }
    public void setTrangThai(boolean trangThai) { this.trangThai = trangThai; }
}
