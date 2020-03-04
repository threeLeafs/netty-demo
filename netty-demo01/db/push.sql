/*
 Navicat Premium Data Transfer

 Source Server         : 本地
 Source Server Type    : MySQL
 Source Server Version : 50719
 Source Host           : localhost:3306
 Source Schema         : push

 Target Server Type    : MySQL
 Target Server Version : 50719
 File Encoding         : 65001

 Date: 09/12/2019 16:59:45
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for access_token
-- ----------------------------
DROP TABLE IF EXISTS `access_token`;
CREATE TABLE `access_token` (
  `pk_id` varchar(31) COLLATE utf8mb4_bin NOT NULL,
  `token` varchar(127) COLLATE utf8mb4_bin NOT NULL,
  `client_type` varchar(31) COLLATE utf8mb4_bin NOT NULL,
  `access_key` varchar(31) COLLATE utf8mb4_bin NOT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `exp_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`pk_id`),
  UNIQUE KEY `access_token_token_uindex` (`token`),
  KEY `access_token_clientType_access_key_index` (`client_type`,`access_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='token验证表';

-- ----------------------------
-- Records of access_token
-- ----------------------------
BEGIN;
INSERT INTO `access_token` VALUES ('1203203909015912449', 'e3452403-a657-40db-8904-9ec1666cfbfe', 'APP', 'root', '2019-12-07 14:45:41', '2019-12-07 14:55:41');
INSERT INTO `access_token` VALUES ('1203204448000753665', '17fd9fa4-8dc5-49ac-a62f-be66904cf30b', 'APP', 'root', '2019-12-07 14:47:50', '2019-12-07 14:57:50');
INSERT INTO `access_token` VALUES ('1203232546222047234', '0feb2f7e-aa42-4c5b-b023-5935109c18c3', 'APP', 'root', '2019-12-07 16:39:29', '2019-12-07 16:49:29');
INSERT INTO `access_token` VALUES ('1203235473074479105', '9f9b145e-7310-4c78-8990-53cb531c449d', 'APP', 'root', '2019-12-07 16:51:06', '2019-12-07 17:01:06');
INSERT INTO `access_token` VALUES ('1203237286448312322', '30ebd98d-c07d-4410-86a1-4df3d7ca072b', 'APP', 'root', '2019-12-07 16:58:19', '2019-12-07 17:08:19');
INSERT INTO `access_token` VALUES ('1203238133030703105', '2b8d521c-e247-48bd-9867-19053f139aff', 'APP', 'root', '2019-12-07 17:01:41', '2019-12-07 17:11:41');
INSERT INTO `access_token` VALUES ('1203238214672830466', '5e631524-755c-4c91-91d1-29878ba431e5', 'APP', 'root', '2019-12-07 17:02:00', '2019-12-07 17:12:00');
INSERT INTO `access_token` VALUES ('1203238332398555138', '69b8f6c5-6a82-460c-8ebe-49097e0f5690', 'APP', 'root', '2019-12-07 17:02:28', '2019-12-07 17:12:28');
INSERT INTO `access_token` VALUES ('1203238378426847233', 'f2a2e72d-4505-4f01-98c7-f168c9525258', 'APP', 'root', '2019-12-07 17:02:39', '2019-12-07 17:12:39');
INSERT INTO `access_token` VALUES ('1203238522115313666', 'bed8e60d-a703-479b-a31c-547127c35a4b', 'APP', 'root', '2019-12-07 17:03:13', '2019-12-07 17:13:13');
INSERT INTO `access_token` VALUES ('1203240773210923010', 'dda94d0e-ca36-48e2-a303-21b2d9280d52', 'APP', 'root', '2019-12-07 17:12:10', '2019-12-07 17:22:10');
COMMIT;

-- ----------------------------
-- Table structure for log_push_task
-- ----------------------------
DROP TABLE IF EXISTS `log_push_task`;
CREATE TABLE `log_push_task` (
  `pk_id` varchar(31) COLLATE utf8mb4_bin NOT NULL,
  `push_id` varchar(63) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '一次推送请求，一条记录',
  `channel_id` varchar(31) COLLATE utf8mb4_bin NOT NULL DEFAULT '',
  `client_type` varchar(31) COLLATE utf8mb4_bin NOT NULL DEFAULT '',
  `app` varchar(31) COLLATE utf8mb4_bin NOT NULL DEFAULT '',
  `user` varchar(31) COLLATE utf8mb4_bin NOT NULL DEFAULT '',
  `group` varchar(127) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '群组',
  `area_code` varchar(31) COLLATE utf8mb4_bin NOT NULL DEFAULT '',
  `country` varchar(31) COLLATE utf8mb4_bin NOT NULL DEFAULT 'CN',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `status` int(11) NOT NULL COMMENT '0-待推送\n1-推送成功\n2-推送失败',
  `content` text COLLATE utf8mb4_bin NOT NULL,
  `push_type` varchar(15) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '推送类型',
  PRIMARY KEY (`pk_id`),
  KEY `log_push_task_app_index` (`app`),
  KEY `log_push_task_client_type_index` (`client_type`),
  KEY `log_push_task_group_index` (`group`),
  KEY `log_push_task_user_index` (`user`),
  KEY `log_push_task_push_type_index` (`push_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='推送日志';

-- ----------------------------
-- Records of log_push_task
-- ----------------------------
BEGIN;
INSERT INTO `log_push_task` VALUES ('1203899911880327169', '123', '0e6b9492', '', '', 'zln', '', '', '', '2019-12-09 12:51:21', 1, '{\"code\":200,\"data\":\"随便推送点什么内容吧1111\",\"date\":\"2019-12-09 12:51:21\",\"id\":\"123\",\"msg\":\"正常\",\"pushType\":\"1\"}', '1');
INSERT INTO `log_push_task` VALUES ('1203928571962241026', '123', '077f2744', '', '', 'zln', '', '', '', '2019-12-09 14:45:14', 1, '{\"code\":200,\"data\":\"随便推送点什么内容吧1111\",\"date\":\"2019-12-09 14:45:14\",\"id\":\"123\",\"msg\":\"正常\",\"pushType\":\"1\"}', '1');
INSERT INTO `log_push_task` VALUES ('1203942281112055810', '7a8e6b11-96bc-45e1-a254-7acdafe0fad0', '16792b25', '', '', 'zln', '', '', '', '2019-12-09 15:39:42', 1, '{\"code\":200,\"data\":\"随便推送点什么内容吧1111\",\"date\":\"2019-12-09 15:39:42\",\"id\":\"7a8e6b11-96bc-45e1-a254-7acdafe0fad0\",\"msg\":\"正常\",\"pushType\":\"1\"}', '1');
INSERT INTO `log_push_task` VALUES ('1203961766933991426', '13fde3f1-8335-4963-9e52-ec62a0bb64ca', '46efca06', '', '', 'zln', '', '', '', '2019-12-09 16:57:08', 1, '{\"code\":200,\"data\":\"随便推送点什么内容吧1111\",\"date\":\"2019-12-09 16:57:08\",\"id\":\"13fde3f1-8335-4963-9e52-ec62a0bb64ca\",\"msg\":\"正常\",\"pushType\":\"1\"}', '1');
COMMIT;

-- ----------------------------
-- Table structure for log_ws_connect
-- ----------------------------
DROP TABLE IF EXISTS `log_ws_connect`;
CREATE TABLE `log_ws_connect` (
  `pk_id` varchar(31) COLLATE utf8mb4_bin NOT NULL,
  `channel_id` varchar(15) COLLATE utf8mb4_bin NOT NULL,
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '0-已连接\n1-已注册，只有此状态的连接才是有效的\n2-注册失败\n3-已下线',
  `token` varchar(63) COLLATE utf8mb4_bin NOT NULL,
  `client_type` varchar(31) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '客户端类型，描述清楚自己是哪个平台的，Web、APP、PAD',
  `app` varchar(31) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '哪个应用，要么是我们系统的应用，要么是others第三方的',
  `user` varchar(31) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '用户名，app+user，唯一标记到一个WebSocket客户端',
  `group` varchar(31) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '群组',
  `area_code` varchar(31) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '区域编码，取国标，默认为空字符串',
  `country` varchar(7) COLLATE utf8mb4_bin NOT NULL DEFAULT 'CN',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `server_host` varchar(127) COLLATE utf8mb4_bin NOT NULL DEFAULT '',
  `server_port` int(11) NOT NULL DEFAULT '10002',
  `instance_flag` varchar(127) COLLATE utf8mb4_bin NOT NULL COMMENT '实例标识，规则自定义',
  PRIMARY KEY (`pk_id`),
  KEY `log_ws_connect_app_index` (`app`),
  KEY `log_ws_connect_area_code_index` (`area_code`),
  KEY `log_ws_connect_client_type_index` (`client_type`),
  KEY `log_ws_connect_country_index` (`country`),
  KEY `log_ws_connect_group_index` (`group`),
  KEY `log_ws_connect_user_index` (`user`),
  KEY `log_ws_connect_channel_id_index` (`channel_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='客户端ws连接日志';

-- ----------------------------
-- Records of log_ws_connect
-- ----------------------------
BEGIN;
INSERT INTO `log_ws_connect` VALUES ('1203961676278304770', '46efca06', 1, 'testToken', '', '', 'zln', '', '', 'CN', '2019-12-09 16:56:47', '2019-12-09 16:56:47', '127.0.0.1', 10002, '127.0.0.1:10002');
COMMIT;

-- ----------------------------
-- Table structure for reg_user
-- ----------------------------
DROP TABLE IF EXISTS `reg_user`;
CREATE TABLE `reg_user` (
  `pk_id` varchar(31) COLLATE utf8mb4_bin NOT NULL COMMENT '主键，使用雪花算法生成',
  `client_type` varchar(31) COLLATE utf8mb4_bin NOT NULL DEFAULT 'DEFAULT' COMMENT '客户端类型',
  `access_key` varchar(31) COLLATE utf8mb4_bin NOT NULL COMMENT '认证key',
  `access_secret` varchar(31) COLLATE utf8mb4_bin NOT NULL COMMENT '认证密码，前期为了方便可以使用明文',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '账号状态\n\n0-未启用\n1-启用\n2-已禁用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`pk_id`),
  UNIQUE KEY `reg_user_access_key_uindex` (`access_key`),
  KEY `reg_user_client_type_index` (`client_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='注册用户，即允许使用推送服务的账号';

-- ----------------------------
-- Records of reg_user
-- ----------------------------
BEGIN;
INSERT INTO `reg_user` VALUES ('1', 'APP', 'root', '123456', 1, '2019-12-07 14:19:57', '2019-12-07 14:19:57');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
