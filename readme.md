

本文是对周志明老师的凤凰架构的学习，原文https://icyfenix.cn/exploration/projects/microservice_arch_kubernetes.html， 做了一些改动，原文作者是按照DDD模式开发的，本文学习时改为了MVC，并且对数据校验做了一些精简，是原文的简化版

# 前端

在前端项目修改以下内容：

>1. 修改前缀请求，改为restful-api, 对应修改mock文件中的restful前缀
>2. 数据请求结果的状态码的一些改变
>2. 前后端分离，在config/index.js中修改proxyTable

- 安装前端的包，npm run install

- 以开发模式mock数据运行 npm run dev

- 编译npm run build,可以把生成的dist文件夹下的内容拷贝到后端的static文件夹下边

# SpringBoot后端

登录：

http://localhost:8080/oauth/token?username=123&password=MuLuKWO8eJn8qpguLt62ROuJ.gMGigi&grant_type=password&client_id=bookstore_frontend&client_secret=bookstore_secret

```
username: 123
password: MuLuKWO8eJn8qpguLt62ROuJ.gMGigi
grant_type: password
client_id: bookstore_frontend
client_secret: bookstore_secret
```

响应：

```json
{
	"access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiIxMjMiLCJzY29wZSI6WyJCUk9XU0VSIl0sImV4cCI6MTY0OTc4OTYwNCwiYXV0aG9yaXRpZXMiOlsiUk9MRV9VU0VSIl0sImp0aSI6ImMyZmVlNmM4LTUwOTktNDA5Yi05ZTViLWQ4OTcxOTk0ZWNhYiIsImNsaWVudF9pZCI6ImJvb2tzdG9yZV9mcm9udGVuZCIsInVzZXJuYW1lIjoiMTIzIn0.iE7op9bW_Heqn6j2UcgknndS1biLzW3-kzprxTatJbw",
	"token_type": "bearer",
	"refresh_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiIxMjMiLCJzY29wZSI6WyJCUk9XU0VSIl0sImF0aSI6ImMyZmVlNmM4LTUwOTktNDA5Yi05ZTViLWQ4OTcxOTk0ZWNhYiIsImV4cCI6MTY1MTA3NDgwNCwiYXV0aG9yaXRpZXMiOlsiUk9MRV9VU0VSIl0sImp0aSI6Ijc0N2E1ZDE4LTdjNDMtNGFkYS05NTI0LTg4NWM3NDU1NDUyNSIsImNsaWVudF9pZCI6ImJvb2tzdG9yZV9mcm9udGVuZCIsInVzZXJuYW1lIjoiMTIzIn0.GwYCHdfa-YHnBorD1aEvgGwWIJJYY65bTnmEyZzR6Nw",
	"expires_in": 10799,
	"scope": "BROWSER",
	"authorities": ["ROLE_USER"],
	"username": "123",
	"jti": "c2fee6c8-5099-409b-9e5b-d8971994ecab"
}

access_token解析：token有三部分
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9是令牌Header--头部包含所使用的签名算法和令牌的类型
{ "alg": "HS256", "typ": "JWT"} 经过Base64Url 编码以后

中间一部分是载荷

第三部分：签名--是对前两部分的防篡改签名。将Header和Payload用Base64URL编码后，再用点(.)连接起来。然后使用签名算法和密钥对这个字符串进行签名。这个密钥只有服务器才知道，不能泄露给用户。

{
  "user_name": "123",
  "scope": [
    "BROWSER"
  ],
  "exp": 1649789604,
  "authorities": [
    "ROLE_USER"
  ],
  "jti": "c2fee6c8-5099-409b-9e5b-d8971994ecab",
  "client_id": "bookstore_frontend",
  "username": "123"
}
```

可以看到，登录前端所传的密码一样（这个密码都是前端通过特定算法产生的加密值），两次登录返回来的token不一样。后台使用BCryptPasswordEncoder().encode进行hash，同一个密码每次存到数据库的值都不一样，这是因为加了不同的盐值，但是通过matches方法却可以对比，原理如下：

```
1）加密过程确实是通过随机盐产生了密文，这样的结果就是每次得到的密文不一致
2）对比的过程并不是再次加密，而是通过截取原来的密文中的7-25位获取上次加密时的盐，再次使用这个盐做hash然后在开头拼上$2a$10$，然后对一些特定位进行base64编码生成的。换言之，就是密文本身包含了盐。
```



- springboot与Vue交互一般有两种方式

  - build前端vue项目，然后把生成的dist目录下的文件拷贝到resources下的static下即可

  - 分开部署

    

- XHR请求与HTTP请求

  xhr，全称为XMLHttpRequest，用于与服务器交互数据，是ajax功能实现所依赖的对象，jquery中的ajax就是对
  xhr的封装。

# SpringCloud后端

服务部署：

- 前端：8080

- springboot：8081

- springcloud

  - basearc_gateway（服务网关）：8081

  - basearc_configuration（配置中心）：8082
  - basearc_registy(注册中心)：8761
  - service_security(安全中心)：8084
  - service_warehouse(库存服务): 8085
  - service_account(账户服务)：8086
  - service_payment(支付服务)：8087

