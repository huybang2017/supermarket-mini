package supermarket.model;

import java.time.LocalDate;

/**
 * Promotion - Chương Trình Khuyến Mãi
 */
public class Promotion {
    private Long id;
    private String maChuongTrinh;
    private String tenChuongTrinh;
    private String loai; // "SanPham" hoặc "HoaDon"
    private LocalDate ngayBatDau;
    private LocalDate ngayKetThuc;
    private String dieuKien;
    private Double giaTriGiam;
    private String loaiGiam; // "PhanTram", "SoTien"
    private Boolean active;

    public Promotion() {
        this.active = true;
    }

    public Promotion(Long id, String tenChuongTrinh, String loai, LocalDate ngayBatDau, LocalDate ngayKetThuc) {
        this();
        this.id = id;
        this.tenChuongTrinh = tenChuongTrinh;
        this.loai = loai;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMaChuongTrinh() { return maChuongTrinh; }
    public void setMaChuongTrinh(String maChuongTrinh) { this.maChuongTrinh = maChuongTrinh; }

    public String getTenChuongTrinh() { return tenChuongTrinh; }
    public void setTenChuongTrinh(String tenChuongTrinh) { this.tenChuongTrinh = tenChuongTrinh; }

    public String getLoai() { return loai; }
    public void setLoai(String loai) { this.loai = loai; }

    public LocalDate getNgayBatDau() { return ngayBatDau; }
    public void setNgayBatDau(LocalDate ngayBatDau) { this.ngayBatDau = ngayBatDau; }

    public LocalDate getNgayKetThuc() { return ngayKetThuc; }
    public void setNgayKetThuc(LocalDate ngayKetThuc) { this.ngayKetThuc = ngayKetThuc; }

    public String getDieuKien() { return dieuKien; }
    public void setDieuKien(String dieuKien) { this.dieuKien = dieuKien; }

    public Double getGiaTriGiam() { return giaTriGiam; }
    public void setGiaTriGiam(Double giaTriGiam) { this.giaTriGiam = giaTriGiam; }

    public String getLoaiGiam() { return loaiGiam; }
    public void setLoaiGiam(String loaiGiam) { this.loaiGiam = loaiGiam; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    @Override
    public String toString() {
        return tenChuongTrinh;
    }
}
