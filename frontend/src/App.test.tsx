import { cleanup, render, screen, within } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import { afterEach, describe, expect, it } from 'vitest'
import App from './App'

afterEach(() => {
  cleanup()
})

describe('MemberShield application', () => {
  it('presents the payment protection purpose and guidance', () => {
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

    expect(screen.getByText('Clear risk explanations')).toBeInTheDocument()
    expect(screen.getByText('Practical next steps')).toBeInTheDocument()
    expect(screen.getByText('No hidden scoring')).toBeInTheDocument()
  })

  it('allows payment details to be reviewed and edited', async () => {
    const user = userEvent.setup()

    render(<App />)

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
})