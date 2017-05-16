PRAGMA foreign_keys = ON;

DROP TABLE IF EXISTS UserAcc;
CREATE TABLE UserAcc(
    id INTEGER PRIMARY KEY,
    username TEXT UNIQUE NOT NULL,
    pass_hash TEXT NOT NULL
);

DROP TABLE IF EXISTS Map;
CREATE TABLE Map(
    id INTEGER PRIMARY KEY,
    name TEXT UNIQUE NOT NULL,
    owner INTEGER REFERENCES UserAcc(id),
    startlat REAL NOT NULL,
    startlng REAL NOT NULL,
    finishlat REAL NOT NULL,
    finishlng REAL NOT NULL,
    rating INTEGER
);

DROP TABLE IF EXISTS MapLine;
CREATE TABLE MapLine(
    id INTEGER PRIMARY KEY,
    draw TEXT NOT NULL,
    map_id INTEGER REFERENCES Map(id) ON DELETE CASCADE
);

INSERT INTO UserAcc (id,username,pass_hash) VALUES(1,'user1','ABC');
INSERT INTO UserAcc (id,username,pass_hash) VALUES(2,'user2','ABC');
INSERT INTO UserAcc (id,username,pass_hash) VALUES(3,'user3','ABC');
INSERT INTO UserAcc (id,username,pass_hash) VALUES(4,'user4','ABC');

INSERT INTO Map (id,name,owner,startlat,startlng,finishlat,finishlng,rating) VALUES(1,'mapa1',1,1.123456,1.345678,1.123456,1.345678,10);
INSERT INTO Map (id,name,owner,startlat,startlng,finishlat,finishlng,rating) VALUES(2,'mapa2',2,1.123457,1.345679,1.123456,1.345678,9);
INSERT INTO Map (id,name,owner,startlat,startlng,finishlat,finishlng,rating) VALUES(3,'mapa3',2,1.123458,1.345681,1.123456,1.345678,9);
INSERT INTO Map (id,name,owner,startlat,startlng,finishlat,finishlng,rating) VALUES(4,'mapa4',3,1.123459,1.345682,1.123456,1.345678,8);
INSERT INTO Map (id,name,owner,startlat,startlng,finishlat,finishlng,rating) VALUES(5,'mapa5',1,1.123411,1.345683,1.123456,1.345678,7);

INSERT INTO MapLine (id,draw,map_id) VALUES(1,'OLA',1);
INSERT INTO MapLine (id,draw,map_id) VALUES(2,'OLAA',1);
INSERT INTO MapLine (id,draw,map_id) VALUES(3,'OLAAA',2);
INSERT INTO MapLine (id,draw,map_id) VALUES(4,'OLAAAA',3);
INSERT INTO MapLine (id,draw,map_id) VALUES(5,'OLAAAAA',3);
INSERT INTO MapLine (id,draw,map_id) VALUES(6,'OLAAAAAA',4);
INSERT INTO MapLine (id,draw,map_id) VALUES(7,'OLAAAAAAA',5);

