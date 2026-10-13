package com.tomward.membershield.assessment.domain

data class RiskAssessment(
    val score: Int,
    val reasons: List<RiskReason>,
) {
    init {
        require(score in 0..100) {
            "Risk score must be between 0 and 100"
        }
    }

    val riskLevel: RiskLevel = RiskLevel.fromScore(score)

    val decision: AssessmentDecision = riskLevel.decision
}

enum class RiskLevel(
    val decision: AssessmentDecision,
) {
    LOW(AssessmentDecision.ALLOW),
    MEDIUM(AssessmentDecision.SHOW_GUIDANCE),
    HIGH(AssessmentDecision.ADDITIONAL_CONFIRMATION_REQUIRED),
    CRITICAL(AssessmentDecision.PAUSE_AND_CONTACT_BANK);

    companion object {
        fun fromScore(score: Int): RiskLevel {
            require(score in 0..100) {
                "Risk score must be between 0 and 100"
            }

            return when {
                score >= 75 -> CRITICAL
                score >= 50 -> HIGH
                score >= 25 -> MEDIUM
                else -> LOW
            }
        }
    }
}

enum class AssessmentDecision {
    ALLOW,
    SHOW_GUIDANCE,
    ADDITIONAL_CONFIRMATION_REQUIRED,
    PAUSE_AND_CONTACT_BANK,
}