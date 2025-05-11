package keski.mert.loan.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LoanQueryInstallmentResponse(
        BigDecimal amount,
        LocalDate dueDate,
        BigDecimal paidAmount,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        LocalDate paymentDate,
        boolean isPaid
) {
}
