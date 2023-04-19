CREATE SCHEMA IF NOT EXISTS money_app
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_general_ci;

CREATE TABLE user_roles
(
    id   BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    role VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);


CREATE TABLE `users`
(
    `id`         BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    `password`   VARCHAR(255) NOT NULL,
    `username`   VARCHAR(255) NOT NULL,
    `email`      VARCHAR(255) NOT NULL,
    `first_name` VARCHAR(255) NOT NULL,
    `last_name`  VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_9ssax1uhaqjdwj3b07t65d7k7` (`username`),
    UNIQUE KEY `UK_5ag1t9gdh0iqvr71tq0cttqif` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE expense_categories
(
    id       BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    category VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE expenses
(
    id               BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    name             VARCHAR(255)   NOT NULL,
    price_per_unit   DECIMAL(10, 2),
    number_of_units  INT(11),
    total_price      DECIMAL(10, 2) NOT NULL,
    date_of_purchase DATETIME       NOT NULL,
    category_id      BIGINT(20) UNSIGNED,
    owner_id         BIGINT(20) UNSIGNED,
    PRIMARY KEY (id),
    CONSTRAINT fk_category FOREIGN KEY (category_id) REFERENCES expense_categories (id) ON DELETE SET NULL,
    CONSTRAINT fk_owner FOREIGN KEY (owner_id) REFERENCES users (id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `income_categories`
(
    `id`       BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    `category` VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `incomes`
(
    `id`                 BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    `amount`             DECIMAL(10, 2) NOT NULL DEFAULT 0,
    `description`        TEXT,
    `created_on`         DATETIME       NOT NULL,
    `income_category_id` BIGINT(20) UNSIGNED,
    `owner_id`           BIGINT(20) UNSIGNED,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`income_category_id`) REFERENCES `income_categories` (`id`) ON DELETE SET NULL,
    FOREIGN KEY (`owner_id`) REFERENCES `users` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE savings
(
    id               BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    amount           DECIMAL      DEFAULT 0,
    date_of_creation DATETIME NOT NULL,
    end_date         DATE     NOT NULL,
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

INSERT INTO users (id, email, first_name, last_name, password, username)
VALUES (1, 'admin@example.com', 'Admin', 'Adminov', '$2a$10$rLls/wZcKmMPgjalrbU6O.pPiqNfJiF4fFg/YFZ9TKEtbOSGybE3G',
        'admin'),
       (2, 'user@example.com', 'User', 'Userov', '$2a$10$vg28sup.GulV1T7UymjzS.Nzcue.yLXUFRPNP1hv1NNt1j24GWewO',
        'user');

INSERT INTO users_user_roles (user_entity_id, user_roles_id)
VALUES (1, 1),
       (1, 2),
       (2, 2);

INSERT INTO income_categories (id, category)
VALUES (1, 'CASH'),
       (2, 'BANK'),
       (3, 'SAVINGS');

INSERT INTO incomes(id, amount, description, created_on, income_category_id, owner_id)
VALUES (1, 10000.56, 'Salary', '2023-04-04 16:51:27.144758', 2, 1),
       (2, 900.56, 'Gift', '2023-04-04 16:51:27.144758', 1, 2),
       (3, 900.56, 'Voucher', '2020-04-04 16:51:27.144758', 1, 2);

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

INSERT INTO expenses (id, name, number_of_units, price_per_unit, date_of_purchase, total_price, category_id, owner_id)
VALUES (1, 'Movie tickets', 2, 25, '2023-01-23 22:22:27', 50, 6, 1),
       (2, 'Internet bill', null, null, '2023-01-23 22:22:27', 25, 12, 2),
       (3, 'tomatoes', 2, 2.5, '2020-01-23 22:22:27', 5, 11, 2),
       (4, 'rent', null, null, '2021-01-23 09:22:27', 800, 12, 2),
       (5, 'holiday', null, null, '2007-06-13 09:00:00', 2800, 7, 1);

INSERT INTO savings (id, amount, date_of_creation, end_date, goal)
VALUES (1, 10000, '2022-12-24 21:26', '2023-04-09', 'Summer holiday in Greece'),
       (2, 100, '2023-02-24 21:26', '2023-04-09', 'not provided by user'),
       (3, 700, '2022-03-09 21:26', '2022-04-09', 'Rent for the  month'),
       (4, 1700, '2022-03-09 21:26', '2023-04-06', 'Bungee jumping');

INSERT INTO savings_owners(saving_entity_id, owners_id)
VALUES (1, 1),
       (2, 2),
       (3, 1),
       (3, 2),
       (4, 1);

INSERT INTO savings_contributors (saving_entity_id, contributors_id)
VALUES (1, 1),
       (1, 2),
       (2, 2),
       (3, 1),
       (3, 2),
       (4, 1);



