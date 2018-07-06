package com.cecdata.bdp2pg.pg

import java.sql.{Connection, ResultSet, Statement}
import java.util.Properties

import com.cecdata.bdp2pg.common._

/**
  * @project bdp2pg
  * @description
  * @author zhuweilin
  * @mail zhuwillyn@163.com
  * @date 2018/07/05 10:30
  * @version
  */
object PostgreUtil {

    def genTable(sql: String, url: String, props: Properties): Unit = {
        try {
            val conn: Connection = CommonUtil.getConn(url, props)
        } catch {
            case e: Exception => {
                e.printStackTrace()
            }
        }
    }


    def execSQL(sql: String, conn: Connection): Unit = {
        var statement: Statement = null
        try {
            statement = conn.createStatement()
            statement.execute(sql)
        } catch {
            case e: Exception => {
                e.printStackTrace()
            }
        } finally {
            statement.close()
        }
    }

    def execSQL(sql: String, statement: Statement): Unit = {
        try {
            statement.execute(sql)
        } catch {
            case e: Exception => {
                e.printStackTrace()
            }
        } finally {
            statement.close()
        }
    }

    def execSQLForResult(sql: String, connection: Connection): ResultSet = {
        var statement: Statement = null
        var resultSet: ResultSet = null
        try {
            statement = connection.createStatement()
            resultSet = statement.executeQuery(sql)
        } catch {
            case e: Exception => {
                e.printStackTrace()
            }
        } finally {
            statement.close()
        }
        resultSet
    }

    def execSQLForResult(sql: String, statement: Statement): ResultSet = {
        var resultSet: ResultSet = null
        try {
            resultSet = statement.executeQuery(sql)
        } catch {
            case e: Exception => {
                e.printStackTrace()
            }
        } finally {
            statement.close()
        }
        resultSet
    }

}
