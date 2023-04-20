CREATE SCHEMA IF NOT EXISTS testdb;

CREATE TABLE user_roles
(
    id   BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    role VARCHAR(255)    NOT NULL,
    PRIMARY KEY (id)
);


CREATE TABLE `users`
(
    `id`         BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    `password`   VARCHAR(255)        NOT NULL,
    `username`   VARCHAR(255)        NOT NULL,
    `email`      VARCHAR(255)        NOT NULL,
    `first_name` VARCHAR(255)        NOT NULL,
    `last_name`  VARCHAR(255)        NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_9ssax1uhaqjdwj3b07t65d7k7` (`username`),
    UNIQUE KEY `UK_5ag1t9gdh0iqvr71tq0cttqif` (`email`)
) ENGINE = InnoDB;

CREATE TABLE expense_categories
(
    id       BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    category VARCHAR(255)        NOT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB;

CREATE TABLE expenses
(
    id               BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    name             VARCHAR(255)        NOT NULL,
    price_per_unit   DECIMAL(10, 2),
    number_of_units  INT(11),
    total_price      DECIMAL(10, 2)      NOT NULL,
    date_of_purchase DATETIME            NOT NULL,
    category_id      BIGINT(20) UNSIGNED,
    owner_id         BIGINT(20) UNSIGNED,
    PRIMARY KEY (id),
    CONSTRAINT fk_category FOREIGN KEY (category_id) REFERENCES expense_categories (id) ON DELETE SET NULL,
    CONSTRAINT fk_owner FOREIGN KEY (owner_id) REFERENCES users (id) ON DELETE SET NULL
) ENGINE = InnoDB;

CREATE TABLE `income_categories`
(
    `id`       BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    `category` VARCHAR(255)        NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB;

CREATE TABLE `incomes`
(
    `id`                 BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    `amount`             DECIMAL(10, 2)      NOT NULL DEFAULT 0,
    `description`        TEXT,
    `created_on`         DATETIME            NOT NULL,
    `income_category_id` BIGINT(20) UNSIGNED,
    `owner_id`           BIGINT(20) UNSIGNED,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`income_category_id`) REFERENCES `income_categories` (`id`) ON DELETE SET NULL,
    FOREIGN KEY (`owner_id`) REFERENCES `users` (`id`) ON DELETE SET NULL
) ENGINE = InnoDB;

CREATE TABLE savings
(
    id               BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    amount           DECIMAL      DEFAULT 0,
    date_of_creation DATETIME        NOT NULL,
    end_date         DATE            NOT NULL,
    goal             VARCHAR(255) DEFAULT 'not provided by user',
    PRIMARY KEY (id)
);

CREATE TABLE users_user_roles
(
    user_entity_id BIGINT UNSIGNED,
    user_roles_id  BIGINT UNSIGNED,
    CONSTRAINT fk_users FOREIGN KEY (user_entity_id)
        REFERENCES users (id),
    CONSTRAINT fk_user_roles FOREIGN KEY (user_roles_id)
        REFERENCES user_roles (id)
);

CREATE TABLE savings_owners
(
    saving_entity_id BIGINT UNSIGNED,
    owners_id        BIGINT UNSIGNED,
    CONSTRAINT
        fk_saving_entity_owners
        FOREIGN
            KEY
            (
             saving_entity_id
                )
            REFERENCES savings
                (
                 id
                    ),
    CONSTRAINT fk_owners_id FOREIGN KEY
        (
         owners_id
            )
        REFERENCES users
            (
             id
                )
);

CREATE TABLE savings_contributors
(
    saving_entity_id BIGINT UNSIGNED,
    contributors_id  BIGINT UNSIGNED,
    CONSTRAINT
        fk_saving_entity_contributors
        FOREIGN
            KEY
            (
             saving_entity_id
                )
            REFERENCES savings
                (
                 id
                    ),
    CONSTRAINT fk_contributors_id FOREIGN KEY
        (
         contributors_id
            )
        REFERENCES users
            (
             id
                )
);

INSERT INTO user_roles
VALUES (1, 'ADMIN'),
       (2, 'USER');

INSERT INTO income_categories (id, category)
VALUES (1, 'CASH'),
       (2, 'BANK'),
       (3, 'SAVINGS');

INSERT INTO expense_categories (id, category)
VALUES (1, 'FOOD_AND_DRINKS'),
       (2, 'BILLS_AND_FEES'),
       (3, 'GIFTS'),
       (4, 'CAR'),
       (5, 'HEALTH'),
       (6, 'SOCIAL_LIFE'),
       (7, 'FAMILY'),
       (8, 'CLOTHES'),
       (9, 'BEAUTY'),
       (10, 'EDUCATION'),
       (11, 'GROCERIES'),
       (12, 'HOME');