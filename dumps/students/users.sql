--
-- Table structure for table `users`
--
DROP TABLE IF EXISTS `users`;

CREATE TABLE `users` ( 
id INT NOT NULL PRIMARY KEY ,
firstname TEXT NULL  ,
lastName TEXT NULL  ,
city TEXT NULL  ,
state TEXT NULL  ,
country TEXT NULL  
);
--
-- Dumping data for table `users`
--
[SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";

INSERT INTO `users` VALUES(2,'shivangi','bhatt','ahmedabad','gujarat','india'),(3,'vanshika','shah','ahmedabad','gujarat','india'),(4,'pooja','shah','surat','gujarat','india');
COMMIT;
