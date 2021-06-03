package com.bigdatapt.homework.homework1.utils;

import com.bigdatapt.homework.homework1.config.S3Config;
import com.bigdatapt.homework.homework1.model.FileData;
import com.bigdatapt.homework.homework1.model.TaskDetail;
import com.bigdatapt.homework.homework1.model.cacheData;

import java.util.List;

public class TaskExcutor {

    public static void runTask(TaskDetail taskDetail){
        if(taskDetail.getJobType()==0){
            if(taskDetail.getDataType()==0){
                Download.DownloadByPath(taskDetail.getPath());
            }
            else{
                Download.DownloadBigData(taskDetail.getPath(),taskDetail.getCurrentpos());
            }
        }
        else if(taskDetail.getJobType()==1){
            if(taskDetail.getDataType()==0){
                Upload.UploadByPath(taskDetail.getPath());
            }
            else{
                Upload.UploadBigData(taskDetail.getPath(),taskDetail.getCurrentpos());
            }
        }
        else if(taskDetail.getJobType()==2){
            Delete.DeleteS3ByPath(taskDetail.getPath());
        }
    }

    public static void runList(List<TaskDetail>taskDetails){
        System.out.println("taskExcutor接收到"+taskDetails.size()+"项任务，开始执行...");
        List<FileData>s3files=null;
        if(cacheData.getCacheByPath(S3Config.cachePath)!=null){
            s3files=cacheData.getCacheByPath(S3Config.cachePath).getLastS3Files();
        }
        cacheData caches=new cacheData(taskDetails,null,null,s3files);
        cacheData.saveToCache(caches, S3Config.cachePath);
        for(int i=0;caches.getTaskDetails().size()>0;i++){
            TaskDetail taskDetail=caches.getTaskDetails().get(0);
            //System.out.println(taskDetail);
            runTask(taskDetail);
            caches.removeAheadTask();
            cacheData.saveToCache(caches,S3Config.cachePath);
        }
        System.out.println("taskExcutor执行完成");
    }

    public static void runfromCache(){
        System.out.println("正在从日志中读取任务");
        cacheData caches=cacheData.getCacheByPath(S3Config.cachePath);
        if(caches==null){
            return;
        }
        List<TaskDetail>taskDetails=caches.getTaskDetails();
        System.out.println("日志中记录的任务数为"+taskDetails.size());
        System.out.println("开始执行日志");
        //System.out.println(taskDetails);
        for(int i=0;i<taskDetails.size();i++){
            TaskDetail taskDetail=taskDetails.get(i);
            runTask(taskDetail);
            caches.removeAheadTask();
            cacheData.saveToCache(caches,S3Config.cachePath);
        }
        System.out.println("日志执行完成，进行下一项命令");
    }
}
