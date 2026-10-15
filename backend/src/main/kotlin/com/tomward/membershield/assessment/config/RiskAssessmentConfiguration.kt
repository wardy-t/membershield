package com.tomward.membershield.assessment.config

import com.tomward.membershield.assessment.risk.LargeFirstPaymentRule
import com.tomward.membershield.assessment.risk.NewRecipientRule
import com.tomward.membershield.assessment.risk.PaymentRiskEngine
import com.tomward.membershield.assessment.risk.RepeatedAttemptsRule
import com.tomward.membershield.assessment.risk.UnusualAmountRule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Clock

@Configuration
class RiskAssessmentConfiguration {

    @Bean
    fun paymentRiskEngine(): PaymentRiskEngine =
        PaymentRiskEngine(
            rules = listOf(
                NewRecipientRule(),
                UnusualAmountRule(),
                LargeFirstPaymentRule(),
                RepeatedAttemptsRule(),
            ),
        )
    @Bean
    fun clock(): Clock = Clock.systemUTC()
}
