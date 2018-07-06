package com.cecdata.bdp2pg.common

import java.sql.{Connection, DriverManager}
import java.util.Properties

/**
  * @project bdp2pg
  * @description
  * @author zhuweilin
  * @mail zhuwillyn@163.com
  * @date 2018/07/05 10:31
  * @version
  */
object CommonUtil {

    def getConn(url: String, props: Properties): Connection = {
        val connect: Connection = DriverManager.getConnection(url, props)
        connect
    }

    def getDriverByUrl(url: String): String = {
        if (url.indexOf("mysql") == 5){
            return Constants.DB.MYSQL_DRIVER
        } else if(url.indexOf("postgresql") == 5){
            return Constants.DB.POSTGRESQL_DRIVER
        }
        throw new RuntimeException("database url error : must be mysql or postgresql url")
    }

    def splicingString(src:String): String ={
        splicingString(src, ",")
    }

    def splicingString(src:Array[String]): String ={
        splicingString(src, ",")
    }

    def splicingString(src:String, regex:String): String ={
        val split: Array[String] = src.split(regex)
        splicingString(split, regex)
    }

    def splicingString(src:Array[String], regex:String): String ={
        val sb = new StringBuilder()
        for (single <- src){
            sb.append(single + regex)
        }
        val res = sb.toString().substring(0, sb.length - 1)
        res
    }

}
