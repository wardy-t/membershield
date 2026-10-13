package com.tomward.membershield.assessment.risk

import com.tomward.membershield.assessment.domain.PaymentRiskContext
import com.tomward.membershield.assessment.domain.RiskAssessment

class PaymentRiskEngine(
    private val rules: List<RiskRule>,
) {

    fun assess(context: PaymentRiskContext): RiskAssessment {
        val reasons = rules.mapNotNull { rule ->
            rule.evaluate(context)
        }

        val score = reasons
            .sumOf { reason -> reason.scoreImpact }
            .coerceIn(0, 100)

        return RiskAssessment(
            score = score,
            reasons = reasons,
        )
    }
}