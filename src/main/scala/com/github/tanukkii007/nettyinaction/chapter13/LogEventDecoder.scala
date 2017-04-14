package com.github.tanukkii007.nettyinaction.chapter13

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.socket.DatagramPacket
import io.netty.handler.codec.MessageToMessageDecoder
import io.netty.util.CharsetUtil


class LogEventDecoder extends MessageToMessageDecoder[DatagramPacket] {
  override def decode(ctx: ChannelHandlerContext, msg: DatagramPacket, out: java.util.List[AnyRef]): Unit = {
    val data = msg.content()
    val i = data.indexOf(0, data.readableBytes(), LogEvent.SEPARATOR)
    val filename = data.slice(0, i).toString(CharsetUtil.UTF_8)
    val logMsg = data.slice(i + 1, data.readableBytes()).toString(CharsetUtil.UTF_8)
    val event = new LogEvent(msg.recipient(), System.currentTimeMillis(), filename, logMsg)
    out.add(event)
  }
}
