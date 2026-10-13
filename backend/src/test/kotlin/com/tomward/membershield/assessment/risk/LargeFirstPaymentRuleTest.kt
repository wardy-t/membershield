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

class LargeFirstPaymentRuleTest {

    private val rule = LargeFirstPaymentRule()

    @Test
    fun `returns a risk reason for a first GBP payment of one thousand pounds`() {
        val result = rule.evaluate(
            riskContext(
                amount = BigDecimal("1000.00"),
                recipientPreviouslyPaid = false,
            ),
        )

        assertEquals(RiskReasonCode.LARGE_FIRST_PAYMENT, result?.code)
        assertEquals(15, result?.scoreImpact)
    }

    @Test
    fun `returns no risk reason below one thousand pounds`() {
        val result = rule.evaluate(
            riskContext(
                amount = BigDecimal("999.99"),
                recipientPreviouslyPaid = false,
            ),
        )

        assertNull(result)
    }

    @Test
    fun `returns no risk reason for a previously paid recipient`() {
        val result = rule.evaluate(
            riskContext(
                amount = BigDecimal("1000.00"),
                recipientPreviouslyPaid = true,
            ),
        )

        assertNull(result)
    }

    @Test
    fun `returns no risk reason when the currency is not GBP`() {
        val result = rule.evaluate(
            riskContext(
                amount = BigDecimal("1000.00"),
                recipientPreviouslyPaid = false,
                currency = Currency.getInstance("EUR"),
            ),
        )

        assertNull(result)
    }

    private fun riskContext(
        amount: BigDecimal,
        recipientPreviouslyPaid: Boolean,
        currency: Currency = Currency.getInstance("GBP"),
    ) = PaymentRiskContext(
        payment = ProposedPayment(
            customerId = UUID.fromString(
                "1bf5b192-1bd6-4c91-8989-fffc0a05887e",
            ),
            recipientId = UUID.fromString(
                "d1dbd4ea-482c-4dc6-a0f1-5d42ab2a82d0",
            ),
            amount = amount,
            currency = currency,
            reference = "Invoice 123",
            requestedAt = Instant.parse("2026-10-13T12:00:00Z"),
        ),
        recipientPreviouslyPaid = recipientPreviouslyPaid,
        recipientTrusted = false,
        customerAveragePayment = BigDecimal("100.00"),
        recentAttemptCount = 0,
    )
}