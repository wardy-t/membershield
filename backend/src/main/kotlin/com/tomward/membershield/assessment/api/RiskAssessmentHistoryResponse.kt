package com.tomward.membershield.assessment.api

import com.tomward.membershield.assessment.domain.AssessmentDecision
import com.tomward.membershield.assessment.domain.RiskLevel
import com.tomward.membershield.assessment.persistence.RiskAssessmentEntity
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

data class RiskAssessmentHistoryResponse(
    val count: Int,
    val assessments: List<RiskAssessmentHistoryItemResponse>,
) {
    companion object {
        fun fromEntities(
            entities: List<RiskAssessmentEntity>,
        ): RiskAssessmentHistoryResponse =
            RiskAssessmentHistoryResponse(
                count = entities.size,
                assessments =
                    entities.map(
                        RiskAssessmentHistoryItemResponse::fromEntity,
                    ),
            )
    }
}

data class RiskAssessmentHistoryItemResponse(
    val id: UUID,
    val recipientId: UUID,
    val amount: BigDecimal,
    val currency: String,
    val paymentReference: String,
    val requestedAt: Instant,
    val score: Int,
    val riskLevel: RiskLevel,
    val decision: AssessmentDecision,
    val assessedAt: Instant,
    val reasons: List<RiskReasonResponse>,
) {
    companion object {
        fun fromEntity(
            entity: RiskAssessmentEntity,
        ): RiskAssessmentHistoryItemResponse =
            RiskAssessmentHistoryItemResponse(
                id = entity.id,
                recipientId = entity.recipientId,
                amount = entity.amount,
                currency = entity.currency,
                paymentReference = entity.paymentReference,
                requestedAt = entity.requestedAt,
                score = entity.score,
                riskLevel = entity.riskLevel,
                decision = entity.decision,
                assessedAt = entity.assessedAt,
                reasons =
                    entity.reasons.map { reason ->
                        RiskReasonResponse(
                            code = reason.code,
                            message = reason.message,
                            scoreImpact = reason.scoreImpact,
                        )
                    },
            )
    }
}