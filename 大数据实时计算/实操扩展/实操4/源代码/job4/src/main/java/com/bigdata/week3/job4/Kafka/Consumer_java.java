package com.bigdata.week3.job4.Kafka;

import com.bigdata.week3.job4.Model.BuyInfo;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010;
import org.apache.flink.util.Collector;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

@SpringBootTest
public class Consumer_java {

    MySqlWriter mySqlWriter=new MySqlWriter();

    private static String accessKey = "17E8AFD1271D6CF443EA";
    private static String secretKey = "Wzc5NTVBQkEzRUE4MzlGM0RFNzQ4MkNCNjBDMDIy";
    private static String endpoint = "http://scut.depts.bingosoft.net:29997";
    private static String bucket = "chenzhuokun";
    private static String key = "demo.txt";
    private static String topic = "data_flka_chenzhuokun_job3";
    private static int period = 5000;
    private static String bootstrapServers = "bigdata35.depts.bingosoft.net:29035,bigdata36.depts.bingosoft.net:29036,bigdata37.depts.bingosoft.net:29037";
    private static String[] filtersDestination = {"德阳市", "湛江市", "佛山市", "乌鲁木齐市", "沈阳市", "北京市"};

    @Test
    public  void run() throws Exception {
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
        for (String city : filtersDestination) {
            DataStream<String> tempStream = stream
                    .filter(new FilterFunction<String>() {
                        @Override
                        public boolean filter(String s) throws Exception {
                            return BuyInfo.getDesfronString(s).equals(city);
                        }
                    })
                    .flatMap((String line, Collector<String> collector) -> {
                        //System.out.println("get data with desitination " + city + ": " + line);
                        collector.collect(BuyInfo.getDesfronString(line)+"@"+line);
                    })
                    .returns(Types.STRING);
            tempStream.writeUsingOutputFormat(mySqlWriter);
            streams.add(tempStream);
        }
        DataStream<String> tempStream = stream
                .filter(new FilterFunction<String>() {
                    @Override
                    public boolean filter(String s) throws Exception {
                        return Arrays.asList(filtersDestination).indexOf(BuyInfo.getDesfronString(s)) == -1;
                    }
                })
                .flatMap((String line, Collector<String> collector) -> {
                    //System.out.println("get data with other destination : " + line);
                    collector.collect(BuyInfo.getDesfronString(line)+"@"+line);
                })
                .returns(Types.STRING);
        tempStream.writeUsingOutputFormat(mySqlWriter);
        env.execute("kafka streaming word count");
    }
}
