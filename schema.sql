-- ========================================================
-- Food Ordering System Database Schema Script
-- Database Name: foodordersystem
-- ========================================================

CREATE DATABASE IF NOT EXISTS `foodordersystem`;
USE `foodordersystem`;

-- 1. Users Table
CREATE TABLE IF NOT EXISTS `users` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(100) NOT NULL,
    `email` VARCHAR(100) UNIQUE NOT NULL,
    `password` VARCHAR(100) NOT NULL,
    `phone` VARCHAR(20) NOT NULL,
    `address` TEXT NOT NULL,
    `security_question` VARCHAR(255) NOT NULL,
    `security_answer` VARCHAR(255) NOT NULL,
    `role` VARCHAR(20) DEFAULT 'CUSTOMER',
    `is_blocked` TINYINT(1) DEFAULT 0,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 2. Restaurants Table
CREATE TABLE IF NOT EXISTS `restaurants` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(100) NOT NULL,
    `address` TEXT NOT NULL,
    `phone` VARCHAR(20) NOT NULL,
    `email` VARCHAR(100),
    `category` VARCHAR(50) DEFAULT 'BOTH',
    `cuisine` VARCHAR(100),
    `status` VARCHAR(20) DEFAULT 'OPEN',
    `rating` DOUBLE DEFAULT 0.0,
    `total_ratings` INT DEFAULT 0,
    `opening_time` TIME,
    `closing_time` TIME,
    `delivery_charge` DOUBLE DEFAULT 0.0,
    `min_order_amount` DOUBLE DEFAULT 0.0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 3. Menu Items Table
CREATE TABLE IF NOT EXISTS `menu` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `restaurant_id` INT NOT NULL,
    `name` VARCHAR(100) NOT NULL,
    `description` TEXT,
    `price` DOUBLE NOT NULL,
    `category` VARCHAR(50) DEFAULT 'VEG',
    `cuisine` VARCHAR(50),
    `availability` VARCHAR(20) DEFAULT 'AVAILABLE',
    `stock_quantity` INT DEFAULT 0,
    `is_featured` TINYINT(1) DEFAULT 0,
    `preparation_time` INT DEFAULT 20,
    FOREIGN KEY (`restaurant_id`) REFERENCES `restaurants`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 4. Orders Table
CREATE TABLE IF NOT EXISTS `orders` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `user_id` INT NOT NULL,
    `restaurant_id` INT NOT NULL,
    `order_number` VARCHAR(50) UNIQUE NOT NULL,
    `total_amount` DOUBLE NOT NULL,
    `discount` DOUBLE DEFAULT 0.0,
    `coupon_code` VARCHAR(50),
    `final_amount` DOUBLE NOT NULL,
    `status` VARCHAR(30) DEFAULT 'PENDING',
    `payment_status` VARCHAR(30) DEFAULT 'PENDING',
    `delivery_address` TEXT NOT NULL,
    `delivery_instructions` TEXT,
    `estimated_delivery_time` INT DEFAULT 30,
    `actual_delivery_time` TIMESTAMP NULL,
    `order_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`restaurant_id`) REFERENCES `restaurants`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 5. Order Items Table
CREATE TABLE IF NOT EXISTS `order_items` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `order_id` INT NOT NULL,
    `menu_id` INT NOT NULL,
    `quantity` INT NOT NULL,
    `price` DOUBLE NOT NULL,
    `subtotal` DOUBLE NOT NULL,
    `special_instructions` VARCHAR(255),
    FOREIGN KEY (`order_id`) REFERENCES `orders`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`menu_id`) REFERENCES `menu`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 6. Payments Table
CREATE TABLE IF NOT EXISTS `payments` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `order_id` INT NOT NULL,
    `amount` DOUBLE NOT NULL,
    `method` VARCHAR(30) NOT NULL,
    `status` VARCHAR(30) DEFAULT 'PENDING',
    `transaction_id` VARCHAR(100),
    `payment_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `card_last_four` VARCHAR(4),
    `upi_id` VARCHAR(100),
    FOREIGN KEY (`order_id`) REFERENCES `orders`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 7. Reviews Table
CREATE TABLE IF NOT EXISTS `reviews` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `user_id` INT NOT NULL,
    `restaurant_id` INT NOT NULL,
    `menu_id` INT NULL,
    `rating` INT NOT NULL,
    `comment` TEXT,
    `type` VARCHAR(30) DEFAULT 'RESTAURANT',
    `review_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `is_verified` TINYINT(1) DEFAULT 1,
    FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`restaurant_id`) REFERENCES `restaurants`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ========================================================
-- Sample Initial Data (Default Admin User & Sample Restaurant)
-- ========================================================

-- Insert Sample Admin User (Email: admin@gmail.com, Password: AdminPass123@)
INSERT IGNORE INTO `users` (`id`, `name`, `email`, `password`, `phone`, `address`, `security_question`, `security_answer`, `role`, `is_blocked`)
VALUES (1, 'Admin User', 'admin@gmail.com', 'AdminPass123@', '9876543210', '101, Admin Tower, Mumbai - 400001', 'What is your favorite color?', 'Blue', 'ADMIN', 0);

-- Insert Sample Customer User (Email: customer@gmail.com, Password: UserPass123@)
INSERT IGNORE INTO `users` (`id`, `name`, `email`, `password`, `phone`, `address`, `security_question`, `security_answer`, `role`, `is_blocked`)
VALUES (2, 'John Doe', 'customer@gmail.com', 'UserPass123@', '9876543211', 'Flat 4B, Sunshine Heights, Mumbai - 400001', 'What is your favorite food?', 'Pizza', 'CUSTOMER', 0);

-- Insert Sample Restaurant
INSERT IGNORE INTO `restaurants` (`id`, `name`, `address`, `phone`, `email`, `category`, `cuisine`, `status`, `rating`, `total_ratings`, `delivery_charge`, `min_order_amount`)
VALUES (1, 'Tasty Bites', '123 Main Street, Downtown', '9876543200', 'info@tastybites.com', 'BOTH', 'Indian & Continental', 'OPEN', 4.5, 10, 40.0, 150.0);

-- Insert Sample Menu Items
INSERT IGNORE INTO `menu` (`id`, `restaurant_id`, `name`, `description`, `price`, `category`, `cuisine`, `availability`, `stock_quantity`, `is_featured`, `preparation_time`)
VALUES 
(1, 1, 'Paneer Butter Masala', 'Rich creamy gravy with fresh cottage cheese', 250.0, 'VEG', 'North Indian', 'AVAILABLE', 20, 1, 25),
(2, 1, 'Butter Naan', 'Tandoor baked flatbread brushed with butter', 40.0, 'VEG', 'North Indian', 'AVAILABLE', 50, 0, 10),
(3, 1, 'Chicken Biryani', 'Aromatic basmati rice cooked with succulent chicken and spices', 320.0, 'NON_VEG', 'Hyderabadi', 'AVAILABLE', 15, 1, 30);
