package keski.mert.loan.dto;

import java.math.BigDecimal;

public record PaymentResponse(
        int numberOfInstallmentsPaid,
        BigDecimal totalAmountPaid,
        boolean isLoanPaidCompletely
) {
}
