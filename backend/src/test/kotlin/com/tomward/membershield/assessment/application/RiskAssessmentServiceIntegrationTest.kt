package com.tomward.membershield.assessment.application

import com.tomward.membershield.assessment.domain.AssessmentDecision
import com.tomward.membershield.assessment.domain.PaymentRiskContext
import com.tomward.membershield.assessment.domain.ProposedPayment
import com.tomward.membershield.assessment.domain.RiskLevel
import com.tomward.membershield.assessment.domain.RiskReasonCode
import com.tomward.membershield.assessment.persistence.RiskAssessmentRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.math.BigDecimal
import java.time.Instant
import java.time.ZoneId
import java.util.Currency
import java.util.UUID
import kotlin.test.assertEquals

@SpringBootTest
class RiskAssessmentServiceIntegrationTest {

    @Autowired
    private lateinit var service: RiskAssessmentService

    @Autowired
    private lateinit var repository: RiskAssessmentRepository

    @BeforeEach
    fun clearAssessments() {
        repository.deleteAll()
    }

    @Test
    fun `persists the payment evidence assessment and ordered reasons`() {
        val customerId = UUID.randomUUID()
        val recipientId = UUID.randomUUID()
        val requestedAt = Instant.parse("2026-10-13T20:30:00Z")

        val context =
            PaymentRiskContext(
                payment =
                    ProposedPayment(
                        customerId = customerId,
                        recipientId = recipientId,
                        amount = BigDecimal("1500.00"),
                        currency = Currency.getInstance("GBP"),
                        reference = "Invoice payment",
                        requestedAt = requestedAt,
                    ),
                recipientPreviouslyPaid = false,
                recipientTrusted = false,
                customerAveragePayment = BigDecimal("400.00"),
                recentAttemptCount = 3,
                customerTimeZone = ZoneId.of("Europe/London"),
            )

        service.assess(context)

        val savedAssessment =
            repository
                .findAllByCustomerIdOrderByAssessedAtDesc(customerId)
                .single()

        assertEquals(customerId, savedAssessment.customerId)
        assertEquals(recipientId, savedAssessment.recipientId)
        assertEquals(
            BigDecimal("1500.00"),
            savedAssessment.amount,
        )
        assertEquals("GBP", savedAssessment.currency)
        assertEquals(
            "Invoice payment",
            savedAssessment.paymentReference,
        )
        assertEquals(requestedAt, savedAssessment.requestedAt)
        assertEquals(false, savedAssessment.recipientPreviouslyPaid)
        assertEquals(false, savedAssessment.recipientTrusted)
        assertEquals(
            BigDecimal("400.00"),
            savedAssessment.customerAveragePayment,
        )
        assertEquals(3, savedAssessment.recentAttemptCount)
        assertEquals(
            "Europe/London",
            savedAssessment.customerTimeZone,
        )
        assertEquals(80, savedAssessment.score)
        assertEquals(RiskLevel.CRITICAL, savedAssessment.riskLevel)
        assertEquals(
            AssessmentDecision.PAUSE_AND_CONTACT_BANK,
            savedAssessment.decision,
        )
        assertEquals(
            listOf(
                RiskReasonCode.NEW_RECIPIENT,
                RiskReasonCode.UNUSUAL_AMOUNT,
                RiskReasonCode.LARGE_FIRST_PAYMENT,
                RiskReasonCode.REPEATED_ATTEMPTS,
            ),
            savedAssessment.reasons.map { reason -> reason.code },
        )
    }
}