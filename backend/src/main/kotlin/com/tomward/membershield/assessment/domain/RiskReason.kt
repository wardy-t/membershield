package com.tomward.membershield.assessment.domain

data class RiskReason(
    val code: RiskReasonCode,
    val message: String,
    val scoreImpact: Int,
)

enum class RiskReasonCode {
    NEW_RECIPIENT,
    UNUSUAL_AMOUNT,
    LARGE_FIRST_PAYMENT,
    REPEATED_ATTEMPTS,
}