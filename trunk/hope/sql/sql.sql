/*
SQLyog Ultimate v9.20 
MySQL - 5.5.31 : Database - hope
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`hope` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `hope`;

/*Table structure for table `subscribe_record_0` */

DROP TABLE IF EXISTS `subscribe_record_0`;

CREATE TABLE `subscribe_record_0` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `yyuid` bigint(20) NOT NULL,
  `anchor` varchar(50) NOT NULL COMMENT '主播id',
  `openId` varchar(50) NOT NULL,
  `guid` varchar(50) NOT NULL,
  `createTime` int(11) NOT NULL COMMENT '创建时间',
  `anchorUid` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_yyuid_anchor` (`yyuid`,`anchor`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `subscribe_record_0` */

/*Table structure for table `test_key_int` */

DROP TABLE IF EXISTS `test_key_int`;

CREATE TABLE `test_key_int` (
  `id_test` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL,
  PRIMARY KEY (`id_test`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

/*Data for the table `test_key_int` */

insert  into `test_key_int`(`id_test`,`name`) values (1,'frankie'),(2,'frankie'),(3,'mouzemin');

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` bigint(10) NOT NULL AUTO_INCREMENT,
  `nickName` varchar(20) DEFAULT NULL,
  `loginName` varchar(20) NOT NULL,
  `loginPassword` varchar(20) NOT NULL,
  `type` int(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `user` */

/*Table structure for table `user_2022` */

DROP TABLE IF EXISTS `user_2022`;

CREATE TABLE `user_2022` (
  `id` bigint(10) NOT NULL AUTO_INCREMENT,
  `nick_name` varchar(20) DEFAULT NULL,
  `login_name` varchar(20) NOT NULL,
  `login_password` varchar(20) NOT NULL,
  `type` int(1) NOT NULL,
  `game_id` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;

/*Data for the table `user_2022` */

insert  into `user_2022`(`id`,`nick_name`,`login_name`,`login_password`,`type`,`game_id`) values (1,'frankie333','mouzemin333333','dddddd',1,'2022'),(2,'frankie','mouzemin3','dddddd',1,'2022'),(3,'frankie123','mouzemin123','dddddd3333',1,'2022'),(4,'frankie','mouzemin3','dddddd',1,'2022'),(5,'frankie','mouzemin','dddddd',2,'2022'),(6,'frankie','mouzemin','dddddd',2,'2022'),(7,'frankie','mouzemin','dddddd',2,'2022'),(8,'frankie','mouzemin','dddddd',2,'2022'),(9,'frankie','mouzemin','dddddd',2,'2022'),(10,'frankie','mouzemin','dddddd',2,'2022'),(11,'frankie','mouzemin','dddddd',2,'2022'),(12,'frankie','mouzemin','dddddd',2,'2022'),(13,'frankie','mouzemin','dddddd',2,'2022'),(14,'frankie','mouzemin','dddddd',2,'2022'),(15,'frankie','mouzemin','dddddd',2,'2022'),(16,'frankie','mouzemin','dddddd',2,'2022');

/*Table structure for table `user_name` */

DROP TABLE IF EXISTS `user_name`;

CREATE TABLE `user_name` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL,
  `age` int(20) DEFAULT NULL,
  `datetime` datetime NOT NULL,
  `date` date DEFAULT NULL,
  `time` timestamp NULL DEFAULT NULL,
  `money` double DEFAULT NULL,
  `nums` bigint(20) DEFAULT NULL,
  `is_first` tinyint(1) DEFAULT NULL,
  `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2200 DEFAULT CHARSET=utf8;

/*Data for the table `user_name` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
