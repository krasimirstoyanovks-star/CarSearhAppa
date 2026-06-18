-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 18, 2026 at 08:50 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `carsearchdb`
--

-- --------------------------------------------------------

--
-- Table structure for table `comments`
--

CREATE TABLE `comments` (
  `id` int(11) NOT NULL,
  `vin` varchar(17) DEFAULT NULL,
  `comment` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `parts`
--

CREATE TABLE `parts` (
  `id` int(11) NOT NULL,
  `make` varchar(50) NOT NULL,
  `model` varchar(50) NOT NULL,
  `name` varchar(100) NOT NULL,
  `category` varchar(100) NOT NULL,
  `price` varchar(20) NOT NULL,
  `availability` varchar(50) NOT NULL,
  `imageUrl` text DEFAULT NULL,
  `description` text DEFAULT NULL,
  `paymentUrl` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `parts`
--

INSERT INTO `parts` (`id`, `make`, `model`, `name`, `category`, `price`, `availability`, `imageUrl`, `description`, `paymentUrl`) VALUES
(1, 'BMW', 'X5', 'Маслен филтър', 'Консумативи', '18.50€.', 'В наличност', 'https://img.freepik.com/frehttps://s13emagst.akamaized.net/products/66401/66400642/images/res_b072ffb68cceb3e282cbaf233afc1486.jpg?width=720&height=720&hash=14A234BD70AC4C32E1319DE1B0A7EB1C', 'Осигурява оптимално пречистване на моторното масло от замърсявания и метални частици. Удължава живота на двигателя.', 'https://buy.stripe.com/test_3cI8wQacagdNeMMcys6Zy01'),
(2, 'BMW', 'X5', 'Предни накладки', 'Спирачна система', '95.00 €.', 'В наличност', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSAs9AlDxJJ8njB3q9e6JhkmQ5S46JrVw07XkpbWGVGZS1XFP1Ec_5MbLI&s=10', 'Висококачествен вентилиран спирачна накладка с антикорозионно покритие. Осигурява максимална безопасност и кратък спирачен път.', 'https://buy.stripe.com/test_3cI8wQacagdNeMMcys6Zy01'),
(3, 'BMW', 'X5', 'Въздушен филтър', 'Консумативи', '32.00 €.', 'Изчерпан', 'https://img.freepik.com/free-photo/air-filter-car-white-background_181624-32522.jpg', 'Високотехнологичен въздушен филтър с голям капацитет на прахоулавяне. Подобрява горивния процес и мощността.', 'https://buy.stripe.com/test_3cI8wQacagdNeMMcys6Zy01'),
(4, 'Audi', 'A4', 'Горивен филтър', 'Консумативи', '45.00 €.', 'В наличност', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQH6cIWS8QFkhHD8oSQYU73PP2WGupGW3Bmf84Q8AouTEkD1mEeTmZoguzk&s=10', 'Високотехнологичен горивен филтър с голям капацитет на прахоулавяне. Подобрява горивния процес и мощността.', 'https://buy.stripe.com/test_3cI8wQacagdNeMMcys6Zy01'),
(5, 'Audi', 'A4', 'Спирачни дискове', 'Спирачна система', '120.00 €.', 'В наличност', 'https://img.freepik.com/free-photo/car-brake-disc-isolated-white-background_181624-28267.jpg', 'Висококачествен вентилиран спирачен диск с антикорозионно покритие. Осигурява максимална безопасност и кратък спирачен път.', 'https://buy.stripe.com/test_fZu28sgAye5F6gg2XS6Zy00'),
(6, 'BMW', '320d', 'Маслен филтър (Bosch)', 'Консумативи', '24.50 €.', 'В наличност', 'https://cdn.autodoc.de/thumb?id=755391&m=0&n=2&lng=bg&rev=94078029', 'Осигурява оптимално пречистване на моторното масло от замърсявания и метални частици. Удължава живота на двигателя.', 'https://buy.stripe.com/test_3cI8wQacagdNeMMcys6Zy01'),
(7, 'BMW', '320d', 'Спирачни дискове - предни', 'Спирачна система', '210.00 €.', 'В наличност', 'https://s13emagst.akamaized.net/products/66401/66400642/images/res_b072ffb68cceb3e282cbaf233afc1486.jpg?width=720&height=720&hash=14A234BD70AC4C32E1319DE1B0A7EB1C', 'Висококачествен вентилиран спирачен диск с антикорозионно покритие. Осигурява максимална безопасност и кратък спирачен път.', 'https://buy.stripe.com/test_fZu28sgAye5F6gg2XS6Zy00'),
(8, 'BMW', '320d', 'Моторно масло 5W30 5L', 'Масла', '125.00 €.', 'В наличност', 'https://automall.md/Images/GetImage?imageId=9583227', 'Напълно синтетично моторно масло за съвременни двигатели. Намалява триенето и разхода на гориво.', 'https://buy.stripe.com/test_eVqeVefwu4v56gg6a46Zy02'),
(9, 'Audi', 'A3', 'Въздушен филтър', 'Консумативи', '28.00 €.', 'В наличност', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT-kd_t3KsKZMnaFfO8hjDAWejXG2DayR-VH4kIIKIBtA&s=10', 'Високотехнологичен въздушен филтър с голям капацитет на прахоулавяне. Подобрява горивния процес и мощността.', 'https://buy.stripe.com/test_9B6aEY6ZY5z96gg9mg6Zy03'),
(10, 'Audi', 'A3', 'Спирачни накладки (TRW)', 'Спирачна система', '85.00 €.', 'В наличност', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSAs9AlDxJJ8njB3q9e6JhkmQ5S46JrVw07XkpbWGVGZS1XFP1Ec_5MbLI&s=10', 'Висококачествен вентилиран спирачна накладка с антикорозионно покритие. Осигурява максимална безопасност и кратък спирачен път.', 'https://buy.stripe.com/test_3cI8wQacagdNeMMcys6Zy01'),
(11, 'Audi', 'A3', 'Запалителна свещ (NGK)', 'Двигател', '19.20 €.', 'В наличност', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQIjwoxeJC6TVlsuidqP-ryIiuF0PPULC96ZAcxm33HHU1koW7UNPjDRUo&s=10', 'Висококачествен запалителна свещ с антикорозионно покритие. Осигурява максимална безопасност и кратък спирачен път.', 'https://buy.stripe.com/test_3cI8wQacagdNeMMcys6Zy01'),
(12, 'Volkswagen', 'Golf 6', 'Ангренажен комплект', 'Двигател', '265.00 €.', 'По заявка', 'https://media.autodoc.de/360_photos/2385000/h-preview.jpg', 'Висококачествен ангренажен комплект с антикорозионно покритие. Осигурява максимална безопасност и кратък спирачен път.', 'https://buy.stripe.com/test_3cI8wQacagdNeMMcys6Zy01'),
(13, 'Volkswagen', 'Golf 6', 'Горивен филтър', 'Консумативи', '42.00 €.', 'В наличност', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQH6cIWS8QFkhHD8oSQYU73PP2WGupGW3Bmf84Q8AouTEkD1mEeTmZoguzk&s=10', 'Високотехнологичен горивен филтър с голям капацитет на прахоулавяне. Подобрява горивния процес и мощността.', 'https://buy.stripe.com/test_3cI8wQacagdNeMMcys6Zy01'),
(14, 'Volkswagen', 'Golf 6', 'Акумулатор 74Ah', 'Електрическа система', '175.00 €.', 'В наличност', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSg5wEYEqc4jZnfcEdCVnogPPmkFgCGukwr1XsQliA9iu1lko6W0oqwqO0&s=10', 'Високотехнологичен акумулатор с голям капацитет на прахоулавяне. Подобрява горивния процес и мощността.', 'https://buy.stripe.com/test_3cI8wQacagdNeMMcys6Zy01'),
(15, 'Renault', 'Megane', 'Филтър купе', 'Консумативи', '28.00 €.', 'В наличност', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTWMxeyd-H6VDH0rct5U1Hhi4Ph4SQCXD_Lfq9TxXN0alwNt4hUn5C4Obda&s=10', 'Високотехнологичен въздушен филтър с голям капацитет на прахоулавяне. Подобрява горивния процес и мощността.', 'https://buy.stripe.com/test_9B6aEY6ZY5z96gg9mg6Zy03'),
(16, 'Renault', 'Megane', 'Водна помпа', 'Охладителна система', '88.00 €.', 'В наличност', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ7UZng1nUZdpY_QHXfoVuyV3T7Lcoi5amt9GnTy-jOPBUIKN6iFuM_IFg&s=10', 'Високотехнологичен водна помпа с голям капацитет на прахоулавяне. Подобрява горивния процес и мощността.', 'https://buy.stripe.com/test_3cI8wQacagdNeMMcys6Zy01'),
(17, 'Renault', 'Megane', 'Преден амортисьор', 'Окачване', '115.00 €.', 'В наличност', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSeFr34T6Csx7-u43hfOXISmx2Av8VWheDH8yXbswLevSZEdMLmMgMfJf_s&s=10', 'Високотехнологичен амортисъор с голям капацитет на прахоулавяне. Подобрява горивния процес и мощността.', 'https://buy.stripe.com/test_3cI8wQacagdNeMMcys6Zy01');

-- --------------------------------------------------------

--
-- Table structure for table `search_history`
--

CREATE TABLE `search_history` (
  `id` int(11) NOT NULL,
  `vin` varchar(17) DEFAULT NULL,
  `search_date` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `username` varchar(50) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `username`, `email`, `password`) VALUES
(1, 'Kras Stoyanov', 'krasku@gmail.com', '$2y$10$cW6K0c4vUbXmEcTJ1QYWvOthkzA0L3Rs9OBofDzVk1ClPWP4OeVji'),
(2, 'User User', 'user@gmail.com', '$2y$10$z948jfNiZDeTblJHGE7w3eELO2hL1SkVbXf1L4odeX4tv9k8fCDnO');

-- --------------------------------------------------------

--
-- Table structure for table `user_garage`
--

CREATE TABLE `user_garage` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `vin` varchar(17) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `user_garage`
--

INSERT INTO `user_garage` (`id`, `user_id`, `vin`) VALUES
(38, 1, 'WAUZZZ8V7KA123456'),
(25, 1, 'WBA8D9G59GNU12345'),
(26, 1, 'WVWZZZ1KZAW123456'),
(41, 2, 'VF1RFB0066AB12345'),
(37, 2, 'WAUZZZ8V7KA123456'),
(43, 2, 'WBA8D9G59GNU12345'),
(40, 2, 'WVWZZZ1KZAW123456');

-- --------------------------------------------------------

--
-- Table structure for table `vehicles`
--

CREATE TABLE `vehicles` (
  `id` int(11) NOT NULL,
  `vin` varchar(17) DEFAULT NULL,
  `make` varchar(100) DEFAULT NULL,
  `model` varchar(100) DEFAULT NULL,
  `year` varchar(10) DEFAULT NULL,
  `engine` varchar(100) DEFAULT NULL,
  `fuel` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `vehicles`
--

INSERT INTO `vehicles` (`id`, `vin`, `make`, `model`, `year`, `engine`, `fuel`) VALUES
(1, 'WBA8D9G59GNU12345', 'BMW', '320d', '2018', '2.0 Diesel', 'Diesel'),
(2, 'VF1RFB0066AB12345', 'Renault', 'Megane', '2017', '1.5 dCi', 'Diesel'),
(3, 'WAUZZZ8V7KA123456', 'Audi', 'A3', '2019', '2.0 TDI', 'Diesel'),
(4, 'WVWZZZ1KZAW123456', 'Volkswagen', 'Golf 6', '2010', '1.6 TDI', 'Diesel');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `comments`
--
ALTER TABLE `comments`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `parts`
--
ALTER TABLE `parts`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `search_history`
--
ALTER TABLE `search_history`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `user_garage`
--
ALTER TABLE `user_garage`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `unique_user_vin` (`user_id`,`vin`);

--
-- Indexes for table `vehicles`
--
ALTER TABLE `vehicles`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `vin` (`vin`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `comments`
--
ALTER TABLE `comments`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `parts`
--
ALTER TABLE `parts`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- AUTO_INCREMENT for table `search_history`
--
ALTER TABLE `search_history`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `user_garage`
--
ALTER TABLE `user_garage`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=45;

--
-- AUTO_INCREMENT for table `vehicles`
--
ALTER TABLE `vehicles`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
