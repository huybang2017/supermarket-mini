package SieuThiMiniGUI;

import DTO.*;
import SieuThiMiniBUS.*;

// Import bộ thư viện giao diện (Được phép dùng *)
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.stream.Collectors;
import javax.swing.filechooser.FileNameExtensionFilter;

// Import thư viện Excel (TUYỆT ĐỐI KHÔNG DÙNG DẤU *, CHỈ IMPORT CLASS CỤ THỂ)
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class QuanLySanPhamGUI extends JPanel {
    
    // Khai báo các biến cục bộ chỉ dùng cho trang này
    private DefaultTableModel model;
    private JTable tblSanPham;
    private JTextField txtSearch;
    // Bảng màu dùng chung cho giao diện này
    private final java.awt.Color primaryColor = new java.awt.Color(0, 123, 255);
    private final java.awt.Color secondaryColor = new java.awt.Color(108, 117, 125);
    private final java.awt.Color bgColor = new java.awt.Color(244, 246, 249);

    // Constructor: Chạy ngay khi class được gọi
    public QuanLySanPhamGUI() {
        initComponents();
        docDSSP(); // Đọc dữ liệu lên bảng khi vừa mở
        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                docDSSP(); // Tải lại danh sách sản phẩm mới nhất
            }
        });
    }

    // Hàm khởi tạo toàn bộ giao diện
    private void initComponents() {
        // Thiết lập layout và màu nền cho CHÍNH PANEL NÀY (this)
        this.setLayout(new BorderLayout(20, 20));
        this.setBackground(bgColor);
        this.setBorder(new EmptyBorder(20, 25, 20, 25));

        JLabel lblTitle = new JLabel("Quản Lý Sản Phẩm");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new java.awt.Color(40, 40, 40));
        this.add(lblTitle, BorderLayout.NORTH);

        // --- BẮT ĐẦU PHẦN BODY (THANH CÔNG CỤ + BẢNG) ---
        JPanel card = new JPanel(new BorderLayout(15, 15));
        card.setBackground(java.awt.Color.WHITE);
        card.setBorder(new CompoundBorder(new LineBorder(new java.awt.Color(230, 230, 230), 1), new EmptyBorder(15, 15, 15, 15)));

        // 1. THANH CÔNG CỤ (HEADER)
        JPanel pnlHeader = new JPanel();
        pnlHeader.setLayout(new BoxLayout(pnlHeader, BoxLayout.Y_AXIS));
        pnlHeader.setOpaque(false);

        JPanel topToolBar = new JPanel(new BorderLayout());
        topToolBar.setOpaque(false);

        // Bộ tìm kiếm
        JPanel pnlSearchGroup = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlSearchGroup.setOpaque(false);
        txtSearch = new JTextField(" Tìm kiếm sản phẩm...");
        txtSearch.setPreferredSize(new Dimension(220, 38));
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtSearch.setForeground(java.awt.Color.GRAY);
        txtSearch.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) { if (txtSearch.getText().trim().equals("Tìm kiếm sản phẩm...")) { txtSearch.setText(""); txtSearch.setForeground(java.awt.Color.BLACK); } }
            public void focusLost(FocusEvent e) { if (txtSearch.getText().trim().isEmpty()) { txtSearch.setForeground(java.awt.Color.GRAY); txtSearch.setText(" Tìm kiếm sản phẩm..."); } }
        });
        
        JButton btnAdvancedToggle = createActionBtn("▼");
        btnAdvancedToggle.setPreferredSize(new Dimension(45, 38));
        pnlSearchGroup.add(txtSearch);
        pnlSearchGroup.add(btnAdvancedToggle);
        topToolBar.add(pnlSearchGroup, BorderLayout.WEST);

        // Các nút bấm
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlButtons.setOpaque(false);
        JButton btnAdd = createActionBtn("+ Thêm");
        btnAdd.addActionListener(e -> showForm(null));
        JButton btnDelete = createActionBtn("- Xóa Sản Phẩm");
        btnDelete.addActionListener(e -> deleteSelectedProduct());
        
        pnlButtons.add(btnAdd); 
        pnlButtons.add(btnDelete);
        topToolBar.add(pnlButtons, BorderLayout.EAST);

        // Panel Lọc Nâng Cao
        JPanel pnlAdvancedSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        pnlAdvancedSearch.setBackground(new java.awt.Color(248, 250, 252));
        pnlAdvancedSearch.setBorder(new MatteBorder(1, 0, 0, 0, new java.awt.Color(230, 230, 230)));
        pnlAdvancedSearch.setVisible(false);
        
        pnlAdvancedSearch.add(new JLabel("Giá từ:"));
        JTextField txtGiaTu = new JTextField(8);
        pnlAdvancedSearch.add(txtGiaTu);
        pnlAdvancedSearch.add(new JLabel("Đến:"));
        JTextField txtGiaDen = new JTextField(8);
        pnlAdvancedSearch.add(txtGiaDen);
        
        JButton btnApplyFilter = createActionBtn("Lọc");
        btnApplyFilter.addActionListener(e -> {
            try {
                String strTu = txtGiaTu.getText().trim();
                String strDen = txtGiaDen.getText().trim();

                // Nếu để trống ô "Giá từ" thì mặc định là 0
                long giaTu = strTu.isEmpty() ? 0 : Long.parseLong(strTu);
                // Nếu để trống ô "Giá đến" thì mặc định là giá trị lớn nhất
                long giaDen = strDen.isEmpty() ? Long.MAX_VALUE : Long.parseLong(strDen);

                if (giaTu > giaDen) {
                    JOptionPane.showMessageDialog(this, "Giá bắt đầu không được lớn hơn giá kết thúc!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Gọi BUS để tìm kiếm
                SanPhamBUS spBUS = new SanPhamBUS();
                ArrayList<SanPhamDTO> dsKetQua = spBUS.timSanPhamTheoGia(giaTu, giaDen);

                // Cập nhật lại bảng
                model.setRowCount(0); 
                if (dsKetQua != null) {
                    for (SanPhamDTO sp : dsKetQua) {
                        model.addRow(new Object[]{sp.getMasanpham(), sp.getTensanpham(), sp.getSoluong(), sp.getDongia(), sp.getDonvitinh(), "⚙ Sửa"});
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập số hợp lệ vào ô giá tiền!", "Lỗi Nhập Liệu", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        JButton btnResetFilter = createActionBtn("Làm Mới");
        btnResetFilter.addActionListener(e -> {
            txtSearch.setText(" Tìm kiếm sản phẩm...");
            txtSearch.setForeground(java.awt.Color.GRAY);
            txtGiaTu.setText("");
            txtGiaDen.setText("");
            docDSSP();
        });
        pnlAdvancedSearch.add(btnApplyFilter);
        pnlAdvancedSearch.add(btnResetFilter);

        pnlHeader.add(topToolBar);
        pnlHeader.add(Box.createVerticalStrut(10));
        pnlHeader.add(pnlAdvancedSearch);
        card.add(pnlHeader, BorderLayout.NORTH);

        // Sự kiện sổ xuống
        btnAdvancedToggle.addActionListener(e -> {
            boolean isVisible = pnlAdvancedSearch.isVisible();
            pnlAdvancedSearch.setVisible(!isVisible);
            btnAdvancedToggle.setText(isVisible ? "▼" : "▲");
            card.revalidate();
        });

        // Tìm kiếm Real-time
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                searchSanPham(txtSearch.getText().trim());
            }
        });
            
        // 2. KHU VỰC BẢNG DỮ LIỆU
        String[] headers = {"ID", "Tên Sản Phẩm", "Số Lượng", "Đơn Giá", "Đơn Vị Tính", "Hành Động"};
        model = new DefaultTableModel(headers, 0) { @Override public boolean isCellEditable(int r, int c) { return false; } };
        tblSanPham = new JTable(model);
        styleTable(tblSanPham);

        tblSanPham.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = tblSanPham.getSelectedRow();
                    int id = Integer.parseInt(tblSanPham.getValueAt(row, 0).toString());
                    for (SanPhamDTO sp : SanPhamBUS.dssp) { if (sp.getMasanpham() == (id)) { showForm(sp); break; } }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(tblSanPham);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(java.awt.Color.WHITE);
        card.add(scrollPane, BorderLayout.CENTER);

        this.add(card, BorderLayout.CENTER);
    }

    // --- CÁC HÀM XỬ LÝ LOGIC ---
    
    private void docDSSP() {
        new SanPhamBUS().docDSSP();
        model.setRowCount(0);
        if(SanPhamBUS.dssp != null) {
            for (SanPhamDTO sp : SanPhamBUS.dssp) {
                model.addRow(new Object[]{sp.getMasanpham(), sp.getTensanpham(), sp.getSoluong(), sp.getDongia(), sp.getDonvitinh(), "⚙ Sửa"});
            }
        }
    }

    private void deleteSelectedProduct() {
        int row = tblSanPham.getSelectedRow();
        if (row != -1) {
            int id = Integer.parseInt(tblSanPham.getValueAt(row, 0).toString());
            if (JOptionPane.showConfirmDialog(this, "Xóa sản phẩm " + id + "?") == JOptionPane.YES_OPTION) {
                new SanPhamBUS().xoa(id);
                docDSSP();
            }
        }
    }

    // ĐÃ SỬA LỖI ĐÓNG MỞ NGOẶC Ở HÀM NÀY
    private void showForm(SanPhamDTO sp) {
        Window parentWindow = SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog((Frame)parentWindow, sp == null ? "Thêm Sản Phẩm" : "Sửa Sản Phẩm", true);
        dialog.setSize(450, 400); 
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel pnlForm = new JPanel(new GridLayout(5, 2, 10, 20));
        pnlForm.setBorder(new EmptyBorder(30, 30, 30, 30));
        pnlForm.setBackground(java.awt.Color.WHITE);

        JTextField txtID = new JTextField(sp != null ? String.valueOf(sp.getMasanpham()) : "");
        txtID.setEditable(false); 
        txtID.setBackground(new java.awt.Color(245, 245, 245)); 
        
        JTextField txtTen = new JTextField(sp != null ? sp.getTensanpham() : "");
        JTextField txtDVT = new JTextField(sp != null ? sp.getDonvitinh() : "");

        LoaiSanPhamBUS lspBus = new LoaiSanPhamBUS();
        lspBus.docDSLSP();
        JComboBox<String> cbLoai = new JComboBox<>();
        cbLoai.addItem("-- Chọn Loại Sản Phẩm --");
        if (LoaiSanPhamBUS.dslsp != null) {
            for (LoaiSanPhamDTO lsp : LoaiSanPhamBUS.dslsp) {
                cbLoai.addItem(lsp.getMaLoai() + " - " + lsp.getTenLoai());
            }
        }

        HangSanXuatBUS hsxBus = new HangSanXuatBUS();
        hsxBus.docDSHSX();
        JComboBox<String> cbHang = new JComboBox<>();
        cbHang.addItem("-- Chọn Hãng Sản Xuất --");
        if (HangSanXuatBUS.dshsx != null) {
            for (HangSanXuatDTO hsx : HangSanXuatBUS.dshsx) {
                cbHang.addItem(hsx.getMaHang() + " - " + hsx.getTenHang());
            }
        }

        if (sp != null) {
            for (int i = 0; i < cbLoai.getItemCount(); i++) {
                if (cbLoai.getItemAt(i).startsWith(String.valueOf(sp.getMaLoai()))) {
                    cbLoai.setSelectedIndex(i);
                    break;
                }
            }
            for (int i = 0; i < cbHang.getItemCount(); i++) {
                if (cbHang.getItemAt(i).startsWith(String.valueOf(sp.getMaHang()))) {
                    cbHang.setSelectedIndex(i);
                    break;
                }
            }
        }

        pnlForm.add(new JLabel("Mã Sản Phẩm:")); pnlForm.add(txtID);
        pnlForm.add(new JLabel("Tên Sản Phẩm:")); pnlForm.add(txtTen);
        pnlForm.add(new JLabel("Loại SP:")); pnlForm.add(cbLoai);
        pnlForm.add(new JLabel("Hãng SX:")); pnlForm.add(cbHang);
        pnlForm.add(new JLabel("Đơn Vị Tính:")); pnlForm.add(txtDVT);

        JButton btnSave = createActionBtn(sp == null ? "Thêm Mới" : "Lưu Thay Đổi");
        btnSave.addActionListener(e -> {
            try {
                if (cbLoai.getSelectedIndex() == 0 || cbHang.getSelectedIndex() == 0 || txtTen.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Vui lòng nhập đầy đủ thông tin!");
                    return;
                }

                SanPhamDTO newSp = new SanPhamDTO();
                if (sp != null) {
                    newSp.setMasanpham(Integer.parseInt(txtID.getText()));
                }
                newSp.setTensanpham(txtTen.getText().trim());
                newSp.setMaLoai(Integer.parseInt(cbLoai.getSelectedItem().toString().split(" - ")[0]));
                newSp.setMaHang(Integer.parseInt(cbHang.getSelectedItem().toString().split(" - ")[0]));
                newSp.setDonvitinh(txtDVT.getText().trim());
                
                newSp.setSoluong(sp != null ? sp.getSoluong() : 0);
                newSp.setDongia(sp != null ? sp.getDongia() : 0L);

                SanPhamBUS bus = new SanPhamBUS();
                if (sp == null) {
                    bus.them(newSp);
                    JOptionPane.showMessageDialog(dialog, "Thêm sản phẩm thành công!");
                } else {
                    bus.sua(newSp);
                    JOptionPane.showMessageDialog(dialog, "Sửa sản phẩm thành công!");
                }
                
                docDSSP();
                dialog.dispose();
            } catch (Exception ex) { 
                JOptionPane.showMessageDialog(dialog, "Lỗi dữ liệu: Vui lòng kiểm tra lại!"); 
            }
        });

        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlBottom.setBackground(java.awt.Color.WHITE);
        pnlBottom.add(btnSave);

        dialog.add(pnlForm, BorderLayout.CENTER);
        dialog.add(pnlBottom, BorderLayout.SOUTH);
        dialog.setVisible(true);
    } // HẾT HÀM SHOWFORM

    private void styleTable(JTable t) {
        t.setRowHeight(45);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        t.setGridColor(new java.awt.Color(245, 245, 245));
        t.setShowVerticalLines(false); 
        t.setSelectionBackground(new java.awt.Color(240, 247, 255));
        t.setSelectionForeground(java.awt.Color.BLACK);
        t.getTableHeader().setReorderingAllowed(false);

        JTableHeader header = t.getTableHeader();
        header.setPreferredSize(new Dimension(0, 45));
        header.setBackground(new java.awt.Color(250, 251, 252));
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setForeground(secondaryColor);
        ((DefaultTableCellRenderer)header.getDefaultRenderer()).setHorizontalAlignment(JLabel.LEFT);
    }

    private JButton createActionBtn(String text) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(140, 38));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBackground(new java.awt.Color(226, 232, 240)); 
        btn.setForeground(java.awt.Color.BLACK); 
        btn.setBorder(new LineBorder(new java.awt.Color(203, 213, 225), 1)); 
        return btn;
    }


    
    private void searchSanPham(String keyword){
        SanPhamBUS bus = new SanPhamBUS();
        SanPhamDTO spt = new SanPhamDTO();
        bus.timSanPham(keyword);
        model.setRowCount(0); 
        model.addRow(new Object[]{
        spt.getMasanpham(),
        spt.getTensanpham(),
        spt.getSoluong(),
        spt.getDongia(),
        spt.getDonvitinh(),
        });
    }


}
