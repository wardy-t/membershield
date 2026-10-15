CREATE TABLE risk_assessments (
    id UUID PRIMARY KEY,
    customer_id UUID NOT NULL,
    recipient_id UUID NOT NULL,
    amount DECIMAL(19, 2) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    payment_reference VARCHAR(140) NOT NULL,
    requested_at TIMESTAMP WITH TIME ZONE NOT NULL,
    recipient_previously_paid BOOLEAN NOT NULL,
    recipient_trusted BOOLEAN NOT NULL,
    customer_average_payment DECIMAL(19, 2) NOT NULL,
    recent_attempt_count INTEGER NOT NULL,
    customer_time_zone VARCHAR(50) NOT NULL,
    score INTEGER NOT NULL,
    risk_level VARCHAR(20) NOT NULL,
    decision VARCHAR(50) NOT NULL,
    assessed_at TIMESTAMP WITH TIME ZONE NOT NULL,

    CONSTRAINT chk_payment_amount_positive
        CHECK (amount > 0),

    CONSTRAINT chk_customer_average_non_negative
        CHECK (customer_average_payment >= 0),

    CONSTRAINT chk_recent_attempt_count_non_negative
        CHECK (recent_attempt_count >= 0),

    CONSTRAINT chk_risk_score_range
        CHECK (score BETWEEN 0 AND 100)
);

CREATE TABLE risk_assessment_reasons (
    assessment_id UUID NOT NULL,
    reason_order INTEGER NOT NULL,
    code VARCHAR(50) NOT NULL,
    message VARCHAR(255) NOT NULL,
    score_impact INTEGER NOT NULL,

    PRIMARY KEY (assessment_id, reason_order),

    CONSTRAINT fk_reason_assessment
        FOREIGN KEY (assessment_id)
        REFERENCES risk_assessments (id)
        ON DELETE CASCADE,

    CONSTRAINT chk_reason_order_non_negative
        CHECK (reason_order >= 0),

    CONSTRAINT chk_score_impact_positive
        CHECK (score_impact > 0)
);

CREATE INDEX idx_risk_assessments_customer
    ON risk_assessments (customer_id);

CREATE INDEX idx_risk_assessments_requested_at
    ON risk_assessments (requested_at);

CREATE INDEX idx_risk_assessments_risk_level
    ON risk_assessments (risk_level);