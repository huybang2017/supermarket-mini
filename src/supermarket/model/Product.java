package supermarket.model;

/**
 * Product - Sản Phẩm
 */
public class Product {
    private Long id;
    private String maSanPham;
    private String tenSanPham;
    private Long idLoai;
    private Long idHang;
    private Double giaBan;
    private Double giaVon;
    private Integer soLuongTon;
    private String donViTinh;
    private String moTa;
    private String hinhAnh;
    private Boolean active;

    public Product() {}

    public Product(Long id, String maSanPham, String tenSanPham, Double giaBan, Integer soLuongTon) {
        this.id = id;
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
        this.giaBan = giaBan;
        this.soLuongTon = soLuongTon;
        this.active = true;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMaSanPham() { return maSanPham; }
    public void setMaSanPham(String maSanPham) { this.maSanPham = maSanPham; }

    public String getTenSanPham() { return tenSanPham; }
    public void setTenSanPham(String tenSanPham) { this.tenSanPham = tenSanPham; }

    public Long getIdLoai() { return idLoai; }
    public void setIdLoai(Long idLoai) { this.idLoai = idLoai; }

    public Long getIdHang() { return idHang; }
    public void setIdHang(Long idHang) { this.idHang = idHang; }

    public Double getGiaBan() { return giaBan; }
    public void setGiaBan(Double giaBan) { this.giaBan = giaBan; }

    public Double getGiaVon() { return giaVon; }
    public void setGiaVon(Double giaVon) { this.giaVon = giaVon; }

    public Integer getSoLuongTon() { return soLuongTon; }
    public void setSoLuongTon(Integer soLuongTon) { this.soLuongTon = soLuongTon; }

    public String getDonViTinh() { return donViTinh; }
    public void setDonViTinh(String donViTinh) { this.donViTinh = donViTinh; }

    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }

    public String getHinhAnh() { return hinhAnh; }
    public void setHinhAnh(String hinhAnh) { this.hinhAnh = hinhAnh; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    @Override
    public String toString() {
        return tenSanPham;
    }
}
