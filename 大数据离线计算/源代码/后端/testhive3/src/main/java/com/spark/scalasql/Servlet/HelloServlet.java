package com.spark.scalasql.Servlet;

import com.alibaba.fastjson.JSON;
import com.spark.scalasql.Factory.SqlSessionFactory;
import com.spark.scalasql.Model.Table;
import com.spark.scalasql.Service.ScalaService;
import com.spark.scalasql.Session.ScalaSqlSession;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class HelloServlet extends HttpServlet{
    @Override
    protected void service(HttpServletRequest request,
                           HttpServletResponse response) throws IOException {
        ScalaSqlSession scalaSqlSession= SqlSessionFactory.getSqlSessionFactory().getSqlSession("bigdata116.depts.bingosoft.net","user19_db",22116,"user19","pass@bingo19");
        ScalaService scalaService=new ScalaService();
        OutputStream out=response.getOutputStream();
        response.setCharacterEncoding("UTF-8");
        response.setHeader("content-type","text/html;charset=UTF-8");
        try {
            Table res=scalaService.SparkSqlExcute(scalaSqlSession,"select * from testtable");
            String json=JSON.toJSONString(res);
            System.out.println(json);
            out.write(json.getBytes("UTF-8"));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        System.out.println();
    }
}
