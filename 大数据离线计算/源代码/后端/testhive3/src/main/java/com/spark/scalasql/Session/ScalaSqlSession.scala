package com.spark.scalasql.Session

import java.net.UnknownHostException
import java.sql.{Connection, DriverManager, ResultSet}
import java.util.Properties

import org.apache.hive.service.cli.HiveSQLException
import org.apache.spark.sql.catalyst.analysis.NoSuchDatabaseException

class ScalaSqlSession(host: String, db: String, port: Integer, userName: String, passwordinput: String) {
  private val hostaddress: String = host
  private val dbname: String = db
  private val portNum: Integer = port
  private val user: String = userName
  private val password: String = passwordinput
  private var url: String = ""
  private var userData: Properties = null
  private var connection: Connection = null

  def isVaild(): String = {
    println("in")
    if (connection == null) {
      url = "jdbc:hive2://" + hostaddress + ":" + portNum + "/" + dbname
      userData = new Properties()
      userData.setProperty("user", user)
      userData.setProperty("password", password)
      userData.setProperty("driverClassName", "org.apache.hive.jdbc.HiveDriver")
      Class.forName("org.apache.hive.jdbc.HiveDriver")
    }
    try {
      connection = DriverManager.getConnection(url, userData)
      excuteSql("select 1")
      "success"
    } catch {
      case ex: Exception => {
        println(ex.getMessage())
        ex.getMessage()
      }
    }
  }

  def isClose(): Boolean = {
    connection.isClosed()
  }

  def getdbname(): String = {
    dbname
  }

  def init(): Unit = {
    if (connection == null) {
      url = "jdbc:hive2://" + hostaddress + ":" + portNum + "/" + dbname
      userData = new Properties()
      userData.setProperty("user", user)
      userData.setProperty("password", password)
      userData.setProperty("driverClassName", "org.apache.hive.jdbc.HiveDriver")
      Class.forName("org.apache.hive.jdbc.HiveDriver")
      connection = DriverManager.getConnection(url, userData)
    }
  }

  def excuteSql(sql: String): ResultSet = {
    if (connection != null) {
      val statement = connection.createStatement()
      val resultSet = statement.executeQuery(sql)
      resultSet
    }
    else {
      null
    }
  }

  def close(): Unit = {
    connection.close()
  }
}
