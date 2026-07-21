ALTER TABLE user
    ADD COLUMN email VARCHAR(128) NULL COMMENT '邮箱（登录账号）' AFTER openid,
    ADD COLUMN password VARCHAR(255) NULL COMMENT 'BCrypt 加密后的密码' AFTER email;

-- 邮箱唯一，防止重复注册
ALTER TABLE user
    ADD UNIQUE INDEX uk_user_email (email);