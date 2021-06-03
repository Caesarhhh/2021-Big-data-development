package com.bigdatapt.homework.homework1.utils;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.bigdatapt.homework.homework1.client.AmazonSingleton;
import com.bigdatapt.homework.homework1.config.S3Config;
import com.bigdatapt.homework.homework1.model.FileData;
import com.bigdatapt.homework.homework1.model.TaskDetail;
import com.bigdatapt.homework.homework1.model.cacheData;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Download {
    public static void DownloadByPath(String path){
        AmazonS3 s3=AmazonSingleton.getInstance().getS3();

        String filePath= S3Config.baseFilePath+path;
        final String keyName = path;

        System.out.format("Downloading %s from S3 bucket %s...\n", keyName, S3Config.bucketName);

        S3ObjectInputStream s3is = null;
        FileOutputStream fos = null;
        try {
            S3Object o = s3.getObject(S3Config.bucketName, keyName);
            s3is = o.getObjectContent();
            fos = new FileOutputStream(new File(filePath));
            byte[] read_buf = new byte[64 * 1024];
            int read_len = 0;
            while ((read_len = s3is.read(read_buf)) > 0) {
                fos.write(read_buf, 0, read_len);
            }
        } catch (AmazonServiceException e) {
            System.err.println(e.toString());
            System.exit(1);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        } finally {
            if (s3is != null) try { s3is.close(); } catch (IOException e) { }
            if (fos != null) try { fos.close(); } catch (IOException e) { }
        }
    }

    public static void DownloadBigData(String path,int currentPos){
        AmazonS3 s3=AmazonSingleton.getInstance().getS3();
        String keyName=path;
        System.out.format("Downloading *largefile* %s from S3 bucket %s...\n", keyName, S3Config.bucketName);
        final String filePath = S3Config.baseFilePath+path;
        //cacheData caches=cacheData.getCacheByPath(S3Config.cachePath);

        File file = new File(filePath);

        S3Object o = null;

        S3ObjectInputStream s3is = null;
        FileOutputStream fos = null;

        try {
            ObjectMetadata oMetaData = s3.getObjectMetadata(S3Config.bucketName, keyName);
            final long contentLength = oMetaData.getContentLength();
            final GetObjectRequest downloadRequest =
                    new GetObjectRequest(S3Config.bucketName, keyName);
            fos = new FileOutputStream(file);
            long filePosition = (S3Config.partSize-1)*(currentPos-1);
            long partSize=S3Config.partSize;
            long partNum=contentLength/partSize+1;
            for (int i = currentPos; filePosition < contentLength; i++) {
                partSize = Math.min(partSize, contentLength - filePosition);
                downloadRequest.setRange(filePosition, filePosition + partSize);
                o = s3.getObject(downloadRequest);
                System.out.format("Downloading part %d\\%d\n", i,partNum);
                filePosition += partSize+1;
                s3is = o.getObjectContent();
                byte[] read_buf = new byte[64 * 1024];
                int read_len = 0;
                while ((read_len = s3is.read(read_buf)) > 0) {
                    fos.write(read_buf, 0, read_len);
                }
                //caches.addAheadPos(1);
                //cacheData.saveToCache(caches,S3Config.cachePath);
            }
        } catch (Exception e) {
            System.err.println(e.toString());
            System.exit(1);
        } finally {
            if (s3is != null) try { s3is.close(); } catch (IOException e) { }
            if (fos != null) try { fos.close(); } catch (IOException e) { }
        }
    }

    public static void DownloadByList(List<FileData> lists){
        for(int i=0;i<lists.size();i++){
            if(!lists.get(i).isFile()){
                File tempfile=new File(S3Config.baseFilePath+lists.get(i).getName());
                if(!tempfile.exists()){
                    tempfile.mkdir();
                }
            }
        }
        for(int i=0;i<lists.size();i++){
            if(lists.get(i).isFile()){
                if(lists.get(i).getSize()>S3Config.threadSize){
                    DownloadBigData(lists.get(i).getName(),1);
                }
                else{
                    DownloadByPath(lists.get(i).getName());
                }
            }
        }
    }

    public static List<TaskDetail> aggDownloadByList(List<FileData>lists){
        List<TaskDetail>taskDetails=new ArrayList<>();
        for(int i=0;i<lists.size();i++){
            if(lists.get(i).isFile()){
                if(lists.get(i).getSize()>S3Config.threadSize){
                    taskDetails.add(new TaskDetail(0,1,lists.get(i).getName(),1));
                }
                else{
                    taskDetails.add(new TaskDetail(0,0,lists.get(i).getName(),0));
                }
            }
        }
        return taskDetails;
    }

    public static void main(String[] args) {
        DownloadBigData("BOOK.pdf",1);
    }
}
