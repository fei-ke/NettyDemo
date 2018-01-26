package com.fei_ke.nettyserver

import org.springframework.web.bind.annotation.*

@RestController
class APIController {
    @RequestMapping("/{who}")
    fun greeting(@PathVariable("who") who: String, @RequestParam map: Map<String, *>): Any? {
        return ChannelCenter.senAndWaite(who, map)
    }

}