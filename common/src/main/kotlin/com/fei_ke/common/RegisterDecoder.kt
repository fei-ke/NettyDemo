package com.fei_ke.common

import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToMessageDecoder
import io.netty.util.ReferenceCountUtil

class RegisterDecoder : MessageToMessageDecoder<Map<String, *>>() {
    override fun decode(ctx: ChannelHandlerContext, msg: Map<String, *>, out: MutableList<Any>) {
        if (msg["method"] == "register") {
            out.add(msg["who"]!!)
        } else {
            out.add(ReferenceCountUtil.retain(msg))
        }
    }
}