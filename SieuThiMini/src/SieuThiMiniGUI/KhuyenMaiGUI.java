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

// Kéo thư viện LGoodDatePicker vào đây
import com.github.lgooddatepicker.components.DatePicker;
import java.time.LocalDate;

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

    // ==========================================
    // FORM ĐÃ ĐƯỢC NÂNG CẤP VỚI LGOODDATEPICKER
    // ==========================================
    private void showForm(ChuongTrinhKhuyenMaiDTO km) {
        Window parentWindow = SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog((Frame)parentWindow, km == null ? "Thêm Khuyến Mãi" : "Sửa Khuyến Mãi", true);
        dialog.setSize(600, 450); // Tăng tí chiều cao để lịch bung ra thoải mái
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel pnlForm = new JPanel(new GridLayout(6, 2, 10, 15));
        pnlForm.setBorder(new EmptyBorder(30, 30, 30, 30));
        pnlForm.setBackground(Color.WHITE);

        // JTextField txtID = new JTextField(km != null ? String.valueOf(km.getId()) : "");
        JTextField txtTen = new JTextField(km != null ? km.getTen() : "");
        JTextArea txtGhiChu = new JTextArea(km != null ? km.getGhiChu() : "");
        txtGhiChu.setRows(3);
        JScrollPane scrollGhiChu = new JScrollPane(txtGhiChu);
        
        // Tạo DatePicker
        DatePicker txtNgayBD = new DatePicker();
        DatePicker txtNgayKT = new DatePicker();
        
        // Đổ dữ liệu cũ lên Lịch (nếu có)
        if (km != null) {
            if (km.getNgayBatDau() != null) {
                java.sql.Date sqlDateBD = new java.sql.Date(km.getNgayBatDau().getTime());
                txtNgayBD.setDate(sqlDateBD.toLocalDate());
            }
            if (km.getNgayKetThuc() != null) {
                java.sql.Date sqlDateKT = new java.sql.Date(km.getNgayKetThuc().getTime());
                txtNgayKT.setDate(sqlDateKT.toLocalDate());
            }
            JTextField txtID = new JTextField(km != null ? String.valueOf(km.getId()) : "");
            txtID.setEditable(false); // Khóa luôn từ đầu cho an toàn
            txtID.setBackground(new Color(245, 245, 245));        }

        JCheckBox cbTrangThai = new JCheckBox("Đang hoạt động");
        if (km != null) cbTrangThai.setSelected(km.isTrangThai());

        // if (km != null) txtID.setEditable(false);

        // pnlForm.add(new JLabel("ID:")); pnlForm.add(txtID);
        pnlForm.add(new JLabel("Tên CT Khuyến Mãi:")); pnlForm.add(txtTen);
        pnlForm.add(new JLabel("Ghi Chú:")); pnlForm.add(scrollGhiChu);
        pnlForm.add(new JLabel("Ngày Bắt Đầu:")); pnlForm.add(txtNgayBD);
        pnlForm.add(new JLabel("Ngày Kết Thúc:")); pnlForm.add(txtNgayKT);
        pnlForm.add(new JLabel("Trạng Thái:")); pnlForm.add(cbTrangThai);

        JButton btnSave = createActionBtn(km == null ? "Thêm Mới" : "Lưu Thay Đổi");
        btnSave.addActionListener(e -> {
            try {
                ChuongTrinhKhuyenMaiDTO newKM = new ChuongTrinhKhuyenMaiDTO();
                // if (km != null) newKM.setId(Integer.parseInt(txtID.getText()));
                newKM.setTen(txtTen.getText());
                newKM.setGhiChu(txtGhiChu.getText());
                
                // --- Xử lý Ngày bắt đầu và Ngày kết thúc ---
                LocalDate dateBD = txtNgayBD.getDate();
                LocalDate dateKT = txtNgayKT.getDate();

                if (dateBD == null || dateKT == null) {
                    JOptionPane.showMessageDialog(dialog, "Vui lòng chọn đầy đủ Ngày bắt đầu và Ngày kết thúc từ lịch!");
                    return;
                }
                
                if (dateKT.isBefore(dateBD)) {
                    JOptionPane.showMessageDialog(dialog, "Lỗi: Ngày kết thúc không thể nhỏ hơn Ngày bắt đầu!");
                    return;
                }

                // Chuyển LocalDate sang java.sql.Date để lưu
                newKM.setNgayBatDau(java.sql.Date.valueOf(dateBD));
                newKM.setNgayKetThuc(java.sql.Date.valueOf(dateKT));
                // -------------------------------------------

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
    // ==========================================


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
                int spId = (int) tblSP.getValueAt(row, 0);
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
        // Tạo dialog đẹp
        Window parentWindow = SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog((Frame)parentWindow, "Thêm Sản Phẩm Vào Khuyến Mãi", true);
        dialog.setSize(650, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));

        // Panel chính
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        // Header
        JLabel lblTitle = new JLabel("🎁 Chọn Sản Phẩm Áp Dụng Khuyến Mãi");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(primaryColor);
        mainPanel.add(lblTitle, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 5, 8, 5);

        // Load danh sách sản phẩm
        new SanPhamBUS().docDSSP();
        JComboBox<String> cbSP = new JComboBox<>();
        cbSP.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        for (SanPhamDTO sp : SanPhamBUS.dssp) {
            cbSP.addItem(sp.getMasanpham() + " - " + sp.getTensanpham() + " | Giá: " + String.format("%,d", sp.getDongia()) + " VNĐ");
        }

        // Label hiển thị giá gốc
        JLabel lblGiaGoc = new JLabel("Giá gốc: 0 VNĐ");
        lblGiaGoc.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblGiaGoc.setForeground(new Color(220, 53, 69));

        // TextField giá trị giảm
        JTextField txtGiamGia = new JTextField();
        txtGiamGia.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtGiamGia.setPreferredSize(new Dimension(200, 35));

        // Label hiển thị giá sau giảm
        JLabel lblGiaSauGiam = new JLabel("Giá sau giảm: 0 VNĐ");
        lblGiaSauGiam.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblGiaSauGiam.setForeground(new Color(40, 167, 69));

        // Radio buttons: Giảm theo số tiền hoặc %
        JRadioButton rbGiamTien = new JRadioButton("Giảm theo số tiền (VNĐ)", true);
        JRadioButton rbGiamPhanTram = new JRadioButton("Giảm theo phần trăm (%)");
        ButtonGroup bgLoaiGiam = new ButtonGroup();
        bgLoaiGiam.add(rbGiamTien);
        bgLoaiGiam.add(rbGiamPhanTram);
        rbGiamTien.setBackground(Color.WHITE);
        rbGiamPhanTram.setBackground(Color.WHITE);

        // Event: Khi chọn sản phẩm khác
        cbSP.addActionListener(e -> {
            if (cbSP.getSelectedItem() != null) {
                String selected = cbSP.getSelectedItem().toString();
                String spId = selected.split(" - ")[0];
                for (SanPhamDTO sp : SanPhamBUS.dssp) {
                    if (sp.getMasanpham() == Integer.parseInt(spId)) {
                        lblGiaGoc.setText("Giá gốc: " + String.format("%,d", sp.getDongia()) + " VNĐ");
                        updateGiaSauGiam(sp.getDongia(), txtGiamGia.getText(), rbGiamPhanTram.isSelected(), lblGiaSauGiam);
                        break;
                    }
                }
            }
        });

        // Event: Khi nhập giá trị giảm
        txtGiamGia.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (cbSP.getSelectedItem() != null) {
                    String selected = cbSP.getSelectedItem().toString();
                    String spId = selected.split(" - ")[0];
                    for (SanPhamDTO sp : SanPhamBUS.dssp) {
                        if (sp.getMasanpham() == Integer.parseInt(spId)) {
                            updateGiaSauGiam(sp.getDongia(), txtGiamGia.getText(), rbGiamPhanTram.isSelected(), lblGiaSauGiam);
                            break;
                        }
                    }
                }
            }
        });

        // Event: Khi đổi loại giảm giá
        ActionListener radioListener = e -> {
            if (cbSP.getSelectedItem() != null) {
                String selected = cbSP.getSelectedItem().toString();
                String spId = selected.split(" - ")[0];
                for (SanPhamDTO sp : SanPhamBUS.dssp) {
                    if (sp.getMasanpham() == Integer.parseInt(spId)) {
                        updateGiaSauGiam(sp.getDongia(), txtGiamGia.getText(), rbGiamPhanTram.isSelected(), lblGiaSauGiam);
                        break;
                    }
                }
            }
        };
        rbGiamTien.addActionListener(radioListener);
        rbGiamPhanTram.addActionListener(radioListener);

        // Trigger initial update
        if (cbSP.getItemCount() > 0) {
            cbSP.setSelectedIndex(0);
        }

        // Layout form
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        formPanel.add(new JLabel("Chọn Sản Phẩm:"), gbc);
        
        gbc.gridy = 1;
        formPanel.add(cbSP, gbc);
        
        gbc.gridy = 2;
        formPanel.add(lblGiaGoc, gbc);

        gbc.gridy = 3; gbc.gridwidth = 1;
        formPanel.add(rbGiamTien, gbc);
        gbc.gridx = 1;
        formPanel.add(rbGiamPhanTram, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        formPanel.add(new JLabel("Giá Trị Giảm:"), gbc);
        
        gbc.gridy = 5;
        formPanel.add(txtGiamGia, gbc);
        
        gbc.gridy = 6;
        JPanel previewPanel = new JPanel(new BorderLayout());
        previewPanel.setBackground(new Color(240, 247, 255));
        previewPanel.setBorder(new CompoundBorder(
            new LineBorder(primaryColor, 2),
            new EmptyBorder(10, 10, 10, 10)
        ));
        previewPanel.add(lblGiaSauGiam, BorderLayout.CENTER);
        formPanel.add(previewPanel, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setBackground(Color.WHITE);
        
        JButton btnSave = new JButton("✓ Thêm Vào Khuyến Mãi");
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSave.setBackground(new Color(40, 167, 69));
        btnSave.setForeground(Color.WHITE);
        btnSave.setFocusPainted(false);
        btnSave.setPreferredSize(new Dimension(180, 40));
        
        JButton btnCancel = new JButton("✗ Hủy");
        btnCancel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnCancel.setBackground(new Color(108, 117, 125));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setFocusPainted(false);
        btnCancel.setPreferredSize(new Dimension(100, 40));

        btnSave.addActionListener(e -> {
            try {
                // Validation
                if (cbSP.getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(dialog, "Vui lòng chọn sản phẩm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String giaTriText = txtGiamGia.getText().trim();
                if (giaTriText.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Vui lòng nhập giá trị giảm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    txtGiamGia.requestFocus();
                    return;
                }

                String spId = cbSP.getSelectedItem().toString().split(" - ")[0];
                int giaTriNhap = Integer.parseInt(giaTriText);

                if (giaTriNhap <= 0) {
                    JOptionPane.showMessageDialog(dialog, "Giá trị giảm phải lớn hơn 0!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Tính giá trị giảm thực tế (VNĐ)
                long giaTriGiam = giaTriNhap;
                long giaGoc = 0L;
                for (SanPhamDTO sp : SanPhamBUS.dssp) {
                    if (sp.getMasanpham() == Integer.parseInt(spId)) {
                        giaGoc = sp.getDongia();
                        break;
                    }
                }

                if (rbGiamPhanTram.isSelected()) {
                    if (giaTriNhap > 100) {
                        JOptionPane.showMessageDialog(dialog, "Phần trăm giảm không được vượt quá 100%!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    giaTriGiam = (giaGoc * giaTriNhap) / 100;
                }

                if (giaTriGiam >= giaGoc) {
                    JOptionPane.showMessageDialog(dialog, "Giá trị giảm không được lớn hơn hoặc bằng giá gốc!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Kiểm tra trùng
                new ChuongTrinhKhuyenMaiSpBUS().docDSKMSP(kmId);
                for (ChuongTrinhKhuyenMaiSpDTO existing : ChuongTrinhKhuyenMaiSpBUS.dskmsp) {
                    if (existing.getSanPhamId() == Integer.parseInt(spId)) {
                        int choice = JOptionPane.showConfirmDialog(dialog, 
                            "Sản phẩm này đã có trong chương trình!\nBạn có muốn cập nhật giá trị giảm?",
                            "Xác nhận", 
                            JOptionPane.YES_NO_OPTION);
                        if (choice == JOptionPane.YES_OPTION) {
                            existing.setGiaTriGiam(giaTriGiam);
                            new ChuongTrinhKhuyenMaiSpBUS().sua(existing);
                            
                            // Update table
                            for (int i = 0; i < modelSP.getRowCount(); i++) {
                                if (modelSP.getValueAt(i, 0).toString().equals(spId)) {
                                    modelSP.setValueAt(giaTriGiam, i, 2);
                                    break;
                                }
                            }
                            JOptionPane.showMessageDialog(dialog, "Cập nhật giá trị giảm thành công!");
                            dialog.dispose();
                        }
                        return;
                    }
                }

                // Thêm mới
                ChuongTrinhKhuyenMaiSpDTO kmsp = new ChuongTrinhKhuyenMaiSpDTO(kmId, Integer.parseInt(spId), giaTriGiam);
                new ChuongTrinhKhuyenMaiSpBUS().them(kmsp);

                String tenSP = cbSP.getSelectedItem().toString().split(" - ")[1].split(" \\| ")[0];
                modelSP.addRow(new Object[]{spId, tenSP, String.format("%,d", giaTriGiam)});
                
                JOptionPane.showMessageDialog(dialog, 
                    "✓ Thêm sản phẩm vào khuyến mãi thành công!\n" +
                    "Sản phẩm: " + tenSP + "\n" +
                    "Giá trị giảm: " + String.format("%,d", giaTriGiam) + " VNĐ",
                    "Thành công", 
                    JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng nhập số hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCancel.addActionListener(e -> dialog.dispose());

        btnPanel.add(btnCancel);
        btnPanel.add(btnSave);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);

        dialog.add(mainPanel);
        dialog.setVisible(true);
    }

    private void updateGiaSauGiam(long giaGoc, String giaTriText, boolean isPhanTram, JLabel lblGiaSauGiam) {
        try {
            if (giaTriText.isEmpty()) {
                lblGiaSauGiam.setText("Giá sau giảm: " + String.format("%,d", giaGoc) + " VNĐ");
                return;
            }
            
            long giaTriNhap = Long.parseLong(giaTriText.trim());
            long giaTriGiam = giaTriNhap;
            
            if (isPhanTram) {
                giaTriGiam = (giaGoc * giaTriNhap) / 100;
            }
            
            long giaSauGiam = giaGoc - giaTriGiam;
            if (giaSauGiam < 0) giaSauGiam = 0;
            
            lblGiaSauGiam.setText("Giá sau giảm: " + String.format("%,d", giaSauGiam) + " VNĐ" + 
                (isPhanTram ? " (-" + giaTriNhap + "%)" : ""));
        } catch (NumberFormatException e) {
            lblGiaSauGiam.setText("Giá sau giảm: Nhập số hợp lệ");
        }
    }

    private void showAddInvoiceCondition(int kmId, DefaultTableModel modelHD) {
        // Tạo dialog đẹp
        Window parentWindow = SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog((Frame)parentWindow, "Thêm Điều Kiện Giảm Giá Hóa Đơn", true);
        dialog.setSize(600, 550);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));

        // Panel chính
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        // Header
        JLabel lblTitle = new JLabel("💳 Thiết Lập Điều Kiện Giảm Giá Hóa Đơn");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(primaryColor);
        mainPanel.add(lblTitle, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 5, 10, 5);

        // TextField điều kiện
        JTextField txtSoTien = new JTextField();
        txtSoTien.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtSoTien.setPreferredSize(new Dimension(200, 35));

        // TextField giá trị giảm
        JTextField txtGiamGia = new JTextField();
        txtGiamGia.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtGiamGia.setPreferredSize(new Dimension(200, 35));

        // Radio buttons: Giảm theo số tiền hoặc %
        JRadioButton rbGiamTien = new JRadioButton("Giảm theo số tiền (VNĐ)", true);
        JRadioButton rbGiamPhanTram = new JRadioButton("Giảm theo phần trăm (%)");
        ButtonGroup bgLoaiGiam = new ButtonGroup();
        bgLoaiGiam.add(rbGiamTien);
        bgLoaiGiam.add(rbGiamPhanTram);
        rbGiamTien.setBackground(Color.WHITE);
        rbGiamPhanTram.setBackground(Color.WHITE);

        // Label preview
        JLabel lblPreview = new JLabel("Ví dụ: Hóa đơn từ 0 VNĐ → Giảm 0 VNĐ");
        lblPreview.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblPreview.setForeground(new Color(40, 167, 69));

        // Event: Update preview khi nhập
        KeyAdapter updatePreview = new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                try {
                    String soTienText = txtSoTien.getText().trim();
                    String giamGiaText = txtGiamGia.getText().trim();
                    
                    if (soTienText.isEmpty() || giamGiaText.isEmpty()) {
                        lblPreview.setText("Ví dụ: Hóa đơn từ 0 VNĐ → Giảm 0 VNĐ");
                        return;
                    }
                    
                    int soTien = Integer.parseInt(soTienText);
                    int giamGia = Integer.parseInt(giamGiaText);
                    
                    String preview;
                    if (rbGiamPhanTram.isSelected()) {
                        int giamThucTe = (soTien * giamGia) / 100;
                        preview = String.format("Ví dụ: Hóa đơn từ %,d VNĐ → Giảm %,d VNĐ (-%d%%)", 
                            soTien, giamThucTe, giamGia);
                    } else {
                        preview = String.format("Ví dụ: Hóa đơn từ %,d VNĐ → Giảm %,d VNĐ", 
                            soTien, giamGia);
                    }
                    lblPreview.setText(preview);
                } catch (NumberFormatException ex) {
                    lblPreview.setText("Vui lòng nhập số hợp lệ!");
                    lblPreview.setForeground(Color.RED);
                    return;
                }
                lblPreview.setForeground(new Color(40, 167, 69));
            }
        };
        
        txtSoTien.addKeyListener(updatePreview);
        txtGiamGia.addKeyListener(updatePreview);
        
        ActionListener radioListener = e -> {
            updatePreview.keyReleased(null);
        };
        rbGiamTien.addActionListener(radioListener);
        rbGiamPhanTram.addActionListener(radioListener);

        // Layout form
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel lblHelp1 = new JLabel("💡 Điều kiện: Giá trị hóa đơn tối thiểu để được giảm");
        lblHelp1.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblHelp1.setForeground(new Color(108, 117, 125));
        formPanel.add(lblHelp1, gbc);

        gbc.gridy = 1;
        formPanel.add(new JLabel("Điều Kiện Hóa Đơn Tối Thiểu (VNĐ):"), gbc);
        
        gbc.gridy = 2;
        formPanel.add(txtSoTien, gbc);

        gbc.gridy = 3; gbc.gridwidth = 1;
        formPanel.add(rbGiamTien, gbc);
        gbc.gridx = 1;
        formPanel.add(rbGiamPhanTram, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        JLabel lblHelp2 = new JLabel("💡 Giá trị giảm: Số tiền hoặc % sẽ được giảm");
        lblHelp2.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblHelp2.setForeground(new Color(108, 117, 125));
        formPanel.add(lblHelp2, gbc);

        gbc.gridy = 5;
        formPanel.add(new JLabel("Giá Trị Giảm:"), gbc);
        
        gbc.gridy = 6;
        formPanel.add(txtGiamGia, gbc);

        // Preview panel
        gbc.gridy = 7;
        JPanel previewPanel = new JPanel(new BorderLayout());
        previewPanel.setBackground(new Color(240, 247, 255));
        previewPanel.setBorder(new CompoundBorder(
            new LineBorder(primaryColor, 2),
            new EmptyBorder(15, 15, 15, 15)
        ));
        previewPanel.add(lblPreview, BorderLayout.CENTER);
        formPanel.add(previewPanel, gbc);

        // Các ví dụ mẫu
        gbc.gridy = 8;
        JPanel examplePanel = new JPanel();
        examplePanel.setLayout(new BoxLayout(examplePanel, BoxLayout.Y_AXIS));
        examplePanel.setBackground(new Color(255, 243, 205));
        examplePanel.setBorder(new CompoundBorder(
            new LineBorder(new Color(255, 193, 7), 1),
            new EmptyBorder(10, 10, 10, 10)
        ));
        
        JLabel lblEx = new JLabel("📝 Ví dụ các điều kiện phổ biến:");
        lblEx.setFont(new Font("Segoe UI", Font.BOLD, 12));
        examplePanel.add(lblEx);
        examplePanel.add(new JLabel("  • Hóa đơn từ 100,000đ → Giảm 10,000đ"));
        examplePanel.add(new JLabel("  • Hóa đơn từ 500,000đ → Giảm 10%"));
        examplePanel.add(new JLabel("  • Hóa đơn từ 1,000,000đ → Giảm 100,000đ"));
        
        formPanel.add(examplePanel, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setBackground(Color.WHITE);
        
        JButton btnSave = new JButton("✓ Thêm Điều Kiện");
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSave.setBackground(new Color(40, 167, 69));
        btnSave.setForeground(Color.WHITE);
        btnSave.setFocusPainted(false);
        btnSave.setPreferredSize(new Dimension(160, 40));
        
        JButton btnCancel = new JButton("✗ Hủy");
        btnCancel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnCancel.setBackground(new Color(108, 117, 125));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setFocusPainted(false);
        btnCancel.setPreferredSize(new Dimension(100, 40));

        btnSave.addActionListener(e -> {
            try {
                // Validation
                String soTienText = txtSoTien.getText().trim();
                String giamGiaText = txtGiamGia.getText().trim();

                if (soTienText.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Vui lòng nhập điều kiện hóa đơn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    txtSoTien.requestFocus();
                    return;
                }

                if (giamGiaText.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Vui lòng nhập giá trị giảm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    txtGiamGia.requestFocus();
                    return;
                }

                int soTien = Integer.parseInt(soTienText);
                int giaTriNhap = Integer.parseInt(giamGiaText);

                if (soTien <= 0) {
                    JOptionPane.showMessageDialog(dialog, "Điều kiện hóa đơn phải lớn hơn 0!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (giaTriNhap <= 0) {
                    JOptionPane.showMessageDialog(dialog, "Giá trị giảm phải lớn hơn 0!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Tính giá trị giảm thực tế
                int giaTriGiam = giaTriNhap;
                if (rbGiamPhanTram.isSelected()) {
                    if (giaTriNhap > 100) {
                        JOptionPane.showMessageDialog(dialog, "Phần trăm giảm không được vượt quá 100%!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    giaTriGiam = (soTien * giaTriNhap) / 100;
                }

                if (giaTriGiam >= soTien) {
                    JOptionPane.showMessageDialog(dialog, "Giá trị giảm không được lớn hơn hoặc bằng điều kiện hóa đơn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Kiểm tra trùng
                new ChuongTrinhKhuyenMaiHDBUS().docDSKMHD(kmId);
                for (ChuongTrinhKhuyenMaiHDDTO existing : ChuongTrinhKhuyenMaiHDBUS.dskmhd) {
                    if (existing.getSoTienHd() == soTien) {
                        int choice = JOptionPane.showConfirmDialog(dialog, 
                            "Điều kiện này đã tồn tại!\nBạn có muốn cập nhật giá trị giảm?",
                            "Xác nhận", 
                            JOptionPane.YES_NO_OPTION);
                        if (choice == JOptionPane.YES_OPTION) {
                            existing.setGiaTriGiam(giaTriGiam);
                            new ChuongTrinhKhuyenMaiHDBUS().sua(existing);
                            
                            // Update table
                            for (int i = 0; i < modelHD.getRowCount(); i++) {
                                if (Integer.parseInt(modelHD.getValueAt(i, 0).toString()) == soTien) {
                                    modelHD.setValueAt(String.format("%,d", giaTriGiam), i, 1);
                                    break;
                                }
                            }
                            JOptionPane.showMessageDialog(dialog, "Cập nhật điều kiện thành công!");
                            dialog.dispose();
                        }
                        return;
                    }
                }

                // Thêm mới
                ChuongTrinhKhuyenMaiHDDTO kmhd = new ChuongTrinhKhuyenMaiHDDTO(kmId, soTien, giaTriGiam);
                new ChuongTrinhKhuyenMaiHDBUS().them(kmhd);

                modelHD.addRow(new Object[]{String.format("%,d", soTien), String.format("%,d", giaTriGiam)});
                
                String message = String.format(
                    "✓ Thêm điều kiện thành công!\n\n" +
                    "Điều kiện: Hóa đơn từ %,d VNĐ\n" +
                    "Giảm giá: %,d VNĐ%s",
                    soTien, 
                    giaTriGiam,
                    rbGiamPhanTram.isSelected() ? " (-" + giaTriNhap + "%)" : ""
                );
                
                JOptionPane.showMessageDialog(dialog, message, "Thành công", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng nhập số hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCancel.addActionListener(e -> dialog.dispose());

        btnPanel.add(btnCancel);
        btnPanel.add(btnSave);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);

        dialog.add(mainPanel);
        dialog.setVisible(true);
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