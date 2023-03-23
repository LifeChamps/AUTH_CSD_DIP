package org.auth.csd.datalab.Utils
import org.apache.flink.api.common.serialization.DeserializationSchema
import org.apache.flink.util.Preconditions
class MQTTSourceConfig [OUT](
                              uriArg: List[String],
                              clientIdPrefixArg: String,
                              topicArg: String,
                              qosArg: Int,
                              deserializationSchemaArg: DeserializationSchema[OUT]
                            ){
  val uri = Preconditions.checkNotNull(uriArg, "uri not set")
  val clientIdPrefix = Preconditions.checkNotNull(clientIdPrefixArg, "clientId not set")
  val topic = Preconditions.checkNotNull(topicArg, "topic not set")
  val qos = Preconditions.checkNotNull(qosArg, "qos not set")
  val deserializationSchema = Preconditions.checkNotNull(deserializationSchemaArg, "deserializationSchema not set")
}

object MQTTSourceConfig {
  class Builder[OUT] {
    var uri: List[String] = _
    var clientIdPrefix: String = _
    var topic: String = _
    var qos: Int = _
    var deserializationSchema: DeserializationSchema[OUT] = _

    def setURI(uri: List[String]): Builder[OUT] = {
      this.uri = uri
      this
    }

    def setClientIdPrefix(clientIdPrefix: String): Builder[OUT] = {
      this.clientIdPrefix = clientIdPrefix
      this
    }

    def setTopic(topic: String): Builder[OUT] = {
      this.topic = topic
      this
    }


    def setQos(qos: Int): Builder[OUT] = {
      this.qos = qos
      this
    }

    def setDeserializationSchema(deserializationSchema: DeserializationSchema[OUT]): Builder[OUT] = {
      this.deserializationSchema = deserializationSchema
      this
    }

    def build(): MQTTSourceConfig[OUT] = {
      new MQTTSourceConfig[OUT](uri, clientIdPrefix, topic, qos, deserializationSchema)
    }
  }

}