# 数据库

```sql
drop database if exists bookstore;
create database bookstore;
use bookstore;
DROP TABLE IF EXISTS specification;
DROP TABLE IF EXISTS advertisement;
DROP TABLE IF EXISTS stockpile;
DROP TABLE IF EXISTS payment;
DROP TABLE IF EXISTS wallet;
DROP TABLE IF EXISTS account;
DROP TABLE IF EXISTS product;

create table account
(
    id bigint not null auto_increment primary key,
    username varchar(50)  not null default '' comment '用户名',
    password varchar(100) not null default '' comment '密码',
    name varchar(50) not null  default '' comment '名字',
    telephone varchar(20) not null default '' comment '电话号码',
    email varchar(100) not null default '' comment '电子邮件',
    location varchar(100) not null default '' comment '居住地',
    insert_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '本条记录创建时间',
    update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '本条记录修改时间',
    is_visible tinyint(1) NOT NULL DEFAULT '1',
    index idx_username(username)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

create table advertisement
(
    id bigint not null auto_increment primary key,
    image varchar(100) not null default '' comment '广告图片存放位置',
    product_id bigint not null  default -1 comment '产品外键',
    insert_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '本条记录创建时间',
    update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '本条记录修改时间',
    is_visible tinyint(1) NOT NULL DEFAULT '1'
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

create table payment
(
    id bigint not null auto_increment primary key,
    pay_id varchar(100) not null default '' comment '支付id',
    create_time datetime not null default CURRENT_TIMESTAMP comment '创建时间',
    total_price decimal not null default '0' comment '总价',
    expires int not null default '0' comment '超时时间',
    payment_link varchar(300) not null default '' comment '支付链接',
    pay_state varchar(20) not null default '' comment '支付状态',
    insert_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '本条记录创建时间',
    update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '本条记录修改时间',
    is_visible tinyint(1) NOT NULL DEFAULT '1'
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

create table product
(
    id bigint not null auto_increment primary key,
    title varchar(50) not null default '' comment '书名',
    price decimal not null  default '0' comment '价格',
    rate float not null default '0' comment '折扣',
    description varchar(8000) not null default '' comment '描述',
    cover varchar(100) not null default '' comment '封面',
    detail varchar(100) not null default '' comment '详情图',
    insert_time timestamp default CURRENT_TIMESTAMP not null comment '本条记录创建时间',
    update_time timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '本条记录修改时间',
    is_visible tinyint(1) default 1 not null,
    index idx_title(title)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

create table specification
(
    id bigint not null auto_increment primary key,
    item varchar(50) not null default '' comment '条目',
    value varchar(100) not null default '' comment '值',
    product_id bigint not null default '-1' comment 'product外键',
    insert_time timestamp default CURRENT_TIMESTAMP not null comment '本条记录创建时间',
    update_time timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '本条记录修改时间',
    is_visible tinyint(1) default 1 not null
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

create table stockpile
(
    id bigint not null auto_increment primary key,
    amount int not null default 0 comment '库存量',
    frozen int not null default 0 comment '冻结的的库存量',
    product_id bigint default -1 not null comment 'product外键',
    insert_time timestamp default CURRENT_TIMESTAMP not null comment '本条记录创建时间',
    update_time timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '本条记录修改时间',
    is_visible tinyint(1) default 1 not null
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

create table wallet
(
    id bigint not null auto_increment primary key,
    money decimal not null default 0 comment '钱包值',
    account_id bigint not null default -1 comment '账号id,account外键',
    insert_time timestamp default CURRENT_TIMESTAMP not null comment '本条记录创建时间',
    update_time timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '本条记录修改时间',
    is_visible tinyint(1) default 1 not null
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

INSERT INTO product (id, title, price, rate, description, cover, detail, insert_time, update_time, is_visible) VALUES (4, '深入理解Java虚拟机（第2版）', 79, 9, '<p>《深入理解Java虚拟机:JVM高级特性与最佳实践(第2版)》内容简介：第1版两年内印刷近10次，4家网上书店的评论近4?000条，98%以上的评论全部为5星级的好评，是整个Java图书领域公认的经典著作和超级畅销书，繁体版在台湾也十分受欢迎。第2版在第1版的基础上做了很大的改进：根据最新的JDK 1.7对全书内容进行了全面的升级和补充；增加了大量处理各种常见JVM问题的技巧和最佳实践；增加了若干与生产环境相结合的实战案例；对第1版中的错误和不足之处的修正；等等。第2版不仅技术更新、内容更丰富，而且实战性更强。</p><p>《深入理解Java虚拟机:JVM高级特性与最佳实践(第2版)》共分为五大部分，围绕内存管理、执行子系统、程序编译与优化、高效并发等核心主题对JVM进行了全面而深入的分析，深刻揭示了JVM的工作原理。</p><p>第一部分从宏观的角度介绍了整个Java技术体系、Java和JVM的发展历程、模块化，以及JDK的编译，这对理解书中后面内容有重要帮助。</p><p>第二部分讲解了JVM的自动内存管理，包括虚拟机内存区域的划分原理以及各种内存溢出异常产生的原因；常见的垃圾收集算法以及垃圾收集器的特点和工作原理；常见虚拟机监控与故障处理工具的原理和使用方法。</p><p>第三部分分析了虚拟机的执行子系统，包括类文件结构、虚拟机类加载机制、虚拟机字节码执行引擎。</p><p>第四部分讲解了程序的编译与代码的优化，阐述了泛型、自动装箱拆箱、条件编译等语法糖的原理；讲解了虚拟机的热点探测方法、HotSpot的即时编译器、编译触发条件，以及如何从虚拟机外部观察和分析JIT编译的数据和结果；</p><p>第五部分探讨了Java实现高效并发的原理，包括JVM内存模型的结构和操作；原子性、可见性和有序性在Java内存模型中的体现；先行发生原则的规则和使用；线程在Java语言中的实现原理；虚拟机实现高效并发所做的一系列锁优化措施。</p>', '/static/cover/jvm2.jpg', '/static/desc/jvm2.jpg', '2022-04-16 16:42:56', '2022-04-16 16:42:56', 1);
INSERT INTO product (id, title, price, rate, description, cover, detail, insert_time, update_time, is_visible) VALUES (7, '深入理解Java虚拟机', 69, 8.6, '<p>作为一位Java程序员，你是否也曾经想深入理解Java虚拟机，但是却被它的复杂和深奥拒之门外？没关系，本书极尽化繁为简之妙，能带领你在轻松中领略Java虚拟机的奥秘。本书是近年来国内出版的唯一一本与Java虚拟机相关的专著，也是唯一一本同时从核心理论和实际运用这两个角度去探讨Java虚拟机的著作，不仅理论分析得透彻，而且书中包含的典型案例和最佳实践也极具现实指导意义。</p><p>全书共分为五大部分。第一部分从宏观的角度介绍了整个Java技术体系的过去、现在和未来，以及如何独立地编译一个OpenJDK7，这对理解后面的内容很有帮助。第二部分讲解了JVM的自动内存管理，包括虚拟机内存区域的划分原理以及各种内存溢出异常产生的原因；常见的垃圾收集算法以及垃圾收集器的特点和工作原理；常见的虚拟机的监控与调试工具的原理和使用方法。第三部分分析了虚拟机的执行子系统，包括Class的文件结构以及如何存储和访问Class中的数据；虚拟机的类创建机制以及类加载器的工作原理和它对虚拟机的意义；虚拟机字节码的执行引擎以及它在实行代码时涉及的内存结构。第四部分讲解了程序的编译与代码的优化，阐述了泛型、自动装箱拆箱、条件编译等语法糖的原理；讲解了虚拟机的热点探测方法、HotSpot的即时编译器、编译触发条件，以及如何从虚拟机外部观察和分析JIT编译的数据和结果。第五部分探讨了Java实现高效并发的原理，包括JVM内存模型的结构和操作；原子性、可见性和有序性在Java内存模型中的体现；先行发生原则的规则和使用；线程在Java语言中的实现原理；虚拟机实现高效并发所做的一系列锁优化措施。</p>', '/static/cover/jvm1.jpg', '', '2022-04-16 16:42:56', '2022-04-16 16:42:56', 1);
INSERT INTO product (id, title, price, rate, description, cover, detail, insert_time, update_time, is_visible) VALUES (3, 'Java虚拟机规范（Java SE 8）', 79, 7.7, '<p>本书完整而准确地阐释了Java虚拟机各方面的细节，围绕Java虚拟机整体架构、编译器、class文件格式、加载、链接与初始化、指令集等核心主题对Java虚拟机进行全面而深入的分析，深刻揭示Java虚拟机的工作原理。同时，书中不仅完整地讲述了由Java SE 8所引入的新特性，例如对包含默认实现代码的接口方法所做的调用，还讲述了为支持类型注解及方法参数注解而对class文件格式所做的扩展，并阐明了class文件中各属性的含义，以及字节码验证的规则。</p>', '/static/cover/jvms8.jpg', '', '2022-04-16 16:42:56', '2022-04-16 16:42:56', 1);
INSERT INTO product (id, title, price, rate, description, cover, detail, insert_time, update_time, is_visible) VALUES (5, 'Java虚拟机规范（Java SE 7）', 69, 8.9, '<p>本书整合了自1999年《Java虚拟机规范（第2版）》发布以来Java世界所出现的技术变化。另外，还修正了第2版中的许多错误，以及对目前主流Java虚拟机实现来说已经过时的内容。最后还处理了一些Java虚拟机和Java语言概念的模糊之处。</p><p>2004年发布的Java SE 5.0版为Java语言带来了翻天覆地的变化，但是对Java虚拟机设计的影响则相对较小。在Java SE 7这个版本中，我们扩充了class文件格式以便支持新的Java语言特性，譬如泛型和变长参数方法等。</p>', '/static/cover/jvms.jpg', '/static/desc/jvms.jpg', '2022-04-16 16:42:56', '2022-04-16 16:42:56', 1);
INSERT INTO product (id, title, price, rate, description, cover, detail, insert_time, update_time, is_visible) VALUES (6, '深入理解OSGi', 79, 7.7, '<p>本书是原创Java技术图书领域继《深入理解Java虚拟机》后的又一实力之作，也是全球首本基于最新OSGi R5.0规范的著作。理论方面，既全面解读了OSGi规范，深刻揭示了OSGi原理，详细讲解了OSGi服务，又系统地介绍了Equinox框架的使用方法，并通过源码分析了该框架的工作机制；实践方面，不仅包含一些典型的案例，还总结了大量的最佳实践，极具实践指导意义。</p><p>全书共14章，分4个部分。第一部分（第1章）：走近OSGi，主要介绍了什么是OSGi以及为什么要使用OSGi。第二部分（第2～4章）：OSGi规范与原理，对最新的OSGi R5.0中的核心规范进行了全面的解读，首先讲解了OSGi模块的建立、描述、依赖关系的处理，然后讲解了Bundle的启动原理和调度管理，最后讲解了与本地及远程服务相关的内容。第三部分：OSGi服务与Equinox应用实践（第5～11章），不仅详细讲解了OSGi服务纲要规范和企业级规范中最常用的几个子规范和服务的技术细节，还通过一个基于Equinox的BBS案例演示了Equinox的使用方法，最重要的是还通过源码分析了Equinox关键功能的实现机制和原理。第四部分：最佳实践（第12～14章），总结了大量关于OSGi的最佳实践，包括从Bundle如何命名、模块划分、依赖关系处理到保持OSGi动态性、管理程序启动顺序、使用API基线管理模块版本等各方面的实践技巧，此外还介绍了Spring DM的原理以及如何在OSGi环节中进行程序测试。</p>', '/static/cover/osgi.jpg', '/static/desc/OSGi.jpg', '2022-04-16 16:42:56', '2022-04-16 16:42:56', 1);
INSERT INTO product (id, title, price, rate, description, cover, detail, insert_time, update_time, is_visible) VALUES (9, '测试商品', 12, 9.99, '<p>测试</p>', '121', '121', '2022-04-16 17:41:31', '2022-04-16 17:43:27', 0);
INSERT INTO product (id, title, price, rate, description, cover, detail, insert_time, update_time, is_visible) VALUES (1, '深入理解Java虚拟机（第3版）', 129, 9.8, '<p>这是一部从工作原理和工程实践两个维度深入剖析JVM的著作，是计算机领域公认的经典，繁体版在台湾也颇受欢迎。</p> <p>自2011年上市以来，前两个版本累计印刷36次，销量超过30万册，两家主要网络书店的评论近90000条，内容上近乎零差评，是原创计算机图书领域不可逾越的丰碑，第3版在第2版的基础上做了重大修订，内容更丰富、实战性更强：根据新版JDK对内容进行了全方位的修订和升级，围绕新技术和生产实践新增逾10万字，包含近50%的全新内容，并对第2版中含糊、瑕疵和错误内容进行了修正。</p> <p>全书一共13章，分为五大部分：</p> <p>第一部分（第1章）走近Java</p> <p>系统介绍了Java的技术体系、发展历程、虚拟机家族，以及动手编译JDK，了解这部分内容能对学习JVM提供良好的指引。</p> <p>第二部分（第2~5章）自动内存管理</p> <p>详细讲解了Java的内存区域与内存溢出、垃圾收集器与内存分配策略、虚拟机性能监控与故障排除等与自动内存管理相关的内容，以及10余个经典的性能优化案例和优化方法；</p> <p>第三部分（第6~9章）虚拟机执行子系统</p> <p>深入分析了虚拟机执行子系统，包括类文件结构、虚拟机类加载机制、虚拟机字节码执行引擎，以及多个类加载及其执行子系统的实战案例；</p> <p>第四部分（第10~11章）程序编译与代码优化</p> <p>详细讲解了程序的前、后端编译与优化，包括前端的易用性优化措施，如泛型、主动装箱拆箱、条件编译等的内容的深入分析；以及后端的性能优化措施，如虚拟机的热点探测方法、HotSpot 的即时编译器、提前编译器，以及各种常见的编译期优化技术；</p> <p>第五部分（第12~13章）高效并发</p> <p>主要讲解了Java实现高并发的原理，包括Java的内存模型、线程与协程，以及线程安全和锁优化。</p> <p>全书以实战为导向，通过大量与实际生产环境相结合的案例分析和展示了解决各种Java技术难题的方案和技巧。</p>', '/static/cover/jvm3.jpg', '/static/desc/jvm3.jpg', '2022-04-16 16:42:56', '2022-04-16 17:39:45', 1);
INSERT INTO product (id, title, price, rate, description, cover, detail, insert_time, update_time, is_visible) VALUES (8, '凤凰架构：构建可靠的大型分布式系统', 0, 0, '<p>这是一部以“如何构建一套可靠的分布式大型软件系统”为叙事主线的开源文档，是一幅帮助开发人员整理现代软件架构各条分支中繁多知识点的技能地图。文章《<a href="https://icyfenix.cn/introduction/about-the-fenix-project.html" target=_blank>什么是“凤凰架构”</a>》详细阐述了这部文档的主旨、目标与名字的来由，文章《<a href="https://icyfenix.cn/exploration/guide/quick-start.html" target=_blank>如何开始</a>》简述了文档每章讨论的主要话题与内容详略分布</p>', '/static/cover/fenix.png', '/static/desc/fenix.jpg', '2022-04-16 16:42:56', '2022-04-16 16:42:56', 1);
INSERT INTO product (id, title, price, rate, description, cover, detail, insert_time, update_time, is_visible) VALUES (2, '智慧的疆界', 69, 9.1, '<p>这是一部对人工智能充满敬畏之心的匠心之作，由《深入理解Java虚拟机》作者耗时一年完成，它将带你从奠基人物、历史事件、学术理论、研究成果、技术应用等5个维度全面读懂人工智能。</p>
<p>本书以时间为主线，用专业的知识、通俗的语言、巧妙的内容组织方式，详细讲解了人工智能这个学科的全貌、能解决什么问题、面临怎样的困难、尝试过哪些努力、取得过多少成绩、未来将向何方发展，尽可能消除人工智能的神秘感，把阳春白雪的人工智能从科学的殿堂推向公众面前。</p>', '/static/cover/ai.jpg', '/static/desc/ai.jpg', '2022-04-16 16:42:56', '2022-04-16 16:42:56', 1);

INSERT INTO account (id, username, password, name, telephone, email, location) VALUES (1, 'icyfenix', '$2a$10$Yj99pZEJDc43MGi4c2HvD.pN6H1T6N1FZgIZWqnFg4e5v/06E94mK', '周志明',  '18888888888', 'icyfenix@gmail.com', '唐家湾港湾大道科技一路3号远光软件股份有限公司');
INSERT INTO account (id, username, password, name, telephone, email, location) VALUES (6, '123', '$2a$10$l/aDX4AFnmB0zvTh9AMrauBujTyKk500l92vEnEePHQQ6.Nj5IVay', '123', '12345678902', '123@qq.com', 'xasxjnasxkl1223');
INSERT INTO account (id, username, password, name, telephone, email, location) VALUES (7, 'WQEE', '$2a$10$v6BIANNBhgCD7/YnL.MtgOEgeyoGr/MoxNJNmEPNFwzWp18ka3hGO', 'WQE', '12345671234', '123@qq.com', '');

INSERT INTO advertisement (id, image, product_id) VALUES (1, '/static/carousel/fenix2.png', 8);
INSERT INTO advertisement (id, image, product_id) VALUES (2, '/static/carousel/ai.png', 2);
INSERT INTO advertisement (id, image, product_id) VALUES (3, '/static/carousel/jvm3.png', 1);

INSERT INTO payment (id, pay_id, create_time, total_price, expires, payment_link, pay_state) VALUES (11, 'f2dbfe6b-66ff-4902-9748-3c4aa529d473', '2022-04-17 12:24:46', 210, 120000, '/pay/modify/f2dbfe6b-66ff-4902-9748-3c4aa529d473?state=PAYED&accountId=7', 'WAITING');
INSERT INTO payment (id, pay_id, create_time, total_price, expires, payment_link, pay_state) VALUES (12, '1b057be4-ea09-4a3e-a60f-7ffce241ab2f', '2022-04-17 12:25:22', 210, 120000, '/pay/modify/1b057be4-ea09-4a3e-a60f-7ffce241ab2f?state=PAYED&accountId=7', 'PAYED');

INSERT INTO specification (id, item, value, product_id) VALUES (1, '作者', '周志明', 1);
INSERT INTO specification (id, item, value, product_id) VALUES (2, '副标题', 'JVM高级特性与最佳实践', 1);
INSERT INTO specification (id, item, value, product_id) VALUES (3, 'ISBN', '9787111641247', 1);
INSERT INTO specification (id, item, value, product_id) VALUES (4, '书名', '深入理解Java虚拟机（第3版）', 1);
INSERT INTO specification (id, item, value, product_id) VALUES (5, '页数', '540', 1);
INSERT INTO specification (id, item, value, product_id) VALUES (6, '丛书', '华章原创精品', 1);
INSERT INTO specification (id, item, value, product_id) VALUES (7, '出版社', '机械工业出版社', 1);
INSERT INTO specification (id, item, value, product_id) VALUES (8, '出版年', '2019-12', 1);
INSERT INTO specification (id, item, value, product_id) VALUES (9, '装帧', '平装', 1);
INSERT INTO specification (id, item, value, product_id) VALUES (10, '作者', '周志明', 2);
INSERT INTO specification (id, item, value, product_id) VALUES (11, 'ISBN', '9787111610496', 2);
INSERT INTO specification (id, item, value, product_id) VALUES (12, '书名', '智慧的疆界', 2);
INSERT INTO specification (id, item, value, product_id) VALUES (13, '副标题', '从图灵机到人工智能', 2);
INSERT INTO specification (id, item, value, product_id) VALUES (14, '页数', '413', 2);
INSERT INTO specification (id, item, value, product_id) VALUES (15, '出版社', '机械工业出版社', 2);
INSERT INTO specification (id, item, value, product_id) VALUES (16, '出版年', '2018-1-1', 2);
INSERT INTO specification (id, item, value, product_id) VALUES (17, '装帧', '平装', 2);
INSERT INTO specification (id, item, value, product_id) VALUES (18, '作者', 'Tim Lindholm / Frank Yellin 等', 3);
INSERT INTO specification (id, item, value, product_id) VALUES (19, '译者', '爱飞翔 / 周志明 / 等 ', 3);
INSERT INTO specification (id, item, value, product_id) VALUES (20, '原作名', 'The Java Virtual Machine Specification, Java SE 8 Edition', 3);
INSERT INTO specification (id, item, value, product_id) VALUES (21, '丛书', 'Java核心技术系列', 3);
INSERT INTO specification (id, item, value, product_id) VALUES (22, 'ISBN', '9787111501596', 3);
INSERT INTO specification (id, item, value, product_id) VALUES (23, '页数', '330', 3);
INSERT INTO specification (id, item, value, product_id) VALUES (24, '出版社', '机械工业出版社', 3);
INSERT INTO specification (id, item, value, product_id) VALUES (25, '出版年', '2015-6', 3);
INSERT INTO specification (id, item, value, product_id) VALUES (26, '装帧', '平装', 3);
INSERT INTO specification (id, item, value, product_id) VALUES (27, '作者', '周志明', 4);
INSERT INTO specification (id, item, value, product_id) VALUES (28, '副标题', 'JVM高级特性与最佳实践', 4);
INSERT INTO specification (id, item, value, product_id) VALUES (29, 'ISBN', '9787111421900', 4);
INSERT INTO specification (id, item, value, product_id) VALUES (30, '书名', '深入理解Java虚拟机（第2版）', 4);
INSERT INTO specification (id, item, value, product_id) VALUES (31, '页数', '433', 4);
INSERT INTO specification (id, item, value, product_id) VALUES (32, '丛书', '华章原创精品', 4);
INSERT INTO specification (id, item, value, product_id) VALUES (33, '出版社', '机械工业出版社', 4);
INSERT INTO specification (id, item, value, product_id) VALUES (34, '出版年', '2013-9-1', 4);
INSERT INTO specification (id, item, value, product_id) VALUES (35, '装帧', '平装', 4);
INSERT INTO specification (id, item, value, product_id) VALUES (36, '作者', 'Tim Lindholm / Frank Yellin 等', 5);
INSERT INTO specification (id, item, value, product_id) VALUES (37, '译者', '周志明 / 薛笛 / 吴璞渊 / 冶秀刚', 5);
INSERT INTO specification (id, item, value, product_id) VALUES (38, '原作名', 'The Java Virtual Machine Specification, Java SE 7 Edition', 5);
INSERT INTO specification (id, item, value, product_id) VALUES (39, '副标题', '从图灵机到人工智能', 5);
INSERT INTO specification (id, item, value, product_id) VALUES (40, 'ISBN', '9787111445159', 5);
INSERT INTO specification (id, item, value, product_id) VALUES (41, '页数', '316', 5);
INSERT INTO specification (id, item, value, product_id) VALUES (42, '出版社', '机械工业出版社', 5);
INSERT INTO specification (id, item, value, product_id) VALUES (43, '丛书', 'Java核心技术系列', 5);
INSERT INTO specification (id, item, value, product_id) VALUES (44, '出版年', '2014-1', 5);
INSERT INTO specification (id, item, value, product_id) VALUES (45, '装帧', '平装', 5);
INSERT INTO specification (id, item, value, product_id) VALUES (46, '作者', '周志明 / 谢小明 ', 6);
INSERT INTO specification (id, item, value, product_id) VALUES (47, '副标题', 'Equinox原理、应用与最佳实践', 6);
INSERT INTO specification (id, item, value, product_id) VALUES (48, 'ISBN', '9787111408871', 6);
INSERT INTO specification (id, item, value, product_id) VALUES (49, '书名', '智慧的疆界', 6);
INSERT INTO specification (id, item, value, product_id) VALUES (50, '丛书', '华章原创精品', 6);
INSERT INTO specification (id, item, value, product_id) VALUES (51, '页数', '432', 6);
INSERT INTO specification (id, item, value, product_id) VALUES (52, '出版社', '机械工业出版社', 6);
INSERT INTO specification (id, item, value, product_id) VALUES (53, '出版年', '2013-2-25', 6);
INSERT INTO specification (id, item, value, product_id) VALUES (54, '装帧', '平装', 6);
INSERT INTO specification (id, item, value, product_id) VALUES (55, '作者', '周志明', 7);
INSERT INTO specification (id, item, value, product_id) VALUES (56, '副标题', 'JVM高级特性与最佳实践', 7);
INSERT INTO specification (id, item, value, product_id) VALUES (57, 'ISBN', '9787111349662', 7);
INSERT INTO specification (id, item, value, product_id) VALUES (58, '书名', '深入理解Java虚拟机', 7);
INSERT INTO specification (id, item, value, product_id) VALUES (59, '页数', '387', 7);
INSERT INTO specification (id, item, value, product_id) VALUES (60, '出版社', '机械工业出版社', 7);
INSERT INTO specification (id, item, value, product_id) VALUES (61, '出版年', '2011-6', 7);
INSERT INTO specification (id, item, value, product_id) VALUES (62, '装帧', '平装', 7);
INSERT INTO specification (id, item, value, product_id) VALUES (63, '作者', '周志明', 8);
INSERT INTO specification (id, item, value, product_id) VALUES (64, 'ISBN', '9787111349662', 8);
INSERT INTO specification (id, item, value, product_id) VALUES (65, '书名', '凤凰架构', 8);
INSERT INTO specification (id, item, value, product_id) VALUES (66, '页数', '409', 8);
INSERT INTO specification (id, item, value, product_id) VALUES (67, '出版社', '机械工业出版社', 8);
INSERT INTO specification (id, item, value, product_id) VALUES (68, '出版年', '2020-6', 8);
INSERT INTO specification (id, item, value, product_id) VALUES (69, '装帧', '在线', 8);
INSERT INTO specification (id, item, value, product_id) VALUES (70, '副标题', '构建可靠的大型分布式系统', 8);

INSERT INTO stockpile (id, amount, frozen, product_id) VALUES (1, 30, 0, 1);
INSERT INTO stockpile (id, amount, frozen, product_id) VALUES (2, 30, 0, 2);
INSERT INTO stockpile (id, amount, frozen, product_id) VALUES (3, 30, 0, 3);
INSERT INTO stockpile (id, amount, frozen, product_id) VALUES (4, 30, 0, 4);
INSERT INTO stockpile (id, amount, frozen, product_id) VALUES (5, 30, 0, 5);
INSERT INTO stockpile (id, amount, frozen, product_id) VALUES (6, 30, 0, 6);
INSERT INTO stockpile (id, amount, frozen, product_id) VALUES (7, 30, 0, 7);
INSERT INTO stockpile (id, amount, frozen, product_id) VALUES (8, 30, 0, 8);

INSERT INTO wallet (id, money, account_id) VALUES (1, 100, 1);
INSERT INTO wallet (id, money, account_id) VALUES (2, 500, 2);
INSERT INTO wallet (id, money, account_id) VALUES (3, 290, 7);

```

