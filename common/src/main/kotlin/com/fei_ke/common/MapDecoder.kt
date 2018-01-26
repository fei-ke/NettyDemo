package com.fei_ke.common

import com.google.gson.Gson
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.json.JsonObjectDecoder
import io.netty.util.CharsetUtil

class MapDecoder : JsonObjectDecoder() {
    override fun decode(ctx: ChannelHandlerContext, inByteBuf: ByteBuf, out: MutableList<Any>) {
        try {
            super.decode(ctx, inByteBuf, out)
        } catch (e: Exception) {
            //invalid JSON
            e.printStackTrace()
            return
        }

        inByteBuf.resetReaderIndex()
        val json = inByteBuf.toString(CharsetUtil.UTF_8)
        val map = Gson().fromJson(json, Map::class.java)
        out.add(map.toMap())

        //toString(Charset) method does not modify readerIndex writerIndex of this buffer， set them manually
        inByteBuf.setIndex(inByteBuf.readableBytes(), inByteBuf.writerIndex())
    }
}
