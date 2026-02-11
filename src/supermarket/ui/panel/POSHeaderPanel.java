package supermarket.ui.panel;

import supermarket.ui.frame.LoginFrame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * POSHeaderPanel - Header bar for POS screen
 *
 * Contains:
 * - App title
 * - User info
 * - Logout button
 *
 * @author Supermarket Mini Team
 * @version 1.0
 */
public class POSHeaderPanel extends JPanel {

    private JLabel titleLabel;
    private JLabel userLabel;
    private JButton logoutButton;

    /**
     * Constructor
     */
    public POSHeaderPanel() {
        initComponents();
        layoutComponents();
    }

    /**
     * Initialize components
     */
    private void initComponents() {
        setBackground(new Color(41, 128, 185)); // Blue
        setPreferredSize(new Dimension(0, 60));

        // Title label
        titleLabel = new JLabel("Mini Supermarket POS");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);

        // User label
        userLabel = new JLabel("Cashier: Admin");
        userLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        userLabel.setForeground(Color.WHITE);

        // Logout button
        logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Arial", Font.BOLD, 12));
        logoutButton.setBackground(new Color(231, 76, 60)); // Red
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.setPreferredSize(new Dimension(100, 35));

        // Logout action
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogout();
            }
        });
    }

    /**
     * Layout components
     */
    private void layoutComponents() {
        setLayout(new BorderLayout(10, 0));
        setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Left panel (title)
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setBackground(new Color(41, 128, 185));
        leftPanel.add(titleLabel);

        // Right panel (user + logout)
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setBackground(new Color(41, 128, 185));
        rightPanel.add(userLabel);
        rightPanel.add(logoutButton);

        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);
    }

    /**
     * Handle logout
     * UI prototype only - just returns to login screen
     */
    private void handleLogout() {
        System.out.println("Logout clicked");

        // Get parent frame
        Window window = SwingUtilities.getWindowAncestor(this);

        if (window != null) {
            // Close main POS window
            window.dispose();

            // Show login screen again
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    LoginFrame loginFrame = new LoginFrame();
                    loginFrame.setVisible(true);
                    System.out.println("✓ Returned to login screen");
                }
            });
        }
    }
}
