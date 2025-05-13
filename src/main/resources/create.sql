CREATE TABLE "user" (
                        id Bigint PRIMARY KEY,
                        user_name VARCHAR(255),
                        voucher VARCHAR(255),
                        phone VARCHAR(20),
                        pic VARCHAR(512),
                        create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        used SMALLINT DEFAULT 1,
                        CONSTRAINT chk_used CHECK (used IN (0, 1, 2))
);

COMMENT ON TABLE "user" IS '用户表';
COMMENT ON COLUMN "user".user_name IS '用户名';
COMMENT ON COLUMN "user".voucher IS '登录凭证';
COMMENT ON COLUMN "user".phone IS '手机号';
COMMENT ON COLUMN "user".pic IS '用户头像URL';
COMMENT ON COLUMN "user".create_time IS '创建时间';
COMMENT ON COLUMN "user".update_time IS '修改时间';
COMMENT ON COLUMN "user".used IS '账户状态: 0-注销 1-可用 2-停用';

CREATE INDEX idx_user_phone ON "user" (phone);


-- 键表 (key_table) - 存储键的元数据信息
CREATE TABLE key_table (
                           id BIGSERIAL PRIMARY KEY,  -- 主键ID，自增长
                           key_name VARCHAR(128) NOT NULL,  -- 键名称，不可为空
                           is_multiple BOOLEAN NOT NULL DEFAULT FALSE,  -- 标识是否支持一对多关系
                           owner_id BIGINT NOT NULL,  -- 所有者用户ID
                           current_value  VARCHAR(512),
                           permission_level int2 NOT NULL DEFAULT 0 CHECK (permission_level IN (-1, 0, 1)),  -- 权限级别：公开/受保护/私有
                           is_deleted BOOLEAN NOT NULL DEFAULT FALSE,  -- 软删除标记
                           created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,  -- 创建时间
                           updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,  -- 更新时间
                           created_by BIGINT NOT NULL,  -- 创建人ID
                           updated_by BIGINT NOT NULL  -- 更新人ID
);

-- 值表 (value_table) - 存储键对应的实际值数据
CREATE TABLE value_table (
                             id BIGSERIAL PRIMARY KEY,  -- 主键ID，自增长
                             key_id BIGINT NOT NULL ,  -- 外键，关联键表ID
                             value_data varchar(512) NOT NULL,  -- 存储的值数据
                             version INTEGER NOT NULL DEFAULT 1,  -- 版本号，用于乐观锁
                             created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,  -- 创建时间
                             updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,  -- 更新时间
                             created_by BIGINT NOT NULL,  -- 创建人ID
                             updated_by BIGINT NOT NULL  -- 更新人ID
);


-- 创建索引提高查询性能
CREATE INDEX idx_key_table_owner ON key_table(owner_id);  -- 按所有者查询优化
CREATE INDEX idx_value_table_key_id ON value_table(key_id);  -- 按键ID查询优化


COMMENT ON TABLE key_table IS '存储键的元数据信息，包括权限控制和版本管理';
COMMENT ON TABLE value_table IS '存储键对应的实际值数据，支持版本控制和软删除';


-- 为key_table列添加说明注释
COMMENT ON COLUMN key_table.id IS '主键ID，自增长';
COMMENT ON COLUMN key_table.key_name IS '键名称，不可为空';
COMMENT ON COLUMN key_table.is_multiple IS '标识是否支持一对多关系';
COMMENT ON COLUMN key_table.owner_id IS '所有者用户ID';
COMMENT ON COLUMN key_table.current_value IS '当前值,对于multiple可为空';
COMMENT ON COLUMN key_table.permission_level IS '权限级别：1(完全公开)/0(公开)/-1(私有)';
COMMENT ON COLUMN key_table.is_deleted IS '软删除标记，TRUE表示已删除';


-- 为value_table列添加说明注释
COMMENT ON COLUMN value_table.id IS '主键ID，自增长';
COMMENT ON COLUMN value_table.key_id IS '外键，关联key_table.id';
COMMENT ON COLUMN value_table.value_data IS '存储的值数据，TEXT类型';
COMMENT ON COLUMN value_table.version IS '版本号';



-- 记录表 - 存储剪切板记录元信息
CREATE TABLE clipboard_history (
                                   id BIGSERIAL PRIMARY KEY,
                                   user_id BIGINT NOT NULL,
                                   created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
                                   system VARCHAR(50) ,
                                   tag_name VARCHAR(100) ,
                                   value_id BIGINT NOT NULL
);

-- 修改值表结构，使用固定长度的二进制存储MD5
CREATE TABLE clipboard_values (
                                  id BIGSERIAL PRIMARY KEY,
                                  content TEXT NOT NULL ,
                                  content_md5 BYTEA NOT NULL CHECK (octet_length(content_md5) = 16) ,
                                  UNIQUE (content_md5)
);


-- 创建索引优化查询性能
CREATE INDEX idx_history_user ON clipboard_history(user_id);
CREATE INDEX idx_history_time ON clipboard_history(created_at);
-- 创建哈希索引
CREATE INDEX idx_values_md5 ON clipboard_values USING HASH (content_md5);




-- 为表添加说明注释
COMMENT ON TABLE clipboard_history IS '剪切板历史记录表，存储用户剪切操作的元数据';
COMMENT ON TABLE clipboard_values IS '剪切板内容存储表，保证相同内容只存一份';

-- 为clipboard_history列添加注释
COMMENT ON COLUMN clipboard_history.user_id IS '关联的用户ID';
COMMENT ON COLUMN clipboard_history.created_at IS '记录创建时间，自动填充';
COMMENT ON COLUMN clipboard_history.system IS '来源操作系统，如Windows/MacOS/Linux';
COMMENT ON COLUMN clipboard_history.tag_name IS '终端设备名称或标识';
COMMENT ON COLUMN clipboard_history.value_id IS '关联clipboard_values表的外键';

-- 为clipboard_values列添加注释
COMMENT ON COLUMN clipboard_values.content IS '实际的剪切板内容';
COMMENT ON COLUMN clipboard_values.content_md5 IS '16字节固定长度的MD5哈希值(二进制格式)，用于快速比对';
COMMENT ON INDEX idx_values_md5 IS '使用HASH索引优化固定长度二进制MD5值的查询性能';

-- 为索引添加注释
COMMENT ON INDEX idx_history_user IS '按用户ID查询的优化索引';
COMMENT ON INDEX idx_history_time IS '按时间排序查询的优化索引';
COMMENT ON INDEX idx_values_md5 IS '按MD5哈希值快速查找内容的索引';

CREATE TABLE execution_log (
                               id SERIAL PRIMARY KEY,
                               class_name VARCHAR(255) NOT NULL,
                               method_name VARCHAR(255) NOT NULL,
                               execution_time BIGINT NOT NULL,
                               execution_date TIMESTAMP NOT NULL,
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX exec_time ON "execution_log" (execution_time);

COMMENT ON TABLE execution_log IS '方法执行日志记录表';
COMMENT ON COLUMN execution_log.class_name IS '类名';
COMMENT ON COLUMN execution_log.method_name IS '方法名';
COMMENT ON COLUMN execution_log.execution_time IS '执行时间(ms)';
COMMENT ON COLUMN execution_log.execution_date IS '执行日期';
COMMENT ON COLUMN execution_log.created_at IS '记录创建时间';

