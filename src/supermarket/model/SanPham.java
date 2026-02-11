package supermarket.model;

/**
 * SanPham (Product) Domain Model
 *
 * Represents a product in the supermarket system
 *
 * @author Supermarket Mini Team
 * @version 1.0
 */
public class SanPham {

    // Fields
    private Long id;
    private String ten;         
    private Integer soLuong;  
    private Double donGia;     
    private int loaiSanPhamId;

    // Constructors
    public SanPham() {
    }

    public SanPham(Long id, String ten, Integer soLuong, Double donGia) {
        this.id = id;
        this.ten = ten;
        this.soLuong = soLuong;
        this.donGia = donGia;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public Integer getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(Integer soLuong) {
        this.soLuong = soLuong;
    }

    public Double getDonGia() {
        return donGia;
    }

    public void setDonGia(Double donGia) {
        this.donGia = donGia;
    }
    
    public int getLoaiSanPhamId(){
        return loaiSanPhamId;
    }
    
    public void setLoaiSanPhamId(Integer loaiSanPhamId){
        this.loaiSanPhamId = loaiSanPhamId;
    }

    @Override
    public String toString() {
        return "SanPham{" +
                "id=" + id +
                ", ten='" + ten + '\'' +
                ", soLuong=" + soLuong +
                ", donGia=" + donGia +
                '}';
    }
}
