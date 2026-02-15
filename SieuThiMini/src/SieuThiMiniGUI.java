package SieuThiMiniGUI;

import DTO.*;
import SieuThiMiniBUS.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

public class SieuThiMiniGUI extends JFrame {
    private Color primaryColor = new Color(0, 123, 255);
    private Color secondaryColor = new Color(108, 117, 125);
    private Color bgColor = new Color(244, 246, 249);
    private Color sidebarBg = Color.WHITE;

    private DefaultTableModel model;
    private JTable tblSanPham;
    private JPanel pnlMainContent;
    private CardLayout cardLayout = new CardLayout();

    public SieuThiMiniGUI() {
        setTitle("Hệ Thống Quản Lý Siêu Thị Mini");
        setSize(1300, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        initSidebar();
        initMainContent();
        docDSSP();
    }

    private void initSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(250, 0));
        sidebar.setBackground(sidebarBg);
        sidebar.setBorder(new MatteBorder(0, 0, 0, 1, new Color(220, 220, 220)));

        JLabel lblLogo = new JLabel("Admin Panel");
        lblLogo.setForeground(primaryColor);
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblLogo.setBorder(new EmptyBorder(30, 25, 30, 10));
        lblLogo.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(lblLogo);

        String[] menus = {"Trang Tổng Quan", "Loại Sản Phẩm", "Quản Lý Sản Phẩm", "Quản Lý Nhập Hàng", "Quản Lý Giá Bán", "Quản Lý Đơn Hàng", "Quản Lý Khách Hàng", "Thống Kê & Báo Cáo"};

