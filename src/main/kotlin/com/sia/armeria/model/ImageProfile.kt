package com.sia.armeria.model

// meta정보 - 이름 너비 높이 촬영 시각
// 픽셀값 통계 - 최소 최대 평균
// 픽셀 히스토그램 - 0-255 픽셀 수

data class ImageProfile(
        val name: String,
        val width: Int,
        val height: Int,
        val originalDate: String,
        val maxPixel: Int,
        val minPixel: Int,
        val avgPixel: Int,
        val histogram: IntArray
)
