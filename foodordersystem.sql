-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Oct 06, 2026 at 07:48 PM
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
-- Database: `foodordersystem`
--

-- --------------------------------------------------------

--
-- Table structure for table `admins`
--

CREATE TABLE `admins` (
  `id` int(10) NOT NULL,
  `user_id` int(10) NOT NULL,
  `admin_level` varchar(20) NOT NULL,
  `created_level` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `admins`
--

INSERT INTO `admins` (`id`, `user_id`, `admin_level`, `created_level`) VALUES
(1, 6, 'SUPER', '2026-07-31 16:56:29');

-- --------------------------------------------------------

--
-- Table structure for table `cart`
--

CREATE TABLE `cart` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `menu_id` int(11) NOT NULL,
  `quantity` int(11) NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `added_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `categories`
--

CREATE TABLE `categories` (
  `id` int(11) NOT NULL,
  `name` varchar(50) NOT NULL,
  `description` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `categories`
--

INSERT INTO `categories` (`id`, `name`, `description`) VALUES
(1, 'Pizza', 'All types of pizza dishes'),
(2, 'Burger', 'Burgers and sandwiches'),
(3, 'Biryani', 'Authentic biryani dishes'),
(4, 'Chinese', 'Chinese cuisine dishes'),
(5, 'South Indian', 'South Indian delicacies'),
(6, 'Dessert', 'Sweet dishes and desserts'),
(7, 'Beverages', 'Drinks and beverages');

-- --------------------------------------------------------

--
-- Table structure for table `delivery`
--

CREATE TABLE `delivery` (
  `id` int(11) NOT NULL,
  `order_id` int(11) NOT NULL,
  `delivery_partner` varchar(100) NOT NULL,
  `partner_phone` varchar(15) DEFAULT NULL,
  `status` varchar(30) DEFAULT 'ASSIGNED',
  `assigned_time` timestamp NULL DEFAULT NULL,
  `picked_up_time` timestamp NULL DEFAULT NULL,
  `delivered_time` timestamp NULL DEFAULT NULL,
  `estimated_time` int(11) DEFAULT NULL,
  `actual_time` int(11) DEFAULT NULL,
  `delivery_address` text DEFAULT NULL,
  `delivery_notes` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `menu`
--

CREATE TABLE `menu` (
  `id` int(11) NOT NULL,
  `restaurant_id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `description` text DEFAULT NULL,
  `price` decimal(10,2) NOT NULL,
  `category` varchar(20) NOT NULL,
  `cuisine` varchar(50) DEFAULT NULL,
  `availability` varchar(20) DEFAULT 'AVAILABLE',
  `stock_quantity` int(11) DEFAULT 0,
  `is_featured` tinyint(1) DEFAULT 0,
  `preparation_time` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `menu`
--

INSERT INTO `menu` (`id`, `restaurant_id`, `name`, `description`, `price`, `category`, `cuisine`, `availability`, `stock_quantity`, `is_featured`, `preparation_time`) VALUES
(1, 1, 'Butter Chicken', 'Creamy tomato gravy with tender chicken pieces', 350.00, 'NON_VEG', 'Indian', 'AVAILABLE', 46, 1, 25),
(2, 1, 'Paneer Tikka', 'Grilled cottage cheese with Indian spices', 280.00, 'VEG', 'Indian', 'AVAILABLE', 40, 1, 20),
(3, 1, 'Garlic Naan', 'Soft bread with garlic butter', 60.00, 'VEG', 'Indian', 'AVAILABLE', 100, 0, 10),
(4, 1, 'Chicken Biryani', 'Fragrant rice with chicken and spices', 320.00, 'NON_VEG', 'Indian', 'AVAILABLE', 30, 1, 30),
(5, 1, 'Dal Makhani', 'Slow cooked black lentils with cream', 220.00, 'VEG', 'Indian', 'AVAILABLE', 45, 0, 25),
(6, 1, 'Gulab Jamun', 'Sweet milk dumplings in sugar syrup', 80.00, 'VEG', 'Indian', 'AVAILABLE', 59, 0, 15),
(7, 1, 'Rogan Josh', 'Kashmiri lamb curry with aromatic spices', 380.00, 'NON_VEG', 'Indian', 'AVAILABLE', 25, 1, 35),
(8, 1, 'Jeera Rice', 'Basmati rice with cumin seeds', 150.00, 'VEG', 'Indian', 'AVAILABLE', 80, 0, 10),
(9, 1, 'Butter Chicken', 'Creamy tomato gravy with tender chicken pieces', 350.00, 'NON_VEG', 'Indian', 'AVAILABLE', 50, 1, 25),
(10, 1, 'Paneer Tikka', 'Grilled cottage cheese with Indian spices', 280.00, 'VEG', 'Indian', 'AVAILABLE', 40, 1, 20),
(11, 1, 'Garlic Naan', 'Soft bread with garlic butter', 60.00, 'VEG', 'Indian', 'AVAILABLE', 100, 0, 10),
(12, 1, 'Chicken Biryani', 'Fragrant rice with chicken and spices', 320.00, 'NON_VEG', 'Indian', 'AVAILABLE', 30, 1, 30),
(13, 1, 'Dal Makhani', 'Slow cooked black lentils with cream', 220.00, 'VEG', 'Indian', 'AVAILABLE', 45, 0, 25),
(14, 1, 'Gulab Jamun', 'Sweet milk dumplings in sugar syrup', 80.00, 'VEG', 'Indian', 'AVAILABLE', 60, 0, 15),
(15, 1, 'Rogan Josh', 'Kashmiri lamb curry with aromatic spices', 380.00, 'NON_VEG', 'Indian', 'AVAILABLE', 25, 1, 35),
(16, 1, 'Jeera Rice', 'Basmati rice with cumin seeds', 150.00, 'VEG', 'Indian', 'AVAILABLE', 80, 0, 10),
(17, 2, 'Margherita Pizza', 'Classic pizza with tomato sauce and mozzarella', 250.00, 'VEG', 'Italian', 'AVAILABLE', 40, 1, 20),
(18, 2, 'Pepperoni Pizza', 'Pizza with pepperoni and cheese', 320.00, 'NON_VEG', 'Italian', 'AVAILABLE', 35, 1, 25),
(19, 2, 'Garlic Breadsticks', 'Breadsticks with garlic and herbs', 120.00, 'VEG', 'Italian', 'AVAILABLE', 12, 0, 15),
(20, 2, 'Pasta Alfredo', 'Creamy pasta with parmesan cheese', 200.00, 'VEG', 'Italian', 'AVAILABLE', 25, 0, 20),
(21, 2, 'Tiramisu', 'Coffee flavored Italian dessert', 150.00, 'VEG', 'Italian', 'AVAILABLE', 26, 0, 10),
(22, 2, 'BBQ Chicken Pizza', 'Pizza with BBQ chicken and onions', 350.00, 'NON_VEG', 'Italian', 'AVAILABLE', 30, 1, 25),
(23, 2, 'Veggie Supreme Pizza', 'Loaded with bell peppers, mushrooms, olives', 280.00, 'VEG', 'Italian', 'AVAILABLE', 35, 0, 22),
(24, 3, 'Palak Paneer', 'Paneer with spinach gravy', 250.00, 'VEG', 'Indian', 'AVAILABLE', 34, 1, 20),
(25, 3, 'Chole Bhature', 'Spicy chickpeas with fried bread', 180.00, 'VEG', 'Indian', 'AVAILABLE', 40, 1, 15),
(26, 3, 'Masala Dosa', 'Crispy dosa with potato filling', 120.00, 'VEG', 'Indian', 'AVAILABLE', 49, 0, 10),
(27, 3, 'Veg Thali', 'Complete vegetarian meal with 5 items', 300.00, 'VEG', 'Indian', 'AVAILABLE', 20, 1, 30),
(28, 3, 'Punjabi Kadhi Pakora', 'Gram flour curry with fried dumplings', 200.00, 'VEG', 'Indian', 'AVAILABLE', 30, 0, 25),
(29, 3, 'Aloo Paratha', 'Stuffed potato bread with butter', 80.00, 'VEG', 'Indian', 'AVAILABLE', 60, 0, 10),
(30, 4, 'California Roll', 'Sushi roll with crab and avocado', 400.00, 'NON_VEG', 'Japanese', 'AVAILABLE', 25, 1, 20),
(31, 4, 'Salmon Sashimi', 'Fresh salmon slices', 450.00, 'NON_VEG', 'Japanese', 'AVAILABLE', 20, 1, 15),
(32, 4, 'Vegetable Tempura', 'Fried vegetables in tempura batter', 280.00, 'VEG', 'Japanese', 'AVAILABLE', 30, 0, 15),
(33, 4, 'Miso Soup', 'Traditional Japanese soup', 120.00, 'VEG', 'Japanese', 'AVAILABLE', 40, 0, 5),
(34, 4, 'Sushi Combo', 'Assorted sushi platter', 600.00, 'NON_VEG', 'Japanese', 'AVAILABLE', 15, 1, 25),
(35, 4, 'Edamame', 'Steamed soybean pods', 150.00, 'VEG', 'Japanese', 'AVAILABLE', 45, 0, 5),
(36, 5, 'Hyderabadi Chicken Biryani', 'Spicy chicken biryani with saffron', 350.00, 'NON_VEG', 'Hyderabadi', 'AVAILABLE', 30, 1, 30),
(37, 5, 'Mutton Biryani', 'Tender mutton biryani with aromatic spices', 400.00, 'NON_VEG', 'Hyderabadi', 'AVAILABLE', 20, 1, 35),
(38, 5, 'Veg Biryani', 'Vegetable biryani with paneer', 250.00, 'VEG', 'Hyderabadi', 'AVAILABLE', 35, 1, 25),
(39, 5, 'Kheer', 'Rice pudding with dry fruits', 100.00, 'VEG', 'Hyderabadi', 'AVAILABLE', 40, 0, 10),
(40, 5, 'Chicken 65', 'Spicy fried chicken appetizer', 220.00, 'NON_VEG', 'Hyderabadi', 'AVAILABLE', 30, 0, 20),
(41, 5, 'Raita', 'Yogurt with cucumber and spices', 60.00, 'VEG', 'Hyderabadi', 'AVAILABLE', 50, 0, 5),
(42, 6, 'Green Salad', 'Fresh garden salad with dressing', 100.00, 'VEG', 'Continental', 'AVAILABLE', 50, 0, 5),
(43, 6, 'Grilled Sandwich', 'Grilled veg sandwich with cheese', 150.00, 'VEG', 'Continental', 'AVAILABLE', 40, 0, 10),
(44, 6, 'Smoothie Bowl', 'Fruit smoothie with granola', 180.00, 'VEG', 'Continental', 'AVAILABLE', 25, 1, 5),
(45, 6, 'Veg Burger', 'Grilled veg patty with lettuce and sauce', 160.00, 'VEG', 'Continental', 'AVAILABLE', 35, 0, 12),
(46, 6, 'Fresh Juice', 'Seasonal fresh fruit juice', 80.00, 'VEG', 'Continental', 'AVAILABLE', 60, 0, 3),
(47, 7, 'Hakka Noodles', 'Stir fried noodles with vegetables', 200.00, 'VEG', 'Chinese', 'AVAILABLE', 45, 1, 15),
(48, 7, 'Chilli Chicken', 'Spicy chicken with bell peppers', 300.00, 'NON_VEG', 'Chinese', 'AVAILABLE', 35, 1, 20),
(49, 7, 'Spring Rolls', 'Crispy vegetable rolls', 150.00, 'VEG', 'Chinese', 'AVAILABLE', 50, 0, 10),
(50, 7, 'Fried Rice', 'Classic fried rice with egg', 180.00, 'NON_VEG', 'Chinese', 'AVAILABLE', 40, 0, 15),
(51, 7, 'Manchurian Gravy', 'Vegetable balls in tangy sauce', 220.00, 'VEG', 'Chinese', 'AVAILABLE', 30, 0, 20),
(52, 7, 'Sweet Corn Soup', 'Creamy sweet corn soup', 120.00, 'VEG', 'Chinese', 'AVAILABLE', 40, 0, 10),
(53, 8, 'Mughlai Chicken', 'Rich chicken curry with nuts and cream', 380.00, 'NON_VEG', 'Mughlai', 'AVAILABLE', 25, 1, 30),
(54, 8, 'Kebab Platter', 'Assorted kebabs with mint chutney', 420.00, 'NON_VEG', 'Mughlai', 'AVAILABLE', 20, 1, 25),
(55, 8, 'Paneer Butter Masala', 'Paneer in rich buttery gravy', 280.00, 'VEG', 'Mughlai', 'AVAILABLE', 34, 1, 20),
(56, 8, 'Tandoori Roti', 'Wheat bread baked in tandoor', 40.00, 'VEG', 'Mughlai', 'AVAILABLE', 98, 0, 5),
(57, 9, 'Idli Sambhar', 'Steamed rice cakes with lentil soup', 80.00, 'VEG', 'South Indian', 'AVAILABLE', 50, 1, 8),
(58, 9, 'Dosa', 'Crispy dosa with chutney and sambhar', 100.00, 'VEG', 'South Indian', 'AVAILABLE', 40, 1, 10),
(59, 9, 'Vada', 'Crispy lentil donuts', 60.00, 'VEG', 'South Indian', 'AVAILABLE', 45, 0, 5),
(60, 9, 'Uttapam', 'Thick pancake with vegetables', 130.00, 'VEG', 'South Indian', 'AVAILABLE', 30, 0, 12),
(61, 10, 'Classic Burger', 'Beef patty with cheese and lettuce', 200.00, 'NON_VEG', 'American', 'AVAILABLE', 40, 1, 15),
(62, 10, 'Veg Burger', 'Grilled veg patty with cheese', 170.00, 'VEG', 'American', 'AVAILABLE', 34, 1, 12),
(63, 10, 'Chicken Burger', 'Crispy chicken with mayo', 220.00, 'NON_VEG', 'American', 'AVAILABLE', 30, 0, 15),
(64, 10, 'French Fries', 'Crispy golden fries', 100.00, 'VEG', 'American', 'AVAILABLE', 60, 0, 8),
(65, 10, 'Milkshake', 'Thick milkshake with ice cream', 130.00, 'VEG', 'American', 'AVAILABLE', 40, 0, 5);

--
-- Triggers `menu`
--
DELIMITER $$
CREATE TRIGGER `menu_stock_status` BEFORE UPDATE ON `menu` FOR EACH ROW BEGIN
    IF NEW.stock_quantity <= 0 THEN
        SET NEW.availability = 'OUT_OF_STOCK';
    ELSE
        SET NEW.availability = 'AVAILABLE';
    END IF;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `orders`
--

CREATE TABLE `orders` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `restaurant_id` int(11) NOT NULL,
  `order_number` varchar(50) NOT NULL,
  `total_amount` decimal(10,2) NOT NULL,
  `discount` decimal(10,2) DEFAULT 0.00,
  `coupon_code` varchar(50) DEFAULT NULL,
  `final_amount` decimal(10,2) NOT NULL,
  `status` varchar(30) DEFAULT 'PENDING',
  `payment_status` varchar(20) DEFAULT 'PENDING',
  `delivery_address` text DEFAULT NULL,
  `delivery_instructions` text DEFAULT NULL,
  `estimated_delivery_time` int(11) DEFAULT NULL,
  `actual_delivery_time` timestamp NULL DEFAULT NULL,
  `order_date` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `orders`
--

INSERT INTO `orders` (`id`, `user_id`, `restaurant_id`, `order_number`, `total_amount`, `discount`, `coupon_code`, `final_amount`, `status`, `payment_status`, `delivery_address`, `delivery_instructions`, `estimated_delivery_time`, `actual_delivery_time`, `order_date`) VALUES
(6, 9, 1, 'ORD1785581458024', 280.00, 0.00, '', 310.00, 'CONFIRMED', 'COD', 'ahemadbad', '', 30, NULL, '2026-08-01 10:50:58'),
(7, 9, 1, 'TEST_PENDING_1785870951560', 100.00, 0.00, '', 120.00, 'PENDING', 'PENDING', 'Test Address', '', 30, NULL, '2026-08-04 19:15:51'),
(8, 9, 1, 'TEST_CANCELLED_1785870951909', 150.00, 0.00, '', 170.00, 'CANCELLED', 'PENDING', 'Test Address', '', 30, NULL, '2026-08-04 19:15:51'),
(9, 3, 10, 'ORD1785917055850', 170.00, 0.00, '', 200.00, 'CANCELLED', 'COD', '101, Sunshine Apartments, Ahmedabad', '', 30, NULL, '2026-08-05 02:34:15'),
(12, 3, 3, 'ORD1785918501844', 120.00, 0.00, '', 145.00, 'CANCELLED', 'PENDING', '101, Sunshine Apartments, Ahmedabad', '', 30, NULL, '2026-08-05 02:58:21'),
(13, 3, 1, 'ORD1785919651254', 80.00, 0.00, '', 110.00, 'COMPLETED', 'PENDING', '101, Sunshine Apartments, Ahmedabad', '', 30, NULL, '2026-08-05 03:17:31'),
(14, 10, 3, 'ORD1785925053443', 250.00, 0.00, '', 275.00, 'COMPLETED', 'PENDING', 'stanza living, Near l.j univercity, ahmedabad - 382210', '', 30, NULL, '2026-08-05 04:47:33'),
(17, 3, 8, 'ORD1785946634856', 360.00, 0.00, '', 395.00, 'PENDING', 'COMPLETED', '101, Sunshine Apartments, Ahmedabad', '', 30, NULL, '2026-08-05 10:47:14');

-- --------------------------------------------------------

--
-- Table structure for table `order_items`
--

CREATE TABLE `order_items` (
  `id` int(11) NOT NULL,
  `order_id` int(11) NOT NULL,
  `menu_id` int(11) NOT NULL,
  `quantity` int(11) NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `subtotal` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `order_items`
--

INSERT INTO `order_items` (`id`, `order_id`, `menu_id`, `quantity`, `price`, `subtotal`) VALUES
(1, 6, 2, 1, 280.00, 280.00),
(2, 7, 1, 1, 100.00, 100.00),
(3, 8, 1, 1, 100.00, 100.00),
(4, 9, 62, 1, 170.00, 170.00),
(7, 12, 26, 1, 120.00, 120.00),
(8, 13, 6, 1, 80.00, 80.00),
(9, 14, 24, 1, 250.00, 250.00),
(12, 17, 55, 1, 280.00, 280.00),
(13, 17, 56, 2, 40.00, 80.00);

-- --------------------------------------------------------

--
-- Table structure for table `payments`
--

CREATE TABLE `payments` (
  `id` int(11) NOT NULL,
  `order_id` int(11) NOT NULL,
  `amount` decimal(10,2) NOT NULL,
  `method` varchar(30) NOT NULL,
  `status` varchar(20) NOT NULL,
  `transaction_id` varchar(100) DEFAULT NULL,
  `payment_date` timestamp NOT NULL DEFAULT current_timestamp(),
  `card_last_four` varchar(4) DEFAULT NULL,
  `upi_id` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `payments`
--

INSERT INTO `payments` (`id`, `order_id`, `amount`, `method`, `status`, `transaction_id`, `payment_date`, `card_last_four`, `upi_id`) VALUES
(4, 12, 145.00, 'CASH', 'PENDING', 'TXN_COD_1785918501880', '2026-08-05 02:58:21', NULL, ''),
(5, 13, 110.00, 'CASH', 'PENDING', 'TXN_COD_1785919651323', '2026-08-05 03:17:31', NULL, ''),
(6, 14, 275.00, 'CASH', 'PENDING', 'TXN_COD_1785925053498', '2026-08-05 04:47:33', NULL, ''),
(9, 17, 395.00, 'UPI', 'COMPLETED', 'TXN_UPI_1785946634887', '2026-08-05 10:47:14', NULL, 'prince@upi');

-- --------------------------------------------------------

--
-- Table structure for table `restaurants`
--

CREATE TABLE `restaurants` (
  `id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `address` varchar(100) DEFAULT NULL,
  `phone` varchar(15) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `category` varchar(100) DEFAULT NULL,
  `cuisine` varchar(100) DEFAULT NULL,
  `status` varchar(100) DEFAULT NULL,
  `rating` decimal(3,2) DEFAULT NULL,
  `total_ratings` int(11) DEFAULT NULL,
  `opening_time` time DEFAULT NULL,
  `closing_time` time DEFAULT NULL,
  `delivery_charge` decimal(10,2) DEFAULT NULL,
  `min_order_amount` decimal(10,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `restaurants`
--

INSERT INTO `restaurants` (`id`, `name`, `address`, `phone`, `email`, `category`, `cuisine`, `status`, `rating`, `total_ratings`, `opening_time`, `closing_time`, `delivery_charge`, `min_order_amount`) VALUES
(1, 'Taste of India', '123, MG Road, Ahmedabad', '079-1234567', 'contact@tasteofindia.com', 'BOTH', 'Indian', 'OPEN', 4.50, 120, '09:00:00', '23:00:00', 30.00, 100.00),
(2, 'Pizza Paradise', '45, Gandhi Nagar, Surat', '0261-7890123', 'info@pizzaparadise.com', 'NON_VEG', 'Italian', 'OPEN', 4.20, 85, '10:00:00', '22:00:00', 40.00, 150.00),
(3, 'Spice Garden', '78, Lake Road, Vadodara', '0265-4567890', 'spicegarden@email.com', 'VEG', 'Indian', 'OPEN', 4.00, 60, '08:00:00', '21:00:00', 25.00, 80.00),
(4, 'Sushi House', '12, Beach Road, Mumbai', '022-3456789', 'sushi@house.com', 'NON_VEG', 'Japanese', 'OPEN', 4.70, 150, '11:00:00', '23:30:00', 50.00, 200.00),
(5, 'Biryani King', '67, Old City, Hyderabad', '040-9876543', 'biryani@king.com', 'NON_VEG', 'Hyderabadi', 'OPEN', 4.80, 200, '08:30:00', '22:30:00', 35.00, 120.00),
(6, 'Green Delight', '34, Eco Park, Pune', '020-4567890', 'green@delight.com', 'VEG', 'Continental', 'OPEN', 4.30, 90, '07:00:00', '20:00:00', 20.00, 60.00),
(7, 'Chinese Wok', '56, Chinatown, Kolkata', '033-7890123', 'wok@chinese.com', 'BOTH', 'Chinese', 'OPEN', 4.10, 75, '10:30:00', '23:00:00', 45.00, 130.00),
(8, 'Royal Darbar', '89, Palace Road, Jaipur', '0141-2345678', 'royal@darbar.com', 'BOTH', 'Mughlai', 'OPEN', 4.60, 110, '09:30:00', '23:30:00', 35.00, 150.00),
(9, 'South Indian Cafe', '34, Temple Street, Chennai', '044-9876543', 'south@cafe.com', 'VEG', 'South Indian', 'OPEN', 4.40, 95, '06:30:00', '21:00:00', 15.00, 50.00),
(10, 'Burger House', '78, Food Street, Delhi', '011-4567890', 'burger@house.com', 'NON_VEG', 'American', 'OPEN', 4.00, 70, '11:00:00', '23:00:00', 30.00, 100.00);

-- --------------------------------------------------------

--
-- Table structure for table `reviews`
--

CREATE TABLE `reviews` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `restaurant_id` int(11) DEFAULT NULL,
  `menu_id` int(11) DEFAULT NULL,
  `rating` int(11) NOT NULL,
  `comment` text DEFAULT NULL,
  `type` varchar(20) NOT NULL,
  `review_date` timestamp NOT NULL DEFAULT current_timestamp(),
  `is_verified` tinyint(1) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `reviews`
--

INSERT INTO `reviews` (`id`, `user_id`, `restaurant_id`, `menu_id`, `rating`, `comment`, `type`, `review_date`, `is_verified`) VALUES
(1, 1, 1, NULL, 5, 'Amazing butter chicken! Best in town', 'RESTAURANT', '2026-07-31 17:15:59', 1),
(2, 2, 1, NULL, 4, 'Paneer tikka was delicious', 'RESTAURANT', '2026-07-31 17:15:59', 1),
(3, 3, 2, NULL, 5, 'Best pizza ever! Must try', 'RESTAURANT', '2026-07-31 17:15:59', 1),
(4, 4, 3, NULL, 4, 'Great vegetarian food', 'RESTAURANT', '2026-07-31 17:15:59', 1),
(5, 5, 5, NULL, 5, 'Authentic Hyderabadi biryani!', 'RESTAURANT', '2026-07-31 17:15:59', 1),
(6, 1, 2, NULL, 4, 'Pepperoni pizza was good', 'RESTAURANT', '2026-07-31 17:15:59', 1),
(7, 3, 4, NULL, 5, 'Best sushi in town!', 'RESTAURANT', '2026-07-31 17:15:59', 1),
(8, 4, 6, NULL, 4, 'Healthy and delicious', 'RESTAURANT', '2026-07-31 17:15:59', 1),
(9, 5, 7, NULL, 4, 'Good Chinese food', 'RESTAURANT', '2026-07-31 17:15:59', 1),
(10, 2, 8, NULL, 5, 'Royal Darbar is amazing!', 'RESTAURANT', '2026-07-31 17:15:59', 1),
(11, 4, 9, NULL, 5, 'Authentic South Indian food!', 'RESTAURANT', '2026-07-31 17:15:59', 1),
(12, 5, 10, NULL, 4, 'Good burgers, nice ambience', 'RESTAURANT', '2026-07-31 17:15:59', 1);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(10) NOT NULL,
  `name` varchar(100) NOT NULL,
  `email` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `phone` varchar(15) NOT NULL,
  `address` text NOT NULL,
  `security_question` varchar(200) NOT NULL,
  `security_answer` varchar(200) NOT NULL,
  `role` varchar(20) NOT NULL,
  `is_blocked` tinyint(1) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `name`, `email`, `password`, `phone`, `address`, `security_question`, `security_answer`, `role`, `is_blocked`, `created_at`, `updated_at`) VALUES
(2, 'aehsan', 'aehsanabbas07@gmail.com', '7878', '9714025784', 'lj', '121', '212', 'CUSTOMER', 0, '2026-07-31 16:41:55', '2026-07-31 16:41:55'),
(3, 'Raj Patel', 'raj@gmail.com', '12345', '9876543210', '101, Sunshine Apartments, Ahmedabad', 'What is your pet name?', 'Tommy', 'CUSTOMER', 0, '2026-07-31 16:48:23', '2026-07-31 16:48:23'),
(4, 'Priya Sharma', 'priya@gmail.com', '12345', '9876543211', '202, Green Valley, Surat', 'What is your favorite color?', 'Blue', 'CUSTOMER', 0, '2026-07-31 16:48:23', '2026-07-31 16:48:23'),
(5, 'Amit Kumar', 'amit@gmail.com', '12345', '9876543212', '303, Lake View, Vadodara', 'What is your birthplace?', 'Delhi', 'CUSTOMER', 0, '2026-07-31 16:48:23', '2026-07-31 16:48:23'),
(6, 'Sneha Reddy', 'sneha@gmail.com', '12345', '9876543213', '404, Park Street, Mumbai', 'What is your school name?', 'DPS', 'CUSTOMER', 0, '2026-07-31 16:48:23', '2026-07-31 16:48:23'),
(7, 'Vikram Singh', 'vikram@gmail.com', '12345', '9876543214', '505, Royal Palace, Jaipur', 'What is your favorite food?', 'Pizza', 'CUSTOMER', 0, '2026-07-31 16:48:23', '2026-07-31 16:48:23'),
(8, 'Admin User', 'admin@gmail.com', 'admin123', '9876543215', 'Admin Office, Ahmedabad', 'What is admin?', 'Admin', 'ADMIN', 0, '2026-07-31 16:48:23', '2026-07-31 16:48:23'),
(9, 'prince patel', 'princepatel1209@gmail.com', '123', '9696969898', 'ahemadbad', 'What is your favorite color?', 'black', 'CUSTOMER', 0, '2026-08-01 09:52:45', '2026-08-01 09:52:45'),
(10, 'Prince Patel', 'Prince@gmail.com', 'Prince@1209', '8854411010', 'stanza living, Near l.j univercity, ahmedabad - 382210', 'What is the name of your first pet?', '963.', 'CUSTOMER', 0, '2026-08-04 17:27:30', '2026-08-04 17:27:30'),
(11, 'aehsan', 'aehsan@gmail.com', 'Aehsan@123', '8975345670', '124345h, 123456, junagadh - 000000', 'What is your mother\'s maiden name?', 'geeta', 'CUSTOMER', 0, '2026-08-04 18:12:19', '2026-08-04 18:12:19'),
(12, 'prince', 'Prince1209@gmail.com', 'Prince@1209', '8545411012', '85, 95620, ahemedabad - 965652', 'What is the name of your first pet?', 'sdsad', 'CUSTOMER', 0, '2026-08-04 19:25:16', '2026-08-04 19:25:16'),
(13, 'Prince', '85Prince@gmail.com', 'Prince@1209', '9654414101', '97, stanza living elgin house, Ahmedabad - 632522', 'What is your favorite food?', 'Panipuri', 'CUSTOMER', 0, '2026-08-05 07:36:24', '2026-08-05 07:36:24'),
(14, 'meet', 'meet@gmail.com', 'Meet@123', '8320402451', 'a, 12345, ahm - 345621', 'What is your favorite food?', 'paqnipuri', 'CUSTOMER', 0, '2026-08-05 03:12:01', '2026-08-05 03:12:01'),
(15, 'po', 'Po12@gmail.com', 'Popoir@20', '9656521012', 'kl+, tyutfrtyuh, tyu - 888888', 'What is the name of your first school?', 'trry', 'CUSTOMER', 0, '2026-08-05 05:32:36', '2026-08-05 05:32:36');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `admins`
--
ALTER TABLE `admins`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `cart`
--
ALTER TABLE `cart`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `menu_id` (`menu_id`);

--
-- Indexes for table `categories`
--
ALTER TABLE `categories`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `delivery`
--
ALTER TABLE `delivery`
  ADD PRIMARY KEY (`id`),
  ADD KEY `order_id` (`order_id`);

--
-- Indexes for table `menu`
--
ALTER TABLE `menu`
  ADD PRIMARY KEY (`id`),
  ADD KEY `restaurant_id` (`restaurant_id`);

--
-- Indexes for table `orders`
--
ALTER TABLE `orders`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `order_number` (`order_number`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `restaurant_id` (`restaurant_id`);

--
-- Indexes for table `order_items`
--
ALTER TABLE `order_items`
  ADD PRIMARY KEY (`id`),
  ADD KEY `order_id` (`order_id`),
  ADD KEY `menu_id` (`menu_id`);

--
-- Indexes for table `payments`
--
ALTER TABLE `payments`
  ADD PRIMARY KEY (`id`),
  ADD KEY `order_id` (`order_id`);

--
-- Indexes for table `restaurants`
--
ALTER TABLE `restaurants`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `reviews`
--
ALTER TABLE `reviews`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `admins`
--
ALTER TABLE `admins`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `cart`
--
ALTER TABLE `cart`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `categories`
--
ALTER TABLE `categories`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `delivery`
--
ALTER TABLE `delivery`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `menu`
--
ALTER TABLE `menu`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=66;

--
-- AUTO_INCREMENT for table `orders`
--
ALTER TABLE `orders`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- AUTO_INCREMENT for table `order_items`
--
ALTER TABLE `order_items`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT for table `payments`
--
ALTER TABLE `payments`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `restaurants`
--
ALTER TABLE `restaurants`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `reviews`
--
ALTER TABLE `reviews`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `cart`
--
ALTER TABLE `cart`
  ADD CONSTRAINT `cart_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `cart_ibfk_2` FOREIGN KEY (`menu_id`) REFERENCES `menu` (`id`);

--
-- Constraints for table `delivery`
--
ALTER TABLE `delivery`
  ADD CONSTRAINT `delivery_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`);

--
-- Constraints for table `menu`
--
ALTER TABLE `menu`
  ADD CONSTRAINT `menu_ibfk_1` FOREIGN KEY (`restaurant_id`) REFERENCES `restaurants` (`id`);

--
-- Constraints for table `orders`
--
ALTER TABLE `orders`
  ADD CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `orders_ibfk_2` FOREIGN KEY (`restaurant_id`) REFERENCES `restaurants` (`id`);

--
-- Constraints for table `order_items`
--
ALTER TABLE `order_items`
  ADD CONSTRAINT `order_items_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
  ADD CONSTRAINT `order_items_ibfk_2` FOREIGN KEY (`menu_id`) REFERENCES `menu` (`id`);

--
-- Constraints for table `payments`
--
ALTER TABLE `payments`
  ADD CONSTRAINT `payments_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
