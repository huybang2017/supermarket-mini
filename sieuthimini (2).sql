-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th3 02, 2026 lúc 07:08 AM
-- Phiên bản máy phục vụ: 10.4.32-MariaDB
-- Phiên bản PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `sieuthimini`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `chitiethoadon`
--

CREATE TABLE `chitiethoadon` (
  `hoaDonId` int(11) DEFAULT NULL,
  `sanPhamId` varchar(11) DEFAULT NULL,
  `soLuong` int(11) DEFAULT NULL,
  `donGia` decimal(10,0) DEFAULT NULL,
  `thanhTien` decimal(10,0) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `chitietphieunhaphang`
--

CREATE TABLE `chitietphieunhaphang` (
  `phieuNhapHangId` int(11) DEFAULT NULL,
  `sanPhamId` varchar(11) DEFAULT NULL,
  `soLuong` int(11) DEFAULT NULL,
  `giaNhap` decimal(10,0) DEFAULT NULL,
  `thanhTien` decimal(10,0) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `chitietsanpham`
--

CREATE TABLE `chitietsanpham` (
  `sanPhamId` varchar(11) DEFAULT NULL,
  `giaMoiNhat` decimal(10,0) DEFAULT NULL,
  `moTa` text DEFAULT NULL,
  `hanSuDung` datetime DEFAULT NULL,
  `trangThai` tinyint(1) DEFAULT NULL,
  `ngaySanXuat` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `chuongtrinhkhuyenmai`
--

CREATE TABLE `chuongtrinhkhuyenmai` (
  `id` int(11) NOT NULL,
  `ten` varchar(255) DEFAULT NULL,
  `ghiChu` text DEFAULT NULL,
  `ngayBatDau` datetime DEFAULT NULL,
  `ngayKetThuc` datetime DEFAULT NULL,
  `trangThai` tinyint(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `chuongtrinhkhuyenmaihd`
--

CREATE TABLE `chuongtrinhkhuyenmaihd` (
  `chuongTrinhKhuyenMaiId` int(11) DEFAULT NULL,
  `soTienHd` int(11) DEFAULT NULL,
  `giaTriGiam` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `chuongtrinhkhuyenmaisp`
--

CREATE TABLE `chuongtrinhkhuyenmaisp` (
  `chuongTrinhKhuyenMaiId` int(11) DEFAULT NULL,
  `sanPhamId` varchar(11) DEFAULT NULL,
  `giaTriGiam` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `hangsanxuat`
--

CREATE TABLE `hangsanxuat` (
  `id` varchar(11) NOT NULL,
  `ten` varchar(255) DEFAULT NULL,
  `diaChi` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `hangsanxuat`
--

INSERT INTO `hangsanxuat` (`id`, `ten`, `diaChi`, `phone`) VALUES
('AC', 'Acecook Vietnam', 'TP.HCM', '028 1122 3344'),
('CC', 'Coca-Cola', 'TP.HCM', '028 1234 5678'),
('KD', 'Kinh Đô (Mondelez)', 'TP.HCM', '028 2222 111'),
('MS', 'Masan Consumer', 'TP.HCM', '028 5555 444'),
('NS', 'Nestle Vietnam', 'Đồng Nai', '025 8888 999'),
('OR', 'Orion Food', 'Bình Dương', '027 1234 567'),
('PP', 'PepsiCo', 'TP.HCM', '028 8765 4321'),
('TH', 'TH Group', 'Nghệ An', '1800 545409'),
('UL', 'Unilever Vietnam', 'TP.HCM', '028 3333 222'),
('VM', 'Vinamilk', 'TP.HCM', '028 9988 7766');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `hoadon`
--

CREATE TABLE `hoadon` (
  `id` int(11) NOT NULL,
  `khachHangId` int(11) DEFAULT NULL,
  `nhanVienId` int(11) DEFAULT NULL,
  `tongTien` decimal(10,0) DEFAULT NULL,
  `ngayLapHD` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `khachhang`
--

CREATE TABLE `khachhang` (
  `id` int(11) NOT NULL,
  `ho` varchar(255) DEFAULT NULL,
  `ten` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `diaChi` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `loaisanpham`
--

CREATE TABLE `loaisanpham` (
  `id` varchar(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `hangId` varchar(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `loaisanpham`
--

INSERT INTO `loaisanpham` (`id`, `name`, `hangId`) VALUES
('BK', 'Bánh Kẹo', 'KD'),
('DU', 'Đồ Uống', 'CC'),
('GD', 'Gia Dụng & Cá Nhân', 'UL'),
('MN', 'Mì Ăn Liền', 'AC'),
('SN', 'Snack & Bí Đỏ', 'OR'),
('SU', 'Sữa & Chế Phẩm', 'VM'),
('TP', 'Thực Phẩm Chế Biến', 'MS');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `nhacungcap`
--

CREATE TABLE `nhacungcap` (
  `id` int(11) NOT NULL,
  `ten` varchar(255) DEFAULT NULL,
  `diaChi` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `nhanvien`
--

CREATE TABLE `nhanvien` (
  `id` int(11) NOT NULL,
  `ho` varchar(255) DEFAULT NULL,
  `ten` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `diaChi` varchar(255) DEFAULT NULL,
  `ngaySinh` datetime DEFAULT NULL,
  `Luong` decimal(10,0) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `phieunhaphang`
--

CREATE TABLE `phieunhaphang` (
  `id` int(11) NOT NULL,
  `nhanVienId` int(11) DEFAULT NULL,
  `tongTien` decimal(10,0) DEFAULT NULL,
  `nhaCungCapId` int(11) DEFAULT NULL,
  `ngayNhap` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `sanpham`
--

CREATE TABLE `sanpham` (
  `id` varchar(11) NOT NULL,
  `loaiSanPhamId` varchar(11) DEFAULT NULL,
  `HangId` varchar(11) DEFAULT NULL,
  `ten` varchar(255) DEFAULT NULL,
  `soLuong` int(11) DEFAULT NULL,
  `donGia` int(11) DEFAULT NULL,
  `donViTinh` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `sanpham`
--

INSERT INTO `sanpham` (`id`, `loaiSanPhamId`, `HangId`, `ten`, `soLuong`, `donGia`, `donViTinh`) VALUES
('SP002', 'GD', 'UL', 'Dầu Gội Sunsilk 170g', 45, 55000, 'Chai'),
('SP003', 'DU', 'CC', 'Nước Ngọt Coca-Cola 330ml', 92, 10000, 'Lon'),
('SP004', 'BK', 'KD', 'Bánh Mì Staff Chà Bông', 25, 12000, 'Cái'),
('SP005', 'SU', 'VM', 'Sữa Chua Vinamilk Có Đường', 60, 7500, 'Hộp'),
('SP006', 'SN', 'PP', 'Snack Lay\'s Khoai Tây Tự Nhiên', 38, 12000, 'Gói'),
('SP007', 'DU', 'PP', 'Nước Ngọt Pepsi 330ml', 85, 10000, 'Lon'),
('SP008', 'MN', 'AC', 'Mì Hảo Hảo Tôm Chua Cay', 150, 4500, 'Gói'),
('SP009', 'DU', 'PP', 'Nước Suối Aquafina 500ml', 100, 5000, 'Chai'),
('SP010', 'MN', 'AC', 'Mì Trộn Indomie Mi Goreng', 40, 6500, 'Gói'),
('SP011', 'SU', 'TH', 'Sữa TH True Milk Nguyên Chất 180ml', 48, 9000, 'Hộp'),
('SP012', 'SU', 'VM', 'Sữa Tươi Vinamilk Ít Đường 180ml', 55, 8500, 'Hộp'),
('SP013', 'BK', 'KD', 'Bánh Que Pocky Chocolate', 30, 18000, 'Hộp'),
('SP014', 'TP', 'MS', 'Xúc Xích Ponnie Thịt Heo', 70, 5000, 'Cây'),
('SP015', 'DU', 'NS', 'Cà phê Nescafé 3in1 (Bịch 20 gói)', 20, 48000, 'Bịch'),
('SP016', 'DU', 'MS', 'Nước Tăng Lực Redbull', 65, 15000, 'Lon'),
('SP017', 'TP', 'MS', 'Nước Mắm Nam Ngư 500ml', 15, 38000, 'Chai'),
('SP018', 'BK', 'KD', 'Kẹo Sing-gum Doublemint', 50, 6000, 'Thanh'),
('SP019', 'TP', 'NS', 'Nước Tương Maggi Đậm Đặc', 22, 19000, 'Chai'),
('SP020', 'SN', 'OR', 'Snack Oishi Bí Đỏ Cay', 45, 6000, 'Gói'),
('SP021', 'MN', 'MS', 'Mì Omachi Xốt Bò Hầm', 80, 8500, 'Gói');

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `chitiethoadon`
--
ALTER TABLE `chitiethoadon`
  ADD KEY `hoaDonId` (`hoaDonId`),
  ADD KEY `chitiethoadon_ibfk_2` (`sanPhamId`);

--
-- Chỉ mục cho bảng `chitietphieunhaphang`
--
ALTER TABLE `chitietphieunhaphang`
  ADD KEY `phieuNhapHangId` (`phieuNhapHangId`),
  ADD KEY `chitietphieunhaphang_ibfk_2` (`sanPhamId`);

--
-- Chỉ mục cho bảng `chitietsanpham`
--
ALTER TABLE `chitietsanpham`
  ADD KEY `sanPhamId` (`sanPhamId`);

--
-- Chỉ mục cho bảng `chuongtrinhkhuyenmai`
--
ALTER TABLE `chuongtrinhkhuyenmai`
  ADD PRIMARY KEY (`id`);

--
-- Chỉ mục cho bảng `chuongtrinhkhuyenmaihd`
--
ALTER TABLE `chuongtrinhkhuyenmaihd`
  ADD KEY `chuongTrinhKhuyenMaiId` (`chuongTrinhKhuyenMaiId`);

--
-- Chỉ mục cho bảng `chuongtrinhkhuyenmaisp`
--
ALTER TABLE `chuongtrinhkhuyenmaisp`
  ADD KEY `chuongTrinhKhuyenMaiId` (`chuongTrinhKhuyenMaiId`),
  ADD KEY `sanPhamId` (`sanPhamId`);

--
-- Chỉ mục cho bảng `hangsanxuat`
--
ALTER TABLE `hangsanxuat`
  ADD PRIMARY KEY (`id`);

--
-- Chỉ mục cho bảng `hoadon`
--
ALTER TABLE `hoadon`
  ADD PRIMARY KEY (`id`),
  ADD KEY `khachHangId` (`khachHangId`),
  ADD KEY `nhanVienId` (`nhanVienId`);

--
-- Chỉ mục cho bảng `khachhang`
--
ALTER TABLE `khachhang`
  ADD PRIMARY KEY (`id`);

--
-- Chỉ mục cho bảng `loaisanpham`
--
ALTER TABLE `loaisanpham`
  ADD PRIMARY KEY (`id`),
  ADD KEY `loaisanpham_ibfk_1` (`hangId`);

--
-- Chỉ mục cho bảng `nhacungcap`
--
ALTER TABLE `nhacungcap`
  ADD PRIMARY KEY (`id`);

--
-- Chỉ mục cho bảng `nhanvien`
--
ALTER TABLE `nhanvien`
  ADD PRIMARY KEY (`id`);

--
-- Chỉ mục cho bảng `phieunhaphang`
--
ALTER TABLE `phieunhaphang`
  ADD PRIMARY KEY (`id`),
  ADD KEY `nhanVienId` (`nhanVienId`),
  ADD KEY `nhaCungCapId` (`nhaCungCapId`);

--
-- Chỉ mục cho bảng `sanpham`
--
ALTER TABLE `sanpham`
  ADD PRIMARY KEY (`id`),
  ADD KEY `sanpham_ibfk_1` (`loaiSanPhamId`),
  ADD KEY `sanpham_ibfk_2` (`HangId`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `chuongtrinhkhuyenmai`
--
ALTER TABLE `chuongtrinhkhuyenmai`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `hoadon`
--
ALTER TABLE `hoadon`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `khachhang`
--
ALTER TABLE `khachhang`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `nhanvien`
--
ALTER TABLE `nhanvien`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `phieunhaphang`
--
ALTER TABLE `phieunhaphang`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `chitiethoadon`
--
ALTER TABLE `chitiethoadon`
  ADD CONSTRAINT `chitiethoadon_ibfk_1` FOREIGN KEY (`hoaDonId`) REFERENCES `hoadon` (`id`),
  ADD CONSTRAINT `chitiethoadon_ibfk_2` FOREIGN KEY (`sanPhamId`) REFERENCES `sanpham` (`id`);

--
-- Các ràng buộc cho bảng `chitietphieunhaphang`
--
ALTER TABLE `chitietphieunhaphang`
  ADD CONSTRAINT `chitietphieunhaphang_ibfk_1` FOREIGN KEY (`phieuNhapHangId`) REFERENCES `phieunhaphang` (`id`),
  ADD CONSTRAINT `chitietphieunhaphang_ibfk_2` FOREIGN KEY (`sanPhamId`) REFERENCES `sanpham` (`id`);

--
-- Các ràng buộc cho bảng `chuongtrinhkhuyenmaihd`
--
ALTER TABLE `chuongtrinhkhuyenmaihd`
  ADD CONSTRAINT `chuongtrinhkhuyenmaihd_ibfk_1` FOREIGN KEY (`chuongTrinhKhuyenMaiId`) REFERENCES `chuongtrinhkhuyenmai` (`id`);

--
-- Các ràng buộc cho bảng `chuongtrinhkhuyenmaisp`
--
ALTER TABLE `chuongtrinhkhuyenmaisp`
  ADD CONSTRAINT `chuongtrinhkhuyenmaisp_ibfk_1` FOREIGN KEY (`chuongTrinhKhuyenMaiId`) REFERENCES `chuongtrinhkhuyenmai` (`id`);

--
-- Các ràng buộc cho bảng `hoadon`
--
ALTER TABLE `hoadon`
  ADD CONSTRAINT `hoadon_ibfk_1` FOREIGN KEY (`khachHangId`) REFERENCES `khachhang` (`id`),
  ADD CONSTRAINT `hoadon_ibfk_2` FOREIGN KEY (`nhanVienId`) REFERENCES `nhanvien` (`id`);

--
-- Các ràng buộc cho bảng `loaisanpham`
--
ALTER TABLE `loaisanpham`
  ADD CONSTRAINT `loaisanpham_ibfk_1` FOREIGN KEY (`hangId`) REFERENCES `hangsanxuat` (`id`);

--
-- Các ràng buộc cho bảng `phieunhaphang`
--
ALTER TABLE `phieunhaphang`
  ADD CONSTRAINT `phieunhaphang_ibfk_1` FOREIGN KEY (`nhanVienId`) REFERENCES `nhanvien` (`id`),
  ADD CONSTRAINT `phieunhaphang_ibfk_2` FOREIGN KEY (`nhaCungCapId`) REFERENCES `nhacungcap` (`id`);

--
-- Các ràng buộc cho bảng `sanpham`
--
ALTER TABLE `sanpham`
  ADD CONSTRAINT `sanpham_ibfk_2` FOREIGN KEY (`HangId`) REFERENCES `hangsanxuat` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
