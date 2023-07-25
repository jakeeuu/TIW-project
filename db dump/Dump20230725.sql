CREATE DATABASE  IF NOT EXISTS `project_db` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `project_db`;
-- MySQL dump 10.13  Distrib 8.0.32, for Win64 (x86_64)
--
-- Host: localhost    Database: project_db
-- ------------------------------------------------------
-- Server version	8.0.32

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
-- Temporary view structure for view `alldata`
--

DROP TABLE IF EXISTS `alldata`;
/*!50001 DROP VIEW IF EXISTS `alldata`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `alldata` AS SELECT 
 1 AS `ProdCode`,
 1 AS `ProdName`,
 1 AS `Description`,
 1 AS `Category`,
 1 AS `Photo`,
 1 AS `Price`,
 1 AS `SupCode`,
 1 AS `SupName`,
 1 AS `Score`,
 1 AS `MaximumN`,
 1 AS `MinimumN`,
 1 AS `Shipping`,
 1 AS `FreeShipping`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `composed`
--

DROP TABLE IF EXISTS `composed`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `composed` (
  `OrderCode` int NOT NULL,
  `ProductCode` int NOT NULL,
  `Quantity` int NOT NULL,
  PRIMARY KEY (`OrderCode`,`ProductCode`),
  KEY `ProductCode_idx` (`ProductCode`),
  KEY `OrderCode_idx` (`OrderCode`),
  CONSTRAINT `composed_ibfk_1` FOREIGN KEY (`OrderCode`) REFERENCES `orders` (`Code`),
  CONSTRAINT `ProductCode` FOREIGN KEY (`ProductCode`) REFERENCES `product` (`Code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `composed`
--

LOCK TABLES `composed` WRITE;
/*!40000 ALTER TABLE `composed` DISABLE KEYS */;
INSERT INTO `composed` VALUES (1,0,1),(2,9,1),(7,2,2),(8,3,1),(11,5,1),(12,9,3);
/*!40000 ALTER TABLE `composed` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `Code` int NOT NULL AUTO_INCREMENT,
  `MailUser` varchar(30) NOT NULL,
  `Supplier` varchar(30) NOT NULL,
  `Total` float NOT NULL,
  `Date` date NOT NULL,
  `Address` varchar(50) NOT NULL,
  `SupCode` int NOT NULL,
  PRIMARY KEY (`Code`),
  KEY `MailUser_idx` (`MailUser`),
  KEY `Supplier_idx` (`Supplier`) /*!80000 INVISIBLE */,
  KEY `Address_idx` (`Address`),
  KEY `SupCode_idx` (`SupCode`),
  CONSTRAINT `Address` FOREIGN KEY (`Address`) REFERENCES `user` (`Address`),
  CONSTRAINT `MailUser` FOREIGN KEY (`MailUser`) REFERENCES `user` (`Mail`),
  CONSTRAINT `SCode` FOREIGN KEY (`SupCode`) REFERENCES `supplier` (`Code`),
  CONSTRAINT `Supplier` FOREIGN KEY (`Supplier`) REFERENCES `supplier` (`Name`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (1,'chiara@gmail.com','Fantasia Store',25,'2023-07-15','Via Dante Alighieri',2),(2,'jack@gmail.com','Ebay',159.99,'2023-07-17','Via Eugenio Montale',1),(7,'chiara@gmail.com','Ebay',4000,'2023-07-25','Via Dante Alighieri',1),(8,'chiara@gmail.com','Star shop',168,'2023-07-25','Via Dante Alighieri',3),(11,'chiara@gmail.com','Kinder ',2,'2023-07-25','Via Dante Alighieri',14),(12,'chiara@gmail.com','Ebay',452.97,'2023-07-25','Via Dante Alighieri',1);
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product` (
  `Code` int NOT NULL AUTO_INCREMENT,
  `Name` varchar(30) NOT NULL,
  `Description` varchar(100) DEFAULT NULL,
  `Category` varchar(30) NOT NULL,
  `Photo` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`Code`)
) ENGINE=InnoDB AUTO_INCREMENT=7853 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES (0,'Manuale del giocatore','Manuale fondamentale per la creazione di personaggi di D&D','Giochi','Manuale_del_giocatore.jpg'),(1,'Spazzola','Spazzola per capelli ricci. Professionale','Igiene','Spazzola.jpeg'),(2,'Unico anello','Un anello per domarli, un anello per trovarli, un anello per ghermirli e nel buio incatenarli','Lusso','Unico_anello.jpeg'),(3,'Morte nera','Set lego della morte nera di Star wars','Giochi','Morte_nera.jpeg'),(4,'Borraccia','Borraccia da un litro , sportiva','Alimentazione','Borraccia.jpeg'),(5,'Kinder Joy Harry Potter','Fantastici funko pop di Harry Potter all\'interno','Alimentazione','Kinder_joy.jpeg'),(6,'Tostapane','Tostapane da non condividere con le coinquiline','Elettrodomestici','Tostapane.jpeg'),(7,'Pacchetto carte pokemon','Gotta Catch Em All','Giochi','Carte_Pokemon.jpeg'),(8,'Millennium Falcon ','Astronave guidata da Han Solo in Star wars','Giochi','Millennium_falcon.jpeg'),(9,'Fantastici 4 action figure','Divertiti con i tuoi eroi preferiti della Marvel','Giochi','Fantastici_4.jpeg');
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `productvisualized`
--

DROP TABLE IF EXISTS `productvisualized`;
/*!50001 DROP VIEW IF EXISTS `productvisualized`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `productvisualized` AS SELECT 
 1 AS `Mail`,
 1 AS `ProdCode`,
 1 AS `Name`,
 1 AS `Description`,
 1 AS `Category`,
 1 AS `Photo`,
 1 AS `Date`,
 1 AS `Time`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `sold_by`
--

DROP TABLE IF EXISTS `sold_by`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sold_by` (
  `ProdCode` int NOT NULL,
  `Price` float NOT NULL DEFAULT '0',
  `Supcode` int NOT NULL,
  PRIMARY KEY (`ProdCode`,`Supcode`),
  KEY `SupCode_idx` (`Supcode`),
  CONSTRAINT `ProdCode` FOREIGN KEY (`ProdCode`) REFERENCES `product` (`Code`),
  CONSTRAINT `SupCode` FOREIGN KEY (`Supcode`) REFERENCES `supplier` (`Code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sold_by`
--

LOCK TABLES `sold_by` WRITE;
/*!40000 ALTER TABLE `sold_by` DISABLE KEYS */;
INSERT INTO `sold_by` VALUES (0,25,2),(0,22,4),(0,19,5),(1,10,9),(1,15,10),(2,2000,1),(2,3500,3),(3,220,1),(3,157,2),(3,168,3),(3,215,4),(3,300,5),(3,120,16),(4,35,7),(4,55,8),(5,2,14),(5,1.65,15),(6,35,11),(6,33,12),(6,28,13),(7,4.99,2),(7,5,4),(8,350,5),(8,289,16),(9,150.99,1);
/*!40000 ALTER TABLE `sold_by` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `spending_ranges`
--

DROP TABLE IF EXISTS `spending_ranges`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `spending_ranges` (
  `SupCode` int NOT NULL,
  `Price` float NOT NULL DEFAULT '0',
  `MaximumN` int DEFAULT NULL,
  `MinimumN` int NOT NULL,
  PRIMARY KEY (`SupCode`,`Price`),
  CONSTRAINT `Code` FOREIGN KEY (`SupCode`) REFERENCES `supplier` (`Code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spending_ranges`
--

LOCK TABLES `spending_ranges` WRITE;
/*!40000 ALTER TABLE `spending_ranges` DISABLE KEYS */;
INSERT INTO `spending_ranges` VALUES (1,0,NULL,11),(1,5,10,4),(1,9,3,1),(2,0,NULL,1),(3,0,NULL,12),(3,7,11,5),(3,12,4,1),(4,0,NULL,6),(4,25,5,1),(5,0,NULL,1),(6,0,NULL,1),(7,0,NULL,11),(7,100,10,1),(8,0,NULL,5),(8,12,4,1),(9,0,NULL,3),(9,22,2,1),(10,0,NULL,1),(11,0,NULL,1),(12,0,NULL,21),(12,6,20,1),(13,0,NULL,9),(13,3,8,6),(13,5,5,1),(14,0,NULL,1),(15,0,NULL,1),(16,0,NULL,1);
/*!40000 ALTER TABLE `spending_ranges` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `supplier`
--

DROP TABLE IF EXISTS `supplier`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `supplier` (
  `Code` int NOT NULL,
  `Name` varchar(30) NOT NULL,
  `Score` int NOT NULL DEFAULT '0',
  `FreeShipping` float DEFAULT NULL,
  PRIMARY KEY (`Code`),
  KEY `Supplier` (`Name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `supplier`
--

LOCK TABLES `supplier` WRITE;
/*!40000 ALTER TABLE `supplier` DISABLE KEYS */;
INSERT INTO `supplier` VALUES (1,'Ebay',2,50),(2,'Fantasia Store',5,35),(3,'Star shop',3,100),(4,'Il covo del nerd',4,66.78),(5,'Feltrinelli',3,29.99),(6,'Decathlon',1,150),(7,'Bottles',2,55),(8,'Air up',3,79.2),(9,'Douglas',5,43.5),(10,'Sephora',1,67),(11,'Unieuro',2,80),(12,'Mediaword',4,35.99),(13,'Euronics',5,25),(14,'Kinder ',3,58),(15,'Carrefour',4,45),(16,'Amazon',4,38);
/*!40000 ALTER TABLE `supplier` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `Mail` varchar(30) NOT NULL,
  `Password` char(8) NOT NULL,
  `Name` varchar(15) NOT NULL,
  `Surname` varchar(15) NOT NULL,
  `Address` varchar(50) NOT NULL,
  PRIMARY KEY (`Mail`),
  KEY `Address` (`Address`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES ('alby@gmail.com','opq','Alberto','Cavallotti','Via Giovanni Verga'),('betti@gmail.com','002','Matteo','Bettiati','Via Publio Virgilio Marone'),('caggiano@gmail.com','004','Alessio','Caggiano','Via Cecco Angiolieri'),('chiara@gmail.com','abc','Chiara','Auriemma','Via Dante Alighieri'),('cri@gmail.com','123','Cristian','Biffi','Via Alessandro Manzoni'),('fede@gmail.com','def','Federico','Pannocchi','Via Alberto Moravia'),('filippo@gmail.com','uvz','Filippo','Balzarini','Via Luigi Pirandelloi'),('foxy@gmail.com','001','Riccardo','Caiani','Via Primo Levi'),('fra@gmail.com','rst','Francesco','Benelle','Via Ludovico Ariosto'),('gaia@gmail.com','ghi','Gaia','Giordano','Via Giovanni Boccaccio'),('gatto@gmail.com','003','Marco','Brambillasca','Via Giacomo Leopardi'),('jack@gmail.com','lmn','Giacomo','Ballabio','Via Eugenio Montale'),('matteo@gmail.com','789','Matteo','Boglioni','Via Ugo Foscolo'),('michele@gmail.com','456','Michele','Cavicchioli','Via Giuseppe Ungaretti');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `visualize`
--

DROP TABLE IF EXISTS `visualize`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `visualize` (
  `MailUser` varchar(30) NOT NULL,
  `ProdCode` int NOT NULL,
  `Date` date NOT NULL,
  `Time` time NOT NULL,
  PRIMARY KEY (`MailUser`,`ProdCode`,`Date`,`Time`),
  KEY `MailUser` (`MailUser`),
  KEY `ProdCode` (`ProdCode`),
  CONSTRAINT `visualize_ibfk_1` FOREIGN KEY (`MailUser`) REFERENCES `user` (`Mail`),
  CONSTRAINT `visualize_ibfk_2` FOREIGN KEY (`ProdCode`) REFERENCES `product` (`Code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `visualize`
--

LOCK TABLES `visualize` WRITE;
/*!40000 ALTER TABLE `visualize` DISABLE KEYS */;
INSERT INTO `visualize` VALUES ('chiara@gmail.com',0,'2023-07-24','11:54:50'),('chiara@gmail.com',1,'2023-07-24','09:00:00'),('chiara@gmail.com',2,'2023-07-25','11:30:27'),('chiara@gmail.com',3,'2023-07-25','11:35:24'),('chiara@gmail.com',4,'2023-07-25','09:52:21'),('betti@gmail.com',5,'2023-07-24','11:48:10'),('chiara@gmail.com',5,'2023-07-25','11:44:47'),('chiara@gmail.com',6,'2023-07-25','11:28:23'),('chiara@gmail.com',7,'2023-07-25','10:12:05'),('chiara@gmail.com',8,'2023-07-25','11:14:39'),('chiara@gmail.com',9,'2023-07-25','12:06:55');
/*!40000 ALTER TABLE `visualize` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Final view structure for view `alldata`
--

/*!50001 DROP VIEW IF EXISTS `alldata`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `alldata` (`ProdCode`,`ProdName`,`Description`,`Category`,`Photo`,`Price`,`SupCode`,`SupName`,`Score`,`MaximumN`,`MinimumN`,`Shipping`,`FreeShipping`) AS select `p`.`Code` AS `Code`,`p`.`Name` AS `Name`,`p`.`Description` AS `Description`,`p`.`Category` AS `Category`,`p`.`Photo` AS `Photo`,`s`.`Price` AS `Price`,`su`.`Code` AS `SupCode`,`su`.`Name` AS `Name`,`su`.`Score` AS `Score`,`r`.`MaximumN` AS `MaximumN`,`r`.`MinimumN` AS `MinimumN`,`r`.`Price` AS `Price`,`su`.`FreeShipping` AS `FreeShipping` from (((`product` `p` join `sold_by` `s` on((`p`.`Code` = `s`.`ProdCode`))) join `supplier` `su` on((`su`.`Code` = `s`.`Supcode`))) join `spending_ranges` `r` on((`r`.`SupCode` = `s`.`Supcode`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `productvisualized`
--

/*!50001 DROP VIEW IF EXISTS `productvisualized`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `productvisualized` (`Mail`,`ProdCode`,`Name`,`Description`,`Category`,`Photo`,`Date`,`Time`) AS select `user`.`Mail` AS `Mail`,`product`.`Code` AS `code`,`product`.`Name` AS `name`,`product`.`Description` AS `description`,`product`.`Category` AS `Category`,`product`.`Photo` AS `Photo`,`visualize`.`Date` AS `Date`,`visualize`.`Time` AS `Time` from ((`visualize` join `product` on((`visualize`.`ProdCode` = `product`.`Code`))) join `user` on((`visualize`.`MailUser` = `user`.`Mail`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-07-25 12:10:38
