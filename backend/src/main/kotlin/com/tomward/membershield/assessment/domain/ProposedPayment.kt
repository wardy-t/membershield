package com.tomward.membershield.assessment.domain

import java.math.BigDecimal
import java.time.Instant
import java.util.Currency
import java.util.UUID

data class ProposedPayment(
    val customerId: UUID,
    val recipientId: UUID,
    val amount: BigDecimal,
    val currency: Currency,
    val reference: String,
    val requestedAt: Instant,
) {
    init {
        require(amount > BigDecimal.ZERO) {
            "Payment amount must be greater than zero"
        }

        require(reference.isNotBlank()) {
            "Payment reference must not be blank"
        }

        require(reference.length <= 140) {
            "Payment reference must not exceed 140 characters"
        }
    }
}