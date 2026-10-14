package com.tomward.membershield.assessment.api

import com.tomward.membershield.assessment.risk.PaymentRiskEngine
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/risk-assessments")
class RiskAssessmentController(
    private val paymentRiskEngine: PaymentRiskEngine,
) {

    @PostMapping
    fun assessPayment(
        @RequestBody request: RiskAssessmentRequest,
    ): RiskAssessmentResponse {
        val assessment = paymentRiskEngine.assess(request.toDomain())

        return RiskAssessmentResponse.fromDomain(assessment)
    }
}