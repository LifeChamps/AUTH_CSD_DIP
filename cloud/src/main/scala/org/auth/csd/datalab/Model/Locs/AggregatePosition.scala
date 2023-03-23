package org.auth.csd.datalab.Model.Locs

import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.JsonNode
import org.auth.csd.datalab.Model.LCEvent

class AggregatePosition extends LCEvent {

  var positions: JsonNode = _
  var day: String = _

  override def setData(jsonNode: JsonNode): Unit = {
    positions = jsonNode.get("positions")
    this.pid = jsonNode.get("pID").asLong()
    this.pilotId = jsonNode.get("pilotId").asText()
    day = jsonNode.get("day").asText()
    this.timestamp = this.formater_iso.format(this.formatter_day.parse(day))
  }

  override def toString: String = {
    s"""{\"patientId\":$pid,\"pilotId\":\"${this.pilotId}\",\"timestamp\":\"$timestamp\",\"datetime\":\"$datetime\",\"day\":\"$day\",\"positions\":$positions}"""
  }

  override def getKey: String = {
    this.pid.toString+"_"+timestamp
  }


}
