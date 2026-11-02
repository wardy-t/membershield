import {
  type AssessmentDecision,
  type RiskAssessmentHistoryItem,
} from './types'

interface AssessmentHistoryProps {
  assessments: RiskAssessmentHistoryItem[]
  isLoading: boolean
  error: string | null
  onRefresh: () => void
}

const decisionLabels: Record<AssessmentDecision, string> = {
  ALLOW: 'Payment allowed',
  SHOW_GUIDANCE: 'Review guidance',
  ADDITIONAL_CONFIRMATION_REQUIRED:
    'Additional confirmation required',
  PAUSE_AND_CONTACT_BANK: 'Pause and contact the bank',
}

function AssessmentHistory({
  assessments,
  isLoading,
  error,
  onRefresh,
}: AssessmentHistoryProps) {
  const formatCurrency = (
    amount: number,
    currency: string,
  ) =>
    new Intl.NumberFormat('en-GB', {
      style: 'currency',
      currency,
    }).format(amount)

  const formatDate = (value: string) =>
    new Intl.DateTimeFormat('en-GB', {
      dateStyle: 'medium',
      timeStyle: 'short',
    }).format(new Date(value))

  return (
    <section
      className="assessment-history"
      aria-labelledby="assessment-history-title"
    >
      <div className="history-heading">
        <div>
          <p className="section-label">Audit history</p>
          <h2 id="assessment-history-title">
            Recent assessments
          </h2>
          <p>
            Review the latest payment checks recorded for this demo
            customer.
          </p>
        </div>

        <button
          className="secondary-button history-refresh"
          type="button"
          onClick={onRefresh}
          disabled={isLoading}
        >
          {isLoading ? 'Refreshing…' : 'Refresh'}
        </button>
      </div>

      {error && (
        <div className="assessment-error" role="alert">
          <strong>History unavailable</strong>
          <p>{error}</p>
        </div>
      )}

      {isLoading && assessments.length === 0 ? (
        <p className="history-status" role="status">
          Loading recent assessments…
        </p>
      ) : assessments.length === 0 ? (
        <div className="history-empty">
          <h3>No assessments recorded yet</h3>
          <p>
            Completed payment checks will appear here after they
            have been assessed and stored.
          </p>
        </div>
      ) : (
        <div className="history-list">
          {assessments.map((assessment) => (
            <details
              className="history-item"
              key={assessment.id}
            >
              <summary>
                <div className="history-payment">
                  <strong>
                    {formatCurrency(
                      assessment.amount,
                      assessment.currency,
                    )}
                  </strong>
                  <span>{assessment.paymentReference}</span>
                </div>

                <div className="history-outcome">
                  <span
                    className={`risk-badge risk-badge--${assessment.riskLevel.toLowerCase()}`}
                  >
                    {assessment.riskLevel.toLowerCase()} risk
                  </span>
                  <strong>{assessment.score}/100</strong>
                </div>
              </summary>

              <div className="history-details">
                <dl>
                  <div>
                    <dt>Assessed</dt>
                    <dd>{formatDate(assessment.assessedAt)}</dd>
                  </div>
                  <div>
                    <dt>Decision</dt>
                    <dd>
                      {decisionLabels[assessment.decision]}
                    </dd>
                  </div>
                </dl>

                <div className="history-reasons">
                  <h3>Contributing signals</h3>

                  {assessment.reasons.length > 0 ? (
                    <ul>
                      {assessment.reasons.map((reason) => (
                        <li key={reason.code}>
                          <span>{reason.message}</span>
                          <strong>+{reason.scoreImpact}</strong>
                        </li>
                      ))}
                    </ul>
                  ) : (
                    <p>No risk signals were identified.</p>
                  )}
                </div>
              </div>
            </details>
          ))}
        </div>
      )}
    </section>
  )
}

export default AssessmentHistory