package org.auth.csd.datalab.Model.Metrics

import org.auth.csd.datalab.Model.LCEvent

abstract class Metric extends LCEvent{
  var key:String
  var value:Double

  override def getKey: String = {
    this.pid.toString +"_"+this.key
  }

  def getKeyMetric:(Long,String)={
    (pid,key)
  }

}
