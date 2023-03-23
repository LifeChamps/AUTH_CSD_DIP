package org.auth.csd.datalab

import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.auth.csd.datalab.Model.Pipelines.{Calculations, Pipeline, Raw, Transform}

import scala.io.Source


object Main_Module {

  def main(args: Array[String]): Unit = {

    val jsonString = Source.fromResource("pipelines.json")
    val json = ujson.read(jsonString.mkString)

    val topics: Array[Pipeline] = json("topics").arr.map(x => {
      val name: String = x("name").toString().substring(1, x("name").toString().length() - 1)
      val from: String = x("from").toString().substring(1, x("from").toString().length() - 1)
      val to: String = x("to").toString().substring(1, x("to").toString().length() - 1)
      val pipeType: String = x("type").toString().substring(1, x("type").toString().length() - 1)
      if (pipeType == "raw") {
        new Raw(name, from, to)
      } else if (pipeType == "transform") {
        val c: String = x("class").toString().substring(1, x("class").toString().length() - 1)
        new Transform(name, from, to, Class.forName(c))
      } else if (pipeType == "Calculations") {
        val c: String = x("class").toString().substring(1, x("class").toString().length() - 1)
        new Calculations(name, from, to, Class.forName(c), x("analytics").arr)
      } else {
        new Raw(name, from, to)
      }
    }).toArray

    val env = StreamExecutionEnvironment.getExecutionEnvironment

    topics.foreach(_.process(env))


    env.execute("DIRAP")
  }

}
