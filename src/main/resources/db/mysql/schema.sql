DROP TABLE IF EXISTS specification;
DROP TABLE IF EXISTS advertisement;
DROP TABLE IF EXISTS stockpile;
DROP TABLE IF EXISTS payment;
DROP TABLE IF EXISTS wallet;
DROP TABLE IF EXISTS account;
DROP TABLE IF EXISTS product;

CREATE TABLE IF NOT EXISTS account
(
    id        INTEGER UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    username  VARCHAR(50),
    password  VARCHAR(100),
    name      VARCHAR(50),
    avatar    VARCHAR(100),
    telephone VARCHAR(20),
    email     VARCHAR(100),
    location  VARCHAR(100),
    INDEX (username)
) engine = InnoDB;

CREATE TABLE IF NOT EXISTS wallet
(
    id         INTEGER UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    money      DECIMAL,
    account_id INTEGER UNSIGNED,
    FOREIGN KEY (account_id) REFERENCES account (id) ON DELETE CASCADE
) engine = InnoDB;

CREATE TABLE IF NOT EXISTS product
(
    id          INTEGER UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    title       VARCHAR(50),
    price       DECIMAL,
    rate        FLOAT,
    description VARCHAR(8000),
    cover       VARCHAR(100),
    detail      VARCHAR(100),
    INDEX (title)
) engine = InnoDB;

CREATE TABLE IF NOT EXISTS stockpile
(
    id         INTEGER UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    amount     INTEGER,
    frozen     INTEGER,
    product_id INTEGER UNSIGNED,
    FOREIGN KEY (product_id) REFERENCES product (id) ON DELETE CASCADE
) engine = InnoDB;

CREATE TABLE IF NOT EXISTS specification
(
    id         INTEGER UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    item       VARCHAR(50),
    value      VARCHAR(100),
    product_id INTEGER UNSIGNED,
    FOREIGN KEY (product_id) REFERENCES product (id) ON DELETE CASCADE
) engine = InnoDB;

CREATE TABLE IF NOT EXISTS advertisement
(
    id         INTEGER UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    image      VARCHAR(100),
    product_id INTEGER UNSIGNED,
    FOREIGN KEY (product_id) REFERENCES product (id) ON DELETE CASCADE
) engine = InnoDB;

CREATE TABLE IF NOT EXISTS payment
(
    id           INTEGER UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    pay_id       VARCHAR(100),
    create_time  DATETIME,
    total_price  DECIMAL,
    expires      INTEGER          NOT NULL,
    payment_link VARCHAR(300),
    pay_state    VARCHAR(20)
) engine = InnoDB;
