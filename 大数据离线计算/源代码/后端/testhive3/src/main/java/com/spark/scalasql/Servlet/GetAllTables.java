package com.spark.scalasql.Servlet;

import com.alibaba.fastjson.JSON;
import com.spark.scalasql.Factory.SqlSessionFactory;
import com.spark.scalasql.Model.Result;
import com.spark.scalasql.Model.Table;
import com.spark.scalasql.Service.ScalaService;
import com.spark.scalasql.Session.ScalaSqlSession;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/getAllTables")
public class GetAllTables extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request,
                           HttpServletResponse response) throws IOException {
        String databaseName = request.getParameter("databaseName");
        String HostAddress = request.getParameter("hostAddress");
        String portNum=request.getParameter("portNum");
        String user=request.getParameter("user");
        String password=request.getParameter("password");
        ScalaSqlSession scalaSqlSession=SqlSessionFactory.getSqlSessionFactory().getSqlSession(HostAddress,databaseName,Integer.parseInt(portNum),user,password);
        ScalaService scalaService=new ScalaService();
        OutputStream out=response.getOutputStream();
        response.setCharacterEncoding("UTF-8");
        response.setHeader("content-type","text/html;charset=UTF-8");
        try {
            List<Table> res=scalaService.getAllTables(scalaSqlSession);
            String json= JSON.toJSONString(Result.succeed(res));
            out.write(json.getBytes("UTF-8"));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
