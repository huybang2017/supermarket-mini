package supermarket.ui.frame;

import supermarket.ui.panel.POSHeaderPanel;
import supermarket.ui.panel.ProductGridPanel;
import supermarket.ui.panel.CartPanel;
import javax.swing.*;
import java.awt.*;

/**
 * MainPOSFrame - Main POS Window
 *
 * Layout:
 * - TOP: Header (title, user, logout)
 * - LEFT: Product grid
 * - RIGHT: Cart panel
 *
 * This is the main working screen of the POS system
 *
 * @author Supermarket Mini Team
 * @version 1.0
 */
public class MainPOSFrame extends JFrame {

    private POSHeaderPanel headerPanel;
    private ProductGridPanel productPanel;
    private CartPanel cartPanel;

    /**
     * Constructor
     */
    public MainPOSFrame() {
        initComponents();
        layoutComponents();
        setupFrame();
    }

    /**
     * Initialize components
     */
    private void initComponents() {
        // Create panels
        headerPanel = new POSHeaderPanel();
        productPanel = new ProductGridPanel();
        cartPanel = new CartPanel();
    }

    /**
     * Layout components
     */
    private void layoutComponents() {
        setLayout(new BorderLayout());

        // Header at top
        add(headerPanel, BorderLayout.NORTH);

        // Main content panel (product list + cart)
        JPanel contentPanel = new JPanel(new BorderLayout(10, 0));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Left: Product list in scroll pane
        JScrollPane productScrollPane = new JScrollPane(productPanel);
        productScrollPane.setBorder(BorderFactory.createTitledBorder("Products"));
        productScrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // Right: Cart panel (fixed width)
        JPanel cartContainer = new JPanel(new BorderLayout());
        cartContainer.setPreferredSize(new Dimension(400, 0));
        cartContainer.add(cartPanel, BorderLayout.CENTER);

        // Add to content panel
        contentPanel.add(productScrollPane, BorderLayout.CENTER);
        contentPanel.add(cartContainer, BorderLayout.EAST);

        // Add content to frame
        add(contentPanel, BorderLayout.CENTER);
    }

    /**
     * Setup frame properties
     */
    private void setupFrame() {
        setTitle("Mini Supermarket POS - Main Screen");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null); // Center on screen
        setMinimumSize(new Dimension(1000, 600));
    }
}
