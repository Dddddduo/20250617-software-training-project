# 商品管理系统

## 项目概述

这是一个基于Java和JDBC技术的商品管理系统，实现了对商品信息的增删改查等基本操作，并提供了仓库商品总价格统计功能。系统通过命令行界面与用户交互，连接到MySQL数据库进行数据存储和管理。

## 技术栈

- **编程语言**：Java 8+
- **数据库**：MySQL
- **技术框架**：JDBC
- **开发工具**：JDK、IDE（如IntelliJ IDEA、Eclipse）

## 数据库设计

### 表结构

系统使用名为`train`的数据库中的`sp`表存储商品信息，表结构如下：

| 字段名   | 数据类型       | 描述         |
|----------|----------------|--------------|
| sid      | int(11)        | 商品编号（主键） |
| sname    | varchar(50)    | 商品名称     |
| sprice   | double         | 商品价格     |
| scategory| varchar(50)    | 商品类别     |
| sinventory| int(11)       | 库存数量     |

### 连接信息

```java
private static final String URL = "jdbc:mysql://182.92.125.34:3306/train";
private static final String USER = "dduo";
private static final String PASSWORD = "2JmJBD5MynXxDCrA";
private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
```

## 功能模块

### 1. 商品管理功能

- **录入商品入库**：添加新商品到数据库，包括名称、价格、类别和库存数量
- **查看所有商品**：显示数据库中所有商品的详细信息
- **查看指定名称商品**：根据商品名称查询相关商品信息
- **修改商品信息**：根据商品编号更新商品的各项信息
- **删除商品**：根据商品编号删除指定商品

### 2. 统计功能

- **统计总价格**：计算仓库中所有商品的总价值（价格×库存数量）

### 3. 辅助功能

- **输入验证**：确保用户输入的合法性
- **菜单导航**：提供清晰的命令行菜单界面
- **操作反馈**：对用户操作给出明确的成功或失败提示

## 运行指南

### 环境要求

- JDK 8或更高版本
- MySQL数据库（需提前创建好`train`数据库和`sp`表）
- MySQL JDBC驱动（`mysql-connector-java`）

### 运行步骤

1. 将代码保存为`HomeWorkHelp.java`文件
2. 确保已安装JDK并配置好环境变量
3. 将MySQL JDBC驱动添加到项目的类路径中
4. 编译代码：`javac HomeWorkHelp.java`
5. 运行程序：`java HomeWorkHelp`

### 数据库准备

在MySQL中执行以下SQL语句创建表：

```sql
CREATE DATABASE IF NOT EXISTS train;
USE train;

CREATE TABLE IF NOT EXISTS sp (
    sid INT AUTO_INCREMENT PRIMARY KEY,
    sname VARCHAR(50) NOT NULL,
    sprice DOUBLE NOT NULL,
    scategory VARCHAR(50) DEFAULT '其他',
    sinventory INT NOT NULL
);
```

## 使用说明

### 系统菜单

程序运行后会显示以下菜单：

```
商品管理系统
1. 录入商品入库
2. 查看所有商品信息
3. 查看指定名称的商品信息
4. 根据编号修改商品信息
5. 根据编号删除商品信息
6. 统计仓库中所有商品的总计价格
7. 退出系统
请您输入功能序号: 
```

### 功能操作说明

#### 1. 录入商品入库

- 输入商品名称、价格、类别（可选，默认为"其他"）和库存数量
- 系统会将商品信息插入到数据库中

#### 2. 查看所有商品信息

- 显示数据库中所有商品的详细信息，包括编号、名称、价格、类别和库存
- 信息按商品编号排序显示

#### 3. 查看指定名称的商品信息

- 输入商品名称（支持模糊查询）
- 系统会显示所有名称包含该关键词的商品信息

#### 4. 根据编号修改商品信息

- 输入要修改的商品编号
- 可以选择修改商品的名称、价格、类别或库存数量（不修改则直接回车）
- 系统会更新数据库中的商品信息

#### 5. 根据编号删除商品信息

- 输入要删除的商品编号
- 系统会验证商品是否存在
- 确认后删除该商品信息

#### 6. 统计仓库中所有商品的总计价格

- 系统计算所有商品的总价值（价格×库存数量）
- 显示计算结果

#### 7. 退出系统

- 结束程序运行
- 显示感谢信息

## 项目结构

### 代码结构

```
HomeWorkHelp.java
├── main方法 - 程序入口
├── 数据库连接配置 - URL、USER、PASSWORD、DRIVER
├── 菜单功能
│   ├── printMenu() - 打印系统菜单
│   ├── getValidChoice() - 获取有效用户选择
├── 商品管理功能
│   ├── addProduct() - 添加商品
│   ├── viewAllProducts() - 查看所有商品
│   ├── viewProductByName() - 按名称查询商品
│   ├── updateProduct() - 修改商品信息
│   ├── deleteProduct() - 删除商品
├── 统计功能
│   ├── calculateTotalPrice() - 统计总价格
├── 辅助功能
│   ├── checkProductExists() - 检查商品是否存在
```

### 核心技术点

- **JDBC连接管理**：使用try-with-resources自动关闭连接、语句和结果集
- **预处理语句(PreparedStatement)**：防止SQL注入，提高性能
- **输入验证**：对用户输入进行合法性检查和错误处理
- **事务管理**：虽然未显式使用，但JDBC默认自动提交事务
- **命令行交互**：通过Scanner实现用户输入和结果显示

## 注意事项

1. 运行前请确保数据库连接信息正确（URL、用户名、密码）
2. 如果修改了数据库连接信息，需要更新代码中的相关常量
3. 输入价格和库存数量时请使用数字格式
4. 删除商品时请谨慎操作，删除后无法恢复
5. 如果出现数据库连接失败，请检查网络连接和数据库服务器状态
6. 程序使用Java 7+的try-with-resources特性，编译时请使用相应的JDK版本

## 版本说明

- 当前版本：1.0
- 最后更新：2025年6月18日

这个商品管理系统实现了基本的商品管理功能，适合作为Java JDBC编程的学习示例。如果需要进一步扩展，可以考虑添加用户认证、更复杂的查询功能或图形用户界面。
