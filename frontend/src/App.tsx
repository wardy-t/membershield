import './App.css'

function App() {
  return (
    <div className="app-shell">
      <header className="app-header">
        <a className="brand" href="/" aria-label="MemberShield home">
          <span className="brand-mark" aria-hidden="true">
            M
          </span>

          <span>
            <span className="brand-name">MemberShield</span>
            <span className="brand-description">
              Payment protection
            </span>
          </span>
        </a>

        <div className="environment-badge">
          <span className="environment-dot" aria-hidden="true" />
          Demo environment
        </div>
      </header>

      <main>
        <section
          className="hero-section"
          aria-labelledby="page-title"
        >
          <p className="eyebrow">Explainable payment protection</p>

          <h1 id="page-title">
            Pause. Check. Pay with confidence.
          </h1>

          <p className="hero-description">
            MemberShield reviews the circumstances around a
            payment and highlights potential warning signs before
            money leaves your account.
          </p>

          <ul className="trust-list" aria-label="MemberShield features">
            <li>Clear risk explanations</li>
            <li>Practical next steps</li>
            <li>No hidden scoring</li>
          </ul>
        </section>

        <section
          className="workspace"
          aria-labelledby="assessment-title"
        >
          <article className="assessment-card">
            <div className="card-heading">
              <div>
                <p className="section-label">Payment check</p>
                <h2 id="assessment-title">
                  Assess a proposed payment
                </h2>
              </div>

              <span className="step-badge">Step 1 of 2</span>
            </div>

            <p className="card-description">
              Provide the payment details and relevant account
              activity. MemberShield will return an explainable
              assessment rather than an unexplained pass or fail.
            </p>

            <div className="assessment-preview">
              <div className="preview-icon" aria-hidden="true">
                ✓
              </div>

              <div>
                <h3>What we will review</h3>
                <p>
                  The recipient, payment amount and recent activity
                  associated with the transaction.
                </p>
              </div>
            </div>
          </article>

          <aside
            className="guidance-card"
            aria-labelledby="guidance-title"
          >
            <p className="section-label">Designed around people</p>
            <h2 id="guidance-title">
              A warning should help—not frighten.
            </h2>

            <p>
              Every result explains which signals were identified
              and what the customer can do next.
            </p>

            <ol className="guidance-list">
              <li>
                <span>1</span>
                Review the payment circumstances
              </li>
              <li>
                <span>2</span>
                Understand each contributing risk signal
              </li>
              <li>
                <span>3</span>
                Receive a clear recommended action
              </li>
            </ol>
          </aside>
        </section>
      </main>

      <footer className="app-footer">
        <p>
          MemberShield is a demonstration application and does not
          provide financial advice.
        </p>
      </footer>
    </div>
  )
}

export default App