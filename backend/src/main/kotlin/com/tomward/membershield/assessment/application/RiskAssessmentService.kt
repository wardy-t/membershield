package com.tomward.membershield.assessment.application

import com.tomward.membershield.assessment.domain.PaymentRiskContext
import com.tomward.membershield.assessment.domain.RiskAssessment
import com.tomward.membershield.assessment.persistence.RiskAssessmentEntity
import com.tomward.membershield.assessment.persistence.RiskAssessmentRepository
import com.tomward.membershield.assessment.persistence.RiskReasonEntity
import com.tomward.membershield.assessment.risk.PaymentRiskEngine
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Clock
import java.time.Instant
import java.util.UUID

@Service
class RiskAssessmentService(
    private val paymentRiskEngine: PaymentRiskEngine,
    private val repository: RiskAssessmentRepository,
    private val clock: Clock,
) {

    @Transactional
    fun assess(context: PaymentRiskContext): RiskAssessment {
        val assessment = paymentRiskEngine.assess(context)

        repository.save(
            assessment.toEntity(
                context = context,
                assessedAt = clock.instant(),
            ),
        )

        return assessment
    }

    private fun RiskAssessment.toEntity(
        context: PaymentRiskContext,
        assessedAt: Instant,
    ): RiskAssessmentEntity {
        val payment = context.payment

        return RiskAssessmentEntity(
            id = UUID.randomUUID(),
            customerId = payment.customerId,
            recipientId = payment.recipientId,
            amount = payment.amount,
            currency = payment.currency.currencyCode,
            paymentReference = payment.reference,
            requestedAt = payment.requestedAt,
            recipientPreviouslyPaid = context.recipientPreviouslyPaid,
            recipientTrusted = context.recipientTrusted,
            customerAveragePayment = context.customerAveragePayment,
            recentAttemptCount = context.recentAttemptCount,
            customerTimeZone = context.customerTimeZone.id,
            score = score,
            riskLevel = riskLevel,
            decision = decision,
            assessedAt = assessedAt,
            reasons =
                reasons.map { reason ->
                    RiskReasonEntity(
                        code = reason.code,
                        message = reason.message,
                        scoreImpact = reason.scoreImpact,
                    )
                },
        )
    }
}