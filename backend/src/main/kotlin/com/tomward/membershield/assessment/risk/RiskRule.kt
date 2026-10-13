package com.tomward.membershield.assessment.risk

import com.tomward.membershield.assessment.domain.PaymentRiskContext
import com.tomward.membershield.assessment.domain.RiskReason

interface RiskRule {

    fun evaluate(context: PaymentRiskContext): RiskReason?
}