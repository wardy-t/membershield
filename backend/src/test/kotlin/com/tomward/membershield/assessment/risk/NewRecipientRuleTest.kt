package com.tomward.membershield.assessment.risk

import com.tomward.membershield.assessment.domain.PaymentRiskContext
import com.tomward.membershield.assessment.domain.ProposedPayment
import com.tomward.membershield.assessment.domain.RiskReasonCode
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.Instant
import java.util.Currency
import java.util.UUID

class NewRecipientRuleTest {

    private val rule = NewRecipientRule()

    @Test
    fun `returns a risk reason when the recipient is new`() {
        val result = rule.evaluate(
            riskContext(recipientPreviouslyPaid = false),
        )

        assertNotNull(result)
        assertEquals(RiskReasonCode.NEW_RECIPIENT, result?.code)
        assertEquals(
            "You have not paid this recipient before.",
            result?.message,
        )
        assertEquals(25, result?.scoreImpact)
    }

    @Test
    fun `returns no risk reason when the recipient has been paid before`() {
        val result = rule.evaluate(
            riskContext(recipientPreviouslyPaid = true),
        )

        assertNull(result)
    }

    private fun riskContext(
        recipientPreviouslyPaid: Boolean,
    ) = PaymentRiskContext(
        payment = ProposedPayment(
            customerId = UUID.fromString("1bf5b192-1bd6-4c91-8989-fffc0a05887e"),
            recipientId = UUID.fromString("d1dbd4ea-482c-4dc6-a0f1-5d42ab2a82d0"),
            amount = BigDecimal("100.00"),
            currency = Currency.getInstance("GBP"),
            reference = "Invoice 123",
            requestedAt = Instant.parse("2026-10-13T12:00:00Z"),
        ),
        recipientPreviouslyPaid = recipientPreviouslyPaid,
        recipientTrusted = false,
        customerAveragePayment = BigDecimal("75.00"),
        recentAttemptCount = 0,
    )
}