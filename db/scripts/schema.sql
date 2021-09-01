CREATE TABLE IF NOT EXISTS items (
    ID SERIAL PRIMARY KEY,
    DESCRIPTION TEXT,
    CREATED TIMASTAMP,
    DONE BOOLEAN,
    ITEMID INT NOT NULL REFERENCES users(ID)
);

CREATE TABLE IF NOT EXISTS users (
    ID SERIAL PRIMARY KEY,
    NAME VARCHAR(255),
    PHONE VARCHAR(255)
)