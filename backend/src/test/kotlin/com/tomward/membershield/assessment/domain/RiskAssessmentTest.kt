package com.tomward.membershield.assessment.domain

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class RiskAssessmentTest {

    @Test
    fun `maps score boundaries to the correct levels and decisions`() {
        val cases = listOf(
            TestCase(0, RiskLevel.LOW, AssessmentDecision.ALLOW),
            TestCase(24, RiskLevel.LOW, AssessmentDecision.ALLOW),
            TestCase(25, RiskLevel.MEDIUM, AssessmentDecision.SHOW_GUIDANCE),
            TestCase(49, RiskLevel.MEDIUM, AssessmentDecision.SHOW_GUIDANCE),
            TestCase(
                50,
                RiskLevel.HIGH,
                AssessmentDecision.ADDITIONAL_CONFIRMATION_REQUIRED,
            ),
            TestCase(
                74,
                RiskLevel.HIGH,
                AssessmentDecision.ADDITIONAL_CONFIRMATION_REQUIRED,
            ),
            TestCase(
                75,
                RiskLevel.CRITICAL,
                AssessmentDecision.PAUSE_AND_CONTACT_BANK,
            ),
            TestCase(
                100,
                RiskLevel.CRITICAL,
                AssessmentDecision.PAUSE_AND_CONTACT_BANK,
            ),
        )

        cases.forEach { case ->
            val assessment = RiskAssessment(
                score = case.score,
                reasons = emptyList(),
            )

            assertEquals(
                case.expectedLevel,
                assessment.riskLevel,
                "Unexpected risk level for score ${case.score}",
            )
            assertEquals(
                case.expectedDecision,
                assessment.decision,
                "Unexpected decision for score ${case.score}",
            )
        }
    }

    @Test
    fun `rejects a score below zero`() {
        val exception = assertThrows<IllegalArgumentException> {
            RiskAssessment(
                score = -1,
                reasons = emptyList(),
            )
        }

        assertEquals(
            "Risk score must be between 0 and 100",
            exception.message,
        )
    }

    @Test
    fun `rejects a score above one hundred`() {
        val exception = assertThrows<IllegalArgumentException> {
            RiskAssessment(
                score = 101,
                reasons = emptyList(),
            )
        }

        assertEquals(
            "Risk score must be between 0 and 100",
            exception.message,
        )
    }

    private data class TestCase(
        val score: Int,
        val expectedLevel: RiskLevel,
        val expectedDecision: AssessmentDecision,
    )
}