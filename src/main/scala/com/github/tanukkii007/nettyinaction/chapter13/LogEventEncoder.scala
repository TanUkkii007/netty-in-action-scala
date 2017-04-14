package com.github.tanukkii007.nettyinaction.chapter13

import java.net.InetSocketAddress
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.socket.DatagramPacket
import io.netty.handler.codec.MessageToMessageEncoder
import io.netty.util.CharsetUtil


class LogEventEncoder(remoteAddress: InetSocketAddress) extends MessageToMessageEncoder[LogEvent] {
  override def encode(ctx: ChannelHandlerContext, msg: LogEvent, out: java.util.List[AnyRef]): Unit = {
    val file = msg.lofFile.getBytes(CharsetUtil.UTF_8)
    val message = msg.msg.getBytes(CharsetUtil.UTF_8)
    val buf = ctx.alloc().buffer(file.length + message.length)
    buf.writeBytes(file)
    buf.writeByte(LogEvent.SEPARATOR)
    buf.writeBytes(message)
    out.add(new DatagramPacket(buf, remoteAddress))
  }
}
