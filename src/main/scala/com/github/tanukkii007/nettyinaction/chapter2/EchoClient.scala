package com.github.tanukkii007.nettyinaction.chapter2

import java.net.InetSocketAddress

import com.typesafe.config.ConfigFactory
import io.netty.bootstrap.Bootstrap
import io.netty.channel.{ChannelFuture, ChannelInitializer}
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioSocketChannel


class EchoClient(host: String, port: Int) {

  def start(): ChannelFuture = {
    val group = new NioEventLoopGroup()
    try {
      val b = new Bootstrap()
      b.group(group)
        .channel(classOf[NioSocketChannel])
        .remoteAddress(new InetSocketAddress(host, port))
        .handler(new ChannelInitializer[SocketChannel] {
          override def initChannel(ch: SocketChannel): Unit = {
            ch.pipeline().addLast(new EchoClientHandler)
          }
        })

      val f: ChannelFuture = b.connect().sync()
      f.channel().closeFuture().sync()
    } finally {
      group.shutdownGracefully().sync()
    }
  }
}

object EchoClient extends App {

  val config = ConfigFactory.load().getConfig("netty-in-action.chapter2.echo-client")
  val host = config.getString("host")
  val port = config.getInt("port")
  new EchoClient(host, port).start()
}