package com.github.tanukkii007.nettyinaction.chapter13

import java.net.InetSocketAddress

import com.typesafe.config.ConfigFactory
import io.netty.bootstrap.Bootstrap
import io.netty.channel.{Channel, ChannelInitializer, ChannelOption}
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioDatagramChannel


class LogEventMonitor(address: InetSocketAddress) {

  val group = new NioEventLoopGroup()
  val bootstrap = new Bootstrap()
  bootstrap.group(group)
    .channel(classOf[NioDatagramChannel])
    .option(ChannelOption.SO_BROADCAST.asInstanceOf[ChannelOption[Any]], true)
      .handler(new ChannelInitializer[Channel] {
        override def initChannel(ch: Channel): Unit = {
          val pipeline = ch.pipeline()
          pipeline.addLast(new LogEventDecoder)
          pipeline.addLast(new LogEventHandler)
        }
      }).localAddress(address

  )

  def bind(): Channel = {
    bootstrap.bind().syncUninterruptibly().channel()
  }

  def stop(): Unit = {
    group.shutdownGracefully()
  }

}

object LogEventMonitor extends App {
  val config = ConfigFactory.load().getConfig("netty-in-action.chapter13.log-event-monitor")
  val port = config.getInt("port")
  val monitor = new LogEventMonitor(new InetSocketAddress(port))
  try {
    val channel = monitor.bind()
    println("LogEventMonitor running")
    channel.closeFuture().await()
  } finally {
    monitor.stop()
  }
}