package com.tomward.membershield.assessment.api

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import java.util.UUID

@SpringBootTest
@AutoConfigureMockMvc
class RiskAssessmentControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `returns a critical assessment when multiple risk rules apply`() {
        val customerId = UUID.randomUUID()
        val recipientId = UUID.randomUUID()

        val requestBody =
            """
            {
              "customerId": "$customerId",
              "recipientId": "$recipientId",
              "amount": 1500.00,
              "currency": "GBP",
              "reference": "Invoice payment",
              "requestedAt": "2026-10-13T20:30:00Z",
              "recipientPreviouslyPaid": false,
              "recipientTrusted": false,
              "customerAveragePayment": 400.00,
              "recentAttemptCount": 3,
              "customerTimeZone": "Europe/London"
            }
            """.trimIndent()

        mockMvc
            .post("/api/v1/risk-assessments") {
                contentType = MediaType.APPLICATION_JSON
                content = requestBody
            }
            .andExpect {
                status { isOk() }
                content { contentTypeCompatibleWith(MediaType.APPLICATION_JSON) }
                jsonPath("$.score") { value(80) }
                jsonPath("$.riskLevel") { value("CRITICAL") }
                jsonPath("$.decision") {
                    value("PAUSE_AND_CONTACT_BANK")
                }
                jsonPath("$.reasons.length()") { value(4) }
                jsonPath("$.reasons[0].code") {
                    value("NEW_RECIPIENT")
                }
                jsonPath("$.reasons[1].code") {
                    value("UNUSUAL_AMOUNT")
                }
                jsonPath("$.reasons[2].code") {
                    value("LARGE_FIRST_PAYMENT")
                }
                jsonPath("$.reasons[3].code") {
                    value("REPEATED_ATTEMPTS")
                }
            }
    }
    @Test
    fun `returns a low-risk assessment when no risk rules apply`() {
        val customerId = UUID.randomUUID()
        val recipientId = UUID.randomUUID()

        val requestBody =
            """
            {
            "customerId": "$customerId",
            "recipientId": "$recipientId",
            "amount": 100.00,
            "currency": "GBP",
            "reference": "Monthly payment",
            "requestedAt": "2026-10-13T12:00:00Z",
            "recipientPreviouslyPaid": true,
            "recipientTrusted": true,
            "customerAveragePayment": 500.00,
            "recentAttemptCount": 0,
            "customerTimeZone": "Europe/London"
            }
            """.trimIndent()

        mockMvc
            .post("/api/v1/risk-assessments") {
                contentType = MediaType.APPLICATION_JSON
                content = requestBody
            }
            .andExpect {
                status { isOk() }
                content {
                    contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                }
                jsonPath("$.score") { value(0) }
                jsonPath("$.riskLevel") { value("LOW") }
                jsonPath("$.decision") { value("ALLOW") }
                jsonPath("$.reasons.length()") { value(0) }
            }
    }
    @Test
    fun `returns bad request when payment amount is invalid`() {
        val customerId = UUID.randomUUID()
        val recipientId = UUID.randomUUID()

        val requestBody =
            """
            {
            "customerId": "$customerId",
            "recipientId": "$recipientId",
            "amount": -10.00,
            "currency": "GBP",
            "reference": "Invalid payment",
            "requestedAt": "2026-10-13T12:00:00Z",
            "recipientPreviouslyPaid": false,
            "recipientTrusted": false,
            "customerAveragePayment": 100.00,
            "recentAttemptCount": 0,
            "customerTimeZone": "Europe/London"
            }
            """.trimIndent()

        mockMvc
            .post("/api/v1/risk-assessments") {
                contentType = MediaType.APPLICATION_JSON
                content = requestBody
            }
            .andExpect {
                status { isBadRequest() }
                content {
                    contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                }
                jsonPath("$.code") { value("INVALID_REQUEST") }
                jsonPath("$.message") {
                    value("Payment amount must be greater than zero")
                }
            }
    }
    @Test
    fun `returns bad request when request body is malformed`() {
        val malformedRequestBody =
            """
            {
            "customerId":
            }
            """.trimIndent()

        mockMvc
            .post("/api/v1/risk-assessments") {
                contentType = MediaType.APPLICATION_JSON
                content = malformedRequestBody
            }
            .andExpect {
                status { isBadRequest() }
                content {
                    contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                }
                jsonPath("$.code") { value("MALFORMED_REQUEST") }
                jsonPath("$.message") {
                    value("The request body is missing or malformed.")
                }
            }
    }
}