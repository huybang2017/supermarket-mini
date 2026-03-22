package SieuThiMiniBUS;

import java.util.ArrayList;

import DTO.NhaCungCapDTO;
import SieuThiMiniDAO.NhaCungCapDAO;

public class NhaCungCapBUS {
    public static ArrayList<NhaCungCapDTO> dsncc;

    public NhaCungCapBUS() {}

    public void docDSNCC() {
        NhaCungCapDAO data = new NhaCungCapDAO();
        // Bỏ if (dsncc == null) để luôn lấy dữ liệu tươi mới nhất từ Database
        dsncc = data.docDSNCC();
    }

    public void them(NhaCungCapDTO ncc) {
        NhaCungCapDAO data = new NhaCungCapDAO();
        data.themNCC(ncc);
        // Không cần dsncc.add(ncc) nữa vì lát sau GUI sẽ gọi docDSNCC() để lấy lại list mới
    }

    public void xoa(int ma) {
        NhaCungCapDAO data = new NhaCungCapDAO();
        data.xoaNCC(ma);
        // Không cần vòng lặp for để xóa thủ công trong mảng nữa
    }

    public void sua(NhaCungCapDTO ncc){
        NhaCungCapDAO data = new NhaCungCapDAO();
        data.suaNCC(ncc);
        // Không cần vòng lặp for để sửa thủ công trong mảng nữa
    }

    public boolean importExcel(NhaCungCapDTO ncc) {
        NhaCungCapDAO data = new NhaCungCapDAO();
        return data.importExcel(ncc);
    }
}
