USE `account`;

CREATE TABLE IF NOT EXISTS `application`
(
    `id`             VARCHAR(32)    NOT NULL COMMENT '应用ID',
    `appellation`    VARCHAR(32)    COMMENT '应用名称',
    `plugin_name`    VARCHAR(32)    COMMENT '插件名称',
    `description`    VARCHAR(255)   COMMENT '详细描述',
    `creator_id`     VARCHAR(32)    COMMENT '创建者ID',
    `create_time`    BIGINT(16)     COMMENT '创建时间',
    `updater_id`     VARCHAR(32)    DEFAULT NULL COMMENT '更新者ID',
    `update_time`    BIGINT(16)     DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
)
    ENGINE = InnoDB
    CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_unicode_ci
    ROW_FORMAT = Dynamic
    COMMENT = '【应用】表';

ALTER TABLE `application`
ADD UNIQUE INDEX `app_name_unique` (`appellation`);

-- 创建系统应用
INSERT INTO `application`
(`id`, `appellation`, `plugin_name`, `description`, `creator_id`, `create_time`)
VALUES
('0000', 'ACCOUNT', 'manager', '账号中心，提供系统的账号管理服务', 'zhengqingwen', 0);

CREATE TABLE IF NOT EXISTS `api`
(
    `id`             VARCHAR(32)    NOT NULL COMMENT '接口ID',
    `app_id`         VARCHAR(32)    COMMENT '应用ID',
    `router`         VARCHAR(255)   COMMENT '接口路由',
    `access_rule`    INT(8)         COMMENT '访问规则',
    `appellation`    VARCHAR(255)   COMMENT '功能名称',
    `description`    VARCHAR(255)   COMMENT '详细描述',
    `creator_id`     VARCHAR(32)    COMMENT '创建者ID',
    `create_time`    BIGINT(16)     COMMENT '创建时间',
    `updater_id`     VARCHAR(32)    DEFAULT NULL COMMENT '更新者ID',
    `update_time`    BIGINT(16)     DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
)
    ENGINE = InnoDB
    CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_unicode_ci
    ROW_FORMAT = Dynamic
    COMMENT = '【接口】表';

ALTER TABLE `api`
ADD UNIQUE INDEX `api_name_unique` (`appellation`),
ADD INDEX `api_app_id_common` (`app_id`);

CREATE TABLE IF NOT EXISTS `provider`
(
    `id`             VARCHAR(32)    NOT NULL COMMENT '服务ID',
    `app_id`         VARCHAR(32)    COMMENT '应用ID',
    `identifier`     VARCHAR(32)    COMMENT '服务编号',
    `entrance`       VARCHAR(255)   COMMENT '访问入口',
    `description`    VARCHAR(255)   COMMENT '详细描述',
    `creator_id`     VARCHAR(32)    COMMENT '创建者ID',
    `create_time`    BIGINT(16)     COMMENT '创建时间',
    `updater_id`     VARCHAR(32)    DEFAULT NULL COMMENT '更新者ID',
    `update_time`    BIGINT(16)     DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
)
    ENGINE = InnoDB
    CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_unicode_ci
    ROW_FORMAT = Dynamic
    COMMENT = '【服务】表';

ALTER TABLE `provider`
ADD UNIQUE INDEX `provider_entrance_unique` (`entrance`),
ADD INDEX `provider_app_id_common` (`app_id`);

CREATE TABLE IF NOT EXISTS `parameter`
(
    `id`             VARCHAR(32)    NOT NULL COMMENT '参数ID',
    `app_id`         VARCHAR(32)    COMMENT '应用ID',
    `appellation`    VARCHAR(64)    COMMENT '参数名',
    `default_value`  VARCHAR(255)   COMMENT '默认值',
    `description`    VARCHAR(255)   COMMENT '详细描述',
    `creator_id`     VARCHAR(32)    COMMENT '创建者ID',
    `create_time`    BIGINT(16)     COMMENT '创建时间',
    `updater_id`     VARCHAR(32)    DEFAULT NULL COMMENT '更新者ID',
    `update_time`    BIGINT(16)     DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
)
    ENGINE = InnoDB
    CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_unicode_ci
    ROW_FORMAT = Dynamic
    COMMENT = '【参数】表';

ALTER TABLE `parameter`
ADD UNIQUE INDEX `param_name_unique` (`appellation`),
ADD INDEX `param_app_id_common` (`app_id`);

-- 创建系统参数
INSERT INTO `parameter`
(`id`, `app_id`, `appellation`, `default_value`, `description`, `creator_id`, `create_time`)
VALUES
('0000', '0000', 'parameter-update-interval', '24',  '服务器系统参数的更新间隔，单位为(hour)', 'zhengqingwen', 0),
('0001', '0000', 'token-manage-number',       '512', 'Token管理的最大数目，即用户的最大登录数', 'zhengqingwen', 0),
('0002', '0000', 'token-manage-duration',     '60',  'Token的有效时间，单位为(min)',        'zhengqingwen', 0);

