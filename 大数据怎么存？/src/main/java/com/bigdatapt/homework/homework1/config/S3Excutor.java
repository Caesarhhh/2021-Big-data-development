package com.bigdatapt.homework.homework1.config;

import com.amazonaws.services.dynamodbv2.xspec.S;
import com.amazonaws.services.ecs.model.Task;
import com.amazonaws.services.s3.AmazonS3;
import com.bigdatapt.homework.homework1.client.AmazonSingleton;
import com.bigdatapt.homework.homework1.model.FileData;
import com.bigdatapt.homework.homework1.model.TaskDetail;
import com.bigdatapt.homework.homework1.model.cacheData;
import com.bigdatapt.homework.homework1.utils.*;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.PostConstruct;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

public class S3Excutor {

    private static AmazonS3 s3= AmazonSingleton.getInstance().getS3();

    private static List<FileData>lastStatefiles=new ArrayList<>();

    List<FileData> getUploadList(List<FileData>s3files,List<FileData>localfiles){
        HashSet<FileData> temp=new HashSet();
        temp.addAll(localfiles);
        temp.removeAll(s3files);
        List<FileData> resultlist=new ArrayList<>(temp);
        List<FileData> templist=resultlist;
        for(int i=0;i<templist.size();i++){
            FileData tempFile=templist.get(i);
            int tempIndex=Query.indexofFile(tempFile.getName(),tempFile.isFile(),s3files);
            if(tempIndex!=-1){
                if(s3files.get(tempIndex).getTime()>tempFile.getTime()){
                    resultlist.remove(templist.get(i));
                }
            }
        }
        return resultlist;
    }

    List<FileData> getDownloadList(List<FileData>s3files,List<FileData>localfiles){
        HashSet<FileData> temp=new HashSet();
        temp.addAll(s3files);
        temp.removeAll(localfiles);
        List<FileData> resultlist=new ArrayList<>(temp);
        List<FileData> templist=resultlist;
        for(int i=0;i<templist.size();i++){
            FileData tempFile=templist.get(i);
            int tempIndex=Query.indexofFile(tempFile.getName(),tempFile.isFile(),localfiles);
            if(tempIndex!=-1){
                if(localfiles.get(tempIndex).getTime()>tempFile.getTime()){
                    resultlist.remove(templist.get(i));
                }
            }
        }
        return resultlist;
    }

    List<FileData> getDeleteList(List<FileData>historyfiles,List<FileData>localfiles){
        HashSet<FileData> temp=new HashSet();
        temp.addAll(historyfiles);
        temp.removeAll(localfiles);
        List<FileData> resultlist=new ArrayList<>(temp);
        List<FileData> templist=resultlist;
        for(int i=0;i<templist.size();i++){
            FileData tempFile=templist.get(i);
            int tempIndex=Query.indexofFile(tempFile.getName(),tempFile.isFile(),localfiles);
            if(tempIndex!=-1){
                resultlist.remove(templist.get(i));
            }
        }
        return resultlist;
    }

    @PostConstruct
    public void init(){
        TaskExcutor.runfromCache();
        System.out.println("-------初始化开始-------");
        List<FileData> s3files= Query.getS3Files();
        List<FileData> localfiles=Query.getLocalFilesByPath("");
        List<FileData> uploadList=getUploadList(s3files,localfiles);
        List<FileData> downloadList=getDownloadList(s3files,localfiles);
        cacheData caches=cacheData.getCacheByPath(S3Config.cachePath);
        if(caches!=null&&caches.getLastS3Files()!=null){
            downloadList=getDownloadList(s3files,caches.getLastS3Files());
        }
        List<TaskDetail>taskDetails=new ArrayList<>();
        taskDetails.addAll(Download.aggDownloadByList(downloadList));
        taskDetails.addAll(Upload.aggUploadByList(uploadList));
        TaskExcutor.runList(taskDetails);
        caches=new cacheData(taskDetails,null,null,Query.getS3Files());
        cacheData.saveToCache(caches,S3Config.cachePath);
        lastStatefiles=Query.getLocalFilesByPath("");
        System.out.println("-------初始化结束--------");
    }

    public S3Excutor(){
        init();
    }

    public void ScheduleJob(){
        System.out.println("-------刷新开始------");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(df.format(new Date()));
        List<FileData> localfiles=Query.getLocalFilesByPath("");
        List<FileData> uploadList=getUploadList(lastStatefiles,localfiles);
        List<FileData> deleteList=getDeleteList(lastStatefiles,localfiles);
//        System.out.println("上次状态的文件是:");
//        System.out.println(lastStatefiles);
//        System.out.println("现在的文件是:");
//        System.out.println(localfiles);
        System.out.println("本次需要上传的文件是:");
        System.out.println(uploadList);
        System.out.println("本次需要删除的文件是:");
        System.out.println(deleteList);
        List<TaskDetail>taskDetails=Upload.aggUploadByList(uploadList);
        taskDetails.addAll(Delete.aggDeleteByList(deleteList));
        List<FileData>s3files=null;
        if(cacheData.getCacheByPath(S3Config.cachePath)!=null){
            s3files=cacheData.getCacheByPath(S3Config.cachePath).getLastS3Files();
        }
        cacheData caches=new cacheData(taskDetails,null,null,s3files);
        cacheData.saveToCache(caches,S3Config.cachePath);
        TaskExcutor.runList(taskDetails);
        lastStatefiles=localfiles;
        System.out.println("-------刷新结束------");
    }

    public static void main(String[] args) {
        S3Excutor s3Excutor=new S3Excutor();
        TimerTask timerTask=new TimerTask() {
            @Override
            public void run() {
                s3Excutor.ScheduleJob();
            }
        };
        Timer timer=new Timer();
        timer.schedule(timerTask,1000,S3Config.RefreshInternal_Second*1000);
    }
}