        for (String m : menus) {
            JButton btn = createSidebarBtn(m);
            if (m.equals("Quản Lý Sản Phẩm")) {
                btn.setBackground(new Color(240, 247, 255));
                btn.setForeground(primaryColor);
                btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
            }
            sidebar.add(btn);
        }
        add(sidebar, BorderLayout.WEST);
    }

    private JButton createSidebarBtn(String text) {
        JButton btn = new JButton("  " + text);
        btn.setMaximumSize(new Dimension(250, 50));
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        btn.setBackground(sidebarBg);
        btn.setForeground(new Color(70, 70, 70));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        return btn;
    }

    private JPanel createSanPhamPanel() {
        JPanel pnl = new JPanel(new BorderLayout(20, 20));
        pnl.setBackground(bgColor);
        pnl.setBorder(new EmptyBorder(20, 25, 20, 25));

        JLabel lblTitle = new JLabel("Quản Lý Sản Phẩm");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(40, 40, 40));
        pnl.add(lblTitle, BorderLayout.NORTH);

        JPanel card = new JPanel(new BorderLayout(15, 15));
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(new LineBorder(new Color(230, 230, 230), 1), new EmptyBorder(15, 15, 15, 15)));

        JPanel topToolBar = new JPanel(new BorderLayout());
        topToolBar.setOpaque(false);
        JTextField txtSearch = new JTextField(" Tìm kiếm sản phẩm...");
        txtSearch.setPreferredSize(new Dimension(250, 38));
        topToolBar.add(txtSearch, BorderLayout.WEST);

        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlButtons.setOpaque(false);

        JButton btnDocDB = createActionBtn("Đọc Dữ Liệu", false);
        btnDocDB.addActionListener(e -> docDSSP());

        JButton btnAdd = createActionBtn("+ Thêm", true);
        btnAdd.addActionListener(e -> showForm(null));

        JButton btnDelete = createActionBtn("- Xóa Sản Phẩm", false);
        btnDelete.setForeground(Color.RED);
        btnDelete.addActionListener(e -> deleteSelectedProduct());

        pnlButtons.add(btnDocDB); pnlButtons.add(btnAdd); pnlButtons.add(btnDelete);
        topToolBar.add(pnlButtons, BorderLayout.EAST);
        card.add(topToolBar, BorderLayout.NORTH);

        // Header và Model Table
        String[] headers = {"ID", "Tên Sản Phẩm", "Số Lượng", "Đơn Giá", "Đơn Vị Tính", "Hành Động"};
        model = new DefaultTableModel(headers, 0) { @Override public boolean isCellEditable(int r, int c) { return false; } };
        tblSanPham = new JTable(model);
        styleTable(tblSanPham);

        tblSanPham.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = tblSanPham.getSelectedRow();
                    String id = tblSanPham.getValueAt(row, 0).toString();
                    for (SanPhamDTO sp : SanPhamBUS.dssp) {
                        if (sp.getMasanpham().equals(id)) { showForm(sp); break; }
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(tblSanPham);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        card.add(scrollPane, BorderLayout.CENTER);

        pnl.add(card, BorderLayout.CENTER);
        return pnl;
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

    private JButton createActionBtn(String text, boolean isPrimary) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(140, 38));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        if (isPrimary) { btn.setBackground(primaryColor); btn.setForeground(Color.WHITE); }
        else { btn.setBackground(Color.WHITE); btn.setForeground(primaryColor); btn.setBorder(new LineBorder(primaryColor, 1)); }
        return btn;
    }

    private void initMainContent() {
        pnlMainContent = new JPanel(cardLayout);
        pnlMainContent.add(createSanPhamPanel(), "Sản Phẩm");
        add(pnlMainContent, BorderLayout.CENTER);
    }

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
            String id = tblSanPham.getValueAt(row, 0).toString();
            if (JOptionPane.showConfirmDialog(this, "Xóa sản phẩm " + id + "?") == JOptionPane.YES_OPTION) {
                new SanPhamBUS().xoa(id);
                docDSSP();
            }
        }
    }

    private void showForm(SanPhamDTO sp) {
        JDialog dialog = new JDialog(this, sp == null ? "Thêm Sản Phẩm" : "Sửa Sản Phẩm", true);
        dialog.setSize(500, 550);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel pnlForm = new JPanel(new GridLayout(7, 2, 10, 20));
        pnlForm.setBorder(new EmptyBorder(30, 30, 30, 30));
        pnlForm.setBackground(Color.WHITE);

        JTextField txtID = new JTextField(sp != null ? sp.getMasanpham() : "");
        JTextField txtTen = new JTextField(sp != null ? sp.getTensanpham() : "");
        JTextField txtSoLuong = new JTextField(sp != null ? String.valueOf(sp.getSoluong()) : "");
        JTextField txtGia = new JTextField(sp != null ? String.valueOf(sp.getDongia()) : "");
        JTextField txtDVT = new JTextField(sp != null ? sp.getDonvitinh() : "");

        // Cập nhật JComboBox với mục "-- Chọn --"
        LoaiSanPhamBUS lspBus = new LoaiSanPhamBUS();
        lspBus.docDSLSP();
        JComboBox<String> cbLoai = new JComboBox<>();
        cbLoai.addItem("-- Chọn Loại Sản Phẩm --");
        for (LoaiSanPhamDTO lsp : LoaiSanPhamBUS.dslsp) {
            cbLoai.addItem(lsp.getMaLoai() + " - " + lsp.getTenLoai());
        }

        HangSanXuatBUS hsxBus = new HangSanXuatBUS();
        hsxBus.docDSLSP(); // Vẫn dùng tên hàm cũ theo BUS của bạn
        JComboBox<String> cbHang = new JComboBox<>();
        cbHang.addItem("-- Chọn Hãng Sản Xuất --");
        for (HangSanXuatDTO hsx : HangSanXuatBUS.dshsx) {
            cbHang.addItem(hsx.getMaHang() + " - " + hsx.getTenHang());
        }

        if (sp != null) {
            txtID.setEditable(false);
            for (int i = 0; i < cbLoai.getItemCount(); i++)
                if (cbLoai.getItemAt(i).startsWith(sp.getMaLoai())) cbLoai.setSelectedIndex(i);
            for (int i = 0; i < cbHang.getItemCount(); i++)
                if (cbHang.getItemAt(i).startsWith(sp.getMaHang())) cbHang.setSelectedIndex(i);
        }

        pnlForm.add(new JLabel("Mã Sản Phẩm:")); pnlForm.add(txtID);
        pnlForm.add(new JLabel("Tên Sản Phẩm:")); pnlForm.add(txtTen);
        pnlForm.add(new JLabel("Loại SP:")); pnlForm.add(cbLoai);
        pnlForm.add(new JLabel("Hãng SX:")); pnlForm.add(cbHang);
        pnlForm.add(new JLabel("Số Lượng:")); pnlForm.add(txtSoLuong);
        pnlForm.add(new JLabel("Đơn Giá:")); pnlForm.add(txtGia);
        pnlForm.add(new JLabel("Đơn Vị Tính:")); pnlForm.add(txtDVT);

        JButton btnSave = new JButton(sp == null ? "Thêm Mới" : "Lưu Thay Đổi");
        btnSave.setBackground(primaryColor);
        btnSave.setForeground(Color.WHITE);
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSave.addActionListener(e -> {
            try {
                // Kiểm tra xem đã chọn Loại và Hãng chưa
                if (cbLoai.getSelectedIndex() == 0 || cbHang.getSelectedIndex() == 0) {
                    JOptionPane.showMessageDialog(dialog, "Vui lòng chọn Loại và Hãng sản xuất!");
                    return;
                }

                SanPhamDTO newSp = new SanPhamDTO();
                newSp.setMasanpham(txtID.getText());
                newSp.setTensanpham(txtTen.getText());
                newSp.setMaLoai(cbLoai.getSelectedItem().toString().split(" - ")[0]);
                newSp.setMaHang(cbHang.getSelectedItem().toString().split(" - ")[0]);
                newSp.setSoluong(Integer.parseInt(txtSoLuong.getText()));
                newSp.setDongia(Integer.parseInt(txtGia.getText()));
                newSp.setDonvitinh(txtDVT.getText());

                SanPhamBUS bus = new SanPhamBUS();
                if (sp == null) bus.them(newSp); else bus.sua(newSp);
                docDSSP();
                dialog.dispose();
            } catch (Exception ex) { JOptionPane.showMessageDialog(dialog, "Lỗi dữ liệu: Vui lòng kiểm tra lại!"); }
        });

        dialog.add(pnlForm, BorderLayout.CENTER);
        dialog.add(btnSave, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
        SwingUtilities.invokeLater(() -> new SieuThiMiniGUI().setVisible(true));
    }
}