-- 创建博客表
create table k_blog(
	blogId INT PRIMARY KEY NOT NULL auto_increment COMMENT '博客id',
	blogName VARCHAR(100) COMMENT '博客名称',
	blogImg VARCHAR(50) COMMENT '博客图片名称',
	introduction VARCHAR(1000) COMMENT '博客介绍',
	content MEDIUMTEXT COMMENT '博客内容',
	time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '博客创建时间',
	browse INT COMMENT '浏览数量',
	praise INT COMMENT '点赞数量',
	blogType VARCHAR(100) COMMENT '博客类型',
	reprintedUrl VARCHAR(200) COMMENT '转载URL'
);

-- 创建博客类型表
create table k_type(
	typeId INT PRIMARY KEY NOT NULL auto_increment COMMENT '博客类型id',
	typeName VARCHAR(100) COMMENT '博客类型'
);

-- 创建博客标签表
create table k_tag(
	tagId INT PRIMARY KEY NOT NULL auto_increment COMMENT '博客标签id',
	tagName VARCHAR(100) COMMENT '博客标签'
);

-- 创建博客标签关联表 (一对多)
create table k_blog_tag(
	id INT PRIMARY KEY NOT NULL auto_increment,
	blogId INT COMMENT '博客ID',
	tagId	INT COMMENT '博客标签ID'
);

-- 创建城市访问量表
create table k_city(
	id INT PRIMARY KEY NOT NULL auto_increment,
	cityName VARCHAR(100) COMMENT '城市',
	codes VARCHAR(100) COMMENT '城市简称',
	visitors	INT COMMENT '访问量'
);

-- 创建用户表
create table k_user(
  id INT PRIMARY KEY NOT NULL auto_increment,
  userName VARCHAR(50),
  password VARCHAR(500)
);

-- 创建角色表
create table k_role(
  id INT PRIMARY KEY NOT NULL auto_increment,
  role VARCHAR(50)
);

-- 创建用户角色关联表
create table k_user_role(
  id INT PRIMARY KEY NOT NULL auto_increment,
  userId INT,
  roleId INT
);

INSERT INTO `k_city` VALUES ('1', '北京', 'beijing', '1758');
INSERT INTO `k_city` VALUES ('2', '天津', 'tianjin', '1103');
INSERT INTO `k_city` VALUES ('3', '上海', 'shanghai', '1443');
INSERT INTO `k_city` VALUES ('4', '重庆', 'chongqing', '491');
INSERT INTO `k_city` VALUES ('5', '河北', 'hebei', '621');
INSERT INTO `k_city` VALUES ('6', '山西', 'shanxi', '495');
INSERT INTO `k_city` VALUES ('7', '辽宁', 'liaoning', '526');
INSERT INTO `k_city` VALUES ('8', '吉林', 'jilin', '1056');
INSERT INTO `k_city` VALUES ('9', '黑龙江', 'heilongjiang', '599');
INSERT INTO `k_city` VALUES ('10', '江苏', 'jiangsu', '743');
INSERT INTO `k_city` VALUES ('11', '浙江', 'zhejiang', '1155');
INSERT INTO `k_city` VALUES ('12', '安徽', 'anhui', '266');
INSERT INTO `k_city` VALUES ('13', '福建', 'fujian', '138');
INSERT INTO `k_city` VALUES ('14', '江西 ', 'jiangxi', '661');
INSERT INTO `k_city` VALUES ('15', '山东', 'shandong', '828');
INSERT INTO `k_city` VALUES ('16', '河南', 'henan', '1052');
INSERT INTO `k_city` VALUES ('17', '湖北', 'hubei', '710');
INSERT INTO `k_city` VALUES ('18', '湖南', 'hunan', '268');
INSERT INTO `k_city` VALUES ('19', '广东', 'guangdong', '2860');
INSERT INTO `k_city` VALUES ('20', '海南', 'hainan', '674');
INSERT INTO `k_city` VALUES ('21', '四川', 'sichuan', '941');
INSERT INTO `k_city` VALUES ('22', '贵州', 'guizhou', '522');
INSERT INTO `k_city` VALUES ('23', '云南', 'yunnan', '719');
INSERT INTO `k_city` VALUES ('24', '陕西', 'shaanxi', '913');
INSERT INTO `k_city` VALUES ('25', '甘肃', 'gansu', '317');
INSERT INTO `k_city` VALUES ('26', '青海', 'qinghai', '723');
INSERT INTO `k_city` VALUES ('27', '台湾', 'taiwan', '586');
INSERT INTO `k_city` VALUES ('28', '内蒙古', 'neimenggu', '645');
INSERT INTO `k_city` VALUES ('29', '广西', 'guangxi', '378');
INSERT INTO `k_city` VALUES ('30', '西藏', 'xizang', '855');
INSERT INTO `k_city` VALUES ('31', '宁夏', 'ningxia', '1044');
INSERT INTO `k_city` VALUES ('32', '新疆', 'xinjiang', '541');
INSERT INTO `k_city` VALUES ('33', '香港', 'xianggang', '487');
INSERT INTO `k_city` VALUES ('34', '澳门', 'aomen', '691');

-- 账号密码都是admin
INSERT INTO `k_user` VALUES ('1', 'admin', '$2a$10$a5/eidRArkbZ0qsG9LtA7.snNoxxKp3M4uvaASXHNopD3yq226pF6');
INSERT INTO `k_role` VALUES ('1', 'ROLE_ADMIN');
INSERT INTO `k_user_role` VALUES ('1', '1', '1');