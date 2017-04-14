package com.github.tanukkii007.nettyinaction.chapter13

import java.io.{File, RandomAccessFile}
import java.net.InetSocketAddress

import com.typesafe.config.ConfigFactory
import io.netty.bootstrap.Bootstrap
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioDatagramChannel


class LogEventBroadcaster(address: InetSocketAddress, file: File) {

  val group = new NioEventLoopGroup()
  val bootstrap = new Bootstrap()
  bootstrap.group(group)
    .channel(classOf[NioDatagramChannel])
    .option(ChannelOption.SO_BROADCAST.asInstanceOf[ChannelOption[Any]], true)
    .handler(new LogEventEncoder(address))

  def run(): Unit = {
    val ch = bootstrap.bind(0).syncUninterruptibly().channel()
    println("LogEventBroadcaster running")

    var continue = true
    var pointer = 0L
    while (continue) {
      val len = file.length()
      if (len < pointer) {
        pointer = len
      } else if (len > pointer) {
        val raf = new RandomAccessFile(file, "r")
        raf.seek(pointer)
        var line = ""
        while ({line = raf.readLine(); line != null}) {
          ch.writeAndFlush(LogEvent(null, -1, file.getAbsolutePath, line))
        }
        pointer = raf.getFilePointer
        raf.close()
      }
      try {
        Thread.sleep(1000)
      } catch {
        case e: InterruptedException =>
          Thread.interrupted()
          continue = false
      }
    }
  }

  def stop(): Unit = {
    group.shutdownGracefully()
  }
}

object LogEventBroadcaster extends App {
  val config = ConfigFactory.load().getConfig("netty-in-action.chapter13.log-event-broadcaster")
  val port = config.getInt("port")
  val filePath = config.getString("file-path")
  val broadcaster = new LogEventBroadcaster(new InetSocketAddress("255.255.255.255", port), new File(filePath))
  try {
    broadcaster.run()
  } finally {
    broadcaster.stop()
  }
}