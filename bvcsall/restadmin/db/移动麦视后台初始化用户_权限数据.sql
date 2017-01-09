

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



-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES ('1', '2014-08-26 17:50:22', null, '2014-08-26 17:50:22', '系统管理员', '', '系统管理员');
INSERT INTO `role` VALUES ('2', '2014-08-26 17:50:46', null, '2014-08-27 11:04:33', '上传人员', '', '上传人员');
INSERT INTO `role` VALUES ('3', '2014-08-27 11:05:20', null, '2014-08-27 11:05:20', '发布人员', '', '发布人员');
INSERT INTO `role` VALUES ('4', '2014-08-27 11:06:10', null, '2014-08-27 11:06:10', '运营专员', '', '运营专员');
INSERT INTO `role` VALUES ('5', '2014-08-27 11:06:28', null, '2014-08-27 11:06:28', '审核人员', '', '审核人员');



-- ----------------------------
-- Records of role_permission
-- ----------------------------
INSERT INTO `role_permission` VALUES ('1', '1');
INSERT INTO `role_permission` VALUES ('1', '2');
INSERT INTO `role_permission` VALUES ('1', '3');
INSERT INTO `role_permission` VALUES ('1', '4');
INSERT INTO `role_permission` VALUES ('1', '5');
INSERT INTO `role_permission` VALUES ('1', '6');
INSERT INTO `role_permission` VALUES ('1', '7');
INSERT INTO `role_permission` VALUES ('1', '8');
INSERT INTO `role_permission` VALUES ('1', '9');
INSERT INTO `role_permission` VALUES ('1', '10');
INSERT INTO `role_permission` VALUES ('1', '11');
INSERT INTO `role_permission` VALUES ('1', '12');
INSERT INTO `role_permission` VALUES ('1', '13');
INSERT INTO `role_permission` VALUES ('1', '14');
INSERT INTO `role_permission` VALUES ('1', '15');
INSERT INTO `role_permission` VALUES ('1', '16');
INSERT INTO `role_permission` VALUES ('1', '17');
INSERT INTO `role_permission` VALUES ('1', '18');
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


-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', '2014-07-29 15:16:11', '2014-08-27 13:46:22', null, null, 'meizhiwen84@163.com', '', '', null, '2014-08-27 13:46:22', '0', null, null, '96e79218965eb72c92a549dd5a330112', null, 'adminuser', 'admin', null, null);
INSERT INTO `user` VALUES ('22', '2014-08-27 11:13:38', '2014-08-27 11:41:45', null, null, null, '', '', null, '2014-08-27 11:41:45', '0', null, null, '96e79218965eb72c92a549dd5a330112', null, 'admin', 'shangchuan', null, null);
INSERT INTO `user` VALUES ('23', '2014-08-27 11:13:49', '2014-08-27 11:13:49', null, null, null, '', '', null, null, null, null, null, '96e79218965eb72c92a549dd5a330112', null, 'admin', 'fabu', null, null);
INSERT INTO `user` VALUES ('24', '2014-08-27 11:14:02', '2014-08-27 11:14:02', null, null, null, '', '', null, null, null, null, null, '96e79218965eb72c92a549dd5a330112', null, 'admin', 'yunying', null, null);
INSERT INTO `user` VALUES ('25', '2014-08-27 11:14:16', '2014-08-27 11:14:16', null, null, null, '', '', null, null, null, null, null, '96e79218965eb72c92a549dd5a330112', null, 'admin', 'shenghe', null, null);



-- ----------------------------
-- Records of user_role
-- ----------------------------
INSERT INTO `user_role` VALUES ('1', '1');
INSERT INTO `user_role` VALUES ('22', '2');
INSERT INTO `user_role` VALUES ('23', '3');
INSERT INTO `user_role` VALUES ('24', '4');
INSERT INTO `user_role` VALUES ('25', '5');
