package com.tomward.membershield.status

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@WebMvcTest(StatusController::class)
class StatusControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun `returns the service status`() {
        mockMvc.get("/api/v1/status")
            .andExpect {
                status { isOk() }
                content {
                    contentType(MediaType.APPLICATION_JSON)
                }
                jsonPath("$.service") {
                    value("membershield-api")
                }
                jsonPath("$.status") {
                    value("UP")
                }
            }
    }
}