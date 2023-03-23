package org.auth.csd.datalab.Model.Metrics

import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.JsonNode

class MetricsCalc extends Metric {
  override var key: String = _
  override var value: Double = _

  override def setData(jsonNode: JsonNode): Unit = {
    val event: JsonNode = jsonNode.get("Metric")
    key = event.get("key").asText()
    value = event.get("value").asDouble(-177053)
    this.pid = event.get("patientId").asLong()
    this.pilotId = event.get("pilotId").asText()
    this.timestamp = this.formater_iso.format(this.formatter_ms.parse(event.get("timestamp").asText()))
  }

  override def toString: String = {
    if (this.key == "bmi_category") {
      val category = value match {
        case 0.0 => "underweight"
        case 1.0 => "healthy"
        case 2.0 => "overweight"
        case 3.0 => "obesity"
      }
      s"""{\"patientId\":$pid,\"pilotId\":\"${this.pilotId}\",\"timestamp\":\"$timestamp\",\"datetime\":\"$datetime\",\"key\":\"$key\",\"value\":\"$category\"}"""
    } else {
      s"""{\"patientId\":$pid,\"pilotId\":\"${this.pilotId}\",\"timestamp\":\"$timestamp\",\"datetime\":\"$datetime\",\"key\":\"$key\",\"value\":$value}"""
    }
  }
  
}
