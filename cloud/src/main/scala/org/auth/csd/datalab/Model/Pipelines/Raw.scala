package org.auth.csd.datalab.Model.Pipelines
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}

class Raw(val n: String, val f:String,val t:String) extends Pipeline {

  override val from: String = f
  override val to: String = t
  override val name: String = n

  override def process(env: StreamExecutionEnvironment): Unit = {
    val data:DataStream[String] = this.add_source(env,from)
    this.add_sink(to,data, properties)

  }


}
