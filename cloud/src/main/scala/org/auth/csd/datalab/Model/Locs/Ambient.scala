package org.auth.csd.datalab.Model.Locs

import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.JsonNode
import org.auth.csd.datalab.Model.LCEvent

class Ambient extends LCEvent {

  var humidity: Long = _
  var temperature: Long = _
  var location: String = _

  override def setData(jsonNode: JsonNode): Unit = {
    this.pid = jsonNode.get("pID").asLong()
    this.pilotId = jsonNode.get("pilotId").asText()
    this.timestamp = jsonNode.get("dateTime").asText()
    humidity = jsonNode.get("humidity").asLong()
    temperature = jsonNode.get("temperature").asLong()
    location = jsonNode.get("location").asText()
  }

  override def toString: String = {
    s"""{\"patientId\":$pid,\"pilotId\":\"${this.pilotId}\",\"timestamp\":\"$timestamp\",\"datetime\":\"$datetime\",\"location\":\"$location\",\"temperature\":$temperature,\"humidity\":$humidity}"""
  }

  override def getKey: String = {
    this.pid.toString + "_" + temperature + "_" + timestamp
  }
}
