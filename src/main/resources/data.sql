DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS current_accounts;

CREATE TABLE current_accounts (
	id INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
	account_no VARCHAR(24) NOT NULL UNIQUE,
	status VARCHAR(255) NOT NULL,
	currency VARCHAR(3) NOT NULL,
	balance DOUBLE NOT NULL DEFAULT 0.0,
	date_created DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	date_modified DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO current_accounts (account_no, status, currency, balance, date_created, date_modified)
VALUES ('RO09BCYP0000001234567890', 'OPEN', 'RON', 1500.0, NOW(), NOW());

CREATE TABLE transactions (
	id INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
	description VARCHAR(255) DEFAULT NULL,
	debit DOUBLE DEFAULT NULL,
	credit DOUBLE DEFAULT NULL,
	balance DOUBLE DEFAULT 0,
	date_created DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	deleted TINYINT(1) DEFAULT 0,
	id_current_account INT(11) NOT NULL,
	
	CONSTRAINT fk_transaction_current_account 
	FOREIGN KEY (id_current_account)
	REFERENCES current_accounts (id)
	ON DELETE NO ACTION
	ON UPDATE NO ACTION
);

INSERT INTO transactions (description, debit, credit, balance, date_created, id_current_account)
VALUES ('Test transaction 1', -100.0, NULL, 1000, '2019-12-01 12:00', 1);