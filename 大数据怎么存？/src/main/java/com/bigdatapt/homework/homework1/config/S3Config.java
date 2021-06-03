package com.bigdatapt.homework.homework1.config;

import java.util.List;

public class S3Config {
    public final static String bucketName = "chenzhuokun";
    public final static String baseFilePath   ="D:\\images\\";
    public final static String accessKey = "17E8AFD1271D6CF443EA";
    public final static String secretKey ="Wzc5NTVBQkEzRUE4MzlGM0RFNzQ4MkNCNjBDMDIy";
    public final static String serviceEndpoint ="http://scut.depts.bingosoft.net:29997";
    public final static String signingRegion = "";
    public final static long partSize=5<<20;
    public final static long threadSize=20<<20;
    public final static long RefreshInternal_Second=20;
    public final static String cachePath="D:\\images\\cacheData.dat";
    public final static String []filtersFile=new String[]{"cacheData.dat"};
    public final static long Overdue_Second=600;
}
