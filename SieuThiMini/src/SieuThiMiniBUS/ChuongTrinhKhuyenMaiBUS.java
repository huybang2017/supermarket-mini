package SieuThiMiniBUS;

import DTO.ChuongTrinhKhuyenMaiDTO;
import SieuThiMiniDAO.ChuongTrinhKhuyenMaiDAO;

import java.util.ArrayList;

public class ChuongTrinhKhuyenMaiBUS {
    public static ArrayList<ChuongTrinhKhuyenMaiDTO> dskm;
    
    public ChuongTrinhKhuyenMaiBUS() {}
    
    public void docDSKM() {
        ChuongTrinhKhuyenMaiDAO data = new ChuongTrinhKhuyenMaiDAO();
        if (dskm == null) dskm = new ArrayList<ChuongTrinhKhuyenMaiDTO>();
        dskm = data.docDSKM();
    }
    
    public void them(ChuongTrinhKhuyenMaiDTO km) {
        ChuongTrinhKhuyenMaiDAO data = new ChuongTrinhKhuyenMaiDAO();
        data.themKM(km);
        dskm.add(km);
    }
    
    public void xoa(int id) {
        ChuongTrinhKhuyenMaiDAO data = new ChuongTrinhKhuyenMaiDAO();
        data.xoaKM(id);
        for (int i = 0; i < dskm.size(); i++) {
            if (dskm.get(i).getId() == id) {
                dskm.remove(i);
                break;
            }
        }
    }
    
    public void sua(ChuongTrinhKhuyenMaiDTO km) {
        ChuongTrinhKhuyenMaiDAO data = new ChuongTrinhKhuyenMaiDAO();
        data.suaKM(km);
        for (int i = 0; i < dskm.size(); i++) {
            if (dskm.get(i).getId() == km.getId()) {
                dskm.set(i, km);
                break;
            }
        }
    }
}
