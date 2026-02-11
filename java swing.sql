CREATE TABLE `KhachHang` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `ho` varchar(255),
  `ten` varchar(255),
  `phone` varchar(255),
  `diaChi` varchar(255)
);

CREATE TABLE `LoaiSanPham` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `name` varchar(255),
  `hangId` int
);

CREATE TABLE `HangSanXuat` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `ten` varchar(255),
  `diaChi` varchar(255),
  `phone` varchar(255)
);

CREATE TABLE `SanPham` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `loaiSanPhamId` int,
  `ten` varchar(255),
  `soLuong` int,
  `donGia` int
);

CREATE TABLE `ChiTietSanPham` (
  `sanPhamId` int,
  `giaMoiNhat` decimal,
  `moTa` text,
  `hanSuDung` dateTime,
  `trangThai` boolean,
  `ngaySanXuat` datetime
);

CREATE TABLE `HoaDon` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `khachHangId` int,
  `nhanVienId` int,
  `tongTien` decimal,
  `ngayLapHD` datetime
);

CREATE TABLE `ChiTietHoaDon` (
  `hoaDonId` int,
  `sanPhamId` int,
  `soLuong` int,
  `donGia` decimal,
  `thanhTien` decimal
);

CREATE TABLE `NhanVien` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `ho` varchar(255),
  `ten` varchar(255),
  `phone` varchar(255),
  `diaChi` varchar(255),
  `ngaySinh` dateTime,
  `Luong` decimal
);

CREATE TABLE `ChuongTrinhKhuyenMai` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `ten` varchar(255),
  `ghiChu` text,
  `ngayBatDau` datetime,
  `ngayKetThuc` datetime,
  `trangThai` boolean
);

CREATE TABLE `ChuongTrinhKhuyenMaiSp` (
  `chuongTrinhKhuyenMaiId` int,
  `sanPhamId` int,
  `giaTriGiam` int
);

CREATE TABLE `ChuongTrinhKhuyenMaiHD` (
  `chuongTrinhKhuyenMaiId` int,
  `soTienHd` int,
  `giaTriGiam` int
);

CREATE TABLE `PhieuNhapHang` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `nhanVienId` int,
  `tongTien` decimal,
  `nhaCungCapId` int,
  `ngayNhap` dateTime
);

CREATE TABLE `NhaCungCap` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `ten` varchar(255),
  `diaChi` varchar(255),
  `phone` varchar(255)
);

CREATE TABLE `ChiTietPhieuNhapHang` (
  `phieuNhapHangId` int,
  `sanPhamId` int,
  `soLuong` int,
  `giaNhap` decimal,
  `thanhTien` decimal
);

ALTER TABLE `LoaiSanPham` ADD FOREIGN KEY (`hangId`) REFERENCES `HangSanXuat` (`id`);

ALTER TABLE `SanPham` ADD FOREIGN KEY (`loaiSanPhamId`) REFERENCES `LoaiSanPham` (`id`);

ALTER TABLE `ChiTietSanPham` ADD FOREIGN KEY (`sanPhamId`) REFERENCES `SanPham` (`id`);

ALTER TABLE `HoaDon` ADD FOREIGN KEY (`khachHangId`) REFERENCES `KhachHang` (`id`);

ALTER TABLE `HoaDon` ADD FOREIGN KEY (`nhanVienId`) REFERENCES `NhanVien` (`id`);

ALTER TABLE `ChiTietHoaDon` ADD FOREIGN KEY (`hoaDonId`) REFERENCES `HoaDon` (`id`);

ALTER TABLE `ChiTietHoaDon` ADD FOREIGN KEY (`sanPhamId`) REFERENCES `SanPham` (`id`);

ALTER TABLE `ChuongTrinhKhuyenMaiSp` ADD FOREIGN KEY (`chuongTrinhKhuyenMaiId`) REFERENCES `ChuongTrinhKhuyenMai` (`id`);

ALTER TABLE `ChuongTrinhKhuyenMaiSp` ADD FOREIGN KEY (`sanPhamId`) REFERENCES `SanPham` (`id`);

ALTER TABLE `ChuongTrinhKhuyenMaiHD` ADD FOREIGN KEY (`chuongTrinhKhuyenMaiId`) REFERENCES `ChuongTrinhKhuyenMai` (`id`);

ALTER TABLE `PhieuNhapHang` ADD FOREIGN KEY (`nhanVienId`) REFERENCES `NhanVien` (`id`);

ALTER TABLE `PhieuNhapHang` ADD FOREIGN KEY (`nhaCungCapId`) REFERENCES `NhaCungCap` (`id`);

ALTER TABLE `ChiTietPhieuNhapHang` ADD FOREIGN KEY (`phieuNhapHangId`) REFERENCES `PhieuNhapHang` (`id`);

ALTER TABLE `ChiTietPhieuNhapHang` ADD FOREIGN KEY (`sanPhamId`) REFERENCES `SanPham` (`id`);
