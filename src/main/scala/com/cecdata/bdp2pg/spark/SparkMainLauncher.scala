package com.cecdata.bdp2pg.spark

import java.sql.{DriverManager, Statement, Connection}
import java.util.Properties

import com.cecdata.bdp2pg.pg.PostgreUtil
import org.apache.commons.lang.StringUtils
import org.apache.spark.sql.{Row, DataFrame, SparkSession}

import com.cecdata.bdp2pg.common._

import scala.collection.mutable
import scala.util.control.Breaks._

/**
  * @project bdp2pg
  * @description
  * @author zhuweilin
  * @mail zhuwillyn@163.com
  * @date 2018/07/05 11:11
  * @version
  */
class SparkMainLauncher(val params: mutable.HashMap[String, String]) {

    def launch(): Unit = {
        // 此为本地local模式，若需运行在集群或者yarn上，需要修改
        val session: SparkSession = SparkSession.builder()
            .appName("LoadHive2MySQL").enableHiveSupport()
            .master("local").getOrCreate()

        val hiveDatabase: String = params(Constants.Help.Short.HIVE_DATABASE)
        val hiveTable: String = params(Constants.Help.Short.HIVE_TABLE)
        val hospitalCode: String = params(Constants.Help.Short.HOSPITAL_CODE)
        val username: String = params(Constants.Help.Short.USERNAME)
        val password: String = params(Constants.Help.Short.PASSWORD)
        val url: String = params(Constants.Help.Short.URL)
        // 若传入的hive数据库名不为空则切换到该库，否则使用默认default库
        if (StringUtils.isNotEmpty(hiveDatabase)) {
            session.sql(s"use ${hiveDatabase}")
        }
        var tables: Array[String] = null
        if (StringUtils.isEmpty(hiveTable)) {
            // 若传入hive表参数为空，则获取该hive库下所有第二列（表名）列表
            tables = session.sql("show tables").rdd.map(row => row.getString(1)).collect()
        } else {
            // 否则按照英文逗号切割
            tables = hiveTable.split(",")
        }
        var inStr: String = null
        if (StringUtils.isNotEmpty(hospitalCode)) {
            val codes: Array[String] = hospitalCode.split(",")
            inStr = CommonUtil.splicingString(codes)
        }
        // 封装数据库参数
        val props = new Properties()
        props.put("user", username)
        props.put("password", password)
        props.put("driver", CommonUtil.getDriverByUrl(url))
        val connection: Connection = CommonUtil.getConn(url, props)
        for (tbl <- tables) {
            var deleteSQL = s"""delete from "${Constants.DB.SCHEMA_NAME}"."${tbl}""""
            var selectSQL = s"select * from ${tbl}"
            if(StringUtils.isNotEmpty(inStr)){
                deleteSQL = deleteSQL + s" where ${Constants.SQL.PARTITION_KEY} in (${inStr})"
                selectSQL = selectSQL + s" where ${Constants.SQL.PARTITION_KEY}  in (${inStr})"
            }
            // 查看hive表结构并获取第一列（列名）数据
            val fields: Array[String] = session.sql(s"desc ${tbl}").rdd.map(row => row.getString(0)).collect()
            // 封装创建表SQL语句
            val builder = new StringBuilder(s"""create table if not exists "${Constants.DB.SCHEMA_NAME}"."${tbl}" (""")
            // breakable块
            breakable {
                for (field <- fields) {
                    // 当列名以#开始则跳出循环（即当出现#时，代表列列表循环完毕，开始循环分区信息，故可忽略）
                    if (field.startsWith("#")) {
                        break
                    }
                    builder.append(s"${field} varchar(255) DEFAULT NULL,")
                }
            }
            // 去除SQL语句最后一个逗号
            val createTableSQL = builder.substring(0, builder.length - 1) + ")"
            // 创建表
            PostgreUtil.execSQL(createTableSQL, connection)
            // 删除数据
            PostgreUtil.execSQL(deleteSQL, connection)
            // 读取hive表中数据并同步至目标库中
            session.sql(selectSQL).write.mode("append").jdbc(url, s""""${Constants.DB.SCHEMA_NAME}"."${tbl}"""", props)
        }
        session.close()
    }

}
