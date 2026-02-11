package supermarket.model;

/**
 * Manufacturer - Hãng Sản Xuất
 */
public class Manufacturer {
    private Long id;
    private String tenHang;
    private String quocGia;
    private String moTa;
    private Boolean active;

    public Manufacturer() {}

    public Manufacturer(Long id, String tenHang, String quocGia) {
        this.id = id;
        this.tenHang = tenHang;
        this.quocGia = quocGia;
        this.active = true;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTenHang() { return tenHang; }
    public void setTenHang(String tenHang) { this.tenHang = tenHang; }

    public String getQuocGia() { return quocGia; }
    public void setQuocGia(String quocGia) { this.quocGia = quocGia; }

    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    @Override
    public String toString() {
        return tenHang;
    }
}
