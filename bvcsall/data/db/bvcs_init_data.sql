/*
Navicat MySQL Data Transfer

Source Server         : 10.18.20.164
Source Server Version : 50505
Source Host           : 10.18.20.164:3306
Source Database       : bvcs

Target Server Type    : MYSQL
Target Server Version : 50505
File Encoding         : 65001

Date: 2014-09-09 14:06:53
*/

SET FOREIGN_KEY_CHECKS = 0;


-- ----------------------------
-- Records of permission
-- ----------------------------
INSERT INTO `permission` VALUES ('1', '2014-08-26 17:39:34', NULL, '2014-08-27 10:56:53', NULL, '系统设置', '0', '5', '', 'BVCS', 'URI', '');
INSERT INTO `permission` VALUES ('2', '2014-08-26 17:39:47', NULL, '2014-09-01 18:37:19', NULL, '用户管理', '1', '2', '', 'BVCS', 'URI', 'user/userlist');
INSERT INTO `permission` VALUES ('3', '2014-08-26 17:55:15', NULL, '2014-08-27 10:59:22', NULL, '角色管理', '1', '3', '', 'BVCS', 'URI', 'role/rolelist');
INSERT INTO `permission` VALUES ('4', '2014-08-26 17:55:39', NULL, '2014-08-27 10:59:13', NULL, '上传账户管理', '1', '1', '', 'BVCS', 'URI', 'videoUploader/videoUploaderlist');
INSERT INTO `permission` VALUES ('5', '2014-08-26 17:56:01', NULL, '2014-08-27 10:59:27', NULL, '菜单管理', '1', '4', '', 'BVCS', 'URI', 'permission/permissionlist');
INSERT INTO `permission` VALUES ('6', '2014-08-26 17:56:15', NULL, '2014-08-27 10:56:47', NULL, '内容管理', '0', '4', '', 'BVCS', 'URI', '');
INSERT INTO `permission` VALUES ('7', '2014-08-26 17:56:30', NULL, '2014-08-27 10:59:02', NULL, '专题管理', '6', '1', '', 'BVCS', 'URI', 'subject/subjectlist');
INSERT INTO `permission` VALUES ('8', '2014-08-26 17:56:47', NULL, '2014-08-27 10:59:05', NULL, '标签管理', '6', '2', '', 'BVCS', 'URI', 'tag/taglist');
INSERT INTO `permission` VALUES ('9', '2014-08-26 17:56:57', NULL, '2014-08-27 10:56:40', NULL, '视频库', '0', '3', '', 'BVCS', 'URI', '');
INSERT INTO `permission` VALUES ('10', '2014-08-26 17:57:21', NULL, '2014-08-26 17:57:21', NULL, '全部视频', '9', '0', '', 'BVCS', 'URI', 'video/allvalidlist');
INSERT INTO `permission` VALUES ('11', '2014-08-26 17:57:32', NULL, '2014-08-27 10:56:31', NULL, '用户视频', '0', '2', '', 'BVCS', 'URI', '');
INSERT INTO `permission` VALUES ('12', '2014-08-26 17:57:54', NULL, '2014-08-27 10:58:29', NULL, '未审核视频', '11', '1', '', 'BVCS', 'URI', 'video/unchecklist');
INSERT INTO `permission` VALUES ('13', '2014-08-26 17:58:12', NULL, '2014-08-27 10:58:40', NULL, '审核通过视频', '11', '3', '', 'BVCS', 'URI', 'video/checkoklist');
INSERT INTO `permission` VALUES ('14', '2014-08-26 17:58:35', NULL, '2014-08-27 10:58:37', NULL, '审核未通过视频', '11', '2', '', 'BVCS', 'URI', 'video/checkfaillist');
INSERT INTO `permission` VALUES ('15', '2014-08-26 17:58:51', NULL, '2014-08-27 10:58:45', NULL, '已删除视频', '11', '4', '', 'BVCS', 'URI', 'video/destroyedlist');
INSERT INTO `permission` VALUES ('16', '2014-08-26 17:59:01', NULL, '2014-08-27 10:56:25', NULL, '管理员视频', '0', '1', '', 'BVCS', 'URI', '');
INSERT INTO `permission` VALUES ('17', '2014-08-26 17:59:19', NULL, '2014-08-27 10:58:17', NULL, '视频上传', '16', '1', '', 'BVCS', 'URI', 'video/index');
INSERT INTO `permission` VALUES ('18', '2014-08-26 17:59:35', NULL, '2014-08-27 10:58:21', NULL, '视频发布', '16', '2', '', 'BVCS', 'URI', 'video/checledVideos');
INSERT INTO `permission` VALUES ('21', '2014-09-09 13:43:31', NULL, '2014-09-09 13:43:39', NULL, '操作日志', '1', '4', 'valid', 'BVCS', 'URI', 'operationLog/operationLoglist');


-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES ('1', '2014-08-26 17:50:22', NULL, '2014-09-09 14:05:54', '超级管理员', '', '超级管理员');


-- ----------------------------
-- Records of role_permission
-- ----------------------------
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
INSERT INTO `role_permission` VALUES ('1', '21');



-- ----------------------------
-- Records of user
-- ----------------------------
-- 
-- Dumping data for table user
--
-- 
-- Dumping data for table user
--
INSERT INTO user (id, create_at, modify_at, activation_code, department, email, is_enabled, is_locked, locked_date, login_date, login_failure_count, login_ip, name, `password`, phone, type, username) VALUES (1, '2014-07-29 15:16:11', '2014-09-09 14:48:45', NULL, NULL, 'meizhiwen84@163.com', TRUE, FALSE, NULL, '2014-09-09 14:48:45', 0, NULL, NULL, '96e79218965eb72c92a549dd5a330112', NULL, 'adminuser', 'admin');

