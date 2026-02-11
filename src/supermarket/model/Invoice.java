package supermarket.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Invoice - Hóa Đơn
 */
public class Invoice {
    private Long id;
    private String maHoaDon;
    private LocalDateTime ngayTao;
    private Long idKhachHang;
    private Long idNhanVien;
    private Double tongTien;
    private Double tienGiam;
    private Double thanhToan;
    private String phuongThucThanhToan;
    private String trangThai;
    private List<InvoiceDetail> chiTiet;

    public Invoice() {
        this.ngayTao = LocalDateTime.now();
        this.chiTiet = new ArrayList<>();
        this.trangThai = "Hoàn thành";
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMaHoaDon() { return maHoaDon; }
    public void setMaHoaDon(String maHoaDon) { this.maHoaDon = maHoaDon; }

    public LocalDateTime getNgayTao() { return ngayTao; }
    public void setNgayTao(LocalDateTime ngayTao) { this.ngayTao = ngayTao; }

    public Long getIdKhachHang() { return idKhachHang; }
    public void setIdKhachHang(Long idKhachHang) { this.idKhachHang = idKhachHang; }

    public Long getIdNhanVien() { return idNhanVien; }
    public void setIdNhanVien(Long idNhanVien) { this.idNhanVien = idNhanVien; }

    public Double getTongTien() { return tongTien; }
    public void setTongTien(Double tongTien) { this.tongTien = tongTien; }

    public Double getTienGiam() { return tienGiam; }
    public void setTienGiam(Double tienGiam) { this.tienGiam = tienGiam; }

    public Double getThanhToan() { return thanhToan; }
    public void setThanhToan(Double thanhToan) { this.thanhToan = thanhToan; }

    public String getPhuongThucThanhToan() { return phuongThucThanhToan; }
    public void setPhuongThucThanhToan(String phuongThucThanhToan) { this.phuongThucThanhToan = phuongThucThanhToan; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public List<InvoiceDetail> getChiTiet() { return chiTiet; }
    public void setChiTiet(List<InvoiceDetail> chiTiet) { this.chiTiet = chiTiet; }
}

/**
 * InvoiceDetail - Chi Tiết Hóa Đơn
 */
class InvoiceDetail {
    private Long id;
    private Long idHoaDon;
    private Long idSanPham;
    private String tenSanPham;
    private Integer soLuong;
    private Double donGia;
    private Double thanhTien;

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getIdHoaDon() { return idHoaDon; }
    public void setIdHoaDon(Long idHoaDon) { this.idHoaDon = idHoaDon; }

    public Long getIdSanPham() { return idSanPham; }
    public void setIdSanPham(Long idSanPham) { this.idSanPham = idSanPham; }

    public String getTenSanPham() { return tenSanPham; }
    public void setTenSanPham(String tenSanPham) { this.tenSanPham = tenSanPham; }

    public Integer getSoLuong() { return soLuong; }
    public void setSoLuong(Integer soLuong) { this.soLuong = soLuong; }

    public Double getDonGia() { return donGia; }
    public void setDonGia(Double donGia) { this.donGia = donGia; }

    public Double getThanhTien() { return thanhTien; }
    public void setThanhTien(Double thanhTien) { this.thanhTien = thanhTien; }
}
