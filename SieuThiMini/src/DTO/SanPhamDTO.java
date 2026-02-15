package DTO;

public class SanPhamDTO {
    private  String masanpham;
    private  String tensanpham;
    private int soluong;
    private int dongia;
    private String donvitinh;
    private String maLoai;
    private String maHang;
    public SanPhamDTO(){}

    public SanPhamDTO(String masanpham, String tensanpham, int soluong, int dongia, String donvitinh){
        this.masanpham = masanpham;
        this.tensanpham = tensanpham;
        this.soluong = soluong;
        this.dongia = dongia;
        this.donvitinh = donvitinh;
        this.maLoai = maLoai;
        this.maHang = maHang;
    }

    public String getMasanpham() { return masanpham; }
    public void setMasanpham(String masanpham) { this.masanpham = masanpham; }
    public String getTensanpham() {return tensanpham;}
    public void setTensanpham(String tensanpham) {this.tensanpham = tensanpham;}
    public int getSoluong() {return soluong;}
    public void setSoluong(int soluong) {this.soluong = soluong;}
    public int getDongia() {return dongia;}
    public void setDongia(int dongia) {this.dongia = dongia;}
    public String getDonvitinh() {return donvitinh;}
    public void setDonvitinh(String donvitinh) {this.donvitinh = donvitinh;}
    public String getMaLoai() { return maLoai; }
    public void setMaLoai(String maLoai) { this.maLoai = maLoai; }
    public String getMaHang() { return maHang; }
    public void setMaHang(String maHang) { this.maHang = maHang; }
}
