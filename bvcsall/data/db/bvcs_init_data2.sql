SET FOREIGN_KEY_CHECKS=0;


-- 
-- Dumping data for table permission
--
INSERT INTO permission VALUES 
  (1, '2014-08-26 17:39:34', NULL, '2014-08-27 10:56:53', NULL, '系统设置', 0, 5, '', 'BVCS', 'URI', ''),
  (2, '2014-08-26 17:39:47', NULL, '2014-09-01 18:37:19', NULL, '用户管理', 1, 2, '', 'BVCS', 'URI', 'user/userlist'),
  (3, '2014-08-26 17:55:15', NULL, '2014-08-27 10:59:22', NULL, '角色管理', 1, 3, '', 'BVCS', 'URI', 'role/rolelist'),
  (4, '2014-08-26 17:55:39', NULL, '2014-08-27 10:59:13', NULL, '上传账户管理', 1, 1, '', 'BVCS', 'URI', 'videoUploader/videoUploaderlist'),
  (5, '2014-08-26 17:56:01', NULL, '2014-08-27 10:59:27', NULL, '菜单管理', 1, 4, '', 'BVCS', 'URI', 'permission/permissionlist'),
  (6, '2014-08-26 17:56:15', NULL, '2014-08-27 10:56:47', NULL, '内容管理', 0, 4, '', 'BVCS', 'URI', ''),
  (7, '2014-08-26 17:56:30', NULL, '2014-08-27 10:59:02', NULL, '专题管理', 6, 1, '', 'BVCS', 'URI', 'subject/subjectlist'),
  (8, '2014-08-26 17:56:47', NULL, '2014-08-27 10:59:05', NULL, '标签管理', 6, 2, '', 'BVCS', 'URI', 'tag/taglist'),
  (9, '2014-08-26 17:56:57', NULL, '2014-08-27 10:56:40', NULL, '视频库', 0, 3, '', 'BVCS', 'URI', ''),
  (10, '2014-08-26 17:57:21', NULL, '2014-08-26 17:57:21', NULL, '全部视频', 9, 0, '', 'BVCS', 'URI', 'video/allvalidlist'),
  (11, '2014-08-26 17:57:32', NULL, '2014-08-27 10:56:31', NULL, '用户视频', 0, 2, '', 'BVCS', 'URI', ''),
  (12, '2014-08-26 17:57:54', NULL, '2014-08-27 10:58:29', NULL, '未审核视频', 11, 1, '', 'BVCS', 'URI', 'video/unchecklist'),
  (13, '2014-08-26 17:58:12', NULL, '2014-08-27 10:58:40', NULL, '审核通过视频', 11, 3, '', 'BVCS', 'URI', 'video/checkoklist'),
  (14, '2014-08-26 17:58:35', NULL, '2014-08-27 10:58:37', NULL, '审核未通过视频', 11, 2, '', 'BVCS', 'URI', 'video/checkfaillist'),
  (15, '2014-08-26 17:58:51', NULL, '2014-08-27 10:58:45', NULL, '已删除视频', 11, 4, '', 'BVCS', 'URI', 'video/destroyedlist'),
  (16, '2014-08-26 17:59:01', NULL, '2014-08-27 10:56:25', NULL, '管理员视频', 0, 1, '', 'BVCS', 'URI', ''),
  (17, '2014-08-26 17:59:19', NULL, '2014-08-27 10:58:17', NULL, '视频上传', 16, 1, '', 'BVCS', 'URI', 'video/index'),
  (18, '2014-08-26 17:59:35', NULL, '2014-08-27 10:58:21', NULL, '视频发布', 16, 2, '', 'BVCS', 'URI', 'video/checledVideos'),
  (21, '2014-09-09 13:43:31', NULL, '2014-09-09 13:43:39', NULL, '操作日志', 1, 4, 'valid', 'BVCS', 'URI', 'operationLog/operationLoglist');


INSERT INTO role VALUES 
  (1, '2014-08-26 17:50:22', NULL, '2014-09-09 14:05:54', '超级管理员', True, '超级管理员');

INSERT INTO role_permission VALUES 
  (1, 2),
  (1, 3),
  (1, 4),
  (1, 5),
  (1, 7),
  (1, 8),
  (1, 10),
  (1, 12),
  (1, 13),
  (1, 14),
  (1, 15),
  (1, 17),
  (1, 18),
  (1, 21);


