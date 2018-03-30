CREATE TABLE User (
id INTEGER NOT NULL,
name VARCHAR(250),
PRIMARY KEY (id)
);
create sequence USER_SEQUENCE;

CREATE TABLE Account (
id INTEGER NOT NULL,
user_id INTEGER,
number VARCHAR(20),
PRIMARY KEY (id),
CONSTRAINT accont_fkey FOREIGN KEY (user_id) REFERENCES User(id)
);
create sequence ACCOUNT_SEQUENCE;

CREATE TABLE Payment (
id INTEGER NOT NULL,
account_id INTEGER,
amount NUMERIC(15,2),
PRIMARY KEY (id),
CONSTRAINT payment_fkey FOREIGN KEY (account_id) REFERENCES Account(id)
);
create sequence PAYMENT_SEQUENCE;

INSERT INTO User (id, name) VALUES (nextval('USER_SEQUENCE'), 'Foo');
INSERT INTO User (id, name) VALUES (nextval('USER_SEQUENCE'), 'Bar');

INSERT INTO Account (id, user_id, number) VALUES (nextval('ACCOUNT_SEQUENCE'), 1, '42001');
INSERT INTO Account (id, user_id, number) VALUES (nextval('ACCOUNT_SEQUENCE'), 2, '42002');
