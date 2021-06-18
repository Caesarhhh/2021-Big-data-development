package com.testscala.scala.实操

import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.scala._

import scala.util.control.Breaks._
import scala.collection.mutable.ArrayBuffer


object job1 {
  val target = "b"
  val timeSeconds = 20
  var index = 0
  var times = new ArrayBuffer[Long]()
  val words = new ArrayBuffer[String]()

  def refreshIndex(word:String): String ={
    times+=System.currentTimeMillis()
    words+=word
    breakable(
      for(i<-index to times.length-1){
        if(timeSeconds*1000+times(index)>System.currentTimeMillis()){
          break()
        }
        index=i
      }
    )
    var res="最近"+timeSeconds+"秒钟"+"出现的带有"+target+"的单词个数为"+(times.length-index).toString()+"\n"
    for (i<-index to times.length-1){
      res+=words(i)+" "
    }
    res
  }

  def main(args: Array[String]) {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    //Linux or Mac:nc -l 9999
    //Windows:nc -l -p 9999
    val text = env.socketTextStream("localhost", 9999)
    val stream = text.flatMap {
      _.toLowerCase.split("\\W+") filter {
        _.contains(target)
      }
    }.map {

      //words+=_
      (refreshIndex(_))
    }
    stream.print()
    env.execute("Window Stream WordCount")
  }
}
