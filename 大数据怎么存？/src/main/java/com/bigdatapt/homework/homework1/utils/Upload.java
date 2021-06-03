package com.bigdatapt.homework.homework1.utils;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.bigdatapt.homework.homework1.model.TaskDetail;
import com.bigdatapt.homework.homework1.model.cacheData;
import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.model.*;
import com.bigdatapt.homework.homework1.client.AmazonSingleton;
import com.bigdatapt.homework.homework1.config.S3Config;
import com.bigdatapt.homework.homework1.model.FileData;

public class Upload {

    public static void UploadByPath(String path) {
        AmazonS3 s3 = AmazonSingleton.getInstance().getS3();

        String filePath = S3Config.baseFilePath + path;
        System.out.format("Uploading %s to S3 bucket %s...\n", filePath, S3Config.bucketName);
        String keyName = path;
        final File file = new File(filePath);

        for (int i = 0; i < 2; i++) {
            try {
                s3.putObject(S3Config.bucketName, keyName, file);
                break;
            } catch (AmazonServiceException e) {
                if (e.getErrorCode().equalsIgnoreCase("NoSuchBucket")) {
                    s3.createBucket(S3Config.bucketName);
                    continue;
                }

                System.err.println(e.toString());
                System.exit(1);
            } catch (AmazonClientException e) {
                try {
                    // detect bucket whether exists
                    s3.getBucketAcl(S3Config.bucketName);
                } catch (AmazonServiceException ase) {
                    if (ase.getErrorCode().equalsIgnoreCase("NoSuchBucket")) {
                        s3.createBucket(S3Config.bucketName);
                        continue;
                    }
                } catch (Exception ignore) {
                }

                System.err.println(e.toString());
                System.exit(1);
            }
        }
    }

    public static void UploadBigData(String path, int currentPos) {
        AmazonS3 s3 = AmazonSingleton.getInstance().getS3();
        String keyName = path;
        cacheData caches = cacheData.getCacheByPath(S3Config.cachePath);
        ArrayList<PartETag> partETags = new ArrayList<PartETag>();
        if (currentPos > 1 && caches.getPartETags() != null) {
            partETags = caches.getPartETags();
        }
        File file = new File(S3Config.baseFilePath + path);
        long contentLength = file.length();
        String uploadId = null;

        try {
            InitiateMultipartUploadResult initiateMultipartUploadResult;
            if (currentPos > 1 && caches != null && caches.getMultipartUploadResult() != null) {
                initiateMultipartUploadResult = caches.getMultipartUploadResult();
            } else {
                InitiateMultipartUploadRequest initRequest = new InitiateMultipartUploadRequest(S3Config.bucketName, keyName);
                initiateMultipartUploadResult=s3.initiateMultipartUpload(initRequest);
            }
            uploadId = initiateMultipartUploadResult.getUploadId();
            System.out.format("upload ID was %s\n", uploadId);
            long filePosition = S3Config.partSize * (currentPos - 1);
            long partSize = S3Config.partSize;
            long partNum = contentLength / partSize + 1;
            for (int i = currentPos; filePosition < contentLength; i++) {
                partSize = Math.min(partSize, contentLength - filePosition);
                UploadPartRequest uploadRequest = new UploadPartRequest()
                        .withBucketName(S3Config.bucketName)
                        .withKey(keyName)
                        .withUploadId(uploadId)
                        .withPartNumber(i)
                        .withFileOffset(filePosition)
                        .withFile(file)
                        .withPartSize(partSize);
                System.out.format("Uploading part %d\\%d\n", i, partNum);
                partETags.add(s3.uploadPart(uploadRequest).getPartETag());
                filePosition += partSize;
                caches.setPartETags(partETags);
                List<TaskDetail> taskDetails = caches.getTaskDetails();
                caches.addAheadPos(1);
                caches.setMultipartUploadResult(initiateMultipartUploadResult);
                cacheData.saveToCache(caches, S3Config.cachePath);
            }
            CompleteMultipartUploadRequest compRequest =
                    new CompleteMultipartUploadRequest(S3Config.bucketName, keyName, uploadId, partETags);

            s3.completeMultipartUpload(compRequest);
        } catch (Exception e) {
            System.err.println(e.toString());
            if (uploadId != null && !uploadId.isEmpty()) {
                s3.abortMultipartUpload(new AbortMultipartUploadRequest(S3Config.bucketName, keyName, uploadId));
            }
            System.exit(1);
        }

    }

    public static void UploadByList(List<FileData> lists) {
        for (int i = 0; i < lists.size(); i++) {
            if (lists.get(i).isFile()) {
                if (lists.get(i).getSize() > S3Config.threadSize) {
                    UploadBigData(lists.get(i).getName(), 1);
                } else {
                    UploadByPath(lists.get(i).getName());
                }
            }
        }
    }

    public static List<TaskDetail> aggUploadByList(List<FileData> lists) {
        List<TaskDetail> taskDetails = new ArrayList<>();
        for (int i = 0; i < lists.size(); i++) {
            if (lists.get(i).isFile()) {
                if (lists.get(i).getSize() > S3Config.threadSize) {
                    taskDetails.add(new TaskDetail(1, 1, lists.get(i).getName(), 1));
                } else {
                    taskDetails.add(new TaskDetail(1, 0, lists.get(i).getName(), 0));
                }
            }
        }
        return taskDetails;
    }

    public static void main(String[] args) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(1);
        arrayList.add(2);
        arrayList.add(3);
        arrayList.add(4);
        arrayList.add(5);
        System.out.println(arrayList);
        arrayList.remove(0);
        System.out.println(arrayList);
    }
}
