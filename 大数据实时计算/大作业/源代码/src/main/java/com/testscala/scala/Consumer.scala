package com.testscala.scala

import java.text.SimpleDateFormat
import java.util
import java.util.{Date, Properties}

import com.bingocloud.services.s3.AmazonS3Client
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.api.common.typeinfo.TypeInformation
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.node.ObjectNode
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks
import org.apache.flink.streaming.api.functions.source.SourceFunction
import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}
import org.apache.flink.streaming.api.watermark.Watermark
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.connectors.kafka.internals.KeyedSerializationSchemaWrapper
import org.apache.flink.streaming.connectors.kafka.{FlinkKafkaConsumer010, FlinkKafkaConsumer09, FlinkKafkaProducer09}
import org.apache.flink.streaming.util.serialization.JSONKeyValueDeserializationSchema

object Consumer {
  val accessKey = "17E8AFD1271D6CF443EA"
  val secretKey = "Wzc5NTVBQkEzRUE4MzlGM0RFNzQ4MkNCNjBDMDIy"
  val endpoint = "http://scut.depts.bingosoft.net:29997"
  val bucket = "chenzhuokun"
  //要读取的文件
  val key = "demo.txt"

  //kafka参数
  val topic = "data_flka_chenzhuokun"
  val bootstrapServers = "bigdata35.depts.bingosoft.net:29035,bigdata36.depts.bingosoft.net:29036,bigdata37.depts.bingosoft.net:29037"

  val user = "汤欣欣"
  val inputTopics: util.ArrayList[String] = new util.ArrayList[String]() {
    {
      add("mn_buy_ticket_1") //车票购买记录主题
      add("mn_hotel_stay_1") //酒店入住信息主题
      add("mn_monitoring_1") //监控系统数据主题
    }
  }

  def main(args: Array[String]): Unit = {

    val topic = "data_flka_chenzhuokun"
    val propss = new Properties
    propss.put("bootstrap.servers", bootstrapServers)
    propss.put("acks", "all")
    propss.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    propss.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)
    val kafkaConsumer = new FlinkKafkaConsumer010[ObjectNode](topic,
      new JSONKeyValueDeserializationSchema(true), propss)
    kafkaConsumer.setCommitOffsetsOnCheckpoints(true)
    val inputKafkaStream = env.addSource(kafkaConsumer)
    inputKafkaStream.filter(x => x.get("value").get("username").asText("").equals(user)).map(x => {
      (x.get("metadata").get("topic").asText("") match {
        case "mn_monitoring_1"
        => x.get("value").get("found_time")
        case _ => x.get("value").get("buy_time")
      }, x)
    }).print()
    env.execute()
  }
}
