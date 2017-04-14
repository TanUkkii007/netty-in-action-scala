package com.github.tanukkii007.nettyinaction.chapter13

import io.netty.channel.{ChannelHandlerContext, SimpleChannelInboundHandler}


class LogEventHandler extends SimpleChannelInboundHandler[LogEvent] {

  override def exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable): Unit = {
    cause.printStackTrace()
    ctx.close()
  }

  override def channelRead0(ctx: ChannelHandlerContext, msg: LogEvent): Unit = {
    val builder = new StringBuilder
    builder.append(msg.receivedTimestamp)
    builder.append(" [")
    builder.append(msg.source.toString)
    builder.append("] [")
    builder.append(msg.lofFile)
    builder.append("] : ")
    builder.append(msg.msg)

    println(builder.toString())
  }
}
