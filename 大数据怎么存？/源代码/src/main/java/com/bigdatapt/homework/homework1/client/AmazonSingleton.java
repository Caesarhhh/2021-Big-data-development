package com.bigdatapt.homework.homework1.client;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.bigdatapt.homework.homework1.config.S3Config;

public class AmazonSingleton {
    private static AmazonSingleton instance=null;
    private AmazonS3 s3=null;
    private AmazonSingleton(){
        final BasicAWSCredentials credentials = new BasicAWSCredentials(S3Config.accessKey, S3Config.secretKey);
        final ClientConfiguration ccfg = new ClientConfiguration().
                withUseExpectContinue(false);

        final AwsClientBuilder.EndpointConfiguration endpoint = new AwsClientBuilder.EndpointConfiguration(S3Config.serviceEndpoint, S3Config.signingRegion);

        s3 = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withClientConfiguration(ccfg)
                .withEndpointConfiguration(endpoint)
                .withPathStyleAccessEnabled(true)
                .build();
    }
    public static synchronized AmazonSingleton getInstance(){
        if(instance==null){
            instance=new AmazonSingleton();
        }
        return instance;
    }
    public AmazonS3 getS3(){
        return s3;
    }
}
