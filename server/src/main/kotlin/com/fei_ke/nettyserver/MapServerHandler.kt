package com.fei_ke.nettyserver

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler

class MapServerHandler : SimpleChannelInboundHandler<Map<String, *>>() {
    override fun channelRead0(ctx: ChannelHandlerContext, msg: Map<String, *>) {
        println(msg.toString())
        ChannelCenter.postResult(msg)
    }

}