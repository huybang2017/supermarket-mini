package SieuThiMiniBUS;

import java.util.ArrayList;

import DTO.PhieuNhapHangDTO;
import SieuThiMiniDAO.PhieuNhapHangDAO;

public class PhieuNhapHangBUS {
    public static ArrayList<PhieuNhapHangDTO> dspn = null;

    public PhieuNhapHangBUS() {}

    public void docDSPN() {
        PhieuNhapHangDAO data = new PhieuNhapHangDAO();
        if (dspn == null) {
            dspn = data.docDSPNH();
        }
    }

    public void them(PhieuNhapHangDTO pn) {
        PhieuNhapHangDAO data = new PhieuNhapHangDAO();
        data.themPNH(pn);
        if (dspn != null) {
            dspn.add(pn);
        }
    }

    public void xoa(int ma) {
        PhieuNhapHangDAO data = new PhieuNhapHangDAO();
        data.xoaPNH(ma);
        for(int i = 0; i < dspn.size(); i++){
            if(dspn.get(i).getMaPNH() == ma){
                dspn.remove(i);
            }
        }
    }

    public void sua(PhieuNhapHangDTO pn){
        PhieuNhapHangDAO data = new PhieuNhapHangDAO();
        data.suaPNH(pn);
        for(int i = 0; i < dspn.size(); i++){
            if(dspn.get(i).getMaPNH() ==(pn.getMaPNH())){
                dspn.set(i,pn);
            }
        }
    }

}