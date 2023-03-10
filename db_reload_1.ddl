-- createdb accounts

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
DROP TABLE IF EXISTS department;
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
DROP TYPE IF EXISTS account_type;
DROP TYPE IF EXISTS action_type;
DROP TYPE IF EXISTS payment_status;
DROP TYPE IF EXISTS card_account_type;
DROP TYPE IF EXISTS client_type;
DROP TYPE IF EXISTS card_provider;

CREATE TABLE IF NOT EXISTS language (
    id          INT                     PRIMARY KEY,
    code        VARCHAR(2)              NOT NULL,
    name        VARCHAR(50)             NOT NULL
);

CREATE TYPE status as ENUM ('Active', 'Blocked', 'Deleted');

CREATE TABLE IF NOT EXISTS status_lang (
    id          SERIAL                  PRIMARY KEY,
    language_id INT                     NOT NULL    REFERENCES language(id) ON DELETE CASCADE ON UPDATE CASCADE,
    status      status                  NOT NULL,
    name        VARCHAR(50)             NOT NULL DEFAULT 'Active'
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

CREATE TABLE IF NOT EXISTS department (
    id          SERIAL                  PRIMARY KEY,
    name        VARCHAR(50)             NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS department_lang (
    id              SERIAL                  PRIMARY KEY,
    language_id     INT                     NOT NULL        REFERENCES language(id) ON DELETE CASCADE ON UPDATE CASCADE,
    department_id   INT                     NOT NULL        REFERENCES department(id) ON DELETE CASCADE ON UPDATE CASCADE,
    name            VARCHAR(50)             NOT NULL
);

CREATE TABLE IF NOT EXISTS job_title (
    id              SERIAL          PRIMARY KEY,
    department_id   INT             NOT NULL            REFERENCES department(id) ON DELETE CASCADE ON UPDATE CASCADE,
    name            VARCHAR(100)    NOT NULL            DEFAULT 'Customer Service'
);

CREATE TABLE IF NOT EXISTS job_title_lang (
    id              SERIAL          PRIMARY KEY,
    language_id     INT             NOT NULL            REFERENCES language(id) ON DELETE CASCADE ON UPDATE CASCADE,
    department_id   INT             NOT NULL            REFERENCES department(id) ON DELETE CASCADE ON UPDATE CASCADE,
    job_title_id    INT             NOT NULL            REFERENCES job_title(id) ON DELETE CASCADE ON UPDATE CASCADE,
    name            VARCHAR(100)    NOT NULL
);

CREATE TABLE IF NOT EXISTS staff (
    id              BIGINT         PRIMARY KEY              REFERENCES login_user(id) ON DELETE CASCADE ON UPDATE CASCADE,
    department_id   INT            NOT NULL                 REFERENCES department(id) ON DELETE CASCADE ON UPDATE CASCADE,
    job_title_id    INT            NOT NULL                 REFERENCES job_title(id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TYPE client_type as ENUM ('Regular', 'VIP', 'Bot');

CREATE TABLE IF NOT EXISTS client_type_lang (
    id                  SERIAL                  PRIMARY KEY,
    language_id         INT                     NOT NULL REFERENCES language(id) ON DELETE CASCADE ON UPDATE CASCADE,
    client_type         client_type             NOT NULL,
    client_type_name    VARCHAR(50)             NOT NULL DEFAULT 'Regular'
);

CREATE TABLE IF NOT EXISTS client (
    id              BIGINT                  PRIMARY KEY     REFERENCES login_user(id) ON DELETE CASCADE ON UPDATE CASCADE,
    client_type     client_type             NOT NULL
);

CREATE TYPE account_type as ENUM ('Distribution', 'Current', 'Card');

CREATE TABLE IF NOT EXISTS account_type_lang (
    id              SERIAL                  PRIMARY KEY,
    language_id     INT                     NOT NULL REFERENCES language(id) ON DELETE CASCADE ON UPDATE CASCADE,
    account_type    account_type            NOT NULL,
    name            VARCHAR(50)             NOT NULL DEFAULT 'Current'
);

CREATE TYPE card_account_type as ENUM ('Debit', 'Credit');

CREATE TABLE IF NOT EXISTS card_account_type_lang (
    id                  SERIAL                  PRIMARY KEY,
    language_id         INT                     NOT NULL        REFERENCES language(id) ON DELETE CASCADE ON UPDATE CASCADE,
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
    language_id     INT                     NOT NULL    REFERENCES language(id) ON DELETE CASCADE ON UPDATE CASCADE,
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
    language_id     INT                 NOT NULL                REFERENCES language(id) ON DELETE CASCADE ON UPDATE CASCADE,
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
    (1, 'en', 'English'),
    (2, 'ru', 'Русский'),
    (3, 'ua', 'Українська');

INSERT INTO status_lang (id, language_id, status, name) VALUES
    (DEFAULT, 1, 'Active', 'Active'),
    (DEFAULT, 1, 'Blocked', 'Blocked'),
    (DEFAULT, 1, 'Deleted', 'Deleted'),
    (DEFAULT, 2, 'Active', 'Активный'),
    (DEFAULT, 2, 'Blocked', 'Заблокированный'),
    (DEFAULT, 2, 'Deleted', 'Удалённый'),
    (DEFAULT, 3, 'Active', 'Активний'),
    (DEFAULT, 3, 'Blocked', 'Заблокований'),
    (DEFAULT, 3, 'Deleted', 'Видалений');

INSERT INTO department (id, name) VALUES
    (1, 'Technical Support'),
    (2, 'Customer Service'),
    (3, 'HR'),
    (4, 'Administration'),
    (5, 'Accounting');

INSERT INTO department_lang (id, language_id, department_id, name) VALUES
    (DEFAULT, 1, 1, 'Technical Support'),
    (DEFAULT, 1, 2, 'Customer Service'),
    (DEFAULT, 1, 3, 'HR'),
    (DEFAULT, 1, 4, 'Administration'),
    (DEFAULT, 1, 5, 'Accounting'),
    (DEFAULT, 1, 2, 'Customer Service'),
    (DEFAULT, 2, 1, 'Техническая поддержка'),
    (DEFAULT, 2, 2, 'Отдел обслуживания клиентов'),
    (DEFAULT, 2, 3, 'Отдел кадров'),
    (DEFAULT, 2, 4, 'Администрация'),
    (DEFAULT, 2, 5, 'Бухгалтерия'),
    (DEFAULT, 3, 1, 'Технічна підтримка'),
    (DEFAULT, 3, 2, 'Відділ обслуговування клієнтів'),
    (DEFAULT, 3, 3, 'Відділ кадрів'),
    (DEFAULT, 3, 4, 'Адміністрація'),
    (DEFAULT, 3, 5, 'Бухгалтерія');

INSERT INTO job_title (id, department_id, name) VALUES
    (1, 1, 'System Administrator'),
    (2, 2, 'Consultant'),
    (3, 2, 'Currency Exchange Manager'),
    (4, 3, 'HR'),
    (5, 3, 'Recruiter'),
    (6, 4, 'Director'),
    (7, 5, 'Accountant');

INSERT INTO job_title_lang (id, language_id, department_id, job_title_id, name) VALUES
    (DEFAULT, 1, 1, 1, 'System Administrator'),
    (DEFAULT, 1, 2, 2, 'Consultant'),
    (DEFAULT, 1, 2, 3, 'Currency Exchange Manager'),
    (DEFAULT, 1, 3, 4, 'HR'),
    (DEFAULT, 1, 3, 5, 'Recruiter'),
    (DEFAULT, 1, 4, 6, 'Director'),
    (DEFAULT, 1, 5, 7, 'Accountant'),
    (DEFAULT, 2, 1, 1, 'Админ'),
    (DEFAULT, 2, 2, 2, 'Консультант'),
    (DEFAULT, 2, 2, 3, 'Отдел кадров'),
    (DEFAULT, 2, 3, 4, 'Директор'),
    (DEFAULT, 2, 3, 5, 'Менеджер обмена валюты'),
    (DEFAULT, 2, 4, 6, 'Менеджер обмена валюты'),
    (DEFAULT, 2, 5, 7, 'Менеджер обмена валюты'),
    (DEFAULT, 3, 1, 1, 'Адмін'),
    (DEFAULT, 3, 2, 2, 'Консультант'),
    (DEFAULT, 3, 2, 3, 'Відділ кадрів'),
    (DEFAULT, 3, 3, 4, 'Директор'),
    (DEFAULT, 3, 3, 5, 'Директор'),
    (DEFAULT, 3, 4, 6, 'Директор'),
    (DEFAULT, 3, 5, 7, 'Менеджер обміну валюти');

INSERT INTO client_type_lang (id, language_id, client_type, client_type_name) VALUES
    (DEFAULT, 1, 'Regular', 'Regular'),
    (DEFAULT, 1, 'VIP', 'VIP'),
    (DEFAULT, 1, 'Bot', 'Bot'),
    (DEFAULT, 2, 'Regular', 'Обычный'),
    (DEFAULT, 2, 'VIP', 'Привилегированный'),
    (DEFAULT, 2, 'Bot', 'Bot'),
    (DEFAULT, 3, 'Regular', 'Звичайний'),
    (DEFAULT, 3, 'VIP', 'Превілізований'),
    (DEFAULT, 3, 'Bot', 'Bot');

INSERT INTO account_type_lang (id, language_id, account_type, name) VALUES
    (DEFAULT, 1, 'Distribution', 'Distribution'),
    (DEFAULT, 1, 'Current', 'Current'),
    (DEFAULT, 1, 'Card', 'Card'),
    (DEFAULT, 2, 'Distribution', 'Распределительный'),
    (DEFAULT, 2, 'Current', 'Текущий'),
    (DEFAULT, 2, 'Card', 'Карточный'),
    (DEFAULT, 3, 'Distribution', 'Розподільчий'),
    (DEFAULT, 3, 'Current', 'Поточний'),
    (DEFAULT, 3, 'Card', 'Картковий');

INSERT INTO card_account_type_lang (id, language_id, card_account_type, name) VALUES
    (DEFAULT, 1, 'Debit', 'Debit'),
    (DEFAULT, 1, 'Credit', 'Credit'),
    (DEFAULT, 2, 'Debit', 'Дебитовая'),
    (DEFAULT, 2, 'Credit', 'Кредитная'),
    (DEFAULT, 3, 'Debit', 'Дебетова'),
    (DEFAULT, 3, 'Credit', 'Кредитна');

INSERT INTO currency_type (id, short_name, name) VALUES
    (1, 'USD', 'United States Dollar'),
    (2, 'EUR', 'Euro'),
    (3, 'UAH', 'Ukrainian Hryvnia');

INSERT INTO payment_status_lang (id, language_id, payment_status, name) VALUES
    (DEFAULT, 1, 'Prepared', 'Prepared'),
    (DEFAULT, 1, 'Sent', 'Sent'),
    (DEFAULT, 2, 'Prepared', 'Подготовленный'),
    (DEFAULT, 2, 'Sent', 'Отосланный'),
    (DEFAULT, 3, 'Prepared', 'Підготовлений'),
    (DEFAULT, 3, 'Sent', 'Відісланий');

INSERT INTO action_type_lang (id, language_id, action_type, name) VALUES
    (DEFAULT, 1, 'Unblock', 'Unblocked'),
    (DEFAULT, 1, 'Done', 'Done'),
    (DEFAULT, 2, 'Unblock', 'Разблокировать'),
    (DEFAULT, 2, 'Done', 'Готово'),
    (DEFAULT, 3, 'Unblock', 'Разблокувати'),
    (DEFAULT, 3, 'Done', 'Зроблено');
