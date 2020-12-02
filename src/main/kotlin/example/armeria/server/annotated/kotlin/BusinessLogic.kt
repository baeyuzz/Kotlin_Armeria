import com.mongodb.client.FindIterable
import example.armeria.server.annotated.model.Profile
import kotlinx.coroutines.asCoroutineDispatcher
import org.bson.Document
import com.drew.imaging.ImageMetadataReader
import com.drew.metadata.Metadata
import example.armeria.server.annotated.kotlin.MongoService
import kotlinx.coroutines.withContext
import java.awt.image.BufferedImage
import java.io.File
import java.util.concurrent.Executors
import javax.imageio.ImageIO

object BusinessLogic {

    private val myDispatcher = Executors.newFixedThreadPool(1).asCoroutineDispatcher()

    suspend fun postTasking() {
        val inputFile = File("c:/dev/image.png")
        val bufferedImage: BufferedImage = ImageIO.read(inputFile)
        var width = 0
        var height = 0
        var name = ""
        var originalDate = ""

        var p: Profile? = null

        withContext(myDispatcher) {

            val metadata: Metadata = ImageMetadataReader.readMetadata(inputFile)

            // 1. exif 이미지 메타 정보
            for (directory in metadata.directories) {
                for (tag in directory.tags) {
                    when {
                        tag.tagName == "Image Height" -> {
                            height = Integer.parseInt(tag.description.split(" ")[0])
                        }
                        tag.tagName == "Image Width" -> {
                            width = Integer.parseInt(tag.description.split(" ")[0])
                        }
                        tag.tagName == "File Name" -> {
                            name = tag.description
                        }
                        tag.tagName == "Date/Time Original" -> {
                            originalDate = tag.description
                        }
                    }
                }
            }
        }

        withContext(myDispatcher) {

            // 2. 픽셀 통계 및 히스토그램
            // gray scale 가정
            var rgbs = bufferedImage.getRGB(0, 0, width, height, null, 0, width)

            var max = 0
            var min = 255
            var sum = 0
            var histogram = IntArray(256)

            for (i in 0 until width * height) {
                val pixel = rgbs[i] and 0xff
                histogram[pixel]++ // 히스토그램
                sum += pixel // pixel avg 구하기 위해서
                min = Integer.min(min, pixel)
                max = Integer.max(max, pixel)
            }

            val div = width * height
            val avg = sum / div

            p = Profile(name, width, height, originalDate, max, min, avg, histogram)


        }

        withContext(myDispatcher) {
            val mongodb = MongoService()
            mongodb.MongoService()
            p?.let { mongodb.insert(it) }
            mongodb.close()

        }
    }

    suspend fun getAllTasking(): List<Profile> {
        var res: FindIterable<Document>
        val profileList = ArrayList<Profile>()

        withContext(myDispatcher) {
            val mongodb = MongoService()
            mongodb.MongoService()
            res = mongodb.findAll()


            for (r in res) {
                val name: String = r["name"] as String
                val width: Int = r["width"] as Int
                val height: Int = r["height"] as Int
                val originalDate: String = r["originalDate"] as String
                val maxPixel: Int = r["maxPixel"] as Int
                val minPixel: Int = r["minPixel"] as Int
                val avgPixel: Int = r["avgPixel"] as Int

                val tmpHistogram = r["histogram"].toString().replace("[", "")
                        .replace("]", "").replace(" ", "").split(",")

                println(tmpHistogram.toString())
                var histogram = IntArray(tmpHistogram.size)

                for ((i, tmp) in tmpHistogram.withIndex()) {
                    histogram[i] = Integer.parseInt(tmp)
                }

                println(histogram.toString())
                val p = Profile(name, width, height, originalDate, maxPixel, minPixel, avgPixel, histogram)

                profileList.add(p)
                for (pl in profileList)
                    println(pl.toString())
            }

            mongodb.close()
        }

        return profileList!!
    }

    suspend fun getOneTasking(id: String): Profile {
        var res: FindIterable<Document>
        var p: Profile? = null

        withContext(myDispatcher) {
            val mongodb = MongoService()
            mongodb.MongoService()
            res = mongodb.findOne(id)!!


            for (r in res) {

                println("---------------${r.toJson()}")

                val name: String = r["name"] as String
                val width: Int = r["width"] as Int
                val height: Int = r["height"] as Int
                val originalDate: String = r["originalDate"] as String
                val maxPixel: Int = r["maxPixel"] as Int
                val minPixel: Int = r["minPixel"] as Int
                val avgPixel: Int = r["avgPixel"] as Int

                val tmpHistogram = r["histogram"].toString().replace("[", "")
                        .replace("]", "").replace(" ", "").split(",")

                println(tmpHistogram.toString())
                var histogram = IntArray(tmpHistogram.size)

                for ((i, tmp) in tmpHistogram.withIndex()) {
                    histogram[i] = Integer.parseInt(tmp)
                }


                p = Profile(name, width, height, originalDate, maxPixel, minPixel, avgPixel, histogram)
            }

            mongodb.close()
        }
        return p!!
    }
}