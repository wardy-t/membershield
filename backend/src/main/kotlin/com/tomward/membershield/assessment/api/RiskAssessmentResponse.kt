package com.tomward.membershield.assessment.api

import com.tomward.membershield.assessment.domain.AssessmentDecision
import com.tomward.membershield.assessment.domain.RiskAssessment
import com.tomward.membershield.assessment.domain.RiskLevel
import com.tomward.membershield.assessment.domain.RiskReason
import com.tomward.membershield.assessment.domain.RiskReasonCode

data class RiskAssessmentResponse(
    val score: Int,
    val riskLevel: RiskLevel,
    val decision: AssessmentDecision,
    val reasons: List<RiskReasonResponse>,
) {
    companion object {
        fun fromDomain(assessment: RiskAssessment): RiskAssessmentResponse =
            RiskAssessmentResponse(
                score = assessment.score,
                riskLevel = assessment.riskLevel,
                decision = assessment.decision,
                reasons = assessment.reasons.map(RiskReasonResponse::fromDomain),
            )
    }
}

data class RiskReasonResponse(
    val code: RiskReasonCode,
    val message: String,
    val scoreImpact: Int,
) {
    companion object {
        fun fromDomain(reason: RiskReason): RiskReasonResponse =
            RiskReasonResponse(
                code = reason.code,
                message = reason.message,
                scoreImpact = reason.scoreImpact,
            )
    }
}