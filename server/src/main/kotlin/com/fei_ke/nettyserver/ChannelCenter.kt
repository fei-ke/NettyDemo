package com.fei_ke.nettyserver

import io.netty.channel.ChannelHandlerContext
import java.util.*
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import kotlin.collections.HashMap

object ChannelCenter {
    private val channelHandlers = HashMap<String, ChannelHandlerContext>()
    private val executors = Executors.newCachedThreadPool()
    private val requests = WeakHashMap<String, Request>()

    fun register(who: String, handler: ChannelHandlerContext) {
        channelHandlers[who] = handler

        handler.writeAndFlush(mapOf(Pair("method", "register"), Pair("who", who)))

        println("register handler: $who->$handler")
    }

    fun unregister(channelHandler: ChannelHandlerContext) {
        channelHandlers.values.remove(channelHandler)
        println("unregister channelHandler: $channelHandler")
    }

    fun senAndWaite(who: String, map: Map<String, *>): Map<String, *>? {
        val context = channelHandlers[who] ?: return mapOf(Pair("result", "no client found"))

        val uuid = UUID.randomUUID().toString()
        val requestMap = map.toMutableMap()
        requestMap.put("uuid", uuid)

        val request = Request()
        requests[uuid] = request

        val future = executors.submit(request)

        context.writeAndFlush(requestMap)

        return future.get()
    }

    fun postResult(result: Map<String, *>) {
        val uuid = result["uuid"].toString()
        val map = result.toMutableMap()
        map.remove("uuid")
        requests[uuid]?.setResult(map)
    }

    class Request : Callable<Map<String, *>> {
        private val lock = Object()
        private lateinit var result: Map<String, *>
        override fun call(): Map<String, *> {
            synchronized(lock) {
                lock.wait()
            }
            return result
        }

        fun setResult(result: Map<String, *>) {
            this.result = result
            synchronized(lock) {
                lock.notify()
            }
        }

    }
}