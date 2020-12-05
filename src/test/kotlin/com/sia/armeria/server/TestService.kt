package com.sia.armeria.server

import com.linecorp.armeria.client.WebClient
import com.linecorp.armeria.common.HttpStatus
import com.linecorp.armeria.server.Server
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

class TestService {
    companion object {
        private lateinit var server: Server
        private lateinit var client: WebClient

        @BeforeAll
        @JvmStatic
        fun beforeClass() {
            server = newServer(0)
            server.start().join()
            client = WebClient.of("http://127.0.0.1:" + server.activeLocalPort())
        }

        @AfterAll
        @JvmStatic
        fun afterClass() {
            server.stop().join()
            client.options().factory().close()
        }
    }

    @Test
    fun testGetOneService() {
        val res = client.get("/profile/5fc8833ce04f591e787a4d43").aggregate().join()
        assertThat(res.status()).isEqualTo(HttpStatus.OK)
        assertThat(res.contentUtf8()).isEqualTo("ImageProfile(name=name, width=1, height=1, originalDate=1, maxPixel=255, minPixel=0, avgPixel=100, histogram=[1, 2, 3])")
    }

    @Test
    fun testGetAllService() {
        val res = client.get("/profile").aggregate().join()
        assertThat(res.status()).isEqualTo(HttpStatus.OK)
    }

    @Test
    fun testPostService() {
        val res = client.post("/profile", "{\"path\" : \"c:/dev/sia/test.jpg\"}").aggregate().join()
        assertThat(res.status()).isEqualTo(HttpStatus.OK)
    }
}
