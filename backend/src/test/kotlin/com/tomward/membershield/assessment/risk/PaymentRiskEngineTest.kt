package com.tomward.membershield.assessment.risk

import com.tomward.membershield.assessment.domain.AssessmentDecision
import com.tomward.membershield.assessment.domain.PaymentRiskContext
import com.tomward.membershield.assessment.domain.ProposedPayment
import com.tomward.membershield.assessment.domain.RiskLevel
import com.tomward.membershield.assessment.domain.RiskReason
import com.tomward.membershield.assessment.domain.RiskReasonCode
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.Instant
import java.util.Currency
import java.util.UUID
import java.time.ZoneId

class PaymentRiskEngineTest {

    @Test
    fun `returns a low-risk assessment when no rules trigger`() {
        val engine = PaymentRiskEngine(rules = emptyList())

        val assessment = engine.assess(riskContext())

        assertEquals(0, assessment.score)
        assertEquals(RiskLevel.LOW, assessment.riskLevel)
        assertEquals(AssessmentDecision.ALLOW, assessment.decision)
        assertTrue(assessment.reasons.isEmpty())
    }

    @Test
    fun `collects triggered reasons and calculates the assessment`() {
        val engine = PaymentRiskEngine(
            rules = listOf(NewRecipientRule()),
        )

        val assessment = engine.assess(
            riskContext(recipientPreviouslyPaid = false),
        )

        assertEquals(25, assessment.score)
        assertEquals(RiskLevel.MEDIUM, assessment.riskLevel)
        assertEquals(
            AssessmentDecision.SHOW_GUIDANCE,
            assessment.decision,
        )
        assertEquals(
            listOf(RiskReasonCode.NEW_RECIPIENT),
            assessment.reasons.map { reason -> reason.code },
        )
    }

    @Test
    fun `caps a combined score at one hundred`() {
        val engine = PaymentRiskEngine(
            rules = listOf(
                FixedRiskRule(scoreImpact = 70),
                FixedRiskRule(scoreImpact = 60),
            ),
        )

        val assessment = engine.assess(riskContext())

        assertEquals(100, assessment.score)
        assertEquals(RiskLevel.CRITICAL, assessment.riskLevel)
        assertEquals(
            AssessmentDecision.PAUSE_AND_CONTACT_BANK,
            assessment.decision,
        )
    }

    @Test
    fun `prevents a combined score from falling below zero`() {
        val engine = PaymentRiskEngine(
            rules = listOf(
                FixedRiskRule(scoreImpact = -30),
            ),
        )

        val assessment = engine.assess(riskContext())

        assertEquals(0, assessment.score)
        assertEquals(RiskLevel.LOW, assessment.riskLevel)
        assertEquals(AssessmentDecision.ALLOW, assessment.decision)
    }

    @Test
    fun `combines multiple triggered rules into one assessment`() {
        val engine = PaymentRiskEngine(
            rules = listOf(
                NewRecipientRule(),
                UnusualAmountRule(),
                LargeFirstPaymentRule(),
            ),
        )

        val assessment = engine.assess(
            riskContext(
                recipientPreviouslyPaid = false,
                amount = BigDecimal("1000.00"),
                customerAveragePayment = BigDecimal("100.00"),
            ),
        )

        assertEquals(65, assessment.score)
        assertEquals(RiskLevel.HIGH, assessment.riskLevel)
        assertEquals(
            AssessmentDecision.ADDITIONAL_CONFIRMATION_REQUIRED,
            assessment.decision,
        )
        assertEquals(
            setOf(
                RiskReasonCode.NEW_RECIPIENT,
                RiskReasonCode.UNUSUAL_AMOUNT,
                RiskReasonCode.LARGE_FIRST_PAYMENT,
            ),
            assessment.reasons
                .map { reason -> reason.code }
                .toSet(),
        )
    }

    private class FixedRiskRule(
        private val scoreImpact: Int,
    ) : RiskRule {

        override fun evaluate(
            context: PaymentRiskContext,
        ): RiskReason {
            return RiskReason(
                code = RiskReasonCode.NEW_RECIPIENT,
                message = "Test risk reason",
                scoreImpact = scoreImpact,
            )
        }
    }

    private fun riskContext(
        recipientPreviouslyPaid: Boolean = true,
        amount: BigDecimal = BigDecimal("100.00"),
        customerAveragePayment: BigDecimal = BigDecimal("75.00"),
    ) = PaymentRiskContext(
        payment = ProposedPayment(
            customerId = UUID.fromString(
                "1bf5b192-1bd6-4c91-8989-fffc0a05887e",
            ),
            recipientId = UUID.fromString(
                "d1dbd4ea-482c-4dc6-a0f1-5d42ab2a82d0",
            ),
            amount = amount,
            currency = Currency.getInstance("GBP"),
            reference = "Invoice 123",
            requestedAt = Instant.parse("2026-10-13T12:00:00Z"),
        ),
        recipientPreviouslyPaid = recipientPreviouslyPaid,
        recipientTrusted = false,
        customerAveragePayment = customerAveragePayment,
        recentAttemptCount = 0,
        customerTimeZone = ZoneId.of("Europe/London"),
    )
}