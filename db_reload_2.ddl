TRUNCATE accounts_unblock_request CASCADE;
TRUNCATE clients_unblock_request CASCADE;
TRUNCATE payment CASCADE;
TRUNCATE card CASCADE;
TRUNCATE account CASCADE;
TRUNCATE client CASCADE;
TRUNCATE staff CASCADE;
TRUNCATE login_user CASCADE;

INSERT INTO login_user (id, first_name, middle_name, last_name, login, password, salt, status, user_type, create_date) VALUES
    (DEFAULT, 'Иван', 'Иванович', 'Иванов', 'Ivanov',
    '266EBAC6287FFE0A0D6098C3CB53D69BF8271242962B998596F715206FFC08DE','70bee2b',
    'Active', 'Client', '2018-11-01 0:0:0'),
    (DEFAULT, 'Петр', 'Петрович', 'Петров', 'Petrov',
    '244014490CCCDCEF16F5E9A096BA3F3EB7056AF69208AD2C8DC7C08E5F9A86A3','a51bf8b4',
    'Blocked', 'Client', '2018-11-01 0:0:0'),
    (DEFAULT, 'Сидр', 'Сидорович', 'Сидоров', 'Sidorov',
    'C4811FAD825356B841B6076BD4E2092C0C615200B1D5FBA78610312033BF7F30','98186d0',
    'Active', 'Client', '2018-11-01 0:0:0'),
    (DEFAULT, 'Админ', 'Админович', 'Админов', 'Adminov',
    '66611AC4EB6748A4DA5566E68E722606F8E4C72706FDC0BB426B9067168A4DDD','94b48e06',
    'Active', 'Staff',
    '2018-10-01 0:0:0'),
    (DEFAULT, 'Exchange', '', 'Bot', 'Exchange Bot',
    '18468C3D5CBF1EB34D8F52E550089CA821DC935F55947EAE7D28FF006E71677E','92e6779d',
    'Active', 'Client', '2015-10-01 0:0:0');

INSERT INTO staff (id, department_id, job_title_id) VALUES
    ((SELECT id FROM login_user WHERE login = 'Adminov'),
     (SELECT id FROM department WHERE name = 'Technical Support'),
     (SELECT id FROM job_title WHERE name = 'System Administrator'));

INSERT INTO client (id, client_type) VALUES
    ((SELECT id FROM login_user WHERE login = 'Ivanov'),'Regular'),
    ((SELECT id FROM login_user WHERE login = 'Petrov'),'Regular'),
    ((SELECT id FROM login_user WHERE login = 'Sidorov'),'VIP'),
    ((SELECT id FROM login_user WHERE login = 'Exchange Bot'),'Bot');

INSERT INTO account (id, number, account_type, card_account_type, credit_limit, balance, currency_type_id, status, client_id, create_date) VALUES
    (DEFAULT, 'BANKEXCHANGEUSD', 'Distribution', 'Debit',
        DEFAULT, 999999.00, (SELECT id FROM currency_type WHERE short_name = 'USD'),
        'Active', (SELECT id FROM login_user WHERE login = 'Exchange Bot'), '2015-10-01 0:0:0'),
    (DEFAULT, 'BANKEXCHANGEUAH', 'Distribution', 'Debit',
        DEFAULT, 999999999.00, (SELECT id FROM currency_type WHERE short_name = 'UAH'),
        'Active', (SELECT id FROM login_user WHERE login = 'Exchange Bot'), '2015-10-01 0:0:0');

INSERT INTO account (id, number, account_type, card_account_type, credit_limit, balance, currency_type_id, status, client_id, create_date) VALUES
    (DEFAULT, 'UA12345670000002603000000011', 'Distribution', 'Debit',
    DEFAULT, 0.00, (SELECT id FROM currency_type WHERE short_name = 'USD'),
    'Active', (SELECT id FROM login_user WHERE login = 'Ivanov'), '2018-11-12 0:0:0'),
    (DEFAULT, 'UA12345670000002600000000012', 'Current', 'Debit',
     DEFAULT, 100.00, (SELECT id FROM currency_type WHERE short_name = 'USD'),
    'Active', (SELECT id FROM login_user WHERE login = 'Ivanov'), '2018-11-11 0:0:0'),
    (DEFAULT, 'UA12345670000002600000000013', 'Current', 'Debit',
    DEFAULT,4400.00, (SELECT id FROM currency_type WHERE short_name = 'UAH'),
    'Active', (SELECT id FROM login_user WHERE login = 'Ivanov'), '2018-11-11 0:0:0'),
    (DEFAULT, 'UA12345670000002605000000014', 'Card', 'Debit',
    DEFAULT, 11350.00, (SELECT id FROM currency_type WHERE short_name = 'UAH'),
    'Active', (SELECT id FROM login_user WHERE login = 'Ivanov'), '2018-11-11 0:0:0'),
    (DEFAULT, 'UA12345670000002605000000015', 'Card', 'Credit',
    5000.00, -200.00, (SELECT id FROM currency_type WHERE short_name = 'UAH'),
    'Active', (SELECT id FROM login_user WHERE login = 'Ivanov'), '2019-12-11 0:0:0');

INSERT INTO card (id, number, provider, isChipped, status, account_id, create_date, expirationDate) VALUES
    (DEFAULT, '0000 0000 0000 0011', 'Visa', false, 'Deleted',
    (SELECT id FROM account WHERE number = 'UA12345670000002605000000014'),
    '2018-11-12 0:0:0', '2021-11-12 0:0:0'),
    (DEFAULT, '0000 0000 0000 0012', 'Visa', true, 'Active',
    (SELECT id FROM account WHERE number = 'UA12345670000002605000000014'),
    '2021-11-14 0:0:0', '2024-11-14 0:0:0'),
    (DEFAULT, '0000 0000 0000 0013', 'Mastercard', true, 'Active',
    (SELECT id FROM account WHERE number = 'UA12345670000002605000000015'),
    '2019-12-11 0:0:0', '2022-12-11 0:0:0');

