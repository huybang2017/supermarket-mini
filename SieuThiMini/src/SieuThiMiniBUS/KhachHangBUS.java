
package SieuThiMiniBUS;
import DTO.KhachHangDTO;
import SieuThiMiniDAO.KhachHangDAO;

import java.util.ArrayList;
public class KhachHangBUS {
    public static ArrayList<KhachHangDTO> dskh;
    public KhachHangBUS(){}
    public void docDSKH(){
        KhachHangDAO data = new KhachHangDAO();
        if(dskh == null) dskh = new ArrayList<KhachHangDTO>();
        dskh = data.docDSKH();
    }
    public void themKH(KhachHangDTO kh){
        KhachHangDAO data = new KhachHangDAO();
        data.themKH(kh);
        dskh.add(kh);
    }
    public void xoaKH(int ma) {
        KhachHangDAO data = new KhachHangDAO();
        data.xoaKH(ma);
        for(int i = 0; i < dskh.size(); i++){
            if(dskh.get(i).getMaKH() == (ma)){
                dskh.remove(i);
            }
        }
    }
    public void suaKH(KhachHangDTO kh){
        KhachHangDAO data = new KhachHangDAO();
        data.suaKH(kh);
        for(int i = 0; i < dskh.size(); i++){
            if(dskh.get(i).getMaKH() == (kh.getMaKH())){
                dskh.set(i,kh);
            }
        }
    }

    public boolean importExcel(KhachHangDTO kh) {
        KhachHangDAO data = new KhachHangDAO();
        return data.importExcel(kh);
    }
    
    public ArrayList<KhachHangDTO> timKhachHang(String keyword) {
        KhachHangDAO data = new KhachHangDAO();
        return data.timKhachHang(keyword);
    }
    public ArrayList<KhachHangDTO> timKhachHangTheoMa(int maTu, int maDen) {
        KhachHangDAO data = new KhachHangDAO();
        return data.timKhachHangTheoMa(maTu, maDen);
    }
}
