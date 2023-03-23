package org.auth.csd.datalab.Model.Metrics

class SimpleMetric(patient_id:Long,k:String,v:Double,ts:Long,date:Long) extends Metric {
  override var key: String = k
  override var value: Double = v
  this.pid=patient_id
  this.timestamp=formater_iso.format(ts)
  override val datetime: String = this.formater_iso.format(date)

  override def toString: String = {
    s"""{\"patientId\":$pid,\"timestamp\":\"$timestamp\",\"datetime\":\"$datetime\",\"key\":\"$key\",\"value\":$value}"""

  }
}
