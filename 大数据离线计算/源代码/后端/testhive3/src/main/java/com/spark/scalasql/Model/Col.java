package com.spark.scalasql.Model;

import java.util.List;

public class Col {
    private String columnName;
    private String columnType;
    private String comment;
    private List<Object> datas;

    public Col(String columnName, String columnType, String comment) {
        this.columnName = columnName;
        this.columnType = columnType;
        this.comment = comment;
        this.datas=null;
    }

    public Col(String columnName, String columnType, String comment, List<Object> datas) {
        this.columnName = columnName;
        this.columnType = columnType;
        this.comment = comment;
        this.datas = datas;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<Object> getDatas() {
        return datas;
    }

    public void setDatas(List<Object> datas) {
        this.datas = datas;
    }

    public void addData(Object data){
        this.datas.add(data);
    }

    @Override
    public String toString() {
        return "Col{" +
                "columnName='" + columnName + '\'' +
                ", columnType='" + columnType + '\'' +
                ", comment='" + comment + '\'' +
                ", datas=" + datas +
                '}';
    }
}
