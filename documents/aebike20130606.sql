-- MySQL dump 10.13  Distrib 5.5.24, for debian-linux-gnu (i686)
--
-- Host: localhost    Database: aebike
-- ------------------------------------------------------
-- Server version	5.5.24-0ubuntu0.12.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `achievement`
--

DROP TABLE IF EXISTS `achievement`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `achievement` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) DEFAULT NULL,
  `description` text,
  `level` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `achievement`
--

LOCK TABLES `achievement` WRITE;
/*!40000 ALTER TABLE `achievement` DISABLE KEYS */;
/*!40000 ALTER TABLE `achievement` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `achievementassignment`
--

DROP TABLE IF EXISTS `achievementassignment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `achievementassignment`
--

LOCK TABLES `achievementassignment` WRITE;
/*!40000 ALTER TABLE `achievementassignment` DISABLE KEYS */;
/*!40000 ALTER TABLE `achievementassignment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `annoucement`
--

DROP TABLE IF EXISTS `annoucement`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `annoucement` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(64) DEFAULT NULL,
  `content` text,
  `url` varchar(512) DEFAULT NULL,
  `announcer` varchar(128) DEFAULT NULL,
  `createtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `annoucement`
--

LOCK TABLES `annoucement` WRITE;
/*!40000 ALTER TABLE `annoucement` DISABLE KEYS */;
/*!40000 ALTER TABLE `annoucement` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `batterystatusemail`
--

DROP TABLE IF EXISTS `batterystatusemail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `batterystatusemail`
--

LOCK TABLES `batterystatusemail` WRITE;
/*!40000 ALTER TABLE `batterystatusemail` DISABLE KEYS */;
/*!40000 ALTER TABLE `batterystatusemail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bike`
--

DROP TABLE IF EXISTS `bike`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bike`
--

LOCK TABLES `bike` WRITE;
/*!40000 ALTER TABLE `bike` DISABLE KEYS */;
/*!40000 ALTER TABLE `bike` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bikeenquiry`
--

DROP TABLE IF EXISTS `bikeenquiry`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bikeenquiry`
--

LOCK TABLES `bikeenquiry` WRITE;
/*!40000 ALTER TABLE `bikeenquiry` DISABLE KEYS */;
/*!40000 ALTER TABLE `bikeenquiry` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chathistory`
--

DROP TABLE IF EXISTS `chathistory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chathistory`
--

LOCK TABLES `chathistory` WRITE;
/*!40000 ALTER TABLE `chathistory` DISABLE KEYS */;
/*!40000 ALTER TABLE `chathistory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `friend`
--

DROP TABLE IF EXISTS `friend`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `friend`
--

LOCK TABLES `friend` WRITE;
/*!40000 ALTER TABLE `friend` DISABLE KEYS */;
/*!40000 ALTER TABLE `friend` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invitefriend`
--

DROP TABLE IF EXISTS `invitefriend`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invitefriend`
--

LOCK TABLES `invitefriend` WRITE;
/*!40000 ALTER TABLE `invitefriend` DISABLE KEYS */;
/*!40000 ALTER TABLE `invitefriend` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `newsandcommunity`
--

DROP TABLE IF EXISTS `newsandcommunity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `newsandcommunity` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(128) DEFAULT NULL,
  `content` text,
  `createtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `location` varchar(256) DEFAULT NULL,
  `img` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `newsandcommunity`
--

LOCK TABLES `newsandcommunity` WRITE;
/*!40000 ALTER TABLE `newsandcommunity` DISABLE KEYS */;
/*!40000 ALTER TABLE `newsandcommunity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `plan`
--

DROP TABLE IF EXISTS `plan`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `plan`
--

LOCK TABLES `plan` WRITE;
/*!40000 ALTER TABLE `plan` DISABLE KEYS */;
INSERT INTO `plan` VALUES (1,21,'e','0000-00-00 00:00:00','0000-00-00 00:00:00','e','e',0,0,NULL,0,0,'','','C',NULL,'1'),(3,20,'æµ·å—','0000-00-00 00:00:00','0000-00-00 00:00:00','æµ·å—å²›æ”¯ä¸€è·¯(36043819,120218842)','å¹¿è¥¿è·¯64å·(36069379,120323504)',0,30897,NULL,0,12,'','mr','C',NULL,'1'),(4,20,'ggg','0000-00-00 00:00:00','0000-00-00 00:00:00','é›¨æ™´è½©é›¨å…·ä¸“é—¨åº—Dåº—(22526564,113380240)','Uæ´¾ç”·è£…ä¸“å–(22551324,113470349)',0,10785,NULL,0,1,'','','C',NULL,'1'),(5,20,'hhvg','0000-00-00 00:00:00','0000-00-00 00:00:00','æ°¸æ™Ÿæ–‡å…·(22518952,113376530)','O\'hagan\'s(22510948,113384202)',0,1573,NULL,0,0,'','','C',NULL,'1'),(6,20,'hhff','0000-00-00 00:00:00','0000-00-00 00:00:00','TÂ·ä¾(22542295,113470233)','é‡‘çº¢äº‘èŠ±å›­å·´åˆ©æ™“ç­‘(22366989,113445897)',0,27394,NULL,0,222,'','','C',NULL,'1');
/*!40000 ALTER TABLE `plan` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `planassignment`
--

DROP TABLE IF EXISTS `planassignment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `planassignment`
--

LOCK TABLES `planassignment` WRITE;
/*!40000 ALTER TABLE `planassignment` DISABLE KEYS */;
/*!40000 ALTER TABLE `planassignment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `plansummary`
--

DROP TABLE IF EXISTS `plansummary`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `plansummary`
--

LOCK TABLES `plansummary` WRITE;
/*!40000 ALTER TABLE `plansummary` DISABLE KEYS */;
/*!40000 ALTER TABLE `plansummary` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `shops`
--

DROP TABLE IF EXISTS `shops`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `shops`
--

LOCK TABLES `shops` WRITE;
/*!40000 ALTER TABLE `shops` DISABLE KEYS */;
/*!40000 ALTER TABLE `shops` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `useraccount`
--

DROP TABLE IF EXISTS `useraccount`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `useraccount`
--

LOCK TABLES `useraccount` WRITE;
/*!40000 ALTER TABLE `useraccount` DISABLE KEYS */;
INSERT INTO `useraccount` VALUES (20,'a','a','a',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'a'),(21,'e','e','e',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'e'),(22,'h','h','h',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'h'),(23,'123','123','123',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'123'),(24,'b','b','b',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'b'),(25,'123321','123321','123321',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'123321@QQ. com'),(26,'jldiablo','123abc','lei',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'diablojl@gmail.com'),(27,'aaa','aaaaaa','aaa',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'aaa@163.com'),(28,'aaa','aaaaaa','aaa',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'aaa@163.com'),(29,'aaa','aaaaaa','aaa',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'aaa@163.com'),(30,'aaa','aaaaaa','aaa',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'aaa@163.com'),(31,'aaa','aaaaaa','aaa',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'aaa@163.com'),(32,'aaa','aaaaaa','aaa',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'aaa@163.com'),(33,'aaa','aaaaaa','aaa',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'aaa@163.com'),(34,'aaa','aaaaaa','aaa',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'aaa@163.com');
/*!40000 ALTER TABLE `useraccount` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `userbattery`
--

DROP TABLE IF EXISTS `userbattery`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
  CONSTRAINT `userbattery_ibfk_1` FOREIGN KEY (`userid`) REFERENCES `useraccount` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `userbattery`
--

LOCK TABLES `userbattery` WRITE;
/*!40000 ALTER TABLE `userbattery` DISABLE KEYS */;
/*!40000 ALTER TABLE `userbattery` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usertravelexp`
--

DROP TABLE IF EXISTS `usertravelexp`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `usertravelexp` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userid` int(11) NOT NULL,
  `totaldistance` int(11) DEFAULT NULL,
  `totaltime` int(11) DEFAULT NULL,
  `totalbatteryused` int(11) DEFAULT NULL,
  `totalbatterytime` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `userid` (`userid`),
  CONSTRAINT `usertravelexp_ibfk_1` FOREIGN KEY (`userid`) REFERENCES `useraccount` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usertravelexp`
--

LOCK TABLES `usertravelexp` WRITE;
/*!40000 ALTER TABLE `usertravelexp` DISABLE KEYS */;
/*!40000 ALTER TABLE `usertravelexp` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-06-06  3:39:41
