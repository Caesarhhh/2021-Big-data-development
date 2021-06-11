package com.spark.scalasql.Dao

import java.sql.ResultSet

import com.spark.scalasql.Session.ScalaSqlSession

class ScalaSql {
  def gettables(scalaSqlSession: ScalaSqlSession): ResultSet ={
    scalaSqlSession.excuteSql("show tables")
  }
  def getdatabases(scalaSqlSession: ScalaSqlSession): ResultSet ={
    scalaSqlSession.excuteSql("show databases")
  }
  def getColsdata(scalaSqlSession: ScalaSqlSession,tablename:String): ResultSet ={
    scalaSqlSession.excuteSql("desc formatted "+tablename)
  }
  def excute(scalaSqlSession: ScalaSqlSession,sql:String): ResultSet ={
    scalaSqlSession.excuteSql(sql)
  }
}
