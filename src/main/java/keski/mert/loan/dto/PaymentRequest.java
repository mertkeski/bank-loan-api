package keski.mert.loan.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record PaymentRequest(
        @NotNull(message = "amount is required")
        @Positive(message = "Amount must be positive")
        BigDecimal amount
) {
}
