package keski.mert.loan.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record NewLoanInstallmentResponse(
        BigDecimal amount,
        LocalDate dueDate
) {
}
