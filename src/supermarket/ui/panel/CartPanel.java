package supermarket.ui.panel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * CartPanel - Shopping cart display
 *
 * Contains:
 * - Cart table (Product, Qty, Price, Total)
 * - Summary panel (Subtotal, Tax, Discount, Total)
 * - Action buttons (Checkout, Clear Cart)
 *
 * @author Supermarket Mini Team
 * @version 1.0
 */
public class CartPanel extends JPanel {

    private JTable cartTable;
    private DefaultTableModel tableModel;
    private JLabel subtotalLabel;
    private JLabel taxLabel;
    private JLabel discountLabel;
    private JLabel totalLabel;
    private JButton checkoutButton;
    private JButton clearButton;

    /**
     * Constructor
     */
    public CartPanel() {
        initComponents();
        layoutComponents();
        loadMockData(); // Load some demo data
    }

    /**
     * Initialize components
     */
    private void initComponents() {
        setLayout(new BorderLayout(0, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Table model
        String[] columns = {"Product", "Qty", "Price", "Total"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Read-only
            }
        };

        // Cart table
        cartTable = new JTable(tableModel);
        cartTable.setFont(new Font("Arial", Font.PLAIN, 13));
        cartTable.setRowHeight(30);
        cartTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        cartTable.getTableHeader().setBackground(new Color(52, 73, 94));
        cartTable.getTableHeader().setForeground(Color.WHITE);

        // Column widths
        cartTable.getColumnModel().getColumn(0).setPreferredWidth(150);
        cartTable.getColumnModel().getColumn(1).setPreferredWidth(50);
        cartTable.getColumnModel().getColumn(2).setPreferredWidth(80);
        cartTable.getColumnModel().getColumn(3).setPreferredWidth(80);

        // Summary labels
        subtotalLabel = new JLabel("Subtotal: 0 VNĐ");
        subtotalLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        taxLabel = new JLabel("Tax (10%): 0 VNĐ");
        taxLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        discountLabel = new JLabel("Discount: 0 VNĐ");
        discountLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        totalLabel = new JLabel("TOTAL: 0 VNĐ");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 18));
        totalLabel.setForeground(new Color(231, 76, 60)); // Red

        // Checkout button
        checkoutButton = new JButton("CHECKOUT");
        checkoutButton.setFont(new Font("Arial", Font.BOLD, 16));
        checkoutButton.setBackground(new Color(46, 204, 113)); // Green
        checkoutButton.setForeground(Color.WHITE);
        checkoutButton.setFocusPainted(false);
        checkoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        checkoutButton.setPreferredSize(new Dimension(0, 50));

        checkoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleCheckout();
            }
        });

        // Clear button
        clearButton = new JButton("Clear Cart");
        clearButton.setFont(new Font("Arial", Font.PLAIN, 12));
        clearButton.setBackground(new Color(149, 165, 166)); // Gray
        clearButton.setForeground(Color.WHITE);
        clearButton.setFocusPainted(false);
        clearButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleClearCart();
            }
        });
    }

    /**
     * Layout components
     */
    private void layoutComponents() {
        // Table scroll pane
        JScrollPane scrollPane = new JScrollPane(cartTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Shopping Cart"));
        add(scrollPane, BorderLayout.CENTER);

        // Bottom panel (summary + buttons)
        JPanel bottomPanel = new JPanel(new BorderLayout(0, 10));
        bottomPanel.setBackground(Color.WHITE);

        // Summary panel
        JPanel summaryPanel = new JPanel();
        summaryPanel.setLayout(new BoxLayout(summaryPanel, BoxLayout.Y_AXIS));
        summaryPanel.setBackground(Color.WHITE);
        summaryPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Summary"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        subtotalLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        taxLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        discountLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        totalLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        summaryPanel.add(subtotalLabel);
        summaryPanel.add(Box.createVerticalStrut(5));
        summaryPanel.add(taxLabel);
        summaryPanel.add(Box.createVerticalStrut(5));
        summaryPanel.add(discountLabel);
        summaryPanel.add(Box.createVerticalStrut(10));
        summaryPanel.add(new JSeparator());
        summaryPanel.add(Box.createVerticalStrut(10));
        summaryPanel.add(totalLabel);

        // Button panel
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 0, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(checkoutButton);
        buttonPanel.add(clearButton);

        bottomPanel.add(summaryPanel, BorderLayout.NORTH);
        bottomPanel.add(buttonPanel, BorderLayout.CENTER);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * Load mock data for demonstration
     */
    private void loadMockData() {
        // Add some demo items
        tableModel.addRow(new Object[]{"Coca Cola 330ml", 2, "10,000", "20,000"});
        tableModel.addRow(new Object[]{"Bánh Oreo", 1, "15,000", "15,000"});

        // Update summary
        updateSummary();
    }

    /**
     * Update summary values
     * Mock calculation only
     */
    private void updateSummary() {
        // Mock values
        double subtotal = 35000;
        double tax = subtotal * 0.1;
        double discount = 0;
        double total = subtotal + tax - discount;

        subtotalLabel.setText(String.format("Subtotal: %,.0f VNĐ", subtotal));
        taxLabel.setText(String.format("Tax (10%%): %,.0f VNĐ", tax));
        discountLabel.setText(String.format("Discount: %,.0f VNĐ", discount));
        totalLabel.setText(String.format("TOTAL: %,.0f VNĐ", total));
    }

    /**
     * Handle checkout
     * UI prototype only
     */
    private void handleCheckout() {
        System.out.println("Checkout button clicked");

        int rowCount = tableModel.getRowCount();
        if (rowCount == 0) {
            JOptionPane.showMessageDialog(
                this,
                "Cart is empty!",
                "Checkout",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        JOptionPane.showMessageDialog(
            this,
            "Checkout functionality not implemented yet.\n" +
            "This is a UI prototype only.\n\n" +
            "Items in cart: " + rowCount,
            "Checkout",
            JOptionPane.INFORMATION_MESSAGE
        );

        System.out.println("  Items in cart: " + rowCount);
    }

    /**
     * Handle clear cart
     * UI prototype only
     */
    private void handleClearCart() {
        System.out.println("Clear cart button clicked");

        int rowCount = tableModel.getRowCount();
        if (rowCount == 0) {
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Clear all items from cart?",
            "Clear Cart",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            tableModel.setRowCount(0);
            updateSummaryEmpty();
            System.out.println("  Cart cleared");
        }
    }

    /**
     * Update summary for empty cart
     */
    private void updateSummaryEmpty() {
        subtotalLabel.setText("Subtotal: 0 VNĐ");
        taxLabel.setText("Tax (10%): 0 VNĐ");
        discountLabel.setText("Discount: 0 VNĐ");
        totalLabel.setText("TOTAL: 0 VNĐ");
    }
}
