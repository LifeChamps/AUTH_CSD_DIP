package org.auth.csd.datalab.Model.Pipelines

import org.apache.flink.api.scala.createTypeInformation
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.auth.csd.datalab.Model.LCEvent

class Transform(n:String,f:String,t:String,c:Class[_]) extends Pipeline with Serializable {
  override val from: String = f
  override val to: String = t
  override val name: String = n
  protected val lcClass: Class[_]=c

  override def process(env: StreamExecutionEnvironment): Unit = {
    val data:DataStream[String] = this.add_source(env,from)
    this.add_sink(to,transformData(data), properties)
  }

  protected def transformData(data:DataStream[String]):DataStream[String]={
    data
      .map(row=>{
        val mapper = new ObjectMapper
        val n:LCEvent = c.getDeclaredConstructor().newInstance().asInstanceOf[LCEvent]
        n.setData(mapper.readTree(row))
        n
      })
      .map(_.toString)
  }
}
