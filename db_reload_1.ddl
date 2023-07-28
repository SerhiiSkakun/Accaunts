--createdb accounts
-- CREATE DATABASE accounts WITH OWNER = "user" ENCODING = 'UTF8';

DROP TABLE IF EXISTS accounts_unblock_request;
DROP TABLE IF EXISTS clients_unblock_request;
DROP TABLE IF EXISTS payment;
DROP TABLE IF EXISTS card;
DROP TABLE IF EXISTS account;
DROP TABLE IF EXISTS client;
DROP TABLE IF EXISTS staff;
DROP TABLE IF EXISTS job_title_lang;
DROP TABLE IF EXISTS job_title;
DROP TABLE IF EXISTS department_lang;
-- DROP TABLE IF EXISTS department;
DROP TABLE IF EXISTS login_user;
DROP TABLE IF EXISTS action_type_lang;
DROP TABLE IF EXISTS payment_status_lang;
DROP TABLE IF EXISTS account_type_lang;
DROP TABLE IF EXISTS card_account_type_lang;
DROP TABLE IF EXISTS client_type_lang;
DROP TABLE IF EXISTS staff_type_lang;
DROP TABLE IF EXISTS status_lang;
DROP TABLE IF EXISTS currency_type;
DROP TABLE IF EXISTS language;
DROP TYPE IF EXISTS user_type;
DROP TYPE IF EXISTS status;
DROP TYPE IF EXISTS department;
DROP TYPE IF EXISTS account_type;
DROP TYPE IF EXISTS action_type;
DROP TYPE IF EXISTS payment_status;
DROP TYPE IF EXISTS card_account_type;
DROP TYPE IF EXISTS client_type;
DROP TYPE IF EXISTS card_provider;

CREATE TABLE IF NOT EXISTS language (
    code        VARCHAR(2)              PRIMARY KEY,
    name        VARCHAR(50)             NOT NULL
);

CREATE TYPE status as ENUM ('Active', 'Blocked', 'Deleted');

CREATE TABLE IF NOT EXISTS status_lang (
    id              SERIAL              PRIMARY KEY,
    language_code   VARCHAR(2)          NOT NULL    REFERENCES language(code) ON DELETE CASCADE ON UPDATE CASCADE,
    status          status              NOT NULL,
    name            VARCHAR(50)         NOT NULL DEFAULT 'Active'
);

CREATE TYPE user_type as ENUM ('Client', 'Staff');

CREATE TABLE IF NOT EXISTS login_user (
    id          BIGSERIAL               PRIMARY KEY,
    first_name  VARCHAR(50)             NOT NULL,
    middle_name VARCHAR(50),
    last_name   VARCHAR(50)             NOT NULL,
    login       VARCHAR(50)             NOT NULL UNIQUE,
    password    VARCHAR(100)            NOT NULL,
    salt        VARCHAR(50)             NOT NULL,
    status      status                  NOT NULL,
    user_type   user_type               NOT NULL,
    create_date TIMESTAMP               NOT NULL DEFAULT now()
);

-- CREATE TABLE IF NOT EXISTS department (
--     id          SERIAL                  PRIMARY KEY,
--     name        VARCHAR(50)             NOT NULL UNIQUE
-- );

CREATE TYPE department as ENUM ('Technical Support', 'Customer Service' , 'HR', 'Administration', 'Accounting');

CREATE TABLE IF NOT EXISTS department_lang (
    id              SERIAL                  PRIMARY KEY,
    language_code   VARCHAR(2)            NOT NULL    REFERENCES language(code) ON DELETE CASCADE ON UPDATE CASCADE,
    department      department              NOT NULL,
    name            VARCHAR(50)             NOT NULL
);

CREATE TABLE IF NOT EXISTS job_title (
    id              SERIAL          PRIMARY KEY,
    department      department      NOT NULL,
    name            VARCHAR(100)    NOT NULL            DEFAULT 'Customer Service'
);

CREATE TABLE IF NOT EXISTS job_title_lang (
    id              SERIAL          PRIMARY KEY,
    language_code   VARCHAR(2)          NOT NULL    REFERENCES language(code) ON DELETE CASCADE ON UPDATE CASCADE,
    department      department      NOT NULL,
    job_title_id    INT             NOT NULL            REFERENCES job_title(id) ON DELETE CASCADE ON UPDATE CASCADE,
    name            VARCHAR(100)    NOT NULL
);

