package com.cecdata.bdp2pg.common

/**
  * @project bdp2pg
  * @description
  * @author zhuweilin
  * @mail zhuwillyn@163.com
  * @date 2018/07/06 11:44
  * @version
  */
object Constants {

    object DB {
        val MYSQL_DRIVER = "com.mysql.jdbc.Driver"
        val POSTGRESQL_DRIVER = "org.postgresql.Driver"
        val SCHEMA_NAME = "cecd_his"
    }

    object SQL {
        val PARTITION_KEY = "SJYYLJGDM_PARTITION"
    }

    object Help {


        object Short {
            val HELP = "h"
            val HIVE_DATABASE = "d"
            val HOSPITAL_CODE = "c"
            val HIVE_TABLE = "t"
            val USERNAME = "n"
            val PASSWORD = "w"
            val URL = "u"
        }

        object Long {
            val HELP = "help"
            val HIVE_DATABASE = "hive-database"
            val HOSPITAL_CODE = "hospital-codes"
            val HIVE_TABLE = "hive-tables"
            val USERNAME = "user"
            val PASSWORD = "password"
            val URL = "url"
        }

    }

}
