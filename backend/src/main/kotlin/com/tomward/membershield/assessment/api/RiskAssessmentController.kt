package com.tomward.membershield.assessment.api

import com.tomward.membershield.assessment.application.RiskAssessmentService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

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

    @GetMapping
    fun getAssessmentHistory(
        @RequestParam customerId: UUID,
        @RequestParam(defaultValue = "10") limit: Int,
    ): RiskAssessmentHistoryResponse {
        val assessments =
            riskAssessmentService.findHistory(
                customerId = customerId,
                limit = limit,
            )

        return RiskAssessmentHistoryResponse.fromEntities(assessments)
    }
}