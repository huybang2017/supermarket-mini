package SieuThiMiniGUI;

import DTO.NhanVienDTO;
import SieuThiMiniBUS.NhanVienBUS;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import com.github.lgooddatepicker.components.DatePicker;
import java.time.LocalDate;
public class QuanLyNhanVienGUI extends JPanel {
    
    private DefaultTableModel model;
    private JTable tblNhanVien;
    
    private Color primaryColor = new Color(0, 123, 255);
    private Color secondaryColor = new Color(108, 117, 125);
    private Color bgColor = new Color(244, 246, 249);

    public QuanLyNhanVienGUI() {
        initComponents();
        docDSNV(); 
    }

    private void initComponents() {
        this.setLayout(new BorderLayout(20, 20));
        this.setBackground(bgColor);
        this.setBorder(new EmptyBorder(20, 25, 20, 25));

        JLabel lblTitle = new JLabel("Quản Lý Nhân Viên");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(40, 40, 40));
        this.add(lblTitle, BorderLayout.NORTH);

        JPanel card = new JPanel(new BorderLayout(15, 15));
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(new LineBorder(new Color(230, 230, 230), 1), new EmptyBorder(15, 15, 15, 15)));

        // 1. THANH CÔNG CỤ
        JPanel pnlHeader = new JPanel();
        pnlHeader.setLayout(new BoxLayout(pnlHeader, BoxLayout.Y_AXIS));
        pnlHeader.setOpaque(false);

        JPanel topToolBar = new JPanel(new BorderLayout());
        topToolBar.setOpaque(false);

        JPanel pnlSearchGroup = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlSearchGroup.setOpaque(false);
        JTextField txtSearch = new JTextField(" Tìm kiếm nhân viên...");
        txtSearch.setPreferredSize(new Dimension(220, 38));
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtSearch.setForeground(Color.GRAY);
        txtSearch.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) { if (txtSearch.getText().trim().equals("Tìm kiếm nhân viên...")) { txtSearch.setText(""); txtSearch.setForeground(Color.BLACK); } }
            public void focusLost(FocusEvent e) { if (txtSearch.getText().trim().isEmpty()) { txtSearch.setForeground(Color.GRAY); txtSearch.setText(" Tìm kiếm nhân viên..."); } }
        });
        
        JButton btnAdvancedToggle = createActionBtn("▼");
        btnAdvancedToggle.setPreferredSize(new Dimension(45, 38));
        pnlSearchGroup.add(txtSearch);
        pnlSearchGroup.add(btnAdvancedToggle);
        topToolBar.add(pnlSearchGroup, BorderLayout.WEST);

        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlButtons.setOpaque(false);
        JButton btnAdd = createActionBtn("+ Thêm NV");
        btnAdd.addActionListener(e -> showForm(null)); 
        JButton btnDelete = createActionBtn("- Xóa NV");
        btnDelete.addActionListener(e -> deleteSelectedEmployee()); 
        pnlButtons.add(btnAdd); 
        pnlButtons.add(btnDelete);
        topToolBar.add(pnlButtons, BorderLayout.EAST);

        // Panel Lọc Nâng Cao (VD: Theo lương)
        JPanel pnlAdvancedSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        pnlAdvancedSearch.setBackground(new Color(248, 250, 252));
        pnlAdvancedSearch.setBorder(new MatteBorder(1, 0, 0, 0, new Color(230, 230, 230)));
        pnlAdvancedSearch.setVisible(false);
        
        pnlAdvancedSearch.add(new JLabel("Lương từ:"));
        JTextField txtLuongTu = new JTextField(8);
        pnlAdvancedSearch.add(txtLuongTu);
        pnlAdvancedSearch.add(new JLabel("Đến:"));
        JTextField txtLuongDen = new JTextField(8);
        pnlAdvancedSearch.add(txtLuongDen);
        
        JButton btnApplyFilter = createActionBtn("Lọc");
        JButton btnResetFilter = createActionBtn("Làm Mới");
        btnResetFilter.addActionListener(e -> {
            txtSearch.setText(" Tìm kiếm nhân viên...");
            txtLuongTu.setText("");
            txtLuongDen.setText("");
            docDSNV();
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

        // Tìm kiếm Real-time
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String keyword = txtSearch.getText().toLowerCase().trim();
                if (keyword.equals("tìm kiếm nhân viên...") || keyword.isEmpty()) { docDSNV(); return; }
                model.setRowCount(0);
                NhanVienBUS bus = new NhanVienBUS();
                if (bus.dsnv != null) {
                    for (NhanVienDTO nv : bus.dsnv) {
                        if (String.valueOf(nv.getMaNV()).contains(keyword) || 
                            nv.getTenNV().toLowerCase().contains(keyword) ||
                            nv.getSdt().contains(keyword)) {
                            model.addRow(new Object[]{nv.getMaNV(), nv.getHoNV(), nv.getTenNV(), nv.getSdt(), nv.getDiaChi(), nv.getNgaySinh(), nv.getLuong(), "⚙ Sửa"});
                        }
                    }
                }
            }
        });

        // 2. BẢNG DỮ LIỆU
        String[] headers = {"Mã NV", "Họ NV", "Tên NV", "Số Điện Thoại", "Địa Chỉ", "Ngày Sinh", "Lương", "Hành Động"};
        model = new DefaultTableModel(headers, 0) { @Override public boolean isCellEditable(int r, int c) { return false; } };
        tblNhanVien = new JTable(model);
        styleTable(tblNhanVien);

        tblNhanVien.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = tblNhanVien.getSelectedRow();
                    int id = Integer.parseInt(tblNhanVien.getValueAt(row, 0).toString());
                    NhanVienBUS bus = new NhanVienBUS();
                    for (NhanVienDTO nv : bus.dsnv) { if (nv.getMaNV() == id) { showForm(nv); break; } }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(tblNhanVien);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        card.add(scrollPane, BorderLayout.CENTER);

        this.add(card, BorderLayout.CENTER);
    }

    // --- CÁC HÀM XỬ LÝ LOGIC ---
    
    private void docDSNV() {
        NhanVienBUS bus = new NhanVienBUS();
        bus.docDSNV();
        model.setRowCount(0);
        if(bus.dsnv != null) {
            for (NhanVienDTO nv : bus.dsnv) {
                model.addRow(new Object[]{nv.getMaNV(), nv.getHoNV(), nv.getTenNV(), nv.getSdt(), nv.getDiaChi(), nv.getNgaySinh(), nv.getLuong(), "⚙ Sửa"});
            }
        }
    }

    private void deleteSelectedEmployee() {
        int row = tblNhanVien.getSelectedRow();
        if (row != -1) {
            int id = Integer.parseInt(tblNhanVien.getValueAt(row, 0).toString());
            if (JOptionPane.showConfirmDialog(this, "Xóa nhân viên có mã " + id + "?", "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                new NhanVienBUS().xoaNV(id);
                docDSNV();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần xóa!");
        }
    }

    private void showForm(NhanVienDTO nv) {
        Window parentWindow = SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog((Frame)parentWindow, nv == null ? "Thêm Nhân Viên" : "Sửa Nhân Viên", true);
        dialog.setSize(400, 550);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel pnlForm = new JPanel(new GridLayout(7, 2, 10, 20));
        pnlForm.setBorder(new EmptyBorder(30, 30, 30, 30));
        pnlForm.setBackground(Color.WHITE);

        JTextField txtID = new JTextField(nv != null ? String.valueOf(nv.getMaNV()) : "");
        JTextField txtHo = new JTextField(nv != null ? nv.getHoNV() : "");
        JTextField txtTen = new JTextField(nv != null ? nv.getTenNV() : "");
        JTextField txtSdt = new JTextField(nv != null ? nv.getSdt() : "");
        JTextField txtDiaChi = new JTextField(nv != null ? nv.getDiaChi() : "");
        DatePicker txtNgaySinh = new DatePicker();

        // Nếu là đang bấm Sửa (nv != null), thì đổ dữ liệu ngày sinh cũ lên Lịch
        if (nv != null && nv.getNgaySinh() != null) {
            txtNgaySinh.setDate(nv.getNgaySinh().toLocalDate());
        }        JTextField txtLuong = new JTextField(nv != null ? String.valueOf(nv.getLuong()) : "");

        if (nv != null) {
            txtID.setEditable(false);
        }

        pnlForm.add(new JLabel("Mã NV (Tự tăng):")); pnlForm.add(txtID); txtID.setEditable(false);
        pnlForm.add(new JLabel("Họ NV:")); pnlForm.add(txtHo);
        pnlForm.add(new JLabel("Tên NV:")); pnlForm.add(txtTen);
        pnlForm.add(new JLabel("Số Điện Thoại:")); pnlForm.add(txtSdt);
        pnlForm.add(new JLabel("Địa Chỉ:")); pnlForm.add(txtDiaChi);
        pnlForm.add(new JLabel("Ngày Sinh:")); pnlForm.add(txtNgaySinh);
        pnlForm.add(new JLabel("Lương:")); pnlForm.add(txtLuong);

        JButton btnSave = createActionBtn(nv == null ? "Thêm Mới" : "Lưu Thay Đổi");
        btnSave.addActionListener(e -> {
            try {
                NhanVienDTO newNv = new NhanVienDTO();
                if (nv != null) newNv.setMaNV(nv.getMaNV());
                newNv.setHoNV(txtHo.getText());
                newNv.setTenNV(txtTen.getText());
                newNv.setSdt(txtSdt.getText());
                newNv.setDiaChi(txtDiaChi.getText());
                LocalDate selectedDate = txtNgaySinh.getDate();

                if (selectedDate == null) {
                    JOptionPane.showMessageDialog(dialog, "Vui lòng chọn ngày sinh từ lịch!");
                    return; // Dừng lại không lưu nếu chưa chọn
                }
                
                // Chuyển sang kiểu java.sql.Date để lưu vào Database
                newNv.setNgaySinh(java.sql.Date.valueOf(selectedDate));                
                newNv.setLuong(Double.parseDouble(txtLuong.getText()));

                NhanVienBUS bus = new NhanVienBUS();
                if (nv == null) bus.themNV(newNv); else bus.suaNV(newNv);
                docDSNV();
                dialog.dispose();
            } catch (Exception ex) { JOptionPane.showMessageDialog(dialog, "Lỗi dữ liệu: Ngày (YYYY-MM-DD) hoặc Lương không hợp lệ!"); }
        });
        dialog.add(pnlForm, BorderLayout.CENTER);
        dialog.add(btnSave, BorderLayout.SOUTH);
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
        btn.setPreferredSize(new Dimension(140, 38));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBackground(new Color(226, 232, 240)); 
        btn.setForeground(Color.BLACK); 
        btn.setBorder(new LineBorder(new Color(203, 213, 225), 1)); 
        return btn;
    }
}