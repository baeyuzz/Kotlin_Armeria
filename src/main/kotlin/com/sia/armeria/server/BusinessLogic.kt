package com.sia.armeria.server


import com.drew.imaging.ImageMetadataReader
import com.drew.metadata.Metadata
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.mongodb.client.FindIterable
import com.sia.armeria.model.ImageProfile
import com.sia.armeria.util.DBConnection
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.withContext
import org.bson.Document
import java.io.File
import java.lang.Exception
import java.util.concurrent.Executors
import javax.imageio.ImageIO

object BusinessLogic {

    private val myDispatcher = Executors.newFixedThreadPool(2).asCoroutineDispatcher()
    private val mongodb = DBConnection()

    suspend fun postTasking(req: String): ImageProfile? {

        // path 가져오기
        val jsonParse = JsonParser()
        val obj: JsonObject = jsonParse.parse(req) as JsonObject
        val path: String = obj.get("path").toString().replace("\"", "")

        // img 불러오기
        var inputFile: File? = null
        try {
            inputFile = File(path)

        } catch (e: Exception) {
            e.printStackTrace()
        }
        val bufferedImage = ImageIO.read(inputFile)

        var width = 0
        var height = 0
        var name = ""
        var originalDate = ""

        // coroutine
        // 1. exif 이미지 메타 정보
        withContext(myDispatcher) {

            val metadata: Metadata = ImageMetadataReader.readMetadata(inputFile)

            for (directory in metadata.directories) {
                for (tag in directory.tags) {
                    when (tag.tagName) {
                        "Image Height" -> height = Integer.parseInt(tag.description.split(" ")[0])
                        "Image Width" -> width = Integer.parseInt(tag.description.split(" ")[0])
                        "File Name" -> name = tag.description
                        "Date/Time Original" -> originalDate = tag.description
                    }
                }
            }
        }

        val rgbs = bufferedImage.getRGB(0, 0, width, height, null, 0, width)
        var max = 0
        var min = 255
        var sum = 0
        var avg = 0
        var histogram = IntArray(256)

        // coroutine
        // 2. 픽셀 통계
        withContext(myDispatcher) {

            // gray scale 가정

            for (i in 0 until width * height) {
                val pixel = rgbs[i] and 0xff
                sum += pixel // pixel avg 구하기 위해서
                min = Integer.min(min, pixel)
                max = Integer.max(max, pixel)
            }

            avg = sum / (width * height)
        }

        // coroutine
        // 3. 히스토그램
        withContext(myDispatcher) {
            for (i in 0 until width * height) {
                val pixel = rgbs[i] and 0xff
                histogram[pixel]++ // 히스토그램
            }
        }

        val p = ImageProfile(name, width, height, originalDate, max, min, avg, histogram)

        // mongoDB에 저장
        p.let { mongodb.insert(it) }

        return p
    }

    suspend fun getAllTasking(): List<ImageProfile> {

        val profileList = ArrayList<ImageProfile>()
        // mongoDB 에서 전체 찾기
        var res: FindIterable<Document> = mongodb.findAll()

        // document -> Profile 객체로 변환
        for (r in res) {
            // ArrayList<Profile> 에 추가
            profileList.add(initializeProfile(r))
        }
        return profileList
    }

    suspend fun getOneTasking(id: String): ImageProfile {
        var p: ImageProfile? = null
        var res: FindIterable<Document> = mongodb.findOne(id)
        for (r in res) {
            p = initializeProfile(r)
        }
        return p!!
    }

    private fun initializeProfile(doc: Document): ImageProfile {
        // db에서 가져온 document -> Profile 객체로 변환
        val name: String = doc["name"] as String
        val width: Int = doc["width"] as Int
        val height: Int = doc["height"] as Int
        val originalDate: String = doc["originalDate"] as String
        val maxPixel: Int = doc["maxPixel"] as Int
        val minPixel: Int = doc["minPixel"] as Int
        val avgPixel: Int = doc["avgPixel"] as Int

        val tmpHistogram = doc["histogram"].toString().replace("[", "")
                .replace("]", "").replace(" ", "").split(",")

        var histogram = IntArray(tmpHistogram.size)
        for ((i, tmp) in tmpHistogram.withIndex()) {
            histogram[i] = Integer.parseInt(tmp)
        }
        return ImageProfile(name, width, height, originalDate, maxPixel, minPixel, avgPixel, histogram)
    }
}