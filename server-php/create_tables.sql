CREATE TABLE `se_data_one` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `client_device_id` VARCHAR(255) NULL DEFAULT NULL COLLATE 'latin1_swedish_ci',
    `data` TEXT NULL DEFAULT NULL COLLATE 'latin1_swedish_ci',
    `timestamp_server` DATETIME NULL DEFAULT NULL
)
    COLLATE='latin1_swedish_ci'
ENGINE=InnoDB
;
