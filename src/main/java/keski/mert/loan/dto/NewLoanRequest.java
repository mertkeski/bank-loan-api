package keski.mert.loan.dto;

import jakarta.validation.constraints.*;
import keski.mert.loan.validation.ValidNumberOfInstallments;

import java.math.BigDecimal;

public record NewLoanRequest(
        @NotNull(message = "customerId is required")
        Long customerId,

        @NotNull(message = "interestRate is required")
        @DecimalMin(value = "0.1", message = "Interest rate must be at least 0.1")
        @DecimalMax(value = "0.5", message = "Interest rate must be at most 0.5")
        BigDecimal interestRate,

        @NotNull(message = "loanAmount is required")
        @Positive(message = "Loan amount must be positive")
        BigDecimal loanAmount,

        @NotNull(message = "numberOfInstallments is required")
        @ValidNumberOfInstallments
        Integer numberOfInstallments
) {
}
