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


