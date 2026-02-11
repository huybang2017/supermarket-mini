package supermarket.service.iface;

import supermarket.model.SanPham;
import java.util.List;

/**
 * SanPham Service Interface (Business Logic Layer)
 *
 * Defines contract for product business operations
 * This layer contains business rules and validations
 *
 * @author Supermarket Mini Team
 * @version 1.0
 */
public interface SanPhamService {

    /**
     * Get all products
     *
     * @return List of all products
     */
    List<SanPham> getAllSanPham();

    /**
     * Get product by ID
     *
     * @param id Product ID
     * @return Product or null if not found
     */
    SanPham getSanPhamById(Long id);

    /**
     * Create a new product
     *
     * @param sanPham Product to create
     * @return Created product
     */
    SanPham createSanPham(SanPham sanPham);

    /**
     * Update existing product
     *
     * @param sanPham Product to update
     * @return Updated product
     */
    SanPham updateSanPham(SanPham sanPham);

    /**
     * Delete product by ID
     *
     * @param id Product ID
     * @return true if deleted successfully
     */
    boolean deleteSanPham(Long id);

    /**
     * Search products by name
     *
     * @param keyword Search keyword
     * @return List of matching products
     */
    List<SanPham> searchByName(String keyword);
}
