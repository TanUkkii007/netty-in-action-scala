package com.github.tanukkii007.nettyinaction.chapter2

import java.net.InetSocketAddress

import com.typesafe.config.ConfigFactory
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.{ChannelFuture, ChannelInitializer}
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel


class EchoServer(port: Int) {

  def start(): ChannelFuture = {
    val group = new NioEventLoopGroup()
    try {
      val b = new ServerBootstrap()
      b.group(group)
        .channel(classOf[NioServerSocketChannel])
        .localAddress(new InetSocketAddress(port))
        .childHandler(new ChannelInitializer[SocketChannel] {
          override def initChannel(ch: SocketChannel): Unit = {
            ch.pipeline().addLast(new EchoServerHandler())
          }
        })

      val f: ChannelFuture = b.bind().sync()
      println(getClass.getName + " started and listen on " + f.channel().localAddress())
      f.channel().closeFuture().sync()
    } finally {
      group.shutdownGracefully().sync()
    }
  }
}

object EchoServer extends App {
  val config = ConfigFactory.load().getConfig("netty-in-action.chapter2.echo-server")
  val port = config.getInt("port")
  new EchoServer(port).start()
}