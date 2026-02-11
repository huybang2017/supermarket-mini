package supermarket.ui.frame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * LoginFrame - Login Screen
 *
 * UI Prototype for authentication screen No actual authentication logic - just
 * UI demonstration
 *
 * Flow: LoginFrame → MainPOSFrame (on login button click)
 *
 * @author Supermarket Mini Team
 * @version 1.0
 */
public class LoginFrame extends JFrame {

    // UI Components
    private JLabel logoLabel;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel footerLabel;
    private JLabel errorLabel;

    /**
     * Constructor
     */
    public LoginFrame() {
        initComponents();
        layoutComponents();
        setupFrame();
    }

    /**
     * Initialize UI components
     */
    private void initComponents() {
        // Logo/Title
        logoLabel = new JLabel("Mini Supermarket POS", SwingConstants.CENTER);
        logoLabel.setFont(new Font("Arial", Font.BOLD, 28));
        logoLabel.setForeground(new Color(41, 128, 185));

        // Labels
        usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        // Input fields
        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameField.setText("admin"); // Pre-filled for demo

        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setText("admin"); // Pre-filled for demo

        // Login button
        loginButton = new JButton("LOGIN");
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.setBackground(new Color(46, 204, 113));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setPreferredSize(new Dimension(200, 40));
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Login button action
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });

        // Enter key on password field triggers login
        passwordField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });

        // Footer
        footerLabel = new JLabel("POS Demo System v1.0", SwingConstants.CENTER);
        footerLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        footerLabel.setForeground(Color.GRAY);

        // Error label (initially hidden)
        errorLabel = new JLabel("", SwingConstants.CENTER);
        errorLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        errorLabel.setForeground(Color.RED);
        errorLabel.setVisible(false);
    }

    /**
     * Layout components
     */
    private void layoutComponents() {
        // --- CẤU HÌNH MÀU SẮC & FONT (Modern Palette) ---
        Color bgColor = new Color(248, 250, 252);     // Nền xám rất nhạt (Slate 50)
        Color cardColor = Color.WHITE;                // Khung trắng
        Color primaryColor = new Color(79, 70, 229);  // Indigo 600
        Color textColor = new Color(30, 41, 59);      // Slate 800

        Font titleFont = new Font("Segoe UI", Font.BOLD, 28); // To hơn
        Font labelFont = new Font("Segoe UI", Font.BOLD, 15);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 16); // Chữ trong ô nhập to hơn

        // --- THIẾT LẬP CHÍNH ---
        this.getContentPane().setBackground(bgColor);
        this.setLayout(new GridBagLayout()); // Để căn giữa "Card" đăng nhập hoàn hảo

        // --- LOGIN CARD (Khung chứa toàn bộ form) ---
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(cardColor);
        // Tăng Padding bên trong lên (60px) để tạo cảm giác thoáng đãng
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                BorderFactory.createEmptyBorder(50, 60, 50, 60)
        ));

        // --- TITLE & LOGO ---
        logoLabel.setText("HỆ THỐNG QUẢN LÝ"); // Hoặc tên Project của Huy
        logoLabel.setFont(titleFont);
        logoLabel.setForeground(textColor);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- FORM PANEL (GridBagLayout để căn chỉnh chính xác) ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(cardColor);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.insets = new Insets(10, 0, 10, 0); // Khoảng cách giữa các hàng

        // Username Section
        usernameLabel.setFont(labelFont);
        gbc.gridy = 0;
        formPanel.add(usernameLabel, gbc);

        usernameField.setFont(fieldFont);
        usernameField.setPreferredSize(new Dimension(400, 50)); // Rộng 400px, Cao 50px
        gbc.gridy = 1;
        formPanel.add(usernameField, gbc);

        // Password Section
        passwordLabel.setFont(labelFont);
        gbc.gridy = 2;
        formPanel.add(passwordLabel, gbc);

        passwordField.setFont(fieldFont);
        passwordField.setPreferredSize(new Dimension(400, 50)); // Đồng bộ kích thước
        gbc.gridy = 3;
        formPanel.add(passwordField, gbc);

        // --- LOGIN BUTTON ---
        loginButton.setText("ĐĂNG NHẬP HỆ THỐNG");
        loginButton.setPreferredSize(new Dimension(400, 55)); // Nút to và bấm dễ hơn
        loginButton.setMaximumSize(new Dimension(400, 55));
        loginButton.setBackground(primaryColor);
        loginButton.setForeground(Color.BLACK);
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.setFocusPainted(false);
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- ASSEMBLE ---
        card.add(logoLabel);
        card.add(Box.createVerticalStrut(40)); // Khoảng cách dưới Title
        card.add(formPanel);

        errorLabel.setForeground(new Color(220, 38, 38)); // Màu đỏ cảnh báo
        card.add(errorLabel);

        card.add(Box.createVerticalStrut(20));
        card.add(loginButton);
        card.add(Box.createVerticalStrut(30));

        footerLabel.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        footerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(footerLabel);

        // Thêm Card vào chính giữa màn hình
        this.add(card);
    }

    /**
     * Setup frame properties
     */
    private void setupFrame() {
        setTitle("Login - Mini Supermarket POS");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 800);
        setLocationRelativeTo(null); // Center on screen
        setResizable(false);
    }

    /**
     * Handle login button click UI prototype only - no actual authentication
     */
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        System.out.println("Login attempted:");
        System.out.println("  Username: " + username);
        System.out.println("  Password: " + (password.isEmpty() ? "(empty)" : "******"));

        // UI prototype - always accept login
        System.out.println("✓ Login successful (mock)");

        // Close login window
        this.dispose();

        // Open main system window
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainSystemFrame mainSystemFrame = new MainSystemFrame();
                mainSystemFrame.setVisible(true);
                System.out.println("✓ Main System window opened - Full POS System");
            }
        });
    }

    /**
     * Show error message (for demo purposes) Currently not used as login always
     * succeeds
     */
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
}
