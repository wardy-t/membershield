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

class UnusualAmountRuleTest {

    private val rule = UnusualAmountRule()

    @Test
    fun `returns a risk reason when amount exceeds three times the average`() {
        val result = rule.evaluate(
            riskContext(
                amount = BigDecimal("300.01"),
                customerAveragePayment = BigDecimal("100.00"),
            ),
        )

        assertEquals(RiskReasonCode.UNUSUAL_AMOUNT, result?.code)
        assertEquals(25, result?.scoreImpact)
    }

    @Test
    fun `returns no risk reason when amount equals three times the average`() {
        val result = rule.evaluate(
            riskContext(
                amount = BigDecimal("300.00"),
                customerAveragePayment = BigDecimal("100.00"),
            ),
        )

        assertNull(result)
    }

    @Test
    fun `returns no risk reason when no average payment is available`() {
        val result = rule.evaluate(
            riskContext(
                amount = BigDecimal("300.00"),
                customerAveragePayment = BigDecimal.ZERO,
            ),
        )

        assertNull(result)
    }

    private fun riskContext(
        amount: BigDecimal,
        customerAveragePayment: BigDecimal,
    ) = PaymentRiskContext(
        payment = ProposedPayment(
            customerId = UUID.fromString(
                "1bf5b192-1bd6-4c91-8989-fffc0a05887e",
            ),
            recipientId = UUID.fromString(
                "d1dbd4ea-482c-4dc6-a0f1-5d42ab2a82d0",
            ),
            amount = amount,
            currency = Currency.getInstance("GBP"),
            reference = "Invoice 123",
            requestedAt = Instant.parse("2026-10-13T12:00:00Z"),
        ),
        recipientPreviouslyPaid = true,
        recipientTrusted = false,
        customerAveragePayment = customerAveragePayment,
        recentAttemptCount = 0,
        customerTimeZone = ZoneId.of("Europe/London"),
    )
}