package com.tomward.membershield.assessment.risk

import com.tomward.membershield.assessment.domain.PaymentRiskContext
import com.tomward.membershield.assessment.domain.RiskReason
import com.tomward.membershield.assessment.domain.RiskReasonCode

class RepeatedAttemptsRule : RiskRule {

    override fun evaluate(context: PaymentRiskContext): RiskReason? {
        if (context.recentAttemptCount < 3) {
            return null
        }

        return RiskReason(
            code = RiskReasonCode.REPEATED_ATTEMPTS,
            message = "Several payment attempts were made within a short period.",
            scoreImpact = 15,
        )
    }
}