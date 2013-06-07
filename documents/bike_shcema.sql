DROP DATABASE IF EXISTS aebike;
CREATE DATABASE aebike;
use aebike;

DROP TABLE IF EXISTS useraccount;
CREATE TABLE useraccount (
id INT NOT NULL PRIMARY KEY,
username VARCHAR(32) NOT NULL,
password VARCHAR(32) NOT NULL,
status VARCHAR(2),
onlinestatus VARCHAR(1),
phonenumber VARCHAR(16),
addressline VARCHAR(512),
city VARCHAR(16),
province VARCHAR(16),
postcode VARCHAR(10),
age TINYINT,
sex CHAR(1),
weight FLOAT,
height FLOAT,
emailaddress VARCHAR(128)
);

DROP TABLE IF EXISTS userbattery;
CREATE TABLE userbattery (
id INT NOT NULL PRIMARY KEY,
userid INT NOT NULL,
bicyclemodule VARCHAR(64),
batteryaddress VARCHAR(64),
bicycleaddress VARCHAR(64),
batterystatus VARCHAR(2),
batteryerrors INT,
FOREIGN KEY (userid) REFERENCES useraccount(id)
);

DROP TABLE IF EXISTS usertravelexp;
CREATE TABLE usertravelexp (
id INT NOT NULL PRIMARY KEY,
userid INT NOT NULL,
totaldistance INT,
totaltime INT,
totalbatteryused INT,
totalbatterytime INT,
FOREIGN KEY (userid) REFERENCES useraccount(id)
);

DROP TABLE IF EXISTS achievement;
CREATE TABLE achievement (
id INT PRIMARY KEY,
name VARCHAR(64),
description TEXT,
level TINYINT
);

DROP TABLE IF EXISTS plan;
CREATE TABLE plan (
id INT PRIMARY KEY,
name VARCHAR(64),
starttime TIMESTAMP,
endtime TIMESTAMP,
startlocation VARCHAR(128),
endlocation VARCHAR(128),
expecttime INT,
distance INT,
pplinterested SMALLINT,
pplgoing SMALLINT,
pplexpected SMALLINT,
description TEXT,
sponsor VARCHAR(64),
type VARCHAR(1),
achievementid INT,
status VARCHAR(1),
FOREIGN KEY (achievementid) REFERENCES achievement(id)
);

DROP TABLE IF EXISTS plansummary;
CREATE TABLE plansummary(
id INT PRIMARY KEY,
userid INT,
planid INT,
tmiecost INT,
distancebybattery INT,
distancetravelled INT,
batteryusage INT,
batterytimeon INT,
remarks VARCHAR(256),
status VARCHAR(1),
FOREIGN KEY (userid) REFERENCES useraccount(id),
FOREIGN KEY (planid) REFERENCES plan(id)
);

DROP TABLE IF EXISTS batterystatusemail;
CREATE TABLE batterystatusemail (
id INT PRIMARY KEY,
userid INT,
title VARCHAR(256),
content TEXT,
senttime TIMESTAMP,
response TEXT,
status VARCHAR(1),
FOREIGN KEY (userid) REFERENCES useraccount(id)
);

DROP TABLE IF EXISTS bike;
CREATE TABLE bike (
id INT PRIMARY KEY,
name VARCHAR(64),
model VARCHAR(64),
properties VARCHAR(512),
price FLOAT,
hieght FLOAT,
weight FLOAT,
wheelsize FLOAT,
speed FLOAT,
speedchangable CHAR(1),
createtime TIMESTAMP default current_timestamp
);

DROP TABLE IF EXISTS bikeenquiry;
CREATE TABLE bikeenquiry (
id INT PRIMARY KEY,
userid INT,
bikeid INT,
message TEXT,
replymessage TEXT,
createtime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
FOREIGN KEY (userid) REFERENCES useraccount(id),
FOREIGN KEY (bikeid) REFERENCES bike(id)
);

DROP TABLE IF EXISTS friend;
CREATE TABLE friend (
id INT PRIMARY KEY,
userid INT,
friendid INT,
status VARCHAR(1),
FOREIGN KEY (userid) REFERENCES useraccount(id),
FOREIGN KEY (friendid) REFERENCES useraccount(id)
);

DROP TABLE IF EXISTS chathistory;
CREATE TABLE chathistory (
id INT PRIMARY KEY,
userid INT,
receiverid INT,
message VARCHAR(256),
status VARCHAR(1),
FOREIGN KEY (userid) REFERENCES useraccount(id),
FOREIGN KEY (receiverid) REFERENCES useraccount(id)
);

DROP TABLE IF EXISTS annoucement;
CREATE TABLE annoucement (
id INT PRIMARY KEY,
title VARCHAR(64),
content TEXT,
url VARCHAR(512),
annoucer VARCHAR(128),
createtime TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

DROP TABLE IF EXISTS newsandcommunity;
CREATE TABLE newsandcommunity (
id INT PRIMARY KEY,
title VARCHAR(128),
content TEXT,
createtime TIMESTAMP default current_timestamp,
location VARCHAR(256),
img VARCHAR(512)
);

DROP TABLE IF EXISTS shops;
CREATE TABLE shops (
id INT PRIMARY KEY,
name VARCHAR(64),
type VARCHAR(64),
location VARCHAR(128),
city VARCHAR(16),
province VARCHAR(16),
postcode VARCHAR(10),
status VARCHAR(1)
);