INSERT INTO account (id, number, account_type, card_account_type, credit_limit, balance, currency_type_id, status, client_id, create_date) VALUES
    (DEFAULT, 'UA12345670000002603000000021', 'Distribution', 'Debit',
    DEFAULT, 0.00, (SELECT id FROM currency_type WHERE short_name = 'USD'),
    'Active', (SELECT id FROM login_user WHERE login = 'Petrov'), '2018-11-12 0:0:0'),
    (DEFAULT, 'UA12345670000002600000000022', 'Current', 'Debit',
    DEFAULT, 0.00, (SELECT id FROM currency_type WHERE short_name = 'USD'),
    'Active', (SELECT id FROM login_user WHERE login = 'Petrov'), '2018-11-11 0:0:0'),
    (DEFAULT, 'UA12345670000002600000000023', 'Current', 'Debit',
    DEFAULT,50.00, (SELECT id FROM currency_type WHERE short_name = 'UAH'),
    'Active', (SELECT id FROM login_user WHERE login = 'Petrov'), '2018-11-11 0:0:0'),
    (DEFAULT, 'UA12345670000002605000000024', 'Card', 'Debit',
     DEFAULT, 28736.00, (SELECT id FROM currency_type WHERE short_name = 'UAH'),
     'Active', (SELECT id FROM login_user WHERE login = 'Petrov'), '2018-11-11 0:0:0'),
    (DEFAULT, 'UA12345670000002605000000025', 'Card', 'Credit',
    5000.00, 5000.00, (SELECT id FROM currency_type WHERE short_name = 'UAH'),
    'Active', (SELECT id FROM login_user WHERE login = 'Petrov'), '2019-12-11 0:0:0');

INSERT INTO card (id, number, provider, isChipped, status, account_id, create_date, expirationDate) VALUES
    (DEFAULT, '0000 0000 0000 0021', 'Visa', false, 'Deleted',
    (SELECT id FROM account WHERE number = 'UA12345670000002605000000024'),
    '2018-11-12 0:0:0', '2021-11-12 0:0:0'),
    (DEFAULT, '0000 0000 0000 0022', 'Visa', true, 'Active',
    (SELECT id FROM account WHERE number = 'UA12345670000002605000000024'),
    '2021-11-14 0:0:0', '2024-11-14 0:0:0'),
    (DEFAULT, '0000 0000 0000 0023', 'Mastercard', true, 'Active',
    (SELECT id FROM account WHERE number = 'UA12345670000002605000000025'),
    '2019-12-11 0:0:0', '2022-12-11 0:0:0');

INSERT INTO account (id, number, account_type, card_account_type, credit_limit, balance, currency_type_id, status, client_id, create_date) VALUES
    (DEFAULT, 'UA12345670000002600000000031', 'Current', 'Debit',
    DEFAULT, 1234.00, (SELECT id FROM currency_type WHERE short_name = 'USD'),
    'Active', (SELECT id FROM login_user WHERE login = 'Sidorov'), '2018-11-11 0:0:0'),
    (DEFAULT, 'UA12345670000002600000000032', 'Current', 'Debit',
    DEFAULT, 1234.00, (SELECT id FROM currency_type WHERE short_name = 'USD'),
    'Active', (SELECT id FROM login_user WHERE login = 'Sidorov'), '2018-11-11 0:0:0'),
    (DEFAULT, 'UA12345670000002600000000033', 'Current', 'Debit',
    DEFAULT,1234.00, (SELECT id FROM currency_type WHERE short_name = 'UAH'),
    'Active', (SELECT id FROM login_user WHERE login = 'Sidorov'), '2018-11-11 0:0:0'),
    (DEFAULT, 'UA12345670000002600000000034', 'Current', 'Debit',
    DEFAULT,1234.00, (SELECT id FROM currency_type WHERE short_name = 'UAH'),
    'Blocked', (SELECT id FROM login_user WHERE login = 'Sidorov'), '2018-11-11 0:0:0'),
    (DEFAULT, 'UA12345670000002605000000035', 'Card', 'Debit',
    DEFAULT, 11100.00, (SELECT id FROM currency_type WHERE short_name = 'UAH'),
    'Active', (SELECT id FROM login_user WHERE login = 'Sidorov'), '2018-11-11 0:0:0'),
    (DEFAULT, 'UA12345670000002605000000036', 'Card', 'Credit',
    75000.00, 3200.00, (SELECT id FROM currency_type WHERE short_name = 'UAH'),
    'Active', (SELECT id FROM login_user WHERE login = 'Sidorov'), '2019-12-11 0:0:0');

INSERT INTO card (id, number, provider, isChipped, status, account_id, create_date, expirationDate) VALUES
    (DEFAULT, '0000 0000 0000 0031', 'Visa', false, 'Deleted',
    (SELECT id FROM account WHERE number = 'UA12345670000002605000000035'),
    '2018-11-12 0:0:0', '2021-11-12 0:0:0'),
    (DEFAULT, '0000 0000 0000 0032', 'Visa', true, 'Active',
    (SELECT id FROM account WHERE number = 'UA12345670000002605000000035'),
    '2021-11-14 0:0:0', '2024-11-14 0:0:0'),
    (DEFAULT, '0000 0000 0000 0033', 'Mastercard', true, 'Active',
    (SELECT id FROM account WHERE number = 'UA12345670000002605000000036'),
    '2019-12-11 0:0:0', '2022-12-11 0:0:0');
