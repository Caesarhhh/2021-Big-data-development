package com.spark.scalasql.Factory;

import com.spark.scalasql.Session.ScalaSqlSession;

import java.util.HashMap;

public class SqlSessionFactory {
    private static HashMap<String, ScalaSqlSession> sessionMap;

    public static SqlSessionFactory sqlSessionFactory=null;

    private SqlSessionFactory(){
        sessionMap=new HashMap<>();
    }

    public static ScalaSqlSession getSqlSession(String host,String dbname,int port,String userName,String password){
        String key=host+dbname+port+userName+password;
        if(sessionMap.containsKey(key)&&(!sessionMap.get(key).isClose())){
            return sessionMap.get(key);
        }
        else{
            ScalaSqlSession scalaSqlSession=new ScalaSqlSession(host,dbname,port,userName,password);
            scalaSqlSession.init();
            sessionMap.put(key,scalaSqlSession);
            return scalaSqlSession;
        }
    }

    public static synchronized SqlSessionFactory getSqlSessionFactory(){
        if(sqlSessionFactory==null){
            sqlSessionFactory=new SqlSessionFactory();
        }
        return sqlSessionFactory;
    }
}
