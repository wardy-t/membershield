package com.tomward.membershield.assessment.domain

import java.math.BigDecimal

data class PaymentRiskContext(
    val payment: ProposedPayment,
    val recipientPreviouslyPaid: Boolean,
    val recipientTrusted: Boolean,
    val customerAveragePayment: BigDecimal,
    val recentAttemptCount: Int,
) {
    init {
        require(customerAveragePayment >= BigDecimal.ZERO) {
            "Customer average payment must not be negative"
        }

        require(recentAttemptCount >= 0) {
            "Recent attempt count must not be negative"
        }
    }
}