INSERT INTO user VALUES 
  (1, '2014-07-29 15:16:11', '2014-09-09 14:48:45', NULL, NULL, 'meizhiwen84@163.com', True, False, NULL, '2014-09-09 14:48:45', 0, NULL, NULL, '96e79218965eb72c92a549dd5a330112', NULL, 'adminuser', 'admin', NULL, NULL);

INSERT INTO user_role VALUES 
  (1, 1);


INSERT INTO wpermission VALUES 
  (1, '2014-08-28 18:24:22', NULL, '2014-08-28 18:24:22', NULL, '视频管理', 0, 1, '', 'BVCS', 'URI', ''),
  (2, '2014-08-28 18:24:29', NULL, '2014-08-28 18:24:29', NULL, '内容管理', 0, 2, '', 'BVCS', 'URI', ''),
  (3, '2014-08-28 18:24:39', NULL, '2014-08-28 18:24:39', NULL, '系统设置', 0, 3, '', 'BVCS', 'URI', ''),
  (4, '2014-08-28 18:24:51', NULL, '2014-09-01 18:39:49', NULL, '视频上传', 1, 1, '', 'BVCS', 'URI', 'video/index'),
  (5, '2014-08-29 10:24:09', NULL, '2014-08-29 10:24:09', NULL, '视频列表', 1, 2, '', 'BVCS', 'URI', 'video/checledVideos'),
  (6, '2014-08-29 10:24:43', NULL, '2014-09-01 14:38:47', NULL, '频道管理', 15, 1, '', 'BVCS', 'URI', 'channel/channellist'),
  (7, '2014-08-29 10:25:05', NULL, '2014-09-01 14:38:59', NULL, '专题管理', 15, 2, '', 'BVCS', 'URI', 'subject/subjectlist'),
  (8, '2014-08-29 10:25:19', NULL, '2014-09-01 14:39:10', NULL, '标签管理', 15, 3, '', 'BVCS', 'URI', 'tag/taglist'),
  (9, '2014-08-29 10:25:40', NULL, '2014-08-29 10:25:40', NULL, '焦点图管理', 2, 4, '', 'BVCS', 'URI', 'fp/picSet'),
  (10, '2014-08-29 10:25:59', NULL, '2014-09-03 17:03:28', NULL, '内容管理', 2, 5, '', 'BVCS', 'URI', 'fileExplorer/index'),
  (11, '2014-08-29 10:26:14', NULL, '2014-08-29 10:26:14', NULL, '用户管理', 3, 1, '', 'BVCS', 'URI', 'user/userlist'),
  (12, '2014-08-29 10:26:33', NULL, '2014-08-29 10:26:33', NULL, '角色管理', 3, 2, '', 'BVCS', 'URI', 'role/rolelist'),
  (13, '2014-08-29 10:26:50', NULL, '2014-08-29 10:26:50', NULL, '菜单管理', 3, 3, '', 'BVCS', 'URI', 'permission/permissionlist'),
  (14, '2014-09-01 11:26:09', NULL, '2014-09-01 11:26:09', NULL, '视频发布', 1, 3, '', 'BVCS', 'URI', 'video/publishList'),
  (15, '2014-09-01 14:38:29', NULL, '2014-09-01 14:40:54', NULL, '数据管理', 0, 3, '', 'BVCS', 'URI', ''),
  (26, '2014-09-09 13:53:29', NULL, '2014-09-09 13:53:44', NULL, '操作日志', 3, 4, 'valid', 'BVCS', 'URI', 'operationLog/operationLoglist');


INSERT INTO wrole VALUES 
  (1, '2014-08-28 18:25:05', NULL, '2014-08-28 18:25:05', '超级管理员', True, '超级管理员');


INSERT INTO wrole_wpermission VALUES 
  (1, 4),
  (1, 5),
  (1, 6),
  (1, 7),
  (1, 8),
  (1, 9),
  (1, 10),
  (1, 11),
  (1, 12),
  (1, 13),
  (1, 14),
  (1, 26);


INSERT INTO wuser VALUES 
  (1, '2014-08-28 18:20:30', NULL, '2014-09-09 14:41:37', NULL, NULL, 'meizhiwen84@163.com', True, False, '2014-08-28 18:20:13', '2014-09-09 14:41:37', 0, NULL, 'admin', '96e79218965eb72c92a549dd5a330112', NULL, 'adminuser', 'admin');


INSERT INTO wuser_wrole VALUES 
  (1, 1);