CREATE TABLE IF NOT EXISTS staff (
    id              BIGINT         PRIMARY KEY              REFERENCES login_user(id) ON DELETE CASCADE ON UPDATE CASCADE,
    department      department     NOT NULL,
    job_title_id    INT            NOT NULL                 REFERENCES job_title(id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TYPE client_type as ENUM ('Regular', 'VIP', 'Bot');

CREATE TABLE IF NOT EXISTS client_type_lang (
    id                  SERIAL                  PRIMARY KEY,
    language_code   VARCHAR(2)          NOT NULL    REFERENCES language(code) ON DELETE CASCADE ON UPDATE CASCADE,
    client_type         client_type             NOT NULL,
    name                VARCHAR(50)             NOT NULL DEFAULT 'Regular'
);

CREATE TABLE IF NOT EXISTS client (
    id              BIGINT                  PRIMARY KEY     REFERENCES login_user(id) ON DELETE CASCADE ON UPDATE CASCADE,
    client_type     client_type             NOT NULL
);

CREATE TYPE account_type as ENUM ('Distribution', 'Current', 'Card');

CREATE TABLE IF NOT EXISTS account_type_lang (
    id              SERIAL                  PRIMARY KEY,
    language_code   VARCHAR(2)          NOT NULL    REFERENCES language(code) ON DELETE CASCADE ON UPDATE CASCADE,
    account_type    account_type            NOT NULL,
    name            VARCHAR(50)             NOT NULL DEFAULT 'Current'
);

CREATE TYPE card_account_type as ENUM ('Debit', 'Credit');

CREATE TABLE IF NOT EXISTS card_account_type_lang (
    id                  SERIAL                  PRIMARY KEY,
    language_code   VARCHAR(2)          NOT NULL    REFERENCES language(code) ON DELETE CASCADE ON UPDATE CASCADE,
    card_account_type   card_account_type       NOT NULL,
    name                VARCHAR(50)             NOT NULL DEFAULT 'Debit'
);

CREATE TABLE IF NOT EXISTS currency_type (
    id          INT                     PRIMARY KEY,
    short_name  VARCHAR(3)              NOT NULL UNIQUE,
    name        VARCHAR(50)             NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS account (
    id                  BIGSERIAL               PRIMARY KEY,
    number              VARCHAR(28)             NOT NULL UNIQUE,
    account_type        account_type            NOT NULL,
    card_account_type   card_account_type       NOT NULL,
    credit_limit        DECIMAL                 NOT NULL                DEFAULT 0 CHECK (credit_limit >= 0),
    balance             DECIMAL                 NOT NULL                CHECK (balance >= -credit_limit),
    currency_type_id    INT                     NOT NULL                REFERENCES currency_type(id) ON DELETE CASCADE ON UPDATE CASCADE,
    status              status                  NOT NULL,
    client_id           BIGINT                  NOT NULL                REFERENCES client(id) ON DELETE CASCADE ON UPDATE CASCADE,
    create_date         TIMESTAMP               NOT NULL                DEFAULT now()
);

CREATE TYPE card_provider AS ENUM ('Visa', 'Mastercard', 'American Express');

CREATE TABLE IF NOT EXISTS card (
    id          BIGSERIAL               PRIMARY KEY,
    number      VARCHAR(19)             NOT NULL UNIQUE,
    provider    card_provider           NOT NULL,
    isChipped   BOOLEAN                 NOT NULL,
    status      status                  NOT NULL,
    account_id  BIGINT                                          REFERENCES account(id) ON DELETE SET NULL ON UPDATE CASCADE,
    create_date TIMESTAMP               NOT NULL                DEFAULT now(),
    expirationDate TIMESTAMP            NOT NULL                DEFAULT (now() + INTERVAL '3 YEAR'),
    CHECK (expirationDate > create_date)
);

CREATE TYPE payment_status AS ENUM ('Prepared', 'Sent');

CREATE TABLE IF NOT EXISTS payment_status_lang (
    id              SERIAL                  PRIMARY KEY,
    language_code   VARCHAR(2)          NOT NULL    REFERENCES language(code) ON DELETE CASCADE ON UPDATE CASCADE,
    payment_status  payment_status          NOT NULL,
    name            VARCHAR(50)             NOT NULL DEFAULT 'Prepared'
);

CREATE TABLE IF NOT EXISTS payment (
    id          BIGSERIAL               PRIMARY KEY,
    from_account_id BIGINT              NOT NULL                REFERENCES account(id) ON DELETE RESTRICT ON UPDATE CASCADE,
    to_account_id   BIGINT              NOT NULL                REFERENCES account(id) ON DELETE RESTRICT ON UPDATE CASCADE,
    sum         DECIMAL                 NOT NULL                CHECK (sum >= 0),
    currency_type_id    INT             NOT NULL                REFERENCES currency_type(id) ON DELETE CASCADE ON UPDATE CASCADE,
    payment_status   payment_status     NOT NULL                NOT NULL DEFAULT 'Prepared',
    create_date TIMESTAMP               NOT NULL                DEFAULT now()
);

CREATE TYPE action_type AS ENUM ('Unblock', 'Done');

CREATE TABLE IF NOT EXISTS action_type_lang (
    id              SERIAL              PRIMARY KEY,
    language_code   VARCHAR(2)          NOT NULL    REFERENCES language(code) ON DELETE CASCADE ON UPDATE CASCADE,
    action_type     action_type         NOT NULL,
    name            VARCHAR(50)         NOT NULL
);

CREATE TABLE IF NOT EXISTS clients_unblock_request (
    id              BIGSERIAL          PRIMARY KEY,
    account_id      BIGINT             NOT NULL                REFERENCES account(id) ON DELETE CASCADE ON UPDATE CASCADE,
    action_type     action_type        NOT NULL,
    create_date     TIMESTAMP          NOT NULL                DEFAULT now()
);

CREATE TABLE IF NOT EXISTS accounts_unblock_request (
    id          BIGSERIAL               PRIMARY KEY,
    client_id   BIGINT                  NOT NULL                REFERENCES client(id) ON DELETE CASCADE ON UPDATE CASCADE,
    action      action_type             NOT NULL,
    create_date TIMESTAMP               NOT NULL                DEFAULT now()
);

INSERT INTO language VALUES
    ('en', 'English'),
    ('ru', 'Русский'),
    ('ua', 'Українська');

INSERT INTO status_lang (id, language_code, status, name) VALUES
    (DEFAULT, 'en', 'Active', 'Active'),
    (DEFAULT, 'en', 'Blocked', 'Blocked'),
    (DEFAULT, 'en', 'Deleted', 'Deleted'),
    (DEFAULT, 'ru', 'Active', 'Активный'),
    (DEFAULT, 'ru', 'Blocked', 'Заблокированный'),
    (DEFAULT, 'ru', 'Deleted', 'Удалённый'),
    (DEFAULT, 'ua', 'Active', 'Активний'),
    (DEFAULT, 'ua', 'Blocked', 'Заблокований'),
    (DEFAULT, 'ua', 'Deleted', 'Видалений');

-- INSERT INTO department (id, name) VALUES
--     (1, 'Technical Support'),
--     (2, 'Customer Service'),
--     (3, 'HR'),
--     (4, 'Administration'),
--     (5, 'Accounting');

INSERT INTO department_lang (id, language_code, department, name) VALUES
    (DEFAULT, 'en', 'Technical Support', 'Technical Support'),
    (DEFAULT, 'en', 'Customer Service', 'Customer Service'),
    (DEFAULT, 'en', 'HR', 'HR'),
    (DEFAULT, 'en', 'Administration', 'Administration'),
    (DEFAULT, 'en', 'Accounting', 'Accounting'),
    (DEFAULT, 'ru', 'Technical Support', 'Техническая поддержка'),
    (DEFAULT, 'ru', 'Customer Service', 'Отдел обслуживания клиентов'),
    (DEFAULT, 'ru', 'HR', 'Отдел кадров'),
    (DEFAULT, 'ru', 'Administration', 'Администрация'),
    (DEFAULT, 'ru', 'Accounting', 'Бухгалтерия'),
    (DEFAULT, 'ua', 'Technical Support', 'Технічна підтримка'),
    (DEFAULT, 'ua', 'Customer Service', 'Відділ обслуговування клієнтів'),
    (DEFAULT, 'ua', 'HR', 'Відділ кадрів'),
    (DEFAULT, 'ua', 'Administration', 'Адміністрація'),
    (DEFAULT, 'ua', 'Accounting', 'Бухгалтерія');

INSERT INTO job_title (id, department, name) VALUES
    (1, 'Technical Support', 'System Administrator'),
    (2, 'Customer Service', 'Consultant'),
    (3, 'Customer Service', 'Currency Exchange Manager'),
    (4, 'HR', 'HR'),
    (5, 'HR', 'Recruiter'),
    (6, 'Administration', 'Director'),
    (7, 'Accounting', 'Accountant');

INSERT INTO job_title_lang (id, language_code, department, job_title_id, name) VALUES
    (DEFAULT, 'en', 'Technical Support', 1, 'System Administrator'),
    (DEFAULT, 'en', 'Customer Service', 2, 'Consultant'),
    (DEFAULT, 'en', 'Customer Service', 3, 'Currency Exchange Manager'),
    (DEFAULT, 'en', 'HR', 4, 'HR'),
    (DEFAULT, 'en', 'HR', 5, 'Recruiter'),
    (DEFAULT, 'en', 'Administration', 6, 'Director'),
    (DEFAULT, 'en', 'Accounting', 7, 'Accountant'),
    (DEFAULT, 'ru', 'Technical Support', 1, 'Админ'),
    (DEFAULT, 'ru', 'Customer Service', 2, 'Консультант'),
    (DEFAULT, 'ru', 'Customer Service', 3, 'Менеджер обмена валюты'),
    (DEFAULT, 'ru', 'HR', 4, 'HR'),
    (DEFAULT, 'ru', 'HR', 5, 'Рекрутер'),
    (DEFAULT, 'ru', 'Administration', 6, 'Директор'),
    (DEFAULT, 'ru', 'Accounting', 7, 'Бухгалтер'),
    (DEFAULT, 'ua', 'Technical Support', 1, 'Адмін'),
    (DEFAULT, 'ua', 'Customer Service', 2, 'Консультант'),
    (DEFAULT, 'ua', 'Customer Service', 3, 'Менеджер обміну валюти'),
    (DEFAULT, 'ua', 'HR', 4, 'HR'),
    (DEFAULT, 'ua', 'HR', 5, 'Рекрутер'),
    (DEFAULT, 'ua', 'Administration', 6, 'Директор'),
    (DEFAULT, 'ua', 'Accounting', 7, 'Бухгалтер');

INSERT INTO client_type_lang (id, language_code, client_type, name) VALUES
    (DEFAULT, 'en', 'Regular', 'Regular'),
    (DEFAULT, 'en', 'VIP', 'VIP'),
    (DEFAULT, 'en', 'Bot', 'Bot'),
    (DEFAULT, 'ru', 'Regular', 'Обычный'),
    (DEFAULT, 'ru', 'VIP', 'Привилегированный'),
    (DEFAULT, 'ru', 'Bot', 'Bot'),
    (DEFAULT, 'ua', 'Regular', 'Звичайний'),
    (DEFAULT, 'ua', 'VIP', 'Превілізований'),
    (DEFAULT, 'ua', 'Bot', 'Bot');

INSERT INTO account_type_lang (id, language_code, account_type, name) VALUES
    (DEFAULT, 'en', 'Distribution', 'Distribution'),
    (DEFAULT, 'en', 'Current', 'Current'),
    (DEFAULT, 'en', 'Card', 'Card'),
    (DEFAULT, 'ru', 'Distribution', 'Распределительный'),
    (DEFAULT, 'ru', 'Current', 'Текущий'),
    (DEFAULT, 'ru', 'Card', 'Карточный'),
    (DEFAULT, 'ua', 'Distribution', 'Розподільчий'),
    (DEFAULT, 'ua', 'Current', 'Поточний'),
    (DEFAULT, 'ua', 'Card', 'Картковий');

INSERT INTO card_account_type_lang (id, language_code, card_account_type, name) VALUES
    (DEFAULT, 'en', 'Debit', 'Debit'),
    (DEFAULT, 'en', 'Credit', 'Credit'),
    (DEFAULT, 'ru', 'Debit', 'Дебитовая'),
    (DEFAULT, 'ru', 'Credit', 'Кредитная'),
    (DEFAULT, 'ua', 'Debit', 'Дебетова'),
    (DEFAULT, 'ua', 'Credit', 'Кредитна');

INSERT INTO currency_type (id, short_name, name) VALUES
    (1, 'USD', 'United States Dollar'),
    (2, 'EUR', 'Euro'),
    (3, 'UAH', 'Ukrainian Hryvnia');

INSERT INTO payment_status_lang (id, language_code, payment_status, name) VALUES
    (DEFAULT, 'en', 'Prepared', 'Prepared'),
    (DEFAULT, 'en', 'Sent', 'Sent'),
    (DEFAULT, 'ru', 'Prepared', 'Подготовленный'),
    (DEFAULT, 'ru', 'Sent', 'Отосланный'),
    (DEFAULT, 'ua', 'Prepared', 'Підготовлений'),
    (DEFAULT, 'ua', 'Sent', 'Відісланий');

INSERT INTO action_type_lang (id, language_code, action_type, name) VALUES
    (DEFAULT, 'en', 'Unblock', 'Unblocked'),
    (DEFAULT, 'en', 'Done', 'Done'),
    (DEFAULT, 'ru', 'Unblock', 'Разблокировать'),
    (DEFAULT, 'ru', 'Done', 'Готово'),
    (DEFAULT, 'ua', 'Unblock', 'Разблокувати'),
    (DEFAULT, 'ua', 'Done', 'Зроблено');
