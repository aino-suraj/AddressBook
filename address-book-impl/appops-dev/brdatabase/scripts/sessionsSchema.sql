CREATE DATABASE  IF NOT EXISTS `sessions` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `sessions`;
-- MySQL dump 10.13  Distrib 8.0.22, for Linux (x86_64)
--
-- Host: localhost    Database: sessions
-- ------------------------------------------------------
-- Server version	5.7.32

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `JettySessions`
--

DROP TABLE IF EXISTS `JettySessions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `JettySessions` (
  `sessionId` varchar(120) NOT NULL,
  `contextPath` varchar(60) NOT NULL,
  `virtualHost` varchar(60) NOT NULL,
  `lastNode` varchar(60) DEFAULT NULL,
  `accessTime` bigint(20) DEFAULT NULL,
  `lastAccessTime` bigint(20) DEFAULT NULL,
  `createTime` bigint(20) DEFAULT NULL,
  `cookieTime` bigint(20) DEFAULT NULL,
  `lastSavedTime` bigint(20) DEFAULT NULL,
  `expiryTime` bigint(20) DEFAULT NULL,
  `maxInterval` bigint(20) DEFAULT NULL,
  `map` blob,
  PRIMARY KEY (`sessionId`,`contextPath`,`virtualHost`),
  KEY `idx_JettySessions_expiry` (`expiryTime`),
  KEY `idx_JettySessions_session` (`sessionId`,`contextPath`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `JettySessions`
--

LOCK TABLES `JettySessions` WRITE;
/*!40000 ALTER TABLE `JettySessions` DISABLE KEYS */;
INSERT INTO `JettySessions` VALUES ('AddressBook1dl6mfq5fcr2j569obhpixs1d0','','0.0.0.0','AddressBook',1634557472301,1634557472301,1634557472301,0,1634557472338,1634559272321,1800000,_binary '�\�\0sr\0java.lang.Integer⠤���8\0I\0valuexr\0java.lang.Number����\��\0\0xp\0\0\0\0'),('AddressBookqrab8tko0kkygysr86doc7p00','','0.0.0.0','ServiceStore',1634557837759,1634557807496,1634557658150,0,1634557837780,1634559637780,1800000,_binary '�\�\0sr\0java.lang.Integer⠤���8\0I\0valuexr\0java.lang.Number����\��\0\0xp\0\0\0\0'),('ServiceStore1ahkysj0hoglf1xj9xhknruhdk2','','0.0.0.0','ServiceStore',1634558315699,1634558070569,1634557886628,0,1634558315717,1634560115717,1800000,_binary '�\�\0sr\0java.lang.Integer⠤���8\0I\0valuexr\0java.lang.Number����\��\0\0xp\0\0\0\0'),('ServiceStore1ddk0fwzw784paed7m2sqahw21','','0.0.0.0','AddressBook',1634557354811,1634557254892,1634557146016,0,1634557355038,1634559155020,1800000,_binary '�\�\0sr\0java.lang.Integer⠤���8\0I\0valuexr\0java.lang.Number����\��\0\0xp\0\0\0\0'),('ServiceStorex6j1r9nfnscg1eyb5cahqstaw0','','0.0.0.0','AddressBook',1634556967941,1634556967446,1634556833599,0,1634556967992,1634558767992,1800000,_binary '�\�\0sr\0java.lang.Integer⠤���8\0I\0valuexr\0java.lang.Number����\��\0\0xp\0\0\0\0');
/*!40000 ALTER TABLE `JettySessions` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-10-18 17:30:11
