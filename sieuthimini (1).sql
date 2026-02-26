-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th2 02, 2026 lúc 08:03 AM
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
('JP', 'Japan', 'Japan', '0123456');

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
('DA', 'Đồ Ăn', 'JP');

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
('001', 'DA', 'JP', 'Cơm Nắm', 20, 25000, 'Cái');

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
