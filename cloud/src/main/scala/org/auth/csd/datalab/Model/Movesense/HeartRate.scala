package org.auth.csd.datalab.Model.Movesense

import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.JsonNode
import org.auth.csd.datalab.Model.LCEvent

class HeartRate extends LCEvent {
  var hr: Double = _
  var rr: Double = _

  override def setData(jsonNode: JsonNode): Unit = {
    val event: JsonNode = this.mapper.readTree(jsonNode.get("HRValue").toString)
    this.pid = event.get("pID").asLong()
    this.pilotId = event.get("pilotId").asText()
    this.timestamp = this.formater_iso.format(this.formatter_ms.parse(event.get("timestamp").asText()))
    hr = event.get("hr").asDouble()
    rr = event.get("rr").asDouble()
  }

  override def toString: String = {
    s"""{\"patientId\":${this.pid},\"pilotId\":\"${this.pilotId}\",\"timestamp\":\"$timestamp\",\"datetime\":\"$datetime\",\"hr_tshirt\":$hr,\"rr_int\":$rr}"""
  }

  override def getKey: String = {
    pid.toString + "_" + hr.toString + "_" + timestamp
  }
}
