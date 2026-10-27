import {
  type PaymentAssessmentFormValues,
  type PaymentAssessmentRequest,
  type RiskAssessmentResponse,
} from './types'

const DEMO_CUSTOMER_ID = '2c7dc8e6-bc71-4aa8-a890-1d6d19f1b12e'
const DEMO_RECIPIENT_ID = '821746c0-ae8c-42dc-9e1a-bc70fc785bcc'

export function createAssessmentRequest(
  values: PaymentAssessmentFormValues,
): PaymentAssessmentRequest {
  return {
    customerId: DEMO_CUSTOMER_ID,
    recipientId: DEMO_RECIPIENT_ID,
    amount: Number(values.amount),
    currency: values.currency,
    reference: values.reference.trim(),
    requestedAt: new Date().toISOString(),
    recipientPreviouslyPaid:
      values.recipientPreviouslyPaid === 'yes',
    recipientTrusted: values.recipientTrusted === 'yes',
    customerAveragePayment: Number(values.customerAveragePayment),
    recentAttemptCount: Number(values.recentAttemptCount),
    customerTimeZone:
      Intl.DateTimeFormat().resolvedOptions().timeZone ||
      'Europe/London',
  }
}

export async function assessPayment(
  values: PaymentAssessmentFormValues,
): Promise<RiskAssessmentResponse> {
  const response = await fetch('/api/v1/risk-assessments', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(createAssessmentRequest(values)),
  })

  if (!response.ok) {
    throw new Error(
      'MemberShield could not assess this payment. Please try again.',
    )
  }

  return (await response.json()) as RiskAssessmentResponse
}