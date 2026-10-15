package com.tomward.membershield.assessment.persistence

import com.tomward.membershield.assessment.domain.AssessmentDecision
import com.tomward.membershield.assessment.domain.RiskLevel
import com.tomward.membershield.assessment.domain.RiskReasonCode
import jakarta.persistence.CollectionTable
import jakarta.persistence.Column
import jakarta.persistence.ElementCollection
import jakarta.persistence.Embeddable
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OrderColumn
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "risk_assessments")
class RiskAssessmentEntity(
    @Id
    val id: UUID,

    @Column(name = "customer_id", nullable = false)
    val customerId: UUID,

    @Column(name = "recipient_id", nullable = false)
    val recipientId: UUID,

    @Column(nullable = false, precision = 19, scale = 2)
    val amount: BigDecimal,

    @Column(nullable = false, length = 3)
    val currency: String,

    @Column(name = "payment_reference", nullable = false, length = 140)
    val paymentReference: String,

    @Column(name = "requested_at", nullable = false)
    val requestedAt: Instant,

    @Column(name = "recipient_previously_paid", nullable = false)
    val recipientPreviouslyPaid: Boolean,

    @Column(name = "recipient_trusted", nullable = false)
    val recipientTrusted: Boolean,

    @Column(
        name = "customer_average_payment",
        nullable = false,
        precision = 19,
        scale = 2,
    )
    val customerAveragePayment: BigDecimal,

    @Column(name = "recent_attempt_count", nullable = false)
    val recentAttemptCount: Int,

    @Column(name = "customer_time_zone", nullable = false, length = 50)
    val customerTimeZone: String,

    @Column(nullable = false)
    val score: Int,

    @Enumerated(EnumType.STRING)
    @Column(name = "risk_level", nullable = false, length = 20)
    val riskLevel: RiskLevel,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    val decision: AssessmentDecision,

    @Column(name = "assessed_at", nullable = false)
    val assessedAt: Instant,

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "risk_assessment_reasons",
        joinColumns = [JoinColumn(name = "assessment_id")],
    )
    @OrderColumn(name = "reason_order")
    val reasons: List<RiskReasonEntity>,
)

@Embeddable
class RiskReasonEntity(
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    val code: RiskReasonCode,

    @Column(nullable = false, length = 255)
    val message: String,

    @Column(name = "score_impact", nullable = false)
    val scoreImpact: Int,
)