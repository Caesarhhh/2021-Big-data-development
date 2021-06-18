package com.bigdata.week3.job4.Kafka;

import com.alibaba.fastjson.JSONObject;
import com.bigdata.week3.job4.Model.BuyInfo;
import com.bigdata.week3.job4.Service.BuyInfoService;
import org.apache.flink.api.common.io.OutputFormat;
import org.apache.flink.configuration.Configuration;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;

public class MySqlWriter implements OutputFormat<String> {
    Statement statement;

    @Override
    public void configure(Configuration configuration) {
        Connection connection= null;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bigdata","caesar","164375");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            statement=connection.createStatement();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void open(int i, int i1) throws IOException {

    }

    @Override
    public void writeRecord(String s) throws IOException {
        BuyInfo buyInfo= JSONObject.parseObject(s.split("@")[1],BuyInfo.class);
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String sql="CREATE TABLE if not exists "+s.split("@")[0]+" (\n" +
                    "  `username` varchar(50) DEFAULT NULL COMMENT '姓名',\n" +
                    "  `buy_time` datetime DEFAULT NULL COMMENT '购票时间',\n" +
                    "  `buy_address` varchar(500) DEFAULT NULL COMMENT '购票地址',\n" +
                    "  `origin` varchar(100) DEFAULT NULL COMMENT '出发地',\n" +
                    "  `destination` varchar(100) DEFAULT NULL COMMENT '目的地'\n" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
            statement.execute(sql);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time="str_to_date('"+sdf.format(buyInfo.getBuy_time())+"', '%Y-%m-%d %H:%i:%s')";
            sql="insert into "+s.split("@")[0]+" (username,buy_time,buy_address,origin,destination) values('"+buyInfo.getUsername()+"',"+time+",'"+buyInfo.getBuy_address()+"','"+buyInfo.getOrigin()+"','"+buyInfo.getDestination()+"');";
            statement.execute(sql);
            System.out.println(sql);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        //System.out.println("数据"+buyInfo+"已经存入表"+s.split("@")[0]+"中");
    }

    @Override
    public void close() throws IOException {

    }
}
