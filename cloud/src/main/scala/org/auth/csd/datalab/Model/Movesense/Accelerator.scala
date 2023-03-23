package org.auth.csd.datalab.Model.Movesense

import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.JsonNode
import org.auth.csd.datalab.Model.LCEvent

class Accelerator extends LCEvent {

  var accx: Double = _
  var accy: Double = _
  var accz: Double = _

  override def setData(jsonNode: JsonNode): Unit = {
    val event: JsonNode = this.mapper.readTree(jsonNode.get("ACCValue").toString)
    this.pid = event.get("pID").asLong()
    this.pilotId = event.get("pilotId").asText()
    this.timestamp = this.formater_iso.format(this.formatter_ms.parse(event.get("timestamp").asText()))
    accx = event.get("accx").asDouble()
    accy = event.get("accy").asDouble()
    accz = event.get("accz").asDouble()
  }

  override def toString: String = {
    s"""{\"patientId\":${this.pid},\"pilotId\":\"${this.pilotId}\",\"timestamp\":\"$timestamp\",\"datetime\":\"$datetime\",\"accx\":$accx,\"accy\":$accy,\"accz\":$accz}"""
  }

  override def getKey: String = {
    pid.toString + "_" + accx.toString + "_" + timestamp
  }
}
