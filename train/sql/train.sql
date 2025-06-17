-- 创建商品表（sp）
CREATE TABLE IF NOT EXISTS sp (
                                  sid INT PRIMARY KEY AUTO_INCREMENT COMMENT '商品编号',
                                  sname VARCHAR(50) NOT NULL COMMENT '商品名称',
    sprice DECIMAL(10, 2) NOT NULL COMMENT '商品价格',
    scategory VARCHAR(30) DEFAULT '其他' COMMENT '商品类别',
    sinventory INT DEFAULT 0 COMMENT '库存数量',
    screate_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    supdate_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
    );

-- 插入样例数据
INSERT INTO sp (sname, sprice, scategory, sinventory) VALUES
                                                          ('笔记本电脑', 5999.00, '电子产品', 10),
                                                          ('无线耳机', 299.00, '电子产品', 20),
                                                          ('保温杯', 89.50, '生活用品', 15),
                                                          ('机械键盘', 349.00, '电子产品', 8),
                                                          ('T恤', 49.90, '服装', 30);