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
    public void xoa(String ma) {
        SanPhamDAO data = new SanPhamDAO();
        data.xoaSP(ma);
        for(int i = 0; i < dssp.size(); i++){
            if(dssp.get(i).getMasanpham().equals(ma)){
                dssp.remove(i);
            }
        }
    }
    public void sua(SanPhamDTO sp){
        SanPhamDAO data = new SanPhamDAO();
        data.suaSP(sp);
        for(int i = 0; i < dssp.size(); i++){
            if(dssp.get(i).getMasanpham().equals(sp.getMasanpham())){
                dssp.set(i,sp);
            }
        }
    }
 }
