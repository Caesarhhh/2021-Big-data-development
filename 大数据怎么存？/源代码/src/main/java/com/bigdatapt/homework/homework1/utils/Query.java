package com.bigdatapt.homework.homework1.utils;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.bigdatapt.homework.homework1.client.AmazonSingleton;
import com.bigdatapt.homework.homework1.config.S3Config;
import com.bigdatapt.homework.homework1.model.FileData;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Query {

    public static int indexofFile(String name,boolean ifFile,List<FileData>list){
        for(int i=0;i<list.size();i++){
            FileData temp=list.get(i);
            if(temp.getName().equals(name)&&ifFile==temp.isFile()){
                return i;
            }
        }
        return -1;
    }

    public static List<FileData> FilterList(List<FileData>fileDatas){
        List<String>filterNames=new ArrayList(Arrays.asList(S3Config.filtersFile));
        List<FileData>result=new ArrayList<>();
        for(FileData fileData:fileDatas){
            if(filterNames.indexOf(fileData.getName())==-1){
                result.add(fileData);
            }
        }
        return result;
    }

    public static List<FileData> getS3Files(){
        AmazonS3 s3= AmazonSingleton.getInstance().getS3();

        List<FileData> results=new ArrayList<>();
        List<S3ObjectSummary> summaries=s3.listObjects("chenzhuokun").getObjectSummaries();
        for(int i=0;i<summaries.size();i++){
            boolean ifFile=true;
            String name=summaries.get(i).getKey();
            if(name.charAt(name.length()-1)=='/'){
                ifFile=false;
                name=name.substring(0,name.length()-1);
            }
            results.add(new FileData(name,summaries.get(i).getLastModified().getTime(),ifFile,summaries.get(i).getSize()));
        }
        return FilterList(results);
    }

    public static List<FileData> getLocalFilesByPath(String path){
        ArrayList<FileData> files = new ArrayList<FileData>();
        File file = new File(S3Config.baseFilePath+path);
        File[] tempList = file.listFiles();

        for (int i = 0; i < tempList.length; i++) {
            if (tempList[i].isFile()) {
                files.add(new FileData(path+tempList[i].toString().substring(S3Config.baseFilePath.length()+path.length()),tempList[i].lastModified(),true,tempList[i].length()));
            }
            if (tempList[i].isDirectory()) {
                files.add(new FileData(path+tempList[i].toString().substring(S3Config.baseFilePath.length()+path.length()),tempList[i].lastModified(),false,0));
                files.addAll(getLocalFilesByPath(path+tempList[i].getName()+"/"));
            }
        }
        return FilterList(files);
    }

    public static void main(String[] args) {
        System.out.println("S3Files: "+getS3Files());
        System.out.println("LocalFiles: "+getLocalFilesByPath(""));
    }
}
