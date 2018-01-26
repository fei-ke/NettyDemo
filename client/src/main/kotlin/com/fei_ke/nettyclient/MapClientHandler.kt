package com.fei_ke.nettyclient

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler

class MapClientHandler : SimpleChannelInboundHandler<Map<String, *>>() {
    private val WHO = "client1"

    override fun channelRead0(ctx: ChannelHandlerContext, msg: Map<String, *>) {
        println(msg.toString())
        if (msg["method"] == "register") {
            println("${msg["who"]} register success")
        } else {
            val result = msg.toMutableMap()
            result["result"] = "client received"
            ctx.writeAndFlush(result)
        }
    }

    override fun channelActive(ctx: ChannelHandlerContext) {
        println("register $WHO")
        val map = mapOf(Pair("method", "register"), Pair("who", "client1"))
        ctx.writeAndFlush(map)
    }
}