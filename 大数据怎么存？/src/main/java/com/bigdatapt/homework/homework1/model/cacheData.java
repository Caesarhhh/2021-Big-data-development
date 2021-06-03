package com.bigdatapt.homework.homework1.model;

import com.amazonaws.services.s3.model.InitiateMultipartUploadRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadResult;
import com.amazonaws.services.s3.model.PartETag;
import com.bigdatapt.homework.homework1.config.S3Config;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class cacheData implements Serializable {
    private List<TaskDetail>taskDetails;
    private ArrayList<PartETag>partETags;
    private InitiateMultipartUploadResult multipartUploadResult;
    private List<FileData>lastS3Files;

    public List<FileData> getLastS3Files() {
        return lastS3Files;
    }

    public void setLastS3Files(List<FileData> lastS3Files) {
        this.lastS3Files = lastS3Files;
    }

    public InitiateMultipartUploadResult getMultipartUploadResult() {
        return multipartUploadResult;
    }

    public void setMultipartUploadResult(InitiateMultipartUploadResult multipartUploadResult) {
        this.multipartUploadResult = multipartUploadResult;
    }

    public List<TaskDetail> getTaskDetails() {
        return taskDetails;
    }

    public void setTaskDetails(List<TaskDetail> taskDetails) {
        this.taskDetails = taskDetails;
    }

    public ArrayList<PartETag> getPartETags() {
        return partETags;
    }

    public void setPartETags(ArrayList<PartETag> partETags) {
        this.partETags = partETags;
    }

    public cacheData(List<TaskDetail> taskDetails, ArrayList<PartETag> partETags, InitiateMultipartUploadResult multipartUploadResult , List<FileData> lastS3Files) {
        this.taskDetails = taskDetails;
        this.lastS3Files = lastS3Files;
        this.partETags = partETags;
        this.multipartUploadResult = multipartUploadResult;
    }

    public void addAheadPos(int offset){
        if(this.taskDetails.size()>0&&this.taskDetails.get(0).getDataType()>0){
            this.taskDetails.get(0).addCurrentpos(offset);
        }
    }

    public void removeAheadTask(){
        if(this.taskDetails.size()>0){
            this.taskDetails.remove(0);
        }
    }

    @Override
    public String toString() {
        return "cacheData{" +
                "taskDetails=" + taskDetails +
                ", lastS3Files=" + lastS3Files +
                ", partETags=" + partETags +
                ", multipartUploadResult=" + multipartUploadResult +
                '}';
    }

    public static cacheData getCacheByPath(String path){
        Object temp=null;
        File file =new File(path);
        FileInputStream in;
        if(System.currentTimeMillis()-file.lastModified()>S3Config.Overdue_Second*1000){
            System.out.println("日志已过期，执行下一项命令");
            return null;
        }
        try {
            in = new FileInputStream(file);
            ObjectInputStream objIn=new ObjectInputStream(in);
            temp=objIn.readObject();
            objIn.close();
        } catch (IOException e) {
            System.out.println("日志不存在，执行下一项命令");
        } catch (ClassNotFoundException e) {
            System.out.println("日志读取错误，执行下一项命令");
        }
        return (cacheData) temp;
    }

    public static void saveToCache(cacheData cacheData,String path){
        File file =new File(path);
        FileOutputStream out;
        try {
            out = new FileOutputStream(file);
            ObjectOutputStream objOut=new ObjectOutputStream(out);
            objOut.writeObject(cacheData);
            objOut.flush();
            objOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
//        List<TaskDetail>taskDetails=new ArrayList<>();
//        ArrayList<PartETag>partETags=new ArrayList<>();
//        cacheData cacheData=new cacheData(taskDetails,partETags);
//        saveToCache(cacheData, S3Config.cachePath);
        cacheData caches=getCacheByPath(S3Config.cachePath);
        System.out.println(caches);
    }
}
