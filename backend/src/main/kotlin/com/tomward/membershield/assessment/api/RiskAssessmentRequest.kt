package com.tomward.membershield.assessment.api

import com.tomward.membershield.assessment.domain.PaymentRiskContext
import com.tomward.membershield.assessment.domain.ProposedPayment
import java.math.BigDecimal
import java.time.Instant
import java.time.ZoneId
import java.util.Currency
import java.util.UUID

data class RiskAssessmentRequest(
    val customerId: UUID,
    val recipientId: UUID,
    val amount: BigDecimal,
    val currency: String,
    val reference: String,
    val requestedAt: Instant,
    val recipientPreviouslyPaid: Boolean,
    val recipientTrusted: Boolean,
    val customerAveragePayment: BigDecimal,
    val recentAttemptCount: Int,
    val customerTimeZone: String,
) {
    fun toDomain(): PaymentRiskContext =
        PaymentRiskContext(
            payment = ProposedPayment(
                customerId = customerId,
                recipientId = recipientId,
                amount = amount,
                currency = Currency.getInstance(currency.uppercase()),
                reference = reference,
                requestedAt = requestedAt,
            ),
            recipientPreviouslyPaid = recipientPreviouslyPaid,
            recipientTrusted = recipientTrusted,
            customerAveragePayment = customerAveragePayment,
            recentAttemptCount = recentAttemptCount,
            customerTimeZone = ZoneId.of(customerTimeZone),
        )
}