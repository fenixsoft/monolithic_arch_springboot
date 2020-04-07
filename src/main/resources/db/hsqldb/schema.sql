DROP TABLE wallet IF EXISTS;
DROP TABLE account IF EXISTS;
DROP TABLE specification IF EXISTS;
DROP TABLE advertisement IF EXISTS;
DROP TABLE stockpile IF EXISTS;
DROP TABLE product IF EXISTS;
DROP TABLE payment IF EXISTS;

CREATE TABLE account
(
    id        INTEGER IDENTITY PRIMARY KEY,
    username  VARCHAR(50),
    password  VARCHAR(100),
    name      VARCHAR(50),
    avatar    VARCHAR(100),
    telephone VARCHAR(20),
    email     VARCHAR(100),
    location  VARCHAR(100)
);
CREATE UNIQUE INDEX account_user ON account (username);
CREATE UNIQUE INDEX account_telephone ON account (telephone);
CREATE UNIQUE INDEX account_email ON account (email);

CREATE TABLE wallet
(
    id         INTEGER IDENTITY PRIMARY KEY,
    money      DECIMAL,
    account_id INTEGER
);
ALTER TABLE wallet
    ADD CONSTRAINT fk_wallet_account FOREIGN KEY (account_id) REFERENCES account (id) ON DELETE CASCADE;

CREATE TABLE product
(
    id          INTEGER IDENTITY PRIMARY KEY,
    title       VARCHAR(50),
    price       DECIMAL,
    rate        FLOAT,
    description VARCHAR(8000),
    cover       VARCHAR(100),
    detail      VARCHAR(100)
);
CREATE INDEX product_title ON product (title);

CREATE TABLE stockpile
(
    id         INTEGER IDENTITY PRIMARY KEY,
    amount     INTEGER,
    frozen     INTEGER,
    product_id INTEGER
);
ALTER TABLE stockpile
    ADD CONSTRAINT fk_stockpile_product FOREIGN KEY (product_id) REFERENCES product (id) ON DELETE CASCADE;

CREATE TABLE specification
(
    id         INTEGER IDENTITY PRIMARY KEY,
    item       VARCHAR(50),
    value      VARCHAR(100),
    product_id INTEGER
);
ALTER TABLE specification
    ADD CONSTRAINT fk_specification_product FOREIGN KEY (product_id) REFERENCES product (id) ON DELETE CASCADE;

CREATE TABLE advertisement
(
    id         INTEGER IDENTITY PRIMARY KEY,
    image      VARCHAR(100),
    product_id INTEGER
);
ALTER TABLE advertisement
    ADD CONSTRAINT fk_advertisement_product FOREIGN KEY (product_id) REFERENCES product (id) ON DELETE CASCADE;

CREATE TABLE payment
(
    id           INTEGER IDENTITY PRIMARY KEY,
    pay_id       VARCHAR(100),
    create_time  DATETIME,
    total_price  DECIMAL,
    expires      INTEGER NOT NULL,
    payment_link VARCHAR(300),
    pay_state    VARCHAR(20)
);
