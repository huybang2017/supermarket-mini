package DTO;

public class SanPhamDTO {
    private  int masanpham;
    private  String tensanpham;
    private int soluong;
    private long dongia;
    private String donvitinh;
    private int maLoai;
    private int maHang;
    private long giaNhap;
    private double loiNhuan;
    public SanPhamDTO(){}

    public SanPhamDTO(int masanpham, String tensanpham, int soluong, long dongia,long giaNhap,double loiNhuan, String donvitinh,int maHang,int maLoai){
        this.masanpham = masanpham;
        this.tensanpham = tensanpham;
        this.soluong = soluong;
        this.dongia = dongia;
        this.donvitinh = donvitinh;
        this.maLoai = maLoai;
        this.maHang = maHang;
        this.giaNhap = giaNhap;
        this.loiNhuan = loiNhuan;
    }

    public int getMasanpham() { return masanpham; }
    public void setMasanpham(int masanpham) { this.masanpham = masanpham; }
    public String getTensanpham() {return tensanpham;}
    public void setTensanpham(String tensanpham) {this.tensanpham = tensanpham;}
    public int getSoluong() {return soluong;}
    public void setSoluong(int soluong) {this.soluong = soluong;}
    public long getDongia() {return dongia;}
    public void setDongia(long dongia) {this.dongia = dongia;}
    public String getDonvitinh() {return donvitinh;}
    public void setDonvitinh(String donvitinh) {this.donvitinh = donvitinh;}
    public int getMaLoai() { return maLoai; }
    public void setMaLoai(int maLoai) { this.maLoai = maLoai; }
    public int getMaHang() { return maHang; }
    public void setMaHang(int maHang) { this.maHang = maHang; }
    public long getGiaNhap() { return giaNhap; }
    public void setGiaNhap(long giaNhap) { this.giaNhap = giaNhap; }

    public double getLoiNhuan() { return loiNhuan; }
    public void setLoiNhuan(double loiNhuan) { this.loiNhuan = loiNhuan; }
}
