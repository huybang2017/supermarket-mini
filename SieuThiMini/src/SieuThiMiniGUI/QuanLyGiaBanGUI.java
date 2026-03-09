package SieuThiMiniGUI;

import DTO.SanPhamDTO;
import SieuThiMiniBUS.SanPhamBUS;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Locale;

public class QuanLyGiaBanGUI extends JPanel {
    private DefaultTableModel model;
    private JTable tblGiaBan;
    
    private Color secondaryColor = new Color(108, 117, 125);
    private Color bgColor = new Color(244, 246, 249);
    private Font fontTitle = new Font("Segoe UI", Font.BOLD, 24);
    private Font fontPlain = new Font("Segoe UI", Font.PLAIN, 14);

    public QuanLyGiaBanGUI() { 
        initComponents(); 
        docDSGiaBan(); 
    }

    private void initComponents() {
        this.setLayout(new BorderLayout(20, 20));
        this.setBackground(bgColor);
        this.setBorder(new EmptyBorder(20, 25, 20, 25));

        JLabel lblTitle = new JLabel("Quản Lý Giá Bán");
        lblTitle.setFont(fontTitle);
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

        // Thanh tìm kiếm
        JPanel pnlSearchGroup = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlSearchGroup.setOpaque(false);
        JTextField txtSearch = new JTextField(" Tìm mã hoặc tên sản phẩm...");
        txtSearch.setPreferredSize(new Dimension(220, 38));
        txtSearch.setFont(fontPlain);
        txtSearch.setForeground(Color.GRAY);
        txtSearch.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) { if (txtSearch.getText().trim().equals("Tìm mã hoặc tên sản phẩm...")) { txtSearch.setText(""); txtSearch.setForeground(Color.BLACK); } }
            public void focusLost(FocusEvent e) { if (txtSearch.getText().trim().isEmpty()) { txtSearch.setForeground(Color.GRAY); txtSearch.setText(" Tìm mã hoặc tên sản phẩm..."); } }
        });
        
        JButton btnAdvToggle = createActionBtn("▼");
        btnAdvToggle.setPreferredSize(new Dimension(45, 38));
        pnlSearchGroup.add(txtSearch); pnlSearchGroup.add(btnAdvToggle);
        topToolBar.add(pnlSearchGroup, BorderLayout.WEST);

        // Tìm kiếm Real-time
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String keyword = txtSearch.getText().toLowerCase().trim();
                if (keyword.equals("tìm mã hoặc tên sản phẩm...") || keyword.isEmpty()) { docDSGiaBan(); return; }
                model.setRowCount(0);
                if (SanPhamBUS.dssp != null) {
                    for (SanPhamDTO sp : SanPhamBUS.dssp) {
                        if (String.valueOf(sp.getMasanpham()).contains(keyword) || sp.getTensanpham().toLowerCase().contains(keyword)) {
                            // Cập nhật lại logic để hiển thị giá nhập và giá bán làm tròn nguyên
                            long giaNhap = Math.round(sp.getGiaNhap()); 
                            double loiNhuan = sp.getLoiNhuan();
                            long giaBan = Math.round(sp.getDongia());
                            model.addRow(new Object[]{sp.getMasanpham(), sp.getTensanpham(), giaNhap, loiNhuan, giaBan, "⚙ Chỉnh Giá"});
                        }
                    }
                }
            }
        });

        String[] headers = {"Mã SP", "Tên Sản Phẩm", "Giá Nhập Vô (VNĐ)", "Lợi Nhuận (%)", "Giá Bán Ra (VNĐ)", "Hành Động"};
        model = new DefaultTableModel(headers, 0) { @Override public boolean isCellEditable(int r, int c) { return false; } };
        tblGiaBan = new JTable(model);
        styleTable(tblGiaBan);

        tblGiaBan.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = tblGiaBan.getSelectedRow();
                    if (row != -1) { 
                        int id = Integer.parseInt(tblGiaBan.getValueAt(row, 0).toString());
                        String ten = tblGiaBan.getValueAt(row, 1).toString();
                        
                        double giaNhap = 0;
                        double loiNhuan = 0;
                        
                        if (SanPhamBUS.dssp != null) {
                            for (SanPhamDTO sp : SanPhamBUS.dssp) {
                                if (sp.getMasanpham() == id) {
                                    giaNhap = sp.getGiaNhap(); 
                                    loiNhuan = sp.getLoiNhuan(); 
                                    break;
                                }
                            }
                        }
                        
                        showEditPriceForm(id, ten, giaNhap, loiNhuan);
                    }
                }
            }
        });
        JScrollPane scrollPane = new JScrollPane(tblGiaBan);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        card.add(pnlHeader, BorderLayout.NORTH);
        card.add(scrollPane, BorderLayout.CENTER);

        this.add(card, BorderLayout.CENTER);
    }

    private void docDSGiaBan() {
        new SanPhamBUS().docDSSP();
        model.setRowCount(0);
        if(SanPhamBUS.dssp != null) {
            for (SanPhamDTO sp : SanPhamBUS.dssp) {
                model.addRow(new Object[]{
                    sp.getMasanpham(), 
                    sp.getTensanpham(), 
                    Math.round(sp.getGiaNhap()), // Trình bày số nguyên
                    sp.getLoiNhuan(), 
                    Math.round(sp.getDongia()),  // Trình bày số nguyên
                    "⚙ Chỉnh Giá"
                });
            }
        }
    }

    // --- FORM CHỈNH SỬA GIÁ BÁN (ĐÃ FIX LỖI LOGIC VÀ PARSE SỐ) ---
    private void showEditPriceForm(int id, String ten, double giaNhapHienTai, double loiNhuanHienTai) {
        Window parentWindow = SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog((Frame)parentWindow, "Chỉnh Sửa Giá Bán - " + ten, true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel pnlForm = new JPanel(new GridLayout(4, 2, 10, 20));
        pnlForm.setBorder(new EmptyBorder(30, 30, 30, 30));
        pnlForm.setBackground(Color.WHITE);

        // FIX: Tránh lỗi Locale gây ra dấu phẩy. Giá nhập thành số nguyên, lợi nhuận bắt buộc dùng dấu chấm
        JTextField txtGiaNhap = new JTextField(String.valueOf(Math.round(giaNhapHienTai)));
        JTextField txtLoiNhuan = new JTextField(String.format(Locale.US, "%.1f", loiNhuanHienTai));
        
        JTextField txtGiaBan = new JTextField();
        txtGiaBan.setEditable(false); 
        txtGiaBan.setBackground(new Color(240, 240, 240));
        // Đặt màu text nổi bật một chút để dễ nhìn giá bán ra
        txtGiaBan.setFont(new Font("Segoe UI", Font.BOLD, 14));
        txtGiaBan.setForeground(new Color(220, 53, 69)); 

        pnlForm.add(new JLabel("Giá Nhập Vô (VNĐ):")); pnlForm.add(txtGiaNhap);
        pnlForm.add(new JLabel("Lợi Nhuận (%):")); pnlForm.add(txtLoiNhuan);
        pnlForm.add(new JLabel("Giá Bán Ra (VNĐ):")); pnlForm.add(txtGiaBan);

        // Hàm tự động tính
        Runnable calcPrice = () -> {
            try {
                String strGiaNhap = txtGiaNhap.getText().trim();
                String strLoiNhuan = txtLoiNhuan.getText().trim();

                // FIX: Nếu người dùng lỡ xóa hết số đi thì gán tạm bằng 0 để tránh lỗi văng form
                if (strGiaNhap.isEmpty() || strLoiNhuan.isEmpty()) {
                    txtGiaBan.setText("0");
                    return;
                }

                long gn = Long.parseLong(strGiaNhap); // Giá nhập nguyên
                double ln = Double.parseDouble(strLoiNhuan);
                
                // Tính giá bán và ép về số nguyên làm tròn
                long gb = Math.round(gn + (gn * ln / 100.0));
                txtGiaBan.setText(String.valueOf(gb));
                
            } catch (NumberFormatException ex) {
                txtGiaBan.setText("Lỗi định dạng số!");
            }
        };

        txtGiaNhap.getDocument().addDocumentListener(new SimpleDocumentListener(calcPrice));
        txtLoiNhuan.getDocument().addDocumentListener(new SimpleDocumentListener(calcPrice));
        calcPrice.run(); // Chạy lần đầu

        JButton btnSave = createActionBtn("Lưu Thay Đổi");
        btnSave.setBackground(new Color(0, 123, 255));
        btnSave.setForeground(Color.WHITE);
        btnSave.addActionListener(e -> {
            try {
                // Parse lại chuẩn xác khi bấm lưu
                String strGiaNhap = txtGiaNhap.getText().trim();
                String strLoiNhuan = txtLoiNhuan.getText().trim();
                String strGiaBan = txtGiaBan.getText().trim();

                if(strGiaNhap.isEmpty() || strLoiNhuan.isEmpty() || strGiaBan.equals("Lỗi định dạng số!")) {
                    JOptionPane.showMessageDialog(dialog, "Vui lòng nhập đúng dữ liệu trước khi lưu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                long gn = Long.parseLong(strGiaNhap);
                double ln = Double.parseDouble(strLoiNhuan);
                long gb = Long.parseLong(strGiaBan);

                SanPhamDTO spUpdate = null;
                for (SanPhamDTO sp : SanPhamBUS.dssp) {
                    if (sp.getMasanpham() == id) {
                        spUpdate = sp;
                        break;
                    }
                }

                if (spUpdate != null) {
                    spUpdate.setGiaNhap((double) gn);
                    spUpdate.setLoiNhuan(ln);
                    spUpdate.setDongia((int) gb); // Ép về int theo DTO của bạn

                    SanPhamBUS spBus = new SanPhamBUS();
                    spBus.sua(spUpdate); 

                    JOptionPane.showMessageDialog(dialog, "Cập nhật giá bán thành công cho SP: " + ten);
                    docDSGiaBan(); 
                    dialog.dispose(); 
                } else {
                    JOptionPane.showMessageDialog(dialog, "Lỗi: Không tìm thấy sản phẩm trong hệ thống!");
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Lỗi nhập liệu: Giá nhập và Lợi nhuận phải là số hợp lệ!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Lỗi hệ thống: " + ex.getMessage());
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

    private class SimpleDocumentListener implements DocumentListener {
        private Runnable action;
        public SimpleDocumentListener(Runnable action) { this.action = action; }
        public void insertUpdate(DocumentEvent e) { action.run(); }
        public void removeUpdate(DocumentEvent e) { action.run(); }
        public void changedUpdate(DocumentEvent e) { action.run(); }
    }
}