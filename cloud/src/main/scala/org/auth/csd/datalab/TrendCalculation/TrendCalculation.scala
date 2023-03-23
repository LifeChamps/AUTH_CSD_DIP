package org.auth.csd.datalab.TrendCalculation

import org.apache.flink.api.scala.metrics.ScalaGauge
import org.apache.flink.configuration.Configuration
import org.apache.flink.metrics.Counter
import org.apache.flink.streaming.api.scala.function.ProcessWindowFunction
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector
import org.auth.csd.datalab.Model.Metrics.{Metric, SimpleMetric}

class TrendCalculation(new_key:String) extends ProcessWindowFunction[((Long, String), Metric), Metric, (Long, String), TimeWindow] {
  @transient private var cpu_time: Long = 0L
  @transient private var counter: Counter = _

  override def process(key: (Long, String), context: Context, elements: Iterable[((Long, String), Metric)], out: Collector[Metric]): Unit = {
    val lastTimestamp = context.window.getEnd
    counter.inc()
    //    val totalsize = elements.map(x=>VM.current().sizeOf(x._2)).sum
    //    println(s"${elements.size} - ${getRuntimeContext.getIndexOfThisSubtask} - $totalsize")
    val time_init = System.currentTimeMillis()
    elements.
      groupBy(_._1)
      .map(x => {
        val e = x._2.map(_._2.value)
        new SimpleMetric(x._1._1, new_key, e.sum / e.size.toDouble, lastTimestamp, time_init)
      })
      .foreach(out.collect)
    val time_final = System.currentTimeMillis()
    cpu_time += (time_final - time_init)

  }

  override def open(parameters: Configuration): Unit = {
    counter = getRuntimeContext
      .getMetricGroup
      .counter("MySlideCounter")

    getRuntimeContext
      .getMetricGroup
      .gauge[Long, ScalaGauge[Long]]("MyTotalTime", ScalaGauge[Long](() => cpu_time))

    getRuntimeContext
      .getMetricGroup
      .gauge[Double, ScalaGauge[Double]]("MyAverageTime", ScalaGauge[Double](() => {
        if (counter.getCount == 0) cpu_time.toDouble
        else cpu_time.toDouble / counter.getCount
      }))
  }
}
