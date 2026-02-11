package supermarket.ui.panel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * ProductGridPanel - Product list display
 *
 * Displays products in a grid layout with cards
 * Each card has:
 * - Product name
 * - Price
 * - Add button
 *
 * @author Supermarket Mini Team
 * @version 1.0
 */
public class ProductGridPanel extends JPanel {

    // Mock product data
    private static class Product {
        String name;
        double price;

        Product(String name, double price) {
            this.name = name;
            this.price = price;
        }
    }

    // Mock products
    private Product[] products = {
        new Product("Coca Cola 330ml", 10000),
        new Product("Pepsi 330ml", 9500),
        new Product("Nước Lavie 500ml", 5000),
        new Product("Bánh Oreo", 15000),
        new Product("Sữa TH True Milk", 30000),
        new Product("Mì Hảo Hảo", 3500),
        new Product("Trứng gà (vỉ)", 35000),
        new Product("Gạo ST25 (kg)", 25000)
    };

    /**
     * Constructor
     */
    public ProductGridPanel() {
        initComponents();
    }

    /**
     * Initialize components
     */
    private void initComponents() {
        setLayout(new GridLayout(0, 2, 10, 10)); // 2 columns, auto rows
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(Color.WHITE);

        // Create product cards
        for (Product product : products) {
            JPanel card = createProductCard(product);
            add(card);
        }
    }

    /**
     * Create product card
     */
    private JPanel createProductCard(final Product product) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(5, 5));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        card.setBackground(Color.WHITE);

        // Product info panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);

        // Product name
        JLabel nameLabel = new JLabel(product.name);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Price
        JLabel priceLabel = new JLabel(formatPrice(product.price));
        priceLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        priceLabel.setForeground(new Color(231, 76, 60)); // Red
        priceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        infoPanel.add(nameLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(priceLabel);

        // Add button
        JButton addButton = new JButton("Add");
        addButton.setFont(new Font("Arial", Font.BOLD, 12));
        addButton.setBackground(new Color(46, 204, 113)); // Green
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add button action
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleAddProduct(product);
            }
        });

        // Add to card
        card.add(infoPanel, BorderLayout.CENTER);
        card.add(addButton, BorderLayout.SOUTH);

        return card;
    }

    /**
     * Handle add product
     * UI prototype only - just console log
     */
    private void handleAddProduct(Product product) {
        System.out.println("Add to cart: " + product.name + " - " + formatPrice(product.price));
        // TODO: In real implementation, add to cart
    }

    /**
     * Format price as VNĐ
     */
    private String formatPrice(double price) {
        return String.format("%,.0f VNĐ", price);
    }
}
