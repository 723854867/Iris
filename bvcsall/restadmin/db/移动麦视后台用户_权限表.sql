/*
Navicat MySQL Data Transfer

Source Server         : 10.18.20.164
Source Server Version : 50505
Source Host           : 10.18.20.164:3306
Source Database       : bvcs

Target Server Type    : MYSQL
Target Server Version : 50505
File Encoding         : 65001

Date: 2014-08-27 13:58:21
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `permission`
-- ----------------------------
DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `creator_id` bigint(20) DEFAULT NULL,
  `modify_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `description` longtext,
  `name` varchar(255) DEFAULT NULL,
  `pid` bigint(20) DEFAULT NULL,
  `sort` int(11) NOT NULL,
  `status` varchar(255) DEFAULT NULL,
  `system_type` varchar(255) NOT NULL,
  `type` varchar(255) NOT NULL,
  `value` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of permission
-- ----------------------------

-- ----------------------------
-- Table structure for `role`
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `creator_id` bigint(20) DEFAULT NULL,
  `modify_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `description` longtext,
  `is_system` bit(1) NOT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of role
-- ----------------------------

-- ----------------------------
-- Table structure for `role_permission`
-- ----------------------------
DROP TABLE IF EXISTS `role_permission`;
CREATE TABLE `role_permission` (
  `role` bigint(20) NOT NULL,
  `permissions` bigint(20) NOT NULL,
  KEY `FKBD40D538374EEBE7` (`role`),
  KEY `FKBD40D538B5413D0E` (`permissions`),
  CONSTRAINT `FKBD40D538B5413D0E` FOREIGN KEY (`permissions`) REFERENCES `permission` (`id`),
  CONSTRAINT `FKBD40D538374EEBE7` FOREIGN KEY (`role`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of role_permission
-- ----------------------------


-- ----------------------------
-- Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_at` datetime NOT NULL,
  `modify_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `activation_code` varchar(255) DEFAULT NULL,
  `department` varchar(255) DEFAULT NULL,
  `email` varchar(200) DEFAULT NULL,
  `is_enabled` bit(1) NOT NULL,
  `is_locked` bit(1) NOT NULL,
  `locked_date` datetime DEFAULT NULL,
  `login_date` datetime DEFAULT NULL,
  `login_failure_count` int(11) DEFAULT NULL,
  `login_ip` varchar(128) DEFAULT NULL,
  `name` varchar(200) DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `type` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  `creator_id` bigint(20) DEFAULT NULL,
  `data_from` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------

-- ----------------------------
-- Table structure for `user_role`
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role` (
  `user` bigint(20) NOT NULL,
  `roles` bigint(20) NOT NULL,
  KEY `FK143BF46A3D93FA2E` (`roles`),
  KEY `FK143BF46A3751C291` (`user`),
  CONSTRAINT `FK143BF46A3751C291` FOREIGN KEY (`user`) REFERENCES `user` (`id`),
  CONSTRAINT `FK143BF46A3D93FA2E` FOREIGN KEY (`roles`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_role
-- ----------------------------
