package com.tomward.membershield.assessment.risk

import com.tomward.membershield.assessment.domain.PaymentRiskContext
import com.tomward.membershield.assessment.domain.RiskReason
import com.tomward.membershield.assessment.domain.RiskReasonCode
import java.math.BigDecimal

class UnusualAmountRule : RiskRule {

    override fun evaluate(context: PaymentRiskContext): RiskReason? {
        if (context.customerAveragePayment == BigDecimal.ZERO) {
            return null
        }

        val unusualAmountThreshold =
            context.customerAveragePayment.multiply(BigDecimal("3"))

        if (context.payment.amount <= unusualAmountThreshold) {
            return null
        }

        return RiskReason(
            code = RiskReasonCode.UNUSUAL_AMOUNT,
            message = "This amount is significantly higher than your recent payments.",
            scoreImpact = 25,
        )
    }
}