### bdp2pg项目介绍
---
#### 一、概述
本项目为scala语言编写，主要是用于将hive数据库中的数据同步至关系型数据库中，目前支持将hive数据同步至postgresql和mysql，使用Spark jdbc操作。
##### 编译环境：
```
 scala版本：2.11.6
 java版本：1.8
 maven：3.5
```

#### 二、结构
项目主体结构如下：
```
├─src
│  ├─main
│  │  ├─resources
│  │  └─scala
│  │      └─com
│  │          └─cecdata
│  │              └─bdp2pg
│  │                  ├─common
│  │                  ├─pg
│  │                  └─spark
│  └─test
│      └─java
```

#### 三、打包
通过maven组件整体打成jar包，如下：
```
# mvn命令行打包
mvn clean package
# 若要跳过测试
mvn clean package -Dmaven.test.skip=true
```
也可以通过idea插件打包，这里不予赘述。

#### 四、运行方式
因为是jar包，所以运行环境必须安装jdk1.8+，具体运行方式：
```
 java -jar bdp2pg-1.0.jar [-c <arg>] -d <arg> [-h] -n <arg> [-t <arg>] -u <arg> -w <arg>
```
下面介绍具体的参数：
```
 -c,--hospital-codes <arg>   The codes of hospital, multi code split by ','.    #机构代码，指定哪些机构数据同步，多个用英文逗号隔开，可为空，为空则所有机构同步
 -d,--hive-database <arg>    The database of hive.  #指定hive数据库下的数据需要同步至pg/mysql，不指定默认为default
 -h,--help                   Print help.    #打印帮助信息
 -n,--user <arg>             The username of database.  #关系型数据库(pg/mysql)用户名
 -t,--hive-tables <arg>      The table of hive, multi table split by ','.   #hive表，指定哪些表需要同步至pg/mysql，不指定为所有表需要同步
 -u,--url <arg>              The jdbc url of database.  #关系型数据库(pg/mysql)url
 -w,--password <arg>         The password for username of database. #关系型数据库(pg/mysql)密码
```

