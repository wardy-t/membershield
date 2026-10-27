import { type FormEvent, useState } from 'react'
import {
  type PaymentAssessmentFormValues,
  type YesNoAnswer,
} from './types'

interface PaymentAssessmentFormProps {
  onSubmit: (values: PaymentAssessmentFormValues) => void
}

const initialValues: PaymentAssessmentFormValues = {
  amount: '',
  currency: 'GBP',
  reference: '',
  recipientPreviouslyPaid: '',
  recipientTrusted: '',
  customerAveragePayment: '',
  recentAttemptCount: '0',
}

function PaymentAssessmentForm({
  onSubmit,
}: PaymentAssessmentFormProps) {
  const [values, setValues] =
    useState<PaymentAssessmentFormValues>(initialValues)

  function updateField<Field extends keyof PaymentAssessmentFormValues>(
    field: Field,
    value: PaymentAssessmentFormValues[Field],
  ) {
    setValues((currentValues) => ({
      ...currentValues,
      [field]: value,
    }))
  }

  function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()
    onSubmit(values)
  }

  return (
    <form className="assessment-form" onSubmit={handleSubmit}>
      <fieldset className="form-section">
        <legend>Payment details</legend>

        <div className="form-grid form-grid--amount">
          <div className="form-field">
            <label htmlFor="amount">Payment amount</label>
            <p className="field-hint" id="amount-hint">
              Enter the total amount you intend to send.
            </p>

            <div className="money-input">
              <span aria-hidden="true">£</span>
              <input
                id="amount"
                name="amount"
                type="number"
                min="0.01"
                step="0.01"
                inputMode="decimal"
                aria-describedby="amount-hint"
                value={values.amount}
                onChange={(event) =>
                  updateField('amount', event.target.value)
                }
                required
              />
            </div>
          </div>

          <div className="form-field">
            <label htmlFor="currency">Currency</label>
            <p className="field-hint" id="currency-hint">
              MemberShield currently assesses UK payments.
            </p>

            <select
            id="currency"
            name="currency"
            aria-describedby="currency-hint"
            value={values.currency}
            onChange={() => updateField('currency', 'GBP')}
            >
            <option value="GBP">GBP — Pound sterling</option>
            </select>
          </div>
        </div>

        <div className="form-field">
          <label htmlFor="reference">Payment reference</label>
          <p className="field-hint" id="reference-hint">
            Use the reference that will appear on the payment.
          </p>

          <input
            id="reference"
            name="reference"
            type="text"
            maxLength={140}
            aria-describedby="reference-hint"
            value={values.reference}
            onChange={(event) =>
              updateField('reference', event.target.value)
            }
            required
          />
        </div>
      </fieldset>

      <fieldset className="form-section">
        <legend>Recipient history</legend>

        <YesNoField
          name="recipientPreviouslyPaid"
          legend="Have you paid this recipient before?"
          hint="A first payment may need additional care."
          value={values.recipientPreviouslyPaid}
          onChange={(answer) =>
            updateField('recipientPreviouslyPaid', answer)
          }
        />

        <YesNoField
          name="recipientTrusted"
          legend="Is this recipient saved as trusted?"
          hint="Only answer yes if you previously verified them."
          value={values.recipientTrusted}
          onChange={(answer) =>
            updateField('recipientTrusted', answer)
          }
        />
      </fieldset>

      <fieldset className="form-section">
        <legend>Recent account activity</legend>

        <div className="form-grid">
          <div className="form-field">
            <label htmlFor="customerAveragePayment">
              Typical payment amount
            </label>
            <p
              className="field-hint"
              id="customer-average-hint"
            >
              Your approximate average recent payment.
            </p>

            <div className="money-input">
              <span aria-hidden="true">£</span>
              <input
                id="customerAveragePayment"
                name="customerAveragePayment"
                type="number"
                min="0"
                step="0.01"
                inputMode="decimal"
                aria-describedby="customer-average-hint"
                value={values.customerAveragePayment}
                onChange={(event) =>
                  updateField(
                    'customerAveragePayment',
                    event.target.value,
                  )
                }
                required
              />
            </div>
          </div>

          <div className="form-field">
            <label htmlFor="recentAttemptCount">
              Recent payment attempts
            </label>
            <p
              className="field-hint"
              id="recent-attempts-hint"
            >
              Attempts made to this recipient recently.
            </p>

            <input
              id="recentAttemptCount"
              name="recentAttemptCount"
              type="number"
              min="0"
              step="1"
              inputMode="numeric"
              aria-describedby="recent-attempts-hint"
              value={values.recentAttemptCount}
              onChange={(event) =>
                updateField(
                  'recentAttemptCount',
                  event.target.value,
                )
              }
              required
            />
          </div>
        </div>
      </fieldset>

      <div className="form-actions">
        <p>
          You can review these details before requesting an
          assessment.
        </p>

        <button className="primary-button" type="submit">
          Continue to review
        </button>
      </div>
    </form>
  )
}

interface YesNoFieldProps {
  name: string
  legend: string
  hint: string
  value: YesNoAnswer
  onChange: (answer: YesNoAnswer) => void
}

function YesNoField({
  name,
  legend,
  hint,
  value,
  onChange,
}: YesNoFieldProps) {
  const hintId = `${name}-hint`

  return (
    <fieldset className="choice-field">
      <legend>{legend}</legend>
      <p className="field-hint" id={hintId}>
        {hint}
      </p>

      <div
        className="choice-options"
        aria-describedby={hintId}
      >
        {(['yes', 'no'] as const).map((answer) => (
          <label className="choice-option" key={answer}>
            <input
              type="radio"
              name={name}
              value={answer}
              checked={value === answer}
              onChange={() => onChange(answer)}
              required
            />
            <span>
              {answer === 'yes' ? 'Yes' : 'No'}
            </span>
          </label>
        ))}
      </div>
    </fieldset>
  )
}

export default PaymentAssessmentForm