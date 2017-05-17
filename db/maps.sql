PRAGMA foreign_keys = ON;

DROP TABLE IF EXISTS UserAcc;
CREATE TABLE UserAcc(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT UNIQUE NOT NULL,
    pass_hash TEXT NOT NULL
);

DROP TABLE IF EXISTS Map;
CREATE TABLE Map(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT UNIQUE NOT NULL,
    owner INTEGER REFERENCES UserAcc(id),
    startlat REAL NOT NULL,
    startlng REAL NOT NULL,
    finishlat REAL NOT NULL,
    finishlng REAL NOT NULL
);

DROP TABLE IF EXISTS MapLine;
CREATE TABLE MapLine(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    draw TEXT NOT NULL,
    map_id INTEGER REFERENCES Map(id) ON DELETE CASCADE
);


INSERT INTO UserAcc (username,pass_hash) VALUES('user1','ABC');
INSERT INTO UserAcc (username,pass_hash) VALUES('user2','ABC');
INSERT INTO UserAcc (username,pass_hash) VALUES('user3','ABC');
INSERT INTO UserAcc (username,pass_hash) VALUES('user4','ABC');

INSERT INTO Map (name,owner,startlat,startlng,finishlat,finishlng) VALUES('mapa1',1,1.123456,1.345678,1.123456,1.345678);
INSERT INTO Map (name,owner,startlat,startlng,finishlat,finishlng) VALUES('mapa2',2,1.123457,1.345679,1.123456,1.345678);
INSERT INTO Map (name,owner,startlat,startlng,finishlat,finishlng) VALUES('mapa3',2,1.123458,1.345681,1.123456,1.345678);
INSERT INTO Map (name,owner,startlat,startlng,finishlat,finishlng) VALUES('mapa4',3,1.123459,1.345682,1.123456,1.345678);
INSERT INTO Map (name,owner,startlat,startlng,finishlat,finishlng) VALUES('mapa5',1,1.123411,1.345683,1.123456,1.345678);

INSERT INTO MapLine (draw,map_id) VALUES('OLA',1);
INSERT INTO MapLine (draw,map_id) VALUES('OLAA',1);
INSERT INTO MapLine (draw,map_id) VALUES('OLAAA',2);
INSERT INTO MapLine (draw,map_id) VALUES('OLAAAA',3);
INSERT INTO MapLine (draw,map_id) VALUES('OLAAAAA',3);
INSERT INTO MapLine (draw,map_id) VALUES('OLAAAAAA',4);
INSERT INTO MapLine (draw,map_id) VALUES('OLAAAAAAA',5);

