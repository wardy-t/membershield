package com.tomward.membershield.assessment.domain

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal
import java.time.Instant
import java.util.Currency
import java.util.UUID

class PaymentRiskContextTest {

    @Test
    fun `rejects a negative customer average payment`() {
        val exception = assertThrows<IllegalArgumentException> {
            riskContext(customerAveragePayment = BigDecimal("-1.00"))
        }

        assertEquals(
            "Customer average payment must not be negative",
            exception.message,
        )
    }

    @Test
    fun `rejects a negative recent attempt count`() {
        val exception = assertThrows<IllegalArgumentException> {
            riskContext(recentAttemptCount = -1)
        }

        assertEquals(
            "Recent attempt count must not be negative",
            exception.message,
        )
    }

    private fun riskContext(
        customerAveragePayment: BigDecimal = BigDecimal("75.00"),
        recentAttemptCount: Int = 0,
    ) = PaymentRiskContext(
        payment = ProposedPayment(
            customerId = UUID.fromString("1bf5b192-1bd6-4c91-8989-fffc0a05887e"),
            recipientId = UUID.fromString("d1dbd4ea-482c-4dc6-a0f1-5d42ab2a82d0"),
            amount = BigDecimal("100.00"),
            currency = Currency.getInstance("GBP"),
            reference = "Invoice 123",
            requestedAt = Instant.parse("2026-10-13T12:00:00Z"),
        ),
        recipientPreviouslyPaid = false,
        recipientTrusted = false,
        customerAveragePayment = customerAveragePayment,
        recentAttemptCount = recentAttemptCount,
    )
}