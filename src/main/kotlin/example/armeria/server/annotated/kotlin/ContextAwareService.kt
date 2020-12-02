package example.armeria.server.annotated.kotlin


import com.linecorp.armeria.common.AggregatedHttpRequest
import com.linecorp.armeria.common.HttpResponse
import com.linecorp.armeria.server.ServiceRequestContext
import com.linecorp.armeria.server.annotation.Get
import com.linecorp.armeria.server.annotation.Param
import com.linecorp.armeria.server.annotation.Post

import kotlinx.coroutines.withContext

import org.slf4j.LoggerFactory

class ContextAwareService {


    // 남은 것
    // 디비 연동
    // img 불러오기..
    // 비동기 제대로 해놓기
    // test code
    // ServiceRequestContext.current() 이건 머임?

    @Post("/")
    suspend fun profile(req: AggregatedHttpRequest): HttpResponse {

        ServiceRequestContext.current()

        withContext(ArmeriaRequestContext()) {


//            log.info("Start blocking task ")
//
//            val path = System.getProperty("user.dir") + "\\src\\test.jpg"
//            val path2 = System.getProperty("user.dir") + "\\src\\test.txt"
//            val finalPath = System.getProperty("user.dir") + "\\src\\final.txt"
//
//            try {
//                var arr : ByteArray = req.content().array()
////                var arr = "abc".encodeToByteArray()
//
//                for(b in arr){
//                    arr.copyOfRange(100,1002)
//                }
//                Files.write(Paths.get(path2), arr)
//
//                val file =  File(path2)
//                //입력 스트림 생성
//                val filereader = FileReader(file);
//
//
//            } catch (e: IOException) {
//                print(e)
//            }

            BusinessLogic.postTasking()

            log.info("Finished blocking task for")

            ServiceRequestContext.current()
        }

        ServiceRequestContext.current()

        return HttpResponse.of("content")
//        return FooResponse(id = id, name = name)
    }

    companion object {
        private val log = LoggerFactory.getLogger(ContextAwareService::class.java)
    }

    @Get("/")
    suspend fun getProfile(): HttpResponse {
        val result = BusinessLogic.getAllTasking()
        return HttpResponse.of(result.toString())
    }

    @Get("/id")
    suspend fun getOneProfile(@Param id: String): HttpResponse {

        val result = BusinessLogic.getOneTasking(id)
        return HttpResponse.of(result.toString())
    }

}
