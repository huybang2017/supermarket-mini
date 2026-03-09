-- ================================================
-- DATABASE: sieuthimini
-- Phiên bản: đầy đủ, chuẩn hoá khoá ngoại
-- ================================================

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";
/*!40101 SET NAMES utf8mb4 */;

CREATE DATABASE IF NOT EXISTS `sieuthimini`
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;
USE `sieuthimini`;

-- Tắt kiểm tra khoá ngoại để DROP không bị lỗi thứ tự
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `ChuongTrinhKhuyenMaiHD`;
DROP TABLE IF EXISTS `ChuongTrinhKhuyenMaiSp`;
DROP TABLE IF EXISTS `ChuongTrinhKhuyenMai`;
DROP TABLE IF EXISTS `ChiTietPhieuNhapHang`;
DROP TABLE IF EXISTS `PhieuNhapHang`;
DROP TABLE IF EXISTS `ChiTietHoaDon`;
DROP TABLE IF EXISTS `HoaDon`;
DROP TABLE IF EXISTS `ChiTietSanPham`;
DROP TABLE IF EXISTS `SanPham`;
DROP TABLE IF EXISTS `LoaiSanPham`;
DROP TABLE IF EXISTS `HangSanXuat`;
DROP TABLE IF EXISTS `NhaCungCap`;
DROP TABLE IF EXISTS `KhachHang`;
DROP TABLE IF EXISTS `NhanVien`;

SET FOREIGN_KEY_CHECKS = 1;

