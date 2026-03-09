package SieuThiMiniBUS;

import DTO.SanPhamDTO;
import SieuThiMiniDAO.SanPhamDAO;

import java.util.ArrayList;

public class SanPhamBUS {
    public static ArrayList<SanPhamDTO> dssp;
    public SanPhamBUS(){}
    public void docDSSP(){
        SanPhamDAO data = new SanPhamDAO();
        if(dssp == null) dssp = new ArrayList<SanPhamDTO>();
        dssp = data.docDSSP();
    }
    public void them(SanPhamDTO sp){
        SanPhamDAO data = new SanPhamDAO();
        data.themSP(sp);
        dssp.add(sp);
    }
    public void xoa(int ma) {
        SanPhamDAO data = new SanPhamDAO();
        data.xoaSP(ma);
        for(int i = 0; i < dssp.size(); i++){
            if(dssp.get(i).getMasanpham() == (ma)){
                dssp.remove(i);
            }
        }
    }
    public void sua(SanPhamDTO sp){
        SanPhamDAO data = new SanPhamDAO();
        data.suaSP(sp);
        for(int i = 0; i < dssp.size(); i++){
            if(dssp.get(i).getMasanpham() == (sp.getMasanpham())){
                dssp.set(i,sp);
            }
        }
    }
    // Hàm cập nhật số lượng thông qua BUS
    public void capNhatSoLuong(int masp, int soLuongMoi) {
        SanPhamDAO data = new SanPhamDAO();
        data.capNhatSoLuong(masp, soLuongMoi); // Gọi DAO để update DB
        
        // Cập nhật luôn trong ArrayList để trên giao diện thay đổi theo
        if (dssp != null) {
            for (int i = 0; i < dssp.size(); i++) {
                if (dssp.get(i).getMasanpham() == masp) {
                    dssp.get(i).setSoluong(soLuongMoi);
                    break;
                }
            }
        }
    }
}
