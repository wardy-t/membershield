package com.tomward.membershield.assessment.risk

import com.tomward.membershield.assessment.domain.PaymentRiskContext
import com.tomward.membershield.assessment.domain.RiskReason
import com.tomward.membershield.assessment.domain.RiskReasonCode
import java.math.BigDecimal
import java.util.Currency

class LargeFirstPaymentRule : RiskRule {

    override fun evaluate(context: PaymentRiskContext): RiskReason? {
        val payment = context.payment
        val poundsSterling = Currency.getInstance("GBP")

        if (payment.currency != poundsSterling) {
            return null
        }

        if (context.recipientPreviouslyPaid) {
            return null
        }

        if (payment.amount < BigDecimal("1000.00")) {
            return null
        }

        return RiskReason(
            code = RiskReasonCode.LARGE_FIRST_PAYMENT,
            message = "This is a high-value first payment to this recipient.",
            scoreImpact = 15,
        )
    }
}