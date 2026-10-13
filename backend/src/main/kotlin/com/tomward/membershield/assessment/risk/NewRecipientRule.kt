package com.tomward.membershield.assessment.risk

import com.tomward.membershield.assessment.domain.PaymentRiskContext
import com.tomward.membershield.assessment.domain.RiskReason
import com.tomward.membershield.assessment.domain.RiskReasonCode

class NewRecipientRule : RiskRule {

    override fun evaluate(context: PaymentRiskContext): RiskReason? {
        if (context.recipientPreviouslyPaid) {
            return null
        }

        return RiskReason(
            code = RiskReasonCode.NEW_RECIPIENT,
            message = "You have not paid this recipient before.",
            scoreImpact = 25,
        )
    }
}