# 接口

>get  /restful-api/accounts/{username}  根据用户名查找账户
>
>post /restful-api/accounts 创建账户
>
>put /restful-api/accounts 更新账户信息
>
>
>
>get /restful-api/advertisements 广告信息
>
>
>
>get /restful-api/products//{id} 根据id查询产品
>
>get  /restful-api/products/stockpile/{productId} 根据产品id查询库存
>
>patch  /restful-api/products/stockpile/{productId} 参数：amount 调整指定产品的库存
>
>delete /restful-api/products/{id} 删除指定产品
>
>put /restful-api/products 更新产品信息
>
>Post  /restful-api/products/  增加产品信息
>
>
>
>post /restful-api/settlements 生成订单
>
>patch /restful-api/pay/{payId} 支付
>
>get /restful-api/modify/{payId}

页面首页请求了广告、产品两个接口用于上边的广告与下边的热销书籍

创建一个用户，调用了创建账户的方法

```
根据支付信息生成一个支付订单，，订单包含总价、超时时间、一个随机生成的uuid、一个支付链接/pay/modify/uuid?state=PAYED&accountId=accountId（实际上是预先写好的图片），这里的付款是模拟的而不是真实支付的，本来应该由第三方回调

订单有4个状态：等待支付、取消、支付、超时回滚
开始用链接生成一个订单时，订单状态是waiting，并且把这个订单放入缓存，key是订单id，同时生成一个2分钟的定时器，返回订单信息

如果用户发送了支付请求，那么就扣减库存（实质上就是解冻库存）扣减钱包数据,修改订单状态
如果用户发送了取消请求，那么就解冻库存同时加库存

无论支付还是取消，2分钟以后都会执行一次定时任务，如果定时任务发现订单还是waiting状态那么就修改为超时未支付，同时付款时要判断
```

