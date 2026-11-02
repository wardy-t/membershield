import {
  cleanup,
  render,
  screen,
  within,
} from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import {
  afterEach,
  beforeEach,
  describe,
  expect,
  it,
  vi,
} from 'vitest'
import App from './App'

const emptyHistoryResponse = {
  count: 0,
  assessments: [],
}

const criticalAssessmentResponse = {
  score: 80,
  riskLevel: 'CRITICAL',
  decision: 'PAUSE_AND_CONTACT_BANK',
  reasons: [
    {
      code: 'NEW_RECIPIENT',
      message: 'You have not paid this recipient before.',
      scoreImpact: 25,
    },
    {
      code: 'UNUSUAL_AMOUNT',
      message:
        'This amount is significantly higher than your recent payments.',
      scoreImpact: 25,
    },
    {
      code: 'LARGE_FIRST_PAYMENT',
      message:
        'This is a high-value first payment to this recipient.',
      scoreImpact: 15,
    },
    {
      code: 'REPEATED_ATTEMPTS',
      message:
        'Several payment attempts were made within a short period.',
      scoreImpact: 15,
    },
  ],
}

const historyItem = {
  id: '023acd65-1b3d-435d-b7a3-82df82655e17',
  recipientId: '821746c0-ae8c-42dc-9e1a-bc70fc785bcc',
  amount: 1500,
  currency: 'GBP',
  paymentReference: 'Invoice payment',
  requestedAt: '2026-10-27T17:15:45.851Z',
  score: 80,
  riskLevel: 'CRITICAL',
  decision: 'PAUSE_AND_CONTACT_BANK',
  assessedAt: '2026-10-27T17:15:45.968801Z',
  reasons: criticalAssessmentResponse.reasons,
}

function jsonResponse(
  body: unknown,
  status = 200,
): Response {
  return new Response(JSON.stringify(body), {
    status,
    headers: {
      'Content-Type': 'application/json',
    },
  })
}

beforeEach(() => {
  vi.spyOn(globalThis, 'fetch').mockResolvedValue(
    jsonResponse(emptyHistoryResponse),
  )
})

afterEach(() => {
  cleanup()
  vi.restoreAllMocks()
})

async function completeHighRiskPaymentForm() {
  const user = userEvent.setup()

  await user.type(screen.getByLabelText('Payment amount'), '1500')

  await user.type(
    screen.getByLabelText('Payment reference'),
    'Invoice payment',
  )

  const previousRecipientGroup = screen.getByRole('group', {
    name: 'Have you paid this recipient before?',
  })

  await user.click(
    within(previousRecipientGroup).getByRole('radio', {
      name: 'No',
    }),
  )

  const trustedRecipientGroup = screen.getByRole('group', {
    name: 'Is this recipient saved as trusted?',
  })

  await user.click(
    within(trustedRecipientGroup).getByRole('radio', {
      name: 'No',
    }),
  )

  await user.type(
    screen.getByLabelText('Typical payment amount'),
    '400',
  )

  const recentAttemptsInput = screen.getByLabelText(
    'Recent payment attempts',
  )

  await user.clear(recentAttemptsInput)
  await user.type(recentAttemptsInput, '3')

  await user.click(
    screen.getByRole('button', {
      name: 'Continue to review',
    }),
  )

  return user
}

