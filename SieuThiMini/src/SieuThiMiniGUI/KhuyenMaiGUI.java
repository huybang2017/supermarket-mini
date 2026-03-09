package SieuThiMiniGUI;

import DTO.*;
import SieuThiMiniBUS.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class KhuyenMaiGUI extends JPanel {
    
    private DefaultTableModel model;
    private JTable tblKhuyenMai;
    
    private Color primaryColor = new Color(0, 123, 255);
    private Color secondaryColor = new Color(108, 117, 125);
    private Color bgColor = new Color(244, 246, 249);

    public KhuyenMaiGUI() {
        initComponents();
        docDSKM();
    }

    private void initComponents() {
        this.setLayout(new BorderLayout(20, 20));
        this.setBackground(bgColor);
        this.setBorder(new EmptyBorder(20, 25, 20, 25));

        JLabel lblTitle = new JLabel("Quản Lý Chương Trình Khuyến Mãi");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(40, 40, 40));
        this.add(lblTitle, BorderLayout.NORTH);

        JPanel card = new JPanel(new BorderLayout(15, 15));
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(new LineBorder(new Color(230, 230, 230), 1), new EmptyBorder(15, 15, 15, 15)));

        JPanel pnlHeader = new JPanel();
        pnlHeader.setLayout(new BoxLayout(pnlHeader, BoxLayout.Y_AXIS));
        pnlHeader.setOpaque(false);

        JPanel topToolBar = new JPanel(new BorderLayout());
        topToolBar.setOpaque(false);

        JPanel pnlSearchGroup = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlSearchGroup.setOpaque(false);
        JTextField txtSearch = new JTextField(" Tìm kiếm chương trình khuyến mãi...");
        txtSearch.setPreferredSize(new Dimension(250, 38));
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtSearch.setForeground(Color.GRAY);
        txtSearch.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) { 
                if (txtSearch.getText().trim().equals("Tìm kiếm chương trình khuyến mãi...")) { 
                    txtSearch.setText(""); 
                    txtSearch.setForeground(Color.BLACK); 
                } 
            }
            public void focusLost(FocusEvent e) { 
                if (txtSearch.getText().trim().isEmpty()) { 
                    txtSearch.setForeground(Color.GRAY); 
                    txtSearch.setText(" Tìm kiếm chương trình khuyến mãi..."); 
                } 
            }
        });
        
        JButton btnAdvancedToggle = createActionBtn("▼");
        btnAdvancedToggle.setPreferredSize(new Dimension(45, 38));
        pnlSearchGroup.add(txtSearch);
        pnlSearchGroup.add(btnAdvancedToggle);
        topToolBar.add(pnlSearchGroup, BorderLayout.WEST);

        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlButtons.setOpaque(false);
        JButton btnAdd = createActionBtn("+ Thêm Khuyến Mãi");
        btnAdd.addActionListener(e -> showForm(null));
        JButton btnDelete = createActionBtn("- Xóa Khuyến Mãi");
        btnDelete.addActionListener(e -> deleteSelectedKM());
        JButton btnViewDetail = createActionBtn("📋 Chi Tiết");
        btnViewDetail.addActionListener(e -> showDetailDialog());
        pnlButtons.add(btnAdd); 
        pnlButtons.add(btnDelete);
        pnlButtons.add(btnViewDetail);
        topToolBar.add(pnlButtons, BorderLayout.EAST);

        JPanel pnlAdvancedSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        pnlAdvancedSearch.setBackground(new Color(248, 250, 252));
        pnlAdvancedSearch.setBorder(new MatteBorder(1, 0, 0, 0, new Color(230, 230, 230)));
        pnlAdvancedSearch.setVisible(false);
        
        pnlAdvancedSearch.add(new JLabel("Trạng thái:"));
        JComboBox<String> cbStatus = new JComboBox<>(new String[]{"Tất cả", "Đang hoạt động", "Không hoạt động"});
        pnlAdvancedSearch.add(cbStatus);
        
        JButton btnApplyFilter = createActionBtn("Lọc");
        btnApplyFilter.addActionListener(e -> {
            String keyword = txtSearch.getText().toLowerCase().trim();
            String status = cbStatus.getSelectedItem().toString();
            filterTable(keyword, status);
        });
        JButton btnResetFilter = createActionBtn("Làm Mới");
        btnResetFilter.addActionListener(e -> {
            txtSearch.setText(" Tìm kiếm chương trình khuyến mãi...");
            cbStatus.setSelectedIndex(0);
            docDSKM();
        });
        pnlAdvancedSearch.add(btnApplyFilter);
        pnlAdvancedSearch.add(btnResetFilter);

        pnlHeader.add(topToolBar);
        pnlHeader.add(Box.createVerticalStrut(10));
        pnlHeader.add(pnlAdvancedSearch);
        card.add(pnlHeader, BorderLayout.NORTH);

        btnAdvancedToggle.addActionListener(e -> {
            boolean isVisible = pnlAdvancedSearch.isVisible();
            pnlAdvancedSearch.setVisible(!isVisible);
            btnAdvancedToggle.setText(isVisible ? "▼" : "▲");
            card.revalidate();
        });

        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String keyword = txtSearch.getText().toLowerCase().trim();
                if (keyword.equals("tìm kiếm chương trình khuyến mãi...") || keyword.isEmpty()) { 
                    docDSKM(); 
                    return; 
                }
                filterTable(keyword, "Tất cả");
            }
        });

        String[] headers = {"ID", "Tên CT Khuyến Mãi", "Ngày Bắt Đầu", "Ngày Kết Thúc", "Trạng Thái", "Hành Động"};
        model = new DefaultTableModel(headers, 0) { 
            @Override 
            public boolean isCellEditable(int r, int c) { return false; } 
        };
        tblKhuyenMai = new JTable(model);
        styleTable(tblKhuyenMai);

        tblKhuyenMai.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = tblKhuyenMai.getSelectedRow();
                    int id = Integer.parseInt(tblKhuyenMai.getValueAt(row, 0).toString());
                    for (ChuongTrinhKhuyenMaiDTO km : ChuongTrinhKhuyenMaiBUS.dskm) { 
                        if (km.getId() == id) { 
                            showForm(km); 
                            break; 
                        } 
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(tblKhuyenMai);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        card.add(scrollPane, BorderLayout.CENTER);

        this.add(card, BorderLayout.CENTER);
    }

    private void docDSKM() {
        new ChuongTrinhKhuyenMaiBUS().docDSKM();
        model.setRowCount(0);
        if(ChuongTrinhKhuyenMaiBUS.dskm != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            for (ChuongTrinhKhuyenMaiDTO km : ChuongTrinhKhuyenMaiBUS.dskm) {
                model.addRow(new Object[]{
                    km.getId(), 
                    km.getTen(), 
                    sdf.format(km.getNgayBatDau()), 
                    sdf.format(km.getNgayKetThuc()), 
                    km.isTrangThai() ? "Đang hoạt động" : "Không hoạt động", 
                    "⚙ Sửa"
                });
            }
        }
    }

    private void filterTable(String keyword, String status) {
        model.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        if (ChuongTrinhKhuyenMaiBUS.dskm != null) {
            for (ChuongTrinhKhuyenMaiDTO km : ChuongTrinhKhuyenMaiBUS.dskm) {
                boolean matchKeyword = keyword.isEmpty() || keyword.equals("tìm kiếm chương trình khuyến mãi...")
                    || String.valueOf(km.getId()).contains(keyword)
                    || km.getTen().toLowerCase().contains(keyword);
                
                boolean matchStatus = status.equals("Tất cả")
                    || (status.equals("Đang hoạt động") && km.isTrangThai())
                    || (status.equals("Không hoạt động") && !km.isTrangThai());
                
                if (matchKeyword && matchStatus) {
                    model.addRow(new Object[]{
                        km.getId(), 
                        km.getTen(), 
                        sdf.format(km.getNgayBatDau()), 
                        sdf.format(km.getNgayKetThuc()), 
                        km.isTrangThai() ? "Đang hoạt động" : "Không hoạt động", 
                        "⚙ Sửa"
                    });
                }
            }
        }
    }

    private void deleteSelectedKM() {
        int row = tblKhuyenMai.getSelectedRow();
        if (row != -1) {
            int id = Integer.parseInt(tblKhuyenMai.getValueAt(row, 0).toString());
            if (JOptionPane.showConfirmDialog(this, "Xóa chương trình khuyến mãi ID " + id + "?\nLưu ý: Sẽ xóa tất cả sản phẩm và điều kiện liên quan!", "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                // Xóa các chi tiết trước
                new ChuongTrinhKhuyenMaiSpBUS().xoaTheoKM(id);
                new ChuongTrinhKhuyenMaiHDBUS().xoaTheoKM(id);
                // Xóa chương trình
                new ChuongTrinhKhuyenMaiBUS().xoa(id);
                docDSKM();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn chương trình cần xóa!");
        }
    }

    private void showForm(ChuongTrinhKhuyenMaiDTO km) {
        Window parentWindow = SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog((Frame)parentWindow, km == null ? "Thêm Khuyến Mãi" : "Sửa Khuyến Mãi", true);
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel pnlForm = new JPanel(new GridLayout(6, 2, 10, 15));
        pnlForm.setBorder(new EmptyBorder(30, 30, 30, 30));
        pnlForm.setBackground(Color.WHITE);

        JTextField txtID = new JTextField(km != null ? String.valueOf(km.getId()) : "");
        JTextField txtTen = new JTextField(km != null ? km.getTen() : "");
        JTextArea txtGhiChu = new JTextArea(km != null ? km.getGhiChu() : "");
        txtGhiChu.setRows(3);
        JScrollPane scrollGhiChu = new JScrollPane(txtGhiChu);
        
        JTextField txtNgayBD = new JTextField(km != null ? new SimpleDateFormat("yyyy-MM-dd").format(km.getNgayBatDau()) : "");
        JTextField txtNgayKT = new JTextField(km != null ? new SimpleDateFormat("yyyy-MM-dd").format(km.getNgayKetThuc()) : "");
        JCheckBox cbTrangThai = new JCheckBox("Đang hoạt động");
        if (km != null) cbTrangThai.setSelected(km.isTrangThai());

        if (km != null) txtID.setEditable(false);

        pnlForm.add(new JLabel("ID:")); pnlForm.add(txtID);
        pnlForm.add(new JLabel("Tên CT Khuyến Mãi:")); pnlForm.add(txtTen);
        pnlForm.add(new JLabel("Ghi Chú:")); pnlForm.add(scrollGhiChu);
        pnlForm.add(new JLabel("Ngày Bắt Đầu (yyyy-MM-dd):")); pnlForm.add(txtNgayBD);
        pnlForm.add(new JLabel("Ngày Kết Thúc (yyyy-MM-dd):")); pnlForm.add(txtNgayKT);
        pnlForm.add(new JLabel("Trạng Thái:")); pnlForm.add(cbTrangThai);

        JButton btnSave = new JButton(km == null ? "Thêm Mới" : "Lưu Thay Đổi");
        btnSave.setBackground(primaryColor);
        btnSave.setForeground(Color.WHITE);
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSave.addActionListener(e -> {
            try {
                ChuongTrinhKhuyenMaiDTO newKM = new ChuongTrinhKhuyenMaiDTO();
                if (km != null) newKM.setId(Integer.parseInt(txtID.getText()));
                newKM.setTen(txtTen.getText());
                newKM.setGhiChu(txtGhiChu.getText());
                
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                newKM.setNgayBatDau(sdf.parse(txtNgayBD.getText()));
                newKM.setNgayKetThuc(sdf.parse(txtNgayKT.getText()));
                newKM.setTrangThai(cbTrangThai.isSelected());

                ChuongTrinhKhuyenMaiBUS bus = new ChuongTrinhKhuyenMaiBUS();
                if (km == null) bus.them(newKM); 
                else bus.sua(newKM);
                docDSKM();
                dialog.dispose();
            } catch (Exception ex) { 
                JOptionPane.showMessageDialog(dialog, "Lỗi dữ liệu: " + ex.getMessage()); 
            }
        });
        dialog.add(pnlForm, BorderLayout.CENTER);
        dialog.add(btnSave, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showDetailDialog() {
        int row = tblKhuyenMai.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn chương trình khuyến mãi!");
            return;
        }

        int kmId = Integer.parseInt(tblKhuyenMai.getValueAt(row, 0).toString());
        String tenKM = tblKhuyenMai.getValueAt(row, 1).toString();

        Window parentWindow = SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog((Frame)parentWindow, "Chi Tiết: " + tenKM, true);
        dialog.setSize(900, 600);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));

        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Tab 1: Sản phẩm khuyến mãi
        JPanel pnlSanPham = createProductTab(kmId);
        tabbedPane.addTab("Sản Phẩm Khuyến Mãi", pnlSanPham);
        
        // Tab 2: Điều kiện hóa đơn
        JPanel pnlHoaDon = createInvoiceTab(kmId);
        tabbedPane.addTab("Điều Kiện Hóa Đơn", pnlHoaDon);

        dialog.add(tabbedPane, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private JPanel createProductTab(int kmId) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        panel.setBackground(Color.WHITE);

        String[] headers = {"Mã SP", "Tên Sản Phẩm", "Giá Trị Giảm (VNĐ)"};
        DefaultTableModel modelSP = new DefaultTableModel(headers, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tblSP = new JTable(modelSP);
        styleTable(tblSP);

        // Load data
        new ChuongTrinhKhuyenMaiSpBUS().docDSKMSP(kmId);
        new SanPhamBUS().docDSSP();
        if (ChuongTrinhKhuyenMaiSpBUS.dskmsp != null) {
            for (ChuongTrinhKhuyenMaiSpDTO kmsp : ChuongTrinhKhuyenMaiSpBUS.dskmsp) {
                String tenSP = "";
                for (SanPhamDTO sp : SanPhamBUS.dssp) {
                    if (sp.getMasanpham() == kmsp.getSanPhamId()) {
                        tenSP = sp.getTensanpham();
                        break;
                    }
                }
                modelSP.addRow(new Object[]{kmsp.getSanPhamId(), tenSP, kmsp.getGiaTriGiam()});
            }
        }

        JButton btnAddSP = createActionBtn("+ Thêm SP");
        btnAddSP.addActionListener(e -> {
            showAddProductToKM(kmId, modelSP);
        });

        JButton btnDeleteSP = createActionBtn("- Xóa SP");
        btnDeleteSP.addActionListener(e -> {
            int row = tblSP.getSelectedRow();
            if (row != -1) {
                int spId = Integer.parseInt(tblSP.getValueAt(row, 0).toString());
                new ChuongTrinhKhuyenMaiSpBUS().xoa(kmId, spId);
                modelSP.removeRow(row);
            }
        });

        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlButtons.setOpaque(false);
        pnlButtons.add(btnAddSP);
        pnlButtons.add(btnDeleteSP);

        panel.add(pnlButtons, BorderLayout.NORTH);
        panel.add(new JScrollPane(tblSP), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createInvoiceTab(int kmId) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        panel.setBackground(Color.WHITE);

        String[] headers = {"Điều Kiện (VNĐ)", "Giá Trị Giảm (VNĐ)"};
        DefaultTableModel modelHD = new DefaultTableModel(headers, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tblHD = new JTable(modelHD);
        styleTable(tblHD);

        // Load data
        new ChuongTrinhKhuyenMaiHDBUS().docDSKMHD(kmId);
        if (ChuongTrinhKhuyenMaiHDBUS.dskmhd != null) {
            for (ChuongTrinhKhuyenMaiHDDTO kmhd : ChuongTrinhKhuyenMaiHDBUS.dskmhd) {
                modelHD.addRow(new Object[]{kmhd.getSoTienHd(), kmhd.getGiaTriGiam()});
            }
        }

        JButton btnAddHD = createActionBtn("+ Thêm Điều Kiện");
        btnAddHD.addActionListener(e -> {
            showAddInvoiceCondition(kmId, modelHD);
        });

        JButton btnDeleteHD = createActionBtn("- Xóa Điều Kiện");
        btnDeleteHD.addActionListener(e -> {
            int row = tblHD.getSelectedRow();
            if (row != -1) {
                int soTien = Integer.parseInt(tblHD.getValueAt(row, 0).toString());
                new ChuongTrinhKhuyenMaiHDBUS().xoa(kmId, soTien);
                modelHD.removeRow(row);
            }
        });

        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlButtons.setOpaque(false);
        pnlButtons.add(btnAddHD);
        pnlButtons.add(btnDeleteHD);

        panel.add(pnlButtons, BorderLayout.NORTH);
        panel.add(new JScrollPane(tblHD), BorderLayout.CENTER);
        return panel;
    }

    private void showAddProductToKM(int kmId, DefaultTableModel modelSP) {
        new SanPhamBUS().docDSSP();
        JComboBox<String> cbSP = new JComboBox<>();
        for (SanPhamDTO sp : SanPhamBUS.dssp) {
            cbSP.addItem(sp.getMasanpham() + " - " + sp.getTensanpham());
        }

        JTextField txtGiamGia = new JTextField();

        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.add(new JLabel("Sản Phẩm:"));
        panel.add(cbSP);
        panel.add(new JLabel("Giá Trị Giảm (VNĐ):"));
        panel.add(txtGiamGia);

        int result = JOptionPane.showConfirmDialog(this, panel, "Thêm Sản Phẩm Vào Khuyến Mãi", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                int spId = Integer.parseInt(cbSP.getSelectedItem().toString().split(" - ")[0]);
                int giaTriGiam = Integer.parseInt(txtGiamGia.getText());

                ChuongTrinhKhuyenMaiSpDTO kmsp = new ChuongTrinhKhuyenMaiSpDTO(kmId, spId, giaTriGiam);
                new ChuongTrinhKhuyenMaiSpBUS().them(kmsp);

                String tenSP = cbSP.getSelectedItem().toString().split(" - ")[1];
                modelSP.addRow(new Object[]{spId, tenSP, giaTriGiam});
                JOptionPane.showMessageDialog(this, "Thêm sản phẩm thành công!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
            }
        }
    }

    private void showAddInvoiceCondition(int kmId, DefaultTableModel modelHD) {
        JTextField txtSoTien = new JTextField();
        JTextField txtGiamGia = new JTextField();

        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.add(new JLabel("Điều Kiện (VNĐ):"));
        panel.add(txtSoTien);
        panel.add(new JLabel("Giá Trị Giảm (VNĐ):"));
        panel.add(txtGiamGia);

        int result = JOptionPane.showConfirmDialog(this, panel, "Thêm Điều Kiện Hóa Đơn", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                int soTien = Integer.parseInt(txtSoTien.getText());
                int giaTriGiam = Integer.parseInt(txtGiamGia.getText());

                ChuongTrinhKhuyenMaiHDDTO kmhd = new ChuongTrinhKhuyenMaiHDDTO(kmId, soTien, giaTriGiam);
                new ChuongTrinhKhuyenMaiHDBUS().them(kmhd);

                modelHD.addRow(new Object[]{soTien, giaTriGiam});
                JOptionPane.showMessageDialog(this, "Thêm điều kiện thành công!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
            }
        }
    }

    private void styleTable(JTable t) {
        t.setRowHeight(45);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        t.setGridColor(new Color(245, 245, 245));
        t.setShowVerticalLines(false);
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
        btn.setPreferredSize(new Dimension(160, 38));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBackground(new Color(226, 232, 240)); 
        btn.setForeground(Color.BLACK); 
        btn.setBorder(new LineBorder(new Color(203, 213, 225), 1)); 
        return btn;
    }
}