```
客户端首先创建一个用户，此时不鉴权

ResourceServerConfiguration：用来配置资源服务器，此处规定服务是无状态的，oauth开头的请求不鉴权，并且使用JWTAccessTokenService提供的token服务

JWTAccessTokenService：注入了JWTAccessToken、OAuthClientDetailsService

JWTAccessToken：规定了签名私钥、同时在令牌中加入用户的其他信息避免只有一个用户名

OAuthClientDetailsService：规定了获取令牌的客户端ID、秘钥，以及获取令牌的方式

WebSecurityConfiguration：用来配置对http的拦截，此处规定static的资源都不鉴权，不允许前台缓存，任何资源都从后端拿
```



# 补充知识

缓存头Cache-Control:

- public：在HTTP请求返回的过程中，在cache-control设置了public这个值，代表这个HTTP请求返回的内容中所经过的任何路径当中，包括一些中间的HTTP的代理服务器以及发出这个请求的客户端浏览器都可以对这个返回的内容进行缓存的操作。
- private：表示发起请求的这个浏览器才能进行缓存的
- no-cache：每次发送请求都要去服务器验证一下，如果服务器告诉可以使用缓存，才使用本地缓存



- 浏览器为什么不允许跨域？

当一个XHR请求 url 的协议、域名、端口三者之间任意一个与当前页面 url 不同即为跨域

