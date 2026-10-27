export type CurrencyCode = 'GBP'

export type YesNoAnswer = '' | 'yes' | 'no'

export interface PaymentAssessmentFormValues {
  amount: string
  currency: CurrencyCode
  reference: string
  recipientPreviouslyPaid: YesNoAnswer
  recipientTrusted: YesNoAnswer
  customerAveragePayment: string
  recentAttemptCount: string
}

export interface PaymentAssessmentRequest {
  customerId: string
  recipientId: string
  amount: number
  currency: CurrencyCode
  reference: string
  requestedAt: string
  recipientPreviouslyPaid: boolean
  recipientTrusted: boolean
  customerAveragePayment: number
  recentAttemptCount: number
  customerTimeZone: string
}