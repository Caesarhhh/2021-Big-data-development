package com.bigdata.week3.job3.Model;

import com.alibaba.fastjson.JSONObject;

import java.util.Date;

public class BuyInfo {
    private String username;
    private Date buy_time;
    private String buy_address;
    private String origin;
    private String destination;

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getBuy_time() {
        return buy_time;
    }

    public void setBuy_time(Date buy_time) {
        this.buy_time = buy_time;
    }

    public String getBuy_address() {
        return buy_address;
    }

    public void setBuy_address(String buy_address) {
        this.buy_address = buy_address;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public BuyInfo(String username, Date buy_time, String buy_address, String origin, String destination) {
        this.username = username;
        this.buy_time = buy_time;
        this.buy_address = buy_address;
        this.origin = origin;
        this.destination = destination;
    }

    public static String getDesfronString(String json){
        BuyInfo buyInfo= JSONObject.parseObject(json,BuyInfo.class);
        return buyInfo.getDestination();
    }

    public BuyInfo() {
    }

    @Override
    public String toString() {
        return "BuyInfo{" +
                "username='" + username + '\'' +
                ", buy_time=" + buy_time +
                ", buy_address='" + buy_address + '\'' +
                ", origin='" + origin + '\'' +
                ", destination='" + destination + '\'' +
                '}';
    }
}
