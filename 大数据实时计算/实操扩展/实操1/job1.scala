package com.testscala.scala.实操

import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time

import scala.util.control.Breaks._
import scala.collection.mutable.ArrayBuffer


object job1 {
  val target = "b"
  val timeSeconds = 60
  var index = 0
  var times = new ArrayBuffer[Long]()
  val words = new ArrayBuffer[String]()

  case class WordWithCount(word: String, count: Long)

  def main(args: Array[String]) {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    //Linux or Mac:nc -l 9999
    //Windows:nc -l -p 9999
    val text = env.socketTextStream("localhost", 9999)
    val windowCounts = text.flatMap {
      _.toLowerCase.split("\\W+") filter {
        _.contains(target)
      }
    }
      .map( word => WordWithCount("最近"+timeSeconds+"秒出现含有"+target+"的单词个数", 1))
      .timeWindowAll(Time.seconds(timeSeconds), Time.seconds(5))
      .sum("count")

    windowCounts.print.setParallelism(1)
    env.execute("Window Stream WordCount")
  }
}
