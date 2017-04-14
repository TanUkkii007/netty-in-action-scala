package com.github.tanukkii007.nettyinaction.chapter13

import java.net.InetSocketAddress


case class LogEvent(source: InetSocketAddress, received: Long, lofFile: String, msg: String)

object LogEvent {
  val SEPARATOR = ':'.toByte
}