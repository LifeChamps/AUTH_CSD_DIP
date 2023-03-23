package org.auth.csd.datalab.Model.Locs

import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.JsonNode
import org.auth.csd.datalab.Model.LCEvent

class Alert extends LCEvent {

  var sensor: String = _
  var error: String = _
  var status: String = _
  var errorValDescr: String = _
  var value: Double = _

  override def setData(jsonNode: JsonNode): Unit = {
    val event: JsonNode = this.mapper.readTree(jsonNode.get("Alert").toString)
    this.pid = event.get("pID").asLong()
    this.pilotId = event.get("pilotId").asText()
    this.timestamp = this.formater_iso.format(this.formatter_ms.parse(event.get("datetime").asText()))
    sensor = event.get("sensor").asText()
    error = event.get("error").asText()
    status = event.get("status").asText()
    errorValDescr = event.get("errorValDescr").asText()
    value = event.get("value").asDouble()
  }

  override def toString: String = {
    s"""{\"patientId\":${this.pid},\"pilotId\":\"${this.pilotId}\",\"timestamp\":\"$timestamp\",\"datetime\":\"$datetime\",\"sensor\":\"$sensor\",\"error\":\"$error\",\"status\":\"$status\",\"errorValDescr\":\"$errorValDescr\",\"value\":$value}"""
  }

  override def getKey: String = {
    this.pid.toString + "_" + error + "_" + timestamp
  }

}
