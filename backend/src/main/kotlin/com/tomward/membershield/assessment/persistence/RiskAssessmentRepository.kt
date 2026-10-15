package com.tomward.membershield.assessment.persistence

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface RiskAssessmentRepository :
    JpaRepository<RiskAssessmentEntity, UUID> {

    fun findAllByCustomerIdOrderByAssessedAtDesc(
        customerId: UUID,
    ): List<RiskAssessmentEntity>
}