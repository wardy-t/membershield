package com.tomward.membershield.assessment.domain

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal
import java.time.Instant
import java.util.Currency
import java.util.UUID

class ProposedPaymentTest {

    @Test
    fun `creates a valid proposed payment`() {
        val payment = proposedPayment()

        assertEquals(BigDecimal("100.00"), payment.amount)
        assertEquals(Currency.getInstance("GBP"), payment.currency)
        assertEquals("Invoice 123", payment.reference)
    }

    @Test
    fun `rejects a zero payment amount`() {
        val exception = assertThrows<IllegalArgumentException> {
            proposedPayment(amount = BigDecimal.ZERO)
        }

        assertEquals(
            "Payment amount must be greater than zero",
            exception.message,
        )
    }

    @Test
    fun `rejects a blank payment reference`() {
        val exception = assertThrows<IllegalArgumentException> {
            proposedPayment(reference = "   ")
        }

        assertEquals(
            "Payment reference must not be blank",
            exception.message,
        )
    }

    @Test
    fun `rejects a payment reference longer than 140 characters`() {
        val exception = assertThrows<IllegalArgumentException> {
            proposedPayment(reference = "a".repeat(141))
        }

        assertEquals(
            "Payment reference must not exceed 140 characters",
            exception.message,
        )
    }

    private fun proposedPayment(
        amount: BigDecimal = BigDecimal("100.00"),
        reference: String = "Invoice 123",
    ) = ProposedPayment(
        customerId = UUID.fromString("1bf5b192-1bd6-4c91-8989-fffc0a05887e"),
        recipientId = UUID.fromString("d1dbd4ea-482c-4dc6-a0f1-5d42ab2a82d0"),
        amount = amount,
        currency = Currency.getInstance("GBP"),
        reference = reference,
        requestedAt = Instant.parse("2026-10-13T12:00:00Z"),
    )
}
