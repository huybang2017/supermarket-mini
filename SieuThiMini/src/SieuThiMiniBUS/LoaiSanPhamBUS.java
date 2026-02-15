package SieuThiMiniBUS;

import DTO.LoaiSanPhamDTO;
import DTO.SanPhamDTO;
import SieuThiMiniDAO.LoaiSanPhamDAO;
import SieuThiMiniDAO.SanPhamDAO;

import java.lang.classfile.Label;
import java.util.ArrayList;

public class LoaiSanPhamBUS {
    public static ArrayList<LoaiSanPhamDTO> dslsp;
    public LoaiSanPhamBUS(){}
    public void docDSLSP(){
        LoaiSanPhamDAO data = new LoaiSanPhamDAO();
        if (dslsp == null) dslsp = new ArrayList<LoaiSanPhamDTO>();
        dslsp = data.docDSLSP();
    }
    public void them(LoaiSanPhamDTO lsp){
        LoaiSanPhamDAO data = new LoaiSanPhamDAO();
        data.themLSP(lsp);
        dslsp.add(lsp);
    }
    public void xoa(String ma) {
        LoaiSanPhamDAO data = new LoaiSanPhamDAO();
        data.xoaLSP(ma);
        for(int i = 0; i < dslsp.size(); i++){
            if(dslsp.get(i).getMaLoai().equals(ma)){
                dslsp.remove(i);
            }
        }
    }
    public void sua(LoaiSanPhamDTO lsp){
        LoaiSanPhamDAO data = new LoaiSanPhamDAO();
        data.suaLSP(lsp);
        for(int i = 0; i < dslsp.size(); i++){
            if(dslsp.get(i).getMaLoai().equals(lsp.getMaLoai())){
                dslsp.set(i,lsp);
            }
        }
    }
}
