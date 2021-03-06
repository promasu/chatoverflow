package org.codeoverflow.chatoverflow.requirement.service.serial.impl

import java.io.InputStream

import org.codeoverflow.chatoverflow.WithLogger
import org.codeoverflow.chatoverflow.api.io.event.serial.{SerialDataAvailableEvent, SerialEvent}
import org.codeoverflow.chatoverflow.api.io.input.SerialInput
import org.codeoverflow.chatoverflow.registry.Impl
import org.codeoverflow.chatoverflow.requirement.impl.EventInputImpl
import org.codeoverflow.chatoverflow.requirement.service.serial.SerialConnector

@Impl(impl = classOf[SerialInput], connector = classOf[SerialConnector])
class SerialInputImpl extends EventInputImpl[SerialEvent, SerialConnector] with SerialInput with WithLogger {

  override def start(): Boolean = {
    sourceConnector.get.addInputListener(bytes => call(new SerialDataAvailableEvent(bytes)))
    true
  }

  override def getInputStream: InputStream = sourceConnector.get.getInputStream

  /**
    * Stops the input, called before source connector will shutdown
    *
    * @return true if stopping was successful
    */
  override def stop(): Boolean = true
}
