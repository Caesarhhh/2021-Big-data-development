package com.spark.scalasql.Model;

import java.util.List;

public class Database {
    private String databaseName;
    private List<Table> tables;

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public List<Table> getTables() {
        return tables;
    }

    public void setTables(List<Table> tables) {
        this.tables = tables;
    }

    public Database(String databaseName, List<Table> tables) {
        this.databaseName = databaseName;
        this.tables = tables;
    }

    @Override
    public String toString() {
        return "Database{" +
                "databaseName='" + databaseName + '\'' +
                ", tables=" + tables +
                '}';
    }
}