CREATE TABLE IF NOT EXISTS `param_value`
(
    `id`             VARCHAR(32)    NOT NULL COMMENT '参数值ID',
    `param_id`       VARCHAR(32)    COMMENT '参数ID',
    `provider_id`    VARCHAR(32)    COMMENT '服务ID',
    `content`        VARCHAR(255)   COMMENT '参数值',
    `description`    VARCHAR(255)   COMMENT '详细描述',
    `creator_id`     VARCHAR(32)    COMMENT '创建者ID',
    `create_time`    BIGINT(16)     COMMENT '创建时间',
    `updater_id`     VARCHAR(32)    DEFAULT NULL COMMENT '更新者ID',
    `update_time`    BIGINT(16)     DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
)
    ENGINE = InnoDB
    CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_unicode_ci
    ROW_FORMAT = Dynamic
    COMMENT = '【参数值】表';

ALTER TABLE `param_value`
ADD INDEX `param_value_unique_combine` (`provider_id`, `param_id`);

CREATE TABLE IF NOT EXISTS `role`
(
    `id`             VARCHAR(32)    NOT NULL COMMENT '角色ID',
    `maximum`        INT(8)         COMMENT '最大成员数',
    `appellation`    VARCHAR(32)    COMMENT '角色的名称',
    `description`    VARCHAR(255)   COMMENT '角色的描述',
    `creator_id`     VARCHAR(32)    COMMENT '创建者ID',
    `create_time`    BIGINT(16)     COMMENT '创建时间',
    `updater_id`     VARCHAR(32)    DEFAULT NULL COMMENT '更新者ID',
    `update_time`    BIGINT(16)     DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
)
    ENGINE = InnoDB
    CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_unicode_ci
    ROW_FORMAT = Dynamic
    COMMENT = '【角色】表';

ALTER TABLE `role`
ADD UNIQUE INDEX `role_name_unique` (`appellation`);

-- 创建系统角色
INSERT INTO `role`
(`id`, `maximum`, `appellation`, `description`, `creator_id`, `create_time`)
VALUES
('mine', 1, 'system_owner', '系统所有者兼终极管理员角色', 'zhengqingwen', 0),
('0000', 0, 'unknown_user', '未登录的用户角色即访客身份', 'zhengqingwen', 0),
('1111', 9, 'plain_member', '系统唯一开放注册的普通用户', 'zhengqingwen', 0);

CREATE TABLE IF NOT EXISTS `member`
(
    `id`             VARCHAR(32)    NOT NULL COMMENT '用户ID',
    `nickname`       VARCHAR(16)    COMMENT '用户昵称',
    `role_id`        VARCHAR(32)    COMMENT '角色ID',
    `authentication` VARCHAR(255)   COMMENT '加密密码',
    `depository`     VARCHAR(255)   COMMENT '文件仓库',
    `creator_id`     VARCHAR(32)    COMMENT '创建者ID',
    `create_time`    BIGINT(16)     COMMENT '创建时间',
    `updater_id`     VARCHAR(32)    DEFAULT NULL COMMENT '更新者ID',
    `update_time`    BIGINT(16)     DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
)
    ENGINE = InnoDB
    CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_unicode_ci
    ROW_FORMAT = Dynamic
    COMMENT = '【用户】表';

ALTER TABLE `member`
ADD UNIQUE INDEX `member_nickname_unique` (`nickname`);

-- 创建系统账号
INSERT INTO `member`
(`id`, `nickname`, `authentication`, `role_id`, `creator_id`, `create_time`)
VALUES
('zhengqingwen', 'WIS', 'e10adc3949ba59abbe56e057f20f883e', 'mine', 'zhengqingwen', 0);

CREATE TABLE IF NOT EXISTS `authority`
(
    `id`             VARCHAR(32)    NOT NULL COMMENT '权限ID',
    `role_id`        VARCHAR(32)    COMMENT '角色ID',
    `api_id`         VARCHAR(32)    COMMENT '接口ID',
    `creator_id`     VARCHAR(32)    COMMENT '创建者ID',
    `create_time`    BIGINT(16)     COMMENT '创建时间',
    `updater_id`     VARCHAR(32)    DEFAULT NULL COMMENT '更新者ID',
    `update_time`    BIGINT(16)     DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
)
    ENGINE = InnoDB
    CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_unicode_ci
    ROW_FORMAT = Dynamic
    COMMENT = '【权限】表';

ALTER TABLE `authority`
ADD INDEX `authority_unique_combine` (`api_id`, `role_id`);
