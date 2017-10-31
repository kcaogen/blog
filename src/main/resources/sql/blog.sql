create table k_blog(
	blogId INT primary key not null auto_increment,
	blogName VARCHAR(100),
	blogImg VARCHAR(50),
	introduction VARCHAR(1000),
	content MEDIUMTEXT,
	time VARCHAR(100),
	browse INT,
	praise INT,
	blogType VARCHAR(100),
	reprintedUrl VARCHAR(200)
);

create table k_blogType(
	typeId INT primary key not null auto_increment,
	typeName VARCHAR(100)
);

create table k_blogTag(
	tagId INT primary key not null auto_increment,
	tagName VARCHAR(100)
);

create table k_blog_tag(
	id INT primary key not null auto_increment,
	blogId INT,
	tagId	INT
);

create table k_city(
	id INT primary key not null auto_increment,
	cityName VARCHAR(100),
	codes VARCHAR(100),
	visitors	INT
);

create table k_user(
  id INT primary key not null auto_increment,
  userName VARCHAR(50),
  password VARCHAR(500)
);

create table k_role(
  id INT primary key not null auto_increment,
  role VARCHAR(50),
);

create table k_user_role(
  id INT primary key not null auto_increment,
  userId INT,
  roleId INT
);