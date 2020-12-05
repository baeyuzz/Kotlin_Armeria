package com.sia.armeria.server

import com.linecorp.armeria.common.AggregatedHttpRequest
import com.linecorp.armeria.common.HttpResponse
import com.linecorp.armeria.server.ServiceRequestContext
import com.linecorp.armeria.server.annotation.Get
import com.linecorp.armeria.server.annotation.Param
import com.linecorp.armeria.server.annotation.Post
import com.sia.armeria.util.ArmeriaRequestContext
import com.sia.armeria.model.ImageProfile
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory


class Controller {

    private val log = LoggerFactory.getLogger("controller")

    @Post("")
    suspend fun postImage(req: AggregatedHttpRequest): HttpResponse {

        ServiceRequestContext.current()

        var p: ImageProfile

        withContext(ArmeriaRequestContext()) {
            p = BusinessLogic.postTasking(req.contentUtf8())!!
        }

        ServiceRequestContext.current()

        log.info("post image")
        return HttpResponse.of(p.toString())
    }


    @Get("")
    suspend fun getAllProfile(): HttpResponse {
        var result: List<ImageProfile>

        withContext(ArmeriaRequestContext()) {

            result = BusinessLogic.getAllTasking()

            ServiceRequestContext.current()
        }
        log.info("get all")

        return HttpResponse.of(result.toString())
    }

    @Get("/:id")
    suspend fun getOneProfile(@Param("id") id: String): HttpResponse {
        var result: ImageProfile

        withContext(ArmeriaRequestContext()) {

            result = BusinessLogic.getOneTasking(id)

            ServiceRequestContext.current()
        }
        log.info("get one")

        return HttpResponse.of(result.toString())
    }

}
