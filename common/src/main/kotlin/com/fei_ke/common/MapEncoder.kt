package com.fei_ke.common

import com.google.gson.Gson
import com.sun.org.apache.xpath.internal.operations.String
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder
import io.netty.util.CharsetUtil

class MapEncoder : MessageToByteEncoder<Map<String, *>>() {
    override fun encode(ctx: ChannelHandlerContext, msg: Map<String, *>, out: ByteBuf) {
        val json = Gson().toJson(msg)
        out.writeBytes(json.toByteArray(CharsetUtil.UTF_8))
    }
}
