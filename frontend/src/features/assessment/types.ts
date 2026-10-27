export type CurrencyCode = 'GBP'

export type YesNoAnswer = '' | 'yes' | 'no'

export type RiskLevel = 'LOW' | 'MEDIUM' | 'HIGH' | 'CRITICAL'

export type AssessmentDecision =
  | 'ALLOW'
  | 'SHOW_GUIDANCE'
  | 'ADDITIONAL_CONFIRMATION_REQUIRED'
  | 'PAUSE_AND_CONTACT_BANK'

export type RiskReasonCode =
  | 'NEW_RECIPIENT'
  | 'UNUSUAL_AMOUNT'
  | 'LARGE_FIRST_PAYMENT'
  | 'REPEATED_ATTEMPTS'

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

export interface RiskReason {
  code: RiskReasonCode
  message: string
  scoreImpact: number
}

export interface RiskAssessmentResponse {
  score: number
  riskLevel: RiskLevel
  decision: AssessmentDecision
  reasons: RiskReason[]
}