-- --------------------------------------------------------
-- 1. HangSanXuat
-- --------------------------------------------------------
CREATE TABLE `HangSanXuat` (
  `id`     INT          NOT NULL AUTO_INCREMENT,
  `ten`    VARCHAR(255) DEFAULT NULL,
  `diaChi` VARCHAR(255) DEFAULT NULL,
  `phone`  VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------
-- 2. NhaCungCap
-- --------------------------------------------------------
CREATE TABLE `NhaCungCap` (
  `id`     INT          NOT NULL AUTO_INCREMENT,
  `ten`    VARCHAR(255) DEFAULT NULL,
  `diaChi` VARCHAR(255) DEFAULT NULL,
  `phone`  VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------
-- 3. KhachHang
-- --------------------------------------------------------
CREATE TABLE `KhachHang` (
  `id`     INT          NOT NULL AUTO_INCREMENT,
  `ho`     VARCHAR(255) DEFAULT NULL,
  `ten`    VARCHAR(255) DEFAULT NULL,
  `phone`  VARCHAR(255) DEFAULT NULL,
  `diaChi` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------
-- 4. NhanVien
-- --------------------------------------------------------
CREATE TABLE `NhanVien` (
  `id`       INT           NOT NULL AUTO_INCREMENT,
  `ho`       VARCHAR(255)  DEFAULT NULL,
  `ten`      VARCHAR(255)  DEFAULT NULL,
  `phone`    VARCHAR(255)  DEFAULT NULL,
  `diaChi`   VARCHAR(255)  DEFAULT NULL,
  `ngaySinh` DATETIME      DEFAULT NULL,
  `Luong`    DECIMAL(10,0) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------
-- 5. LoaiSanPham
-- --------------------------------------------------------
CREATE TABLE `LoaiSanPham` (
  `id`     INT          NOT NULL AUTO_INCREMENT,
  `name`   VARCHAR(255) DEFAULT NULL,
  `hangId` INT          DEFAULT NULL COMMENT 'Hãng sản xuất mặc định của loại SP',
  PRIMARY KEY (`id`),
  KEY `fk_loaisp_hang` (`hangId`),
  CONSTRAINT `fk_loaisp_hang`
    FOREIGN KEY (`hangId`) REFERENCES `HangSanXuat` (`id`)
    ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------
-- 6. SanPham
-- --------------------------------------------------------
CREATE TABLE `SanPham` (
  `id`                INT          NOT NULL AUTO_INCREMENT,
  `loaiSanPhamId`     INT          DEFAULT NULL,
  `hangId`            INT          DEFAULT NULL,
  `ten`               VARCHAR(255) DEFAULT NULL,
  `soLuong`           INT          DEFAULT NULL,
  `donGia`            INT          DEFAULT NULL,
  `donViTinh`         VARCHAR(20)  DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_sp_loaisp` (`loaiSanPhamId`),
  KEY `fk_sp_hang`   (`hangId`),
  CONSTRAINT `fk_sp_loaisp`
    FOREIGN KEY (`loaiSanPhamId`) REFERENCES `LoaiSanPham` (`id`)
    ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_sp_hang`
    FOREIGN KEY (`hangId`) REFERENCES `HangSanXuat` (`id`)
    ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------
-- 7. ChiTietSanPham
-- --------------------------------------------------------
CREATE TABLE `ChiTietSanPham` (
  `id`          INT           NOT NULL AUTO_INCREMENT,
  `sanPhamId`   INT           DEFAULT NULL,
  `giaMoiNhat`  DECIMAL(10,0) DEFAULT NULL,
  `moTa`        TEXT          DEFAULT NULL,
  `hanSuDung`   DATETIME      DEFAULT NULL,
  `trangThai`   TINYINT(1)    DEFAULT NULL,
  `ngaySanXuat` DATETIME      DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_ctsp_sp` (`sanPhamId`),
  CONSTRAINT `fk_ctsp_sp`
    FOREIGN KEY (`sanPhamId`) REFERENCES `SanPham` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------
-- 8. HoaDon
-- --------------------------------------------------------
CREATE TABLE `HoaDon` (
  `id`          INT           NOT NULL AUTO_INCREMENT,
  `khachHangId` INT           DEFAULT NULL,
  `nhanVienId`  INT           DEFAULT NULL,
  `tongTien`    DECIMAL(10,0) DEFAULT NULL,
  `ngayLapHD`   DATETIME      DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_hd_kh` (`khachHangId`),
  KEY `fk_hd_nv` (`nhanVienId`),
  CONSTRAINT `fk_hd_kh`
    FOREIGN KEY (`khachHangId`) REFERENCES `KhachHang` (`id`)
    ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_hd_nv`
    FOREIGN KEY (`nhanVienId`) REFERENCES `NhanVien` (`id`)
    ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------
-- 9. ChiTietHoaDon
-- --------------------------------------------------------
CREATE TABLE `ChiTietHoaDon` (
  `id`        INT           NOT NULL AUTO_INCREMENT,
  `hoaDonId`  INT           DEFAULT NULL,
  `sanPhamId` INT           DEFAULT NULL,
  `soLuong`   INT           DEFAULT NULL,
  `donGia`    DECIMAL(10,0) DEFAULT NULL,
  `thanhTien` DECIMAL(10,0) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_cthd_hd` (`hoaDonId`),
  KEY `fk_cthd_sp` (`sanPhamId`),
  CONSTRAINT `fk_cthd_hd`
    FOREIGN KEY (`hoaDonId`) REFERENCES `HoaDon` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_cthd_sp`
    FOREIGN KEY (`sanPhamId`) REFERENCES `SanPham` (`id`)
    ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------
-- 10. PhieuNhapHang
-- --------------------------------------------------------
CREATE TABLE `PhieuNhapHang` (
  `id`           INT           NOT NULL AUTO_INCREMENT,
  `nhanVienId`   INT           DEFAULT NULL,
  `nhaCungCapId` INT           DEFAULT NULL,
  `tongTien`     DECIMAL(10,0) DEFAULT NULL,
  `ngayNhap`     DATETIME      DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_pn_nv`  (`nhanVienId`),
  KEY `fk_pn_ncc` (`nhaCungCapId`),
  CONSTRAINT `fk_pn_nv`
    FOREIGN KEY (`nhanVienId`) REFERENCES `NhanVien` (`id`)
    ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_pn_ncc`
    FOREIGN KEY (`nhaCungCapId`) REFERENCES `NhaCungCap` (`id`)
    ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------
-- 11. ChiTietPhieuNhapHang
-- --------------------------------------------------------
CREATE TABLE `ChiTietPhieuNhapHang` (
  `id`              INT           NOT NULL AUTO_INCREMENT,
  `phieuNhapHangId` INT           DEFAULT NULL,
  `sanPhamId`       INT           DEFAULT NULL,
  `soLuong`         INT           DEFAULT NULL,
  `giaNhap`         DECIMAL(10,0) DEFAULT NULL,
  `thanhTien`       DECIMAL(10,0) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_ctpn_pn` (`phieuNhapHangId`),
  KEY `fk_ctpn_sp` (`sanPhamId`),
  CONSTRAINT `fk_ctpn_pn`
    FOREIGN KEY (`phieuNhapHangId`) REFERENCES `PhieuNhapHang` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_ctpn_sp`
    FOREIGN KEY (`sanPhamId`) REFERENCES `SanPham` (`id`)
    ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------
-- 12. ChuongTrinhKhuyenMai
-- --------------------------------------------------------
CREATE TABLE `ChuongTrinhKhuyenMai` (
  `id`          INT          NOT NULL AUTO_INCREMENT,
  `ten`         VARCHAR(255) DEFAULT NULL,
  `ghiChu`      TEXT         DEFAULT NULL,
  `ngayBatDau`  DATETIME     DEFAULT NULL,
  `ngayKetThuc` DATETIME     DEFAULT NULL,
  `trangThai`   TINYINT(1)   DEFAULT 1,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------
-- 13. ChuongTrinhKhuyenMaiSp
-- --------------------------------------------------------
CREATE TABLE `ChuongTrinhKhuyenMaiSp` (
  `id`                     INT NOT NULL AUTO_INCREMENT,
  `chuongTrinhKhuyenMaiId` INT DEFAULT NULL,
  `sanPhamId`              INT DEFAULT NULL,
  `giaTriGiam`             INT DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_kmsp_km` (`chuongTrinhKhuyenMaiId`),
  KEY `fk_kmsp_sp` (`sanPhamId`),
  CONSTRAINT `fk_kmsp_km`
    FOREIGN KEY (`chuongTrinhKhuyenMaiId`) REFERENCES `ChuongTrinhKhuyenMai` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_kmsp_sp`
    FOREIGN KEY (`sanPhamId`) REFERENCES `SanPham` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------
-- 14. ChuongTrinhKhuyenMaiHD
-- --------------------------------------------------------
CREATE TABLE `ChuongTrinhKhuyenMaiHD` (
  `id`                     INT NOT NULL AUTO_INCREMENT,
  `chuongTrinhKhuyenMaiId` INT DEFAULT NULL,
  `soTienHd`               INT DEFAULT NULL,
  `giaTriGiam`             INT DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_kmhd_km` (`chuongTrinhKhuyenMaiId`),
  CONSTRAINT `fk_kmhd_km`
    FOREIGN KEY (`chuongTrinhKhuyenMaiId`) REFERENCES `ChuongTrinhKhuyenMai` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ========================================================
-- DỮ LIỆU MẪU
-- ========================================================

START TRANSACTION;

INSERT INTO `HangSanXuat` (`ten`, `diaChi`, `phone`) VALUES
('Acecook Vietnam',    'TP.HCM',     '028 1122 3344'),
('Coca-Cola',          'TP.HCM',     '028 1234 5678'),
('Kinh Đô (Mondelez)', 'TP.HCM',     '028 2222 111'),
('Masan Consumer',     'TP.HCM',     '028 5555 444'),
('Nestle Vietnam',     'Đồng Nai',   '025 8888 999'),
('Orion Food',         'Bình Dương', '027 1234 567'),
('PepsiCo',            'TP.HCM',     '028 8765 4321'),
('TH Group',           'Nghệ An',    '1800 545409'),
('Unilever Vietnam',   'TP.HCM',     '028 3333 222'),
('Vinamilk',           'TP.HCM',     '028 9988 7766');

INSERT INTO `LoaiSanPham` (`name`, `hangId`) VALUES
('Bánh Kẹo',           3),
('Đồ Uống',            2),
('Gia Dụng & Cá Nhân', 9),
('Mì Ăn Liền',         1),
('Snack & Bí Đỏ',      6),
('Sữa & Chế Phẩm',     10),
('Thực Phẩm Chế Biến', 4);

INSERT INTO `KhachHang` (`ho`, `ten`, `phone`, `diaChi`) VALUES
('Nguyễn Văn',  'An',     '0901234567', 'Quận 1, TP.HCM'),
('Trần Thị',    'Bảo',    '0912345678', 'Quận 2, TP.HCM'),
('Lê Hoàng',    'Cường',  '0923456789', 'Quận 3, TP.HCM'),
('Phạm',        'Dung',   '0934567890', 'Quận 4, TP.HCM'),
('Hoàng Tuấn',  'Em',     '0945678901', 'Quận 5, TP.HCM'),
('Vũ Bích',     'Phượng', '0956789012', 'Quận 6, TP.HCM'),
('Đặng Kim',    'Giao',   '0967890123', 'Quận 7, TP.HCM'),
('Bùi Xuân',    'Hiếu',   '0978901234', 'Quận 8, TP.HCM'),
('Đỗ Minh',     'Inh',    '0989012345', 'Quận 9, TP.HCM'),
('Hồ Thanh',    'Kiệt',   '0990123456', 'Quận 10, TP.HCM'),
('Ngô Đình',    'Long',   '0809876543', 'Quận 11, TP.HCM'),
('Dương Ngọc',  'Mai',    '0818765432', 'Quận 12, TP.HCM'),
('Lý Tiểu',     'Nam',    '0827654321', 'Bình Thạnh, TP.HCM'),
('Vương Tuấn',  'Oanh',   '0836543210', 'Phú Nhuận, TP.HCM'),
('Trịnh Trọng', 'Phúc',   '0845432109', 'Gò Vấp, TP.HCM'),
('Mai Quỳnh',   'Quân',   '0854321098', 'Tân Bình, TP.HCM'),
('Đào Huy',     'Rô',     '0863210987', 'Tân Phú, TP.HCM'),
('Đoàn Văn',    'Sáng',   '0872109876', 'Bình Tân, TP.HCM'),
('Lâm Tài',     'Tâm',    '0881098765', 'Thủ Đức, TP.HCM'),
('Phùng Thị',   'Uyên',   '0890987654', 'Bình Chánh, TP.HCM');

INSERT INTO `NhanVien` (`ho`, `ten`, `phone`, `diaChi`, `ngaySinh`, `Luong`) VALUES
('Nguyễn Ngọc Phương', 'Duy',  '0354271956', 'abc', '2006-08-30', 50000000),
('Đặng Thanh',         'Tuấn', '0972139443', 'abc', '2006-07-09', 100000);

INSERT INTO `SanPham` (`loaiSanPhamId`, `hangId`, `ten`, `soLuong`, `donGia`, `donViTinh`) VALUES
(3, 9,  'Dầu Gội Sunsilk 170g',               45,  55000, 'Chai'),
(2, 2,  'Nước Ngọt Coca-Cola 330ml',           92,  10000, 'Lon'),
(1, 3,  'Bánh Mì Staff Chà Bông',              25,  12000, 'Cái'),
(6, 10, 'Sữa Chua Vinamilk Có Đường',          60,   7500, 'Hộp'),
(5, 7,  'Snack Lay''s Khoai Tây Tự Nhiên',     38,  12000, 'Gói'),
(2, 7,  'Nước Ngọt Pepsi 330ml',               85,  10000, 'Lon'),
(4, 1,  'Mì Hảo Hảo Tôm Chua Cay',           150,   4500, 'Gói'),
(2, 7,  'Nước Suối Aquafina 500ml',           100,   5000, 'Chai'),
(4, 1,  'Mì Trộn Indomie Mi Goreng',           40,   6500, 'Gói'),
(6, 8,  'Sữa TH True Milk Nguyên Chất 180ml',  48,   9000, 'Hộp'),
(6, 10, 'Sữa Tươi Vinamilk Ít Đường 180ml',   55,   8500, 'Hộp'),
(1, 3,  'Bánh Que Pocky Chocolate',            30,  18000, 'Hộp'),
(7, 4,  'Xúc Xích Ponnie Thịt Heo',            70,   5000, 'Cây'),
(2, 5,  'Cà phê Nescafé 3in1 (Bịch 20 gói)',  20,  48000, 'Bịch'),
(2, 4,  'Nước Tăng Lực Redbull',              65,  15000, 'Lon'),
(7, 4,  'Nước Mắm Nam Ngư 500ml',             15,  38000, 'Chai'),
(1, 3,  'Kẹo Sing-gum Doublemint',            50,   6000, 'Thanh'),
(7, 5,  'Nước Tương Maggi Đậm Đặc',           22,  19000, 'Chai'),
(5, 6,  'Snack Oishi Bí Đỏ Cay',              45,   6000, 'Gói'),
(4, 4,  'Mì Omachi Xốt Bò Hầm',              80,   8500, 'Gói');

INSERT INTO `ChuongTrinhKhuyenMai` (`ten`, `ghiChu`, `ngayBatDau`, `ngayKetThuc`, `trangThai`) VALUES
('Khuyến mãi Tết 2026', 'Giảm giá đặc biệt dịp Tết Nguyên Đán', '2026-01-15', '2026-02-15', 1),
('Khuyến mãi Hè 2026',  'Giảm giá mùa hè sôi động',              '2026-06-01', '2026-08-31', 1),
('Black Friday 2026',    'Giảm giá cực sốc Black Friday',          '2026-11-20', '2026-11-30', 0);

INSERT INTO `ChuongTrinhKhuyenMaiHD` (`chuongTrinhKhuyenMaiId`, `soTienHd`, `giaTriGiam`) VALUES
(1, 100000, 10000),
(1, 200000, 25000),
(1, 500000, 75000),
(2, 150000, 20000),
(2, 300000, 50000);

COMMIT;
