package com.bigdatapt.homework.homework1.model;

import java.io.Serializable;
import java.util.Objects;

public class FileData implements Serializable {
    private String name;
    private long time;
    private boolean isFile;
    private long size;

    public FileData(String name, long time, boolean isFile, long size) {
        this.name = name;
        this.time = time;
        this.isFile = isFile;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isFile() {
        return isFile;
    }

    public void setFile(boolean file) {
        isFile = file;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileData fileData = (FileData) o;
        return time == fileData.time &&
                isFile == fileData.isFile &&
                Objects.equals(name, fileData.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, time, isFile);
    }

    @Override
    public String toString() {
        return "FileData{" +
                "name='" + name + '\'' +
                ", time=" + time +
                ", isFile=" + isFile +
                ", size=" + size +
                '}';
    }
}
