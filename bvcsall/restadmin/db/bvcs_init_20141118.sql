/*
Navicat MySQL Data Transfer

Source Server         : 麦视测试
Source Server Version : 50540
Source Host           : 192.168.108.160:3306
Source Database       : bvcs

Target Server Type    : MYSQL
Target Server Version : 50540
File Encoding         : 65001

Date: 2014-11-18 11:40:37
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `permission`
-- ----------------------------
DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_at` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `creator_id` bigint(20) DEFAULT NULL,
  `modify_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `description` longtext COLLATE utf8_bin,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `pid` bigint(20) DEFAULT NULL,
  `sort` int(11) NOT NULL,
  `status` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `system_type` varchar(255) COLLATE utf8_bin NOT NULL,
  `type` varchar(255) COLLATE utf8_bin NOT NULL,
  `value` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of permission
-- ----------------------------
INSERT INTO `permission` VALUES ('1', '2014-08-26 17:39:34', null, '2014-08-27 10:56:53', null, '系统设置', '0', '5', '', 'BVCS', 'URI', '');
INSERT INTO `permission` VALUES ('2', '2014-08-26 17:39:47', null, '2014-08-27 10:59:18', null, '用户管理', '1', '2', '', 'BVCS', 'URI', 'user/userlist');
INSERT INTO `permission` VALUES ('3', '2014-08-26 17:55:15', null, '2014-08-27 10:59:22', null, '角色管理', '1', '3', '', 'BVCS', 'URI', 'role/rolelist');
INSERT INTO `permission` VALUES ('4', '2014-08-26 17:55:39', null, '2014-08-27 10:59:13', null, '上传账户管理', '1', '1', '', 'BVCS', 'URI', 'videoUploader/videoUploaderlist');
INSERT INTO `permission` VALUES ('5', '2014-08-26 17:56:01', null, '2014-08-27 10:59:27', null, '菜单管理', '1', '4', '', 'BVCS', 'URI', 'permission/permissionlist');
INSERT INTO `permission` VALUES ('6', '2014-08-26 17:56:15', null, '2014-08-27 10:56:47', null, '内容管理', '0', '4', '', 'BVCS', 'URI', '');
INSERT INTO `permission` VALUES ('7', '2014-08-26 17:56:30', null, '2014-08-27 10:59:02', null, '专题管理', '6', '1', '', 'BVCS', 'URI', 'subject/subjectlist');
INSERT INTO `permission` VALUES ('8', '2014-08-26 17:56:47', null, '2014-08-27 10:59:05', null, '标签管理', '6', '2', '', 'BVCS', 'URI', 'tag/taglist');
INSERT INTO `permission` VALUES ('9', '2014-08-26 17:56:57', null, '2014-08-27 10:56:40', null, '视频库', '0', '3', '', 'BVCS', 'URI', '');
INSERT INTO `permission` VALUES ('10', '2014-08-26 17:57:21', null, '2014-08-26 17:57:21', null, '全部视频', '9', '0', '', 'BVCS', 'URI', 'video/allvalidlist');
INSERT INTO `permission` VALUES ('11', '2014-08-26 17:57:32', null, '2014-08-27 10:56:31', null, '用户视频', '0', '2', '', 'BVCS', 'URI', '');
INSERT INTO `permission` VALUES ('12', '2014-08-26 17:57:54', null, '2014-08-27 10:58:29', null, '未审核视频', '11', '1', '', 'BVCS', 'URI', 'video/unchecklist');
INSERT INTO `permission` VALUES ('13', '2014-08-26 17:58:12', null, '2014-08-27 10:58:40', null, '审核通过视频', '11', '3', '', 'BVCS', 'URI', 'video/checkoklist');
INSERT INTO `permission` VALUES ('14', '2014-08-26 17:58:35', null, '2014-08-27 10:58:37', null, '审核未通过视频', '11', '2', '', 'BVCS', 'URI', 'video/checkfaillist');
INSERT INTO `permission` VALUES ('15', '2014-08-26 17:58:51', null, '2014-08-27 10:58:45', null, '已删除视频', '11', '4', '', 'BVCS', 'URI', 'video/destroyedlist');
INSERT INTO `permission` VALUES ('16', '2014-08-26 17:59:01', null, '2014-08-27 10:56:25', null, '管理员视频', '0', '1', '', 'BVCS', 'URI', '');
INSERT INTO `permission` VALUES ('17', '2014-08-26 17:59:19', null, '2014-08-27 10:58:17', null, '视频上传', '16', '1', '', 'BVCS', 'URI', 'video/index');
INSERT INTO `permission` VALUES ('18', '2014-08-26 17:59:35', null, '2014-08-27 10:58:21', null, '视频发布', '16', '2', '', 'BVCS', 'URI', 'video/checledVideos');
INSERT INTO `permission` VALUES ('20', '2014-11-12 16:25:13', null, '2014-11-12 16:25:59', null, '活动管理', '6', '1', 'valid', 'BVCS', 'URI', 'activity/activitylist');
INSERT INTO `permission` VALUES ('21', '2014-11-12 16:26:04', null, '2014-11-12 16:26:31', null, '投诉管理', '6', '1', 'valid', 'BVCS', 'URI', 'complain/complainlist');
INSERT INTO `permission` VALUES ('22', '2014-11-12 17:01:03', null, '2014-11-12 17:01:59', null, '消息推送', '6', '1', '', 'BVCS', 'URI', 'notify/notelist');
INSERT INTO `permission` VALUES ('23', '2014-11-18 11:37:17', null, '2014-11-18 11:37:34', null, '操作日志', '1', '1', 'valid', 'BVCS', 'URI', 'operationLog/operationLoglist');

-- ----------------------------
-- Table structure for `role`
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_at` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `creator_id` bigint(20) DEFAULT NULL,
  `modify_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `description` longtext COLLATE utf8_bin,
  `is_system` bit(1) NOT NULL,
  `name` varchar(255) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES ('1', '2014-08-26 17:50:22', null, '2014-08-26 17:50:22', 0xE7B3BBE7BB9FE7AEA1E79086E59198, '', '系统管理员');
INSERT INTO `role` VALUES ('2', '2014-08-26 17:50:46', null, '2014-08-27 11:04:33', 0xE4B88AE4BCA0E4BABAE59198, '', '上传人员');
INSERT INTO `role` VALUES ('3', '2014-08-27 11:05:20', null, '2014-08-27 11:05:20', 0xE58F91E5B883E4BABAE59198, '', '发布人员');
INSERT INTO `role` VALUES ('4', '2014-08-27 11:06:10', null, '2014-08-27 11:06:10', 0xE8BF90E890A5E4B893E59198, '', '运营专员');
INSERT INTO `role` VALUES ('5', '2014-08-27 11:06:28', null, '2014-08-27 11:06:28', 0xE5AEA1E6A0B8E4BABAE59198, '', '审核人员');

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of role_permission
-- ----------------------------
INSERT INTO `role_permission` VALUES ('2', '17');
INSERT INTO `role_permission` VALUES ('3', '18');
INSERT INTO `role_permission` VALUES ('4', '1');
INSERT INTO `role_permission` VALUES ('4', '2');
INSERT INTO `role_permission` VALUES ('4', '3');
INSERT INTO `role_permission` VALUES ('4', '4');
INSERT INTO `role_permission` VALUES ('4', '5');
INSERT INTO `role_permission` VALUES ('4', '6');
INSERT INTO `role_permission` VALUES ('4', '7');
INSERT INTO `role_permission` VALUES ('4', '8');
INSERT INTO `role_permission` VALUES ('5', '12');
INSERT INTO `role_permission` VALUES ('1', '2');
INSERT INTO `role_permission` VALUES ('1', '3');
INSERT INTO `role_permission` VALUES ('1', '4');
INSERT INTO `role_permission` VALUES ('1', '5');
INSERT INTO `role_permission` VALUES ('1', '7');
INSERT INTO `role_permission` VALUES ('1', '8');
INSERT INTO `role_permission` VALUES ('1', '10');
INSERT INTO `role_permission` VALUES ('1', '12');
INSERT INTO `role_permission` VALUES ('1', '13');
INSERT INTO `role_permission` VALUES ('1', '14');
INSERT INTO `role_permission` VALUES ('1', '15');
INSERT INTO `role_permission` VALUES ('1', '17');
INSERT INTO `role_permission` VALUES ('1', '18');
INSERT INTO `role_permission` VALUES ('1', '20');
INSERT INTO `role_permission` VALUES ('1', '21');
INSERT INTO `role_permission` VALUES ('1', '22');
INSERT INTO `role_permission` VALUES ('1', '23');

-- ----------------------------
-- Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_at` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `modify_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `activation_code` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `department` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `email` varchar(200) COLLATE utf8_bin DEFAULT NULL,
  `is_enabled` bit(1) NOT NULL,
  `is_locked` bit(1) NOT NULL,
  `locked_date` datetime DEFAULT NULL,
  `login_date` datetime DEFAULT NULL,
  `login_failure_count` int(11) DEFAULT NULL,
  `login_ip` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `name` varchar(200) COLLATE utf8_bin DEFAULT NULL,
  `password` varchar(255) COLLATE utf8_bin NOT NULL,
  `phone` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `type` varchar(255) COLLATE utf8_bin NOT NULL,
  `username` varchar(255) COLLATE utf8_bin NOT NULL,
  `creator_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', '2014-07-29 15:16:11', '2014-11-18 11:38:16', null, null, 'meizhiwen84@163.com', '', '', null, '2014-11-18 11:38:16', '0', null, null, '96e79218965eb72c92a549dd5a330112', null, 'adminuser', 'admin', null);
INSERT INTO `user` VALUES ('22', '2014-08-27 11:13:38', '2014-08-27 11:41:45', null, null, null, '', '', null, '2014-08-27 11:41:45', '0', null, null, '96e79218965eb72c92a549dd5a330112', null, 'admin', 'shangchuan', null);
INSERT INTO `user` VALUES ('23', '2014-08-27 11:13:49', '2014-08-27 11:13:49', null, null, null, '', '', null, null, null, null, null, '96e79218965eb72c92a549dd5a330112', null, 'admin', 'fabu', null);
INSERT INTO `user` VALUES ('24', '2014-08-27 11:14:02', '2014-08-27 11:14:02', null, null, null, '', '', null, null, null, null, null, '96e79218965eb72c92a549dd5a330112', null, 'admin', 'yunying', null);
INSERT INTO `user` VALUES ('25', '2014-08-27 11:14:16', '2014-08-27 11:14:16', null, null, null, '', '', null, null, null, null, null, '96e79218965eb72c92a549dd5a330112', null, 'admin', 'shenghe', null);

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of user_role
-- ----------------------------
INSERT INTO `user_role` VALUES ('1', '1');
INSERT INTO `user_role` VALUES ('22', '2');
INSERT INTO `user_role` VALUES ('23', '3');
INSERT INTO `user_role` VALUES ('24', '4');
INSERT INTO `user_role` VALUES ('25', '5');
