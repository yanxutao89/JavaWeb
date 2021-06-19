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

 Date: 01/06/2021 14:53:48
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for smia_licenses
-- ----------------------------
DROP TABLE IF EXISTS `smsia_licenses`;
CREATE TABLE `smsia_licenses`  (
    `license_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '证书ID',
    `organization_id` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '组织ID',
    `license_type` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '证书类型',
    `product_name` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '产品名称',
    `license_max` int(11) NOT NULL COMMENT '证书最大值',
    `license_allocated` int(11) NULL DEFAULT NULL COMMENT '证书分配值',
    `comment` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '解释',
    PRIMARY KEY (`license_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of smia_licenses
-- ----------------------------
INSERT INTO `smsia_licenses` VALUES ('08dbe05-606e-4dad-9d33-90ef10e334f9', '442adb6e-fa58-47f3-9ca2-ed1fecdfe86c', 'core-prod', 'WildCat Application Gateway', 16, 16, NULL);
INSERT INTO `smsia_licenses` VALUES ('38777179-7094-4200-9d61-edb101c6ea84', '442adb6e-fa58-47f3-9ca2-ed1fecdfe86c', 'user', 'HR-PowerSuite', 100, 4, NULL);
INSERT INTO `smsia_licenses` VALUES ('f3831f8c-c338-4ebe-a82a-e2fc1d1ff78a', 'e254f8c-c442-4ebe-a82a-e2fc1d1ff78a', 'user', 'customer-crm-co', 100, 5, NULL);
INSERT INTO `smsia_licenses` VALUES ('t9876f8c-c338-4abc-zf6a-ttt1', 'e254f8c-c442-4ebe-a82a-e2fc1d1ff78a', 'user', 'suitability-plus', 200, 189, NULL);

SET FOREIGN_KEY_CHECKS = 1;
