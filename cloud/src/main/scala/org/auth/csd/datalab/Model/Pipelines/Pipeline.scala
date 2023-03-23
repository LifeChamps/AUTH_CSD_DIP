package org.auth.csd.datalab.Model.Pipelines

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.api.scala.createTypeInformation
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer
import org.auth.csd.datalab.Utils.Helpers.readEnvVariable
import org.auth.csd.datalab.Utils.{MQTTSource, MQTTSourceConfig}
import java.util.Properties

abstract class Pipeline() {

  val from: String
  val to: String
  val name: String
  protected val qosArgs = 1
  protected val clientIdPrefixArg = "lifechamps"
  protected val properties:Properties = new Properties()
  properties.setProperty("bootstrap.servers", readEnvVariable("KAFKA_BROKERS"))


  def process(env: StreamExecutionEnvironment): Unit = {}

  def add_source(env: StreamExecutionEnvironment, mqtt_topic: String): DataStream[String] = {
    val mqtt_config = new MQTTSourceConfig[String](List[String](),
      this.clientIdPrefixArg,
      mqtt_topic,
      this.qosArgs,
      new SimpleStringSchema)
    env.addSource(new MQTTSource(mqtt_config, 1L))
      .map(x => (x, x)).keyBy(_._1)
      .window(TumblingProcessingTimeWindows.of(Time.seconds(1)))
      .reduce((x, y) => { //remove duplicates that come in 1 second window
        (x._1, x._2)
      })
      .map(_._2)
  }

  def add_sink(kafka_topic:String,data: DataStream[String],properties:Properties):Unit = {
    val kafka_sink = new FlinkKafkaProducer[String](kafka_topic,new SimpleStringSchema(),properties)
    data.addSink(kafka_sink)
  }

  override def toString: String = {
     s"""{\"name\":$name,\"from\":$from,\"to\":$to}"""
  }
}
