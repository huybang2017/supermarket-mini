package SieuThiMiniGUI;

import DTO.*;
import SieuThiMiniBUS.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;

public class QuanLySanPhamGUI extends JPanel {
    
    // Khai báo các biến cục bộ chỉ dùng cho trang này
    private DefaultTableModel model;
    private JTable tblSanPham;
    
    // Bảng màu dùng chung cho giao diện này
    private Color primaryColor = new Color(0, 123, 255);
    private Color secondaryColor = new Color(108, 117, 125);
    private Color bgColor = new Color(244, 246, 249);

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
        lblTitle.setForeground(new Color(40, 40, 40));
        this.add(lblTitle, BorderLayout.NORTH);

        // --- BẮT ĐẦU PHẦN BODY (THANH CÔNG CỤ + BẢNG) ---
        JPanel card = new JPanel(new BorderLayout(15, 15));
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(new LineBorder(new Color(230, 230, 230), 1), new EmptyBorder(15, 15, 15, 15)));

        // 1. THANH CÔNG CỤ (HEADER)
        JPanel pnlHeader = new JPanel();
        pnlHeader.setLayout(new BoxLayout(pnlHeader, BoxLayout.Y_AXIS));
        pnlHeader.setOpaque(false);

        JPanel topToolBar = new JPanel(new BorderLayout());
        topToolBar.setOpaque(false);

        // Bộ tìm kiếm
        JPanel pnlSearchGroup = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlSearchGroup.setOpaque(false);
        JTextField txtSearch = new JTextField(" Tìm kiếm sản phẩm...");
        txtSearch.setPreferredSize(new Dimension(220, 38));
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtSearch.setForeground(Color.GRAY);
        txtSearch.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) { if (txtSearch.getText().trim().equals("Tìm kiếm sản phẩm...")) { txtSearch.setText(""); txtSearch.setForeground(Color.BLACK); } }
            public void focusLost(FocusEvent e) { if (txtSearch.getText().trim().isEmpty()) { txtSearch.setForeground(Color.GRAY); txtSearch.setText(" Tìm kiếm sản phẩm..."); } }
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
        btnAdd.addActionListener(e -> showForm(null)); // Logic nằm trong file này
        JButton btnDelete = createActionBtn("- Xóa Sản Phẩm");
        btnDelete.addActionListener(e -> deleteSelectedProduct()); // Logic nằm trong file này
        pnlButtons.add(btnAdd); 
        pnlButtons.add(btnDelete);
        topToolBar.add(pnlButtons, BorderLayout.EAST);

        // Panel Lọc Nâng Cao
        JPanel pnlAdvancedSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        pnlAdvancedSearch.setBackground(new Color(248, 250, 252));
        pnlAdvancedSearch.setBorder(new MatteBorder(1, 0, 0, 0, new Color(230, 230, 230)));
        pnlAdvancedSearch.setVisible(false);
        
        pnlAdvancedSearch.add(new JLabel("Giá từ:"));
        JTextField txtGiaTu = new JTextField(8);
        pnlAdvancedSearch.add(txtGiaTu);
        pnlAdvancedSearch.add(new JLabel("Đến:"));
        JTextField txtGiaDen = new JTextField(8);
        pnlAdvancedSearch.add(txtGiaDen);
        
        JButton btnApplyFilter = createActionBtn("Lọc");
        JButton btnResetFilter = createActionBtn("Làm Mới");
        btnResetFilter.addActionListener(e -> {
            txtSearch.setText(" Tìm kiếm sản phẩm...");
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
                String keyword = txtSearch.getText().toLowerCase().trim();
                if (keyword.equals("tìm kiếm sản phẩm...") || keyword.isEmpty()) { docDSSP(); return; }
                model.setRowCount(0);
                if (SanPhamBUS.dssp != null) {
                    for (SanPhamDTO sp : SanPhamBUS.dssp) {
                        if (String.valueOf(sp.getMasanpham()).contains(keyword) || sp.getTensanpham().toLowerCase().contains(keyword)) {
                            model.addRow(new Object[]{sp.getMasanpham(), sp.getTensanpham(), sp.getSoluong(), sp.getDongia(), sp.getDonvitinh(), "⚙ Sửa"});
                        }
                    }
                }
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
        scrollPane.getViewport().setBackground(Color.WHITE);
        card.add(scrollPane, BorderLayout.CENTER);

        this.add(card, BorderLayout.CENTER);
    }

    // --- CÁC HÀM XỬ LÝ LOGIC CHUYỂN HẾT VÀO ĐÂY ---
    
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
            // Chú ý: Component truyền vào JOptionPane.showConfirmDialog đổi thành 'this'
            if (JOptionPane.showConfirmDialog(this, "Xóa sản phẩm " + id + "?") == JOptionPane.YES_OPTION) {
                new SanPhamBUS().xoa(id);
                docDSSP();
            }
        }
    }

    private void showForm(SanPhamDTO sp) {
        Window parentWindow = SwingUtilities.getWindowAncestor(this);
        // Điều chỉnh kích thước Dialog gọn gàng hơn vì đã bớt cột
        JDialog dialog = new JDialog((Frame)parentWindow, sp == null ? "Thêm Sản Phẩm" : "Sửa Sản Phẩm", true);
        dialog.setSize(450, 400); 
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        // Sử dụng GridLayout 5 hàng (Mã, Tên, Loại, Hãng, ĐVT)
        JPanel pnlForm = new JPanel(new GridLayout(5, 2, 10, 20));
        pnlForm.setBorder(new EmptyBorder(30, 30, 30, 30));
        pnlForm.setBackground(Color.WHITE);

        // 1. Ô Mã Sản Phẩm: KHÓA CHỨC NĂNG GHI
        JTextField txtID = new JTextField(sp != null ? String.valueOf(sp.getMasanpham()) : "");
        txtID.setEditable(false); // Không cho phép chỉnh sửa
        txtID.setBackground(new Color(245, 245, 245)); // Đổi màu xám nhẹ để báo hiệu bị khóa
        
        JTextField txtTen = new JTextField(sp != null ? sp.getTensanpham() : "");
        JTextField txtDVT = new JTextField(sp != null ? sp.getDonvitinh() : "");

        // 2. ComboBox Loại Sản Phẩm
        LoaiSanPhamBUS lspBus = new LoaiSanPhamBUS();
        lspBus.docDSLSP();
        JComboBox<String> cbLoai = new JComboBox<>();
        cbLoai.addItem("-- Chọn Loại Sản Phẩm --");
        if (LoaiSanPhamBUS.dslsp != null) {
            for (LoaiSanPhamDTO lsp : LoaiSanPhamBUS.dslsp) {
                cbLoai.addItem(lsp.getMaLoai() + " - " + lsp.getTenLoai());
            }
        }

        // 3. ComboBox Hãng Sản Xuất
        HangSanXuatBUS hsxBus = new HangSanXuatBUS();
        hsxBus.docDSHSX();
        JComboBox<String> cbHang = new JComboBox<>();
        cbHang.addItem("-- Chọn Hãng Sản Xuất --");
        if (HangSanXuatBUS.dshsx != null) {
            for (HangSanXuatDTO hsx : HangSanXuatBUS.dshsx) {
                cbHang.addItem(hsx.getMaHang() + " - " + hsx.getTenHang());
            }
        }

        // Đổ dữ liệu cũ vào nếu là chế độ Sửa
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

        // 4. Nút Lưu
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
                
                // Gán giá trị mặc định cho các cột đã xóa trong Form để tránh lỗi logic
                newSp.setSoluong(sp != null ? sp.getSoluong() : 0);
                newSp.setDongia(sp != null ? sp.getDongia() : 0L);

                SanPhamBUS bus = new SanPhamBUS();
                if (sp == null) bus.them(newSp); else bus.sua(newSp);
                
                docDSSP();
                dialog.dispose();
            } catch (Exception ex) { 
                JOptionPane.showMessageDialog(dialog, "Lỗi dữ liệu: Vui lòng kiểm tra lại!"); 
            }
        });

        dialog.add(pnlForm, BorderLayout.CENTER);
        dialog.add(btnSave, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void styleTable(JTable t) {
        t.setRowHeight(45);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        t.setGridColor(new Color(245, 245, 245));
        t.setShowVerticalLines(false); // Bỏ lưới dọc giống giao diện cũ bạn thích
        t.setSelectionBackground(new Color(240, 247, 255));
        t.setSelectionForeground(Color.BLACK);
        t.getTableHeader().setReorderingAllowed(false);

        JTableHeader header = t.getTableHeader();
        header.setPreferredSize(new Dimension(0, 45));
        header.setBackground(new Color(250, 251, 252));
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setForeground(secondaryColor);
        ((DefaultTableCellRenderer)header.getDefaultRenderer()).setHorizontalAlignment(JLabel.LEFT);
    }


    private JButton createActionBtn(String text) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(140, 38));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBackground(new Color(226, 232, 240)); 
        btn.setForeground(Color.BLACK); 
        btn.setBorder(new LineBorder(new Color(203, 213, 225), 1)); 
        return btn;
    }
}