describe('MemberShield application', () => {
  it('presents the payment protection purpose and empty history', async () => {
    render(<App />)

    expect(
      screen.getByRole('heading', {
        level: 1,
        name: 'Pause. Check. Pay with confidence.',
      }),
    ).toBeInTheDocument()

    expect(
      screen.getByRole('heading', {
        level: 2,
        name: 'Assess a proposed payment',
      }),
    ).toBeInTheDocument()

    expect(
      screen.getByText('Clear risk explanations'),
    ).toBeInTheDocument()

    expect(
      screen.getByText('Practical next steps'),
    ).toBeInTheDocument()

    expect(
      screen.getByText('No hidden scoring'),
    ).toBeInTheDocument()

    expect(
      await screen.findByRole('heading', {
        name: 'No assessments recorded yet',
      }),
    ).toBeInTheDocument()
  })

  it('displays persisted assessment history and reasons', async () => {
    vi.mocked(fetch).mockResolvedValueOnce(
      jsonResponse({
        count: 1,
        assessments: [historyItem],
      }),
    )

    render(<App />)

    expect(
      await screen.findByText('Invoice payment'),
    ).toBeInTheDocument()

    expect(screen.getByText('£1,500.00')).toBeInTheDocument()
    expect(screen.getByText('critical risk')).toBeInTheDocument()
    expect(screen.getByText('80/100')).toBeInTheDocument()

    const user = userEvent.setup()

    await user.click(screen.getByText('Invoice payment'))

    expect(
      screen.getByText('Pause and contact the bank'),
    ).toBeInTheDocument()

    expect(
      screen.getByText(
        'You have not paid this recipient before.',
      ),
    ).toBeInTheDocument()

    expect(
      screen.getByText(
        'Several payment attempts were made within a short period.',
      ),
    ).toBeInTheDocument()
  })

  it('allows payment details to be reviewed and edited', async () => {
    render(<App />)

    await screen.findByRole('heading', {
      name: 'No assessments recorded yet',
    })

    const user = await completeHighRiskPaymentForm()

    expect(
      screen.getByRole('heading', {
        level: 2,
        name: 'Review your payment details',
      }),
    ).toBeInTheDocument()

    expect(screen.getByText('£1,500.00')).toBeInTheDocument()
    expect(screen.getByText('Invoice payment')).toBeInTheDocument()
    expect(screen.getByText('£400.00')).toBeInTheDocument()

    const recentAttemptsTerm = screen.getByText(
      'Recent payment attempts',
      {
        selector: 'dt',
      },
    )

    const recentAttemptsRow = recentAttemptsTerm.parentElement

    expect(recentAttemptsRow).not.toBeNull()

    expect(
      within(recentAttemptsRow!).getByRole('definition'),
    ).toHaveTextContent('3')

    await user.click(
      screen.getByRole('button', {
        name: 'Edit payment details',
      }),
    )

    expect(
      screen.getByRole('heading', {
        level: 2,
        name: 'Assess a proposed payment',
      }),
    ).toBeInTheDocument()
  })

  it('displays an assessment and refreshes history', async () => {
    const fetchMock = vi.mocked(fetch)

    fetchMock
      .mockResolvedValueOnce(
        jsonResponse(emptyHistoryResponse),
      )
      .mockResolvedValueOnce(
        jsonResponse(criticalAssessmentResponse),
      )
      .mockResolvedValueOnce(
        jsonResponse({
          count: 1,
          assessments: [
            {
              ...historyItem,
              reasons: [],
            },
          ],
        }),
      )

    render(<App />)

    await screen.findByRole('heading', {
      name: 'No assessments recorded yet',
    })

    const user = await completeHighRiskPaymentForm()

    await user.click(
      screen.getByRole('button', {
        name: 'Assess payment',
      }),
    )

    expect(
      await screen.findByRole('heading', {
        level: 2,
        name: 'Your payment assessment',
      }),
    ).toBeInTheDocument()

    expect(screen.getByText('Critical risk')).toBeInTheDocument()
    expect(screen.getByText('80')).toBeInTheDocument()

    expect(
      screen.getByRole('heading', {
        name: 'Pause this payment',
      }),
    ).toBeInTheDocument()

    expect(
      screen.getByText(
        'You have not paid this recipient before.',
      ),
    ).toBeInTheDocument()

    expect(
      await screen.findByText('Invoice payment'),
    ).toBeInTheDocument()

    expect(fetchMock).toHaveBeenCalledWith(
      '/api/v1/risk-assessments',
      expect.objectContaining({
        method: 'POST',
      }),
    )

    expect(fetchMock).toHaveBeenCalledTimes(3)
  })

  it('shows a helpful message when the assessment fails', async () => {
    const fetchMock = vi.mocked(fetch)

    fetchMock
      .mockResolvedValueOnce(
        jsonResponse(emptyHistoryResponse),
      )
      .mockResolvedValueOnce(
        new Response(null, {
          status: 500,
        }),
      )

    render(<App />)

    await screen.findByRole('heading', {
      name: 'No assessments recorded yet',
    })

    const user = await completeHighRiskPaymentForm()

    await user.click(
      screen.getByRole('button', {
        name: 'Assess payment',
      }),
    )

    expect(
      await screen.findByRole('alert'),
    ).toHaveTextContent(
      'MemberShield could not assess this payment. Please try again.',
    )

    expect(
      screen.getByRole('button', {
        name: 'Assess payment',
      }),
    ).toBeEnabled()

    expect(fetchMock).toHaveBeenCalledTimes(2)
  })
})