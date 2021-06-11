package com.spark.scalasql.Model;

import java.util.List;

public class Table {
    private String tableName;
    private List<Col> cols;

    public Table(String tableName, List<Col> cols) {
        this.tableName = tableName;
        this.cols = cols;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<Col> getCols() {
        return cols;
    }

    public void setCols(List<Col> cols) {
        this.cols = cols;
    }

    @Override
    public String toString() {
        return "Table{" +
                "tableName='" + tableName + '\'' +
                ", cols=" + cols +
                '}';
    }
}
