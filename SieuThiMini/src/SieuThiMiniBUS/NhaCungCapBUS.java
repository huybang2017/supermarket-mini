package SieuThiMiniBUS;

import java.util.ArrayList;

import DTO.NhaCungCapDTO;
import SieuThiMiniDAO.NhaCungCapDAO;

public class NhaCungCapBUS {
    public static ArrayList<NhaCungCapDTO> dsncc;

    public NhaCungCapBUS() {}

    public void docDSNCC() {
        NhaCungCapDAO data = new NhaCungCapDAO();
        if (dsncc == null) {
            dsncc = data.docDSNCC();
        }
    }

    public void them(NhaCungCapDTO ncc) {
        NhaCungCapDAO data = new NhaCungCapDAO();
        data.themNCC(ncc);
        dsncc.add(ncc);
    }

    public void xoa(String ma) {
        NhaCungCapDAO data = new NhaCungCapDAO();
        data.xoaNCC(ma);
        for(int i = 0; i < dsncc.size(); i++){
            if(dsncc.get(i).getMaNCC().equals(ma)){
                dsncc.remove(i);
            }
        }
    }

    public void sua(NhaCungCapDTO ncc){
        NhaCungCapDAO data = new NhaCungCapDAO();
        data.suaNCC(ncc);
        for(int i = 0; i < dsncc.size(); i++){
            if(dsncc.get(i).getMaNCC().equals(ncc.getMaNCC())){
                dsncc.set(i,ncc);
            }
        }
    }
}