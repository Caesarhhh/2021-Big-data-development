package com.bigdatapt.homework.homework1.utils;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.bigdatapt.homework.homework1.client.AmazonSingleton;
import com.bigdatapt.homework.homework1.config.S3Config;
import com.bigdatapt.homework.homework1.model.FileData;
import com.bigdatapt.homework.homework1.model.TaskDetail;

import java.util.ArrayList;
import java.util.List;

public class Delete {
    public static void DeleteS3ByPath(String path){
        System.out.println("Delete file "+path+" ...");
        AmazonS3 s3= AmazonSingleton.getInstance().getS3();
        try{
            s3.deleteObject(S3Config.bucketName,path);
        }catch (AmazonServiceException e) {
        }
    }

    public static List<TaskDetail> aggDeleteByList(List<FileData>lists){
        List<TaskDetail>taskDetails=new ArrayList<>();
        for(int i=0;i<lists.size();i++){
            if(lists.get(i).isFile()){
                taskDetails.add(new TaskDetail(2,0,lists.get(i).getName(),0));
            }
        }
        return taskDetails;
    }
}
