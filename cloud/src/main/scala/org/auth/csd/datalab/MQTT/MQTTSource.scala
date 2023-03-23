package org.auth.csd.datalab.Utils

import java.util
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.atomic.AtomicBoolean
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.source.{MessageAcknowledgingSourceBase, ParallelSourceFunction, SourceFunction}
import org.auth.csd.datalab.Utils.Helpers.readEnvVariable
import org.eclipse.paho.client.mqttv3._
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence


import scala.collection.convert.WrapAsScala._

class MQTTSource[OUT](config: MQTTSourceConfig[OUT], c_sleep: Long)
  extends MessageAcknowledgingSourceBase[OUT, String](classOf[String])
    with MqttCallback
    with ParallelSourceFunction[OUT] {

  val uri: Seq[String] = config.uri
  val clientIdPrefix = config.clientIdPrefix
  val topic = config.topic
  val qos = config.qos
  val deserializationSchema = config.deserializationSchema

  val sleep = c_sleep

  var client: MqttClient = _
  var genClientId: String = _
  var running: AtomicBoolean = new AtomicBoolean(false)
  val queue = new LinkedBlockingQueue[(String, MqttMessage)]
  val unacknowledgedMessages = new util.HashMap[String, MqttMessage]()


  override def open(parameters: Configuration): Unit = {
    super.open(parameters)

    // usual brokers(such as mosquitto) are disconnecting clients that have same client id.
    // because brokers judges it to be an old connection.
    // so this implementation generate unique client ids for each threads.
    genClientId = "%s_%04d%05d".format(
      clientIdPrefix,
      Thread.currentThread().getId % 10000,
      System.currentTimeMillis % 100000
    )
    val part_id = getRuntimeContext.getIndexOfThisSubtask
    val broker_url = readEnvVariable("MQTT_BROKER")
    if (broker_url != null) connect(broker_url)
    running.set(true)
  }

  override def close(): Unit = {
    super.close()
    if (client != null) {
      client.close()
      running.set(false)
    }
  }

  override def run(ctx: SourceFunction.SourceContext[OUT]): Unit = {
    var count = 0
    while (running.get()) {
      if (client != null) {
        val message = queue.take()
        val msg = message._2
        val value = deserializationSchema.deserialize(message._2.getPayload)
        val result = deserializationSchema.deserialize(value.toString
          //          .replace("SensorValue", "")
          //          .replace("\"","\'")
          .getBytes())
        ctx.getCheckpointLock.synchronized {
          ctx.collect(result)
          if (msg.getQos > 0) {
            val id = message.hashCode().toString
            addId(id)
            unacknowledgedMessages.synchronized {
              unacknowledgedMessages.put(id, msg)
            }
          }
        }
      } else {
        Thread.sleep(sleep)
        val part_id = getRuntimeContext.getIndexOfThisSubtask
        val broker_url = readEnvVariable("MQTT_BROKER")
        if (broker_url != null) {
          connect(broker_url)
        }
      }
    }

  }

  override def cancel(): Unit = {
    this.running.set(false)
  }

  override def connectionLost(cause: Throwable): Unit = {
    client.reconnect()
  }

  override def deliveryComplete(token: IMqttDeliveryToken): Unit = {}

  override def messageArrived(topic: String, message: MqttMessage): Unit = {
    queue.put((topic, message))
  }

  override def acknowledgeIDs(checkpointId: Long, uIds: util.Set[String]): Unit = {
    uIds.foreach { id =>
      unacknowledgedMessages.synchronized {
        val message = unacknowledgedMessages.get(id)
        message.synchronized {
          //message.notify()
          unacknowledgedMessages.remove(id)
        }
      }
    }
  }

  def connect(broker_url: String): Unit = {
    val mqtt_username = readEnvVariable("MQTT_USERNAME")
    val mqtt_password = readEnvVariable("MQTT_PASSWORD")
    if (client == null) {
      val connOpts = new MqttConnectOptions
      connOpts.setCleanSession(true)
      connOpts.setUserName(mqtt_username)
      connOpts.setPassword(mqtt_password.toCharArray)
      if(broker_url.split(("://"))(0)=="ssl") {
        //        connOpts.setHttpsHostnameVerificationEnabled(false)
        val factory = SecurityUtil.getSocketFactory(readEnvVariable("MQTT_CA"))
        connOpts.setSocketFactory(factory)
      }
      client = new MqttClient(broker_url, genClientId, new MemoryPersistence())
      client.connect(connOpts)
      client.subscribe(topic, qos)
      client.setCallback(this)
    }
  }
}
