package com.cecdata.bdp2pg

import com.cecdata.bdp2pg.spark.SparkMainLauncher
import com.cecdata.bdp2pg.common._
import org.apache.commons.cli._

import scala.collection.mutable


/**
  * @project bdp2pg
  * @description
  * @author zhuweilin
  * @mail zhuwillyn@163.com
  * @date 2018/07/04 14:28
  * @version
  */
object MainApplication {

    def main(args: Array[String]) {
        val options = new Options()
        val help = new Option(Constants.Help.Short.HELP, Constants.Help.Long.HELP, false, "Print help.")
        help.setRequired(false)
        options.addOption(help)
        // 抽取源数据的hive数据库名称
        val hiveDatabase = new Option(Constants.Help.Short.HIVE_DATABASE, Constants.Help.Long.HIVE_DATABASE, true, "The database of hive.")
        hiveDatabase.setRequired(true)
        options.addOption(hiveDatabase)
        // 抽取源数据指定医院代码，多个医院用英文逗号隔开
        val codes = new Option(Constants.Help.Short.HOSPITAL_CODE, Constants.Help.Long.HOSPITAL_CODE, true, "The codes of hospital, multi code split by ','.")
        codes.setRequired(false)
        options.addOption(codes)
        // 抽取数据指定hive相关表，多个表用英文逗号隔开
        val hiveTbls = new Option(Constants.Help.Short.HIVE_TABLE, Constants.Help.Long.HIVE_TABLE, true, "The table of hive, multi table split by ','.")
        hiveTbls.setRequired(false)
        options.addOption(hiveTbls)
        // 目标数据库用户名
        val user = new Option(Constants.Help.Short.USERNAME, Constants.Help.Long.USERNAME, true, "The username of database.")
        user.setRequired(true)
        options.addOption(user)
        // 目标数据库密码
        val password = new Option(Constants.Help.Short.PASSWORD, Constants.Help.Long.PASSWORD, true, "The password for username of database.")
        password.setRequired(true)
        options.addOption(password)
        // 目标数据库连接
        val url = new Option(Constants.Help.Short.URL, Constants.Help.Long.URL, true, "The jdbc url of database.")
        url.setRequired(true)
        options.addOption(url)

        val parser = new PosixParser()
        val helpFormatter = new HelpFormatter()
        helpFormatter.setWidth(120)

        var commandLine: CommandLine = null
        try {
            // 当输入参数为空或者"-h" "--help"时打印帮助信息
            commandLine = parser.parse(options, args)
            if (commandLine.hasOption(Constants.Help.Short.HELP) || commandLine.getOptions().length == 0) {
                helpFormatter.printHelp("java -jar bdp2pg-1.0.jar", options, true)
                System.exit(-1)
            }
        } catch {
            case ex: ParseException => {
                println(ex.getMessage)
                helpFormatter.printHelp("java -jar bdp2pg-1.0.jar", options, true)
                System.exit(-1)
            }
            case e: Exception => {
                println(e.getMessage)
                System.exit(-1)
            }
        }
        // 将参数解析并封装至map中传递给spark job具体执行类
        val params = new mutable.HashMap[String, String]()
        val opts: Array[Option] = commandLine.getOptions
        for (opt <- opts){
            val shortOpt: String = opt.getOpt
            // val longOpt: String = opt.getLongOpt
            val value: String = opt.getValue
            params.put(shortOpt, value)
        }
        val launcher: SparkMainLauncher = new SparkMainLauncher(params)
        // 启动spark job
        launcher.launch()
    }

}
