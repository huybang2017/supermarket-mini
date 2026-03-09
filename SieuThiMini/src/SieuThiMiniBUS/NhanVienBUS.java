package SieuThiMiniBUS;
import DTO.NhanVienDTO;
import SieuThiMiniDAO.NhanVienDAO;

import java.util.ArrayList;
public class NhanVienBUS {
    public static ArrayList<NhanVienDTO> dsnv;
    public NhanVienBUS(){}
    public void docDSNV(){
        NhanVienDAO data = new NhanVienDAO();
        if(dsnv == null) dsnv = new ArrayList<NhanVienDTO>();
        dsnv = data.docDSNV();
    }
    public void themNV(NhanVienDTO nv){
        NhanVienDAO data = new NhanVienDAO();
        data.themNV(nv);
        dsnv.add(nv);
    }
    public void xoaNV(int ma) {
        NhanVienDAO data = new NhanVienDAO();
        data.xoaNV(ma);
        for(int i = 0; i < dsnv.size(); i++){
            if(dsnv.get(i).getMaNV() == (ma)){
                dsnv.remove(i);
            }
        }
    }
    public void suaNV(NhanVienDTO nv){
        NhanVienDAO data = new NhanVienDAO();
        data.suaNV(nv);
        for(int i = 0; i < dsnv.size(); i++){
            if(dsnv.get(i).getMaNV() == (nv.getMaNV())){
                dsnv.set(i,nv);
            }
        }
    }
}
