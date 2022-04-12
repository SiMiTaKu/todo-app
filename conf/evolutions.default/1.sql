/*
CREATE TABLE `to_do_category` (
                                  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
                                  `name` VARCHAR(255) NOT NULL,
                                  `slug` VARCHAR(64) CHARSET ascii NOT NULL,
                                  `color` TINYINT UNSIGNED NOT NULL,
                                  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO to_do_category(name,slug,color) values('フロントエンド','front',1);
INSERT INTO to_do_category(name,slug,color) values('バックエンド','back',2);
INSERT INTO to_do_category(name,slug,color) values('インフラ','infra',3);
*/

CREATE TABLE to_do (
                       `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
                       `category_id` bigint(20) unsigned NOT NULL,
                       `title` VARCHAR(255) NOT NULL,
                       /*
                       body` TEXT,
                       `state` TINYINT UNSIGNED NOT NULL,
                        */
                       `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                       `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


INSERT INTO to_do(id, category_id, title, updated_at) VALUES
                                                                 (1, 2, 'todo1', '2020-03-15 13:15:00.012345'),
                                                                 (2, 2, 'todo1', '2020-03-15 14:15:00.012345'),
                                                                 (3, 4, 'todo1', '2020-03-15 15:15:00.012345'),
                                                                 (4, 5, 'todo1', '2020-03-15 16:15:00.012345'),
                                                                 (5, 1, 'todo1', '2020-03-15 17:15:00.012345');

DROP TABLE to_do;