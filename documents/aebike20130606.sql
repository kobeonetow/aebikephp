-- MySQL dump 10.13  Distrib 5.5.24, for debian-linux-gnu (i686)
--
-- Host: localhost    Database: aebike
-- ------------------------------------------------------
-- Server version	5.5.24-0ubuntu0.12.04.1
set character set utf8;

--
-- Table structure for table `achievement`
--

DROP TABLE IF EXISTS `achievement`;
CREATE TABLE `achievement` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) DEFAULT NULL,
  `description` text,
  `level` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ;

--
-- Table structure for table `achievementassignment`
--

DROP TABLE IF EXISTS `achievementassignment`;
CREATE TABLE `achievementassignment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userid` int(11) DEFAULT NULL,
  `planid` int(11) NOT NULL,
  `achievementid` int(11) NOT NULL,
  `status` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `userid` (`userid`),
  KEY `planid` (`planid`),
  KEY `achievementid` (`achievementid`),
  CONSTRAINT `achievementassignment_ibfk_1` FOREIGN KEY (`userid`) REFERENCES `useraccount` (`id`),
  CONSTRAINT `achievementassignment_ibfk_2` FOREIGN KEY (`planid`) REFERENCES `plan` (`id`),
  CONSTRAINT `achievementassignment_ibfk_3` FOREIGN KEY (`achievementid`) REFERENCES `achievement` (`id`)
);

--
-- Table structure for table `annoucement`
--

DROP TABLE IF EXISTS `annoucement`;
CREATE TABLE `annoucement` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(64) DEFAULT NULL,
  `content` text,
  `url` varchar(512) DEFAULT NULL,
  `announcer` varchar(128) DEFAULT NULL,
  `createtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ;

--
-- Table structure for table `batterystatusemail`
--

DROP TABLE IF EXISTS `batterystatusemail`;
CREATE TABLE `batterystatusemail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userid` int(11) DEFAULT NULL,
  `title` varchar(256) DEFAULT NULL,
  `content` text,
  `senttime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `response` text,
  `status` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `userid` (`userid`),
  CONSTRAINT `batterystatusemail_ibfk_1` FOREIGN KEY (`userid`) REFERENCES `useraccount` (`id`)
) ;

--
-- Table structure for table `bike`
--

DROP TABLE IF EXISTS `bike`;
CREATE TABLE `bike` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) DEFAULT NULL,
  `model` varchar(64) DEFAULT NULL,
  `properties` varchar(512) DEFAULT NULL,
  `price` float DEFAULT NULL,
  `height` float DEFAULT NULL,
  `weight` float DEFAULT NULL,
  `wheelsize` float DEFAULT NULL,
  `speed` float DEFAULT NULL,
  `speedchangable` char(1) DEFAULT NULL,
  `createtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ;

--
-- Table structure for table `bikeenquiry`
--

DROP TABLE IF EXISTS `bikeenquiry`;
CREATE TABLE `bikeenquiry` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userid` int(11) DEFAULT NULL,
  `bikeid` int(11) DEFAULT NULL,
  `message` text,
  `replymessage` text,
  `createtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `userid` (`userid`),
  KEY `bikeid` (`bikeid`),
  CONSTRAINT `bikeenquiry_ibfk_1` FOREIGN KEY (`userid`) REFERENCES `useraccount` (`id`),
  CONSTRAINT `bikeenquiry_ibfk_2` FOREIGN KEY (`bikeid`) REFERENCES `bike` (`id`)
) ;

--
-- Table structure for table `chathistory`
--

DROP TABLE IF EXISTS `chathistory`;
CREATE TABLE `chathistory` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userid` int(11) DEFAULT NULL,
  `receiverid` int(11) DEFAULT NULL,
  `message` varchar(256) DEFAULT NULL,
  `status` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `userid` (`userid`),
  KEY `receiverid` (`receiverid`),
  CONSTRAINT `chathistory_ibfk_1` FOREIGN KEY (`userid`) REFERENCES `useraccount` (`id`),
  CONSTRAINT `chathistory_ibfk_2` FOREIGN KEY (`receiverid`) REFERENCES `useraccount` (`id`)
) ;

--
-- Table structure for table `friend`
--

DROP TABLE IF EXISTS `friend`;
CREATE TABLE `friend` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userid` int(11) DEFAULT NULL,
  `friendid` int(11) DEFAULT NULL,
  `status` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `userid` (`userid`),
  KEY `friendid` (`friendid`),
  CONSTRAINT `friend_ibfk_1` FOREIGN KEY (`userid`) REFERENCES `useraccount` (`id`),
  CONSTRAINT `friend_ibfk_2` FOREIGN KEY (`friendid`) REFERENCES `useraccount` (`id`)
) ;

--
-- Table structure for table `invitefriend`
--

DROP TABLE IF EXISTS `invitefriend`;
CREATE TABLE `invitefriend` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userid` int(11) NOT NULL,
  `friendid` int(11) NOT NULL,
  `planid` int(11) NOT NULL,
  `status` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `userid` (`userid`),
  KEY `friendid` (`friendid`),
  KEY `fk_planid` (`planid`),
  CONSTRAINT `fk_planid` FOREIGN KEY (`planid`) REFERENCES `plan` (`id`),
  CONSTRAINT `invitefriend_ibfk_1` FOREIGN KEY (`userid`) REFERENCES `useraccount` (`id`),
  CONSTRAINT `invitefriend_ibfk_2` FOREIGN KEY (`friendid`) REFERENCES `useraccount` (`id`)
) DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Table structure for table `newsandcommunity`
--