所谓跨域就是阻止一个域的javascript脚本和另外一个域的内容进行交互，为了安全，不同源的客户端js脚本在没有明确授权的情况下，不能读写对方资源（a.com下的js脚本采用ajax不能读取b.com里面的文件数据，会发生跨域错误）

前后端分离开发的时候就涉及到跨域

- 怎么解决跨域？
  - 禁用浏览器的同源策略
  - 合并前后端项目
  - JSONP实现跨域请求的原理就是动态创建script标签，利用“src”不受同源策略约束的性质来实现跨域获取数据
  - 代理，让同一域名后端去请求
  - CORS，后台的请求响应头告诉浏览器“我的这个请求很安全，允许当前域名跨域访问”。如何去实现的呢，其实这是利用了CORS（Cross-Origin Resource Sharing, 跨源资源共享），听起来牛逼哄哄的，其实也就是一个W3C新标准，浏览器检测到响应头的一些字段的值后，跳过同源策略。



公司sso鉴权

```
首先去访问app1，没带cookie等有效的信息，app1给一个302重定向（包含自己的clientid、回调地址），浏览器重新访问sso，用户在sso中心登录，登录成功后浏览器在sso的域名下记录下某种cookie，然后浏览器得到一个sso给的code，浏览器带着这个code去app1,app1去sso换取token，然后返给前端在app1的域名下记录下某种cookie，以后携带cookie访问app1即可

访问app2时，前边流程一样当重定向时用户不必自己登录，如果sso下的cookie仍然有效那么会自己登录，后边流程也一样
```

