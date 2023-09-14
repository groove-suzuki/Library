CREATE DATABASE `suzuki` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

CREATE TABLE `bookmst` (
  `bookNum` varchar(5) NOT NULL,
  `bookBranchNum` varchar(2) NOT NULL,
  `bookName` varchar(255) NOT NULL,
  `genreCode` varchar(2) NOT NULL,
  PRIMARY KEY (`bookNum`,`bookBranchNum`,`bookName`,`genreCode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `lendtable` (
  `cardId` varchar(5) NOT NULL,
  `bookNum` varchar(5) NOT NULL,
  `bookBranchNum` varchar(2) NOT NULL,
  `returnDate` varchar(10) NOT NULL,
  PRIMARY KEY (`cardId`,`bookNum`,`bookBranchNum`,`returnDate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `usermst` (
  `cardId` varchar(5) NOT NULL,
  `userName` varchar(255) NOT NULL,
  PRIMARY KEY (`cardId`,`userName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
