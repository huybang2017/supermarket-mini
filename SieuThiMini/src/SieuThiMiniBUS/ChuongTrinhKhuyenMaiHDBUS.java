package SieuThiMiniBUS;

import DTO.ChuongTrinhKhuyenMaiHDDTO;
import SieuThiMiniDAO.ChuongTrinhKhuyenMaiHDDAO;

import java.util.ArrayList;

public class ChuongTrinhKhuyenMaiHDBUS {
    public static ArrayList<ChuongTrinhKhuyenMaiHDDTO> dskmhd;
    
    public ChuongTrinhKhuyenMaiHDBUS() {}
    
    public void docDSKMHD(int ctkm_id) {
        ChuongTrinhKhuyenMaiHDDAO data = new ChuongTrinhKhuyenMaiHDDAO();
        if (dskmhd == null) dskmhd = new ArrayList<ChuongTrinhKhuyenMaiHDDTO>();
        dskmhd = data.docDSKMHD(ctkm_id);
    }
    
    public void docTatCa() {
        ChuongTrinhKhuyenMaiHDDAO data = new ChuongTrinhKhuyenMaiHDDAO();
        if (dskmhd == null) dskmhd = new ArrayList<ChuongTrinhKhuyenMaiHDDTO>();
        dskmhd = data.docTatCa();
    }
    
    public void them(ChuongTrinhKhuyenMaiHDDTO kmhd) {
        ChuongTrinhKhuyenMaiHDDAO data = new ChuongTrinhKhuyenMaiHDDAO();
        data.themKMHD(kmhd);
        dskmhd.add(kmhd);
    }
    
    public void xoa(int ctkm_id, int soTienHd) {
        ChuongTrinhKhuyenMaiHDDAO data = new ChuongTrinhKhuyenMaiHDDAO();
        data.xoaKMHD(ctkm_id, soTienHd);
        for (int i = 0; i < dskmhd.size(); i++) {
            if (dskmhd.get(i).getChuongTrinhKhuyenMaiId() == ctkm_id 
                && dskmhd.get(i).getSoTienHd() == soTienHd) {
                dskmhd.remove(i);
                break;
            }
        }
    }
    
    public void xoaTheoKM(int ctkm_id) {
        ChuongTrinhKhuyenMaiHDDAO data = new ChuongTrinhKhuyenMaiHDDAO();
        data.xoaTheoKM(ctkm_id);
        dskmhd.removeIf(kmhd -> kmhd.getChuongTrinhKhuyenMaiId() == ctkm_id);
    }
    
    public void sua(ChuongTrinhKhuyenMaiHDDTO kmhd) {
        ChuongTrinhKhuyenMaiHDDAO data = new ChuongTrinhKhuyenMaiHDDAO();
        data.suaKMHD(kmhd);
        for (int i = 0; i < dskmhd.size(); i++) {
            if (dskmhd.get(i).getChuongTrinhKhuyenMaiId() == kmhd.getChuongTrinhKhuyenMaiId()
                && dskmhd.get(i).getSoTienHd() == kmhd.getSoTienHd()) {
                dskmhd.set(i, kmhd);
                break;
            }
        }
    }
}
