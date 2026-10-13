package com.tomward.membershield.assessment.risk

import com.tomward.membershield.assessment.domain.PaymentRiskContext
import com.tomward.membershield.assessment.domain.ProposedPayment
import com.tomward.membershield.assessment.domain.RiskReasonCode
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.Instant
import java.util.Currency
import java.util.UUID
import java.time.ZoneId

class RepeatedAttemptsRuleTest {

    private val rule = RepeatedAttemptsRule()

    @Test
    fun `returns a risk reason for three recent attempts`() {
        val result = rule.evaluate(
            riskContext(recentAttemptCount = 3),
        )

        assertEquals(RiskReasonCode.REPEATED_ATTEMPTS, result?.code)
        assertEquals(15, result?.scoreImpact)
    }

    @Test
    fun `returns no risk reason for two recent attempts`() {
        val result = rule.evaluate(
            riskContext(recentAttemptCount = 2),
        )

        assertNull(result)
    }

    private fun riskContext(
        recentAttemptCount: Int,
    ) = PaymentRiskContext(
        payment = ProposedPayment(
            customerId = UUID.fromString(
                "1bf5b192-1bd6-4c91-8989-fffc0a05887e",
            ),
            recipientId = UUID.fromString(
                "d1dbd4ea-482c-4dc6-a0f1-5d42ab2a82d0",
            ),
            amount = BigDecimal("100.00"),
            currency = Currency.getInstance("GBP"),
            reference = "Invoice 123",
            requestedAt = Instant.parse("2026-10-13T12:00:00Z"),
        ),
        recipientPreviouslyPaid = true,
        recipientTrusted = false,
        customerAveragePayment = BigDecimal("75.00"),
        recentAttemptCount = recentAttemptCount,
        customerTimeZone = ZoneId.of("Europe/London"),
    )
}