import { render, screen } from '@testing-library/react'
import { describe, expect, it } from 'vitest'
import App from './App'

describe('MemberShield application shell', () => {
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

    expect(
      screen.getByText('Clear risk explanations'),
    ).toBeInTheDocument()

    expect(
      screen.getByText('Practical next steps'),
    ).toBeInTheDocument()

    expect(
      screen.getByText('No hidden scoring'),
    ).toBeInTheDocument()
  })
})