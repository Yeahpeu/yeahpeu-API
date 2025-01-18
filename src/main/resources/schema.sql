
-- userEntity
CREATE TABLE `user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `username` VARCHAR(50) NOT NULL UNIQUE,
    `password` VARCHAR(255) NOT NULL,
    `role` VARCHAR(50) NOT NULL,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ChatRoom
CREATE TABLE `chat_room` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `title` VARCHAR(255) NOT NULL,
    `reserved_member_count` INT NOT NULL,
    `used_member_count` INT NOT NULL,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ChatMessage
CREATE TABLE `chat_message` (
    `message_id` BIGINT NOT NULL,
    `room_id` BIGINT NOT NULL,
    `text` TEXT NOT NULL,
    `parent_message_id` BIGINT NULL,
    `sender_id` BIGINT NOT NULL,
    `sent_at` DATETIME NOT NULL,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`message_id`, `room_id`),
    INDEX `idx_sender` (`sender_id`),
    INDEX `idx_parent_message` (`parent_message_id`),
    FOREIGN KEY (`sender_id`) REFERENCES `user`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`room_id`) REFERENCES `chat_room`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ChatRoomUser

CREATE TABLE `chat_room_user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `user_id` BIGINT NOT NULL,
    `chat_room_id` BIGINT NOT NULL,
    `last_read_message_id` BIGINT NOT NULL,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_user_room` (`user_id`, `chat_room_id`),
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`chat_room_id`) REFERENCES `chat_room`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- MessageContent
CREATE TABLE `chat_message_content` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `content_type` VARCHAR(50) NOT NULL,
    `url` VARCHAR(255) NOT NULL,
    `message_id` BIGINT NOT NULL,
    `room_id` BIGINT NOT NULL,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`message_id`, `room_id`) REFERENCES `chat_message`(`message_id`, `room_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- budget
CREATE TABLE `budget` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `wedding_id` BIGINT NOT NULL,
    `total_budget` BIGINT NOT NULL,
    `used_budget` BIGINT NOT NULL,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_wedding_budget` (`wedding_id`),
    FOREIGN KEY (`wedding_id`) REFERENCES `wedding`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- Category
CREATE TABLE `category` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(255) NOT NULL,
    `parent_id` BIGINT NULL,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`parent_id`) REFERENCES `category`(`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--CateforyExpense

CREATE TABLE `category_expense` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `wedding_id` BIGINT NOT NULL,
    `category_id` BIGINT NOT NULL,
    `expense` INT NOT NULL,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`wedding_id`) REFERENCES `wedding`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`category_id`) REFERENCES `category`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Checklist

CREATE TABLE `checklist` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `schedule_id` BIGINT NOT NULL,
    `name` VARCHAR(255) NOT NULL,
    `is_completed` BOOLEAN NOT NULL DEFAULT FALSE,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`schedule_id`) REFERENCES `schedule`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Schedule

CREATE TABLE `schedule` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `wedding_id` BIGINT NOT NULL,
    `title` VARCHAR(255) NOT NULL,
    `date` DATETIME NOT NULL,
    `location` VARCHAR(255) NULL,
    `main_category_id` BIGINT NULL,
    `sub_category_id` BIGINT NULL,
    `price` VARCHAR(255) NULL,
    `is_completed` BOOLEAN NOT NULL DEFAULT FALSE,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`wedding_id`) REFERENCES `wedding`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`main_category_id`) REFERENCES `category`(`id`) ON DELETE SET NULL,
    FOREIGN KEY (`sub_category_id`) REFERENCES `category`(`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- UserCategory

CREATE TABLE `user_category` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `user_id` BIGINT NOT NULL,
    `category_id` BIGINT NOT NULL,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`category_id`) REFERENCES `category`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Wedding

CREATE TABLE `wedding` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `bride_id` BIGINT NOT NULL,
    `groom_id` BIGINT NOT NULL,
    `wedding_day` DATETIME NOT NULL,
    `is_onboarded` BOOLEAN NOT NULL,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`bride_id`) REFERENCES `user`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`groom_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- WishItem

CREATE TABLE `wish_item` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `wishlist_id` BIGINT NOT NULL,
    `title` VARCHAR(255) NOT NULL,
    `image_url` VARCHAR(255) NULL,
    `price` INT NOT NULL,
    `link_url` VARCHAR(255) NOT NULL,
    `maker` VARCHAR(255) NULL,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`wishlist_id`) REFERENCES `wishlist`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Wishlist

CREATE TABLE `wishlist` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `wedding_id` BIGINT NOT NULL,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`wedding_id`) REFERENCES `wedding`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

