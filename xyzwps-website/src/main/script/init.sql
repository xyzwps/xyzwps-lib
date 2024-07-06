CREATE TABLE `users` (
  `id`           bigint unsigned  NOT NULL AUTO_INCREMENT,
  `phone`        varchar(16)      NOT NULL,
  `display_name` varchar(30)  DEFAULT NULL,
  `avatar`       varchar(200) DEFAULT NULL,
  `password`     varchar(200) DEFAULT NULL,
  `email`        varchar(100) DEFAULT NULL,
  `status`       varchar(10)      NOT NULL,
  `create_time`  datetime         NOT NULL,
  `update_time`  datetime         NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_phone` (`phone`)
) ENGINE=InnoDB;