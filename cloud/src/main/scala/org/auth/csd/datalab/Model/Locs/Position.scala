package org.auth.csd.datalab.Model.Locs

import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.JsonNode
import org.auth.csd.datalab.Model.LCEvent

import java.time.{LocalDate, LocalDateTime}
import java.util.Calendar

class Position extends LCEvent {
  var event: JsonNode = _
  var position: JsonNode = _
  var location: String = _
  var dateEntered: String = _
  var dateUpdate: String = _
  var totalActivations: Int = _
  var recentActivations: Int = _

  override def setData(jsonNode: JsonNode): Unit = {
    event = this.mapper.readTree(jsonNode.get("Location").toString)
    //    .asInstanceOf[ArrayNode].get(0).get("Location")
    this.pid = event.get("pID").asLong()
    this.pilotId = event.get("pilotId").asText()
    position = event.get("position")
    location = position.get("location").asText()
    dateEntered = this.formater_iso.format(this.formatter_ms.parse(position.get("dateEntered").asText()))
    dateUpdate= this.formater_iso.format(this.formatter_ms.parse(position.get("dateUpdate").asText()))
    this.timestamp = this.dateUpdate
    totalActivations= position.get("totalActivations").asInt()
    recentActivations = position.get("recentActivations").asInt()
  }


  override def getKey(): String = {
    this.pid.toString + "_" + location + "_" + timestamp
  }

  override def toString: String = {
    s"""{\"patientId\":${this.pid},\"pilotId\":\"${this.pilotId}\",\"timestamp\":\"$timestamp\",\"location\":\"$location\",\"dateEntered\":\"$dateEntered\",\"dateUpdate\":\"$dateEntered\",\"datetime\":\"$datetime\",\"totalActivations\":$totalActivations,\"recentActivations\":$recentActivations}"""


  }


}