-- ================================================
-- SCRIPT TẠO CÁC BẢNG KHUYẾN MÃI
-- Chạy script này trong MySQL để tạo các bảng
-- ================================================

USE sieuthimini;

-- Bảng Chương Trình Khuyến Mãi
CREATE TABLE IF NOT EXISTS ChuongTrinhKhuyenMai (
    id INT PRIMARY KEY AUTO_INCREMENT,
    ten VARCHAR(255) NOT NULL,
    ghiChu TEXT,
    ngayBatDau DATETIME NOT NULL,
    ngayKetThuc DATETIME NOT NULL,
    trangThai BOOLEAN DEFAULT TRUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Bảng Chương Trình Khuyến Mãi cho Sản Phẩm
CREATE TABLE IF NOT EXISTS ChuongTrinhKhuyenMaiSp (
    chuongTrinhKhuyenMaiId INT NOT NULL,
    sanPhamId INT NOT NULL,
    giaTriGiam INT NOT NULL,
    PRIMARY KEY (chuongTrinhKhuyenMaiId, sanPhamId),
    FOREIGN KEY (chuongTrinhKhuyenMaiId) REFERENCES ChuongTrinhKhuyenMai(id) ON DELETE CASCADE,
    FOREIGN KEY (sanPhamId) REFERENCES SanPham(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Bảng Chương Trình Khuyến Mãi cho Hóa Đơn
CREATE TABLE IF NOT EXISTS ChuongTrinhKhuyenMaiHD (
    chuongTrinhKhuyenMaiId INT NOT NULL,
    soTienHd INT NOT NULL,
    giaTriGiam INT NOT NULL,
    PRIMARY KEY (chuongTrinhKhuyenMaiId, soTienHd),
    FOREIGN KEY (chuongTrinhKhuyenMaiId) REFERENCES ChuongTrinhKhuyenMai(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ================================================
-- DỮ LIỆU MẪU (OPTIONAL)
-- ================================================

-- Thêm chương trình khuyến mãi mẫu
INSERT INTO ChuongTrinhKhuyenMai (ten, ghiChu, ngayBatDau, ngayKetThuc, trangThai) VALUES
('Khuyến mãi Tết 2026', 'Giảm giá đặc biệt dịp Tết Nguyên Đán', '2026-01-15 00:00:00', '2026-02-15 23:59:59', TRUE),
('Khuyến mãi Hè 2026', 'Giảm giá mùa hè sôi động', '2026-06-01 00:00:00', '2026-08-31 23:59:59', TRUE),
('Black Friday 2026', 'Giảm giá cực sốc Black Friday', '2026-11-20 00:00:00', '2026-11-30 23:59:59', FALSE);

-- Thêm sản phẩm khuyến mãi mẫu (giả sử có sản phẩm id 1, 2, 3)
-- INSERT INTO ChuongTrinhKhuyenMaiSp (chuongTrinhKhuyenMaiId, sanPhamId, giaTriGiam) VALUES
-- (1, 1, 5000),
-- (1, 2, 10000),
-- (2, 3, 15000);

-- Thêm điều kiện hóa đơn mẫu
INSERT INTO ChuongTrinhKhuyenMaiHD (chuongTrinhKhuyenMaiId, soTienHd, giaTriGiam) VALUES
(1, 100000, 10000),
(1, 200000, 25000),
(1, 500000, 75000),
(2, 150000, 20000),
(2, 300000, 50000);

-- ================================================
-- KIỂM TRA DỮ LIỆU
-- ================================================

SELECT * FROM ChuongTrinhKhuyenMai;
SELECT * FROM ChuongTrinhKhuyenMaiSp;
SELECT * FROM ChuongTrinhKhuyenMaiHD;
