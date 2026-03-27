package SieuThiMiniBUS;

import java.util.ArrayList;

import DTO.NhaCungCapDTO;
import SieuThiMiniDAO.NhaCungCapDAO;

public class NhaCungCapBUS {
    public static ArrayList<NhaCungCapDTO> dsncc;

    public NhaCungCapBUS() {}

    public void docDSNCC() {
        NhaCungCapDAO data = new NhaCungCapDAO();
        dsncc = data.docDSNCC();
    }

    public void them(NhaCungCapDTO ncc) {
        NhaCungCapDAO data = new NhaCungCapDAO();
        data.themNCC(ncc);
    }

    public void xoa(int ma) {
        NhaCungCapDAO data = new NhaCungCapDAO();
        data.xoaNCC(ma);
    }

    public void sua(NhaCungCapDTO ncc){
        NhaCungCapDAO data = new NhaCungCapDAO();
        data.suaNCC(ncc);
    }

    public boolean importExcel(NhaCungCapDTO ncc) {
        NhaCungCapDAO data = new NhaCungCapDAO();
        return data.importExcel(ncc);
    }

    public ArrayList<NhaCungCapDTO> timNhaCungCap(String keyword) {
        NhaCungCapDAO data = new NhaCungCapDAO();
        return data.timNhaCungCap(keyword);
    }
}
