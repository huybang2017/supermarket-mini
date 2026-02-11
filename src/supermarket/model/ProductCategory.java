package supermarket.model;

/**
 * ProductCategory - Loại Sản Phẩm
 *
 * @author Supermarket Mini Team
 */
public class ProductCategory {
    private Long id;
    private String tenLoai;
    private String moTa;
    private Boolean active;

    public ProductCategory() {}

    public ProductCategory(Long id, String tenLoai) {
        this.id = id;
        this.tenLoai = tenLoai;
        this.active = true;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTenLoai() { return tenLoai; }
    public void setTenLoai(String tenLoai) { this.tenLoai = tenLoai; }

    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    @Override
    public String toString() {
        return tenLoai;
    }
}
