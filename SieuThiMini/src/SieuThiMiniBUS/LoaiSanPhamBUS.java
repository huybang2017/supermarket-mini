package SieuThiMiniBUS;

import DTO.LoaiSanPhamDTO;
import SieuThiMiniDAO.LoaiSanPhamDAO;
import java.util.ArrayList;

public class LoaiSanPhamBUS {
    public static ArrayList<LoaiSanPhamDTO> dslsp;
    private LoaiSanPhamDAO data = new LoaiSanPhamDAO();

    public LoaiSanPhamBUS() {}

    public void docDSLSP() {
        if (dslsp == null) dslsp = new ArrayList<>();
        dslsp = data.docDSLSP();
    }

    public boolean them(LoaiSanPhamDTO lsp) {
        if (data.themLSP(lsp)) {
            docDSLSP(); // Load lại danh sách để lấy ID tự tăng từ database
            return true;
        }
        return false;
    }

    public boolean xoa(int ma) {
        if (data.xoaLSP(ma)) {
            dslsp.removeIf(lsp -> lsp.getMaLoai() == ma);
            return true;
        }
        return false;
    }

    public boolean sua(LoaiSanPhamDTO lspMoi) {
        if (data.suaLSP(lspMoi)) {
            for (int i = 0; i < dslsp.size(); i++) {
                if (dslsp.get(i).getMaLoai() == lspMoi.getMaLoai()) {
                    dslsp.set(i, lspMoi);
                    break;
                }
            }
            return true;
        }
        return false;
    }
    
    // Hàm hỗ trợ lấy 1 DTO theo mã
    public LoaiSanPhamDTO getLoaiSP(int ma) {
        for (LoaiSanPhamDTO lsp : dslsp) {
            if (lsp.getMaLoai() == ma) return lsp;
        }
        return null;
    }
}