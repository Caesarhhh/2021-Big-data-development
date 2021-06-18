package com.bigdata.week3.job3.Kafka

import java.util.Properties

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

object Producer {
  val accessKey = "17E8AFD1271D6CF443EA"
  val secretKey = "Wzc5NTVBQkEzRUE4MzlGM0RFNzQ4MkNCNjBDMDIy"
  val endpoint = "http://scut.depts.bingosoft.net:29997"
  val bucket = "chenzhuokun"
  //要读取的文件
  val key = "mn_hotel_stay.csv"

  //kafka参数
  val topic = "data_flka_chenzhuokun_job3"
  val bootstrapServers = "bigdata35.depts.bingosoft.net:29035,bigdata36.depts.bingosoft.net:29036,bigdata37.depts.bingosoft.net:29037"

  def main(args: Array[String]): Unit = {
    val s3Content = "test"
    produceToKafka(s3Content)
  }

  def produceToKafka(s3Content: String): Unit = {
    val props = new Properties
    props.put("bootstrap.servers", bootstrapServers)
    props.put("acks", "all")
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    val producer = new KafkaProducer[String, String](props)
    val dataArr = s3Content.split("\n")
    println("开始传输")
    for (s <- dataArr) {
      if (!s.trim.isEmpty) {
        val record = new ProducerRecord[String, String](topic, null, s)
        println("开始生产数据：" + s)
        producer.send(record)
      }
    }
    producer.flush()
    producer.close()
  }
}
