package com.tomward.membershield.assessment.api

import com.tomward.membershield.assessment.application.RiskAssessmentService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/risk-assessments")
class RiskAssessmentController(
    private val riskAssessmentService: RiskAssessmentService,
) {

    @PostMapping
    fun assessPayment(
        @RequestBody request: RiskAssessmentRequest,
    ): RiskAssessmentResponse {
        val assessment =
            riskAssessmentService.assess(request.toDomain())

        return RiskAssessmentResponse.fromDomain(assessment)
    }
}