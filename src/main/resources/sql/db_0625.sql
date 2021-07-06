/*
SQLyog Ultimate v13.1.1 (64 bit)
MySQL - 8.0.23 : Database - shixun
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`shixun` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `shixun`;

/*Table structure for table `t_file` */

DROP TABLE IF EXISTS `t_file`;

CREATE TABLE `t_file` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '文件的ID',
  `realFileName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '用户指定的文件名',
  `uploaderId` int DEFAULT NULL COMMENT '上传者ID',
  `uploadTime` datetime DEFAULT NULL COMMENT '上传时间',
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '在存储介质中的相对路径',
  `savedFileName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '在存储介质中保存的文件名',
  `identifier` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '文件的唯一识别码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=64 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `t_file` */


/*Table structure for table `t_user` */

DROP TABLE IF EXISTS `t_user`;

CREATE TABLE `t_user` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '用户的邮箱，作为登录名',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '用户名，实质为昵称',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '用户的密码',
  PRIMARY KEY (`id`),
  UNIQUE KEY `EMAIL` (`email`) COMMENT '邮箱应为唯一的'
) ENGINE=InnoDB AUTO_INCREMENT=77 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `t_user` */

insert  into `t_user`(`id`,`email`,`username`,`password`) values 
(1,'1046221731@qq.com','admin','123'),
(2,'1@qq.com','user1','password1'),
(3,'2@qq.com','user2','password2'),
(4,'3@qq.com','user3','pwd3'),
(5,'4@qq.com','user4','pwd4'),
(6,'5@qq.com','user5','pwd5'),
(7,'6@qq.com','user6','pwd6'),
(8,'7@qq.com','user7','pwd7'),
(9,'8@qq.com','user8','pwd8'),
(10,'9@qq.com','user9','pwd9'),
(11,'10@qq.com','user10','pwd10'),
(12,'11@qq.com','user11','pwd11'),
(13,'12@qq.com','user12','pwd12'),
(14,'13@qq.com','user13','pwd13'),
(15,'14@qq.com','user14','pwd14'),
(16,'15@qq.com','user15','pwd15'),
(17,'16@qq.com','user16','pwd16'),
(18,'17@qq.com','user17','pwd17'),
(19,'18@qq.com','user18','pwd18'),
(20,'19@qq.com','user19','pwd19'),
(21,'20@qq.com','user20','pwd20'),
(22,'21@qq.com','user21','pwd21'),
(23,'22@qq.com','user22','pwd22'),
(24,'23@qq.com','user23','pwd23'),
(25,'24@qq.com','user24','pwd24'),
(26,'25@qq.com','user25','pwd25'),
(27,'26@qq.com','user26','pwd26'),
(28,'27@qq.com','user27','pwd27'),
(29,'28@qq.com','user28','pwd28'),
(30,'29@qq.com','user29','pwd29');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
