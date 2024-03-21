
CREATE TABLE IF NOT EXISTS socks (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    color VARCHAR(64),
    cotton_part INT CHECK (cotton_part <= 100 AND cotton_part > 0)
);

CREATE TABLE IF NOT EXISTS warehouse (
    id BIGSERIAL PRIMARY KEY,
    total INT default 0,
    socks_id BIGINT REFERENCES socks(id) NOT NULL
);

CREATE TABLE IF NOT EXISTS employee (
    id SERIAL PRIMARY KEY,
    name VARCHAR(64) NOT NULL,
    role VARCHAR(64)
);

CREATE TABLE IF NOT EXISTS income (
    id BIGSERIAL PRIMARY KEY,
    created TIMESTAMP,
    employee_id INT REFERENCES employee(id) NOT NULL
);

CREATE TABLE IF NOT EXISTS outcome (
    id BIGSERIAL PRIMARY KEY,
    created TIMESTAMP,
    employee_id INT REFERENCES employee(id) NOT NULL
);

CREATE TABLE IF NOT EXISTS socks_income (
    id BIGSERIAL PRIMARY KEY,
    quantity INT default 0,
    socks_id BIGINT REFERENCES socks(id) NOT NULL,
    income_id BIGINT REFERENCES income(id) NOT NULL
);

CREATE TABLE IF NOT EXISTS socks_outcome (
     id BIGSERIAL PRIMARY KEY,
     quantity INT default 0,
     socks_id BIGINT REFERENCES socks(id) NOT NULL,
     outcome_id BIGINT REFERENCES outcome(id) NOT NULL
);