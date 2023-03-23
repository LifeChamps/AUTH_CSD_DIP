package org.auth.csd.datalab.Model.Locs

import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.JsonNode
import org.auth.csd.datalab.Model.LCEvent

class Appliances extends LCEvent {

  var appliance: String = _
  var action: String = _

  override def setData(jsonNode: JsonNode): Unit = {
    this.pid = jsonNode.get("pID").asLong()
    this.pilotId = jsonNode.get("pilotId").asText()
    this.timestamp = jsonNode.get("dateTime").asText()
    appliance = jsonNode.get("appliance").asText()
    action = jsonNode.get("action").asText()
  }

  override def toString: String = {
    s"""{\"patientId\":$pid,\"pilotId\":\"${this.pilotId}\",\"timestamp\":\"$timestamp\",\"datetime\":\"$datetime\",\"appliance\":\"$appliance\",\"action\":\"$action\"}"""
  }

  override def getKey: String = {
    this.pid.toString + "_" + appliance + "_" + timestamp
  }
}
