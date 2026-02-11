package supermarket.app;

import supermarket.ui.frame.LoginFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Supermarket Mini POS System - Main Entry Point
 *
 * This is the MAIN CLASS that NetBeans will execute.
 *
 * Responsibilities:
 * - Set Look and Feel
 * - Launch application on EDT (Event Dispatch Thread)
 * - Display Login Screen
 *
 * Entry Flow:
 * SupermarketMini.main() → LoginFrame → MainPOSFrame
 *
 * @author Supermarket Mini Team
 * @version 1.0
 */
public class SupermarketMini {

    /**
     * Main method - Application entry point
     *
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        // Set system Look and Feel for native appearance
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            System.out.println("✓ System Look and Feel applied");
        } catch (Exception e) {
            System.err.println("⚠ Could not set Look and Feel: " + e.getMessage());
            // Continue with default Look and Feel
        }

        // Launch UI on Event Dispatch Thread (required for Swing thread safety)
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                System.out.println("========================================");
                System.out.println("  Mini Supermarket POS System");
                System.out.println("  UI Prototype v1.0");
                System.out.println("========================================");

                // Create and display login screen
                LoginFrame loginFrame = new LoginFrame();
                loginFrame.setVisible(true);

                System.out.println("✓ Login screen displayed");
            }
        });
    }
}
