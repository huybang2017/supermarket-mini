package DTO;

public class SanPhamDTO {
    private  int masanpham;
    private  String tensanpham;
    private int soluong;
    private int dongia;
    private String donvitinh;
    private int maLoai;
    private int maHang;
    public SanPhamDTO(){}

    public SanPhamDTO(int masanpham, String tensanpham, int soluong, int dongia, String donvitinh,int maLoai,int maHang){
        this.masanpham = masanpham;
        this.tensanpham = tensanpham;
        this.soluong = soluong;
        this.dongia = dongia;
        this.donvitinh = donvitinh;
        this.maLoai = maLoai;
        this.maHang = maHang;
    }

    public int getMasanpham() { return masanpham; }
    public void setMasanpham(int masanpham) { this.masanpham = masanpham; }
    public String getTensanpham() {return tensanpham;}
    public void setTensanpham(String tensanpham) {this.tensanpham = tensanpham;}
    public int getSoluong() {return soluong;}
    public void setSoluong(int soluong) {this.soluong = soluong;}
    public int getDongia() {return dongia;}
    public void setDongia(int dongia) {this.dongia = dongia;}
    public String getDonvitinh() {return donvitinh;}
    public void setDonvitinh(String donvitinh) {this.donvitinh = donvitinh;}
    public int getMaLoai() { return maLoai; }
    public void setMaLoai(int maLoai) { this.maLoai = maLoai; }
    public int getMaHang() { return maHang; }
    public void setMaHang(int maHang) { this.maHang = maHang; }
}
