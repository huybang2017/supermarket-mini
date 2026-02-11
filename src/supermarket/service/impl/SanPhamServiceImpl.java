package supermarket.service.impl;

import supermarket.model.SanPham;
import supermarket.repository.iface.SanPhamRepository;
import supermarket.service.iface.SanPhamService;
import java.util.ArrayList;
import java.util.List;

/**
 * SanPham Service Implementation (Business Logic)
 *
 * This is a DEMO implementation with stubbed business logic
 * NO real validation, NO real business rules (yet)
 *
 * Design Pattern: Dependency Injection
 * - Repository is injected via constructor
 *
 * @author Supermarket Mini Team
 * @version 1.0
 */
public class SanPhamServiceImpl implements SanPhamService {

    private final SanPhamRepository sanPhamRepository;

    /**
     * Constructor Injection
     *
     * @param sanPhamRepository Repository implementation
     */
    public SanPhamServiceImpl(SanPhamRepository sanPhamRepository) {
        this.sanPhamRepository = sanPhamRepository;
    }

    @Override
    public List<SanPham> getAllSanPham() {
        // TODO: Add business logic
        // - Filter by active status
        // - Apply user permissions
        // - Add caching
        return sanPhamRepository.findAll();
    }

    @Override
    public SanPham getSanPhamById(Long id) {
        // TODO: Add validation
        // - Check if ID is valid
        // - Log access
        return sanPhamRepository.findById(id);
    }

    @Override
    public SanPham createSanPham(SanPham sanPham) {
        // TODO: Add business validation
        // - Validate name is not empty
        // - Validate price is positive
        // - Validate quantity is non-negative
        // - Check for duplicate names
        // - Set created date
        return sanPhamRepository.save(sanPham);
    }

    @Override
    public SanPham updateSanPham(SanPham sanPham) {
        // TODO: Add business validation
        // - Validate product exists
        // - Validate price is positive
        // - Validate quantity is non-negative
        // - Set updated date
        return sanPhamRepository.update(sanPham);
    }

    @Override
    public boolean deleteSanPham(Long id) {
        // TODO: Add business rules
        // - Check if product is in pending orders
        // - Check if product has recent sales
        // - Soft delete instead of hard delete
        return sanPhamRepository.deleteById(id);
    }

    @Override
    public List<SanPham> searchByName(String keyword) {
        // TODO: Implement search logic
        // - Search by name (case-insensitive)
        // - Search by code
        // - Search by category

        List<SanPham> allProducts = sanPhamRepository.findAll();
        List<SanPham> results = new ArrayList<>();

        if (keyword == null || keyword.trim().isEmpty()) {
            return allProducts;
        }

        String lowerKeyword = keyword.toLowerCase();
        for (SanPham sp : allProducts) {
            if (sp.getTen().toLowerCase().contains(lowerKeyword)) {
                results.add(sp);
            }
        }

        return results;
    }
}
