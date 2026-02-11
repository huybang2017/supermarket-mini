package supermarket.repository.iface;

import supermarket.model.SanPham;
import java.util.List;

/**
 * SanPham Repository Interface (Data Access Layer)
 *
 * Defines contract for product data access operations
 * This interface abstracts the data source
 *
 * @author Supermarket Mini Team
 * @version 1.0
 */
public interface SanPhamRepository {

    /**
     * Get all products
     *
     * @return List of all products
     */
    List<SanPham> findAll();

    /**
     * Find product by ID
     *
     * @param id Product ID
     * @return Product or null if not found
     */
    SanPham findById(Long id);

    /**
     * Save a product
     *
     * @param sanPham Product to save
     * @return Saved product
     */
    SanPham save(SanPham sanPham);

    /**
     * Update a product
     *
     * @param sanPham Product to update
     * @return Updated product
     */
    SanPham update(SanPham sanPham);

    /**
     * Delete product by ID
     *
     * @param id Product ID
     * @return true if deleted successfully
     */
    boolean deleteById(Long id);
}