oauth2鉴权

```
https://www.ruanyifeng.com/blog/2019/04/oauth-grant-types.html
```



kubectl cluster-info

```
Kubernetes control plane is running at https://kubernetes.docker.internal:6443
CoreDNS is running at https://kubernetes.docker.internal:6443/api/v1/namespaces/kube-system/services/kube-dns:dns/proxy

To further debug and diagnose cluster problems, use 'kubectl cluster-info dump'.
```

kubectl get nodes

```
NAME       STATUS  ROLES         AGE  VERSION
docker-desktop  Ready  control-plane,master  172d  v1.21.3
```



kubectl get pods -n kube-system

```
NAME                   READY  STATUS  RESTARTS  AGE
coredns-558bd4d5db-5hrk4         1/1   Running  0     172d
coredns-558bd4d5db-wjpbk         1/1   Running  0     172d
etcd-docker-desktop           1/1   Running  0     172d
kube-apiserver-docker-desktop      1/1   Running  3     172d
kube-controller-manager-docker-desktop  1/1   Running  0     172d
kube-proxy-xlfkk             1/1   Running  0     172d
kube-scheduler-docker-desktop      1/1   Running  1     172d
storage-provisioner           1/1   Running  3     172d
vpnkit-controller            1/1   Running  108    172d
```

