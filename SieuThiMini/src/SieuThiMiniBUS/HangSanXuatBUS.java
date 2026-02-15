package SieuThiMiniBUS;

import DTO.HangSanXuatDTO;
import DTO.LoaiSanPhamDTO;
import SieuThiMiniDAO.HangSanXuatDAO;
import SieuThiMiniDAO.HangSanXuatDAO;

import java.util.ArrayList;

public class HangSanXuatBUS {
    public static ArrayList<HangSanXuatDTO> dshsx;
    public HangSanXuatBUS(){}
    public void docDSLSP(){
        HangSanXuatDAO data = new HangSanXuatDAO();
        if (dshsx == null) dshsx = new ArrayList<HangSanXuatDTO>();
        dshsx = data.docDSHSX();
    }
    public void them(HangSanXuatDTO hsx){
        HangSanXuatDAO data = new HangSanXuatDAO();
        data.themHSX(hsx);
        dshsx.add(hsx);
    }
    public void xoa(String ma) {
        HangSanXuatDAO data = new HangSanXuatDAO();
        data.xoaHSX(ma);
        for(int i = 0; i < dshsx.size(); i++){
            if(dshsx.get(i).getMaHang().equals(ma)){
                dshsx.remove(i);
            }
        }
    }
    public void sua(HangSanXuatDTO hsx){
        HangSanXuatDAO data = new HangSanXuatDAO();
        data.suaHSX(hsx);
        for(int i = 0; i < dshsx.size(); i++){
            if(dshsx.get(i).getMaHang().equals(hsx.getMaHang())){
                dshsx.set(i,hsx);
            }
        }
    }
}
