import { type PaymentAssessmentFormValues } from './types'

interface PaymentAssessmentReviewProps {
  values: PaymentAssessmentFormValues
  onEdit: () => void
}

function PaymentAssessmentReview({
  values,
  onEdit,
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

      <div className="review-actions">
        <button
          className="secondary-button"
          type="button"
          onClick={onEdit}
        >
          Edit payment details
        </button>

        <p>
          The assessment action will be connected to the secure API
          in the next development slice.
        </p>
      </div>
    </div>
  )
}

export default PaymentAssessmentReview