dashboard-svc.yaml

```
# 内容
kind: Service
apiVersion: v1
metadata:
  labels:
    k8s-app: kubernetes-dashboard
  name: kubernetes-dashboard
  namespace: kubernetes-dashboard
spec:
  type: NodePort
  ports:
    - port: 443
      targetPort: 8443
  selector:
    k8s-app: kubernetes-dashboard
```

kubectl apply -f dashboard-svc.yaml

```
# 结果
apiVersion: v1
kind: ServiceAccount
metadata:
  name: dashboard-admin
  namespace: kube-system
---
kind: ClusterRoleBinding
apiVersion: rbac.authorization.k8s.io/v1beta1
metadata:
  name: dashboard-admin
subjects:
  - kind: ServiceAccount
    name: dashboard-admin
    namespace: kube-system
roleRef:
  kind: ClusterRole
  name: cluster-admin
  apiGroup: rbac.authorization.k8s.io
```

kubectl apply -f dashboard-svc-account.yaml

kubectl get secret -n kube-system |grep admin|awk '{print $1}'

```
dashboard-admin-token-bwgjv
```

kubectl describe secret dashboard-admin-token-bwgjv -n kube-system|grep '^token'|awk '{print $2}'

```
注意内容与上一条输出相关，将得到一个token
```

kubectl proxy 

访问：

http://localhost:8001/api/v1/namespaces/kubernetes-dashboard/services/https:kubernetes-dashboard:/proxy/

用token登入将得到仪表盘



