CREATE TABLE currency (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        code VARCHAR,
        full_name VARCHAR,
        sign VARCHAR );
		
CREATE UNIQUE INDEX code_index ON currency(code);

CREATE TABLE ExchangeRate (
	id INTEGER PRIMARY KEY AUTOINCREMENT,
	base_currency_id INTEGER REFERENCES currency (id),
	target_currency_id INTEGER REFERENCES currency (id),
	rate DECIMAL(6),
	FOREIGN KEY (base_currency_id) REFERENCES currencies (id),
	FOREIGN KEY (target_currency_id)  REFERENCES currencies (id) );