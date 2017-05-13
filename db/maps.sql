PRAGMA foreign_keys = ON;

DROP TABLE IF EXISTS UserAcc;
DROP TABLE IF EXISTS Map;
DROP TABLE IF EXISTS MapLine;

CREATE TABLE UserAcc (

    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT NOT NULL,
    pass_hash TEXT NOT NULL,
    email TEXT NOT NULL,

);

CREATE TABLE Map (

    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    owner INTEGER REFERENCES UserAcc(id),
    rating INTEGER

);

CREATE TABLE MapLine(

    id INTEGER PRIMARY KEY AUTOINCREMENT,
    draw TEXT NOT NULL,
    map_id INTEGER REFERENCES Map(id) ON DELETE CASCADE

);
