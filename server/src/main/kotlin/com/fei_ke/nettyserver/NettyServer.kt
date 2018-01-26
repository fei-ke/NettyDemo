package com.fei_ke.nettyserver

import com.fei_ke.common.MapDecoder
import com.fei_ke.common.MapEncoder
import com.fei_ke.common.RegisterDecoder
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.Channel
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.EventLoopGroup
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy


@Service
class NettyServer {
    private var channel: Channel? = null
    private var bossGroup: EventLoopGroup? = null
    private var workerGroup: EventLoopGroup? = null

    @PostConstruct
    @Throws(Exception::class)
    fun run() {
        bossGroup = NioEventLoopGroup()
        workerGroup = NioEventLoopGroup()
        val serverBootstrap = ServerBootstrap()
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel::class.java)
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(object : ChannelInitializer<SocketChannel>() {
                    @Throws(Exception::class)
                    public override fun initChannel(ch: SocketChannel) {
                        ch.pipeline()
                                .addLast(MapDecoder())
                                .addLast(MapEncoder())
                                .addLast(RegisterDecoder())
                                .addLast(RegisterHandler())
                                .addLast(MapServerHandler())
                    }
                })
        channel = serverBootstrap.bind(5678).sync().channel()
    }

    @PreDestroy
    fun stop() {
        bossGroup?.shutdownGracefully()
        workerGroup?.shutdownGracefully()
        channel?.closeFuture()?.syncUninterruptibly()

        bossGroup = null
        workerGroup = null
        channel = null
    }
}