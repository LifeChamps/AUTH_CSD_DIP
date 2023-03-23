package org.auth.csd.datalab.Model.Pipelines
import org.apache.flink.api.scala.createTypeInformation
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.api.windowing.assigners.SlidingProcessingTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time
import org.auth.csd.datalab.Model.LCEvent
import org.auth.csd.datalab.Model.Metrics.Metric
import org.auth.csd.datalab.TrendCalculation.TrendCalculation
import ujson.Value

import scala.collection.mutable.ArrayBuffer

class Calculations(n:String,f:String,t:String,c:Class[_],analytics: ArrayBuffer[Value]) extends Transform(n,f,t,c) {
  protected val calculations: ArrayBuffer[Value] = analytics
  override def process(env: StreamExecutionEnvironment): Unit = {
    val data:DataStream[String] = this.add_source(env,from)
    val dataTransformed:DataStream[Metric]=data
      .map(row=>{
        val mapper = new ObjectMapper
        val n:Metric = c.getDeclaredConstructor().newInstance().asInstanceOf[Metric]
        n.setData(mapper.readTree(row))
        n
      })

    for(i <- calculations.indices){
      val ctype = calculations(i)("type").str
      if(ctype=="TrendCalculation"){
        this.createTrendCalculations(dataTransformed,calculations(i))
      }
    }
  }

  private def createTrendCalculations(data:DataStream[Metric],v:Value):Unit={
    val inputKeys:List[String]=v("inputkey").arr.map(_.toString()).toList
    val outputKeys:List[String]=v("outputkey").arr.map(_.toString()).toList
    if(inputKeys.size!=outputKeys.size){
      System.exit(1)
    }
    val hoursWindow:Int = v("window_h").num.toInt
    val hoursSlide:Int = v("slide_h").num.toInt
    val window: SlidingProcessingTimeWindows = SlidingProcessingTimeWindows.of(Time.hours(hoursWindow), Time.hours(hoursSlide))

    for((a,b)<- inputKeys zip outputKeys){
      val dataTransformed = data
        .filter(_.getKey == a)
        .map(x=>((x.pid,x.getKey),x))
        .keyBy(_._1)
        .window(window)
        .process(new TrendCalculation(b))
        .map(_.toString)
      this.add_sink(to,dataTransformed, properties)
    }
  }

}
