package org.auth.csd.datalab.Model

import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.JsonNode
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper

import java.text.SimpleDateFormat
import java.util.Date

abstract class LCEvent {
  var timestamp: String = _
  var pid: Long = _
  var pilotId: String = _
  val mapper = new ObjectMapper()
  val formater_iso = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
  val datetime: String = formater_iso.format(new Date(System.currentTimeMillis()))
  val formatter_ms = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy")
  val formatter_day = new SimpleDateFormat("yyyy-MM-dd")

  def getKey: String = {
    pid.toString + timestamp
  }
  def setData(jsonNode: JsonNode):Unit={}
}