DROP TABLE IF EXISTS `newsandcommunity`;
CREATE TABLE `newsandcommunity` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(128) DEFAULT NULL,
  `content` text,
  `createtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `location` varchar(256) DEFAULT NULL,
  `img` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ;

--
-- Table structure for table `plan`
--

DROP TABLE IF EXISTS `plan`;
CREATE TABLE `plan` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userid` int(11) NOT NULL,
  `name` varchar(64) DEFAULT NULL,
  `starttime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `endtime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `startlocation` varchar(128) DEFAULT NULL,
  `endlocation` varchar(128) DEFAULT NULL,
  `expecttime` int(11) DEFAULT NULL,
  `distance` int(11) DEFAULT NULL,
  `pplinterested` smallint(6) DEFAULT NULL,
  `pplgoing` smallint(6) DEFAULT NULL,
  `pplexpected` smallint(6) DEFAULT NULL,
  `description` text,
  `sponsor` varchar(64) DEFAULT NULL,
  `type` varchar(1) DEFAULT NULL,
  `achievementid` int(11) DEFAULT NULL,
  `status` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `achievementid` (`achievementid`),
  KEY `fk_userid` (`userid`),
  CONSTRAINT `fk_userid` FOREIGN KEY (`userid`) REFERENCES `useraccount` (`id`),
  CONSTRAINT `plan_ibfk_1` FOREIGN KEY (`achievementid`) REFERENCES `achievement` (`id`)
) AUTO_INCREMENT=7 ;

--
-- Table structure for table `planassignment`
--

DROP TABLE IF EXISTS `planassignment`;
CREATE TABLE `planassignment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userid` int(11) DEFAULT NULL,
  `planid` int(11) NOT NULL,
  `starttime` timestamp NULL DEFAULT NULL,
  `endtime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  `status` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `userid` (`userid`),
  KEY `planid` (`planid`),
  CONSTRAINT `planassignment_ibfk_1` FOREIGN KEY (`userid`) REFERENCES `useraccount` (`id`),
  CONSTRAINT `planassignment_ibfk_2` FOREIGN KEY (`planid`) REFERENCES `plan` (`id`)
) ;

--
-- Table structure for table `plansummary`
--

DROP TABLE IF EXISTS `plansummary`;
CREATE TABLE `plansummary` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userid` int(11) DEFAULT NULL,
  `planid` int(11) DEFAULT NULL,
  `timecost` int(11) DEFAULT NULL,
  `distancebybattery` int(11) DEFAULT NULL,
  `distancetravelled` int(11) DEFAULT NULL,
  `batteryusage` int(11) DEFAULT NULL,
  `batterytimeon` int(11) DEFAULT NULL,
  `remarks` varchar(256) DEFAULT NULL,
  `status` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `userid` (`userid`),
  KEY `planid` (`planid`),
  CONSTRAINT `plansummary_ibfk_1` FOREIGN KEY (`userid`) REFERENCES `useraccount` (`id`),
  CONSTRAINT `plansummary_ibfk_2` FOREIGN KEY (`planid`) REFERENCES `plan` (`id`)
) ;

--
-- Table structure for table `shops`
--

DROP TABLE IF EXISTS `shops`;
CREATE TABLE `shops` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) DEFAULT NULL,
  `type` varchar(64) DEFAULT NULL,
  `location` varchar(128) DEFAULT NULL,
  `city` varchar(16) DEFAULT NULL,
  `province` varchar(16) DEFAULT NULL,
  `postcode` varchar(10) DEFAULT NULL,
  `status` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ;

--
-- Table structure for table `useraccount`
--

DROP TABLE IF EXISTS `useraccount`;
CREATE TABLE `useraccount` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(256) NOT NULL,
  `password` varchar(128) DEFAULT NULL,
  `name` varchar(32) NOT NULL,
  `status` varchar(2) DEFAULT NULL,
  `onlinestatus` varchar(1) DEFAULT NULL,
  `phonenumber` varchar(16) DEFAULT NULL,
  `addressline` varchar(512) DEFAULT NULL,
  `city` varchar(16) DEFAULT NULL,
  `province` varchar(16) DEFAULT NULL,
  `postcode` varchar(10) DEFAULT NULL,
  `age` tinyint(4) DEFAULT NULL,
  `sex` char(1) DEFAULT NULL,
  `weight` float DEFAULT NULL,
  `height` float DEFAULT NULL,
  `emailaddress` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`)
) AUTO_INCREMENT=35 ;

--
-- Table structure for table `userbattery`
--

DROP TABLE IF EXISTS `userbattery`;
CREATE TABLE `userbattery` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userid` int(11) NOT NULL,
  `bicyclemodel` varchar(64) DEFAULT NULL,
  `batteryaddress` varchar(64) DEFAULT NULL,
  `bicycleaddress` varchar(64) DEFAULT NULL,
  `batterystatus` varchar(2) DEFAULT NULL,
  `batteryerrors` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `userid` (`userid`),
  FOREIGN KEY (`userid`) REFERENCES `useraccount` (`id`)
) ;

--
-- Table structure for table `usertravelexp`
--

DROP TABLE IF EXISTS `usertravelexp`;
CREATE TABLE `usertravelexp` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userid` int(11) NOT NULL,
  `totaldistance` int(11) DEFAULT NULL,
  `totaltime` int(11) DEFAULT NULL,
  `totalbatteryused` int(11) DEFAULT NULL,
  `totalbatterytime` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `userid` (`userid`),
  FOREIGN KEY (`userid`) REFERENCES `useraccount` (`id`)
) ;