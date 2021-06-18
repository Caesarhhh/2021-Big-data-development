package com.testscala.scala;

import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010;
import org.apache.flink.util.Collector;
import java.util.*;

public class Consumer_java {
    private static String accessKey = "17E8AFD1271D6CF443EA";
    private static String secretKey = "Wzc5NTVBQkEzRUE4MzlGM0RFNzQ4MkNCNjBDMDIy";
    private static String endpoint = "http://scut.depts.bingosoft.net:29997";
    private static String bucket = "chenzhuokun";
    private static String key = "demo.txt";
    private static String topic = "data_flka_chenzhuokun01";
    private static int period = 5000;
    private static String bootstrapServers = "bigdata35.depts.bingosoft.net:29035,bigdata36.depts.bingosoft.net:29036,bigdata37.depts.bingosoft.net:29037";
    private static S3Writer s3Writer = new S3Writer(accessKey, secretKey, endpoint, bucket, "storage/", 5000);
    private static String[] filtersHotel = {"白云宾馆", "东方宾馆", "珠岛宾馆", "华泰宾馆", "越秀宾馆", "君达华海宾馆"};
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", bootstrapServers);
        properties.setProperty("acks", "all");
        properties.setProperty("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.setProperty("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.setProperty("group.id", "flink-group");
        FlinkKafkaConsumer010<String> consumer = new FlinkKafkaConsumer010<String>(topic, new SimpleStringSchema(), properties);
        consumer.setCommitOffsetsOnCheckpoints(true);
        DataStream<String> stream = env.addSource(consumer);
        List<DataStream<String>> streams = new ArrayList<>();
        for (String hotel : filtersHotel) {
            DataStream<String> tempStream = stream
                    .filter(new FilterFunction<String>() {
                        @Override
                        public boolean filter(String s) throws Exception {
                            String cityt = s.split(",")[2];
                            String city = cityt.substring(1, cityt.length() - 1);
                            return city.equals(hotel);
                        }
                    })
                    .flatMap((String line, Collector<String> collector) -> {
                        System.out.println("get data with hotel " + hotel + ": " + line);
                        collector.collect(line);
                    })
                    .returns(Types.STRING);
            tempStream.writeUsingOutputFormat(new S3Writer(accessKey, secretKey, endpoint, bucket, "storage/" + hotel + "/", period));
            streams.add(tempStream);
        }
        DataStream<String> tempStream = stream
                .filter(new FilterFunction<String>() {
                    @Override
                    public boolean filter(String s) throws Exception {
                        String cityt = s.split(",")[2];
                        String city = cityt.substring(1, cityt.length() - 1);
                        return Arrays.asList(filtersHotel).indexOf(city) == -1;
                    }
                })
                .flatMap((String line, Collector<String> collector) -> {
                    System.out.println("get data with other hotel : " + line);
                    collector.collect(line);
                })
                .returns(Types.STRING);
        tempStream.writeUsingOutputFormat(new S3Writer(accessKey, secretKey, endpoint, bucket, "storage/others/", period));
        env.execute("kafka streaming word count");
    }
}
