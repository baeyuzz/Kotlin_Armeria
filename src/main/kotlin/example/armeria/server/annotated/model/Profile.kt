package example.armeria.server.annotated.model

import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field

// meta정보 - 이름 너비 높이 촬영 시각
// 픽셀값 통계 - 최소 최대 평균
// 픽셀 히스토그램 - 0-255 픽셀 수

@Document(collection="imgProfile")
data class Profile(

        @Field("name")
        val name: String,
        @Field("width")
        val width: Int,
        @Field("height")
        val height: Int,
        @Field("originalDate")
        val originalDate: String,
        @Field("maxPixel")
        val maxPixel: Int,
        @Field("minPixel")
        val minPixel: Int,
        @Field("avgPixel")
        val avgPixel: Int,
        @Field("histogram")
        val histogram: IntArray
)
