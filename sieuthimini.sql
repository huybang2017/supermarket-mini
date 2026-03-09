-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th3 09, 2026 lúc 08:19 AM
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
-- Cấu trúc bảng cho bảng `ai_dexuat_sanpham`
--

CREATE TABLE `ai_dexuat_sanpham` (
  `id` int(11) NOT NULL,
  `khachHangId` int(11) DEFAULT NULL,
  `sanPhamId` int(11) DEFAULT NULL,
  `diemTinCay` decimal(5,2) DEFAULT NULL COMMENT 'Tỷ lệ % độ tự tin AI dự đoán KH sẽ mua',
  `lyDoGoiY` varchar(255) DEFAULT NULL COMMENT 'Ví dụ: Mua cùng sản phẩm X, Dựa trên lịch sử...',
  `ngayTao` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `chitiethoadon`
--

CREATE TABLE `chitiethoadon` (
  `hoaDonId` int(11) DEFAULT NULL,
  `sanPhamId` int(11) DEFAULT NULL,
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
  `sanPhamId` int(11) DEFAULT NULL,
  `soLuong` int(11) DEFAULT NULL,
  `giaNhap` decimal(10,0) DEFAULT NULL,
  `thanhTien` decimal(10,0) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `chitietsanpham`
--

CREATE TABLE `chitietsanpham` (
  `sanPhamId` int(11) DEFAULT NULL,
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
  `sanPhamId` int(11) DEFAULT NULL,
  `giaTriGiam` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `hangsanxuat`
--

CREATE TABLE `hangsanxuat` (
  `id` int(11) NOT NULL,
  `ten` varchar(255) DEFAULT NULL,
  `diaChi` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `hangsanxuat`
--

INSERT INTO `hangsanxuat` (`id`, `ten`, `diaChi`, `phone`) VALUES
(1, 'Acecook Vietnam', 'TP.HCM', '028 1122 3344'),
(2, 'Coca-Cola', 'TP.HCM', '028 1234 5678'),
(3, 'Kinh Đô (Mondelez)', 'TP.HCM', '028 2222 111'),
(4, 'Masan Consumer', 'TP.HCM', '028 5555 444'),
(5, 'Nestle Vietnam', 'Đồng Nai', '025 8888 999'),
(6, 'Orion Food', 'Bình Dương', '027 1234 567'),
(7, 'PepsiCo', 'TP.HCM', '028 8765 4321'),
(8, 'TH Group', 'Nghệ An', '1800 545409'),
(9, 'Unilever Vietnam', 'TP.HCM', '028 3333 222'),
(10, 'Vinamilk', 'TP.HCM', '028 9988 7766');

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

--
-- Đang đổ dữ liệu cho bảng `khachhang`
--

INSERT INTO `khachhang` (`id`, `ho`, `ten`, `phone`, `diaChi`) VALUES
(1, 'Nguyễn Văn', 'An', '0901234567', 'Quận 1, TP.HCM'),
(2, 'Trần Thị', 'Bảo', '0912345678', 'Quận 2, TP.HCM'),
(3, 'Lê Hoàng', 'Cường', '0923456789', 'Quận 3, TP.HCM'),
(4, 'Phạm', 'Dung', '0934567890', 'Quận 4, TP.HCM'),
(5, 'Hoàng Tuấn', 'Em', '0945678901', 'Quận 5, TP.HCM'),
(6, 'Vũ Bích', 'Phượng', '0956789012', 'Quận 6, TP.HCM'),
(7, 'Đặng Kim', 'Giao', '0967890123', 'Quận 7, TP.HCM'),
(8, 'Bùi Xuân', 'Hiếu', '0978901234', 'Quận 8, TP.HCM'),
(9, 'Đỗ Minh', 'Inh', '0989012345', 'Quận 9, TP.HCM'),
(10, 'Hồ Thanh', 'Kiệt', '0990123456', 'Quận 10, TP.HCM'),
(11, 'Ngô Đình', 'Long', '0809876543', 'Quận 11, TP.HCM'),
(12, 'Dương Ngọc', 'Mai', '0818765432', 'Quận 12, TP.HCM'),
(13, 'Lý Tiểu', 'Nam', '0827654321', 'Bình Thạnh, TP.HCM'),
(14, 'Vương Tuấn', 'Oanh', '0836543210', 'Phú Nhuận, TP.HCM'),
(15, 'Trịnh Trọng', 'Phúc', '0845432109', 'Gò Vấp, TP.HCM'),
(16, 'Mai Quỳnh', 'Quân', '0854321098', 'Tân Bình, TP.HCM'),
(17, 'Đào Huy', 'Rô', '0863210987', 'Tân Phú, TP.HCM'),
(18, 'Đoàn Văn', 'Sáng', '0872109876', 'Bình Tân, TP.HCM'),
(19, 'Lâm Tài', 'Tâm', '0881098765', 'Thủ Đức, TP.HCM'),
(20, 'Phùng Thị', 'Uyên', '0890987654', 'Bình Chánh, TP.HCM');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `loaisanpham`
--

CREATE TABLE `loaisanpham` (
  `id` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `hangId` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `loaisanpham`
--

INSERT INTO `loaisanpham` (`id`, `name`, `hangId`) VALUES
(1, 'Bánh Kẹo', 3),
(2, 'Đồ Uống', 2),
(3, 'Gia Dụng & Cá Nhân', 9),
(4, 'Mì Ăn Liền', 1),
(5, 'Snack & Bí Đỏ', 6),
(6, 'Sữa & Chế Phẩm', 10),
(7, 'Thực Phẩm Chế Biến', 4);

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

--
-- Đang đổ dữ liệu cho bảng `nhanvien`
--

INSERT INTO `nhanvien` (`id`, `ho`, `ten`, `phone`, `diaChi`, `ngaySinh`, `Luong`) VALUES
(1, 'Nguyễn Ngọc Phương', 'Duy', '0354271956', 'abc', '2006-08-30 00:00:00', 50000000),
(2, 'Đặng Thanh', 'Tuấn', '0972139443', 'abc', '2006-07-09 00:00:00', 100000);

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
  `id` int(11) NOT NULL,
  `loaiSanPhamId` int(11) DEFAULT NULL,
  `hangId` int(11) DEFAULT NULL,
  `ten` varchar(255) DEFAULT NULL,
  `soLuong` int(11) DEFAULT NULL,
  `donGia` int(11) DEFAULT NULL,
  `donViTinh` varchar(20) DEFAULT NULL,
  `ai_TuKhoa` text DEFAULT NULL COMMENT 'Từ khóa gợi ý do AI tạo',
  `ai_VectorDacTrung` text DEFAULT NULL COMMENT 'Chuỗi vector embedding để AI tìm kiếm ngữ nghĩa'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `sanpham`
--

INSERT INTO `sanpham` (`id`, `loaiSanPhamId`, `hangId`, `ten`, `soLuong`, `donGia`, `donViTinh`, `ai_TuKhoa`, `ai_VectorDacTrung`) VALUES
(1, 3, 9, 'Dầu Gội Sunsilk 170g', 45, 55000, 'Chai', NULL, NULL),
(2, 2, 2, 'Nước Ngọt Coca-Cola 330ml', 92, 10000, 'Lon', NULL, NULL),
(3, 1, 3, 'Bánh Mì Staff Chà Bông', 25, 12000, 'Cái', NULL, NULL),
(4, 6, 10, 'Sữa Chua Vinamilk Có Đường', 60, 7500, 'Hộp', NULL, NULL),
(5, 5, 7, 'Snack Lay\'s Khoai Tây Tự Nhiên', 38, 12000, 'Gói', NULL, NULL),
(6, 2, 7, 'Nước Ngọt Pepsi 330ml', 85, 10000, 'Lon', NULL, NULL),
(7, 4, 1, 'Mì Hảo Hảo Tôm Chua Cay', 150, 4500, 'Gói', NULL, NULL),
(8, 2, 7, 'Nước Suối Aquafina 500ml', 100, 5000, 'Chai', NULL, NULL),
(9, 4, 1, 'Mì Trộn Indomie Mi Goreng', 40, 6500, 'Gói', NULL, NULL),
(10, 6, 8, 'Sữa TH True Milk Nguyên Chất 180ml', 48, 9000, 'Hộp', NULL, NULL),
(11, 6, 10, 'Sữa Tươi Vinamilk Ít Đường 180ml', 55, 8500, 'Hộp', NULL, NULL),
(12, 1, 3, 'Bánh Que Pocky Chocolate', 30, 18000, 'Hộp', NULL, NULL),
(13, 7, 4, 'Xúc Xích Ponnie Thịt Heo', 70, 5000, 'Cây', NULL, NULL),
(14, 2, 5, 'Cà phê Nescafé 3in1 (Bịch 20 gói)', 20, 48000, 'Bịch', NULL, NULL),
(15, 2, 4, 'Nước Tăng Lực Redbull', 65, 15000, 'Lon', NULL, NULL),
(16, 7, 4, 'Nước Mắm Nam Ngư 500ml', 15, 38000, 'Chai', NULL, NULL),
(17, 1, 3, 'Kẹo Sing-gum Doublemint', 50, 6000, 'Thanh', NULL, NULL),
(18, 7, 5, 'Nước Tương Maggi Đậm Đặc', 22, 19000, 'Chai', NULL, NULL),
(19, 5, 6, 'Snack Oishi Bí Đỏ Cay', 45, 6000, 'Gói', NULL, NULL),
(20, 4, 4, 'Mì Omachi Xốt Bò Hầm', 80, 8500, 'Gói', NULL, NULL);

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `ai_dexuat_sanpham`
--
ALTER TABLE `ai_dexuat_sanpham`
  ADD PRIMARY KEY (`id`),
  ADD KEY `khachHangId` (`khachHangId`),
  ADD KEY `sanPhamId` (`sanPhamId`);

--
-- Chỉ mục cho bảng `chitiethoadon`
--
ALTER TABLE `chitiethoadon`
  ADD KEY `hoaDonId` (`hoaDonId`),
  ADD KEY `sanPhamId` (`sanPhamId`);

--
-- Chỉ mục cho bảng `chitietphieunhaphang`
--
ALTER TABLE `chitietphieunhaphang`
  ADD KEY `phieuNhapHangId` (`phieuNhapHangId`),
  ADD KEY `sanPhamId` (`sanPhamId`);

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
  ADD KEY `hangId` (`hangId`);

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
  ADD KEY `loaiSanPhamId` (`loaiSanPhamId`),
  ADD KEY `hangId` (`hangId`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `ai_dexuat_sanpham`
--
ALTER TABLE `ai_dexuat_sanpham`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `chuongtrinhkhuyenmai`
--
ALTER TABLE `chuongtrinhkhuyenmai`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT cho bảng `hangsanxuat`
--
ALTER TABLE `hangsanxuat`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT cho bảng `hoadon`
--
ALTER TABLE `hoadon`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `khachhang`
--
ALTER TABLE `khachhang`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- AUTO_INCREMENT cho bảng `loaisanpham`
--
ALTER TABLE `loaisanpham`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT cho bảng `nhacungcap`
--
ALTER TABLE `nhacungcap`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `nhanvien`
--
ALTER TABLE `nhanvien`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT cho bảng `phieunhaphang`
--
ALTER TABLE `phieunhaphang`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `sanpham`
--
ALTER TABLE `sanpham`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `ai_dexuat_sanpham`
--
ALTER TABLE `ai_dexuat_sanpham`
  ADD CONSTRAINT `fk_ai_kh` FOREIGN KEY (`khachHangId`) REFERENCES `khachhang` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_ai_sp` FOREIGN KEY (`sanPhamId`) REFERENCES `sanpham` (`id`) ON DELETE CASCADE;

--
-- Các ràng buộc cho bảng `chitiethoadon`
--
ALTER TABLE `chitiethoadon`
  ADD CONSTRAINT `fk_cthd_hd` FOREIGN KEY (`hoaDonId`) REFERENCES `hoadon` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_cthd_sp` FOREIGN KEY (`sanPhamId`) REFERENCES `sanpham` (`id`);

--
-- Các ràng buộc cho bảng `chitietphieunhaphang`
--
ALTER TABLE `chitietphieunhaphang`
  ADD CONSTRAINT `fk_ctpn_pn` FOREIGN KEY (`phieuNhapHangId`) REFERENCES `phieunhaphang` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_ctpn_sp` FOREIGN KEY (`sanPhamId`) REFERENCES `sanpham` (`id`);

--
-- Các ràng buộc cho bảng `chitietsanpham`
--
ALTER TABLE `chitietsanpham`
  ADD CONSTRAINT `fk_ctsp_sp` FOREIGN KEY (`sanPhamId`) REFERENCES `sanpham` (`id`) ON DELETE CASCADE;

--
-- Các ràng buộc cho bảng `chuongtrinhkhuyenmaihd`
--
ALTER TABLE `chuongtrinhkhuyenmaihd`
  ADD CONSTRAINT `fk_kmhd_km` FOREIGN KEY (`chuongTrinhKhuyenMaiId`) REFERENCES `chuongtrinhkhuyenmai` (`id`) ON DELETE CASCADE;

--
-- Các ràng buộc cho bảng `chuongtrinhkhuyenmaisp`
--
ALTER TABLE `chuongtrinhkhuyenmaisp`
  ADD CONSTRAINT `fk_kmsp_km` FOREIGN KEY (`chuongTrinhKhuyenMaiId`) REFERENCES `chuongtrinhkhuyenmai` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_kmsp_sp` FOREIGN KEY (`sanPhamId`) REFERENCES `sanpham` (`id`) ON DELETE CASCADE;

--
-- Các ràng buộc cho bảng `hoadon`
--
ALTER TABLE `hoadon`
  ADD CONSTRAINT `fk_hd_kh` FOREIGN KEY (`khachHangId`) REFERENCES `khachhang` (`id`),
  ADD CONSTRAINT `fk_hd_nv` FOREIGN KEY (`nhanVienId`) REFERENCES `nhanvien` (`id`);

--
-- Các ràng buộc cho bảng `loaisanpham`
--
ALTER TABLE `loaisanpham`
  ADD CONSTRAINT `fk_loaisp_hangsx` FOREIGN KEY (`hangId`) REFERENCES `hangsanxuat` (`id`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- Các ràng buộc cho bảng `phieunhaphang`
--
ALTER TABLE `phieunhaphang`
  ADD CONSTRAINT `fk_pn_ncc` FOREIGN KEY (`nhaCungCapId`) REFERENCES `nhacungcap` (`id`),
  ADD CONSTRAINT `fk_pn_nv` FOREIGN KEY (`nhanVienId`) REFERENCES `nhanvien` (`id`);

--
-- Các ràng buộc cho bảng `sanpham`
--
ALTER TABLE `sanpham`
  ADD CONSTRAINT `fk_sp_hangsx` FOREIGN KEY (`hangId`) REFERENCES `hangsanxuat` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_sp_loaisp` FOREIGN KEY (`loaiSanPhamId`) REFERENCES `loaisanpham` (`id`) ON DELETE SET NULL ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
