package supermarket.repository.impl;

import supermarket.model.SanPham;
import supermarket.repository.iface.SanPhamRepository;
import java.util.ArrayList;
import java.util.List;

/**
 * SanPham Repository Implementation (Mock Data)
 *
 * This is a DEMO implementation using in-memory mock data
 * NO database, NO SQL, NO JDBC
 *
 * @author Supermarket Mini Team
 * @version 1.0
 */
public class SanPhamRepositoryImpl implements SanPhamRepository {

    // Mock in-memory database
    private List<SanPham> database;

    public SanPhamRepositoryImpl() {
        this.database = new ArrayList<>();
        initializeMockData();
    }

    /**
     * Initialize mock data for demonstration
     */
    private void initializeMockData() {
        database.add(new SanPham(1L, "Coca Cola 330ml", 100, 10000.0));
        database.add(new SanPham(2L, "Pepsi 330ml", 80, 9500.0));
        database.add(new SanPham(3L, "Nước suối Lavie 500ml", 200, 5000.0));
        database.add(new SanPham(4L, "Bánh Oreo", 50, 15000.0));
        database.add(new SanPham(5L, "Sữa TH True Milk", 60, 30000.0));
        database.add(new SanPham(6L, "Mì Hảo Hảo", 150, 3500.0));
        database.add(new SanPham(7L, "Trứng gà (vỉ)", 40, 35000.0));
        database.add(new SanPham(8L, "Gạo ST25 (kg)", 30, 25000.0));
    }

    @Override
    public List<SanPham> findAll() {
        // TODO: Replace with actual database query
        // SELECT * FROM SanPham
        return new ArrayList<>(database);
    }

    @Override
    public SanPham findById(Long id) {
        // TODO: Replace with actual database query
        // SELECT * FROM SanPham WHERE id = ?
        for (SanPham sp : database) {
            if (sp.getId().equals(id)) {
                return sp;
            }
        }
        return null;
    }

    @Override
    public SanPham save(SanPham sanPham) {
        // TODO: Replace with actual database INSERT
        // INSERT INTO SanPham (...) VALUES (...)
        database.add(sanPham);
        return sanPham;
    }

    @Override
    public SanPham update(SanPham sanPham) {
        // TODO: Replace with actual database UPDATE
        // UPDATE SanPham SET ... WHERE id = ?
        SanPham existing = findById(sanPham.getId());
        if (existing != null) {
            database.remove(existing);
            database.add(sanPham);
        }
        return sanPham;
    }

    @Override
    public boolean deleteById(Long id) {
        // TODO: Replace with actual database DELETE
        // DELETE FROM SanPham WHERE id = ?
        SanPham sanPham = findById(id);
        if (sanPham != null) {
            database.remove(sanPham);
            return true;
        }
        return false;
    }
}
