PRAGMA foreign_keys = ON;

DROP TABLE IF EXISTS UserAcc;
CREATE TABLE UserAcc(
    id INTEGER PRIMARY KEY,
    username TEXT NOT NULL,
    pass_hash TEXT NOT NULL,
    email TEXT NOT NULL
);

DROP TABLE IF EXISTS Map;
CREATE TABLE Map(
    id INTEGER PRIMARY KEY,
    name TEXT NOT NULL,
    owner INTEGER REFERENCES UserAcc(id),
    rating INTEGER
);

DROP TABLE IF EXISTS MapLine;
CREATE TABLE MapLine(
    id INTEGER PRIMARY KEY,
    draw TEXT NOT NULL,
    map_id INTEGER REFERENCES Map(id) ON DELETE CASCADE
);

INSERT INTO UserAcc (id,username,pass_hash,email) VALUES(1,'user1','ABC','user1@generic.pt');
INSERT INTO UserAcc (id,username,pass_hash,email) VALUES(2,'user2','ABC','user2@generic.pt');
INSERT INTO UserAcc (id,username,pass_hash,email) VALUES(3,'user3','ABC','user3@generic.pt');
INSERT INTO UserAcc (id,username,pass_hash,email) VALUES(4,'user4','ABC','user4@generic.pt');

INSERT INTO Map (id,name,owner,rating) VALUES(1,'mapa1',1,10);
INSERT INTO Map (id,name,owner,rating) VALUES(2,'mapa2',2,9);
INSERT INTO Map (id,name,owner,rating) VALUES(3,'mapa3',2,9);
INSERT INTO Map (id,name,owner,rating) VALUES(4,'mapa4',3,8);
INSERT INTO Map (id,name,owner,rating) VALUES(5,'mapa5',1,7);

INSERT INTO MapLine (id,draw,map_id) VALUES(1,'OLA',1);
INSERT INTO MapLine (id,draw,map_id) VALUES(2,'OLAA',1);
INSERT INTO MapLine (id,draw,map_id) VALUES(3,'OLAAA',2);
INSERT INTO MapLine (id,draw,map_id) VALUES(4,'OLAAAA',3);
INSERT INTO MapLine (id,draw,map_id) VALUES(5,'OLAAAAA',3);
INSERT INTO MapLine (id,draw,map_id) VALUES(6,'OLAAAAAA',4);
INSERT INTO MapLine (id,draw,map_id) VALUES(7,'OLAAAAAAA',5);

