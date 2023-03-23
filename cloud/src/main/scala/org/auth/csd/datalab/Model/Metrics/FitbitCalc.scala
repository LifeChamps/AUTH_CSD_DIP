package org.auth.csd.datalab.Model.Metrics

import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.JsonNode

class FitbitCalc extends Metric {
  override var key: String = _
  override var value: Double = _

  override def setData(jsonNode: JsonNode): Unit = {
    key = jsonNode.get("key").asText()
    value = jsonNode.get("value").asDouble(-177053)
    this.pilotId = jsonNode.get("pilotId").asText()
    this.pid = jsonNode.get("patientId").asLong()
    this.timestamp = this.formater_iso.format(this.formatter_ms.parse(jsonNode.get("timestamp").asText()))
  }

  override def toString: String = {
    if(!key.equals("hr")){
      s"""{\"patientId\":$pid,\"pilotId\":\"${this.pilotId}\",\"timestamp\":\"$timestamp\",\"datetime\":\"$datetime\",\"key\":\"$key\",\"value\":$value}"""
    }else{
      s"""{\"patientId\":$pid,\"pilotId\":\"${this.pilotId}\",s\"timestamp\":\"$timestamp\",\"datetime\":\"$datetime\",\"key\":\"$key\",\"value\":${value.toString}"""
    }
  }

}
