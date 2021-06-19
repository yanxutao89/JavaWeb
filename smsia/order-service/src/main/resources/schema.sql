/*
 Navicat Premium Data Transfer

 Source Server         : 127.0.0.1-3306
 Source Server Type    : MySQL
 Source Server Version : 80011
 Source Host           : localhost:3306
 Source Schema         : java_web

 Target Server Type    : MySQL
 Target Server Version : 80011
 File Encoding         : 65001

 Date: 02/06/2021 14:28:49
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for smsia_organizations
-- ----------------------------
DROP TABLE IF EXISTS `smsia_organizations`;
CREATE TABLE `smsia_organizations`  (
    `organization_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '组织ID',
    `name` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '名称',
    `contact_name` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '联系人名称',
    `contact_email` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '联系人邮箱',
    `contact_phone` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '联系电话',
    PRIMARY KEY (`organization_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of smsia_organizations
-- ----------------------------
INSERT INTO `smsia_organizations` VALUES ('442adb6e-fa58-47f3-9ca2-ed1fecdfe86c', 'HR-PowerSuite', 'Doug Drewry', 'doug.drewry@hr.com', '920-555-1212');
INSERT INTO `smsia_organizations` VALUES ('e254f8c-c442-4ebe-a82a-e2fc1d1ff78a', 'customer-crm-co', 'Mark Balster', 'mark.balster@custcrmco.com', '823-555-1212');

SET FOREIGN_KEY_CHECKS = 1;
