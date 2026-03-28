-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th3 27, 2026 lúc 04:11 PM
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
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `chitiethoadon`
--

CREATE TABLE `chitiethoadon` (
  `id` int(11) NOT NULL,
  `hoaDonId` int(11) DEFAULT NULL,
  `sanPhamId` int(11) DEFAULT NULL,
  `soLuong` int(11) DEFAULT NULL,
  `donGia` decimal(10,0) DEFAULT NULL,
  `thanhTien` decimal(10,0) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `chitiethoadon`
--

-- INSERT INTO `chitiethoadon` (`id`, `hoaDonId`, `sanPhamId`, `soLuong`, `donGia`, `thanhTien`) VALUES
-- (1, 1, 1, 2, 10000, 20000),
-- (2, 2, 2, 3, 10000, 30000),
-- (3, 3, 3, 2, 5000, 10000),
-- (4, 4, 4, 5, 10000, 50000),
-- (5, 5, 5, 2, 5000, 10000),
-- (6, 6, 6, 3, 10000, 30000),
-- (7, 7, 7, 2, 20000, 40000),
-- (8, 8, 8, 1, 7500, 7500),
-- (9, 9, 9, 5, 5000, 25000),
-- (10, 10, 10, 3, 15000, 45000),
-- (11, 11, 11, 2, 20000, 40000),
-- (12, 12, 12, 1, 10000, 10000),
-- (13, 13, 13, 2, 15000, 30000),
-- (14, 14, 14, 1, 15000, 15000),
-- (15, 15, 15, 2, 15000, 30000),
-- (16, 16, 16, 2, 20000, 40000),
-- (17, 17, 17, 2, 25000, 50000),
-- (18, 18, 18, 3, 20000, 60000),
-- (19, 19, 19, 2, 15000, 30000),
-- (20, 20, 20, 1, 25000, 25000),
-- (21, 21, 1, 1, 10000, 10000),
-- (22, 22, 2, 3, 10000, 30000),
-- (23, 23, 3, 3, 5000, 15000),
-- (24, 24, 4, 2, 10000, 20000),
-- (25, 25, 5, 5, 5000, 25000),
-- (26, 26, 6, 3, 10000, 30000),
-- (27, 27, 7, 1, 20000, 20000),
-- (28, 28, 8, 2, 7500, 15000),
-- (29, 29, 9, 5, 5000, 25000),
-- (30, 30, 10, 1, 15000, 15000),
-- (31, 31, 11, 3, 20000, 60000),
-- (32, 32, 12, 4, 10000, 40000),
-- (33, 33, 13, 3, 15000, 45000),
-- (34, 34, 14, 1, 15000, 15000),
-- (35, 35, 15, 2, 15000, 30000),
-- (36, 36, 16, 2, 20000, 40000),
-- (37, 37, 17, 2, 25000, 50000),
-- (38, 38, 18, 2, 20000, 40000),
-- (39, 39, 19, 1, 15000, 15000),
-- (40, 40, 20, 1, 25000, 25000),
-- (41, 41, 1, 2, 10000, 20000),
-- (42, 42, 2, 3, 10000, 30000),
-- (43, 43, 3, 2, 5000, 10000),
-- (44, 44, 4, 5, 10000, 50000),
-- (45, 45, 5, 2, 5000, 10000),
-- (46, 46, 6, 3, 10000, 30000),
-- (47, 47, 7, 2, 20000, 40000),
-- (48, 48, 8, 1, 7500, 7500),
-- (49, 49, 9, 5, 5000, 25000),
-- (50, 50, 10, 3, 15000, 45000),
-- (51, 51, 1, 1, 10000, 10000),
-- (52, 51, 12, 1, 10000, 10000),
-- (53, 51, 13, 1, 15000, 15000),
-- (54, 51, 18, 1, 20000, 20000),
-- (55, 51, 17, 1, 25000, 25000),
-- (56, 51, 8, 1, 7500, 7500),
-- (57, 52, 1, 1, 10000, 10000),
-- (58, 52, 2, 2, 10000, 20000),
-- (59, 52, 8, 2, 7500, 15000),
-- (60, 52, 19, 1, 15000, 15000),
-- (61, 52, 17, 1, 25000, 25000),
-- (62, 53, 9, 1, 5000, 5000),
-- (63, 53, 8, 1, 7500, 7500),
-- (64, 53, 14, 1, 15000, 15000),
-- (65, 54, 2, 5, 10000, 50000),
-- (66, 54, 3, 5, 5000, 25000),
-- (67, 54, 15, 5, 15000, 75000),
-- (68, 55, 2, 4, 10000, 40000),
-- (69, 55, 7, 4, 15000, 60000),
-- (70, 55, 8, 8, 7500, 60000),
-- (71, 55, 13, 4, 15000, 60000),
-- (72, 55, 12, 4, 10000, 40000),
-- (73, 55, 11, 2, 20000, 40000),
-- (74, 55, 6, 2, 10000, 20000),
-- (75, 55, 1, 5, 9000, 45000),
-- (76, 55, 3, 1, 5000, 5000),
-- (77, 55, 15, 1, 15000, 15000),
-- (78, 55, 10, 2, 15000, 30000),
-- (79, 55, 5, 3, 5000, 15000),
-- (80, 55, 4, 5, 10000, 50000),
-- (81, 55, 9, 2, 5000, 10000),
-- (82, 55, 14, 1, 15000, 15000),
-- (83, 1, 1, 2, 10000, 20000),
-- (84, 2, 2, 3, 10000, 30000),
-- (85, 3, 3, 2, 5000, 10000),
-- (86, 4, 4, 5, 10000, 50000),
-- (87, 5, 5, 2, 5000, 10000),
-- (88, 6, 6, 3, 10000, 30000),
-- (89, 7, 7, 2, 20000, 40000),
-- (90, 8, 8, 1, 7500, 7500),
-- (91, 9, 9, 5, 5000, 25000),
-- (92, 10, 10, 3, 15000, 45000),
-- (93, 11, 11, 2, 20000, 40000),
-- (94, 12, 12, 1, 10000, 10000),
-- (95, 13, 13, 2, 15000, 30000),
-- (96, 14, 14, 1, 15000, 15000),
-- (97, 15, 15, 2, 15000, 30000),
-- (98, 16, 16, 2, 20000, 40000),
-- (99, 17, 17, 2, 25000, 50000),
-- (100, 18, 18, 3, 20000, 60000),
-- (101, 19, 19, 2, 15000, 30000),
-- (102, 20, 20, 1, 25000, 25000),
-- (103, 21, 1, 1, 10000, 10000),
-- (104, 22, 2, 3, 10000, 30000),
-- (105, 23, 3, 3, 5000, 15000),
-- (106, 24, 4, 2, 10000, 20000),
-- (107, 25, 5, 5, 5000, 25000),
-- (108, 26, 6, 3, 10000, 30000),
-- (109, 27, 7, 1, 20000, 20000),
-- (110, 28, 8, 2, 7500, 15000),
-- (111, 29, 9, 5, 5000, 25000),
-- (112, 30, 10, 1, 15000, 15000),
-- (113, 31, 11, 3, 20000, 60000),
-- (114, 32, 12, 4, 10000, 40000),
-- (115, 33, 13, 3, 15000, 45000),
-- (116, 34, 14, 1, 15000, 15000),
-- (117, 35, 15, 2, 15000, 30000),
-- (118, 36, 16, 2, 20000, 40000),
-- (119, 37, 17, 2, 25000, 50000),
-- (120, 38, 18, 2, 20000, 40000),
-- (121, 39, 19, 1, 15000, 15000),
-- (122, 40, 20, 1, 25000, 25000),
-- (123, 41, 1, 2, 10000, 20000),
-- (124, 42, 2, 3, 10000, 30000),
-- (125, 43, 3, 2, 5000, 10000),
-- (126, 44, 4, 5, 10000, 50000),
-- (127, 45, 5, 2, 5000, 10000),
-- (128, 46, 6, 3, 10000, 30000),
-- (129, 47, 7, 2, 20000, 40000),
-- (130, 48, 8, 1, 7500, 7500),
-- (131, 49, 9, 5, 5000, 25000),
-- (132, 50, 10, 3, 15000, 45000),
-- (133, 51, 1, 1, 10000, 10000),
-- (134, 51, 12, 1, 10000, 10000),
-- (135, 51, 13, 1, 15000, 15000),
-- (136, 51, 18, 1, 20000, 20000),
-- (137, 51, 17, 1, 25000, 25000),
-- (138, 51, 8, 1, 7500, 7500),
-- (139, 52, 1, 1, 10000, 10000),
-- (140, 52, 2, 2, 10000, 20000),
-- (141, 52, 8, 2, 7500, 15000),
-- (142, 52, 19, 1, 15000, 15000),
-- (143, 52, 17, 1, 25000, 25000),
-- (144, 53, 9, 1, 5000, 5000),
-- (145, 53, 8, 1, 7500, 7500),
-- (146, 53, 14, 1, 15000, 15000),
-- (147, 54, 2, 5, 10000, 50000),
-- (148, 54, 3, 5, 5000, 25000),
-- (149, 54, 15, 5, 15000, 75000),
-- (150, 55, 2, 4, 10000, 40000),
-- (151, 55, 7, 4, 15000, 60000),
-- (152, 55, 8, 8, 7500, 60000),
-- (153, 55, 13, 4, 15000, 60000),
-- (154, 55, 12, 4, 10000, 40000),
-- (155, 55, 11, 2, 20000, 40000),
-- (156, 55, 6, 2, 10000, 20000),
-- (157, 55, 1, 5, 9000, 45000),
-- (158, 55, 3, 1, 5000, 5000),
-- (159, 55, 15, 1, 15000, 15000),
-- (160, 55, 10, 2, 15000, 30000),
-- (161, 55, 5, 3, 5000, 15000),
-- (162, 55, 4, 5, 10000, 50000),
-- (163, 55, 9, 2, 5000, 10000),
-- (164, 55, 14, 1, 15000, 15000),
-- (165, 1, 1, 2, 10000, 20000),
-- (166, 2, 2, 3, 10000, 30000),
-- (167, 3, 3, 2, 5000, 10000),
-- (168, 4, 4, 5, 10000, 50000),
-- (169, 5, 5, 2, 5000, 10000),
-- (170, 6, 6, 3, 10000, 30000),
-- (171, 7, 7, 2, 20000, 40000),
-- (172, 8, 8, 1, 7500, 7500),
-- (173, 9, 9, 5, 5000, 25000),
-- (174, 10, 10, 3, 15000, 45000),
-- (175, 11, 11, 2, 20000, 40000),
-- (176, 12, 12, 1, 10000, 10000),
-- (177, 13, 13, 2, 15000, 30000),
-- (178, 14, 14, 1, 15000, 15000),
-- (179, 15, 15, 2, 15000, 30000),
-- (180, 16, 16, 2, 20000, 40000),
-- (181, 17, 17, 2, 25000, 50000),
-- (182, 18, 18, 3, 20000, 60000),
-- (183, 19, 19, 2, 15000, 30000),
-- (184, 20, 20, 1, 25000, 25000),
-- (185, 21, 1, 1, 10000, 10000),
-- (186, 22, 2, 3, 10000, 30000),
-- (187, 23, 3, 3, 5000, 15000),
-- (188, 24, 4, 2, 10000, 20000),
-- (189, 25, 5, 5, 5000, 25000),
-- (190, 26, 6, 3, 10000, 30000),
-- (191, 27, 7, 1, 20000, 20000),
-- (192, 28, 8, 2, 7500, 15000),
-- (193, 29, 9, 5, 5000, 25000),
-- (194, 30, 10, 1, 15000, 15000),
-- (195, 31, 11, 3, 20000, 60000),
-- (196, 32, 12, 4, 10000, 40000),
-- (197, 33, 13, 3, 15000, 45000),
-- (198, 34, 14, 1, 15000, 15000),
-- (199, 35, 15, 2, 15000, 30000),
-- (200, 36, 16, 2, 20000, 40000),
-- (201, 37, 17, 2, 25000, 50000),
-- (202, 38, 18, 2, 20000, 40000),
-- (203, 39, 19, 1, 15000, 15000),
-- (204, 40, 20, 1, 25000, 25000),
-- (205, 41, 1, 2, 10000, 20000),
-- (206, 42, 2, 3, 10000, 30000),
-- (207, 43, 3, 2, 5000, 10000),
-- (208, 44, 4, 5, 10000, 50000),
-- (209, 45, 5, 2, 5000, 10000),
-- (210, 46, 6, 3, 10000, 30000),
-- (211, 47, 7, 2, 20000, 40000),
-- (212, 48, 8, 1, 7500, 7500),
-- (213, 49, 9, 5, 5000, 25000),
-- (214, 50, 10, 3, 15000, 45000),
-- (215, 51, 1, 1, 10000, 10000),
-- (216, 51, 12, 1, 10000, 10000),
-- (217, 51, 13, 1, 15000, 15000),
-- (218, 51, 18, 1, 20000, 20000),
-- (219, 51, 17, 1, 25000, 25000),
-- (220, 51, 8, 1, 7500, 7500),
-- (221, 52, 1, 1, 10000, 10000),
-- (222, 52, 2, 2, 10000, 20000),
-- (223, 52, 8, 2, 7500, 15000),
-- (224, 52, 19, 1, 15000, 15000),
-- (225, 52, 17, 1, 25000, 25000),
-- (226, 53, 9, 1, 5000, 5000),
-- (227, 53, 8, 1, 7500, 7500),
-- (228, 53, 14, 1, 15000, 15000),
-- (229, 54, 2, 5, 10000, 50000),
-- (230, 54, 3, 5, 5000, 25000),
-- (231, 54, 15, 5, 15000, 75000),
-- (232, 55, 2, 4, 10000, 40000),
-- (233, 55, 7, 4, 15000, 60000),
-- (234, 55, 8, 8, 7500, 60000),
-- (235, 55, 13, 4, 15000, 60000),
-- (236, 55, 12, 4, 10000, 40000),
-- (237, 55, 11, 2, 20000, 40000),
-- (238, 55, 6, 2, 10000, 20000),
-- (239, 55, 1, 5, 9000, 45000),
-- (240, 55, 3, 1, 5000, 5000),
-- (241, 55, 15, 1, 15000, 15000),
-- (242, 55, 10, 2, 15000, 30000),
-- (243, 55, 5, 3, 5000, 15000),
-- (244, 55, 4, 5, 10000, 50000),
-- (245, 55, 9, 2, 5000, 10000),
-- (246, 55, 14, 1, 15000, 15000);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `chitietphieunhaphang`
--

CREATE TABLE `chitietphieunhaphang` (
  `id` int(11) NOT NULL,
  `phieuNhapHangId` int(11) DEFAULT NULL,
  `sanPhamId` int(11) DEFAULT NULL,
  `soLuong` int(11) DEFAULT NULL,
  `giaNhap` decimal(10,0) DEFAULT NULL,
  `thanhTien` decimal(10,0) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `chitietphieunhaphang`
--

-- INSERT INTO `chitietphieunhaphang` (`id`, `phieuNhapHangId`, `sanPhamId`, `soLuong`, `giaNhap`, `thanhTien`) VALUES
-- (1, 1, 1, 50, 8000, 400000),
-- (2, 2, 2, 50, 8000, 400000),
-- (3, 3, 3, 50, 4000, 200000),
-- (4, 4, 4, 50, 8000, 400000),
-- (5, 5, 5, 50, 4000, 200000),
-- (6, 6, 6, 50, 8000, 400000),
-- (7, 7, 7, 50, 16000, 800000),
-- (8, 8, 8, 50, 6000, 300000),
-- (9, 9, 9, 50, 4000, 200000),
-- (10, 10, 10, 50, 12000, 600000),
-- (11, 11, 11, 50, 16000, 800000),
-- (12, 12, 12, 50, 8000, 400000),
-- (13, 13, 13, 50, 12000, 600000),
-- (14, 14, 14, 50, 12000, 600000),
-- (15, 15, 15, 50, 12000, 600000),
-- (16, 16, 16, 50, 16000, 800000),
-- (17, 17, 17, 50, 20000, 1000000),
-- (18, 18, 18, 50, 16000, 800000),
-- (19, 19, 19, 50, 12000, 600000),
-- (20, 20, 20, 50, 20000, 1000000),
-- (21, 21, 1, 50, 8000, 400000),
-- (22, 22, 2, 50, 8000, 400000),
-- (23, 23, 3, 50, 4000, 200000),
-- (24, 24, 4, 50, 8000, 400000),
-- (25, 25, 5, 50, 4000, 200000),
-- (26, 26, 6, 50, 8000, 400000),
-- (27, 27, 7, 50, 16000, 800000),
-- (28, 28, 8, 50, 6000, 300000),
-- (29, 29, 9, 50, 4000, 200000),
-- (30, 30, 10, 50, 12000, 600000),
-- (31, 31, 11, 50, 16000, 800000),
-- (32, 32, 12, 50, 8000, 400000),
-- (33, 33, 13, 50, 12000, 600000),
-- (34, 34, 14, 50, 12000, 600000),
-- (35, 35, 15, 50, 12000, 600000),
-- (36, 36, 16, 50, 16000, 800000),
-- (37, 37, 17, 50, 20000, 1000000),
-- (38, 38, 18, 50, 16000, 800000),
-- (39, 39, 19, 50, 12000, 600000),
-- (40, 40, 20, 50, 20000, 1000000),
-- (41, 41, 1, 50, 8000, 400000),
-- (42, 42, 2, 50, 8000, 400000),
-- (43, 43, 3, 50, 4000, 200000),
-- (44, 44, 4, 50, 8000, 400000),
-- (45, 45, 5, 50, 4000, 200000),
-- (46, 46, 6, 50, 8000, 400000),
-- (47, 47, 7, 50, 16000, 800000),
-- (48, 48, 8, 50, 6000, 300000),
-- (49, 49, 9, 50, 4000, 200000),
-- (50, 50, 10, 50, 12000, 600000),
-- (51, 51, 4, 50, 5000, 250000),
-- (52, 1, 1, 50, 8000, 400000),
-- (53, 2, 2, 50, 8000, 400000),
-- (54, 3, 3, 50, 4000, 200000),
-- (55, 4, 4, 50, 8000, 400000),
-- (56, 5, 5, 50, 4000, 200000),
-- (57, 6, 6, 50, 8000, 400000),
-- (58, 7, 7, 50, 16000, 800000),
-- (59, 8, 8, 50, 6000, 300000),
-- (60, 9, 9, 50, 4000, 200000),
-- (61, 10, 10, 50, 12000, 600000),
-- (62, 11, 11, 50, 16000, 800000),
-- (63, 12, 12, 50, 8000, 400000),
-- (64, 13, 13, 50, 12000, 600000),
-- (65, 14, 14, 50, 12000, 600000),
-- (66, 15, 15, 50, 12000, 600000),
-- (67, 16, 16, 50, 16000, 800000),
-- (68, 17, 17, 50, 20000, 1000000),
-- (69, 18, 18, 50, 16000, 800000),
-- (70, 19, 19, 50, 12000, 600000),
-- (71, 20, 20, 50, 20000, 1000000),
-- (72, 21, 1, 50, 8000, 400000),
-- (73, 22, 2, 50, 8000, 400000),
-- (74, 23, 3, 50, 4000, 200000),
-- (75, 24, 4, 50, 8000, 400000),
-- (76, 25, 5, 50, 4000, 200000),
-- (77, 26, 6, 50, 8000, 400000),
-- (78, 27, 7, 50, 16000, 800000),
-- (79, 28, 8, 50, 6000, 300000),
-- (80, 29, 9, 50, 4000, 200000),
-- (81, 30, 10, 50, 12000, 600000),
-- (82, 31, 11, 50, 16000, 800000),
-- (83, 32, 12, 50, 8000, 400000),
-- (84, 33, 13, 50, 12000, 600000),
-- (85, 34, 14, 50, 12000, 600000),
-- (86, 35, 15, 50, 12000, 600000),
-- (87, 36, 16, 50, 16000, 800000),
-- (88, 37, 17, 50, 20000, 1000000),
-- (89, 38, 18, 50, 16000, 800000),
-- (90, 39, 19, 50, 12000, 600000),
-- (91, 40, 20, 50, 20000, 1000000),
-- (92, 41, 1, 50, 8000, 400000),
-- (93, 42, 2, 50, 8000, 400000),
-- (94, 43, 3, 50, 4000, 200000),
-- (95, 44, 4, 50, 8000, 400000),
-- (96, 45, 5, 50, 4000, 200000),
-- (97, 46, 6, 50, 8000, 400000),
-- (98, 47, 7, 50, 16000, 800000),
-- (99, 48, 8, 50, 6000, 300000),
-- (100, 49, 9, 50, 4000, 200000),
-- (101, 50, 10, 50, 12000, 600000),
-- (102, 1, 1, 50, 8000, 400000),
-- (103, 2, 2, 50, 8000, 400000),
-- (104, 3, 3, 50, 4000, 200000),
-- (105, 4, 4, 50, 8000, 400000),
-- (106, 5, 5, 50, 4000, 200000),
-- (107, 6, 6, 50, 8000, 400000),
-- (108, 7, 7, 50, 16000, 800000),
-- (109, 8, 8, 50, 6000, 300000),
-- (110, 9, 9, 50, 4000, 200000),
-- (111, 10, 10, 50, 12000, 600000),
-- (112, 11, 11, 50, 16000, 800000),
-- (113, 12, 12, 50, 8000, 400000),
-- (114, 13, 13, 50, 12000, 600000),
-- (115, 14, 14, 50, 12000, 600000),
-- (116, 15, 15, 50, 12000, 600000),
-- (117, 16, 16, 50, 16000, 800000),
-- (118, 17, 17, 50, 20000, 1000000),
-- (119, 18, 18, 50, 16000, 800000),
-- (120, 19, 19, 50, 12000, 600000),
-- (121, 20, 20, 50, 20000, 1000000),
-- (122, 21, 1, 50, 8000, 400000),
-- (123, 22, 2, 50, 8000, 400000),
-- (124, 23, 3, 50, 4000, 200000),
-- (125, 24, 4, 50, 8000, 400000),
-- (126, 25, 5, 50, 4000, 200000),
-- (127, 26, 6, 50, 8000, 400000),
-- (128, 27, 7, 50, 16000, 800000),
-- (129, 28, 8, 50, 6000, 300000),
-- (130, 29, 9, 50, 4000, 200000),
-- (131, 30, 10, 50, 12000, 600000),
-- (132, 31, 11, 50, 16000, 800000),
-- (133, 32, 12, 50, 8000, 400000),
-- (134, 33, 13, 50, 12000, 600000),
-- (135, 34, 14, 50, 12000, 600000),
-- (136, 35, 15, 50, 12000, 600000),
-- (137, 36, 16, 50, 16000, 800000),
-- (138, 37, 17, 50, 20000, 1000000),
-- (139, 38, 18, 50, 16000, 800000),
-- (140, 39, 19, 50, 12000, 600000),
-- (141, 40, 20, 50, 20000, 1000000),
-- (142, 41, 1, 50, 8000, 400000),
-- (143, 42, 2, 50, 8000, 400000),
-- (144, 43, 3, 50, 4000, 200000),
-- (145, 44, 4, 50, 8000, 400000),
-- (146, 45, 5, 50, 4000, 200000),
-- (147, 46, 6, 50, 8000, 400000),
-- (148, 47, 7, 50, 16000, 800000),
-- (149, 48, 8, 50, 6000, 300000),
-- (150, 49, 9, 50, 4000, 200000),
-- (151, 50, 10, 50, 12000, 600000);

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
  `trangThai` tinyint(1) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `chuongtrinhkhuyenmai`
--

-- INSERT INTO `chuongtrinhkhuyenmai` (`id`, `ten`, `ghiChu`, `ngayBatDau`, `ngayKetThuc`, `trangThai`) VALUES
-- (1, 'Siêu Sale 1/4', '', '2026-03-27 00:00:00', '2026-04-15 00:00:00', 0);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `chuongtrinhkhuyenmaihd`
--

CREATE TABLE `chuongtrinhkhuyenmaihd` (
  `id` int(11) NOT NULL,
  `chuongTrinhKhuyenMaiId` int(11) DEFAULT NULL,
  `soTienHd` int(11) DEFAULT NULL,
  `giaTriGiam` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `chuongtrinhkhuyenmaihd`
--

-- INSERT INTO `chuongtrinhkhuyenmaihd` (`id`, `chuongTrinhKhuyenMaiId`, `soTienHd`, `giaTriGiam`) VALUES
-- (3, 1, 500000, 10000);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `chuongtrinhkhuyenmaisp`
--

CREATE TABLE `chuongtrinhkhuyenmaisp` (
  `id` int(11) NOT NULL,
  `chuongTrinhKhuyenMaiId` int(11) DEFAULT NULL,
  `sanPhamId` int(11) DEFAULT NULL,
  `giaTriGiam` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `chuongtrinhkhuyenmaisp`
--

-- INSERT INTO `chuongtrinhkhuyenmaisp` (`id`, `chuongTrinhKhuyenMaiId`, `sanPhamId`, `giaTriGiam`) VALUES
-- (5, 1, 1, 1000),
-- (6, 1, 7, 5000);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `hangsanxuat`
--

CREATE TABLE `hangsanxuat` (
  `id` int(11) NOT NULL,
  `ten` varchar(255) DEFAULT NULL,
  `diaChi` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `hangsanxuat`
--

-- INSERT INTO `hangsanxuat` (`id`, `ten`, `diaChi`, `phone`) VALUES
-- (1, 'Suntory PepsiCo', NULL, NULL),
-- (2, 'Coca-Cola VN', NULL, NULL),
-- (3, 'Acecook VN', NULL, NULL),
-- (4, 'Masan Consumer', NULL, NULL),
-- (5, 'Vinamilk', NULL, NULL),
-- (6, 'Nestlé VN', NULL, NULL),
-- (7, 'Orion Food', NULL, NULL),
-- (8, 'Kido Group', NULL, NULL),
-- (9, 'Heineken VN', NULL, NULL),
-- (10, 'C.P. Vietnam', NULL, NULL);

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `hoadon`
--

-- INSERT INTO `hoadon` (`id`, `khachHangId`, `nhanVienId`, `tongTien`, `ngayLapHD`) VALUES
-- (1, 1, 1, 20000, '2025-01-02 07:15:00'),
-- (2, 2, 2, 30000, '2025-01-08 12:30:00'),
-- (3, 3, 1, 10000, '2025-01-14 18:45:00'),
-- (4, 4, 2, 50000, '2025-01-22 20:10:00'),
-- (5, 5, 1, 10000, '2025-01-28 09:00:00'),
-- (6, 1, 2, 30000, '2025-02-04 11:20:00'),
-- (7, 2, 1, 40000, '2025-02-10 13:40:00'),
-- (8, 3, 2, 7500, '2025-02-18 15:55:00'),
-- (9, 4, 1, 25000, '2025-02-25 19:15:00'),
-- (10, 5, 2, 45000, '2025-03-03 21:05:00'),
-- (11, 1, 1, 40000, '2025-03-09 08:30:00'),
-- (12, 2, 2, 10000, '2025-03-15 10:45:00'),
-- (13, 3, 1, 30000, '2025-03-22 14:20:00'),
-- (14, 4, 2, 15000, '2025-03-29 17:00:00'),
-- (15, 5, 1, 30000, '2025-04-05 19:30:00'),
-- (16, 1, 2, 40000, '2025-04-12 07:45:00'),
-- (17, 2, 1, 50000, '2025-04-18 09:10:00'),
-- (18, 3, 2, 60000, '2025-04-25 12:25:00'),
-- (19, 4, 1, 30000, '2025-05-02 16:40:00'),
-- (20, 5, 2, 25000, '2025-05-09 18:50:00'),
-- (21, 1, 1, 10000, '2025-05-16 20:00:00'),
-- (22, 2, 2, 30000, '2025-05-23 22:15:00'),
-- (23, 3, 1, 15000, '2025-05-30 06:30:00'),
-- (24, 4, 2, 20000, '2025-06-06 08:40:00'),
-- (25, 5, 1, 25000, '2025-06-13 11:55:00'),
-- (26, 1, 2, 30000, '2025-06-20 13:10:00'),
-- (27, 2, 1, 20000, '2025-06-27 15:20:00'),
-- (28, 3, 2, 15000, '2025-07-04 17:35:00'),
-- (29, 4, 1, 25000, '2025-07-11 19:45:00'),
-- (30, 5, 2, 15000, '2025-07-18 21:00:00'),
-- (31, 1, 1, 60000, '2025-07-25 07:15:00'),
-- (32, 2, 2, 40000, '2025-08-01 09:30:00'),
-- (33, 3, 1, 45000, '2025-08-08 11:40:00'),
-- (34, 4, 2, 15000, '2025-08-15 14:55:00'),
-- (35, 5, 1, 30000, '2025-08-22 16:10:00'),
-- (36, 1, 2, 40000, '2025-08-29 18:25:00'),
-- (37, 2, 1, 50000, '2025-09-05 20:30:00'),
-- (38, 3, 2, 40000, '2025-09-12 22:45:00'),
-- (39, 4, 1, 15000, '2025-09-19 06:50:00'),
-- (40, 5, 2, 25000, '2025-09-26 08:05:00'),
-- (41, 1, 1, 20000, '2025-10-03 10:15:00'),
-- (42, 2, 2, 30000, '2025-10-10 12:30:00'),
-- (43, 3, 1, 10000, '2025-10-17 14:40:00'),
-- (44, 4, 2, 50000, '2025-10-24 16:55:00'),
-- (45, 5, 1, 10000, '2025-10-31 19:10:00'),
-- (46, 1, 2, 30000, '2025-11-07 21:20:00'),
-- (47, 2, 1, 40000, '2025-11-14 07:35:00'),
-- (48, 3, 2, 7500, '2025-11-21 09:45:00'),
-- (49, 4, 1, 25000, '2025-11-28 11:00:00'),
-- (50, 5, 2, 45000, '2025-12-05 13:10:00'),
-- (51, 1, 1, 87500, '2026-03-22 22:05:06'),
-- (52, 1, 1, 85000, '2026-03-22 22:23:46'),
-- (53, 6, 1, 27500, '2026-03-22 22:24:17'),
-- (54, 6, 1, 150000, '2026-03-22 23:24:24'),
-- (55, 6, 2, 495000, '2026-03-25 20:45:07');

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `khachhang`
--

-- INSERT INTO `khachhang` (`id`, `ho`, `ten`, `phone`, `diaChi`) VALUES
-- (1, 'Trần Văn', 'Nam', '0911222333', 'Quận 1, TP.HCM'),
-- (2, 'Lê Thị', 'Hoa', '0922333444', 'Quận 3, TP.HCM'),
-- (3, 'Phạm', 'Hùng', '0933444555', 'Quận 5, TP.HCM'),
-- (4, 'Vũ Bích', 'Phượng', '0944555666', 'Quận 7, TP.HCM'),
-- (5, 'Hoàng Tuấn', 'Anh', '0955666777', 'Thủ Đức, TP.HCM'),
-- (6, 'nguyễn', 'duy', '0354271956', 'avc');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `loaisanpham`
--

CREATE TABLE `loaisanpham` (
  `id` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `loaisanpham`
--

-- INSERT INTO `loaisanpham` (`id`, `name`) VALUES
-- (1, 'Đồ uống lạnh'),
-- (2, 'Mì & Thực phẩm ăn liền'),
-- (3, 'Sữa & Chế phẩm'),
-- (4, 'Bánh Kẹo & Snack'),
-- (5, 'Đồ ăn nhanh (Ready-to-eat)'),
-- (6, 'Rượu Bia'),
-- (7, 'Hàng Tiêu Dùng / Y Tế');

-- -- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `nhacungcap`
--

CREATE TABLE `nhacungcap` (
  `id` int(11) NOT NULL,
  `ten` varchar(255) DEFAULT NULL,
  `diaChi` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `nhacungcap`
--

-- INSERT INTO `nhacungcap` (`id`, `ten`, `diaChi`, `phone`) VALUES
-- (1, 'NPP Nước Giải Khát Tân Bình', 'Tân Bình, TP.HCM', '0901 111 222'),
-- (2, 'NPP Thực Phẩm Chợ Lớn', 'Quận 6, TP.HCM', '0902 222 333'),
-- (3, 'Đại Lý Bánh Kẹo Hưng Thịnh', 'Quận 5, TP.HCM', '0903 333 444'),
-- (4, 'Công Ty TNHH Phú Thái', 'Quận 10, TP.HCM', '0904 444 555'),
-- (5, 'NPP Hàng Tiêu Dùng Sài Gòn', 'Thủ Đức, TP.HCM', '0905 555 666');

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- -- Đang đổ dữ liệu cho bảng `nhanvien`
-- --

-- INSERT INTO `nhanvien` (`id`, `ho`, `ten`, `phone`, `diaChi`, `ngaySinh`, `Luong`) VALUES
-- (1, 'Nguyễn Ngọc Phương', 'Duy', '0354271956', 'Sài Gòn', '2006-12-22 00:00:00', 15000000),
-- (2, 'Đặng Thanh', 'Tuấn', '0972139443', 'Sài Gòn', '2006-07-09 00:00:00', 12000000);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `phieunhaphang`
--

CREATE TABLE `phieunhaphang` (
  `id` int(11) NOT NULL,
  `nhanVienId` int(11) DEFAULT NULL,
  `nhaCungCapId` int(11) DEFAULT NULL,
  `tongTien` decimal(10,0) DEFAULT NULL,
  `ngayNhap` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `phieunhaphang`
-- --

-- INSERT INTO `phieunhaphang` (`id`, `nhanVienId`, `nhaCungCapId`, `tongTien`, `ngayNhap`) VALUES
-- (1, 1, 1, 400000, '2025-01-05 00:00:00'),
-- (2, 2, 2, 400000, '2025-01-15 00:00:00'),
-- (3, 1, 3, 200000, '2025-02-02 00:00:00'),
-- (4, 2, 4, 400000, '2025-02-20 00:00:00'),
-- (5, 1, 5, 200000, '2025-03-05 00:00:00'),
-- (6, 2, 1, 400000, '2025-03-18 00:00:00'),
-- (7, 1, 2, 800000, '2025-04-01 00:00:00'),
-- (8, 2, 3, 300000, '2025-04-12 00:00:00'),
-- (9, 1, 4, 200000, '2025-05-05 00:00:00'),
-- (10, 2, 5, 600000, '2025-05-22 00:00:00'),
-- (11, 1, 1, 800000, '2025-06-08 00:00:00'),
-- (12, 2, 2, 400000, '2025-06-25 00:00:00'),
-- (13, 1, 3, 600000, '2025-07-02 00:00:00'),
-- (14, 2, 4, 600000, '2025-07-19 00:00:00'),
-- (15, 1, 5, 600000, '2025-08-05 00:00:00'),
-- (16, 2, 1, 800000, '2025-08-21 00:00:00'),
-- (17, 1, 2, 1000000, '2025-09-07 00:00:00'),
-- (18, 2, 3, 800000, '2025-09-28 00:00:00'),
-- (19, 1, 4, 600000, '2025-10-10 00:00:00'),
-- (20, 2, 5, 1250000, '2025-10-25 00:00:00'),
-- (21, 1, 1, 400000, '2025-11-05 00:00:00'),
-- (22, 2, 2, 400000, '2025-11-20 00:00:00'),
-- (23, 1, 3, 200000, '2025-12-05 00:00:00'),
-- (24, 2, 4, 400000, '2025-12-20 00:00:00'),
-- (25, 1, 5, 200000, '2026-01-04 00:00:00'),
-- (26, 2, 1, 400000, '2026-01-18 00:00:00'),
-- (27, 1, 2, 800000, '2026-02-02 00:00:00'),
-- (28, 2, 3, 300000, '2026-02-15 00:00:00'),
-- (29, 1, 4, 200000, '2026-03-05 00:00:00'),
-- (30, 2, 5, 600000, '2026-03-22 00:00:00'),
-- (31, 1, 1, 800000, '2026-04-08 00:00:00'),
-- (32, 2, 2, 400000, '2026-04-25 00:00:00'),
-- (33, 1, 3, 600000, '2026-05-02 00:00:00'),
-- (34, 2, 4, 600000, '2026-05-19 00:00:00'),
-- (35, 1, 5, 600000, '2026-06-05 00:00:00'),
-- (36, 2, 1, 800000, '2026-06-21 00:00:00'),
-- (37, 1, 2, 1000000, '2026-07-07 00:00:00'),
-- (38, 2, 3, 800000, '2026-07-28 00:00:00'),
-- (39, 1, 4, 600000, '2026-08-10 00:00:00'),
-- (40, 2, 5, 1250000, '2026-08-25 00:00:00'),
-- (41, 1, 1, 400000, '2026-09-05 00:00:00'),
-- (42, 2, 2, 400000, '2026-09-20 00:00:00'),
-- (43, 1, 3, 200000, '2026-10-05 00:00:00'),
-- (44, 2, 4, 400000, '2026-10-20 00:00:00'),
-- (45, 1, 5, 200000, '2026-11-04 00:00:00'),
-- (46, 2, 1, 400000, '2026-11-18 00:00:00'),
-- (47, 1, 2, 800000, '2026-12-02 00:00:00'),
-- (48, 2, 3, 300000, '2026-12-10 00:00:00'),
-- (49, 1, 4, 200000, '2026-12-20 00:00:00'),
-- (50, 2, 5, 600000, '2026-12-28 00:00:00'),
-- (51, 1, 1, 250000, '2026-03-27 00:00:00');

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
  `giaNhap` bigint(20) DEFAULT 0,
  `loiNhuan` double DEFAULT 0,
  `donViTinh` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `sanpham`
--

-- INSERT INTO `sanpham` (`id`, `loaiSanPhamId`, `hangId`, `ten`, `soLuong`, `donGia`, `giaNhap`, `loiNhuan`, `donViTinh`) VALUES
-- (1, 1, 1, 'Nước ngọt Pepsi 320ml', 93, 10000, 0, 0, 'Lon'),
-- (2, 1, 2, 'Nước ngọt Coca-Cola 320ml', 89, 10000, 0, 0, 'Lon'),
-- (3, 1, 1, 'Nước suối Aquafina 500ml', 144, 5000, 0, 0, 'Chai'),
-- (4, 1, 1, 'Trà Ô Long TEA+ Plus 455ml', 75, 10000, 0, 0, 'Chai'),
-- (5, 2, 3, 'Mì ly Hảo Hảo Tôm Chua Cay', 117, 5000, 0, 0, 'Ly'),
-- (6, 2, 3, 'Mì ly Modern Lẩu Thái', 88, 10000, 0, 0, 'Ly'),
-- (7, 5, 4, 'Xúc xích Ponnie 3+1', 56, 20000, 0, 0, 'Gói'),
-- (8, 3, 5, 'Sữa tươi Vinamilk Ít Đường 180ml', 88, 7500, 0, 0, 'Hộp'),
-- (9, 3, 5, 'Sữa chua uống Probi 65ml', 117, 5000, 0, 0, 'Chai'),
-- (10, 1, 6, 'Cà phê lon Nescafe', 48, 15000, 0, 0, 'Lon'),
-- (11, 4, 7, 'Bánh Chocopie Hộp 2 cái', 38, 20000, 0, 0, 'Hộp'),
-- (12, 4, 7, 'Snack khoai tây Ostar', 75, 10000, 0, 0, 'Gói'),
-- (13, 5, 8, 'Bánh mì Sandwich Kido', 25, 15000, 0, 0, 'Gói'),
-- (14, 5, 10, 'Bánh bao nhân thịt trứng cút', 23, 15000, 0, 0, 'Cái'),
-- (15, 1, 2, 'Nước tăng lực Redbull', 104, 15000, 0, 0, 'Lon'),
-- (16, 6, 9, 'Bia Tiger Thường 330ml', 200, 20000, 0, 0, 'Lon'),
-- (17, 6, 9, 'Bia Heineken Silver 330ml', 98, 25000, 0, 0, 'Lon'),
-- (18, 5, 4, 'Cơm nắm cá ngừ Mayo', 19, 20000, 0, 0, 'Cái'),
-- (19, 4, 6, 'Kẹo Kitkat Trà Xanh', 34, 15000, 0, 0, 'Thanh'),
-- (20, 7, 2, 'Khăn ướt cồn Let-green', 45, 25000, 0, 0, 'Gói');

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `chitiethoadon`
--
ALTER TABLE `chitiethoadon`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_cthd_hd` (`hoaDonId`),
  ADD KEY `fk_cthd_sp` (`sanPhamId`);

--
-- Chỉ mục cho bảng `chitietphieunhaphang`
--
ALTER TABLE `chitietphieunhaphang`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_ctpn_pn` (`phieuNhapHangId`),
  ADD KEY `fk_ctpn_sp` (`sanPhamId`);

--
-- Chỉ mục cho bảng `chuongtrinhkhuyenmai`
--
ALTER TABLE `chuongtrinhkhuyenmai`
  ADD PRIMARY KEY (`id`);

--
-- Chỉ mục cho bảng `chuongtrinhkhuyenmaihd`
--
ALTER TABLE `chuongtrinhkhuyenmaihd`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_kmhd_km` (`chuongTrinhKhuyenMaiId`);

--
-- Chỉ mục cho bảng `chuongtrinhkhuyenmaisp`
--
ALTER TABLE `chuongtrinhkhuyenmaisp`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_kmsp_km` (`chuongTrinhKhuyenMaiId`),
  ADD KEY `fk_kmsp_sp` (`sanPhamId`);

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
  ADD KEY `fk_hd_kh` (`khachHangId`),
  ADD KEY `fk_hd_nv` (`nhanVienId`);

--
-- Chỉ mục cho bảng `khachhang`
--
ALTER TABLE `khachhang`
  ADD PRIMARY KEY (`id`);

--
-- Chỉ mục cho bảng `loaisanpham`
--
ALTER TABLE `loaisanpham`
  ADD PRIMARY KEY (`id`);

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
  ADD KEY `fk_pn_nv` (`nhanVienId`),
  ADD KEY `fk_pn_ncc` (`nhaCungCapId`);

--
-- Chỉ mục cho bảng `sanpham`
--
ALTER TABLE `sanpham`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_sp_loaisp` (`loaiSanPhamId`),
  ADD KEY `fk_sp_hang` (`hangId`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `chitiethoadon`
--
ALTER TABLE `chitiethoadon`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=247;

--
-- AUTO_INCREMENT cho bảng `chitietphieunhaphang`
--
ALTER TABLE `chitietphieunhaphang`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=152;

--
-- AUTO_INCREMENT cho bảng `chuongtrinhkhuyenmai`
--
ALTER TABLE `chuongtrinhkhuyenmai`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT cho bảng `chuongtrinhkhuyenmaihd`
--
ALTER TABLE `chuongtrinhkhuyenmaihd`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT cho bảng `chuongtrinhkhuyenmaisp`
--
ALTER TABLE `chuongtrinhkhuyenmaisp`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT cho bảng `hangsanxuat`
--
ALTER TABLE `hangsanxuat`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT cho bảng `hoadon`
--
ALTER TABLE `hoadon`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=56;

--
-- AUTO_INCREMENT cho bảng `khachhang`
--
ALTER TABLE `khachhang`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT cho bảng `loaisanpham`
--
ALTER TABLE `loaisanpham`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT cho bảng `nhacungcap`
--
ALTER TABLE `nhacungcap`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT cho bảng `nhanvien`
--
ALTER TABLE `nhanvien`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT cho bảng `phieunhaphang`
--
ALTER TABLE `phieunhaphang`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=52;

--
-- AUTO_INCREMENT cho bảng `sanpham`
--
ALTER TABLE `sanpham`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `chitiethoadon`
--
ALTER TABLE `chitiethoadon`
  ADD CONSTRAINT `fk_cthd_hd` FOREIGN KEY (`hoaDonId`) REFERENCES `hoadon` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_cthd_sp` FOREIGN KEY (`sanPhamId`) REFERENCES `sanpham` (`id`) ON UPDATE CASCADE;

--
-- Các ràng buộc cho bảng `chitietphieunhaphang`
--
ALTER TABLE `chitietphieunhaphang`
  ADD CONSTRAINT `fk_ctpn_pn` FOREIGN KEY (`phieuNhapHangId`) REFERENCES `phieunhaphang` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_ctpn_sp` FOREIGN KEY (`sanPhamId`) REFERENCES `sanpham` (`id`) ON UPDATE CASCADE;

--
-- Các ràng buộc cho bảng `chuongtrinhkhuyenmaihd`
--
ALTER TABLE `chuongtrinhkhuyenmaihd`
  ADD CONSTRAINT `fk_kmhd_km` FOREIGN KEY (`chuongTrinhKhuyenMaiId`) REFERENCES `chuongtrinhkhuyenmai` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Các ràng buộc cho bảng `chuongtrinhkhuyenmaisp`
--
ALTER TABLE `chuongtrinhkhuyenmaisp`
  ADD CONSTRAINT `fk_kmsp_km` FOREIGN KEY (`chuongTrinhKhuyenMaiId`) REFERENCES `chuongtrinhkhuyenmai` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_kmsp_sp` FOREIGN KEY (`sanPhamId`) REFERENCES `sanpham` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Các ràng buộc cho bảng `hoadon`
--
ALTER TABLE `hoadon`
  ADD CONSTRAINT `fk_hd_kh` FOREIGN KEY (`khachHangId`) REFERENCES `khachhang` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_hd_nv` FOREIGN KEY (`nhanVienId`) REFERENCES `nhanvien` (`id`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- Các ràng buộc cho bảng `phieunhaphang`
--
ALTER TABLE `phieunhaphang`
  ADD CONSTRAINT `fk_pn_ncc` FOREIGN KEY (`nhaCungCapId`) REFERENCES `nhacungcap` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_pn_nv` FOREIGN KEY (`nhanVienId`) REFERENCES `nhanvien` (`id`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- Các ràng buộc cho bảng `sanpham`
--
ALTER TABLE `sanpham`
  ADD CONSTRAINT `fk_sp_hang` FOREIGN KEY (`hangId`) REFERENCES `hangsanxuat` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_sp_loaisp` FOREIGN KEY (`loaiSanPhamId`) REFERENCES `loaisanpham` (`id`) ON DELETE SET NULL ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
