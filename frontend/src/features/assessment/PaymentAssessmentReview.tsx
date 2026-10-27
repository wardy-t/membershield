import { type PaymentAssessmentFormValues } from './types'

interface PaymentAssessmentReviewProps {
  values: PaymentAssessmentFormValues
  isSubmitting: boolean
  error: string | null
  onEdit: () => void
  onAssess: () => void
}

function PaymentAssessmentReview({
  values,
  isSubmitting,
  error,
  onEdit,
  onAssess,
}: PaymentAssessmentReviewProps) {
  const formatCurrency = (value: string) =>
    new Intl.NumberFormat('en-GB', {
      style: 'currency',
      currency: values.currency,
    }).format(Number(value))

  return (
    <div className="assessment-review">
      <div
        className="review-notice"
        role="status"
        aria-live="polite"
      >
        <span aria-hidden="true">✓</span>

        <div>
          <h3>Details ready for assessment</h3>
          <p>
            Review the information below before MemberShield
            assesses the payment.
          </p>
        </div>
      </div>

      <dl className="review-list">
        <div>
          <dt>Payment amount</dt>
          <dd>{formatCurrency(values.amount)}</dd>
        </div>

        <div>
          <dt>Currency</dt>
          <dd>{values.currency}</dd>
        </div>

        <div>
          <dt>Payment reference</dt>
          <dd>{values.reference}</dd>
        </div>

        <div>
          <dt>Previously paid recipient</dt>
          <dd>
            {values.recipientPreviouslyPaid === 'yes'
              ? 'Yes'
              : 'No'}
          </dd>
        </div>

        <div>
          <dt>Trusted recipient</dt>
          <dd>
            {values.recipientTrusted === 'yes' ? 'Yes' : 'No'}
          </dd>
        </div>

        <div>
          <dt>Typical payment amount</dt>
          <dd>
            {formatCurrency(values.customerAveragePayment)}
          </dd>
        </div>

        <div>
          <dt>Recent payment attempts</dt>
          <dd>{values.recentAttemptCount}</dd>
        </div>
      </dl>

      {error && (
        <div className="assessment-error" role="alert">
          <strong>Assessment unavailable</strong>
          <p>{error}</p>
        </div>
      )}

      <div className="review-actions">
        <button
          className="secondary-button"
          type="button"
          onClick={onEdit}
          disabled={isSubmitting}
        >
          Edit payment details
        </button>

        <button
          className="primary-button"
          type="button"
          onClick={onAssess}
          disabled={isSubmitting}
        >
          {isSubmitting ? 'Assessing payment…' : 'Assess payment'}
        </button>
      </div>
    </div>
  )
}

export default PaymentAssessmentReview