package com.spark.scalasql.Service;

import com.spark.scalasql.Dao.ScalaSql;
import com.spark.scalasql.Model.Col;
import com.spark.scalasql.Model.Table;
import com.spark.scalasql.Session.ScalaSqlSession;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ScalaService {
    ScalaSql scalaSql=new ScalaSql();
    public List<String>getAlltableNames(ScalaSqlSession scalaSqlSession) throws SQLException {
        ResultSet rs=scalaSql.gettables(scalaSqlSession);
        List<String>result=new ArrayList<String>();
        while(rs.next()){
            result.add(rs.getString(1));
        }
        return result;
    }
    public Table getTableCols(ScalaSqlSession scalaSqlSession, String tableName) throws SQLException {
        ResultSet rs=scalaSql.getColsdata(scalaSqlSession,tableName);
        List<Col>cols=new ArrayList<Col>();
        while (rs.next()) {
            String asjbh = rs.getString(1);
            if(asjbh.length()==0){
                break;
            }
            String ajmc = rs.getString(2);
            String bamjbh = rs.getString(3);
            cols.add(new Col(asjbh,ajmc,bamjbh));
        }
        return new Table(tableName,cols);
    }
    public Col getAllDatabases(ScalaSqlSession scalaSqlSession) throws SQLException {
        ResultSet resultSet=scalaSql.getdatabases(scalaSqlSession);
        List<String>res=new ArrayList<>();
        ResultSetMetaData resultSetMetaData=resultSet.getMetaData();
        int columnCount=resultSetMetaData.getColumnCount();
        List<Col>cols=new ArrayList<Col>();
        for(int i=1;i<columnCount+1;i++){
            cols.add(new Col(resultSetMetaData.getColumnName(i),resultSetMetaData.getColumnTypeName(i),"",new ArrayList<Object>()));
        }
        while(resultSet.next()){
            for(int i=0;i<columnCount;i++){
                cols.get(i).addData(resultSet.getObject(cols.get(i).getColumnName()));
            }
        }
        return cols.get(0);
    }
    public List<Table>getAllTables(ScalaSqlSession scalaSqlSession) throws SQLException {
        List<String>tableNames=getAlltableNames(scalaSqlSession);
        List<Table>result=new ArrayList<Table>();
        for(int i=0;i< tableNames.size();i++){
            result.add(getTableCols(scalaSqlSession,tableNames.get(i)));
        }
        return result;
    }
    public Table SparkSqlExcute(ScalaSqlSession scalaSqlSession,String sql) throws SQLException {
        ResultSet rs=scalaSql.excute(scalaSqlSession,sql);
        ResultSetMetaData resultSetMetaData=rs.getMetaData();
        int columnCount=resultSetMetaData.getColumnCount();
        List<Col>cols=new ArrayList<Col>();
        for(int i=1;i<columnCount+1;i++){
            cols.add(new Col(resultSetMetaData.getColumnName(i),resultSetMetaData.getColumnTypeName(i),"",new ArrayList<Object>()));
        }
        while(rs.next()){
            for(int i=0;i<columnCount;i++){
                cols.get(i).addData(rs.getObject(cols.get(i).getColumnName()));
            }
        }
        return new Table(sql,cols);
    }
}
