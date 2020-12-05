package com.sia.armeria.server


import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import com.linecorp.armeria.server.Server
import com.linecorp.armeria.server.docs.DocService
import org.slf4j.LoggerFactory
import kotlin.concurrent.thread


fun main(args: Array<String>) {
    val server = newServer(8080)

    var root = LoggerFactory
            .getLogger(Logger.ROOT_LOGGER_NAME) as Logger
    root.level = Level.INFO

    System.setProperty("kotlinx.coroutines.debug", "on")
    Runtime.getRuntime().addShutdownHook(
            thread(start = false) {
                server.stop().join()
                log.info("Server has been stopped.")
            }
    )
    server.start().join()

    log.info("Doc service at http://127.0.0.1:8080/docs")
}

fun newServer(port: Int): Server {
    return Server.builder()
        .http(port)
        .annotatedService()
        .pathPrefix("/profile")
        .build(Controller())
        .serviceUnder("/docs", DocService())
        .build()
}

private val log = LoggerFactory.getLogger("main")



