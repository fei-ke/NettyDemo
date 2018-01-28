package com.fei_ke.nettyclient

import com.fei_ke.common.MapDecoder
import com.fei_ke.common.MapEncoder
import com.fei_ke.common.RegisterDecoder
import io.netty.bootstrap.Bootstrap
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioSocketChannel


class NettyClient {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            NettyClient().run()
        }
    }

    @Throws(Exception::class)
    fun run() {
        val workerGroup = NioEventLoopGroup()
        try {
            val bootstrap = Bootstrap()
            bootstrap.group(workerGroup)
                    .channel(NioSocketChannel::class.java)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(object : ChannelInitializer<SocketChannel>() {
                        @Throws(Exception::class)
                        public override fun initChannel(ch: SocketChannel) {
                            ch.pipeline()
                                    .addLast(MapDecoder())
                                    .addLast(MapEncoder())
                                    .addLast(MapClientHandler())
                        }
                    })

            val channelFuture = bootstrap.connect("localhost", 5678).sync()

            channelFuture.channel().closeFuture().sync()
        } finally {
            workerGroup.shutdownGracefully()
        }
    }
}
