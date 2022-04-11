CREATE TABLE to_do (
                       `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
                       `category_id` bigint(20) unsigned NOT NULL,
                       `title` VARCHAR(255) NOT NULL,
                       `body` TEXT,
                       `state` TINYINT UNSIGNED NOT NULL,
                       `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                       `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO to_do(id, category_id, title, state, updated_at) VALUES
                                                                 (1, 2, 'to_do1', 1,'2020-03-15 13:15:00.012345'),
                                                                 (2, 2, 'to_do2', 3,'2020-03-15 14:15:00.012345'),
                                                                 (3, 4, 'to_do3', 2,'2020-03-15 15:15:00.012345'),
                                                                 (4, 5, 'to_do4', 3,'2020-03-15 16:15:00.012345'),
                                                                 (5, 1, 'to_do5', 3,'2020-03-15 17:15:00.012345');

