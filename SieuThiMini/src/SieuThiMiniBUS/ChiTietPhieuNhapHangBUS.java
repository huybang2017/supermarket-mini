package SieuThiMiniBUS;

import java.util.ArrayList;
import DTO.ChiTietPhieuNhapHangDTO;
import SieuThiMiniDAO.ChiTietPhieuNhapHangDAO;

public class ChiTietPhieuNhapHangBUS {
    public static ArrayList<ChiTietPhieuNhapHangDTO> dsctpn;
    private ChiTietPhieuNhapHangDAO data = new ChiTietPhieuNhapHangDAO();

    public ChiTietPhieuNhapHangBUS() {}

    public ArrayList<ChiTietPhieuNhapHangDTO> timTheoMaPN(int maPN) {
        if (dsctpn == null) dsctpn = new ArrayList<>();
        return data.docTheoMaPN(maPN);
    }

    public boolean them(ChiTietPhieuNhapHangDTO ct) {
        // TỰ ĐỘNG TÍNH THÀNH TIỀN (Chìa khóa để fix lỗi y như bên Hóa Đơn)
        ct.setThanhTien(ct.getSoLuong() * ct.getDonGia());
        
        boolean success = data.themCTPNH(ct);
        if (success && dsctpn != null) {
            dsctpn.add(ct);
        }
        return success;
    }

    public void xoa(int ma) {
        data.xoaCTPNH(ma);
        if (dsctpn != null) {
            dsctpn.removeIf(ct -> ct.getMaPNH() == ma);
        }
    }

    public void sua(int maPN, ArrayList<ChiTietPhieuNhapHangDTO> dsMoi) {
        data.xoaCTPNH(maPN);
        for (ChiTietPhieuNhapHangDTO ct : dsMoi) {
            // Tự động tính lại thành tiền trước khi thêm
            ct.setThanhTien(ct.getSoLuong() * ct.getDonGia());
            data.themCTPNH(ct);
        }
        
        ArrayList<ChiTietPhieuNhapHangDTO> dsSauKhiSua = data.docTheoMaPN(maPN);
        if (dsctpn != null) {
            dsctpn.removeIf(ct -> ct.getMaPNH() == maPN);
            dsctpn.addAll(dsSauKhiSua);
        }
    }
}