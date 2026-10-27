import { type RiskAssessmentResponse } from './types'

interface RiskAssessmentResultProps {
  assessment: RiskAssessmentResponse
  onStartAgain: () => void
}

const riskLevelLabels = {
  LOW: 'Low risk',
  MEDIUM: 'Medium risk',
  HIGH: 'High risk',
  CRITICAL: 'Critical risk',
}

const decisionContent = {
  ALLOW: {
    heading: 'No significant warning signs identified',
    message:
      'MemberShield has not identified circumstances that require additional action.',
  },
  SHOW_GUIDANCE: {
    heading: 'Review the payment before continuing',
    message:
      'Some circumstances deserve additional consideration before you make this payment.',
  },
  ADDITIONAL_CONFIRMATION_REQUIRED: {
    heading: 'Complete an additional check',
    message:
      'Several warning signs were identified. Confirm the payment details and recipient before continuing.',
  },
  PAUSE_AND_CONTACT_BANK: {
    heading: 'Pause this payment',
    message:
      'Multiple warning signs were identified. Do not continue until you have independently verified the payment.',
  },
}

function RiskAssessmentResult({
  assessment,
  onStartAgain,
}: RiskAssessmentResultProps) {
  const decision = decisionContent[assessment.decision]

  return (
    <div className="assessment-result" aria-live="polite">
      <div
        className={`result-summary result-summary--${assessment.riskLevel.toLowerCase()}`}
      >
        <p className="result-level">
          {riskLevelLabels[assessment.riskLevel]}
        </p>

        <p className="result-score">
          <strong>{assessment.score}</strong>
          <span>out of 100</span>
        </p>

        <h3>{decision.heading}</h3>
        <p>{decision.message}</p>
      </div>

      <section
        className="result-reasons"
        aria-labelledby="risk-reasons-title"
      >
        <h3 id="risk-reasons-title">
          Why MemberShield reached this result
        </h3>

        {assessment.reasons.length > 0 ? (
          <ul>
            {assessment.reasons.map((reason) => (
              <li key={reason.code}>
                <div>
                  <p>{reason.message}</p>
                  <span>{reason.code.replaceAll('_', ' ')}</span>
                </div>

                <strong>+{reason.scoreImpact}</strong>
              </li>
            ))}
          </ul>
        ) : (
          <p>
            No payment-risk signals were identified from the
            information provided.
          </p>
        )}
      </section>

      <button
        className="primary-button"
        type="button"
        onClick={onStartAgain}
      >
        Assess another payment
      </button>
    </div>
  )
}

export default RiskAssessmentResult