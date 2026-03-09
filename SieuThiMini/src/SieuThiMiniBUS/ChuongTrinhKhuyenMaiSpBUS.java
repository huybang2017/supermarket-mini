package SieuThiMiniBUS;

import DTO.ChuongTrinhKhuyenMaiSpDTO;
import SieuThiMiniDAO.ChuongTrinhKhuyenMaiSpDAO;

import java.util.ArrayList;

public class ChuongTrinhKhuyenMaiSpBUS {
    public static ArrayList<ChuongTrinhKhuyenMaiSpDTO> dskmsp;
    
    public ChuongTrinhKhuyenMaiSpBUS() {}
    
    public void docDSKMSP(int ctkm_id) {
        ChuongTrinhKhuyenMaiSpDAO data = new ChuongTrinhKhuyenMaiSpDAO();
        if (dskmsp == null) dskmsp = new ArrayList<ChuongTrinhKhuyenMaiSpDTO>();
        dskmsp = data.docDSKMSP(ctkm_id);
    }
    
    public void docTatCa() {
        ChuongTrinhKhuyenMaiSpDAO data = new ChuongTrinhKhuyenMaiSpDAO();
        if (dskmsp == null) dskmsp = new ArrayList<ChuongTrinhKhuyenMaiSpDTO>();
        dskmsp = data.docTatCa();
    }
    
    public void them(ChuongTrinhKhuyenMaiSpDTO kmsp) {
        ChuongTrinhKhuyenMaiSpDAO data = new ChuongTrinhKhuyenMaiSpDAO();
        data.themKMSP(kmsp);
        dskmsp.add(kmsp);
    }
    
    public void xoa(int ctkm_id, int sp_id) {
        ChuongTrinhKhuyenMaiSpDAO data = new ChuongTrinhKhuyenMaiSpDAO();
        data.xoaKMSP(ctkm_id, sp_id);
        
        // Thêm if (dskmsp != null) để chặn lỗi văng app
        if (dskmsp != null) { 
            for (int i = 0; i < dskmsp.size(); i++) {
                if (dskmsp.get(i).getChuongTrinhKhuyenMaiId() == ctkm_id 
                    && dskmsp.get(i).getSanPhamId() == sp_id) {
                    dskmsp.remove(i);
                    break;
                }
            }
        }
    }
    
    public void xoaTheoKM(int ctkm_id) {
        ChuongTrinhKhuyenMaiSpDAO data = new ChuongTrinhKhuyenMaiSpDAO();
        data.xoaTheoKM(ctkm_id);
        
        // Thêm if (dskmsp != null) để chặn lỗi văng app
        if (dskmsp != null) {
            dskmsp.removeIf(kmsp -> kmsp.getChuongTrinhKhuyenMaiId() == ctkm_id);
        }
    }    
    public void sua(ChuongTrinhKhuyenMaiSpDTO kmsp) {
        ChuongTrinhKhuyenMaiSpDAO data = new ChuongTrinhKhuyenMaiSpDAO();
        data.suaKMSP(kmsp);
        for (int i = 0; i < dskmsp.size(); i++) {
            if (dskmsp.get(i).getChuongTrinhKhuyenMaiId() == kmsp.getChuongTrinhKhuyenMaiId()
                && dskmsp.get(i).getSanPhamId() == kmsp.getSanPhamId()) {
                dskmsp.set(i, kmsp);
                break;
            }
        }
    }
}