-- ----------------------------
-- Records of user_role
-- ----------------------------
INSERT INTO `user_role` VALUES ('1', '1');



-- ----------------------------
-- Records of wpermission
-- ----------------------------
INSERT INTO `wpermission` VALUES ('1', '2014-08-28 18:24:22', NULL, '2014-08-28 18:24:22', NULL, '视频管理', '0', '1', '', 'BVCS', 'URI', '');
INSERT INTO `wpermission` VALUES ('2', '2014-08-28 18:24:29', NULL, '2014-08-28 18:24:29', NULL, '内容管理', '0', '2', '', 'BVCS', 'URI', '');
INSERT INTO `wpermission` VALUES ('3', '2014-08-28 18:24:39', NULL, '2014-08-28 18:24:39', NULL, '系统设置', '0', '3', '', 'BVCS', 'URI', '');
INSERT INTO `wpermission` VALUES ('4', '2014-08-28 18:24:51', NULL, '2014-09-01 18:39:49', NULL, '视频上传', '1', '1', '', 'BVCS', 'URI', 'video/index');
INSERT INTO `wpermission` VALUES ('5', '2014-08-29 10:24:09', NULL, '2014-08-29 10:24:09', NULL, '视频列表', '1', '2', '', 'BVCS', 'URI', 'video/checledVideos');
INSERT INTO `wpermission` VALUES ('6', '2014-08-29 10:24:43', NULL, '2014-09-01 14:38:47', NULL, '频道管理', '15', '1', '', 'BVCS', 'URI', 'channel/channellist');
INSERT INTO `wpermission` VALUES ('7', '2014-08-29 10:25:05', NULL, '2014-09-01 14:38:59', NULL, '专题管理', '15', '2', '', 'BVCS', 'URI', 'subject/subjectlist');
INSERT INTO `wpermission` VALUES ('8', '2014-08-29 10:25:19', NULL, '2014-09-01 14:39:10', NULL, '标签管理', '15', '3', '', 'BVCS', 'URI', 'tag/taglist');
INSERT INTO `wpermission` VALUES ('9', '2014-08-29 10:25:40', NULL, '2014-08-29 10:25:40', NULL, '焦点图管理', '2', '4', '', 'BVCS', 'URI', 'fp/picSet');
INSERT INTO `wpermission` VALUES ('10', '2014-08-29 10:25:59', NULL, '2014-09-03 17:03:28', NULL, '内容管理', '2', '5', '', 'BVCS', 'URI', 'fileExplorer/index');
INSERT INTO `wpermission` VALUES ('11', '2014-08-29 10:26:14', NULL, '2014-08-29 10:26:14', NULL, '用户管理', '3', '1', '', 'BVCS', 'URI', 'user/userlist');
INSERT INTO `wpermission` VALUES ('12', '2014-08-29 10:26:33', NULL, '2014-08-29 10:26:33', NULL, '角色管理', '3', '2', '', 'BVCS', 'URI', 'role/rolelist');
INSERT INTO `wpermission` VALUES ('13', '2014-08-29 10:26:50', NULL, '2014-08-29 10:26:50', NULL, '菜单管理', '3', '3', '', 'BVCS', 'URI', 'permission/permissionlist');
INSERT INTO `wpermission` VALUES ('14', '2014-09-01 11:26:09', NULL, '2014-09-01 11:26:09', NULL, '视频发布', '1', '3', '', 'BVCS', 'URI', 'video/publishList');
INSERT INTO `wpermission` VALUES ('15', '2014-09-01 14:38:29', NULL, '2014-09-01 14:40:54', NULL, '数据管理', '0', '3', '', 'BVCS', 'URI', '');
INSERT INTO `wpermission` VALUES ('26', '2014-09-09 13:53:29', NULL, '2014-09-09 13:53:44', NULL, '操作日志', '3', '4', 'valid', 'BVCS', 'URI', 'operationLog/operationLoglist');


-- ----------------------------
-- Records of wrole
-- ----------------------------
INSERT INTO `wrole` VALUES ('1', '2014-08-28 18:25:05', NULL, '2014-08-28 18:25:05', '超级管理员', '', '超级管理员');


-- ----------------------------
-- Records of wrole_wpermission
-- ----------------------------
INSERT INTO `wrole_wpermission` VALUES ('1', '4');
INSERT INTO `wrole_wpermission` VALUES ('1', '5');
INSERT INTO `wrole_wpermission` VALUES ('1', '6');
INSERT INTO `wrole_wpermission` VALUES ('1', '7');
INSERT INTO `wrole_wpermission` VALUES ('1', '8');
INSERT INTO `wrole_wpermission` VALUES ('1', '9');
INSERT INTO `wrole_wpermission` VALUES ('1', '10');
INSERT INTO `wrole_wpermission` VALUES ('1', '11');
INSERT INTO `wrole_wpermission` VALUES ('1', '12');
INSERT INTO `wrole_wpermission` VALUES ('1', '13');
INSERT INTO `wrole_wpermission` VALUES ('1', '14');
INSERT INTO `wrole_wpermission` VALUES ('1', '26');


-- ----------------------------
-- Records of wuser
-- ----------------------------
INSERT INTO `wuser` VALUES ('1', '2014-08-28 18:20:30', NULL, '2014-09-09 14:00:37', NULL, NULL, 'meizhiwen84@163.com', '', '', '2014-08-28 18:20:13', '2014-09-09 14:00:37', '0', NULL, 'admin', '96e79218965eb72c92a549dd5a330112', NULL, 'adminuser', 'admin');


-- ----------------------------
-- Records of wuser_wrole
-- ----------------------------
INSERT INTO `wuser_wrole` VALUES ('1', '1');
