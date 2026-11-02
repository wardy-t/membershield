package com.tomward.membershield.assessment.api

import com.tomward.membershield.assessment.domain.AssessmentDecision
import com.tomward.membershield.assessment.domain.RiskLevel
import com.tomward.membershield.assessment.persistence.RiskAssessmentEntity
import com.tomward.membershield.assessment.persistence.RiskAssessmentRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

@SpringBootTest
@AutoConfigureMockMvc
class RiskAssessmentHistoryControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var repository: RiskAssessmentRepository

    @BeforeEach
    fun clearAssessments() {
        repository.deleteAll()
    }

    @Test
    fun `returns only the requested customer history newest first and within limit`() {
        val customerId = UUID.randomUUID()
        val otherCustomerId = UUID.randomUUID()

        repository.saveAll(
            listOf(
                assessment(
                    customerId = customerId,
                    paymentReference = "Older payment",
                    assessedAt =
                        Instant.parse("2026-10-20T10:00:00Z"),
                ),
                assessment(
                    customerId = customerId,
                    paymentReference = "Newest payment",
                    assessedAt =
                        Instant.parse("2026-10-21T10:00:00Z"),
                ),
                assessment(
                    customerId = otherCustomerId,
                    paymentReference = "Other customer payment",
                    assessedAt =
                        Instant.parse("2026-10-22T10:00:00Z"),
                ),
            ),
        )

        mockMvc
            .get("/api/v1/risk-assessments") {
                param("customerId", customerId.toString())
                param("limit", "1")
            }
            .andExpect {
                status { isOk() }
                content {
                    contentTypeCompatibleWith(
                        MediaType.APPLICATION_JSON,
                    )
                }
                jsonPath("$.count") { value(1) }
                jsonPath("$.assessments.length()") { value(1) }
                jsonPath("$.assessments[0].paymentReference") {
                    value("Newest payment")
                }
                jsonPath("$.assessments[0].score") {
                    value(25)
                }
                jsonPath("$.assessments[0].riskLevel") {
                    value("MEDIUM")
                }
                jsonPath("$.assessments[0].decision") {
                    value("SHOW_GUIDANCE")
                }
            }
    }

    @Test
    fun `returns bad request when history limit is outside allowed range`() {
        mockMvc
            .get("/api/v1/risk-assessments") {
                param("customerId", UUID.randomUUID().toString())
                param("limit", "51")
            }
            .andExpect {
                status { isBadRequest() }
                content {
                    contentTypeCompatibleWith(
                        MediaType.APPLICATION_JSON,
                    )
                }
                jsonPath("$.code") {
                    value("INVALID_REQUEST")
                }
                jsonPath("$.message") {
                    value("History limit must be between 1 and 50")
                }
            }
    }

    private fun assessment(
        customerId: UUID,
        paymentReference: String,
        assessedAt: Instant,
    ): RiskAssessmentEntity =
        RiskAssessmentEntity(
            id = UUID.randomUUID(),
            customerId = customerId,
            recipientId = UUID.randomUUID(),
            amount = BigDecimal("250.00"),
            currency = "GBP",
            paymentReference = paymentReference,
            requestedAt = assessedAt.minusSeconds(60),
            recipientPreviouslyPaid = false,
            recipientTrusted = false,
            customerAveragePayment = BigDecimal("100.00"),
            recentAttemptCount = 0,
            customerTimeZone = "Europe/London",
            score = 25,
            riskLevel = RiskLevel.MEDIUM,
            decision = AssessmentDecision.SHOW_GUIDANCE,
            assessedAt = assessedAt,
            reasons = emptyList(),
        )
}