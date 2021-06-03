package com.bigdatapt.homework.homework1.model;

import java.io.Serializable;

public class TaskDetail implements Serializable {
    private int JobType;//0下载，1上传,2删除
    private int DataType;//0小数据，1大数据
    private String path;//数据路径信息
    private int currentpos;//对于大任务的分片当前执行位置，小任务无意义

    public TaskDetail(int jobType, int dataType, String path, int currentpos) {
        JobType = jobType;
        DataType = dataType;
        this.path = path;
        this.currentpos = currentpos;
    }

    public int getJobType() {
        return JobType;
    }

    public void setJobType(int jobType) {
        JobType = jobType;
    }

    public int getDataType() {
        return DataType;
    }

    public void setDataType(int dataType) {
        DataType = dataType;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getCurrentpos() {
        return currentpos;
    }

    public void setCurrentpos(int currentpos) {
        this.currentpos = currentpos;
    }

    public void addCurrentpos(int offset){this.currentpos+=offset;}

    @Override
    public String toString() {
        return "TaskDetail{" +
                "JobType=" + JobType +
                ", DataType=" + DataType +
                ", path='" + path + '\'' +
                ", currentpos=" + currentpos +
                '}';
    }
}
