package SieuThiMiniBUS;

import java.util.ArrayList;

import DTO.ChiTietPhieuNhapHangDTO;
import SieuThiMiniDAO.ChiTietPhieuNhapHangDAO;

public class ChiTietPhieuNhapHangBUS {
    public static ArrayList<ChiTietPhieuNhapHangDTO> dsctpn;

    public ChiTietPhieuNhapHangBUS() {}

    public ArrayList<ChiTietPhieuNhapHangDTO> timTheoMaPN(int maPN) {
        ChiTietPhieuNhapHangDAO data = new ChiTietPhieuNhapHangDAO();
        if (dsctpn == null) dsctpn = new ArrayList<ChiTietPhieuNhapHangDTO>();
        return data.docTheoMaPN(maPN);
    }

    public void them(ChiTietPhieuNhapHangDTO ct) {
        ChiTietPhieuNhapHangDAO data = new ChiTietPhieuNhapHangDAO();
        data.themCTPNH(ct);
    }

    public void xoa(int ma) {
        ChiTietPhieuNhapHangDAO data = new ChiTietPhieuNhapHangDAO();
        data.xoaCTPNH(ma);
        for(int i = 0; i < dsctpn.size(); i++){
            if(dsctpn.get(i).getMaPNH() == (ma)){
                dsctpn.remove(i);
            }
        }
    }

    public void sua(ChiTietPhieuNhapHangDTO ct){
        ChiTietPhieuNhapHangDAO data = new ChiTietPhieuNhapHangDAO();
        data.suaCTPNH(ct);
        for(int i = 0; i < dsctpn.size(); i++){
            if(dsctpn.get(i).getMaPNH()==(ct.getMaPNH())){
                dsctpn.set(i,ct);
            }
        }
    }
}