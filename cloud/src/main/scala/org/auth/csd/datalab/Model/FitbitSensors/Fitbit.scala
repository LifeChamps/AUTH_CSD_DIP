package org.auth.csd.datalab.Model.FitbitSensors

import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.JsonNode
import org.auth.csd.datalab.Model.LCEvent

class Fitbit extends LCEvent{
  var jsonNode:JsonNode=_

  override def setData(jsonNode: JsonNode): Unit = {
    this.jsonNode=jsonNode
  }

  override def toString: String = {
    jsonNode.toString
  }
}
