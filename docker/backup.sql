-- MySQL dump 10.13  Distrib 8.0.42, for Linux (x86_64)
--
-- Host: localhost    Database: mf_db
-- ------------------------------------------------------
-- Server version	8.0.42

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `ADMINS`
--

DROP TABLE IF EXISTS `ADMINS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ADMINS` (
  `fk_user` int NOT NULL,
  `id` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKhp41pq62xgvtc2ybhg0ogn9k2` (`fk_user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ADMINS`
--

LOCK TABLES `ADMINS` WRITE;
/*!40000 ALTER TABLE `ADMINS` DISABLE KEYS */;
INSERT INTO `ADMINS` VALUES (3,1),(52,2);
/*!40000 ALTER TABLE `ADMINS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ADMINS_SEQ`
--

DROP TABLE IF EXISTS `ADMINS_SEQ`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ADMINS_SEQ` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ADMINS_SEQ`
--

LOCK TABLES `ADMINS_SEQ` WRITE;
/*!40000 ALTER TABLE `ADMINS_SEQ` DISABLE KEYS */;
INSERT INTO `ADMINS_SEQ` VALUES (101);
/*!40000 ALTER TABLE `ADMINS_SEQ` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ASSISTANCE`
--

DROP TABLE IF EXISTS `ASSISTANCE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ASSISTANCE` (
  `fk_student` int NOT NULL,
  `fk_training_session` int NOT NULL,
  `id` int NOT NULL,
  `date_time` datetime(6) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ASSISTANCE`
--

LOCK TABLES `ASSISTANCE` WRITE;
/*!40000 ALTER TABLE `ASSISTANCE` DISABLE KEYS */;
INSERT INTO `ASSISTANCE` VALUES (1,1,1,'2025-05-12 20:58:14.952666'),(1,1,2,'2025-05-13 20:20:46.581000');
/*!40000 ALTER TABLE `ASSISTANCE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ASSISTANCE_SEQ`
--

DROP TABLE IF EXISTS `ASSISTANCE_SEQ`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ASSISTANCE_SEQ` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ASSISTANCE_SEQ`
--

LOCK TABLES `ASSISTANCE_SEQ` WRITE;
/*!40000 ALTER TABLE `ASSISTANCE_SEQ` DISABLE KEYS */;
INSERT INTO `ASSISTANCE_SEQ` VALUES (151);
/*!40000 ALTER TABLE `ASSISTANCE_SEQ` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `INVOICES`
--

DROP TABLE IF EXISTS `INVOICES`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `INVOICES` (
  `fk_user` int NOT NULL,
  `id` int NOT NULL,
  `total` decimal(38,2) NOT NULL,
  `date` datetime(6) NOT NULL,
  `status` enum('ACTIVE','DELETED','INACTIVE','NOT_PAID','NOT_SENT','PAID','PENDING','SENT','SUSPENDED') NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `INVOICES`
--

LOCK TABLES `INVOICES` WRITE;
/*!40000 ALTER TABLE `INVOICES` DISABLE KEYS */;
INSERT INTO `INVOICES` VALUES (1,1,50.00,'2025-05-11 20:58:14.971163','PAID'),(102,402,50.00,'2025-05-15 16:15:00.000000','PAID'),(1,652,121.00,'2025-05-15 19:01:00.000000','PAID');
/*!40000 ALTER TABLE `INVOICES` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `INVOICES_SEQ`
--

DROP TABLE IF EXISTS `INVOICES_SEQ`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `INVOICES_SEQ` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `INVOICES_SEQ`
--

LOCK TABLES `INVOICES_SEQ` WRITE;
/*!40000 ALTER TABLE `INVOICES_SEQ` DISABLE KEYS */;
INSERT INTO `INVOICES_SEQ` VALUES (751);
/*!40000 ALTER TABLE `INVOICES_SEQ` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `INVOICE_LINES`
--

DROP TABLE IF EXISTS `INVOICE_LINES`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `INVOICE_LINES` (
  `fk_invoice` int NOT NULL,
  `fk_product_service` int NOT NULL,
  `id` int NOT NULL,
  `quantity` int NOT NULL,
  `subtotal` decimal(38,2) NOT NULL,
  `unit_price` decimal(38,2) NOT NULL,
  `description` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `INVOICE_LINES`
--

LOCK TABLES `INVOICE_LINES` WRITE;
/*!40000 ALTER TABLE `INVOICE_LINES` DISABLE KEYS */;
INSERT INTO `INVOICE_LINES` VALUES (1,1,1,1,50.00,50.00,NULL),(402,1,452,1,50.00,50.00,'Clase de JiuJitsu'),(652,1,702,2,100.00,50.00,'Clase de JiuJitsu');
/*!40000 ALTER TABLE `INVOICE_LINES` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `INVOICE_LINES_SEQ`
--

DROP TABLE IF EXISTS `INVOICE_LINES_SEQ`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `INVOICE_LINES_SEQ` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `INVOICE_LINES_SEQ`
--

LOCK TABLES `INVOICE_LINES_SEQ` WRITE;
/*!40000 ALTER TABLE `INVOICE_LINES_SEQ` DISABLE KEYS */;
INSERT INTO `INVOICE_LINES_SEQ` VALUES (801);
/*!40000 ALTER TABLE `INVOICE_LINES_SEQ` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `IVA_TYPE`
--

DROP TABLE IF EXISTS `IVA_TYPE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `IVA_TYPE` (
  `id` int NOT NULL,
  `percentage` decimal(38,2) NOT NULL,
  `description` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `IVA_TYPE`
--

LOCK TABLES `IVA_TYPE` WRITE;
/*!40000 ALTER TABLE `IVA_TYPE` DISABLE KEYS */;
INSERT INTO `IVA_TYPE` VALUES (1,21.00,'IVA General');
/*!40000 ALTER TABLE `IVA_TYPE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `IVA_TYPE_SEQ`
--

DROP TABLE IF EXISTS `IVA_TYPE_SEQ`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `IVA_TYPE_SEQ` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `IVA_TYPE_SEQ`
--

LOCK TABLES `IVA_TYPE_SEQ` WRITE;
/*!40000 ALTER TABLE `IVA_TYPE_SEQ` DISABLE KEYS */;
INSERT INTO `IVA_TYPE_SEQ` VALUES (151);
/*!40000 ALTER TABLE `IVA_TYPE_SEQ` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MEMBERSHIPS`
--

DROP TABLE IF EXISTS `MEMBERSHIPS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `MEMBERSHIPS` (
  `end_date` date NOT NULL,
  `id` int NOT NULL,
  `start_date` date NOT NULL,
  `status` enum('ACTIVE','DELETED','INACTIVE','NOT_PAID','NOT_SENT','PAID','PENDING','SENT','SUSPENDED') NOT NULL,
  `type` enum('ADVANCED','BASIC','NO_LIMIT','PREMIUM','TRIAL') NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MEMBERSHIPS`
--

LOCK TABLES `MEMBERSHIPS` WRITE;
/*!40000 ALTER TABLE `MEMBERSHIPS` DISABLE KEYS */;
INSERT INTO `MEMBERSHIPS` VALUES ('2025-06-12',1,'2025-05-12','ACTIVE','BASIC'),('2025-08-12',2,'2025-05-12','ACTIVE','ADVANCED'),('2025-11-12',3,'2025-05-12','ACTIVE','PREMIUM'),('2026-05-12',4,'2025-05-12','ACTIVE','NO_LIMIT'),('2025-05-16',52,'2025-05-13','ACTIVE','TRIAL');
/*!40000 ALTER TABLE `MEMBERSHIPS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MEMBERSHIPS_SEQ`
--

DROP TABLE IF EXISTS `MEMBERSHIPS_SEQ`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `MEMBERSHIPS_SEQ` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MEMBERSHIPS_SEQ`
--

LOCK TABLES `MEMBERSHIPS_SEQ` WRITE;
/*!40000 ALTER TABLE `MEMBERSHIPS_SEQ` DISABLE KEYS */;
INSERT INTO `MEMBERSHIPS_SEQ` VALUES (151);
/*!40000 ALTER TABLE `MEMBERSHIPS_SEQ` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `NOTIFICATIONS`
--

DROP TABLE IF EXISTS `NOTIFICATIONS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `NOTIFICATIONS` (
  `id` int NOT NULL,
  `shipping_date` datetime(6) NOT NULL,
  `type` varchar(100) DEFAULT NULL,
  `title` varchar(200) NOT NULL,
  `message` text,
  `status` enum('ACTIVE','DELETED','INACTIVE','NOT_PAID','NOT_SENT','PAID','PENDING','SENT','SUSPENDED') DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `NOTIFICATIONS`
--

LOCK TABLES `NOTIFICATIONS` WRITE;
/*!40000 ALTER TABLE `NOTIFICATIONS` DISABLE KEYS */;
INSERT INTO `NOTIFICATIONS` VALUES (1,'2025-05-12 20:58:14.873221','Bienvenida','Bienvenido a MemberFlow','Gracias por registrarte','ACTIVE');
/*!40000 ALTER TABLE `NOTIFICATIONS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `NOTIFICATIONS_SEQ`
--

DROP TABLE IF EXISTS `NOTIFICATIONS_SEQ`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `NOTIFICATIONS_SEQ` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `NOTIFICATIONS_SEQ`
--

LOCK TABLES `NOTIFICATIONS_SEQ` WRITE;
/*!40000 ALTER TABLE `NOTIFICATIONS_SEQ` DISABLE KEYS */;
INSERT INTO `NOTIFICATIONS_SEQ` VALUES (51);
/*!40000 ALTER TABLE `NOTIFICATIONS_SEQ` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PAYMENTS`
--

DROP TABLE IF EXISTS `PAYMENTS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `PAYMENTS` (
  `amount` decimal(38,2) NOT NULL,
  `fk_invoice` int NOT NULL,
  `id` int NOT NULL,
  `payment_date` datetime(6) NOT NULL,
  `payment_method` enum('BANK_TRANSFER','CASH','CREDIT_CARD') NOT NULL,
  `status` enum('ACTIVE','DELETED','INACTIVE','NOT_PAID','NOT_SENT','PAID','PENDING','SENT','SUSPENDED') NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK3ktj4gndg7wyqu8wqoyqlxonw` (`fk_invoice`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PAYMENTS`
--

LOCK TABLES `PAYMENTS` WRITE;
/*!40000 ALTER TABLE `PAYMENTS` DISABLE KEYS */;
INSERT INTO `PAYMENTS` VALUES (50.00,1,1,'2025-05-12 20:58:15.052213','CREDIT_CARD','PAID'),(50.00,402,252,'2025-05-15 18:15:38.102000','CREDIT_CARD','PAID'),(121.00,652,402,'2025-05-15 21:26:04.492000','CREDIT_CARD','PAID');
/*!40000 ALTER TABLE `PAYMENTS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PAYMENTS_SEQ`
--

DROP TABLE IF EXISTS `PAYMENTS_SEQ`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `PAYMENTS_SEQ` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PAYMENTS_SEQ`
--

LOCK TABLES `PAYMENTS_SEQ` WRITE;
/*!40000 ALTER TABLE `PAYMENTS_SEQ` DISABLE KEYS */;
INSERT INTO `PAYMENTS_SEQ` VALUES (501);
/*!40000 ALTER TABLE `PAYMENTS_SEQ` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PERMISSIONS`
--

DROP TABLE IF EXISTS `PERMISSIONS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `PERMISSIONS` (
  `id` int NOT NULL,
  `name` enum('FULL_ACCESS','MANAGE_STUDENTS','VIEW_OWN_DATA') NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PERMISSIONS`
--

LOCK TABLES `PERMISSIONS` WRITE;
/*!40000 ALTER TABLE `PERMISSIONS` DISABLE KEYS */;
INSERT INTO `PERMISSIONS` VALUES (1,'FULL_ACCESS'),(2,'MANAGE_STUDENTS'),(3,'VIEW_OWN_DATA');
/*!40000 ALTER TABLE `PERMISSIONS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PERMISSIONS_SEQ`
--

DROP TABLE IF EXISTS `PERMISSIONS_SEQ`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `PERMISSIONS_SEQ` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PERMISSIONS_SEQ`
--

LOCK TABLES `PERMISSIONS_SEQ` WRITE;
/*!40000 ALTER TABLE `PERMISSIONS_SEQ` DISABLE KEYS */;
INSERT INTO `PERMISSIONS_SEQ` VALUES (101);
/*!40000 ALTER TABLE `PERMISSIONS_SEQ` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PRODUCTS_SERVICES`
--

DROP TABLE IF EXISTS `PRODUCTS_SERVICES`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `PRODUCTS_SERVICES` (
  `fk_iva_type` int NOT NULL,
  `id` int NOT NULL,
  `price` decimal(38,2) NOT NULL,
  `type` varchar(45) NOT NULL,
  `name` varchar(100) NOT NULL,
  `description` varchar(250) DEFAULT NULL,
  `status` enum('ACTIVE','DELETED','INACTIVE','NOT_PAID','NOT_SENT','PAID','PENDING','SENT','SUSPENDED') NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PRODUCTS_SERVICES`
--

LOCK TABLES `PRODUCTS_SERVICES` WRITE;
/*!40000 ALTER TABLE `PRODUCTS_SERVICES` DISABLE KEYS */;
INSERT INTO `PRODUCTS_SERVICES` VALUES (1,1,50.00,'SERVICE','Clase de JiuJitsu',NULL,'ACTIVE');
/*!40000 ALTER TABLE `PRODUCTS_SERVICES` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PRODUCTS_SERVICES_SEQ`
--

DROP TABLE IF EXISTS `PRODUCTS_SERVICES_SEQ`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `PRODUCTS_SERVICES_SEQ` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PRODUCTS_SERVICES_SEQ`
--

LOCK TABLES `PRODUCTS_SERVICES_SEQ` WRITE;
/*!40000 ALTER TABLE `PRODUCTS_SERVICES_SEQ` DISABLE KEYS */;
INSERT INTO `PRODUCTS_SERVICES_SEQ` VALUES (101);
/*!40000 ALTER TABLE `PRODUCTS_SERVICES_SEQ` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ROLES`
--

DROP TABLE IF EXISTS `ROLES`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ROLES` (
  `id` int NOT NULL,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ROLES`
--

LOCK TABLES `ROLES` WRITE;
/*!40000 ALTER TABLE `ROLES` DISABLE KEYS */;
INSERT INTO `ROLES` VALUES (1,'ROLE_STUDENT'),(2,'ROLE_TEACHER'),(3,'ROLE_ADMIN');
/*!40000 ALTER TABLE `ROLES` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ROLES_PERMISSIONS`
--

DROP TABLE IF EXISTS `ROLES_PERMISSIONS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ROLES_PERMISSIONS` (
  `fk_permission` int NOT NULL,
  `fk_role` int NOT NULL,
  PRIMARY KEY (`fk_role`,`fk_permission`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ROLES_PERMISSIONS`
--

LOCK TABLES `ROLES_PERMISSIONS` WRITE;
/*!40000 ALTER TABLE `ROLES_PERMISSIONS` DISABLE KEYS */;
INSERT INTO `ROLES_PERMISSIONS` VALUES (3,1),(2,2),(3,2),(1,3);
/*!40000 ALTER TABLE `ROLES_PERMISSIONS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ROLES_SEQ`
--

DROP TABLE IF EXISTS `ROLES_SEQ`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ROLES_SEQ` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ROLES_SEQ`
--

LOCK TABLES `ROLES_SEQ` WRITE;
/*!40000 ALTER TABLE `ROLES_SEQ` DISABLE KEYS */;
INSERT INTO `ROLES_SEQ` VALUES (101);
/*!40000 ALTER TABLE `ROLES_SEQ` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `STUDENTS`
--

DROP TABLE IF EXISTS `STUDENTS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `STUDENTS` (
  `birthdate` date NOT NULL,
  `fk_membership` int DEFAULT NULL,
  `fk_user` int NOT NULL,
  `id` int NOT NULL,
  `dni` varchar(10) NOT NULL,
  `belt` varchar(20) DEFAULT NULL,
  `parent_name` varchar(50) DEFAULT NULL,
  `medical_report` varchar(500) DEFAULT NULL,
  `progress` text,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK3ci8o1dbwgn49fx4tkhk579fk` (`fk_user`),
  UNIQUE KEY `UKoljr3bj7rsof5ys5e9v932g9l` (`dni`),
  UNIQUE KEY `UKgjomxh7uemp64sjo69mkefqbh` (`fk_membership`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `STUDENTS`
--

LOCK TABLES `STUDENTS` WRITE;
/*!40000 ALTER TABLE `STUDENTS` DISABLE KEYS */;
INSERT INTO `STUDENTS` VALUES ('2005-05-20',52,1,1,'12345678A','Blanco','Padre Estudiante','Apto','Buena evolución'),('2025-05-13',1,102,2,'x7969338k','Blanco','','',NULL);
/*!40000 ALTER TABLE `STUDENTS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `STUDENTS_GROUPS`
--

DROP TABLE IF EXISTS `STUDENTS_GROUPS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `STUDENTS_GROUPS` (
  `fk_group` int NOT NULL,
  `fk_student` int NOT NULL,
  PRIMARY KEY (`fk_group`,`fk_student`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `STUDENTS_GROUPS`
--

LOCK TABLES `STUDENTS_GROUPS` WRITE;
/*!40000 ALTER TABLE `STUDENTS_GROUPS` DISABLE KEYS */;
INSERT INTO `STUDENTS_GROUPS` VALUES (1,1),(1,2);
/*!40000 ALTER TABLE `STUDENTS_GROUPS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `STUDENTS_SEQ`
--

DROP TABLE IF EXISTS `STUDENTS_SEQ`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `STUDENTS_SEQ` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `STUDENTS_SEQ`
--

LOCK TABLES `STUDENTS_SEQ` WRITE;
/*!40000 ALTER TABLE `STUDENTS_SEQ` DISABLE KEYS */;
INSERT INTO `STUDENTS_SEQ` VALUES (101);
/*!40000 ALTER TABLE `STUDENTS_SEQ` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `STUDENT_HISTORY`
--

DROP TABLE IF EXISTS `STUDENT_HISTORY`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `STUDENT_HISTORY` (
  `event_date` date DEFAULT NULL,
  `fk_student` int NOT NULL,
  `id` int NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `event_type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `STUDENT_HISTORY`
--

LOCK TABLES `STUDENT_HISTORY` WRITE;
/*!40000 ALTER TABLE `STUDENT_HISTORY` DISABLE KEYS */;
INSERT INTO `STUDENT_HISTORY` VALUES ('2025-05-12',1,1,'Primera clase','Clase de prueba');
/*!40000 ALTER TABLE `STUDENT_HISTORY` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `STUDENT_HISTORY_SEQ`
--

DROP TABLE IF EXISTS `STUDENT_HISTORY_SEQ`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `STUDENT_HISTORY_SEQ` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `STUDENT_HISTORY_SEQ`
--

LOCK TABLES `STUDENT_HISTORY_SEQ` WRITE;
/*!40000 ALTER TABLE `STUDENT_HISTORY_SEQ` DISABLE KEYS */;
INSERT INTO `STUDENT_HISTORY_SEQ` VALUES (101);
/*!40000 ALTER TABLE `STUDENT_HISTORY_SEQ` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `TEACHERS`
--

DROP TABLE IF EXISTS `TEACHERS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `TEACHERS` (
  `fk_user` int NOT NULL,
  `id` int NOT NULL,
  `discipline` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK352b5808rcgb1un47hwig1gq2` (`fk_user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `TEACHERS`
--

LOCK TABLES `TEACHERS` WRITE;
/*!40000 ALTER TABLE `TEACHERS` DISABLE KEYS */;
INSERT INTO `TEACHERS` VALUES (2,1,'Jiu-Jitsu');
/*!40000 ALTER TABLE `TEACHERS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `TEACHERS_SEQ`
--

DROP TABLE IF EXISTS `TEACHERS_SEQ`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `TEACHERS_SEQ` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `TEACHERS_SEQ`
--

LOCK TABLES `TEACHERS_SEQ` WRITE;
/*!40000 ALTER TABLE `TEACHERS_SEQ` DISABLE KEYS */;
INSERT INTO `TEACHERS_SEQ` VALUES (51);
/*!40000 ALTER TABLE `TEACHERS_SEQ` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `TRAINING_GROUPS`
--

DROP TABLE IF EXISTS `TRAINING_GROUPS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `TRAINING_GROUPS` (
  `fk_teacher` int NOT NULL,
  `id` int NOT NULL,
  `schedule` datetime(6) NOT NULL,
  `level` varchar(45) DEFAULT NULL,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `TRAINING_GROUPS`
--

LOCK TABLES `TRAINING_GROUPS` WRITE;
/*!40000 ALTER TABLE `TRAINING_GROUPS` DISABLE KEYS */;
INSERT INTO `TRAINING_GROUPS` VALUES (1,1,'2025-05-13 20:58:14.905684',NULL,'Grupo JiuJitsu Avanzado');
/*!40000 ALTER TABLE `TRAINING_GROUPS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `TRAINING_GROUPS_SEQ`
--

DROP TABLE IF EXISTS `TRAINING_GROUPS_SEQ`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `TRAINING_GROUPS_SEQ` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `TRAINING_GROUPS_SEQ`
--

LOCK TABLES `TRAINING_GROUPS_SEQ` WRITE;
/*!40000 ALTER TABLE `TRAINING_GROUPS_SEQ` DISABLE KEYS */;
INSERT INTO `TRAINING_GROUPS_SEQ` VALUES (101);
/*!40000 ALTER TABLE `TRAINING_GROUPS_SEQ` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `TRAINING_SESSIONS`
--

DROP TABLE IF EXISTS `TRAINING_SESSIONS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `TRAINING_SESSIONS` (
  `fk_group` int NOT NULL,
  `id` int NOT NULL,
  `date_time` datetime(6) DEFAULT NULL,
  `status` enum('ACTIVE','DELETED','INACTIVE','NOT_PAID','NOT_SENT','PAID','PENDING','SENT','SUSPENDED') DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `TRAINING_SESSIONS`
--

LOCK TABLES `TRAINING_SESSIONS` WRITE;
/*!40000 ALTER TABLE `TRAINING_SESSIONS` DISABLE KEYS */;
INSERT INTO `TRAINING_SESSIONS` VALUES (1,1,'2025-05-14 20:58:14.943096','ACTIVE');
/*!40000 ALTER TABLE `TRAINING_SESSIONS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `TRAINING_SESSIONS_SEQ`
--

DROP TABLE IF EXISTS `TRAINING_SESSIONS_SEQ`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `TRAINING_SESSIONS_SEQ` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `TRAINING_SESSIONS_SEQ`
--

LOCK TABLES `TRAINING_SESSIONS_SEQ` WRITE;
/*!40000 ALTER TABLE `TRAINING_SESSIONS_SEQ` DISABLE KEYS */;
INSERT INTO `TRAINING_SESSIONS_SEQ` VALUES (101);
/*!40000 ALTER TABLE `TRAINING_SESSIONS_SEQ` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `USERS`
--

DROP TABLE IF EXISTS `USERS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `USERS` (
  `fk_role` int NOT NULL,
  `id` int NOT NULL,
  `register_date` datetime(6) NOT NULL,
  `phone_number` varchar(30) NOT NULL,
  `name` varchar(50) NOT NULL,
  `surname` varchar(50) NOT NULL,
  `address` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `status` enum('ACTIVE','DELETED','INACTIVE','NOT_PAID','NOT_SENT','PAID','PENDING','SENT','SUSPENDED') NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKavh1b2ec82audum2lyjx2p1ws` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `USERS`
--

LOCK TABLES `USERS` WRITE;
/*!40000 ALTER TABLE `USERS` DISABLE KEYS */;
INSERT INTO `USERS` VALUES (1,1,'2025-05-12 20:58:14.649335','600123456','Student','One','Calle Estudiante 123','student@example.com','$2a$10$HDe34Jaf7gq3jLOV2lLX8.Mvxo4WCuyi74kmoBr5V424bHbz/Ig3e','ACTIVE'),(2,2,'2025-05-12 20:58:14.774906','611123456','Teacher','Uno','Calle Maestro 1','teacher@example.com','$2a$10$RIrnwB/1ljmjIu/sEwaXkeQ7oW3vjjOx5Ph/wqKFRUz/x.WXE4oyW','ACTIVE'),(3,3,'2025-05-12 20:58:14.852822','622123456','Admin','Root','Central','admin@example.com','$2a$10$0/NSag7TSZoGgm9tPrPuW.i0OaRBCTLnZZ9JjumZ9UTiRmGZs6EOy','ACTIVE'),(3,52,'2025-05-12 21:09:23.754771','675529497','Dennis','Eckerskorn','Carrer d\'Azorín, 30 Bajo','deckerskorn@test.com','$2a$10$8QuqqxUD7NHaD2XP5r4L8u0jBanKdngP3Q2yQks6swdr4smXyIxg6','ACTIVE'),(1,102,'2025-05-13 19:29:19.883022','1234567','Miriam','Valiña','Carrer d\'Azorín, 30 Bajo','mv@mf.com','$2a$10$mXh/4xTxdOOZEtkQPMbGuOO1kT47z0DFQXq8CtGbTptIAcfpk33qW','ACTIVE');
/*!40000 ALTER TABLE `USERS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `USERS_NOTIFICATIONS`
--

DROP TABLE IF EXISTS `USERS_NOTIFICATIONS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `USERS_NOTIFICATIONS` (
  `fk_notification` int NOT NULL,
  `fk_user` int NOT NULL,
  PRIMARY KEY (`fk_notification`,`fk_user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `USERS_NOTIFICATIONS`
--

LOCK TABLES `USERS_NOTIFICATIONS` WRITE;
/*!40000 ALTER TABLE `USERS_NOTIFICATIONS` DISABLE KEYS */;
INSERT INTO `USERS_NOTIFICATIONS` VALUES (1,1),(1,2),(1,3);
/*!40000 ALTER TABLE `USERS_NOTIFICATIONS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `USERS_SEQ`
--

DROP TABLE IF EXISTS `USERS_SEQ`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `USERS_SEQ` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `USERS_SEQ`
--

LOCK TABLES `USERS_SEQ` WRITE;
/*!40000 ALTER TABLE `USERS_SEQ` DISABLE KEYS */;
INSERT INTO `USERS_SEQ` VALUES (201);
/*!40000 ALTER TABLE `USERS_SEQ` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `admins`
--

DROP TABLE IF EXISTS `admins`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admins` (
  `fk_user` int NOT NULL,
  `id` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKhp41pq62xgvtc2ybhg0ogn9k2` (`fk_user`),
  CONSTRAINT `FK7atylq0pwosys9nu9m6i615yf` FOREIGN KEY (`fk_user`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admins`
--

LOCK TABLES `admins` WRITE;
/*!40000 ALTER TABLE `admins` DISABLE KEYS */;
INSERT INTO `admins` VALUES (3,1),(52,2);
/*!40000 ALTER TABLE `admins` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `admins_seq`
--

DROP TABLE IF EXISTS `admins_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admins_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admins_seq`
--

LOCK TABLES `admins_seq` WRITE;
/*!40000 ALTER TABLE `admins_seq` DISABLE KEYS */;
INSERT INTO `admins_seq` VALUES (101);
/*!40000 ALTER TABLE `admins_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `assistance`
--

DROP TABLE IF EXISTS `assistance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `assistance` (
  `fk_student` int NOT NULL,
  `fk_training_session` int NOT NULL,
  `id` int NOT NULL,
  `date_time` datetime(6) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKhrljnnasyhhy0wacovapkhgll` (`fk_student`),
  KEY `FKr5gxvfqh4k1jws31ymlw80nin` (`fk_training_session`),
  CONSTRAINT `FKhrljnnasyhhy0wacovapkhgll` FOREIGN KEY (`fk_student`) REFERENCES `students` (`id`),
  CONSTRAINT `FKr5gxvfqh4k1jws31ymlw80nin` FOREIGN KEY (`fk_training_session`) REFERENCES `training_sessions` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `assistance`
--

LOCK TABLES `assistance` WRITE;
/*!40000 ALTER TABLE `assistance` DISABLE KEYS */;
INSERT INTO `assistance` VALUES (2,302,352,'2025-05-11 14:19:43.234000');
/*!40000 ALTER TABLE `assistance` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `assistance_seq`
--

DROP TABLE IF EXISTS `assistance_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `assistance_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `assistance_seq`
--

LOCK TABLES `assistance_seq` WRITE;
/*!40000 ALTER TABLE `assistance_seq` DISABLE KEYS */;
INSERT INTO `assistance_seq` VALUES (451);
/*!40000 ALTER TABLE `assistance_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invoice_lines`
--

DROP TABLE IF EXISTS `invoice_lines`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `invoice_lines` (
  `fk_invoice` int NOT NULL,
  `fk_product_service` int NOT NULL,
  `id` int NOT NULL,
  `quantity` int NOT NULL,
  `subtotal` decimal(38,2) NOT NULL,
  `unit_price` decimal(38,2) NOT NULL,
  `description` text,
  PRIMARY KEY (`id`),
  KEY `FK16v42x9o42cpvobfprperyat1` (`fk_invoice`),
  KEY `FKt49wl938ncng8h5i3ivhw2851` (`fk_product_service`),
  CONSTRAINT `FK16v42x9o42cpvobfprperyat1` FOREIGN KEY (`fk_invoice`) REFERENCES `invoices` (`id`),
  CONSTRAINT `FKt49wl938ncng8h5i3ivhw2851` FOREIGN KEY (`fk_product_service`) REFERENCES `products_services` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoice_lines`
--

LOCK TABLES `invoice_lines` WRITE;
/*!40000 ALTER TABLE `invoice_lines` DISABLE KEYS */;
INSERT INTO `invoice_lines` VALUES (1,1,1,1,50.00,50.00,NULL),(1,1,2,1,50.00,50.00,NULL);
/*!40000 ALTER TABLE `invoice_lines` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invoice_lines_seq`
--

DROP TABLE IF EXISTS `invoice_lines_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `invoice_lines_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoice_lines_seq`
--

LOCK TABLES `invoice_lines_seq` WRITE;
/*!40000 ALTER TABLE `invoice_lines_seq` DISABLE KEYS */;
INSERT INTO `invoice_lines_seq` VALUES (101);
/*!40000 ALTER TABLE `invoice_lines_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invoices`
--

DROP TABLE IF EXISTS `invoices`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `invoices` (
  `fk_user` int NOT NULL,
  `id` int NOT NULL,
  `total` decimal(38,2) NOT NULL,
  `date` datetime(6) NOT NULL,
  `status` enum('ACTIVE','DELETED','INACTIVE','NOT_PAID','NOT_SENT','PAID','PENDING','SENT','SUSPENDED') NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKdhc51ih4oqto9ng00e4ioso7w` (`fk_user`),
  CONSTRAINT `FKdhc51ih4oqto9ng00e4ioso7w` FOREIGN KEY (`fk_user`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoices`
--

LOCK TABLES `invoices` WRITE;
/*!40000 ALTER TABLE `invoices` DISABLE KEYS */;
INSERT INTO `invoices` VALUES (1,1,50.00,'2025-05-08 16:22:31.643179','PAID');
/*!40000 ALTER TABLE `invoices` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invoices_seq`
--

DROP TABLE IF EXISTS `invoices_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `invoices_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoices_seq`
--

LOCK TABLES `invoices_seq` WRITE;
/*!40000 ALTER TABLE `invoices_seq` DISABLE KEYS */;
INSERT INTO `invoices_seq` VALUES (51);
/*!40000 ALTER TABLE `invoices_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `iva_type`
--

DROP TABLE IF EXISTS `iva_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `iva_type` (
  `id` int NOT NULL,
  `percentage` decimal(38,2) NOT NULL,
  `description` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `iva_type`
--

LOCK TABLES `iva_type` WRITE;
/*!40000 ALTER TABLE `iva_type` DISABLE KEYS */;
INSERT INTO `iva_type` VALUES (1,21.00,'IVA General');
/*!40000 ALTER TABLE `iva_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `iva_type_seq`
--

DROP TABLE IF EXISTS `iva_type_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `iva_type_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `iva_type_seq`
--

LOCK TABLES `iva_type_seq` WRITE;
/*!40000 ALTER TABLE `iva_type_seq` DISABLE KEYS */;
INSERT INTO `iva_type_seq` VALUES (51);
/*!40000 ALTER TABLE `iva_type_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `memberships`
--

DROP TABLE IF EXISTS `memberships`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `memberships` (
  `end_date` date NOT NULL,
  `id` int NOT NULL,
  `start_date` date NOT NULL,
  `status` enum('ACTIVE','DELETED','INACTIVE','NOT_PAID','NOT_SENT','PAID','PENDING','SENT','SUSPENDED') NOT NULL,
  `type` enum('ADVANCED','BASIC','NO_LIMIT','PREMIUM','TRIAL') NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `memberships`
--

LOCK TABLES `memberships` WRITE;
/*!40000 ALTER TABLE `memberships` DISABLE KEYS */;
INSERT INTO `memberships` VALUES ('2025-06-09',1,'2025-05-09','ACTIVE','BASIC'),('2025-08-09',2,'2025-05-09','ACTIVE','ADVANCED'),('2025-11-09',3,'2025-05-09','ACTIVE','PREMIUM'),('2026-05-09',4,'2025-05-09','ACTIVE','NO_LIMIT'),('2025-05-31',52,'2025-05-14','ACTIVE','TRIAL');
/*!40000 ALTER TABLE `memberships` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `memberships_seq`
--

DROP TABLE IF EXISTS `memberships_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `memberships_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `memberships_seq`
--

LOCK TABLES `memberships_seq` WRITE;
/*!40000 ALTER TABLE `memberships_seq` DISABLE KEYS */;
INSERT INTO `memberships_seq` VALUES (151);
/*!40000 ALTER TABLE `memberships_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notifications`
--

DROP TABLE IF EXISTS `notifications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notifications` (
  `id` int NOT NULL,
  `shipping_date` datetime(6) NOT NULL,
  `type` varchar(100) DEFAULT NULL,
  `title` varchar(200) NOT NULL,
  `message` text,
  `status` enum('ACTIVE','DELETED','INACTIVE','NOT_PAID','NOT_SENT','PAID','PENDING','SENT','SUSPENDED') DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notifications`
--

LOCK TABLES `notifications` WRITE;
/*!40000 ALTER TABLE `notifications` DISABLE KEYS */;
INSERT INTO `notifications` VALUES (1,'2025-05-09 16:22:31.581882','Bienvenida','Bienvenido a MemberFlow','Gracias por registrarte','ACTIVE'),(2,'2025-05-09 22:00:00.000000','','Hola','Hola','ACTIVE');
/*!40000 ALTER TABLE `notifications` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notifications_seq`
--

DROP TABLE IF EXISTS `notifications_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notifications_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notifications_seq`
--

LOCK TABLES `notifications_seq` WRITE;
/*!40000 ALTER TABLE `notifications_seq` DISABLE KEYS */;
INSERT INTO `notifications_seq` VALUES (101);
/*!40000 ALTER TABLE `notifications_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payments`
--

DROP TABLE IF EXISTS `payments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payments` (
  `amount` decimal(38,2) NOT NULL,
  `fk_invoice` int NOT NULL,
  `id` int NOT NULL,
  `payment_date` datetime(6) NOT NULL,
  `payment_method` enum('BANK_TRANSFER','CASH','CREDIT_CARD') NOT NULL,
  `status` enum('ACTIVE','DELETED','INACTIVE','NOT_PAID','NOT_SENT','PAID','PENDING','SENT','SUSPENDED') NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK3ktj4gndg7wyqu8wqoyqlxonw` (`fk_invoice`),
  CONSTRAINT `FKdryjs8tgr3kyuopc9aiwn1a62` FOREIGN KEY (`fk_invoice`) REFERENCES `invoices` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payments`
--

LOCK TABLES `payments` WRITE;
/*!40000 ALTER TABLE `payments` DISABLE KEYS */;
INSERT INTO `payments` VALUES (50.00,1,1,'2025-05-09 16:22:31.697527','CREDIT_CARD','PAID');
/*!40000 ALTER TABLE `payments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payments_seq`
--

DROP TABLE IF EXISTS `payments_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payments_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payments_seq`
--

LOCK TABLES `payments_seq` WRITE;
/*!40000 ALTER TABLE `payments_seq` DISABLE KEYS */;
INSERT INTO `payments_seq` VALUES (51);
/*!40000 ALTER TABLE `payments_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `permissions`
--

DROP TABLE IF EXISTS `permissions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `permissions` (
  `id` int NOT NULL,
  `name` enum('FULL_ACCESS','MANAGE_STUDENTS','VIEW_OWN_DATA') NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `permissions`
--

LOCK TABLES `permissions` WRITE;
/*!40000 ALTER TABLE `permissions` DISABLE KEYS */;
INSERT INTO `permissions` VALUES (1,'FULL_ACCESS'),(2,'MANAGE_STUDENTS'),(3,'VIEW_OWN_DATA');
/*!40000 ALTER TABLE `permissions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `permissions_seq`
--

DROP TABLE IF EXISTS `permissions_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `permissions_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `permissions_seq`
--

LOCK TABLES `permissions_seq` WRITE;
/*!40000 ALTER TABLE `permissions_seq` DISABLE KEYS */;
INSERT INTO `permissions_seq` VALUES (101);
/*!40000 ALTER TABLE `permissions_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `products_services`
--

DROP TABLE IF EXISTS `products_services`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `products_services` (
  `fk_iva_type` int NOT NULL,
  `id` int NOT NULL,
  `price` decimal(38,2) NOT NULL,
  `type` varchar(45) NOT NULL,
  `name` varchar(100) NOT NULL,
  `description` varchar(250) DEFAULT NULL,
  `status` enum('ACTIVE','DELETED','INACTIVE','NOT_PAID','NOT_SENT','PAID','PENDING','SENT','SUSPENDED') NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKr8m24u7qbyvbd8sjiq7g3jik0` (`fk_iva_type`),
  CONSTRAINT `FKr8m24u7qbyvbd8sjiq7g3jik0` FOREIGN KEY (`fk_iva_type`) REFERENCES `iva_type` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products_services`
--

LOCK TABLES `products_services` WRITE;
/*!40000 ALTER TABLE `products_services` DISABLE KEYS */;
INSERT INTO `products_services` VALUES (1,1,50.00,'Servicio','Clase de JiuJitsu',NULL,'ACTIVE');
/*!40000 ALTER TABLE `products_services` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `products_services_seq`
--

DROP TABLE IF EXISTS `products_services_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `products_services_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products_services_seq`
--

LOCK TABLES `products_services_seq` WRITE;
/*!40000 ALTER TABLE `products_services_seq` DISABLE KEYS */;
INSERT INTO `products_services_seq` VALUES (51);
/*!40000 ALTER TABLE `products_services_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `id` int NOT NULL,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (1,'ROLE_STUDENT'),(2,'ROLE_TEACHER'),(3,'ROLE_ADMIN');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles_permissions`
--

DROP TABLE IF EXISTS `roles_permissions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles_permissions` (
  `fk_permission` int NOT NULL,
  `fk_role` int NOT NULL,
  PRIMARY KEY (`fk_role`,`fk_permission`),
  KEY `FKlboaow7qn1m64rwjr8ifl6w2d` (`fk_permission`),
  CONSTRAINT `FKlboaow7qn1m64rwjr8ifl6w2d` FOREIGN KEY (`fk_permission`) REFERENCES `permissions` (`id`),
  CONSTRAINT `FKo60viy4q37nhjd2xyl242anba` FOREIGN KEY (`fk_role`) REFERENCES `roles` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles_permissions`
--

LOCK TABLES `roles_permissions` WRITE;
/*!40000 ALTER TABLE `roles_permissions` DISABLE KEYS */;
INSERT INTO `roles_permissions` VALUES (1,3),(2,2),(3,1),(3,2);
/*!40000 ALTER TABLE `roles_permissions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles_seq`
--

DROP TABLE IF EXISTS `roles_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles_seq`
--

LOCK TABLES `roles_seq` WRITE;
/*!40000 ALTER TABLE `roles_seq` DISABLE KEYS */;
INSERT INTO `roles_seq` VALUES (101);
/*!40000 ALTER TABLE `roles_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `student_history`
--

DROP TABLE IF EXISTS `student_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `student_history` (
  `event_date` date DEFAULT NULL,
  `fk_student` int NOT NULL,
  `id` int NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `event_type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKfifyby18m8fetblwf5nfg33gx` (`fk_student`),
  CONSTRAINT `FKfifyby18m8fetblwf5nfg33gx` FOREIGN KEY (`fk_student`) REFERENCES `students` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `student_history`
--

LOCK TABLES `student_history` WRITE;
/*!40000 ALTER TABLE `student_history` DISABLE KEYS */;
INSERT INTO `student_history` VALUES ('2025-05-09',1,1,'Primera clase','Clase de prueba');
/*!40000 ALTER TABLE `student_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `student_history_seq`
--

DROP TABLE IF EXISTS `student_history_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `student_history_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `student_history_seq`
--

LOCK TABLES `student_history_seq` WRITE;
/*!40000 ALTER TABLE `student_history_seq` DISABLE KEYS */;
INSERT INTO `student_history_seq` VALUES (51);
/*!40000 ALTER TABLE `student_history_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `students`
--

DROP TABLE IF EXISTS `students`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `students` (
  `birthdate` date NOT NULL,
  `fk_membership` int DEFAULT NULL,
  `fk_user` int NOT NULL,
  `id` int NOT NULL,
  `dni` varchar(10) NOT NULL,
  `belt` varchar(20) DEFAULT NULL,
  `parent_name` varchar(50) DEFAULT NULL,
  `medical_report` varchar(500) DEFAULT NULL,
  `progress` text,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK3ci8o1dbwgn49fx4tkhk579fk` (`fk_user`),
  UNIQUE KEY `UKoljr3bj7rsof5ys5e9v932g9l` (`dni`),
  UNIQUE KEY `UKgjomxh7uemp64sjo69mkefqbh` (`fk_membership`),
  CONSTRAINT `FKj3wyb55uh73yti619x84pfe7s` FOREIGN KEY (`fk_membership`) REFERENCES `memberships` (`id`),
  CONSTRAINT `FKkqp52wo7o073loxihnbf133wq` FOREIGN KEY (`fk_user`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `students`
--

LOCK TABLES `students` WRITE;
/*!40000 ALTER TABLE `students` DISABLE KEYS */;
INSERT INTO `students` VALUES ('2005-05-20',1,1,1,'12345678A','Blanco','Padre Estudiante','Apto','Buena evolución'),('2025-05-10',3,103,2,'123456789','Blanco','','',NULL);
/*!40000 ALTER TABLE `students` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `students_groups`
--

DROP TABLE IF EXISTS `students_groups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `students_groups` (
  `fk_group` int NOT NULL,
  `fk_student` int NOT NULL,
  PRIMARY KEY (`fk_group`,`fk_student`),
  KEY `FKqibb3ps0nfj4kkx6kd1qn7mkd` (`fk_student`),
  CONSTRAINT `FKp6ck2q2xcptfj526ipa1ki56g` FOREIGN KEY (`fk_group`) REFERENCES `training_groups` (`id`),
  CONSTRAINT `FKqibb3ps0nfj4kkx6kd1qn7mkd` FOREIGN KEY (`fk_student`) REFERENCES `students` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `students_groups`
--

LOCK TABLES `students_groups` WRITE;
/*!40000 ALTER TABLE `students_groups` DISABLE KEYS */;
INSERT INTO `students_groups` VALUES (452,2);
/*!40000 ALTER TABLE `students_groups` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `students_seq`
--

DROP TABLE IF EXISTS `students_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `students_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `students_seq`
--

LOCK TABLES `students_seq` WRITE;
/*!40000 ALTER TABLE `students_seq` DISABLE KEYS */;
INSERT INTO `students_seq` VALUES (101);
/*!40000 ALTER TABLE `students_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `teachers`
--

DROP TABLE IF EXISTS `teachers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `teachers` (
  `fk_user` int NOT NULL,
  `id` int NOT NULL,
  `discipline` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK352b5808rcgb1un47hwig1gq2` (`fk_user`),
  CONSTRAINT `FKeujs3kyjh75c0u93b731i9rua` FOREIGN KEY (`fk_user`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `teachers`
--

LOCK TABLES `teachers` WRITE;
/*!40000 ALTER TABLE `teachers` DISABLE KEYS */;
INSERT INTO `teachers` VALUES (2,1,'Jiu-Jitsu'),(102,2,'Jiujitsu');
/*!40000 ALTER TABLE `teachers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `teachers_seq`
--

DROP TABLE IF EXISTS `teachers_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `teachers_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `teachers_seq`
--

LOCK TABLES `teachers_seq` WRITE;
/*!40000 ALTER TABLE `teachers_seq` DISABLE KEYS */;
INSERT INTO `teachers_seq` VALUES (101);
/*!40000 ALTER TABLE `teachers_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `training_groups`
--

DROP TABLE IF EXISTS `training_groups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `training_groups` (
  `fk_teacher` int NOT NULL,
  `id` int NOT NULL,
  `schedule` datetime(6) NOT NULL,
  `level` varchar(45) DEFAULT NULL,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKhgbvfsm24yptkm76cbhtbv4s4` (`fk_teacher`),
  CONSTRAINT `FKhgbvfsm24yptkm76cbhtbv4s4` FOREIGN KEY (`fk_teacher`) REFERENCES `teachers` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `training_groups`
--

LOCK TABLES `training_groups` WRITE;
/*!40000 ALTER TABLE `training_groups` DISABLE KEYS */;
INSERT INTO `training_groups` VALUES (2,452,'2025-10-22 12:35:00.000000','Avanzado','JiuJitsu');
/*!40000 ALTER TABLE `training_groups` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `training_groups_seq`
--

DROP TABLE IF EXISTS `training_groups_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `training_groups_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `training_groups_seq`
--

LOCK TABLES `training_groups_seq` WRITE;
/*!40000 ALTER TABLE `training_groups_seq` DISABLE KEYS */;
INSERT INTO `training_groups_seq` VALUES (551);
/*!40000 ALTER TABLE `training_groups_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `training_sessions`
--

DROP TABLE IF EXISTS `training_sessions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `training_sessions` (
  `fk_group` int NOT NULL,
  `id` int NOT NULL,
  `date_time` datetime(6) NOT NULL,
  `status` enum('ACTIVE','DELETED','INACTIVE','NOT_PAID','NOT_SENT','PAID','PENDING','SENT','SUSPENDED') NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKkjj30gdo7uua9ycvlriax2vep` (`fk_group`),
  CONSTRAINT `FKkjj30gdo7uua9ycvlriax2vep` FOREIGN KEY (`fk_group`) REFERENCES `training_groups` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `training_sessions`
--

LOCK TABLES `training_sessions` WRITE;
/*!40000 ALTER TABLE `training_sessions` DISABLE KEYS */;
INSERT INTO `training_sessions` VALUES (452,302,'2025-10-22 12:35:00.000000','ACTIVE');
/*!40000 ALTER TABLE `training_sessions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `training_sessions_seq`
--

DROP TABLE IF EXISTS `training_sessions_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `training_sessions_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `training_sessions_seq`
--

LOCK TABLES `training_sessions_seq` WRITE;
/*!40000 ALTER TABLE `training_sessions_seq` DISABLE KEYS */;
INSERT INTO `training_sessions_seq` VALUES (401);
/*!40000 ALTER TABLE `training_sessions_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `fk_role` int NOT NULL,
  `id` int NOT NULL,
  `register_date` datetime(6) NOT NULL,
  `phone_number` varchar(30) NOT NULL,
  `name` varchar(50) NOT NULL,
  `surname` varchar(50) NOT NULL,
  `address` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `status` enum('ACTIVE','DELETED','INACTIVE','NOT_PAID','NOT_SENT','PAID','PENDING','SENT','SUSPENDED') NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKavh1b2ec82audum2lyjx2p1ws` (`email`),
  KEY `FK5k6cr6ejda3jhunshjbx4nhai` (`fk_role`),
  CONSTRAINT `FK5k6cr6ejda3jhunshjbx4nhai` FOREIGN KEY (`fk_role`) REFERENCES `roles` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,1,'2025-05-09 16:22:31.399041','600123456','Student','One','Calle Estudiante 123','student@example.com','$2a$10$G13aVWunAMLwQNDdca0BGuXeYxfH65TFriKHzpwU3OGZvR/6O.poO','ACTIVE'),(2,2,'2025-05-09 16:22:31.501044','611123456','Teacher','Uno','Calle Maestro 1','teacher@example.com','$2a$10$.0KMijc2nwqwJgUDiosN1.vzXHqiy9Ge6gzOiOKLwmOmiLa9yC85a','ACTIVE'),(3,3,'2025-05-09 16:22:31.568593','622123456','Admin','Root','Central','admin@example.com','$2a$10$u9k50gWu3eMG.KF1brVeC.O6UPob0DQgAdNxc.Y63AE1ZPkcwRVNy','ACTIVE'),(3,52,'2025-05-09 16:24:48.404428','675529497','Dennis','Eckerskorn','Carrer d\'Azorín, 30 Bajo','deckerskorn@mf.com','$2a$10$sg1Cy1AvimKBWEaZvGG3Ue.qdwclJre7VBvAHKrPTtY5dD9R4ZWqO','ACTIVE'),(2,102,'2025-05-10 19:07:22.957486','1234567','Steve','Newman','Pedreguer','steve@mm.com','$2a$10$iNJiV73bUopf0iiQvouqM.SWLPOnzCjRFPbilWLbHiRR7l6IivrAu','ACTIVE'),(1,103,'2025-05-10 19:09:09.423529','1234567','Miriam','Valiña','Carrer d\'Azorín, 30 Bajo','miriam@mf.com','$2a$10$ghObW0nZdg8qo.RRNgEZd.OrUC0vlebRjuaKeX5uEvH/6nwgqqtCa','ACTIVE');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users_notifications`
--

DROP TABLE IF EXISTS `users_notifications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users_notifications` (
  `fk_notification` int NOT NULL,
  `fk_user` int NOT NULL,
  PRIMARY KEY (`fk_notification`,`fk_user`),
  KEY `FKr4iwf238qbl6oa8x16gj17jud` (`fk_user`),
  CONSTRAINT `FK3sbul72qkfqxu2vgqk5fw2u` FOREIGN KEY (`fk_notification`) REFERENCES `notifications` (`id`),
  CONSTRAINT `FKr4iwf238qbl6oa8x16gj17jud` FOREIGN KEY (`fk_user`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users_notifications`
--

LOCK TABLES `users_notifications` WRITE;
/*!40000 ALTER TABLE `users_notifications` DISABLE KEYS */;
INSERT INTO `users_notifications` VALUES (1,1),(1,2),(1,3),(2,52);
/*!40000 ALTER TABLE `users_notifications` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users_seq`
--

DROP TABLE IF EXISTS `users_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users_seq`
--

LOCK TABLES `users_seq` WRITE;
/*!40000 ALTER TABLE `users_seq` DISABLE KEYS */;
INSERT INTO `users_seq` VALUES (201);
/*!40000 ALTER TABLE `users_seq` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-15 21:41:35
